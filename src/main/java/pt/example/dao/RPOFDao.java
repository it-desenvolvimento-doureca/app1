package pt.example.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_CAB;

public class RPOFDao extends GenericDaoJpaImpl<RP_OF_CAB, Integer> implements GenericDao<RP_OF_CAB, Integer> {
	public RPOFDao() {
		super(RP_OF_CAB.class);
	}

	public List<RP_OF_CAB> getall() {

		Query query = entityManager.createQuery("Select a,b,c from RP_OF_CAB a, RP_OF_OP_CAB b,RP_OF_OP_FUNC c "
				+ "where a.ID_UTZ_CRIA = c.ID_UTZ_CRIA and c.ID_OP_CAB=b.ID_OP_CAB and a.ID_OF_CAB = b.ID_OF_CAB and a.ID_OF_CAB_ORIGEM = null order by c.DATA_INI desc, c.HORA_INI desc ");
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

		Query query = entityManager
				.createQuery("Select a from RP_OF_CAB a where a.ID_UTZ_CRIA = :id and a.ESTADO NOT IN ('C','A','M')");
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

	public List<RP_OF_CAB> verifica(String of_num, String op_cod, String op_num) {

		Query query = entityManager.createQuery(
				"Select a from RP_OF_CAB a where a.OF_NUM = :of_num and a.OP_NUM = :op_num and a.OP_COD = :op_cod and a.ESTADO NOT IN ('C','A','M')");
		query.setParameter("of_num", of_num);
		query.setParameter("op_cod", op_cod);
		query.setParameter("op_num", op_num);
		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_CAB> pesquisa_avancada(List<HashMap<String, String>> dados) throws ParseException {
		HashMap<String, String> firstMap = dados.get(0);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		String squery;

		Query query = entityManager.createQuery("select a,b,c from RP_OF_CAB a, RP_OF_OP_CAB b,RP_OF_OP_FUNC c "
				+ "where a.ID_UTZ_CRIA = c.ID_UTZ_CRIA and c.ID_OP_CAB=b.ID_OP_CAB and a.ID_OF_CAB = b.ID_OF_CAB and"
				+ "	a.ID_OF_CAB in( "
				+ "	select f.ID_OF_CAB from RP_OF_OP_CAB f,RP_OF_OP_LIN c where f.ID_OP_CAB = c.ID_OP_CAB and  "
				+ "						(((not :design_ref != null) or (c.REF_DES like :design_ref)) and "
				+ "						((not :ref != null) or (c.REF_NUM like :ref)) )" + "	) "
				+ "and a.ID_OF_CAB in("
				+ "select h.ID_OF_CAB from RP_OF_OP_CAB h,RP_OF_OP_FUNC e where h.ID_OP_CAB = e.ID_OP_CAB and "
				+ "((not :date3 != null) or (e.DATA_INI <= :date3)) and ((not :date1 !=  null) or (e.DATA_INI >= :date1)) and "
				+ "((not :date4 != null) or (e.DATA_FIM <= :date4)) and ((not :date2 != null) or (e.DATA_FIM >= :date2)) and "
				+ "((not '" + firstMap.get("hora4") + "' != 'null') or (e.HORA_FIM <= '" + firstMap.get("hora4")
				+ "')) and ((not '" + firstMap.get("hora2") + "' != 'null') or (e.HORA_FIM >= '" + firstMap.get("hora2")
				+ "')) and " + "((not '" + firstMap.get("hora3") + "' != 'null') or (e.HORA_INI <= '"
				+ firstMap.get("hora3") + "')) and ((not '" + firstMap.get("hora1") + "' != 'null') or (e.HORA_INI >= '"
				+ firstMap.get("hora1") + "')) and "
				+ "((not :func != null) or (e.NOME_UTZ_CRIA like :func))) and ((not :ordem != null) or (a.OF_NUM like :ordem)) and "
				+ "((not :operacao != null) or (a.OP_NUM like :operacao)) and ((not :maquina != null) or (a.MAQ_NUM like :maquina)) and"
				+ " a.ID_OF_CAB_ORIGEM = null  order by c.DATA_INI desc, c.HORA_INI desc");

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

		date = (firstMap.get("date1") == null ? null : formatter.parse(firstMap.get("date1")));
		query.setParameter("date1", date); // Data Início >=

		date = (firstMap.get("date3") == null ? null : formatter.parse(firstMap.get("date3")));
		query.setParameter("date3", date); // <= Data Início

		date = (firstMap.get("date2") == null ? null : formatter.parse(firstMap.get("date2")));
		query.setParameter("date2", date); // Data Fim >=

		date = (firstMap.get("date4") == null ? null : formatter.parse(firstMap.get("date4")));
		query.setParameter("date4", date); // <= Data Fim

		List<RP_OF_CAB> utz = query.getResultList();
		return utz;

	}

}
