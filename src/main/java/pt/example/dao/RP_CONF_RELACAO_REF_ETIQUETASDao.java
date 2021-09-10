package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_CONF_RELACAO_REF_ETIQUETAS;

public class RP_CONF_RELACAO_REF_ETIQUETASDao extends GenericDaoJpaImpl<RP_CONF_RELACAO_REF_ETIQUETAS, Integer>
		implements GenericDao<RP_CONF_RELACAO_REF_ETIQUETAS, Integer> {
	public RP_CONF_RELACAO_REF_ETIQUETASDao() {
		super(RP_CONF_RELACAO_REF_ETIQUETAS.class);
	}

	public List<RP_CONF_RELACAO_REF_ETIQUETAS> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_CONF_RELACAO_REF_ETIQUETAS a where a.ID  = :id ");
		query.setParameter("id", id);
		List<RP_CONF_RELACAO_REF_ETIQUETAS> data = query.getResultList();
		return data;

	}

	public List<RP_CONF_RELACAO_REF_ETIQUETAS> getall() {
		Query query = entityManager.createQuery(
				"select  a from RP_CONF_RELACAO_REF_ETIQUETAS a ");
		List<RP_CONF_RELACAO_REF_ETIQUETAS> data = query.getResultList();
		return data;

	}

}
