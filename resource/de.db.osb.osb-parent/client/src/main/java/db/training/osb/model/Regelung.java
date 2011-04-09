/**
 * 
 */
package db.training.osb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.training.easy.core.model.EasyPersistentObjectVc;

/**
 * @author michels
 * 
 */
@MappedSuperclass()
@SuppressWarnings("serial")
public abstract class Regelung extends EasyPersistentObjectVc {

	private Integer bbpRglNr;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Boolean betroffenSpfv;

	private Boolean betroffenSpnv;

	private Boolean betroffenSgv;

	@ManyToOne(optional = true)
	@JoinColumn(name = "bstVon_ID")
	private Betriebsstelle bstVon;

	@ManyToOne(optional = true)
	@JoinColumn(name = "bstBis_ID")
	private Betriebsstelle bstBis;

	/**
	 * gibt an, ob die Sperrung durchgegend von <code>zeitVon</code> bis <code>zeitBis</code> gilt
	 * (=true), oder ob die Sperrung unterbrochen wird und nur zu bestimmten Zeiten eines Tages gilt
	 */
	@Column(name = "durchgehend")
	private Boolean durchgehend;

	@Column(name = "fahrplanjahr", nullable = false, scale = 4)
	private Integer fahrplanjahr;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private Verkehrstageregelung vtr;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "vzgStrecke_ID")
	private VzgStrecke vzgStrecke;

	@Column(name = "schichtweise")
	private Boolean schichtweise;

	private Boolean ungueltig;

	/**
	 * Mindestangabe: dd.mm.yyyy, Maximalangabe: dd.mm.yyyy hh:mm
	 */
	@Column(name = "zeit_von")
	@Temporal(TemporalType.TIMESTAMP)
	private Date zeitVon;

	/**
	 * Mindestangabe: dd.mm.yyyy, Maximalangabe: dd.mm.yyyy hh:mm
	 */
	@Column(name = "zeit_bis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date zeitBis;

	@Column(name = "kommentar")
	@Lob()
	private String kommentar;

	@Column(name = "anztemp_ID")
	private String anzeigeTemplateId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vzgStreckeBis_ID")
	private VzgStrecke vzgStreckeBis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vzgStreckeUeber_ID")
	private VzgStrecke vzgStreckeUeber;

	@Column(name = "zeit_von_koordiniert")
	@Temporal(TemporalType.TIMESTAMP)
	private Date zeitVonKoordiniert;

	@Column(name = "zeit_bis_koordiniert")
	@Temporal(TemporalType.TIMESTAMP)
	private Date zeitBisKoordiniert;

	@ManyToOne
	@JoinColumn(name = "bstVonKoordiniert_ID")
	private Betriebsstelle bstVonKoordiniert;

	@ManyToOne
	@JoinColumn(name = "bstBisKoordiniert_ID")
	private Betriebsstelle bstBisKoordiniert;

	@ManyToOne(fetch = FetchType.LAZY)
	private SAPMassnahme massnahme;

	protected Regelung() {
	}

	protected Regelung(Date zeitVon, Date zeitBis) {
		this.zeitVon = zeitVon;
		this.zeitBis = zeitBis;
	}

	public Boolean getSchichtweise() {
		return schichtweise;
	}

	public void setSchichtweise(Boolean schichtweise) {
		this.schichtweise = schichtweise;
	}

	public Boolean getUngueltig() {
		return ungueltig;
	}

	public void setUngueltig(Boolean ungueltig) {
		this.ungueltig = ungueltig;
	}

	public Integer getBbpRglNr() {
		return bbpRglNr;
	}

	public void setBbpRglNr(Integer bbpRglNr) {
		this.bbpRglNr = bbpRglNr;
	}

	public String getBbpMassnahmeId() {
		return bbpMassnahmeId;
	}

	public void setBbpMassnahmeId(String bbpMassnahmeId) {
		this.bbpMassnahmeId = bbpMassnahmeId;
	}

	public Boolean getBetroffenSpfv() {
		return betroffenSpfv;
	}

	public void setBetroffenSpfv(Boolean betroffenSpfv) {
		this.betroffenSpfv = betroffenSpfv;
	}

	public Boolean getBetroffenSpnv() {
		return betroffenSpnv;
	}

	public void setBetroffenSpnv(Boolean betroffenSpnv) {
		this.betroffenSpnv = betroffenSpnv;
	}

	public Boolean getBetroffenSgv() {
		return betroffenSgv;
	}

	public void setBetroffenSgv(Boolean betroffenSgv) {
		this.betroffenSgv = betroffenSgv;
	}

	public Betriebsstelle getBstVon() {
		return bstVon;
	}

	public void setBstVon(Betriebsstelle bstVon) {
		this.bstVon = bstVon;
	}

	public Betriebsstelle getBstBis() {
		return bstBis;
	}

	public void setBstBis(Betriebsstelle bstBis) {
		this.bstBis = bstBis;
	}

	public Boolean getDurchgehend() {
		return durchgehend;
	}

	public void setDurchgehend(Boolean durchgehend) {
		this.durchgehend = durchgehend;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public SAPMassnahme getMassnahme() {
		return massnahme;
	}

	public void setMassnahme(SAPMassnahme massnahme) {
		this.massnahme = massnahme;
	}

	public Verkehrstageregelung getVtr() {
		return vtr;
	}

	public void setVtr(Verkehrstageregelung vtr) {
		this.vtr = vtr;
	}

	public VzgStrecke getVzgStrecke() {
		return vzgStrecke;
	}

	public void setVzgStrecke(VzgStrecke vzgStrecke) {
		this.vzgStrecke = vzgStrecke;
	}

	public Date getZeitVon() {
		return zeitVon;
	}

	public void setZeitVon(Date zeitVon) {
		this.zeitVon = zeitVon;
	}

	public Date getZeitBis() {
		return zeitBis;
	}

	public void setZeitBis(Date zeitBis) {
		this.zeitBis = zeitBis;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getAnzeigeTemplateId() {
		return anzeigeTemplateId;
	}

	public void setAnzeigeTemplateId(String anzeigeTemplateId) {
		this.anzeigeTemplateId = anzeigeTemplateId;
	}

	public VzgStrecke getVzgStreckeBis() {
		return vzgStreckeBis;
	}

	public void setVzgStreckeBis(VzgStrecke vzgStreckeBis) {
		this.vzgStreckeBis = vzgStreckeBis;
	}

	public VzgStrecke getVzgStreckeUeber() {
		return vzgStreckeUeber;
	}

	public void setVzgStreckeUeber(VzgStrecke vzgStreckeUeber) {
		this.vzgStreckeUeber = vzgStreckeUeber;
	}

	public Date getZeitVonKoordiniert() {
		return zeitVonKoordiniert;
	}

	public void setZeitVonKoordiniert(Date zeitVonKoordiniert) {
		this.zeitVonKoordiniert = zeitVonKoordiniert;
	}

	public Date getZeitBisKoordiniert() {
		return zeitBisKoordiniert;
	}

	public void setZeitBisKoordiniert(Date zeitBisKoordiniert) {
		this.zeitBisKoordiniert = zeitBisKoordiniert;
	}

	public Betriebsstelle getBstVonKoordiniert() {
		return bstVonKoordiniert;
	}

	public void setBstVonKoordiniert(Betriebsstelle bstVonKoordiniert) {
		this.bstVonKoordiniert = bstVonKoordiniert;
	}

	public Betriebsstelle getBstBisKoordiniert() {
		return bstBisKoordiniert;
	}

	public void setBstBisKoordiniert(Betriebsstelle bstBisKoordiniert) {
		this.bstBisKoordiniert = bstBisKoordiniert;
	}

}
