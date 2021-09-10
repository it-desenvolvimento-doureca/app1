package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX;

public class RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIXDao extends GenericDaoJpaImpl<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX, Integer>
		implements GenericDao<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX, Integer> {
	public RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIXDao() {
		super(RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX.class);
	}

	public List<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX a where a.ID  = :id ");
		query.setParameter("id", id);
		List<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX> data = query.getResultList();
		return data;

	}

	public List<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX> getall() {
		Query query = entityManager.createQuery(
				"select  a.* from RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX a ");
		List<RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX> data = query.getResultList();
		return data;

	}

}
