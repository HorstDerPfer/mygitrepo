package db.training.bob.model.fplo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Hibernate;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "fahrplan")
public class Fahrplan extends EasyPersistentObject implements Historizable {

	@Column(name = "Lfd")
	private Integer lfd;

	@Column(name = "Bst", length = 10)
	private String bst;

	@Column(name = "Ankunft")
	@Temporal(TemporalType.TIME)
	private Date ankunft;

	@Column(name = "Abfahrt")
	@Temporal(TemporalType.TIME)
	private Date abfahrt;

	@Column(name = "Haltart", length = 3)
	private String haltart;

	@Column(name = "VzG")
	private Integer vzg;

	@Column(name = "Zugfolge")
	private String zugfolge;

	@Column(name = "Relativ")
	private Integer relativ;

	@Column(name = "IdFahrplan", unique = true, nullable = false)
	private Integer idFahrplan;

	// @Column(name = "Tagwechsel")
	// private Boolean tagwechsel;

	public Integer getLfd() {
		return lfd;
	}

	public void setLfd(Integer lfd) {
		this.lfd = lfd;
	}

	public String getBst() {
		return bst;
	}

	public void setBst(String bst) {
		this.bst = bst;
	}

	public Date getAnkunft() {
		return ankunft;
	}

	public void setAnkunft(Date ankunft) {
		this.ankunft = ankunft;
	}

	public Date getAbfahrt() {
		return abfahrt;
	}

	public void setAbfahrt(Date abfahrt) {
		this.abfahrt = abfahrt;
	}

	public String getHaltart() {
		return haltart;
	}

	public void setHaltart(String haltart) {
		this.haltart = haltart;
	}

	public Integer getVzg() {
		return vzg;
	}

	public void setVzg(Integer vzg) {
		this.vzg = vzg;
	}

	public String getZugfolge() {
		return zugfolge;
	}

	public void setZugfolge(String zugfolge) {
		this.zugfolge = zugfolge;
	}

	public Integer getRelativ() {
		return relativ;
	}

	public void setRelativ(Integer relativ) {
		this.relativ = relativ;
	}

	public void setIdFahrplan(Integer idFahrplan) {
		this.idFahrplan = idFahrplan;
	}

	public Integer getIdFahrplan() {
		return idFahrplan;
	}

	// public Boolean getTagwechsel() {
	// return tagwechsel;
	// }
	//
	// public void setTagwechsel(Boolean tagwechsel) {
	// this.tagwechsel = tagwechsel;
	// }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((idFahrplan == null) ? 0 : idFahrplan.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// Weil Hibernate für Lazy-Initialization Proxy Klassen generiert, reicht
		// if (getClass() != obj.getClass())
		// nicht aus. Hibernate.getClass() liefert "Proxy-freie" Klassen
		if (Hibernate.getClass(this) != Hibernate.getClass(obj))
			return false;
		final Fahrplan other = (Fahrplan) obj;
		if (idFahrplan == null) {
			if (other.idFahrplan != null)
				return false;
		} else if (!idFahrplan.equals(other.idFahrplan))
			return false;
		return true;
	}
}