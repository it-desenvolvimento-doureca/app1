package pt.example.dao;


import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_OP_ETIQUETA;

public class RP_OF_OP_ETIQUETADao extends GenericDaoJpaImpl<RP_OF_OP_ETIQUETA,Integer> implements GenericDao<RP_OF_OP_ETIQUETA,Integer> {
	public RP_OF_OP_ETIQUETADao() {
		super(RP_OF_OP_ETIQUETA.class);
	}
	
	
	public List<RP_OF_OP_ETIQUETA> getbyid_etiqueta(Integer ID_REF_ETIQUETA,Integer ID_OP_LIN) {

		Query query = entityManager.createQuery("Select a from RP_OF_OP_ETIQUETA a where a.ID_REF_ETIQUETA = :id and a.ID_OP_LIN = :id2");
		query.setParameter("id", ID_REF_ETIQUETA);
		query.setParameter("id2", ID_OP_LIN);
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}
	
	public List<RP_OF_OP_ETIQUETA> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_OF_OP_ETIQUETA a where a.ID_REF_ETIQUETA = :id");
		query.setParameter("id", id);
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}
		
	public List<RP_OF_OP_ETIQUETA> getbyid_oplin(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_OF_OP_ETIQUETA a where a.ID_OP_LIN = :id");
		query.setParameter("id", id);
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}
	
	public List<RP_OF_OP_ETIQUETA> getall(){

		Query query = entityManager.createQuery("Select a from RP_OF_OP_ETIQUETA a ");
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}

}
