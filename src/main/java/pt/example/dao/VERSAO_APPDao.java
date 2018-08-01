package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_CONF_CHEF_SEC;
import pt.example.entity.RP_OF_CAB;
import pt.example.entity.VERSAO_APP;

public class VERSAO_APPDao extends GenericDaoJpaImpl<VERSAO_APP, Integer> implements GenericDao<VERSAO_APP, Integer> {
	public VERSAO_APPDao() {
		super(VERSAO_APP.class);
	}

	public List<VERSAO_APP> getallv() {

		Query query = entityManager.createQuery("Select a from VERSAO_APP a ");

		List<VERSAO_APP> utz = query.getResultList();
		return utz;

	}
}
