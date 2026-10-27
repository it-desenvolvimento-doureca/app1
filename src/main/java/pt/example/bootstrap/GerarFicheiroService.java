package pt.example.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pt.example.rest.SIIP;

// Gera os ficheiros de interface de uma OF em segundo plano.
//
// O tablet so espera que o pedido seja aceite e sai logo da pagina. Antes, o tablet esperava
// 1,5s antes de pedir o ficheiro e, se entretanto a pagina recarregasse (ex.: nova versao da
// app detetada no login), o pedido nunca saia e o ficheiro nao era gerado, sem deixar log.
//
// Antes de gerar espera um pouco e confirma que todos os operarios da OF ja tem data de fim:
// ha gravacoes do tablet que nao sao esperadas (ex.: RP_OF_PREP_LIN) e, com varios operarios
// ou componentes, cada OP_CAB fecha em paralelo. As esperas sao feitas fora de transacao; a
// geracao corre numa transacao propria (SIIP.gerarFicheiro).
//
// Rede de seguranca: quando o trabalho fica concluido (SIIP.updateRP_OF_CAB), e agendada uma
// verificacao. Se ate la o tablet nao tiver pedido o ficheiro (refresh ou browser fechado a meio,
// rede, quem terminou nao e o lider...), o servidor gera-o. Os pedidos recebidos ficam so em
// memoria: se o servidor reiniciar entre a conclusao e a verificacao, essa verificacao perde-se.
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class GerarFicheiroService {

	private static final Logger LOGGER = Logger.getLogger(GerarFicheiroService.class.getName());

	private static final long ESPERA_INICIAL_MS = 2000;
	private static final long ESPERA_ENTRE_TENTATIVAS_MS = 2000;
	private static final int TENTATIVAS = 3;

	// Tempo entre a conclusao e a verificacao: o tablet pede o ficheiro logo a seguir a gravar
	private static final long ESPERA_VERIFICACAO_MS = 60 * 1000L;
	// Um pedido conta para a verificacao se chegou ate 5 min antes da conclusao (no fim de
	// trabalho pelo login, o pedido e a gravacao da OF saem ao mesmo tempo)
	private static final long JANELA_PEDIDO_MS = ESPERA_VERIFICACAO_MS + 5 * 60 * 1000L;
	private static final long VALIDADE_PEDIDO_MS = 60 * 60 * 1000L;

	// ID_OF_CAB -> hora do ultimo pedido de ficheiro (conclusao) recebido do tablet
	private static final ConcurrentHashMap<Integer, Long> PEDIDOS_RECEBIDOS = new ConcurrentHashMap<>();

	// Registos de operarios da OF (e dos componentes) ainda sem data de fim
	private static final String SQL_OPERARIOS_ABERTOS = "select count(*) from RP_OF_OP_FUNC f "
			+ "inner join RP_OF_OP_CAB oc on oc.ID_OP_CAB = f.ID_OP_CAB "
			+ "inner join RP_OF_CAB c on c.ID_OF_CAB = oc.ID_OF_CAB "
			+ "where (c.ID_OF_CAB = :id or c.ID_OF_CAB_ORIGEM = :id) "
			+ "and (f.DATA_FIM_M2 is null or f.HORA_FIM_M2 is null)";

	@PersistenceContext(unitName = "persistenceUnit")
	private EntityManager entityManager;

	// @EJB (e nao @Inject) porque o SIIP tambem injeta este servico
	@EJB
	private SIIP siip;

	@Resource
	private TimerService timerService;

	public void registarPedido(Integer id) {
		long agora = System.currentTimeMillis();
		PEDIDOS_RECEBIDOS.values().removeIf(hora -> agora - hora > VALIDADE_PEDIDO_MS);
		PEDIDOS_RECEBIDOS.put(id, agora);
	}

	public void agendarVerificacao(Integer id) {
		timerService.createSingleActionTimer(ESPERA_VERIFICACAO_MS, new TimerConfig(id, false));
	}

	@Timeout
	public void verificarPedido(Timer timer) {
		Integer id = (Integer) timer.getInfo();
		Long pedido = PEDIDOS_RECEBIDOS.get(id);
		if (pedido != null && System.currentTimeMillis() - pedido <= JANELA_PEDIDO_MS) {
			return;
		}

		List<?> dados = entityManager
				.createNativeQuery("select ESTADO, ISNULL(IP_POSTO_FINAL, IP_POSTO) from RP_OF_CAB where ID_OF_CAB = :id")
				.setParameter("id", id).getResultList();
		if (dados.isEmpty()) {
			return;
		}
		Object[] of = (Object[]) dados.get(0);
		// entretanto anulada ou em edicao: a geracao desses estados e feita pelo ecra
		if (of[0] == null || !"C".equals(of[0].toString().trim())) {
			return;
		}

		LOGGER.warning("[FICHEIRO] OF " + id + ": concluida sem pedido de ficheiro do tablet, gerado pelo servidor");
		HashMap<String, String> posto = new HashMap<>();
		posto.put("ip_posto", (of[1] != null) ? of[1].toString().trim() : null);
		List<HashMap<String, String>> data2 = new ArrayList<>();
		data2.add(posto);
		gerar(id, "C", false, data2);
	}

	@Asynchronous
	public void gerar(Integer id, String estado, Boolean manual, List<HashMap<String, String>> data2) {
		long inicio = System.currentTimeMillis();
		// WARNING para ficar no server.log: o nivel configurado no servidor nao mostra INFO
		LOGGER.warning("[FICHEIRO] OF " + id + " estado " + estado + ": pedido recebido");

		pausa(ESPERA_INICIAL_MS);
		if ("C".equals(estado)) {
			esperarOperariosFechados(id);
		}

		try {
			siip.gerarFicheiro(id, estado, false, manual, data2);
			LOGGER.warning("[FICHEIRO] OF " + id + " estado " + estado + ": gerado em "
					+ (System.currentTimeMillis() - inicio) + " ms");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "[FICHEIRO] OF " + id + " estado " + estado + ": falhou: " + e.getMessage(), e);
		}
	}

	// Gera na mesma se continuarem registos abertos, para nao perder o ficheiro; fica o aviso no log
	private void esperarOperariosFechados(Integer id) {
		int abertos = 0;
		for (int tentativa = 1; tentativa <= TENTATIVAS; tentativa++) {
			try {
				abertos = ((Number) entityManager.createNativeQuery(SQL_OPERARIOS_ABERTOS).setParameter("id", id)
						.getSingleResult()).intValue();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "[FICHEIRO] OF " + id + ": verificacao de operarios falhou: " + e.getMessage(), e);
				return;
			}
			if (abertos == 0) {
				return;
			}
			if (tentativa < TENTATIVAS) {
				pausa(ESPERA_ENTRE_TENTATIVAS_MS);
			}
		}
		LOGGER.warning("[FICHEIRO] OF " + id + ": " + abertos
				+ " registo(s) de operario ainda sem data de fim, ficheiro gerado assim mesmo");
	}

	private static void pausa(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
