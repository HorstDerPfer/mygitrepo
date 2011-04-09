package db.training.bob.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "terminuebersicht")
public abstract class TerminUebersicht extends EasyPersistentObject implements Historizable {

	public TerminUebersicht() {
		zvfEntwurfStatus = StatusType.NEUTRAL;
		stellungnahmeEVUStatus = StatusType.NEUTRAL;
		zvFStatus = StatusType.NEUTRAL;
		fploStatus = StatusType.NEUTRAL;
		eingabeGFD_ZStatus = StatusType.NEUTRAL;
	}

	// nur KS
	@Column(name = "studiegrobkonzept")
	@Temporal(TemporalType.DATE)
	private Date studieGrobkonzept;

	@Column(name = "zvfentwurf")
	@Temporal(TemporalType.DATE)
	private Date zvfEntwurf;

	@Transient
	private StatusType zvfEntwurfStatus;

	@Column(name = "stellungnahmeevu")
	@Temporal(TemporalType.DATE)
	private Date stellungnahmeEVU;

	@Transient
	private StatusType stellungnahmeEVUStatus;

	@Column(name = "zvf")
	@Temporal(TemporalType.DATE)
	private Date zvF;

	@Transient
	private StatusType zvFStatus;

	@Column(name = "fplo")
	@Temporal(TemporalType.DATE)
	private Date fplo;

	@Transient
	private StatusType fploStatus;

	// nur KS und QS
	@Column(name = "eingabegfdz")
	@Temporal(TemporalType.DATE)
	private Date eingabeGFD_Z;

	@Transient
	private StatusType eingabeGFD_ZStatus;

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

	// Was tut diese Funktion? (mst)
	// public void setTerminStatus(Date beginnDatum) {
	// Date soll = TerminBerechnung.getZvfEntwurfSoll(beginnDatum);
	// setZvfEntwurfStatus(TerminBerechnung.getStatus(soll, zvfEntwurf, null));
	// soll = TerminBerechnung.getStellungnahmeEvuSoll(beginnDatum);
	// setStellungnahmeEVUStatus(TerminBerechnung.getStatus(soll, stellungnahmeEVU, null));
	// soll = TerminBerechnung.getZvfSoll(beginnDatum);
	// setZvFStatus(TerminBerechnung.getStatus(soll, zvF, null));
	// soll = TerminBerechnung.getFploSoll(beginnDatum);
	// setFploStatus(TerminBerechnung.getStatus(soll, fplo, null));
	// soll = TerminBerechnung.getEingabeGFDZSoll(beginnDatum);
	// setEingabeGFD_ZStatus(TerminBerechnung.getStatus(soll, eingabeGFD_Z, null));
	// }
}
