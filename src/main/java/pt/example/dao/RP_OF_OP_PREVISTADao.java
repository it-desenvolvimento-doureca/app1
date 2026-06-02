package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_OP_PREVISTA;

public class RP_OF_OP_PREVISTADao extends GenericDaoJpaImpl<RP_OF_OP_PREVISTA, Integer>
		implements GenericDao<RP_OF_OP_PREVISTA, Integer> {

	public RP_OF_OP_PREVISTADao() {
		super(RP_OF_OP_PREVISTA.class);
	}

	public boolean existsByOfNumAndOpCod(String ofNum, String opCod) {
		Query query = entityManager.createQuery(
				"Select count(a) from RP_OF_OP_PREVISTA a where a.OF_NUM = :ofNum and a.OP_COD = :opCod");
		query.setParameter("ofNum", ofNum);
		query.setParameter("opCod", opCod);
		Long count = (Long) query.getSingleResult();
		return count > 0;
	}

	public List<RP_OF_OP_PREVISTA> findByOfNumAndOpCod(String ofNum, String opCod) {
		Query query = entityManager.createQuery(
				"Select a from RP_OF_OP_PREVISTA a where a.OF_NUM = :ofNum and a.OP_COD = :opCod");
		query.setParameter("ofNum", ofNum);
		query.setParameter("opCod", opCod);
		return query.getResultList();
	}

}
