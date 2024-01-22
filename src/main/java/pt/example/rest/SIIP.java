package pt.example.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.jasperreports.engine.JRException;
import pt.example.bootstrap.ConnectProgress;
import pt.example.bootstrap.Printer;
import pt.example.bootstrap.ReportGenerator;
import pt.example.bootstrap.SendEmail;
import pt.example.dao.CONTROLO_ETIQUETASDao;
import pt.example.dao.GER_EVENTODao;
import pt.example.dao.GER_POSTOSDao;
import pt.example.dao.RPCONFUTZPERFDao;
import pt.example.dao.RPOFDao;
import pt.example.dao.RP_CAIXAS_INCOMPLETASDao;
import pt.example.dao.RP_CONF_CHEF_SECDao;
import pt.example.dao.RP_CONF_FAMILIA_COMPDao;
import pt.example.dao.RP_CONF_FAMILIA_DEF_COMPRADASDao;
import pt.example.dao.RP_CONF_OPDao;
import pt.example.dao.RP_CONF_OP_NPREVDao;
import pt.example.dao.RP_CONF_RELACAO_REF_ETIQUETASDao;
import pt.example.dao.RP_GESTAO_ETIQUETAS_MATRIXDao;
import pt.example.dao.RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIXDao;
import pt.example.dao.RP_OF_DEF_LINDao;
import pt.example.dao.RP_OF_LST_DEFDao;
import pt.example.dao.RP_OF_OP_CABDao;
import pt.example.dao.RP_OF_OP_ETIQUETADao;
import pt.example.dao.RP_OF_OP_FUNCDao;
import pt.example.dao.RP_OF_OP_LINDao;
import pt.example.dao.RP_OF_OUTRODEF_LINDao;
import pt.example.dao.RP_OF_PARA_LINDao;
import pt.example.dao.RP_OF_PREP_LINDao;
import pt.example.dao.ST_PEDIDOSDao;
import pt.example.dao.VERSAO_APPDao;
import pt.example.entity.CONTROLO_ETIQUETAS;
import pt.example.entity.EMAIL;
import pt.example.entity.GER_EVENTO;
import pt.example.entity.GER_EVENTOS_CONF;
import pt.example.entity.GER_POSTOS;
import pt.example.entity.RP_CAIXAS_INCOMPLETAS;
import pt.example.entity.RP_CONF_CHEF_SEC;
import pt.example.entity.RP_CONF_FAMILIA_COMP;
import pt.example.entity.RP_CONF_FAMILIA_DEF_COMPRADAS;
import pt.example.entity.RP_CONF_OP;
import pt.example.entity.RP_CONF_OP_NPREV;
import pt.example.entity.RP_CONF_RELACAO_REF_ETIQUETAS;
import pt.example.entity.RP_CONF_UTZ_PERF;
import pt.example.entity.RP_GESTAO_ETIQUETAS_MATRIX;
import pt.example.entity.RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX;
import pt.example.entity.RP_OF_CAB;
import pt.example.entity.RP_OF_DEF_LIN;
import pt.example.entity.RP_OF_LST_DEF;
import pt.example.entity.RP_OF_OP_CAB;
import pt.example.entity.RP_OF_OP_ETIQUETA;
import pt.example.entity.RP_OF_OP_FUNC;
import pt.example.entity.RP_OF_OP_LIN;
import pt.example.entity.RP_OF_OUTRODEF_LIN;
import pt.example.entity.RP_OF_PARA_LIN;
import pt.example.entity.RP_OF_PREP_LIN;
import pt.example.entity.ST_PEDIDOS;
import pt.example.entity.VERSAO_APP;

@Stateless
@Path("/siip")
public class SIIP {

	@PersistenceContext(unitName = "persistenceUnit")
	protected EntityManager entityManager;

	@Inject
	private RPOFDao dao;

	@Inject
	private RPCONFUTZPERFDao dao1;

	@Inject
	private RP_CONF_CHEF_SECDao dao2;

	@Inject
	private RP_CONF_OPDao dao3;

	@Inject
	private RP_CONF_OP_NPREVDao dao4;

	@Inject
	private RP_OF_DEF_LINDao dao5;

	@Inject
	private RP_OF_OP_CABDao dao6;

	@Inject
	private RP_OF_OP_LINDao dao7;

	@Inject
	private RP_OF_PREP_LINDao dao8;

	@Inject
	private RP_OF_PARA_LINDao dao9;

	@Inject
	private RP_CONF_FAMILIA_COMPDao dao10;

	@Inject
	private RP_OF_OP_FUNCDao dao11;

	@Inject
	private RP_OF_OUTRODEF_LINDao dao12;

	@Inject
	private RP_OF_OP_ETIQUETADao dao13;

	@Inject
	private RP_OF_LST_DEFDao dao14;

	@Inject
	private GER_EVENTODao dao15;

	@Inject
	private VERSAO_APPDao dao16;

	@Inject
	private RP_CONF_FAMILIA_DEF_COMPRADASDao dao17;

	@Inject
	private ST_PEDIDOSDao dao18;

	@Inject
	private RP_CAIXAS_INCOMPLETASDao dao19;

	@Inject
	private CONTROLO_ETIQUETASDao dao20;

	@Inject
	private RP_GESTAO_ETIQUETAS_MATRIXDao dao21;

	@Inject
	private RP_CONF_RELACAO_REF_ETIQUETASDao dao22;

	@Inject
	private RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIXDao dao23;

	@Inject
	private GER_POSTOSDao dao24;

	// RP_CONF_UTZ_PERF***************************************************************
	@POST
	@Path("/createRP_CONF_UTZ_PERF")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_UTZ_PERF insertRP_CONF_UTZ_PERF(final RP_CONF_UTZ_PERF data) {
		return dao1.create(data);
	}

	@GET
	@Path("/getRP_CONF_UTZ_PERF")
	@Produces("application/json")
	public List<RP_CONF_UTZ_PERF> getRP_CONF_UTZ_PERF() {
		return dao1.allEntries();
	}

	@GET
	@Path("/getRP_CONF_UTZ_PERFid/{id}")
	@Produces("application/json")
	public List<RP_CONF_UTZ_PERF> getRP_CONF_UTZ_PERF_id(@PathParam("id") String id) {
		return dao1.getbyid(id);
	}

	@DELETE
	@Path("/deleteRP_CONF_UTZ_PERF/{id}")
	public void deleteRP_CONF_UTZ_PERF(@PathParam("id") Integer id) {
		RP_CONF_UTZ_PERF RP_CONF_UTZ_PERF = new RP_CONF_UTZ_PERF();
		RP_CONF_UTZ_PERF.setID_CONF_UTZ_PERF(id);
		dao1.delete(RP_CONF_UTZ_PERF);
	}

	@PUT
	@Path("/updateRP_CONF_UTZ_PERF")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_UTZ_PERF updateuser(final RP_CONF_UTZ_PERF RP_CONF_UTZ_PERF) {
		RP_CONF_UTZ_PERF.setID_CONF_UTZ_PERF(RP_CONF_UTZ_PERF.getID_CONF_UTZ_PERF());
		return dao1.update(RP_CONF_UTZ_PERF);
	}

	// RP_CONF_CHEF_SEC***************************************************************
	@POST
	@Path("/createRP_CONF_CHEF_SEC")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_CHEF_SEC insertRP_CONF_CHEF_SECF(final RP_CONF_CHEF_SEC data) {
		return dao2.create(data);
	}

	@GET
	@Path("/getRP_CONF_CHEF_SEC")
	@Produces("application/json")
	public List<RP_CONF_CHEF_SEC> getRP_CONF_CHEF_SEC() {
		return dao2.allEntries();
	}

	@GET
	@Path("/getRP_CONF_CHEF_SECbyidUSER/{id}")
	@Produces("application/json")
	public List<RP_CONF_CHEF_SEC> getRP_CONF_CHEF_SECbyID(@PathParam("id") String id) {
		return dao2.getallbyid(id);
	}

	@DELETE
	@Path("/deleteRP_CONF_CHEF_SEC/{id}")
	public void deleteRP_CONF_CHEF_SEC(@PathParam("id") Integer id) {
		RP_CONF_CHEF_SEC RP_CONF_CHEF_SEC = new RP_CONF_CHEF_SEC();
		RP_CONF_CHEF_SEC.setID_CONF_CHEF_SEC(id);
		dao2.delete(RP_CONF_CHEF_SEC);
	}

	@PUT
	@Path("/updateRP_CONF_CHEF_SEC")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_CHEF_SEC updateuser(final RP_CONF_CHEF_SEC RP_CONF_CHEF_SEC) {
		RP_CONF_CHEF_SEC.setID_UTZ(RP_CONF_CHEF_SEC.getID_UTZ());
		return dao2.update(RP_CONF_CHEF_SEC);
	}

	// RP_CONF_OP***************************************************************

	@GET
	@Path("/getRP_CONF_OP")
	@Produces("application/json")
	public List<RP_CONF_OP> getRP_CONF_OP() {
		return dao3.getall();
	}

	@POST
	@Path("/createRP_CONF_OP")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_OP insertRP_CONF_OP(final RP_CONF_OP data) {
		return dao3.create(data);
	}

	@DELETE
	@Path("/deleteRP_CONF_OP/{id}")
	public void deleteRP_CONF_OP(@PathParam("id") Integer id) {
		RP_CONF_OP RP_CONF_OP = new RP_CONF_OP();
		RP_CONF_OP.setID_CONF_OP(id);
		dao3.delete(RP_CONF_OP);
	}

	@PUT
	@Path("/updateRP_CONF_OP")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_OP updateRP_CONF_OP(final RP_CONF_OP RP_CONF_OP) {
		RP_CONF_OP.setID_OP_PRINC(RP_CONF_OP.getID_OP_PRINC());
		return dao3.update(RP_CONF_OP);
	}

	@PUT
	@Path("/getRP_CONF_OPbyid")
	@Produces("application/json")
	public List<RP_CONF_OP> getRP_CONF_OPbyid(final String id) {
		return dao3.getbyid(id.replace("\"", ""));
	}

	// RP_CONF_OP_NPREV***********************************************************

	@GET
	@Path("/getRP_CONF_OP_NPREV")
	@Produces("application/json")
	public List<RP_CONF_OP_NPREV> getRP_CONF_OP_NPREV() {
		return dao4.allEntries();
	}

	@POST
	@Path("/createRP_CONF_OP_NPREV")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_OP_NPREV insertRP_CONF_OP_NPREV(final RP_CONF_OP_NPREV data) {
		return dao4.create(data);
	}

	@DELETE
	@Path("/deleteRP_CONF_OP_NPREV/{id}")
	public void deleteRP_CONF_OP_NPREV(@PathParam("id") Integer id) {
		RP_CONF_OP_NPREV RP_CONF_OP_NPREV = new RP_CONF_OP_NPREV();
		RP_CONF_OP_NPREV.setID_CONF_OP_NPREV(id);
		dao4.delete(RP_CONF_OP_NPREV);
	}

	// RP_OF_CAB***********************************************************

	@POST
	@Path("/getRP_OF_CAB/{start}")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_CAB> listof(final String data, @PathParam("start") Integer start) {
		return dao.getall(data, start);
	}

	@POST
	@Path("/getRP_OF_CAB2/{start}")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_CAB> listof2(final String data, @PathParam("start") Integer start) {
		return dao.getall2(data, start);
	}

	@POST
	@Path("/getallRP_OF_CAB")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_CAB> listallof(final List<HashMap<String, String>> data) {
		return dao.getalllist(data);
	}

	@GET
	@Path("/getRP_OF_CABbyid/{id}")
	@Produces("application/json")
	public List<RP_OF_CAB> listofbyid(@PathParam("id") Integer id) {
		return dao.getallbyid(id);
	}

	@GET
	@Path("/listofcurrentof/{id}")
	@Produces("application/json")
	public List<RP_OF_CAB> listofcurrentof(@PathParam("id") String id) {
		return dao.getbyid(id);
	}

	@GET
	@Path("/getof/{id}")
	@Produces("application/json")
	public List<RP_OF_CAB> getof(@PathParam("id") Integer id) {
		return dao.getof(id);
	}

	@GET
	@Path("/verifica/{of_num}/{op_cod}/{op_num}/{user}")
	@Produces("application/json")
	public List<RP_OF_CAB> verifica(@PathParam("of_num") String of_num, @PathParam("op_cod") String op_cod,
			@PathParam("op_num") String op_num, @PathParam("user") String user) {
		return dao.verifica(of_num, op_cod, op_num, user);
	}

	@GET
	@Path("/verificaetiquetas/{of_num}")
	@Produces("application/json")
	public List<Object[]> verificaetiquetas(@PathParam("of_num") String of_num) {
		@SuppressWarnings("unchecked")
		List<Object[]> dados = entityManager.createNativeQuery(
				"select c.REF_NUM,d.REF_ETIQUETA,(d.QUANT_BOAS_M2 + QUANT_DEF_M2) as QUANT,QUANT_ETIQUETA from RP_OF_CAB a "
						+ "inner join RP_OF_OP_CAB b on a.ID_OF_CAB = b.ID_OF_CAB "
						+ "inner join RP_OF_OP_LIN c on b.ID_OP_CAB = c.ID_OP_CAB "
						+ "inner join RP_OF_OP_ETIQUETA d on d.ID_OP_LIN = c.ID_OP_LIN " + "where a.ID_OF_CAB_ORIGEM = "
						+ of_num)
				.getResultList();
		return dados;
	}

	@POST
	@Path("/createRP_OF_CAB")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_CAB insertRP_OF_CAB(final RP_OF_CAB data) {
		return dao.create(data);
	}

	@POST
	@Path("/createupdateRP_OF_DEF_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_DEF_LIN> insertupdateRP_OF_DEF_LIN(final RP_OF_DEF_LIN data) {
		return dao5.createupdate(data);
	}

	@POST
	@Path("/pesquisa_avancada/{start}")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_CAB> pesquisa_avancada(final List<HashMap<String, String>> data,
			@PathParam("start") Integer start) throws ParseException {
		return dao.pesquisa_avancada(data, start);
	}

	@PUT
	@Path("/updateRP_OF_CAB")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_CAB updateRP_OF_CAB(final RP_OF_CAB RP_OF_CAB) {
		RP_OF_CAB.setESTADO(RP_OF_CAB.getESTADO());
		return dao.update(RP_OF_CAB);
	}

	@POST
	@Path("/createupdateESTADOS")
	@Consumes("*/*")
	@Produces("application/json")
	public HashMap<String, String> insertupdate_estados(final List<HashMap<String, String>> data) {
		return dao.updateEstados(data);
	}

	// RP_OF_OP_CAB*************************************************************

	@POST
	@Path("/createRP_OF_OP_CAB")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_CAB insertRP_OF_OP_CAB(final RP_OF_OP_CAB data) {
		return dao6.create(data);
	}

	@POST
	@Path("/getdataof/{id}/{user}")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> getdataof(@PathParam("id") Integer id, @PathParam("user") String user,
			final ArrayList<String> estado) {
		return dao11.getbyid(id, user, estado);
	}

	@GET
	@Path("/getRP_OF_OP_CABid/{id}/{id2}")
	@Produces("application/json")
	public List<RP_OF_OP_CAB> getdataof(@PathParam("id") Integer id, @PathParam("id2") Integer id2) {
		return dao6.getid(id, id2);
	}

	@GET
	@Path("/getMaxID")
	@Produces("application/json")
	public List<Integer> getMaxID() {
		return dao6.getMaxID();
	}

	@PUT
	@Path("/updateRP_OF_OP_CAB")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_CAB updateRP_OF_OP_CAB(final RP_OF_OP_CAB RP_OF_OP_CAB) {
		RP_OF_OP_CAB.setID_OF_CAB(RP_OF_OP_CAB.getID_OF_CAB());
		return dao6.update(RP_OF_OP_CAB);
	}

	// RP_OF_OP_LIN************************************************************

	@POST
	@Path("/createRP_OF_OP_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_LIN insertRP_OF_OP_LIN(final RP_OF_OP_LIN data) {
		return dao7.create(data);
	}

	@GET
	@Path("/getRP_OF_OP_LINid/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_LIN> getbyid(@PathParam("id") Integer id) {
		return dao7.getbyid(id);
	}

	@GET
	@Path("/getRP_OF_OP_LINidcontrolo/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_LIN> getbyidontrolo(@PathParam("id") Integer id) {
		return dao7.getbyidcontrolo(id);
	}

	@GET
	@Path("/getRP_OF_OP_LIN/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_LIN> getid(@PathParam("id") Integer id) {
		return dao7.getid(id);
	}

	@GET
	@Path("/getRP_OF_OP_LINallid/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_LIN> getallbyid(@PathParam("id") Integer id) {
		return dao7.getallbyid(id);
	}

	@GET
	@Path("/getRP_OF_OP_LINOp/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_LIN> getop(@PathParam("id") Integer id) {
		return dao7.getop(id);
	}

	@PUT
	@Path("/updateRP_OF_OP_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_LIN updateRP_OF_OP_LIN(final RP_OF_OP_LIN RP_OF_OP_LIN) {
		RP_OF_OP_LIN.setQUANT_DEF_TOTAL(RP_OF_OP_LIN.getQUANT_DEF_TOTAL());
		return dao7.update(RP_OF_OP_LIN);
	}

	// RP_OF_PREP_LIN***********************************
	@GET
	@Path("/getbyidRP_OF_PREP_LIN/{id}")
	@Produces("application/json")
	public List<RP_OF_PREP_LIN> getbyidRP_OF_PREP_LIN(@PathParam("id") Integer id) {
		return dao8.getbyid(id);
	}

	@GET
	@Path("/getbyidRP_OF_PREP_LIN2/{id}")
	@Produces("application/json")
	public List<RP_OF_PREP_LIN> getbyidRP_OF_PREP_LIN2(@PathParam("id") Integer id) {
		return dao8.getbyid2(id);
	}

	@POST
	@Path("/createRP_OF_PREP_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_PREP_LIN insertRP_OF_PREP_LIN(final RP_OF_PREP_LIN data) {
		return dao8.create(data);
	}

	@PUT
	@Path("/updateRP_OF_PREP_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_PREP_LIN updateRP_OF_PREP_LIN(final RP_OF_PREP_LIN RP_OF_PREP_LIN) {
		RP_OF_PREP_LIN.setESTADO(RP_OF_PREP_LIN.getESTADO());
		return dao8.update(RP_OF_PREP_LIN);
	}

	// RP_CONF_FAMILIA_DEF_COMPRADAS***********************************
	@GET
	@Path("/getRP_CONF_FAMILIA_DEF_COMPRADAS")
	@Produces("application/json")
	public List<RP_CONF_FAMILIA_DEF_COMPRADAS> getRP_CONF_FAMILIA_DEF_COMPRADAS() {
		return dao17.allEntries();
	}

	/*
	 * @GET
	 * 
	 * @Path("/getRP_CONF_FAMILIA_DEF_COMPRADAScodfam/{codfam}")
	 * 
	 * @Produces("application/json") public List<RP_CONF_FAMILIA_DEF_COMPRADAS>
	 * getRP_CONF_FAMILIA_DEF_COMPRADAScodfam(@PathParam("codfam") String
	 * codfam) { return dao17.getbycodfam(codfam); }
	 */

	@POST
	@Path("/createRP_CONF_FAMILIA_DEF_COMPRADAS")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_FAMILIA_DEF_COMPRADAS insertRP_CONF_FAMILIA_DEF_COMPRADAS(final RP_CONF_FAMILIA_DEF_COMPRADAS data) {
		return dao17.create(data);
	}

	@PUT
	@Path("/updateRP_CONF_FAMILIA_DEF_COMPRADAS")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_FAMILIA_DEF_COMPRADAS updateRP_CONF_FAMILIA_DEF_COMPRADAS(
			final RP_CONF_FAMILIA_DEF_COMPRADAS RP_CONF_FAMILIA_DEF_COMPRADAS) {
		RP_CONF_FAMILIA_DEF_COMPRADAS.setCOD_FAMILIA_DEF(RP_CONF_FAMILIA_DEF_COMPRADAS.getCOD_FAMILIA_DEF());
		return dao17.update(RP_CONF_FAMILIA_DEF_COMPRADAS);
	}

	@DELETE
	@Path("/deleteRP_CONF_FAMILIA_DEF_COMPRADAS/{id}")
	public void deleteRP_CONF_FAMILIA_DEF_COMPRADAS(@PathParam("id") String id) {
		RP_CONF_FAMILIA_DEF_COMPRADAS RP_CONF_FAMILIA_DEF_COMPRADAS = new RP_CONF_FAMILIA_DEF_COMPRADAS();
		RP_CONF_FAMILIA_DEF_COMPRADAS.setCOD_FAMILIA_DEF(id);
		dao17.delete(RP_CONF_FAMILIA_DEF_COMPRADAS);
	}

	// RP_CONF_FAMILIA_COMP***********************************
	@GET
	@Path("/getRP_CONF_FAMILIA_COMP")
	@Produces("application/json")
	public List<RP_CONF_FAMILIA_COMP> getRP_CONF_FAMILIA_COMP() {
		return dao10.allEntries();
	}

	@GET
	@Path("/getRP_CONF_FAMILIA_COMPcodfam/{codfam}")
	@Produces("application/json")
	public List<RP_CONF_FAMILIA_COMP> getRP_CONF_FAMILIA_COMPcodfam(@PathParam("codfam") String codfam) {
		return dao10.getbycodfam(codfam);
	}

	@POST
	@Path("/createRP_CONF_FAMILIA_COMP")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_FAMILIA_COMP insertRP_CONF_FAMILIA_COMP(final RP_CONF_FAMILIA_COMP data) {
		return dao10.create(data);
	}

	@PUT
	@Path("/updateRP_CONF_FAMILIA_COMP")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_FAMILIA_COMP updateRP_CONF_FAMILIA_COMP(final RP_CONF_FAMILIA_COMP RP_CONF_FAMILIA_COMP) {
		RP_CONF_FAMILIA_COMP.setCOD_FAMILIA_COMP(RP_CONF_FAMILIA_COMP.getCOD_FAMILIA_COMP());
		return dao10.update(RP_CONF_FAMILIA_COMP);
	}

	@DELETE
	@Path("/deleteRP_CONF_FAMILIA_COMP/{id}")
	public void deleteRP_CONF_FAMILIA_COMP(@PathParam("id") String id) {
		RP_CONF_FAMILIA_COMP RP_CONF_FAMILIA_COMP = new RP_CONF_FAMILIA_COMP();
		RP_CONF_FAMILIA_COMP.setCOD_FAMILIA_COMP(id);
		dao10.delete(RP_CONF_FAMILIA_COMP);
	}

	@GET
	@Path("/duplicarRegistos/{id}")
	@Produces("application/json")
	@SuppressWarnings("unchecked")
	public Integer duplicarRegistos(@PathParam("id") Integer id) {
		Integer ID_OF_CAB = null;
		Query q = entityManager.createNativeQuery("INSERT  INTO RP_OF_CAB "
				+ "(ID_OF_CAB_ORIGEM,OF_NUM,OF_OBS,OP_COD,OP_NUM,OP_DES,SEC_NUM,SEC_DES,MAQ_NUM,MAQ_DES,ID_UTZ_CRIA,NOME_UTZ_CRIA,DATA_HORA_CRIA,ID_UTZ_MODIF,NOME_UTZ_MODIF,DATA_HORA_MODIF,ESTADO,OP_COD_ORIGEM,OP_PREVISTA,MAQ_NUM_ORIG,VERSAO_MODIF,ID_UTZ_EDICAO,ESTADO_INICIAL) "
				+ "select ID_OF_CAB_ORIGEM,OF_NUM,OF_OBS,OP_COD,OP_NUM,OP_DES,SEC_NUM,SEC_DES,MAQ_NUM,MAQ_DES,ID_UTZ_CRIA,NOME_UTZ_CRIA,DATA_HORA_CRIA,ID_UTZ_MODIF,NOME_UTZ_MODIF,DATA_HORA_MODIF,ESTADO,OP_COD_ORIGEM,OP_PREVISTA,MAQ_NUM_ORIG,VERSAO_MODIF,ID_UTZ_EDICAO,ESTADO_INICIAL from RP_OF_CAB WHERE ID_OF_CAB = "
				+ id + ";SELECT @@IDENTITY ");
		BigDecimal biid = (BigDecimal) q.getSingleResult();
		ID_OF_CAB = biid.intValue();
		cria_RP_OF_OP_CAB(id, ID_OF_CAB);

		List<Object[]> dados = entityManager
				.createNativeQuery(
						"SELECT ID_OF_CAB,'' from RP_OF_CAB  WHERE ID_OF_CAB_ORIGEM = " + id + "  ORDER BY ID_OF_CAB")
				.getResultList();

		for (Object[] content : dados) {
			Query q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_CAB "
					+ "(ID_OF_CAB_ORIGEM,OF_NUM,OF_OBS,OP_COD,OP_NUM,OP_DES,SEC_NUM,SEC_DES,MAQ_NUM,MAQ_DES,ID_UTZ_CRIA,NOME_UTZ_CRIA,DATA_HORA_CRIA,ID_UTZ_MODIF,NOME_UTZ_MODIF,DATA_HORA_MODIF,ESTADO,OP_COD_ORIGEM,OP_PREVISTA,MAQ_NUM_ORIG,VERSAO_MODIF,ID_UTZ_EDICAO,ESTADO_INICIAL) "
					+ "select " + ID_OF_CAB
					+ ",OF_NUM,OF_OBS,OP_COD,OP_NUM,OP_DES,SEC_NUM,SEC_DES,MAQ_NUM,MAQ_DES,ID_UTZ_CRIA,NOME_UTZ_CRIA,DATA_HORA_CRIA,ID_UTZ_MODIF,NOME_UTZ_MODIF,DATA_HORA_MODIF,ESTADO,OP_COD_ORIGEM,OP_PREVISTA,MAQ_NUM_ORIG,VERSAO_MODIF,ID_UTZ_EDICAO,ESTADO_INICIAL from RP_OF_CAB WHERE ID_OF_CAB = "
					+ content[0] + " ;SELECT @@IDENTITY ");
			BigDecimal biid2 = (BigDecimal) q1.getSingleResult();
			cria_RP_OF_OP_CAB(Integer.parseInt(content[0].toString()), biid2.intValue());
		}

		return ID_OF_CAB;
	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_OP_CAB(Integer id_of_cab, Integer id_of_cab_novo) {
		Integer id_op_cab = null;
		List<Object[]> dados = entityManager.createNativeQuery(
				"SELECT ID_OP_CAB,'' from RP_OF_OP_CAB  WHERE ID_OF_CAB = " + id_of_cab + "  ORDER BY ID_OP_CAB")
				.getResultList();

		for (Object[] content : dados) {
			Query q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_OP_CAB "
					+ "(ID_OF_CAB,TEMPO_PREP_TOTAL,TEMPO_PARA_TOTAL,TEMPO_EXEC_TOTAL,TEMPO_PREP_TOTAL_M1,TEMPO_EXEC_TOTAL_M1,TEMPO_PARA_TOTAL_M1,TEMPO_PREP_TOTAL_M2,TEMPO_EXEC_TOTAL_M2,TEMPO_PARA_TOTAL_M2) "
					+ "select " + id_of_cab_novo
					+ ",TEMPO_PREP_TOTAL,TEMPO_PARA_TOTAL,TEMPO_EXEC_TOTAL,TEMPO_PREP_TOTAL_M1,TEMPO_EXEC_TOTAL_M1,TEMPO_PARA_TOTAL_M1,TEMPO_PREP_TOTAL_M2,TEMPO_EXEC_TOTAL_M2,TEMPO_PARA_TOTAL_M2 from RP_OF_OP_CAB WHERE ID_OP_CAB = "
					+ content[0] + " ;SELECT @@IDENTITY ");
			BigDecimal biid = (BigDecimal) q1.getSingleResult();
			id_op_cab = biid.intValue();
			cria_RP_OF_OP_LIN(Integer.parseInt(content[0].toString()), id_op_cab);
			cria_RP_OF_PARA_LIN(Integer.parseInt(content[0].toString()), id_op_cab);
			cria_RP_OF_PREP_LIN(Integer.parseInt(content[0].toString()), id_op_cab);
			cria_RP_OF_OP_FUNC(Integer.parseInt(content[0].toString()), id_op_cab);

		}
	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_OP_LIN(Integer id_op_cab, Integer id_op_cab_novo) {

		Integer id_op_lin = null;

		List<Object[]> dados = entityManager
				.createNativeQuery(
						"SELECT ID_OP_LIN,'' from RP_OF_OP_LIN  WHERE ID_OP_CAB = " + id_op_cab + " ORDER BY ID_OP_LIN")
				.getResultList();

		for (Object[] content : dados) {
			Query q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_OP_LIN "
					+ "(ID_OP_CAB,REF_NUM,REF_DES,REF_IND,REF_VAR1,REF_VAR2,REF_INDNUMENR,QUANT_OF,QUANT_BOAS_TOTAL,QUANT_DEF_TOTAL,PERC_DEF,PERC_OBJETIV,OBS_REF,QUANT_BOAS_TOTAL_M1,QUANT_DEF_TOTAL_M1,QUANT_BOAS_TOTAL_M2,QUANT_DEF_TOTAL_M2,VERSAO_MODIF,GESCOD) "
					+ "select " + id_op_cab_novo
					+ ",REF_NUM,REF_DES,REF_IND,REF_VAR1,REF_VAR2,REF_INDNUMENR,QUANT_OF,QUANT_BOAS_TOTAL,QUANT_DEF_TOTAL,PERC_DEF,PERC_OBJETIV,OBS_REF,QUANT_BOAS_TOTAL_M1,QUANT_DEF_TOTAL_M1,QUANT_BOAS_TOTAL_M2,QUANT_DEF_TOTAL_M2,VERSAO_MODIF,GESCOD from RP_OF_OP_LIN WHERE ID_OP_LIN = "
					+ content[0] + " ;SELECT @@IDENTITY ");
			BigDecimal biid = (BigDecimal) q1.getSingleResult();
			id_op_lin = biid.intValue();

			cria_RP_OF_OP_ETIQUETA(Integer.parseInt(content[0].toString()), id_op_lin);
			cria_RP_OF_LST_DEF(Integer.parseInt(content[0].toString()), id_op_lin);
			cria_RP_OF_OUTRODEF_LIN(id_op_lin, null, Integer.parseInt(content[0].toString()), null);
			cria_RP_OF_DEF_LIN(id_op_lin, null, Integer.parseInt(content[0].toString()), null);
		}

	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_OP_ETIQUETA(Integer id_op_lin, Integer id_op_lin_novo) {
		Integer id_ref_etiqueta = null;

		List<Object[]> dados = entityManager
				.createNativeQuery("SELECT ID_REF_ETIQUETA,'' from RP_OF_OP_ETIQUETA  WHERE ID_OP_LIN = " + id_op_lin
						+ " ORDER BY ID_REF_ETIQUETA")
				.getResultList();

		for (Object[] content : dados) {
			Query q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_OP_ETIQUETA "
					+ "(ID_OP_LIN,REF_NUM,REF_LOTE,REF_ETIQUETA,OF_NUM_ORIGEM,OP_COD_ORIGEM,QUANT_ETIQUETA,QUANT_BOAS,QUANT_DEF,ID_UTZ_CRIA,ID_UTZ_MODIF,DATA_HORA_MODIF,OBS_ETIQ,QUANT_BOAS_M1,QUANT_DEF_M1,QUANT_BOAS_M2,QUANT_DEF_M2,VERSAO_MODIF,OP_COD_FAM,OP_NUM,NOVO,OFDATFR,ATIVO,APAGADO) "
					+ "select " + id_op_lin_novo
					+ ",REF_NUM,REF_LOTE,REF_ETIQUETA,OF_NUM_ORIGEM,OP_COD_ORIGEM,QUANT_ETIQUETA,QUANT_BOAS,QUANT_DEF,ID_UTZ_CRIA,ID_UTZ_MODIF,DATA_HORA_MODIF,OBS_ETIQ,QUANT_BOAS_M1,QUANT_DEF_M1,QUANT_BOAS_M2,QUANT_DEF_M2,VERSAO_MODIF,OP_COD_FAM,OP_NUM,NOVO,OFDATFR,ATIVO,APAGADO from RP_OF_OP_ETIQUETA WHERE ID_REF_ETIQUETA = "
					+ content[0] + " ;SELECT @@IDENTITY ");

			BigDecimal biid = (BigDecimal) q1.getSingleResult();
			id_ref_etiqueta = biid.intValue();
			cria_RP_OF_OUTRODEF_LIN(id_op_lin, Integer.parseInt(content[0].toString()), id_op_lin_novo,
					id_ref_etiqueta);
			cria_RP_OF_DEF_LIN(id_op_lin, Integer.parseInt(content[0].toString()), id_op_lin_novo, id_ref_etiqueta);
		}

	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_OUTRODEF_LIN(Integer id_op_lin, Integer id_ref_etiqueta, Integer id_op_lin_novo,
			Integer id_ref_etiqueta_novo) {
		List<Object[]> dados = entityManager
				.createNativeQuery("SELECT ID_OUTROSDEF_LIN,ID_REF_ETIQUETA from RP_OF_OUTRODEF_LIN  WHERE ID_OP_LIN = "
						+ id_op_lin + " AND ID_REF_ETIQUETA = " + id_ref_etiqueta + " ORDER BY ID_OUTROSDEF_LIN")
				.getResultList();

		for (Object[] content : dados) {
			int q1 = entityManager
					.createNativeQuery("INSERT  INTO RP_OF_OUTRODEF_LIN "
							+ "(ID_OP_LIN,ID_UTZ_CRIA,ID_UTZ_MODIF,ID_DEF_OUTRO,QUANT_OUTRODEF,RES_OUTRODEF,OBS_OUTRODEF,QUANT_OUTRODEF_M1,ID_REF_ETIQUETA) "
							+ "select " + id_op_lin_novo
							+ " ,ID_UTZ_CRIA,ID_UTZ_MODIF,ID_DEF_OUTRO,QUANT_OUTRODEF,RES_OUTRODEF,OBS_OUTRODEF,QUANT_OUTRODEF_M1,"
							+ id_ref_etiqueta_novo + " from RP_OF_OUTRODEF_LIN WHERE ID_OUTROSDEF_LIN = " + content[0]
							+ " AND ID_REF_ETIQUETA = " + id_ref_etiqueta_novo + "")
					.executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_LST_DEF(Integer id_op_lin, Integer id_op_lin_novo) {
		List<Object[]> dados = entityManager.createNativeQuery(
				"SELECT ID_LST_DEF,'' from RP_OF_LST_DEF  WHERE ID_OP_LIN = " + id_op_lin + " ORDER BY ID_LST_DEF")
				.getResultList();

		for (Object[] content : dados) {
			int q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_LST_DEF "
					+ "(ID_OP_LIN,COD_DEF,DESC_DEF,DATA_HORA_MODIF,ID_UTZ_CRIA) " + "select " + id_op_lin_novo
					+ ",COD_DEF,DESC_DEF,DATA_HORA_MODIF,ID_UTZ_CRIA from RP_OF_LST_DEF WHERE ID_LST_DEF = "
					+ content[0] + "  ").executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_PARA_LIN(Integer id_op_cab, Integer id_op_cab_novo) {
		List<Object[]> dados = entityManager.createNativeQuery(
				"SELECT ID_PARA_LIN,'' from RP_OF_PARA_LIN  WHERE ID_OP_CAB = " + id_op_cab + " ORDER BY ID_PARA_LIN")
				.getResultList();

		for (Object[] content : dados) {
			int q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_PARA_LIN "
					+ "(ID_OP_CAB,DATA_INI,HORA_INI,DATA_FIM,HORA_FIM,ID_UTZ_CRIA,ID_UTZ_MODIF,DATA_HORA_MODIF,TIPO_PARAGEM,DES_PARAGEM,ESTADO,MOMENTO_PARAGEM,DATA_FIM_M1,HORA_FIM_M1,DATA_FIM_M2,HORA_FIM_M2,DATA_INI_M1,HORA_INI_M1,DATA_INI_M2,HORA_INI_M2,MOMENTO_PARAGEM_M1,DES_PARAGEM_M1,TIPO_PARAGEM_M1,MOMENTO_PARAGEM_M2,DES_PARAGEM_M2,TIPO_PARAGEM_M2) "
					+ "select " + id_op_cab_novo
					+ ",DATA_INI,HORA_INI,DATA_FIM,HORA_FIM,ID_UTZ_CRIA,ID_UTZ_MODIF,DATA_HORA_MODIF,TIPO_PARAGEM,DES_PARAGEM,ESTADO,MOMENTO_PARAGEM,DATA_FIM_M1,HORA_FIM_M1,DATA_FIM_M2,HORA_FIM_M2,DATA_INI_M1,HORA_INI_M1,DATA_INI_M2,HORA_INI_M2,MOMENTO_PARAGEM_M1,DES_PARAGEM_M1,TIPO_PARAGEM_M1,MOMENTO_PARAGEM_M2,DES_PARAGEM_M2,TIPO_PARAGEM_M2 from RP_OF_PARA_LIN WHERE ID_PARA_LIN = "
					+ content[0] + " ").executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_PREP_LIN(Integer id_op_cab, Integer id_op_cab_novo) {
		List<Object[]> dados = entityManager.createNativeQuery(
				"SELECT ID_PREP_LIN,'' from RP_OF_PREP_LIN  WHERE ID_OP_CAB = " + id_op_cab + " ORDER BY ID_PREP_LIN")
				.getResultList();

		for (Object[] content : dados) {
			int q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_PREP_LIN "
					+ "(ID_OP_CAB,DATA_INI,HORA_INI,DATA_FIM,HORA_FIM,ID_UTZ_CRIA,ID_UTZ_MODIF,DATA_HORA_MODIF,ESTADO,DATA_FIM_M1,HORA_FIM_M1,DATA_FIM_M2,HORA_FIM_M2,DATA_INI_M1,HORA_INI_M1,DATA_INI_M2,HORA_INI_M2) "
					+ "select " + id_op_cab_novo
					+ ",DATA_INI,HORA_INI,DATA_FIM,HORA_FIM,ID_UTZ_CRIA,ID_UTZ_MODIF,DATA_HORA_MODIF,ESTADO,DATA_FIM_M1,HORA_FIM_M1,DATA_FIM_M2,HORA_FIM_M2,DATA_INI_M1,HORA_INI_M1,DATA_INI_M2,HORA_INI_M2 from RP_OF_PREP_LIN WHERE ID_PREP_LIN = "
					+ content[0] + " ").executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_DEF_LIN(Integer id_op_lin, Integer id_ref_etiqueta, Integer id_op_lin_novo,
			Integer id_ref_etiqueta_novo) {
		List<Object[]> dados = entityManager
				.createNativeQuery("SELECT ID_OP_LIN,ID_REF_ETIQUETA from RP_OF_DEF_LIN  WHERE ID_OP_LIN = " + id_op_lin
						+ " and ID_REF_ETIQUETA = " + id_ref_etiqueta + " ORDER BY ID_OP_LIN")
				.getResultList();

		for (Object[] content : dados) {
			int q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_DEF_LIN "
					+ "(ID_REF_ETIQUETA,ID_OP_LIN,ID_UTZ_CRIA,ID_UTZ_MODIF,COD_DEF,DESC_DEF,QUANT_DEF,DATA_HORA_REG,OBS_DEF,QUANT_DEF_M1,QUANT_DEF_M2,VERSAO_MODIF) "
					+ "select " + id_ref_etiqueta_novo + "," + id_op_lin_novo
					+ ",ID_UTZ_CRIA,ID_UTZ_MODIF,COD_DEF,DESC_DEF,QUANT_DEF,DATA_HORA_REG,OBS_DEF,QUANT_DEF_M1,QUANT_DEF_M2,VERSAO_MODIF from RP_OF_DEF_LIN WHERE ID_OP_LIN = "
					+ content[0] + " and ID_REF_ETIQUETA = " + content[1] + "").executeUpdate();
		}

	}

	@SuppressWarnings("unchecked")
	public void cria_RP_OF_OP_FUNC(Integer id_op_cab, Integer id_op_cab_novo) {
		List<Object[]> dados = entityManager.createNativeQuery(
				"SELECT ID_OP_FUNC,'' from RP_OF_OP_FUNC  WHERE ID_OP_CAB = " + id_op_cab + " ORDER BY ID_OP_FUNC")
				.getResultList();

		for (Object[] content : dados) {
			int q1 = entityManager.createNativeQuery("INSERT  INTO RP_OF_OP_FUNC "
					+ "(ID_OP_CAB,DATA_INI,HORA_INI,DATA_FIM,HORA_FIM,ID_EQUIPA,ID_UTZ_CRIA,NOME_UTZ_CRIA,PERFIL_CRIA,ID_UTZ_MODIF,NOME_UTZ_MODIF,PERFIL_MODIF,DATA_HORA_MODIF,ESTADO,DATA_INI_M1,HORA_INI_M1,DATA_FIM_M1,HORA_FIM_M1,DATA_INI_M2,HORA_INI_M2,DATA_FIM_M2,HORA_FIM_M2) "
					+ "select " + id_op_cab_novo
					+ ",DATA_INI,HORA_INI,DATA_FIM,HORA_FIM,ID_EQUIPA,ID_UTZ_CRIA,NOME_UTZ_CRIA,PERFIL_CRIA,ID_UTZ_MODIF,NOME_UTZ_MODIF,PERFIL_MODIF,DATA_HORA_MODIF,ESTADO,DATA_INI_M1,HORA_INI_M1,DATA_FIM_M1,HORA_FIM_M1,DATA_INI_M2,HORA_INI_M2,DATA_FIM_M2,HORA_FIM_M2 from RP_OF_OP_FUNC WHERE ID_OP_FUNC = "
					+ content[0] + " ").executeUpdate();
		}

	}

	// RP_OF_OP_FUNC***********************************
	@GET
	@Path("/getbyidRP_OF_OP_FUNC")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNC() {
		return dao11.allEntries();
	}

	@POST
	@Path("/createRP_OF_OP_FUNC")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_FUNC insertRP_OF_OP_FUNC(final RP_OF_OP_FUNC data) {
		return dao11.create(data);
	}

	@PUT
	@Path("/updateRP_OF_OP_FUNC")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_FUNC updateRP_OF_OP_FUNC(final RP_OF_OP_FUNC RP_OF_OP_FUNC) {
		RP_OF_OP_FUNC.setID_OP_FUNC(RP_OF_OP_FUNC.getID_OP_FUNC());
		return dao11.update(RP_OF_OP_FUNC);
	}

	@GET
	@Path("/checkuser/{user}")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> checkuser(@PathParam("user") String user) {
		return dao11.checkuser(user);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCid/{id}/{user}")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNCid(@PathParam("id") Integer id, @PathParam("user") String user) {
		return dao11.getbyallID_OP_CAB(id, user);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCusers/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNC_users(@PathParam("id") Integer id) {
		return dao11.getUsers(id);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCuser/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNC_user(@PathParam("id") Integer id) {
		return dao11.getUser(id);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCallusers/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNCall_users(@PathParam("id") Integer id) {
		return dao11.getallUsers(id);
	}

	@GET
	@Path("/getallUsersTEMPPREP/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_FUNC> getallUsersTEMPPREP(@PathParam("id") Integer id) {
		return dao11.getallUsersTEMPPREP(id);
	}

	// RP_OF_PARA_LIN******************************************

	@GET
	@Path("/getbyallID_OP_CAB/{id}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyallID_OP_CAB(@PathParam("id") Integer id) {
		return dao9.getbyallID_OP_CAB(id);
	}

	@GET
	@Path("/getbyallUSER/{id}/{user}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyallUSER(@PathParam("id") Integer id, @PathParam("user") String user) {
		return dao9.getbyallUser(id, user);
	}

	@GET
	@Path("/getbyallUSERIDOFCAB/{id}/{user}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyallUSERIDOFCAB(@PathParam("id") Integer id, @PathParam("user") String user) {
		return dao9.getbyallUserIDOFCAB(id, user);
	}

	@GET
	@Path("/getbyidRP_OF_PARA_LIN/{id}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyidRP_OF_PARA_LIN(@PathParam("id") Integer id) {
		return dao9.getbyid(id);
	}

	@GET
	@Path("/apagapausasbyid_op_cab/{id}")
	@Produces("application/json")
	public void apagapausasbyid_op_cab(@PathParam("id") Integer id) {
		Query query = entityManager.createNativeQuery(
				"delete RP_OF_PARA_LIN where ID_OP_CAB = " + id + " and DATA_FIM is null and DATA_FIM_M1 is null");

		query.executeUpdate();
	}

	@GET
	@Path("/getbyidRP_OF_PARA_LIN2/{id}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyidRP_OF_PARA_LIN2(@PathParam("id") Integer id) {
		return dao9.getbyid2(id);
	}

	@GET
	@Path("/getbyid_op_cabRP_OF_PARA_LIN/{id}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyid_op_cabRP_OF_PARA_LIN(@PathParam("id") Integer id) {
		return dao9.getbyid_op_cab(id);
	}

	@POST
	@Path("/createRP_OF_PARA_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_PARA_LIN insertRP_OF_PARA_LIN(final RP_OF_PARA_LIN data) {
		return dao9.create(data);
	}

	@PUT
	@Path("/updateRP_OF_PARA_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_PARA_LIN updateRP_OF_PARA_LIN(final RP_OF_PARA_LIN RP_OF_PARA_LIN) {
		RP_OF_PARA_LIN.setESTADO(RP_OF_PARA_LIN.getESTADO());
		return dao9.update(RP_OF_PARA_LIN);
	}

	// RP_OF_DEF_LIN********************************************

	@POST
	@Path("/createRP_OF_DEF_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_DEF_LIN insertRP_OF_DEF_LIN(final RP_OF_DEF_LIN data) {
		return dao5.create(data);
	}

	@GET
	@Path("/getbyidRP_OF_DEF_LIN/{id}/{id2}/{id_ref}")
	@Produces("application/json")
	public List<RP_OF_DEF_LIN> getbyRP_OF_DEF_LIN(@PathParam("id") String id, @PathParam("id2") Integer id2,
			@PathParam("id_ref") String id_ref) {
		return dao5.getbyid(id, id2, id_ref);
	}

	@GET
	@Path("/getbyidRP_OF_DEF_LINall/{id2}/{id_ref}")
	@Produces("application/json")
	public List<RP_OF_DEF_LIN> getbyRP_OF_DEF_LINall(@PathParam("id2") Integer id2,
			@PathParam("id_ref") String id_ref) {
		return dao5.getbyidall(id2, id_ref);
	}

	@GET
	@Path("/getbyidRP_OF_DEF_LINidoplin/{id}")
	@Produces("application/json")
	public List<RP_OF_DEF_LIN> getbyid_op_lin(@PathParam("id") Integer id) {
		return dao5.getbyid_op_lin(id);
	}

	@GET
	@Path("/getbyidRP_OF_DEF_LINidoplin_etiq/{id}/{id_etiq}")
	@Produces("application/json")
	public List<RP_OF_DEF_LIN> getbyid_op_lin_id_etiq(@PathParam("id") Integer id,
			@PathParam("id_etiq") Integer id_etiq) {
		return dao5.getbyid_op_lin_id_etiqueta(id, id_etiq);
	}

	@GET
	@Path("/getbyidDEF/{id}")
	@Produces("application/json")
	public List<RP_OF_DEF_LIN> getbyidDEF(@PathParam("id") Integer id) {
		return dao5.getbyidDEF(id);
	}

	@PUT
	@Path("/updateRP_OF_DEF_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_DEF_LIN updateRP_OF_DEF_LIN(final RP_OF_DEF_LIN RP_OF_DEF_LIN) {
		RP_OF_DEF_LIN.setQUANT_DEF(RP_OF_DEF_LIN.getQUANT_DEF());
		return dao5.update(RP_OF_DEF_LIN);
	}

	@DELETE
	@Path("/deleteRP_OF_DEF_LIN/{id}/{etiqueta}")
	public void deleteRP_OF_DEF_LIN(@PathParam("id") Integer id, @PathParam("etiqueta") Integer etiqueta) {
		dao5.apagar_id_op_lin(id, etiqueta);
	}

	@DELETE
	@Path("/deleteRP_OF_DEF_LIN2/{id}")
	public void apagar_id_DEF_LIN(@PathParam("id") Integer id) {
		dao5.apagar_id_DEF_LIN(id);
	}
	// RP_OF_OUTRODEF_LIN****************************************************

	@POST
	@Path("/createRP_OF_OUTRODEF_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OUTRODEF_LIN insertRP_OF_OUTRODEF_LIN(final RP_OF_OUTRODEF_LIN data) {
		return dao12.create(data);
	}

	@GET
	@Path("/getbyidRP_OF_OUTRODEF_LINF/{id}")
	@Produces("application/json")
	public List<RP_OF_OUTRODEF_LIN> getbyidRP_OF_OUTRODEF_LIN(@PathParam("id") Integer id) {
		return dao12.getbyid(id);
	}

	@GET
	@Path("/getbyidRP_OF_OUTRODEF_LINFetiqueta/{id}/{etiqueta}")
	@Produces("application/json")
	public List<RP_OF_OUTRODEF_LIN> getbyidRP_OF_OUTRODEF_LINetiqueta(@PathParam("id") Integer id,
			@PathParam("etiqueta") Integer etiqueta) {
		return dao12.getbyidetiqueta(id, etiqueta);
	}

	@PUT
	@Path("/updateRP_OF_OUTRODEF_LIN")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OUTRODEF_LIN updateRP_OF_OUTRODEF_LIN(final RP_OF_OUTRODEF_LIN RP_OF_OUTRODEF_LIN) {
		RP_OF_OUTRODEF_LIN.setQUANT_OUTRODEF(RP_OF_OUTRODEF_LIN.getQUANT_OUTRODEF());
		return dao12.update(RP_OF_OUTRODEF_LIN);
	}

	// RP_OF_OP_ETIQUETA***************************************************************
	@POST
	@Path("/createRP_OF_OP_ETIQUETA")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_ETIQUETA insertRP_OF_OP_ETIQUETA(final RP_OF_OP_ETIQUETA data) {
		return dao13.create(data);
	}

	@GET
	@Path("/getRP_OF_OP_ETIQUETA")
	@Produces("application/json")
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETA() {
		return dao13.allEntries();
	}

	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETA_id(@PathParam("id") Integer id) {
		return dao13.getbyid(id);
	}

	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid/{id}/{id2}")
	@Produces("application/json")
	public List<RP_OF_OP_ETIQUETA> getbyid_etiqueta(@PathParam("id") Integer ID_OP_LIN,
			@PathParam("id2") Integer ID_REF_ETIQUETA) {
		return dao13.getbyid_etiqueta(ID_REF_ETIQUETA, ID_OP_LIN);
	}

	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid_op_lin/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETAbyid_op_lin(@PathParam("id") Integer id) {
		return dao13.getbyid_oplin(id);
	}

	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid_op_lindef/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETAbyid_op_lindef(@PathParam("id") Integer id) {
		return dao13.getbyid_oplindef(id);
	}

	@DELETE
	@Path("/deleteRP_OF_OP_ETIQUETA/{id}")
	public void deleteRP_OF_OP_ETIQUETA(@PathParam("id") Integer id) {
		RP_OF_OP_ETIQUETA RP_OF_OP_ETIQUETA = new RP_OF_OP_ETIQUETA();
		RP_OF_OP_ETIQUETA.setID_REF_ETIQUETA(id);
		dao13.delete(RP_OF_OP_ETIQUETA);
	}

	@GET
	@Path("/apagarRP_OF_OP_ETIQUETA/{id}")
	public void apagarRP_OF_OP_ETIQUETA(@PathParam("id") Integer id) {
		int update_RP_ETIQ = entityManager
				.createNativeQuery(
						"UPDATE RP_OF_OP_ETIQUETA SET ATIVO = 0,APAGADO = 1 WHERE ID_REF_ETIQUETA = " + id + "")
				.executeUpdate();

	}

	@PUT
	@Path("/updateRP_OF_OP_ETIQUETA")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_OP_ETIQUETA updateRP_OF_OP_ETIQUETA(final RP_OF_OP_ETIQUETA RP_OF_OP_ETIQUETA) {
		RP_OF_OP_ETIQUETA.setQUANT_BOAS(RP_OF_OP_ETIQUETA.getQUANT_BOAS());
		return dao13.update(RP_OF_OP_ETIQUETA);
	}

	// RP_OF_LST_DEF***************************************************************
	@POST
	@Path("/createRP_OF_LST_DEF")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_OF_LST_DEF insertRP_OF_LST_DEF(final RP_OF_LST_DEF data) {
		return dao14.create(data);
	}

	@GET
	@Path("/getRP_OF_LST_DEF")
	@Produces("application/json")
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEF() {
		return dao14.allEntries();
	}

	@GET
	@Path("/getRP_OF_LST_DEFid/{id}")
	@Produces("application/json")
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEF_id(@PathParam("id") Integer id) {
		return dao14.getbyid(id);
	}

	@GET
	@Path("/getRP_OF_LST_DEFgetallfam")
	@Produces("application/json")
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEFgetallfam() {
		return dao14.getallfam();
	}

	@POST
	@Path("/getRP_OF_LST_DEFgetfam")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEFgetfam(final ArrayList<String> data) {
		return dao14.getfam(data);
	}

	@GET
	@Path("/getRP_OF_LST_DEFbyid_op_lin/{id}")
	@Produces("application/json")
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEFbyid_op_lin(@PathParam("id") Integer id) {
		return dao14.getdef(id);
	}

	@DELETE
	@Path("/deleteRP_OF_LST_DEF/{id}")
	public void deleteRP_OF_LST_DEF(@PathParam("id") Integer id) {
		RP_OF_LST_DEF RP_OF_LST_DEF = new RP_OF_LST_DEF();
		RP_OF_LST_DEF.setID_LST_DEF(id);
		dao14.delete(RP_OF_LST_DEF);
	}

	@DELETE
	@Path("/deleteRP_OF_LST_DEFid_op_lin/{id}")
	public void deleteRP_OF_LST_DEFid_op_lin(@PathParam("id") Integer id) {
		dao14.apagar_idop_lin(id);
	}

	// VERSAO_APP*************************************************
	@GET
	@Path("/getVERSAO_APP")
	@Produces("application/json")
	public List<VERSAO_APP> getVERSAO_APPO() {
		return dao16.getallv();
	}

	// GER_EVENTO***************************************************************
	@POST
	@Path("/createGER_EVENTO")
	@Consumes("*/*")
	@Produces("application/json")
	public GER_EVENTO insertGER_EVENTO(final GER_EVENTO data) {
		return dao15.create(data);
	}

	@GET
	@Path("/getGER_EVENTO")
	@Produces("application/json")
	public List<GER_EVENTO> getGER_EVENTO() {
		return dao15.getall();
	}

	@GET
	@Path("/getGER_EVENTObyidOrigem/{id}/{campo}")
	@Produces("application/json")
	public List<GER_EVENTO> getGER_EVENTO_idOrigem(@PathParam("id") Integer id, @PathParam("campo") String campo) {
		return dao15.getbyidOrigem(id, campo);
	}

	@GET
	@Path("/getGER_EVENTObyid/{id}")
	@Produces("application/json")
	public List<GER_EVENTO> getGER_EVENTO_id(@PathParam("id") Integer id) {
		return dao15.getbyid(id);
	}

	@DELETE
	@Path("/deleteGER_EVENTO/{id}")
	public void deleteGER_EVENTO(@PathParam("id") Integer id) {
		GER_EVENTO GER_EVENTO = new GER_EVENTO();
		GER_EVENTO.setID_EVENTO(id);
		dao15.delete(GER_EVENTO);
	}

	@PUT
	@Path("/updateGER_EVENTO")
	@Consumes("*/*")
	@Produces("application/json")
	public GER_EVENTO updateGER_EVENTO(final GER_EVENTO GER_EVENTO) {
		GER_EVENTO.setMENSAGEM(GER_EVENTO.getMENSAGEM());
		return dao15.update(GER_EVENTO);
	}

	// ST_PEDIDOS***************************************************************
	@POST
	@Path("/createST_PEDIDOS")
	@Consumes("*/*")
	@Produces("application/json")
	public ST_PEDIDOS insertST_PEDIDOS(final ST_PEDIDOS data) {
		Query query = entityManager.createNativeQuery("select b.COD_SECTOR,b.DES_SECTOR from RH_FUNCIONARIOS a "
				+ "INNER join RH_SECTORES b on a.COD_SECTOR = b.COD_SECTOR " + "where a.COD_FUNCIONARIO = "
				+ data.getUTZ_CRIA());

		if (data.getCOD_SECTOR() == null) {

			List<Object[]> dados = query.getResultList();
			Integer cod = null;
			String desc = null;
			for (Object[] content : dados) {
				cod = (content[0] == null) ? null : Integer.parseInt(content[0].toString());
				desc = (content[1] == null) ? null : content[1].toString();
			}
			data.setCOD_SECTOR(cod);
			data.setDES_SECTOR(desc);
		}
		return dao18.create(data);
	}

	@GET
	@Path("/getST_PEDIDOS")
	@Produces("application/json")
	public List<ST_PEDIDOS> getST_PEDIDOS() {
		return dao18.getall();
	}

	@GET
	@Path("/getST_PEDIDOSbyid/{id}")
	@Produces("application/json")
	public List<ST_PEDIDOS> getST_PEDIDOS_id(@PathParam("id") Integer id) {
		return dao18.getbyid(id);
	}

	@DELETE
	@Path("/deleteST_PEDIDOS/{id}")
	public void deleteST_PEDIDOS(@PathParam("id") Integer id) {
		ST_PEDIDOS ST_PEDIDOS = new ST_PEDIDOS();
		ST_PEDIDOS.setID_PEDIDO(id);
		dao18.delete(ST_PEDIDOS);
	}

	@PUT
	@Path("/updateST_PEDIDOS")
	@Consumes("*/*")
	@Produces("application/json")
	public ST_PEDIDOS updateST_PEDIDOS(final ST_PEDIDOS ST_PEDIDOS) {
		ST_PEDIDOS.setID_PEDIDO(ST_PEDIDOS.getID_PEDIDO());
		return dao18.update(ST_PEDIDOS);
	}

	// RP_CAIXAS_INCOMPLETAS***************************************************************
	@POST
	@Path("/createRP_CAIXAS_INCOMPLETAS")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CAIXAS_INCOMPLETAS insertRP_CAIXAS_INCOMPLETAS(final RP_CAIXAS_INCOMPLETAS data) {
		return dao19.create(data);
	}

	@GET
	@Path("/getRP_CAIXAS_INCOMPLETAS")
	@Produces("application/json")
	public List<RP_CAIXAS_INCOMPLETAS> getRP_CAIXAS_INCOMPLETAS() {
		return dao19.getall();
	}

	@GET
	@Path("/getRP_CAIXAS_INCOMPLETASbyid/{id}")
	@Produces("application/json")
	public List<RP_CAIXAS_INCOMPLETAS> getRP_CAIXAS_INCOMPLETAS_id(@PathParam("id") Integer id) {
		return dao19.getbyid(id);
	}

	@GET
	@Path("/getRP_CAIXAS_INCOMPLETASbyid_of_cab/{id}")
	@Produces("application/json")
	public List<RP_CAIXAS_INCOMPLETAS> getRP_CAIXAS_INCOMPLETASbyid_of_cab(@PathParam("id") Integer id) {
		return dao19.getbyid_of_cab(id);
	}

	@DELETE
	@Path("/deleteRP_CAIXAS_INCOMPLETAS/{id}")
	public void deleteRP_CAIXAS_INCOMPLETAS(@PathParam("id") Integer id) {
		RP_CAIXAS_INCOMPLETAS RP_CAIXAS_INCOMPLETAS = new RP_CAIXAS_INCOMPLETAS();
		RP_CAIXAS_INCOMPLETAS.setID_CAIXA_INCOMPLETA(id);
		dao19.delete(RP_CAIXAS_INCOMPLETAS);
	}

	@PUT
	@Path("/updateRP_CAIXAS_INCOMPLETAS")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CAIXAS_INCOMPLETAS updateRP_CAIXAS_INCOMPLETAS(final RP_CAIXAS_INCOMPLETAS RP_CAIXAS_INCOMPLETAS) {
		RP_CAIXAS_INCOMPLETAS.setID_CAIXA_INCOMPLETA(RP_CAIXAS_INCOMPLETAS.getID_CAIXA_INCOMPLETA());
		return dao19.update(RP_CAIXAS_INCOMPLETAS);
	}

	// Atualizar campos M1 quando clica no bot�o editar
	@GET
	@Path("/atualizarcampos/{id}")
	@Produces("application/json")
	public int atualizarcampos(@PathParam("id") Integer id) {

		Query query = entityManager.createNativeQuery(
				"UPDATE RP_OF_DEF_LIN SET QUANT_DEF_M1 = QUANT_DEF_M2  where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ") "
						+ "UPDATE RP_OF_OP_CAB SET TEMPO_PARA_TOTAL_M1 = TEMPO_PARA_TOTAL_M2  , TEMPO_EXEC_TOTAL_M1 = TEMPO_EXEC_TOTAL_M2 , TEMPO_PREP_TOTAL_M1 = TEMPO_PREP_TOTAL_M2  WHERE ID_OF_CAB = "
						+ id + " "
						+ "UPDATE RP_OF_OP_ETIQUETA SET NOVO = 0,APAGADO = 0, QUANT_BOAS_M1 = QUANT_BOAS_M2 , QUANT_DEF_M1 = QUANT_DEF_M2  where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ")"
						+ "UPDATE RP_OF_OP_FUNC SET DATA_FIM_M1 = DATA_FIM_M2  , DATA_INI_M1 = DATA_INI_M2  , HORA_FIM_M1 = HORA_FIM_M2  , HORA_INI_M1 = HORA_INI_M2 where ID_OP_FUNC in (select c.ID_OP_FUNC from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_FUNC c on c.ID_OP_CAB = b.ID_OP_CAB where a.ID_OF_CAB = "
						+ id + " ) "
						+ "UPDATE RP_OF_PARA_LIN SET  TIPO_PARAGEM_M1 = TIPO_PARAGEM_M2, DES_PARAGEM_M1 = DES_PARAGEM_M2, MOMENTO_PARAGEM_M1 = MOMENTO_PARAGEM_M2 ,DATA_FIM_M1 = DATA_FIM_M2  , DATA_INI_M1 = DATA_INI_M2  , HORA_FIM_M1 = HORA_FIM_M2  , HORA_INI_M1 = HORA_INI_M2 where ID_OP_CAB in (select b.ID_OP_CAB from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB where a.ID_OF_CAB = "
						+ id + " ) "
						+ "UPDATE RP_OF_PREP_LIN SET DATA_FIM_M1 = DATA_FIM_M2  , DATA_INI_M1 = DATA_INI_M2  , HORA_FIM_M1 = HORA_FIM_M2  , HORA_INI_M1 = HORA_INI_M2 where ID_OP_CAB in (select b.ID_OP_CAB from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB where a.ID_OF_CAB = "
						+ id + " ) "
						+ "UPDATE RP_OF_OP_LIN SET QUANT_BOAS_TOTAL_M1 = QUANT_BOAS_TOTAL_M2 , QUANT_DEF_TOTAL_M1 = QUANT_DEF_TOTAL_M2 where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ")");

		int dados = query.executeUpdate();

		return dados;
	}

	public int atualizarcampos2(Integer id) {

		Query query = entityManager.createNativeQuery(
				"UPDATE RP_OF_DEF_LIN SET QUANT_DEF_M2 = QUANT_DEF_M1  where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ") "
						+ "UPDATE RP_OF_OP_CAB SET TEMPO_PARA_TOTAL_M2 = TEMPO_PARA_TOTAL_M1  , TEMPO_EXEC_TOTAL_M2 = TEMPO_EXEC_TOTAL_M1 , TEMPO_PREP_TOTAL_M2 = TEMPO_PREP_TOTAL_M1  WHERE ID_OF_CAB = "
						+ id + " "
						+ "UPDATE RP_OF_OP_ETIQUETA SET QUANT_BOAS_M2 = QUANT_BOAS_M1 , QUANT_DEF_M2 = QUANT_DEF_M1  where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ") and NOVO != 1"
						+ "UPDATE RP_OF_OP_FUNC SET DATA_FIM_M2 = DATA_FIM_M1  , DATA_INI_M2 = DATA_INI_M1  , HORA_FIM_M2 = HORA_FIM_M1  , HORA_INI_M2 = HORA_INI_M1 where ID_OP_FUNC in (select c.ID_OP_FUNC from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_FUNC c on c.ID_OP_CAB = b.ID_OP_CAB where a.ID_OF_CAB = "
						+ id + " ) "
						+ "UPDATE RP_OF_PARA_LIN SET  TIPO_PARAGEM_M2 = TIPO_PARAGEM_M1, DES_PARAGEM_M2= DES_PARAGEM_M1, MOMENTO_PARAGEM_M2 = MOMENTO_PARAGEM_M1 ,DATA_FIM_M2 = DATA_FIM_M1  , DATA_INI_M2 = DATA_INI_M1  , HORA_FIM_M2 = HORA_FIM_M1  , HORA_INI_M2 = HORA_INI_M1 where ID_OP_CAB in (select b.ID_OP_CAB from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB where a.ID_OF_CAB = "
						+ id + " ) "
						+ "UPDATE RP_OF_PREP_LIN SET DATA_FIM_M2 = DATA_FIM_M1  , DATA_INI_M2 = DATA_INI_M1  , HORA_FIM_M2 = HORA_FIM_M1  , HORA_INI_M2 = HORA_INI_M1 where ID_OP_CAB in (select b.ID_OP_CAB from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB where a.ID_OF_CAB = "
						+ id + " ) "
						+ "UPDATE RP_OF_OP_LIN SET QUANT_BOAS_TOTAL_M2 = QUANT_BOAS_TOTAL_M1 , QUANT_DEF_TOTAL_M2 = QUANT_DEF_TOTAL_M1 where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ")");

		int dados = query.executeUpdate();

		return dados;
	}

	@GET
	@Path("/verificaopnum/{id}")
	@Produces("application/json")
	public List<RP_OF_OP_ETIQUETA> verificaopnum(@PathParam("id") Integer id) {
		return dao13.verificaopnum(id);
	}

	@GET
	@Path("/atualizaropenum/{id}")
	@Produces("application/json")
	public void atualizaropenum(@PathParam("id") Integer id) {
		atualiza_editar(id, getURL());
	}

	@GET
	@Path("/testeligacao")
	@Produces("application/json")
	public boolean testeligacao() {
		return true;
	}

	@GET
	@Path("/atualizarestado/{id}/{user}/{estado}")
	@Produces("application/json")
	public void atualizarestado(@PathParam("id") Integer id, @PathParam("user") String user,
			@PathParam("estado") String estado) {
		entityManager.createNativeQuery("UPDATE RP_OF_OP_FUNC SET ESTADO = '" + estado
				+ "' ,DATA_HORA_MODIF = GETDATE(),ID_UTZ_MODIF = '" + user
				+ "' WHERE ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB =  " + id + ")")
				.executeUpdate();
		String query_user = "";
		if (estado.equals("R")) {
			query_user = ",ID_UTZ_EDICAO = '" + user + "' ,ESTADO_INICIAL = ESTADO";
		} else /* if (estado.equals("C")) */ {
			atualizarcampos2(id);
		}
		entityManager.createNativeQuery(
				"UPDATE RP_OF_CAB SET ESTADO = '" + estado + "',DATA_HORA_MODIF = GETDATE(),ID_UTZ_MODIF = '" + user
						+ "' " + query_user + " WHERE ID_OF_CAB = " + id + "")
				.executeUpdate();
	}

	@GET
	@Path("/atualizartotais/{id}/{modo}")
	@Produces("application/json")
	public void atualizartotais(@PathParam("id") Integer id, @PathParam("modo") Boolean modo) {
		String query = "";
		String query2 = "";
		String query3 = "";
		if (modo) {
			query = " QUANT_BOAS_TOTAL_M2 = r.boas, QUANT_DEF_TOTAL_M2 = r.def ";
			query2 = "QUANT_DEF_TOTAL_M2 = r.def ";
			query3 = "QUANT_DEF_M2 = r.def ";
		} else {
			query = "QUANT_BOAS_TOTAL = r.boas,QUANT_BOAS_TOTAL_M1 = r.boas, QUANT_BOAS_TOTAL_M2 = r.boas,QUANT_DEF_TOTAL = r.def,QUANT_DEF_TOTAL_M1 = r.def, QUANT_DEF_TOTAL_M2 = r.def ";
			query2 = "QUANT_DEF_TOTAL = r.def,QUANT_DEF_TOTAL_M1 = r.def, QUANT_DEF_TOTAL_M2 = r.def ";
			query3 = "QUANT_DEF = r.def,QUANT_DEF_M1 = r.def, QUANT_DEF_M2 = r.def ";
		}

		entityManager.createNativeQuery(
				"DELETE RP_OF_DEF_LIN WHERE ID_DEF_LIN in (SELECT T2.ID_DEF_LIN FROM RP_OF_DEF_LIN T1,RP_OF_DEF_LIN T2 "
						+ "WHERE  T1.ID_DEF_LIN < T2.ID_DEF_LIN AND  T1.COD_DEF = T2.COD_DEF AND  T1.ID_OP_LIN = T2.ID_OP_LIN "
						+ "AND T1.ID_OP_LIN = (select top 1 ID_OP_LIN from RP_OF_OP_LIN where ID_OP_CAB = " + id
						+ ") )")
				.executeUpdate();

		entityManager.createNativeQuery(
				"DELETE RP_OF_DEF_LIN WHERE ID_DEF_LIN in (SELECT T2.ID_DEF_LIN FROM RP_OF_DEF_LIN T1,RP_OF_DEF_LIN T2 "
						+ "WHERE  T1.ID_DEF_LIN < T2.ID_DEF_LIN AND  T1.COD_DEF = T2.COD_DEF AND  T1.ID_OP_LIN = T2.ID_OP_LIN AND T1.ID_REF_ETIQUETA = T2 .ID_REF_ETIQUETA AND T1.ID_REF_ETIQUETA is not null  AND T2.ID_REF_ETIQUETA is not null "
						+ "AND T1.ID_OP_LIN in (select ID_OP_LIN from RP_OF_OP_LIN where ID_OP_CAB in ( "
						+ "select ID_OP_CAB from  RP_OF_OP_CAB a inner join RP_OF_CAB b on a.ID_OF_CAB = b.ID_OF_CAB "
						+ "where b.ID_OF_CAB_ORIGEM in (select ID_OF_CAB from RP_OF_OP_CAB where ID_OP_CAB = " + id
						+ ") )))")
				.executeUpdate();

		entityManager
				.createNativeQuery("UPDATE tt  set " + query + " from "
						+ "(select ID_OP_CAB, (select coalesce(SUM(dd.QUANT_DEF_M2),0) from RP_OF_DEF_LIN dd inner join   RP_OF_OP_ETIQUETA ee on dd.ID_OP_LIN = ee.ID_OP_LIN and dd.ID_REF_ETIQUETA = ee.ID_REF_ETIQUETA where dd.ID_OP_LIN = a.ID_OP_LIN and ee.ATIVO = 1) as def "
						+ ",(select coalesce(SUM(x.QUANT_BOAS_M2),0) from RP_OF_OP_ETIQUETA x where ID_OP_LIN = a.ID_OP_LIN and x.ATIVO = 1) as boas from RP_OF_OP_LIN a "
						+ "where ID_OP_CAB in (Select b.ID_OP_CAB from RP_OF_OP_CAB b "
						+ "inner join RP_OF_CAB c on b.ID_OF_CAB = c.ID_OF_CAB where c.ID_OF_CAB_ORIGEM in  (select ID_OF_CAB from RP_OF_OP_CAB where ID_OP_CAB = "
						+ id + ") )) as r ,RP_OF_OP_LIN as tt where tt.ID_OP_CAB = r.ID_OP_CAB")
				.executeUpdate();
		entityManager.createNativeQuery("UPDATE tt  set " + query2 + " from "
				+ "(select ID_OP_LIN, (select coalesce(SUM(QUANT_DEF_M2),0) from RP_OF_DEF_LIN where ID_OP_LIN = a.ID_OP_LIN) as def "
				+ "from RP_OF_OP_LIN a " + "where ID_OP_CAB = " + id
				+ ") as r ,RP_OF_OP_LIN as tt where tt.ID_OP_LIN = r.ID_OP_LIN").executeUpdate();

		entityManager
				.createNativeQuery("UPDATE tt  set " + query3 + " from "
						+ "(select x.ID_REF_ETIQUETA, (select coalesce(SUM(QUANT_DEF_M2),0) from RP_OF_DEF_LIN where ID_OP_LIN = a.ID_OP_LIN and ID_REF_ETIQUETA = x.ID_REF_ETIQUETA ) as def "
						+ "from RP_OF_OP_LIN a " + "inner join RP_OF_OP_ETIQUETA x on a.ID_OP_LIN = x.ID_OP_LIN "
						+ "where ID_OP_CAB in (Select b.ID_OP_CAB from RP_OF_OP_CAB b "
						+ "inner join RP_OF_CAB c on b.ID_OF_CAB = c.ID_OF_CAB where c.ID_OF_CAB_ORIGEM in  (select ID_OF_CAB from RP_OF_OP_CAB where ID_OP_CAB = "
						+ id + ") )) as r ,RP_OF_OP_ETIQUETA as tt where tt.ID_REF_ETIQUETA = r.ID_REF_ETIQUETA")
				.executeUpdate();

	}

	// ATUALIZA OP_NUM
	// **************************************************************
	public void atualiza_editar(Integer id, String url) {
		Query query = entityManager.createNativeQuery(
				"select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM,OP_NUM from RP_OF_CAB where ID_OF_CAB = " + id
						+ " or ID_OF_CAB_ORIGEM = " + id);

		List<Object[]> dados = query.getResultList();

		for (Object[] content : dados) {

			if (content[1] == null) {
				Integer id_origem = Integer.parseInt(content[0].toString());
				Query query3 = entityManager.createNativeQuery(
						"select ID_OP_LIN,REF_NUM from RP_OF_OP_LIN where ID_OP_CAB in (select xx.ID_OP_CAB from RP_OF_OP_CAB xx where xx.ID_OF_CAB = "
								+ id_origem + ")");
				List<Object[]> dados3 = query3.getResultList();
				for (Object[] content3 : dados3) {
					atualiza(id_origem, "PF", null, url);
				}
			} else {
				Integer id_origem = Integer.parseInt(content[1].toString());

				Query query2 = entityManager.createNativeQuery(
						"select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA,c.OP_NUM,b.ID_OP_LIN  from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where a.ID_OF_CAB = "
								+ Integer.parseInt(content[0].toString()));

				List<Object[]> dados2 = query2.getResultList();

				for (Object[] content2 : dados2) {
					Integer etiqueta = Integer.parseInt(content2[2].toString());
					atualiza(etiqueta, "C", null, url);
				}

			}

		}

	}

	public String atualiza(Integer id, String tipo, Integer estado, String url) {
		String estad = "";
		if (estado != null)
			estad = "and ESTADO = 0";

		Query query = entityManager.createNativeQuery(
				"select RESCOD,DATDEB,PROREF,OFNUM,OPECOD,ID,HEUDEB from RP_AUX_OPNUM where ID_CAMPO = " + id
						+ " and TIPO = '" + tipo + "' " + estad + "");
		List<Object[]> dados = query.getResultList();
		ConnectProgress connectionProgress = new ConnectProgress();
		String op_num = null;
		for (Object[] content : dados) {

			try {
				op_num = connectionProgress.GetOP_NUM(content[0].toString(), content[1].toString(),
						content[2].toString(), content[3].toString(), content[4].toString(), url,
						content[6].toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (op_num != null && !op_num.isEmpty()) {
				if (tipo.equals("C")) {
					int update_RP_ETIQ = entityManager.createNativeQuery(
							"UPDATE RP_OF_OP_ETIQUETA SET OP_NUM = " + op_num + " WHERE ID_REF_ETIQUETA = " + id + "")
							.executeUpdate();
					int up1 = entityManager.createNativeQuery(
							"UPDATE RP_AUX_OPNUM SET ESTADO = 1, DATA_MODIFICACAO = GETDATE() WHERE ID = " + content[5]
									+ "")
							.executeUpdate();
				} else {
					int update_RP_OF = entityManager
							.createNativeQuery(
									"UPDATE RP_OF_CAB SET OP_NUM = " + op_num + " WHERE ID_OF_CAB = " + id + "")
							.executeUpdate();
					int up2 = entityManager.createNativeQuery(
							"UPDATE RP_AUX_OPNUM SET ESTADO = 1 ,  DATA_MODIFICACAO = GETDATE() WHERE ID = "
									+ content[5] + "")
							.executeUpdate();
				}
			}
		}

		return op_num;

	}

	public String getURL() {
		String url = "";
		Query query_folder = entityManager.createNativeQuery("select top 1 * from GER_PARAMETROS a");

		List<Object[]> dados_folder = query_folder.getResultList();

		for (Object[] content : dados_folder) {
			url = content[2].toString();
		}

		return url;
	}

	@POST
	@Path("/getANALISERAPIDA")
	@Consumes("*/*")
	@Produces("application/json")
	public List<HashMap<String, String>> allopNOTIN(final List<HashMap<String, String>> data) {

		HashMap<String, String> firstMap = data.get(0);
		String OF_NUM = firstMap.get("OF_NUM");
		String MAQ_NUM = firstMap.get("MAQ_NUM");
		String ID_UTZ_CRIA = firstMap.get("ID_UTZ_CRIA");
		String OP_NUM = firstMap.get("OP_NUM");

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Query query = entityManager.createNativeQuery("select "
				+ "d.ID_UTZ_CRIA,AVG(CASE WHEN DATEPART(HOUR, CONVERT( TIME,b.TEMPO_EXEC_TOTAL_M2)) = 0 THEN 0 ELSE c.QUANT_BOAS_TOTAL_M2/DATEPART(HOUR, CONVERT( TIME,b.TEMPO_EXEC_TOTAL_M2)) END) as TimeStampHour "
				+ "from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on a.ID_OF_CAB = b.ID_OF_CAB "
				+ "inner join RP_OF_OP_LIN c on b.ID_OP_CAB = c.ID_OP_CAB "
				+ "inner join RP_OF_OP_FUNC d on b.ID_OP_CAB = d.ID_OP_CAB " + "where a.ID_OF_CAB_ORIGEM is null "
				+ "and a.OP_COD_ORIGEM = 85 "
				+ " and a.MAQ_NUM = '032'and a.OF_NUM = 1806322495 and d.DATA_INI_M2 >= DATEADD(MONTH, -3, GETDATE()) GROUP BY d.ID_UTZ_CRIA");

		List<Object[]> dados = query.getResultList();

		for (Object[] content : dados) {
			HashMap<String, String> x = new HashMap<>();
			x.put("ID_UTZ_CRIA", content[0].toString());
			x.put("MEDIA", content[1].toString());
			list.add(x);
		}
		return list;
	}

	// CRIAR
	// FICHEIRO****************************************************************

	@POST
	@Path("/ficheiroManual/{todos}")
	@Produces("application/zip")
	public Response getFicheiroManual(final List<String> dados_of, @PathParam("todos") Boolean todos)
			throws IOException, ParseException {

		String data = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
		String data_file = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
		String url = getURL();
		String estado = "C";
		Boolean ficheirosdownload = true;

		Boolean pausa = true;
		Integer comp_num = 1;

		String nome_ficheiro = "";
		for (String content_of : dados_of) {

			ConnectProgress connectionProgress = new ConnectProgress();
			String op_num = null;

			if (op_num == null || op_num.isEmpty()) {

				Query query = entityManager.createNativeQuery(
						"select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM,OP_NUM from RP_OF_CAB where ID_OF_CAB = "
								+ content_of + " or ID_OF_CAB_ORIGEM = " + content_of);

				List<Object[]> dados = query.getResultList();

				for (Object[] content : dados) {
					data = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
					String inform_file = "";
					Integer total = 1;
					// se for PF cria ficheiro (se o estado for modifica��o
					// cria
					// 2)
					if (content[1] == null && todos) {
						Integer id_origem = Integer.parseInt(content[0].toString());
						Query query3 = entityManager.createNativeQuery(
								"select ID_OP_LIN,REF_NUM from RP_OF_OP_LIN where ID_OP_CAB in (select xx.ID_OP_CAB from RP_OF_OP_CAB xx where xx.ID_OF_CAB = "
										+ id_origem + ")");
						List<Object[]> dados3 = query3.getResultList();
						total = dados3.size();
						for (Object[] content3 : dados3) {

							inform_file = content[2].toString() + "_" + content3[1].toString() + "_PF";
							String inform_file2 = "";

							String OPNUM = (content[4] == null) ? "NULL" : content[4].toString();

							if (estado.equals("M")) {
								OPNUM = atualiza(id_origem, "PF", 0, url);
								nome_ficheiro = "correcao" + data + inform_file + ".txt";
								inform_file2 = content[2].toString() + "_correcao_PAUSA";

								if (OPNUM == null)
									OPNUM = (content[4] == null) ? "NULL" : content[4].toString();
								criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
										"P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total,
										ficheirosdownload, data, null, estado, true, null);
								criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
										estado, data + inform_file2, OPNUM, content3[0].toString(), false, total,
										ficheirosdownload, data_file, null, estado, true, null);

							} else {
							}
							inform_file2 = content[2].toString() + "_PAUSA";

							nome_ficheiro = data + inform_file + ".txt";
							if (estado.equals("A")) {
								nome_ficheiro = "anulacao_" + nome_ficheiro;
							}
							criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
									"P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total,
									ficheirosdownload, data_file, null, estado, true, null);
							pausa = false;
							criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
									estado, data + inform_file2, OPNUM, content3[0].toString(), pausa, total,
									ficheirosdownload, data_file, null, estado, true, null);

							// se for COMP verifica se exitem etiquetas para
							// o
							// comp
							// e cria
							// ficheiro
						}
					} else {
						Integer id_origem = Integer.parseInt(content[1].toString());
						String data_query = "";
						if (estado.equals("M"))
							data_query = " and  (c.VERSAO_MODIF != (select VERSAO_MODIF from RP_OF_CAB where ID_OF_CAB = "
									+ id_origem
									+ ") or (c.QUANT_BOAS_M1 != c.QUANT_BOAS_M2 or c.QUANT_DEF_M1 != c.QUANT_DEF_M2 or c.NOVO = 1 or c.APAGADO = 1)) ";
						Query query2 = entityManager.createNativeQuery(
								"select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA,c.OP_NUM,b.ID_OP_LIN,c.NOVO,c.ATIVO,c.APAGADO  from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where b.TIPO_PECA != 'COM' and a.ID_OF_CAB = "
										+ Integer.parseInt(content[0].toString()) + data_query);

						List<Object[]> dados2 = query2.getResultList();
						Integer etiqueta_num = 1;
						for (Object[] content2 : dados2) {
							inform_file = content[2].toString() + "_C" + comp_num + "E" + etiqueta_num;
							etiqueta_num++;
							Integer etiqueta = Integer.parseInt(content2[2].toString());
							Integer id_of_cab = Integer.parseInt(content2[1].toString());
							String OPNUM = (content2[3] == null) ? "NULL" : content2[3].toString();
							String novaet = (content2[5] != null) ? content2[5].toString() : "0";
							String ativo = (content2[6] != null) ? content2[6].toString() : "0";
							String apagado = (content2[7] != null) ? content2[7].toString() : "0";
							if (novaet.equals("true")) {
								novaet = "1";
							}

							Boolean manual = false;
							if (todos) {
								manual = true;
							}

							if (estado.equals("M") && !novaet.equals("1")
									&& ((!ativo.equals("true") && apagado.equals("true"))
											|| (ativo.equals("true") && !apagado.equals("true")))) {
								OPNUM = atualiza(etiqueta, "C", 0, url);
								nome_ficheiro = "correcao" + data + inform_file + ".txt";
								if (OPNUM == null)
									OPNUM = (content2[3] == null) ? "NULL" : content2[3].toString();
								criarFicheiro(id_of_cab, 1, nome_ficheiro, "COMP", content2[0].toString(), id_origem,
										etiqueta, estado, null, OPNUM, content2[4].toString(), false, 1,
										ficheirosdownload, data_file, novaet, estado, manual, null);
							}

							nome_ficheiro = data + inform_file + ".txt";
							if (estado.equals("A")) {
								nome_ficheiro = "anulacao_" + nome_ficheiro;
							}
							if (ativo.equals("true")) {
								criarFicheiro(id_of_cab, 2, nome_ficheiro, "COMP", content2[0].toString(), id_origem,
										etiqueta, estado, null, OPNUM, content2[4].toString(), false, 1,
										ficheirosdownload, data_file, novaet, estado, manual, null);
							}
						}
						if (dados2.size() > 0)
							comp_num++;
					}

				}
			}

		}

		if (ficheirosdownload) {

			final File file = new File("c:/sgiid/temp_files/" + data_file + ".zip");
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=ficheiros.zip");
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					file.delete();
				}
			}, 5000);
			return response.build();

		} else {
			return null;
		}

	}

	
	@POST
	@Path("/ficheiroManualDestino/{todos}")
	@Produces("application/zip")
	public Response getFicheiroManualDestino(final List<String> dados_of, @PathParam("todos") Boolean todos)
			throws IOException, ParseException {

		String data = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
		String data_file = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
		String url = getURL();
		String estado = "C";
		Boolean ficheirosdownload = false;

		Boolean pausa = true;
		Integer comp_num = 1;

		String nome_ficheiro = "";
		for (String content_of : dados_of) {

			ConnectProgress connectionProgress = new ConnectProgress();
			String op_num = null;

			if (op_num == null || op_num.isEmpty()) {

				Query query = entityManager.createNativeQuery(
						"select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM,OP_NUM from RP_OF_CAB where ID_OF_CAB = "
								+ content_of + " or ID_OF_CAB_ORIGEM = " + content_of);

				List<Object[]> dados = query.getResultList();

				for (Object[] content : dados) {
					data = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
					String inform_file = "";
					Integer total = 1;
					// se for PF cria ficheiro (se o estado for modifica��o
					// cria
					// 2)
					if (content[1] == null && todos) {
						Integer id_origem = Integer.parseInt(content[0].toString());
						Query query3 = entityManager.createNativeQuery(
								"select ID_OP_LIN,REF_NUM from RP_OF_OP_LIN where ID_OP_CAB in (select xx.ID_OP_CAB from RP_OF_OP_CAB xx where xx.ID_OF_CAB = "
										+ id_origem + ")");
						List<Object[]> dados3 = query3.getResultList();
						total = dados3.size();
						for (Object[] content3 : dados3) {

							inform_file = content[2].toString() + "_" + content3[1].toString() + "_PF";
							String inform_file2 = "";

							String OPNUM = (content[4] == null) ? "NULL" : content[4].toString();

							if (estado.equals("M")) {
								OPNUM = atualiza(id_origem, "PF", 0, url);
								nome_ficheiro = "correcao" + data + inform_file + ".txt";
								inform_file2 = content[2].toString() + "_correcao_PAUSA";

								if (OPNUM == null)
									OPNUM = (content[4] == null) ? "NULL" : content[4].toString();
								criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
										"P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total,
										ficheirosdownload, data, null, estado, true, null);
								criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
										estado, data + inform_file2, OPNUM, content3[0].toString(), false, total,
										ficheirosdownload, data_file, null, estado, true, null);

							} else {
							}
							inform_file2 = content[2].toString() + "_PAUSA";

							nome_ficheiro = data + inform_file + ".txt";
							if (estado.equals("A")) {
								nome_ficheiro = "anulacao_" + nome_ficheiro;
							}
							criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
									"P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total,
									ficheirosdownload, data_file, null, estado, true, null);
							pausa = false;
							criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
									estado, data + inform_file2, OPNUM, content3[0].toString(), pausa, total,
									ficheirosdownload, data_file, null, estado, true, null);

							// se for COMP verifica se exitem etiquetas para
							// o
							// comp
							// e cria
							// ficheiro
						}
					} else {
						Integer id_origem = Integer.parseInt(content[1].toString());
						String data_query = "";
						if (estado.equals("M"))
							data_query = " and  (c.VERSAO_MODIF != (select VERSAO_MODIF from RP_OF_CAB where ID_OF_CAB = "
									+ id_origem
									+ ") or (c.QUANT_BOAS_M1 != c.QUANT_BOAS_M2 or c.QUANT_DEF_M1 != c.QUANT_DEF_M2 or c.NOVO = 1 or c.APAGADO = 1)) ";
						Query query2 = entityManager.createNativeQuery(
								"select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA,c.OP_NUM,b.ID_OP_LIN,c.NOVO,c.ATIVO,c.APAGADO  from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where /*b.TIPO_PECA != 'COM' and*/ a.ID_OF_CAB = "
										+ Integer.parseInt(content[0].toString()) + data_query);

						List<Object[]> dados2 = query2.getResultList();
						Integer etiqueta_num = 1;
						for (Object[] content2 : dados2) {
							inform_file = content[2].toString() + "_C" + comp_num + "E" + etiqueta_num;
							etiqueta_num++;
							Integer etiqueta = Integer.parseInt(content2[2].toString());
							Integer id_of_cab = Integer.parseInt(content2[1].toString());
							String OPNUM = (content2[3] == null) ? "NULL" : content2[3].toString();
							String novaet = (content2[5] != null) ? content2[5].toString() : "0";
							String ativo = (content2[6] != null) ? content2[6].toString() : "0";
							String apagado = (content2[7] != null) ? content2[7].toString() : "0";
							if (novaet.equals("true")) {
								novaet = "1";
							}

							Boolean manual = false;
							if (todos) {
								manual = true;
							}

							if (estado.equals("M") && !novaet.equals("1")
									&& ((!ativo.equals("true") && apagado.equals("true"))
											|| (ativo.equals("true") && !apagado.equals("true")))) {
								OPNUM = atualiza(etiqueta, "C", 0, url);
								nome_ficheiro = "correcao" + data + inform_file + ".txt";
								if (OPNUM == null)
									OPNUM = (content2[3] == null) ? "NULL" : content2[3].toString();
								criarFicheiro(id_of_cab, 1, nome_ficheiro, "COMP", content2[0].toString(), id_origem,
										etiqueta, estado, null, OPNUM, content2[4].toString(), false, 1,
										ficheirosdownload, data_file, novaet, estado, manual, null);
							}

							nome_ficheiro = data + inform_file + ".txt";
							if (estado.equals("A")) {
								nome_ficheiro = "anulacao_" + nome_ficheiro;
							}
							if (ativo.equals("true")) {
								criarFicheiro(id_of_cab, 2, nome_ficheiro, "COMP", content2[0].toString(), id_origem,
										etiqueta, estado, null, OPNUM, content2[4].toString(), false, 1,
										ficheirosdownload, data_file, novaet, estado, manual, null);
							}
						}
						if (dados2.size() > 0)
							comp_num++;
					}

				}
			}

		}

		if (ficheirosdownload) {

			final File file = new File("c:/sgiid/temp_files/" + data_file + ".zip");
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=ficheiros.zip");
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					file.delete();
				}
			}, 5000);
			return response.build();

		} else {
			return null;
		}

	}
	
	@POST
	@Path("/getOFS")
	@Produces("application/json")
	public List<HashMap<String, String>> getOFS(final List<HashMap<String, String>> datas)
			throws IOException, ParseException {

		HashMap<String, String> firstMap = datas.get(0);

		String datainicio = firstMap.get("datainicio");
		String datafim = firstMap.get("datafim");
		String url = getURL();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {

			Query query_of = entityManager.createNativeQuery(
					"select a.ID_OF_CAB, a.OF_NUM,c.ID_UTZ_CRIA,a.OP_COD_ORIGEM,c.DATA_INI_M2,c.HORA_INI_M2,c.NOME_UTZ_CRIA,c.DATA_FIM_M2,c.HORA_FIM_M2,a.OP_NUM "
							+ ",d.QUANT_BOAS_TOTAL_M2,d.QUANT_DEF_TOTAL_M2,d.REF_NUM,d.REF_DES from RP_OF_CAB a "
							+ "inner join RP_OF_OP_CAB b on  b.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB =  a.ID_OF_CAB) "
							+ "inner join RP_OF_OP_FUNC c on c.ID_OP_CAB in  (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = a.ID_OF_CAB) and b.ID_OP_CAB = c.ID_OP_CAB "
							+ "inner join RP_OF_OP_LIN d on d.ID_OP_CAB = b.ID_OP_CAB "
							+ "where a.ID_UTZ_CRIA = c.ID_UTZ_CRIA and  "
							+ "CAST((cast(DATA_FIM_M2 as datetime) + cast(HORA_FIM_M2 as datetime)) AS datetime) >= '"
							+ datainicio
							+ "' and CAST((cast(DATA_FIM_M2 as datetime) + cast(HORA_FIM_M2 as datetime)) AS datetime) <= '"
							+ datafim + "' and ID_OF_CAB_ORIGEM is null AND DATA_FIM_M2 is not null ");

			List<Object[]> dados_of = query_of.getResultList();

			Boolean pausa = true;
			for (Object[] content_of : dados_of) {

				ConnectProgress connectionProgress = new ConnectProgress();
				String op_num = null;

				op_num = connectionProgress.verificaOF(content_of[2].toString(), content_of[4].toString(),
						content_of[1].toString(), content_of[3].toString(), url, content_of[5].toString(),
						content_of[10].toString(), content_of[11].toString(), content_of[12].toString());

				if (op_num == null || op_num.isEmpty()) {
					HashMap<String, String> x = new HashMap<>();
					x.put("RESCOD", content_of[2].toString());
					x.put("DATDEB", content_of[4].toString());
					x.put("OFNUM", content_of[1].toString());
					x.put("OPECOD", content_of[3].toString());
					x.put("HEUDEB", content_of[5].toString());
					x.put("NOME", content_of[6].toString());
					x.put("ID", content_of[0].toString());
					x.put("DATAFIM", content_of[7].toString());
					x.put("HORAFIM", content_of[8].toString());
					x.put("OP_NUM", (content_of[9] != null) ? content_of[9].toString() : "--");
					x.put("BOAS", content_of[10].toString());
					x.put("DEFEITOS", content_of[11].toString());
					x.put("REF", content_of[12].toString());
					list.add(x);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	@POST
	@Path("/getOFSCOMPONENTES")
	@Produces("application/json")
	public List<HashMap<String, String>> getOFSCOMPONENTES(final List<HashMap<String, String>> datas)
			throws IOException, ParseException {

		HashMap<String, String> firstMap = datas.get(0);

		String datainicio = firstMap.get("datainicio");
		String datafim = firstMap.get("datafim");
		String url = getURL();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {

			Query query_of = entityManager.createNativeQuery(
					"select e.ID_OF_CAB,(select x.OF_NUM from RP_OF_CAB x where x.ID_OF_CAB =  e.ID_OF_CAB_ORIGEM) as OF_NUM ,g.ID_UTZ_CRIA,a.OPECOD,g.DATA_INI_M2,g.HORA_INI_M2,g.NOME_UTZ_CRIA,g.DATA_FIM_M2,g.HORA_FIM_M2,b.REF_ETIQUETA,c.REF_NUM,c.REF_DES,a.OFNUM from RP_AUX_OPNUM  a "
							+ "inner join RP_OF_OP_ETIQUETA b on a.ID_CAMPO = b.ID_REF_ETIQUETA "
							+ "inner join RP_OF_OP_LIN c on b.ID_OP_LIN = c.ID_OP_LIN "
							+ "inner join RP_OF_OP_CAB d on c.ID_OP_CAB = d.ID_OP_CAB "
							+ "inner join RP_OF_CAB e on d.ID_OF_CAB = e.ID_OF_CAB "
							+ "inner join RP_OF_OP_CAB f on  f.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB =  e.ID_OF_CAB_ORIGEM) "
							+ "inner join RP_OF_OP_FUNC g on g.ID_OP_CAB in  (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = e.ID_OF_CAB_ORIGEM) and f.ID_OP_CAB = g.ID_OP_CAB "
							+ "where CAST((cast(DATDEB as datetime) + cast(HEUDEB as datetime)) AS datetime)  >= '"
							+ datainicio + "' "
							+ "AND CAST((cast(DATDEB as datetime) + cast(HEUDEB as datetime)) AS datetime)  <='"
							+ datafim + "' AND a.ESTADO !=1");

			List<Object[]> dados_of = query_of.getResultList();

			Boolean pausa = true;
			for (Object[] content_of : dados_of) {

				ConnectProgress connectionProgress = new ConnectProgress();
				String op_num = null;

				op_num = connectionProgress.GetOP_NUM(content_of[2].toString(), content_of[4].toString(),
						content_of[10].toString(), content_of[12].toString(), content_of[3].toString(), url,
						content_of[5].toString());

				if (op_num == null || op_num.isEmpty()) {
					HashMap<String, String> x = new HashMap<>();
					x.put("RESCOD", content_of[2].toString());
					x.put("DATDEB", content_of[4].toString());
					x.put("OFNUM", content_of[1].toString());
					x.put("OPECOD", content_of[3].toString());
					x.put("HEUDEB", content_of[5].toString());
					x.put("NOME", content_of[6].toString());
					x.put("ID", content_of[0].toString());
					x.put("DATAFIM", content_of[7].toString());
					x.put("HORAFIM", content_of[8].toString());
					x.put("DATAFIM", content_of[7].toString());
					x.put("ETIQUETA", content_of[9].toString());
					x.put("REF", content_of[10].toString());
					x.put("REF_DES", content_of[11].toString());
					list.add(x);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	@POST
	@Path("/ficheiro/{id}/{estado}/{ficheiros}/{manual}")
	@Produces("application/zip")
	public Response getFicheiro(@PathParam("id") Integer id, @PathParam("estado") String estado,
			@PathParam("ficheiros") Boolean ficheirosdownload, @PathParam("manual") Boolean manual,
			final List<HashMap<String, String>> data2) throws IOException, ParseException {

		HashMap<String, String> firstMap = data2.get(0);
		String ip_posto = firstMap.get("ip_posto");
		String data = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
		String url = getURL();
		try {
			Thread.sleep(3000);
			Integer comp_num = 1;

			String nome_ficheiro = "";

			Query query = entityManager.createNativeQuery(
					"select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM,OP_NUM from RP_OF_CAB where ID_OF_CAB = " + id
							+ " or ID_OF_CAB_ORIGEM = " + id);

			if (estado.equals("C") && !ficheirosdownload && !manual) {
				eventosAoConcluir(id);
			}

			List<Object[]> dados = query.getResultList();

			Boolean pausa = true;
			for (Object[] content : dados) {
				String inform_file = "";
				Integer total = 1;
				// se for PF cria ficheiro (se o estado for modifica��o cria 2)
				if (content[1] == null) {
					Integer id_origem = Integer.parseInt(content[0].toString());
					Query query3 = entityManager.createNativeQuery(
							"select ID_OP_LIN,REF_NUM from RP_OF_OP_LIN where ID_OP_CAB in (select xx.ID_OP_CAB from RP_OF_OP_CAB xx where xx.ID_OF_CAB = "
									+ id_origem + ")");
					List<Object[]> dados3 = query3.getResultList();
					total = dados3.size();
					for (Object[] content3 : dados3) {

						inform_file = content[2].toString() + "_" + content3[1].toString() + "_PF";
						String inform_file2 = "";

						String OPNUM = (content[4] == null) ? "NULL" : content[4].toString();

						if (estado.equals("M")) {
							OPNUM = atualiza(id_origem, "PF", 0, url);
							nome_ficheiro = "correcao" + data + inform_file + ".txt";
							inform_file2 = content[2].toString() + "_correcao_PAUSA";

							if (OPNUM == null)
								OPNUM = (content[4] == null) ? "NULL" : content[4].toString();
							criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
									"P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total,
									ficheirosdownload, data, null, estado, manual, ip_posto);
							criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, null,
									estado, data + inform_file2, OPNUM, content3[0].toString(), false, total,
									ficheirosdownload, data, null, estado, manual, ip_posto);

							/*
							 * criarFicheiro(id_origem, 1, nome_ficheiro, "PF",
							 * content[3].toString(), id_origem, null, estado,
							 * data + inform_file2, OPNUM,
							 * content3[0].toString(), pausa, total,
							 * ficheirosdownload, data);
							 */
						} else {
							/*
							 * criarFicheiro(id_origem, 2, nome_ficheiro, "PF",
							 * content[3].toString(), id_origem, null, "P", data
							 * + inform_file2, OPNUM, content3[0].toString(),
							 * pausa, total, ficheirosdownload, data); pausa =
							 * false;
							 */
						}
						inform_file2 = content[2].toString() + "_PAUSA";
						/*
						 * criarFicheiro(id_origem, 2, nome_ficheiro, "PF",
						 * content[3].toString(), id_origem, null, estado, data
						 * + inform_file2, OPNUM, content3[0].toString(), pausa,
						 * total, ficheirosdownload, data);
						 */

						nome_ficheiro = data + inform_file + ".txt";
						if (estado.equals("A")) {
							nome_ficheiro = "anulacao_" + nome_ficheiro;
						}
						criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, null, "P",
								data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload,
								data, null, estado, manual, ip_posto);
						pausa = false;
						criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, null, estado,
								data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload,
								data, null, estado, manual, ip_posto);

						// se for COMP verifica se exitem etiquetas para o comp
						// e cria
						// ficheiro
					}
				} else {
					Integer id_origem = Integer.parseInt(content[1].toString());
					String data_query = "";
					if (estado.equals("M"))
						data_query = " and  (c.VERSAO_MODIF != (select VERSAO_MODIF from RP_OF_CAB where ID_OF_CAB = "
								+ id_origem
								+ ") or (c.QUANT_BOAS_M1 != c.QUANT_BOAS_M2 or c.QUANT_DEF_M1 != c.QUANT_DEF_M2 or c.NOVO = 1 or c.APAGADO = 1)) ";
					Query query2 = entityManager.createNativeQuery(
							"select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA,c.OP_NUM,b.ID_OP_LIN,c.NOVO,c.ATIVO,c.APAGADO   from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where b.TIPO_PECA != 'COM' and a.ID_OF_CAB = "
									+ Integer.parseInt(content[0].toString()) + data_query);

					List<Object[]> dados2 = query2.getResultList();
					Integer etiqueta_num = 1;
					for (Object[] content2 : dados2) {
						inform_file = content[2].toString() + "_C" + comp_num + "E" + etiqueta_num;
						etiqueta_num++;
						Integer etiqueta = Integer.parseInt(content2[2].toString());
						Integer id_of_cab = Integer.parseInt(content2[1].toString());
						String OPNUM = (content2[3] == null) ? "NULL" : content2[3].toString();
						String novaet = (content2[5] != null) ? content2[5].toString() : "0";
						String ativo = (content2[6] != null) ? content2[6].toString() : "0";
						String apagado = (content2[7] != null) ? content2[7].toString() : "0";

						if (novaet.equals("true")) {
							novaet = "1";
						}

						if (estado.equals("M") && !novaet.equals("1")
								&& ((!ativo.equals("true") && apagado.equals("true"))
										|| (ativo.equals("true") && !apagado.equals("true")))) {
							OPNUM = atualiza(etiqueta, "C", 0, url);
							nome_ficheiro = "correcao" + data + inform_file + ".txt";
							if (OPNUM == null)
								OPNUM = (content2[3] == null) ? "NULL" : content2[3].toString();
							criarFicheiro(id_of_cab, 1, nome_ficheiro, "COMP", content2[0].toString(), id_origem,
									etiqueta, estado, null, OPNUM, content2[4].toString(), false, 1, ficheirosdownload,
									data, novaet, estado, manual, ip_posto);
						}

						nome_ficheiro = data + inform_file + ".txt";
						if (estado.equals("A")) {
							nome_ficheiro = "anulacao_" + nome_ficheiro;
						}
						if (ativo.equals("true")) {
							criarFicheiro(id_of_cab, 2, nome_ficheiro, "COMP", content2[0].toString(), id_origem,
									etiqueta, estado, null, OPNUM, content2[4].toString(), false, 1, ficheirosdownload,
									data, novaet, estado, manual, ip_posto);
						}
					}
					if (dados2.size() > 0)
						comp_num++;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (estado.equals("C"))
			ficheiroimprimiretiqueta(ip_posto, id, ficheirosdownload, data);

		criarFicheiroConsumo(id, ficheirosdownload, data);

		if (ficheirosdownload) {

			final File file = new File("c:/sgiid/temp_files/" + data + ".zip");
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=ficheiros.zip");
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					file.delete();
				}
			}, 5000);
			return response.build();

		} else {
			return null;
		}

	}

	public void eventosAoConcluir(Integer id) {
		String referencia = "", descricao_referencia = "", perc_obj = "", perc_def = "", utilizador = "", of_num = "",
				data_cria = "", etiquetas = "";
		String[] keyValuePairs = null;

		Query query = entityManager.createNativeQuery(
				"select PERC_OBJETIV,  cast( (CAST(NULLIF(QUANT_DEF_TOTAL_M2,0)AS float) / CAST(( QUANT_BOAS_TOTAL_M2 +  QUANT_DEF_TOTAL_M2) AS float)) * 100  as numeric(36,2)), "
						+ "REF_NUM,REF_DES,c.ID_UTZ_CRIA,c.NOME_UTZ_CRIA,(select OF_NUM from RP_OF_CAB where ID_OF_CAB = "
						+ id + " ) as OF_NUM,"
						+ "(select top 1 (cast(DATA_INI_M2 as datetime) + cast(HORA_INI_M2 as datetime)) FROM RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB = "
						+ id + ") ) DATA" + ",c.ID_OF_CAB_ORIGEM,a.ID_OP_LIN from RP_OF_OP_LIN  a "
						+ "left join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB left join RP_OF_CAB c on b.ID_OF_CAB = c.ID_OF_CAB "
						+ "where  (c.ID_OF_CAB = " + id + " or c.ID_OF_CAB_ORIGEM = " + id
						+ ") and  ISNULL(cast( (CAST(NULLIF(QUANT_DEF_TOTAL_M2,0)AS float) / CAST(( QUANT_BOAS_TOTAL_M2 +  QUANT_DEF_TOTAL_M2)AS float)) * 100  as numeric(36,2)),0) > ISNULL(PERC_OBJETIV,0) "
						+ "and a.GESCOD in ('OFCF','OFCF2') and ISNULL(PERC_OBJETIV,0) != 0");

		List<Object[]> dados = query.getResultList();

		for (Object[] content : dados) {

			referencia = content[2].toString();
			descricao_referencia = content[3].toString();
			perc_obj = content[0].toString() + " %";
			perc_def = content[1].toString() + " %";
			utilizador = content[4].toString() + " - " + content[5].toString();
			of_num = content[6].toString();
			data_cria = content[7].toString().substring(0, 19);

			if (content[6] == null) {
				String[] keyValuePairs1 = { "referencia::" + referencia,
						"descricao_referencia::" + descricao_referencia, "perc_obj::" + perc_obj,
						"perc_def::" + perc_def, "utilizador::" + utilizador, "of_num::" + of_num,
						"data_cria::" + data_cria, "etiquetas::" + etiquetas };
				keyValuePairs = keyValuePairs1;
				verficaEventos(keyValuePairs, "Ao Concluir Trabalho - Alerta Objetivos", "");
			} else {
				etiquetas = "<table  border='1'><tr><th><b>N� Etiqueta</b></th><th><b>Lote</b></th><th><b>OF Origem</b></th><th><b>Data OF</b></th></tr>";

				Query query_comp = entityManager.createNativeQuery(
						"select REF_LOTE,REF_ETIQUETA,OF_NUM_ORIGEM,OFDATFR from RP_OF_OP_ETIQUETA where ID_OP_LIN ="
								+ content[9]);

				List<Object[]> dados_comp = query_comp.getResultList();

				for (Object[] cont : dados_comp) {
					etiquetas += "<tr><td style='padding: 0px 5px 0px 5px;'>" + cont[1] + "</td>"
							+ "<td style='padding: 0px 5px 0px 5px;'>" + cont[0] + "</td>"
							+ "<td style='padding: 0px 5px 0px 5px;'>" + cont[2] + "</td>"
							+ "<td style='padding: 0px 5px 0px 5px;'>" + cont[3] + "</td></tr>";
				}

				etiquetas += "</table>";

				String[] keyValuePairs2 = { "referencia::" + referencia,
						"descricao_referencia::" + descricao_referencia, "perc_obj::" + perc_obj,
						"perc_def::" + perc_def, "utilizador::" + utilizador, "of_num::" + of_num,
						"data_cria::" + data_cria, "etiquetas::" + etiquetas };
				keyValuePairs = keyValuePairs2;
				verficaEventos(keyValuePairs, "Ao Concluir Trabalho - Alerta Objetivos", "");

			}
		}

	}

	public void criarFicheiro(Integer id, Integer ficheiro, String nome_ficheiro, String tipo, String of,
			Integer id_origem, Integer id_etiqueta, String estado, String nome_ficheiro2, String OP_NUM,
			String ID_OP_LIN, Boolean cria_pausa, Integer total, Boolean ficheirosdownload, String nomezip,
			String novaetiqueta, String estado2, Boolean manual, String ip_posto) throws IOException, ParseException {

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
			sequencia = sequencia();

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
					if (content[11].toString().equals("1")) {
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
				data_A += content[5].toString().replaceAll("-", ""); // Date
																		// d�but
				data_A += content[6].toString().replace(":", "").substring(0, 6); // Heure
																					// d�but
				data_A += content[7].toString().replaceAll("-", ""); // Date
																		// fin
				data_A += content[8].toString().replace(":", "").substring(0, 6); // Heure
																					// fin
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
				if (cria_pausa) {
					linha_utz.put(content[1].toString(), data_A);
					linha_utz_inicio.put(content[1].toString(), data_A.substring(0, 87));
				}

			}

			if (existe_maquina && tipo.equals("PF") /* && !estado.equals("P") */) {
				data_maquina += data;
				data = data_maquina;
			}

			// PAUSA
			// estado.equals("P") &&
			if (cria_pausa) {
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
						+ "inner join RP_OF_PARA_LIN c on c.ID_OP_CAB = b.ID_OP_CAB " + "where a.ID_OF_CAB = " + id);

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

					String seq = sequencia();

					StringBuffer buf3 = new StringBuffer(linha_utz.get(content2[7].toString()));
					buf3.replace(18, 27, seq);
					String linha3 = buf3.toString();
					if (!existe_maquina) {
						data_pausa_p += linha3;
					}

					String linha_A_MAQUINA = "";
					if (existe_maquina) {
						// linha A MAQ e PESSOAS
						BufferedReader bufReader = new BufferedReader(new StringReader(data_maquina));

						String line = null;
						while ((line = bufReader.readLine()) != null) {

							StringBuffer buf6 = new StringBuffer(line);
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
						buf2.replace(18, 27, seq);
						String linha2 = buf2.toString();
						// data_pausa_p += linha2.substring(0, 87) + data_pausa;

						if (!criou_PAUSA) {
							/*
							 * Integer totalprep = 0; Integer totalexecucao = 0;
							 * 
							 * Query querytotal = entityManager
							 * .createNativeQuery("select  (select count(*) from RP_OF_OP_FUNC a "
							 * +
							 * "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
							 * +
							 * "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB "
							 * + "where ID_OF_CAB = " + id + " and c." +
							 * DATA_INI + " is not null and c." + DATA_FIM +
							 * " is not null ) as totalprep, (select count(*) from RP_OF_OP_FUNC a "
							 * +
							 * "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB "
							 * +
							 * "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB "
							 * + "where ID_OF_CAB = " + id + " and " +
							 * " ((cast(a." + DATA_FIM +
							 * " as datetime) + cast(a." + HORA_FIM +
							 * " as datetime)) > (cast(c." + DATA_FIM +
							 * " as datetime) + cast(c." + HORA_FIM +
							 * " as datetime))" +
							 * "or c.HORA_INI_M2 is null)) as totalexecucao ");
							 * 
							 * List<Object[]> dadostotal =
							 * querytotal.getResultList();
							 * 
							 * for (Object[] contenttotal : dadostotal) {
							 * totalexecucao =
							 * Integer.parseInt(contenttotal[1].toString());
							 * totalprep =
							 * Integer.parseInt(contenttotal[0].toString()); }
							 */

							// criar pausas maquina estado preparacao

							// if (totalprep > 0)

							if (!estado2.equals("M") || (estado2.equals(
									"M") /*
											 * && content2[9].toString().
											 * equals("1")
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
					buf.replace(18, 27, seq);
					String linha = buf.toString();
					data_pausa_p += linha + data_pausa;

					if (estado2.equals("M") && content2[9].toString().equals("1")
							&& !content2[10].toString().equals("1") && Float.parseFloat(content2[4].toString()) > 0) {
						criar_ficheiro_Pausa(data_pausa_p, path2, count, ficheirosdownload, nome_ficheiro2, nomezip,
								path_error);
					} else if (estado2.equals("M") && content2[10].toString().equals("1") && ficheiro == 2
							&& ((content2[4] != null) ? Float.parseFloat(content2[4].toString()) : 0) > 0) {
						criar_ficheiro_Pausa(data_pausa_p, path2, count, ficheirosdownload, nome_ficheiro2, nomezip,
								path_error);
					} else if (!estado2.equals("M") && Float.parseFloat(content2[4].toString()) > 0) {
						criar_ficheiro_Pausa(data_pausa_p, path2, count, ficheirosdownload, nome_ficheiro2, nomezip,
								path_error);
					}

				}
			}

			if (!estado.equals("P")) {
				String data3 = "";

				//data3 = " ,CASE when (c.QUANT_BOAS_TOTAL_M1 != c.QUANT_BOAS_TOTAL_M2 or e.QUANT_BOAS_M1 != e.QUANT_BOAS_M2   ) then 1 else 0 END as alterado ";
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
						if (content3[18].toString().equals("1")) {
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
					 * StringBuffer buf = new StringBuffer(data_quantidades);
					 * buf.replace(70, 84, "              "); data_maquina =
					 * buf.toString(); data += data_maquina;
					 */

				}
			}

			if (!estado.equals("P")) {

				String data4 = "";

				if (estado.equals("M")) {
					if (ficheiro == 1) {
						if (tipo.equals("COMP")) {
							//data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M1 != 0 or f.APAGADO = 1)";
						} else {
							//data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M1 != 0) ";
						}

					} else {
						//data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M2 != 0) ";
					}

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
						if (content4[21].toString().equals("1")) {
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

					data_defeitos += SINAL; // Signe
					data_defeitos += "                                                                                                       ";
					String obs = (content4[20] != null) ? content4[20].toString() : "";

					data_defeitos += (obs + "                                        ").substring(0, 39); // Texte
																											// libre
					data_defeitos += "\r\n";
					data += data_defeitos;
					/*
					 * StringBuffer buf = new StringBuffer(data_defeitos);
					 * buf.replace(70, 84, "              "); data_maquina =
					 * buf.toString(); data += data_maquina;
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
						verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
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
							verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
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
			verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
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
				verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
				criarfileerro(estado, patherro, data, alteracoes);
				ex.printStackTrace();
				return;
			}
		}
	}

	public void criarFicheiroConsumo(Integer id_of_cab, Boolean ficheirosdownload, String nomezip) throws IOException {
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

		sequencia = sequencia();
		List<Object[]> dados = null;

		Query query = entityManager.createNativeQuery("DECLARE @ID_OF_CAB int = " + id_of_cab + "; "
				+ "select t.ETQNUM,e.OF_NUM,e.OP_NUM,e.OP_COD_ORIGEM,e.MAQ_NUM_ORIG,m.DATA_INI_M2,m.HORA_INI_M2,m.DATA_FIM_M2,m.HORA_FIM_M2 "
				+ ",(select ID_TURNO from RP_CONF_TURNO WHERE m.HORA_INI_M2 > HORA_INICIO and m.HORA_INI_M2 < HORA_FIM) N_EQUIPA,t.PROREF,t.VA1REF,t.VA2REF,t.INDREF,t.INDNUMENR,t.UNICOD, "
				+ "t.LIECOD,t.EMPCOD,t.ETQORILOT1,t.ETQNUMENR,(d.QUANT_BOAS_M2 + d.QUANT_DEF_M2 ) QUANT,t.LOTNUMENR "
				+ "from RP_OF_CAB a inner join RP_OF_OP_CAB b on a.ID_OF_CAB = b.ID_OF_CAB "
				+ "inner join RP_OF_OP_LIN c on b.ID_OP_CAB = c.ID_OP_CAB inner join RP_OF_OP_ETIQUETA d on c.ID_OP_LIN = d.ID_OP_LIN "
				+ "left join (select ETQNUM,PROREF,VA1REF,VA2REF,INDREF,st.INDNUMENR,UNICOD,LIECOD,EMPCOD,ETQORILOT1,ETQNUMENR,sl.LOTNUMENR from SILVER.dbo.SETQDE st "
				+ "left join  SILVER.dbo.STOLOT sl on st.INDNUMENR = sl.INDNUMENR and st.ETQORILOT1 = sl.LOTREF "
				+ "where ETQNUM in (select RIGHT ('000'+CAST(td.REF_ETIQUETA as varchar(10)),10) from RP_OF_CAB ta inner join RP_OF_OP_CAB tb on ta.ID_OF_CAB = tb.ID_OF_CAB "
				+ "inner join RP_OF_OP_LIN tc on tb.ID_OP_CAB = tc.ID_OP_CAB inner join RP_OF_OP_ETIQUETA td on tc.ID_OP_LIN = td.ID_OP_LIN) "
				+ ") t on RIGHT ('000'+CAST(d.REF_ETIQUETA as varchar(10)),10) = t.ETQNUM "
				+ "left join (select * from RP_OF_CAB where ID_OF_CAB = @ID_OF_CAB) e on  a.ID_OF_CAB_ORIGEM = e.ID_OF_CAB "
				+ "left join (select g.ID_OF_CAB,h.* from RP_OF_CAB f inner join RP_OF_OP_CAB g on f.ID_OF_CAB = g.ID_OF_CAB "
				+ "inner join RP_OF_OP_FUNC h on g.ID_OP_CAB = h.ID_OP_CAB and f.ID_UTZ_CRIA = h.ID_UTZ_CRIA where f.ID_OF_CAB = @ID_OF_CAB) m on a.ID_OF_CAB_ORIGEM = m.ID_OF_CAB "
				+ "where a.ID_OF_CAB_ORIGEM = @ID_OF_CAB and (d.QUANT_BOAS_M2 + d.QUANT_DEF_M2 ) > 0");

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
			data += ("0010").substring(0, 4);// N� Op�ration

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
				verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
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
						verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
						e1.printStackTrace();
					}
				}
				if (fw2 != null) {
					try {
						fw2.close();
					} catch (IOException e1) {
						String[] keyValuePairs = {
								"TEXTO_ERRO ::" + e1.getMessage() + " " + file2.getAbsolutePath() + "", };
						verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
			}
		}
		if (error)

		{
			String[] keyValuePairs = { "TEXTO_ERRO ::" + err + "", };
			verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", path_error);
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
			Integer count, String estado, String path_error) throws ParseException {

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

		String seq = sequencia();
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
						nome_ficheiro2, nomezip, count, ESTADO, path_error);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
					verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
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

	public String sequencia() {
		String sequencia = "000000000";
		Query query_seq = entityManager.createNativeQuery(
				"select top 1 NUMERO_SEQUENCIA,DATA_SEQUENCIA from GER_SEQUENCIA_FICHEIRO where DATA_SEQUENCIA = CONVERT (date, GETDATE())");

		List<Object[]> dados_seq = query_seq.getResultList();
		if (dados_seq.size() > 0) {
			Integer val = 1;
			for (Object[] contentseq : dados_seq) {
				val = Integer.parseInt(contentseq[0].toString()) + 1;
				sequencia = ("000000000" + val).substring(("000000000" + val).length() - 9,
						("000000000" + val).length());
			}
			entityManager.createNativeQuery("UPDATE GER_SEQUENCIA_FICHEIRO SET NUMERO_SEQUENCIA = " + val
					+ " where DATA_SEQUENCIA = CONVERT (date, GETDATE())").executeUpdate();
		} else {
			sequencia = "000000001";
			entityManager
					.createNativeQuery(
							"INSERT INTO GER_SEQUENCIA_FICHEIRO (DATA_SEQUENCIA,NUMERO_SEQUENCIA) VALUES (GETDATE(),1)")
					.executeUpdate();
		}
		return sequencia;
	}

	/* Email ************************************/
	@POST
	@Path("/sendemail")
	@Consumes("*/*")
	@Produces("application/json")
	public EMAIL sendemail(final EMAIL data) {

		// String mensagem = data.getMENSAGEM();

		// dadosmensagem(mensagem, "execucao");

		// System.out.println(data.getMENSAGEM());

		SendEmail send = new SendEmail();
		send.enviarEmail(data.getDE(), data.getPARA(), data.getASSUNTO(), data.getMENSAGEM(), data.getNOME_FICHEIRO());

		return data;

	}

	@POST
	@Path("/verficaEventos")
	@Consumes("*/*")
	@Produces("application/json")
	public List<HashMap<String, String>> verficaEventos(final List<HashMap<String, String>> data) {
		HashMap<String, String> firstMap = data.get(0);

		String value = firstMap.get("DADOS");
		value = value.substring(1, value.length() - 1);
		String[] keyValuePairs = value.split(";#;");

		Query query3 = entityManager.createQuery("Select a from GER_EVENTOS_CONF a where MODULO = "
				+ firstMap.get("MODULO") + " and MOMENTO = '" + firstMap.get("MOMENTO") + "' " + "and PAGINA = '"
				+ firstMap.get("PAGINA") + "' and ESTADO = " + firstMap.get("ESTADO") + "");
		List<GER_EVENTOS_CONF> dados = query3.getResultList();

		for (GER_EVENTOS_CONF borderTypes : dados) {

			// System.out.println(borderTypes.getEMAIL_ASSUNTO());
			EMAIL email = new EMAIL();
			// email.setASSUNTO(borderTypes.getEMAIL_ASSUNTO());
			email.setDE("alertas.it.doureca@gmail.com");

			email.setPARA(borderTypes.getEMAIL_PARA());
			String mensagem = borderTypes.getEMAIL_MENSAGEM();
			String assunto = borderTypes.getEMAIL_ASSUNTO();
			for (String pair : keyValuePairs) {

				String[] entry = pair.split("::");

				mensagem = mensagem.replace("{" + entry[0].trim() + "}", entry[1].trim());
				assunto = assunto.replace("{" + entry[0].trim() + "}", (entry.length > 1) ? entry[1].trim() : "");
			}
			email.setASSUNTO(assunto);
			email.setMENSAGEM(mensagem);
			sendemail(email);
		}

		return data;
	}

	public Object getvalues(String campo, Object dd) {
		// System.out.println(getvalues("EMAIL_ASSUNTO", borderTypes[0]));
		try {
			try {
				return dd.getClass().getDeclaredField(campo).get(dd);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

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

	public String dadosmensagem(String mensagem, String pagina) {

		Map<String, String> results = new HashMap<String, String>();

		Query query3 = entityManager.createQuery("Select a from GER_EVENTO a where ID_EVENTO = :id");
		query3.setParameter("id", 3);

		List<GER_EVENTO> dados = query3.getResultList();

		for (GER_EVENTO borderTypes : dados) {

			// System.out.println(borderTypes.getASSUNTO());
			results.put("ASSUNTO", (borderTypes.getASSUNTO() == null) ? "" : borderTypes.getASSUNTO());
			results.put("MENSAGEM", (borderTypes.getMENSAGEM() == null) ? "" : borderTypes.getMENSAGEM());
		}

		for (Entry<String, String> entry : results.entrySet()) {
			mensagem = mensagem.replace("{" + entry.getKey() + "}", entry.getValue());
		}

		// System.out.println(mensagem);

		/*
		 * ArrayList<String> campos = new ArrayList<String>(); Pattern p =
		 * Pattern.compile(Pattern.quote("{") + "(.*?)" + Pattern.quote("}"));
		 * Matcher m = p.matcher(mensagem); while (m.find()) { if
		 * (!campos.contains(m.group(1))) { campos.add(m.group(1)); //
		 * System.out.println(m.group(1)); } }
		 */

		return mensagem;
	}

	private byte[] zipFiles(File directory, String[] files) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte bytes[] = new byte[2048];

		/*
		 * for (String fileName : files) { FileInputStream fis = new
		 * FileInputStream( directory.getPath() +
		 * ZipDownloadServlet.FILE_SEPARATOR + fileName); BufferedInputStream
		 * bis = new BufferedInputStream(fis);
		 * 
		 * zos.putNextEntry(new ZipEntry(fileName));
		 * 
		 * int bytesRead; while ((bytesRead = bis.read(bytes)) != -1) {
		 * zos.write(bytes, 0, bytesRead); } zos.closeEntry(); bis.close();
		 * fis.close(); }
		 */
		zos.flush();
		baos.flush();
		zos.close();
		baos.close();

		return baos.toByteArray();
	}

	public void verficaEventos(String[] keyValuePairs, String momento, String fgilepath) {

		List<String> x = new ArrayList<>();

		Query query3 = entityManager.createQuery("Select a from GER_EVENTOS_CONF a where MODULO = 4 and MOMENTO = '"
				+ momento + "' " + "and PAGINA = 'INTERNO' and ESTADO  != 0");
		List<GER_EVENTOS_CONF> dados = query3.getResultList();

		for (GER_EVENTOS_CONF borderTypes : dados) {

			// System.out.println(borderTypes.getEMAIL_ASSUNTO());
			EMAIL email = new EMAIL();
			// email.setASSUNTO(borderTypes.getEMAIL_ASSUNTO());
			email.setDE("alertas.it.doureca@gmail.com");

			email.setPARA(borderTypes.getEMAIL_PARA());
			String mensagem = borderTypes.getEMAIL_MENSAGEM();
			String assunto = borderTypes.getEMAIL_ASSUNTO();

			for (String pair : keyValuePairs) {
				String[] entry = pair.split("::");
				mensagem = mensagem.replace("{" + entry[0].trim() + "}", entry[1].trim());
				assunto = assunto.replace("{" + entry[0].trim() + "}", (entry.length > 1) ? entry[1].trim() : "");
			}
			email.setASSUNTO(assunto);
			email.setMENSAGEM(mensagem);
			sendemail(email);

		}
	}

	@GET
	@Path("/imprimir/{nomeficheiro}/{impressora}")
	@Produces("application/json")
	public Response imprimir(@PathParam("nomeficheiro") String nomeficheiro,
			@PathParam("impressora") String impressora) {
		Printer impressoras = new Printer();
		return impressoras.imprimir(nomeficheiro, impressora, "sgiid");
	}

	@GET
	@Path("/getIMPRESORA/{ip}")
	@Produces("application/json")
	public List<Object> getIMPRESORA(@PathParam("ip") String ip) {
		Query query = entityManager.createNativeQuery("select * from GER_POSTOS a where a.IP_POSTO = :ip");
		query.setParameter("ip", ip);
		List<Object> data = query.getResultList();
		return data;
	}

	/* FICHEIRO ************************************/
	@GET
	@Path("/get/{format}/{filename}/{PROREF}/{relatorio}")
	// @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	public Response getFile(@PathParam("format") String format, @PathParam("filename") String Name,
			@PathParam("PROREF") String PROREF, @PathParam("relatorio") String relatorio) {
		String fileName = null;
		String filepath = "sgiid";
		ReportGenerator report = new ReportGenerator();
		try {
			fileName = report.relatorio(format, Name, PROREF, relatorio, getURL(), filepath);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileName != null) {
			File file = new File("/" + filepath + "/relatorios/" + fileName);
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=report." + format + "");
			return response.build();
		} else {
			return null;
		}

	}

	public void ficheiroimprimiretiqueta(String ip_posto, Integer ID, Boolean ficheirosdownload, String nomezip)
			throws IOException {

		String modelo_REPORT = "";
		String nomeimpressora = "";
		String ipimpressora = "";
		String data_etiq = "";
		String path2 = "";
		String path_error = "";

		/*** CAMPOS PARA O FICHEIRO ***/

		String DATCRE = "";
		String QUANT = "";
		String UNIDADE = "";
		String ETIQUETA = "";
		String PROREF = "";
		String ETQORILOT1 = "";
		String DESC1 = "";
		String DESC2 = "";
		String ETQORIDOC1 = "";

		/******************************/

		Query query_folder = entityManager.createNativeQuery(
				"select top 1  PASTA_FICHEIRO,PASTA_ETIQUETAS,NOME_IMPRESSORA,IP_IMPRESSORA,MODELO_REPORT_PRODUCAO,PASTA_DESTINO_ERRO from GER_PARAMETROS a,GER_POSTOS b where IP_POSTO ='"
						+ ip_posto + "'");

		List<Object[]> dados_folder = query_folder.getResultList();

		Query query_etiquetas = entityManager.createNativeQuery(
				"select (c.QUANT_ETIQUETA - (c.QUANT_BOAS_M2 +   c.QUANT_DEF_M2)) QUANT, e.ETQNUM,b.REF_NUM,PRODES1,PRODES2,c.REF_LOTE as ETQORILOT1"
						+ ",e.DATCRE,e.UNICOD,ETQORIDOC1 from RP_OF_OP_CAB a "
						+ "inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB "
						+ "INNER join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB "
						+ "INNER join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN "
						+ "LEFT join  (select PRODES1,PRODES2,ETQNUM,UNICOD,DATCRE, h.ETQORIDOC1 from SILVER.dbo.SETQDE h "
						+ "inner join SILVER.dbo.SDTPRA g on h.PROREF = g.PROREF) e on e.ETQNUM = RIGHT(CONCAT('000', c.REF_ETIQUETA) ,10) "
						+ "where   a.ID_OF_CAB in (select ID_OF_CAB from RP_OF_CAB where ID_OF_CAB_ORIGEM = " + ID
						+ ") "
						+ "and b.TIPO_PECA != 'COM' AND  (c.QUANT_ETIQUETA - (c.QUANT_BOAS_M2 +   c.QUANT_DEF_M2)) > 0");

		List<Object[]> dados_etiqeutas = query_etiquetas.getResultList();

		String nome_ficheiro = "ETIQUETA_PRODUCAO_NUM_" + ID + ".txt";

		for (Object[] content : dados_folder) {
			path2 = content[1].toString() + nome_ficheiro;
			path_error = content[5].toString() + nome_ficheiro;
			nomeimpressora = content[2].toString();
			if (content[3] != null) {
				ipimpressora = content[3].toString();
			}
			modelo_REPORT = content[4].toString();
		}

		data_etiq += "LAB_NAME=" + modelo_REPORT + "\r\n";
		if (!ipimpressora.isEmpty() && ipimpressora != null) {
			// ipimpressora = ",->" + ipimpressora;
		}
		data_etiq += "THT_NAME=" + nomeimpressora /* + ipimpressora */ + "\r\n";
		data_etiq += "AF100;AF101;AF1;AF2;A2;AF3;A3;AF4;A4;AF5;A5;AF6;AF7;A7;AF8;AF9;AF10;AF11;AF24;AF12;AF16;A16;AF17;AF18;AF19;AF20;A20;AF21;A21;AF22;AF23;AF25;AF26;AF27;AF28;AF29;AF30;AF31;AF32;AF33;AF34;AF35;AF36;AF37;AF38;AF39;AF40;AF41;AF42;AF43;AF44;XF01;END;\r\n";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Integer count = 0;
		for (Object[] value : dados_etiqeutas) {
			count++;
			DATCRE = value[6].toString();
			QUANT = value[0].toString();
			UNIDADE = value[7].toString();
			ETIQUETA = value[1].toString();
			PROREF = value[2].toString();
			ETQORILOT1 = value[5].toString();
			DESC1 = value[3].toString();
			DESC2 = value[4].toString();
			ETQORIDOC1 = value[8].toString();

			java.util.Date date1 = null;
			try {
				date1 = formatter.parse(DATCRE);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			formatter = new SimpleDateFormat("yyMMdd");
			String datas = formatter.format(date1);

			String quant = String.format("%.3f", Float.parseFloat(QUANT)).replace("$", ",");
			data_etiq += "DOURECA - PT- 4940;DOURECA - PT- 4940;;;;";
			data_etiq += quant + " " + ";" // AF3
					+ "Q;" // A3
					+ ";" // AF4
					+ ";" // A4
					+ ETIQUETA + ";" // AF5
					+ "S;" // A5
					+ DESC1 + ";" // AF6
					+ PROREF + ";" // AF7
					+ "30S;" // A7
					+ ";" // AF8
					+ ";" // AF9
					+ ";" // AF10
					+ ";" // AF11
					+ ";" // AF24
					+ ";" // AF12
					// + "D"
					+ datas + ";" // AF16
					+ "P;" // A16
					+ ";" // AF17
					+ ";" // AF18
					+ ";" // AF19
					+ ";" // AF20
					+ ";" // A20
					+ ETQORILOT1 + ";" // AF21
					+ "H;" // A21
					+ ";" // AF22
					+ ";" // AF23
					+ ";" // AF25
					+ "secu0.bmp;" // AF26
					+ ";" // AF27
					+ ";" // AF28
					+ ";" // AF29
					+ ";" // AF30
					+ DESC1 + ";" // AF31
					+ DESC2 + ";" // AF32
					+ ";" // AF33
					+ "S0;" // AF34
					+ ";" // AF35
					+ ";" // AF36
					+ ";" // AF37
					+ ETQORIDOC1 + ";" // AF38
					+ ((UNIDADE != null) ? UNIDADE : "") + ";" // AF39
					+ quant + ";" // AF40
					+ ((UNIDADE != null) ? UNIDADE : "") + ";" // AF41
					+ ";" // AF42
					+ ";" // AF43
					+ ";" // AF44
					+ "1;" // XF01
					+ ";\r\n";
		}

		if (count > 0) {
			if (!ficheirosdownload) {
				if (!path2.isEmpty())
					criar_ficheiro(data_etiq, path2, path_error, false, "");
			} else {

				Map<String, String> env = new HashMap<>();
				env.put("create", "true");
				java.nio.file.Path pathh = Paths.get("c:/sgiid/temp_files/" + nomezip + ".zip");
				URI uri = URI.create("jar:" + pathh.toUri());
				try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
					java.nio.file.Path nf = fs.getPath(nome_ficheiro);
					try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8,
							StandardOpenOption.CREATE)) {
						writer.write(data_etiq);
					}
				}

			}
		}

	}

	/************************************* CONTROLO_ETIQUETAS */
	@POST
	@Path("/createCONTROLO_ETIQUETAS")
	@Consumes("*/*")
	@Produces("application/json")
	public CONTROLO_ETIQUETAS insertCONTROLO_ETIQUETAS(final CONTROLO_ETIQUETAS data) {
		return dao20.create(data);
	}

	@GET
	@Path("/getCONTROLO_ETIQUETAS")
	@Produces("application/json")
	public List<CONTROLO_ETIQUETAS> getCONTROLO_ETIQUETAS() {
		return dao20.getall();
	}

	@GET
	@Path("/getCONTROLO_ETIQUETASVerificaSeExiste/{etiqueta}/{caixa}")
	@Produces("application/json")
	public List<CONTROLO_ETIQUETAS> getCONTROLO_ETIQUETASVerificaSeExiste(@PathParam("etiqueta") String etiqueta,
			@PathParam("caixa") String caixa) {
		return dao20.VerificaSeExiste(etiqueta, caixa);
	}

	@GET
	@Path("/getCONTROLO_ETIQUETASbyid/{id}")
	@Produces("application/json")
	public List<CONTROLO_ETIQUETAS> getCONTROLO_ETIQUETASbyid(@PathParam("id") Integer id) {
		return dao20.getbyid(id);
	}

	@GET
	@Path("/getCONTROLO_ETIQUETASbyCAIXA/{caixa}")
	@Produces("application/json")
	public List<CONTROLO_ETIQUETAS> getCONTROLO_ETIQUETASbyCAIXA(@PathParam("caixa") String caixa) {
		return dao20.getallbyCAIXA(caixa);
	}

	@DELETE
	@Path("/deleteCONTROLO_ETIQUETAS/{id}")
	public void deleteCONTROLO_ETIQUETAS(@PathParam("id") Integer id) {
		CONTROLO_ETIQUETAS CONTROLO_ETIQUETAS = new CONTROLO_ETIQUETAS();
		CONTROLO_ETIQUETAS.setID(id);
		dao20.delete(CONTROLO_ETIQUETAS);
	}

	@PUT
	@Path("/updateCONTROLO_ETIQUETAS")
	@Consumes("*/*")
	@Produces("application/json")
	public CONTROLO_ETIQUETAS updateCONTROLO_ETIQUETAS(final CONTROLO_ETIQUETAS CONTROLO_ETIQUETAS) {
		CONTROLO_ETIQUETAS.setID(CONTROLO_ETIQUETAS.getID());
		return dao20.update(CONTROLO_ETIQUETAS);
	}

	@PUT
	@Path("/updateCONTROLO_ETIQUETASTODAS")
	@Consumes("*/*")
	@Produces("application/json")
	public int updateCONTROLO_ETIQUETASTODAS(final CONTROLO_ETIQUETAS CONTROLO_ETIQUETAS) {
		int query_folder = entityManager
				.createNativeQuery("  UPDATE [CONTROLO_ETIQUETAS] SET TOTAL = TOTAL + 1,ESTADO = 'D',UTZ_MODIF = '"
						+ CONTROLO_ETIQUETAS.getUTZ_MODIF() + "',DATA_MODIF=GETDATE() " + " WHERE ETIQUETA = '"
						+ CONTROLO_ETIQUETAS.getETIQUETA() + "' AND ETIQUETA_CAIXA = '"
						+ CONTROLO_ETIQUETAS.getETIQUETA_CAIXA() + "' AND ESTADO != 'R'")
				.executeUpdate();
		entityManager.createNativeQuery("  UPDATE [CONTROLO_ETIQUETAS] SET TOTAL = TOTAL + 1, ESTADO = 'D' "
				+ " WHERE ETIQUETA = '" + CONTROLO_ETIQUETAS.getETIQUETA() + "' AND ETIQUETA_CAIXA != '"
				+ CONTROLO_ETIQUETAS.getETIQUETA_CAIXA() + "' AND ESTADO != 'R'").executeUpdate();
		return query_folder;
	}

	@GET
	@Path("/getCONTROLO_ETIQUETAS_ULTIMA_DUPLICADA")
	@Produces("application/json")
	public List getCONTROLO_ETIQUETAS_ULTIMA_DUPLICADA() {

		List query_folder = entityManager
				.createNativeQuery(
						"select top 1 a.ID, a.ETIQUETA,a.TOTAL,a.DATA_CRIA,a.DATA_MODIF,a.ETIQUETA_CAIXA from CONTROLO_ETIQUETAS a WHERE TOTAL > 1   order by a.DATA_MODIF desc")
				.getResultList();

		return query_folder;
	}

	/************************************* RP_GESTAO_ETIQUETAS_MATRIX */
	@POST
	@Path("/createRP_GESTAO_ETIQUETAS_MATRIX")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_GESTAO_ETIQUETAS_MATRIX insertRP_GESTAO_ETIQUETAS_MATRIX(final RP_GESTAO_ETIQUETAS_MATRIX data) {
		return dao21.create(data);
	}

	@GET
	@Path("/getRP_GESTAO_ETIQUETAS_MATRIX")
	@Produces("application/json")
	public List<RP_GESTAO_ETIQUETAS_MATRIX> getRP_GESTAO_ETIQUETAS_MATRIX() {
		return dao21.getall();
	}

	@GET
	@Path("/getRP_GESTAO_ETIQUETAS_MATRIXbyid/{id}")
	@Produces("application/json")
	public List<RP_GESTAO_ETIQUETAS_MATRIX> getRP_GESTAO_ETIQUETAS_MATRIXbyid(@PathParam("id") Integer id) {
		return dao21.getbyid(id);
	}

	@POST
	@Path("/getRP_GESTAO_ETIQUETAS_MATRIXbylote_referencia")
	@Produces("application/json")
	public List<RP_GESTAO_ETIQUETAS_MATRIX> getRP_GESTAO_ETIQUETAS_MATRIXbylote_referencia(
			final List<HashMap<String, String>> data) {
		HashMap<String, String> firstMap = data.get(0);

		String LOTE = firstMap.get("LOTE");
		String REF = firstMap.get("REF");

		return dao21.getbylote_referencia(LOTE, REF);
	}

	@DELETE
	@Path("/deleteRP_GESTAO_ETIQUETAS_MATRIX/{id}")
	public void deleteRP_GESTAO_ETIQUETAS_MATRIX(@PathParam("id") Integer id) {
		RP_GESTAO_ETIQUETAS_MATRIX RP_GESTAO_ETIQUETAS_MATRIX = new RP_GESTAO_ETIQUETAS_MATRIX();
		RP_GESTAO_ETIQUETAS_MATRIX.setID(id);
		dao21.delete(RP_GESTAO_ETIQUETAS_MATRIX);
	}

	@PUT
	@Path("/updateRP_GESTAO_ETIQUETAS_MATRIX")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_GESTAO_ETIQUETAS_MATRIX updateRP_GESTAO_ETIQUETAS_MATRIX(
			final RP_GESTAO_ETIQUETAS_MATRIX RP_GESTAO_ETIQUETAS_MATRIX) {
		RP_GESTAO_ETIQUETAS_MATRIX.setID(RP_GESTAO_ETIQUETAS_MATRIX.getID());
		return dao21.update(RP_GESTAO_ETIQUETAS_MATRIX);
	}

	@GET
	@Path("/getRP_GESTAO_ETIQUETAS_MATRIXatualizanumeroimpressoes/{id}/{estado}/{utz}/{chefe}")
	@Produces("application/json")
	public int getRP_GESTAO_ETIQUETAS_MATRIXatualizanumeroimpressoes(@PathParam("id") Integer id,
			@PathParam("estado") String estado, @PathParam("utz") String utz, @PathParam("chefe") String chefe) {

		int query_folder = entityManager.createNativeQuery("  UPDATE [RP_GESTAO_ETIQUETAS_MATRIX] SET ESTADO = '"
				+ estado + "' , NUMERO_IMPRESSOES = NUMERO_IMPRESSOES + 1 WHERE ID = " + id).executeUpdate();
		String idchefe = null;
		if (!chefe.equals("SEMCHEFE"))
			idchefe = chefe;
		
		atualizahistoricomatrix(id, estado, utz,idchefe);
		return query_folder;
	}

	public void atualizahistoricomatrix(Integer id, String estado, String utz,String chefe) {
		String descricao = "";
		String insertidchefe = "";
		if (estado.equals("I")) {
			descricao = "Utilizador imprimiu etiqueta.";
		} else if (estado.equals("R")) {
			descricao = "Utilizador reimprimiu etiqueta.";
		}
		
		if (chefe ==  null) {
			insertidchefe = " null ";
		} else if (estado.equals("R")) {
			insertidchefe = " '"+chefe+"'";
		}
		
		entityManager
				.createNativeQuery(
						"INSERT INTO [dbo].[RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX] ([ID_ETIQUETA_MATRIX] ,[DESCRICAO] ,[NUMERO_IMPRESSAO] ,[DATA_IMPRESSAO] ,[UTZ_IMPRESSAO],CHEFE) "
								+ "SELECT [ID] as [ID_ETIQUETA_MATRIX] ,'" + descricao
								+ "' as [DESCRICAO] ,[NUMERO_IMPRESSOES] ,GETDATE() as [DATA_IMPRESSAO] ,'" + utz
								+ "' as [UTZ_IMPRESSAO],"+insertidchefe+" as CHEFE FROM [dbo].[RP_GESTAO_ETIQUETAS_MATRIX] WHERE  ID = " + id)
				.executeUpdate();
	}

	/************************************* RP_CONF_RELACAO_REF_ETIQUETAS */
	@POST
	@Path("/createRP_CONF_RELACAO_REF_ETIQUETAS")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_RELACAO_REF_ETIQUETAS insertRP_CONF_RELACAO_REF_ETIQUETAS(final RP_CONF_RELACAO_REF_ETIQUETAS data) {
		return dao22.create(data);
	}

	@GET
	@Path("/getRP_CONF_RELACAO_REF_ETIQUETAS")
	@Produces("application/json")
	public List<RP_CONF_RELACAO_REF_ETIQUETAS> getRP_CONF_RELACAO_REF_ETIQUETAS() {
		return dao22.getall();
	}

	@GET
	@Path("/getRP_CONF_RELACAO_REF_ETIQUETASbyid/{id}")
	@Produces("application/json")
	public List<RP_CONF_RELACAO_REF_ETIQUETAS> getRP_CONF_RELACAO_REF_ETIQUETASbyid(@PathParam("id") Integer id) {
		return dao22.getbyid(id);
	}

	@DELETE
	@Path("/deleteRP_CONF_RELACAO_REF_ETIQUETAS/{id}")
	public void deleteRP_CONF_RELACAO_REF_ETIQUETAS(@PathParam("id") Integer id) {
		RP_CONF_RELACAO_REF_ETIQUETAS RP_CONF_RELACAO_REF_ETIQUETAS = new RP_CONF_RELACAO_REF_ETIQUETAS();
		RP_CONF_RELACAO_REF_ETIQUETAS.setID(id);
		dao22.delete(RP_CONF_RELACAO_REF_ETIQUETAS);
	}

	@PUT
	@Path("/updateRP_CONF_RELACAO_REF_ETIQUETAS")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_CONF_RELACAO_REF_ETIQUETAS updateRP_CONF_RELACAO_REF_ETIQUETAS(
			final RP_CONF_RELACAO_REF_ETIQUETAS RP_CONF_RELACAO_REF_ETIQUETAS) {
		RP_CONF_RELACAO_REF_ETIQUETAS.setID(RP_CONF_RELACAO_REF_ETIQUETAS.getID());
		return dao22.update(RP_CONF_RELACAO_REF_ETIQUETAS);
	}

	/************************************* RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX */
	@POST
	@Path("/createRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX insertRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX(
			final RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX data) {
		return dao23.create(data);
	}

	@GET
	@Path("/getRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX")
	@Produces("application/json")
	public List<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX> getRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX() {
		return dao23.getall();
	}

	@GET
	@Path("/getRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIXbyid/{id}")
	@Produces("application/json")
	public List<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX> getRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIXbyid(
			@PathParam("id") Integer id) {
		return dao23.getbyid(id);
	}

	@DELETE
	@Path("/deleteRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX/{id}")
	public void deleteRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX(@PathParam("id") Integer id) {
		RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX = new RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX();
		RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX.setID(id);
		dao23.delete(RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX);
	}

	@PUT
	@Path("/updateRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX")
	@Consumes("*/*")
	@Produces("application/json")
	public RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX updateRP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX(
			final RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX) {
		RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX.setID(RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX.getID());
		return dao23.update(RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX);
	}

	/***** IMPRESSAO ETIQUETAS *****/
	@GET
	@Path("/getGER_POSTOSbyip/{ip}")
	@Produces("application/json")
	public List<GER_POSTOS> getGER_POSTOSbyip(@PathParam("ip") String ip) {
		return dao24.getByIp(ip);
	}

	/* FICHEIRO ************************************/
	@GET
	@Path("/get/{format}/{filename}/{id}/{relatorio}/{subPASTA}")
	// @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	public Response getFile(@PathParam("format") String format, @PathParam("filename") String Name,
			@PathParam("id") Integer ID, @PathParam("relatorio") String relatorio,
			@PathParam("subPASTA") String subPASTA) {
		String fileName = null;
		String filepath = getFILEPATH();
		String subPASTA_path = subPASTA + "/";

		if (subPASTA.equals("nenhuma"))
			subPASTA_path = "";

		ReportGenerator report = new ReportGenerator();
		try {
			fileName = report.relatorio(format, Name, ID, relatorio, getURLJASPER(), filepath, subPASTA_path, null,
					null, null, null);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileName != null) {
			File file = new File("/" + filepath + "/relatorios/" + fileName);
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=report." + format + "");
			return response.build();
		} else {
			return null;
		}

	}

	public String getURLJASPER() {
		String url = "";
		Query query_folder = entityManager.createNativeQuery("select top 1 * from GER_PARAMETROS a");

		List<Object[]> dados_folder = query_folder.getResultList();

		for (Object[] content : dados_folder) {
			url = content[3].toString();
		}

		return url;
	}

	public String getFILEPATH() {
		String filepath = "";
		Query query_folder = entityManager
				.createNativeQuery("select top 1 PASTA_JASPERREPORT,PASTA_FICHEIRO from GER_PARAMETROS a");

		List<Object[]> dados_folder = query_folder.getResultList();

		for (Object[] content : dados_folder) {
			filepath = content[0].toString();
		}

		return filepath;
	}

}
