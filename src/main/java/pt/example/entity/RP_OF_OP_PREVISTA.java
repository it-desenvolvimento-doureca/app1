package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_OF_OP_PREVISTA")
public class RP_OF_OP_PREVISTA {

	private Integer ID;
	private String OF_NUM;
	private String OP_COD;
	private Timestamp DATA_CRIA;
	private Integer OP_NUM;
	private String UTZ_CRIA;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	@Column(name = "OF_NUM")
	public String getOF_NUM() {
		return OF_NUM;
	}

	public void setOF_NUM(String oF_NUM) {
		OF_NUM = oF_NUM;
	}

	@Column(name = "OP_COD")
	public String getOP_COD() {
		return OP_COD;
	}

	public void setOP_COD(String oP_COD) {
		OP_COD = oP_COD;
	}

	@Column(name = "DATA_CRIA")
	public Timestamp getDATA_CRIA() {
		return DATA_CRIA;
	}

	public void setDATA_CRIA(Timestamp dATA_CRIA) {
		DATA_CRIA = dATA_CRIA;
	}

	@Column(name = "OP_NUM")
	public Integer getOP_NUM() {
		return OP_NUM;
	}

	public void setOP_NUM(Integer oP_NUM) {
		OP_NUM = oP_NUM;
	}

	@Column(name = "UTZ_CRIA")
	public String getUTZ_CRIA() {
		return UTZ_CRIA;
	}

	public void setUTZ_CRIA(String uTZ_CRIA) {
		UTZ_CRIA = uTZ_CRIA;
	}

}
