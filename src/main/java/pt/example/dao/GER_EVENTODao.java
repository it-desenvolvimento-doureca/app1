package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.GER_EVENTO;
import pt.example.entity.GER_EVENTO;

public class GER_EVENTODao extends GenericDaoJpaImpl<GER_EVENTO, Integer> implements GenericDao<GER_EVENTO, Integer> {
	public GER_EVENTODao() {
		super(GER_EVENTO.class);
	}

	public List<GER_EVENTO> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from GER_EVENTO a where a.ID_EVENTO = :id");
		query.setParameter("id", id);
		List<GER_EVENTO> data = query.getResultList();
		return data;

	}

	public List<GER_EVENTO> getbyidOrigem(Integer id,String campo) {

		Query query = entityManager.createQuery("Select a from GER_EVENTO a where a.ID_ORIGEM = :id and CAMPO_ORIGEM= :campo order by DATA_HORA_CRIA desc");
		query.setParameter("id", id);
		query.setParameter("campo", campo);
		List<GER_EVENTO> data = query.getResultList();
		return data;

	}

	public List<GER_EVENTO> getall() {

		Query query = entityManager.createQuery("Select a from GER_EVENTO a ");
		List<GER_EVENTO> data = query.getResultList();
		return data;

	}

}
