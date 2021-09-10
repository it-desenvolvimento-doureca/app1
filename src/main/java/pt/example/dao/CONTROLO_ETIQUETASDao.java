package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.CONTROLO_ETIQUETAS;

public class CONTROLO_ETIQUETASDao extends GenericDaoJpaImpl<CONTROLO_ETIQUETAS, Integer>
		implements GenericDao<CONTROLO_ETIQUETAS, Integer> {
	public CONTROLO_ETIQUETASDao() {
		super(CONTROLO_ETIQUETAS.class);
	}

	public List<CONTROLO_ETIQUETAS> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from CONTROLO_ETIQUETAS a where a.ID  = :id ");
		query.setParameter("id", id);
		List<CONTROLO_ETIQUETAS> data = query.getResultList();
		return data;

	}

	public List<CONTROLO_ETIQUETAS> getall() {

		Query query = entityManager.createNativeQuery(
				"select top 10 a.ID, a.ETIQUETA,a.TOTAL,a.DATA_CRIA,a.DATA_MODIF,a.ETIQUETA_CAIXA from CONTROLO_ETIQUETAS a where ESTADO != 'R' order by a.DATA_MODIF desc");
		List<CONTROLO_ETIQUETAS> data = query.getResultList();
		return data;

	}

	public List<CONTROLO_ETIQUETAS> getallbyCAIXA(String caixa) {

		Query query = entityManager
				.createQuery("select a from CONTROLO_ETIQUETAS a where ESTADO != 'R' and a.ETIQUETA_CAIXA = '" + caixa
						+ "' order by a.DATA_MODIF desc");
		List<CONTROLO_ETIQUETAS> data = query.getResultList();
		return data;

	}

	public List<CONTROLO_ETIQUETAS> VerificaSeExiste(String etiqueta, String caixa) {

		Query query = entityManager.createNativeQuery(
				"select (select top 1 x.ETIQUETA_CAIXA from CONTROLO_ETIQUETAS x where x.ETIQUETA = '" + etiqueta
						+ "'  and ESTADO != 'R' order by DATA_MODIF desc) OUTRA_CAIXA, "
						+ "(Select a.ETIQUETA_CAIXA from CONTROLO_ETIQUETAS a where a.ETIQUETA = '" + etiqueta
						+ "' and a.ETIQUETA_CAIXA  = '" + caixa + "'  and ESTADO != 'R') CAIXA "
						+ ",(select SUM(x.TOTAL) from CONTROLO_ETIQUETAS x where x.ETIQUETA_CAIXA = '0010263147' and ESTADO != 'R')  TOTAL ");

		List<CONTROLO_ETIQUETAS> data = query.getResultList();
		return data;

	}

}
