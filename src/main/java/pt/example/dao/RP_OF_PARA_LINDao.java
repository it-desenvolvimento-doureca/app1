package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_PARA_LIN;

public class RP_OF_PARA_LINDao extends GenericDaoJpaImpl<RP_OF_PARA_LIN, Integer>
		implements GenericDao<RP_OF_PARA_LIN, Integer> {
	public RP_OF_PARA_LINDao() {
		super(RP_OF_PARA_LIN.class);
	}

	public List<RP_OF_PARA_LIN> getbyid(Integer id) {

		Query query = entityManager
				.createQuery("Select a from RP_OF_PARA_LIN a where a.ID_PARA_LIN = :id and a.ESTADO NOT IN ('C') ");
		query.setParameter("id", id);
		List<RP_OF_PARA_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_PARA_LIN> getbyid2(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_OF_PARA_LIN a where a.ID_PARA_LIN = :id");
		query.setParameter("id", id);
		List<RP_OF_PARA_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_PARA_LIN> getbyallID_OP_CAB(Integer id) {

		Query query = entityManager
				.createQuery("Select a from RP_OF_PARA_LIN a where a.ID_OP_CAB = :id and a.ESTADO ='C' ");
		query.setParameter("id", id);
		List<RP_OF_PARA_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_PARA_LIN> getbyallUser(Integer id, String user) {

		Query query = entityManager
				.createQuery("Select a from RP_OF_PARA_LIN a where a.ID_OP_CAB = :id and a.ID_UTZ_CRIA = :user ");
		query.setParameter("id", id);
		query.setParameter("user", user);
		List<RP_OF_PARA_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_PARA_LIN> getbyallUserIDOFCAB(Integer id, String user) {

		Query query = entityManager.createNativeQuery(
				"select TIPO_PARAGEM_M2 from RP_OF_PARA_LIN WHERE ID_OP_CAB in ( " + "select a.ID_OP_CAB from RP_OF_OP_CAB  a "
						+ "inner join RP_OF_OP_FUNC b on a.ID_OP_CAB = b.ID_OP_CAB and b.ID_UTZ_CRIA = '" + user + "' "
						+ "where ID_OF_CAB = " + id + ")  ");

		List<RP_OF_PARA_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_PARA_LIN> getbyid_op_cab(Integer id) {

		Query query = entityManager
				.createQuery("Select a from RP_OF_PARA_LIN a where a.ID_OP_CAB = :id and a.ESTADO NOT IN ('C')");
		query.setParameter("id", id);
		List<RP_OF_PARA_LIN> utz = query.getResultList();
		return utz;

	}

}
