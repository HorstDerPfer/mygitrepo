package db.training.bob.model.zvf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import db.training.bob.model.Regionalbereich;
import db.training.bob.model.zvf.helper.EinsNullLeerAdapter;
import db.training.bob.model.zvf.helper.RegionalbereichAdapter;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "regionalbereich", "beteiligt", "fplonr" })
@Entity
@Table(name = "niederlassung")
public class Niederlassung implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, insertable = true, updatable = true)
	@XmlTransient
	protected Integer id;

	@XmlValue
	@OneToOne(fetch = FetchType.EAGER)
	@XmlJavaTypeAdapter(value = RegionalbereichAdapter.class, type = Regionalbereich.class)
	protected Regionalbereich regionalbereich;

	@XmlAttribute
	@XmlSchemaType(name = "anySimpleType")
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	protected Boolean beteiligt;

	@XmlAttribute(name = "fplo", required = true)
	@Column(name = "fplonr", length = 5)
	protected Integer fplonr;

	@XmlTransient
	@Column(name = "reihenfolgennr")
	private Integer reihenfolgenNr;

	protected Integer bearbeitungsStatus = 0;

	public Niederlassung() {
		this.beteiligt = false;
		this.bearbeitungsStatus = 0;
	}

	public Niederlassung(Regionalbereich rb) {
		this.regionalbereich = rb;
		this.beteiligt = false;
		this.bearbeitungsStatus = 0;
	}

	public Niederlassung(Regionalbereich rb, boolean beteiligt) {
		this.regionalbereich = rb;
		this.beteiligt = beteiligt;
		this.bearbeitungsStatus = 0;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public Boolean getBeteiligt() {
		if (beteiligt == null)
			return false;
		return beteiligt;
	}

	public void setBeteiligt(Boolean value) {
		this.beteiligt = value;
	}

	public Integer getFplonr() {
		return fplonr;
	}

	public void setFplonr(Integer fplonr) {
		this.fplonr = fplonr;
	}

	public Integer getReihenfolgenNr() {
		return reihenfolgenNr;
	}

	public void setReihenfolgenNr(Integer reihenfolgenNr) {
		this.reihenfolgenNr = reihenfolgenNr;
	}

	public Integer getBearbeitungsStatus() {
		return bearbeitungsStatus;
	}

	public void setBearbeitungsStatus(Integer bearbeitungsStatus) {
		this.bearbeitungsStatus = bearbeitungsStatus;
	}
}
