package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_CAIXAS_INCOMPLETAS;

public class RP_CAIXAS_INCOMPLETASDao extends GenericDaoJpaImpl<RP_CAIXAS_INCOMPLETAS, Integer> implements GenericDao<RP_CAIXAS_INCOMPLETAS, Integer> {
	public RP_CAIXAS_INCOMPLETASDao() {
		super(RP_CAIXAS_INCOMPLETAS.class);
	}

	public List<RP_CAIXAS_INCOMPLETAS> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_CAIXAS_INCOMPLETAS a where a.ID_CAIXA_INCOMPLETA = :id");
		query.setParameter("id", id);
		List<RP_CAIXAS_INCOMPLETAS> data = query.getResultList();
		return data;

	}
	
	public List<RP_CAIXAS_INCOMPLETAS> getbyid_of_cab(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_CAIXAS_INCOMPLETAS a where a.ID_OF_CAB = :id");
		query.setParameter("id", id);
		List<RP_CAIXAS_INCOMPLETAS> data = query.getResultList();
		return data;

	}

	public List<RP_CAIXAS_INCOMPLETAS> getall() {

		Query query = entityManager.createQuery("Select a from RP_CAIXAS_INCOMPLETAS a ");
		List<RP_CAIXAS_INCOMPLETAS> data = query.getResultList();
		return data;

	}

}
