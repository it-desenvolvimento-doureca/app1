package pt.example.bootstrap;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

// Recalcula as boas e os defeitos das linhas COMP de uma OF a partir das etiquetas ativas e de
// RP_OF_DEF_LIN. Chamado quando a OF fica concluida: o ecra so recalcula no CONCLUIR/SAIR
// (atualizartotais), e o total de defeitos da linha podia ficar so com os de uma etiqueta.
//
// Corre em segundo plano e numa transacao propria:
// - se falhar (erro ou deadlock), a conclusao da OF nao e desfeita;
// - nao fica a espera de quem o chamou: se a transacao que concluiu a OF ainda tiver linhas
//   bloqueadas, este espera que ela termine (sincrono com REQUIRES_NEW, ficavam as duas
//   a esperar uma pela outra).
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class RecalcularTotaisService {

	private static final Logger LOGGER = Logger.getLogger(RecalcularTotaisService.class.getName());

	private static final int TENTATIVAS = 3;
	private static final int SQL_DEADLOCK = 1205;

	// _M2 e sempre recalculado; o original e o _M1 so em linhas que nunca passaram pelo modo
	// edicao (VERSAO_MODIF vazio), onde as tres versoes sao iguais. OFs de recuperacao de pecas
	// ficam de fora: la a quantidade da etiqueta comeca toda como defeito.
	// DEADLOCK_PRIORITY LOW: num deadlock com um operario a gravar, a vitima e este update (que
	// volta a tentar) e nao a gravacao do operario. Reposto em NORMAL tambem em erro, porque a
	// ligacao volta ao pool.
	// RP_OF_CAB/RP_OF_OP_CAB com NOLOCK: so servem para encontrar as linhas, e a OF pode ainda
	// estar bloqueada pela transacao que a concluiu.
	private static final String SQL_RECALCULAR = "SET DEADLOCK_PRIORITY LOW; "
			+ "BEGIN TRY "
			+ " UPDATE l SET "
			+ "  QUANT_BOAS_TOTAL_M2 = t.boas, QUANT_DEF_TOTAL_M2 = t.def,"
			+ "  QUANT_BOAS_TOTAL = CASE WHEN ISNULL(l.VERSAO_MODIF,0) = 0 THEN t.boas ELSE l.QUANT_BOAS_TOTAL END,"
			+ "  QUANT_BOAS_TOTAL_M1 = CASE WHEN ISNULL(l.VERSAO_MODIF,0) = 0 THEN t.boas ELSE l.QUANT_BOAS_TOTAL_M1 END,"
			+ "  QUANT_DEF_TOTAL = CASE WHEN ISNULL(l.VERSAO_MODIF,0) = 0 THEN t.def ELSE l.QUANT_DEF_TOTAL END,"
			+ "  QUANT_DEF_TOTAL_M1 = CASE WHEN ISNULL(l.VERSAO_MODIF,0) = 0 THEN t.def ELSE l.QUANT_DEF_TOTAL_M1 END"
			+ " FROM RP_OF_OP_LIN l"
			+ " INNER JOIN RP_OF_OP_CAB oc WITH (NOLOCK) ON oc.ID_OP_CAB = l.ID_OP_CAB"
			+ " INNER JOIN RP_OF_CAB c WITH (NOLOCK) ON c.ID_OF_CAB = oc.ID_OF_CAB"
			+ " INNER JOIN RP_OF_CAB o WITH (NOLOCK) ON o.ID_OF_CAB = c.ID_OF_CAB_ORIGEM"
			+ " CROSS APPLY (SELECT"
			+ "   ISNULL((SELECT SUM(x.QUANT_BOAS_M2) FROM RP_OF_OP_ETIQUETA x"
			+ "     WHERE x.ID_OP_LIN = l.ID_OP_LIN AND x.ATIVO = 1),0) AS boas,"
			+ "   ISNULL((SELECT SUM(dd.QUANT_DEF_M2) FROM RP_OF_DEF_LIN dd INNER JOIN RP_OF_OP_ETIQUETA ee"
			+ "     ON ee.ID_REF_ETIQUETA = dd.ID_REF_ETIQUETA AND ee.ID_OP_LIN = dd.ID_OP_LIN AND ee.ATIVO = 1"
			+ "     WHERE dd.ID_OP_LIN = l.ID_OP_LIN),0) AS def) t"
			+ " WHERE o.ID_OF_CAB = (SELECT ISNULL(ID_OF_CAB_ORIGEM, ID_OF_CAB) FROM RP_OF_CAB WITH (NOLOCK) WHERE ID_OF_CAB = :id)"
			+ " AND NOT EXISTS (SELECT 1 FROM RP_CONF_OP_RECUPERACAO_PECAS r WHERE r.ID_OP = o.OP_COD_ORIGEM)"
			+ " AND (ISNULL(l.QUANT_DEF_TOTAL_M2,0) <> t.def OR ISNULL(l.QUANT_BOAS_TOTAL_M2,0) <> t.boas);"
			+ " SET DEADLOCK_PRIORITY NORMAL; "
			+ "END TRY "
			+ "BEGIN CATCH "
			+ " SET DEADLOCK_PRIORITY NORMAL; "
			+ " THROW; "
			+ "END CATCH";

	@PersistenceContext(unitName = "persistenceUnit")
	private EntityManager entityManager;

	@Resource
	private UserTransaction utx;

	@Asynchronous
	public void recalcular(Integer id_of_cab) {
		if (id_of_cab == null) {
			return;
		}
		for (int tentativa = 1; tentativa <= TENTATIVAS; tentativa++) {
			try {
				utx.begin();
				entityManager.createNativeQuery(SQL_RECALCULAR).setParameter("id", id_of_cab).executeUpdate();
				utx.commit();
				return;
			} catch (Exception e) {
				rollback();
				if (isDeadlock(e) && tentativa < TENTATIVAS) {
					LOGGER.log(Level.INFO, "recalcular totais OF " + id_of_cab + ": deadlock, nova tentativa ("
							+ (tentativa + 1) + "/" + TENTATIVAS + ")");
					pausa(1000L * tentativa);
					continue;
				}
				LOGGER.log(Level.WARNING, "recalcular totais OF " + id_of_cab + " falhou: " + e.getMessage(), e);
				return;
			}
		}
	}

	private void rollback() {
		try {
			int status = utx.getStatus();
			if (status != Status.STATUS_NO_TRANSACTION) {
				utx.rollback();
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "recalcular totais: rollback falhou: " + e.getMessage(), e);
		}
	}

	private static boolean isDeadlock(Throwable e) {
		for (Throwable t = e; t != null; t = t.getCause()) {
			if (t instanceof SQLException && ((SQLException) t).getErrorCode() == SQL_DEADLOCK) {
				return true;
			}
		}
		return false;
	}

	private static void pausa(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
