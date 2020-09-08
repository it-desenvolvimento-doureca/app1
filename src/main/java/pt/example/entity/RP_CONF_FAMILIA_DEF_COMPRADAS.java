package pt.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "RP_CONF_FAMILIA_DEF_COMPRADAS")
public class RP_CONF_FAMILIA_DEF_COMPRADAS {

	private String COD_FAMILIA_DEF;
	private String NOME_FAMILIA_DEF;
	
	@Id
	@Column(name = "COD_FAMILIA_DEF")
	public String getCOD_FAMILIA_DEF() {
		return COD_FAMILIA_DEF;
	}

	public void setCOD_FAMILIA_DEF(String cOD_FAMILIA_DEF) {
		COD_FAMILIA_DEF = cOD_FAMILIA_DEF;
	}

	@Column(name = "NOME_FAMILIA_DEF")
	public String getNOME_FAMILIA_DEF() {
		return NOME_FAMILIA_DEF;
	}

	public void setNOME_FAMILIA_DEF(String nOME_FAMILIA_DEF) {
		NOME_FAMILIA_DEF = nOME_FAMILIA_DEF;
	}

}
