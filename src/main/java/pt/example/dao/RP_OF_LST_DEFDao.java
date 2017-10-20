package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_LST_DEF;

public class RP_OF_LST_DEFDao extends GenericDaoJpaImpl<RP_OF_LST_DEF, Integer>
		implements GenericDao<RP_OF_LST_DEF, Integer> {
	public RP_OF_LST_DEFDao() {
		super(RP_OF_LST_DEF.class);
	}

	public List<RP_OF_LST_DEF> getbyid(Integer id_utz) {

		Query query = entityManager.createQuery("Select a from RP_OF_LST_DEF a where a.ID_LST_DEF = :id ");
		query.setParameter("id", id_utz);
		List<RP_OF_LST_DEF> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_LST_DEF> getdef(Integer id_utz) {

		Query query = entityManager.createQuery("Select a from RP_OF_LST_DEF a where a.ID_OP_LIN = :id ");
		query.setParameter("id", id_utz);
		List<RP_OF_LST_DEF> utz = query.getResultList();
		return utz;

	}

	public int apagar_idop_lin(Integer id) {

		Query query = entityManager.createQuery("DELETE FROM RP_OF_LST_DEF a where a.ID_OP_LIN = :id ");
		query.setParameter("id", id);
		int utz = query.executeUpdate();
		return utz;

	}

	public List<RP_OF_LST_DEF> getall() {

		Query query = entityManager.createQuery("Select a from RP_OF_LST_DEF a ");
		List<RP_OF_LST_DEF> utz = query.getResultList();
		return utz;

	}

}
