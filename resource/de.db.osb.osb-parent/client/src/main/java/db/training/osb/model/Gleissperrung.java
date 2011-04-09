package db.training.osb.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts.util.MessageResources;

import db.training.hibernate.history.Historizable;
import db.training.osb.model.babett.monitoring.Grund;
import db.training.osb.model.babett.monitoring.Realisierungsgrad;
import db.training.osb.model.babett.monitoring.Status;
import db.training.osb.model.enums.SperrungAuswirkung;

/**
 * beschreibt einen Sperrzustand bzw. eine Betriebsweise einer Maßnahme
 * 
 * @author michels
 * 
 */
@Entity
@Table(name = "gleissperrung")
@SuppressWarnings("serial")
public class Gleissperrung extends Regelung implements Historizable {

	/**
	 * stellt die Reihenfolge der Datensätze für die Zuordnung gegenüber der Maßnahme dar
	 */
	@Column(name = "lfd_nr", updatable = false)
	private int lfdNr;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Buendel> buendel;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Baustelle> baustellen;

	@Column(name = "km_von")
	private Float kmVon;

	@Column(name = "km_bis")
	private Float kmBis;

	@Column(name = "sig_weiche_von")
	private String sigWeicheVon;

	@Column(name = "sig_weiche_bis")
	private String sigWeicheBis;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private Betriebsweise betriebsweise;

	/**
	 * gibt die Dauer des Sperrbedarfs an. Die Maßeinheit, in der der Sperrbedarf angegeben ist,
	 * wird in <code>sperrbedarfEinheit</code> angegeben.
	 */
	@Column(name = "sperrbedarf")
	private Float sperrbedarf;

	@Column(name = "sperrpausenbedarf_esp")
	private Integer sperrpausenbedarfEsp;

	@Column(name = "sperrpausenbedarf_tsp")
	private Integer sperrpausenbedarfTsp;

	@Column(name = "sperrpausenbedarf_bfgl")
	private Integer sperrpausenbedarfBfGl;

	@Column(name = "auswirkung")
	@Enumerated(value = EnumType.STRING)
	private SperrungAuswirkung auswirkung;

	@Column(name = "bau_lue")
	private Boolean bauLue;

	@Column(name = "bezeichnung")
	private String bezeichnung;

	/**
	 * <ul>
	 * <li>0 - eingleisige Strecke; Pfeil in beide Richtungen <br />
	 * (&lt; ESP / TSP &gt;)</li>
	 * <li>1 - Richtungsgleis; Pfeil nach rechts <br />
	 * (ESP / TSP &gt;)</li>
	 * <li>2 - Gegenrichtung; Pfeil nach links <br />
	 * (&lt; ESP / TSP)</li>
	 * </ul>
	 */
	@Column(name = "richtungskennzahl")
	private Integer richtungsKennzahl;

	/**
	 * Baulagegeschwindigkeit
	 */
	@Column(name = "geschwindigkeit_bau_la")
	private Double geschwindigkeitBauLa;

	@Column(name = "typ_sicherungsleistung")
	private String typSicherungsleistung;

	@Column(nullable = true)
	private Boolean vorschlagLisba;

	@Column(nullable = true)
	private Integer geschwindigkeitVzg;

	@Column(length = 32, nullable = true)
	private String ergaenzungBetriebsweise;

	@ManyToOne
	private Fahrplanregelung fahrplanregelung;

	@Column(scale = 4, precision = 1)
	private Float fzvMusterzug;

	@Temporal(TemporalType.TIMESTAMP)
	private Date monitoringKbbZvFEntwurf;

	@Temporal(TemporalType.TIMESTAMP)
	private Date monitoringKbbZvFEndstueck;

	@Temporal(TemporalType.TIMESTAMP)
	private Date monitoringKbbUebSpv;

	@Temporal(TemporalType.TIMESTAMP)
	private Date monitoringKbbUebSgv;

	@Temporal(TemporalType.TIMESTAMP)
	private Date monitoringKbbStatusmeldung;

	@ManyToOne(optional = true)
	private Grund monitoringGrund;

	@ManyToOne(optional = true)
	private Status monitoringStatus;

	@ManyToOne(optional = true)
	private Realisierungsgrad monitoringRealisierunggrad;

	private Boolean ausregelungNfpl;

	private Boolean grundentlastungNfpl;

	private boolean deleted;

	public Gleissperrung() {
	}

	public Gleissperrung(Date zeitVon, Date zeitBis) {
		super(zeitVon, zeitBis);
	}

	public Set<Buendel> getBuendel() {
		return buendel;
	}

	public void setBuendel(Set<Buendel> buendel) {
		this.buendel = buendel;
	}

	public Set<Baustelle> getBaustellen() {
		return this.baustellen;
	}

	public void setBaustellen(Set<Baustelle> baustellen) {
		this.baustellen = baustellen;
	}

	public int getLfdNr() {
		return lfdNr;
	}

	public void setLfdNr(int lfdNr) {
		this.lfdNr = lfdNr;
	}

	public Float getKmVon() {
		return kmVon;
	}

	public void setKmVon(Float kmVon) {
		this.kmVon = kmVon;
	}

	public Float getKmBis() {
		return kmBis;
	}

	public void setKmBis(Float kmBis) {
		this.kmBis = kmBis;
	}

	public String getSigWeicheVon() {
		return sigWeicheVon;
	}

	public void setSigWeicheVon(String sigWeicheVon) {
		this.sigWeicheVon = sigWeicheVon;
	}

	public String getSigWeicheBis() {
		return sigWeicheBis;
	}

	public void setSigWeicheBis(String sigWeicheBis) {
		this.sigWeicheBis = sigWeicheBis;
	}

	public Betriebsweise getBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(Betriebsweise betriebsweise) {
		this.betriebsweise = betriebsweise;
	}

	public Float getSperrbedarf() {
		return sperrbedarf;
	}

	public void setSperrbedarf(Float sperrbedarf) {
		this.sperrbedarf = sperrbedarf;
	}

	public Integer getSperrpausenbedarfEsp() {
		return sperrpausenbedarfEsp;
	}

	public void setSperrpausenbedarfEsp(Integer sperrpausenbedarfEsp) {
		this.sperrpausenbedarfEsp = sperrpausenbedarfEsp;
	}

	public Integer getSperrpausenbedarfTsp() {
		return sperrpausenbedarfTsp;
	}

	public void setSperrpausenbedarfTsp(Integer sperrpausenbedarfTsp) {
		this.sperrpausenbedarfTsp = sperrpausenbedarfTsp;
	}

	public Integer getSperrpausenbedarfBfGl() {
		return sperrpausenbedarfBfGl;
	}

	public void setSperrpausenbedarfBfGl(Integer sperrpausenbedarfBfGl) {
		this.sperrpausenbedarfBfGl = sperrpausenbedarfBfGl;
	}

	public SperrungAuswirkung getAuswirkung() {
		return auswirkung;
	}

	public void setAuswirkung(SperrungAuswirkung auswirkung) {
		this.auswirkung = auswirkung;
	}

	public Boolean getBauLue() {
		return bauLue;
	}

	public void setBauLue(Boolean bauLue) {
		this.bauLue = bauLue;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public Integer getRichtungsKennzahl() {
		return richtungsKennzahl;
	}

	public void setRichtungsKennzahl(Integer richtungsKennzahl) {
		this.richtungsKennzahl = richtungsKennzahl;
	}

	public Double getGeschwindigkeitBauLa() {
		return geschwindigkeitBauLa;
	}

	public void setGeschwindigkeitBauLa(Double geschwindigkeitBauLa) {
		this.geschwindigkeitBauLa = geschwindigkeitBauLa;
	}

	public String getTypSicherungsleistung() {
		return typSicherungsleistung;
	}

	public void setTypSicherungsleistung(String typSicherungsleistung) {
		this.typSicherungsleistung = typSicherungsleistung;
	}

	public Boolean getVorschlagLisba() {
		return vorschlagLisba;
	}

	public void setVorschlagLisba(Boolean vorschlagLisba) {
		this.vorschlagLisba = vorschlagLisba;
	}

	public Integer getGeschwindigkeitVzg() {
		return geschwindigkeitVzg;
	}

	public void setGeschwindigkeitVzg(Integer geschwindigkeitVzg) {
		this.geschwindigkeitVzg = geschwindigkeitVzg;
	}

	public String getErgaenzungBetriebsweise() {
		return ergaenzungBetriebsweise;
	}

	public void setErgaenzungBetriebsweise(String ergaenzungBetriebsweise) {
		this.ergaenzungBetriebsweise = ergaenzungBetriebsweise;
	}

	public Fahrplanregelung getFahrplanregelung() {
		return fahrplanregelung;
	}

	public void setFahrplanregelung(Fahrplanregelung fahrplanregelung) {
		this.fahrplanregelung = fahrplanregelung;
	}

	public Float getFzvMusterzug() {
		return fzvMusterzug;
	}

	public void setFzvMusterzug(Float fzvMusterzug) {
		this.fzvMusterzug = fzvMusterzug;
	}

	public Date getMonitoringKbbZvFEntwurf() {
		return monitoringKbbZvFEntwurf;
	}

	public void setMonitoringKbbZvFEntwurf(Date monitoringKbbZvFEntwurf) {
		this.monitoringKbbZvFEntwurf = monitoringKbbZvFEntwurf;
	}

	public Date getMonitoringKbbZvFEndstueck() {
		return monitoringKbbZvFEndstueck;
	}

	public void setMonitoringKbbZvFEndstueck(Date monitoringKbbZvFEndstueck) {
		this.monitoringKbbZvFEndstueck = monitoringKbbZvFEndstueck;
	}

	public Date getMonitoringKbbUebSpv() {
		return monitoringKbbUebSpv;
	}

	public void setMonitoringKbbUebSpv(Date monitoringKbbUebSpv) {
		this.monitoringKbbUebSpv = monitoringKbbUebSpv;
	}

	public Date getMonitoringKbbUebSgv() {
		return monitoringKbbUebSgv;
	}

	public void setMonitoringKbbUebSgv(Date monitoringKbbUebSgv) {
		this.monitoringKbbUebSgv = monitoringKbbUebSgv;
	}

	public Date getMonitoringKbbStatusmeldung() {
		return monitoringKbbStatusmeldung;
	}

	public void setMonitoringKbbStatusmeldung(Date monitoringKbbStatusmeldung) {
		this.monitoringKbbStatusmeldung = monitoringKbbStatusmeldung;
	}

	public Grund getMonitoringGrund() {
		return monitoringGrund;
	}

	public void setMonitoringGrund(Grund monitoringGrund) {
		this.monitoringGrund = monitoringGrund;
	}

	public Status getMonitoringStatus() {
		return monitoringStatus;
	}

	public void setMonitoringStatus(Status monitoringStatus) {
		this.monitoringStatus = monitoringStatus;
	}

	public Realisierungsgrad getMonitoringRealisierunggrad() {
		return monitoringRealisierunggrad;
	}

	public void setMonitoringRealisierunggrad(Realisierungsgrad monitoringRealisierunggrad) {
		this.monitoringRealisierunggrad = monitoringRealisierunggrad;
	}

	public Boolean getAusregelungNfpl() {
		return ausregelungNfpl;
	}

	public void setAusregelungNfpl(Boolean ausregelungNfpl) {
		this.ausregelungNfpl = ausregelungNfpl;
	}

	public Boolean getGrundentlastungNfpl() {
		return grundentlastungNfpl;
	}

	public void setGrundentlastungNfpl(Boolean grundentlastungNfpl) {
		this.grundentlastungNfpl = grundentlastungNfpl;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Transient
	public boolean isZeitraeumeUnterschiedlich() {
		if (((getZeitVon() == null && getZeitVonKoordiniert() != null) || (getZeitVon() != null && !getZeitVon()
		    .equals(getZeitVonKoordiniert())))
		    || ((getZeitBis() == null && getZeitBisKoordiniert() != null) || (getZeitBis() != null && !getZeitBis()
		        .equals(getZeitBisKoordiniert())))) {
			return true;
		}
		return false;
	}

	@Transient
	public boolean isBetriebsstellenUnterschiedlich() {
		if (((getBstVon() == null && getBstVonKoordiniert() != null) || (getBstVon() != null && !getBstVon()
		    .equals(getBstVonKoordiniert())))
		    || ((getBstBis() == null && getBstBisKoordiniert() != null) || (getBstBis() != null && !getBstBis()
		        .equals(getBstBisKoordiniert())))) {
			return true;
		}
		return false;
	}

	@Transient
	public String getSperrpausenbedarf() {
		MessageResources msgRes = MessageResources.getMessageResources("MessageResources");
		StringBuilder sb = new StringBuilder();
		if (getSperrpausenbedarfEsp() != null) {
			sb.append(getSperrpausenbedarfEsp()).append(" ");
			sb.append(msgRes.getMessage("sperrpausenbedarf.art.ESP"));
		} else if (getSperrpausenbedarfTsp() != null) {
			sb.append(getSperrpausenbedarfTsp()).append(" ");
			sb.append(msgRes.getMessage("sperrpausenbedarf.art.TSP"));
		} else if (getSperrpausenbedarfBfGl() != null) {
			sb.append(getSperrpausenbedarfBfGl()).append(" ");
			sb.append(msgRes.getMessage("sperrpausenbedarf.art.SPERR_BF_GL"));
		}
		return sb.toString();
	}

}