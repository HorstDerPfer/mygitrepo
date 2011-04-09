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
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "folge", "ausfall", "ersatz" })
@Entity
@Table(name = "halt")
public class Halt extends EasyPersistentObject {

	@XmlElement(required = true)
	@Column(name = "folge")
	protected Integer folge;

	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.LAZY)
	protected Bahnhof ausfall;

	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.LAZY)
	protected Bahnhof ersatz;

	public Halt() {

	}

	public Integer getFolge() {
		return folge;
	}

	public void setFolge(Integer folge) {
		this.folge = folge;
	}

	public Bahnhof getAusfall() {
		return ausfall;
	}

	public void setAusfall(Bahnhof value) {
		this.ausfall = value;
	}

	public Bahnhof getErsatz() {
		return ersatz;
	}

	public void setErsatz(Bahnhof value) {
		this.ersatz = value;
	}

}
