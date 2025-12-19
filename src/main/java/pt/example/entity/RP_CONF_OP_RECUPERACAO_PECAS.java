package pt.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "RP_CONF_OP_RECUPERACAO_PECAS")
public class RP_CONF_OP_RECUPERACAO_PECAS { 

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @JsonProperty("ID")
	    private Integer ID;

	    @Column(name = "ID_OP", nullable = false, length = 50)
	    @JsonProperty("ID_OP")
	    private String ID_OP;

	    @Column(name = "NOME_OP", nullable = false, length = 255)
	    @JsonProperty("NOME_OP")
	    private String NOME_OP;

	    @Column(name = "SECNUMENR1_OP", length = 100)
	    @JsonProperty("SECNUMENR1_OP")
	    private String SECNUMENR1_OP;

	    // Getters and setters

	    public Integer getID() {
	        return ID;
	    }

	    public void setID(Integer ID) {
	        this.ID = ID;
	    }

	    public String getID_OP() {
	        return ID_OP;
	    }

	    public void setID_OP(String ID_OP) {
	        this.ID_OP = ID_OP;
	    }

	    public String getNOME_OP() {
	        return NOME_OP;
	    }

	    public void setNOME_OP(String NOME_OP) {
	        this.NOME_OP = NOME_OP;
	    }

	    public String getSECNUMENR1_OP() {
	        return SECNUMENR1_OP;
	    }

	    public void setSECNUMENR1_OP(String SECNUMENR1_OP) {
	        this.SECNUMENR1_OP = SECNUMENR1_OP;
	    }
	}

