package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_CAIXAS_INCOMPLETAS")
public class RP_CAIXAS_INCOMPLETAS {

	public Integer ID_CAIXA_INCOMPLETA;
	public String ETQNUM;
	public String REF_NUM;
	public Integer QUANT_ETIQUETA;
	public String UNIDADE;
	public Timestamp DATA_CRIA;
	public Integer UTZ_CRIA;
	public Integer ID_OF_CAB;

	@Id
	@Column(name = "ID_CAIXA_INCOMPLETA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_CAIXA_INCOMPLETA() {
		return ID_CAIXA_INCOMPLETA;
	}

	@Column(name = "ETQNUM")
	public String getETQNUM() {
		return ETQNUM;
	}

	@Column(name = "REF_NUM")
	public String getREF_NUM() {
		return REF_NUM;
	}

	@Column(name = "QUANT_ETIQUETA")
	public Integer getQUANT_ETIQUETA() {
		return QUANT_ETIQUETA;
	}

	@Column(name = "UNIDADE")
	public String getUNIDADE() {
		return UNIDADE;
	}

	@Column(name = "DATA_CRIA")
	public Timestamp getDATA_CRIA() {
		return DATA_CRIA;
	}

	@Column(name = "UTZ_CRIA")
	public Integer getUTZ_CRIA() {
		return UTZ_CRIA;
	}

	@Column(name = "ID_OF_CAB")
	public Integer getID_OF_CAB() {
		return ID_OF_CAB;
	}

	public void setID_CAIXA_INCOMPLETA(Integer iD_CAIXA_INCOMPLETA) {
		ID_CAIXA_INCOMPLETA = iD_CAIXA_INCOMPLETA;
	}

	public void setETQNUM(String eTQNUM) {
		ETQNUM = eTQNUM;
	}

	public void setREF_NUM(String rEF_NUM) {
		REF_NUM = rEF_NUM;
	}

	public void setQUANT_ETIQUETA(Integer qUANT_ETIQUETA) {
		QUANT_ETIQUETA = qUANT_ETIQUETA;
	}

	public void setUNIDADE(String uNIDADE) {
		UNIDADE = uNIDADE;
	}

	public void setDATA_CRIA(Timestamp dATA_CRIA) {
		DATA_CRIA = dATA_CRIA;
	}

	public void setUTZ_CRIA(Integer uTZ_CRIA) {
		UTZ_CRIA = uTZ_CRIA;
	}

	public void setID_OF_CAB(Integer iD_OF_CAB) {
		ID_OF_CAB = iD_OF_CAB;
	}

}
