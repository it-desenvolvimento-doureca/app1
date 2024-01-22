package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_OP_ETIQUETA;

public class RP_OF_OP_ETIQUETADao extends GenericDaoJpaImpl<RP_OF_OP_ETIQUETA, Integer>
		implements GenericDao<RP_OF_OP_ETIQUETA, Integer> {
	public RP_OF_OP_ETIQUETADao() {
		super(RP_OF_OP_ETIQUETA.class);
	}

	public List<RP_OF_OP_ETIQUETA> getbyid_etiqueta(Integer ID_REF_ETIQUETA, Integer ID_OP_LIN) {

		Query query = entityManager
				.createQuery("Select a from RP_OF_OP_ETIQUETA a where a.ID_REF_ETIQUETA = :id and a.ID_OP_LIN = :id2");
		query.setParameter("id", ID_REF_ETIQUETA);
		query.setParameter("id2", ID_OP_LIN);
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}

	public List<RP_OF_OP_ETIQUETA> verificaopnum(Integer id) {

		Query query = entityManager.createNativeQuery(
				"select COUNT(*) as etiq_total, COUNT(a.OP_NUM) opnum_total,(select COUNT(*) from RP_OF_CAB where ID_OF_CAB = "
						+ id + " and OP_NUM is null ) as totalof " + " from RP_OF_OP_ETIQUETA a "
						+ "left join RP_OF_OP_LIN b on a.ID_OP_LIN = b.ID_OP_LIN "
						+ "left join RP_OF_OP_CAB c on b.ID_OP_CAB = c.ID_OP_CAB "
						+ "left join RP_OF_CAB d on c.ID_OF_CAB = d.ID_OF_CAB where d.ID_OF_CAB_ORIGEM = " + id + " "
						+ "and ((a.QUANT_BOAS_M2 > 0 and a.QUANT_DEF_M2 = 0) or (a.QUANT_BOAS_M2 = 0 and a.QUANT_DEF_M2 > 0) or (a.QUANT_BOAS_M2 > 0 and a.QUANT_DEF_M2 > 0) )");
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}

	public List<RP_OF_OP_ETIQUETA> getbyid(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_OF_OP_ETIQUETA a where a.ID_REF_ETIQUETA = :id");
		query.setParameter("id", id);
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}

	public List<RP_OF_OP_ETIQUETA> getbyid_oplin(Integer id) {

		Query query = entityManager
				.createQuery("Select a from RP_OF_OP_ETIQUETA a where a.ID_OP_LIN = :id AND a.ATIVO = 1");
		query.setParameter("id", id);
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}

	public List<RP_OF_OP_ETIQUETA> getall() {

		Query query = entityManager.createQuery("Select a from RP_OF_OP_ETIQUETA a ");
		List<RP_OF_OP_ETIQUETA> data = query.getResultList();
		return data;

	}

}
