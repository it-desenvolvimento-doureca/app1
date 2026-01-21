package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_OPERARIOS_CAIXA;

public class RP_OF_OPERARIOS_CAIXADao extends GenericDaoJpaImpl<RP_OF_OPERARIOS_CAIXA, Integer> implements GenericDao<RP_OF_OPERARIOS_CAIXA, Integer> {
	public RP_OF_OPERARIOS_CAIXADao() {
		super(RP_OF_OPERARIOS_CAIXA.class);
	}

	public List<RP_OF_OPERARIOS_CAIXA> getByIdOfCab(Integer idOfCab) {
		Query query = entityManager.createQuery("Select a from RP_OF_OPERARIOS_CAIXA a where a.ID_OF_CAB = :idOfCab order by a.DATA_HORA_CRIA");
		query.setParameter("idOfCab", idOfCab);
		List<RP_OF_OPERARIOS_CAIXA> data = query.getResultList();
		return data;
	}

	public Long countByIdOfCab(Integer idOfCab) {
		Query query = entityManager.createQuery("Select count(a) from RP_OF_OPERARIOS_CAIXA a where a.ID_OF_CAB = :idOfCab");
		query.setParameter("idOfCab", idOfCab);
		return (Long) query.getSingleResult();
	}

	public List<RP_OF_OPERARIOS_CAIXA> getByIdOfCabAndIdUtz(Integer idOfCab, String idUtz) {
		Query query = entityManager.createQuery("Select a from RP_OF_OPERARIOS_CAIXA a where a.ID_OF_CAB = :idOfCab and a.ID_UTZ = :idUtz");
		query.setParameter("idOfCab", idOfCab);
		query.setParameter("idUtz", idUtz);
		List<RP_OF_OPERARIOS_CAIXA> data = query.getResultList();
		return data;
	}

}
