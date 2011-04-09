package db.training.osb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "umleitung_fahrplanregelung", uniqueConstraints = @UniqueConstraint(columnNames = {
        "umleitung_ID", "fahrplanregelung_ID" }))
public class UmleitungFahrplanregelungLink extends EasyPersistentObjectVc implements Historizable {

	@ManyToOne(fetch = FetchType.LAZY)
	private Fahrplanregelung fahrplanregelung;

	@ManyToOne(fetch = FetchType.LAZY)
	private Umleitung umleitung;

	@Column(name = "anzahl_sgv")
	private Integer anzahlSGV;

	@Column(name = "anzahl_spnv")
	private Integer anzahlSPNV;

	@Column(name = "anzahl_spfv")
	private Integer anzahlSPFV;

	@Column(name = "anzahl_sgv_gegenrich")
	private Integer anzahlSGVGegenRich;

	@Column(name = "anzahl_spnv_gegenrich")
	private Integer anzahlSPNVGegenRich;

	@Column(name = "anzahl_spfv_gegenrich")
	private Integer anzahlSPFVGegenRich;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Integer bbpRglNr;

	public Fahrplanregelung getFahrplanregelung() {
		return fahrplanregelung;
	}

	public void setFahrplanregelung(Fahrplanregelung fahrplanregelung) {
		this.fahrplanregelung = fahrplanregelung;
	}

	public Umleitung getUmleitung() {
		return umleitung;
	}

	public void setUmleitung(Umleitung umleitung) {
		this.umleitung = umleitung;
	}

	public Integer getAnzahlSGV() {
		return anzahlSGV;
	}

	public void setAnzahlSGV(Integer anzahlSGV) {
		this.anzahlSGV = anzahlSGV;
	}

	public Integer getAnzahlSPNV() {
		return anzahlSPNV;
	}

	public void setAnzahlSPNV(Integer anzahlSPNV) {
		this.anzahlSPNV = anzahlSPNV;
	}

	public Integer getAnzahlSPFV() {
		return anzahlSPFV;
	}

	public void setAnzahlSPFV(Integer anzahlSPFV) {
		this.anzahlSPFV = anzahlSPFV;
	}

	public Integer getAnzahlSGVGegenRich() {
		return anzahlSGVGegenRich;
	}

	public void setAnzahlSGVGegenRich(Integer anzahlSGVGegenRich) {
		this.anzahlSGVGegenRich = anzahlSGVGegenRich;
	}

	public Integer getAnzahlSPNVGegenRich() {
		return anzahlSPNVGegenRich;
	}

	public void setAnzahlSPNVGegenRich(Integer anzahlSPNVGegenRich) {
		this.anzahlSPNVGegenRich = anzahlSPNVGegenRich;
	}

	public Integer getAnzahlSPFVGegenRich() {
		return anzahlSPFVGegenRich;
	}

	public void setAnzahlSPFVGegenRich(Integer anzahlSPFVGegenRich) {
		this.anzahlSPFVGegenRich = anzahlSPFVGegenRich;
	}

	public String getBbpMassnahmeId() {
		return bbpMassnahmeId;
	}

	public void setBbpMassnahmeId(String bbpMassnahmeId) {
		this.bbpMassnahmeId = bbpMassnahmeId;
	}

	public Integer getBbpRglNr() {
		return bbpRglNr;
	}

	public void setBbpRglNr(Integer bbpRglNr) {
		this.bbpRglNr = bbpRglNr;
	}

	public UmleitungFahrplanregelungLink clone() {
		UmleitungFahrplanregelungLink ufl = new UmleitungFahrplanregelungLink();
		ufl.setId(null);
		// Wird Fahrplanregelung mitkopiert, kommt es beim Speichern zur Verletzung des Unique-Keys
		// (Fahrplanregelung, Umleitung)
		ufl.setFahrplanregelung(null);
		ufl.setUmleitung(getUmleitung());
		ufl.setLastChangeDate(new Date());
		ufl.setAnzahlSGV(getAnzahlSGV());
		ufl.setAnzahlSGVGegenRich(getAnzahlSGVGegenRich());
		ufl.setAnzahlSPFV(getAnzahlSPFV());
		ufl.setAnzahlSPFVGegenRich(getAnzahlSPFVGegenRich());
		ufl.setAnzahlSPNV(getAnzahlSPNV());
		ufl.setAnzahlSPNVGegenRich(getAnzahlSPNVGegenRich());
		ufl.setBbpMassnahmeId(getBbpMassnahmeId());
		ufl.setBbpRglNr(getBbpRglNr());
		return ufl;
	}

}