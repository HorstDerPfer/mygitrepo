package db.training.bob.model.fplo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_FPLO")
public class ISA_Fplo implements Serializable {

	@Id
	@Column(name = "ID_FPLO", nullable = false)
	private Integer idFplo;

	@Column(name = "ID_ZUG")
	private Integer idZug;

	@Column(name = "FPLO")
	private String fplo;

	public ISA_Fplo() {
	}

	public Integer getIdFplo() {
		return this.idFplo;
	}

	public void setIdFplo(Integer idFplo) {
		this.idFplo = idFplo;
	}

	public Integer getIdZug() {
		return this.idZug;
	}

	public void setIdZug(Integer idZug) {
		this.idZug = idZug;
	}

	public String getFplo() {
		return this.fplo;
	}

	public void setFplo(String fplo) {
		this.fplo = fplo;
	}

}
