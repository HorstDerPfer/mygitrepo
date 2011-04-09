package db.training.bob.model.zvf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@Entity
@Table(name = "regelweg")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "liniennr", "abgangsbahnhof", "zielbahnhof" })
public class Regelweg extends EasyPersistentObject {

	@XmlElement(required = true)
	@Column(name = "liniennr")
	protected String liniennr;

	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.LAZY)
	protected Bahnhof abgangsbahnhof;

	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.LAZY)
	protected Bahnhof zielbahnhof;

	public Regelweg() {

	}

	public String getLiniennr() {
		return liniennr;
	}

	public void setLiniennr(String value) {
		this.liniennr = value;
	}

	public Bahnhof getAbgangsbahnhof() {
		return abgangsbahnhof;
	}

	public void setAbgangsbahnhof(Bahnhof value) {
		this.abgangsbahnhof = value;
	}

	public Bahnhof getZielbahnhof() {
		return zielbahnhof;
	}

	public void setZielbahnhof(Bahnhof value) {
		this.zielbahnhof = value;
	}

}
