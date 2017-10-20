package pt.example.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_OF_OP_ETIQUETA")
public class RP_OF_OP_ETIQUETA {

	private Integer ID_REF_ETIQUETA;
	private Integer ID_OP_LIN;
	private String REF_NUM;
	private String REF_ETIQUETA;
	private String REF_LOTE;
	private String OF_NUM_ORIGEM;
	private String OP_COD_ORIGEM;
	private Integer QUANT_ETIQUETA;
	private Integer QUANT_BOAS;
	private Integer QUANT_DEF;
	private String ID_UTZ_CRIA;
	private String ID_UTZ_MODIF;
	private Timestamp DATA_HORA_MODIF;

	@Id
	@Column(name = "ID_REF_ETIQUETA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_REF_ETIQUETA() {
		return ID_REF_ETIQUETA;
	}

	@Column(name = "ID_OP_LIN")
	public Integer getID_OP_LIN() {
		return ID_OP_LIN;
	}

	@Column(name = "REF_NUM")
	public String getREF_NUM() {
		return REF_NUM;
	}

	@Column(name = "REF_ETIQUETA")
	public String getREF_ETIQUETA() {
		return REF_ETIQUETA;
	}

	@Column(name = "REF_LOTE")
	public String getREF_LOTE() {
		return REF_LOTE;
	}

	@Column(name = "OF_NUM_ORIGEM")
	public String getOF_NUM_ORIGEM() {
		return OF_NUM_ORIGEM;
	}

	@Column(name = "OP_COD_ORIGEM")
	public String getOP_COD_ORIGEM() {
		return OP_COD_ORIGEM;
	}

	@Column(name = "QUANT_ETIQUETA")
	public Integer getQUANT_ETIQUETA() {
		return QUANT_ETIQUETA;
	}

	@Column(name = "QUANT_BOAS")
	public Integer getQUANT_BOAS() {
		return QUANT_BOAS;
	}

	@Column(name = "QUANT_DEF")
	public Integer getQUANT_DEF() {
		return QUANT_DEF;
	}

	@Column(name = "ID_UTZ_CRIA")
	public String getID_UTZ_CRIA() {
		return ID_UTZ_CRIA;
	}

	@Column(name = "ID_UTZ_MODIF")
	public String getID_UTZ_MODIF() {
		return ID_UTZ_MODIF;
	}

	@Column(name = "DATA_HORA_MODIF")
	public Timestamp getDATA_HORA_MODIF() {
		return DATA_HORA_MODIF;
	}

	public void setID_REF_ETIQUETA(Integer iD_REF_ETIQUETA) {
		ID_REF_ETIQUETA = iD_REF_ETIQUETA;
	}

	public void setID_OP_LIN(Integer iD_OP_LIN) {
		ID_OP_LIN = iD_OP_LIN;
	}

	public void setREF_NUM(String rEF_NUM) {
		REF_NUM = rEF_NUM;
	}

	public void setREF_ETIQUETA(String rEF_ETIQUETA) {
		REF_ETIQUETA = rEF_ETIQUETA;
	}

	public void setREF_LOTE(String rEF_LOTE) {
		REF_LOTE = rEF_LOTE;
	}

	public void setOF_NUM_ORIGEM(String oF_NUM_ORIGEM) {
		OF_NUM_ORIGEM = oF_NUM_ORIGEM;
	}

	public void setOP_COD_ORIGEM(String oP_COD_ORIGEM) {
		OP_COD_ORIGEM = oP_COD_ORIGEM;
	}

	public void setQUANT_ETIQUETA(Integer qUANT_ETIQUETA) {
		QUANT_ETIQUETA = qUANT_ETIQUETA;
	}

	public void setQUANT_BOAS(Integer qUANT_BOAS) {
		QUANT_BOAS = qUANT_BOAS;
	}

	public void setQUANT_DEF(Integer qUANT_DEF) {
		QUANT_DEF = qUANT_DEF;
	}

	public void setID_UTZ_CRIA(String iD_UTZ_CRIA) {
		ID_UTZ_CRIA = iD_UTZ_CRIA;
	}

	public void setID_UTZ_MODIF(String iD_UTZ_MODIF) {
		ID_UTZ_MODIF = iD_UTZ_MODIF;
	}

	public void setDATA_HORA_MODIF(Timestamp dATA_HORA_MODIF) {
		DATA_HORA_MODIF = dATA_HORA_MODIF;
	}

}
