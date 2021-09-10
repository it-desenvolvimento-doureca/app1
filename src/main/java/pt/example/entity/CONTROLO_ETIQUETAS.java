package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONTROLO_ETIQUETAS")
public class CONTROLO_ETIQUETAS {
	public Integer ID;
	public String ETIQUETA;
	public Integer TOTAL;
	public Timestamp DATA_CRIA;
	public Timestamp DATA_MODIF;
	public String UTZ_CRIA;
	public String UTZ_MODIF;
	public String ETIQUETA_CAIXA;
	public String REFERENCIA;
	public String ESTADO;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID() {
		return ID;
	}

	@Column(name = "ETIQUETA")
	public String getETIQUETA() {
		return ETIQUETA;
	}

	@Column(name = "TOTAL")
	public Integer getTOTAL() {
		return TOTAL;
	}

	@Column(name = "DATA_CRIA")
	public Timestamp getDATA_CRIA() {
		return DATA_CRIA;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setETIQUETA(String eTIQUETA) {
		ETIQUETA = eTIQUETA;
	}

	public void setTOTAL(Integer tOTAL) {
		TOTAL = tOTAL;
	}

	public void setDATA_CRIA(Timestamp dATA_CRIA) {
		DATA_CRIA = dATA_CRIA;
	}

	@Column(name = "DATA_MODIF")
	public Timestamp getDATA_MODIF() {
		return DATA_MODIF;
	}

	public void setDATA_MODIF(Timestamp dATA_MODIF) {
		DATA_MODIF = dATA_MODIF;
	}

	@Column(name = "UTZ_CRIA")
	public String getUTZ_CRIA() {
		return UTZ_CRIA;
	}

	@Column(name = "UTZ_MODIF")
	public String getUTZ_MODIF() {
		return UTZ_MODIF;
	}

	@Column(name = "ETIQUETA_CAIXA")
	public String getETIQUETA_CAIXA() {
		return ETIQUETA_CAIXA;
	}

	@Column(name = "REFERENCIA")
	public String getREFERENCIA() {
		return REFERENCIA;
	}

	public void setUTZ_CRIA(String uTZ_CRIA) {
		UTZ_CRIA = uTZ_CRIA;
	}

	public void setUTZ_MODIF(String uTZ_MODIF) {
		UTZ_MODIF = uTZ_MODIF;
	}

	public void setETIQUETA_CAIXA(String eTIQUETA_CAIXA) {
		ETIQUETA_CAIXA = eTIQUETA_CAIXA;
	}

	public void setREFERENCIA(String rEFERENCIA) {
		REFERENCIA = rEFERENCIA;
	}

	@Column(name = "ESTADO")
	public String getESTADO() {
		return ESTADO;
	}

	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}

}
