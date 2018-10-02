package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_OF_LST_DEF")
public class RP_OF_LST_DEF {

	private Integer ID_LST_DEF;
	private Integer ID_OP_LIN;
	private String COD_DEF;
	private String DESC_DEF;
	private Timestamp DATA_HORA_MODIF;
	private String ID_UTZ_CRIA;

	@Id
	@Column(name = "ID_LST_DEF")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_LST_DEF() {
		return ID_LST_DEF;
	}

	@Column(name = "ID_OP_LIN")
	public Integer getID_OP_LIN() {
		return ID_OP_LIN;
	}

	@Column(name = "COD_DEF")
	public String getCOD_DEF() {
		return COD_DEF;
	}

	@Column(name = "DESC_DEF")
	public String getDESC_DEF() {
		return DESC_DEF;
	}

	@Column(name = "DATA_HORA_MODIF")
	public Timestamp getDATA_HORA_MODIF() {
		return DATA_HORA_MODIF;
	}

	@Column(name = "ID_UTZ_CRIA")
	public String getID_UTZ_CRIA() {
		return ID_UTZ_CRIA;
	}

	public void setID_LST_DEF(Integer iD_LST_DEF) {
		ID_LST_DEF = iD_LST_DEF;
	}

	public void setID_OP_LIN(Integer iD_OP_LIN) {
		ID_OP_LIN = iD_OP_LIN;
	}

	public void setCOD_DEF(String cOD_DEF) {
		COD_DEF = cOD_DEF;
	}

	public void setDESC_DEF(String dESC_DEF) {
		DESC_DEF = dESC_DEF;
	}

	public void setDATA_HORA_MODIF(Timestamp dATA_HORA_MODIF) {
		DATA_HORA_MODIF = dATA_HORA_MODIF;
	}

	public void setID_UTZ_CRIA(String iD_UTZ_CRIA) {
		ID_UTZ_CRIA = iD_UTZ_CRIA;
	}
}
