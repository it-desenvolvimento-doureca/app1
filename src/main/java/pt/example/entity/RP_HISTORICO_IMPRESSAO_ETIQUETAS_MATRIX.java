package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX")
public class RP_HISTORICO_IMPRESSAO_ETIQUETAS_MATRIX {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer ID;
	@Column(name = "ID_ETIQUETA_MATRIX")
	private Integer ID_ETIQUETA_MATRIX;
	@Column(name = "DESCRICAO")
	private String DESCRICAO;
	@Column(name = "NUMERO_IMPRESSAO")
	private Integer NUMERO_IMPRESSAO;
	@Column(name = "DATA_IMPRESSAO")
	private Timestamp DATA_IMPRESSAO;
	@Column(name = "UTZ_IMPRESSAO")
	private String UTZ_IMPRESSAO;
	@Column(name = "CHEFE")
	private String CHEFE;

	public Integer getID() {
		return ID;
	}

	public Integer getID_ETIQUETA_MATRIX() {
		return ID_ETIQUETA_MATRIX;
	}

	public String getDESCRICAO() {
		return DESCRICAO;
	}

	public Integer getNUMERO_IMPRESSAO() {
		return NUMERO_IMPRESSAO;
	}

	public Timestamp getDATA_IMPRESSAO() {
		return DATA_IMPRESSAO;
	}

	public String getUTZ_IMPRESSAO() {
		return UTZ_IMPRESSAO;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setID_ETIQUETA_MATRIX(Integer iD_ETIQUETA_MATRIX) {
		ID_ETIQUETA_MATRIX = iD_ETIQUETA_MATRIX;
	}

	public void setDESCRICAO(String dESCRICAO) {
		DESCRICAO = dESCRICAO;
	}

	public void setNUMERO_IMPRESSAO(Integer nUMERO_IMPRESSAO) {
		NUMERO_IMPRESSAO = nUMERO_IMPRESSAO;
	}

	public void setDATA_IMPRESSAO(Timestamp dATA_IMPRESSAO) {
		DATA_IMPRESSAO = dATA_IMPRESSAO;
	}

	public void setUTZ_IMPRESSAO(String uTZ_IMPRESSAO) {
		UTZ_IMPRESSAO = uTZ_IMPRESSAO;
	}

	public String getCHEFE() {
		return CHEFE;
	}

	public void setCHEFE(String cHEFE) {
		CHEFE = cHEFE;
	}

}
