package db.training.bob.model;

import java.util.Date;

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
@Table(name = "terminuebersicht_gevu")
public class TerminUebersichtGueterverkehrsEVU extends EasyPersistentObject implements
    TerminUebersichtEVU, Historizable {

	@ManyToOne(fetch = FetchType.EAGER)
	private EVUGruppe evuGruppe;

	/**
	 * dient nach der Migration von #425 als Backup für die Verknüpfung zwischen Baumaßnahme und
	 * Schnittstelle
	 * 
	 * @deprecated nicht verwenden
	 */
	@SuppressWarnings("unused")
	@Deprecated
	@Column(name = "baumassnahme_id")
	private Integer baumassnahmeId;

	// //////
	// Soll Termine / Meilensteine
	@Column(name = "masteruebgv_soll")
	@Temporal(TemporalType.DATE)
	private Date masterUebergabeblattGVSoll;

	@Column(name = "uebgv_soll")
	@Temporal(TemporalType.DATE)
	private Date uebergabeblattGVSoll;

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
	@Column(name = "masteruebgv")
	@Temporal(TemporalType.DATE)
	private Date masterUebergabeblattGV;

	@Column(name = "uebgv")
	@Temporal(TemporalType.DATE)
	private Date uebergabeblattGV;

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
	private StatusType masterUebergabeblattGVStatus;

	@Transient
	private StatusType uebergabeblattGVStatus;

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

	@Column(name = "masteruebgverforderlich", nullable = false)
	private boolean masterUebergabeblattGVErforderlich;

	@Column(name = "uebgverforderlich", nullable = false)
	private boolean uebergabeblattGVErforderlich;

	@Column(name = "zvfentwurferforderlich", nullable = false)
	private boolean zvfEntwurfErforderlich;

	@Column(name = "stellungnahmeevuerforderlich", nullable = false)
	private boolean stellungnahmeEVUErforderlich;

	@Column(name = "zvferforderlich", nullable = false)
	private boolean zvfErforderlich;

	@Column(name = "fploerforderlich", nullable = false)
	private boolean fploErforderlich;

	@Column(name = "eingabegfdzerforderlich", nullable = false)
	private boolean eingabeGFD_ZErforderlich;

	public TerminUebersichtGueterverkehrsEVU() {
		super();
		masterUebergabeblattGVStatus = StatusType.NEUTRAL;
		uebergabeblattGVStatus = StatusType.NEUTRAL;
		zvfEntwurfStatus = StatusType.NEUTRAL;
		stellungnahmeEVUStatus = StatusType.NEUTRAL;
		zvFStatus = StatusType.NEUTRAL;
		fploStatus = StatusType.NEUTRAL;
		eingabeGFD_ZStatus = StatusType.NEUTRAL;
	}

	public TerminUebersichtGueterverkehrsEVU(Art baumassnahmenart) {
		this();
		setErforderlichHaekchen(baumassnahmenart);
	}

	public void setErforderlichHaekchen(Art art) {
		setZvfEntwurfErforderlich(true);
		setFploErforderlich(true);

		if (art == Art.A || art == Art.KS) {
			setStellungnahmeEVUErforderlich(true);
			setZvfErforderlich(true);
			setMasterUebergabeblattGVErforderlich(false);
			setUebergabeblattGVErforderlich(false);
			setEingabeGFD_ZErforderlich(false);
		} else if (art == Art.B) {
			setStellungnahmeEVUErforderlich(false);
			setZvfErforderlich(false);
			setMasterUebergabeblattGVErforderlich(false);
			setUebergabeblattGVErforderlich(false);
			setEingabeGFD_ZErforderlich(false);
		} else if (art == Art.QS) {
			setStellungnahmeEVUErforderlich(true);
			setZvfErforderlich(true);
			setMasterUebergabeblattGVErforderlich(true);
			setUebergabeblattGVErforderlich(true);
			setEingabeGFD_ZErforderlich(true);
		}
	}

	// //////////
	// Properties
	public Date getMasterUebergabeblattGVSoll() {
		return masterUebergabeblattGVSoll;
	}

	public void setMasterUebergabeblattGVSoll(Date masterUebergabeblattGVSoll) {
		this.masterUebergabeblattGVSoll = masterUebergabeblattGVSoll;
	}

	public Date getUebergabeblattGVSoll() {
		return uebergabeblattGVSoll;
	}

	public void setUebergabeblattGVSoll(Date uebergabeblattGVSoll) {
		this.uebergabeblattGVSoll = uebergabeblattGVSoll;
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

	public Date getMasterUebergabeblattGV() {
		return masterUebergabeblattGV;
	}

	public void setMasterUebergabeblattGV(Date masterUebergabeblattGV) {
		this.masterUebergabeblattGV = masterUebergabeblattGV;
	}

	public Date getUebergabeblattGV() {
		return uebergabeblattGV;
	}

	public void setUebergabeblattGV(Date uebergabeblattGV) {
		this.uebergabeblattGV = uebergabeblattGV;
	}

	public StatusType getMasterUebergabeblattGVStatus() {
		return masterUebergabeblattGVStatus;
	}

	public void setMasterUebergabeblattGVStatus(StatusType masterUebergabeblattGVStatus) {
		this.masterUebergabeblattGVStatus = masterUebergabeblattGVStatus;
	}

	public StatusType getUebergabeblattGVStatus() {
		return uebergabeblattGVStatus;
	}

	public void setUebergabeblattGVStatus(StatusType uebergabeblattGVStatus) {
		this.uebergabeblattGVStatus = uebergabeblattGVStatus;
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

	public boolean isStellungnahmeEVUErforderlich() {
		return stellungnahmeEVUErforderlich;
	}

	public void setStellungnahmeEVUErforderlich(boolean stellungnahmeEVUErforderlich) {
		this.stellungnahmeEVUErforderlich = stellungnahmeEVUErforderlich;
	}

	/**
	 * @param uebergabeblattGVErforderlich
	 *            the uebergabeblattGVErforderlich to set
	 */
	public void setUebergabeblattGVErforderlich(boolean uebergabeblattGVErforderlich) {
		this.uebergabeblattGVErforderlich = uebergabeblattGVErforderlich;
	}

	/**
	 * @return the uebergabeblattGVErforderlich
	 */
	public boolean isUebergabeblattGVErforderlich() {
		return uebergabeblattGVErforderlich;
	}

	/**
	 * @param masterUebergabeblattGVErforderlich
	 *            the masterUebergabeblattGVErforderlich to set
	 */
	public void setMasterUebergabeblattGVErforderlich(boolean masterUebergabeblattGVErforderlich) {
		this.masterUebergabeblattGVErforderlich = masterUebergabeblattGVErforderlich;
	}

	/**
	 * @return the masterUebergabeblattGVErforderlich
	 */
	public boolean isMasterUebergabeblattGVErforderlich() {
		return masterUebergabeblattGVErforderlich;
	}

	public boolean isZvfErforderlich() {
		return zvfErforderlich;
	}

	public void setZvfErforderlich(boolean zvfErforderlich) {
		this.zvfErforderlich = zvfErforderlich;
	}

	public boolean isFploErforderlich() {
		return fploErforderlich;
	}

	public void setFploErforderlich(boolean fploErforderlich) {
		this.fploErforderlich = fploErforderlich;
	}

	public boolean isEingabeGFD_ZErforderlich() {
		return eingabeGFD_ZErforderlich;
	}

	public void setEingabeGFD_ZErforderlich(boolean eingabeGFD_ZErforderlich) {
		this.eingabeGFD_ZErforderlich = eingabeGFD_ZErforderlich;
	}

	// ////////
	// Methoden
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		TerminUebersichtGueterverkehrsEVU pEVU = (TerminUebersichtGueterverkehrsEVU) obj;
		return evuGruppe.equals(pEVU.evuGruppe);
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == evuGruppe ? 0 : evuGruppe.hashCode());
		return hash;
	}

	public void setTerminStatus() {
		setZvfEntwurfStatus(Terminberechnung.getStatus(zvfEntwurfSoll, zvfEntwurf,
		    isZvfEntwurfErforderlich()));

		setStellungnahmeEVUStatus(Terminberechnung.getStatus(stellungnahmeEVUSoll,
		    stellungnahmeEVU, isStellungnahmeEVUErforderlich()));

		setZvFStatus(Terminberechnung.getStatus(zvFSoll, zvF, isZvfErforderlich()));

		setMasterUebergabeblattGVStatus(Terminberechnung.getStatus(masterUebergabeblattGVSoll,
		    masterUebergabeblattGV, isMasterUebergabeblattGVErforderlich()));

		setUebergabeblattGVStatus(Terminberechnung.getStatus(uebergabeblattGVSoll,
		    uebergabeblattGV, isUebergabeblattGVErforderlich()));

		setFploStatus(Terminberechnung.getStatus(fploSoll, fplo, isFploErforderlich()));

		setEingabeGFD_ZStatus(Terminberechnung.getStatus(eingabeGFD_ZSoll, eingabeGFD_Z,
		    isEingabeGFD_ZErforderlich()));

	}

	public void setSollTermine(Date beginnDatum, Art baumassnahmeArt) {
		// Status nur berechnen, wenn EVU gesetzt
		if (getEvuGruppe() == null)
			return;

		MeilensteinService service = null;
		try {
			service = EasyServiceFactory.getInstance().createMeilensteinService();
		} catch (NullPointerException e) {
			service = new MeilensteinServiceImpl();
		}

		zvfEntwurfSoll = service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.GEVU,
		    Meilensteinbezeichnungen.ZVF_ENTW);
		zvFSoll = service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.GEVU,
		    Meilensteinbezeichnungen.ZVF);
		stellungnahmeEVUSoll = service.getSollTermin(beginnDatum, baumassnahmeArt,
		    Schnittstelle.GEVU, Meilensteinbezeichnungen.STELLUNGN_EVU);
		uebergabeblattGVSoll = service.getSollTermin(beginnDatum, baumassnahmeArt,
		    Schnittstelle.GEVU, Meilensteinbezeichnungen.UEB_GV);
		fploSoll = service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.GEVU,
		    Meilensteinbezeichnungen.FPLO);
		eingabeGFD_ZSoll = service.getSollTermin(beginnDatum, baumassnahmeArt, Schnittstelle.GEVU,
		    Meilensteinbezeichnungen.EING_GFDZ);

		if (baumassnahmeArt.equals(Art.A) || baumassnahmeArt.equals(Art.QS)
		    || baumassnahmeArt.equals(Art.KS)) {
			masterUebergabeblattGVSoll = service.getSollTermin(beginnDatum, baumassnahmeArt,
			    Schnittstelle.GEVU, Meilensteinbezeichnungen.M_UEB_GV);
		}
	}

	public Integer getVerbleibendeTage_masterUebergabeblattGV() {
		return calcVerbleibendeTage(new Date(), getMasterUebergabeblattGVSoll());
	}

	public Integer getVerbleibendeTage_uebergabeblattGV() {
		return calcVerbleibendeTage(new Date(), getUebergabeblattGVSoll());
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

	public Integer getVerbleibendeTage_eingabeGFD_Z() throws IllegalArgumentException {
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