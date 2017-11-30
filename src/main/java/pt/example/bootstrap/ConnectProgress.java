package pt.example.bootstrap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
			
			//Class.forName("com.ddtek.jdbcx.openedge.OpenEdgeDataSource40");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			// the openedge url
			//String url2 = pasta.filePath;
			// String url =
			// "jdbc:datadirect:openedge://192.168.30.25:20612;DatabaseName=silv-exp;User=SYSPROGRESS;Password=SYSPROGRESS;";
			// get the openedge database connection
			//url = "jdbc:sqlserver://localhost:54447;databaseName=demo;User=admin;Password=sa123;";
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

	public List<HashMap<String, String>> getOF(String ofnum,String url) throws SQLException {

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

	public List<HashMap<String, String>> getOP(String ofanumenr,String url) throws SQLException {

		String query = "select OPECOD,OPENUM,OPEDES,SECNUMENR1 from SOFD where ofanumenr= '" + ofanumenr + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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
				// parser das opera��es
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

	public List<HashMap<String, String>> getallOPNOTIN(String data,String url) throws SQLException {
		String query = "select OPECOD,OPEDES,SECNUMENR1 from SDTOPP";
		if (!data.equals("null")) {
			query += " where OPECOD not in(" + data + ")";
		}

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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

	public List<HashMap<String, String>> getallfamNOTIN(String data,String url) throws SQLException {
		String query = "select FAMCOD,FAMLIB from SPAFAM";
		if (!data.equals("null")) {
			query += " where FAMCOD not in(" + data + ")";
		}

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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
				// parser das opera��es
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

	public List<HashMap<String, String>> getMaq(String SECNUMENR,String url) throws SQLException {

		String query = "select a.SECCOD, a.ssecod ,c.SECLIB,b.SSEDES from SDTSEC a "
				+ "inner join SPASSE b on a.ssecod = b.ssecod "
				+ "inner join SPASEC c on a.seccod = c.seccod " + "where a.SECNUMENR= '" + SECNUMENR + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
				HashMap<String, String> x = new HashMap<>();
				x.put("SECCOD", rs.getString("SECCOD"));
				x.put("ssecod", rs.getString("ssecod"));
				x.put("SECLIB", rs.getString("SECLIB"));
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

	public List<HashMap<String, String>> getAllMaq(String SECCOD,String url) throws SQLException {

		String query = "select b.ssecod,a.SSEDES from SPASSE a inner join SDTSEC b on a.ssecod = b.ssecod where b.SECCOD= '"
				+ SECCOD + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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

		String query = "select DISTINCT LEFT(QUACOD,2) as fam from SPUA";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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

	public List<HashMap<String, String>> getfilhos(String pai,String url) throws SQLException {

		String query = "select b.PROREF,b.PRODES1,b.PRODES2,a.PROREFCST,b.PRDFAMCOD from SDTNCL a "
				+ "inner join SDTPRA b on a.PROREFCST = b.PROREF  " + " where a.PROREFCSE ='" + pai + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
				HashMap<String, String> x = new HashMap<>();
				x.put("PROREF", rs.getString("PROREF"));
				x.put("PRODES1", rs.getString("PRODES1"));
				x.put("PRODES2", rs.getString("PRODES2"));
				x.put("PROREFCST", rs.getString("PROREFCST"));
				x.put("PRDFAMCOD", rs.getString("PRDFAMCOD"));
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

	public List<HashMap<String, String>> getDefeitos(String fam,String url) throws SQLException {

		String query = "select QUACOD,QUALIB from SPAQUA where  LEFT(QUACOD,2)='" + fam + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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

	public List<HashMap<String, String>> getRef(String OFANUMENR,String url) throws SQLException {

		String query = "select a.PROREF, b.PRODES1,b.PRODES2,a.VA1REF, a.VA2REF,a.INDREF,a.OFBQTEINI,a.INDNUMENR,c.FAMCOD,d.ZPAVAL,b.PRDFAMCOD "
				+ "from SOFB a " + "inner join SDTPRA b on a.PROREF = b.PROREF  "
				+ "inner join SPAFAM c on b.PRDFAMCOD = c.FAMCOD  "
				+ "left join (select * from SDTZPA f where f.ZPACOD='ALER') d on b.ZPANUM = d.ZPANUM "
				+ "inner join SPAPRT e on b.PROTYPCOD = e.PROTYPCOD " + "where a.OFANUMENR= '" + OFANUMENR
				+ "'  and e.FABCON = 1";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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

	public List<HashMap<String, String>> getOPtop1(String ofanumenr,String url) throws SQLException {

		String query = "select top 1 * from SOFD a where ofanumenr= '" + ofanumenr
				+ "' and OPECOD != '' order by a.OPENUM desc";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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

	public List<HashMap<String, String>> getEtiqueta(String etiqueta,String url) throws SQLException {

		String query = "select b.OFNUM, b.ofanumenr,ofref,a.ETQEMBQTE,a.INDNUMENR,a.VA1REF,a.VA2REF,a.INDREF,a.PROREF from SETQDE a "
				+ "inner join SOFA b on b.ofnum = left(a.etqoridoc1,10) " + "where a.etqnum = '" + etiqueta
				+ "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		/// System.out.println(query);
		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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
				// parser das opera��es
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

	public List<HashMap<String, String>> getUser(String RESCOD,String url) throws SQLException {

		String query = "select RESCOD,RESDES from SDTRES  where RESTYPCOD = 'MO' and RESCOD='" + RESCOD + "'";

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Usa sempre assim que fecha os resources automaticamente
		try (Connection connection = getConnection(url);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// parser das opera��es
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
				// parser das opera��es
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

}