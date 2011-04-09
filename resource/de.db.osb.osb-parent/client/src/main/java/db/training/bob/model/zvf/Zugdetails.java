package db.training.bob.model.zvf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@Embeddable
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "gattungsnummer", "vmax", "tfz", "last", "laenge", "brems" })
public class Zugdetails implements Serializable {

	@XmlElement(required = true)
	@Column(name = "gattungsnummer", length = 5)
	protected String gattungsnummer;

	@XmlElement(required = true)
	@Column(name = "vmax")
	protected Integer vmax;

	@XmlElement(required = true)
	@Embedded
	@Column(name = "tfz")
	protected Tfz tfz;

	@XmlElement(required = true)
	@Embedded
	@Column(name = "last")
	protected Last last;

	@XmlElement(required = true)
	@Column(name = "laenge")
	protected Integer laenge;

	@XmlElement(required = true)
	@Column(name = "brems")
	protected String brems;

	public Zugdetails() {
		this.tfz = new Tfz();
		this.last = new Last();
	}

	public String getGattungsnummer() {
		return gattungsnummer;
	}

	public void setGattungsnummer(String value) {
		this.gattungsnummer = value;
	}

	public Integer getVmax() {
		return vmax;
	}

	public void setVmax(Integer value) {
		this.vmax = value;
	}

	public Tfz getTfz() {
		return tfz;
	}

	public void setTfz(Tfz value) {
		this.tfz = value;
	}

	public Last getLast() {
		return last;
	}

	public void setLast(Last value) {
		this.last = value;
	}

	public Integer getLaenge() {
		return laenge;
	}

	public void setLaenge(Integer value) {
		this.laenge = value;
	}

	public String getBrems() {
		return brems;
	}

	public void setBrems(String value) {
		this.brems = value;
	}

}
