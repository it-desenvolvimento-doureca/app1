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

		Query query = entityManager.createNativeQuery("Select a.ID, a.REFERENCIA,a.LOTE,a.QTD,a.ESTADO,a.NUMERO_IMPRESSOES from RP_GESTAO_ETIQUETAS_MATRIX a "
				+ "where ISNULL(a.ESTADO,'') != 'P' and LTRIM(RTRIM(a.LOTE)) like :lote order by a.DATA_CRIA desc");
		query.setParameter("lote", "%" + (Lote == null ? "" : Lote.trim()) + "%");

		List<RP_GESTAO_ETIQUETAS_MATRIX> data = query.getResultList();
		return data;

	}
	
	/* Todas as etiquetas (o serviço já as envia para a NiceLabel); pendentes primeiro, depois as mais recentes */
	public List<RP_GESTAO_ETIQUETAS_MATRIX> getall() {

		Query query = entityManager.createNativeQuery(
				"select top 500 a.ID, a.REFERENCIA,a.LOTE,a.QTD,a.ESTADO,a.NUMERO_IMPRESSOES,a.DATA_CRIA from RP_GESTAO_ETIQUETAS_MATRIX a "
						+ "order by case when a.ESTADO = 'P' then 0 else 1 end, a.DATA_CRIA desc");
		List<RP_GESTAO_ETIQUETAS_MATRIX> data = query.getResultList();
		return data;

	}

	/*
	 * Linhas da carga do WinRobot (PR_WINROBOT_CAB.NUMERO_IDENTICACAO_CARGA = lote), agrupadas por referência. Só postos
	 * de carga das linhas 1, 2.0 (velha) e 2 (LINHA 1, 2, 3; a 4 é a pintura) e sem trabalhos anulados ou cancelados.
	 */
	public List<Object[]> getcarga(String lote) {

		Query query = entityManager.createNativeQuery(
				"select a.COD_REF, max(a.DESIGN_REF) as DESIGN_REF, sum(isnull(a.NUMERO_PECAS,0)) as QTD, "
						+ "(select count(*) from RP_GESTAO_ETIQUETAS_MATRIX m where LTRIM(RTRIM(m.LOTE)) = :lote and m.REFERENCIA = a.COD_REF) as JA_EXISTEM "
						+ "from PR_WINROBOT_CAB c inner join PR_WINROBOT_ARTICLES a on a.ID_CAB = c.ID "
						+ "where LTRIM(RTRIM(c.NUMERO_IDENTICACAO_CARGA)) = :lote and c.TIPO_POSTO = 'CARGA' and c.LINHA in (1, 2, 3) "
						+ "and ISNULL(c.ESTADO,'') not in ('A', 'CANC') group by a.COD_REF order by a.COD_REF");
		query.setParameter("lote", lote == null ? "" : lote.trim());
		List<Object[]> data = query.getResultList();
		return data;

	}

}
