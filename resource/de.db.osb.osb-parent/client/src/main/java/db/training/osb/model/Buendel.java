package db.training.osb.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.hibernate.history.Historizable;

@Entity
@Table(name = "buendel")
public class Buendel extends EasyPersistentObjectVc implements Historizable {

	private static final long serialVersionUID = -6702412498444830852L;

	@ManyToMany(mappedBy = "buendel")
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<Gleissperrung> gleissperrungen;

	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<MasterBuendel> masterBuendel = new HashSet<MasterBuendel>();

	@ManyToMany(mappedBy = "buendel", fetch = FetchType.LAZY)
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<Fahrplanregelung> fahrplanregelungen = new HashSet<Fahrplanregelung>();

	@ManyToOne(fetch = FetchType.EAGER)
	private Betriebsweise betriebsweise;

	/**
	 * @deprecated Wird noch fuer Migration von Hr. Wittmann benoetigt (10.09.2010)
	 */
	@Column(name = "betriebsweise")
	private String betriebsweise_unused;

	@Column(name = "vorschlag_aufnahme_pp")
	private Boolean vorschlagAufnahmePP;

	@Column(name = "bestimmung_aufnahme_pp")
	private Boolean bestimmungAufnahmePP;

	@Column(name = "zeitraum_start_geplant")
	@Temporal(TemporalType.DATE)
	private Date durchfuehrungsZeitraumStartGeplant;

	@Column(name = "zeitraum_ende_geplant")
	@Temporal(TemporalType.DATE)
	private Date durchfuehrungsZeitraumEndeGeplant;

	@Column(name = "zeitraum_start_koord")
	@Temporal(TemporalType.DATE)
	private Date durchfuehrungsZeitraumStartKoordiniert;

	@Column(name = "zeitraum_ende_koord")
	@Temporal(TemporalType.DATE)
	private Date durchfuehrungsZeitraumEndeKoordiniert;

	@Column(name = "baukosten_esp")
	private Double baukostenESP;

	@Column(name = "baukosten_tsp")
	private Double baukostenTSP;

	@Column(name = "EIU_TSP")
	private Double eiuTsp;

	@Column(name = "Netz_TSP")
	private Double netzTsp;

	@Column(name = "Prod_TSP")
	private Double prodTsp;

	@Column(name = "EVU_TSP")
	private Double evuTsp;

	@Column(name = "SPFV_TSP_Gesamt")
	private Double spfvTspGesamt;

	@Column(name = "SPFV_TSP_Kosten")
	private Double spfvTspKosten;

	@Column(name = "SPFV_TSP_Umsatz")
	private Double spfvTspUmsatz;

	@Column(name = "SPNV_TSP_Gesamt")
	private Double spnvTspGesamt;

	@Column(name = "SPNV_TSP_Kosten")
	private Double spnvTspKosten;

	@Column(name = "SPNV_TSP_Umsatz")
	private Double spnvTspUmsatz;

	@Column(name = "SGV_TSP_Gesamt")
	private Double sgvTspGesamt;

	@Column(name = "SGV_TSP_Kosten")
	private Double sgvTspKosten;

	@Column(name = "SGV_TSP_Umsatz")
	private Double sgvTspUmsatz;

	@Column(name = "EIU_ESP")
	private Double eiuEsp;

	@Column(name = "Netz_ESP")
	private Double netzEsp;

	@Column(name = "Prod_ESP")
	private Double prodEsp;

	@Column(name = "EVU_ESP")
	private Double evuEsp;

	@Column(name = "SPFV_ESP_Gesamt")
	private Double spfvEspGesamt;

	@Column(name = "SPFV_ESP_Kosten")
	private Double spfvEspKosten;

	@Column(name = "SPFV_ESP_Umsatz")
	private Double spfvEspUmsatz;

	@Column(name = "SPNV_ESP_Gesamt")
	private Double spnvEspGesamt;

	@Column(name = "SPNV_ESP_Kosten")
	private Double spnvEspKosten;

	@Column(name = "SPNV_ESP_Umsatz")
	private Double spnvEspUmsatz;

	@Column(name = "SGV_ESP_Gesamt")
	private Double sgvEspGesamt;

	@Column(name = "SGV_ESP_Kosten")
	private Double sgvEspKosten;

	@Column(name = "SGV_ESP_Umsatz")
	private Double sgvEspUmsatz;

	@Column(name = "EIU_VkS")
	private Integer eiuVkS;

	@Column(name = "Netz_VkS")
	private Double netzVkS;

	@Column(name = "Prod_VkS")
	private Double prodVkS;

	@Column(name = "EVU_VkS")
	private Double evuVkS;

	@Column(name = "SPFV_VkS_Gesamt")
	private Double spfvVkSGesamt;

	@Column(name = "SPFV_VkS_Kosten")
	private Double spfvVkSKosten;

	@Column(name = "SPFV_VkS_Umsatz")
	private Double spfvVkSUmsatz;

	@Column(name = "SPNV_VkS_Gesamt")
	private Double spnvVkSGesamt;

	@Column(name = "SPNV_VkS_Kosten")
	private Double spnvVkSKosten;

	@Column(name = "SPNV_VkS_Umsatz")
	private Double spnvVkSUmsatz;

	@Column(name = "SGV_VkS_Gesamt")
	private Double sgvVkSGesamt;

	@Column(name = "SGV_VkS_Kosten")
	private Double sgvVkSKosten;

	@Column(name = "SGV_VkS_Umsatz")
	private Double sgvVkSUmsatz;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private VzgStrecke hauptStrecke;

	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade( { CascadeType.SAVE_UPDATE })
	private Set<VzgStrecke> weitereStrecken = new HashSet<VzgStrecke>();

	@Column(name = "fahrplanjahr", nullable = false, length = 4)
	private Integer fahrplanjahr;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Regionalbereich regionalbereich;

	/**
	 * laufende Nummer in Abhängigkeit von Regionalbereich und Fahrplanjahr, wird beim Speichern
	 * automatisch gesetzt
	 */
	@Column(name = "lfdNr", nullable = false)
	private Integer lfdNr;

	@Column(name = "buendel_name")
	private String buendelName;

	@Column(name = "szb_betriebsweise_tsp")
	private Double sperrzeitbedarfBuendelsBetreibswesieTSP;

	@Column(name = "szb_betriebsweise_esp")
	private Double sperrzeitbedarfBuendelsBetreibswesieESP;

	@Column(name = "szb_betriebsweise_vks")
	private Double sperrzeitbedarfBuendelsBetreibswesievKS;

	@Column(name = "rest_kapa_esp_alle_produkte")
	private Double restKapaBuendelEspAlleProdukte;

	@Column(name = "rest_kapa_esp_spfv")
	private Double restKapaBuendelEspSpfv;

	@Column(name = "rest_kapa_esp_spnv")
	private Double restKapaBuendelEspSpnv;

	@Column(name = "rest_kapa_esp_sgv")
	private Double restKapaBuendelEspSgv;

	@Column(name = "rest_kapa_vks_alle_produkte")
	private Double restKapaBuendelVksAlleProdukte;

	@Column(name = "rest_kapa_vks_spfv")
	private Double restKapaBuendelVksSpfv;

	@Column(name = "rest_kapa_vks_spnv")
	private Double restKapaBuendelVksSpnv;

	@Column(name = "rest_kapa_vks_sgv")
	private Double restKapaBuendelVksSgv;

	@ManyToOne(fetch = FetchType.EAGER)
	private Betriebsstelle startBahnhof;

	@Column
	private Boolean startBahnhofBefahrbar;

	@ManyToOne(fetch = FetchType.EAGER)
	private Betriebsstelle endeBahnhof;

	@Column
	private Boolean endeBahnhofBefahrbar;

	@Column(name = "BetroffZuege")
	private Double betroffeneZuegeSperrabschnitt;

	@Column(name = "BauzeitTSP")
	private Double bauzeitBuendelTagenTsp;

	@Column(name = "BetroffPTKM")
	private Double betroffenePersonenkilometer;

	@Column(name = "Sperrdauer")
	private Double sperrdauerBuendel;

	/* Kommentar feld. */
	@Lob
	@Column(name = "bemerkung")
	private String bemerkung;

	/* BetroffeneZuege SPFV SPNV SGV */
	@Column(name = "BetroffZuegeSPFV")
	private Double betroffeneZuegeSPFV;

	@Column(name = "BetroffZuegeSPNV")
	private Double betroffeneZuegeSPNV;

	@Column(name = "BetroffZuegeSGV")
	private Double betroffeneZuegeSGV;

	private boolean deleted;

	private boolean fixiert;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fixierungsDatum;

	@ManyToOne
	private AnkermassnahmeArt ankermassnahmeArt;

	@Column(name = "baukosten_vor_buendelung")
	private Double baukostenVorBuendelung;

	@Column(name = "baukosten_nach_buendelung")
	private Double baukostenNachBuendelung;

	@Column(name = "sperrzeitbedarf_buendel")
	private Integer sperrzeitbedarfBuendel;

	@Column(name = "sperrzeit_ersparnis")
	private Integer sperrzeitErsparnis;

	@Column(name = "baukosten_ersparnis")
	private Double baukostenErsparnis;

	public Set<Gleissperrung> getGleissperrungen() {
		return this.gleissperrungen;
	}

	public void setGleissperrungen(Set<Gleissperrung> gleissperrungen) {
		this.gleissperrungen = gleissperrungen;
	}

	public Set<MasterBuendel> getMasterBuendel() {
		return masterBuendel;
	}

	public void setMasterBuendel(Set<MasterBuendel> masterBuendel) {
		this.masterBuendel = masterBuendel;
	}

	public Betriebsweise getBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(Betriebsweise betriebsweise) {
		this.betriebsweise = betriebsweise;
	}

	public String getBetriebsweise_unused() {
		return betriebsweise_unused;
	}

	public void setBetriebsweise_unused(String betriebsweiseUnused) {
		betriebsweise_unused = betriebsweiseUnused;
	}

	public Boolean getVorschlagAufnahmePP() {
		return vorschlagAufnahmePP;
	}

	public void setVorschlagAufnahmePP(Boolean vorschlagAufnahmePP) {
		this.vorschlagAufnahmePP = vorschlagAufnahmePP;
	}

	public Boolean getBestimmungAufnahmePP() {
		return bestimmungAufnahmePP;
	}

	public void setBestimmungAufnahmePP(Boolean bestimmungAufnahmePP) {
		this.bestimmungAufnahmePP = bestimmungAufnahmePP;
	}

	public Date getDurchfuehrungsZeitraumStartGeplant() {
		return durchfuehrungsZeitraumStartGeplant;
	}

	public void setDurchfuehrungsZeitraumStartGeplant(Date durchfuehrungsZeitraumStartGeplant) {
		this.durchfuehrungsZeitraumStartGeplant = durchfuehrungsZeitraumStartGeplant;
	}

	public Date getDurchfuehrungsZeitraumEndeGeplant() {
		return durchfuehrungsZeitraumEndeGeplant;
	}

	public void setDurchfuehrungsZeitraumEndeGeplant(Date durchfuehrungsZeitraumEndeGeplant) {
		this.durchfuehrungsZeitraumEndeGeplant = durchfuehrungsZeitraumEndeGeplant;
	}

	public Date getDurchfuehrungsZeitraumStartKoordiniert() {
		return durchfuehrungsZeitraumStartKoordiniert;
	}

	public void setDurchfuehrungsZeitraumStartKoordiniert(
	    Date durchfuehrungsZeitraumStartKoordiniert) {
		this.durchfuehrungsZeitraumStartKoordiniert = durchfuehrungsZeitraumStartKoordiniert;
	}

	public Date getDurchfuehrungsZeitraumEndeKoordiniert() {
		return durchfuehrungsZeitraumEndeKoordiniert;
	}

	public void setDurchfuehrungsZeitraumEndeKoordiniert(Date durchfuehrungsZeitraumEndeKoordiniert) {
		this.durchfuehrungsZeitraumEndeKoordiniert = durchfuehrungsZeitraumEndeKoordiniert;
	}

	public Double getBaukostenESP() {
		return baukostenESP;
	}

	public void setBaukostenESP(Double baukostenESP) {
		this.baukostenESP = baukostenESP;
	}

	public Double getBaukostenTSP() {
		return baukostenTSP;
	}

	public void setBaukostenTSP(Double baukostenTSP) {
		this.baukostenTSP = baukostenTSP;
	}

	public Double getEiuTsp() {
		return eiuTsp;
	}

	public void setEiuTsp(Double eiuTsp) {
		this.eiuTsp = eiuTsp;
	}

	public Double getNetzTsp() {
		return netzTsp;
	}

	public void setNetzTsp(Double netzTsp) {
		this.netzTsp = netzTsp;
	}

	public Double getProdTsp() {
		return prodTsp;
	}

	public void setProdTsp(Double prodTsp) {
		this.prodTsp = prodTsp;
	}

	public Double getEvuTsp() {
		return evuTsp;
	}

	public void setEvuTsp(Double evuTsp) {
		this.evuTsp = evuTsp;
	}

	public Double getSpfvTspGesamt() {
		return spfvTspGesamt;
	}

	public void setSpfvTspGesamt(Double spfvTspGesamt) {
		this.spfvTspGesamt = spfvTspGesamt;
	}

	public Double getSpfvTspKosten() {
		return spfvTspKosten;
	}

	public void setSpfvTspKosten(Double spfvTspKosten) {
		this.spfvTspKosten = spfvTspKosten;
	}

	public Double getSpfvTspUmsatz() {
		return spfvTspUmsatz;
	}

	public void setSpfvTspUmsatz(Double spfvTspUmsatz) {
		this.spfvTspUmsatz = spfvTspUmsatz;
	}

	public Double getSpnvTspGesamt() {
		return spnvTspGesamt;
	}

	public void setSpnvTspGesamt(Double spnvTspGesamt) {
		this.spnvTspGesamt = spnvTspGesamt;
	}

	public Double getSpnvTspKosten() {
		return spnvTspKosten;
	}

	public void setSpnvTspKosten(Double spnvTspKosten) {
		this.spnvTspKosten = spnvTspKosten;
	}

	public Double getSpnvTspUmsatz() {
		return spnvTspUmsatz;
	}

	public void setSpnvTspUmsatz(Double spnvTspUmsatz) {
		this.spnvTspUmsatz = spnvTspUmsatz;
	}

	public Double getSgvTspGesamt() {
		return sgvTspGesamt;
	}

	public void setSgvTspGesamt(Double sgvTspGesamt) {
		this.sgvTspGesamt = sgvTspGesamt;
	}

	public Double getSgvTspKosten() {
		return sgvTspKosten;
	}

	public void setSgvTspKosten(Double sgvTspKosten) {
		this.sgvTspKosten = sgvTspKosten;
	}

	public Double getSgvTspUmsatz() {
		return sgvTspUmsatz;
	}

	public void setSgvTspUmsatz(Double sgvTspUmsatz) {
		this.sgvTspUmsatz = sgvTspUmsatz;
	}

	public Double getEiuEsp() {
		return eiuEsp;
	}

	public void setEiuEsp(Double eiuEsp) {
		this.eiuEsp = eiuEsp;
	}

	public Double getNetzEsp() {
		return netzEsp;
	}

	public void setNetzEsp(Double netzEsp) {
		this.netzEsp = netzEsp;
	}

	public Double getProdEsp() {
		return prodEsp;
	}

	public void setProdEsp(Double prodEsp) {
		this.prodEsp = prodEsp;
	}

	public Double getEvuEsp() {
		return evuEsp;
	}

	public void setEvuEsp(Double evuEsp) {
		this.evuEsp = evuEsp;
	}

	public Double getSpfvEspGesamt() {
		return spfvEspGesamt;
	}

	public void setSpfvEspGesamt(Double spfvEspGesamt) {
		this.spfvEspGesamt = spfvEspGesamt;
	}

	public Double getSpfvEspKosten() {
		return spfvEspKosten;
	}

	public void setSpfvEspKosten(Double spfvEspKosten) {
		this.spfvEspKosten = spfvEspKosten;
	}

	public Double getSpfvEspUmsatz() {
		return spfvEspUmsatz;
	}

	public void setSpfvEspUmsatz(Double spfvEspUmsatz) {
		this.spfvEspUmsatz = spfvEspUmsatz;
	}

	public Double getSpnvEspGesamt() {
		return spnvEspGesamt;
	}

	public void setSpnvEspGesamt(Double spnvEspGesamt) {
		this.spnvEspGesamt = spnvEspGesamt;
	}

	public Double getSpnvEspKosten() {
		return spnvEspKosten;
	}

	public void setSpnvEspKosten(Double spnvEspKosten) {
		this.spnvEspKosten = spnvEspKosten;
	}

	public Double getSpnvEspUmsatz() {
		return spnvEspUmsatz;
	}

	public void setSpnvEspUmsatz(Double spnvEspUmsatz) {
		this.spnvEspUmsatz = spnvEspUmsatz;
	}

	public Double getSgvEspGesamt() {
		return sgvEspGesamt;
	}

	public void setSgvEspGesamt(Double sgvEspGesamt) {
		this.sgvEspGesamt = sgvEspGesamt;
	}

	public Double getSgvEspKosten() {
		return sgvEspKosten;
	}

	public void setSgvEspKosten(Double sgvEspKosten) {
		this.sgvEspKosten = sgvEspKosten;
	}

	public Double getSgvEspUmsatz() {
		return sgvEspUmsatz;
	}

	public void setSgvEspUmsatz(Double sgvEspUmsatz) {
		this.sgvEspUmsatz = sgvEspUmsatz;
	}

	public Integer getEiuVkS() {
		return eiuVkS;
	}

	public void setEiuVkS(Integer eiuVkS) {
		this.eiuVkS = eiuVkS;
	}

	public Double getNetzVkS() {
		return netzVkS;
	}

	public void setNetzVkS(Double netzVkS) {
		this.netzVkS = netzVkS;
	}

	public Double getProdVkS() {
		return prodVkS;
	}

	public void setProdVkS(Double prodVkS) {
		this.prodVkS = prodVkS;
	}

	public Double getEvuVkS() {
		return evuVkS;
	}

	public void setEvuVkS(Double evuVkS) {
		this.evuVkS = evuVkS;
	}

	public Double getSpfvVkSGesamt() {
		return spfvVkSGesamt;
	}

	public void setSpfvVkSGesamt(Double spfvVkSGesamt) {
		this.spfvVkSGesamt = spfvVkSGesamt;
	}

	public Double getSpfvVkSKosten() {
		return spfvVkSKosten;
	}

	public void setSpfvVkSKosten(Double spfvVkSKosten) {
		this.spfvVkSKosten = spfvVkSKosten;
	}

	public Double getSpfvVkSUmsatz() {
		return spfvVkSUmsatz;
	}

	public void setSpfvVkSUmsatz(Double spfvVkSUmsatz) {
		this.spfvVkSUmsatz = spfvVkSUmsatz;
	}

	public Double getSpnvVkSGesamt() {
		return spnvVkSGesamt;
	}

	public void setSpnvVkSGesamt(Double spnvVkSGesamt) {
		this.spnvVkSGesamt = spnvVkSGesamt;
	}

	public Double getSpnvVkSKosten() {
		return spnvVkSKosten;
	}

	public void setSpnvVkSKosten(Double spnvVkSKosten) {
		this.spnvVkSKosten = spnvVkSKosten;
	}

	public Double getSpnvVkSUmsatz() {
		return spnvVkSUmsatz;
	}

	public void setSpnvVkSUmsatz(Double spnvVkSUmsatz) {
		this.spnvVkSUmsatz = spnvVkSUmsatz;
	}

	public Double getSgvVkSGesamt() {
		return sgvVkSGesamt;
	}

	public void setSgvVkSGesamt(Double sgvVkSGesamt) {
		this.sgvVkSGesamt = sgvVkSGesamt;
	}

	public Double getSgvVkSKosten() {
		return sgvVkSKosten;
	}

	public void setSgvVkSKosten(Double sgvVkSKosten) {
		this.sgvVkSKosten = sgvVkSKosten;
	}

	public Double getSgvVkSUmsatz() {
		return sgvVkSUmsatz;
	}

	public void setSgvVkSUmsatz(Double sgvVkSUmsatz) {
		this.sgvVkSUmsatz = sgvVkSUmsatz;
	}

	public VzgStrecke getHauptStrecke() {
		return hauptStrecke;
	}

	public void setHauptStrecke(VzgStrecke hauptStrecke) {
		this.hauptStrecke = hauptStrecke;
	}

	public Set<Fahrplanregelung> getFahrplanregelungen() {
		return fahrplanregelungen;
	}

	public void setFahrplanregelungen(Set<Fahrplanregelung> fahrplanregelungen) {
		this.fahrplanregelungen = fahrplanregelungen;
	}

	public Set<VzgStrecke> getWeitereStrecken() {
		return weitereStrecken;
	}

	public void setWeitereStrecken(Set<VzgStrecke> weitereStrecken) {
		this.weitereStrecken = weitereStrecken;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public String getBuendelName() {
		return buendelName;
	}

	public void setBuendelName(String buendelName) {
		this.buendelName = buendelName;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public Double getSperrzeitbedarfBuendelsBetreibswesieTSP() {
		return sperrzeitbedarfBuendelsBetreibswesieTSP;
	}

	public void setSperrzeitbedarfBuendelsBetreibswesieTSP(
	    Double sperrzeitbedarfBuendelsBetreibswesieTSP) {
		this.sperrzeitbedarfBuendelsBetreibswesieTSP = sperrzeitbedarfBuendelsBetreibswesieTSP;
	}

	public Double getSperrzeitbedarfBuendelsBetreibswesieESP() {
		return sperrzeitbedarfBuendelsBetreibswesieESP;
	}

	public void setSperrzeitbedarfBuendelsBetreibswesieESP(
	    Double sperrzeitbedarfBuendelsBetreibswesieESP) {
		this.sperrzeitbedarfBuendelsBetreibswesieESP = sperrzeitbedarfBuendelsBetreibswesieESP;
	}

	public Double getSperrzeitbedarfBuendelsBetreibswesievKS() {
		return sperrzeitbedarfBuendelsBetreibswesievKS;
	}

	public void setSperrzeitbedarfBuendelsBetreibswesievKS(
	    Double sperrzeitbedarfBuendelsBetreibswesievKS) {
		this.sperrzeitbedarfBuendelsBetreibswesievKS = sperrzeitbedarfBuendelsBetreibswesievKS;
	}

	public Double getRestKapaBuendelEspAlleProdukte() {
		return restKapaBuendelEspAlleProdukte;
	}

	public void setRestKapaBuendelEspAlleProdukte(Double restKapaBuendelEspAlleProdukte) {
		this.restKapaBuendelEspAlleProdukte = restKapaBuendelEspAlleProdukte;
	}

	public Double getRestKapaBuendelEspSpfv() {
		return restKapaBuendelEspSpfv;
	}

	public void setRestKapaBuendelEspSpfv(Double restKapaBuendelEspSpfv) {
		this.restKapaBuendelEspSpfv = restKapaBuendelEspSpfv;
	}

	public Double getRestKapaBuendelEspSpnv() {
		return restKapaBuendelEspSpnv;
	}

	public void setRestKapaBuendelEspSpnv(Double restKapaBuendelEspSpnv) {
		this.restKapaBuendelEspSpnv = restKapaBuendelEspSpnv;
	}

	public Double getRestKapaBuendelEspSgv() {
		return restKapaBuendelEspSgv;
	}

	public void setRestKapaBuendelEspSgv(Double restKapaBuendelEspSgv) {
		this.restKapaBuendelEspSgv = restKapaBuendelEspSgv;
	}

	public Double getRestKapaBuendelVksAlleProdukte() {
		return restKapaBuendelVksAlleProdukte;
	}

	public void setRestKapaBuendelVksAlleProdukte(Double restKapaBuendelVksAlleProdukte) {
		this.restKapaBuendelVksAlleProdukte = restKapaBuendelVksAlleProdukte;
	}

	public Double getRestKapaBuendelVksSpfv() {
		return restKapaBuendelVksSpfv;
	}

	public void setRestKapaBuendelVksSpfv(Double restKapaBuendelVksSpfv) {
		this.restKapaBuendelVksSpfv = restKapaBuendelVksSpfv;
	}

	public Double getRestKapaBuendelVksSpnv() {
		return restKapaBuendelVksSpnv;
	}

	public void setRestKapaBuendelVksSpnv(Double restKapaBuendelVksSpnv) {
		this.restKapaBuendelVksSpnv = restKapaBuendelVksSpnv;
	}

	public Double getRestKapaBuendelVksSgv() {
		return restKapaBuendelVksSgv;
	}

	public void setRestKapaBuendelVksSgv(Double restKapaBuendelVksSgv) {
		this.restKapaBuendelVksSgv = restKapaBuendelVksSgv;
	}

	public Betriebsstelle getStartBahnhof() {
		return startBahnhof;
	}

	public void setStartBahnhof(Betriebsstelle startBahnhof) {
		this.startBahnhof = startBahnhof;
	}

	public Betriebsstelle getEndeBahnhof() {
		return endeBahnhof;
	}

	public void setEndeBahnhof(Betriebsstelle endeBahnhof) {
		this.endeBahnhof = endeBahnhof;
	}

	public Double getBetroffeneZuegeSperrabschnitt() {
		return betroffeneZuegeSperrabschnitt;
	}

	public void setBetroffeneZuegeSperrabschnitt(Double betroffeneZuegeSperrabschnitt) {
		this.betroffeneZuegeSperrabschnitt = betroffeneZuegeSperrabschnitt;
	}

	public Double getBauzeitBuendelTagenTsp() {
		return bauzeitBuendelTagenTsp;
	}

	public void setBauzeitBuendelTagenTsp(Double bauzeitBuendelTagenTsp) {
		this.bauzeitBuendelTagenTsp = bauzeitBuendelTagenTsp;
	}

	public Double getBetroffenePersonenkilometer() {
		return betroffenePersonenkilometer;
	}

	public void setBetroffenePersonenkilometer(Double betroffenePersonenkilometer) {
		this.betroffenePersonenkilometer = betroffenePersonenkilometer;
	}

	public Double getSperrdauerBuendel() {
		return sperrdauerBuendel;
	}

	public void setSperrdauerBuendel(Double sperrdauerBuendel) {
		this.sperrdauerBuendel = sperrdauerBuendel;
	}

	public Integer getLfdNr() {
		return lfdNr;
	}

	public void setLfdNr(Integer lfd) {
		this.lfdNr = lfd;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public Double getBetroffeneZuegeSPFV() {
		return betroffeneZuegeSPFV;
	}

	public void setBetroffeneZuegeSPFV(Double betroffeneZuegeSPFV) {
		this.betroffeneZuegeSPFV = betroffeneZuegeSPFV;
	}

	public Double getBetroffeneZuegeSPNV() {
		return betroffeneZuegeSPNV;
	}

	public void setBetroffeneZuegeSPNV(Double betroffeneZuegeSPNV) {
		this.betroffeneZuegeSPNV = betroffeneZuegeSPNV;
	}

	public Double getBetroffeneZuegeSGV() {
		return betroffeneZuegeSGV;
	}

	public void setBetroffeneZuegeSGV(Double betroffeneZuegeSGV) {
		this.betroffeneZuegeSGV = betroffeneZuegeSGV;
	}

	public Boolean getStartBahnhofBefahrbar() {
		return startBahnhofBefahrbar;
	}

	public void setStartBahnhofBefahrbar(Boolean startBahnhofBefahrbar) {
		this.startBahnhofBefahrbar = startBahnhofBefahrbar;
	}

	public Boolean getEndeBahnhofBefahrbar() {
		return endeBahnhofBefahrbar;
	}

	public void setEndeBahnhofBefahrbar(Boolean endeBahnhofBefahrbar) {
		this.endeBahnhofBefahrbar = endeBahnhofBefahrbar;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isFixiert() {
		return fixiert;
	}

	public void setFixiert(boolean fixiert) {
		this.fixiert = fixiert;
	}

	public Date getFixierungsDatum() {
		return fixierungsDatum;
	}

	public void setFixierungsDatum(Date fixierungsDatum) {
		this.fixierungsDatum = fixierungsDatum;
	}

	public AnkermassnahmeArt getAnkermassnahmeArt() {
		return ankermassnahmeArt;
	}

	public void setAnkermassnahmeArt(AnkermassnahmeArt ankermassnahmeArt) {
		this.ankermassnahmeArt = ankermassnahmeArt;
	}

	public Integer getSperrzeitbedarfBuendel() {
		return sperrzeitbedarfBuendel;
	}

	public void setSperrzeitbedarfBuendel(Integer sperrzeitbedarfBuendel) {
		this.sperrzeitbedarfBuendel = sperrzeitbedarfBuendel;
	}

	public Double getBaukostenVorBuendelung() {
		return baukostenVorBuendelung;
	}

	public void setBaukostenVorBuendelung(Double baukostenVorBuendelung) {
		this.baukostenVorBuendelung = baukostenVorBuendelung;
	}

	public Double getBaukostenNachBuendelung() {
		return baukostenNachBuendelung;
	}

	public void setBaukostenNachBuendelung(Double baukostenNachBuendelung) {
		this.baukostenNachBuendelung = baukostenNachBuendelung;
	}

	public Integer getSperrzeitErsparnis() {
		return sperrzeitErsparnis;
	}

	public void setSperrzeitErsparnis(Integer sperrzeitErsparnis) {
		this.sperrzeitErsparnis = sperrzeitErsparnis;
	}

	public Double getBaukostenErsparnis() {
		return baukostenErsparnis;
	}

	public void setBaukostenErsparnis(Double baukostenErsparnis) {
		this.baukostenErsparnis = baukostenErsparnis;
	}

	public String getBuendelId() {
		// ID setzt sich zusammen aus:
		// - 1./2. Stelle ID Regionalbereich
		// - 3./4. Stelle Fahrplanjahr
		// - 5./8. Stelle LfdNr. eindeutig unter Regionalbereich und FplJahr

		StringBuffer sb = new StringBuffer("00.00.0000");

		if (getRegionalbereich() != null) {
			String rbId = Integer.toString(getRegionalbereich().getId());
			// simply and stupid :-)
			if (rbId.length() == 1)
				sb.replace(1, 2, rbId);
			else if (rbId.length() == 2)
				sb.replace(0, 2, rbId);
			else if (rbId.length() == 3) {
				sb = new StringBuffer("0" + sb);
				sb.replace(0, 3, rbId);
			}
		}

		if (getFahrplanjahr() != null) {
			String fj = Integer.toString(getFahrplanjahr());
			if (fj.length() == 4) {
				fj = fj.substring(2);
			}
			sb.replace(3, 5, fj);
		}

		if (getLfdNr() != null) {
			String lfd = Integer.toString(getLfdNr());
			sb.replace(10 - lfd.length(), 10, lfd);
		} else {
			throw new IllegalStateException(
			    "Buendel muss erst gespeichert werden bevor eine ID ausgegeben werden kann.");
		}

		return sb.toString();
	}

	@Transient
	public String getWeitereStreckenCsv() {
		if (getWeitereStrecken() != null) {
			int size = getWeitereStrecken().size();
			StringBuffer weitereStrecken = new StringBuffer();
			int i = 0;
			for (VzgStrecke strecke : getWeitereStrecken()) {
				i = i + 1;
				weitereStrecken.append(strecke.getNummer());
				if (i < size)
					weitereStrecken.append(',');
			}
			return weitereStrecken.toString();
		}
		return null;
	}

	@Override
	@Transient
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Bündel [").append(getId());
		sb.append(" - ").append(getBuendelId()).append(", ");
		sb.append(getBuendelName()).append("]");
		return sb.toString();
	}

	@Transient
	public Integer getSummeSperrpausenbedarfGesamt() {
		Integer sum = null;
		if (getSummeSperrpausenbedarfBfGl() != null) {
			sum = getSummeSperrpausenbedarfBfGl();
		}
		if (getSummeSperrpausenbedarfEsp() != null) {
			if (sum == null)
				sum = getSummeSperrpausenbedarfEsp();
			else
				sum += getSummeSperrpausenbedarfEsp();
		}
		if (getSummeSperrpausenbedarfTsp() != null) {
			if (sum == null)
				sum = getSummeSperrpausenbedarfTsp();
			else
				sum += getSummeSperrpausenbedarfTsp();
		}
		if (getEiuVkS() != null) {
			if (sum == null)
				sum = getEiuVkS();
			else
				sum += getEiuVkS();
		}
		return sum;
	}

	@Transient
	public Integer getSummeSperrpausenbedarfEsp() {
		Integer sum = null;
		for (Gleissperrung gl : getGleissperrungen()) {
			if (gl.getSperrpausenbedarfEsp() != null) {
				if (sum == null)
					sum = gl.getSperrpausenbedarfEsp();
				else
					sum += gl.getSperrpausenbedarfEsp();
			}
		}
		return sum;
	}

	@Transient
	public Integer getSummeSperrpausenbedarfTsp() {
		Integer sum = null;
		for (Gleissperrung gl : getGleissperrungen()) {
			if (gl.getSperrpausenbedarfTsp() != null) {
				if (sum == null)
					sum = gl.getSperrpausenbedarfTsp();
				else
					sum += gl.getSperrpausenbedarfTsp();
			}
		}
		return sum;
	}

	@Transient
	public Integer getSummeSperrpausenbedarfBfGl() {
		Integer sum = null;
		for (Gleissperrung gl : getGleissperrungen()) {
			if (gl.getSperrpausenbedarfBfGl() != null) {
				if (sum == null)
					sum = gl.getSperrpausenbedarfBfGl();
				else
					sum += gl.getSperrpausenbedarfBfGl();
			}
		}
		return sum;
	}

	/**
	 * Liefert direkt zugewiesene Gleissperrungen und die ueber deren Massnahme zugewiesenen
	 * Gleissperrungen zurueck.
	 * 
	 * @return gleissperrungenGesamt
	 */
	@Transient
	public List<Gleissperrung> getGleissperrungenGesamt() {
		// Gleissperrungen des Buendels und ueber Massnahmen verbundene
		List<Gleissperrung> gleissperrungenGesamt = new ArrayList<Gleissperrung>();
		for (Gleissperrung gs : getGleissperrungen()) {
			// direkt zugewiesene Gleissperrung in Liste aufnehmen
			if (!gleissperrungenGesamt.contains(gs)) {
				gleissperrungenGesamt.add(gs);
			}
			// ueber Massnahme zugewiesene Gleissperrungen in Liste aufnehmen
			if (gs.getMassnahme() != null) {
				for (Gleissperrung mgs : gs.getMassnahme().getGleissperrungen()) {
					if (!gleissperrungenGesamt.contains(mgs)) {
						gleissperrungenGesamt.add(mgs);
					}
				}
			}
		}
		return gleissperrungenGesamt;
	}

	@Transient
	public void refreshWeitereStrecken() {
		// weitereStrecken muss mit Strecken der Gleissperrung befuellt werden
		List<VzgStrecke> weitereStrecken = new ArrayList<VzgStrecke>();
		for (Gleissperrung gl : getGleissperrungen()) {
			if (gl.getVzgStrecke() != null && !weitereStrecken.contains(gl.getVzgStrecke())
			    && (getHauptStrecke() == null || !getHauptStrecke().equals(gl.getVzgStrecke()))) {
				weitereStrecken.add(gl.getVzgStrecke());
			}
		}
		getWeitereStrecken().clear();
		getWeitereStrecken().addAll(weitereStrecken);
	}

	@Transient
	public Buendel clone() {
		Buendel buendel = new Buendel();
		buendel.setId(null);

		buendel.setAnkermassnahmeArt(getAnkermassnahmeArt());
		buendel.setBuendelName(getBuendelName());
		buendel.setDeleted(isDeleted());
		buendel.setFahrplanjahr(getFahrplanjahr());
		buendel.setFahrplanregelungen(getFahrplanregelungen());
		buendel.setFixiert(isFixiert());
		buendel.setFixierungsDatum(getFixierungsDatum());
		// Gleissperrungen sollen nicht mitkopiert werden #510
		// buendel.setGleissperrungen(getGleissperrungen());
		buendel.setHauptStrecke(getHauptStrecke());
		buendel.setLastChangeDate(getLastChangeDate());
		buendel.setLfdNr(getLfdNr());
		buendel.setMasterBuendel(getMasterBuendel());
		buendel.setEndeBahnhof(getEndeBahnhof());
		buendel.setEndeBahnhofBefahrbar(getEndeBahnhofBefahrbar());
		buendel.setStartBahnhof(getStartBahnhof());
		buendel.setStartBahnhofBefahrbar(getStartBahnhofBefahrbar());
		buendel.setWeitereStrecken(getWeitereStrecken());
		buendel.setRegionalbereich(getRegionalbereich());
		buendel.setDurchfuehrungsZeitraumEndeGeplant(getDurchfuehrungsZeitraumEndeGeplant());
		buendel
		    .setDurchfuehrungsZeitraumEndeKoordiniert(getDurchfuehrungsZeitraumEndeKoordiniert());
		buendel.setDurchfuehrungsZeitraumStartGeplant(getDurchfuehrungsZeitraumStartGeplant());
		buendel
		    .setDurchfuehrungsZeitraumStartKoordiniert(getDurchfuehrungsZeitraumStartKoordiniert());

		buendel.setBaukostenErsparnis(getBaukostenErsparnis());
		buendel.setBaukostenESP(getBaukostenESP());
		buendel.setBaukostenNachBuendelung(getBaukostenNachBuendelung());
		buendel.setBaukostenTSP(getBaukostenTSP());
		buendel.setBaukostenVorBuendelung(getBaukostenVorBuendelung());
		buendel.setBauzeitBuendelTagenTsp(getBauzeitBuendelTagenTsp());
		buendel.setBemerkung(getBemerkung());
		buendel.setBestimmungAufnahmePP(getBestimmungAufnahmePP());
		buendel.setBetriebsweise(getBetriebsweise());
		buendel.setBetriebsweise_unused(getBetriebsweise_unused());
		buendel.setBetroffenePersonenkilometer(getBetroffenePersonenkilometer());
		buendel.setBetroffeneZuegeSGV(getBetroffeneZuegeSGV());
		buendel.setBetroffeneZuegeSperrabschnitt(getBetroffeneZuegeSperrabschnitt());
		buendel.setBetroffeneZuegeSPFV(getBetroffeneZuegeSPFV());
		buendel.setBetroffeneZuegeSPNV(getBetroffeneZuegeSPNV());
		buendel.setEiuEsp(getEiuEsp());
		buendel.setEiuTsp(getEiuTsp());
		buendel.setEiuVkS(getEiuVkS());
		buendel.setEvuEsp(getEvuEsp());
		buendel.setEvuTsp(getEvuTsp());
		buendel.setEvuVkS(getEvuVkS());
		buendel.setNetzEsp(getNetzEsp());
		buendel.setNetzTsp(getNetzTsp());
		buendel.setNetzVkS(getNetzVkS());
		buendel.setProdEsp(getProdEsp());
		buendel.setProdTsp(getProdTsp());
		buendel.setProdVkS(getProdVkS());
		buendel.setRestKapaBuendelEspAlleProdukte(getRestKapaBuendelEspAlleProdukte());
		buendel.setRestKapaBuendelEspSgv(getRestKapaBuendelEspSgv());
		buendel.setRestKapaBuendelEspSpfv(getRestKapaBuendelEspSpfv());
		buendel.setRestKapaBuendelEspSpnv(getRestKapaBuendelEspSpnv());
		buendel.setRestKapaBuendelVksAlleProdukte(getRestKapaBuendelVksAlleProdukte());
		buendel.setRestKapaBuendelVksSgv(getRestKapaBuendelVksSgv());
		buendel.setRestKapaBuendelVksSpfv(getRestKapaBuendelVksSpfv());
		buendel.setRestKapaBuendelVksSpnv(getRestKapaBuendelVksSpnv());
		buendel.setSgvEspGesamt(getSgvEspGesamt());
		buendel.setSgvEspKosten(getSgvEspKosten());
		buendel.setSgvEspUmsatz(getSgvEspUmsatz());
		buendel.setSgvTspGesamt(getSgvTspGesamt());
		buendel.setSgvTspKosten(getSgvTspKosten());
		buendel.setSgvTspUmsatz(getSgvTspUmsatz());
		buendel.setSgvVkSGesamt(getSgvVkSGesamt());
		buendel.setSgvVkSKosten(getSgvVkSKosten());
		buendel.setSgvVkSUmsatz(getSgvVkSUmsatz());
		buendel.setSperrdauerBuendel(getSperrdauerBuendel());
		buendel.setSperrzeitbedarfBuendel(getSperrzeitbedarfBuendel());
		buendel
		    .setSperrzeitbedarfBuendelsBetreibswesieESP(getSperrzeitbedarfBuendelsBetreibswesieESP());
		buendel
		    .setSperrzeitbedarfBuendelsBetreibswesieTSP(getSperrzeitbedarfBuendelsBetreibswesieTSP());
		buendel
		    .setSperrzeitbedarfBuendelsBetreibswesievKS(getSperrzeitbedarfBuendelsBetreibswesievKS());
		buendel.setSperrzeitErsparnis(getSperrzeitErsparnis());
		buendel.setSpfvEspGesamt(getSpfvEspGesamt());
		buendel.setSpfvEspKosten(getSpfvEspKosten());
		buendel.setSpfvEspUmsatz(getSpfvEspUmsatz());
		buendel.setSpfvTspGesamt(getSpfvTspGesamt());
		buendel.setSpfvTspKosten(getSpfvTspKosten());
		buendel.setSpfvTspUmsatz(getSpfvTspUmsatz());
		buendel.setSpfvVkSGesamt(getSpfvVkSGesamt());
		buendel.setSpnvVkSKosten(getSpnvVkSKosten());
		buendel.setSpnvVkSUmsatz(getSpnvVkSUmsatz());
		buendel.setSpnvEspGesamt(getSpnvEspGesamt());
		buendel.setSpnvEspKosten(getSpnvEspKosten());
		buendel.setSpnvEspUmsatz(getSpnvEspUmsatz());
		buendel.setSpnvTspGesamt(getSpnvTspGesamt());
		buendel.setSpnvTspKosten(getSpnvTspKosten());
		buendel.setSpnvTspUmsatz(getSpnvTspUmsatz());
		buendel.setSpnvVkSGesamt(getSpnvVkSGesamt());
		buendel.setSpnvVkSKosten(getSpnvVkSKosten());
		buendel.setSpnvVkSUmsatz(getSpnvVkSUmsatz());
		buendel.setVorschlagAufnahmePP(getVorschlagAufnahmePP());
		return buendel;
	}
}