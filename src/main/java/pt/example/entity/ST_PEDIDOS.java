package pt.example.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ST_PEDIDOS")
public class ST_PEDIDOS {
	public Integer ID_PEDIDO;
	public String PROREF;
	public String ETQNUM;
	public Integer QUANT;
	public String LOTE;
	public String ARMAZEM;
	public String DESCRICAO;
	public String UTZ_CRIA;
	public Timestamp DATA_CRIA;
	public Integer COD_SECTOR;
	public String DES_SECTOR;
	public String LOCAL;

	public Integer COD_SECTOR_OF;
	public String DES_SECTOR_OF;
	public String IP_POSTO;

	@Id
	@Column(name = "ID_PEDIDO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_PEDIDO() {
		return ID_PEDIDO;
	}

	@Column(name = "PROREF")
	public String getPROREF() {
		return PROREF;
	}

	@Column(name = "ETQNUM")
	public String getETQNUM() {
		return ETQNUM;
	}

	@Column(name = "QUANT")
	public Integer getQUANT() {
		return QUANT;
	}

	@Column(name = "LOTE")
	public String getLOTE() {
		return LOTE;
	}

	@Column(name = "ARMAZEM")
	public String getARMAZEM() {
		return ARMAZEM;
	}

	@Column(name = "DESCRICAO")
	public String getDESCRICAO() {
		return DESCRICAO;
	}

	@Column(name = "UTZ_CRIA")
	public String getUTZ_CRIA() {
		return UTZ_CRIA;
	}

	@Column(name = "DATA_CRIA")
	public Timestamp getDATA_CRIA() {
		return DATA_CRIA;
	}

	public void setID_PEDIDO(Integer iD_PEDIDO) {
		ID_PEDIDO = iD_PEDIDO;
	}

	public void setPROREF(String pROREF) {
		PROREF = pROREF;
	}

	public void setETQNUM(String eTQNUM) {
		ETQNUM = eTQNUM;
	}

	public void setQUANT(Integer qUANT) {
		QUANT = qUANT;
	}

	public void setLOTE(String lOTE) {
		LOTE = lOTE;
	}

	public void setARMAZEM(String aRMAZEM) {
		ARMAZEM = aRMAZEM;
	}

	public void setDESCRICAO(String dESCRICAO) {
		DESCRICAO = dESCRICAO;
	}

	public void setUTZ_CRIA(String uTZ_CRIA) {
		UTZ_CRIA = uTZ_CRIA;
	}

	public void setDATA_CRIA(Timestamp dATA_CRIA) {
		DATA_CRIA = dATA_CRIA;
	}

	@Column(name = "COD_SECTOR")
	public Integer getCOD_SECTOR() {
		return COD_SECTOR;
	}

	@Column(name = "DES_SECTOR")
	public String getDES_SECTOR() {
		return DES_SECTOR;
	}

	public void setCOD_SECTOR(Integer cOD_SECTOR) {
		COD_SECTOR = cOD_SECTOR;
	}

	public void setDES_SECTOR(String dES_SECTOR) {
		DES_SECTOR = dES_SECTOR;
	}

	@Column(name = "LOCAL")
	public String getLOCAL() {
		return LOCAL;
	}

	public void setLOCAL(String lOCAL) {
		LOCAL = lOCAL;
	}

	@Column(name = "COD_SECTOR_OF")
	public Integer getCOD_SECTOR_OF() {
		return COD_SECTOR_OF;
	}

	@Column(name = "DES_SECTOR_OF")
	public String getDES_SECTOR_OF() {
		return DES_SECTOR_OF;
	}

	@Column(name = "IP_POSTO")
	public String getIP_POSTO() {
		return IP_POSTO;
	}

	public void setCOD_SECTOR_OF(Integer cOD_SECTOR_OF) {
		COD_SECTOR_OF = cOD_SECTOR_OF;
	}

	public void setDES_SECTOR_OF(String dES_SECTOR_OF) {
		DES_SECTOR_OF = dES_SECTOR_OF;
	}

	public void setIP_POSTO(String iP_POSTO) {
		IP_POSTO = iP_POSTO;
	}
}
