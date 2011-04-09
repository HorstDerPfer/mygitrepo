package db.training.bob.model.zvf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.IndexColumn;

import db.training.bob.model.zvf.helper.IsoDateAdapter;
import db.training.bob.model.zvf.helper.JaNeinAdapter;
import db.training.bob.model.zvf.helper.VzGStreckeAdapter;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.osb.model.VzgStrecke;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "startbst", "endbst", "streckeVZG", "export", "massnahme",
        "betriebsweise", "grund", "baubeginn", "bauende", "zeitraumUnterbrochen" })
@Entity
@Table(name = "zvfStrecke")
public class Strecke extends EasyPersistentObject {

	@XmlElement(required = true)
	@Column(name = "startbst")
	protected String startbst;

	@XmlElement(required = true)
	@Column(name = "endbst")
	protected String endbst;

	@XmlElementWrapper(name = "vzgliste")
	@XmlElement(name = "vzg")
	@XmlJavaTypeAdapter(value = VzGStreckeAdapter.class, type = VzgStrecke.class)
	@ManyToMany(fetch = FetchType.LAZY)
	@IndexColumn(name = "nr", base = 0)
	protected List<VzgStrecke> streckeVZG = new ArrayList<VzgStrecke>();

	@XmlElement(name = "export", required = false)
	@XmlJavaTypeAdapter(value = JaNeinAdapter.class, type = Boolean.class)
	@Column(name = "export")
	protected Boolean export;

	@XmlElement(required = true)
	@Column(name = "massnahme")
	protected String massnahme;

	@XmlElement(required = true)
	@Column(name = "betriebsweise")
	protected String betriebsweise;

	@XmlElement(required = true)
	@Column(name = "grund")
	protected String grund;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = IsoDateAdapter.class, type = Date.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "baubeginn")
	protected Date baubeginn;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = IsoDateAdapter.class, type = Date.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "bauende")
	protected Date bauende;

	@XmlElement(name = "zeitraum_unterbrochen", required = true)
	@XmlJavaTypeAdapter(value = JaNeinAdapter.class, type = Boolean.class)
	@Column(name = "zeitraum_unterbrochen")
	protected Boolean zeitraumUnterbrochen;

	public String getStartbst() {
		return startbst;
	}

	public void setStartbst(String value) {
		this.startbst = value;
	}

	public String getEndbst() {
		return endbst;
	}

	public void setEndbst(String value) {
		this.endbst = value;
	}

	public List<VzgStrecke> getStreckeVZG() {
		return streckeVZG;
	}

	public void setStreckeVZG(List<VzgStrecke> streckeVZG) {
		this.streckeVZG = streckeVZG;
	}

	public Boolean getExport() {
		return export;
	}

	public void setExport(Boolean export) {
		this.export = export;
	}

	public String getMassnahme() {
		return massnahme;
	}

	public void setMassnahme(String value) {
		this.massnahme = value;
	}

	public String getBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(String value) {
		this.betriebsweise = value;
	}

	public String getGrund() {
		return grund;
	}

	public void setGrund(String value) {
		this.grund = value;
	}

	public Date getBaubeginn() {
		return baubeginn;
	}

	public void setBaubeginn(Date value) {
		this.baubeginn = value;
	}

	public Date getBauende() {
		return bauende;
	}

	public void setBauende(Date value) {
		this.bauende = value;
	}

	public Boolean getZeitraumUnterbrochen() {
		if (zeitraumUnterbrochen == null)
			return false;
		return zeitraumUnterbrochen;
	}

	public void setZeitraumUnterbrochen(Boolean value) {
		this.zeitraumUnterbrochen = value;
	}

}
