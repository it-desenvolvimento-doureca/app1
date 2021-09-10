package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_GESTAO_ETIQUETAS_MATRIX;

public class RP_GESTAO_ETIQUETAS_MATRIXDao extends GenericDaoJpaImpl<RP_GESTAO_ETIQUETAS_MATRIX, Integer>
		implements GenericDao<RP_GESTAO_ETIQUETAS_MATRIX, Integer> {
	public RP_GESTAO_ETIQUETAS_MATRIXDao() {
		super(RP_GESTAO_ETIQUETAS_MATRIX.class);
	}

	public List<RP_GESTAO_ETIQUETAS_MATRIX> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_GESTAO_ETIQUETAS_MATRIX a where a.ID  = :id ");
		query.setParameter("id", id);
		List<RP_GESTAO_ETIQUETAS_MATRIX> data = query.getResultList();
		return data;

	}

	public List<RP_GESTAO_ETIQUETAS_MATRIX> getbylote_referencia(String Lote,String referencia) {

		Query query = entityManager.createNativeQuery("Select a.ID, a.REFERENCIA,a.LOTE,a.QTD,a.ESTADO,a.NUMERO_IMPRESSOES from RP_GESTAO_ETIQUETAS_MATRIX a where a.ESTADO != 'P' and a.LOTE  = '"+Lote+"' /*and a.REFERENCIA  = '"+referencia+"'*/ ");
		 
		List<RP_GESTAO_ETIQUETAS_MATRIX> data = query.getResultList();
		return data;

	}
	
	public List<RP_GESTAO_ETIQUETAS_MATRIX> getall() {

		Query query = entityManager.createNativeQuery(
				"select  a.ID, a.REFERENCIA,a.LOTE,a.QTD,a.ESTADO,a.NUMERO_IMPRESSOES from RP_GESTAO_ETIQUETAS_MATRIX a WHERE a.ESTADO = 'P' order by a.DATA_CRIA asc");
		List<RP_GESTAO_ETIQUETAS_MATRIX> data = query.getResultList();
		return data;

	}

}
