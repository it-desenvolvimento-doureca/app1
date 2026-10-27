package pt.example.bootstrap;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class GerarFicheiroService {

	private static final Logger LOGGER = Logger.getLogger(GerarFicheiroService.class.getName());

	private static final long ESPERA_INICIAL_MS = 2000;
	private static final long ESPERA_ENTRE_TENTATIVAS_MS = 2000;
	private static final int TENTATIVAS = 3;

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
