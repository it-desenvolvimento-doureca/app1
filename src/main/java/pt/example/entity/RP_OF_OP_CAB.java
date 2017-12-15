package pt.example.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RP_OF_OP_CAB")
public class RP_OF_OP_CAB {

	private Integer ID_OP_CAB;
	private Integer ID_OF_CAB;
	private String TEMPO_PREP_TOTAL;
	private String TEMPO_PARA_TOTAL;
	private String TEMPO_EXEC_TOTAL;
	private String TEMPO_PREP_TOTAL_M1;
	private String TEMPO_PARA_TOTAL_M1;
	private String TEMPO_EXEC_TOTAL_M1;
	private String TEMPO_PREP_TOTAL_M2;
	private String TEMPO_PARA_TOTAL_M2;
	private String TEMPO_EXEC_TOTAL_M2;

	@Id
	@Column(name = "ID_OP_CAB")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getID_OP_CAB() {
		return ID_OP_CAB;
	}

	@Column(name = "ID_OF_CAB")
	public Integer getID_OF_CAB() {
		return ID_OF_CAB;
	}

	@Column(name = "TEMPO_PREP_TOTAL")
	public String getTEMPO_PREP_TOTAL() {
		return TEMPO_PREP_TOTAL;
	}

	@Column(name = "TEMPO_PARA_TOTAL")
	public String getTEMPO_PARA_TOTAL() {
		return TEMPO_PARA_TOTAL;
	}

	@Column(name = "TEMPO_EXEC_TOTAL")
	public String getTEMPO_EXEC_TOTAL() {
		return TEMPO_EXEC_TOTAL;
	}

	@Column(name = "TEMPO_PREP_TOTAL_M1")
	public String getTEMPO_PREP_TOTAL_M1() {
		return TEMPO_PREP_TOTAL_M1;
	}

	@Column(name = "TEMPO_PARA_TOTAL_M1")
	public String getTEMPO_PARA_TOTAL_M1() {
		return TEMPO_PARA_TOTAL_M1;
	}

	@Column(name = "TEMPO_EXEC_TOTAL_M1")
	public String getTEMPO_EXEC_TOTAL_M1() {
		return TEMPO_EXEC_TOTAL_M1;
	}

	@Column(name = "TEMPO_PREP_TOTAL_M2")
	public String getTEMPO_PREP_TOTAL_M2() {
		return TEMPO_PREP_TOTAL_M2;
	}

	@Column(name = "TEMPO_PARA_TOTAL_M2")
	public String getTEMPO_PARA_TOTAL_M2() {
		return TEMPO_PARA_TOTAL_M2;
	}

	@Column(name = "TEMPO_EXEC_TOTAL_M2")
	public String getTEMPO_EXEC_TOTAL_M2() {
		return TEMPO_EXEC_TOTAL_M2;
	}

	public void setTEMPO_PREP_TOTAL_M1(String tEMPO_PREP_TOTAL_M1) {
		TEMPO_PREP_TOTAL_M1 = tEMPO_PREP_TOTAL_M1;
	}

	public void setTEMPO_PARA_TOTAL_M1(String tEMPO_PARA_TOTAL_M1) {
		TEMPO_PARA_TOTAL_M1 = tEMPO_PARA_TOTAL_M1;
	}

	public void setTEMPO_EXEC_TOTAL_M1(String tEMPO_EXEC_TOTAL_M1) {
		TEMPO_EXEC_TOTAL_M1 = tEMPO_EXEC_TOTAL_M1;
	}

	public void setTEMPO_PREP_TOTAL_M2(String tEMPO_PREP_TOTAL_M2) {
		TEMPO_PREP_TOTAL_M2 = tEMPO_PREP_TOTAL_M2;
	}

	public void setTEMPO_PARA_TOTAL_M2(String tEMPO_PARA_TOTAL_M2) {
		TEMPO_PARA_TOTAL_M2 = tEMPO_PARA_TOTAL_M2;
	}

	public void setTEMPO_EXEC_TOTAL_M2(String tEMPO_EXEC_TOTAL_M2) {
		TEMPO_EXEC_TOTAL_M2 = tEMPO_EXEC_TOTAL_M2;
	}

	public void setID_OP_CAB(Integer iD_OP_CAB) {
		ID_OP_CAB = iD_OP_CAB;
	}

	public void setID_OF_CAB(Integer iD_OF_CAB) {
		ID_OF_CAB = iD_OF_CAB;
	}

	public void setTEMPO_PREP_TOTAL(String tEMPO_PREP_TOTAL) {
		TEMPO_PREP_TOTAL = tEMPO_PREP_TOTAL;
	}

	public void setTEMPO_PARA_TOTAL(String tEMPO_PARA_TOTAL) {
		TEMPO_PARA_TOTAL = tEMPO_PARA_TOTAL;
	}

	public void setTEMPO_EXEC_TOTAL(String tEMPO_EXEC_TOTAL) {
		TEMPO_EXEC_TOTAL = tEMPO_EXEC_TOTAL;
	}

}
