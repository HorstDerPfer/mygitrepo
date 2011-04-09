package db.training.bob.model.fplo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_FAHRPLAN")
public class ISA_Fahrplan implements Serializable {

	@Id
	@Column(name = "ID_FAHRPLAN", nullable = false, precision = 20, scale = 0)
	private Integer idFahrplan;

	// @Column(name = "ID_ZUG", precision = 20, scale = 0)
	// private Integer idZug;

	@Column(name = "LFD", precision = 20, scale = 0)
	private Integer lfd;

	@Column(name = "BST", length = 10)
	private String bst;

	@Column(name = "ANKUNFT", length = 8)
	private String ankunft;

	@Column(name = "ABFAHRT", length = 8)
	private String abfahrt;

	@Column(name = "ZUGFOLGE", length = 3000)
	private String zugfolge;

	@Column(name = "HALTART", length = 6)
	private String haltart;

	@Column(name = "VZG", length = 5)
	private String vzg;

	public ISA_Fahrplan() {
	}

	public Integer getIdFahrplan() {
		return this.idFahrplan;
	}

	public void setIdFahrplan(Integer idFahrplan) {
		this.idFahrplan = idFahrplan;
	}

	// public Integer getIdZug() {
	// return this.idZug;
	// }
	//
	// public void setIdZug(Integer idZug) {
	// this.idZug = idZug;
	// }

	public Integer getLfd() {
		return this.lfd;
	}

	public void setLfd(Integer lfd) {
		this.lfd = lfd;
	}

	public String getBst() {
		return this.bst;
	}

	public void setBst(String bst) {
		this.bst = bst;
	}

	public String getAnkunft() {
		return this.ankunft;
	}

	public void setAnkunft(String ankunft) {
		this.ankunft = ankunft;
	}

	public String getAbfahrt() {
		return this.abfahrt;
	}

	public void setAbfahrt(String abfahrt) {
		this.abfahrt = abfahrt;
	}

	public String getZugfolge() {
		return this.zugfolge;
	}

	public void setZugfolge(String zugfolge) {
		this.zugfolge = zugfolge;
	}

	public String getHaltart() {
		return this.haltart;
	}

	public void setHaltart(String haltart) {
		this.haltart = haltart;
	}

	public String getVzg() {
		return this.vzg;
	}

	public void setVzg(String vzg) {
		this.vzg = vzg;
	}

}
