package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_OF_DEF_LIN")
public class RP_OF_DEF_LIN {

	private Integer ID_DEF_LIN;
	private Integer ID_REF_ETIQUETA;
	private Integer ID_OP_LIN;
	private String ID_UTZ_CRIA;
	private String ID_UTZ_MODIF;
	private String OBS_DEF;
	private String COD_DEF;
	private String DESC_DEF;
	private String DESC_FAMILIA;
	private Integer QUANT_DEF;
	private Timestamp DATA_HORA_REG;
	private Integer QUANT_DEF_M1;
	private Integer QUANT_DEF_M2;
	private Integer VERSAO_MODIF;
	private Integer QUANT_DEF_ORIGINAL;

	@Id
	@Column(name = "ID_DEF_LIN")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_DEF_LIN() {
		return ID_DEF_LIN;
	}

	@Column(name = "ID_OP_LIN")
	public Integer getID_OP_LIN() {
		return ID_OP_LIN;
	}

	@Column(name = "ID_UTZ_CRIA")
	public String getID_UTZ_CRIA() {
		return ID_UTZ_CRIA;
	}

	@Column(name = "ID_UTZ_MODIF")
	public String getID_UTZ_MODIF() {
		return ID_UTZ_MODIF;
	}

	@Column(name = "COD_DEF")
	public String getCOD_DEF() {
		return COD_DEF;
	}

	@Column(name = "DESC_DEF")
	public String getDESC_DEF() {
		return DESC_DEF;
	}

	@Column(name = "QUANT_DEF")
	public Integer getQUANT_DEF() {
		return QUANT_DEF;
	}

	@Column(name = "DATA_HORA_REG")
	public Timestamp getDATA_HORA_REG() {
		return DATA_HORA_REG;
	}

	@Column(name = "OBS_DEF")
	public String getOBS_DEF() {
		return OBS_DEF;
	}

	@Column(name = "ID_REF_ETIQUETA")
	public Integer getID_REF_ETIQUETA() {
		return ID_REF_ETIQUETA;
	}

	@Column(name = "QUANT_DEF_M1")
	public Integer getQUANT_DEF_M1() {
		return QUANT_DEF_M1;
	}

	@Column(name = "QUANT_DEF_M2")
	public Integer getQUANT_DEF_M2() {
		return QUANT_DEF_M2;
	}

	@Column(name = "VERSAO_MODIF")
	public Integer getVERSAO_MODIF() {
		return VERSAO_MODIF;
	}

	public void setVERSAO_MODIF(Integer vERSAO_MODIF) {
		VERSAO_MODIF = vERSAO_MODIF;
	}

	public void setQUANT_DEF_M1(Integer qUANT_DEF_M1) {
		QUANT_DEF_M1 = qUANT_DEF_M1;
	}

	public void setQUANT_DEF_M2(Integer qUANT_DEF_M2) {
		QUANT_DEF_M2 = qUANT_DEF_M2;
	}

	public void setID_REF_ETIQUETA(Integer iD_REF_ETIQUETA) {
		ID_REF_ETIQUETA = iD_REF_ETIQUETA;
	}

	public void setOBS_DEF(String oBS_DEF) {
		OBS_DEF = oBS_DEF;
	}

	public void setID_DEF_LIN(Integer iD_DEF_LIN) {
		ID_DEF_LIN = iD_DEF_LIN;
	}

	public void setID_OP_LIN(Integer iD_OP_LIN) {
		ID_OP_LIN = iD_OP_LIN;
	}

	public void setID_UTZ_CRIA(String iD_UTZ_CRIA) {
		ID_UTZ_CRIA = iD_UTZ_CRIA;
	}

	public void setID_UTZ_MODIF(String iD_UTZ_MODIF) {
		ID_UTZ_MODIF = iD_UTZ_MODIF;
	}

	public void setCOD_DEF(String cOD_DEF) {
		COD_DEF = cOD_DEF;
	}

	public void setDESC_DEF(String dESC_DEF) {
		DESC_DEF = dESC_DEF;
	}

	public void setQUANT_DEF(Integer qUANT_DEF) {
		QUANT_DEF = qUANT_DEF;
	}

	public void setDATA_HORA_REG(Timestamp dATA_HORA_REG) {
		DATA_HORA_REG = dATA_HORA_REG;
	}

	@Column(name = "QUANT_DEF_ORIGINAL")
	public Integer getQUANT_DEF_ORIGINAL() {
		return QUANT_DEF_ORIGINAL;
	}

	public void setQUANT_DEF_ORIGINAL(Integer qUANT_DEF_ORIGINAL) {
		QUANT_DEF_ORIGINAL = qUANT_DEF_ORIGINAL;
	}

	@Column(name = "DESC_FAMILIA")
	public String getDESC_FAMILIA() {
		return DESC_FAMILIA;
	}

	public void setDESC_FAMILIA(String dESC_FAMILIA) {
		DESC_FAMILIA = dESC_FAMILIA;
	}

}
