package pt.example.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "RP_OF_PREP_LIN")
public class RP_OF_PREP_LIN {

	private Integer ID_PREP_LIN;
	private Integer ID_OP_CAB;
	private Date DATA_INI;
	private Time HORA_INI;
	private Date DATA_FIM;
	private Time HORA_FIM;
	private String ID_UTZ_CRIA;
	private String ID_UTZ_MODIF;
	private Timestamp DATA_HORA_MODIF;
	private String ESTADO;

	private Date DATA_INI_M1;
	private Time HORA_INI_M1;
	private Date DATA_FIM_M1;
	private Time HORA_FIM_M1;
	private Date DATA_INI_M2;
	private Time HORA_INI_M2;
	private Date DATA_FIM_M2;
	private Time HORA_FIM_M2;

	@Id
	@Column(name = "ID_PREP_LIN")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_PREP_LIN() {
		return ID_PREP_LIN;
	}

	@Column(name = "ID_OP_CAB")
	public Integer getID_OP_CAB() {
		return ID_OP_CAB;
	}

	@Column(name = "DATA_INI")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt_PT", timezone = "Europe/Lisbon")
	public Date getDATA_INI() {
		return DATA_INI;
	}

	@Column(name = "HORA_INI")
	public Time getHORA_INI() {
		return HORA_INI;
	}

	@Column(name = "DATA_FIM")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt_PT", timezone = "Europe/Lisbon")
	public Date getDATA_FIM() {
		return DATA_FIM;
	}

	@Column(name = "HORA_FIM")
	public Time getHORA_FIM() {
		return HORA_FIM;
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

	@Column(name = "ESTADO")
	public String getESTADO() {
		return ESTADO;
	}

	@Column(name = "DATA_INI_M1")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt_PT", timezone = "Europe/Lisbon")
	public Date getDATA_INI_M1() {
		return DATA_INI_M1;
	}

	@Column(name = "HORA_INI_M1")
	public Time getHORA_INI_M1() {
		return HORA_INI_M1;
	}

	@Column(name = "DATA_FIM_M1")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt_PT", timezone = "Europe/Lisbon")
	public Date getDATA_FIM_M1() {
		return DATA_FIM_M1;
	}

	@Column(name = "HORA_FIM_M1")
	public Time getHORA_FIM_M1() {
		return HORA_FIM_M1;
	}

	@Column(name = "DATA_INI_M2")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt_PT", timezone = "Europe/Lisbon")
	public Date getDATA_INI_M2() {
		return DATA_INI_M2;
	}

	@Column(name = "HORA_INI_M2")
	public Time getHORA_INI_M2() {
		return HORA_INI_M2;
	}

	@Column(name = "DATA_FIM_M2")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt_PT", timezone = "Europe/Lisbon")
	public Date getDATA_FIM_M2() {
		return DATA_FIM_M2;
	}

	@Column(name = "HORA_FIM_M2")
	public Time getHORA_FIM_M2() {
		return HORA_FIM_M2;
	}

	public void setDATA_INI_M1(Date dATA_INI_M1) {
		DATA_INI_M1 = dATA_INI_M1;
	}

	public void setHORA_INI_M1(Time hORA_INI_M1) {
		HORA_INI_M1 = hORA_INI_M1;
	}

	public void setDATA_FIM_M1(Date dATA_FIM_M1) {
		DATA_FIM_M1 = dATA_FIM_M1;
	}

	public void setHORA_FIM_M1(Time hORA_FIM_M1) {
		HORA_FIM_M1 = hORA_FIM_M1;
	}

	public void setDATA_INI_M2(Date dATA_INI_M2) {
		DATA_INI_M2 = dATA_INI_M2;
	}

	public void setHORA_INI_M2(Time hORA_INI_M2) {
		HORA_INI_M2 = hORA_INI_M2;
	}

	public void setDATA_FIM_M2(Date dATA_FIM_M2) {
		DATA_FIM_M2 = dATA_FIM_M2;
	}

	public void setHORA_FIM_M2(Time hORA_FIM_M2) {
		HORA_FIM_M2 = hORA_FIM_M2;
	}

	public void setID_PREP_LIN(Integer iD_PREP_LIN) {
		ID_PREP_LIN = iD_PREP_LIN;
	}

	public void setID_OP_CAB(Integer iD_OP_CAB) {
		ID_OP_CAB = iD_OP_CAB;
	}

	public void setDATA_INI(Date dATA_INI) {
		DATA_INI = dATA_INI;
	}

	public void setHORA_INI(Time hORA_INI) {
		HORA_INI = hORA_INI;
	}

	public void setDATA_FIM(Date dATA_FIM) {
		DATA_FIM = dATA_FIM;
	}

	public void setHORA_FIM(Time hORA_FIM) {
		HORA_FIM = hORA_FIM;
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

	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}

}
