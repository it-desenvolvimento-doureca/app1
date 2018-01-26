package pt.example.rest;

import java.awt.geom.Arc2D.Float;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import pt.example.bootstrap.SendEmail;
import pt.example.bootstrap.conf;
import pt.example.dao.GER_EVENTODao;
import pt.example.dao.RPCONFUTZPERFDao;
import pt.example.dao.RPOFDao;
import pt.example.dao.RP_CONF_CHEF_SECDao;
import pt.example.dao.RP_CONF_FAMILIA_COMPDao;
import pt.example.dao.RP_CONF_OPDao;
import pt.example.dao.RP_CONF_OP_NPREVDao;
import pt.example.dao.RP_OF_DEF_LINDao;
import pt.example.dao.RP_OF_LST_DEFDao;
import pt.example.dao.RP_OF_OP_CABDao;
import pt.example.dao.RP_OF_OP_ETIQUETADao;
import pt.example.dao.RP_OF_OP_FUNCDao;
import pt.example.dao.RP_OF_OP_LINDao;
import pt.example.dao.RP_OF_OUTRODEF_LINDao;
import pt.example.dao.RP_OF_PARA_LINDao;
import pt.example.dao.RP_OF_PREP_LINDao;
import pt.example.entity.EMAIL;
import pt.example.entity.GER_EVENTO;
import pt.example.entity.GER_EVENTOS_CONF;
import pt.example.entity.RP_CONF_CHEF_SEC;
import pt.example.entity.RP_CONF_FAMILIA_COMP;
import pt.example.entity.RP_CONF_OP;
import pt.example.entity.RP_CONF_OP_NPREV;
import pt.example.entity.RP_CONF_UTZ_PERF;
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

	@GET
	@Path("/getRP_CONF_OPbyid/{id}")
	@Produces("application/json")
	public List<RP_CONF_OP> getRP_CONF_OPbyid(@PathParam("id") String id) {
		return dao3.getbyid(id);
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

	@GET
	@Path("/getRP_OF_CAB")
	@Produces("application/json")
	public List<RP_OF_CAB> listof() {
		return dao.getall();
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
	@Path("/verifica/{of_num}/{op_cod}/{op_num}")
	@Produces("application/json")
	public List<RP_OF_CAB> verifica(@PathParam("of_num") String of_num, @PathParam("op_cod") String op_cod,
			@PathParam("op_num") String op_num) {
		return dao.verifica(of_num, op_cod, op_num);
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
	@Path("/pesquisa_avancada")
	@Consumes("*/*")
	@Produces("application/json")
	public List<RP_OF_CAB> pesquisa_avancada(final List<HashMap<String, String>> data) throws ParseException {
		return dao.pesquisa_avancada(data);
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

	// RP_OF_PARA_LIN******************************************

	@GET
	@Path("/getbyallID_OP_CAB/{id}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyallID_OP_CAB(@PathParam("id") Integer id) {
		return dao9.getbyallID_OP_CAB(id);
	}

	@GET
	@Path("/getbyidRP_OF_PARA_LIN/{id}")
	@Produces("application/json")
	public List<RP_OF_PARA_LIN> getbyidRP_OF_PARA_LIN(@PathParam("id") Integer id) {
		return dao9.getbyid(id);
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

	@DELETE
	@Path("/deleteRP_OF_OP_ETIQUETA/{id}")
	public void deleteRP_OF_OP_ETIQUETA(@PathParam("id") Integer id) {
		RP_OF_OP_ETIQUETA RP_OF_OP_ETIQUETA = new RP_OF_OP_ETIQUETA();
		RP_OF_OP_ETIQUETA.setID_REF_ETIQUETA(id);
		dao13.delete(RP_OF_OP_ETIQUETA);
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

	// Atualizar campos M1 quando clica no botão editar
	@GET
	@Path("/atualizarcampos/{id}")
	@Produces("application/json")
	public int atualizarcampos(@PathParam("id") Integer id) {

		Query query = entityManager.createNativeQuery(
				"UPDATE RP_OF_DEF_LIN SET QUANT_DEF_M1 = QUANT_DEF_M2  where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ") "
						+ "										UPDATE RP_OF_OP_CAB SET TEMPO_PARA_TOTAL_M1 = TEMPO_PARA_TOTAL_M2  , TEMPO_EXEC_TOTAL_M1 = TEMPO_EXEC_TOTAL_M2 , TEMPO_PREP_TOTAL_M1 = TEMPO_PREP_TOTAL_M2  WHERE ID_OF_CAB = "
						+ id + ""
						+ "UPDATE RP_OF_OP_ETIQUETA SET QUANT_BOAS_M1 = QUANT_BOAS_M2 , QUANT_DEF_M1 = QUANT_DEF_M2  where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ")"
						+ "UPDATE RP_OF_OP_FUNC SET DATA_FIM_M1 = DATA_FIM_M2  , DATA_INI_M1 = DATA_INI_M2  , HORA_FIM_M1 = HORA_FIM_M2  , HORA_INI_M1 = HORA_INI_M2 where ID_OP_FUNC in (select c.ID_OP_FUNC from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_FUNC c on c.ID_OP_CAB = b.ID_OP_CAB where a.ID_OF_CAB = "
						+ id + " )"
						+ "UPDATE RP_OF_OP_LIN SET QUANT_BOAS_TOTAL_M1 = QUANT_BOAS_TOTAL_M2 , QUANT_DEF_TOTAL_M1 = QUANT_DEF_TOTAL_M2 where ID_OP_LIN in (select c.ID_OP_LIN from RP_OF_CAB a inner join RP_OF_OP_CAB b on b.ID_OF_CAB = a.ID_OF_CAB  inner join RP_OF_OP_LIN c on c.ID_OP_CAB = b.ID_OP_CAB  where a.ID_OF_CAB = "
						+ id + " or ID_OF_CAB_ORIGEM = " + id + ")");

		int dados = query.executeUpdate();

		return dados;
	}

	// CRIAR
	// FICHEIRO****************************************************************

	@GET
	@Path("/ficheiro/{id}/{estado}")
	@Produces("application/json")
	public void getFicheiro(@PathParam("id") Integer id, @PathParam("estado") String estado)
			throws IOException, ParseException {
		Integer comp_num = 1;

		String data = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
		String nome_ficheiro = "";

		Query query = entityManager.createNativeQuery(
				"select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM from RP_OF_CAB where ID_OF_CAB = " + id
						+ " or ID_OF_CAB_ORIGEM = " + id);

		List<Object[]> dados = query.getResultList();

		for (Object[] content : dados) {
			String inform_file = "";

			// se for PF cria ficheiro (se o estado for modificaçºão cria 2)
			if (content[1] == null) {
				Integer id_origem = Integer.parseInt(content[0].toString());
				inform_file = content[2].toString() + "_PF";
				if (estado.equals("M")) {

					nome_ficheiro = "correcao" + data + inform_file + ".txt";
					criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, null, estado);
				}

				nome_ficheiro = data + inform_file + ".txt";
				criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, null, estado);

				// se for COMP verifica se exitem etiquetas para o comp e cria
				// ficheiro
			} else {
				Integer id_origem = Integer.parseInt(content[1].toString());
				String data_query = "";
				if (estado.equals("M"))
					data_query = " and  c.VERSAO_MODIF != (select VERSAO_MODIF from RP_OF_CAB where ID_OF_CAB = "
							+ id_origem + ") ";
				Query query2 = entityManager.createNativeQuery(
						"select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA  from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where a.ID_OF_CAB = "
								+ Integer.parseInt(content[0].toString()) + data_query);

				List<Object[]> dados2 = query2.getResultList();

				Integer etiqueta_num = 1;
				for (Object[] content2 : dados2) {
					inform_file = content[2].toString() + "_C" + comp_num + "E" + etiqueta_num + ".txt";
					etiqueta_num++;
					Integer etiqueta = Integer.parseInt(content2[2].toString());
					Integer id_of_cab = Integer.parseInt(content2[1].toString());
					if (estado.equals("M")) {

						nome_ficheiro = "correcao" + data + inform_file + ".txt";
						criarFicheiro(id_of_cab, 1, nome_ficheiro, "COMP", content2[0].toString(), id_origem, etiqueta,
								estado);
					}

					nome_ficheiro = data + inform_file + ".txt";
					criarFicheiro(id_of_cab, 2, nome_ficheiro, "COMP", content2[0].toString(), id_origem, etiqueta,
							estado);
				}
				if (dados2.size() > 0)
					comp_num++;
			}

		}
	}

	public void criarFicheiro(Integer id, Integer ficheiro, String nome_ficheiro, String tipo, String of,
			Integer id_origem, Integer id_etiqueta, String estado) throws IOException, ParseException {
		String DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, SINAL, QUANT_BOAS_TOTAL, QUANT_BOAS, QUANT_DEF, TEMPO_PREP_TOTAL,
				TEMPO_EXEC_TOTAL = "";

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

		}

		BufferedWriter bw = null;
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		String data_atual = formate.format(new Date());
		FileWriter fw = null;
		String sequencia = "";
		String path = "";
		String data = "";
		String data_maquina = "";
		boolean existe_maquina = false;
		boolean lider = true;
		boolean primeira_linha = true;
		String data_inicio = "";

		Query query_folder = entityManager.createNativeQuery("select top 1 * from GER_PARAMETROS a");

		List<Object[]> dados_folder = query_folder.getResultList();

		for (Object[] content : dados_folder) {
			path = content[1] + nome_ficheiro;
		}

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

		try {

			Query query = entityManager
					.createNativeQuery("select a.OF_NUM,c.ID_UTZ_CRIA,a.OP_NUM,a.SEC_NUM,a.MAQ_NUM_ORIG,c." + DATA_INI
							+ ",c." + HORA_INI + ",c." + DATA_FIM + ",c." + HORA_FIM + ", " + "b." + TEMPO_PREP_TOTAL
							+ " as Decimalprep,b." + TEMPO_EXEC_TOTAL + " as Decimalexec "
							+ ",a.OP_PREVISTA,a.OP_COD_ORIGEM, (select ID_TURNO from RP_CONF_TURNO where CAST(c."
							+ HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno, "
							+ "CASE when (c.DATA_INI_M2 != c.DATA_INI_M1 or c.HORA_INI_M1 != c.HORA_INI_M2 or c.DATA_FIM_M2 != c.DATA_FIM_M1 or c.HORA_FIM_M1 != c.HORA_FIM_M2 or "
							+ "b.TEMPO_EXEC_TOTAL_M1 != b.TEMPO_EXEC_TOTAL_M2 or b.TEMPO_PREP_TOTAL_M1 != b.TEMPO_PREP_TOTAL_M2  ) then 1 else 0 END as alterado "
							+ " from RP_OF_CAB a "
							+ "inner join RP_OF_OP_CAB b on  b.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
							+ id + ")"
							+ "inner join RP_OF_OP_FUNC c on c.ID_OP_CAB in  (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
							+ id_origem + ") and b.ID_OP_CAB = c.ID_OP_CAB " + "where a.ID_OF_CAB = " + id);

			List<Object[]> dados = query.getResultList();

			for (Object[] content : dados) {
				// System.out.println(content[0]);
				data += "01        ";// Société
				data += data_atual; // Date suivi
				data += sequencia; // N° séquence
				if (content[11].toString().equals("1") || estado.equals("M")) {
					data += "    ";// + Ligne de production
				} else {
					data += (content[12] + "    ").substring(0, 4);// + Ligne de
																	// production
				}

				data += "1";// Type N° OF
				data += (of + "         ").substring(0, 10); // N° OF

				if (estado.equals("M")) {
					data += "1";// Type opération
				} else {
					data += content[11];// Type opération
				}

				if (content[11].toString().equals("1")) {
					data += ("0000" + content[2]).substring(("0000" + content[2]).length() - 4,
							("0000" + content[2]).length()); // N° Opération
				} else {
					data += ("    ").substring(0, 4);// N° Opération
				}

				// if (!content[4].toString().equals("000")) {
				if (content[4].toString().equals("000")) {
					if (primeira_linha) {
						data += "1";// Position ( S12 )
						primeira_linha = false;
					} else {
						data += "2";// Position ( S12 )
					}
				} else {
					data += "2";// Position ( S12 )
				}

				data += (content[3] + "         ").substring(0, 10);// Code
																	// section
				data += (content[4] + "         ").substring(0, 10); // Code
																		// sous-section
				if (content[13] != null) {
					data += content[13]; // N° d'équipe
				} else {
					data += "01";
				}

				// Type de ressource
				data += ("MO" + "         ").substring(0, 4);

				// Code ressource
				data += (content[1] + "         ").substring(0, 10);

				data += "   A"; // N° établissement + Type d'élément A
				data += content[5].toString().replaceAll("-", ""); // Date début
				data += content[6].toString().replace(":", "").substring(0, 6); // Heure
																				// début
				data += content[7].toString().replaceAll("-", ""); // Date
																	// fin
				data += content[8].toString().replace(":", "").substring(0, 6); // Heure
																				// fin
				data += "04002"; // Nombre de postes + Origine temps prépa.

				// Temps de préparation

				String temp_pre = "000000000000000";
				if (content[9] != null) {
					String[] parts = content[9].toString().split(":");

					double number = Double.parseDouble(parts[0]) + (Double.parseDouble(parts[1]) / 60)
							+ (Double.parseDouble(parts[2]) / 3600);
					String parts_prep = String.format("%.4f", number).replace(",", "");
					String size = temp_pre + parts_prep.replace("$", "0");
					temp_pre = (size).substring(size.length() - 15, size.length());
				}

				if (tipo.equals("PF") && !estado.equals("M")) {
					data += temp_pre;

				} else if (tipo.equals("PF") && content[14].toString().equals("1")) {
					data += temp_pre;
					alteracoes = true;
				} else {
					data += "000000000000000";
				}

				data += SINAL; // Signe
				data += "22"; // Arrêts compris + Origine temps exécution

				// Temps d'exécution
				String temp_exec = "000000000000000";
				if (content[10] != null) {
					String[] parts2 = content[10].toString().split(":");

					double number2 = Double.parseDouble(parts2[0]) + (Double.parseDouble(parts2[1]) / 60)
							+ (Double.parseDouble(parts2[2]) / 3600);
					String parts_exec = String.format("%.4f", number2).replace(",", "");
					String size = temp_exec + parts_exec.replace("$", "0");
					temp_exec = (size).substring(size.length() - 15, size.length());
				}
				if (tipo.equals("PF") && !estado.equals("M")) {
					data += temp_exec;
				} else if (tipo.equals("PF") && content[14].toString().equals("1")) {
					data += temp_exec;
				} else {
					data += "000000000000000";
				}

				data += SINAL; // Signe
				data += "22         \r\n"; // Arrêts compris + Etat opération +
											// N°lot Vérif
				if (lider) {
					if (!content[4].toString().equals("000")) {
						// System.out.println(content[4]);
						existe_maquina = true;
						StringBuffer buf = new StringBuffer(data);
						buf.replace(70, 84, "              ");
						buf.replace(47, 48, "1");
						data_maquina = buf.toString();
						lider = false;
					}
					data_inicio = data.substring(0, 87);
				}

			}

			if (existe_maquina && tipo.equals("PF")) {
				data_maquina += data;
				data = data_maquina;
			}

			if (tipo.equals("PF") && !estado.equals("M")) {
				Query query2 = entityManager.createNativeQuery("select c.DATA_INI,c.HORA_INI,c.DATA_FIM,c.HORA_FIM, "
						+ "cast((DATEDIFF(second,DATEADD(DAY, DATEDIFF(DAY, c.HORA_INI, c.DATA_INI ), CAST(c.HORA_INI AS DATETIME)), DATEADD(DAY, DATEDIFF(DAY, c.HORA_FIM, c.DATA_FIM ), CAST(c.HORA_FIM AS DATETIME)))/3600.00) as decimal(18,4)) as timediff, "
						+ "c.TIPO_PARAGEM,c.MOMENTO_PARAGEM from RP_OF_CAB a "
						+ "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
						+ "inner join RP_OF_PARA_LIN c on c.ID_OP_CAB = b.ID_OP_CAB " + "where a.ID_OF_CAB = " + id);

				List<Object[]> dados2 = query2.getResultList();

				for (Object[] content2 : dados2) {

					String data_pausa = "";
					data_pausa += "B"; // Type d'élément B
					data_pausa += content2[0].toString().replaceAll("-", ""); // Date
					// début
					data_pausa += content2[1].toString().replace(":", "").substring(0, 6); // Heure
																							// début
					data_pausa += content2[2].toString().replaceAll("-", ""); // Date
					// fin
					data_pausa += content2[3].toString().replace(":", "").substring(0, 6); // Heure
																							// fin

					data_pausa += (content2[5] + "    ").substring(0, 4);// Code
																			// section

					data_pausa += "3"; // Origine arrêt prépa.

					// Temps d'arrêt/prépa.

					String temp_pre = "000000000000000";
					if (content2[6].toString().equals("P")) {
						String parts_prep = (content2[4].toString()).replace(".", "");
						String size = temp_pre + parts_prep;
						temp_pre = (size).substring(size.length() - 15, size.length());
					}
					data_pausa += temp_pre;
					data_pausa += SINAL; // Signe
					data_pausa += "3"; // Origine arrêt exécution

					// Temps d'arrêt/exécution
					String temp_exec = "000000000000000";
					if (content2[6].toString().equals("E")) {
						String parts_exec = content2[4].toString().replace(".", "");
						String size = temp_exec + parts_exec;
						temp_exec = (size).substring(size.length() - 15, size.length());
					}
					data_pausa += temp_exec;
					data_pausa += SINAL; // Signe
					data_pausa += "                                       \r\n"; // Texte
																					// libre
					if (existe_maquina && tipo.equals("PF"))
						data += data_maquina.substring(0, 87) + data_pausa;
					data += data_inicio + data_pausa;
				}
			}

			String data3 = "";

			data3 = " ,CASE when (c.QUANT_BOAS_TOTAL_M1 != c.QUANT_BOAS_TOTAL_M2 or e.QUANT_BOAS_M1 != e.QUANT_BOAS_M2   ) then 1 else 0 END as alterado ";

			Query query3 = entityManager.createNativeQuery(
					"Select a.ID_OF_CAB_ORIGEM,a.OF_NUM,e.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,d."
							+ DATA_INI + ",d." + HORA_INI + ",d." + DATA_FIM + ",d." + HORA_FIM
							+ ",d.ID_UTZ_CRIA,c.REF_IND,cast(c." + QUANT_BOAS_TOTAL
							+ " as decimal(18,4)) as qtd1,cast(e." + QUANT_BOAS + " as decimal(18,4)) as qtd2 "
							+ ", a.OP_PREVISTA, c.OBS_REF, (select ID_TURNO from RP_CONF_TURNO where CAST( d."
							+ HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno, a.OP_COD_ORIGEM "
							+ data3 + " from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
							+ "inner join RP_OF_OP_LIN c on  b.ID_OP_CAB = c.ID_OP_CAB "
							+ "inner join RP_OF_OP_FUNC d on d.ID_OP_CAB = b.ID_OP_CAB and d.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = "
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
							+ data3 + " from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
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

				data_quantidades += "01        ";// Société
				data_quantidades += data_atual; // Date suivi

				data_quantidades += sequencia; // N° séquence

				if (content3[18].toString().equals("1") || estado.equals("M")) {
					data_quantidades += "    ";// + Ligne de production
				} else {
					data_quantidades += (content3[21] + "    ").substring(0, 4);// +
																				// Ligne
																				// de
					// production
				}

				data_quantidades += "1";// Type N° OF

				if (content3[0] == null) {
					data_quantidades += (content3[1] + "         ").substring(0, 10); // N°
																						// OF
				} else {
					data_quantidades += (content3[2] + "         ").substring(0, 10); // N°
																						// OF
				}

				if (estado.equals("M")) {
					data_quantidades += "1";// Type opération
				} else {
					data_quantidades += content3[18];// Type opération
				}

				if (content3[18].toString().equals("1")) {
					data_quantidades += ("0000" + content3[3]).substring(("0000" + content3[3]).length() - 4,
							("0000" + content3[3]).length()); // N° Opération
				} else {
					data_quantidades += ("    ").substring(0, 4);// N° Opération
				}

				data_quantidades += "1";// Position ( S12 )

				data_quantidades += (content3[9] + "         ").substring(0, 10);// Code
				// section
				data_quantidades += (content3[8] + "         ").substring(0, 10); // Code
				// sous-section

				if (content3[20] != null) {
					data_quantidades += content3[20]; // N° d'équipe
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

				data_quantidades += "   Q"; // N° établissement + Type d'élément
											// Q

				data_quantidades += content3[10].toString().replaceAll("-", ""); // Date
																					// début
				data_quantidades += content3[11].toString().replace(":", "").substring(0, 6); // Heure
				// début
				data_quantidades += content3[12].toString().replaceAll("-", ""); // Date
				// fin
				data_quantidades += content3[13].toString().replace(":", "").substring(0, 6); // Heure
				// fin

				// Référence produit
				data_quantidades += (content3[4] + "                 ").substring(0, 17);
				// Variante (1)
				data_quantidades += (content3[5] + "                 ").substring(0, 10);
				// Variante (2)
				data_quantidades += (content3[6] + "                 ").substring(0, 10);
				// Indice produit
				data_quantidades += (content3[15] + "                 ").substring(0, 10);
				// N° enreg. Produit
				if (content3[7] != null) {
					data_quantidades += ("000000000" + content3[7]).substring(("000000000" + content3[7]).length() - 9,
							("000000000" + content3[7]).length());
				} else {
					data_quantidades += "000000000";
				}

				data_quantidades += "1";// PType quantité

				// Quantité bonne
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
				data_quantidades += "    "; // Unité
				data_quantidades += "000000000000000"; // Qté bonne (US2)
				// N° d'étiquette suivie + N° enreg. étiquette + Lieu ( entrée )
				// + Emplacement ( entrée ) +
				// Référence du lot ( entrée ) + N° d'étiquette ( entrée )
				// +Texte libre
				data_quantidades += "                                                                                    ";
				String obs = (content3[19] != null) ? content3[19].toString() : "";

				data_quantidades += (obs + "                                        ").substring(0, 39);
				data_quantidades += "\r\n";
				data += data_quantidades;
				/*
				 * StringBuffer buf = new StringBuffer(data_quantidades);
				 * buf.replace(70, 84, "              "); data_maquina =
				 * buf.toString(); data += data_maquina;
				 */

			}

			String data4 = "";

			if (estado.equals("M")) {
				if (ficheiro == 1) {
					data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2) ";
				} else {
					data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M2 != 0) ";
				}

			}

			Query query4 = entityManager.createNativeQuery("Select d.COD_DEF,cast(d." + QUANT_DEF
					+ " as decimal(18,4)),a.ID_OF_CAB_ORIGEM,a.OF_NUM,f.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,e."
					+ DATA_INI + ",e." + HORA_INI + ",e." + DATA_FIM + ",e." + HORA_FIM + ", "
					+ "d.ID_UTZ_CRIA,c.REF_IND,c." + QUANT_BOAS_TOTAL + ",f." + QUANT_BOAS + " ,d.OBS_DEF "
					+ ", a.OP_PREVISTA , (select ID_TURNO from RP_CONF_TURNO where CAST( e." + HORA_INI
					+ "  as time) between HORA_INICIO and HORA_FIM ) as turno,a.OP_COD_ORIGEM " + "from RP_OF_CAB a "
					+ "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
					+ "inner join RP_OF_OP_LIN c on  b.ID_OP_CAB = c.ID_OP_CAB "
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
					+ "  as time) between HORA_INICIO and HORA_FIM ) as turno, a.OP_COD_ORIGEM  " + "from RP_OF_CAB a "
					+ "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB "
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
				data_defeitos += "01        ";// Société
				data_defeitos += data_atual; // Date suivi

				data_defeitos += sequencia; // N° séquence
				if (content4[21].toString().equals("1") || estado.equals("M")) {
					data_defeitos += "    ";// + Ligne de production
				} else {
					data_defeitos += (content4[23] + "    ").substring(0, 4);// +
																				// Ligne
																				// de
					// production
				}
				data_defeitos += "1";// Type N° OF

				if (content4[2] == null) {
					data_defeitos += (content4[3] + "         ").substring(0, 10); // N°
																					// OF
				} else {
					data_defeitos += (content4[4] + "         ").substring(0, 10); // N°
																					// OF
				}

				if (estado.equals("M")) {
					data_defeitos += "1";// Type opération
				} else {
					data_defeitos += content4[21];// Type opération
				}

				if (content4[21].toString().equals("1")) {
					data_defeitos += ("0000" + content4[5]).substring(("0000" + content4[5]).length() - 4,
							("0000" + content4[5]).length()); // N° Opération

				} else {
					data_defeitos += ("    ").substring(0, 4);// N° Opération
				}

				data_defeitos += "1";// Position ( S12 )

				data_defeitos += (content4[11] + "         ").substring(0, 10);// Code
				// section
				data_defeitos += (content4[10] + "         ").substring(0, 10); // Code
				// sous-section
				if (content4[22] != null) {
					data_defeitos += content4[22]; // N° d'équipe
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

				data_defeitos += "   R"; // N° établissement + Type d'élément Q

				data_defeitos += content4[12].toString().replaceAll("-", ""); // Date
																				// début
				data_defeitos += content4[13].toString().replace(":", "").substring(0, 6); // Heure
				// début
				data_defeitos += content4[14].toString().replaceAll("-", ""); // Date
				// fin
				data_defeitos += content4[15].toString().replace(":", "").substring(0, 6); // Heure
				// fin

				// Référence produit
				data_defeitos += (content4[6] + "                 ").substring(0, 17);
				// Variante (1)
				data_defeitos += (content4[7] + "                 ").substring(0, 10);
				// Variante (2)
				data_defeitos += (content4[8] + "                 ").substring(0, 10);
				// Indice produit
				data_defeitos += (content4[17] + "                 ").substring(0, 10);
				// N° enreg. Produit
				if (content4[9] != null) {
					data_defeitos += ("000000000" + content4[9]).substring(("000000000" + content4[9]).length() - 9,
							("000000000" + content4[9]).length());
				} else {
					data_defeitos += "000000000";
				}

				// Code rebut
				data_defeitos += (content4[0] + "    ").substring(0, 4);

				data_defeitos += "1"; // Type quantité

				// Quantité rebutée
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

			/// String data = "Campo1: \r\n" + "Campo2:\r\n" + "Campo3:\r\n" +
			/// "Campo4:";

			if (!estado.equals("M")) {
				File file = new File(path);

				// if file doesnt exists, then create it

				file.createNewFile();

				// true = append file
				fw = new FileWriter(file.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);

				bw.write(data);
			} else if (estado.equals("M") && alteracoes) {
				File file = new File(path);

				// if file doesnt exists, then create it

				file.createNewFile();

				// true = append file
				fw = new FileWriter(file.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);

				bw.write(data);
			}

		} catch (

		IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
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
		String[] keyValuePairs = value.split(",");

		Query query3 = entityManager.createQuery("Select a from GER_EVENTOS_CONF a where MODULO = "
				+ firstMap.get("MODULO") + " and MOMENTO = '" + firstMap.get("MOMENTO") + "' " + "and PAGINA = '"
				+ firstMap.get("PAGINA") + "' and ESTADO = " + firstMap.get("ESTADO") + "");
		List<GER_EVENTOS_CONF> dados = query3.getResultList();

		for (GER_EVENTOS_CONF borderTypes : dados) {

			// System.out.println(borderTypes.getEMAIL_ASSUNTO());
			EMAIL email = new EMAIL();
			email.setASSUNTO(borderTypes.getEMAIL_ASSUNTO());
			email.setDE("alertas.it.doureca@gmail.com");

			email.setPARA(borderTypes.getEMAIL_PARA());
			String mensagem = borderTypes.getEMAIL_MENSAGEM();
			for (String pair : keyValuePairs) {
				String[] entry = pair.split(":");
				mensagem = mensagem.replace("{" + entry[0].trim() + "}", entry[1].trim());
			}
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

		System.out.println(mensagem);

		/*
		 * ArrayList<String> campos = new ArrayList<String>(); Pattern p =
		 * Pattern.compile(Pattern.quote("{") + "(.*?)" + Pattern.quote("}"));
		 * Matcher m = p.matcher(mensagem); while (m.find()) { if
		 * (!campos.contains(m.group(1))) { campos.add(m.group(1)); //
		 * System.out.println(m.group(1)); } }
		 */

		return mensagem;
	}

}
