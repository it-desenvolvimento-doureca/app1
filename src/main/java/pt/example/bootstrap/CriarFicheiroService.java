package pt.example.bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.example.entity.EMAIL;
import pt.example.entity.GER_EVENTOS_CONF;

@Stateless
public class CriarFicheiroService {

	@PersistenceContext(unitName = "persistenceUnit")
	protected EntityManager entityManager;

	private List<Object[]> verificaPecasRecuperacaoInternal(Integer id) {
		@SuppressWarnings("unchecked")
		List<Object[]> dados = entityManager.createNativeQuery("select count(*) total, null text from RP_OF_CAB a "
				+ "inner join RP_CONF_OP_RECUPERACAO_PECAS b on a.OP_COD_ORIGEM = b.ID_OP "
				+ "where a.ID_OF_CAB = :id")
				.setParameter("id", id).getResultList();
		return dados;
	}
	public void criarFicheiro(Integer id, Integer ficheiro, String nome_ficheiro, String tipo, String of,
			Integer id_origem, Integer id_etiqueta, String estado, String nome_ficheiro2, String OP_NUM,
			String ID_OP_LIN, Boolean cria_pausa, Integer total, Boolean ficheirosdownload, String nomezip,
			String novaetiqueta, String estado2, Boolean manual, String ip_posto) throws IOException, ParseException {

		// Verificar se é trabalho do Muro (campo ETIQUETA não nulo e não vazio)
		Boolean isMuro = false;
		Query queryMuro = entityManager.createNativeQuery("select ETIQUETA from RP_OF_CAB where ID_OF_CAB = ?1")
				.setParameter(1, id_origem);
		List<?> dadosMuro = queryMuro.getResultList();
		if (!dadosMuro.isEmpty() && dadosMuro.get(0) != null) {
			String etiquetaMuro = dadosMuro.get(0).toString().trim();
			isMuro = !etiquetaMuro.isEmpty();
		}

		// Variável para acumular pausas quando for Muro
		String pausasMuro = "";

		if (tipo == "COMP") {
			Query query_matrix = entityManager.createNativeQuery("select a.ID_OF_CAB,a.OF_NUM from RP_OF_CAB a "
					+ "inner join DOC_DIC_POSTOS b on a.IP_POSTO = b.IP_POSTO "
					+ "inner join PR_DIC_MAQUINAS_MATRIX c on b.ID_MAQUINA = b.ID_MAQUINA "
					+ "where a.ID_OF_CAB = :id and b.TIPO_POSTO = 'ETIQUETAS_MATRIX' "
					+ "and a.MAQ_NUM = c.MAQUINA_SILVER").setParameter("id", id_origem);

			List<Object[]> dados_matrix = query_matrix.getResultList();

			if (dados_matrix.size() > 0) {
				return;
			}
		}

		String DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, SINAL, QUANT_BOAS_TOTAL, QUANT_BOAS, QUANT_DEF, TEMPO_PREP_TOTAL,
				TIPO_PARAGEM, MOMENTO_PARAGEM, TEMPO_EXEC_TOTAL = "";

		if (novaetiqueta == null)
			novaetiqueta = "0";
		if (estado.equals("A") || estado2.equals("A")) {
			nome_ficheiro2 = "anulacao_" + nome_ficheiro2;
		}

		Boolean alteracoes = false;

		if (ficheiro == 1) {

			DATA_INI = "DATA_INI_M1";
			HORA_INI = "HORA_INI_M1";
			DATA_FIM = "DATA_FIM_M1";
			HORA_FIM = "HORA_FIM_M1";
			SINAL = "-";
			QUANT_BOAS_TOTAL = "QUANT_BOAS_TOTAL_M1";
			QUANT_BOAS = "QUANT_BOAS_M1";
			QUANT_DEF = "QUANT_DEF_M1";
			TEMPO_PREP_TOTAL = "TEMPO_PREP_TOTAL_M1";
			TEMPO_EXEC_TOTAL = "TEMPO_EXEC_TOTAL_M1";
			TIPO_PARAGEM = "TIPO_PARAGEM_M1";
			MOMENTO_PARAGEM = "MOMENTO_PARAGEM_M1";

		} else {
			if (manual) {
				DATA_INI = "DATA_INI";
				HORA_INI = "HORA_INI";
				DATA_FIM = "DATA_FIM";
				HORA_FIM = "HORA_FIM";
				SINAL = "+";
				QUANT_BOAS_TOTAL = "QUANT_BOAS_TOTAL";
				QUANT_BOAS = "QUANT_BOAS";
				QUANT_DEF = "QUANT_DEF";
				TEMPO_PREP_TOTAL = "TEMPO_PREP_TOTAL";
				TEMPO_EXEC_TOTAL = "TEMPO_EXEC_TOTAL";
				TIPO_PARAGEM = "TIPO_PARAGEM";
				MOMENTO_PARAGEM = "MOMENTO_PARAGEM";
			} else {
				DATA_INI = "DATA_INI_M2";
				HORA_INI = "HORA_INI_M2";
				DATA_FIM = "DATA_FIM_M2";
				HORA_FIM = "HORA_FIM_M2";
				SINAL = "+";
				QUANT_BOAS_TOTAL = "QUANT_BOAS_TOTAL_M2";
				QUANT_BOAS = "QUANT_BOAS_M2";
				QUANT_DEF = "QUANT_DEF_M2";
				TEMPO_PREP_TOTAL = "TEMPO_PREP_TOTAL_M2";
				TEMPO_EXEC_TOTAL = "TEMPO_EXEC_TOTAL_M2";
				TIPO_PARAGEM = "TIPO_PARAGEM_M2";
				MOMENTO_PARAGEM = "MOMENTO_PARAGEM_M2";
			}
		}

		if (estado.equals("A") || estado2.equals("A")) {
			SINAL = "-";
		}

		BufferedWriter bw = null;
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		String data_atual = formate.format(new Date());
		FileWriter fw = null;
		String sequencia = "000000000";
		String path = "";
		String path2 = "";
		String path_error = "";
		String patherro = "";
		String data = "";
		String data_maquina = "";
		boolean existe_maquina = false;
		boolean lider = true;
		boolean atualiza = true;
		boolean primeira_linha = true;
		String data_inicio = "";

		HashMap<String, String> linha_utz = new HashMap<String, String>();
		HashMap<String, String> linha_utz_inicio = new HashMap<String, String>();

		Query query_folder = entityManager.createNativeQuery("select top 1 * from GER_PARAMETROS a");

		List<Object[]> dados_folder = query_folder.getResultList();

		for (Object[] content : dados_folder) {
			path = content[1] + nome_ficheiro;
			path2 = content[1] + nome_ficheiro2;
			patherro = content[17] + nome_ficheiro;
			path_error = content[17] + nome_ficheiro2;
		}

		if (!estado.equals("P"))
			sequencia = sequencia(id.toString());

		try {

			Query query = entityManager
					.createNativeQuery("select a.OF_NUM,c.ID_UTZ_CRIA,a.OP_NUM,a.SEC_NUM,a.MAQ_NUM_ORIG,c." + DATA_INI
							+ ",c." + HORA_INI + ",c." + DATA_FIM + ",c." + HORA_FIM + ", " + "b." + TEMPO_PREP_TOTAL
							+ " as Decimalprep,b." + TEMPO_EXEC_TOTAL + " as Decimalexec "
							+ ",a.OP_PREVISTA,a.OP_COD_ORIGEM, (select ID_TURNO from RP_CONF_TURNO where CAST(c."
							+ HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno, "
							+ "CASE when (c.DATA_INI_M2 != c.DATA_INI_M1 or c.HORA_INI_M1 != c.HORA_INI_M2 or c.DATA_FIM_M2 != c.DATA_FIM_M1 or c.HORA_FIM_M1 != c.HORA_FIM_M2 or "
							+ "b.TEMPO_EXEC_TOTAL_M1 != b.TEMPO_EXEC_TOTAL_M2 or b.TEMPO_PREP_TOTAL_M1 != b.TEMPO_PREP_TOTAL_M2  ) then 1 else 1 END as alterado "
							+ ", (select REF_NUM from RP_OF_OP_LIN where ID_OP_LIN = " + ID_OP_LIN + " ),a.ID_OF_CAB "
							+ " from RP_OF_CAB a "
							+ "inner join RP_OF_OP_CAB b on  b.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
							+ id_origem + ")"
							+ "inner join RP_OF_OP_FUNC c on c.ID_OP_CAB in  (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
							+ id_origem + ") and b.ID_OP_CAB = c.ID_OP_CAB " + "where a.ID_OF_CAB = " + id);

			List<Object[]> dados = query.getResultList();

			for (Object[] content : dados) {
				String data_A = "";
				// System.out.println(content[0]);
				data_A += "01        ";// Soci�t�
				data_A += content[5].toString().replaceAll("-", ""); // Date
																		// suivi
				data_A += sequencia; // N� s�quence

				if (novaetiqueta.equals("1")) {
					data_A += (content[12] + "    ").substring(0, 4);
				} else {
					if (content[11].toString().equals("1") || estado.equals("M") || estado.equals("A")
							|| estado2.equals("A")) {
						data_A += "    ";// + Ligne de production
					} else {
						data_A += (content[12] + "    ").substring(0, 4);// +
																			// Ligne
																			// de
																			// production
					}
				}

				data_A += "1";// Type N� OF
				data_A += (of + "         ").substring(0, 10); // N� OF

				if ((estado.equals("A") || (estado.equals("M"))) && !novaetiqueta.equals("1")) {
					data_A += "1";// Type op�ration
				} else {
					data_A += content[11];// Type op�ration
				}

				// OP_NUM
				if (estado.equals("C")) {
					if (content[11].toString().equals("1") && !OP_NUM.equals("NULL")) {
						data_A += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
								("0000" + OP_NUM).length()); // N� Op�ration
					} else {
						data_A += ("    ").substring(0, 4);// N� Op�ration
					}
				} else {
					if (!OP_NUM.equals("NULL")) {
						data_A += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
								("0000" + OP_NUM).length()); // N�
																// Op�ration
					} else {
						data_A += ("    ").substring(0, 4);// N� Op�ration
					}
				}

				// if (!content[4].toString().equals("000")) {
				if (content[4].toString().equals("000")) {
					if (primeira_linha) {
						data_A += "1";// Position ( S12 )
						primeira_linha = false;
					} else {
						data_A += "2";// Position ( S12 )
					}
				} else {
					data_A += "2";// Position ( S12 )
				}

				data_A += (content[3] + "         ").substring(0, 10);// Code
																		// section
				data_A += (content[4] + "         ").substring(0, 10); // Code
																		// sous-section
				if (content[13] != null) {
					data_A += content[13]; // N� d'�quipe
				} else {
					data_A += "01";
				}

				// Type de ressource
				data_A += ("MO" + "         ").substring(0, 4);

				// Code ressource
				data_A += (content[1] + "         ").substring(0, 10);

				data_A += "   A"; // N� �tablissement + Type d'�l�ment A
				if (content[5] == null || content[6] == null || content[7] == null || content[8] == null) {
					LOGGER.warning("DATA_INI/FIM null para id_origem=" + id_origem + " - race condition, ficheiro nao gerado");
					continue;
				}
				data_A += content[5].toString().replaceAll("-", ""); // Date debut
				data_A += content[6].toString().replace(":", "").substring(0, 6); // Heure debut
				data_A += content[7].toString().replaceAll("-", ""); // Date fin
				data_A += content[8].toString().replace(":", "").substring(0, 6); // Heure fin
				data_A += "04002"; // Nombre de postes + Origine temps pr�pa.

				// Temps de pr�paration

				String temp_pre = "000000000000000";
				double number = 0;
				if (content[9] != null) {
					String[] parts = content[9].toString().split(":");
					if (!parts[0].equals("aN")) {
						number = Double.parseDouble(parts[0]) + (Double.parseDouble(parts[1]) / 60)
								+ (Double.parseDouble(parts[2]) / 3600);
						number = number / total;
						number = (number > 0) ? number : 0;
						String parts_prep = String.format("%.4f", number).replace(",", "");
						String size = temp_pre + parts_prep.replace("$", "");
						temp_pre = (size).substring(size.length() - 15, size.length());
					}
				}

				if (tipo.equals("PF") && !estado.equals("M") && !estado.equals("P")) {
					data_A += temp_pre;

				} else if (tipo.equals("PF") && content[14].toString().equals("1") && !estado.equals("P")) {
					data_A += temp_pre;
					alteracoes = true;
				} else {
					data_A += "000000000000000";
				}

				data_A += SINAL; // Signe
				data_A += "22"; // Arr�ts compris + Origine temps ex�cution

				// Temps d'ex�cution
				String temp_exec = "000000000000000";
				double number2 = 0;
				if (content[10] != null) {
					String[] parts2 = content[10].toString().split(":");
					if (!parts2[0].equals("aN")) {
						number2 = Double.parseDouble(parts2[0]) + (Double.parseDouble(parts2[1]) / 60)
								+ (Double.parseDouble(parts2[2]) / 3600);
						number2 = number2 / total;
						number2 = (number2 > 0) ? number2 : 0;
						String parts_exec = String.format("%.4f", number2).replace(",", "");
						String size = temp_exec + parts_exec.replace("$", "");
						temp_exec = (size).substring(size.length() - 15, size.length());
					}
				}
				if (tipo.equals("PF") && !estado.equals("M") && !estado.equals("P")) {
					data_A += temp_exec;
				} else if (tipo.equals("PF") && content[14].toString().equals("1") && !estado.equals("P")) {
					data_A += temp_exec;
				} else {
					data_A += "000000000000000";
				}

				data_A += SINAL; // Signe
				data_A += "22         \r\n"; // Arr�ts compris + Etat op�ration
												// +
												// N�lot V�rif
				if (lider) {
					if (!content[4].toString().equals("000")) {
						// System.out.println(content[4]);
						existe_maquina = true;
						StringBuffer buf = new StringBuffer(data_A);
						buf.replace(70, 84, "              ");
						buf.replace(47, 48, "1");

						String temp_exec2 = "000000000000000";
						String temp_pre2 = "000000000000000";
						double tempprep = getTempos(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, id_origem,
								"P");
						double tempexec = getTempos(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, id_origem,
								"E");

						// tempprep = number - tempprep;

						String parts_exec = String.format("%.4f", tempprep).replace(",", "");
						String size = temp_exec2 + parts_exec.replace("$", "");
						temp_exec2 = (size).substring(size.length() - 15, size.length());

						// tempexec = number2 - tempexec;

						String parts_prep = String.format("%.4f", tempexec).replace(",", "");
						String size2 = temp_pre2 + parts_prep.replace("$", "");
						temp_pre2 = (size2).substring(size2.length() - 15, size2.length());

						buf.replace(121, 136, temp_exec2);
						buf.replace(139, 154, temp_pre2);
						data_maquina = buf.toString();
						lider = false;
					}
					data_inicio = data_A.substring(0, 87);

					if (content[11].toString().equals("2") && (estado.equals("C") || estado.equals("M")) && atualiza
							&& ficheiro != 1) {
						Integer id_t = id_etiqueta;
						String tipo_t = "C";
						if (id_etiqueta == null) {
							id_t = id;
							tipo_t = "PF";
						}
						atualizatabela_AUX(content[1].toString(), content[5].toString(), content[15].toString(), of,
								content[12].toString(), id_t, tipo_t, content[6].toString());
						atualiza = false;

					}
				}

				data += data_A;
				// if (estado.equals("P")) {
				// Preencher linha_utz quando cria_pausa=true OU quando for Muro (para usar nas
				// pausas)
				if (cria_pausa || isMuro) {
					linha_utz.put(content[1].toString(), data_A);
					linha_utz_inicio.put(content[1].toString(), data_A.substring(0, 87));
				}

			}

			if (existe_maquina && tipo.equals("PF") /* && !estado.equals("P") */) {
				data_maquina += data;
				data = data_maquina;
			}

			// PAUSA
			// Para Muro: processar pausas quando estado != "P" (quando o ficheiro vai ser
			// escrito)
			// Para não-Muro: processar apenas quando cria_pausa=true (comportamento
			// original)
			if (cria_pausa || (isMuro && !estado.equals("P"))) {
				Boolean criou_PAUSA = false;
				Query query2 = entityManager.createNativeQuery("select c." + DATA_INI + ",c." + HORA_INI + ",c."
						+ DATA_FIM + ",c." + HORA_FIM + ", " + "cast((DATEDIFF(second,DATEADD(DAY, DATEDIFF(DAY, c."
						+ HORA_INI + ", c." + DATA_INI + " ), CAST(c." + HORA_INI
						+ " AS DATETIME)), DATEADD(DAY, DATEDIFF(DAY, c." + HORA_FIM + ", c." + DATA_FIM + " ), CAST(c."
						+ HORA_FIM + " AS DATETIME)))/3600.00) as decimal(18,4)) as timediff, " + "c." + TIPO_PARAGEM
						+ ",c." + MOMENTO_PARAGEM + ",c.ID_UTZ_CRIA as utz1,a.ID_UTZ_CRIA as utz2, "
						+ "CASE when (c.MOMENTO_PARAGEM_M2 != c.MOMENTO_PARAGEM_M1 or c.TIPO_PARAGEM_M2 != c.TIPO_PARAGEM_M1 or c.DATA_INI_M2 != c.DATA_INI_M1 or c.HORA_INI_M1 != c.HORA_INI_M2 or c.DATA_FIM_M2 != c.DATA_FIM_M1 or c.HORA_FIM_M1 != c.HORA_FIM_M2 ) then 1 else 1 END as alterado, "
						+ "CASE when (c.DATA_INI_M1 is null or c.HORA_INI_M1 is null or c.DATA_FIM_M1 is null or c.HORA_FIM_M1 is null ) then 1 else 0 END as novo "
						+ "from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
						+ "inner join RP_OF_PARA_LIN c on c.ID_OP_CAB = b.ID_OP_CAB " + "where a.ID_OF_CAB = " + id
						+ " and  c." + DATA_INI + " is not null and  c." + DATA_FIM + " is not null ");

				List<Object[]> dados2 = query2.getResultList();

				Integer count = 0;
				for (Object[] content2 : dados2) {
					count++;
					String data_pausa = "";
					String data_pausa_p = "";
					data_pausa += "B"; // Type d'�l�ment B
					data_pausa += ((content2[0] != null) ? content2[0] : "").toString().replaceAll("-", ""); // Date
					// d�but
					data_pausa += ((content2[1] != null) ? content2[1] : "      ").toString().replace(":", "")
							.substring(0, 6); // Heure
					// d�but
					data_pausa += ((content2[2] != null) ? content2[2] : "").toString().replaceAll("-", ""); // Date
					// fin
					data_pausa += ((content2[3] != null) ? content2[3] : "      ").toString().replace(":", "")
							.substring(0, 6); // Heure
					// fin

					data_pausa += (content2[5] + "    ").substring(0, 4);// Code
																			// section

					data_pausa += "3"; // Origine arr�t pr�pa.

					// Temps d'arr�t/pr�pa.

					String temp_pre = "000000000000000";
					if (content2[6] != null && content2[6].toString().equals("P")) {
						String parts_prep = (((content2[4] != null) ? content2[4] : "").toString()).replace(".", "");
						String size = temp_pre + parts_prep;
						temp_pre = (size).substring(size.length() - 15, size.length());
					}
					data_pausa += temp_pre;
					data_pausa += SINAL; // Signe
					data_pausa += "3"; // Origine arr�t ex�cution

					// Temps d'arr�t/ex�cution
					String temp_exec = "000000000000000";
					if (content2[6] != null && content2[6].toString().equals("E")) {
						String parts_exec = ((content2[4] != null) ? content2[4] : "").toString().replace(".", "");
						String size = temp_exec + parts_exec;
						temp_exec = (size).substring(size.length() - 15, size.length());
					}
					data_pausa += temp_exec;
					data_pausa += SINAL; // Signe
					data_pausa += "                                       \r\n"; // Texte
																					// libre

					StringBuffer buf3 = new StringBuffer(linha_utz.get(content2[7].toString()));
					String seq = sequencia(id.toString());

					if (!isMuro) {
						buf3.replace(18, 27, seq);
					}

					String linha3 = buf3.toString();
					if (!existe_maquina) {
						if (isMuro) {
						} else {
							data_pausa_p += linha3;
						}
					}

					String linha_A_MAQUINA = "";
					if (existe_maquina) {
						// linha A MAQ e PESSOAS
						BufferedReader bufReader = new BufferedReader(new StringReader(data_maquina));

						String line = null;
						while ((line = bufReader.readLine()) != null) {

							StringBuffer buf6 = new StringBuffer(line);
							if (!isMuro)
								buf6.replace(18, 27, seq);
							buf6.replace(121, 136, "000000000000000");
							buf6.replace(139, 154, "000000000000000");
							String linha6 = buf6.toString();
							if (buf6.substring(74, 84).trim().equals(content2[7].toString())
									|| buf6.substring(74, 84).equals("          ")) {
								data_pausa_p += linha6 + "\r\n";
							}
							if (buf6.lastIndexOf("MO") == -1) {
								linha_A_MAQUINA = linha6 + "\r\n";
							}
						}

					}

					if (existe_maquina && tipo.equals("PF")
							&& (content2[7].toString().equals(content2[8].toString()))) {
						StringBuffer buf2 = new StringBuffer(data_maquina);
						if (!isMuro)
							buf2.replace(18, 27, seq);
						String linha2 = buf2.toString();
						// data_pausa_p += linha2.substring(0, 87) + data_pausa;

						if (!criou_PAUSA) {
							/*
							 * Integer totalprep = 0; Integer totalexecucao = 0;
							 * 
							 * Query querytotal = entityManager
							 * .createNativeQuery("select  (select count(*) from RP_OF_OP_FUNC a " +
							 * "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " +
							 * "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " +
							 * "where ID_OF_CAB = " + id + " and c." + DATA_INI + " is not null and c." +
							 * DATA_FIM +
							 * " is not null ) as totalprep, (select count(*) from RP_OF_OP_FUNC a " +
							 * "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " +
							 * "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " +
							 * "where ID_OF_CAB = " + id + " and " + " ((cast(a." + DATA_FIM +
							 * " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM
							 * + " as datetime) + cast(c." + HORA_FIM + " as datetime))" +
							 * "or c.HORA_INI_M2 is null)) as totalexecucao ");
							 * 
							 * List<Object[]> dadostotal = querytotal.getResultList();
							 * 
							 * for (Object[] contenttotal : dadostotal) { totalexecucao =
							 * Integer.parseInt(contenttotal[1].toString()); totalprep =
							 * Integer.parseInt(contenttotal[0].toString()); }
							 */

							// criar pausas maquina estado preparacao

							// if (totalprep > 0)

							if (!estado2.equals("M")
									|| (estado2.equals("M") /*
															 * && content2[9].toString(). equals("1")
															 */)) {

								CRIAPAUSASMAQUINA(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, TIPO_PARAGEM,
										SINAL, linha2.substring(0, 87), linha_A_MAQUINA, path2, ficheirosdownload,
										nome_ficheiro2, nomezip, id, "P", path_error);

								// criar pausas maquina estado execucao
								// if (totalexecucao > 0)
								CRIAPAUSASMAQUINA(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, TIPO_PARAGEM,
										SINAL, linha2.substring(0, 87), linha_A_MAQUINA, path2, ficheirosdownload,
										nome_ficheiro2, nomezip, id, "E", path_error);

								criou_PAUSA = true;
							}
						}

					}

					StringBuffer buf = new StringBuffer(linha_utz_inicio.get(content2[7].toString()));
					if (!isMuro)
						buf.replace(18, 27, seq);
					String linha = buf.toString();
					data_pausa_p += linha + data_pausa;

					// Verificar se deve criar ficheiro de pausa
					boolean deveCriarPausa = false;
					if (estado2.equals("M") && content2[9].toString().equals("1")
							&& !content2[10].toString().equals("1") && Float.parseFloat(content2[4].toString()) > 0) {
						deveCriarPausa = true;
					} else if (estado2.equals("M") && content2[10].toString().equals("1") && ficheiro == 2
							&& ((content2[4] != null) ? Float.parseFloat(content2[4].toString()) : 0) > 0) {
						deveCriarPausa = true;
					} else if (!estado2.equals("M") && Float.parseFloat(content2[4].toString()) > 0) {
						deveCriarPausa = true;
					}

					if (deveCriarPausa) {
						if (isMuro) {
							// Se for Muro, acumula as pausas para concatenar ao ficheiro normal
							pausasMuro += data_pausa_p;
						} else {
							// Caso contrário, cria ficheiro separado
							criar_ficheiro_Pausa(data_pausa_p, path2, count, ficheirosdownload, nome_ficheiro2, nomezip,
									path_error);
						}
					}

				}
			}

			// Se for Muro, concatenar as pausas ao ficheiro normal
			if (isMuro && !pausasMuro.isEmpty()) {
				data += pausasMuro;
			}

			if (!estado.equals("P") && !estado.equals("M")) {
				String data3 = "";

				// data3 = " ,CASE when (c.QUANT_BOAS_TOTAL_M1 != c.QUANT_BOAS_TOTAL_M2 or
				// e.QUANT_BOAS_M1 != e.QUANT_BOAS_M2 ) then 1 else 0 END as alterado ";
				data3 = " ,1 as alterado ";

				Query query3 = entityManager.createNativeQuery(
						"Select a.ID_OF_CAB_ORIGEM,a.OF_NUM,e.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,d."
								+ DATA_INI + ",d." + HORA_INI + ",d." + DATA_FIM + ",d." + HORA_FIM
								+ ",d.ID_UTZ_CRIA,c.REF_IND,cast(c." + QUANT_BOAS_TOTAL
								+ " as decimal(18,4)) as qtd1,cast(e." + QUANT_BOAS + " as decimal(18,4)) as qtd2 "
								+ ", a.OP_PREVISTA, c.OBS_REF, (select ID_TURNO from RP_CONF_TURNO where CAST( d."
								+ HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno, a.OP_COD_ORIGEM "
								+ data3 + " from RP_OF_CAB a "
								+ "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
								+ "inner join RP_OF_OP_LIN c on  c.ID_OP_LIN = " + ID_OP_LIN + " "
								+ "inner join RP_OF_OP_FUNC d on d.ID_OP_CAB = b.ID_OP_CAB and d.ID_OP_CAB in (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
								+ id_origem + " ) " + "left join RP_OF_OP_ETIQUETA e on e.ID_OP_LIN = c.ID_OP_LIN "
								+ "where a.ID_OF_CAB = " + id
								+ " and (a.OF_NUM is not null or e.OF_NUM_ORIGEM is not null) ");

				Query query3_COMP = entityManager.createNativeQuery(
						"Select a.ID_OF_CAB_ORIGEM,a.OF_NUM,e.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,d."
								+ DATA_INI + ",d." + HORA_INI + ",d." + DATA_FIM + ",d." + HORA_FIM
								+ ",d.ID_UTZ_CRIA,c.REF_IND,cast(c." + QUANT_BOAS_TOTAL
								+ " as decimal(18,4)) as qtd1,cast(e." + QUANT_BOAS + " as decimal(18,4)) as qtd2 "
								+ ", a.OP_PREVISTA, c.OBS_REF, (select ID_TURNO from RP_CONF_TURNO where CAST( d."
								+ HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno,a.OP_COD_ORIGEM "
								+ data3 + " from RP_OF_CAB a "
								+ "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
								+ "inner join RP_OF_OP_LIN c on  b.ID_OP_CAB = c.ID_OP_CAB "
								+ "inner join RP_OF_OP_FUNC d on d.ID_OP_CAB = (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
								+ id_origem + " ) " + "left join RP_OF_OP_ETIQUETA e on e.ID_OP_LIN = c.ID_OP_LIN "
								+ "where a.ID_OF_CAB = " + id
								+ " and (a.OF_NUM is not null or e.OF_NUM_ORIGEM is not null)  and e.ID_REF_ETIQUETA ="
								+ id_etiqueta);

				List<Object[]> dados3;

				if (tipo.equals("COMP")) {
					dados3 = query3_COMP.getResultList();
				} else {
					dados3 = query3.getResultList();
				}

				for (Object[] content3 : dados3) {
					// alteracoes = true;
					String data_quantidades = "";

					data_quantidades += "01        ";// Soci�t�
					data_quantidades += content3[10].toString().replaceAll("-", "");
					// Date suivi

					data_quantidades += sequencia; // N� s�quence

					if (novaetiqueta.equals("1")) {
						data_quantidades += (content3[21] + "    ").substring(0, 4);
					} else {
						if (content3[18].toString().equals("1") || estado.equals("M") || estado.equals("A")
								|| estado2.equals("A")) {
							data_quantidades += "    ";// + Ligne de production
						} else {
							data_quantidades += (content3[21] + "    ").substring(0, 4);// +
																						// Ligne
																						// de
							// production
						}
					}
					data_quantidades += "1";// Type N� OF

					if (content3[0] == null) {
						data_quantidades += (content3[1] + "         ").substring(0, 10); // N�
																							// OF
					} else {
						data_quantidades += (content3[2] + "         ").substring(0, 10); // N�
																							// OF
					}

					if ((estado.equals("A") || estado.equals("M")) && !novaetiqueta.equals("1")) {
						data_quantidades += "1";// Type op�ration
					} else {
						data_quantidades += content3[18];// Type op�ration
					}

					// OP_NUM
					if (estado.equals("C")) {
						if (content3[18].toString().equals("1") && !OP_NUM.equals("NULL")) {
							data_quantidades += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
									("0000" + OP_NUM).length()); // N� Op�ration
						} else {
							data_quantidades += ("    ").substring(0, 4);// N�
																			// Op�ration
						}
					} else {
						if (!OP_NUM.equals("NULL")) {
							data_quantidades += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
									("0000" + OP_NUM).length()); // N� Op�ration
						} else {
							data_quantidades += ("    ").substring(0, 4);// N�
																			// Op�ration
						}
					}

					data_quantidades += "1";// Position ( S12 )

					data_quantidades += (content3[9] + "         ").substring(0, 10);// Code
					// section
					data_quantidades += (content3[8] + "         ").substring(0, 10); // Code
					// sous-section

					if (content3[20] != null) {
						data_quantidades += content3[20]; // N� d'�quipe
					} else {
						data_quantidades += "01";
					}

					// Type de ressource
					if (content3[0] == null) {
						if (content3[8].toString().equals("000")) {
							data_quantidades += ("MO" + "         ").substring(0, 4);
						} else {
							data_quantidades += "    ";
						}
					} else {
						data_quantidades += ("MO" + "         ").substring(0, 4);
					}

					// Code ressource
					if (content3[0] == null) {
						if (content3[8].toString().equals("000")) {
							data_quantidades += (content3[14] + "         ").substring(0, 10);
						} else {
							data_quantidades += "          ";
						}
					} else {
						data_quantidades += (content3[14] + "         ").substring(0, 10);
					}

					data_quantidades += "   Q"; // N� �tablissement + Type
												// d'�l�ment
												// Q

					data_quantidades += content3[10].toString().replaceAll("-", ""); // Date
																						// d�but
					data_quantidades += content3[11].toString().replace(":", "").substring(0, 6); // Heure
					// d�but
					data_quantidades += content3[12].toString().replaceAll("-", ""); // Date
					// fin
					data_quantidades += content3[13].toString().replace(":", "").substring(0, 6); // Heure
					// fin

					// R�f�rence produit
					data_quantidades += (content3[4] + "                 ").substring(0, 17);
					// Variante (1)
					data_quantidades += (((content3[5] != null) ? content3[5] : "") + "                 ").substring(0,
							10);
					// Variante (2)
					data_quantidades += (((content3[6] != null) ? content3[6] : "") + "                 ").substring(0,
							10);
					// Indice produit
					data_quantidades += (((content3[15] != null) ? content3[15] : "") + "                 ")
							.substring(0, 10);
					// N� enreg. Produit
					if (content3[7] != null) {
						data_quantidades += ("000000000" + content3[7]).substring(
								("000000000" + content3[7]).length() - 9, ("000000000" + content3[7]).length());
					} else {
						data_quantidades += "000000000";
					}

					data_quantidades += "1";// PType quantit�

					// Quantit� bonne
					String quantidades = "000000000000000";

					if (content3[0] == null) {
						if (content3[16] != null) {
							String parts = content3[16].toString().replace(".", "");
							String size = quantidades + parts;
							quantidades = (size).substring(size.length() - 15, size.length());
						}
					} else {
						if (content3[17] != null) {
							String parts = content3[17].toString().replace(".", "");
							String size = quantidades + parts;
							quantidades = (size).substring(size.length() - 15, size.length());
						}

					}

					if (estado.equals("M")) {
						if (content3[22].toString().equals("0")) {
							quantidades = "000000000000000";
							data_quantidades += quantidades + "  ";
						} else {
							data_quantidades += quantidades + "  ";
							alteracoes = true;
						}

					} else {
						data_quantidades += quantidades + "  ";
					}

					data_quantidades += SINAL; // Signe
					data_quantidades += "    "; // Unit�
					data_quantidades += "000000000000000"; // Qt� bonne (US2)
					// N� d'�tiquette suivie
					data_quantidades += "          ";
					// N� enreg. �tiquette
					data_quantidades += "         ";
					// Lieu (entr�e )
					data_quantidades += "          ";
					// + Emplacement ( entr�e )
					// data_quantidades += " ";
					// R�f�rence du lot ( entr�e )
					data_quantidades += "          ";
					if (!tipo.equals("COMP")) {
						data_quantidades += (of + "                                   ").substring(0, 35);
					} else {
						data_quantidades += (/* content3[2] + */"                                   ").substring(0, 35);
					}
					data_quantidades += "          ";
					// N� d'�tiquette ( entr�e )
					// data_quantidades += " ";
					// +Texte libre
					// String obs = (content3[19] != null) ?
					// content3[19].toString() : "";
					String obs = "";
					obs += id_origem;
					if (!tipo.equals("COMP") && ip_posto != null) {
						String nomeimpressora = "";
						String ipimpressora = "";
						Boolean imprime = true;

						Query query_impressora = entityManager.createNativeQuery(
								"select top 1  NOME_IMPRESSORA_SILVER,IP_IMPRESSORA from GER_POSTOS b where IP_POSTO ='"
										+ ip_posto + "'");
						List<Object[]> dados_impressora = query_impressora.getResultList();
						for (Object[] content2 : dados_impressora) {
							if (content2[0] != null)
								nomeimpressora = content2[0].toString();
							if (content2[1] != null) {
								ipimpressora = content2[1].toString();
							}
							imprime = true;
						}
						if (imprime)
							obs += "@" + nomeimpressora;
					}
					data_quantidades += (obs + "                                         ").substring(0, 40);

					String etiquetas = "";
					if (!tipo.equals("COMP")) {
						Query query_caixa = entityManager
								.createNativeQuery("select ETQNUM,REF_NUM from RP_CAIXAS_INCOMPLETAS where ID_OF_CAB = "
										+ id_origem + " and REF_NUM = '" + content3[4] + "'");
						List<Object[]> dados_caixas = query_caixa.getResultList();
						for (Object[] contentcax : dados_caixas) {
							etiquetas += contentcax[0] + ";";
						}

						// etiquetas =
						// "1234567890;1234567890;1234567890;1234567890;1234567890;";
					}
					data_quantidades += (etiquetas + "                                                      ")
							.substring(0, 54);
					data_quantidades += "\r\n";
					data += data_quantidades;
					/*
					 * StringBuffer buf = new StringBuffer(data_quantidades); buf.replace(70, 84,
					 * "              "); data_maquina = buf.toString(); data += data_maquina;
					 */

				}
			} else if (estado.equals("M") && !tipo.equals("COMP")) {
				data += crialinhareferencia(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, QUANT_BOAS_TOTAL, QUANT_BOAS,
						QUANT_DEF, ID_OP_LIN, id_origem, id, sequencia, OP_NUM, estado, estado2, novaetiqueta, SINAL,
						tipo, of);
			}

			if (!estado.equals("P")) {

				String data4 = "";

				if (estado.equals("M")) {
					if (ficheiro == 1) {
						if (tipo.equals("COMP")) {
							// data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M1 != 0 or
							// f.APAGADO = 1)";
						} else {
							// data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M1 != 0) ";
						}

					} else {
						// data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M2 != 0) ";
					}

				}
				Boolean pecasRecuperacao = false;
				if (tipo.equals("COMP")) {
					List<Object[]> resultado = verificaPecasRecuperacaoInternal(id_origem);
					if (resultado != null && !resultado.isEmpty()) {
						Object[] linha = resultado.get(0);
						Number count = (Number) linha[0];
						if (count != null && count.intValue() > 0) {
							pecasRecuperacao = true;
						}
					}
				}

				if (pecasRecuperacao) {
					return;
				}

				if (pecasRecuperacao) {
					data4 = " AND (CAST(d.QUANT_DEF_ORIGINAL AS decimal(18,4)) <> (CAST(d.QUANT_DEF_ORIGINAL AS decimal(18,4)) - CAST(d."
							+ QUANT_DEF + " AS decimal(18,4))))";
				}

				Query query4 = entityManager.createNativeQuery("Select d.COD_DEF,cast(d." + QUANT_DEF
						+ " as decimal(18,4)),a.ID_OF_CAB_ORIGEM,a.OF_NUM,f.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,e."
						+ DATA_INI + ",e." + HORA_INI + ",e." + DATA_FIM + ",e." + HORA_FIM + ", "
						+ "d.ID_UTZ_CRIA,c.REF_IND,c." + QUANT_BOAS_TOTAL + ",f." + QUANT_BOAS + " ,d.OBS_DEF "
						+ ", a.OP_PREVISTA , (select ID_TURNO from RP_CONF_TURNO where CAST( e." + HORA_INI
						+ "  as time) between HORA_INICIO and HORA_FIM ) as turno,a.OP_COD_ORIGEM "
						+ "from RP_OF_CAB a " + "inner join RP_OF_OP_LIN c on  c.ID_OP_LIN = " + ID_OP_LIN + " "
						+ "inner join RP_OF_DEF_LIN d on d.ID_OP_LIN = c.ID_OP_LIN "
						+ "inner join RP_OF_OP_FUNC e on e.ID_OP_CAB = (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
						+ id_origem + " ) "
						+ "left join RP_OF_OP_ETIQUETA f on f.ID_OP_LIN = c.ID_OP_LIN and f.ID_REF_ETIQUETA = d.ID_REF_ETIQUETA "
						+ "where a.ID_OF_CAB = " + id + data4 + " order by c.REF_NUM,d.COD_DEF");

				Query query4_COMP = entityManager.createNativeQuery("Select d.COD_DEF,cast(d." + QUANT_DEF
						+ " as decimal(18,4)),a.ID_OF_CAB_ORIGEM,a.OF_NUM,f.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,e."
						+ DATA_INI + ",e." + HORA_INI + ",e." + DATA_FIM + ",e." + HORA_FIM + ", "
						+ "d.ID_UTZ_CRIA,c.REF_IND,c." + QUANT_BOAS_TOTAL + ",f." + QUANT_BOAS + " ,d.OBS_DEF "
						+ ", a.OP_PREVISTA , (select ID_TURNO from RP_CONF_TURNO where CAST( e." + HORA_INI
						+ "  as time) between HORA_INICIO and HORA_FIM ) as turno, a.OP_COD_ORIGEM  "
						+ "from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
						+ "inner join RP_OF_OP_LIN c on  b.ID_OP_CAB = c.ID_OP_CAB "
						+ "inner join RP_OF_DEF_LIN d on d.ID_OP_LIN = c.ID_OP_LIN "
						+ "inner join RP_OF_OP_FUNC e on e.ID_OP_CAB = (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
						+ id_origem + " ) "
						+ "left join RP_OF_OP_ETIQUETA f on f.ID_OP_LIN = c.ID_OP_LIN and f.ID_REF_ETIQUETA = d.ID_REF_ETIQUETA "
						+ "where a.ID_OF_CAB = " + id + data4 + " and d.ID_REF_ETIQUETA = " + id_etiqueta
						+ " order by c.REF_NUM,d.COD_DEF");

				List<Object[]> dados4;

				if (tipo.equals("COMP")) {
					dados4 = query4_COMP.getResultList();
				} else {
					dados4 = query4.getResultList();
				}

				for (Object[] content4 : dados4) {
					alteracoes = true;
					String data_defeitos = "";
					data_defeitos += "01        ";// Soci�t�
					data_defeitos += content4[12].toString().replaceAll("-", ""); // Date
																					// suivi

					data_defeitos += sequencia; // N� s�quence

					if (novaetiqueta.equals("1")) {
						data_defeitos += (content4[23] + "    ").substring(0, 4);
					} else {
						if (content4[21].toString().equals("1") || estado.equals("M") || estado.equals("A")
								|| estado2.equals("A")) {
							data_defeitos += "    ";// + Ligne de production
						} else {
							data_defeitos += (content4[23] + "    ").substring(0, 4);// +
																						// Ligne
																						// de
							// production
						}
					}
					data_defeitos += "1";// Type N� OF

					if (content4[2] == null) {
						data_defeitos += (content4[3] + "         ").substring(0, 10); // N�
																						// OF
					} else {
						data_defeitos += (content4[4] + "         ").substring(0, 10); // N�
																						// OF
					}

					if ((estado.equals("A") || estado.equals("M")) && !novaetiqueta.equals("1")) {
						data_defeitos += "1";// Type op�ration
					} else {
						data_defeitos += content4[21];// Type op�ration
					}

					// OP_NUM
					if (estado.equals("C")) {
						if (content4[21].toString().equals("1") && !OP_NUM.equals("NULL")) {
							data_defeitos += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
									("0000" + OP_NUM).length()); // N� Op�ration
						} else {
							data_defeitos += ("    ").substring(0, 4);// N�
																		// Op�ration
						}
					} else {
						if (!OP_NUM.equals("NULL")) {
							data_defeitos += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
									("0000" + OP_NUM).length()); // N� Op�ration
						} else {
							data_defeitos += ("    ").substring(0, 4);// N�
						}
					}

					data_defeitos += "1";// Position ( S12 )

					data_defeitos += (content4[11] + "         ").substring(0, 10);// Code
					// section
					data_defeitos += (content4[10] + "         ").substring(0, 10); // Code
					// sous-section
					if (content4[22] != null) {
						data_defeitos += content4[22]; // N� d'�quipe
					} else {
						data_defeitos += "01";
					}

					// Type de ressource
					if (content4[2] == null) {
						if (content4[10].toString().equals("000")) {
							data_defeitos += ("MO" + "         ").substring(0, 4);
						} else {
							data_defeitos += "    ";
						}
					} else {
						data_defeitos += ("MO" + "         ").substring(0, 4);
					}

					// Code ressource
					if (content4[2] == null) {
						if (content4[10].toString().equals("000")) {
							data_defeitos += (content4[16] + "         ").substring(0, 10);
						} else {
							data_defeitos += "          ";
						}
					} else {
						data_defeitos += (content4[16] + "         ").substring(0, 10);
					}

					data_defeitos += "   R"; // N� �tablissement + Type
												// d'�l�ment Q

					data_defeitos += content4[12].toString().replaceAll("-", ""); // Date
																					// d�but
					data_defeitos += content4[13].toString().replace(":", "").substring(0, 6); // Heure
					// d�but
					data_defeitos += content4[14].toString().replaceAll("-", ""); // Date
					// fin
					data_defeitos += content4[15].toString().replace(":", "").substring(0, 6); // Heure
					// fin

					// R�f�rence produit
					data_defeitos += (content4[6] + "                 ").substring(0, 17);
					// Variante (1)
					data_defeitos += (((content4[7] != null) ? content4[7] : "") + "                 ").substring(0,
							10);
					// Variante (2)
					data_defeitos += (((content4[8] != null) ? content4[8] : "") + "                 ").substring(0,
							10);
					// Indice produit
					data_defeitos += (((content4[17] != null) ? content4[17] : "") + "                 ").substring(0,
							10);
					// N� enreg. Produit
					if (content4[9] != null) {
						data_defeitos += ("000000000" + content4[9]).substring(("000000000" + content4[9]).length() - 9,
								("000000000" + content4[9]).length());
					} else {
						data_defeitos += "000000000";
					}

					// Code rebut
					data_defeitos += (content4[0] + "    ").substring(0, 4);

					data_defeitos += "1"; // Type quantit�

					// Quantit� rebut�e
					String quantidades = "000000000000000";

					if (content4[1] != null) {
						String parts = content4[1].toString().replace(".", "");
						String size = quantidades + parts;
						quantidades = (size).substring(size.length() - 15, size.length());
					}

					data_defeitos += quantidades + "  ";

					if (pecasRecuperacao) {
						if (SINAL.equals("+")) {
							data_defeitos += "-";
						} else {
							data_defeitos += "+";
						}
					} else {
						data_defeitos += SINAL; // Signe
					}

					data_defeitos += "                                                                                                       ";
					String obs = (content4[20] != null) ? content4[20].toString() : "";

					data_defeitos += (obs + "                                        ").substring(0, 39); // Texte
																											// libre
					data_defeitos += "\r\n";
					data += data_defeitos;
					/*
					 * StringBuffer buf = new StringBuffer(data_defeitos); buf.replace(70, 84,
					 * "              "); data_maquina = buf.toString(); data += data_maquina;
					 */
				}
			}

			/// String data = "Campo1: \r\n" + "Campo2:\r\n" + "Campo3:\r\n" +
			/// "Campo4:";
			if (!ficheirosdownload) {
				if (!estado.equals("M") && !estado.equals("P")) {
					File file = new File(path);

					// if file doesnt exists, then create it

					try {
						file.createNewFile();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						String[] keyValuePairs = {
								"TEXTO_ERRO ::" + e2.getMessage() + " " + file.getAbsolutePath() + "", };
						verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
						criarfileerro(estado, patherro, data, alteracoes);
						e2.printStackTrace();
						return;
					}

					// true = append file
					// fw = new FileWriter(file.getAbsoluteFile(), true);
					try {
						fw = new FileWriter(file.getAbsoluteFile(), true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bw = new BufferedWriter(fw);

					bw.write(data);
				} else if (estado.equals("M") && alteracoes && !estado.equals("P")) {
					File file = new File(path);

					// if file doesnt exists, then create it

					try {
						file.createNewFile();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						String[] keyValuePairs = {
								"TEXTO_ERRO ::" + e2.getMessage() + " " + file.getAbsolutePath() + "", };
						if (file.getAbsolutePath() != null)
							verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
						criarfileerro(estado, patherro, data, alteracoes);
						e2.printStackTrace();
						return;
					}

					// true = append file
					// fw = new FileWriter(file.getAbsoluteFile(), true);
					try {
						fw = new FileWriter(file.getAbsoluteFile(), true);
					} catch (IOException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}
					bw = new BufferedWriter(fw);

					bw.write(data);
				}
			} else {

				// if (!estado.equals("M") && !estado.equals("P")) {
				if (!estado.equals("P")) {
					Map<String, String> env = new HashMap<>();
					env.put("create", "true");
					java.nio.file.Path pathh = Paths.get("c:/sgiid/temp_files/" + nomezip + ".zip");
					URI uri = URI.create("jar:" + pathh.toUri());
					try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
						java.nio.file.Path nf = fs.getPath(nome_ficheiro);
						try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8,
								StandardOpenOption.CREATE)) {
							writer.write(data);
						}
					}
				}
			}

		} catch (IOException e) {
			String[] keyValuePairs = { "TEXTO_ERRO ::" + e.getMessage() + "", };
			verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
			criarfileerro(estado, patherro, data, alteracoes);
			e.printStackTrace();
			return;
		} finally {

			try {

				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}

			} catch (IOException ex) {
				String[] keyValuePairs = { "TEXTO_ERRO ::" + ex.getMessage() + "", };
				verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
				criarfileerro(estado, patherro, data, alteracoes);
				ex.printStackTrace();
				return;
			}
		}
	}

	public String crialinhareferencia(String DATA_INI, String HORA_INI, String DATA_FIM, String HORA_FIM,
			String QUANT_BOAS_TOTAL, String QUANT_BOAS, String QUANT_DEF, String ID_OP_LIN, Integer id_origem,
			Integer id, String sequencia, String OP_NUM, String estado, String estado2, String novaetiqueta,
			String SINAL, String tipo, String of) {
		String data3 = "";

		data3 = " ,1 as alterado ";

		Query query3 = entityManager.createNativeQuery(
				"Select a.ID_OF_CAB_ORIGEM,a.OF_NUM,e.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,d."
						+ DATA_INI + ",d." + HORA_INI + ",d." + DATA_FIM + ",d." + HORA_FIM
						+ ",d.ID_UTZ_CRIA,c.REF_IND,cast(c." + QUANT_BOAS_TOTAL + " as decimal(18,4)) as qtd1,cast(e."
						+ QUANT_BOAS + " as decimal(18,4)) as qtd2 "
						+ ", a.OP_PREVISTA, c.OBS_REF, (select ID_TURNO from RP_CONF_TURNO where CAST( d." + HORA_INI
						+ "  as time) between HORA_INICIO and HORA_FIM ) as turno, a.OP_COD_ORIGEM " + data3
						+ " from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
						+ "inner join RP_OF_OP_LIN c on  c.ID_OP_LIN = " + ID_OP_LIN + " "
						+ "inner join RP_OF_OP_FUNC d on d.ID_OP_CAB = b.ID_OP_CAB and d.ID_OP_CAB in (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
						+ id_origem + " ) " + "left join RP_OF_OP_ETIQUETA e on e.ID_OP_LIN = c.ID_OP_LIN "
						+ "where a.ID_OF_CAB = " + id + " and (a.OF_NUM is not null or e.OF_NUM_ORIGEM is not null) ");

		List<Object[]> dados3;

		dados3 = query3.getResultList();

		String data_quantidades = "";

		for (Object[] content3 : dados3) {
			// alteracoes = true;

			data_quantidades += "01        ";// Soci�t�
			data_quantidades += content3[10].toString().replaceAll("-", "");
			// Date suivi

			data_quantidades += sequencia; // N� s�quence

			if (novaetiqueta.equals("1")) {
				data_quantidades += (content3[21] + "    ").substring(0, 4);
			} else {
				if (content3[18].toString().equals("1") || estado.equals("M") || estado.equals("A")
						|| estado2.equals("A")) {
					data_quantidades += "    ";// + Ligne de production
				} else {
					data_quantidades += (content3[21] + "    ").substring(0, 4);// +
																				// Ligne
																				// de
					// production
				}
			}

			data_quantidades += "1";// Type N� OF

			if (content3[0] == null) {
				data_quantidades += (content3[1] + "         ").substring(0, 10); // N�
																					// OF
			} else {
				data_quantidades += (content3[2] + "         ").substring(0, 10); // N�
																					// OF
			}

			if ((estado.equals("A") || estado.equals("M")) && !novaetiqueta.equals("1")) {
				data_quantidades += "1";// Type op�ration
			} else {
				data_quantidades += content3[18];// Type op�ration
			}

			// OP_NUM
			if (estado.equals("C")) {
				if (content3[18].toString().equals("1") && !OP_NUM.equals("NULL")) {
					data_quantidades += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
							("0000" + OP_NUM).length()); // N� Op�ration
				} else {
					data_quantidades += ("    ").substring(0, 4);// N�
																	// Op�ration
				}
			} else {
				if (!OP_NUM.equals("NULL")) {
					data_quantidades += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4,
							("0000" + OP_NUM).length()); // N� Op�ration
				} else {
					data_quantidades += ("    ").substring(0, 4);// N�
																	// Op�ration
				}
			}

			data_quantidades += "1";// Position ( S12 )

			data_quantidades += (content3[9] + "         ").substring(0, 10);// Code
			// section
			data_quantidades += (content3[8] + "         ").substring(0, 10); // Code
			// sous-section

			if (content3[20] != null) {
				data_quantidades += content3[20]; // N� d'�quipe
			} else {
				data_quantidades += "01";
			}

			// Type de ressource
			if (content3[0] == null) {
				if (content3[8].toString().equals("000")) {
					data_quantidades += ("MO" + "         ").substring(0, 4);
				} else {
					data_quantidades += "    ";
				}
			} else {
				data_quantidades += ("MO" + "         ").substring(0, 4);
			}

			// Code ressource
			if (content3[0] == null) {
				if (content3[8].toString().equals("000")) {
					data_quantidades += (content3[14] + "         ").substring(0, 10);
				} else {
					data_quantidades += "          ";
				}
			} else {
				data_quantidades += (content3[14] + "         ").substring(0, 10);
			}

			data_quantidades += "   Q"; // N� �tablissement + Type
										// d'�l�ment
										// Q

			data_quantidades += content3[10].toString().replaceAll("-", ""); // Date
																				// d�but
			data_quantidades += content3[11].toString().replace(":", "").substring(0, 6); // Heure
			// d�but
			data_quantidades += content3[12].toString().replaceAll("-", ""); // Date
			// fin
			data_quantidades += content3[13].toString().replace(":", "").substring(0, 6); // Heure
			// fin

			// R�f�rence produit
			data_quantidades += (content3[4] + "                 ").substring(0, 17);
			// Variante (1)
			data_quantidades += (((content3[5] != null) ? content3[5] : "") + "                 ").substring(0, 10);
			// Variante (2)
			data_quantidades += (((content3[6] != null) ? content3[6] : "") + "                 ").substring(0, 10);
			// Indice produit
			data_quantidades += (((content3[15] != null) ? content3[15] : "") + "                 ").substring(0, 10);
			// N� enreg. Produit
			if (content3[7] != null) {
				data_quantidades += ("000000000" + content3[7]).substring(("000000000" + content3[7]).length() - 9,
						("000000000" + content3[7]).length());
			} else {
				data_quantidades += "000000000";
			}

			data_quantidades += "1";// PType quantit�

			/*
			 * if (estado.equals("M")) { if (content3[22].toString().equals("0")) {
			 */
			String quantidades = "000000000000000";
			data_quantidades += quantidades + "  ";
			/*
			 * } else { data_quantidades += quantidades + "  "; alteracoes = true; }
			 * 
			 * } else { data_quantidades += quantidades + "  "; }
			 */

			data_quantidades += SINAL; // Signe
			data_quantidades += "    "; // Unit�
			data_quantidades += "000000000000000"; // Qt� bonne (US2)
			// N� d'�tiquette suivie
			data_quantidades += "          ";
			// N� enreg. �tiquette
			data_quantidades += "         ";
			// Lieu (entr�e )
			data_quantidades += "          ";
			// + Emplacement ( entr�e )
			// data_quantidades += " ";
			// R�f�rence du lot ( entr�e )
			data_quantidades += "          ";
			if (!tipo.equals("COMP")) {
				data_quantidades += (of + "                                   ").substring(0, 35);
			} else {
				data_quantidades += (/* content3[2] + */"                                   ").substring(0, 35);
			}
			data_quantidades += "          ";
			// N� d'�tiquette ( entr�e )
			// data_quantidades += " ";
			// +Texte libre
			// String obs = (content3[19] != null) ?
			// content3[19].toString() : "";
			String obs = "";
			obs += id_origem;
			/*
			 * if (!tipo.equals("COMP") && ip_posto != null) { String nomeimpressora = "";
			 * String ipimpressora = ""; Boolean imprime = true;
			 * 
			 * Query query_impressora = entityManager.createNativeQuery(
			 * "select top 1  NOME_IMPRESSORA_SILVER,IP_IMPRESSORA from GER_POSTOS b where IP_POSTO ='"
			 * + ip_posto + "'"); List<Object[]> dados_impressora =
			 * query_impressora.getResultList(); for (Object[] content2 : dados_impressora)
			 * { nomeimpressora = content2[0].toString(); if (content2[1] != null) {
			 * ipimpressora = content2[1].toString(); } imprime = true; } if (imprime) obs
			 * += "@" + nomeimpressora; }
			 */
			data_quantidades += (obs + "                                         ").substring(0, 40);

			String etiquetas = "";
			/*
			 * if (!tipo.equals("COMP")) { Query query_caixa = entityManager
			 * .createNativeQuery("select ETQNUM,REF_NUM from RP_CAIXAS_INCOMPLETAS where ID_OF_CAB = "
			 * + id_origem + " and REF_NUM = '" + content3[4] + "'"); List<Object[]>
			 * dados_caixas = query_caixa.getResultList(); for (Object[] contentcax :
			 * dados_caixas) { etiquetas += contentcax[0] + ";"; }
			 * 
			 * 
			 * }
			 */

			data_quantidades += (etiquetas + "                                                      ").substring(0, 54);
			data_quantidades += "\r\n";

		}
		return data_quantidades;
	}

	public void criarFicheiroConsumo(Integer id_of_cab, Boolean ficheirosdownload, String nomezip,
			Boolean primeira_OPENUM) throws IOException {

		if (primeira_OPENUM == false)
			return;

		Query query_matrix = entityManager.createNativeQuery("select a.ID_OF_CAB,a.OF_NUM from RP_OF_CAB a "
				+ "inner join DOC_DIC_POSTOS b  with(nolock) on a.IP_POSTO = b.IP_POSTO "
				+ "inner join PR_DIC_MAQUINAS_MATRIX c  with(nolock) on b.ID_MAQUINA = b.ID_MAQUINA "
				+ "where a.ID_OF_CAB = :id and b.TIPO_POSTO = 'ETIQUETAS_MATRIX' " + "and a.MAQ_NUM = c.MAQUINA_SILVER")
				.setParameter("id", id_of_cab);

		List<Object[]> dados_matrix = query_matrix.getResultList();

		if (dados_matrix.size() > 0) {
			return;
		}

		java.util.Date datacria = new java.util.Date();
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat horaformate = new SimpleDateFormat("HHmmss");
		String datatual = formate.format(datacria);
		String horatual = horaformate.format(datacria);

		List<HashMap<String, String>> lista = null;
		final ConnectProgress connectionProgress = new ConnectProgress();

		String path = "";
		String path_error = "";

		Query query_folder = entityManager.createNativeQuery(
				"select top 1  PASTA_FICHEIRO,PASTA_ETIQUETAS,MODELO_REPORT,PASTA_DESTINO_ERRO from GER_PARAMETROS a");
		String nome_ficheiro = datatual + horatual + "_ETIQUETA_PRODUCAO_STOCK_ID" + id_of_cab + ".txt";
		List<Object[]> dados_folder = query_folder.getResultList();
		for (Object[] content : dados_folder) {
			path = content[0] + nome_ficheiro;
			path_error = content[3] + nome_ficheiro;
		}

		String sequencia = "000000000";
		String data = "";

		sequencia = sequencia(id_of_cab.toString());
		List<Object[]> dados = null;

		Query query = entityManager.createNativeQuery("DECLARE @ID_OF_CAB int = " + id_of_cab + "; "
				+ "select t.ETQNUM,e.OF_NUM,e.OP_NUM,e.OP_COD_ORIGEM,e.MAQ_NUM_ORIG,m.DATA_INI_M2,m.HORA_INI_M2,m.DATA_FIM_M2,m.HORA_FIM_M2 "
				+ ",(select ID_TURNO from RP_CONF_TURNO WHERE m.HORA_INI_M2 > HORA_INICIO and m.HORA_INI_M2 < HORA_FIM) N_EQUIPA,t.PROREF,t.VA1REF,t.VA2REF,t.INDREF,t.INDNUMENR,t.UNICOD, "
				+ "t.LIECOD,t.EMPCOD,t.ETQORILOT1,t.ETQNUMENR,"
				+ " (d.QUANT_BOAS_M2 + CASE WHEN TIPO_PECA  in ('COM','COMS') THEN 0 ELSE d.QUANT_DEF_M2 END) QUANT,t.LOTNUMENR "
				+ "from RP_OF_CAB a  with(nolock) inner join RP_OF_OP_CAB b  with(nolock) on a.ID_OF_CAB = b.ID_OF_CAB "
				+ "inner join RP_OF_OP_LIN c with(nolock) on b.ID_OP_CAB = c.ID_OP_CAB inner join RP_OF_OP_ETIQUETA d  with(nolock) on c.ID_OP_LIN = d.ID_OP_LIN "
				+ "left join (select ETQNUM,st.PROREF,VA1REF,VA2REF,INDREF,st.INDNUMENR,UNICOD,LIECOD,EMPCOD,ETQORILOT1,ETQNUMENR,sl.LOTNUMENR from SILVER.dbo.SETQDE st  with(nolock) "
				+ "left join  SILVER.dbo.STOLOT sl  with(nolock) on st.INDNUMENR = sl.INDNUMENR and st.ETQORILOT1 = sl.LOTREF "
				+ "where ETQNUM in (select RIGHT ('000'+CAST(td.REF_ETIQUETA as varchar(10)),10) from RP_OF_CAB ta inner join RP_OF_OP_CAB tb  with(nolock) on ta.ID_OF_CAB = tb.ID_OF_CAB "
				+ "inner join RP_OF_OP_LIN tc on tb.ID_OP_CAB = tc.ID_OP_CAB inner join RP_OF_OP_ETIQUETA td   with(nolock)on tc.ID_OP_LIN = td.ID_OP_LIN) "
				+ ") t on RIGHT ('000'+CAST(d.REF_ETIQUETA as varchar(10)),10) = t.ETQNUM "
				+ "left join (select * from RP_OF_CAB  with(nolock) where ID_OF_CAB = @ID_OF_CAB) e on  a.ID_OF_CAB_ORIGEM = e.ID_OF_CAB "
				+ "left join (select g.ID_OF_CAB,h.* from RP_OF_CAB f  with(nolock) inner join RP_OF_OP_CAB g  with(nolock) on f.ID_OF_CAB = g.ID_OF_CAB "
				+ "inner join RP_OF_OP_FUNC h  with(nolock) on g.ID_OP_CAB = h.ID_OP_CAB and f.ID_UTZ_CRIA = h.ID_UTZ_CRIA where f.ID_OF_CAB = @ID_OF_CAB) m on a.ID_OF_CAB_ORIGEM = m.ID_OF_CAB "
				+ "where a.ID_OF_CAB_ORIGEM = @ID_OF_CAB and (d.QUANT_BOAS_M2 + CASE WHEN TIPO_PECA in ('COM','COMS') THEN 0 ELSE d.QUANT_DEF_M2 END ) > 0");

		dados = query.getResultList();

		for (Object[] content : dados) {

			String of = content[1].toString();
			String SECCAO = content[3].toString();
			String SUBSECCAO = content[4].toString();
			String REF_COMPOSTO = "";
			String INDNUMCSE = "";
			String NCLRANG = "";

			try {
				lista = connectionProgress.getOrigineComposant(getURL(), content[10].toString(), of);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (lista.size() > 0) {
				NCLRANG = lista.get(0).get("NCLRANG");
				REF_COMPOSTO = lista.get(0).get("PROREF");
				INDNUMCSE = lista.get(0).get("INDNUMCSE");
			}

			data += "01        ";// Soci�t�
			data += datatual; // Date suivi
			data += sequencia; // N� s�quence

			data += "    ";// + Ligne de production

			data += "1";// Type N� OF
			data += (of + "          ").substring(0, 10); // N� OF

			data += "1";// Type op�ration

			// OP_NUM
			String OP_NUM = (content[2] == null) ? "" : content[2].toString();
			data += ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4, ("0000" + OP_NUM).length());
			// data += ("0010").substring(0, 4);// N� Op�ration

			data += "1";// Position ( S12 )

			// Code section
			data += (SECCAO + "          ").substring(0, 10);

			// Code sous-section
			data += (SUBSECCAO + "          ").substring(0, 10);

			// N� d'�quipe

			String num_equipe = "00";
			if (content[9] != null) {
				String size = num_equipe + content[9];
				num_equipe = (size).substring(size.length() - 2, size.length());
				data += num_equipe;
			} else {
				data += "  ";
			}

			// Type de ressource
			data += ("    ").substring(0, 4);

			// Code ressource
			data += ("          ").substring(0, 10);

			data += "   C"; // N� �tablissement + Type d'�l�ment C

			data += content[5].toString().replaceAll("-", "");

			// Heure d�but
			data += content[6].toString().replace(":", "").substring(0, 6);

			// Date fin
			data += content[7].toString().replaceAll("-", "");

			// Heure fin
			data += content[8].toString().replace(":", "").substring(0, 6);

			// Origine composant

			data += "0";

			// R�f�rence compos�
			data += (REF_COMPOSTO + "                 ").substring(0, 17);

			// Variante compos� (1)
			data += ("          ").substring(0, 10);

			// Variante compos� (2)
			data += ("          ").substring(0, 10);

			// Indice du compos�
			data += ("          ").substring(0, 10);

			// N� enregistrement Cs�
			String enregistrementcse = "000000000";
			String sizecse = enregistrementcse + INDNUMCSE;
			enregistrementcse = (sizecse).substring(sizecse.length() - 9, sizecse.length());
			data += enregistrementcse;

			// N� de rang

			String rang = "00000";
			if (NCLRANG != null) {
				String size = rang + NCLRANG;
				rang = (size).substring(size.length() - 5, size.length());
				data += rang;
			} else {
				data += rang;
			}

			// data += (NCLRANG + " ").substring(0, 5);

			// R�f�rence composant
			data += (content[10] + "                 ").substring(0, 17);

			// Variante composant (1)
			if (content[11] != null) {
				data += (content[11] + "          ").substring(0, 10);
			} else {
				data += "          ";
			}

			// Variante composant (2)
			if (content[12] != null) {
				data += (content[12] + "          ").substring(0, 10);
			} else {
				data += "          ";
			}

			// Indice du composant
			if (content[13] != null) {
				data += (content[13] + "          ").substring(0, 10);
			} else {
				data += "          ";
			}

			// N� enregistrement Cst

			String enregistrement = "000000000";
			if (content[14] != null) {
				String size = enregistrement + content[14];
				enregistrement = (size).substring(size.length() - 9, size.length());
				data += enregistrement;
			} else {
				data += enregistrement;
			}

			// Type quantit�
			data += "1"; // Signe

			// Quantit�
			if (content[20] != null) {
				String result = String.format("%.3f", content[20]).replace("$", ",");
				String[] parts = result.split(",");
				String part1 = "00000000000";
				String part2 = "0000";
				if (parts.length > 0) {
					if (parts[0] != null) {
						String size = part1 + parts[0];
						part1 = (size).substring(size.length() - 11, size.length());
					}
					if (parts.length > 1) {
						String size = parts[1] + part2;
						part2 = (size).substring(0, 4);
					}
				}
				data += (part1 + part2 + "  ").substring(0, 17);
			} else {
				data += "000000000000000  ";
			}

			data += "+"; // Signe

			// Unit�
			if (content[15] != null) {
				data += (content[15] + "    ").substring(0, 4);
			} else {
				data += "    ";
			}

			// Quantit� (US2)
			data += "               ";

			// Lieu origine
			if (content[16] != null) {
				data += (content[16] + "          ").substring(0, 10);
			} else {
				data += "          ";
			}

			// Emplacement origine
			if (content[17] != null) {
				data += (content[17] + "          ").substring(0, 10);
			} else {
				data += "          ";
			}

			// R�f�rence du lot
			if (content[18] != null) {
				data += (content[18] + "                                   ").substring(0, 35);
			} else {
				data += "                                   ";
			}

			// N� de lot interne
			String lotinterne = "000000000";
			if (content[21] != null) {
				String size = lotinterne + content[21];
				lotinterne = (size).substring(size.length() - 9, size.length());
				data += lotinterne;
			} else {
				data += lotinterne;
			}

			// N� d'�tiquette
			if (content[0] != null) {
				data += (content[0] + "          ").substring(0, 10);
			} else {
				data += "          ";
			}

			// N� enreg. �tiquette
			String etiquette = "000000000";
			if (content[19] != null) {
				String size = etiquette + content[19];
				etiquette = (size).substring(size.length() - 9, size.length());
				data += etiquette;
			} else {
				data += etiquette;
			}

			// Texte libre

			data += ("                                        ").substring(0, 40);

			data += "\r\n";
		}

		if (data.length() > 0) {
			if (!ficheirosdownload) {
				criar_ficheiro(data, path, path_error, false, "");
			} else {
				Map<String, String> env = new HashMap<>();
				env.put("create", "true");
				java.nio.file.Path pathh = Paths.get("c:/sgiid/temp_files/" + nomezip + ".zip");
				URI uri = URI.create("jar:" + pathh.toUri());
				try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
					java.nio.file.Path nf = fs.getPath(nome_ficheiro);
					try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8,
							StandardOpenOption.CREATE)) {
						writer.write(data);
					}
				}

			}
		}

	}

	public static boolean isFileExists(File file) {
		return file.exists() && !file.isDirectory();
	}

	public void criar_ficheiro(String data, String path, String path_error, Boolean error, String err) {
		File file2 = new File(path);
		// if (file2.delete())
		// if file doesnt exists, then create it
		if (!isFileExists(file2)) {
			try {
				file2.createNewFile();
			} catch (IOException e2) {
				String[] keyValuePairs = { "TEXTO_ERRO ::" + e2.getMessage() + " " + file2.getAbsolutePath() + "", };
				verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
				e2.printStackTrace();
			}
		}
		BufferedWriter bw2 = null;
		FileWriter fw2 = null;
		// true = append file
		try {
			fw2 = new FileWriter(file2.getAbsoluteFile(), true);
		} catch (IOException e) {

			if (!error)
				criar_ficheiro(data, path_error, path_error, true, e.getMessage() + " " + file2.getAbsolutePath());
			e.printStackTrace();
		}
		if (fw2 != null) {
			bw2 = new BufferedWriter(fw2);
			try {
				bw2.write(data);
				if (bw2 != null) {
					bw2.close();
				}
				if (fw2 != null) {
					fw2.close();
				}
			} catch (IOException e) {
				if (bw2 != null) {
					try {
						bw2.close();
					} catch (IOException e1) {
						String[] keyValuePairs = {
								"TEXTO_ERRO ::" + e1.getMessage() + " " + file2.getAbsolutePath() + "", };
						verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
						e1.printStackTrace();
					}
				}
				if (fw2 != null) {
					try {
						fw2.close();
					} catch (IOException e1) {
						String[] keyValuePairs = {
								"TEXTO_ERRO ::" + e1.getMessage() + " " + file2.getAbsolutePath() + "", };
						verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
			}
		}
		if (error)

		{
			String[] keyValuePairs = { "TEXTO_ERRO ::" + err + "", };
			verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", path_error, null);
		}
	}

	public void criarfileerro(String estado, String path, String data, Boolean alteracoes) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {

			if (!estado.equals("M") && !estado.equals("P")) {
				File file = new File(path);

				// if file doesnt exists, then create it

				try {
					file.createNewFile();
				} catch (IOException e2) {
					// TODO Auto-generated catch block

					e2.printStackTrace();
				}

				// true = append file
				// fw = new FileWriter(file.getAbsoluteFile(), true);
				try {
					fw = new FileWriter(file.getAbsoluteFile(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bw = new BufferedWriter(fw);

				try {
					bw.write(data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (estado.equals("M") && alteracoes && !estado.equals("P")) {
				File file = new File(path);

				// if file doesnt exists, then create it

				try {
					file.createNewFile();
				} catch (IOException e2) {
					// TODO Auto-generated catch block

					e2.printStackTrace();
				}

				// true = append file
				// fw = new FileWriter(file.getAbsoluteFile(), true);
				try {
					fw = new FileWriter(file.getAbsoluteFile(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
				bw = new BufferedWriter(fw);

				try {
					bw.write(data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} finally {

			try {

				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}

			} catch (IOException ex) {
				return;
			}
		}
	}

	public void criar_ficheiro_PausaMAQUINA(Object[] content2, String SINAL, String linha_inicial,
			String linha_A_MAQUINA, String path2, Boolean ficheirosdownload, String nome_ficheiro2, String nomezip,
			Integer count, String estado, String path_error, String id) throws ParseException {

		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

		String data_pausa = "";
		String data_pausa_p = "";
		String data_pausa_p2 = "";
		data_pausa_p += linha_A_MAQUINA;
		data_pausa += "B"; // Type d'�l�ment B

		// Date d�but
		// Heure d�but
		data_pausa += ((content2[0] != null) ? f.format(p.parse(content2[0].toString())) : "").toString();

		// Date fin
		// Heure fin
		data_pausa += ((content2[1] != null) ? f.format(p.parse(content2[1].toString())) : "").toString();

		data_pausa += (content2[3] + "    ").substring(0, 4);// Code
																// section

		data_pausa += "3"; // Origine arr�t pr�pa.

		// Temps d'arr�t/pr�pa.

		String temp_pre = "000000000000000";
		if (content2[4] != null && content2[4].toString().equals("P")) {
			String parts_prep = (((content2[2] != null) ? content2[2] : "").toString()).replace(".", "");
			String size = temp_pre + parts_prep;
			temp_pre = (size).substring(size.length() - 15, size.length());
		}
		data_pausa += temp_pre;
		data_pausa += SINAL; // Signe
		data_pausa += "3"; // Origine arr�t ex�cution

		// Temps d'arr�t/ex�cution
		String temp_exec = "000000000000000";
		if (content2[4] != null && content2[4].toString().equals("E")) {
			String parts_exec = ((content2[2] != null) ? content2[2] : "").toString().replace(".", "");
			String size = temp_exec + parts_exec;
			temp_exec = (size).substring(size.length() - 15, size.length());
		}

		data_pausa += temp_exec;
		data_pausa += SINAL; // Signe
		data_pausa += "                                       \r\n"; // Texte
		// libre

		data_pausa_p += linha_inicial + data_pausa;

		BufferedReader bufReader = new BufferedReader(new StringReader(data_pausa_p));

		String seq = sequencia(id);
		String line = null;
		try {
			while ((line = bufReader.readLine()) != null) {

				StringBuffer buf6 = new StringBuffer(line);
				buf6.replace(18, 27, seq);
				String linha6 = buf6.toString();
				data_pausa_p2 += linha6 + "\r\n";

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Float num = (float) 0;
			if (content2[2] != null) {
				num = Float.parseFloat(content2[2].toString());
			}
			if (num > 0)
				criar_ficheiro_Pausa(data_pausa_p2, path2 + "_MAQ_" + estado, count, ficheirosdownload,
						nome_ficheiro2 + "_MAQ_" + estado, nomezip, path_error);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void criar_ficheiro_Pausa(String data, String path2, Integer count, Boolean ficheirosdownload,
			String nomeficheiro, String nomezip, String path_error) throws IOException {
		if (!ficheirosdownload) {
			File file2 = new File(path2 + "_" + count + ".txt");

			// if file doesnt exists, then create it

			try {
				file2.createNewFile();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				String[] keyValuePairs = { "TEXTO_ERRO ::" + e2.getMessage() + " " + file2.getAbsolutePath() + "", };
				if (file2.getAbsolutePath() != null)
					verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "", null);
				criar_ficheiro_Pausa(data, path_error, count, false, nomeficheiro, nomezip, path_error);
				e2.printStackTrace();
				return;
			}
			BufferedWriter bw2 = null;
			FileWriter fw2 = null;
			// true = append file
			try {
				fw2 = new FileWriter(file2.getAbsoluteFile(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
			bw2 = new BufferedWriter(fw2);
			try {
				bw2.write(data);
				if (bw2 != null) {
					bw2.close();
				}
				if (fw2 != null) {
					fw2.close();
				}
			} catch (IOException e) {
				if (bw2 != null) {
					try {
						bw2.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (fw2 != null) {
					try {
						fw2.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
			}
		} else {
			Map<String, String> env = new HashMap<>();
			env.put("create", "true");
			java.nio.file.Path pathh = Paths.get("c:/sgiid/temp_files/" + nomezip + ".zip");
			URI uri = URI.create("jar:" + pathh.toUri());
			try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
				java.nio.file.Path nf = fs.getPath(nomeficheiro + "_" + count + ".txt");
				try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
					writer.write(data);
				}
			}
		}
	}

	public String sequencia(String id) {
		String sequencia = "000000000";
		Query query_seq = entityManager.createNativeQuery(
				"select top 1 NUMERO_SEQUENCIA,DATA_SEQUENCIA from GER_SEQUENCIA_FICHEIRO where DATA_SEQUENCIA = CONVERT (date, GETDATE())");

		List<Object[]> dados_seq = query_seq.getResultList();
		if (dados_seq.size() > 0) {
			Integer val = 1;
			for (Object[] contentseq : dados_seq) {
				val = Integer.parseInt(contentseq[0].toString()) + 1;
				sequencia = ("000000000" + val + id).substring(("000000000" + val + id).length() - 9,
						("000000000" + val + id).length());
			}
			entityManager.createNativeQuery("UPDATE GER_SEQUENCIA_FICHEIRO SET NUMERO_SEQUENCIA = " + val
					+ " where DATA_SEQUENCIA = CONVERT (date, GETDATE())").executeUpdate();
		} else {
			sequencia = ("000000001" + id).substring(("000000001" + id).length() - 9, ("000000001" + id).length());
			entityManager
					.createNativeQuery(
							"INSERT INTO GER_SEQUENCIA_FICHEIRO (DATA_SEQUENCIA,NUMERO_SEQUENCIA) VALUES (GETDATE(),1)")
					.executeUpdate();
		}
		return sequencia;
	}

	public void atualizatabela_AUX(String RESCOD, String DATDEB, String PROREF, String OFNUM, String OPECOD,
			Integer ID_OF_CAB, String TIPO, String HEUDEB) {
		entityManager.createNativeQuery("BEGIN IF NOT EXISTS  ( SELECT * FROM RP_AUX_OPNUM WHERE RESCOD = " + RESCOD
				+ " /*and DATDEB = '" + DATDEB + "'*/ and PROREF = '" + PROREF + "' and OFNUM = '" + OFNUM
				+ "' and OPECOD = '" + OPECOD + "' and ID_CAMPO = " + ID_OF_CAB + " and TIPO = '" + TIPO
				+ "' /*and HEUDEB = '" + HEUDEB + "'*/)"
				+ "BEGIN INSERT INTO RP_AUX_OPNUM (RESCOD,DATDEB,PROREF,OFNUM,OPECOD,DATA_CRIACAO,DATA_MODIFICACAO,ID_CAMPO,ESTADO,TIPO,HEUDEB) VALUES ("
				+ " '" + RESCOD + "','" + DATDEB + "','" + PROREF + "','" + OFNUM + "','" + OPECOD
				+ "',GETDATE(),GETDATE()," + ID_OF_CAB + ",0,'" + TIPO + "','" + HEUDEB + "') " + "END END")
				.executeUpdate();
	}

	public void verficaEventos(String[] keyValuePairs, String momento, String fgilepath, String para) {

		List<String> x = new ArrayList<>();

		Query query3 = entityManager.createQuery("Select a from GER_EVENTOS_CONF a where MODULO = 4 and MOMENTO = '"
				+ momento + "' " + "and PAGINA = 'INTERNO' and ESTADO  != 0");
		List<GER_EVENTOS_CONF> dados = query3.getResultList();

		for (GER_EVENTOS_CONF borderTypes : dados) {

			// System.out.println(borderTypes.getEMAIL_ASSUNTO());
			EMAIL email = new EMAIL();
			// email.setASSUNTO(borderTypes.getEMAIL_ASSUNTO());
			email.setDE("alertas.it.doureca@gmail.com");

			String s1 = para;
			String s2 = borderTypes.getEMAIL_PARA();
			String email_para = concatenateWithComma(s1, s2);

			email.setPARA(email_para);
			String mensagem = borderTypes.getEMAIL_MENSAGEM();
			String assunto = borderTypes.getEMAIL_ASSUNTO();

			for (String pair : keyValuePairs) {
				String[] entry = pair.split("::");
				mensagem = mensagem.replace("{" + entry[0].trim() + "}", (entry.length > 1) ? entry[1].trim() : "");
				assunto = assunto.replace("{" + entry[0].trim() + "}", (entry.length > 1) ? entry[1].trim() : "");
			}
			email.setASSUNTO(assunto);
			email.setMENSAGEM(mensagem);
			new SendEmail().enviarEmail(email.getDE(), email.getPARA(), email.getASSUNTO(), email.getMENSAGEM(), email.getNOME_FICHEIRO());

		}
	}

	public static String concatenateWithComma(String... strings) {
		StringBuilder sb = new StringBuilder();
		for (String str : strings) {
			if (str != null) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(str);
			}
		}
		return sb.toString();
	}

	private String getURL() {
		String url = "";
		Query query_folder = entityManager.createNativeQuery("select top 1 * from GER_PARAMETROS a");
		List<Object[]> dados_folder = query_folder.getResultList();
		for (Object[] content : dados_folder) {
			url = content[2].toString();
		}
		return url;
	}
}
