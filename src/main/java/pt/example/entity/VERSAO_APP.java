package pt.example.entity;

import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VERSAO_APP")
public class VERSAO_APP {

	private String VERSAO;
	private Timestamp DATA_MODIFICAO;
	private Integer ID;

	@Id
	@Column(name = "ID")
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	@Column(name = "VERSAO")
	public String getVERSAO() {
		return VERSAO;
	}

	@Column(name = "DATA_MODIFICAO")
	public Timestamp getDATA_MODIFICAO() {
		return DATA_MODIFICAO;
	}

	public void setVERSAO(String vERSAO) {
		VERSAO = vERSAO;
	}

	public void setDATA_MODIFICAO(Timestamp dATA_MODIFICAO) {
		DATA_MODIFICAO = dATA_MODIFICAO;
	}

}
