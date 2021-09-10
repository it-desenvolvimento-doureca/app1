package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_CONF_RELACAO_REF_ETIQUETAS")
public class RP_CONF_RELACAO_REF_ETIQUETAS {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer ID;
	@Column(name = "REF_PRINCIPAL")
	private String REF_PRINCIPAL;
	@Column(name = "REF_IMPRIMIR")
	private String REF_IMPRIMIR;
	@Column(name = "DATA_CRIA")
	private Timestamp DATA_CRIA;
	@Column(name = "UTZ_CRIA")
	private String UTZ_CRIA;
	@Column(name = "DATA_MODIF")
	private Timestamp DATA_MODIF;
	@Column(name = "UTZ_MODIF")
	private String UTZ_MODIF;

	public Integer getID() {
		return ID;
	}

	public String getREF_PRINCIPAL() {
		return REF_PRINCIPAL;
	}

	public String getREF_IMPRIMIR() {
		return REF_IMPRIMIR;
	}

	public Timestamp getDATA_CRIA() {
		return DATA_CRIA;
	}

	public String getUTZ_CRIA() {
		return UTZ_CRIA;
	}

	public Timestamp getDATA_MODIF() {
		return DATA_MODIF;
	}

	public String getUTZ_MODIF() {
		return UTZ_MODIF;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setREF_PRINCIPAL(String rEF_PRINCIPAL) {
		REF_PRINCIPAL = rEF_PRINCIPAL;
	}

	public void setREF_IMPRIMIR(String rEF_IMPRIMIR) {
		REF_IMPRIMIR = rEF_IMPRIMIR;
	}

	public void setDATA_CRIA(Timestamp dATA_CRIA) {
		DATA_CRIA = dATA_CRIA;
	}

	public void setUTZ_CRIA(String uTZ_CRIA) {
		UTZ_CRIA = uTZ_CRIA;
	}

	public void setDATA_MODIF(Timestamp dATA_MODIF) {
		DATA_MODIF = dATA_MODIF;
	}

	public void setUTZ_MODIF(String uTZ_MODIF) {
		UTZ_MODIF = uTZ_MODIF;
	}

}
