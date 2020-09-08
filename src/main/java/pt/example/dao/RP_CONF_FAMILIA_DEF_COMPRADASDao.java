package pt.example.dao;


import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_CONF_FAMILIA_DEF_COMPRADAS;

public class RP_CONF_FAMILIA_DEF_COMPRADASDao extends GenericDaoJpaImpl<RP_CONF_FAMILIA_DEF_COMPRADAS,Integer> implements GenericDao<RP_CONF_FAMILIA_DEF_COMPRADAS,Integer> {
	public RP_CONF_FAMILIA_DEF_COMPRADASDao() {
		super(RP_CONF_FAMILIA_DEF_COMPRADAS.class);
	}


	/*public List<RP_CONF_FAMILIA_DEF_COMPRADAS> getbycodfam(String codfam) {

		Query query = entityManager.createQuery("Select a from RP_CONF_FAMILIA_DEF_COMPRADAS a where a.COD_FAMILIA_COMP = :codfam");
		query.setParameter("codfam", codfam);
		List<RP_CONF_FAMILIA_DEF_COMPRADAS> utz = query.getResultList();
		return utz;

	}*/
}
