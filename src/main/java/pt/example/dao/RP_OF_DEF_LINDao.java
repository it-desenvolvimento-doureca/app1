package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_DEF_LIN;
import pt.example.entity.RP_OF_OP_LIN;

public class RP_OF_DEF_LINDao extends GenericDaoJpaImpl<RP_OF_DEF_LIN, Integer>
		implements GenericDao<RP_OF_DEF_LIN, Integer> {
	public RP_OF_DEF_LINDao() {
		super(RP_OF_DEF_LIN.class);
	}

	public List<RP_OF_DEF_LIN> getbyid(String id, Integer id2, String id_ref) {
		Query query = null;
		if (id_ref.equals("null")) {

			query = entityManager.createNativeQuery(
					"select a.COD_DEF,a.DESC_DEF, a.QUANT_DEF,a.ID_DEF_LIN,a.OBS_DEF  from RP_OF_DEF_LIN a where ID_OP_LIN = :id2 and substring(a.COD_DEF, 1, 2) = :id and  ID_REF_ETIQUETA is null "
							+ "UNION select b.COD_DEF,b.DESC_DEF,'' QUANT_DEF,'' ID_DEF_LIN, '' OBS_DEF from RP_OF_LST_DEF b where "
							+ "b.ID_OP_LIN = :id2 and b.COD_DEF not in (select c.COD_DEF from RP_OF_DEF_LIN c where c.ID_OP_LIN=:id2 and ID_REF_ETIQUETA is null) and substring(b.COD_DEF, 1, 2) = :id ");
		} else {
			query = entityManager.createNativeQuery(
					"select a.COD_DEF,a.DESC_DEF, a.QUANT_DEF,a.ID_DEF_LIN,a.OBS_DEF from RP_OF_DEF_LIN a where ID_OP_LIN = :id2 and substring(a.COD_DEF, 1, 2) = :id and  ID_REF_ETIQUETA = "
							+ id_ref + " "
							+ "UNION select b.COD_DEF,b.DESC_DEF,'' QUANT_DEF,'' ID_DEF_LIN, '' OBS_DEF from RP_OF_LST_DEF b where "
							+ "b.ID_OP_LIN = :id2 and b.COD_DEF not in (select c.COD_DEF from RP_OF_DEF_LIN c where c.ID_OP_LIN=:id2 and ID_REF_ETIQUETA = "
							+ id_ref + ") and substring(b.COD_DEF, 1, 2) = :id ");
		}
		query.setParameter("id", id);
		query.setParameter("id2", id2);
		List<RP_OF_DEF_LIN> utz = query.getResultList();
		return utz;

	}
	
	public List<RP_OF_DEF_LIN> getbyidall(Integer id2, String id_ref) {
		Query query = null;
		if (id_ref.equals("null")) {

			query = entityManager.createNativeQuery(
					"select a.COD_DEF,a.DESC_DEF, a.QUANT_DEF,a.ID_DEF_LIN,a.OBS_DEF  from RP_OF_DEF_LIN a where ID_OP_LIN = :id2 and  ID_REF_ETIQUETA is null "
							+ "UNION select b.COD_DEF,b.DESC_DEF,'' QUANT_DEF,'' ID_DEF_LIN, '' OBS_DEF from RP_OF_LST_DEF b where "
							+ "b.ID_OP_LIN = :id2 and b.COD_DEF not in (select c.COD_DEF from RP_OF_DEF_LIN c where c.ID_OP_LIN=:id2 and ID_REF_ETIQUETA is null)");
		} else {
			query = entityManager.createNativeQuery(
					"select a.COD_DEF,a.DESC_DEF, a.QUANT_DEF,a.ID_DEF_LIN,a.OBS_DEF from RP_OF_DEF_LIN a where ID_OP_LIN = :id2 and  ID_REF_ETIQUETA = "
							+ id_ref + " "
							+ "UNION select b.COD_DEF,b.DESC_DEF,'' QUANT_DEF,'' ID_DEF_LIN, '' OBS_DEF from RP_OF_LST_DEF b where "
							+ "b.ID_OP_LIN = :id2 and b.COD_DEF not in (select c.COD_DEF from RP_OF_DEF_LIN c where c.ID_OP_LIN=:id2 and ID_REF_ETIQUETA = "
							+ id_ref + ") ");
		}

		query.setParameter("id2", id2);
		List<RP_OF_DEF_LIN> utz = query.getResultList();
		return utz;

	}
	
	public List<RP_OF_DEF_LIN> getbyid_op_lin(Integer id) {

		Query query = entityManager
				.createQuery("Select a from RP_OF_DEF_LIN a where ID_OP_LIN = :id order by COD_DEF asc");
		query.setParameter("id", id);
		List<RP_OF_DEF_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_DEF_LIN> getbyid_op_lin_id_etiqueta(Integer id, Integer id_etiq) {

		Query query = entityManager.createQuery(
				"Select a from RP_OF_DEF_LIN a where ID_OP_LIN = :id and ID_REF_ETIQUETA = :id_etiq order by COD_DEF asc");
		query.setParameter("id", id);
		query.setParameter("id_etiq", id_etiq);
		List<RP_OF_DEF_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_DEF_LIN> getbyidDEF(Integer id) {

		Query query = entityManager.createQuery("Select a from RP_OF_DEF_LIN a where a.ID_DEF_LIN = :id");
		query.setParameter("id", id);
		List<RP_OF_DEF_LIN> utz = query.getResultList();
		return utz;

	}

	public int apagar_id_op_lin(Integer id, Integer etiqueta) {

		Query query = entityManager
				.createQuery("DELETE FROM RP_OF_DEF_LIN a where a.ID_OP_LIN = :id and ID_REF_ETIQUETA = :etiqueta");
		query.setParameter("id", id);
		query.setParameter("etiqueta", etiqueta);
		int utz = query.executeUpdate();
		return utz;

	}

	public int apagar_id_DEF_LIN(Integer id) {

		Query query = entityManager.createQuery("DELETE FROM RP_OF_DEF_LIN a where a.ID_DEF_LIN = :id");
		query.setParameter("id", id);
		int utz = query.executeUpdate();
		return utz;

	}

	public RP_OF_DEF_LIN createupdate(RP_OF_DEF_LIN data) {

		Query query = entityManager.createQuery("select a from RP_OF_DEF_LIN a where a.ID_OP_LIN = "
				+ data.getID_OP_LIN() + " and a.COD_DEF=" + data.getCOD_DEF() + "and a.ID_REF_ETIQUETA=" + data.getID_REF_ETIQUETA() + "");
		// query.setParameter("id", id);

		List<RP_OF_DEF_LIN> utz2 = query.getResultList();
		System.out.println(utz2.size());
		if (utz2.size() > 0) {
			data.setID_DEF_LIN(utz2.get(0).getID_DEF_LIN());
			Query query2 = entityManager.createQuery("UPDATE RP_OF_DEF_LIN a set a.QUANT_DEF = " + data.getQUANT_DEF()
					+ ", a.ID_UTZ_MODIF=" + data.getID_UTZ_CRIA() + "  where a.ID_OP_LIN = " + data.getID_OP_LIN()
					+ " and a.COD_DEF=" + data.getCOD_DEF() + "");
			this.update(data);
			return data;
		} else {
			return this.create(data);
		}

	}

}
