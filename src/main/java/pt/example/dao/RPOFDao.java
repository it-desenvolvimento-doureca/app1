package pt.example.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_CAB;

public class RPOFDao extends GenericDaoJpaImpl<RP_OF_CAB, Integer> implements GenericDao<RP_OF_CAB, Integer> {
	public RPOFDao() {
		super(RP_OF_CAB.class);
	}

	public List<RP_OF_CAB> getall(String data, Integer start) {
		String querydata = "and a.SEC_NUM in (" + data + ")";
		if (data.equals("ADMIN")) {
			querydata = "";
		}

		Query query = entityManager.createQuery("Select a,b,c, "
				+ "(select count(d.ID_EVENTO) from GER_EVENTO d where d.ID_ORIGEM = a.ID_OF_CAB and d.CAMPO_ORIGEM= 'ID_OF_CAB' and d.ESTADO != 'A') as count_messages, "
				+ "(select count(d.ID_EVENTO) from GER_EVENTO d where d.ID_ORIGEM = a.ID_OF_CAB and d.CAMPO_ORIGEM= 'ID_OF_CAB' and d.ESTADO = 'L') as estado "
				+ "from RP_OF_CAB a, RP_OF_OP_CAB b,RP_OF_OP_FUNC c "
				+ "where a.ID_UTZ_CRIA = c.ID_UTZ_CRIA and c.ID_OP_CAB=b.ID_OP_CAB and a.ID_OF_CAB = b.ID_OF_CAB and a.ID_OF_CAB_ORIGEM = null "
				+ querydata + " order by c.DATA_INI_M2 desc, c.HORA_INI_M2 desc ");
		query.setFirstResult(start);
		query.setMaxResults(26);
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_CAB> getall2(List<HashMap<String, String>> data, Integer start) {
		HashMap<String, String> firstMap = data.get(0);
		String SEC_NUM = firstMap.get("sec_num").toString();
		
		String querydata = "and xa.SEC_NUM in (" + SEC_NUM + ")";
		if (SEC_NUM.equals("ADMIN")) {
			querydata = "";
		}
		String querydataw = "(select xa.ID_OF_CAB FROM RP_OF_CAB xa, RP_OF_OP_CAB xb,RP_OF_OP_FUNC xc where xa.ID_UTZ_CRIA = xc.ID_UTZ_CRIA and xc.ID_OP_CAB=xb.ID_OP_CAB and xa.ID_OF_CAB = xb.ID_OF_CAB and ID_OF_CAB_ORIGEM is null "
				+ querydata + "  order by  xc.DATA_INI_M2 DESC,xc.HORA_INI_M2 DESC OFFSET  " + start
				+ " ROWS  FETCH NEXT 25 ROWS ONLY ) ";
		Query query = entityManager.createNativeQuery(
				"SELECT CASE  WHEN  a.ID_OF_CAB_ORIGEM is not null THEN a.ID_OF_CAB_ORIGEM ELSE a.ID_OF_CAB END as idofacab, b.ID_OP_CAB,c.ID_OP_LIN,d.ID_OP_FUNC,"
						+ "a.OF_NUM,a.OP_COD_ORIGEM,a.OP_DES,a.MAQ_NUM,a.MAQ_DES, d.ID_UTZ_CRIA,d.NOME_UTZ_CRIA,d.DATA_INI_M2,d.HORA_INI_M2,d.DATA_FIM_M2,d.HORA_FIM_M2,b.TEMPO_EXEC_TOTAL_M2,"
						+ "c.REF_NUM,c.REF_DES,c.QUANT_OF,c.QUANT_BOAS_TOTAL_M2,c.QUANT_DEF_TOTAL_M2,c.PERC_DEF,c.PERC_OBJETIV,d.ESTADO,a.ID_UTZ_CRIA as idcria, "
						+ "(select count(xx.ID_EVENTO) from GER_EVENTO xx where xx.ID_ORIGEM = a.ID_OF_CAB and xx.CAMPO_ORIGEM= 'ID_OF_CAB' and xx.ESTADO != 'A') as count_messages, "
						+ "(select count(xx.ID_EVENTO) from GER_EVENTO xx where xx.ID_ORIGEM = a.ID_OF_CAB and xx.CAMPO_ORIGEM= 'ID_OF_CAB' and xx.ESTADO = 'L') as estados "
						+ ",(select CAST((cast(xc.DATA_INI_M2 as datetime) + cast(xc.HORA_INI_M2 as datetime)) AS DATETIME) as dd FROM RP_OF_CAB xa, RP_OF_OP_CAB xb,RP_OF_OP_FUNC xc where xa.ID_UTZ_CRIA = xc.ID_UTZ_CRIA and xc.ID_OP_CAB=xb.ID_OP_CAB and xa.ID_OF_CAB = xb.ID_OF_CAB and ID_OF_CAB_ORIGEM is null and xa.ID_OF_CAB = (CASE WHEN  a.ID_OF_CAB_ORIGEM is not null THEN a.ID_OF_CAB_ORIGEM ELSE a.ID_OF_CAB END)) as dataorder,a.OP_NUM "
						+ "FROM RP_OF_CAB a LEFT JOIN RP_OF_OP_CAB b on a.ID_OF_CAB = b.ID_OF_CAB "
						+ "LEFT JOIN RP_OF_OP_LIN c on b.ID_OP_CAB = c.ID_OP_CAB "
						+ "LEFT JOIN RP_OF_OP_FUNC d on b.ID_OP_CAB = d.ID_OP_CAB " + "where a.ID_OF_CAB in "
						+ querydataw + " " + "OR a.ID_OF_CAB_ORIGEM in " + querydataw + " "
						+ " ORDER BY dataorder DESC,idofacab desc,ID_OF_CAB_ORIGEM,b.ID_OP_CAB,d.DATA_INI_M2 ASC,d.HORA_INI_M2 ASC");
		/*
		 * query.setFirstResult(start); query.setMaxResults(26);
		 */
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_CAB> getalllist(List<HashMap<String, String>> data) {
		HashMap<String, String> firstMap = data.get(0);
		String data2 = firstMap.get("fam").toString();
		String date;
		String date2;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		date = (firstMap.get("date1") == null ? null : firstMap.get("date1").toString());

		date2 = (firstMap.get("date2") == null ? null : firstMap.get("date2").toString());

		Query query = entityManager.createNativeQuery("DECLARE @columns NVARCHAR(MAX), @sql NVARCHAR(MAX); "
				+ "SET @columns = N''; " + "SELECT @columns += N', p.' + QUOTENAME(COD_DEF) "
				+ "FROM (select distinct COD_DEF from RP_OF_LST_DEF " + "where LEFT(COD_DEF,2) in (" + data2
				+ ")) AS x; " + "SET @sql = N' "
				+ "SELECT OF_NUM,ID_OF_CAB, ID_OF_CAB_ORIGEM,OF_NUM_ORIGEM,ID_OP_LIN,ID_REF_ETIQUETA,REF_ETIQUETA,NOME_UTZ_CRIA,REF_NUM,REF_DES,data,OP_COD_ORIGEM,OP_DES,hora,data2, ' + STUFF(@columns, 1, 2, '') + ' "
				+ "FROM " + "(select " + "f.QUANT_DEF_M2,f.COD_DEF, "
				+ "a.OF_NUM,a.ID_OF_CAB, a.ID_OF_CAB_ORIGEM,e.OF_NUM_ORIGEM,c.ID_OP_LIN,e.ID_REF_ETIQUETA,e.REF_ETIQUETA,a.NOME_UTZ_CRIA,c.REF_NUM,c.REF_DES,g.DATA_INI_M2 as data, a.OP_COD_ORIGEM,a.OP_DES,g.HORA_INI_M2 as hora, a.DATA_HORA_CRIA as data2 "
				+ "from RP_OF_CAB a " + "inner join RP_OF_OP_CAB b on a.ID_OF_CAB = b.ID_OF_CAB "
				+ "inner join RP_OF_OP_LIN c on b.ID_OP_CAB = c.ID_OP_CAB "
				+ "left join RP_OF_OP_ETIQUETA e on c.ID_OP_LIN = e.ID_OP_LIN "
				+ "left join RP_OF_OP_FUNC g on g.ID_OP_CAB = (select top 1 x.ID_OP_CAB from RP_OF_OP_CAB x where x.ID_OF_CAB =a.ID_OF_CAB) "
				+ "left join RP_OF_DEF_LIN f on c.ID_OP_LIN = f.ID_OP_LIN " + "where ((not''" + firstMap.get("op_cod")
				+ "'' != ''null'') or (a.OP_COD_ORIGEM =''" + firstMap.get("op_cod") + "'')) and " + "((not''" + date
				+ "'' != ''null'') or (g.DATA_INI_M2 >=''" + date + "'')) and ((not ''" + date2
				+ "'' != ''null'') or (g.DATA_INI_M2 <=''" + date2 + "'')) and " + "((not ''" + firstMap.get("user")
				+ "'' != ''null'') or (a.NOME_UTZ_CRIA like ''%" + firstMap.get("user") + "%'')) and " + "((not ''"
				+ firstMap.get("ref") + "'' != ''null'') or (c.REF_NUM like ''%" + firstMap.get("ref") + "%'')) "
				+ ") AS j " + "PIVOT " + "( "
				+ "SUM(QUANT_DEF_M2) FOR COD_DEF IN (' + STUFF(REPLACE(@columns, ', p.[', ',['), 1, 1, '') + ') ) AS p  order by p.data,hora;'; "
				+ "PRINT @sql; " + "EXEC sp_executesql @sql;");
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_CAB> getallbyid(Integer id) {

		Query query = entityManager.createQuery("Select b,c from RP_OF_CAB a, RP_OF_OP_CAB b,RP_OF_OP_FUNC c "
				+ "where b.ID_OF_CAB = :id and c.ID_OP_CAB=b.ID_OP_CAB and a.ID_OF_CAB = b.ID_OF_CAB");
		query.setParameter("id", id);
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_CAB> getbyid(String id_utz) {

		Query query = entityManager.createQuery(
				"Select a from RP_OF_CAB a where a.ID_UTZ_CRIA = :id and a.ESTADO NOT IN ('C','A','M','R')");
		query.setParameter("id", id_utz);
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_CAB> getof(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_OF_CAB a where a.ID_OF_CAB = :id");
		query.setParameter("id", id);
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_CAB> verifica(String of_num, String op_cod, String op_num, String user) {

		Query query2 = entityManager.createNativeQuery("UPDATE RP_OF_CAB set ESTADO = 'C' "
				+ "where 0 = ( select count(*) from RP_OF_CAB a "
				+ "inner join RP_OF_OP_CAB b on a.ID_OF_CAB = b.ID_OF_CAB "
				+ "inner join RP_OF_OP_FUNC c on b.ID_OP_CAB = c.ID_OP_CAB and a.ID_UTZ_CRIA = c.ID_UTZ_CRIA "
				+ "where c.ESTADO NOT IN ('C','A','M','R') "
				+ "and a.ID_OF_CAB = (Select a.ID_OF_CAB from RP_OF_CAB a where a.OF_NUM = '" + of_num
				+ "' and a.OP_NUM = " + op_num + " " + "and a.OP_COD = '" + op_cod
				+ "' and a.ESTADO NOT IN ('C','A','M','R'))) "
				+ "and ID_OF_CAB = (Select a.ID_OF_CAB from RP_OF_CAB a where a.OF_NUM = '" + of_num + "' "
				+ "and a.OP_NUM = " + op_num + " and a.OP_COD = '" + op_cod + "' and a.ESTADO NOT IN ('C','A','M','R'))");

		query2.executeUpdate();

		Query query = entityManager.createQuery("Select a,(select count(*) from RP_OF_OP_FUNC where ID_UTZ_CRIA ='"
				+ user
				+ "' AND ESTADO in ('P','E','S') ) from RP_OF_CAB a where a.OF_NUM = :of_num and a.OP_NUM = :op_num and a.OP_COD = :op_cod and a.ESTADO NOT IN ('C','A','M','R')");
		query.setParameter("of_num", of_num);
		query.setParameter("op_cod", op_cod);
		query.setParameter("op_num", op_num);
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public String datas(String dt) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/*
		 * Calendar c = Calendar.getInstance(); try { c.setTime(sdf1.parse(dt));
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * //c.add(Calendar.DATE, 1); // number of days to add
		 */
		try {
			dt = sdf.format(sdf1.parse(dt));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;
	}

	public List<RP_OF_CAB> pesquisa_avancada(List<HashMap<String, String>> dados, Integer start) throws ParseException {
		HashMap<String, String> firstMap = dados.get(0);
		String date1;
		String date2;
		String date3;
		String date4;
		String squery;
		String squeryetiqueta = " ";
		String ordenacao = "order by c.DATA_INI_M2 desc, c.HORA_INI_M2 desc";
		// System.out.println(firstMap.get("ordenacao"));

		date1 = (firstMap.get("date1") != null) ? datas(firstMap.get("date1")) : null; // Data
																						// In�cio
																						// >=
		date2 = (firstMap.get("date2") != null) ? datas(firstMap.get("date2")) : null;// Data
																						// Fim
																						// >=
		date3 = (firstMap.get("date3") != null) ? datas(firstMap.get("date3")) : null;// <=
																						// Data
																						// In�cio
		date4 = (firstMap.get("date4") != null) ? datas(firstMap.get("date4")) : null;// <=
																						// Data
																						// Fim

		if (firstMap.get("ordenacao") != null) {
			ordenacao = "order by " + firstMap.get("ordenacao");
		}

		String querydata = "and a.SEC_NUM in (" + firstMap.get("sec_num") + ")";
		if (firstMap.get("sec_num").equals("ADMIN")) {
			querydata = "";
		}
		String estado = "null";
		if (firstMap.get("estado") != null) {
			estado = "C";
		}

		if (firstMap.get("etiqueta") != null) {
			squeryetiqueta = "and a.ID_OF_CAB in( select "
					+ "CASE WHEN sss.ID_OF_CAB_ORIGEM = null THEN ff.ID_OF_CAB  ELSE sss.ID_OF_CAB_ORIGEM END "
					+ "from RP_OF_OP_CAB ff,RP_OF_OP_LIN cc, RP_OF_CAB sss,RP_OF_OP_ETIQUETA et  where ff.ID_OP_CAB = cc.ID_OP_CAB and ff.ID_OF_CAB = sss.ID_OF_CAB  and et.ID_OP_LIN = cc.ID_OP_LIN and "
					+ "((not '" + firstMap.get("etiqueta") + "' != 'null') or (et.REF_ETIQUETA like '"
					+ firstMap.get("etiqueta") + "'))   	) ";
		}

		Query query = entityManager.createQuery("select a,b,c, "
				+ "(select count(d.ID_EVENTO) from GER_EVENTO d where d.ID_ORIGEM = a.ID_OF_CAB and d.CAMPO_ORIGEM= 'ID_OF_CAB' and d.ESTADO != 'A') as count_messages, "
				+ "(select count(d.ID_EVENTO) from GER_EVENTO d where d.ID_ORIGEM = a.ID_OF_CAB and d.CAMPO_ORIGEM= 'ID_OF_CAB' and d.ESTADO = 'L') as estado "
				+ "from RP_OF_CAB a, RP_OF_OP_CAB b,RP_OF_OP_FUNC c "
				+ "where a.ID_UTZ_CRIA = c.ID_UTZ_CRIA and c.ID_OP_CAB=b.ID_OP_CAB and a.ID_OF_CAB = b.ID_OF_CAB and"
				+ "	a.ID_OF_CAB in( " + "	select "
				+ "CASE WHEN ss.ID_OF_CAB_ORIGEM = null THEN f.ID_OF_CAB  ELSE ss.ID_OF_CAB_ORIGEM END "
				+ "from RP_OF_OP_CAB f,RP_OF_OP_LIN c, RP_OF_CAB ss  where f.ID_OP_CAB = c.ID_OP_CAB and f.ID_OF_CAB = ss.ID_OF_CAB and  "
				+ "			(((not :design_ref != null) or (c.REF_DES like :design_ref)) and "
				+ "			((not :ref != null) or (c.REF_NUM like :ref)) and " + "			((not '"
				+ firstMap.get("qtt") + "' != 'null') or (c.QUANT_BOAS_TOTAL_M2 >=  '" + firstMap.get("qtt")
				+ "')) and ((not '" + firstMap.get("qttmenor") + "' != 'null') or (c.QUANT_BOAS_TOTAL_M2 <= '"
				+ firstMap.get("qttmenor") + "'))) " + "	) " + squeryetiqueta + "and a.ID_OF_CAB in("
				+ "select h.ID_OF_CAB from RP_OF_OP_CAB h,RP_OF_OP_FUNC e where h.ID_OP_CAB = e.ID_OP_CAB and "
				+ "((not '" + date3 + "' != 'null') or (e.DATA_INI_M2 <= '" + date3 + "')) and ((not '" + date1
				+ "' !=  'null') or (e.DATA_INI_M2 >= '" + date1 + "')) and " + "((not '" + date4
				+ "' != 'null') or (e.DATA_FIM_M2 <= '" + date4 + "')) and ((not '" + date2
				+ "' != 'null') or (e.DATA_FIM_M2 >= '" + date2 + "')) and " + "((not '" + estado
				+ "' != 'null') or (e.ESTADO in (" + firstMap.get("estado") + "))) and " + "((not '"
				+ firstMap.get("tempo_prod_maior") + "' != 'null') or (h.TEMPO_EXEC_TOTAL_M2 >=  '"
				+ firstMap.get("tempo_prod_maior") + "')) and ((not '" + firstMap.get("tempo_prod_menor")
				+ "' != 'null') or (h.TEMPO_EXEC_TOTAL_M2 <= '" + firstMap.get("tempo_prod_menor") + "')) and "
				+ "((not '" + firstMap.get("hora4") + "' != 'null') or (e.HORA_FIM_M2 <= '" + firstMap.get("hora4")
				+ "')) and ((not '" + firstMap.get("hora2") + "' != 'null') or (e.HORA_FIM_M2 >= '"
				+ firstMap.get("hora2") + "')) and " + "((not '" + firstMap.get("hora3")
				+ "' != 'null') or (e.HORA_INI_M2 <= '" + firstMap.get("hora3") + "')) and ((not '"
				+ firstMap.get("hora1") + "' != 'null') or (e.HORA_INI_M2 >= '" + firstMap.get("hora1") + "')) and "
				+ "((not :func != null) or (e.NOME_UTZ_CRIA like :func) or (e.ID_UTZ_CRIA like :func))) and ((not :ordem != null) or (a.OF_NUM like :ordem)) and "
				+ "((not :operacao != null) or (a.OP_COD_ORIGEM like :operacao)) and ((not :maquina != null) or (a.MAQ_NUM like :maquina)) and"
				+ " a.ID_OF_CAB_ORIGEM = null " + querydata + " " + ordenacao + "");

		// query.setParameter("estado", '%'+firstMap.get("estado")+'%');
		squery = (firstMap.get("func") == null ? null : '%' + firstMap.get("func") + '%');
		query.setParameter("func", squery);

		squery = (firstMap.get("ref") == null ? null : '%' + firstMap.get("ref") + '%');
		query.setParameter("ref", squery);

		squery = (firstMap.get("design_ref") == null ? null : '%' + firstMap.get("design_ref") + '%');
		query.setParameter("design_ref", squery);

		squery = (firstMap.get("ordem") == null ? null : '%' + firstMap.get("ordem") + '%');
		query.setParameter("ordem", squery);

		squery = (firstMap.get("operacao") == null ? null : '%' + firstMap.get("operacao") + '%');
		query.setParameter("operacao", squery);

		squery = (firstMap.get("maquina") == null ? null : '%' + firstMap.get("maquina") + '%');
		query.setParameter("maquina", squery);

		query.setFirstResult(start);
		query.setMaxResults(26);

		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public HashMap<String, String> updateEstados(List<HashMap<String, String>> dados) {
		HashMap<String, String> firstMap = dados.get(0);

		String quer = "Update RP_OF_OP_FUNC SET ESTADO = '" + firstMap.get("ESTADO") + "', " + "ID_UTZ_MODIF = '"
				+ firstMap.get("ID_UTZ_MODIF") + "', DATA_HORA_MODIF = '" + firstMap.get("DATA_HORA_MODIF")
				+ "', PERFIL_MODIF = '" + firstMap.get("PERFIL_MODIF") + "', " + "NOME_UTZ_MODIF = '"
				+ firstMap.get("NOME_UTZ_MODIF") + "' " + "from RP_OF_OP_FUNC a, RP_OF_OP_CAB b,RP_OF_CAB c  "
				+ "where a.ID_OP_CAB = b.ID_OP_CAB and b.ID_OF_CAB = c.ID_OF_CAB and b.ID_OP_CAB in (select ID_OP_CAB from RP_OF_OP_CAB where ID_OF_CAB = "
				+ firstMap.get("ID_OF_CAB") + ")";

		Query query = entityManager.createNativeQuery(quer);
		query.executeUpdate();

		String quer2 = "Update RP_OF_CAB SET ESTADO = '" + firstMap.get("ESTADO") + "', " + "ID_UTZ_MODIF = '"
				+ firstMap.get("ID_UTZ_MODIF") + "', DATA_HORA_MODIF = '" + firstMap.get("DATA_HORA_MODIF")
				+ "', VERSAO_MODIF = '" + firstMap.get("VERSAO_MODIF") + "', " + "NOME_UTZ_MODIF = '"
				+ firstMap.get("NOME_UTZ_MODIF") + "' " + "where ID_OF_CAB = " + firstMap.get("ID_OF_CAB")
				+ " or ID_OF_CAB_ORIGEM = " + firstMap.get("ID_OF_CAB") + " ";
		Query query2 = entityManager.createNativeQuery(quer2);
		query2.executeUpdate();
		return null;

	}

}
