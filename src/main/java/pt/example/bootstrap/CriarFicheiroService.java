package pt.example.bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.example.entity.EMAIL;
import pt.example.entity.GER_EVENTOS_CONF;

/**
 * Gera ficheiros Silver-CS de registo de producao.
 *
 * Tipos de registo:
 *   A  Suivi ressource          pos 1-167
 *   B  Arrets / pausas          pos 1-194
 *   Q  Quantites bonnes         pos 1-334
 *   R  Rebuts / defeitos        pos 1-338
 *   C  Consommation             pos 1-395
 *
 * Cabecalho comum (pos 1-87):
 *   [1-10]  Societe
 *   [11-18] Date suivi  (yyyyMMdd)
 *   [19-27] No sequence (9 chars)
 *   [28-31] Ligne de production (4 chars)
 *   [32]    Type No OF
 *   [33-42] No OF
 *   [43]    Type operation
 *   [44-47] No Operation
 *   [48]    Position S12
 *   [49-58] Code section
 *   [59-68] Code sous-section
 *   [69-70] No equipe
 *   [71-74] Type ressource
 *   [75-84] Code ressource
 *   [85-87] No etablissement
 */
@Stateless
public class CriarFicheiroService {

	private static final Logger LOG = Logger.getLogger(CriarFicheiroService.class.getName());

	@PersistenceContext(unitName = "persistenceUnit")
	protected EntityManager entityManager;

	// ── Silver-CS: constantes de campo ───────────────────────────────────────
	private static final String SOCIEDADE  = "01        ";
	private static final String CRLF       = "\r\n";
	private static final String ZEROS_15   = "000000000000000";
	private static final String ZEROS_9    = "000000000";
	private static final String ESPACOS_10 = "          ";
	private static final String ESPACOS_4  = "    ";

	// ── Indices da query principal (Registo A) ────────────────────────────────
	private static final int QA_OF_NUM       = 0;
	private static final int QA_UTZ_CRIA     = 1;
	private static final int QA_SEC_NUM      = 3;
	private static final int QA_MAQ_NUM_ORIG = 4;
	private static final int QA_DATA_INI     = 5;
	private static final int QA_HORA_INI     = 6;
	private static final int QA_DATA_FIM     = 7;
	private static final int QA_HORA_FIM     = 8;
	private static final int QA_TEMPO_PREP   = 9;
	private static final int QA_TEMPO_EXEC   = 10;
	private static final int QA_OP_PREVISTA  = 11;
	private static final int QA_OP_COD_ORIG  = 12;
	private static final int QA_TURNO        = 13;
	private static final int QA_ALTERADO     = 14;
	private static final int QA_REF_NUM      = 15;

	// ── Indices da query Q/R (Quantidades/Defeitos) ───────────────────────────
	private static final int QQ_ID_CAB_ORIG   = 0;
	private static final int QQ_OF_NUM        = 1;
	private static final int QQ_OF_NUM_ORIG   = 2;
	private static final int QQ_REF_NUM       = 4;
	private static final int QQ_REF_VAR1      = 5;
	private static final int QQ_REF_VAR2      = 6;
	private static final int QQ_REF_INDNUMENR = 7;
	private static final int QQ_MAQ_NUM_ORIG  = 8;
	private static final int QQ_SEC_NUM       = 9;
	private static final int QQ_DATA_INI      = 10;
	private static final int QQ_HORA_INI      = 11;
	private static final int QQ_DATA_FIM      = 12;
	private static final int QQ_HORA_FIM      = 13;
	private static final int QQ_UTZ_CRIA      = 14;
	private static final int QQ_REF_IND       = 15;
	private static final int QQ_QUANT_TOTAL   = 16;
	private static final int QQ_QUANT         = 17;
	private static final int QQ_OP_PREVISTA   = 18;
	private static final int QQ_TURNO         = 20;
	private static final int QQ_OP_COD_ORIG   = 21;
	private static final int QQ_ALTERADO      = 22;

	private static final int QR_COD_DEF       = 0;
	private static final int QR_QUANT_DEF     = 1;
	private static final int QR_ID_CAB_ORIG   = 2;
	private static final int QR_OF_NUM        = 3;
	private static final int QR_OF_NUM_ORIG   = 4;
	private static final int QR_REF_NUM       = 6;
	private static final int QR_REF_VAR1      = 7;
	private static final int QR_REF_VAR2      = 8;
	private static final int QR_REF_INDNUMENR = 9;
	private static final int QR_MAQ_NUM_ORIG  = 10;
	private static final int QR_SEC_NUM       = 11;
	private static final int QR_DATA_INI      = 12;
	private static final int QR_HORA_INI      = 13;
	private static final int QR_DATA_FIM      = 14;
	private static final int QR_HORA_FIM      = 15;
	private static final int QR_UTZ_CRIA      = 16;
	private static final int QR_REF_IND       = 17;
	private static final int QR_QUANT_TOTAL   = 18;
	private static final int QR_QUANT         = 19;
	private static final int QR_OBS_DEF       = 20;
	private static final int QR_OP_PREVISTA   = 21;
	private static final int QR_TURNO         = 22;
	private static final int QR_OP_COD_ORIG   = 23;

	// ─────────────────────────────────────────────────────────────────────────
	// MaquinaConfig: encapsula nomes de coluna M1 / M2 / Manual
	// ─────────────────────────────────────────────────────────────────────────
	static final class MaquinaConfig {
		final String DATA_INI, HORA_INI, DATA_FIM, HORA_FIM;
		final String QUANT_BOAS_TOTAL, QUANT_BOAS, QUANT_DEF;
		final String TEMPO_PREP_TOTAL, TEMPO_EXEC_TOTAL;
		final String TIPO_PARAGEM, MOMENTO_PARAGEM;
		final String SINAL;

		private MaquinaConfig(String sufixo, String sinal) {
			String s = sufixo.isEmpty() ? "" : "_" + sufixo;
			DATA_INI         = "DATA_INI"         + s;
			HORA_INI         = "HORA_INI"         + s;
			DATA_FIM         = "DATA_FIM"         + s;
			HORA_FIM         = "HORA_FIM"         + s;
			QUANT_BOAS_TOTAL = "QUANT_BOAS_TOTAL" + s;
			QUANT_BOAS       = "QUANT_BOAS"       + s;
			QUANT_DEF        = "QUANT_DEF"        + s;
			TEMPO_PREP_TOTAL = "TEMPO_PREP_TOTAL" + s;
			TEMPO_EXEC_TOTAL = "TEMPO_EXEC_TOTAL" + s;
			TIPO_PARAGEM     = "TIPO_PARAGEM"     + s;
			MOMENTO_PARAGEM  = "MOMENTO_PARAGEM"  + s;
			SINAL            = sinal;
		}

		static MaquinaConfig para(int ficheiro, boolean manual) {
			if (ficheiro == 1) return new MaquinaConfig("M1", "-");
			if (manual)        return new MaquinaConfig("",   "+");
			return             new MaquinaConfig("M2",        "+");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Helpers de formatacao Silver-CS
	// ─────────────────────────────────────────────────────────────────────────

	/** Pad a direita com espacos ate ao comprimento indicado. Null-safe. */
	static String padRight(Object value, int length) {
		String s = value == null ? "" : value.toString();
		if (s.length() >= length) return s.substring(0, length);
		StringBuilder sb = new StringBuilder(length);
		sb.append(s);
		for (int i = s.length(); i < length; i++) sb.append(' ');
		return sb.toString();
	}

	/** Pad a esquerda com zeros. Ex: "42" + length=9 -> "000000042". Null-safe. */
	static String padZeroLeft(Object value, int length) {
		String s = value == null ? "" : value.toString();
		String z  = ZEROS_15.substring(0, Math.min(length, 15));
		String combined = z + s;
		return combined.substring(combined.length() - length);
	}

	/** Remove hifenes da data -> yyyyMMdd. Null -> 8 espacos. */
	static String formatDate(Object value) {
		return value == null ? "        " : value.toString().replaceAll("-", "");
	}

	/** Remove ":" e trunca a 6 chars -> HHmmss. Null -> 6 espacos. */
	static String formatTime(Object value) {
		if (value == null) return "      ";
		String s = value.toString().replace(":", "");
		return s.length() >= 6 ? s.substring(0, 6) : padRight(s, 6);
	}

	/** No Operacao Silver-CS (4 chars, zero-padded). Retorna 4 espacos se nulo/NULL/vazio. */
	static String formatOpNum(String opNum) {
		if (opNum == null || opNum.isEmpty() || "NULL".equals(opNum)) return ESPACOS_4;
		String s = "0000" + opNum;
		return s.substring(s.length() - 4);
	}

	/**
	 * Regra de escrita do OP_NUM no ficheiro:
	 * escreve sempre que OP_NUM exista (o numero da operacao imprevista
	 * e conhecido desde a criacao: 9000+OP_COD_ORIGEM).
	 */
	static String formatOpNumFicheiro(String opNum, String estado, String opPrevista) {
		/*if ("C".equals(estado)) {
			return "1".equals(opPrevista) ? formatOpNum(opNum) : ESPACOS_4;
		}*/
		return formatOpNum(opNum);
	}

	/** Converte tempo "HH:mm:ss" em decimal Silver-CS 15 chars. Zeros se invalido. */
	static String formatTempo(Object value, int total) {
		if (value == null) return ZEROS_15;
		String[] p = value.toString().split(":");
		if (p.length < 3 || "aN".equals(p[0])) return ZEROS_15;
		try {
			double h = Double.parseDouble(p[0])
					 + Double.parseDouble(p[1]) / 60.0
					 + Double.parseDouble(p[2]) / 3600.0;
			h = Math.max(0.0, h / total);
			// remove separadores decimais (virgula em PT, ponto em EN, $ em alguns locales)
			String f = String.format("%.4f", h).replace(",", "").replace(".", "").replace("$", "");
			return padZeroLeft(f, 15);
		} catch (NumberFormatException e) {
			return ZEROS_15;
		}
	}

	/** Converte decimal ("NN.DDD") em campo numerico Silver-CS sem ponto, zero-padded. */
	static String formatQuantidade(Object value, int length) {
		if (value == null) return ZEROS_15.substring(0, length);
		return padZeroLeft(value.toString().replace(".", ""), length);
	}

	/**
	 * Linha de producao Silver-CS (4 chars).
	 * Esconde (espacos) quando OP_PREVISTA=1, estado=M/A, ou estado2=A.
	 */
	static String ligneProduction(Object opCodOrig, boolean novaEtq,
			String opPrevista, String estado, String estado2) {
		if (novaEtq) return padRight(opCodOrig, 4);
		boolean esconder = "1".equals(opPrevista)
				|| "M".equals(estado) || "A".equals(estado) || "A".equals(estado2);
		return esconder ? ESPACOS_4 : padRight(opCodOrig, 4);
	}

	/**
	 * Constroi o cabecalho comum Silver-CS (87 chars) partilhado pelos registos A, Q e R.
	 *
	 * Posicoes (1-indexed):
	 *   [1-10]  Societe
	 *   [11-18] Date suivi
	 *   [19-27] No sequencia
	 *   [28-31] Ligne de production
	 *   [32]    Type No OF
	 *   [33-42] No OF
	 *   [43]    Type operation
	 *   [44-47] No Operation
	 *   [48]    Position S12
	 *   [49-58] Code section
	 *   [59-68] Code sous-section
	 *   [69-70] No equipe
	 *   [71-74] Type ressource
	 *   [75-84] Code ressource
	 *   [85-87] No etablissement
	 */
	static String buildCabecalho(
			String dataTracking, String sequencia,
			String linhaProd, String numOf,
			String tipoOp, String opNum4,
			String posicao, String seccao, String subseccao,
			Object equipa, String tipoRecurso, String codigoRecurso) {

		StringBuilder sb = new StringBuilder(87);
		sb.append(SOCIEDADE);
		sb.append(formatDate(dataTracking));
		sb.append(sequencia);
		sb.append(linhaProd);
		sb.append("1");
		sb.append(padRight(numOf, 10));
		sb.append(tipoOp);
		sb.append(opNum4);
		sb.append(posicao);
		sb.append(padRight(seccao, 10));
		sb.append(padRight(subseccao, 10));
		sb.append(equipa != null ? equipa.toString() : "01");
		sb.append(padRight(tipoRecurso, 4));
		sb.append(padRight(codigoRecurso, 10));
		// [85-87] No etablissement incluido pelos callers em "   A/Q/R/C"
		return sb.toString();
	}
	// ─────────────────────────────────────────────────────────────────────────
	// Helpers de acesso a BD
	// ─────────────────────────────────────────────────────────────────────────

	private boolean isPostoMatrix(Integer idOfCabOrigem) {
		@SuppressWarnings("unchecked")
		List<?> r = entityManager.createNativeQuery(
				"SELECT a.ID_OF_CAB FROM RP_OF_CAB a"
				+ " INNER JOIN DOC_DIC_POSTOS b ON a.IP_POSTO = b.IP_POSTO"
				+ " INNER JOIN PR_DIC_MAQUINAS_MATRIX c ON b.ID_MAQUINA = b.ID_MAQUINA"
				+ " WHERE a.ID_OF_CAB = :id AND b.TIPO_POSTO = 'ETIQUETAS_MATRIX'"
				+ " AND a.MAQ_NUM = c.MAQUINA_SILVER")
				.setParameter("id", idOfCabOrigem).getResultList();
		return !r.isEmpty();
	}

	private boolean isTrabalhoMuro(Integer idOfCabOrigem) {
		@SuppressWarnings("unchecked")
		List<Object> r = entityManager
				.createNativeQuery("SELECT ETIQUETA FROM RP_OF_CAB WHERE ID_OF_CAB = :id")
				.setParameter("id", idOfCabOrigem).getResultList();
		if (r.isEmpty() || r.get(0) == null) return false;
		return !r.get(0).toString().trim().isEmpty();
	}

	/** Retorna [path, path2, patherro, path_error]. */
	private String[] carregarCaminhos(String nomeFicheiro, String nomeFicheiro2) {
		@SuppressWarnings("unchecked")
		List<Object[]> rows = entityManager
				.createNativeQuery("SELECT TOP 1 * FROM GER_PARAMETROS").getResultList();
		if (rows.isEmpty()) return new String[]{"","","",""};
		Object[] p = rows.get(0);
		return new String[]{
			p[1] + nomeFicheiro, p[1] + nomeFicheiro2,
			p[17] + nomeFicheiro, p[17] + nomeFicheiro2
		};
	}

	/**
	 * Verifica no inicio de criarFicheiro se a combinacao OF_NUM+OP_COD_ORIGEM
	 * ja foi registada em RP_OF_OP_PREVISTA (ou seja, se a operacao prevista
	 * ja foi enviada ao Silver numa terminacao anterior).
	 * Retorna "1" se ja existe, ou o valor actual de OP_PREVISTA da BD se nao existe.
	 */
	private String resolverOpPrevistaParaFicheiro(Integer idOfCabOrigem, String ofNum) {
		@SuppressWarnings("unchecked")
		List<Object[]> rows = entityManager.createNativeQuery(
				"SELECT OP_PREVISTA, OP_COD_ORIGEM FROM RP_OF_CAB WHERE ID_OF_CAB = :id")
				.setParameter("id", idOfCabOrigem)
				.getResultList();
		if (rows.isEmpty()) return "1";
		Object[] row = rows.get(0);
		String opPrevista  = row[0] != null ? row[0].toString() : "1";
		String opCodOrigem = row[1] != null ? row[1].toString() : null;
		if (!"2".equals(opPrevista) || opCodOrigem == null) return opPrevista;
		// OP_PREVISTA=2: verificar se ja existe na tabela de controlo
		@SuppressWarnings("unchecked")
		List<?> existe = entityManager.createNativeQuery(
				"SELECT 1 FROM RP_OF_OP_PREVISTA WHERE OF_NUM = :of AND OP_COD = :op")
				.setParameter("of", ofNum)
				.setParameter("op", opCodOrigem)
				.getResultList();
		return existe.isEmpty() ? "2" : "1";
	}

	private List<Object[]> verificaPecasRecuperacaoInternal(Integer id) {
		@SuppressWarnings("unchecked")
		List<Object[]> dados = entityManager.createNativeQuery(
				"SELECT COUNT(*) total, NULL text FROM RP_OF_CAB a"
				+ " INNER JOIN RP_CONF_OP_RECUPERACAO_PECAS b ON a.OP_COD_ORIGEM = b.ID_OP"
				+ " WHERE a.ID_OF_CAB = :id")
				.setParameter("id", id).getResultList();
		return dados;
	}

	private String buscarNomeImpressora(String ipPosto) {
		if (ipPosto == null) return "";
		@SuppressWarnings("unchecked")
		List<Object> rows = entityManager.createNativeQuery(
				"SELECT TOP 1 NOME_IMPRESSORA_SILVER FROM GER_POSTOS WHERE IP_POSTO = :ip")
				.setParameter("ip", ipPosto).getResultList();
		return rows.isEmpty() || rows.get(0) == null ? "" : rows.get(0).toString();
	}

	private String buscarEtiquetasCaixas(Integer idOfCab, String refNum) {
		@SuppressWarnings("unchecked")
		List<Object> rows = entityManager.createNativeQuery(
				"SELECT ETQNUM FROM RP_CAIXAS_INCOMPLETAS WHERE ID_OF_CAB = :id AND REF_NUM = :ref")
				.setParameter("id", idOfCab).setParameter("ref", refNum).getResultList();
		StringBuilder sb = new StringBuilder();
		for (Object row : rows) sb.append(row).append(";");
		return sb.toString();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// criarFicheiro — metodo principal
	// ─────────────────────────────────────────────────────────────────────────

	public void criarFicheiro(Integer id, Integer ficheiro, String nome_ficheiro, String tipo, String of,
			Integer id_origem, Integer id_etiqueta, String estado, String nome_ficheiro2, String OP_NUM,
			String ID_OP_LIN, Boolean cria_pausa, Integer total, Boolean ficheirosdownload, String nomezip,
			String novaetiqueta, String estado2, Boolean manual, String ip_posto,
			String opPrevistaFicheiro) throws IOException, ParseException {

		if ("COMP".equals(tipo) && isPostoMatrix(id_origem)) return;

		// Se nao foi passado explicitamente, determinar da BD
		if (opPrevistaFicheiro == null) {
			opPrevistaFicheiro = resolverOpPrevistaParaFicheiro(id_origem, of);
		}

		boolean isMuro = isTrabalhoMuro(id_origem);
		if (novaetiqueta == null) novaetiqueta = "0";
		if ("A".equals(estado) || "A".equals(estado2)) nome_ficheiro2 = "anulacao_" + nome_ficheiro2;

		MaquinaConfig cfg = MaquinaConfig.para(ficheiro, manual);
		String sinal = ("A".equals(estado) || "A".equals(estado2)) ? "-" : cfg.SINAL;

		String[] caminhos = carregarCaminhos(nome_ficheiro, nome_ficheiro2);
		String path = caminhos[0], path2 = caminhos[1], patherro = caminhos[2], path_error = caminhos[3];
		String sequencia = "P".equals(estado) ? "000000000" : sequencia(id.toString());

		boolean existeMaquina = false, lider = true, atualiza = true, primeiraLinha = true;
		boolean houvAlteracoes = false;
		String conteudo = "", dadosMaquina = "", pausasMuro = "";
		Map<String, String> linhaUtz = new HashMap<>(), linhaUtzInicio = new HashMap<>();

		// ── Query Registo A ───────────────────────────────────────────────────
		@SuppressWarnings("unchecked")
		List<Object[]> registosA = entityManager.createNativeQuery(
				"SELECT a.OF_NUM, c.ID_UTZ_CRIA, a.OP_NUM, a.SEC_NUM, a.MAQ_NUM_ORIG,"
				+ " c." + cfg.DATA_INI + ", c." + cfg.HORA_INI + ", c." + cfg.DATA_FIM + ", c." + cfg.HORA_FIM
				+ ", b." + cfg.TEMPO_PREP_TOTAL + " AS prep, b." + cfg.TEMPO_EXEC_TOTAL + " AS exec_"
				+ ", a.OP_PREVISTA, a.OP_COD_ORIGEM"
				+ ", (SELECT ID_TURNO FROM RP_CONF_TURNO WHERE CAST(c." + cfg.HORA_INI + " AS time) BETWEEN HORA_INICIO AND HORA_FIM) AS turno"
				+ ", CASE WHEN (c.DATA_INI_M2 != c.DATA_INI_M1 OR c.HORA_INI_M1 != c.HORA_INI_M2"
				+ "   OR c.DATA_FIM_M2 != c.DATA_FIM_M1 OR c.HORA_FIM_M1 != c.HORA_FIM_M2"
				+ "   OR b.TEMPO_EXEC_TOTAL_M1 != b.TEMPO_EXEC_TOTAL_M2"
				+ "   OR b.TEMPO_PREP_TOTAL_M1 != b.TEMPO_PREP_TOTAL_M2) THEN 1 ELSE 1 END AS alterado"
				+ ", (SELECT REF_NUM FROM RP_OF_OP_LIN WHERE ID_OP_LIN = " + ID_OP_LIN + "), a.ID_OF_CAB"
				+ " FROM RP_OF_CAB a"
				+ " INNER JOIN RP_OF_OP_CAB b ON b.ID_OP_CAB IN"
				+ "   (SELECT x.ID_OP_CAB FROM RP_OF_OP_CAB x WHERE x.ID_OF_CAB = :ido)"
				+ " INNER JOIN RP_OF_OP_FUNC c ON c.ID_OP_CAB IN"
				+ "   (SELECT x.ID_OP_CAB FROM RP_OF_OP_CAB x WHERE x.ID_OF_CAB = :ido2)"
				+ "   AND b.ID_OP_CAB = c.ID_OP_CAB"
				+ " WHERE a.ID_OF_CAB = :id")
				.setParameter("ido", id_origem).setParameter("ido2", id_origem).setParameter("id", id)
				.getResultList();

		for (Object[] row : registosA) {
			if (row[QA_DATA_INI] == null || row[QA_HORA_INI] == null
					|| row[QA_DATA_FIM] == null || row[QA_HORA_FIM] == null) {
				LOG.warning("DATA_INI/FIM nulo id_origem=" + id_origem + " — registo ignorado");
				continue;
			}
			boolean novaEtq = "1".equals(novaetiqueta);
			String tipoOp = ("A".equals(estado) || "M".equals(estado)) && !novaEtq ? "1" : opPrevistaFicheiro;
			String maqOrig = row[QA_MAQ_NUM_ORIG] != null ? row[QA_MAQ_NUM_ORIG].toString() : "000";

			String posicao;
			if ("000".equals(maqOrig)) { posicao = primeiraLinha ? "1" : "2"; primeiraLinha = false; }
			else posicao = "2";

			boolean incluiTempos = "PF".equals(tipo) && !"M".equals(estado) && !"P".equals(estado);
			boolean incluiTemposAlt = "PF".equals(tipo) && "1".equals(row[QA_ALTERADO] != null ? row[QA_ALTERADO].toString() : "0") && !"P".equals(estado);
			if (incluiTemposAlt) houvAlteracoes = true;

			String cab = buildCabecalho(
					row[QA_DATA_INI].toString(), sequencia,
					ligneProduction(row[QA_OP_COD_ORIG], novaEtq, opPrevistaFicheiro, estado, estado2),
					of, tipoOp, formatOpNumFicheiro(OP_NUM, estado, opPrevistaFicheiro),
					posicao, row[QA_SEC_NUM] != null ? row[QA_SEC_NUM].toString() : "",
					maqOrig, row[QA_TURNO], "MO",
					row[QA_UTZ_CRIA] != null ? row[QA_UTZ_CRIA].toString() : "");

			StringBuilder ra = new StringBuilder(cab);
			ra.append("   A");
			ra.append(formatDate(row[QA_DATA_INI]));
			ra.append(formatTime(row[QA_HORA_INI]));
			ra.append(formatDate(row[QA_DATA_FIM]));
			ra.append(formatTime(row[QA_HORA_FIM]));
			ra.append("04002");
			ra.append((incluiTempos || incluiTemposAlt) ? formatTempo(row[QA_TEMPO_PREP], total) : ZEROS_15);
			ra.append(sinal).append("22");
			ra.append((incluiTempos || incluiTemposAlt) ? formatTempo(row[QA_TEMPO_EXEC], total) : ZEROS_15);
			ra.append(sinal).append("22         ").append(CRLF);

			String linhaA = ra.toString();

			if (lider && !"000".equals(maqOrig)) {
				existeMaquina = true;
				StringBuffer buf = new StringBuffer(linhaA);
				buf.replace(70, 84, "              "); buf.replace(47, 48, "1");
				double tp = getTempos(cfg.DATA_INI, cfg.HORA_INI, cfg.DATA_FIM, cfg.HORA_FIM, cfg.MOMENTO_PARAGEM, id_origem, "P");
				double te = getTempos(cfg.DATA_INI, cfg.HORA_INI, cfg.DATA_FIM, cfg.HORA_FIM, cfg.MOMENTO_PARAGEM, id_origem, "E");
				buf.replace(121, 136, padZeroLeft(String.format("%.4f", tp).replace(",", "").replace(".", "").replace("$", ""), 15));
				buf.replace(139, 154, padZeroLeft(String.format("%.4f", te).replace(",", "").replace(".", "").replace("$", ""), 15));
				dadosMaquina = buf.toString();
				lider = false;
			}

			if ("2".equals(opPrevistaFicheiro) && ("C".equals(estado) || "M".equals(estado)) && atualiza && ficheiro != 1) {
				Integer idT = id_etiqueta != null ? id_etiqueta : id;
				String tipoT = id_etiqueta != null ? "C" : "PF";
				atualizatabela_AUX(
						row[QA_UTZ_CRIA] != null ? row[QA_UTZ_CRIA].toString() : "",
						row[QA_DATA_INI].toString(),
						row[QA_REF_NUM] != null ? row[QA_REF_NUM].toString() : "",
						of,
						row[QA_OP_COD_ORIG] != null ? row[QA_OP_COD_ORIG].toString() : "",
						idT, tipoT, row[QA_HORA_INI].toString());
				atualiza = false;
			}

			conteudo += linhaA;
			if (cria_pausa || isMuro) {
				String utz = row[QA_UTZ_CRIA] != null ? row[QA_UTZ_CRIA].toString() : "";
				linhaUtz.put(utz, linhaA);
				linhaUtzInicio.put(utz, linhaA.substring(0, 87));
			}
		}

		// conteudoParaPausas = maquina + pessoas (equivalente ao data_maquina original)
		String conteudoParaPausas = (existeMaquina && "PF".equals(tipo))
				? dadosMaquina + conteudo : conteudo;
		if (existeMaquina && "PF".equals(tipo)) conteudo = dadosMaquina + conteudo;

		// ── Pausas (Registo B) ────────────────────────────────────────────────
		if (cria_pausa || (isMuro && !"P".equals(estado))) {
			pausasMuro = processarPausas(id, id_origem, cfg, sinal, estado, estado2,
					ficheiro, tipo, existeMaquina, isMuro, dadosMaquina, conteudoParaPausas,
					path2, path_error, ficheirosdownload, nome_ficheiro2, nomezip,
					linhaUtz, linhaUtzInicio);
		}
		if (isMuro && !pausasMuro.isEmpty()) conteudo += pausasMuro;

		// ── Quantidades (Q) e Defeitos (R) ────────────────────────────────────
		if (!"P".equals(estado) && !"M".equals(estado)) {
			conteudo += buildRegistosQ(id, id_origem, id_etiqueta, tipo, of, OP_NUM,
					estado, estado2, novaetiqueta, sequencia, sinal, ip_posto, cfg, ID_OP_LIN, opPrevistaFicheiro);
			boolean[] fa = {houvAlteracoes};
			conteudo += buildRegistosR(id, id_origem, id_etiqueta, tipo, of, OP_NUM,
					estado, estado2, novaetiqueta, sequencia, sinal, cfg, fa, ID_OP_LIN, opPrevistaFicheiro);
			houvAlteracoes = fa[0];
		} else if ("M".equals(estado) && !"COMP".equals(tipo)) {
			conteudo += crialinhareferencia(cfg.DATA_INI, cfg.HORA_INI, cfg.DATA_FIM, cfg.HORA_FIM,
					cfg.QUANT_BOAS_TOTAL, cfg.QUANT_BOAS, cfg.QUANT_DEF, ID_OP_LIN,
					id_origem, id, sequencia, OP_NUM, estado, estado2, novaetiqueta, sinal, tipo, of);
		}

		// ── Escrita ───────────────────────────────────────────────────────────
		if (!"P".equals(estado) && (!"M".equals(estado) || houvAlteracoes)) {
			try {
				if (ficheirosdownload) {
					escreverZip(conteudo, nome_ficheiro, nomezip);
				} else {
					escreverLocal(conteudo, path);
				}
			} catch (IOException e) {
				notificarErro(e.getMessage());
				criarfileerro(estado, patherro, conteudo, houvAlteracoes);
				LOG.severe("Erro ao escrever ficheiro: " + e.getMessage());
			}
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Pausas — Registo B
	// ─────────────────────────────────────────────────────────────────────────

	private String processarPausas(
			Integer id, Integer id_origem, MaquinaConfig cfg, String sinal,
			String estado, String estado2, Integer ficheiro, String tipo,
			boolean existeMaquina, boolean isMuro, String dadosMaquina, String conteudoParaPausas,
			String path2, String path_error, Boolean ficheirosdownload,
			String nome_ficheiro2, String nomezip,
			Map<String, String> linhaUtz, Map<String, String> linhaUtzInicio)
			throws IOException, ParseException {

		String pausasMuro = "";

		@SuppressWarnings("unchecked")
		List<Object[]> pausas = entityManager.createNativeQuery(
				"SELECT c." + cfg.DATA_INI + ", c." + cfg.HORA_INI
				+ ", c." + cfg.DATA_FIM + ", c." + cfg.HORA_FIM
				+ ", CAST((DATEDIFF(second,"
				+ "  DATEADD(DAY, DATEDIFF(DAY, c." + cfg.HORA_INI + ", c." + cfg.DATA_INI + "), CAST(c." + cfg.HORA_INI + " AS DATETIME)),"
				+ "  DATEADD(DAY, DATEDIFF(DAY, c." + cfg.HORA_FIM + ", c." + cfg.DATA_FIM + "), CAST(c." + cfg.HORA_FIM + " AS DATETIME))"
				+ ") / 3600.00) AS decimal(18,4)) AS timediff"
				+ ", c." + cfg.TIPO_PARAGEM + ", c." + cfg.MOMENTO_PARAGEM
				+ ", c.ID_UTZ_CRIA AS utz1, a.ID_UTZ_CRIA AS utz2"
				+ ", CASE WHEN (c.MOMENTO_PARAGEM_M2 != c.MOMENTO_PARAGEM_M1 OR c.TIPO_PARAGEM_M2 != c.TIPO_PARAGEM_M1"
				+ "   OR c.DATA_INI_M2 != c.DATA_INI_M1 OR c.HORA_INI_M1 != c.HORA_INI_M2"
				+ "   OR c.DATA_FIM_M2 != c.DATA_FIM_M1 OR c.HORA_FIM_M1 != c.HORA_FIM_M2) THEN 1 ELSE 1 END AS alterado"
				+ ", CASE WHEN (c.DATA_INI_M1 IS NULL OR c.HORA_INI_M1 IS NULL"
				+ "   OR c.DATA_FIM_M1 IS NULL OR c.HORA_FIM_M1 IS NULL) THEN 1 ELSE 0 END AS novo"
				+ " FROM RP_OF_CAB a"
				+ " INNER JOIN RP_OF_OP_CAB b ON b.ID_OF_CAB = a.ID_OF_CAB"
				+ " INNER JOIN RP_OF_PARA_LIN c ON c.ID_OP_CAB = b.ID_OP_CAB"
				+ " WHERE a.ID_OF_CAB = :id"
				+ "   AND c." + cfg.DATA_INI + " IS NOT NULL AND c." + cfg.DATA_FIM + " IS NOT NULL")
				.setParameter("id", id).getResultList();

		int count = 0;
		boolean criouPausa = false;

		for (Object[] p : pausas) {
			count++;
			String utz = p[7] != null ? p[7].toString() : "";

			StringBuilder rb = new StringBuilder();
			rb.append("B");
			rb.append(formatDate(p[0])); rb.append(formatTime(p[1]));
			rb.append(formatDate(p[2])); rb.append(formatTime(p[3]));
			rb.append(padRight(p[5], 4)).append("3");

			String tPrep = "P".equals(p[6] != null ? p[6].toString() : "") ? formatQuantidade(p[4], 15) : ZEROS_15;
			rb.append(tPrep).append(sinal).append("3");
			String tExec = "E".equals(p[6] != null ? p[6].toString() : "") ? formatQuantidade(p[4], 15) : ZEROS_15;
			rb.append(tExec).append(sinal);
			rb.append(padRight("", 39)).append(CRLF);

			String seq = sequencia(id.toString());
			String linhaAUtz   = linhaUtz.containsKey(utz) ? linhaUtz.get(utz) : "";
			String linhaAInicio = linhaUtzInicio.containsKey(utz) ? linhaUtzInicio.get(utz) : "";

			StringBuffer bufA = new StringBuffer(linhaAUtz);
			if (!isMuro && bufA.length() > 27) bufA.replace(18, 27, seq);

			String conteudoPausa = "";
			String linhaAMaquina = "";

			if (!existeMaquina) {
				if (!isMuro) conteudoPausa += bufA.toString();
			} else {
				try (BufferedReader br = new BufferedReader(new StringReader(conteudoParaPausas))) {
					String line;
					while ((line = br.readLine()) != null) {
						StringBuffer b6 = new StringBuffer(line);
						if (!isMuro && b6.length() > 27) b6.replace(18, 27, seq);
						if (b6.length() >= 154) { b6.replace(121, 136, ZEROS_15); b6.replace(139, 154, ZEROS_15); }
						String l6 = b6.toString();
						String recurso = b6.length() >= 84 ? b6.substring(74, 84).trim() : "";
						if (recurso.equals(utz) || recurso.isEmpty()) conteudoPausa += l6 + CRLF;
						if (!l6.contains("MO")) linhaAMaquina = l6 + CRLF;
					}
				}
				String utz2 = p[8] != null ? p[8].toString() : "";
				if ("PF".equals(tipo) && utz.equals(utz2) && !criouPausa) {
					StringBuffer bufM = new StringBuffer(dadosMaquina);
					if (!isMuro && bufM.length() > 27) bufM.replace(18, 27, seq);
					String lm = bufM.toString();
					String cab87 = lm.length() >= 87 ? lm.substring(0, 87) : lm;
					CRIAPAUSASMAQUINA(cfg.DATA_INI, cfg.HORA_INI, cfg.DATA_FIM, cfg.HORA_FIM,
							cfg.MOMENTO_PARAGEM, cfg.TIPO_PARAGEM, sinal,
							cab87, linhaAMaquina, path2, ficheirosdownload,
							nome_ficheiro2, nomezip, id, "P", path_error);
					CRIAPAUSASMAQUINA(cfg.DATA_INI, cfg.HORA_INI, cfg.DATA_FIM, cfg.HORA_FIM,
							cfg.MOMENTO_PARAGEM, cfg.TIPO_PARAGEM, sinal,
							cab87, linhaAMaquina, path2, ficheirosdownload,
							nome_ficheiro2, nomezip, id, "E", path_error);
					criouPausa = true;
				}
			}

			StringBuffer bufI = new StringBuffer(linhaAInicio);
			if (!isMuro && bufI.length() > 27) bufI.replace(18, 27, seq);
			conteudoPausa += bufI.toString() + rb.toString();

			float diff = p[4] != null ? Float.parseFloat(p[4].toString()) : 0f;
			String alterado = p[9] != null ? p[9].toString() : "0";
			String novo = p[10] != null ? p[10].toString() : "0";
			boolean criarPausa =
				("M".equals(estado2) && "1".equals(alterado) && !"1".equals(novo) && diff > 0)
				|| ("M".equals(estado2) && "1".equals(novo) && ficheiro == 2 && diff > 0)
				|| (!"M".equals(estado2) && diff > 0);

			if (criarPausa) {
				if (isMuro) pausasMuro += conteudoPausa;
				else criar_ficheiro_Pausa(conteudoPausa, path2, count, ficheirosdownload, nome_ficheiro2, nomezip, path_error);
			}
		}
		return pausasMuro;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Registo Q — Quantidades boas
	// ─────────────────────────────────────────────────────────────────────────

	private String buildRegistosQ(Integer id, Integer id_origem, Integer id_etiqueta,
			String tipo, String of, String OP_NUM, String estado, String estado2,
			String novaetiqueta, String sequencia, String sinal, String ip_posto,
			MaquinaConfig cfg, String ID_OP_LIN, String opPrevistaFicheiro) {

		String sqlCols = "SELECT a.ID_OF_CAB_ORIGEM, a.OF_NUM, e.OF_NUM_ORIGEM, a.OP_NUM, c.REF_NUM,"
			+ " c.REF_VAR1, c.REF_VAR2, c.REF_INDNUMENR, a.MAQ_NUM_ORIG, a.SEC_NUM,"
			+ " d." + cfg.DATA_INI + ", d." + cfg.HORA_INI + ", d." + cfg.DATA_FIM + ", d." + cfg.HORA_FIM
			+ ", d.ID_UTZ_CRIA, c.REF_IND,"
			+ " CAST(c." + cfg.QUANT_BOAS_TOTAL + " AS decimal(18,4)) AS qtd1,"
			+ " CAST(e." + cfg.QUANT_BOAS + " AS decimal(18,4)) AS qtd2,"
			+ " a.OP_PREVISTA, c.OBS_REF,"
			+ " (SELECT ID_TURNO FROM RP_CONF_TURNO WHERE CAST(d." + cfg.HORA_INI + " AS time) BETWEEN HORA_INICIO AND HORA_FIM) AS turno,"
			+ " a.OP_COD_ORIGEM, 1 AS alterado FROM RP_OF_CAB a"
			+ " INNER JOIN RP_OF_OP_CAB b ON b.ID_OF_CAB = a.ID_OF_CAB";

		@SuppressWarnings("unchecked")
		List<Object[]> rows = "COMP".equals(tipo)
			? entityManager.createNativeQuery(sqlCols
				+ " INNER JOIN RP_OF_OP_LIN c ON b.ID_OP_CAB = c.ID_OP_CAB"
				+ " INNER JOIN RP_OF_OP_FUNC d ON d.ID_OP_CAB ="
				+ "   (SELECT TOP 1 x.ID_OP_CAB FROM RP_OF_OP_CAB x WHERE x.ID_OF_CAB = :ido)"
				+ " LEFT JOIN RP_OF_OP_ETIQUETA e ON e.ID_OP_LIN = c.ID_OP_LIN"
				+ " WHERE a.ID_OF_CAB = :id AND (a.OF_NUM IS NOT NULL OR e.OF_NUM_ORIGEM IS NOT NULL)"
				+ "   AND e.ID_REF_ETIQUETA = :etq")
				.setParameter("ido", id_origem).setParameter("id", id).setParameter("etq", id_etiqueta)
				.getResultList()
			: (ID_OP_LIN == null || "NULL".equals(ID_OP_LIN)) ? new java.util.ArrayList<>()
			: entityManager.createNativeQuery(sqlCols
				+ " INNER JOIN RP_OF_OP_LIN c ON c.ID_OP_LIN = :oplin"
				+ " INNER JOIN RP_OF_OP_FUNC d ON d.ID_OP_CAB = b.ID_OP_CAB"
				+ "   AND d.ID_OP_CAB IN (SELECT TOP 1 x.ID_OP_CAB FROM RP_OF_OP_CAB x WHERE x.ID_OF_CAB = :ido)"
				+ " LEFT JOIN RP_OF_OP_ETIQUETA e ON e.ID_OP_LIN = c.ID_OP_LIN"
				+ " WHERE a.ID_OF_CAB = :id AND (a.OF_NUM IS NOT NULL OR e.OF_NUM_ORIGEM IS NOT NULL)")
				.setParameter("oplin", Long.parseLong(ID_OP_LIN))
				.setParameter("ido", id_origem).setParameter("id", id)
				.getResultList();

		StringBuilder sb = new StringBuilder();
		for (Object[] row : rows)
			sb.append(buildRegistoQ(row, of, OP_NUM, estado, estado2, novaetiqueta,
					sequencia, sinal, tipo, ip_posto, id_origem, opPrevistaFicheiro));
		return sb.toString();
	}

	private String buildRegistoQ(Object[] row, String of, String OP_NUM,
			String estado, String estado2, String novaetiqueta,
			String sequencia, String sinal, String tipo, String ip_posto, Integer id_origem,
			String opPrevistaFicheiro) {

		boolean novaEtq = "1".equals(novaetiqueta);
		String tipoOp = ("A".equals(estado) || "M".equals(estado)) && !novaEtq ? "1" : opPrevistaFicheiro;
		boolean isCabOrig = row[QQ_ID_CAB_ORIG] == null;
		boolean secPrinc  = "000".equals(row[QQ_MAQ_NUM_ORIG] != null ? row[QQ_MAQ_NUM_ORIG].toString() : "");
		boolean moAtivo   = isCabOrig ? secPrinc : true;

		String cab = buildCabecalho(
				row[QQ_DATA_INI] != null ? row[QQ_DATA_INI].toString() : "", sequencia,
				ligneProduction(row[QQ_OP_COD_ORIG], novaEtq, opPrevistaFicheiro, estado, estado2),
				isCabOrig ? padRight(row[QQ_OF_NUM], 10) : padRight(row[QQ_OF_NUM_ORIG], 10),
				tipoOp, formatOpNumFicheiro(OP_NUM, estado, opPrevistaFicheiro), "1",
				row[QQ_SEC_NUM] != null ? row[QQ_SEC_NUM].toString() : "",
				row[QQ_MAQ_NUM_ORIG] != null ? row[QQ_MAQ_NUM_ORIG].toString() : "",
				row[QQ_TURNO],
				moAtivo ? "MO  " : ESPACOS_4,
				moAtivo ? padRight(row[QQ_UTZ_CRIA], 10) : ESPACOS_10);

		String quant = isCabOrig ? formatQuantidade(row[QQ_QUANT_TOTAL], 15) : formatQuantidade(row[QQ_QUANT], 15);
		if ("M".equals(estado)) {
			boolean alt = !"0".equals(row[QQ_ALTERADO] != null ? row[QQ_ALTERADO].toString() : "0");
			quant = alt ? quant : ZEROS_15;
		}

		StringBuilder sb = new StringBuilder(cab);
		sb.append("   Q");
		sb.append(formatDate(row[QQ_DATA_INI])); sb.append(formatTime(row[QQ_HORA_INI]));
		sb.append(formatDate(row[QQ_DATA_FIM])); sb.append(formatTime(row[QQ_HORA_FIM]));
		sb.append(padRight(row[QQ_REF_NUM], 17));
		sb.append(padRight(row[QQ_REF_VAR1], 10)); sb.append(padRight(row[QQ_REF_VAR2], 10));
		sb.append(padRight(row[QQ_REF_IND], 10));
		sb.append(padZeroLeft(row[QQ_REF_INDNUMENR], 9));
		sb.append("1");
		sb.append(quant).append("  ").append(sinal);
		sb.append(ESPACOS_4).append(ZEROS_15);
		sb.append(ESPACOS_10).append(ZEROS_9).append(ESPACOS_10).append(ESPACOS_10);
		sb.append("COMP".equals(tipo) ? padRight("", 35) : padRight(of, 35));
		sb.append(ESPACOS_10);
		String obs = id_origem.toString();
		if (!"COMP".equals(tipo) && ip_posto != null) obs += "@" + buscarNomeImpressora(ip_posto);
		sb.append(padRight(obs, 40));
		String etq = !"COMP".equals(tipo) ? buscarEtiquetasCaixas(id_origem, row[QQ_REF_NUM] != null ? row[QQ_REF_NUM].toString() : "") : "";
		sb.append(padRight(etq, 54)).append(CRLF);
		return sb.toString();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Registo R — Defeitos
	// ─────────────────────────────────────────────────────────────────────────

	private String buildRegistosR(Integer id, Integer id_origem, Integer id_etiqueta,
			String tipo, String of, String OP_NUM, String estado, String estado2,
			String novaetiqueta, String sequencia, String sinal, MaquinaConfig cfg, boolean[] fa,
			String ID_OP_LIN, String opPrevistaFicheiro) {

		boolean pecasRecup = false;
		if ("COMP".equals(tipo)) {
			List<Object[]> r = verificaPecasRecuperacaoInternal(id_origem);
			if (!r.isEmpty()) { Number n = (Number) r.get(0)[0]; pecasRecup = n != null && n.intValue() > 0; }
		}
		if (pecasRecup) return "";

		String sqlBase = "SELECT d.COD_DEF, CAST(d." + cfg.QUANT_DEF + " AS decimal(18,4)),"
			+ " a.ID_OF_CAB_ORIGEM, a.OF_NUM, f.OF_NUM_ORIGEM, a.OP_NUM,"
			+ " c.REF_NUM, c.REF_VAR1, c.REF_VAR2, c.REF_INDNUMENR,"
			+ " a.MAQ_NUM_ORIG, a.SEC_NUM,"
			+ " e." + cfg.DATA_INI + ", e." + cfg.HORA_INI + ", e." + cfg.DATA_FIM + ", e." + cfg.HORA_FIM
			+ ", d.ID_UTZ_CRIA, c.REF_IND, c." + cfg.QUANT_BOAS_TOTAL + ", f." + cfg.QUANT_BOAS + ", d.OBS_DEF,"
			+ " a.OP_PREVISTA,"
			+ " (SELECT ID_TURNO FROM RP_CONF_TURNO WHERE CAST(e." + cfg.HORA_INI + " AS time) BETWEEN HORA_INICIO AND HORA_FIM) AS turno,"
			+ " a.OP_COD_ORIGEM FROM RP_OF_CAB a";

		@SuppressWarnings("unchecked")
		List<Object[]> rows = "COMP".equals(tipo)
			? entityManager.createNativeQuery(sqlBase
				+ " INNER JOIN RP_OF_OP_CAB b ON b.ID_OF_CAB = a.ID_OF_CAB"
				+ " INNER JOIN RP_OF_OP_LIN c ON b.ID_OP_CAB = c.ID_OP_CAB"
				+ " INNER JOIN RP_OF_DEF_LIN d ON d.ID_OP_LIN = c.ID_OP_LIN"
				+ " INNER JOIN RP_OF_OP_FUNC e ON e.ID_OP_CAB ="
				+ "   (SELECT TOP 1 x.ID_OP_CAB FROM RP_OF_OP_CAB x WHERE x.ID_OF_CAB = :ido)"
				+ " LEFT JOIN RP_OF_OP_ETIQUETA f ON f.ID_OP_LIN = c.ID_OP_LIN AND f.ID_REF_ETIQUETA = d.ID_REF_ETIQUETA"
				+ " WHERE a.ID_OF_CAB = :id AND d.ID_REF_ETIQUETA = :etq ORDER BY c.REF_NUM, d.COD_DEF")
				.setParameter("ido", id_origem).setParameter("id", id).setParameter("etq", id_etiqueta)
				.getResultList()
			: (ID_OP_LIN == null || "NULL".equals(ID_OP_LIN)) ? new java.util.ArrayList<>()
			: entityManager.createNativeQuery(sqlBase
				+ " INNER JOIN RP_OF_OP_LIN c ON c.ID_OP_LIN = :oplin"
				+ " INNER JOIN RP_OF_DEF_LIN d ON d.ID_OP_LIN = c.ID_OP_LIN"
				+ " INNER JOIN RP_OF_OP_FUNC e ON e.ID_OP_CAB ="
				+ "   (SELECT TOP 1 x.ID_OP_CAB FROM RP_OF_OP_CAB x WHERE x.ID_OF_CAB = :ido)"
				+ " LEFT JOIN RP_OF_OP_ETIQUETA f ON f.ID_OP_LIN = c.ID_OP_LIN AND f.ID_REF_ETIQUETA = d.ID_REF_ETIQUETA"
				+ " WHERE a.ID_OF_CAB = :id ORDER BY c.REF_NUM, d.COD_DEF")
				.setParameter("oplin", Long.parseLong(ID_OP_LIN))
				.setParameter("ido", id_origem).setParameter("id", id)
				.getResultList();

		StringBuilder sb = new StringBuilder();
		for (Object[] row : rows) {
			fa[0] = true;
			sb.append(buildRegistoR(row, of, OP_NUM, estado, estado2, novaetiqueta, sequencia, sinal, pecasRecup, opPrevistaFicheiro));
		}
		return sb.toString();
	}

	private String buildRegistoR(Object[] row, String of, String OP_NUM,
			String estado, String estado2, String novaetiqueta,
			String sequencia, String sinalOrig, boolean pecasRecup, String opPrevistaFicheiro) {

		boolean novaEtq = "1".equals(novaetiqueta);
		String tipoOp = ("A".equals(estado) || "M".equals(estado)) && !novaEtq ? "1" : opPrevistaFicheiro;
		boolean isCabOrig = row[QR_ID_CAB_ORIG] == null;
		boolean secPrinc  = "000".equals(row[QR_MAQ_NUM_ORIG] != null ? row[QR_MAQ_NUM_ORIG].toString() : "");
		boolean moAtivo   = isCabOrig ? secPrinc : true;

		String cab = buildCabecalho(
				row[QR_DATA_INI] != null ? row[QR_DATA_INI].toString() : "", sequencia,
				ligneProduction(row[QR_OP_COD_ORIG], novaEtq, opPrevistaFicheiro, estado, estado2),
				isCabOrig ? padRight(row[QR_OF_NUM], 10) : padRight(row[QR_OF_NUM_ORIG], 10),
				tipoOp, formatOpNumFicheiro(OP_NUM, estado, opPrevistaFicheiro), "1",
				row[QR_SEC_NUM] != null ? row[QR_SEC_NUM].toString() : "",
				row[QR_MAQ_NUM_ORIG] != null ? row[QR_MAQ_NUM_ORIG].toString() : "",
				row[QR_TURNO],
				moAtivo ? "MO  " : ESPACOS_4,
				moAtivo ? padRight(row[QR_UTZ_CRIA], 10) : ESPACOS_10);

		String sinal = pecasRecup ? ("+".equals(sinalOrig) ? "-" : "+") : sinalOrig;

		StringBuilder sb = new StringBuilder(cab);
		sb.append("   R");
		sb.append(formatDate(row[QR_DATA_INI])); sb.append(formatTime(row[QR_HORA_INI]));
		sb.append(formatDate(row[QR_DATA_FIM])); sb.append(formatTime(row[QR_HORA_FIM]));
		sb.append(padRight(row[QR_REF_NUM], 17));
		sb.append(padRight(row[QR_REF_VAR1], 10)); sb.append(padRight(row[QR_REF_VAR2], 10));
		sb.append(padRight(row[QR_REF_IND], 10));
		sb.append(padZeroLeft(row[QR_REF_INDNUMENR], 9));
		sb.append(padRight(row[QR_COD_DEF], 4)).append("1");
		sb.append(formatQuantidade(row[QR_QUANT_DEF], 15)).append("  ").append(sinal);
		sb.append(padRight("", 103));  // campos reservados Silver-CS tipo R
		sb.append(padRight(row[QR_OBS_DEF] != null ? row[QR_OBS_DEF].toString() : "", 39));
		sb.append(CRLF);
		return sb.toString();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// crialinhareferencia
	// ─────────────────────────────────────────────────────────────────────────

	public String crialinhareferencia(String DATA_INI, String HORA_INI, String DATA_FIM, String HORA_FIM,
			String QUANT_BOAS_TOTAL, String QUANT_BOAS, String QUANT_DEF, String ID_OP_LIN, Integer id_origem,
			Integer id, String sequencia, String OP_NUM, String estado, String estado2, String novaetiqueta,
			String SINAL, String tipo, String of) {

		@SuppressWarnings("unchecked")
		List<Object[]> rows = entityManager.createNativeQuery(
				"SELECT a.ID_OF_CAB_ORIGEM, a.OF_NUM, e.OF_NUM_ORIGEM, a.OP_NUM, c.REF_NUM,"
				+ " c.REF_VAR1, c.REF_VAR2, c.REF_INDNUMENR, a.MAQ_NUM_ORIG, a.SEC_NUM,"
				+ " d." + DATA_INI + ", d." + HORA_INI + ", d." + DATA_FIM + ", d." + HORA_FIM
				+ ", d.ID_UTZ_CRIA, c.REF_IND,"
				+ " CAST(c." + QUANT_BOAS_TOTAL + " AS decimal(18,4)) AS qtd1,"
				+ " CAST(e." + QUANT_BOAS + " AS decimal(18,4)) AS qtd2,"
				+ " a.OP_PREVISTA, c.OBS_REF,"
				+ " (SELECT ID_TURNO FROM RP_CONF_TURNO WHERE CAST(d." + HORA_INI + " AS time) BETWEEN HORA_INICIO AND HORA_FIM) AS turno,"
				+ " a.OP_COD_ORIGEM, 1 AS alterado"
				+ " FROM RP_OF_CAB a"
				+ " INNER JOIN RP_OF_OP_CAB b ON b.ID_OF_CAB = a.ID_OF_CAB"
				+ " INNER JOIN RP_OF_OP_LIN c ON c.ID_OP_LIN = :oplin"
				+ " INNER JOIN RP_OF_OP_FUNC d ON d.ID_OP_CAB = b.ID_OP_CAB"
				+ "   AND d.ID_OP_CAB IN (SELECT TOP 1 x.ID_OP_CAB FROM RP_OF_OP_CAB x WHERE x.ID_OF_CAB = :ido)"
				+ " LEFT JOIN RP_OF_OP_ETIQUETA e ON e.ID_OP_LIN = c.ID_OP_LIN"
				+ " WHERE a.ID_OF_CAB = :id AND (a.OF_NUM IS NOT NULL OR e.OF_NUM_ORIGEM IS NOT NULL)")
				.setParameter("oplin", id_origem)
				.setParameter("ido", id_origem)
				.setParameter("id", id)
				.getResultList();

		StringBuilder sb = new StringBuilder();
		for (Object[] row : rows) {
			String opPrevRow = row[QQ_OP_PREVISTA] != null ? row[QQ_OP_PREVISTA].toString() : "1";
			sb.append(buildRegistoQ(row, of, OP_NUM, estado, estado2, novaetiqueta,
					sequencia, SINAL, tipo, null, id_origem, opPrevRow));
		}
		return sb.toString();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// criarFicheiroConsumo — Registo C
	// ─────────────────────────────────────────────────────────────────────────

	public void criarFicheiroConsumo(Integer id_of_cab, Boolean ficheirosdownload, String nomezip,
			Boolean primeira_OPENUM) throws IOException {

		if (!primeira_OPENUM) return;
		if (isPostoMatrix(id_of_cab)) return;

		Date agora = new Date();
		String dataAtual = new SimpleDateFormat("yyyyMMdd").format(agora);
		String horaAtual = new SimpleDateFormat("HHmmss").format(agora);
		String nomeFicheiro = dataAtual + horaAtual + "_ETIQUETA_PRODUCAO_STOCK_ID" + id_of_cab + ".txt";

		@SuppressWarnings("unchecked")
		List<Object[]> cfg = entityManager.createNativeQuery(
				"SELECT TOP 1 PASTA_FICHEIRO, PASTA_ETIQUETAS, MODELO_REPORT, PASTA_DESTINO_ERRO FROM GER_PARAMETROS")
				.getResultList();
		if (cfg.isEmpty()) return;
		String path      = cfg.get(0)[0] + nomeFicheiro;
		String pathErro  = cfg.get(0)[3] + nomeFicheiro;

		String sequencia = sequencia(id_of_cab.toString());
		String url = getURL();

		// NOTA: DECLARE @var = :param não é suportado em SQL Server prepared statements
		// id_of_cab é Integer (não user input), concatenação é segura
		@SuppressWarnings("unchecked")
		List<Object[]> rows = entityManager.createNativeQuery(
				"DECLARE @ID_OF_CAB int = " + id_of_cab + "; "
				+ "SELECT t.ETQNUM, e.OF_NUM, e.OP_NUM, e.OP_COD_ORIGEM, e.MAQ_NUM_ORIG,"
				+ " m.DATA_INI_M2, m.HORA_INI_M2, m.DATA_FIM_M2, m.HORA_FIM_M2,"
				+ " (SELECT ID_TURNO FROM RP_CONF_TURNO WHERE m.HORA_INI_M2 > HORA_INICIO AND m.HORA_INI_M2 < HORA_FIM) N_EQUIPA,"
				+ " t.PROREF, t.VA1REF, t.VA2REF, t.INDREF, t.INDNUMENR, t.UNICOD,"
				+ " t.LIECOD, t.EMPCOD, t.ETQORILOT1, t.ETQNUMENR,"
				+ " (d.QUANT_BOAS_M2 + CASE WHEN TIPO_PECA IN ('COM','COMS') THEN 0 ELSE d.QUANT_DEF_M2 END) QUANT,"
				+ " t.LOTNUMENR"
				+ " FROM RP_OF_CAB a WITH(NOLOCK)"
				+ " INNER JOIN RP_OF_OP_CAB b WITH(NOLOCK) ON a.ID_OF_CAB = b.ID_OF_CAB"
				+ " INNER JOIN RP_OF_OP_LIN c WITH(NOLOCK) ON b.ID_OP_CAB = c.ID_OP_CAB"
				+ " INNER JOIN RP_OF_OP_ETIQUETA d WITH(NOLOCK) ON c.ID_OP_LIN = d.ID_OP_LIN"
				+ " LEFT JOIN (SELECT ETQNUM, st.PROREF, VA1REF, VA2REF, INDREF, st.INDNUMENR, UNICOD,"
				+ "   LIECOD, EMPCOD, ETQORILOT1, ETQNUMENR, sl.LOTNUMENR"
				+ "   FROM SILVER.dbo.SETQDE st WITH(NOLOCK)"
				+ "   LEFT JOIN SILVER.dbo.STOLOT sl WITH(NOLOCK) ON st.INDNUMENR = sl.INDNUMENR AND st.ETQORILOT1 = sl.LOTREF"
				+ "   WHERE ETQNUM IN (SELECT RIGHT('000'+CAST(td.REF_ETIQUETA AS varchar(10)),10)"
				+ "     FROM RP_OF_CAB ta INNER JOIN RP_OF_OP_CAB tb WITH(NOLOCK) ON ta.ID_OF_CAB = tb.ID_OF_CAB"
				+ "     INNER JOIN RP_OF_OP_LIN tc ON tb.ID_OP_CAB = tc.ID_OP_CAB"
				+ "     INNER JOIN RP_OF_OP_ETIQUETA td WITH(NOLOCK) ON tc.ID_OP_LIN = td.ID_OP_LIN)"
				+ " ) t ON RIGHT('000'+CAST(d.REF_ETIQUETA AS varchar(10)),10) = t.ETQNUM"
				+ " LEFT JOIN (SELECT * FROM RP_OF_CAB WITH(NOLOCK) WHERE ID_OF_CAB = @ID_OF_CAB) e ON a.ID_OF_CAB_ORIGEM = e.ID_OF_CAB"
				+ " LEFT JOIN (SELECT g.ID_OF_CAB, h.* FROM RP_OF_CAB f WITH(NOLOCK)"
				+ "   INNER JOIN RP_OF_OP_CAB g WITH(NOLOCK) ON f.ID_OF_CAB = g.ID_OF_CAB"
				+ "   INNER JOIN RP_OF_OP_FUNC h WITH(NOLOCK) ON g.ID_OP_CAB = h.ID_OP_CAB AND f.ID_UTZ_CRIA = h.ID_UTZ_CRIA"
				+ "   WHERE f.ID_OF_CAB = @ID_OF_CAB) m ON a.ID_OF_CAB_ORIGEM = m.ID_OF_CAB"
				+ " WHERE a.ID_OF_CAB_ORIGEM = @ID_OF_CAB"
				+ "   AND (d.QUANT_BOAS_M2 + CASE WHEN TIPO_PECA IN ('COM','COMS') THEN 0 ELSE d.QUANT_DEF_M2 END) > 0")
				.getResultList();

		StringBuilder data = new StringBuilder();
		ConnectProgress cp = new ConnectProgress();

		for (Object[] row : rows) {
			String of      = row[1] != null ? row[1].toString() : "";
			String seccao  = row[3] != null ? row[3].toString() : "";
			String subSec  = row[4] != null ? row[4].toString() : "";
			String refComp = "", indNumCse = "", nclRang = "";

			try {
				List<HashMap<String, String>> lista = cp.getOrigineComposant(url, row[10] != null ? row[10].toString() : "", of);
				if (!lista.isEmpty()) {
					nclRang    = lista.get(0).get("NCLRANG");
					refComp    = lista.get(0).get("PROREF");
					indNumCse  = lista.get(0).get("INDNUMCSE");
				}
			} catch (SQLException e) {
				LOG.warning("Erro getOrigineComposant: " + e.getMessage());
			}

			String opNum = row[2] != null ? row[2].toString() : "";
			String equipe = row[9] != null ? padZeroLeft(row[9].toString(), 2) : "  ";

			String cab = buildCabecalho(
					row[5] != null ? row[5].toString() : dataAtual, sequencia,
					ESPACOS_4, of, "1", formatOpNum(opNum),
					"1", seccao, subSec,
					equipe.equals("  ") ? null : equipe,
					ESPACOS_4, ESPACOS_10);

			StringBuilder sb = new StringBuilder(cab);
			sb.append("   C");
			sb.append(formatDate(row[5])); sb.append(formatTime(row[6]));
			sb.append(formatDate(row[7])); sb.append(formatTime(row[8]));
			sb.append("0");
			sb.append(padRight(refComp, 17));
			sb.append(padRight("", 10)); sb.append(padRight("", 10)); sb.append(padRight("", 10));
			sb.append(padZeroLeft(indNumCse, 9));
			sb.append(padZeroLeft(nclRang != null ? nclRang : "", 5));
			sb.append(padRight(row[10], 17));
			sb.append(padRight(row[11], 10)); sb.append(padRight(row[12], 10));
			sb.append(padRight(row[13], 10));
			sb.append(padZeroLeft(row[14], 9));
			sb.append("1");
			// Quantite (11 inteiros + 4 decimais sem virgula + "  ")
			sb.append(formatQuantidadeC(row[20]));
			sb.append("+");
			sb.append(padRight(row[15], 4));
			sb.append(padRight("", 15));
			sb.append(padRight(row[16], 10)); sb.append(padRight(row[17], 10));
			sb.append(padRight(row[18], 35));
			sb.append(padZeroLeft(row[21], 9));
			sb.append(padRight(row[0], 10));
			sb.append(padZeroLeft(row[19], 9));
			sb.append(padRight("", 40));
			sb.append(CRLF);
			data.append(sb);
		}

		if (data.length() > 0) {
			if (!ficheirosdownload) criar_ficheiro(data.toString(), path, pathErro, false, "");
			else escreverZip(data.toString(), nomeFicheiro, nomezip);
		}
	}

	private static String formatQuantidadeC(Object value) {
		if (value == null) return "000000000000000  ";
		try {
			String[] p = String.format("%.3f", Double.parseDouble(value.toString())).replace(",", ".").replace("$", ".").split("\\.");
			String intPart = padZeroLeft(p[0], 11);
			String decPart = p.length > 1 ? (p[1] + "0000").substring(0, 4) : "0000";
			return (intPart + decPart + "  ").substring(0, 17);
		} catch (NumberFormatException e) {
			return "000000000000000  ";
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Escrita de ficheiros
	// ─────────────────────────────────────────────────────────────────────────

	private void escreverLocal(String conteudo, String path) throws IOException {
		File f = new File(path);
		f.createNewFile();
		try (FileWriter fw = new FileWriter(f, true); BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(conteudo);
		}
	}

	private void escreverZip(String conteudo, String nomeFicheiro, String nomezip) throws IOException {
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");
		java.nio.file.Path zipPath = Paths.get("c:/sgiid/temp_files/" + nomezip + ".zip");
		URI uri = URI.create("jar:" + zipPath.toUri());
		try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
			java.nio.file.Path nf = fs.getPath(nomeFicheiro);
			try (Writer w = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
				w.write(conteudo);
			}
		}
	}

	public static boolean isFileExists(File file) {
		return file.exists() && !file.isDirectory();
	}

	public void criar_ficheiro(String conteudo, String path, String pathErro, boolean isErro, String msg) {
		File f = new File(path);
		if (!isFileExists(f)) {
			try { f.createNewFile(); }
			catch (IOException e) {
				notificarErro(e.getMessage() + " " + f.getAbsolutePath());
				if (!isErro) criar_ficheiro(conteudo, pathErro, pathErro, true, e.getMessage());
				return;
			}
		}
		try (FileWriter fw = new FileWriter(f, true); BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(conteudo);
		} catch (IOException e) {
			notificarErro(e.getMessage() + " " + f.getAbsolutePath());
		}
		if (isErro) notificarErro(msg);
	}

	public void criarfileerro(String estado, String path, String conteudo, boolean houvAlteracoes) {
		boolean deve = (!"M".equals(estado) && !"P".equals(estado))
				|| ("M".equals(estado) && houvAlteracoes);
		if (!deve) return;
		File f = new File(path);
		try { f.createNewFile(); } catch (IOException ignored) {}
		try (FileWriter fw = new FileWriter(f, true); BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(conteudo);
		} catch (IOException e) {
			LOG.warning("Erro criarfileerro: " + e.getMessage());
		}
	}

	public void criar_ficheiro_Pausa(String conteudo, String path2, Integer count,
			boolean ficheirosdownload, String nomeFicheiro, String nomezip, String pathErro) throws IOException {
		if (!ficheirosdownload) {
			File f = new File(path2 + "_" + count + ".txt");
			try { f.createNewFile(); }
			catch (IOException e) {
				notificarErro(e.getMessage() + " " + f.getAbsolutePath());
				criar_ficheiro_Pausa(conteudo, pathErro, count, false, nomeFicheiro, nomezip, pathErro);
				return;
			}
			try (FileWriter fw = new FileWriter(f, true); BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write(conteudo);
			}
		} else {
			escreverZip(conteudo, nomeFicheiro + "_" + count + ".txt", nomezip);
		}
	}

	public void criar_ficheiro_PausaMAQUINA(Object[] pausa, String sinal, String linhaInicial,
			String linhaAMaquina, String path2, Boolean ficheirosdownload, String nomeFicheiro2,
			String nomezip, Integer count, String estado, String pathErro, String id)
			throws ParseException, IOException {

		SimpleDateFormat fmtOut = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat fmtIn  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

		StringBuilder rb = new StringBuilder("B");
		rb.append(pausa[0] != null ? fmtOut.format(fmtIn.parse(pausa[0].toString())) : "");
		rb.append(pausa[1] != null ? fmtOut.format(fmtIn.parse(pausa[1].toString())) : "");
		rb.append(padRight(pausa[3], 4)).append("3");
		rb.append("P".equals(pausa[4] != null ? pausa[4].toString() : "") ? formatQuantidade(pausa[2], 15) : ZEROS_15);
		rb.append(sinal).append("3");
		rb.append("E".equals(pausa[4] != null ? pausa[4].toString() : "") ? formatQuantidade(pausa[2], 15) : ZEROS_15);
		rb.append(sinal).append(padRight("", 39)).append(CRLF);

		String conteudo = linhaAMaquina + linhaInicial + rb.toString();
		String seq = sequencia(id);
		StringBuilder resultado = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new StringReader(conteudo))) {
			String line;
			while ((line = br.readLine()) != null) {
				StringBuffer buf = new StringBuffer(line);
				if (buf.length() > 27) buf.replace(18, 27, seq);
				resultado.append(buf).append(CRLF);
			}
		}
		float dur = pausa[2] != null ? Float.parseFloat(pausa[2].toString()) : 0f;
		if (dur > 0) {
			criar_ficheiro_Pausa(resultado.toString(), path2 + "_MAQ_" + estado,
					count, ficheirosdownload, nomeFicheiro2 + "_MAQ_" + estado, nomezip, pathErro);
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// CRIAPAUSASMAQUINA e getTempos — T-SQL com cursor (mantido verbatim).
	// NOTA: DATA_INI/HORA_INI sao nomes de colunas SQL (nao valores), por isso
	//       nao podem ser parametrizados. ID_OF_CAB e um Integer validado.
	// ─────────────────────────────────────────────────────────────────────────



	// ─────────────────────────────────────────────────────────────────────────
	// sequencia, atualizatabela_AUX, verficaEventos, utilitarios
	// ─────────────────────────────────────────────────────────────────────────

	public String sequencia(String id) {
		@SuppressWarnings("unchecked")
		List<Object> rows = entityManager.createNativeQuery(
				"SELECT TOP 1 NUMERO_SEQUENCIA FROM GER_SEQUENCIA_FICHEIRO"
				+ " WHERE DATA_SEQUENCIA = CONVERT(date, GETDATE())")
				.getResultList();

		if (!rows.isEmpty()) {
			int val = Integer.parseInt(rows.get(0).toString()) + 1;
			entityManager.createNativeQuery(
					"UPDATE GER_SEQUENCIA_FICHEIRO SET NUMERO_SEQUENCIA = :v"
					+ " WHERE DATA_SEQUENCIA = CONVERT(date, GETDATE())")
					.setParameter("v", val).executeUpdate();
			String s = "000000000" + val + id;
			return s.substring(s.length() - 9);
		}

		entityManager.createNativeQuery(
				"INSERT INTO GER_SEQUENCIA_FICHEIRO (DATA_SEQUENCIA, NUMERO_SEQUENCIA) VALUES (GETDATE(), 1)")
				.executeUpdate();
		String s = "000000001" + id;
		return s.substring(s.length() - 9);
	}

	public void atualizatabela_AUX(String rescod, String datdeb, String proref, String ofnum,
			String opecod, Integer idOfCab, String tipo, String heudeb) {
		entityManager.createNativeQuery(
				"BEGIN IF NOT EXISTS (SELECT * FROM RP_AUX_OPNUM"
				+ " WHERE RESCOD = :rescod AND PROREF = :proref AND OFNUM = :ofnum"
				+ " AND OPECOD = :opecod AND ID_CAMPO = :idCampo AND TIPO = :tipo)"
				+ " BEGIN INSERT INTO RP_AUX_OPNUM"
				+ "   (RESCOD, DATDEB, PROREF, OFNUM, OPECOD, DATA_CRIACAO, DATA_MODIFICACAO, ID_CAMPO, ESTADO, TIPO, HEUDEB)"
				+ "   VALUES (:rescod, :datdeb, :proref, :ofnum, :opecod, GETDATE(), GETDATE(), :idCampo, 0, :tipo, :heudeb)"
				+ " END END")
				.setParameter("rescod",  rescod)
				.setParameter("datdeb",  datdeb)
				.setParameter("proref",  proref)
				.setParameter("ofnum",   ofnum)
				.setParameter("opecod",  opecod)
				.setParameter("idCampo", idOfCab)
				.setParameter("tipo",    tipo)
				.setParameter("heudeb",  heudeb)
				.executeUpdate();
	}

	public void verficaEventos(String[] keyValuePairs, String momento, String filePath, String para) {
		@SuppressWarnings("unchecked")
		List<GER_EVENTOS_CONF> confs = entityManager.createQuery(
				"SELECT a FROM GER_EVENTOS_CONF a WHERE a.MODULO = 4 AND a.MOMENTO = :m"
				+ " AND a.PAGINA = 'INTERNO' AND a.ESTADO != 0", GER_EVENTOS_CONF.class)
				.setParameter("m", momento).getResultList();

		for (GER_EVENTOS_CONF conf : confs) {
			String mensagem = conf.getEMAIL_MENSAGEM();
			String assunto  = conf.getEMAIL_ASSUNTO();
			for (String par : keyValuePairs) {
				String[] e = par.split("::");
				String chave = e[0].trim(), valor = e.length > 1 ? e[1].trim() : "";
				mensagem = mensagem.replace("{" + chave + "}", valor);
				assunto  = assunto.replace("{" + chave + "}", valor);
			}
			String emailPara = concatenateWithComma(para, conf.getEMAIL_PARA());
			new SendEmail().enviarEmail("alertas.it.doureca@gmail.com", emailPara, assunto, mensagem, null);
		}
	}

	private void notificarErro(String mensagem) {
		verficaEventos(new String[]{"TEXTO_ERRO ::" + mensagem}, "ERROS REGISTOS PRODUCAO", "", null);
	}

	public static String concatenateWithComma(String... strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			if (s != null && !s.isEmpty()) {
				if (sb.length() > 0) sb.append(",");
				sb.append(s);
			}
		}
		return sb.toString();
	}

	private String getURL() {
		@SuppressWarnings("unchecked")
		List<Object[]> rows = entityManager
				.createNativeQuery("SELECT TOP 1 * FROM GER_PARAMETROS").getResultList();
		return rows.isEmpty() || rows.get(0)[2] == null ? "" : rows.get(0)[2].toString();
	}



	// CRIAPAUSASMAQUINA e getTempos mantidos verbatim (T-SQL com cursor)

	public void CRIAPAUSASMAQUINA(String DATA_INI, String HORA_INI, String DATA_FIM, String HORA_FIM,
			String MOMENTO_PARAGEM, String TIPO_PARAGEM, String SINAL, String linha_inicial, String linha_A_MAQUINA,
			String path2, Boolean ficheirosdownload, String nome_ficheiro2, String nomezip, Integer ID_OF_CAB,
			String ESTADO, String path_error) {

		Query query2 = entityManager.createNativeQuery("declare @parents table " + "(Data_inicio datetime, "
				+ "Data_fim datetime, " + "ID int) " + "DECLARE @ID_UTZ_CRIA NVARCHAR(6) "
				+ "DECLARE @ESTADO NVARCHAR(6) = '" + ESTADO + "' " + "DECLARE @Data_inicio datetime  "
				+ "DECLARE @Data_fim datetime " + "DECLARE @Data_fim2 datetime " + "DECLARE @ID INT "
				+ "DECLARE @ID2 INT " + "DECLARE @ID_RESULTADO INT " + "DECLARE @COUNT INT = 1 "
				+ "DECLARE @COUNT1 INT = 0 " + "DECLARE @TOTAL INT = 0 " + "DECLARE @ID_OF_CAB INT = " + ID_OF_CAB + " "
				+ "DECLARE @getid CURSOR " + "DECLARE @getid2 CURSOR " + "SET @getid = CURSOR FOR SELECT ID_PARA_LIN  "
				+ "FROM  RP_OF_PARA_LIN  "
				+ "where ID_OP_CAB in (select  ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB)  " + "and "
				+ MOMENTO_PARAGEM + " = @ESTADO " + "and  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)) <> (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) "
				+ "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) " + "OPEN @getid "
				+ "FETCH NEXT " + "FROM @getid INTO @ID " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN  " + "SET @COUNT1= 0 "
				+ "SELECT @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)),@ID_UTZ_CRIA = ID_UTZ_CRIA  " + ",@Data_fim =  (cast(" + DATA_FIM
				+ " as datetime) + cast(" + HORA_FIM + " as datetime)) "
				+ "FROM  RP_OF_PARA_LIN where ID_PARA_LIN = @ID " + "IF @ESTADO = 'E' " + "BEGIN "
				+ "select @TOTAL = count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  "
				+ "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI
				+ " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM
				+ " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI
				+ " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null	)) " + "and(cast(a." + DATA_FIM
				+ " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE "
				+ "BEGIN " + "select @TOTAL =  count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB "
				+ "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI
				+ "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END "
				+ "WHILE (@COUNT1  = 0) " + "BEGIN " + "IF EXISTS (SELECT * FROM RP_OF_PARA_LIN where  " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL "
				+ "BEGIN " + "SET @getid2 = CURSOR FOR (SELECT ID_PARA_LIN FROM RP_OF_PARA_LIN where  " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio  "
				+ "and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) "
				+ "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) "
				+ "OPEN @getid2 " + "FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN "
				+ "SELECT TOP 1 @ID_RESULTADO=ID_PARA_LIN, @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast("
				+ HORA_INI + " as datetime)), " + "@Data_fim =  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM
				+ " as datetime)), @ID_UTZ_CRIA = ID_UTZ_CRIA  " + "FROM  RP_OF_PARA_LIN  "
				+ "where ID_PARA_LIN = @ID2 " + "IF @ESTADO = 'E' " + "BEGIN "
				+ "select @TOTAL = count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  "
				+ "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI
				+ " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM
				+ " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI
				+ " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null	)) " + "and(cast(a." + DATA_FIM
				+ " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE "
				+ "BEGIN " + "select @TOTAL =  count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB "
				+ "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI
				+ "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END "
				+ "SET @COUNT= @COUNT+1 " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime))), "
				+ "@Data_fim2 = (select MIN((cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)))  "
				+ "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI
				+ " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  "
				+ "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio "
				+ "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID "
				+ "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 "
				+ "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID_RESULTADO)	 "
				+ "set @COUNT = 1	 " + "IF(@Data_fim2 is not null) SET @COUNT = @TOTAL " + "END "
				+ "IF not EXISTS (SELECT * FROM RP_OF_PARA_LIN where  +" + "(cast( " + DATA_INI
				+ " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and + (cast( " + DATA_INI
				+ " as datetime) + cast(" + HORA_INI
				+ " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL "
				+ "BEGIN SET @COUNT= 1 END " + "FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "END " + "END " + "ELSE "
				+ "BEGIN " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast(" + DATA_FIM
				+ " as datetime) + cast(" + HORA_FIM + " as datetime))), " + "@Data_fim2 = (select MIN((cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)))  "
				+ "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI
				+ " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  "
				+ "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio "
				+ "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID "
				+ "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 SET @ID_RESULTADO=@ID "
				+ "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID)	 "
				+ "set @COUNT = 1	 " + "END " + "SET @COUNT1= @COUNT1+1 " + "END " + "END " + "FETCH NEXT "
				+ "FROM @getid INTO @ID " + "set @COUNT = 1	 "
				+ "END select a.Data_inicio,a.Data_fim , cast((DATEDIFF(second,a.Data_inicio, a.Data_fim)/3600.00) as decimal(18,4)) as timediff, b.TIPO_PARAGEM_M2 ,b."
				+ MOMENTO_PARAGEM + " from @parents a inner join RP_OF_PARA_LIN b on a.ID = b.ID_PARA_LIN");

		List<Object[]> dados2 = query2.getResultList();

		Integer count = 0;
		for (Object[] content2 : dados2) {
			count++;
			try {
				criar_ficheiro_PausaMAQUINA(content2, SINAL, linha_inicial, linha_A_MAQUINA, path2, ficheirosdownload,
						nome_ficheiro2, nomezip, count, ESTADO, path_error, ID_OF_CAB.toString());
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}
		}

	}


	public double getTempos(String DATA_INI, String HORA_INI, String DATA_FIM, String HORA_FIM, String MOMENTO_PARAGEM,
			Integer ID_OF_CAB, String ESTADO) {
		double number = 0;
		Query query2 = entityManager.createNativeQuery("declare @parents table " + "(Data_inicio datetime, "
				+ "Data_fim datetime, " + "ID int) " + "DECLARE @ID_UTZ_CRIA NVARCHAR(6) "
				+ "DECLARE @ESTADO NVARCHAR(6) = '" + ESTADO + "' " + "DECLARE @Data_inicio datetime  "
				+ "DECLARE @Data_fim datetime " + "DECLARE @Data_fim2 datetime " + "DECLARE @ID INT "
				+ "DECLARE @ID2 INT " + "DECLARE @ID_RESULTADO INT " + "DECLARE @COUNT INT = 1 "
				+ "DECLARE @COUNT1 INT = 0 " + "DECLARE @TOTAL INT = 0 " + "DECLARE @ID_OF_CAB INT = " + ID_OF_CAB + " "
				+ "DECLARE @getid CURSOR " + "DECLARE @getid2 CURSOR " + "SET @getid = CURSOR FOR SELECT ID_PARA_LIN  "
				+ "FROM  RP_OF_PARA_LIN  "
				+ "where ID_OP_CAB in (select  ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB)  " + "and "
				+ MOMENTO_PARAGEM + " = @ESTADO " + "and  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)) <> (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) "
				+ "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) " + "OPEN @getid "
				+ "FETCH NEXT " + "FROM @getid INTO @ID " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN  " + "SET @COUNT1= 0 "
				+ "SELECT @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)),@ID_UTZ_CRIA = ID_UTZ_CRIA  " + ",@Data_fim =  (cast(" + DATA_FIM
				+ " as datetime) + cast(" + HORA_FIM + " as datetime)) "
				+ "FROM  RP_OF_PARA_LIN where ID_PARA_LIN = @ID " + "IF @ESTADO = 'E' " + "BEGIN "
				+ "select @TOTAL = count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  "
				+ "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI
				+ " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM
				+ " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI
				+ " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null	)) " + "and(cast(a." + DATA_FIM
				+ " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE "
				+ "BEGIN " + "select @TOTAL =  count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB "
				+ "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI
				+ "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END "
				+ "WHILE (@COUNT1  = 0) " + "BEGIN " + "IF EXISTS (SELECT * FROM RP_OF_PARA_LIN where  " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL "
				+ "BEGIN " + "SET @getid2 = CURSOR FOR (SELECT ID_PARA_LIN FROM RP_OF_PARA_LIN where  " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio  "
				+ "and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) "
				+ "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) "
				+ "OPEN @getid2 " + "FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN "
				+ "SELECT TOP 1 @ID_RESULTADO=ID_PARA_LIN, @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast("
				+ HORA_INI + " as datetime)), " + "@Data_fim =  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM
				+ " as datetime)), @ID_UTZ_CRIA = ID_UTZ_CRIA  " + "FROM  RP_OF_PARA_LIN  "
				+ "where ID_PARA_LIN = @ID2 " + "IF @ESTADO = 'E' " + "BEGIN "
				+ "select @TOTAL = count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  "
				+ "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI
				+ " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM
				+ " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI
				+ " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null	)) " + "and(cast(a." + DATA_FIM
				+ " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE "
				+ "BEGIN " + "select @TOTAL =  count(*) from RP_OF_OP_FUNC a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
				+ "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB "
				+ "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM
				+ " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI
				+ "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END "
				+ "SET @COUNT= @COUNT+1 " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime))), "
				+ "@Data_fim2 = (select MIN((cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI
				+ " as datetime)))  "
				+ "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI
				+ " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  "
				+ "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio "
				+ "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID "
				+ "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 "
				+ "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID_RESULTADO)	 "
				+ "set @COUNT = 1	 " + "IF(@Data_fim2 is not null) SET @COUNT = @TOTAL " + "END "
				+ "IF not EXISTS (SELECT * FROM RP_OF_PARA_LIN where  +" + "(cast( " + DATA_INI
				+ " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and + (cast( " + DATA_INI
				+ " as datetime) + cast(" + HORA_INI
				+ " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL "
				+ "BEGIN SET @COUNT= 1 END" + " FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "END " + "END " + "ELSE "
				+ "BEGIN " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast(" + DATA_FIM
				+ " as datetime) + cast(" + HORA_FIM + " as datetime))), " + "@Data_fim2 = (select MIN((cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)))  "
				+ "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast("
				+ DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI
				+ " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  "
				+ "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast("
				+ DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio "
				+ "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID "
				+ "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 "
				+ "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID_RESULTADO)	 "
				+ "set @COUNT = 1	 " + "END " + "SET @COUNT1= @COUNT1+1 " + "END " + "END " + "FETCH NEXT "
				+ "FROM @getid INTO @ID " + "set @COUNT = 1	 " + "END "
				+ "IF @ESTADO = 'P' BEGIN select (cast((DATEDIFF(second, (cast(" + DATA_INI + " as datetime) + cast("
				+ HORA_INI + " as datetime)),  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM
				+ " as datetime)))/3600.00) as decimal(18,4)) - "
				+ "(select  COALESCE(SUM( cast((DATEDIFF(second,a.Data_inicio, a.Data_fim)/3600.00) as decimal(18,4))),0) from @parents a inner join RP_OF_PARA_LIN b on a.ID = b.ID_PARA_LIN ) ),'' as dfs "
				+ "from RP_OF_PREP_LIN where ID_OP_CAB in (select TOP 1 ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) END ELSE BEGIN "
				+ "select (cast((DATEDIFF(second, (cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI
				+ " as datetime)),  (cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM
				+ " as datetime)))/3600.00) as decimal(18,4)) - "
				+ "( select COALESCE( SUM(  cast((DATEDIFF(second,a.Data_inicio, a.Data_fim)/3600.00) as decimal(18,4))),0) from @parents a inner join RP_OF_PARA_LIN b on a.ID = b.ID_PARA_LIN "
				+ ") ) - COALESCE((cast((DATEDIFF(second, (cast(b." + DATA_INI + " as datetime) + cast(b." + HORA_INI
				+ " as datetime)),  (cast(b." + DATA_FIM + " as datetime) + cast(b." + HORA_FIM
				+ " as datetime)))/3600.00) as decimal(18,4))),0) "
				+ ",'' as df from RP_OF_OP_FUNC a left join RP_OF_PREP_LIN b on  a.ID_OP_CAB = b.ID_OP_CAB where a.ID_OP_CAB in (select TOP 1 ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) END");

		List<Object[]> dados2 = query2.getResultList();

		Integer count = 0;
		for (Object[] content2 : dados2) {
			count++;
			number = (content2[0] != null) ? Double.parseDouble(content2[0].toString()) : 0;
		}
		if (number < 0)
			number = 0;
		return number;
	}

}