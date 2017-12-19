package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GER_EVENTO")
public class GER_EVENTO {

	private Integer ID_EVENTO;
	private Integer MODULO;
	private Integer ID_ORIGEM;
	private String CAMPO_ORIGEM;
	private Timestamp DATA_HORA_CRIA;
	private String ID_UTZ_CRIA;
	private String NOME_UTZ_CRIA;
	private String MENSAGEM;
	private String ASSUNTO;
	private Timestamp DATA_HORA_LEITURA;
	private String ID_UTZ_LEITURA;
	private String NOME_UTZ_LEITURA;
	private String ESTADO;

	@Id
	@Column(name = "ID_EVENTO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_EVENTO() {
		return ID_EVENTO;
	}

	@Column(name = "MODULO")
	public Integer getMODULO() {
		return MODULO;
	}

	@Column(name = "ID_ORIGEM")
	public Integer getID_ORIGEM() {
		return ID_ORIGEM;
	}

	@Column(name = "DATA_HORA_CRIA")
	public Timestamp getDATA_HORA_CRIA() {
		return DATA_HORA_CRIA;
	}

	@Column(name = "ID_UTZ_CRIA")
	public String getID_UTZ_CRIA() {
		return ID_UTZ_CRIA;
	}

	@Column(name = "MENSAGEM")
	public String getMENSAGEM() {
		return MENSAGEM;
	}

	@Column(name = "ASSUNTO")
	public String getASSUNTO() {
		return ASSUNTO;
	}

	@Column(name = "DATA_HORA_LEITURA")
	public Timestamp getDATA_HORA_LEITURA() {
		return DATA_HORA_LEITURA;
	}

	@Column(name = "ID_UTZ_LEITURA")
	public String getID_UTZ_LEITURA() {
		return ID_UTZ_LEITURA;
	}

	@Column(name = "NOME_UTZ_LEITURA")
	public String getNOME_UTZ_LEITURA() {
		return NOME_UTZ_LEITURA;
	}

	public void setID_EVENTO(Integer iD_EVENTO) {
		ID_EVENTO = iD_EVENTO;
	}

	public void setMODULO(Integer mODULO) {
		MODULO = mODULO;
	}

	public void setID_ORIGEM(Integer iD_ORIGEM) {
		ID_ORIGEM = iD_ORIGEM;
	}

	public void setDATA_HORA_CRIA(Timestamp dATA_HORA_CRIA) {
		DATA_HORA_CRIA = dATA_HORA_CRIA;
	}

	public void setID_UTZ_CRIA(String iD_UTZ_CRIA) {
		ID_UTZ_CRIA = iD_UTZ_CRIA;
	}

	public void setMENSAGEM(String mENSAGEM) {
		MENSAGEM = mENSAGEM;
	}

	public void setASSUNTO(String aSSUNTO) {
		ASSUNTO = aSSUNTO;
	}

	public void setDATA_HORA_LEITURA(Timestamp dATA_HORA_LEITURA) {
		DATA_HORA_LEITURA = dATA_HORA_LEITURA;
	}

	public void setID_UTZ_LEITURA(String iD_UTZ_LEITURA) {
		ID_UTZ_LEITURA = iD_UTZ_LEITURA;
	}

	public void setNOME_UTZ_LEITURA(String nOME_UTZ_LEITURA) {
		NOME_UTZ_LEITURA = nOME_UTZ_LEITURA;
	}

	@Column(name = "CAMPO_ORIGEM")
	public String getCAMPO_ORIGEM() {
		return CAMPO_ORIGEM;
	}

	public void setCAMPO_ORIGEM(String cAMPO_ORIGEM) {
		CAMPO_ORIGEM = cAMPO_ORIGEM;
	}

	@Column(name = "ESTADO")
	public String getESTADO() {
		return ESTADO;
	}

	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}

	@Column(name = "NOME_UTZ_CRIA")
	public String getNOME_UTZ_CRIA() {
		return NOME_UTZ_CRIA;
	}

	public void setNOME_UTZ_CRIA(String nOME_UTZ_CRIA) {
		NOME_UTZ_CRIA = nOME_UTZ_CRIA;
	}

}
