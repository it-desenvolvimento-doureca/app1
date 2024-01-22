package pt.example.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import pt.example.bootstrap.ConnectProgress;
import pt.example.bootstrap.SendEmail;
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
import pt.example.dao.VERSAO_APPDao;
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

	@POST
	@Path("/createRP_CONF_UTZ_PERF")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_CONF_UTZ_PERF updateuser(RP_CONF_UTZ_PERF RP_CONF_UTZ_PERF) {
		RP_CONF_UTZ_PERF.setID_CONF_UTZ_PERF(RP_CONF_UTZ_PERF.getID_CONF_UTZ_PERF());
		return (RP_CONF_UTZ_PERF) this.dao1.update(RP_CONF_UTZ_PERF);
	}

	@POST
	@Path("/createRP_CONF_CHEF_SEC")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_CONF_CHEF_SEC updateuser(RP_CONF_CHEF_SEC RP_CONF_CHEF_SEC) {
		RP_CONF_CHEF_SEC.setID_UTZ(RP_CONF_CHEF_SEC.getID_UTZ());
		return (RP_CONF_CHEF_SEC) this.dao2.update(RP_CONF_CHEF_SEC);
	}

	@GET
	@Path("/getRP_CONF_OP")
	@Produces({"application/json"})
	public List<RP_CONF_OP> getRP_CONF_OP() {
		return this.dao3.getall();
	}

	@POST
	@Path("/createRP_CONF_OP")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_CONF_OP updateRP_CONF_OP(RP_CONF_OP RP_CONF_OP) {
		RP_CONF_OP.setID_OP_PRINC(RP_CONF_OP.getID_OP_PRINC());
		return (RP_CONF_OP) this.dao3.update(RP_CONF_OP);
	}

	@PUT
	@Path("/getRP_CONF_OPbyid")
	@Produces({"application/json"})
	public List<RP_CONF_OP> getRP_CONF_OPbyid(String id) {
		return this.dao3.getbyid(id.replace("\"", ""));
	}

	@GET
	@Path("/getRP_CONF_OP_NPREV")
	@Produces({"application/json"})
	public List<RP_CONF_OP_NPREV> getRP_CONF_OP_NPREV() {
		return this.dao4.allEntries();
	}

	@POST
	@Path("/createRP_CONF_OP_NPREV")
	@Consumes({"**"})
	@Produces({"application/json"})
	public List<RP_OF_CAB> listof(String data, @PathParam("start") Integer start) {
		return this.dao.getall(data, start);
	}

	@POST
	@Path("/getRP_OF_CAB2/{start}")
	@Consumes({"**"})
	@Produces({"application/json"})
	public List<RP_OF_CAB> listallof(List<HashMap<String, String>> data) {
		return this.dao.getalllist(data);
	}

	@GET
	@Path("/getRP_OF_CABbyid/{id}")
	@Produces({"application/json"})
	public List<RP_OF_CAB> listofbyid(@PathParam("id") Integer id) {
		return this.dao.getallbyid(id);
	}

	@GET
	@Path("/listofcurrentof/{id}")
	@Produces({"application/json"})
	public List<RP_OF_CAB> listofcurrentof(@PathParam("id") String id) {
		return this.dao.getbyid(id);
	}

	@GET
	@Path("/getof/{id}")
	@Produces({"application/json"})
	public List<RP_OF_CAB> getof(@PathParam("id") Integer id) {
		return this.dao.getof(id);
	}

	@GET
	@Path("/verifica/{of_num}/{op_cod}/{op_num}/{user}")
	@Produces({"application/json"})
	public List<RP_OF_CAB> verifica(@PathParam("of_num") String of_num, @PathParam("op_cod") String op_cod,
			@PathParam("op_num") String op_num, @PathParam("user") String user) {
		return this.dao.verifica(of_num, op_cod, op_num, user);
	}

	@POST
	@Path("/createRP_OF_CAB")
	@Consumes({"**"})
	@Produces({"application/json"})
	public List<RP_OF_DEF_LIN> insertupdateRP_OF_DEF_LIN(RP_OF_DEF_LIN data) {
		return this.dao5.createupdate(data);
	}

	@POST
	@Path("/pesquisa_avancada/{start}")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_CAB updateRP_OF_CAB(RP_OF_CAB RP_OF_CAB) {
		RP_OF_CAB.setESTADO(RP_OF_CAB.getESTADO());
		return (RP_OF_CAB) this.dao.update(RP_OF_CAB);
	}

	@POST
	@Path("/createupdateESTADOS")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_OP_CAB insertRP_OF_OP_CAB(RP_OF_OP_CAB data) {
		return (RP_OF_OP_CAB) this.dao6.create(data);
	}

	@POST
	@Path("/getdataof/{id}/{user}")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_OP_CAB updateRP_OF_OP_CAB(RP_OF_OP_CAB RP_OF_OP_CAB) {
		RP_OF_OP_CAB.setID_OF_CAB(RP_OF_OP_CAB.getID_OF_CAB());
		return (RP_OF_OP_CAB) this.dao6.update(RP_OF_OP_CAB);
	}

	@POST
	@Path("/createRP_OF_OP_LIN")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_OP_LIN updateRP_OF_OP_LIN(RP_OF_OP_LIN RP_OF_OP_LIN) {
		RP_OF_OP_LIN.setQUANT_DEF_TOTAL(RP_OF_OP_LIN.getQUANT_DEF_TOTAL());
		return (RP_OF_OP_LIN) this.dao7.update(RP_OF_OP_LIN);
	}

	@GET
	@Path("/getbyidRP_OF_PREP_LIN/{id}")
	@Produces({"application/json"})
	public List<RP_OF_PREP_LIN> getbyidRP_OF_PREP_LIN(@PathParam("id") Integer id) {
		return this.dao8.getbyid(id);
	}

	@GET
	@Path("/getbyidRP_OF_PREP_LIN2/{id}")
	@Produces({"application/json"})
	public List<RP_OF_PREP_LIN> getbyidRP_OF_PREP_LIN2(@PathParam("id") Integer id) {
		return this.dao8.getbyid2(id);
	}

	@POST
	@Path("/createRP_OF_PREP_LIN")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_PREP_LIN updateRP_OF_PREP_LIN(RP_OF_PREP_LIN RP_OF_PREP_LIN) {
		RP_OF_PREP_LIN.setESTADO(RP_OF_PREP_LIN.getESTADO());
		return (RP_OF_PREP_LIN) this.dao8.update(RP_OF_PREP_LIN);
	}

	@GET
	@Path("/getRP_CONF_FAMILIA_COMP")
	@Produces({"application/json"})
	public List<RP_CONF_FAMILIA_COMP> getRP_CONF_FAMILIA_COMP() {
		return this.dao10.allEntries();
	}

	@GET
	@Path("/getRP_CONF_FAMILIA_COMPcodfam/{codfam}")
	@Produces({"application/json"})
	public List<RP_CONF_FAMILIA_COMP> getRP_CONF_FAMILIA_COMPcodfam(@PathParam("codfam") String codfam) {
		return this.dao10.getbycodfam(codfam);
	}

	@POST
	@Path("/createRP_CONF_FAMILIA_COMP")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_CONF_FAMILIA_COMP updateRP_CONF_FAMILIA_COMP(RP_CONF_FAMILIA_COMP RP_CONF_FAMILIA_COMP) {
		RP_CONF_FAMILIA_COMP.setCOD_FAMILIA_COMP(RP_CONF_FAMILIA_COMP.getCOD_FAMILIA_COMP());
		return (RP_CONF_FAMILIA_COMP) this.dao10.update(RP_CONF_FAMILIA_COMP);
	}

	@DELETE
	@Path("/deleteRP_CONF_FAMILIA_COMP/{id}")
	public void deleteRP_CONF_FAMILIA_COMP(@PathParam("id") String id) {
		RP_CONF_FAMILIA_COMP RP_CONF_FAMILIA_COMP = new RP_CONF_FAMILIA_COMP();
		RP_CONF_FAMILIA_COMP.setCOD_FAMILIA_COMP(id);
		this.dao10.delete(RP_CONF_FAMILIA_COMP);
	}

	@GET
	@Path("/getbyidRP_OF_OP_FUNC")
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNC() {
		return this.dao11.allEntries();
	}

	@POST
	@Path("/createRP_OF_OP_FUNC")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_OP_FUNC updateRP_OF_OP_FUNC(RP_OF_OP_FUNC RP_OF_OP_FUNC) {
		RP_OF_OP_FUNC.setID_OP_FUNC(RP_OF_OP_FUNC.getID_OP_FUNC());
		return (RP_OF_OP_FUNC) this.dao11.update(RP_OF_OP_FUNC);
	}

	@GET
	@Path("/checkuser/{user}")
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> checkuser(@PathParam("user") String user) {
		return this.dao11.checkuser(user);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCid/{id}/{user}")
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNCid(@PathParam("id") Integer id, @PathParam("user") String user) {
		return this.dao11.getbyallID_OP_CAB(id, user);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCusers/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNC_users(@PathParam("id") Integer id) {
		return this.dao11.getUsers(id);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCuser/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNC_user(@PathParam("id") Integer id) {
		return this.dao11.getUser(id);
	}

	@GET
	@Path("/getRP_OF_OP_FUNCallusers/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> getRP_OF_OP_FUNCall_users(@PathParam("id") Integer id) {
		return this.dao11.getallUsers(id);
	}

	@GET
	@Path("/getallUsersTEMPPREP/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> getallUsersTEMPPREP(@PathParam("id") Integer id) {
		return this.dao11.getallUsersTEMPPREP(id);
	}

	@GET
	@Path("/getbyallID_OP_CAB/{id}")
	@Produces({"application/json"})
	public List<RP_OF_PARA_LIN> getbyallID_OP_CAB(@PathParam("id") Integer id) {
		return this.dao9.getbyallID_OP_CAB(id);
	}

	@GET
	@Path("/getbyallUSER/{id}/{user}")
	@Produces({"application/json"})
	public List<RP_OF_PARA_LIN> getbyallUSER(@PathParam("id") Integer id, @PathParam("user") String user) {
		return this.dao9.getbyallUser(id, user);
	}

	@GET
	@Path("/getbyallUSERIDOFCAB/{id}/{user}")
	@Produces({"application/json"})
	public List<RP_OF_PARA_LIN> getbyallUSERIDOFCAB(@PathParam("id") Integer id, @PathParam("user") String user) {
		return this.dao9.getbyallUserIDOFCAB(id, user);
	}

	@GET
	@Path("/getbyidRP_OF_PARA_LIN/{id}")
	@Produces({"application/json"})
	public List<RP_OF_PARA_LIN> getbyidRP_OF_PARA_LIN(@PathParam("id") Integer id) {
		return this.dao9.getbyid(id);
	}

	@GET
	@Path("/apagapausasbyid_op_cab/{id}")
	@Produces({"application/json"})
	public void apagapausasbyid_op_cab(@PathParam("id") Integer id) {
		Query query = this.entityManager.createNativeQuery(
				"delete RP_OF_PARA_LIN where ID_OP_CAB = " + id + " and DATA_FIM is null and DATA_FIM_M1 is null");
		query.executeUpdate();
	}

	@GET
	@Path("/getbyidRP_OF_PARA_LIN2/{id}")
	@Produces({"application/json"})
	public List<RP_OF_PARA_LIN> getbyidRP_OF_PARA_LIN2(@PathParam("id") Integer id) {
		return this.dao9.getbyid2(id);
	}

	@GET
	@Path("/getbyid_op_cabRP_OF_PARA_LIN/{id}")
	@Produces({"application/json"})
	public List<RP_OF_PARA_LIN> getbyid_op_cabRP_OF_PARA_LIN(@PathParam("id") Integer id) {
		return this.dao9.getbyid_op_cab(id);
	}

	@POST
	@Path("/createRP_OF_PARA_LIN")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_PARA_LIN updateRP_OF_PARA_LIN(RP_OF_PARA_LIN RP_OF_PARA_LIN) {
		RP_OF_PARA_LIN.setESTADO(RP_OF_PARA_LIN.getESTADO());
		return (RP_OF_PARA_LIN) this.dao9.update(RP_OF_PARA_LIN);
	}

	@POST
	@Path("/createRP_OF_DEF_LIN")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_DEF_LIN updateRP_OF_DEF_LIN(RP_OF_DEF_LIN RP_OF_DEF_LIN) {
		RP_OF_DEF_LIN.setQUANT_DEF(RP_OF_DEF_LIN.getQUANT_DEF());
		return (RP_OF_DEF_LIN) this.dao5.update(RP_OF_DEF_LIN);
	}

	@DELETE
	@Path("/deleteRP_OF_DEF_LIN/{id}/{etiqueta}")
	public void deleteRP_OF_DEF_LIN(@PathParam("id") Integer id, @PathParam("etiqueta") Integer etiqueta) {
		this.dao5.apagar_id_op_lin(id, etiqueta);
	}

	@DELETE
	@Path("/deleteRP_OF_DEF_LIN2/{id}")
	public void apagar_id_DEF_LIN(@PathParam("id") Integer id) {
		this.dao5.apagar_id_DEF_LIN(id);
	}

	@POST
	@Path("/createRP_OF_OUTRODEF_LIN")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_OUTRODEF_LIN updateRP_OF_OUTRODEF_LIN(RP_OF_OUTRODEF_LIN RP_OF_OUTRODEF_LIN) {
		RP_OF_OUTRODEF_LIN.setQUANT_OUTRODEF(RP_OF_OUTRODEF_LIN.getQUANT_OUTRODEF());
		return (RP_OF_OUTRODEF_LIN) this.dao12.update(RP_OF_OUTRODEF_LIN);
	}

	@POST
	@Path("/createRP_OF_OP_ETIQUETA")
	@Consumes({"**"})
	@Produces({"application/json"})
	public RP_OF_OP_ETIQUETA updateRP_OF_OP_ETIQUETA(RP_OF_OP_ETIQUETA RP_OF_OP_ETIQUETA) {
		RP_OF_OP_ETIQUETA.setQUANT_BOAS(RP_OF_OP_ETIQUETA.getQUANT_BOAS());
		return (RP_OF_OP_ETIQUETA) this.dao13.update(RP_OF_OP_ETIQUETA);
	}

	@POST
	@Path("/createRP_OF_LST_DEF")
	@Consumes({"**"})
	@Produces({"application/json"})
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEFgetfam(ArrayList<String> data) {
		return this.dao14.getfam(data);
	}

	@GET
	@Path("/getRP_OF_LST_DEFbyid_op_lin/{id}")
	@Produces({"application/json"})
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEFbyid_op_lin(@PathParam("id") Integer id) {
		return this.dao14.getdef(id);
	}

	@DELETE
	@Path("/deleteRP_OF_LST_DEF/{id}")
	public void deleteRP_OF_LST_DEF(@PathParam("id") Integer id) {
		RP_OF_LST_DEF RP_OF_LST_DEF = new RP_OF_LST_DEF();
		RP_OF_LST_DEF.setID_LST_DEF(id);
		this.dao14.delete(RP_OF_LST_DEF);
	}

	@DELETE
	@Path("/deleteRP_OF_LST_DEFid_op_lin/{id}")
	public void deleteRP_OF_LST_DEFid_op_lin(@PathParam("id") Integer id) {
		this.dao14.apagar_idop_lin(id);
	}

	@GET
	@Path("/getVERSAO_APP")
	@Produces({"application/json"})
	public List<VERSAO_APP> getVERSAO_APPO() {
		return this.dao16.getallv();
	}

	@POST
	@Path("/createGER_EVENTO")
	@Consumes({"**"})
	@Produces({"application/json"})
	public GER_EVENTO updateGER_EVENTO(GER_EVENTO GER_EVENTO) {
		GER_EVENTO.setMENSAGEM(GER_EVENTO.getMENSAGEM());
		return (GER_EVENTO) this.dao15.update(GER_EVENTO);
	}

	@GET
	@Path("/atualizarcampos/{id}")
	@Produces({"application/json"})
	public int atualizarcampos(@PathParam("id") Integer id) {
		Query query = this.entityManager.createNativeQuery(
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
		Query query = this.entityManager.createNativeQuery(
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
	@Produces({"application/json"})
	public List<RP_OF_OP_ETIQUETA> verificaopnum(@PathParam("id") Integer id) {
		return this.dao13.verificaopnum(id);
	}

	@GET
	@Path("/atualizaropenum/{id}")
	@Produces({"application/json"})
	public void atualizaropenum(@PathParam("id") Integer id) {
		this.atualiza_editar(id, this.getURL());
	}

	@GET
	@Path("/testeligacao")
	@Produces({"application/json"})
	public boolean testeligacao() {
		return true;
	}

	@GET
	@Path("/atualizarestado/{id}/{user}/{estado}")
	@Produces({"application/json"})
	public void atualizarestado(@PathParam("id") Integer id, @PathParam("user") String user,
			@PathParam("estado") String estado) {
		this.entityManager
				.createNativeQuery("UPDATE RP_OF_OP_FUNC SET ESTADO = '" + estado
						+ "' ,DATA_HORA_MODIF = GETDATE(),ID_UTZ_MODIF = '" + user
						+ "' WHERE ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB =  " + id + ")")
				.executeUpdate();
		String query_user = "";
		if (estado.equals("R")) {
			query_user = ",ID_UTZ_EDICAO = '" + user + "' ,ESTADO_INICIAL = ESTADO";
		} else {
			this.atualizarcampos2(id);
		}

		this.entityManager.createNativeQuery(
				"UPDATE RP_OF_CAB SET ESTADO = '" + estado + "',DATA_HORA_MODIF = GETDATE(),ID_UTZ_MODIF = '" + user
						+ "' " + query_user + " WHERE ID_OF_CAB = " + id)
				.executeUpdate();
	}

	@GET
	@Path("/atualizartotais/{id}/{modo}")
	@Produces({"application/json"})
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

		this.entityManager.createNativeQuery(
				"DELETE RP_OF_DEF_LIN WHERE ID_DEF_LIN in (SELECT T2.ID_DEF_LIN FROM RP_OF_DEF_LIN T1,RP_OF_DEF_LIN T2 WHERE  T1.ID_DEF_LIN < T2.ID_DEF_LIN AND  T1.COD_DEF = T2.COD_DEF AND  T1.ID_OP_LIN = T2.ID_OP_LIN AND T1.ID_OP_LIN = (select top 1 ID_OP_LIN from RP_OF_OP_LIN where ID_OP_CAB = "
						+ id + ") )")
				.executeUpdate();
		this.entityManager.createNativeQuery(
				"DELETE RP_OF_DEF_LIN WHERE ID_DEF_LIN in (SELECT T2.ID_DEF_LIN FROM RP_OF_DEF_LIN T1,RP_OF_DEF_LIN T2 WHERE  T1.ID_DEF_LIN < T2.ID_DEF_LIN AND  T1.COD_DEF = T2.COD_DEF AND  T1.ID_OP_LIN = T2.ID_OP_LIN AND T1.ID_REF_ETIQUETA = T2 .ID_REF_ETIQUETA AND T1.ID_REF_ETIQUETA is not null  AND T2.ID_REF_ETIQUETA is not null AND T1.ID_OP_LIN in (select ID_OP_LIN from RP_OF_OP_LIN where ID_OP_CAB in ( select ID_OP_CAB from  RP_OF_OP_CAB a inner join RP_OF_CAB b on a.ID_OF_CAB = b.ID_OF_CAB where b.ID_OF_CAB_ORIGEM in (select ID_OF_CAB from RP_OF_OP_CAB where ID_OP_CAB = "
						+ id + ") )))")
				.executeUpdate();
		this.entityManager.createNativeQuery("UPDATE tt  set " + query + " from "
				+ "(select ID_OP_CAB, (select coalesce(SUM(dd.QUANT_DEF_M2),0) from RP_OF_DEF_LIN dd inner join   RP_OF_OP_ETIQUETA ee on dd.ID_OP_LIN = ee.ID_OP_LIN and dd.ID_REF_ETIQUETA = ee.ID_REF_ETIQUETA where dd.ID_OP_LIN = a.ID_OP_LIN and ee.ATIVO = 1) as def "
				+ ",(select coalesce(SUM(x.QUANT_BOAS_M2),0) from RP_OF_OP_ETIQUETA x where ID_OP_LIN = a.ID_OP_LIN and x.ATIVO = 1) as boas from RP_OF_OP_LIN a "
				+ "where ID_OP_CAB in (Select b.ID_OP_CAB from RP_OF_OP_CAB b "
				+ "inner join RP_OF_CAB c on b.ID_OF_CAB = c.ID_OF_CAB where c.ID_OF_CAB_ORIGEM in  (select ID_OF_CAB from RP_OF_OP_CAB where ID_OP_CAB = "
				+ id + ") )) as r ,RP_OF_OP_LIN as tt where tt.ID_OP_CAB = r.ID_OP_CAB").executeUpdate();
		this.entityManager.createNativeQuery("UPDATE tt  set " + query2 + " from "
				+ "(select ID_OP_LIN, (select coalesce(SUM(QUANT_DEF_M2),0) from RP_OF_DEF_LIN where ID_OP_LIN = a.ID_OP_LIN) as def "
				+ "from RP_OF_OP_LIN a " + "where ID_OP_CAB = " + id
				+ ") as r ,RP_OF_OP_LIN as tt where tt.ID_OP_LIN = r.ID_OP_LIN").executeUpdate();
		this.entityManager.createNativeQuery("UPDATE tt  set " + query3 + " from "
				+ "(select x.ID_REF_ETIQUETA, (select coalesce(SUM(QUANT_DEF_M2),0) from RP_OF_DEF_LIN where ID_OP_LIN = a.ID_OP_LIN and ID_REF_ETIQUETA = x.ID_REF_ETIQUETA ) as def "
				+ "from RP_OF_OP_LIN a " + "inner join RP_OF_OP_ETIQUETA x on a.ID_OP_LIN = x.ID_OP_LIN "
				+ "where ID_OP_CAB in (Select b.ID_OP_CAB from RP_OF_OP_CAB b "
				+ "inner join RP_OF_CAB c on b.ID_OF_CAB = c.ID_OF_CAB where c.ID_OF_CAB_ORIGEM in  (select ID_OF_CAB from RP_OF_OP_CAB where ID_OP_CAB = "
				+ id + ") )) as r ,RP_OF_OP_ETIQUETA as tt where tt.ID_REF_ETIQUETA = r.ID_REF_ETIQUETA")
				.executeUpdate();
	}

	public void atualiza_editar(Integer id, String url) {
		Query query = this.entityManager.createNativeQuery(
				"select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM,OP_NUM from RP_OF_CAB where ID_OF_CAB = " + id
						+ " or ID_OF_CAB_ORIGEM = " + id);
		List<Object[]> dados = query.getResultList();
		Iterator var6 = dados.iterator();

		while (true) {
			while (var6.hasNext()) {
				Object[] content = (Object[]) var6.next();
				Integer id_origem;
				Query query2;
				List dados2;
				Object[] content2;
				Iterator var11;
				if (content[1] == null) {
					id_origem = Integer.parseInt(content[0].toString());
					query2 = this.entityManager.createNativeQuery(
							"select ID_OP_LIN,REF_NUM from RP_OF_OP_LIN where ID_OP_CAB in (select xx.ID_OP_CAB from RP_OF_OP_CAB xx where xx.ID_OF_CAB = "
									+ id_origem + ")");
					dados2 = query2.getResultList();
					var11 = dados2.iterator();

					while (var11.hasNext()) {
						content2 = (Object[]) var11.next();
						this.atualiza(id_origem, "PF", (Integer) null, url);
					}
				} else {
					id_origem = Integer.parseInt(content[1].toString());
					query2 = this.entityManager.createNativeQuery(
							"select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA,c.OP_NUM,b.ID_OP_LIN  from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where a.ID_OF_CAB = "
									+ Integer.parseInt(content[0].toString()));
					dados2 = query2.getResultList();
					var11 = dados2.iterator();

					while (var11.hasNext()) {
						content2 = (Object[]) var11.next();
						Integer etiqueta = Integer.parseInt(content2[2].toString());
						this.atualiza(etiqueta, "C", (Integer) null, url);
					}
				}
			}

			return;
		}
	}

	public String atualiza(Integer id, String tipo, Integer estado, String url) {
		String estad = "";
		if (estado != null) {
			estad = "and ESTADO = 0";
		}

		Query query = this.entityManager.createNativeQuery(
				"select RESCOD,DATDEB,PROREF,OFNUM,OPECOD,ID,HEUDEB from RP_AUX_OPNUM where ID_CAMPO = " + id
						+ " and TIPO = '" + tipo + "' " + estad);
		List<Object[]> dados = query.getResultList();
		ConnectProgress connectionProgress = new ConnectProgress();
		String op_num = null;
		Iterator var11 = dados.iterator();

		while (var11.hasNext()) {
			Object[] content = (Object[]) var11.next();

			try {
				op_num = connectionProgress.GetOP_NUM(content[0].toString(), content[1].toString(),
						content[2].toString(), content[3].toString(), content[4].toString(), url,
						content[6].toString());
			} catch (SQLException var14) {
				var14.printStackTrace();
			}

			if (op_num != null && !op_num.isEmpty()) {
				int update_RP_ETIQ;
				int var13;
				if (tipo.equals("C")) {
					update_RP_ETIQ = this.entityManager.createNativeQuery(
							"UPDATE RP_OF_OP_ETIQUETA SET OP_NUM = " + op_num + " WHERE ID_REF_ETIQUETA = " + id)
							.executeUpdate();
					var13 = this.entityManager.createNativeQuery(
							"UPDATE RP_AUX_OPNUM SET ESTADO = 1, DATA_MODIFICACAO = GETDATE() WHERE ID = " + content[5])
							.executeUpdate();
				} else {
					update_RP_ETIQ = this.entityManager
							.createNativeQuery("UPDATE RP_OF_CAB SET OP_NUM = " + op_num + " WHERE ID_OF_CAB = " + id)
							.executeUpdate();
					var13 = this.entityManager.createNativeQuery(
							"UPDATE RP_AUX_OPNUM SET ESTADO = 1 ,  DATA_MODIFICACAO = GETDATE() WHERE ID = "
									+ content[5])
							.executeUpdate();
				}
			}
		}

		return op_num;
	}

	public String getURL() {
		String url = "";
		Query query_folder = this.entityManager.createNativeQuery("select top 1 * from GER_PARAMETROS a");
		List<Object[]> dados_folder = query_folder.getResultList();

		Object[] content;
		for (Iterator var5 = dados_folder.iterator(); var5.hasNext(); url = content[2].toString()) {
			content = (Object[]) var5.next();
		}

		return url;
	}

	@POST
	@Path("/getANALISERAPIDA")
	@Consumes({"**"})
	@Produces({"application/json"})
	public EMAIL sendemail(EMAIL data) {
		SendEmail send = new SendEmail();
		send.enviarEmail(data.getDE(), data.getPARA(), data.getASSUNTO(), data.getMENSAGEM(), data.getNOME_FICHEIRO());
		return data;
	}

	@POST @Path("/verficaEventos")@Consumes({"* and PROREF = '"+PROREF+"' and OFNUM = '"+OFNUM+"' and OPECOD = "+OPECOD+" and ID_CAMPO = "+ID_OF_CAB+" and TIPO = '"+TIPO+"' )"+"BEGIN INSERT INTO RP_AUX_OPNUM (RESCOD,DATDEB,PROREF,OFNUM,OPECOD,DATA_CRIACAO,DATA_MODIFICACAO,ID_CAMPO,ESTADO,TIPO,HEUDEB) VALUES ("+" '"+RESCOD+"','"+DATDEB+"','"+PROREF+"','"+OFNUM+"','"+OPECOD+"',GETDATE(),GETDATE(),"+ID_OF_CAB+",0,'"+TIPO+"','"+HEUDEB+"') "+"END END").executeUpdate();
	}

	public String dadosmensagem(String mensagem, String pagina) {
		Map<String, String> results = new HashMap();
		Query query3 = this.entityManager.createQuery("Select a from GER_EVENTO a where ID_EVENTO = :id");
		query3.setParameter("id", 3);
		List<GER_EVENTO> dados = query3.getResultList();
		Iterator var7 = dados.iterator();

		while (var7.hasNext()) {
			GER_EVENTO borderTypes = (GER_EVENTO) var7.next();
			results.put("ASSUNTO", borderTypes.getASSUNTO() == null ? "" : borderTypes.getASSUNTO());
			results.put("MENSAGEM", borderTypes.getMENSAGEM() == null ? "" : borderTypes.getMENSAGEM());
		}

		Map.Entry entry;
		for (var7 = results.entrySet().iterator(); var7.hasNext(); mensagem = mensagem
				.replace("{" + (String) entry.getKey() + "}", (CharSequence) entry.getValue())) {
			entry = (Map.Entry) var7.next();
		}

		return mensagem;
	}

	private byte[] zipFiles(File directory, String[] files) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte[] bytes = new byte[2048];
		zos.flush();
		baos.flush();
		zos.close();
		baos.close();
		return baos.toByteArray();
	}

	public void verficaEventos(String[] keyValuePairs, String momento, String fgilepath) {
		new ArrayList();
		Query query3 = this.entityManager
				.createQuery("Select a from GER_EVENTOS_CONF a where MODULO = 4 and MOMENTO = '" + momento + "' "
						+ "and PAGINA = 'INTERNO' and ESTADO  != 0");
		List<GER_EVENTOS_CONF> dados = query3.getResultList();
		Iterator var8 = dados.iterator();

		while (var8.hasNext()) {
			GER_EVENTOS_CONF borderTypes = (GER_EVENTOS_CONF) var8.next();
			EMAIL email = new EMAIL();
			email.setDE("alertas.it.doureca@gmail.com");
			email.setPARA(borderTypes.getEMAIL_PARA());
			String mensagem = borderTypes.getEMAIL_MENSAGEM();
			String assunto = borderTypes.getEMAIL_ASSUNTO();
			String[] var15 = keyValuePairs;
			int var14 = keyValuePairs.length;

			for (int var13 = 0; var13 < var14; ++var13) {
				String pair = var15[var13];
				String[] entry = pair.split("::");
				mensagem = mensagem.replace("{" + entry[0].trim() + "}", entry[1].trim());
				assunto = assunto.replace("{" + entry[0].trim() + "}", entry.length > 1 ? entry[1].trim() : "");
			}

			email.setASSUNTO(assunto);
			email.setMENSAGEM(mensagem);
			this.sendemail(email);
		}

	}
}

/*
	DECOMPILATION REPORT

	Decompiled from: C:\Users\it2\eclipse-workspace\app\src\main\ImportedClasses/pt/example/rest/SIIP.class
	Total time: 4374 ms
	"})
	@Produces({"application/json"})
	public RP_CONF_UTZ_PERF insertRP_CONF_UTZ_PERF(RP_CONF_UTZ_PERF data) {
	return (RP_CONF_UTZ_PERF)this.dao1.create(data);
	}
	@GET
	@Path("/getRP_CONF_UTZ_PERF")
	@Produces({"application/json"})
	public List<RP_CONF_UTZ_PERF> getRP_CONF_UTZ_PERF() {
	return this.dao1.allEntries();
	}
	@GET
	@Path("/getRP_CONF_UTZ_PERFid/{id}")
	@Produces({"application/json"})
	public List<RP_CONF_UTZ_PERF> getRP_CONF_UTZ_PERF_id(@PathParam("id") String id) {
	return this.dao1.getbyid(id);
	}
	@DELETE
	@Path("/deleteRP_CONF_UTZ_PERF/{id}")
	public void deleteRP_CONF_UTZ_PERF(@PathParam("id") Integer id) {
	RP_CONF_UTZ_PERF RP_CONF_UTZ_PERF = new RP_CONF_UTZ_PERF();
	RP_CONF_UTZ_PERF.setID_CONF_UTZ_PERF(id);
	this.dao1.delete(RP_CONF_UTZ_PERF);
	}
	@PUT
	@Path("/updateRP_CONF_UTZ_PERF")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_CONF_CHEF_SEC insertRP_CONF_CHEF_SECF(RP_CONF_CHEF_SEC data) {
	return (RP_CONF_CHEF_SEC)this.dao2.create(data);
	}
	@GET
	@Path("/getRP_CONF_CHEF_SEC")
	@Produces({"application/json"})
	public List<RP_CONF_CHEF_SEC> getRP_CONF_CHEF_SEC() {
	return this.dao2.allEntries();
	}
	@GET
	@Path("/getRP_CONF_CHEF_SECbyidUSER/{id}")
	@Produces({"application/json"})
	public List<RP_CONF_CHEF_SEC> getRP_CONF_CHEF_SECbyID(@PathParam("id") String id) {
	return this.dao2.getallbyid(id);
	}
	@DELETE
	@Path("/deleteRP_CONF_CHEF_SEC/{id}")
	public void deleteRP_CONF_CHEF_SEC(@PathParam("id") Integer id) {
	RP_CONF_CHEF_SEC RP_CONF_CHEF_SEC = new RP_CONF_CHEF_SEC();
	RP_CONF_CHEF_SEC.setID_CONF_CHEF_SEC(id);
	this.dao2.delete(RP_CONF_CHEF_SEC);
	}
	@PUT
	@Path("/updateRP_CONF_CHEF_SEC")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_CONF_OP insertRP_CONF_OP(RP_CONF_OP data) {
	return (RP_CONF_OP)this.dao3.create(data);
	}
	@DELETE
	@Path("/deleteRP_CONF_OP/{id}")
	public void deleteRP_CONF_OP(@PathParam("id") Integer id) {
	RP_CONF_OP RP_CONF_OP = new RP_CONF_OP();
	RP_CONF_OP.setID_CONF_OP(id);
	this.dao3.delete(RP_CONF_OP);
	}
	@PUT
	@Path("/updateRP_CONF_OP")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_CONF_OP_NPREV insertRP_CONF_OP_NPREV(RP_CONF_OP_NPREV data) {
	return (RP_CONF_OP_NPREV)this.dao4.create(data);
	}
	@DELETE
	@Path("/deleteRP_CONF_OP_NPREV/{id}")
	public void deleteRP_CONF_OP_NPREV(@PathParam("id") Integer id) {
	RP_CONF_OP_NPREV RP_CONF_OP_NPREV = new RP_CONF_OP_NPREV();
	RP_CONF_OP_NPREV.setID_CONF_OP_NPREV(id);
	this.dao4.delete(RP_CONF_OP_NPREV);
	}
	@POST
	@Path("/getRP_OF_CAB/{start}")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public List<RP_OF_CAB> listof2(String data, @PathParam("start") Integer start) {
	return this.dao.getall2(data, start);
	}
	@POST
	@Path("/getallRP_OF_CAB")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_CAB insertRP_OF_CAB(RP_OF_CAB data) {
	return (RP_OF_CAB)this.dao.create(data);
	}
	@POST
	@Path("/createupdateRP_OF_DEF_LIN")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public List<RP_OF_CAB> pesquisa_avancada(List<HashMap<String, String>> data, @PathParam("start") Integer start) throws ParseException {
	return this.dao.pesquisa_avancada(data, start);
	}
	@PUT
	@Path("/updateRP_OF_CAB")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public HashMap<String, String> insertupdate_estados(List<HashMap<String, String>> data) {
	return this.dao.updateEstados(data);
	}
	@POST
	@Path("/createRP_OF_OP_CAB")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public List<RP_OF_OP_FUNC> getdataof(@PathParam("id") Integer id, @PathParam("user") String user, ArrayList<String> estado) {
	return this.dao11.getbyid(id, user, estado);
	}
	@GET
	@Path("/getRP_OF_OP_CABid/{id}/{id2}")
	@Produces({"application/json"})
	public List<RP_OF_OP_CAB> getdataof(@PathParam("id") Integer id, @PathParam("id2") Integer id2) {
	return this.dao6.getid(id, id2);
	}
	@GET
	@Path("/getMaxID")
	@Produces({"application/json"})
	public List<Integer> getMaxID() {
	return this.dao6.getMaxID();
	}
	@PUT
	@Path("/updateRP_OF_OP_CAB")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_OP_LIN insertRP_OF_OP_LIN(RP_OF_OP_LIN data) {
	return (RP_OF_OP_LIN)this.dao7.create(data);
	}
	@GET
	@Path("/getRP_OF_OP_LINid/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_LIN> getbyid(@PathParam("id") Integer id) {
	return this.dao7.getbyid(id);
	}
	@GET
	@Path("/getRP_OF_OP_LINidcontrolo/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_LIN> getbyidontrolo(@PathParam("id") Integer id) {
	return this.dao7.getbyidcontrolo(id);
	}
	@GET
	@Path("/getRP_OF_OP_LIN/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_LIN> getid(@PathParam("id") Integer id) {
	return this.dao7.getid(id);
	}
	@GET
	@Path("/getRP_OF_OP_LINallid/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_LIN> getallbyid(@PathParam("id") Integer id) {
	return this.dao7.getallbyid(id);
	}
	@GET
	@Path("/getRP_OF_OP_LINOp/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_LIN> getop(@PathParam("id") Integer id) {
	return this.dao7.getop(id);
	}
	@PUT
	@Path("/updateRP_OF_OP_LIN")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_PREP_LIN insertRP_OF_PREP_LIN(RP_OF_PREP_LIN data) {
	return (RP_OF_PREP_LIN)this.dao8.create(data);
	}
	@PUT
	@Path("/updateRP_OF_PREP_LIN")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_CONF_FAMILIA_COMP insertRP_CONF_FAMILIA_COMP(RP_CONF_FAMILIA_COMP data) {
	return (RP_CONF_FAMILIA_COMP)this.dao10.create(data);
	}
	@PUT
	@Path("/updateRP_CONF_FAMILIA_COMP")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_OP_FUNC insertRP_OF_OP_FUNC(RP_OF_OP_FUNC data) {
	return (RP_OF_OP_FUNC)this.dao11.create(data);
	}
	@PUT
	@Path("/updateRP_OF_OP_FUNC")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_PARA_LIN insertRP_OF_PARA_LIN(RP_OF_PARA_LIN data) {
	return (RP_OF_PARA_LIN)this.dao9.create(data);
	}
	@PUT
	@Path("/updateRP_OF_PARA_LIN")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_DEF_LIN insertRP_OF_DEF_LIN(RP_OF_DEF_LIN data) {
	return (RP_OF_DEF_LIN)this.dao5.create(data);
	}
	@GET
	@Path("/getbyidRP_OF_DEF_LIN/{id}/{id2}/{id_ref}")
	@Produces({"application/json"})
	public List<RP_OF_DEF_LIN> getbyRP_OF_DEF_LIN(@PathParam("id") String id, @PathParam("id2") Integer id2, @PathParam("id_ref") String id_ref) {
	return this.dao5.getbyid(id, id2, id_ref);
	}
	@GET
	@Path("/getbyidRP_OF_DEF_LINall/{id2}/{id_ref}")
	@Produces({"application/json"})
	public List<RP_OF_DEF_LIN> getbyRP_OF_DEF_LINall(@PathParam("id2") Integer id2, @PathParam("id_ref") String id_ref) {
	return this.dao5.getbyidall(id2, id_ref);
	}
	@GET
	@Path("/getbyidRP_OF_DEF_LINidoplin/{id}")
	@Produces({"application/json"})
	public List<RP_OF_DEF_LIN> getbyid_op_lin(@PathParam("id") Integer id) {
	return this.dao5.getbyid_op_lin(id);
	}
	@GET
	@Path("/getbyidRP_OF_DEF_LINidoplin_etiq/{id}/{id_etiq}")
	@Produces({"application/json"})
	public List<RP_OF_DEF_LIN> getbyid_op_lin_id_etiq(@PathParam("id") Integer id, @PathParam("id_etiq") Integer id_etiq) {
	return this.dao5.getbyid_op_lin_id_etiqueta(id, id_etiq);
	}
	@GET
	@Path("/getbyidDEF/{id}")
	@Produces({"application/json"})
	public List<RP_OF_DEF_LIN> getbyidDEF(@PathParam("id") Integer id) {
	return this.dao5.getbyidDEF(id);
	}
	@PUT
	@Path("/updateRP_OF_DEF_LIN")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_OUTRODEF_LIN insertRP_OF_OUTRODEF_LIN(RP_OF_OUTRODEF_LIN data) {
	return (RP_OF_OUTRODEF_LIN)this.dao12.create(data);
	}
	@GET
	@Path("/getbyidRP_OF_OUTRODEF_LINF/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OUTRODEF_LIN> getbyidRP_OF_OUTRODEF_LIN(@PathParam("id") Integer id) {
	return this.dao12.getbyid(id);
	}
	@GET
	@Path("/getbyidRP_OF_OUTRODEF_LINFetiqueta/{id}/{etiqueta}")
	@Produces({"application/json"})
	public List<RP_OF_OUTRODEF_LIN> getbyidRP_OF_OUTRODEF_LINetiqueta(@PathParam("id") Integer id, @PathParam("etiqueta") Integer etiqueta) {
	return this.dao12.getbyidetiqueta(id, etiqueta);
	}
	@PUT
	@Path("/updateRP_OF_OUTRODEF_LIN")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_OP_ETIQUETA insertRP_OF_OP_ETIQUETA(RP_OF_OP_ETIQUETA data) {
	return (RP_OF_OP_ETIQUETA)this.dao13.create(data);
	}
	@GET
	@Path("/getRP_OF_OP_ETIQUETA")
	@Produces({"application/json"})
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETA() {
	return this.dao13.allEntries();
	}
	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETA_id(@PathParam("id") Integer id) {
	return this.dao13.getbyid(id);
	}
	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid/{id}/{id2}")
	@Produces({"application/json"})
	public List<RP_OF_OP_ETIQUETA> getbyid_etiqueta(@PathParam("id") Integer ID_OP_LIN, @PathParam("id2") Integer ID_REF_ETIQUETA) {
	return this.dao13.getbyid_etiqueta(ID_REF_ETIQUETA, ID_OP_LIN);
	}
	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid_op_lin/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETAbyid_op_lin(@PathParam("id") Integer id) {
	return this.dao13.getbyid_oplin(id);
	}
	@GET
	@Path("/getRP_OF_OP_ETIQUETAbyid_op_lindef/{id}")
	@Produces({"application/json"})
	public List<RP_OF_OP_ETIQUETA> getRP_OF_OP_ETIQUETAbyid_op_lindef(@PathParam("id") Integer id) {
	return this.dao13.getbyid_oplindef(id);
	}
	@DELETE
	@Path("/deleteRP_OF_OP_ETIQUETA/{id}")
	public void deleteRP_OF_OP_ETIQUETA(@PathParam("id") Integer id) {
	RP_OF_OP_ETIQUETA RP_OF_OP_ETIQUETA = new RP_OF_OP_ETIQUETA();
	RP_OF_OP_ETIQUETA.setID_REF_ETIQUETA(id);
	this.dao13.delete(RP_OF_OP_ETIQUETA);
	}
	@GET
	@Path("/apagarRP_OF_OP_ETIQUETA/{id}")
	public void apagarRP_OF_OP_ETIQUETA(@PathParam("id") Integer id) {
	int update_RP_ETIQ = this.entityManager.createNativeQuery("UPDATE RP_OF_OP_ETIQUETA SET ATIVO = 0,APAGADO = 1 WHERE ID_REF_ETIQUETA = " + id).executeUpdate();
	}
	@PUT
	@Path("/updateRP_OF_OP_ETIQUETA")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public RP_OF_LST_DEF insertRP_OF_LST_DEF(RP_OF_LST_DEF data) {
	return (RP_OF_LST_DEF)this.dao14.create(data);
	}
	@GET
	@Path("/getRP_OF_LST_DEF")
	@Produces({"application/json"})
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEF() {
	return this.dao14.allEntries();
	}
	@GET
	@Path("/getRP_OF_LST_DEFid/{id}")
	@Produces({"application/json"})
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEF_id(@PathParam("id") Integer id) {
	return this.dao14.getbyid(id);
	}
	@GET
	@Path("/getRP_OF_LST_DEFgetallfam")
	@Produces({"application/json"})
	public List<RP_OF_LST_DEF> getRP_OF_LST_DEFgetallfam() {
	return this.dao14.getallfam();
	}
	@POST
	@Path("/getRP_OF_LST_DEFgetfam")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public GER_EVENTO insertGER_EVENTO(GER_EVENTO data) {
	return (GER_EVENTO)this.dao15.create(data);
	}
	@GET
	@Path("/getGER_EVENTO")
	@Produces({"application/json"})
	public List<GER_EVENTO> getGER_EVENTO() {
	return this.dao15.getall();
	}
	@GET
	@Path("/getGER_EVENTObyidOrigem/{id}/{campo}")
	@Produces({"application/json"})
	public List<GER_EVENTO> getGER_EVENTO_idOrigem(@PathParam("id") Integer id, @PathParam("campo") String campo) {
	return this.dao15.getbyidOrigem(id, campo);
	}
	@GET
	@Path("/getGER_EVENTObyid/{id}")
	@Produces({"application/json"})
	public List<GER_EVENTO> getGER_EVENTO_id(@PathParam("id") Integer id) {
	return this.dao15.getbyid(id);
	}
	@DELETE
	@Path("/deleteGER_EVENTO/{id}")
	public void deleteGER_EVENTO(@PathParam("id") Integer id) {
	GER_EVENTO GER_EVENTO = new GER_EVENTO();
	GER_EVENTO.setID_EVENTO(id);
	this.dao15.delete(GER_EVENTO);
	}
	@PUT
	@Path("/updateGER_EVENTO")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public List<HashMap<String, String>> allopNOTIN(List<HashMap<String, String>> data) {
	HashMap<String, String> firstMap = (HashMap)data.get(0);
	String OF_NUM = (String)firstMap.get("OF_NUM");
	String MAQ_NUM = (String)firstMap.get("MAQ_NUM");
	String ID_UTZ_CRIA = (String)firstMap.get("ID_UTZ_CRIA");
	String OP_NUM = (String)firstMap.get("OP_NUM");
	List<HashMap<String, String>> list = new ArrayList();
	Query query = this.entityManager.createNativeQuery("select d.ID_UTZ_CRIA,AVG(CASE WHEN DATEPART(HOUR, CONVERT( TIME,b.TEMPO_EXEC_TOTAL_M2)) = 0 THEN 0 ELSE c.QUANT_BOAS_TOTAL_M2/DATEPART(HOUR, CONVERT( TIME,b.TEMPO_EXEC_TOTAL_M2)) END) as TimeStampHour from RP_OF_CAB a inner join RP_OF_OP_CAB b on a.ID_OF_CAB = b.ID_OF_CAB inner join RP_OF_OP_LIN c on b.ID_OP_CAB = c.ID_OP_CAB inner join RP_OF_OP_FUNC d on b.ID_OP_CAB = d.ID_OP_CAB where a.ID_OF_CAB_ORIGEM is null and a.OP_COD_ORIGEM = 85  and a.MAQ_NUM = '032'and a.OF_NUM = 1806322495 and d.DATA_INI_M2 >= DATEADD(MONTH, -3, GETDATE()) GROUP BY d.ID_UTZ_CRIA");
	List<Object[]> dados = query.getResultList();
	Iterator var11 = dados.iterator();
	while(var11.hasNext()) {
	Object[] content = (Object[])var11.next();
	HashMap<String, String> x = new HashMap();
	x.put("ID_UTZ_CRIA", content[0].toString());
	x.put("MEDIA", content[1].toString());
	list.add(x);
	}
	return list;
	}
	@POST
	@Path("/ficheiroManual/{todos}")
	@Produces({"application/zip"})
	public Response getFicheiroManual(List<String> dados_of, @PathParam("todos") Boolean todos) throws IOException, ParseException {
	String data = (new SimpleDateFormat("yyyyMMddHHmmss_")).format(new Date());
	String data_file = (new SimpleDateFormat("yyyyMMddHHmmss_")).format(new Date());
	String url = this.getURL();
	String estado = "C";
	Boolean ficheirosdownload = true;
	Boolean pausa = true;
	Integer comp_num = 1;
	String nome_ficheiro = "";
	Iterator var12 = dados_of.iterator();
	label145:
	while(true) {
	String content_of;
	Object op_num;
	do {
	if (!var12.hasNext()) {
	if (ficheirosdownload) {
	final File file = new File("c:/sgiid/temp_files/" + data_file + ".zip");
	Response.ResponseBuilder response = Response.ok(file);
	response.header("Content-Disposition", "attachment; filename=ficheiros.zip");
	Timer timer = new Timer();
	timer.schedule(new TimerTask() {
	public void run() {
	file.delete();
	}
	}, 5000L);
	return response.build();
	}
	return null;
	}
	content_of = (String)var12.next();
	new ConnectProgress();
	op_num = null;
	} while(op_num != null && !((String)op_num).isEmpty());
	Query query = this.entityManager.createNativeQuery("select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM,OP_NUM from RP_OF_CAB where ID_OF_CAB = " + content_of + " or ID_OF_CAB_ORIGEM = " + content_of);
	List<Object[]> dados = query.getResultList();
	Iterator var18 = dados.iterator();
	while(true) {
	while(true) {
	if (!var18.hasNext()) {
	continue label145;
	}
	Object[] content = (Object[])var18.next();
	data = (new SimpleDateFormat("yyyyMMddHHmmss_")).format(new Date());
	String inform_file = "";
	Integer total = 1;
	Integer id_origem;
	if (content[1] == null && todos) {
	id_origem = Integer.parseInt(content[0].toString());
	Query query3 = this.entityManager.createNativeQuery("select ID_OP_LIN,REF_NUM from RP_OF_OP_LIN where ID_OP_CAB in (select xx.ID_OP_CAB from RP_OF_OP_CAB xx where xx.ID_OF_CAB = " + id_origem + ")");
	List<Object[]> dados3 = query3.getResultList();
	total = dados3.size();
	Iterator var40 = dados3.iterator();
	while(var40.hasNext()) {
	Object[] content3 = (Object[])var40.next();
	inform_file = content[2].toString() + "_" + content3[1].toString() + "_PF";
	String inform_file2 = "";
	String OPNUM = content[4] == null ? "NULL" : content[4].toString();
	if (estado.equals("M")) {
	OPNUM = this.atualiza(id_origem, "PF", 0, url);
	nome_ficheiro = "correcao" + data + inform_file + ".txt";
	inform_file2 = content[2].toString() + "_correcao_PAUSA";
	if (OPNUM == null) {
	OPNUM = content[4] == null ? "NULL" : content[4].toString();
	}
	this.criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, "P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload, data, (String)null, estado, true);
	this.criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, estado, data + inform_file2, OPNUM, content3[0].toString(), false, total, ficheirosdownload, data_file, (String)null, estado, true);
	}
	inform_file2 = content[2].toString() + "_PAUSA";
	nome_ficheiro = data + inform_file + ".txt";
	if (estado.equals("A")) {
	nome_ficheiro = "anulacao_" + nome_ficheiro;
	}
	this.criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, "P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload, data_file, (String)null, estado, true);
	pausa = false;
	this.criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, estado, data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload, data_file, (String)null, estado, true);
	}
	} else {
	id_origem = Integer.parseInt(content[1].toString());
	String data_query = "";
	if (estado.equals("M")) {
	data_query = " and  (c.VERSAO_MODIF != (select VERSAO_MODIF from RP_OF_CAB where ID_OF_CAB = " + id_origem + ") or (c.QUANT_BOAS_M1 != c.QUANT_BOAS_M2 or c.QUANT_DEF_M1 != c.QUANT_DEF_M2 or c.NOVO = 1 or c.APAGADO = 1)) ";
	}
	Query query2 = this.entityManager.createNativeQuery("select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA,c.OP_NUM,b.ID_OP_LIN,c.NOVO,c.ATIVO,c.APAGADO  from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where a.ID_OF_CAB = " + Integer.parseInt(content[0].toString()) + data_query);
	List<Object[]> dados2 = query2.getResultList();
	Integer etiqueta_num = 1;
	Iterator var27 = dados2.iterator();
	while(var27.hasNext()) {
	Object[] content2 = (Object[])var27.next();
	inform_file = content[2].toString() + "_C" + comp_num + "E" + etiqueta_num;
	etiqueta_num = etiqueta_num + 1;
	Integer etiqueta = Integer.parseInt(content2[2].toString());
	Integer id_of_cab = Integer.parseInt(content2[1].toString());
	String OPNUM = content2[3] == null ? "NULL" : content2[3].toString();
	String novaet = content2[5] != null ? content2[5].toString() : "0";
	String ativo = content2[6] != null ? content2[6].toString() : "0";
	String apagado = content2[7] != null ? content2[7].toString() : "0";
	if (novaet.equals("true")) {
	novaet = "1";
	}
	Boolean manual = false;
	if (todos) {
	manual = true;
	}
	if (estado.equals("M") && !novaet.equals("1") && (!ativo.equals("true") && apagado.equals("true") || ativo.equals("true") && !apagado.equals("true"))) {
	OPNUM = this.atualiza(etiqueta, "C", 0, url);
	nome_ficheiro = "correcao" + data + inform_file + ".txt";
	if (OPNUM == null) {
	OPNUM = content2[3] == null ? "NULL" : content2[3].toString();
	}
	this.criarFicheiro(id_of_cab, 1, nome_ficheiro, "COMP", content2[0].toString(), id_origem, etiqueta, estado, (String)null, OPNUM, content2[4].toString(), false, 1, ficheirosdownload, data_file, novaet, estado, manual);
	}
	nome_ficheiro = data + inform_file + ".txt";
	if (estado.equals("A")) {
	nome_ficheiro = "anulacao_" + nome_ficheiro;
	}
	if (ativo.equals("true")) {
	this.criarFicheiro(id_of_cab, 2, nome_ficheiro, "COMP", content2[0].toString(), id_origem, etiqueta, estado, (String)null, OPNUM, content2[4].toString(), false, 1, ficheirosdownload, data_file, novaet, estado, manual);
	}
	}
	if (dados2.size() > 0) {
	comp_num = comp_num + 1;
	}
	}
	}
	}
	}
	}
	@POST
	@Path("/getOFS")
	@Produces({"application/json"})
	public List<HashMap<String, String>> getOFS(List<HashMap<String, String>> datas) throws IOException, ParseException {
	HashMap<String, String> firstMap = (HashMap)datas.get(0);
	String datainicio = (String)firstMap.get("datainicio");
	String datafim = (String)firstMap.get("datafim");
	String url = this.getURL();
	List<HashMap<String, String>> list = new ArrayList();
	try {
	Query query_of = this.entityManager.createNativeQuery("select a.ID_OF_CAB, a.OF_NUM,c.ID_UTZ_CRIA,a.OP_COD_ORIGEM,c.DATA_INI_M2,c.HORA_INI_M2,c.NOME_UTZ_CRIA,c.DATA_FIM_M2,c.HORA_FIM_M2,a.OP_NUM ,d.QUANT_BOAS_TOTAL_M2,d.QUANT_DEF_TOTAL_M2,d.REF_NUM,d.REF_DES from RP_OF_CAB a inner join RP_OF_OP_CAB b on  b.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB =  a.ID_OF_CAB) inner join RP_OF_OP_FUNC c on c.ID_OP_CAB in  (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = a.ID_OF_CAB) and b.ID_OP_CAB = c.ID_OP_CAB inner join RP_OF_OP_LIN d on d.ID_OP_CAB = b.ID_OP_CAB where a.ID_UTZ_CRIA = c.ID_UTZ_CRIA and  CAST((cast(DATA_FIM_M2 as datetime) + cast(HORA_FIM_M2 as datetime)) AS datetime) >= '" + datainicio + "' and CAST((cast(DATA_FIM_M2 as datetime) + cast(HORA_FIM_M2 as datetime)) AS datetime) <= '" + datafim + "' and ID_OF_CAB_ORIGEM is null AND DATA_FIM_M2 is not null ");
	List<Object[]> dados_of = query_of.getResultList();
	Boolean pausa = true;
	Iterator var11 = dados_of.iterator();
	while(true) {
	Object[] content_of;
	String op_num;
	do {
	if (!var11.hasNext()) {
	return list;
	}
	content_of = (Object[])var11.next();
	ConnectProgress connectionProgress = new ConnectProgress();
	op_num = null;
	op_num = connectionProgress.verificaOF(content_of[2].toString(), content_of[4].toString(), content_of[1].toString(), content_of[3].toString(), url, content_of[5].toString(), content_of[10].toString(), content_of[11].toString(), content_of[12].toString());
	} while(op_num != null && !op_num.isEmpty());
	HashMap<String, String> x = new HashMap();
	x.put("RESCOD", content_of[2].toString());
	x.put("DATDEB", content_of[4].toString());
	x.put("OFNUM", content_of[1].toString());
	x.put("OPECOD", content_of[3].toString());
	x.put("HEUDEB", content_of[5].toString());
	x.put("NOME", content_of[6].toString());
	x.put("ID", content_of[0].toString());
	x.put("DATAFIM", content_of[7].toString());
	x.put("HORAFIM", content_of[8].toString());
	x.put("OP_NUM", content_of[9] != null ? content_of[9].toString() : "--");
	x.put("BOAS", content_of[10].toString());
	x.put("DEFEITOS", content_of[11].toString());
	x.put("REF", content_of[12].toString());
	list.add(x);
	}
	} catch (Exception var15) {
	var15.printStackTrace();
	return list;
	}
	}
	@POST
	@Path("/getOFSCOMPONENTES")
	@Produces({"application/json"})
	public List<HashMap<String, String>> getOFSCOMPONENTES(List<HashMap<String, String>> datas) throws IOException, ParseException {
	HashMap<String, String> firstMap = (HashMap)datas.get(0);
	String datainicio = (String)firstMap.get("datainicio");
	String datafim = (String)firstMap.get("datafim");
	String url = this.getURL();
	List<HashMap<String, String>> list = new ArrayList();
	try {
	Query query_of = this.entityManager.createNativeQuery("select e.ID_OF_CAB,(select x.OF_NUM from RP_OF_CAB x where x.ID_OF_CAB =  e.ID_OF_CAB_ORIGEM) as OF_NUM ,g.ID_UTZ_CRIA,a.OPECOD,g.DATA_INI_M2,g.HORA_INI_M2,g.NOME_UTZ_CRIA,g.DATA_FIM_M2,g.HORA_FIM_M2,b.REF_ETIQUETA,c.REF_NUM,c.REF_DES,a.OFNUM from RP_AUX_OPNUM  a inner join RP_OF_OP_ETIQUETA b on a.ID_CAMPO = b.ID_REF_ETIQUETA inner join RP_OF_OP_LIN c on b.ID_OP_LIN = c.ID_OP_LIN inner join RP_OF_OP_CAB d on c.ID_OP_CAB = d.ID_OP_CAB inner join RP_OF_CAB e on d.ID_OF_CAB = e.ID_OF_CAB inner join RP_OF_OP_CAB f on  f.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB =  e.ID_OF_CAB_ORIGEM) inner join RP_OF_OP_FUNC g on g.ID_OP_CAB in  (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = e.ID_OF_CAB_ORIGEM) and f.ID_OP_CAB = g.ID_OP_CAB where CAST((cast(DATDEB as datetime) + cast(HEUDEB as datetime)) AS datetime)  >= '" + datainicio + "' " + "AND CAST((cast(DATDEB as datetime) + cast(HEUDEB as datetime)) AS datetime)  <='" + datafim + "' AND a.ESTADO !=1");
	List<Object[]> dados_of = query_of.getResultList();
	Boolean pausa = true;
	Iterator var11 = dados_of.iterator();
	while(true) {
	Object[] content_of;
	String op_num;
	do {
	if (!var11.hasNext()) {
	return list;
	}
	content_of = (Object[])var11.next();
	ConnectProgress connectionProgress = new ConnectProgress();
	op_num = null;
	op_num = connectionProgress.GetOP_NUM(content_of[2].toString(), content_of[4].toString(), content_of[10].toString(), content_of[12].toString(), content_of[3].toString(), url, content_of[5].toString());
	} while(op_num != null && !op_num.isEmpty());
	HashMap<String, String> x = new HashMap();
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
	} catch (Exception var15) {
	var15.printStackTrace();
	return list;
	}
	}
	@GET
	@Path("/ficheiro/{id}/{estado}/{ficheiros}/{manual}")
	@Produces({"application/zip"})
	public Response getFicheiro(@PathParam("id") Integer id, @PathParam("estado") String estado, @PathParam("ficheiros") Boolean ficheirosdownload, @PathParam("manual") Boolean manual) throws IOException, ParseException {
	String data = (new SimpleDateFormat("yyyyMMddHHmmss_")).format(new Date());
	String url = this.getURL();
	try {
	Thread.sleep(3000L);
	Integer comp_num = 1;
	String nome_ficheiro = "";
	Query query = this.entityManager.createNativeQuery("select ID_OF_CAB,ID_OF_CAB_ORIGEM,ID_UTZ_CRIA,OF_NUM,OP_NUM from RP_OF_CAB where ID_OF_CAB = " + id + " or ID_OF_CAB_ORIGEM = " + id);
	if (estado.equals("C") && !ficheirosdownload && !manual) {
	this.eventosAoConcluir(id);
	}
	List<Object[]> dados = query.getResultList();
	Boolean pausa = true;
	Iterator var13 = dados.iterator();
	label138:
	while(true) {
	while(true) {
	if (!var13.hasNext()) {
	break label138;
	}
	Object[] content = (Object[])var13.next();
	String inform_file = "";
	Integer total = 1;
	Integer id_origem;
	if (content[1] == null) {
	id_origem = Integer.parseInt(content[0].toString());
	Query query3 = this.entityManager.createNativeQuery("select ID_OP_LIN,REF_NUM from RP_OF_OP_LIN where ID_OP_CAB in (select xx.ID_OP_CAB from RP_OF_OP_CAB xx where xx.ID_OF_CAB = " + id_origem + ")");
	List<Object[]> dados3 = query3.getResultList();
	total = dados3.size();
	Iterator var36 = dados3.iterator();
	while(var36.hasNext()) {
	Object[] content3 = (Object[])var36.next();
	inform_file = content[2].toString() + "_" + content3[1].toString() + "_PF";
	String inform_file2 = "";
	String OPNUM = content[4] == null ? "NULL" : content[4].toString();
	if (estado.equals("M")) {
	OPNUM = this.atualiza(id_origem, "PF", 0, url);
	nome_ficheiro = "correcao" + data + inform_file + ".txt";
	inform_file2 = content[2].toString() + "_correcao_PAUSA";
	if (OPNUM == null) {
	OPNUM = content[4] == null ? "NULL" : content[4].toString();
	}
	this.criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, "P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload, data, (String)null, estado, manual);
	this.criarFicheiro(id_origem, 1, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, estado, data + inform_file2, OPNUM, content3[0].toString(), false, total, ficheirosdownload, data, (String)null, estado, manual);
	}
	inform_file2 = content[2].toString() + "_PAUSA";
	nome_ficheiro = data + inform_file + ".txt";
	if (estado.equals("A")) {
	nome_ficheiro = "anulacao_" + nome_ficheiro;
	}
	this.criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, "P", data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload, data, (String)null, estado, manual);
	pausa = false;
	this.criarFicheiro(id_origem, 2, nome_ficheiro, "PF", content[3].toString(), id_origem, (Integer)null, estado, data + inform_file2, OPNUM, content3[0].toString(), pausa, total, ficheirosdownload, data, (String)null, estado, manual);
	}
	} else {
	id_origem = Integer.parseInt(content[1].toString());
	String data_query = "";
	if (estado.equals("M")) {
	data_query = " and  (c.VERSAO_MODIF != (select VERSAO_MODIF from RP_OF_CAB where ID_OF_CAB = " + id_origem + ") or (c.QUANT_BOAS_M1 != c.QUANT_BOAS_M2 or c.QUANT_DEF_M1 != c.QUANT_DEF_M2 or c.NOVO = 1 or c.APAGADO = 1)) ";
	}
	Query query2 = this.entityManager.createNativeQuery("select OF_NUM_ORIGEM,a.ID_OF_CAB,c.ID_REF_ETIQUETA,c.OP_NUM,b.ID_OP_LIN,c.NOVO,c.ATIVO,c.APAGADO   from RP_OF_OP_CAB a inner join RP_OF_OP_LIN b on a.ID_OP_CAB = b.ID_OP_CAB inner join RP_OF_CAB d on  d.ID_OF_CAB = a.ID_OF_CAB inner join RP_OF_OP_ETIQUETA c on b.ID_OP_LIN = c.ID_OP_LIN  where a.ID_OF_CAB = " + Integer.parseInt(content[0].toString()) + data_query);
	List<Object[]> dados2 = query2.getResultList();
	Integer etiqueta_num = 1;
	Iterator var22 = dados2.iterator();
	while(var22.hasNext()) {
	Object[] content2 = (Object[])var22.next();
	inform_file = content[2].toString() + "_C" + comp_num + "E" + etiqueta_num;
	etiqueta_num = etiqueta_num + 1;
	Integer etiqueta = Integer.parseInt(content2[2].toString());
	Integer id_of_cab = Integer.parseInt(content2[1].toString());
	String OPNUM = content2[3] == null ? "NULL" : content2[3].toString();
	String novaet = content2[5] != null ? content2[5].toString() : "0";
	String ativo = content2[6] != null ? content2[6].toString() : "0";
	String apagado = content2[7] != null ? content2[7].toString() : "0";
	if (novaet.equals("true")) {
	novaet = "1";
	}
	if (estado.equals("M") && !novaet.equals("1") && (!ativo.equals("true") && apagado.equals("true") || ativo.equals("true") && !apagado.equals("true"))) {
	OPNUM = this.atualiza(etiqueta, "C", 0, url);
	nome_ficheiro = "correcao" + data + inform_file + ".txt";
	if (OPNUM == null) {
	OPNUM = content2[3] == null ? "NULL" : content2[3].toString();
	}
	this.criarFicheiro(id_of_cab, 1, nome_ficheiro, "COMP", content2[0].toString(), id_origem, etiqueta, estado, (String)null, OPNUM, content2[4].toString(), false, 1, ficheirosdownload, data, novaet, estado, manual);
	}
	nome_ficheiro = data + inform_file + ".txt";
	if (estado.equals("A")) {
	nome_ficheiro = "anulacao_" + nome_ficheiro;
	}
	if (ativo.equals("true")) {
	this.criarFicheiro(id_of_cab, 2, nome_ficheiro, "COMP", content2[0].toString(), id_origem, etiqueta, estado, (String)null, OPNUM, content2[4].toString(), false, 1, ficheirosdownload, data, novaet, estado, manual);
	}
	}
	if (dados2.size() > 0) {
	comp_num = comp_num + 1;
	}
	}
	}
	}
	} catch (Exception var29) {
	var29.printStackTrace();
	}
	if (ficheirosdownload) {
	final File file = new File("c:/sgiid/temp_files/" + data + ".zip");
	Response.ResponseBuilder response = Response.ok(file);
	response.header("Content-Disposition", "attachment; filename=ficheiros.zip");
	Timer timer = new Timer();
	timer.schedule(new TimerTask() {
	public void run() {
	file.delete();
	}
	}, 5000L);
	return response.build();
	} else {
	return null;
	}
	}
	public void eventosAoConcluir(Integer id) {
	String referencia = "";
	String descricao_referencia = "";
	String perc_obj = "";
	String perc_def = "";
	String utilizador = "";
	String of_num = "";
	String data_cria = "";
	String etiquetas = "";
	String[] keyValuePairs = null;
	Query query = this.entityManager.createNativeQuery("select PERC_OBJETIV,  cast( (CAST(NULLIF(QUANT_DEF_TOTAL_M2,0)AS float) / CAST(( QUANT_BOAS_TOTAL_M2 +  QUANT_DEF_TOTAL_M2) AS float))  100  as numeric(36,2)), REF_NUM,REF_DES,c.ID_UTZ_CRIA,c.NOME_UTZ_CRIA,(select OF_NUM from RP_OF_CAB where ID_OF_CAB = " + id + " ) as OF_NUM," + "(select top 1 (cast(DATA_INI_M2 as datetime) + cast(HORA_INI_M2 as datetime)) FROM RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB = " + id + ") ) DATA" + ",c.ID_OF_CAB_ORIGEM,a.ID_OP_LIN from RP_OF_OP_LIN  a " + "left join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB left join RP_OF_CAB c on b.ID_OF_CAB = c.ID_OF_CAB " + "where  (c.ID_OF_CAB = " + id + " or c.ID_OF_CAB_ORIGEM = " + id + ") and  ISNULL(cast( (CAST(NULLIF(QUANT_DEF_TOTAL_M2,0)AS float) / CAST(( QUANT_BOAS_TOTAL_M2 +  QUANT_DEF_TOTAL_M2)AS float))  100  as numeric(36,2)),0) > ISNULL(PERC_OBJETIV,0) " + "and a.GESCOD in ('OFCF','OFCF2') and ISNULL(PERC_OBJETIV,0) != 0");
	List<Object[]> dados = query.getResultList();
	Iterator var14 = dados.iterator();
	while(true) {
	while(var14.hasNext()) {
	Object[] content = (Object[])var14.next();
	referencia = content[2].toString();
	descricao_referencia = content[3].toString();
	perc_obj = content[0].toString() + " %";
	perc_def = content[1].toString() + " %";
	utilizador = content[4].toString() + " - " + content[5].toString();
	of_num = content[6].toString();
	data_cria = content[7].toString().substring(0, 19);
	if (content[6] == null) {
	String[] keyValuePairs1 = new String[]{"referencia::" + referencia, "descricao_referencia::" + descricao_referencia, "perc_obj::" + perc_obj, "perc_def::" + perc_def, "utilizador::" + utilizador, "of_num::" + of_num, "data_cria::" + data_cria, "etiquetas::" + etiquetas};
	this.verficaEventos(keyValuePairs1, "Ao Concluir Trabalho - Alerta Objetivos", "");
	} else {
	etiquetas = "<table  border='1'><tr><th><b>Nº Etiqueta</b></th><th><b>Lote</b></th><th><b>OF Origem</b></th><th><b>Data OF</b></th></tr>";
	Query query_comp = this.entityManager.createNativeQuery("select REF_LOTE,REF_ETIQUETA,OF_NUM_ORIGEM,OFDATFR from RP_OF_OP_ETIQUETA where ID_OP_LIN =" + content[9]);
	List<Object[]> dados_comp = query_comp.getResultList();
	Object[] cont;
	for(Iterator var18 = dados_comp.iterator(); var18.hasNext(); etiquetas = etiquetas + "<tr><td style='padding: 0px 5px 0px 5px;'>" + cont[1] + "</td>" + "<td style='padding: 0px 5px 0px 5px;'>" + cont[0] + "</td>" + "<td style='padding: 0px 5px 0px 5px;'>" + cont[2] + "</td>" + "<td style='padding: 0px 5px 0px 5px;'>" + cont[3] + "</td></tr>") {
	cont = (Object[])var18.next();
	}
	etiquetas = etiquetas + "</table>";
	String[] keyValuePairs2 = new String[]{"referencia::" + referencia, "descricao_referencia::" + descricao_referencia, "perc_obj::" + perc_obj, "perc_def::" + perc_def, "utilizador::" + utilizador, "of_num::" + of_num, "data_cria::" + data_cria, "etiquetas::" + etiquetas};
	this.verficaEventos(keyValuePairs2, "Ao Concluir Trabalho - Alerta Objetivos", "");
	}
	}
	return;
	}
	}
	public void criarFicheiro(Integer id, Integer ficheiro, String nome_ficheiro, String tipo, String of, Integer id_origem, Integer id_etiqueta, String estado, String nome_ficheiro2, String OP_NUM, String ID_OP_LIN, Boolean cria_pausa, Integer total, Boolean ficheirosdownload, String nomezip, String novaetiqueta, String estado2, Boolean manual) throws IOException, ParseException {
	String TEMPO_EXEC_TOTAL = "";
	if (novaetiqueta == null) {
	novaetiqueta = "0";
	}
	if (estado.equals("A") || estado2.equals("A")) {
	nome_ficheiro2 = "anulacao_" + nome_ficheiro2;
	}
	Boolean alteracoes = false;
	String DATA_INI;
	String HORA_INI;
	String DATA_FIM;
	String HORA_FIM;
	String SINAL;
	String QUANT_BOAS_TOTAL;
	String QUANT_BOAS;
	String QUANT_DEF;
	String TEMPO_PREP_TOTAL;
	String TIPO_PARAGEM;
	String MOMENTO_PARAGEM;
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
	} else if (manual) {
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
	if (estado.equals("A") || estado2.equals("A")) {
	SINAL = "-";
	}
	BufferedWriter bw = null;
	SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
	formate.format(new Date());
	FileWriter fw = null;
	String sequencia = "000000000";
	String path = "";
	String path2 = "";
	String data = "";
	String data_maquina = "";
	boolean existe_maquina = false;
	boolean lider = true;
	boolean atualiza = true;
	boolean primeira_linha = true;
	String data_inicio = "";
	HashMap<String, String> linha_utz = new HashMap();
	HashMap<String, String> linha_utz_inicio = new HashMap();
	Query query_folder = this.entityManager.createNativeQuery("select top 1  from GER_PARAMETROS a");
	List<Object[]> dados_folder = query_folder.getResultList();
	Object[] content;
	for(Iterator var51 = dados_folder.iterator(); var51.hasNext(); path2 = content[1] + nome_ficheiro2) {
	content = (Object[])var51.next();
	path = content[1] + nome_ficheiro;
	}
	if (!estado.equals("P")) {
	sequencia = this.sequencia();
	}
	try {
	Query query = this.entityManager.createNativeQuery("select a.OF_NUM,c.ID_UTZ_CRIA,a.OP_NUM,a.SEC_NUM,a.MAQ_NUM_ORIG,c." + DATA_INI + ",c." + HORA_INI + ",c." + DATA_FIM + ",c." + HORA_FIM + ", " + "b." + TEMPO_PREP_TOTAL + " as Decimalprep,b." + TEMPO_EXEC_TOTAL + " as Decimalexec " + ",a.OP_PREVISTA,a.OP_COD_ORIGEM, (select ID_TURNO from RP_CONF_TURNO where CAST(c." + HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno, " + "CASE when (c.DATA_INI_M2 != c.DATA_INI_M1 or c.HORA_INI_M1 != c.HORA_INI_M2 or c.DATA_FIM_M2 != c.DATA_FIM_M1 or c.HORA_FIM_M1 != c.HORA_FIM_M2 or " + "b.TEMPO_EXEC_TOTAL_M1 != b.TEMPO_EXEC_TOTAL_M2 or b.TEMPO_PREP_TOTAL_M1 != b.TEMPO_PREP_TOTAL_M2  ) then 1 else 0 END as alterado " + ", (select REF_NUM from RP_OF_OP_LIN where ID_OP_LIN = " + ID_OP_LIN + " ),a.ID_OF_CAB " + " from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OP_CAB in (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = " + id_origem + ")" + "inner join RP_OF_OP_FUNC c on c.ID_OP_CAB in  (select x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = " + id_origem + ") and b.ID_OP_CAB = c.ID_OP_CAB " + "where a.ID_OF_CAB = " + id);
	List<Object[]> dados = query.getResultList();
	Iterator var53 = dados.iterator();
	String quantidades;
	String obs;
	String tipo_t;
	String temp_pre2;
	String size;
	String data_quantidades;
	while(var53.hasNext()) {
	Object[] content = (Object[])var53.next();
	String data_A = "";
	data_A = data_A + "01        ";
	data_A = data_A + content[5].toString().replaceAll("-", "");
	data_A = data_A + sequencia;
	if (novaetiqueta.equals("1")) {
	data_A = data_A + (content[12] + "    ").substring(0, 4);
	} else if (!content[11].toString().equals("1") && !estado.equals("M") && !estado.equals("A") && !estado2.equals("A")) {
	data_A = data_A + (content[12] + "    ").substring(0, 4);
	} else {
	data_A = data_A + "    ";
	}
	data_A = data_A + "1";
	data_A = data_A + (of + "         ").substring(0, 10);
	if ((estado.equals("A") || estado.equals("M")) && !novaetiqueta.equals("1")) {
	data_A = data_A + "1";
	} else {
	data_A = data_A + content[11];
	}
	if (estado.equals("C")) {
	if (content[11].toString().equals("1")) {
	data_A = data_A + ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4, ("0000" + OP_NUM).length());
	} else {
	data_A = data_A + "    ".substring(0, 4);
	}
	} else if (!OP_NUM.equals("NULL")) {
	data_A = data_A + ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4, ("0000" + OP_NUM).length());
	} else {
	data_A = data_A + "    ".substring(0, 4);
	}
	if (content[4].toString().equals("000")) {
	if (primeira_linha) {
	data_A = data_A + "1";
	primeira_linha = false;
	} else {
	data_A = data_A + "2";
	}
	} else {
	data_A = data_A + "2";
	}
	data_A = data_A + (content[3] + "         ").substring(0, 10);
	data_A = data_A + (content[4] + "         ").substring(0, 10);
	if (content[13] != null) {
	data_A = data_A + content[13];
	} else {
	data_A = data_A + "01";
	}
	data_A = data_A + "MO         ".substring(0, 4);
	data_A = data_A + (content[1] + "         ").substring(0, 10);
	data_A = data_A + "   A";
	data_A = data_A + content[5].toString().replaceAll("-", "");
	data_A = data_A + content[6].toString().replace(":", "").substring(0, 6);
	data_A = data_A + content[7].toString().replaceAll("-", "");
	data_A = data_A + content[8].toString().replace(":", "").substring(0, 6);
	data_A = data_A + "04002";
	String temp_pre = "000000000000000";
	double number = 0.0;
	if (content[9] != null) {
	String[] parts = content[9].toString().split(":");
	if (!parts[0].equals("aN")) {
	number = Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]) / 60.0 + Double.parseDouble(parts[2]) / 3600.0;
	number /= (double)total;
	number = number > 0.0 ? number : 0.0;
	quantidades = String.format("%.4f", number).replace(",", "");
	obs = temp_pre + quantidades.replace("$", "");
	temp_pre = obs.substring(obs.length() - 15, obs.length());
	}
	}
	if (tipo.equals("PF") && !estado.equals("M") && !estado.equals("P")) {
	data_A = data_A + temp_pre;
	} else if (tipo.equals("PF") && content[14].toString().equals("1") && !estado.equals("P")) {
	data_A = data_A + temp_pre;
	alteracoes = true;
	} else {
	data_A = data_A + "000000000000000";
	}
	data_A = data_A + SINAL;
	data_A = data_A + "22";
	data_quantidades = "000000000000000";
	double number2 = 0.0;
	if (content[10] != null) {
	String[] parts2 = content[10].toString().split(":");
	if (!parts2[0].equals("aN")) {
	number2 = Double.parseDouble(parts2[0]) + Double.parseDouble(parts2[1]) / 60.0 + Double.parseDouble(parts2[2]) / 3600.0;
	number2 /= (double)total;
	number2 = number2 > 0.0 ? number2 : 0.0;
	tipo_t = String.format("%.4f", number2).replace(",", "");
	temp_pre2 = data_quantidades + tipo_t.replace("$", "");
	data_quantidades = temp_pre2.substring(temp_pre2.length() - 15, temp_pre2.length());
	}
	}
	if (tipo.equals("PF") && !estado.equals("M") && !estado.equals("P")) {
	data_A = data_A + data_quantidades;
	} else if (tipo.equals("PF") && content[14].toString().equals("1") && !estado.equals("P")) {
	data_A = data_A + data_quantidades;
	} else {
	data_A = data_A + "000000000000000";
	}
	data_A = data_A + SINAL;
	data_A = data_A + "22         \r\n";
	if (lider) {
	if (!content[4].toString().equals("000")) {
	existe_maquina = true;
	StringBuffer buf = new StringBuffer(data_A);
	buf.replace(70, 84, "              ");
	buf.replace(47, 48, "1");
	tipo_t = "000000000000000";
	temp_pre2 = "000000000000000";
	double tempprep = this.getTempos(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, id_origem, "P");
	double tempexec = this.getTempos(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, id_origem, "E");
	String parts_exec = String.format("%.4f", tempprep).replace(",", "");
	size = tipo_t + parts_exec.replace("$", "");
	tipo_t = size.substring(size.length() - 15, size.length());
	String parts_prep = String.format("%.4f", tempexec).replace(",", "");
	String size2 = temp_pre2 + parts_prep.replace("$", "");
	temp_pre2 = size2.substring(size2.length() - 15, size2.length());
	buf.replace(121, 136, tipo_t);
	buf.replace(139, 154, temp_pre2);
	data_maquina = buf.toString();
	lider = false;
	}
	data_inicio = data_A.substring(0, 87);
	if (content[11].toString().equals("2") && (estado.equals("C") || estado.equals("M")) && atualiza && ficheiro != 1) {
	Integer id_t = id_etiqueta;
	tipo_t = "C";
	if (id_etiqueta == null) {
	id_t = id;
	tipo_t = "PF";
	}
	this.atualizatabela_AUX(content[1].toString(), content[5].toString(), content[15].toString(), of, content[12].toString(), id_t, tipo_t, content[6].toString());
	atualiza = false;
	}
	}
	data = data + data_A;
	if (cria_pausa) {
	linha_utz.put(content[1].toString(), data_A);
	linha_utz_inicio.put(content[1].toString(), data_A.substring(0, 87));
	}
	}
	if (existe_maquina && tipo.equals("PF")) {
	data_maquina = data_maquina + data;
	data = data_maquina;
	}
	Iterator var57;
	Query query4;
	Object[] content3;
	String size;
	if (cria_pausa) {
	Boolean criou_PAUSA = false;
	query4 = this.entityManager.createNativeQuery("select c." + DATA_INI + ",c." + HORA_INI + ",c." + DATA_FIM + ",c." + HORA_FIM + ", " + "cast((DATEDIFF(second,DATEADD(DAY, DATEDIFF(DAY, c." + HORA_INI + ", c." + DATA_INI + " ), CAST(c." + HORA_INI + " AS DATETIME)), DATEADD(DAY, DATEDIFF(DAY, c." + HORA_FIM + ", c." + DATA_FIM + " ), CAST(c." + HORA_FIM + " AS DATETIME)))/3600.00) as decimal(18,4)) as timediff, " + "c." + TIPO_PARAGEM + ",c." + MOMENTO_PARAGEM + ",c.ID_UTZ_CRIA as utz1,a.ID_UTZ_CRIA as utz2, " + "CASE when (c.MOMENTO_PARAGEM_M2 != c.MOMENTO_PARAGEM_M1 or c.TIPO_PARAGEM_M2 != c.TIPO_PARAGEM_M1 or c.DATA_INI_M2 != c.DATA_INI_M1 or c.HORA_INI_M1 != c.HORA_INI_M2 or c.DATA_FIM_M2 != c.DATA_FIM_M1 or c.HORA_FIM_M1 != c.HORA_FIM_M2 ) then 1 else 0 END as alterado, " + "CASE when (c.DATA_INI_M1 is null or c.HORA_INI_M1 is null or c.DATA_FIM_M1 is null or c.HORA_FIM_M1 is null ) then 1 else 0 END as novo " + "from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB " + "inner join RP_OF_PARA_LIN c on c.ID_OP_CAB = b.ID_OP_CAB " + "where a.ID_OF_CAB = " + id);
	List<Object[]> dados2 = query4.getResultList();
	Integer count = 0;
	var57 = dados2.iterator();
	label4280:
	while(true) {
	while(true) {
	if (!var57.hasNext()) {
	break label4280;
	}
	content3 = (Object[])var57.next();
	count = count + 1;
	data_quantidades = "";
	quantidades = "";
	data_quantidades = data_quantidades + "B";
	data_quantidades = data_quantidades + (content3[0] != null ? content3[0] : "").toString().replaceAll("-", "");
	data_quantidades = data_quantidades + (content3[1] != null ? content3[1] : "      ").toString().replace(":", "").substring(0, 6);
	data_quantidades = data_quantidades + (content3[2] != null ? content3[2] : "").toString().replaceAll("-", "");
	data_quantidades = data_quantidades + (content3[3] != null ? content3[3] : "      ").toString().replace(":", "").substring(0, 6);
	data_quantidades = data_quantidades + (content3[5] + "    ").substring(0, 4);
	data_quantidades = data_quantidades + "3";
	obs = "000000000000000";
	if (content3[6] != null && content3[6].toString().equals("P")) {
	size = (content3[4] != null ? content3[4] : "").toString().replace(".", "");
	tipo_t = obs + size;
	obs = tipo_t.substring(tipo_t.length() - 15, tipo_t.length());
	}
	data_quantidades = data_quantidades + obs;
	data_quantidades = data_quantidades + SINAL;
	data_quantidades = data_quantidades + "3";
	size = "000000000000000";
	if (content3[6] != null && content3[6].toString().equals("E")) {
	tipo_t = (content3[4] != null ? content3[4] : "").toString().replace(".", "");
	temp_pre2 = size + tipo_t;
	size = temp_pre2.substring(temp_pre2.length() - 15, temp_pre2.length());
	}
	data_quantidades = data_quantidades + size;
	data_quantidades = data_quantidades + SINAL;
	data_quantidades = data_quantidades + "                                       \r\n";
	tipo_t = this.sequencia();
	StringBuffer buf3 = new StringBuffer((String)linha_utz.get(content3[7].toString()));
	buf3.replace(18, 27, tipo_t);
	String linha3 = buf3.toString();
	if (!existe_maquina) {
	quantidades = quantidades + linha3;
	}
	String linha_A_MAQUINA = "";
	String linha2;
	if (existe_maquina) {
	BufferedReader bufReader = new BufferedReader(new StringReader(data_maquina));
	linha2 = null;
	while((linha2 = bufReader.readLine()) != null) {
	StringBuffer buf6 = new StringBuffer(linha2);
	buf6.replace(18, 27, tipo_t);
	buf6.replace(121, 136, "000000000000000");
	buf6.replace(139, 154, "000000000000000");
	size = buf6.toString();
	if (buf6.substring(74, 84).trim().equals(content3[7].toString()) || buf6.substring(74, 84).equals("          ")) {
	quantidades = quantidades + size + "\r\n";
	}
	if (buf6.lastIndexOf("MO") == -1) {
	linha_A_MAQUINA = size + "\r\n";
	}
	}
	}
	StringBuffer buf;
	if (existe_maquina && tipo.equals("PF") && content3[7].toString().equals(content3[8].toString())) {
	buf = new StringBuffer(data_maquina);
	buf.replace(18, 27, tipo_t);
	linha2 = buf.toString();
	if (!criou_PAUSA && (!estado2.equals("M") || estado2.equals("M"))) {
	this.CRIAPAUSASMAQUINA(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, TIPO_PARAGEM, SINAL, linha2.substring(0, 87), linha_A_MAQUINA, path2, ficheirosdownload, nome_ficheiro2, nomezip, id, "P");
	this.CRIAPAUSASMAQUINA(DATA_INI, HORA_INI, DATA_FIM, HORA_FIM, MOMENTO_PARAGEM, TIPO_PARAGEM, SINAL, linha2.substring(0, 87), linha_A_MAQUINA, path2, ficheirosdownload, nome_ficheiro2, nomezip, id, "E");
	criou_PAUSA = true;
	}
	}
	buf = new StringBuffer((String)linha_utz_inicio.get(content3[7].toString()));
	buf.replace(18, 27, tipo_t);
	linha2 = buf.toString();
	quantidades = quantidades + linha2 + data_quantidades;
	if (estado2.equals("M") && content3[9].toString().equals("1") && !content3[10].toString().equals("1") && Float.parseFloat(content3[4].toString()) > 0.0F) {
	this.criar_ficheiro_Pausa(quantidades, path2, count, ficheirosdownload, nome_ficheiro2, nomezip);
	} else if (estado2.equals("M") && content3[10].toString().equals("1") && ficheiro == 2 && (content3[4] != null ? Float.parseFloat(content3[4].toString()) : 0.0F) > 0.0F) {
	this.criar_ficheiro_Pausa(quantidades, path2, count, ficheirosdownload, nome_ficheiro2, nomezip);
	} else if (!estado2.equals("M") && Float.parseFloat(content3[4].toString()) > 0.0F) {
	this.criar_ficheiro_Pausa(quantidades, path2, count, ficheirosdownload, nome_ficheiro2, nomezip);
	}
	}
	}
	}
	String data4;
	List dados4;
	Query query4_COMP;
	if (!estado.equals("P")) {
	data4 = "";
	data4 = " ,CASE when (c.QUANT_BOAS_TOTAL_M1 != c.QUANT_BOAS_TOTAL_M2 or e.QUANT_BOAS_M1 != e.QUANT_BOAS_M2   ) then 1 else 0 END as alterado ";
	query4 = this.entityManager.createNativeQuery("Select a.ID_OF_CAB_ORIGEM,a.OF_NUM,e.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,d." + DATA_INI + ",d." + HORA_INI + ",d." + DATA_FIM + ",d." + HORA_FIM + ",d.ID_UTZ_CRIA,c.REF_IND,cast(c." + QUANT_BOAS_TOTAL + " as decimal(18,4)) as qtd1,cast(e." + QUANT_BOAS + " as decimal(18,4)) as qtd2 " + ", a.OP_PREVISTA, c.OBS_REF, (select ID_TURNO from RP_CONF_TURNO where CAST( d." + HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno, a.OP_COD_ORIGEM " + data4 + " from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB " + "inner join RP_OF_OP_LIN c on  c.ID_OP_LIN = " + ID_OP_LIN + " " + "inner join RP_OF_OP_FUNC d on d.ID_OP_CAB = b.ID_OP_CAB and d.ID_OP_CAB in (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = " + id_origem + " ) " + "left join RP_OF_OP_ETIQUETA e on e.ID_OP_LIN = c.ID_OP_LIN " + "where a.ID_OF_CAB = " + id + " and (a.OF_NUM is not null or e.OF_NUM_ORIGEM is not null) ");
	query4_COMP = this.entityManager.createNativeQuery("Select a.ID_OF_CAB_ORIGEM,a.OF_NUM,e.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,d." + DATA_INI + ",d." + HORA_INI + ",d." + DATA_FIM + ",d." + HORA_FIM + ",d.ID_UTZ_CRIA,c.REF_IND,cast(c." + QUANT_BOAS_TOTAL + " as decimal(18,4)) as qtd1,cast(e." + QUANT_BOAS + " as decimal(18,4)) as qtd2 " + ", a.OP_PREVISTA, c.OBS_REF, (select ID_TURNO from RP_CONF_TURNO where CAST( d." + HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno,a.OP_COD_ORIGEM " + data4 + " from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB " + "inner join RP_OF_OP_LIN c on  b.ID_OP_CAB = c.ID_OP_CAB " + "inner join RP_OF_OP_FUNC d on d.ID_OP_CAB = (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = " + id_origem + " ) " + "left join RP_OF_OP_ETIQUETA e on e.ID_OP_LIN = c.ID_OP_LIN " + "where a.ID_OF_CAB = " + id + " and (a.OF_NUM is not null or e.OF_NUM_ORIGEM is not null)  and e.ID_REF_ETIQUETA =" + id_etiqueta);
	if (tipo.equals("COMP")) {
	dados4 = query4_COMP.getResultList();
	} else {
	dados4 = query4.getResultList();
	}
	for(var57 = dados4.iterator(); var57.hasNext(); data = data + data_quantidades) {
	content3 = (Object[])var57.next();
	data_quantidades = "";
	data_quantidades = data_quantidades + "01        ";
	data_quantidades = data_quantidades + content3[10].toString().replaceAll("-", "");
	data_quantidades = data_quantidades + sequencia;
	if (novaetiqueta.equals("1")) {
	data_quantidades = data_quantidades + (content3[21] + "    ").substring(0, 4);
	} else if (!content3[18].toString().equals("1") && !estado.equals("M") && !estado.equals("A") && !estado2.equals("A")) {
	data_quantidades = data_quantidades + (content3[21] + "    ").substring(0, 4);
	} else {
	data_quantidades = data_quantidades + "    ";
	}
	data_quantidades = data_quantidades + "1";
	if (content3[0] == null) {
	data_quantidades = data_quantidades + (content3[1] + "         ").substring(0, 10);
	} else {
	data_quantidades = data_quantidades + (content3[2] + "         ").substring(0, 10);
	}
	if ((estado.equals("A") || estado.equals("M")) && !novaetiqueta.equals("1")) {
	data_quantidades = data_quantidades + "1";
	} else {
	data_quantidades = data_quantidades + content3[18];
	}
	if (estado.equals("C")) {
	if (content3[18].toString().equals("1")) {
	data_quantidades = data_quantidades + ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4, ("0000" + OP_NUM).length());
	} else {
	data_quantidades = data_quantidades + "    ".substring(0, 4);
	}
	} else if (!OP_NUM.equals("NULL")) {
	data_quantidades = data_quantidades + ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4, ("0000" + OP_NUM).length());
	} else {
	data_quantidades = data_quantidades + "    ".substring(0, 4);
	}
	data_quantidades = data_quantidades + "1";
	data_quantidades = data_quantidades + (content3[9] + "         ").substring(0, 10);
	data_quantidades = data_quantidades + (content3[8] + "         ").substring(0, 10);
	if (content3[20] != null) {
	data_quantidades = data_quantidades + content3[20];
	} else {
	data_quantidades = data_quantidades + "01";
	}
	if (content3[0] == null) {
	if (content3[8].toString().equals("000")) {
	data_quantidades = data_quantidades + "MO         ".substring(0, 4);
	} else {
	data_quantidades = data_quantidades + "    ";
	}
	} else {
	data_quantidades = data_quantidades + "MO         ".substring(0, 4);
	}
	if (content3[0] == null) {
	if (content3[8].toString().equals("000")) {
	data_quantidades = data_quantidades + (content3[14] + "         ").substring(0, 10);
	} else {
	data_quantidades = data_quantidades + "          ";
	}
	} else {
	data_quantidades = data_quantidades + (content3[14] + "         ").substring(0, 10);
	}
	data_quantidades = data_quantidades + "   Q";
	data_quantidades = data_quantidades + content3[10].toString().replaceAll("-", "");
	data_quantidades = data_quantidades + content3[11].toString().replace(":", "").substring(0, 6);
	data_quantidades = data_quantidades + content3[12].toString().replaceAll("-", "");
	data_quantidades = data_quantidades + content3[13].toString().replace(":", "").substring(0, 6);
	data_quantidades = data_quantidades + (content3[4] + "                 ").substring(0, 17);
	data_quantidades = data_quantidades + ((content3[5] != null ? content3[5] : "") + "                 ").substring(0, 10);
	data_quantidades = data_quantidades + ((content3[6] != null ? content3[6] : "") + "                 ").substring(0, 10);
	data_quantidades = data_quantidades + ((content3[15] != null ? content3[15] : "") + "                 ").substring(0, 10);
	if (content3[7] != null) {
	data_quantidades = data_quantidades + ("000000000" + content3[7]).substring(("000000000" + content3[7]).length() - 9, ("000000000" + content3[7]).length());
	} else {
	data_quantidades = data_quantidades + "000000000";
	}
	data_quantidades = data_quantidades + "1";
	quantidades = "000000000000000";
	if (content3[0] == null) {
	if (content3[16] != null) {
	obs = content3[16].toString().replace(".", "");
	size = quantidades + obs;
	quantidades = size.substring(size.length() - 15, size.length());
	}
	} else if (content3[17] != null) {
	obs = content3[17].toString().replace(".", "");
	size = quantidades + obs;
	quantidades = size.substring(size.length() - 15, size.length());
	}
	if (estado.equals("M")) {
	if (content3[22].toString().equals("0")) {
	quantidades = "000000000000000";
	data_quantidades = data_quantidades + quantidades + "  ";
	} else {
	data_quantidades = data_quantidades + quantidades + "  ";
	alteracoes = true;
	}
	} else {
	data_quantidades = data_quantidades + quantidades + "  ";
	}
	data_quantidades = data_quantidades + SINAL;
	data_quantidades = data_quantidades + "    ";
	data_quantidades = data_quantidades + "000000000000000";
	data_quantidades = data_quantidades + "                                                                                    ";
	obs = content3[19] != null ? content3[19].toString() : "";
	data_quantidades = data_quantidades + (obs + "                                        ").substring(0, 39);
	data_quantidades = data_quantidades + "\r\n";
	}
	}
	if (!estado.equals("P")) {
	data4 = "";
	if (estado.equals("M")) {
	if (ficheiro == 1) {
	if (tipo.equals("COMP")) {
	data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M1 != 0 or f.APAGADO = 1)";
	} else {
	data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M1 != 0) ";
	}
	} else {
	data4 = " and (d.QUANT_DEF_M1 != d.QUANT_DEF_M2 and d.QUANT_DEF_M2 != 0) ";
	}
	}
	query4 = this.entityManager.createNativeQuery("Select d.COD_DEF,cast(d." + QUANT_DEF + " as decimal(18,4)),a.ID_OF_CAB_ORIGEM,a.OF_NUM,f.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,e." + DATA_INI + ",e." + HORA_INI + ",e." + DATA_FIM + ",e." + HORA_FIM + ", " + "d.ID_UTZ_CRIA,c.REF_IND,c." + QUANT_BOAS_TOTAL + ",f." + QUANT_BOAS + " ,d.OBS_DEF " + ", a.OP_PREVISTA , (select ID_TURNO from RP_CONF_TURNO where CAST( e." + HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno,a.OP_COD_ORIGEM " + "from RP_OF_CAB a " + "inner join RP_OF_OP_LIN c on  c.ID_OP_LIN = " + ID_OP_LIN + " " + "inner join RP_OF_DEF_LIN d on d.ID_OP_LIN = c.ID_OP_LIN " + "inner join RP_OF_OP_FUNC e on e.ID_OP_CAB = (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = " + id_origem + " ) " + "left join RP_OF_OP_ETIQUETA f on f.ID_OP_LIN = c.ID_OP_LIN and f.ID_REF_ETIQUETA = d.ID_REF_ETIQUETA " + "where a.ID_OF_CAB = " + id + data4 + " order by c.REF_NUM,d.COD_DEF");
	query4_COMP = this.entityManager.createNativeQuery("Select d.COD_DEF,cast(d." + QUANT_DEF + " as decimal(18,4)),a.ID_OF_CAB_ORIGEM,a.OF_NUM,f.OF_NUM_ORIGEM,a.OP_NUM,c.REF_NUM,c.REF_VAR1,c.REF_VAR2,c.REF_INDNUMENR, a.MAQ_NUM_ORIG,a.SEC_NUM,e." + DATA_INI + ",e." + HORA_INI + ",e." + DATA_FIM + ",e." + HORA_FIM + ", " + "d.ID_UTZ_CRIA,c.REF_IND,c." + QUANT_BOAS_TOTAL + ",f." + QUANT_BOAS + " ,d.OBS_DEF " + ", a.OP_PREVISTA , (select ID_TURNO from RP_CONF_TURNO where CAST( e." + HORA_INI + "  as time) between HORA_INICIO and HORA_FIM ) as turno, a.OP_COD_ORIGEM  " + "from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on  b.ID_OF_CAB = a.ID_OF_CAB " + "inner join RP_OF_OP_LIN c on  b.ID_OP_CAB = c.ID_OP_CAB " + "inner join RP_OF_DEF_LIN d on d.ID_OP_LIN = c.ID_OP_LIN " + "inner join RP_OF_OP_FUNC e on e.ID_OP_CAB = (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB = " + id_origem + " ) " + "left join RP_OF_OP_ETIQUETA f on f.ID_OP_LIN = c.ID_OP_LIN and f.ID_REF_ETIQUETA = d.ID_REF_ETIQUETA " + "where a.ID_OF_CAB = " + id + data4 + " and d.ID_REF_ETIQUETA = " + id_etiqueta + " order by c.REF_NUM,d.COD_DEF");
	if (tipo.equals("COMP")) {
	dados4 = query4_COMP.getResultList();
	} else {
	dados4 = query4.getResultList();
	}
	for(var57 = dados4.iterator(); var57.hasNext(); data = data + data_quantidades) {
	content3 = (Object[])var57.next();
	alteracoes = true;
	data_quantidades = "";
	data_quantidades = data_quantidades + "01        ";
	data_quantidades = data_quantidades + content3[12].toString().replaceAll("-", "");
	data_quantidades = data_quantidades + sequencia;
	if (novaetiqueta.equals("1")) {
	data_quantidades = data_quantidades + (content3[23] + "    ").substring(0, 4);
	} else if (!content3[21].toString().equals("1") && !estado.equals("M") && !estado.equals("A") && !estado2.equals("A")) {
	data_quantidades = data_quantidades + (content3[23] + "    ").substring(0, 4);
	} else {
	data_quantidades = data_quantidades + "    ";
	}
	data_quantidades = data_quantidades + "1";
	if (content3[2] == null) {
	data_quantidades = data_quantidades + (content3[3] + "         ").substring(0, 10);
	} else {
	data_quantidades = data_quantidades + (content3[4] + "         ").substring(0, 10);
	}
	if ((estado.equals("A") || estado.equals("M")) && !novaetiqueta.equals("1")) {
	data_quantidades = data_quantidades + "1";
	} else {
	data_quantidades = data_quantidades + content3[21];
	}
	if (estado.equals("C")) {
	if (content3[21].toString().equals("1")) {
	data_quantidades = data_quantidades + ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4, ("0000" + OP_NUM).length());
	} else {
	data_quantidades = data_quantidades + "    ".substring(0, 4);
	}
	} else if (!OP_NUM.equals("NULL")) {
	data_quantidades = data_quantidades + ("0000" + OP_NUM).substring(("0000" + OP_NUM).length() - 4, ("0000" + OP_NUM).length());
	} else {
	data_quantidades = data_quantidades + "    ".substring(0, 4);
	}
	data_quantidades = data_quantidades + "1";
	data_quantidades = data_quantidades + (content3[11] + "         ").substring(0, 10);
	data_quantidades = data_quantidades + (content3[10] + "         ").substring(0, 10);
	if (content3[22] != null) {
	data_quantidades = data_quantidades + content3[22];
	} else {
	data_quantidades = data_quantidades + "01";
	}
	if (content3[2] == null) {
	if (content3[10].toString().equals("000")) {
	data_quantidades = data_quantidades + "MO         ".substring(0, 4);
	} else {
	data_quantidades = data_quantidades + "    ";
	}
	} else {
	data_quantidades = data_quantidades + "MO         ".substring(0, 4);
	}
	if (content3[2] == null) {
	if (content3[10].toString().equals("000")) {
	data_quantidades = data_quantidades + (content3[16] + "         ").substring(0, 10);
	} else {
	data_quantidades = data_quantidades + "          ";
	}
	} else {
	data_quantidades = data_quantidades + (content3[16] + "         ").substring(0, 10);
	}
	data_quantidades = data_quantidades + "   R";
	data_quantidades = data_quantidades + content3[12].toString().replaceAll("-", "");
	data_quantidades = data_quantidades + content3[13].toString().replace(":", "").substring(0, 6);
	data_quantidades = data_quantidades + content3[14].toString().replaceAll("-", "");
	data_quantidades = data_quantidades + content3[15].toString().replace(":", "").substring(0, 6);
	data_quantidades = data_quantidades + (content3[6] + "                 ").substring(0, 17);
	data_quantidades = data_quantidades + ((content3[7] != null ? content3[7] : "") + "                 ").substring(0, 10);
	data_quantidades = data_quantidades + ((content3[8] != null ? content3[8] : "") + "                 ").substring(0, 10);
	data_quantidades = data_quantidades + ((content3[17] != null ? content3[17] : "") + "                 ").substring(0, 10);
	if (content3[9] != null) {
	data_quantidades = data_quantidades + ("000000000" + content3[9]).substring(("000000000" + content3[9]).length() - 9, ("000000000" + content3[9]).length());
	} else {
	data_quantidades = data_quantidades + "000000000";
	}
	data_quantidades = data_quantidades + (content3[0] + "    ").substring(0, 4);
	data_quantidades = data_quantidades + "1";
	quantidades = "000000000000000";
	if (content3[1] != null) {
	obs = content3[1].toString().replace(".", "");
	size = quantidades + obs;
	quantidades = size.substring(size.length() - 15, size.length());
	}
	data_quantidades = data_quantidades + quantidades + "  ";
	data_quantidades = data_quantidades + SINAL;
	data_quantidades = data_quantidades + "                                                                                                       ";
	obs = content3[20] != null ? content3[20].toString() : "";
	data_quantidades = data_quantidades + (obs + "                                        ").substring(0, 39);
	data_quantidades = data_quantidades + "\r\n";
	}
	}
	if (!ficheirosdownload) {
	File file;
	String[] keyValuePairs;
	if (!estado.equals("M") && !estado.equals("P")) {
	file = new File(path);
	try {
	file.createNewFile();
	} catch (IOException var135) {
	keyValuePairs = new String[]{"TEXTO_ERRO ::" + var135.getMessage() + " " + file.getAbsolutePath()};
	this.verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
	var135.printStackTrace();
	}
	try {
	fw = new FileWriter(file.getAbsoluteFile(), true);
	} catch (IOException var134) {
	var134.printStackTrace();
	}
	bw = new BufferedWriter(fw);
	bw.write(data);
	} else if (estado.equals("M") && alteracoes && !estado.equals("P")) {
	file = new File(path);
	try {
	file.createNewFile();
	} catch (IOException var140) {
	keyValuePairs = new String[]{"TEXTO_ERRO ::" + var140.getMessage() + " " + file.getAbsolutePath()};
	if (file.getAbsolutePath() != null) {
	this.verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
	}
	var140.printStackTrace();
	}
	try {
	fw = new FileWriter(file.getAbsoluteFile(), true);
	} catch (IOException var133) {
	var133.printStackTrace();
	}
	bw = new BufferedWriter(fw);
	bw.write(data);
	}
	} else if (!estado.equals("P")) {
	Map<String, String> env = new HashMap();
	env.put("create", "true");
	java.nio.file.Path pathh = Paths.get("c:/sgiid/temp_files/" + nomezip + ".zip");
	URI uri = URI.create("jar:" + pathh.toUri());
	Throwable var157 = null;
	content3 = null;
	try {
	FileSystem fs = FileSystems.newFileSystem(uri, env);
	try {
	java.nio.file.Path nf = fs.getPath(nome_ficheiro);
	Throwable var165 = null;
	obs = null;
	try {
	Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
	try {
	writer.write(data);
	} finally {
	if (writer != null) {
	writer.close();
	}
	}
	} catch (Throwable var137) {
	if (var165 == null) {
	var165 = var137;
	} else if (var165 != var137) {
	var165.addSuppressed(var137);
	}
	throw var165;
	}
	} finally {
	if (fs != null) {
	fs.close();
	}
	}
	} catch (Throwable var139) {
	if (var157 == null) {
	var157 = var139;
	} else if (var157 != var139) {
	var157.addSuppressed(var139);
	}
	throw var157;
	}
	}
	} catch (IOException var141) {
	String[] keyValuePairs = new String[]{"TEXTO_ERRO ::" + var141.getMessage()};
	this.verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
	var141.printStackTrace();
	} finally {
	try {
	if (bw != null) {
	bw.close();
	}
	if (fw != null) {
	fw.close();
	}
	} catch (IOException var132) {
	String[] keyValuePairs = new String[]{"TEXTO_ERRO ::" + var132.getMessage()};
	this.verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
	var132.printStackTrace();
	}
	}
	}
	public void criar_ficheiro_PausaMAQUINA(Object[] content2, String SINAL, String linha_inicial, String linha_A_MAQUINA, String path2, Boolean ficheirosdownload, String nome_ficheiro2, String nomezip, Integer count, String estado) throws ParseException {
	SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
	String data_pausa = "";
	String data_pausa_p = "";
	String data_pausa_p2 = "";
	data_pausa_p = data_pausa_p + linha_A_MAQUINA;
	data_pausa = data_pausa + "B";
	data_pausa = data_pausa + (content2[0] != null ? f.format(p.parse(content2[0].toString())) : "").toString();
	data_pausa = data_pausa + (content2[1] != null ? f.format(p.parse(content2[1].toString())) : "").toString();
	data_pausa = data_pausa + (content2[3] + "    ").substring(0, 4);
	data_pausa = data_pausa + "3";
	String temp_pre = "000000000000000";
	String temp_exec;
	String parts_exec;
	if (content2[4] != null && content2[4].toString().equals("P")) {
	temp_exec = (content2[2] != null ? content2[2] : "").toString().replace(".", "");
	parts_exec = temp_pre + temp_exec;
	temp_pre = parts_exec.substring(parts_exec.length() - 15, parts_exec.length());
	}
	data_pausa = data_pausa + temp_pre;
	data_pausa = data_pausa + SINAL;
	data_pausa = data_pausa + "3";
	temp_exec = "000000000000000";
	String seq;
	if (content2[4] != null && content2[4].toString().equals("E")) {
	parts_exec = (content2[2] != null ? content2[2] : "").toString().replace(".", "");
	seq = temp_exec + parts_exec;
	temp_exec = seq.substring(seq.length() - 15, seq.length());
	}
	data_pausa = data_pausa + temp_exec;
	data_pausa = data_pausa + SINAL;
	data_pausa = data_pausa + "                                       \r\n";
	data_pausa_p = data_pausa_p + linha_inicial + data_pausa;
	BufferedReader bufReader = new BufferedReader(new StringReader(data_pausa_p));
	seq = this.sequencia();
	String line = null;
	try {
	while((line = bufReader.readLine()) != null) {
	StringBuffer buf6 = new StringBuffer(line);
	buf6.replace(18, 27, seq);
	String linha6 = buf6.toString();
	data_pausa_p2 = data_pausa_p2 + linha6 + "\r\n";
	}
	} catch (IOException var24) {
	var24.printStackTrace();
	}
	try {
	Float num = 0.0F;
	if (content2[2] != null) {
	num = Float.parseFloat(content2[2].toString());
	}
	if (num > 0.0F) {
	this.criar_ficheiro_Pausa(data_pausa_p2, path2 + "_MAQ_" + estado, count, ficheirosdownload, nome_ficheiro2 + "_MAQ_" + estado, nomezip);
	}
	} catch (IOException var23) {
	var23.printStackTrace();
	}
	}
	public void CRIAPAUSASMAQUINA(String DATA_INI, String HORA_INI, String DATA_FIM, String HORA_FIM, String MOMENTO_PARAGEM, String TIPO_PARAGEM, String SINAL, String linha_inicial, String linha_A_MAQUINA, String path2, Boolean ficheirosdownload, String nome_ficheiro2, String nomezip, Integer ID_OF_CAB, String ESTADO) {
	Query query2 = this.entityManager.createNativeQuery("declare @parents table (Data_inicio datetime, Data_fim datetime, ID int) DECLARE @ID_UTZ_CRIA NVARCHAR(6) DECLARE @ESTADO NVARCHAR(6) = '" + ESTADO + "' " + "DECLARE @Data_inicio datetime  " + "DECLARE @Data_fim datetime " + "DECLARE @Data_fim2 datetime " + "DECLARE @ID INT " + "DECLARE @ID2 INT " + "DECLARE @ID_RESULTADO INT " + "DECLARE @COUNT INT = 1 " + "DECLARE @COUNT1 INT = 0 " + "DECLARE @TOTAL INT = 0 " + "DECLARE @ID_OF_CAB INT = " + ID_OF_CAB + " " + "DECLARE @getid CURSOR " + "DECLARE @getid2 CURSOR " + "SET @getid = CURSOR FOR SELECT ID_PARA_LIN  " + "FROM  RP_OF_PARA_LIN  " + "where ID_OP_CAB in (select  ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB)  " + "and " + MOMENTO_PARAGEM + " = @ESTADO " + "and  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) <> (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) " + "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) " + "OPEN @getid " + "FETCH NEXT " + "FROM @getid INTO @ID " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN  " + "SET @COUNT1= 0 " + "SELECT @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)),@ID_UTZ_CRIA = ID_UTZ_CRIA  " + ",@Data_fim =  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) " + "FROM  RP_OF_PARA_LIN where ID_PARA_LIN = @ID " + "IF @ESTADO = 'E' " + "BEGIN " + "select @TOTAL = count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  " + "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI + " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI + " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null\t)) " + "and(cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE " + "BEGIN " + "select @TOTAL =  count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI + "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END " + "WHILE (@COUNT1  = 0) " + "BEGIN " + "IF EXISTS (SELECT  FROM RP_OF_PARA_LIN where  " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL " + "BEGIN " + "SET @getid2 = CURSOR FOR (SELECT ID_PARA_LIN FROM RP_OF_PARA_LIN where  " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio  " + "and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) " + "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) " + "OPEN @getid2 " + "FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN " + "SELECT TOP 1 @ID_RESULTADO=ID_PARA_LIN, @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)), " + "@Data_fim =  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)), @ID_UTZ_CRIA = ID_UTZ_CRIA  " + "FROM  RP_OF_PARA_LIN  " + "where ID_PARA_LIN = @ID2 " + "IF @ESTADO = 'E' " + "BEGIN " + "select @TOTAL = count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  " + "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI + " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI + " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null\t)) " + "and(cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE " + "BEGIN " + "select @TOTAL =  count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI + "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END " + "SET @COUNT= @COUNT+1 " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime))), " + "@Data_fim2 = (select MIN((cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)))  " + "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  " + "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio " + "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID " + "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 " + "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID_RESULTADO)\t " + "set @COUNT = 1\t " + "IF(@Data_fim2 is not null) SET @COUNT = @TOTAL " + "END " + "IF not EXISTS (SELECT  FROM RP_OF_PARA_LIN where  +" + "(cast( " + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and + (cast( " + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL " + "BEGIN SET @COUNT= 1 END " + "FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "END " + "END " + "ELSE " + "BEGIN " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime))), " + "@Data_fim2 = (select MIN((cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)))  " + "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  " + "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio " + "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID " + "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 " + "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID_RESULTADO)\t " + "set @COUNT = 1\t " + "END " + "SET @COUNT1= @COUNT1+1 " + "END " + "END " + "FETCH NEXT " + "FROM @getid INTO @ID " + "set @COUNT = 1\t " + "END select a.Data_inicio,a.Data_fim , cast((DATEDIFF(second,a.Data_inicio, a.Data_fim)/3600.00) as decimal(18,4)) as timediff, b.TIPO_PARAGEM_M2 ,b." + MOMENTO_PARAGEM + " from @parents a inner join RP_OF_PARA_LIN b on a.ID = b.ID_PARA_LIN");
	List<Object[]> dados2 = query2.getResultList();
	Integer count = 0;
	Iterator var20 = dados2.iterator();
	while(var20.hasNext()) {
	Object[] content2 = (Object[])var20.next();
	count = count + 1;
	try {
	this.criar_ficheiro_PausaMAQUINA(content2, SINAL, linha_inicial, linha_A_MAQUINA, path2, ficheirosdownload, nome_ficheiro2, nomezip, count, ESTADO);
	} catch (ParseException var22) {
	var22.printStackTrace();
	}
	}
	}
	public double getTempos(String DATA_INI, String HORA_INI, String DATA_FIM, String HORA_FIM, String MOMENTO_PARAGEM, Integer ID_OF_CAB, String ESTADO) {
	double number = 0.0;
	Query query2 = this.entityManager.createNativeQuery("declare @parents table (Data_inicio datetime, Data_fim datetime, ID int) DECLARE @ID_UTZ_CRIA NVARCHAR(6) DECLARE @ESTADO NVARCHAR(6) = '" + ESTADO + "' " + "DECLARE @Data_inicio datetime  " + "DECLARE @Data_fim datetime " + "DECLARE @Data_fim2 datetime " + "DECLARE @ID INT " + "DECLARE @ID2 INT " + "DECLARE @ID_RESULTADO INT " + "DECLARE @COUNT INT = 1 " + "DECLARE @COUNT1 INT = 0 " + "DECLARE @TOTAL INT = 0 " + "DECLARE @ID_OF_CAB INT = " + ID_OF_CAB + " " + "DECLARE @getid CURSOR " + "DECLARE @getid2 CURSOR " + "SET @getid = CURSOR FOR SELECT ID_PARA_LIN  " + "FROM  RP_OF_PARA_LIN  " + "where ID_OP_CAB in (select  ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB)  " + "and " + MOMENTO_PARAGEM + " = @ESTADO " + "and  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) <> (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) " + "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) " + "OPEN @getid " + "FETCH NEXT " + "FROM @getid INTO @ID " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN  " + "SET @COUNT1= 0 " + "SELECT @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)),@ID_UTZ_CRIA = ID_UTZ_CRIA  " + ",@Data_fim =  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) " + "FROM  RP_OF_PARA_LIN where ID_PARA_LIN = @ID " + "IF @ESTADO = 'E' " + "BEGIN " + "select @TOTAL = count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  " + "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI + " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI + " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null\t)) " + "and(cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE " + "BEGIN " + "select @TOTAL =  count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI + "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END " + "WHILE (@COUNT1  = 0) " + "BEGIN " + "IF EXISTS (SELECT  FROM RP_OF_PARA_LIN where  " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL " + "BEGIN " + "SET @getid2 = CURSOR FOR (SELECT ID_PARA_LIN FROM RP_OF_PARA_LIN where  " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and " + "(cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio  " + "and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) " + "order by (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) " + "OPEN @getid2 " + "FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "WHILE @@FETCH_STATUS = 0 " + "BEGIN " + "SELECT TOP 1 @ID_RESULTADO=ID_PARA_LIN, @Data_inicio =  (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)), " + "@Data_fim =  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)), @ID_UTZ_CRIA = ID_UTZ_CRIA  " + "FROM  RP_OF_PARA_LIN  " + "where ID_PARA_LIN = @ID2 " + "IF @ESTADO = 'E' " + "BEGIN " + "select @TOTAL = count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "left join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and  " + "((cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) > (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) " + "or c." + HORA_INI + " is null) and ((cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) <= @Data_inicio or ( " + "(cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI + " as datetime)) <= @Data_inicio and c." + HORA_INI + " is null\t)) " + "and(cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)) >= @Data_inicio  " + "END " + "ELSE " + "BEGIN " + "select @TOTAL =  count() from RP_OF_OP_FUNC a " + "inner join RP_OF_OP_CAB b on a.ID_OP_CAB = b.ID_OP_CAB " + "inner join RP_OF_PREP_LIN c on a.ID_OP_CAB = c.ID_OP_CAB " + "where ID_OF_CAB = @ID_OF_CAB and c.DATA_INI_M2 is not null " + "and (cast(c." + DATA_FIM + " as datetime) + cast(c." + HORA_FIM + " as datetime)) > @Data_fim " + " and (cast(c." + DATA_INI + "  as datetime) + cast(c." + HORA_INI + " as datetime)) <= @Data_inicio " + "END " + "SET @COUNT= @COUNT+1 " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime))), " + "@Data_fim2 = (select MIN((cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)))  " + "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  " + "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio " + "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID " + "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 " + "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID_RESULTADO)\t " + "set @COUNT = 1\t " + "IF(@Data_fim2 is not null) SET @COUNT = @TOTAL " + "END " + "IF not EXISTS (SELECT  FROM RP_OF_PARA_LIN where  +" + "(cast( " + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) < @Data_fim and + (cast( " + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) > @Data_inicio and  ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and ID_UTZ_CRIA <> @ID_UTZ_CRIA) AND   @COUNT <> @TOTAL " + "BEGIN SET @COUNT= 1 END" + " FETCH NEXT " + "FROM @getid2 INTO @ID2 " + "END " + "END " + "ELSE " + "BEGIN " + "IF(@COUNT = @TOTAL) " + "BEGIN " + "SELECT TOP 1  @Data_fim = MIN((cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime))), " + "@Data_fim2 = (select MIN((cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)))  " + "from  RP_OF_OP_FUNC where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) >= @Data_inicio and (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)) <= @Data_fim) " + "FROM  RP_OF_PARA_LIN  " + "where ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) and  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) <= @Data_fim " + "AND (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)) > @Data_inicio " + "IF(@ID_RESULTADO is null) SET @ID_RESULTADO=@ID " + "IF(@Data_fim2 is not null) SET @Data_fim=@Data_fim2 " + "insert into @parents (Data_inicio,Data_fim,ID) values (@Data_inicio,@Data_fim,@ID_RESULTADO)\t " + "set @COUNT = 1\t " + "END " + "SET @COUNT1= @COUNT1+1 " + "END " + "END " + "FETCH NEXT " + "FROM @getid INTO @ID " + "set @COUNT = 1\t " + "END " + "IF @ESTADO = 'P' BEGIN select (cast((DATEDIFF(second, (cast(" + DATA_INI + " as datetime) + cast(" + HORA_INI + " as datetime)),  (cast(" + DATA_FIM + " as datetime) + cast(" + HORA_FIM + " as datetime)))/3600.00) as decimal(18,4)) - " + "(select  COALESCE(SUM( cast((DATEDIFF(second,a.Data_inicio, a.Data_fim)/3600.00) as decimal(18,4))),0) from @parents a inner join RP_OF_PARA_LIN b on a.ID = b.ID_PARA_LIN ) ),'' as dfs " + "from RP_OF_PREP_LIN where ID_OP_CAB in (select TOP 1 ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) END ELSE BEGIN " + "select (cast((DATEDIFF(second, (cast(a." + DATA_INI + " as datetime) + cast(a." + HORA_INI + " as datetime)),  (cast(a." + DATA_FIM + " as datetime) + cast(a." + HORA_FIM + " as datetime)))/3600.00) as decimal(18,4)) - " + "( select COALESCE( SUM(  cast((DATEDIFF(second,a.Data_inicio, a.Data_fim)/3600.00) as decimal(18,4))),0) from @parents a inner join RP_OF_PARA_LIN b on a.ID = b.ID_PARA_LIN " + ") ) - COALESCE((cast((DATEDIFF(second, (cast(b." + DATA_INI + " as datetime) + cast(b." + HORA_INI + " as datetime)),  (cast(b." + DATA_FIM + " as datetime) + cast(b." + HORA_FIM + " as datetime)))/3600.00) as decimal(18,4))),0) " + ",'' as df from RP_OF_OP_FUNC a left join RP_OF_PREP_LIN b on  a.ID_OP_CAB = b.ID_OP_CAB where a.ID_OP_CAB in (select TOP 1 ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB  = @ID_OF_CAB) END");
	List<Object[]> dados2 = query2.getResultList();
	Integer count = 0;
	Object[] content2;
	for(Iterator var14 = dados2.iterator(); var14.hasNext(); number = content2[0] != null ? Double.parseDouble(content2[0].toString()) : 0.0) {
	content2 = (Object[])var14.next();
	count = count + 1;
	}
	if (number < 0.0) {
	number = 0.0;
	}
	return number;
	}
	public void criar_ficheiro_Pausa(String data, String path2, Integer count, Boolean ficheirosdownload, String nomeficheiro, String nomezip) throws IOException {
	if (!ficheirosdownload) {
	File file2 = new File(path2 + "_" + count + ".txt");
	try {
	file2.createNewFile();
	} catch (IOException var61) {
	String[] keyValuePairs = new String[]{"TEXTO_ERRO ::" + var61.getMessage() + " " + file2.getAbsolutePath()};
	if (file2.getAbsolutePath() != null) {
	this.verficaEventos(keyValuePairs, "ERROS REGISTOS PRODUCAO", "");
	}
	var61.printStackTrace();
	}
	BufferedWriter bw2 = null;
	FileWriter fw2 = null;
	try {
	fw2 = new FileWriter(file2.getAbsoluteFile(), true);
	} catch (IOException var55) {
	var55.printStackTrace();
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
	} catch (IOException var60) {
	if (bw2 != null) {
	try {
	bw2.close();
	} catch (IOException var54) {
	var54.printStackTrace();
	}
	}
	if (fw2 != null) {
	try {
	fw2.close();
	} catch (IOException var53) {
	var53.printStackTrace();
	}
	}
	var60.printStackTrace();
	}
	} else {
	Map<String, String> env = new HashMap();
	env.put("create", "true");
	java.nio.file.Path pathh = Paths.get("c:/sgiid/temp_files/" + nomezip + ".zip");
	URI uri = URI.create("jar:" + pathh.toUri());
	Throwable var10 = null;
	Object var11 = null;
	try {
	FileSystem fs = FileSystems.newFileSystem(uri, env);
	try {
	java.nio.file.Path nf = fs.getPath(nomeficheiro + "_" + count + ".txt");
	Throwable var14 = null;
	Object var15 = null;
	try {
	Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
	try {
	writer.write(data);
	} finally {
	if (writer != null) {
	writer.close();
	}
	}
	} catch (Throwable var57) {
	if (var14 == null) {
	var14 = var57;
	} else if (var14 != var57) {
	var14.addSuppressed(var57);
	}
	throw var14;
	}
	} finally {
	if (fs != null) {
	fs.close();
	}
	}
	} catch (Throwable var59) {
	if (var10 == null) {
	var10 = var59;
	} else if (var10 != var59) {
	var10.addSuppressed(var59);
	}
	throw var10;
	}
	}
	}
	public String sequencia() {
	String sequencia = "000000000";
	Query query_seq = this.entityManager.createNativeQuery("select top 1 NUMERO_SEQUENCIA,DATA_SEQUENCIA from GER_SEQUENCIA_FICHEIRO where DATA_SEQUENCIA = CONVERT (date, GETDATE())");
	List<Object[]> dados_seq = query_seq.getResultList();
	if (dados_seq.size() > 0) {
	Integer val = 1;
	for(Iterator var6 = dados_seq.iterator(); var6.hasNext(); sequencia = ("000000000" + val).substring(("000000000" + val).length() - 9, ("000000000" + val).length())) {
	Object[] contentseq = (Object[])var6.next();
	val = Integer.parseInt(contentseq[0].toString()) + 1;
	}
	this.entityManager.createNativeQuery("UPDATE GER_SEQUENCIA_FICHEIRO SET NUMERO_SEQUENCIA = " + val + " where DATA_SEQUENCIA = CONVERT (date, GETDATE())").executeUpdate();
	} else {
	sequencia = "000000001";
	this.entityManager.createNativeQuery("INSERT INTO GER_SEQUENCIA_FICHEIRO (DATA_SEQUENCIA,NUMERO_SEQUENCIA) VALUES (GETDATE(),1)").executeUpdate();
	}
	return sequencia;
	}
	@POST
	@Path("/sendemail")
	@Consumes({"
	"})
	@Produces({"application/json"})
	public List<HashMap<String, String>> verficaEventos(List<HashMap<String, String>> data) {
	HashMap<String, String> firstMap = (HashMap)data.get(0);
	String value = (String)firstMap.get("DADOS");
	value = value.substring(1, value.length() - 1);
	String[] keyValuePairs = value.split(",");
	Query query3 = this.entityManager.createQuery("Select a from GER_EVENTOS_CONF a where MODULO = " + (String)firstMap.get("MODULO") + " and MOMENTO = '" + (String)firstMap.get("MOMENTO") + "' " + "and PAGINA = '" + (String)firstMap.get("PAGINA") + "' and ESTADO = " + (String)firstMap.get("ESTADO"));
	List<GER_EVENTOS_CONF> dados = query3.getResultList();
	Iterator var8 = dados.iterator();
	while(var8.hasNext()) {
	GER_EVENTOS_CONF borderTypes = (GER_EVENTOS_CONF)var8.next();
	EMAIL email = new EMAIL();
	email.setDE("alertas.it.doureca@gmail.com");
	email.setPARA(borderTypes.getEMAIL_PARA());
	String mensagem = borderTypes.getEMAIL_MENSAGEM();
	String assunto = borderTypes.getEMAIL_ASSUNTO();
	String[] var15 = keyValuePairs;
	int var14 = keyValuePairs.length;
	for(int var13 = 0; var13 < var14; ++var13) {
	String pair = var15[var13];
	String[] entry = pair.split("::");
	mensagem = mensagem.replace("{" + entry[0].trim() + "}", entry[1].trim());
	assunto = assunto.replace("{" + entry[0].trim() + "}", entry.length > 1 ? entry[1].trim() : "");
	}
	email.setASSUNTO(assunto);
	email.setMENSAGEM(mensagem);
	this.sendemail(email);
	}
	return data;
	}
	public Object getvalues(String campo, Object dd) {
	try {
	try {
	return dd.getClass().getDeclaredField(campo).get(dd);
	} catch (IllegalArgumentException var4) {
	var4.printStackTrace();
	} catch (IllegalAccessException var5) {
	var5.printStackTrace();
	}
	} catch (NoSuchFieldException var6) {
	var6.printStackTrace();
	} catch (SecurityException var7) {
	var7.printStackTrace();
	}
	return null;
	}
	public void atualizatabela_AUX(String RESCOD, String DATDEB, String PROREF, String OFNUM, String OPECOD, Integer ID_OF_CAB, String TIPO, String HEUDEB) {
	this.entityManager.createNativeQuery("BEGIN IF NOT EXISTS  ( SELECT  FROM RP_AUX_OPNUM WHERE RESCOD = " + RESCOD + " and DATDEB = '" + DATDEB + "'
	and HEUDEB = '" + HEUDEB + "'
	Decompiled with FernFlower version 232.10203.10.
*/