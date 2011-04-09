package db.training.bob.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import db.training.bob.service.MeilensteinService;
import db.training.bob.service.Terminberechnung;
import db.training.bob.util.DateUtils;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "terminuebersicht_bbp")
public class TerminUebersichtBaubetriebsplanung extends EasyPersistentObject implements
    Historizable {

	// //////
	// Soll Termine / Meilensteine

	@Column(name = "studiegrobkonzept_soll")
	@Temporal(TemporalType.DATE)
	private Date studieGrobkonzeptSoll;

	@Column(name = "anforderungbbzr_soll")
	@Temporal(TemporalType.DATE)
	private Date anforderungBBZRSoll;

	/**
	 * ZvF-Vorentwurf
	 */
	@Column(name = "biueentwurf_soll")
	@Temporal(TemporalType.DATE)
	private Date biUeEntwurfSoll;

	@Column(name = "zvfentwurf_soll")
	@Temporal(TemporalType.DATE)
	private Date zvfEntwurfSoll;

	@Column(name = "koordinationsergebnis_soll")
	@Temporal(TemporalType.DATE)
	private Date koordinationsErgebnisSoll;

	@Column(name = "gesamtkonzeptbbzr_soll")
	@Temporal(TemporalType.DATE)
	private Date gesamtKonzeptBBZRSoll;

	@Column(name = "biue_soll")
	@Temporal(TemporalType.DATE)
	private Date biUeSoll;

	@Column(name = "zvf_soll")
	@Temporal(TemporalType.DATE)
	private Date zvfSoll;

	// //////
	// Ist Termine

	// nur KS
	@Column(name = "studiegrobkonzept")
	@Temporal(TemporalType.DATE)
	private Date studieGrobkonzept;

	@Column(name = "anforderungbbzr")
	@Temporal(TemporalType.DATE)
	private Date anforderungBBZR;

	/**
	 * ZvF-Vorentwurf
	 */
	@Column(name = "biueentwurf")
	@Temporal(TemporalType.DATE)
	private Date biUeEntwurf;

	@Column(name = "zvfentwurf")
	@Temporal(TemporalType.DATE)
	private Date zvfEntwurf;

	@Column(name = "koordinationsergebnis")
	@Temporal(TemporalType.DATE)
	private Date koordinationsErgebnis;

	@Column(name = "gesamtkonzeptbbzr")
	@Temporal(TemporalType.DATE)
	private Date gesamtKonzeptBBZR;

	@Column(name = "biue")
	@Temporal(TemporalType.DATE)
	private Date biUe;

	@Column(name = "zvf")
	@Temporal(TemporalType.DATE)
	private Date zvf;

	// //////
	// Status

	@Transient
	private StatusType anforderungBBZRStatus;

	@Transient
	private StatusType biUeEntwurfStatus;

	@Transient
	private StatusType zvfEntwurfStatus;

	@Transient
	private StatusType koordinationsErgebnisStatus;

	@Transient
	private StatusType gesamtKonzeptBBZRStatus;

	@Transient
	private StatusType biUeStatus;

	@Transient
	private StatusType zvfStatus;

	@Column(name = "biueentwurferforderlich", nullable = false)
	private boolean biUeEntwurfErforderlich = false;

	@Column(name = "zvfentwurferforderlich", nullable = false)
	private boolean zvfEntwurfErforderlich = false;

	@Column(name = "koord_erforderlich", nullable = false)
	private boolean koordinationsergebnisErforderlich = false;

	@Column(name = "gesamtkonzeptbbzrerforderlich", nullable = false)
	private boolean gesamtkonzeptBBZRErforderlich = false;

	@Column(name = "biueerforderlich", nullable = false)
	private boolean biUeErforderlich = false;

	@Column(name = "zvferforderlich", nullable = false)
	private boolean zvFErforderlich = false;

	protected TerminUebersichtBaubetriebsplanung() {

	}

	public TerminUebersichtBaubetriebsplanung(Art art) {
		anforderungBBZRStatus = StatusType.NEUTRAL;
		biUeEntwurfStatus = StatusType.NEUTRAL;
		zvfEntwurfStatus = StatusType.NEUTRAL;
		koordinationsErgebnisStatus = StatusType.NEUTRAL;
		gesamtKonzeptBBZRStatus = StatusType.NEUTRAL;
		biUeStatus = StatusType.NEUTRAL;
		zvfStatus = StatusType.NEUTRAL;

		setErforderlichHaekchen(art);
	}

	public void setErforderlichHaekchen(Art art) {
		biUeEntwurfErforderlich = true;
		zvfEntwurfErforderlich = true;
		if (art == Art.A || art == Art.QS) {
			koordinationsergebnisErforderlich = false;
			gesamtkonzeptBBZRErforderlich = true;
			biUeErforderlich = false;
			zvFErforderlich = true;
		} else if (art == Art.B) {
			koordinationsergebnisErforderlich = false;
			gesamtkonzeptBBZRErforderlich = false;
			biUeErforderlich = false;
			zvFErforderlich = false;
		} else if (art == Art.KS) {
			koordinationsergebnisErforderlich = false;
			gesamtkonzeptBBZRErforderlich = false;
			biUeErforderlich = false;
			zvFErforderlich = true;
		}
	}

	// //////////
	// Properties
	public Date getStudieGrobkonzeptSoll() {
		return studieGrobkonzeptSoll;
	}

	public void setStudieGrobkonzeptSoll(Date studieGrobkonzeptSoll) {
		this.studieGrobkonzeptSoll = studieGrobkonzeptSoll;
	}

	public Date getAnforderungBBZRSoll() {
		return anforderungBBZRSoll;
	}

	public void setAnforderungBBZRSoll(Date anforderungBBZRSoll) {
		this.anforderungBBZRSoll = anforderungBBZRSoll;
	}

	/**
	 * ZvF-Vorentwurf
	 */
	public Date getBiUeEntwurfSoll() {
		return biUeEntwurfSoll;
	}

	public void setBiUeEntwurfSoll(Date biUeEntwurfSoll) {
		this.biUeEntwurfSoll = biUeEntwurfSoll;
	}

	public Date getZvfEntwurfSoll() {
		return zvfEntwurfSoll;
	}

	public void setZvfEntwurfSoll(Date zvfEntwurfSoll) {
		this.zvfEntwurfSoll = zvfEntwurfSoll;
	}

	public Date getKoordinationsErgebnisSoll() {
		return koordinationsErgebnisSoll;
	}

	public void setKoordinationsErgebnisSoll(Date koordinationsErgebnisSoll) {
		this.koordinationsErgebnisSoll = koordinationsErgebnisSoll;
	}

	public Date getGesamtKonzeptBBZRSoll() {
		return gesamtKonzeptBBZRSoll;
	}

	public void setGesamtKonzeptBBZRSoll(Date gesamtKonzeptBBZRSoll) {
		this.gesamtKonzeptBBZRSoll = gesamtKonzeptBBZRSoll;
	}

	public Date getBiUeSoll() {
		return biUeSoll;
	}

	public void setBiUeSoll(Date biUeSoll) {
		this.biUeSoll = biUeSoll;
	}

	public Date getZvfSoll() {
		return zvfSoll;
	}

	public void setZvfSoll(Date zvfSoll) {
		this.zvfSoll = zvfSoll;
	}

	public Date getAnforderungBBZR() {
		return anforderungBBZR;
	}

	public void setAnforderungBBZR(Date anforderungBBZR) {
		this.anforderungBBZR = anforderungBBZR;
	}

	public Date getStudieGrobkonzept() {
		return studieGrobkonzept;
	}

	public void setStudieGrobkonzept(Date studieGrobkonzept) {
		this.studieGrobkonzept = studieGrobkonzept;
	}

	/**
	 * ZvF-Vorentwurf
	 */
	public Date getBiUeEntwurf() {
		return biUeEntwurf;
	}

	public void setBiUeEntwurf(Date biUeEntwurf) {
		this.biUeEntwurf = biUeEntwurf;
	}

	public Date getZvfEntwurf() {
		return zvfEntwurf;
	}

	public void setZvfEntwurf(Date zvfEntwurf) {
		this.zvfEntwurf = zvfEntwurf;
	}

	public Date getKoordinationsErgebnis() {
		return koordinationsErgebnis;
	}

	public void setKoordinationsErgebnis(Date koordinationsErgebnis) {
		this.koordinationsErgebnis = koordinationsErgebnis;
	}

	public Date getGesamtKonzeptBBZR() {
		return gesamtKonzeptBBZR;
	}

	public void setGesamtKonzeptBBZR(Date gesamtKonzeptBBZR) {
		this.gesamtKonzeptBBZR = gesamtKonzeptBBZR;
	}

	public Date getBiUe() {
		return biUe;
	}

	public void setBiUe(Date biUe) {
		this.biUe = biUe;
	}

	public Date getZvf() {
		return zvf;
	}

	public void setZvf(Date zvf) {
		this.zvf = zvf;
	}

	public StatusType getAnforderungBBZRStatus() {
		return anforderungBBZRStatus;
	}

	public void setAnforderungBBZRStatus(StatusType anforderungBBZRStatus) {
		this.anforderungBBZRStatus = anforderungBBZRStatus;
	}

	public StatusType getBiUeEntwurfStatus() {
		return biUeEntwurfStatus;
	}

	public void setBiUeEntwurfStatus(StatusType biUeEntwurfStatus) {
		this.biUeEntwurfStatus = biUeEntwurfStatus;
	}

	public StatusType getZvfEntwurfStatus() {
		return zvfEntwurfStatus;
	}

	public void setZvfEntwurfStatus(StatusType zvfEntwurfStatus) {
		this.zvfEntwurfStatus = zvfEntwurfStatus;
	}

	public StatusType getKoordinationsErgebnisStatus() {
		return koordinationsErgebnisStatus;
	}

	public void setKoordinationsErgebnisStatus(StatusType koordinationsErgebnisStatus) {
		this.koordinationsErgebnisStatus = koordinationsErgebnisStatus;
	}

	public StatusType getGesamtKonzeptBBZRStatus() {
		return gesamtKonzeptBBZRStatus;
	}

	public void setGesamtKonzeptBBZRStatus(StatusType gesamtKonzeptBBZRStatus) {
		this.gesamtKonzeptBBZRStatus = gesamtKonzeptBBZRStatus;
	}

	public StatusType getBiUeStatus() {
		return biUeStatus;
	}

	public void setBiUeStatus(StatusType biUeStatus) {
		this.biUeStatus = biUeStatus;
	}

	public StatusType getZvfStatus() {
		return zvfStatus;
	}

	public void setZvfStatus(StatusType zvfStatus) {
		this.zvfStatus = zvfStatus;
	}

	public boolean isBiUeEntwurfErforderlich() {
		return biUeEntwurfErforderlich;
	}

	public void setBiUeEntwurfErforderlich(boolean biUeEntwurfErforderlich) {
		this.biUeEntwurfErforderlich = biUeEntwurfErforderlich;
	}

	public boolean isZvfEntwurfErforderlich() {
		return zvfEntwurfErforderlich;
	}

	public void setZvfEntwurfErforderlich(boolean zvfEntwurfErforderlich) {
		this.zvfEntwurfErforderlich = zvfEntwurfErforderlich;
	}

	public boolean isKoordinationsergebnisErforderlich() {
		return koordinationsergebnisErforderlich;
	}

	public void setKoordinationsergebnisErforderlich(boolean koordinationsergebnisErforderlich) {
		this.koordinationsergebnisErforderlich = koordinationsergebnisErforderlich;
	}

	public boolean isGesamtkonzeptBBZRErforderlich() {
		return gesamtkonzeptBBZRErforderlich;
	}

	public void setGesamtkonzeptBBZRErforderlich(boolean gesamtkonzeptBBZRErforderlich) {
		this.gesamtkonzeptBBZRErforderlich = gesamtkonzeptBBZRErforderlich;
	}

	public boolean isBiUeErforderlich() {
		return biUeErforderlich;
	}

	public void setBiUeErforderlich(boolean biUeErforderlich) {
		this.biUeErforderlich = biUeErforderlich;
	}

	public boolean isZvFErforderlich() {
		return zvFErforderlich;
	}

	public void setZvFErforderlich(boolean zvFErforderlich) {
		this.zvFErforderlich = zvFErforderlich;
	}

	// ////////
	// Methoden

	/**
	 * aktualisiert die TerminStatus-Properties basierend auf den SOLL/IST-Terminen
	 */
	public void setTerminStatus() {
		setAnforderungBBZRStatus(Terminberechnung.getStatus(anforderungBBZRSoll, anforderungBBZR,
		    null));

		setBiUeEntwurfStatus(Terminberechnung.getStatus(biUeEntwurfSoll, biUeEntwurf,
		    isBiUeEntwurfErforderlich()));

		setZvfEntwurfStatus(Terminberechnung.getStatus(zvfEntwurfSoll, zvfEntwurf,
		    isZvfEntwurfErforderlich()));

		setKoordinationsErgebnisStatus(Terminberechnung.getStatus(koordinationsErgebnisSoll,
		    koordinationsErgebnis, isKoordinationsergebnisErforderlich()));

		setGesamtKonzeptBBZRStatus(Terminberechnung.getStatus(gesamtKonzeptBBZRSoll,
		    gesamtKonzeptBBZR, isGesamtkonzeptBBZRErforderlich()));

		setBiUeStatus(Terminberechnung.getStatus(biUeSoll, biUe, isBiUeErforderlich()));

		setZvfStatus(Terminberechnung.getStatus(zvfSoll, zvf, isZvFErforderlich()));
	}

	/**
	 * berechnet die SOLL-Termine abhängig vom Startdatum der Baumaßnahme und der Maßnahmenart neu
	 * wenn der Terminkette ein EVU zugeordnet ist und der jeweilige Termin noch nicht gesetzt
	 * wurde.
	 * 
	 * @param beginnDatum
	 * @param baumassnahmeArt
	 */
	public void setSollTermine(Date beginnDatum, Art baumassnahmeArt) {
		MeilensteinService service = EasyServiceFactory.getInstance().createMeilensteinService();

		setAnforderungBBZRSoll(service.getSollTermin(beginnDatum, baumassnahmeArt,
		    Schnittstelle.BBP, Meilensteinbezeichnungen.ANF_BBZR));
		setBiUeEntwurfSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.BBP,
		    Meilensteinbezeichnungen.BIUE_ENTW));
		setZvfEntwurfSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.BBP,
		    Meilensteinbezeichnungen.ZVF_ENTW));
		setBiUeSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.BBP,
		    Meilensteinbezeichnungen.BIUE));
		setZvfSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.BBP,
		    Meilensteinbezeichnungen.ZVF));

		if (baumassnahmeArt.equals(Art.A) || baumassnahmeArt.equals(Art.QS)) {
			setKoordinationsErgebnisSoll(service.getSollTermin(beginnDatum, baumassnahmeArt,
			    Schnittstelle.BBP, Meilensteinbezeichnungen.KOORD_ERG));
			setGesamtKonzeptBBZRSoll(service.getSollTermin(beginnDatum, baumassnahmeArt,
			    Schnittstelle.BBP, Meilensteinbezeichnungen.GESAMTKONZ_BBZR));
		}
	}

	public int getVerbleibendeTage_StudieGrobkonzept() {
		return DateUtils.getDateDiffInDays(new Date(), getStudieGrobkonzeptSoll());
	}

	public int getVerbleibendeTage_AnforderungBBZR() {
		return DateUtils.getDateDiffInDays(new Date(), getAnforderungBBZRSoll());
	}

	public int getVerbleibendeTage_BiUeEntwurf() {
		return DateUtils.getDateDiffInDays(new Date(), getBiUeEntwurfSoll());
	}

	public int getVerbleibendeTage_ZvfEntwurf() {
		return DateUtils.getDateDiffInDays(new Date(), getZvfEntwurfSoll());
	}

	public int getVerbleibendeTage_KoordinationsErgebnis() {
		return DateUtils.getDateDiffInDays(new Date(), getKoordinationsErgebnisSoll());
	}

	public int getVerbleibendeTage_GesamtKonzeptBBZR() {
		return DateUtils.getDateDiffInDays(new Date(), getGesamtKonzeptBBZRSoll());
	}

	public int getVerbleibendeTage_BiUe() {
		return DateUtils.getDateDiffInDays(new Date(), getBiUeSoll());
	}

	public int getVerbleibendeTage_Zvf() {
		return DateUtils.getDateDiffInDays(new Date(), getZvfSoll());
	}
}
