package db.training.bob.model.zvf;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.model.zvf.helper.AbweichungsartAdapter;
import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@Embeddable
@Table(name = "abweichung")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "umleitung", "umleitweg", "verspaetung", "ausfallvon",
        "ausfallbis", "vorplanab", "halt", "regelungen" })
public class Abweichung extends EasyPersistentObject {

	@XmlAttribute(name = "art", required = true)
	@XmlSchemaType(name = "anySimpleType")
	@Column(name = "art")
	@XmlJavaTypeAdapter(value = AbweichungsartAdapter.class, type = Abweichungsart.class)
	protected Abweichungsart art;

	@XmlElement
	@Column(name = "umleitung")
	protected String umleitung;

	@CollectionOfElements(fetch = FetchType.EAGER)
	@IndexColumn(name = "pos")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@XmlElementWrapper(name = "umleitweg")
	@XmlElement(name = "ds100")
	protected List<String> umleitweg;

	@XmlElement
	@Column(name = "verspaetung")
	protected Integer verspaetung;

	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.EAGER)
	protected Bahnhof ausfallvon = null;

	@XmlElement
	@OneToOne(fetch = FetchType.EAGER)
	protected Bahnhof ausfallbis = null;

	@XmlElement
	@OneToOne(fetch = FetchType.EAGER)
	protected Bahnhof vorplanab = null;

	@XmlElementWrapper(name = "haltliste")
	@XmlElement(name = "halt")
	@IndexColumn(name = "haltnr")
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	protected List<Halt> halt;
	
	@XmlElementWrapper(name = "regelungsliste")
	@XmlElement(name = "regelung")
	@IndexColumn(name = "regelungnr")
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	protected List<RegelungAbw> regelungen;

	public Abweichung() {
		this.halt = new ArrayList<Halt>();
		this.regelungen = new ArrayList<RegelungAbw>();
	}

	public List<String> getUmleitweg() {
		return umleitweg;
	}

	public void setUmleitweg(List<String> umleitweg) {
		this.umleitweg = umleitweg;
	}

	public Integer getVerspaetung() {
		return verspaetung;
	}

	public void setVerspaetung(Integer value) {
		this.verspaetung = value;
	}

	public String getUmleitung() {
		return umleitung;
	}

	public void setUmleitung(String value) {
		this.umleitung = value;
	}

	public Bahnhof getAusfallvon() {
		return ausfallvon;
	}

	public void setAusfallvon(Bahnhof value) {
		this.ausfallvon = value;
	}

	public Bahnhof getAusfallbis() {
		return ausfallbis;
	}

	public void setAusfallbis(Bahnhof value) {
		this.ausfallbis = value;
	}

	public Bahnhof getVorplanab() {
		return vorplanab;
	}

	public void setVorplanab(Bahnhof value) {
		this.vorplanab = value;
	}

	public List<Halt> getHalt() {
		if (halt == null) {
			halt = new ArrayList<Halt>();
		}
		return this.halt;
	}

	public void setHalt(List<Halt> value) {

		this.halt = value;
	}

	public List<RegelungAbw> getRegelungen() {
		return regelungen;
	}

	public void setRegelungen(List<RegelungAbw> regelungen) {
		this.regelungen = regelungen;
	}

	public Abweichungsart getArt() {
		if (art == null) {
			return Abweichungsart.VERSPAETUNG;
		}
		return art;
	}

	public void setArt(Abweichungsart value) {
		this.art = value;
	}
}