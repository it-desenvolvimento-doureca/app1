package pt.example.rest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import pt.example.bootstrap.ConnectProgress;

@Stateless
@Path("/demo")
public class DemoRest {

	@GET
	@Path("/silver/{ofnum}")
	@Produces("application/json")
	public List<HashMap<String, String>> silver(@PathParam("ofnum") String ofnum)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getOF(ofnum, getURL());
		return dados;
	}

	@GET
	@Path("/getofpai_filho/{ofnum}")
	@Produces("application/json")
	public List<HashMap<String, String>> getofpai_filho(@PathParam("ofnum") String ofnum)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getofpai_filho(ofnum, getURL());
		return dados;
	}
	
	@GET
	@Path("/getEtiquetasTrabalho/{id_of_cab}")
	@Produces("application/json")
	public List<HashMap<String, String>> getEtiquetasTrabalho(@PathParam("id_of_cab") String id_of_cab)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getEtiquetasTrabalho(id_of_cab, getURL());
		return dados;
	}

	@GET
	@Path("/operacao/{ofanumenr}")
	@Produces("application/json")
	public List<HashMap<String, Object>> operacao(@PathParam("ofanumenr") String ofanumenr)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, Object>> dados = connectionProgress.getOP(ofanumenr, getURL());
		return dados;
	}

	@GET
	@Path("/operacaoTop1/{ofanumenr}")
	@Produces("application/json")
	public List<HashMap<String, String>> operacaoTop1(@PathParam("ofanumenr") String ofanumenr)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getOPtop1(ofanumenr, getURL());
		return dados;
	}

	@GET
	@Path("/allop")
	@Produces("application/json")
	public List<HashMap<String, String>> allop() throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getallOP(getURL());
		return dados;
	}

	@POST
	@Path("/allopNOTIN")
	@Consumes("*/*")
	@Produces("application/json")
	public List<HashMap<String, String>> allopNOTIN(final List<String> data)
			throws SQLException, ClassNotFoundException {
		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getallOPNOTIN(data, getURL());
		return dados;
	}

	@POST
	@Path("/allfamNOTIN")
	@Consumes("*/*")
	@Produces("application/json")
	public List<HashMap<String, String>> allfamNOTIN(final List<String> data)
			throws SQLException, ClassNotFoundException {
		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getallfamNOTIN(data, getURL());
		return dados;
	}

	@GET
	@Path("/maquina/{SECNUMENR}")
	@Produces("application/json")
	public List<HashMap<String, String>> maquina(@PathParam("SECNUMENR") String SECNUMENR)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getMaq(SECNUMENR, getURL());
		return dados;
	}

	@GET
	@Path("/getOPTIPO/{OPECOD}")
	@Produces("application/json")
	public List<HashMap<String, String>> getOPTIPO(@PathParam("OPECOD") String OPECOD)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getOPTIPO(OPECOD, getURL());
		return dados;
	}

	@GET
	@Path("/allmaquina/{SECNUMENR}")
	@Produces("application/json")
	public List<HashMap<String, String>> Allmaquina(@PathParam("SECNUMENR") String SECCOD)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getAllMaq(SECCOD, getURL());
		return dados;
	}

	@GET
	@Path("/tipofaltas")
	@Produces("application/json")
	public List<HashMap<String, String>> getTipoFalta() throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getTipoFalta(getURL());
		return dados;
	}

	@GET
	@Path("/users")
	@Produces("application/json")
	public List<HashMap<String, String>> getUsers() throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getUsers(getURL());
		return dados;
	}

	@GET
	@Path("/searchuser/{RESCOD}")
	@Produces("application/json")
	public List<HashMap<String, String>> getUser(@PathParam("RESCOD") String RESCOD)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getUser(RESCOD, getURL());
		return dados;
	}

	@PersistenceContext(unitName = "persistenceUnit")
	protected EntityManager entityManager;

	public String getURL() {
		String url = "";
		Query query_folder = entityManager.createNativeQuery("select top 1 * from GER_PARAMETROS a");

		List<Object[]> dados_folder = query_folder.getResultList();

		for (Object[] content : dados_folder) {
			url = content[2].toString();
		}

		return url;
	}

	@GET
	@Path("/sessoes")
	@Produces("application/json")
	public List<HashMap<String, String>> getSessoes() throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getSessoes(getURL());
		return dados;
	}

	@GET
	@Path("/getSeccao")
	@Produces("application/json")
	public List<HashMap<String, String>> getSeccao() throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getSeccao(getURL());
		return dados;
	}

	@GET
	@Path("/getEtiquetacaixas/{etiqueta}")
	@Produces("application/json")
	public List<HashMap<String, String>> getEtiquetacaixas(@PathParam("etiqueta") String etiqueta)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getEtiquetacaixas(etiqueta, getURL());
		return dados;
	}
	
	@POST
	@Path("/valida_REFERENCIA_CONTROLO_ETIQUETAS")
	@Produces("application/json")
	public List<HashMap<String, String>> valida_REFERENCIA_CONTROLO_ETIQUETAS(final List<HashMap<String, String>> data)
			throws SQLException, ClassNotFoundException {
		HashMap<String, String> firstMap = data.get(0);
		String proref = firstMap.get("proref");
		 

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.valida_REFERENCIA_CONTROLO_ETIQUETAS(proref, getURL());
		return dados;
	}

	@POST
	@Path("/gama_embalagem")
	@Produces("application/json")
	public List<HashMap<String, String>> gama_embalagem(final List<HashMap<String, String>> data)
			throws SQLException, ClassNotFoundException {
		HashMap<String, String> firstMap = data.get(0);
		String proref = firstMap.get("proref");
		String id_of_cab = firstMap.get("id_of_cab");

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.gama_embalagem(proref, id_of_cab, getURL());
		return dados;
	}
	
	@POST
	@Path("/getEtiquetacaixaMatrix")
	@Produces("application/json")
	public List<HashMap<String, String>> getEtiquetacaixaMatrix(final List<HashMap<String, String>> data)
			throws SQLException, ClassNotFoundException {
		HashMap<String, String> firstMap = data.get(0);
		String etiqueta = firstMap.get("etiqueta"); 

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getEtiquetacaixaMatrix(etiqueta, getURL());
		return dados;
	}

	@GET
	@Path("/getEtiqueta/{etiqueta}")
	@Produces("application/json")
	public List<HashMap<String, String>> getEtiqueta(@PathParam("etiqueta") String etiqueta)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getEtiqueta(etiqueta, getURL());
		return dados;
	}

	@GET
	@Path("/getDefeitosOFNUM/{ofnum}")
	@Produces("application/json")
	public List<HashMap<String, String>> getDefeitosOFNUM(@PathParam("ofnum") String ofnum)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getDefeitosOFNUM(ofnum, getURL());
		return dados;
	}
	
	@GET
	@Path("/consulta_Impressao/{proref}")
	@Produces("application/json")
	public List<HashMap<String, String>> consulta_Impressao(@PathParam("proref") String proref)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.consulta_Impressao(proref, getURL());
		return dados;
	}

	@GET
	@Path("/referencias/{OFANUMENR}")
	@Produces("application/json")
	public List<HashMap<String, String>> getRef(@PathParam("OFANUMENR") String OFANUMENR)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getRef(OFANUMENR, getURL());
		return dados;
	}

	@GET
	@Path("/familias")
	@Produces("application/json")
	public List<HashMap<String, String>> getFamilias() throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getFamilias(getURL());
		return dados;
	}

	@GET
	@Path("/getfilhos/{pai}")
	@Produces("application/json")
	public List<HashMap<String, String>> getfilhos(@PathParam("pai") String pai)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getfilhos(pai, getURL());
		return dados;
	}

	@GET
	@Path("/getfilhosprimeiro/{OFANUMENR}")
	@Produces("application/json")
	public List<HashMap<String, String>> getfilhosprimeiro(@PathParam("OFANUMENR") String OFANUMENR)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getfilhosprimeiro(OFANUMENR, getURL());
		return dados;
	}

	@GET
	@Path("/defeitos/{fam}")
	@Produces("application/json")
	public List<HashMap<String, String>> getDefeitos(@PathParam("fam") String fam)
			throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getDefeitos(fam, getURL());
		return dados;
	}

	@POST
	@Path("/defeitos2")
	@Produces("application/json")
	public List<HashMap<String, String>> getDefeitos2(final String fam) throws SQLException, ClassNotFoundException {

		ConnectProgress connectionProgress = new ConnectProgress();

		List<HashMap<String, String>> dados = connectionProgress.getDefeitos2(fam, getURL());
		return dados;
	}

}
