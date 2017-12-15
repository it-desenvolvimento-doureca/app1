package pt.example.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_OP_FUNC;

public class RP_OF_OP_FUNCDao extends GenericDaoJpaImpl<RP_OF_OP_FUNC, Integer>
		implements GenericDao<RP_OF_OP_FUNC, Integer> {
	public RP_OF_OP_FUNCDao() {
		super(RP_OF_OP_FUNC.class);
	}

	public List<RP_OF_OP_FUNC> getbyid(Integer id_of_cab, String user, ArrayList<String> estado) {

		Query query = entityManager.createQuery("Select a,b,c from RP_OF_OP_FUNC a,RP_OF_CAB b,RP_OF_OP_CAB c "
				+ "where c.ID_OF_CAB = b.ID_OF_CAB and c.ID_OP_CAB = a.ID_OP_CAB and "
				+ "a.ID_OP_CAB  in (select e.ID_OP_CAB from RP_OF_CAB d, RP_OF_OP_CAB e  "
				+ "where (d.ID_OF_CAB =:id or d.ID_OF_CAB_ORIGEM = :id) and e.ID_OF_CAB = d.ID_OF_CAB) "
				+ "and c.ID_OF_CAB in (select d.ID_OF_CAB from RP_OF_CAB d where d.ID_OF_CAB_ORIGEM = :id or d.ID_OF_CAB=:id) and  "
				+ "a.ID_UTZ_CRIA = :user and b.ESTADO NOT IN (:estado) and a.ESTADO NOT IN (:estado) order by b.ID_OF_CAB_ORIGEM,b.ID_OF_CAB");
		query.setParameter("id", id_of_cab);
		query.setParameter("user", user);
		query.setParameter("estado", estado);
		List<RP_OF_OP_FUNC> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_OP_FUNC> checkuser(String user) {

		Query query = entityManager.createQuery("Select a,b from RP_OF_OP_FUNC a,RP_OF_OP_CAB b, RP_OF_CAB c "
				+ "where a.ID_OP_CAB = b.ID_OP_CAB and b.ID_OF_CAB = c.ID_OF_CAB and c.ID_OF_CAB_ORIGEM IS NULL and "
				+ "a.ID_UTZ_CRIA = :user and a.ESTADO NOT IN ('C','A','M')");

		query.setParameter("user", user);
		List<RP_OF_OP_FUNC> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_OP_FUNC> getbyallID_OP_CAB(Integer id, String user) {

		Query query = entityManager.createQuery(
				"Select a,c from RP_OF_OP_FUNC a, RP_OF_OP_CAB b,RP_OF_CAB c where a.ID_OP_CAB = b.ID_OP_CAB and b.ID_OF_CAB = c.ID_OF_CAB and a.ID_OP_CAB = :id and a.ID_UTZ_CRIA = :user ");
		query.setParameter("id", id);
		query.setParameter("user", user);
		List<RP_OF_OP_FUNC> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_OP_FUNC> getUsers(Integer id) {

		Query query = entityManager.createQuery(
				"select a from RP_OF_OP_FUNC a, RP_OF_OP_CAB b, RP_OF_CAB c where c.ID_OF_CAB = :id and b.ID_OF_CAB = c.ID_OF_CAB and b.ID_OP_CAB = a.ID_OP_CAB and a.ESTADO NOT IN ('C','A','M')");
		query.setParameter("id", id);
		List<RP_OF_OP_FUNC> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_OP_FUNC> getallUsers(Integer id) {

		Query query = entityManager.createQuery(
				"select a from RP_OF_OP_FUNC a, RP_OF_OP_CAB b, RP_OF_CAB c where c.ID_OF_CAB = :id and b.ID_OF_CAB = c.ID_OF_CAB and b.ID_OP_CAB = a.ID_OP_CAB");
		query.setParameter("id", id);
		List<RP_OF_OP_FUNC> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_OP_FUNC> getUser(Integer id) {

		Query query = entityManager.createQuery("select a from RP_OF_OP_FUNC a  where a.ID_OP_FUNC = :id");
		query.setParameter("id", id);
		List<RP_OF_OP_FUNC> utz = query.getResultList();
		return utz;

	}

}
