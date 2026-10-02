package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_OF_OP_LIN;

public class RP_OF_OP_LINDao extends GenericDaoJpaImpl<RP_OF_OP_LIN, Integer>
		implements GenericDao<RP_OF_OP_LIN, Integer> {
	public RP_OF_OP_LINDao() {
		super(RP_OF_OP_LIN.class);
	}

	public List<RP_OF_OP_LIN> getbyid(Integer id) {

		String sql = "SELECT e.ID_OP_LIN, e.ID_OP_CAB, e.REF_NUM, e.REF_DES, e.QUANT_OF, e.QUANT_BOAS_TOTAL_M2, e.QUANT_DEF_TOTAL_M2, e.OBS_REF, e.PROQTEFMT, e.TIPO_PECA, g.ID_UTZ_CRIA, g.ID_OF_CAB_ORIGEM, g.OF_NUM, e.NCLQTE, e.CONFIRMAR_ZERO "
				+ "	 FROM RP_OF_OP_LIN e "
				+ "	 JOIN RP_OF_OP_CAB f ON f.ID_OP_CAB = e.ID_OP_CAB "
				+ "	 JOIN RP_OF_CAB g ON f.ID_OF_CAB = g.ID_OF_CAB "
				+ "	 WHERE e.ID_OP_CAB IN ( "
				+ "	     SELECT a.ID_OP_CAB "
				+ "	     FROM RP_OF_OP_CAB a "
				+ "	     WHERE a.ID_OF_CAB IN ( "
				+ "	         SELECT c.ID_OF_CAB "
				+ "	         FROM RP_OF_OP_CAB b "
				+ "	         JOIN RP_OF_CAB c ON b.ID_OF_CAB = c.ID_OF_CAB_ORIGEM "
				+ "	         WHERE b.ID_OP_CAB = :id "
				+ "	     ) "
				+ "	     UNION "
				+ "	     SELECT :id "
				+ "	     UNION "
				+ "	     SELECT r.ID_OP_CAB "
				+ "	     FROM RP_OF_OP_CAB r "
				+ "	     WHERE r.ID_OF_CAB IN ( "
				+ "	         SELECT x.ID_OF_CAB "
				+ "	         FROM RP_OF_OP_CAB x "
				+ "	         WHERE x.ID_OP_CAB = :id "
				+ "	     ) AND r.ID_OP_CAB != :id "
				+ "	 ) "
				+ "	 ORDER BY g.ID_OF_CAB_ORIGEM, e.ID_OP_LIN";

			Query query = entityManager.createNativeQuery(sql).setParameter("id", id);
			List<RP_OF_OP_LIN> utz = query.getResultList();
		return utz;

	}
	
	public List<RP_OF_OP_LIN> getbyidcontrolo(Integer id_utz) {

		// Mesmo filtro que o getbyid: os tres ramos em UNION em vez de OR. Com OR o SQL Server
		// nao usa os indices e le as tabelas todas; chamada varias vezes em paralelo, esta query
		// prendia ligacoes do pool SGIID ate o esgotar.
		// Continua a devolver entidades (e.*), o JSON do endpoint nao muda.
		String sql = "SELECT e.* "
				+ "	 FROM RP_OF_OP_LIN e "
				+ "	 JOIN RP_OF_OP_CAB f ON f.ID_OP_CAB = e.ID_OP_CAB "
				+ "	 JOIN RP_OF_CAB g ON f.ID_OF_CAB = g.ID_OF_CAB "
				+ "	 WHERE e.ID_OP_CAB IN ( "
				+ "	     SELECT a.ID_OP_CAB "
				+ "	     FROM RP_OF_OP_CAB a "
				+ "	     WHERE a.ID_OF_CAB IN ( "
				+ "	         SELECT c.ID_OF_CAB "
				+ "	         FROM RP_OF_OP_CAB b "
				+ "	         JOIN RP_OF_CAB c ON b.ID_OF_CAB = c.ID_OF_CAB_ORIGEM "
				+ "	         WHERE b.ID_OP_CAB = :id "
				+ "	     ) "
				+ "	     UNION "
				+ "	     SELECT :id "
				+ "	     UNION "
				+ "	     SELECT r.ID_OP_CAB "
				+ "	     FROM RP_OF_OP_CAB r "
				+ "	     WHERE r.ID_OF_CAB IN ( "
				+ "	         SELECT x.ID_OF_CAB "
				+ "	         FROM RP_OF_OP_CAB x "
				+ "	         WHERE x.ID_OP_CAB = :id "
				+ "	     ) AND r.ID_OP_CAB != :id "
				+ "	 ) "
				+ "	 ORDER BY g.ID_OF_CAB_ORIGEM, e.ID_OP_LIN";

		Query query = entityManager.createNativeQuery(sql, RP_OF_OP_LIN.class).setParameter("id", id_utz);
		List<RP_OF_OP_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_OP_LIN> getid(Integer id_utz) {

		Query query = entityManager.createQuery("Select a from RP_OF_OP_LIN a where a.ID_OP_LIN = :id");
		query.setParameter("id", id_utz);
		List<RP_OF_OP_LIN> utz = query.getResultList();
		return utz;

	}

	public List<RP_OF_OP_LIN> getallbyid(Integer id) {

		Query query = entityManager.createQuery(
				"Select b,g from RP_OF_OP_CAB a,RP_OF_OP_LIN b, RP_OF_CAB g  where a.ID_OF_CAB = g.ID_OF_CAB and ( "
				+ "g.ID_OF_CAB in (select c.ID_OF_CAB from  RP_OF_OP_CAB c where c.ID_OP_CAB = :id)  "
				+ " or g.ID_OF_CAB_ORIGEM  in (select c.ID_OF_CAB from  RP_OF_OP_CAB c where c.ID_OP_CAB = :id)) "
				+ "and b.ID_OP_CAB = a.ID_OP_CAB order by  g.ID_OF_CAB_ORIGEM,b.ID_OP_LIN");
		query.setParameter("id", id);
		List<RP_OF_OP_LIN> utz = query.getResultList();
		return utz;

	}

	public List<Object[]> getallbyid_fast(Integer id) {

		String sql = "SELECT b.QUANT_BOAS_TOTAL_M2, b.QUANT_DEF_TOTAL_M2, b.NCLQTE, b.QUANT_OF, "
				+ "b.TIPO_PECA, b.REF_NUM, b.REF_DES, b.CONFIRMAR_ZERO, g.ID_OF_CAB_ORIGEM "
				+ "FROM RP_OF_OP_LIN b "
				+ "JOIN RP_OF_OP_CAB a ON b.ID_OP_CAB = a.ID_OP_CAB "
				+ "JOIN RP_OF_CAB g ON a.ID_OF_CAB = g.ID_OF_CAB "
				+ "WHERE a.ID_OF_CAB IN ( "
				+ "    SELECT ID_OF_CAB FROM RP_OF_OP_CAB WHERE ID_OP_CAB = :id "
				+ "    UNION "
				+ "    SELECT g2.ID_OF_CAB FROM RP_OF_CAB g2 "
				+ "    WHERE g2.ID_OF_CAB_ORIGEM IN (SELECT ID_OF_CAB FROM RP_OF_OP_CAB WHERE ID_OP_CAB = :id) "
				+ ") "
				+ "ORDER BY g.ID_OF_CAB_ORIGEM, b.ID_OP_LIN";
		Query query = entityManager.createNativeQuery(sql).setParameter("id", id);
		return query.getResultList();

	}

	public List<RP_OF_OP_LIN> getop(Integer id) {

		Query query = entityManager.createQuery(
				"Select c from RP_OF_OP_CAB a,RP_OF_OP_LIN b,RP_OF_CAB c where b.ID_OP_LIN = :id and b.ID_OP_CAB = a.ID_OP_CAB and a.ID_OF_CAB = c.ID_OF_CAB");
		query.setParameter("id", id);
		List<RP_OF_OP_LIN> utz = query.getResultList();
		return utz;

	}

}
