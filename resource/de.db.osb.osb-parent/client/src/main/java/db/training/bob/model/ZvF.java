package db.training.bob.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "zvf")
public abstract class ZvF extends EasyPersistentObject implements Historizable {

	@Column(name = "datum")
	@Temporal(TemporalType.DATE)
	private Date datum;

	@Column(name = "zuggattung")
	private String zugGattung;

	@Column(name = "zugnr")
	private Integer zugNr;

	@Column(name = "abgangsbahnhof")
	private String abgangsbahnhof;

	@Column(name = "zielbahnhof")
	private String zielbahnhof;

	@Column(name = "umleitungsstrecke")
	private String umleitungsStrecke;

	@Column(name = "verspaetunginminuten")
	private Integer verspaetungInMinuten;

	@Column(name = "bemerkungnetz")
	private String bemerkungNetz;

	/**
	 * @return the datum
	 */
	public Date getDatum() {
		return datum;
	}

	/**
	 * @param datum
	 *            the datum to set
	 */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	/**
	 * @return the zugGattung
	 */
	public String getZugGattung() {
		return zugGattung;
	}

	/**
	 * @param zugGattung
	 *            the zugGattung to set
	 */
	public void setZugGattung(String zugGattung) {
		this.zugGattung = zugGattung;
	}

	/**
	 * @return the zugNr
	 */
	public Integer getZugNr() {
		return zugNr;
	}

	/**
	 * @param zugNr
	 *            the zugNr to set
	 */
	public void setZugNr(Integer zugNr) {
		this.zugNr = zugNr;
	}

	/**
	 * @return the abgangsbahnhof
	 */
	public String getAbgangsbahnhof() {
		return abgangsbahnhof;
	}

	/**
	 * @param abgangsbahnhof
	 *            the abgangsbahnhof to set
	 */
	public void setAbgangsbahnhof(String abgangsbahnhof) {
		this.abgangsbahnhof = abgangsbahnhof;
	}

	/**
	 * @return the zielbahnhof
	 */
	public String getZielbahnhof() {
		return zielbahnhof;
	}

	/**
	 * @param zielbahnhof
	 *            the zielbahnhof to set
	 */
	public void setZielbahnhof(String zielbahnhof) {
		this.zielbahnhof = zielbahnhof;
	}

	/**
	 * @return the umleitungsStrecke
	 */
	public String getUmleitungsStrecke() {
		return umleitungsStrecke;
	}

	/**
	 * @param umleitungsStrecke
	 *            the umleitungsStrecke to set
	 */
	public void setUmleitungsStrecke(String umleitungsStrecke) {
		this.umleitungsStrecke = umleitungsStrecke;
	}

	/**
	 * @return the verspaetungInMinuten
	 */
	public Integer getVerspaetungInMinuten() {
		return verspaetungInMinuten;
	}

	/**
	 * @param verspaetungInMinuten
	 *            the verspaetungInMinuten to set
	 */
	public void setVerspaetungInMinuten(Integer verspaetungInMinuten) {
		this.verspaetungInMinuten = verspaetungInMinuten;
	}

	/**
	 * @return the bemerkungNetz
	 */
	public String getBemerkungNetz() {
		return bemerkungNetz;
	}

	/**
	 * @param bemerkungNetz
	 *            the bemerkungNetz to set
	 */
	public void setBemerkungNetz(String bemerkungNetz) {
		this.bemerkungNetz = bemerkungNetz;
	}

}
