package pt.example.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import pt.example.entity.RP_CONF_OP;

public class RP_CONF_OPDao extends GenericDaoJpaImpl<RP_CONF_OP, Integer> implements GenericDao<RP_CONF_OP, Integer> {
	public RP_CONF_OPDao() {
		super(RP_CONF_OP.class);
	}

	public List<RP_CONF_OP> getbyid(String id_OP_PRINC) {

		// ID_OP_PRINC é varchar. Em vez de concatenar os valores crus no IN(...)
		// (o que partia com valores não-numéricos -> "Invalid column name 'TC'"
		// e abria HQL injection), separa o CSV e vincula uma lista de strings.
		List<String> ids = new ArrayList<>();
		if (id_OP_PRINC != null) {
			for (String s : id_OP_PRINC.split(",")) {
				s = s.replace("'", "").trim();
				if (!s.isEmpty()) {
					ids.add(s);
				}
			}
		}
		if (ids.isEmpty()) {
			return new ArrayList<>();
		}

		Query query = entityManager.createQuery(
				"Select a from RP_CONF_OP a where a.ID_OP_PRINC in (:ids) order by a.ID_OP_SEC");
		query.setParameter("ids", ids);
		return query.getResultList();

	}

	public List<RP_CONF_OP> getall() {

		Query query = entityManager.createQuery("Select a from RP_CONF_OP a order by a.ID_OP_PRINC");
		List<RP_CONF_OP> utz = query.getResultList();
		return utz;

	}

}
