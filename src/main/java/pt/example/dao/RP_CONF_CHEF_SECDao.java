package pt.example.dao;


import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_CONF_CHEF_SEC;
import pt.example.entity.RP_OF_CAB;

public class RP_CONF_CHEF_SECDao extends GenericDaoJpaImpl<RP_CONF_CHEF_SEC,Integer> implements GenericDao<RP_CONF_CHEF_SEC,Integer> {
	public RP_CONF_CHEF_SECDao() {
		super(RP_CONF_CHEF_SEC.class);
	}


	public List<RP_CONF_CHEF_SEC> getallbyid(String id) {

		Query query = entityManager.createQuery("Select a from RP_CONF_CHEF_SEC a where a.ID_UTZ = :id");
		query.setParameter("id", id);
		List<RP_CONF_CHEF_SEC> utz = query.getResultList();
		return utz;

	}
}
