package db.training.bob.model.zvf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import db.training.bob.model.zvf.helper.Haltart;
import db.training.bob.model.zvf.helper.HaltartAdapter;
import db.training.bob.model.zvf.helper.TimeAdapter;
import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@Entity
@Table(name = "knotenzeit")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "bahnhof", "haltart", "ankunft", "abfahrt", "relativlage" })
public class Knotenzeit extends EasyPersistentObject {

	@XmlElement
	@Column(name = "bahnhof", length = 5)
	protected String bahnhof;

	@XmlElement
	@Column(name = "haltart")
	@XmlJavaTypeAdapter(value = HaltartAdapter.class, type = Haltart.class)
	protected Haltart haltart;

	@XmlElement
	@XmlJavaTypeAdapter(value = TimeAdapter.class, type = Date.class)
	@Column(name = "ankunft")
	@Temporal(TemporalType.TIME)
	protected Date ankunft;

	@XmlElement
	@XmlJavaTypeAdapter(value = TimeAdapter.class, type = Date.class)
	@Column(name = "abfahrt")
	@Temporal(TemporalType.TIME)
	protected Date abfahrt;

	@XmlElement
	@Column(name = "relativlage")
	protected Integer relativlage;

	public Knotenzeit() {
		haltart = Haltart.LEER;
	}

	public String getBahnhof() {
		return bahnhof;
	}

	public void setBahnhof(String bahnhof) {
		this.bahnhof = bahnhof;
	}

	public Haltart getHaltart() {
		return haltart;
	}

	public void setHaltart(Haltart haltart) {
		this.haltart = haltart;
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

	public Integer getRelativlage() {
		return relativlage;
	}

	public void setRelativlage(Integer relativlage) {
		this.relativlage = relativlage;
	}

}
