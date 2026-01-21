package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_OF_CAB")
public class RP_OF_CAB {

	private Integer ID_OF_CAB;
	private Integer ID_OF_CAB_ORIGEM;
	private String OF_NUM;
	private String OF_OBS;
	private String OP_COD;
	private String OP_NUM;
	private String OP_DES;
	private String SEC_NUM;
	private String SEC_DES;
	private String MAQ_NUM;
	private String MAQ_NUM_ORIG;
	private String MAQ_DES;
	private String ID_UTZ_CRIA;
	private String NOME_UTZ_CRIA;
	private Timestamp DATA_HORA_CRIA;
	private String ID_UTZ_MODIF;
	private String ID_UTZ_EDICAO;
	private String NOME_UTZ_MODIF;
	private Timestamp DATA_HORA_MODIF;
	private String ESTADO;
	private String OP_COD_ORIGEM;
	private String OP_PREVISTA;
	private Integer VERSAO_MODIF;
	private String ESTADO_INICIAL;
	private String IP_POSTO;
	private String IP_POSTO_FINAL;
	private Boolean PRIMEIRA_OPENUM;
	private Boolean ULTIMA_OPENUM;
	private String ETIQUETA;

	// Informações Adicionais
	private Boolean TESTE_DIMENSIONAL;
	private Boolean OPERARIO_FORMACAO;
	private Boolean ORIGEM_RECLAMACAO;
	private Boolean STOCK_ETIQUETA_30;
	private Boolean DEFEITOS_INJECAO;
	private Boolean DEVOLUCAO_CLIENTE;
	private Boolean GAMA_EMBALAGEM_INCORRETA;
	private Boolean MODO_DEGRADADO;

	@Id
	@Column(name = "ID_OF_CAB")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_OF_CAB() {
		return ID_OF_CAB;
	}

	@Column(name = "OF_NUM")
	public String getOF_NUM() {
		return OF_NUM;
	}

	@Column(name = "OP_DES")
	public String getOP_DES() {
		return OP_DES;
	}

	@Column(name = "SEC_NUM")
	public String getSEC_NUM() {
		return SEC_NUM;
	}

	@Column(name = "SEC_DES")
	public String getSEC_DES() {
		return SEC_DES;
	}

	@Column(name = "MAQ_NUM")
	public String getMAQ_NUM() {
		return MAQ_NUM;
	}

	@Column(name = "MAQ_DES")
	public String getMAQ_DES() {
		return MAQ_DES;
	}

	@Column(name = "ID_UTZ_CRIA")
	public String getID_UTZ_CRIA() {
		return ID_UTZ_CRIA;
	}

	@Column(name = "NOME_UTZ_CRIA")
	public String getNOME_UTZ_CRIA() {
		return NOME_UTZ_CRIA;
	}

	@Column(name = "DATA_HORA_CRIA")
	public Timestamp getDATA_HORA_CRIA() {
		return DATA_HORA_CRIA;
	}

	@Column(name = "ID_UTZ_MODIF")
	public String getID_UTZ_MODIF() {
		return ID_UTZ_MODIF;
	}

	@Column(name = "NOME_UTZ_MODIF")
	public String getNOME_UTZ_MODIF() {
		return NOME_UTZ_MODIF;
	}

	@Column(name = "DATA_HORA_MODIF")
	public Timestamp getDATA_HORA_MODIF() {
		return DATA_HORA_MODIF;
	}

	@Column(name = "ESTADO")
	public String getESTADO() {
		return ESTADO;
	}

	@Column(name = "OP_NUM")
	public String getOP_NUM() {
		return OP_NUM;
	}

	@Column(name = "OP_COD_ORIGEM")
	public String getOP_COD_ORIGEM() {
		return OP_COD_ORIGEM;
	}

	@Column(name = "VERSAO_MODIF")
	public Integer getVERSAO_MODIF() {
		return VERSAO_MODIF;
	}

	public void setVERSAO_MODIF(Integer vERSAO_MODIF) {
		VERSAO_MODIF = vERSAO_MODIF;
	}

	public void setOP_COD_ORIGEM(String oP_COD_ORIGEM) {
		OP_COD_ORIGEM = oP_COD_ORIGEM;
	}

	public void setOP_NUM(String oP_NUM) {
		OP_NUM = oP_NUM;
	}

	public void setID_OF_CAB(Integer iD_OF_CAB) {
		ID_OF_CAB = iD_OF_CAB;
	}

	public void setOF_NUM(String oF_NUM) {
		OF_NUM = oF_NUM;
	}

	public void setOP_DES(String oP_DES) {
		OP_DES = oP_DES;
	}

	public void setSEC_NUM(String sEC_NUM) {
		SEC_NUM = sEC_NUM;
	}

	public void setSEC_DES(String sEC_DES) {
		SEC_DES = sEC_DES;
	}

	public void setMAQ_NUM(String mAQ_NUM) {
		MAQ_NUM = mAQ_NUM;
	}

	public void setMAQ_DES(String mAQ_DES) {
		MAQ_DES = mAQ_DES;
	}

	public void setID_UTZ_CRIA(String iD_UTZ_CRIA) {
		ID_UTZ_CRIA = iD_UTZ_CRIA;
	}

	public void setNOME_UTZ_CRIA(String nOME_UTZ_CRIA) {
		NOME_UTZ_CRIA = nOME_UTZ_CRIA;
	}

	public void setDATA_HORA_CRIA(Timestamp dATA_HORA_CRIA) {
		DATA_HORA_CRIA = dATA_HORA_CRIA;
	}

	public void setID_UTZ_MODIF(String iD_UTZ_MODIF) {
		ID_UTZ_MODIF = iD_UTZ_MODIF;
	}

	public void setNOME_UTZ_MODIF(String nOME_UTZ_MODIF) {
		NOME_UTZ_MODIF = nOME_UTZ_MODIF;
	}

	public void setDATA_HORA_MODIF(Timestamp dATA_HORA_MODIF) {
		DATA_HORA_MODIF = dATA_HORA_MODIF;
	}

	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}

	@Column(name = "ID_OF_CAB_ORIGEM")
	public Integer getID_OF_CAB_ORIGEM() {
		return ID_OF_CAB_ORIGEM;
	}

	public void setID_OF_CAB_ORIGEM(Integer iD_OF_CAB_ORIGEM) {
		ID_OF_CAB_ORIGEM = iD_OF_CAB_ORIGEM;
	}

	@Column(name = "OF_OBS")
	public String getOF_OBS() {
		return OF_OBS;
	}

	@Column(name = "OP_COD")
	public String getOP_COD() {
		return OP_COD;
	}

	public void setOF_OBS(String oF_OBS) {
		OF_OBS = oF_OBS;
	}

	public void setOP_COD(String oP_COD) {
		OP_COD = oP_COD;
	}

	@Column(name = "OP_PREVISTA")
	public String getOP_PREVISTA() {
		return OP_PREVISTA;
	}

	public void setOP_PREVISTA(String oP_PREVISTA) {
		OP_PREVISTA = oP_PREVISTA;
	}

	@Column(name = "MAQ_NUM_ORIG")
	public String getMAQ_NUM_ORIG() {
		return MAQ_NUM_ORIG;
	}

	public void setMAQ_NUM_ORIG(String mAQ_NUM_ORIG) {
		MAQ_NUM_ORIG = mAQ_NUM_ORIG;
	}

	@Column(name = "ID_UTZ_EDICAO")
	public String getID_UTZ_EDICAO() {
		return ID_UTZ_EDICAO;
	}

	public void setID_UTZ_EDICAO(String iD_UTZ_EDICAO) {
		ID_UTZ_EDICAO = iD_UTZ_EDICAO;
	}

	@Column(name = "ESTADO_INICIAL")
	public String getESTADO_INICIAL() {
		return ESTADO_INICIAL;
	}

	public void setESTADO_INICIAL(String eSTADO_INICIAL) {
		ESTADO_INICIAL = eSTADO_INICIAL;
	}

	@Column(name = "IP_POSTO")
	public String getIP_POSTO() {
		return IP_POSTO;
	}

	public void setIP_POSTO(String iP_POSTO) {
		IP_POSTO = iP_POSTO;
	}

	@Column(name = "IP_POSTO_FINAL")
	public String getIP_POSTO_FINAL() {
		return IP_POSTO_FINAL;
	}

	public void setIP_POSTO_FINAL(String iP_POSTO_FINAL) {
		IP_POSTO_FINAL = iP_POSTO_FINAL;
	}

	@Column(name = "PRIMEIRA_OPENUM")
	public Boolean getPRIMEIRA_OPENUM() {
		return PRIMEIRA_OPENUM;
	}

	@Column(name = "ULTIMA_OPENUM")
	public Boolean getULTIMA_OPENUM() {
		return ULTIMA_OPENUM;
	}

	public void setPRIMEIRA_OPENUM(Boolean pRIMEIRA_OPENUM) {
		PRIMEIRA_OPENUM = pRIMEIRA_OPENUM;
	}

	public void setULTIMA_OPENUM(Boolean uLTIMA_OPENUM) {
		ULTIMA_OPENUM = uLTIMA_OPENUM;
	}

	@Column(name = "ETIQUETA")
	public String getETIQUETA() {
		return ETIQUETA;
	}

	public void setETIQUETA(String eTIQUETA) {
		ETIQUETA = eTIQUETA;
	}

	@Column(name = "TESTE_DIMENSIONAL")
	public Boolean getTESTE_DIMENSIONAL() {
		return TESTE_DIMENSIONAL;
	}

	public void setTESTE_DIMENSIONAL(Boolean tESTE_DIMENSIONAL) {
		TESTE_DIMENSIONAL = tESTE_DIMENSIONAL;
	}

	@Column(name = "OPERARIO_FORMACAO")
	public Boolean getOPERARIO_FORMACAO() {
		return OPERARIO_FORMACAO;
	}

	public void setOPERARIO_FORMACAO(Boolean oPERARIO_FORMACAO) {
		OPERARIO_FORMACAO = oPERARIO_FORMACAO;
	}

	@Column(name = "ORIGEM_RECLAMACAO")
	public Boolean getORIGEM_RECLAMACAO() {
		return ORIGEM_RECLAMACAO;
	}

	public void setORIGEM_RECLAMACAO(Boolean oRIGEM_RECLAMACAO) {
		ORIGEM_RECLAMACAO = oRIGEM_RECLAMACAO;
	}

	@Column(name = "STOCK_ETIQUETA_30")
	public Boolean getSTOCK_ETIQUETA_30() {
		return STOCK_ETIQUETA_30;
	}

	public void setSTOCK_ETIQUETA_30(Boolean sTOCK_ETIQUETA_30) {
		STOCK_ETIQUETA_30 = sTOCK_ETIQUETA_30;
	}

	@Column(name = "DEFEITOS_INJECAO")
	public Boolean getDEFEITOS_INJECAO() {
		return DEFEITOS_INJECAO;
	}

	public void setDEFEITOS_INJECAO(Boolean dEFEITOS_INJECAO) {
		DEFEITOS_INJECAO = dEFEITOS_INJECAO;
	}

	@Column(name = "DEVOLUCAO_CLIENTE")
	public Boolean getDEVOLUCAO_CLIENTE() {
		return DEVOLUCAO_CLIENTE;
	}

	public void setDEVOLUCAO_CLIENTE(Boolean dEVOLUCAO_CLIENTE) {
		DEVOLUCAO_CLIENTE = dEVOLUCAO_CLIENTE;
	}

	@Column(name = "GAMA_EMBALAGEM_INCORRETA")
	public Boolean getGAMA_EMBALAGEM_INCORRETA() {
		return GAMA_EMBALAGEM_INCORRETA;
	}

	public void setGAMA_EMBALAGEM_INCORRETA(Boolean gAMA_EMBALAGEM_INCORRETA) {
		GAMA_EMBALAGEM_INCORRETA = gAMA_EMBALAGEM_INCORRETA;
	}

	@Column(name = "MODO_DEGRADADO")
	public Boolean getMODO_DEGRADADO() {
		return MODO_DEGRADADO;
	}

	public void setMODO_DEGRADADO(Boolean mODO_DEGRADADO) {
		MODO_DEGRADADO = mODO_DEGRADADO;
	}

}
