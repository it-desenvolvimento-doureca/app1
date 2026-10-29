package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_GESTAO_ETIQUETAS_MATRIX")
public class RP_GESTAO_ETIQUETAS_MATRIX {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer ID;
	@Column(name = "REFERENCIA")
	private String REFERENCIA;
	@Column(name = "LOTE")
	private String LOTE;
	@Column(name = "QTD")
	private Integer QTD;
	@Column(name = "ESTADO")
	private String ESTADO;
	@Column(name = "NUMERO_IMPRESSOES")
	private Integer NUMERO_IMPRESSOES;
	@Column(name = "DATA_CRIA")
	private Timestamp DATA_CRIA;
	@Column(name = "CONTEUDO_ORIGINAL")
	private String CONTEUDO_ORIGINAL;
	@Column(name = "END1")
	private String END1;
	@Column(name = "END2")
	private String END2;

	public Integer getID() {
		return ID;
	}

	public String getREFERENCIA() {
		return REFERENCIA;
	}

	public String getLOTE() {
		return LOTE;
	}

	public Integer getQTD() {
		return QTD;
	}

	public String getESTADO() {
		return ESTADO;
	}

	public Integer getNUMERO_IMPRESSOES() {
		return NUMERO_IMPRESSOES;
	}

	public Timestamp getDATA_CRIA() {
		return DATA_CRIA;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setREFERENCIA(String rEFERENCIA) {
		REFERENCIA = rEFERENCIA;
	}

	public void setLOTE(String lOTE) {
		LOTE = lOTE;
	}

	public void setQTD(Integer qTD) {
		QTD = qTD;
	}

	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}

	public void setNUMERO_IMPRESSOES(Integer nUMERO_IMPRESSOES) {
		NUMERO_IMPRESSOES = nUMERO_IMPRESSOES;
	}

	public void setDATA_CRIA(Timestamp dATA_CRIA) {
		DATA_CRIA = dATA_CRIA;
	}

	public String getCONTEUDO_ORIGINAL() {
		return CONTEUDO_ORIGINAL;
	}

	public void setCONTEUDO_ORIGINAL(String cONTEUDO_ORIGINAL) {
		CONTEUDO_ORIGINAL = cONTEUDO_ORIGINAL;
	}

	public String getEND1() {
		return END1;
	}

	public void setEND1(String eND1) {
		END1 = eND1;
	}

	public String getEND2() {
		return END2;
	}

	public void setEND2(String eND2) {
		END2 = eND2;
	}

}
