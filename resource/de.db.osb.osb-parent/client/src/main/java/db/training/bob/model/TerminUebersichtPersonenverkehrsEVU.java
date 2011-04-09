package db.training.bob.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import db.training.bob.service.MeilensteinService;
import db.training.bob.service.MeilensteinServiceImpl;
import db.training.bob.service.Terminberechnung;
import db.training.bob.util.DateUtils;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.hibernate.history.Historizable;
import db.training.logwrapper.Logger;

@SuppressWarnings("serial")
@Entity
@Table(name = "terminuebersicht_pevu")
public class TerminUebersichtPersonenverkehrsEVU extends EasyPersistentObject implements
    TerminUebersichtEVU, Historizable {

	@ManyToOne(fetch = FetchType.EAGER)
	private EVUGruppe evuGruppe;

	/**
	 * dient nach der Migration von #425 als Backup für die Verknüpfung zwischen Baumaßnahme und
	 * Schnittstelle
	 * 
	 * @deprecated nicht verwenden
	 */
	@Deprecated
	@Column(name = "baumassnahme_id")
	@SuppressWarnings("unused")
	private Integer baumassnahmeId;

	// //////
	// Soll Termine / Meilensteine
	@Column(name = "masteruebpv_soll")
	@Temporal(TemporalType.DATE)
	private Date masterUebergabeblattPVSoll;

	@Column(name = "uebpv_soll")
	@Temporal(TemporalType.DATE)
	private Date uebergabeblattPVSoll;

	@Column(name = "bkonzeptevu_soll")
	@Temporal(TemporalType.DATE)
	private Date bKonzeptEVUSoll;

	// nur KS
	@Column(name = "studiegrobkonzept_soll")
	@Temporal(TemporalType.DATE)
	private Date studieGrobkonzeptSoll;

	@Column(name = "zvfentwurf_soll")
	@Temporal(TemporalType.DATE)
	private Date zvfEntwurfSoll;

	@Column(name = "stellungnahmeevu_soll")
	@Temporal(TemporalType.DATE)
	private Date stellungnahmeEVUSoll;

	@Column(name = "zvf_soll")
	@Temporal(TemporalType.DATE)
	private Date zvFSoll;

	@Column(name = "fplo_soll")
	@Temporal(TemporalType.DATE)
	private Date fploSoll;

	// nur KS und QS
	@Column(name = "eingabegfdz_soll")
	@Temporal(TemporalType.DATE)
	private Date eingabeGFD_ZSoll;

	// //////
	// Ist Termine
	@Column(name = "masteruebpv")
	@Temporal(TemporalType.DATE)
	private Date masterUebergabeblattPV;

	@Column(name = "uebpv")
	@Temporal(TemporalType.DATE)
	private Date uebergabeblattPV;

	@Column(name = "bkonzeptevu")
	@Temporal(TemporalType.DATE)
	private Date bKonzeptEVU;

	// nur KS
	@Column(name = "studiegrobkonzept")
	@Temporal(TemporalType.DATE)
	private Date studieGrobkonzept;

	@Column(name = "zvfentwurf")
	@Temporal(TemporalType.DATE)
	private Date zvfEntwurf;

	@Column(name = "stellungnahmeevu")
	@Temporal(TemporalType.DATE)
	private Date stellungnahmeEVU;

	@Column(name = "zvf")
	@Temporal(TemporalType.DATE)
	private Date zvF;

	@Column(name = "fplo")
	@Temporal(TemporalType.DATE)
	private Date fplo;

	// nur KS und QS
	@Column(name = "eingabegfdz")
	@Temporal(TemporalType.DATE)
	private Date eingabeGFD_Z;

	// //////
	// Status
	@Transient
	private StatusType masterUebergabeblattPVStatus;

	@Transient
	private StatusType uebergabeblattPVStatus;

	@Transient
	private StatusType bKonzeptEVUStatus;

	@Transient
	private StatusType zvfEntwurfStatus;

	@Transient
	private StatusType stellungnahmeEVUStatus;

	@Transient
	private StatusType zvFStatus;

	@Transient
	private StatusType fploStatus;

	@Transient
	private StatusType eingabeGFD_ZStatus;

	@Column(name = "zvfentwurferforderlich", nullable = false)
	private boolean zvfEntwurfErforderlich;

	@Column(name = "stellungnahmeevuerforderlich", nullable = false)
	private boolean stellungnahmeEVUErforderlich;

	@Column(name = "zvferforderlich", nullable = false)
	private boolean zvfErforderlich;

	@Column(name = "masteruebpverforderlich", nullable = false)
	private boolean masterUebergabeblattPVErforderlich;

	@Column(name = "uebpverforderlich", nullable = false)
	private boolean uebergabeblattPVErforderlich;

	@Column(name = "bkonzeptevuerforderlich", nullable = false)
	private boolean bKonzeptEVUErforderlich;

	@Column(name = "fploerforderlich", nullable = false)
	private boolean fploErforderlich;

	@Column(name = "eingabegfdzerforderlich", nullable = false)
	private boolean eingabeGFD_ZErforderlich;

	@Column(name = "ausfaellesev")
	private boolean ausfaelleSEV;

	public TerminUebersichtPersonenverkehrsEVU() {
		super();
		masterUebergabeblattPVStatus = StatusType.NEUTRAL;
		uebergabeblattPVStatus = StatusType.NEUTRAL;
		bKonzeptEVUStatus = StatusType.NEUTRAL;
		zvfEntwurfStatus = StatusType.NEUTRAL;
		stellungnahmeEVUStatus = StatusType.NEUTRAL;
		zvFStatus = StatusType.NEUTRAL;
		fploStatus = StatusType.NEUTRAL;
		eingabeGFD_ZStatus = StatusType.NEUTRAL;
		ausfaelleSEV = false;
	}

	public TerminUebersichtPersonenverkehrsEVU(Art baumassnahmenart) {
		this();

		setErforderlichHaekchen(baumassnahmenart);
	}

	public void setErforderlichHaekchen(Art art) {
		setZvfEntwurfErforderlich(true);
		setFploErforderlich(true);
		if (art == Art.A) {
			setStellungnahmeEVUErforderlich(true);
			setZvfErforderlich(true);
			setMasterUebergabeblattPVErforderlich(false);
			setUebergabeblattPVErforderlich(false);
			setEingabeGFD_ZErforderlich(false);
			setBKonzeptEVUErforderlich(true);
		} else if (art == Art.B) {
			setStellungnahmeEVUErforderlich(false);
			setZvfErforderlich(false);
			setMasterUebergabeblattPVErforderlich(false);
			setUebergabeblattPVErforderlich(false);
			setEingabeGFD_ZErforderlich(false);
			setBKonzeptEVUErforderlich(false);
		} else if (art == Art.KS) {
			setStellungnahmeEVUErforderlich(true);
			setZvfErforderlich(true);
			setMasterUebergabeblattPVErforderlich(false);
			setUebergabeblattPVErforderlich(true);
			setEingabeGFD_ZErforderlich(true);
			setBKonzeptEVUErforderlich(true);
		} else if (art == Art.QS) {
			setStellungnahmeEVUErforderlich(true);
			setZvfErforderlich(true);
			setMasterUebergabeblattPVErforderlich(true);
			setUebergabeblattPVErforderlich(true);
			setEingabeGFD_ZErforderlich(true);
			setBKonzeptEVUErforderlich(true);
		}
	}

	// //////////
	// Properties
	public Date getMasterUebergabeblattPVSoll() {
		return masterUebergabeblattPVSoll;
	}

	public void setMasterUebergabeblattPVSoll(Date masterUebergabeblattPVSoll) {
		this.masterUebergabeblattPVSoll = masterUebergabeblattPVSoll;
	}

	public Date getUebergabeblattPVSoll() {
		return uebergabeblattPVSoll;
	}

	public void setUebergabeblattPVSoll(Date uebergabeblattPVSoll) {
		this.uebergabeblattPVSoll = uebergabeblattPVSoll;
	}

	public Date getBKonzeptEVUSoll() {
		return bKonzeptEVUSoll;
	}

	public void setBKonzeptEVUSoll(Date konzeptEVUSoll) {
		bKonzeptEVUSoll = konzeptEVUSoll;
	}

	public Date getStudieGrobkonzeptSoll() {
		return studieGrobkonzeptSoll;
	}

	public void setStudieGrobkonzeptSoll(Date studieGrobkonzeptSoll) {
		this.studieGrobkonzeptSoll = studieGrobkonzeptSoll;
	}

	public Date getZvfEntwurfSoll() {
		return zvfEntwurfSoll;
	}

	public void setZvfEntwurfSoll(Date zvfEntwurfSoll) {
		this.zvfEntwurfSoll = zvfEntwurfSoll;
	}

	public Date getStellungnahmeEVUSoll() {
		return stellungnahmeEVUSoll;
	}

	public void setStellungnahmeEVUSoll(Date stellungnahmeEVUSoll) {
		this.stellungnahmeEVUSoll = stellungnahmeEVUSoll;
	}

	public Date getZvFSoll() {
		return zvFSoll;
	}

	public void setZvFSoll(Date zvFSoll) {
		this.zvFSoll = zvFSoll;
	}

	public Date getFploSoll() {
		return fploSoll;
	}

	public void setFploSoll(Date fploSoll) {
		this.fploSoll = fploSoll;
	}

	public Date getEingabeGFD_ZSoll() {
		return eingabeGFD_ZSoll;
	}

	public void setEingabeGFD_ZSoll(Date eingabeGFD_ZSoll) {
		this.eingabeGFD_ZSoll = eingabeGFD_ZSoll;
	}

	public Date getMasterUebergabeblattPV() {
		return masterUebergabeblattPV;
	}

	public void setMasterUebergabeblattPV(Date masterUebergabeblattPV) {
		this.masterUebergabeblattPV = masterUebergabeblattPV;
	}

	public Date getUebergabeblattPV() {
		return uebergabeblattPV;
	}

	public void setUebergabeblattPV(Date uebergabeBlattPV) {
		this.uebergabeblattPV = uebergabeBlattPV;
	}

	public boolean isAusfaelleSEV() {
		return ausfaelleSEV;
	}

	public void setAusfaelleSEV(boolean ausfaelleSEV) {
		this.ausfaelleSEV = ausfaelleSEV;
	}

	public Date getBKonzeptEVU() {
		return bKonzeptEVU;
	}

	public void setBKonzeptEVU(Date konzeptEVU) {
		bKonzeptEVU = konzeptEVU;
	}

	public StatusType getMasterUebergabeblattPVStatus() {
		return masterUebergabeblattPVStatus;
	}

	public void setMasterUebergabeblattPVStatus(StatusType masterUebergabeblattPVStatus) {
		this.masterUebergabeblattPVStatus = masterUebergabeblattPVStatus;
	}

	public StatusType getUebergabeblattPVStatus() {
		return uebergabeblattPVStatus;
	}

	public void setUebergabeblattPVStatus(StatusType uebergabeblattPVStatus) {
		this.uebergabeblattPVStatus = uebergabeblattPVStatus;
	}

	public StatusType getBKonzeptEVUStatus() {
		return bKonzeptEVUStatus;
	}

	public void setBKonzeptEVUStatus(StatusType konzeptEVUStatus) {
		bKonzeptEVUStatus = konzeptEVUStatus;
	}

	public EVUGruppe getEvuGruppe() {
		return evuGruppe;
	}

	public void setEvuGruppe(EVUGruppe evugruppe) {
		this.evuGruppe = evugruppe;
	}

	public StatusType getZvfEntwurfStatus() {
		return zvfEntwurfStatus;
	}

	public void setZvfEntwurfStatus(StatusType zvfEntwurfStatus) {
		this.zvfEntwurfStatus = zvfEntwurfStatus;
	}

	public StatusType getStellungnahmeEVUStatus() {
		return stellungnahmeEVUStatus;
	}

	public void setStellungnahmeEVUStatus(StatusType stellungnahmeEVUStatus) {
		this.stellungnahmeEVUStatus = stellungnahmeEVUStatus;
	}

	public StatusType getZvFStatus() {
		return zvFStatus;
	}

	public void setZvFStatus(StatusType zvFStatus) {
		this.zvFStatus = zvFStatus;
	}

	public StatusType getFploStatus() {
		return fploStatus;
	}

	public void setFploStatus(StatusType fploStatus) {
		this.fploStatus = fploStatus;
	}

	public StatusType getEingabeGFD_ZStatus() {
		return eingabeGFD_ZStatus;
	}

	public void setEingabeGFD_ZStatus(StatusType eingabeGFD_ZStatus) {
		this.eingabeGFD_ZStatus = eingabeGFD_ZStatus;
	}

	public Date getStudieGrobkonzept() {
		return studieGrobkonzept;
	}

	public void setStudieGrobkonzept(Date studieGrobkonzept) {
		this.studieGrobkonzept = studieGrobkonzept;
	}

	public Date getZvfEntwurf() {
		return zvfEntwurf;
	}

	public void setZvfEntwurf(Date zvfEntwurf) {
		this.zvfEntwurf = zvfEntwurf;
	}

	public Date getStellungnahmeEVU() {
		return stellungnahmeEVU;
	}

	public void setStellungnahmeEVU(Date stellungnahmeEVU) {
		this.stellungnahmeEVU = stellungnahmeEVU;
	}

	public Date getZvF() {
		return zvF;
	}

	public void setZvF(Date zvF) {
		this.zvF = zvF;
	}

	public Date getFplo() {
		return fplo;
	}

	public void setFplo(Date fplo) {
		this.fplo = fplo;
	}

	public Date getEingabeGFD_Z() {
		return eingabeGFD_Z;
	}

	public void setEingabeGFD_Z(Date eingabeGFD_Z) {
		this.eingabeGFD_Z = eingabeGFD_Z;
	}

	public boolean isZvfEntwurfErforderlich() {
		return zvfEntwurfErforderlich;
	}

	public void setZvfEntwurfErforderlich(boolean zvfEntwurfErforderlich) {
		this.zvfEntwurfErforderlich = zvfEntwurfErforderlich;
	}

	public boolean isZvfErforderlich() {
		return zvfErforderlich;
	}

	public void setZvfErforderlich(boolean zvfErforderlich) {
		this.zvfErforderlich = zvfErforderlich;
	}

	public boolean isStellungnahmeEVUErforderlich() {
		return stellungnahmeEVUErforderlich;
	}

	public void setStellungnahmeEVUErforderlich(boolean stellungnahmeEVUErforderlich) {
		this.stellungnahmeEVUErforderlich = stellungnahmeEVUErforderlich;
	}

	/**
	 * @param masterUebergabeblattPVErforderlich
	 *            the masterUebergabeblattPVErforderlich to set
	 */
	public void setMasterUebergabeblattPVErforderlich(boolean masterUebergabeblattPVErforderlich) {
		this.masterUebergabeblattPVErforderlich = masterUebergabeblattPVErforderlich;
	}

	/**
	 * @return the masterUebergabeblattPVErforderlich
	 */
	public boolean isMasterUebergabeblattPVErforderlich() {
		return masterUebergabeblattPVErforderlich;
	}

	/**
	 * @param uebergabeblattPVErforderlich
	 *            the uebergabeblattPVErforderlich to set
	 */
	public void setUebergabeblattPVErforderlich(boolean uebergabeblattPVErforderlich) {
		this.uebergabeblattPVErforderlich = uebergabeblattPVErforderlich;
	}

	/**
	 * @return the uebergabeblattPVErforderlich
	 */
	public boolean isUebergabeblattPVErforderlich() {
		return uebergabeblattPVErforderlich;
	}

	public void setBKonzeptEVUErforderlich(boolean bKonzeptEVUErforderlich) {
		this.bKonzeptEVUErforderlich = bKonzeptEVUErforderlich;
	}

	public boolean isBKonzeptEVUErforderlich() {
		return bKonzeptEVUErforderlich;
	}

	public void setFploErforderlich(boolean fploErforderlich) {
		this.fploErforderlich = fploErforderlich;
	}

	public boolean isFploErforderlich() {
		return fploErforderlich;
	}

	public void setEingabeGFD_ZErforderlich(boolean eingabeGFD_ZErforderlich) {
		this.eingabeGFD_ZErforderlich = eingabeGFD_ZErforderlich;
	}

	public boolean isEingabeGFD_ZErforderlich() {
		return eingabeGFD_ZErforderlich;
	}

	// ////////
	// Methoden
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		TerminUebersichtPersonenverkehrsEVU pEVU = (TerminUebersichtPersonenverkehrsEVU) obj;
		return evuGruppe.equals(pEVU.evuGruppe);
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == evuGruppe ? 0 : evuGruppe.hashCode());
		return hash;
	}

	public static StatusType getStellungnahmeEVUStatus(
	    Set<TerminUebersichtPersonenverkehrsEVU> pevus) {
		StatusType result = StatusType.RED;

		for (TerminUebersichtPersonenverkehrsEVU pevu : pevus) {
			if (result != StatusType.RED) {
				result = pevu.getStellungnahmeEVUStatus();
			}
		}

		return result;
	}

	/**
	 * aktualisiert die TerminStatus-Properties basierend auf den SOLL/IST-Terminen
	 */
	public void setTerminStatus() {
		setZvfEntwurfStatus(Terminberechnung.getStatus(zvfEntwurfSoll, zvfEntwurf,
		    isZvfEntwurfErforderlich()));

		setStellungnahmeEVUStatus(Terminberechnung.getStatus(stellungnahmeEVUSoll,
		    stellungnahmeEVU, isStellungnahmeEVUErforderlich()));

		setZvFStatus(Terminberechnung.getStatus(zvFSoll, zvF, isZvfErforderlich()));

		setMasterUebergabeblattPVStatus(Terminberechnung.getStatus(masterUebergabeblattPVSoll,
		    masterUebergabeblattPV, isMasterUebergabeblattPVErforderlich()));

		setUebergabeblattPVStatus(Terminberechnung.getStatus(uebergabeblattPVSoll,
		    uebergabeblattPV, isUebergabeblattPVErforderlich()));

		setBKonzeptEVUStatus(Terminberechnung.getStatus(bKonzeptEVUSoll, bKonzeptEVU,
		    isBKonzeptEVUErforderlich()));

		setFploStatus(Terminberechnung.getStatus(fploSoll, fplo, isFploErforderlich()));

		setEingabeGFD_ZStatus(Terminberechnung.getStatus(eingabeGFD_ZSoll, eingabeGFD_Z,
		    isEingabeGFD_ZErforderlich()));
	}

	/**
	 * berechnet die SOLL-Termine neu wenn der Terminkette ein EVU zugeordnet ist und der jeweilige
	 * Termin noch nicht gesetzt wurde.
	 * 
	 * @param beginnDatum
	 * @param baumassnahmeArt
	 */
	public void setSollTermine(Date beginnDatum, Art baumassnahmeArt) {
		// diese Termine werden für Berechnungen verwendet
		// für die Anzeige/JSP gibt es Methoden in der Klasse Baumassnahme (getSollTerminePEVU,
		// getSollTermineBBP, getSollTermineGEVU)

		// Status nur berechnen, wenn EVU gesetzt
		if (getEvuGruppe() == null)
			return;

		MeilensteinService service = null;
		try {
			service = EasyServiceFactory.getInstance().createMeilensteinService();
		} catch (NullPointerException e) {
			service = new MeilensteinServiceImpl();
		}

		setZvfEntwurfSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.PEVU,
		    Meilensteinbezeichnungen.ZVF_ENTW));
		setZvFSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.PEVU,
		    Meilensteinbezeichnungen.ZVF));
		setStellungnahmeEVUSoll(service.getSollTermin(beginnDatum, baumassnahmeArt,
		    Schnittstelle.PEVU, Meilensteinbezeichnungen.STELLUNGN_EVU));
		setUebergabeblattPVSoll(service.getSollTermin(beginnDatum, baumassnahmeArt,
		    Schnittstelle.PEVU, Meilensteinbezeichnungen.UEB_PV));
		setFploSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.PEVU,
		    Meilensteinbezeichnungen.FPLO));
		setEingabeGFD_ZSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.PEVU,
		    Meilensteinbezeichnungen.EING_GFDZ));
		setBKonzeptEVUSoll(service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.PEVU,
		    Meilensteinbezeichnungen.BKONZ_EVU));

		if (baumassnahmeArt.equals(Art.A) || baumassnahmeArt.equals(Art.QS)
		    || baumassnahmeArt.equals(Art.KS)) {
			setMasterUebergabeblattPVSoll(service.getSollTermin(beginnDatum, baumassnahmeArt,
			    Schnittstelle.PEVU, Meilensteinbezeichnungen.M_UEB_PV));
		}
	}

	public Integer getVerbleibendeTage_masterUebergabeblattPV() {
		return calcVerbleibendeTage(new Date(), getMasterUebergabeblattPVSoll());
	}

	public Integer getVerbleibendeTage_uebergabeblattPV() {
		return calcVerbleibendeTage(new Date(), getUebergabeblattPVSoll());
	}

	public Integer getVerbleibendeTage_bKonzeptEVU() {
		return calcVerbleibendeTage(new Date(), getBKonzeptEVUSoll());
	}

	public Integer getVerbleibendeTage_studieGrobkonzept() {
		return calcVerbleibendeTage(new Date(), getStudieGrobkonzeptSoll());
	}

	public Integer getVerbleibendeTage_zvfEntwurf() {
		return calcVerbleibendeTage(new Date(), getZvfEntwurfSoll());
	}

	public Integer getVerbleibendeTage_stellungnahmeEVU() {
		return calcVerbleibendeTage(new Date(), getStellungnahmeEVUSoll());
	}

	public Integer getVerbleibendeTage_zvF() {
		return calcVerbleibendeTage(new Date(), getZvFSoll());
	}

	public Integer getVerbleibendeTage_fplo() {
		return calcVerbleibendeTage(new Date(), getFploSoll());
	}

	public Integer getVerbleibendeTage_eingabeGFD_Z() {
		return calcVerbleibendeTage(new Date(), getEingabeGFD_ZSoll());
	}

	private Integer calcVerbleibendeTage(Date ist, Date soll) throws IllegalArgumentException {

		// nur Objekte, die in der Datenbank gespeichert wurden, liefern gültige Daten bei der
		// Terminberechnung
		if (this.getId() == null) {
			return null;
		}

		Integer result = null;
		try {
			result = DateUtils.getDateDiffInDays(ist, soll);
		} catch (IllegalArgumentException ex) {
			Logger log = Logger.getLogger(this.getClass());

			if (log.isDebugEnabled()) {
				log.debug(ex);
			}
		}
		return result;
	}
}
