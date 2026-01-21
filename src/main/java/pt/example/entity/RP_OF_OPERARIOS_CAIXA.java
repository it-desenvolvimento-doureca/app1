package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_OF_OPERARIOS_CAIXA")
public class RP_OF_OPERARIOS_CAIXA {

	private Integer ID_OPERARIO_CAIXA;
	private Integer ID_OF_CAB;
	private String ID_UTZ;
	private String NOME_UTZ;
	private Timestamp DATA_HORA_CRIA;

	@Id
	@Column(name = "ID_OPERARIO_CAIXA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_OPERARIO_CAIXA() {
		return ID_OPERARIO_CAIXA;
	}

	public void setID_OPERARIO_CAIXA(Integer iD_OPERARIO_CAIXA) {
		ID_OPERARIO_CAIXA = iD_OPERARIO_CAIXA;
	}

	@Column(name = "ID_OF_CAB")
	public Integer getID_OF_CAB() {
		return ID_OF_CAB;
	}

	public void setID_OF_CAB(Integer iD_OF_CAB) {
		ID_OF_CAB = iD_OF_CAB;
	}

	@Column(name = "ID_UTZ")
	public String getID_UTZ() {
		return ID_UTZ;
	}

	public void setID_UTZ(String iD_UTZ) {
		ID_UTZ = iD_UTZ;
	}

	@Column(name = "NOME_UTZ")
	public String getNOME_UTZ() {
		return NOME_UTZ;
	}

	public void setNOME_UTZ(String nOME_UTZ) {
		NOME_UTZ = nOME_UTZ;
	}

	@Column(name = "DATA_HORA_CRIA")
	public Timestamp getDATA_HORA_CRIA() {
		return DATA_HORA_CRIA;
	}

	public void setDATA_HORA_CRIA(Timestamp dATA_HORA_CRIA) {
		DATA_HORA_CRIA = dATA_HORA_CRIA;
	}

}
