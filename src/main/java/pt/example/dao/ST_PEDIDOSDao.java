package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.ST_PEDIDOS;

public class ST_PEDIDOSDao extends GenericDaoJpaImpl<ST_PEDIDOS, Integer> implements GenericDao<ST_PEDIDOS, Integer> {
	public ST_PEDIDOSDao() {
		super(ST_PEDIDOS.class);
	}

	public List<ST_PEDIDOS> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from ST_PEDIDOS a where a.ID_PEDIDO = :id");
		query.setParameter("id", id);
		List<ST_PEDIDOS> data = query.getResultList();
		return data;

	}

	public List<ST_PEDIDOS> getall() {

		Query query = entityManager.createQuery("Select a from ST_PEDIDOS a ");
		List<ST_PEDIDOS> data = query.getResultList();
		return data;

	}

}
