package pt.example.dao;

import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_CONF_OP_RECUPERACAO_PECAS;

public class RP_CONF_OP_RECUPERACAO_PECASDao extends GenericDaoJpaImpl<RP_CONF_OP_RECUPERACAO_PECAS, Integer> implements GenericDao<RP_CONF_OP_RECUPERACAO_PECAS, Integer> {
	public RP_CONF_OP_RECUPERACAO_PECASDao() {
		super(RP_CONF_OP_RECUPERACAO_PECAS.class);
	}

	public List<RP_CONF_OP_RECUPERACAO_PECAS> getbyid(String id_OP_PRINC) {
 
		
		Query query = entityManager.createQuery(
				"Select a from RP_CONF_OP_RECUPERACAO_PECAS a where a.ID_OP_PRINC in (" + id_OP_PRINC + ") order by a.ID_OP_SEC");
		// query.setParameter("id", id_utz);
		List<RP_CONF_OP_RECUPERACAO_PECAS> utz = query.getResultList();
		return utz;

	}

	public List<RP_CONF_OP_RECUPERACAO_PECAS> getall() {

		Query query = entityManager.createQuery("Select a from RP_CONF_OP_RECUPERACAO_PECAS a order by a.ID");
		List<RP_CONF_OP_RECUPERACAO_PECAS> utz = query.getResultList();
		return utz;

	}

}
