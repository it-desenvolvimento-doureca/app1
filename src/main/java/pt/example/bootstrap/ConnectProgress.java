package pt.example.bootstrap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

public class ConnectProgress {

	private static final String QUERY = "select * from SOFA where utimod= 'recep1'";
	public static String querySofaGetAll = "select * from SOFA where utimod='%s'";
	Connection globalconnection = null;

	public static void main(String[] args) throws SQLException {
	}

	private Connection getConnection(String url) throws SQLException {
		try {
			conf pasta = new conf();

			// pasta.teste();
			// the openedge driver string

			// Class.forName("com.ddtek.jdbcx.openedge.OpenEdgeDataSource40");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			// the openedge url
			// String url2 = pasta.filePath;
			// String url =
			// "jdbc:datadirect:openedge://192.168.30.25:20612;DatabaseName=silv-exp;User=SYSPROGRESS;Password=SYSPROGRESS;";
			// get the openedge database connection
			// url =
			// "jdbc:sqlserver://localhost:54447;databaseName=demo;User=admin;Password=sa123;";
			globalconnection = DriverManager.getConnection(url);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			globalconnection.close();
			// System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			globalconnection.close();
			// System.exit(2);
		} finally {
			// connection.close();
		}
		return globalconnection;
	}

	public List<String> getSofas(String url) throws SQLException {
		String query = String.format(ConnectProgress.querySofaGetAll, "recep1");

		List<String> x = new ArrayList<>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				String coffeeName = rs.getString("OFNUM");
				x.add(coffeeName);
			}
			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return x;
	}

	public List<HashMap<String, String>> getOF(String ofnum, String url) throws SQLException {

		String query = "select a.ofnum,a.ofanumenr,a.ofref,b.OFETAT from SOFA a "
				+ " left join SOFB b on a.OFANUMENR = b.OFANUMENR where a.ofnum= '" + ofnum + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				HashMap<String, String> x = new HashMap<>();
				x.put("ofnum", rs.getString("ofnum"));
				x.put("ofanumenr", rs.getString("ofanumenr"));
				x.put("ofref", rs.getString("ofref"));
				x.put("OFETAT", rs.getString("OFETAT"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getOP(String ofanumenr, String url) throws SQLException {

		String query = "select OPECOD,OPENUM,OPEDES,SECNUMENR1 from SOFD where ofanumenr= '" + ofanumenr
				+ "' order by OPENUM";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("OPECOD", rs.getString("OPECOD"));
				x.put("OPENUM", rs.getString("OPENUM"));
				x.put("OPEDES", rs.getString("OPEDES"));
				x.put("SECNUMENR1", rs.getString("SECNUMENR1"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getallOP(String url) throws SQLException {

		String query = "select OPECOD,OPEDES,SECNUMENR1 from SDTOPP";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("OPECOD", rs.getString("OPECOD"));
				x.put("OPEDES", rs.getString("OPEDES"));
				x.put("SECNUMENR1", rs.getString("SECNUMENR1"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getallOPNOTIN(List<String> data, String url) throws SQLException {
		String query = "select OPECOD,OPEDES,SECNUMENR1 from SDTOPP";
		if (data != null) {
			String inString = "";

			for (int i = 0; i < data.size(); i++) {
				if (i == 0)
					inString = inString + "'" + (String) data.get(i) + "'";
				else
					inString = inString + ",'" + (String) data.get(i) + "'";
			}
			query += " where OPECOD not in(" + inString + ")";
		}

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("OPECOD", rs.getString("OPECOD"));
				x.put("OPEDES", rs.getString("OPEDES"));
				x.put("SECNUMENR1", rs.getString("SECNUMENR1"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getallfamNOTIN(List<String> data, String url) throws SQLException {
		String query = "select FAMCOD,FAMLIB from SPAFAM";
		if (data != null) {
			String inString = "";
			for (int i = 0; i < data.size(); i++) {
				if (i == 0)
					inString = inString + "'" + (String) data.get(i) + "'";
				else
					inString = inString + ",'" + (String) data.get(i) + "'";
			}
			query += " where FAMCOD not in(" + inString + ")";
		}

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("FAMCOD", rs.getString("FAMCOD"));
				x.put("FAMLIB", rs.getString("FAMLIB"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getTipoFalta(String url) throws SQLException {

		String query = "select DISTINCT * from SPAARR";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("ARRCOD", rs.getString("ARRCOD"));
				x.put("arrlib", rs.getString("arrlib"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getOPTIPO(String OPECOD, String url) throws SQLException {

		String query = "SELECT a.SECTYP, b.OPECOD, a.SECCOD, a.SSECOD,c.SSEDES FROM SDTSEC a LEFT JOIN SDTOPP b ON b.SECNUMENR1=a.SECNUMENR "
				+ "LEFT JOIN SPASSE c ON a.SSECOD=c.SSECOD WHERE b.OPECOD= '" + OPECOD + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("SECTYP", rs.getString("SECTYP"));
				x.put("OPECOD", rs.getString("OPECOD"));
				x.put("SECCOD", rs.getString("SECCOD"));
				x.put("SSECOD", rs.getString("SSECOD"));
				x.put("SSEDES", rs.getString("SSEDES"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getMaq(String SECNUMENR, String url) throws SQLException {

		String query = "select a.SECCOD, a.ssecod ,c.SECLIB,b.SSEDES,a.SECTYP from SDTSEC a "
				+ "inner join SPASSE b on a.ssecod = b.ssecod " + "inner join SPASEC c on a.seccod = c.seccod "
				+ "where a.SECNUMENR= '" + SECNUMENR + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("SECCOD", rs.getString("SECCOD"));
				x.put("ssecod", rs.getString("ssecod"));
				x.put("SECLIB", rs.getString("SECLIB"));
				x.put("SSEDES", rs.getString("SSEDES"));
				x.put("SECTYP", rs.getString("SECTYP"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getAllMaq(String SECCOD, String url) throws SQLException {

		String query = "select b.ssecod,a.SSEDES from SPASSE a inner join SDTSEC b on a.ssecod = b.ssecod where b.SECCOD= '"
				+ SECCOD + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("ssecod", rs.getString("ssecod"));
				x.put("SSEDES", rs.getString("SSEDES"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getFamilias(String url) throws SQLException {

		String query = "select DISTINCT LEFT(QUACOD,2) as fam from SPAQUA";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("fam", rs.getString("fam"));
				// x.put("SSEDES", rs.getString("SSEDES"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getfilhos(String pai, String url) throws SQLException {

		String query = "select b.PROREF,b.PRODES1,b.PRODES2,a.PROREFCST,b.PRDFAMCOD,c.ZPAVAL,b.GESCOD from SDTNCL a "
				+ "inner join SDTPRA b on a.PROREFCST = b.PROREF  left join SDTZPA c on b.ZPANUM = c.ZPANUM and  c.zpacod='ALER' "
				+ " where a.PROREFCSE ='" + pai + "' and  ( b.PROTYPCOD != 'EMBA' AND b.PROTYPCOD != 'COM')"; // AND b.PROTYPCOD != 'COM'

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("PROREF", rs.getString("PROREF"));
				x.put("PRODES1", rs.getString("PRODES1"));
				x.put("PRODES2", rs.getString("PRODES2"));
				x.put("PROREFCST", rs.getString("PROREFCST"));
				x.put("PRDFAMCOD", rs.getString("PRDFAMCOD"));
				x.put("ZPAVAL", rs.getString("ZPAVAL"));
				x.put("GESCOD", rs.getString("GESCOD"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getfilhosprimeiro(String OFANUMENR, String url) throws SQLException {

		String query = "select b.PROREF,b.PRODES1,b.PRODES2,b.PRDFAMCOD,c.ZPAVAL,b.GESCOD from SOFC  a "
				+ "left join SDTPRA b on a.PROREF = b.PROREF "
				+ "left join SDTZPA c on b.ZPANUM = c.ZPANUM and  c.zpacod='ALER' " + "where a.OFANUMENR = '"
				+ OFANUMENR + "' and  (b.PROTYPCOD != 'EMBA' AND b.PROTYPCOD != 'COM')"; // AND b.PROTYPCOD != 'COM'

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("PROREF", rs.getString("PROREF"));
				x.put("PRODES1", rs.getString("PRODES1"));
				x.put("PRODES2", rs.getString("PRODES2"));
				x.put("PRDFAMCOD", rs.getString("PRDFAMCOD"));
				x.put("ZPAVAL", rs.getString("ZPAVAL"));
				x.put("GESCOD", rs.getString("GESCOD"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getDefeitos(String fam, String url) throws SQLException {

		String query = "select QUACOD,QUALIB from SPAQUA where  LEFT(QUACOD,2)='" + fam + "' and QUATYP != '2'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("QUACOD", rs.getString("QUACOD"));
				x.put("QUALIB", rs.getString("QUALIB"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}
	
	public List<HashMap<String, String>> getDefeitos2(String fam, String url) throws SQLException {

		String query = "select QUACOD,QUALIB from SPAQUA where  LEFT(QUACOD,2) in (" + fam + ") and QUATYP != '2'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("QUACOD", rs.getString("QUACOD"));
				x.put("QUALIB", rs.getString("QUALIB"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getRef(String OFANUMENR, String url) throws SQLException {

		String query = "select a.PROREF, b.PRODES1,b.PRODES2,a.VA1REF, a.VA2REF,a.INDREF,a.OFBQTEINI,a.INDNUMENR,c.FAMCOD,d.ZPAVAL,b.PRDFAMCOD,b.GESCOD "
				+ "from SOFB a " + "inner join SDTPRA b on a.PROREF = b.PROREF  "
				+ "inner join SPAFAM c on b.PRDFAMCOD = c.FAMCOD  "
				+ "left join (select * from SDTZPA f where f.ZPACOD='ALER') d on b.ZPANUM = d.ZPANUM "
				+ "inner join SPAPRT e on b.PROTYPCOD = e.PROTYPCOD " + "where a.OFANUMENR= '" + OFANUMENR
				+ "'  and e.FABCON = 1  and  b.PROTYPCOD != 'COM'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("PROREF", rs.getString("PROREF"));
				x.put("PRODES1", rs.getString("PRODES1"));
				x.put("PRODES2", rs.getString("PRODES2"));
				x.put("VA1REF", rs.getString("VA1REF"));
				x.put("VA2REF", rs.getString("VA2REF"));
				x.put("INDREF", rs.getString("INDREF"));
				x.put("OFBQTEINI", rs.getString("OFBQTEINI"));
				x.put("INDNUMENR", rs.getString("INDNUMENR"));
				x.put("FAMCOD", rs.getString("FAMCOD"));
				x.put("ZPAVAL", rs.getString("ZPAVAL"));
				x.put("PRDFAMCOD", rs.getString("PRDFAMCOD"));
				x.put("GESCOD", rs.getString("GESCOD"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getOPtop1(String ofanumenr, String url) throws SQLException {

		String query = "select * from SOFD a where ofanumenr= '" + ofanumenr
				+ "' and OPECOD != '' order by a.OPENUM desc";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("OPECOD", rs.getString("OPECOD"));
				x.put("OPENUM", rs.getString("OPENUM"));
				x.put("OPEDES", rs.getString("OPEDES"));
				x.put("SECNUMENR1", rs.getString("SECNUMENR1"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			// connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getEtiqueta(String etiqueta, String url) throws SQLException {

		/*
		 * String query =
		 * "select b.OFNUM, b.ofanumenr,ofref,a.ETQEMBQTE,c.INDNUMENR,c.VA1REF,c.VA2REF,c.INDREF,c.PROREF from SETQDE a "
		 * +
		 * "inner join SOFA b on b.ofnum = left(a.etqoridoc1,10) inner join SOFB c on b.ofanumenr = c.ofanumenr"
		 * + " where a.etqnum = '" + etiqueta + "'";
		 */
		String query = "select b.OFNUM, b.ofanumenr,ofref,a.ETQEMBQTE,c.INDNUMENR,c.VA1REF,c.VA2REF,c.INDREF,c.PROREF,b.OFDATFR from SETQDE a "
				+ "LEFT join SOFB c on  c.ofbnumenr = a.orinumenr LEFT join SOFA b on b.ofanumenr = c.ofanumenr "
				+ " where a.etqnum = '" + etiqueta + "'";
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// System.out.println(query);
		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("OFNUM", rs.getString("OFNUM"));
				x.put("ofanumenr", rs.getString("ofanumenr"));
				x.put("ofref", rs.getString("ofref"));
				x.put("ETQEMBQTE", rs.getString("ETQEMBQTE"));
				x.put("INDNUMENR", rs.getString("INDNUMENR"));
				x.put("VA1REF", rs.getString("VA1REF"));
				x.put("VA2REF", rs.getString("VA2REF"));
				x.put("INDREF", rs.getString("INDREF"));
				x.put("PROREF", rs.getString("PROREF"));
				x.put("OFDATFR", rs.getString("OFDATFR"));
				list.add(x);
			}
			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getUsers(String url) throws SQLException {

		String query = "select RESCOD,RESDES from SDTRES  where RESTYPCOD = 'MO' ";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("RESCOD", rs.getString("RESCOD"));
				x.put("RESDES", rs.getString("RESDES"));
				list.add(x);
			}

			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getUser(String RESCOD, String url) throws SQLException {

		String query = "select RESCOD,RESDES from SDTRES  where RESTYPCOD = 'MO' and RESCOD='" + RESCOD + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("RESCOD", rs.getString("RESCOD"));
				x.put("RESDES", rs.getString("RESDES"));
				list.add(x);
			}

			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getSessoes(String url) throws SQLException {

		String query = "select SECCOD,SECLIB from SPASEC  ";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("SECCOD", rs.getString("SECCOD"));
				x.put("SECLIB", rs.getString("SECLIB"));
				list.add(x);
			}

			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public List<HashMap<String, String>> getSeccao(String url) throws SQLException {

		String query = "select SECCOD,SECLIB,SECNUMINT from SPASEC where SECNUMINT is not null and SECNUMINT != ''";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				HashMap<String, String> x = new HashMap<>();
				x.put("SECCOD", rs.getString("SECCOD"));
				x.put("SECLIB", rs.getString("SECLIB"));
				x.put("SECNUMINT", rs.getString("SECNUMINT"));
				list.add(x);
			}

			stmt.close();
			rs.close();
			connection.close();
			// globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return list;
	}

	public String GetOP_NUM(String RESCOD, String DATDEB, String PROREF, String OFNUM, String OPECOD, String url,
			String HEUDEB) throws SQLException {

		/*
		 * String query ="SELECT d.OPENUM	" + "FROM SCPSVQ a	" +
		 * "INNER JOIN SCPSVA b ON a.svanumenr = b.svanumenr	" +
		 * "INNER JOIN SOFA c ON b.svanumenr = c.ofanumenr " +
		 * "INNER JOIN SOFD d ON d.OFANUMENR = c.ofanumenr " +
		 * "INNER JOIN SOFB e ON d.OFANUMENR = e.ofanumenr " +
		 * "WHERE b.rescod ='"+RESCOD+"' AND b.DATDEB='"+DATDEB+"'	" +
		 * "AND e.PROREF ='"+PROREF+"' AND c.OFNUM='"+OFNUM+"' AND d.OPECOD='"
		 * +OPECOD+"'"; String query = "SELECT top 1 b.OPENUM FROM SCPSVA	a "
		 * + "INNER JOIN SOFD b ON a.OFANUMENR = b.OFANUMENR " +
		 * " INNER JOIN SOFA c ON a.ofanumenr = c.ofanumenr " +
		 * "INNER JOIN SOFB e ON c.OFANUMENR = e.ofanumenr " +
		 * "WHERE a.rescod ='"+RESCOD+"' AND a.DATDEB='"
		 * +DATDEB+"'	and a.HEUDEB ='"+HEUDEB.substring(0, 8)+"' " +
		 * " AND e.PROREF ='"+PROREF+"' AND c.OFNUM='"+OFNUM+"' AND b.OPECOD='"
		 * +OPECOD+"' order by b.OPENUM desc";
		 */
		String query = "SELECT SOFD.OPENUM FROM SCPSVA " + "INNER JOIN SOFD ON SCPSVA.ofdnumenr = SOFD.ofdnumenr "
				+ "INNER JOIN SOFA ON SOFD.ofanumenr = SOFA.ofanumenr "
				+ "INNER JOIN SOFB ON SOFB.ofanumenr = SOFA.ofanumenr " + "WHERE SCPSVA.rescod = '" + RESCOD
				+ "' AND SCPSVA.datdeb = '" + DATDEB + "' AND SCPSVA.heudeb = '" + HEUDEB.substring(0, 8)
				+ "' AND SOFB.proref = '" + PROREF + "' " + "AND SOFA.ofnum = '" + OFNUM + "' AND SOFD.opecod = '"
				+ OPECOD + "' ";
		String val = null;

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				val = rs.getString("OPENUM");
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return val;
	}

	public String verificaOF(String RESCOD, String DATDEB, String OFNUM, String OPECOD, String url, String HEUDEB)
			throws SQLException {

		String query = "SELECT SOFD.OPENUM FROM SCPSVA " + "INNER JOIN SOFD ON SCPSVA.ofdnumenr = SOFD.ofdnumenr "
				+ "INNER JOIN SOFA ON SOFD.ofanumenr = SOFA.ofanumenr " + "WHERE SCPSVA.rescod = '" + RESCOD
				+ "' AND SCPSVA.datdeb = '" + DATDEB + "' AND SCPSVA.heudeb = '" + HEUDEB.substring(0, 8)
				+ "' AND SOFA.ofnum = '" + OFNUM + "' AND SOFD.opecod = '" + OPECOD + "' ";
		String val = null;
		// System.out.println(query);
		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das operações
				val = rs.getString("OPENUM");
			}
			stmt.close();
			rs.close();
			connection.close();
			globalconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			globalconnection.close();
		}
		return val;
	}

}