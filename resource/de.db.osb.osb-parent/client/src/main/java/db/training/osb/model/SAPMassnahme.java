package db.training.osb.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.easy.core.model.User;
import db.training.hibernate.history.Historizable;
import db.training.osb.model.babett.Arbeitstyp;
import db.training.osb.model.babett.Finanztyp;
import db.training.osb.model.babett.FolgeNichtausfuehrung;
import db.training.osb.model.babett.Grossmaschine;
import db.training.osb.model.babett.Phase;
import db.training.osb.model.babett.StatusBbzr;
import db.training.osb.model.enums.Bauverfahren;
import db.training.osb.model.enums.MassnahmeBehandlung;
import db.training.osb.model.enums.TiefentwaesserungLage;

@Entity
@Table(name = "sperrpausenbedarf")
public class SAPMassnahme extends EasyPersistentObjectVc implements Historizable {

	private static final long serialVersionUID = 8190197191351261679L;

	@Column(name = "zeilennummer")
	private Integer zeilenNummer;

	@ManyToOne
	private Regionalbereich regionalbereich;

	@Column(name = "gewerk")
	private String gewerk;

	@Column(name = "untergewerk")
	private String untergewerk;

	@Column(name = "psp_element")
	private String pspElement;

	@Column(name = "projektdefinition_db_bez")
	private String projektDefinitionDbBez;

	@ManyToOne
	private VzgStrecke hauptStrecke;

	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade( { CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<VzgStrecke> weitereStrecken;

	@ManyToOne
	@JoinColumn(name = "bstVon_ID")
	private Betriebsstelle betriebsstelleVon;

	@ManyToOne
	@JoinColumn(name = "bstBis_ID")
	private Betriebsstelle betriebsstelleBis;

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
	@Column(name = "richtungs_kennzahl")
	private Integer richtungsKennzahl;

	@Column(name = "weichen_gleisnummer_Bf_Gleisen")
	private String weichenGleisnummerBfGleisen;

	@Column(name = "umbaulaenge")
	private Integer umbaulaenge;

	@Column(length = 32)
	private String weichenbauform;

	@Column(name = "einbau_pss")
	private Boolean einbauPss;

	@Column(name = "notwendige_laenge_pss")
	private Integer notwendigeLaengePss;

	@Column(name = "tiefentwaesserung_lage")
	@Enumerated(value = EnumType.STRING)
	private TiefentwaesserungLage tiefentwaesserungLage;

	@Column(name = "oberleitungsanpassung")
	private Boolean oberleitungsAnpassung;

	@Column(name = "kabelkanal")
	private Boolean kabelkanal;

	@Column(name = "km_von")
	private Double kmVon;

	@Column(name = "km_bis")
	private Double kmBis;

	@Column(name = "bahnsteige")
	private Boolean bahnsteige;

	@Column(name = "lst")
	private Boolean lst;

	@Column(name = "bauverfahren")
	@Enumerated(value = EnumType.STRING)
	private Bauverfahren bauverfahren;

	@Column(name = "geplante_nennleistung")
	private Integer geplanteNennleistung;

	@Embedded()
	private GravierendeAenderung letzteGravierendeAenderung;

	@ManyToOne(fetch = FetchType.LAZY)
	private Paket paket;

	@Column(name = "technischer_platz")
	private String technischerPlatz;

	/** Ist Kommentar des Anmelders */
	@Lob
	@Column(name = "kommentar")
	private String kommentar;

	/** Bemerkungen (KBB) zur Koordination */
	@Lob
	@Column(name = "kommentar_koordination")
	private String kommentarKoordination;

	// TODO: auf EnumType.STRING ändern
	@Column(name = "massnahme_behandlung")
	@Enumerated(value = EnumType.ORDINAL)
	private MassnahmeBehandlung massnahmeBehandlung;

	@Column(name = "urspruengliches_planungsjahr")
	private Integer urspruenglichesPlanungsjahr;

	@Column(name = "geaendertes_planungsjahr")
	private Integer geaendertesPlanungsjahr;

	// TODO: Inhalte prüfen/aufräumen
	@Column(name = "kommentar_l_npp1")
	private String kommentarlNPP1;

	@Column(name = "bautermin_start")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bauterminStart;

	@Column(name = "bautermin_ende")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bauterminEnde;

	@Column(name = "lfdNr", nullable = false)
	private Integer lfdNr;

	@Column(name = "fahrplanjahr", nullable = false)
	private Integer fahrplanjahr;

	@ManyToMany(mappedBy = "massnahmen")
	private Set<TopProjekt> topProjekte;

	@ManyToOne(optional = true)
	private SAPMassnahme vorbedingung;

	@ManyToOne(optional = true)
	private SAPMassnahme vorphase;

	@ManyToOne(optional = true)
	private SAPMassnahme unterDeckung;

	@Column(nullable = false)
	private boolean ungueltig;

	/** arbeiten_durchgehend */
	private Boolean durchgehend;

	/** arbeiten_schichtweise */
	private Boolean schichtweise;

	@ManyToOne(optional = true)
	private Verkehrstageregelung vtr;

	@ManyToOne(optional = true)
	private Arbeitstyp arbeiten;

	/** arbeiten_bst_id */
	@ManyToOne(optional = true)
	private Betriebsstelle arbeitenBetriebsstelle;

	private String arbeitenKommentar;

	@ManyToOne(optional = true)
	private Phase phase;

	@ManyToOne(optional = true)
	private Anmelder anmelder;

	@Column(length = 32)
	private String anmelderErgaenzung;

	@Temporal(TemporalType.TIMESTAMP)
	private Date ersteAnmeldung;

	@Temporal(TemporalType.TIMESTAMP)
	private Date letzteAenderungAnmeldung;

	private Boolean genehmigungsanforderung;

	private Boolean bbEi;

	@ManyToOne(optional = true)
	private User genehmiger;

	@Temporal(TemporalType.TIMESTAMP)
	private Date genehmigungsDatum;

	@Column(length = 127)
	private String genehmigungsbedingungen;

	@ManyToOne(optional = true)
	private Regionalbereich genehmigerRegionalbereich;

	@Column(name = "betranr", length = 32)
	private String betraNr;

	/**
	 * Vorgangsnummer der zugehörigen Baumaßnahme in BOB
	 */
	private String baumassnahmeInBob;

	@Enumerated(EnumType.STRING)
	private StatusBbzr statusBbzr;

	private Boolean laEintragR406;

	private Boolean lueHinweis;

	private Boolean studie;

	// TODO: Folge-Nichtausführung in separate Tabelle auslagern??
	@ManyToOne()
	private FolgeNichtausfuehrung folgeNichtausfuehrung;

	@Temporal(TemporalType.TIMESTAMP)
	private Date folgeNichtausfuehrungBeginn;

	@Column(scale = 3, precision = 1)
	private Float folgeNichtausfuehrungFzv;

	@Column(name = "folgeNichtausfuehrungLa")
	private Integer folgeNichtausfuehrungLaGeschwindigkeit;

	@ManyToOne(optional = true)
	private Grossmaschine grossmaschine;

	@ManyToOne(optional = true)
	private Finanztyp finanztyp;

	@Temporal(TemporalType.DATE)
	private Date anforderungBbzr;

	@Temporal(TemporalType.DATE)
	private Date entwurfBbzr;

	@Temporal(TemporalType.DATE)
	private Date erledigungBbzr;

	private String interneBemerkungenZKbb;

	private Integer hervorhebFarbe;

	private Boolean nichtbehandlungIb;

	private String bbpMassnahmeId;

	private String bbpMassnahmeIdVorphase;

	private Boolean hasFolgephaseMassnahme;

	private Boolean entwurf;

	private Integer prioritaetPhase;

	@ManyToOne
	private SAPMassnahme ankermassnahme;

	@ManyToOne
	private AnkermassnahmeArt ankermassnahmeArt;

	@Column(name = "anztemp_ID", length = 32)
	private String anzeigeTemplateId;

	@Column(name = "anztemp_file")
	private String anzeigeTemplateFile;

	@ManyToOne
	@JoinColumn(name = "vzgStrecke_bis_ID")
	private VzgStrecke vzgStreckeBis;

	@ManyToOne
	@JoinColumn(name = "auftraggeber_ID")
	private Anmelder auftraggeber;

	@Column(name = "bautermin_start_koordiniert")
	private Date bauterminStartKoordiniert;

	@Column(name = "bautermin_ende_koordiniert")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bauterminEndeKoordiniert;

	@ManyToOne
	@JoinColumn(name = "bstVonKoordiniert_ID")
	private Betriebsstelle betriebsstelleVonKoordiniert;

	@ManyToOne
	@JoinColumn(name = "bstBisKoordiniert_ID")
	private Betriebsstelle betriebsstelleBisKoordiniert;

	@OneToMany(mappedBy = "massnahme")
	private Set<Oberleitung> oberleitungen;

	@OneToMany(mappedBy = "massnahme")
	private Set<Langsamfahrstelle> langsamfahrstellen;

	@OneToMany(mappedBy = "massnahme")
	private Set<Gleissperrung> gleissperrungen;

	public Integer getLfdNr() {
		return lfdNr;
	}

	public void setLfdNr(Integer lfdNr) {
		this.lfdNr = lfdNr;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public String getGewerk() {
		return gewerk;
	}

	public void setGewerk(String gewerk) {
		this.gewerk = gewerk;
	}

	public String getUntergewerk() {
		return untergewerk;
	}

	public void setUntergewerk(String untergewerk) {
		this.untergewerk = untergewerk;
	}

	public String getPspElement() {
		return pspElement;
	}

	public void setPspElement(String pspElement) {
		this.pspElement = pspElement;
	}

	public String getProjektDefinitionDbBez() {
		return projektDefinitionDbBez;
	}

	public void setProjektDefinitionDbBez(String projektDefinitionDbBez) {
		this.projektDefinitionDbBez = projektDefinitionDbBez;
	}

	/**
	 * Streckennummer - wird beim Import gesetzt und nie geändert
	 */
	public VzgStrecke getHauptStrecke() {
		return hauptStrecke;
	}

	/**
	 * Streckennummer - wird beim Import gesetzt und nie geändert
	 */
	public void setHauptStrecke(VzgStrecke hauptStrecke) {
		this.hauptStrecke = hauptStrecke;
	}

	public Set<VzgStrecke> getWeitereStrecken() {
		return weitereStrecken;
	}

	public void setWeitereStrecken(Set<VzgStrecke> weitereStrecken) {
		this.weitereStrecken = weitereStrecken;
	}

	public Integer getRichtungsKennzahl() {
		return richtungsKennzahl;
	}

	public void setRichtungsKennzahl(Integer richtungsKennzahl) {
		this.richtungsKennzahl = richtungsKennzahl;
	}

	public String getWeichenGleisnummerBfGleisen() {
		return weichenGleisnummerBfGleisen;
	}

	public void setWeichenGleisnummerBfGleisen(String weichenGleisnummerBfGleisen) {
		this.weichenGleisnummerBfGleisen = weichenGleisnummerBfGleisen;
	}

	public Integer getUmbaulaenge() {
		return umbaulaenge;
	}

	public void setUmbaulaenge(Integer umbaulaenge) {
		this.umbaulaenge = umbaulaenge;
	}

	public String getWeichenbauform() {
		return weichenbauform;
	}

	public void setWeichenbauform(String weichenbauform) {
		this.weichenbauform = weichenbauform;
	}

	public Boolean getEinbauPss() {
		return einbauPss;
	}

	public void setEinbauPss(Boolean einbauPss) {
		this.einbauPss = einbauPss;
	}

	public Integer getNotwendigeLaengePss() {
		return notwendigeLaengePss;
	}

	public void setNotwendigeLaengePss(Integer notwendigeLaengePss) {
		this.notwendigeLaengePss = notwendigeLaengePss;
	}

	public void setTiefentwaesserungLage(TiefentwaesserungLage tiefentwaesserungLage) {
		this.tiefentwaesserungLage = tiefentwaesserungLage;
	}

	public TiefentwaesserungLage getTiefentwaesserungLage() {
		return tiefentwaesserungLage;
	}

	public void setOberleitungen(Set<Oberleitung> oberleitungen) {
		this.oberleitungen = oberleitungen;
	}

	public Set<Oberleitung> getOberleitungen() {
		return oberleitungen;
	}

	public Boolean getOberleitungsAnpassung() {
		return oberleitungsAnpassung;
	}

	public void setOberleitungsAnpassung(Boolean oberleitungsAnpassung) {
		this.oberleitungsAnpassung = oberleitungsAnpassung;
	}

	public Boolean getKabelkanal() {
		return kabelkanal;
	}

	public void setKabelkanal(Boolean kabelkanal) {
		this.kabelkanal = kabelkanal;
	}

	public Double getKmVon() {
		return kmVon;
	}

	public void setKmVon(Double kmVon) {
		this.kmVon = kmVon;
	}

	public Double getKmBis() {
		return kmBis;
	}

	public void setKmBis(Double kmBis) {
		this.kmBis = kmBis;
	}

	public Boolean getBahnsteige() {
		return bahnsteige;
	}

	public void setBahnsteige(Boolean bahnsteige) {
		this.bahnsteige = bahnsteige;
	}

	public Boolean getLst() {
		return lst;
	}

	public void setLst(Boolean lst) {
		this.lst = lst;
	}

	public Bauverfahren getBauverfahren() {
		return bauverfahren;
	}

	public void setBauverfahren(Bauverfahren bauverfahren) {
		this.bauverfahren = bauverfahren;
	}

	public Integer getGeplanteNennleistung() {
		return geplanteNennleistung;
	}

	public void setGeplanteNennleistung(Integer geplanteNennleistung) {
		this.geplanteNennleistung = geplanteNennleistung;
	}

	public Set<Langsamfahrstelle> getLangsamfahrstellen() {
		return langsamfahrstellen;
	}

	public void setLangsamfahrstellen(Set<Langsamfahrstelle> langsamfahrstellen) {
		this.langsamfahrstellen = langsamfahrstellen;
	}

	public void setLetzteGravierendeAenderung(GravierendeAenderung letzteGravierendeAenderung) {
		this.letzteGravierendeAenderung = letzteGravierendeAenderung;
	}

	public GravierendeAenderung getLetzteGravierendeAenderung() {
		return letzteGravierendeAenderung;
	}

	public Betriebsstelle getBetriebsstelleVon() {
		return betriebsstelleVon;
	}

	public void setBetriebsstelleVon(Betriebsstelle betriebsstelleVon) {
		this.betriebsstelleVon = betriebsstelleVon;
	}

	public Betriebsstelle getBetriebsstelleBis() {
		return betriebsstelleBis;
	}

	public void setBetriebsstelleBis(Betriebsstelle betriebsstelleBis) {
		this.betriebsstelleBis = betriebsstelleBis;
	}

	public Paket getPaket() {
		return paket;
	}

	public void setPaket(Paket paket) {
		this.paket = paket;
	}

	public Integer getZeilenNummer() {
		return zeilenNummer;
	}

	public void setZeilenNummer(Integer zeilenNummer) {
		this.zeilenNummer = zeilenNummer;
	}

	public String getTechnischerPlatz() {
		return technischerPlatz;
	}

	public void setTechnischerPlatz(String technischerPlatz) {
		this.technischerPlatz = technischerPlatz;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public MassnahmeBehandlung getMassnahmeBehandlung() {
		return massnahmeBehandlung;
	}

	public void setMassnahmeBehandlung(MassnahmeBehandlung massnahmeBehandlung) {
		this.massnahmeBehandlung = massnahmeBehandlung;
	}

	public Integer getUrspruenglichesPlanungsjahr() {
		return urspruenglichesPlanungsjahr;
	}

	public void setUrspruenglichesPlanungsjahr(Integer urspruenglichesPlanungsjahr) {
		this.urspruenglichesPlanungsjahr = urspruenglichesPlanungsjahr;
	}

	public Integer getGeaendertesPlanungsjahr() {
		return geaendertesPlanungsjahr;
	}

	public void setGeaendertesPlanungsjahr(Integer geaendertesPlanungsjahr) {
		this.geaendertesPlanungsjahr = geaendertesPlanungsjahr;
	}

	public String getKommentarKoordination() {
		return kommentarKoordination;
	}

	public void setKommentarKoordination(String kommentarKoordination) {
		this.kommentarKoordination = kommentarKoordination;
	}

	public String getKommentarlNPP1() {
		return kommentarlNPP1;
	}

	public void setKommentarlNPP1(String kommentarlNPP1) {
		this.kommentarlNPP1 = kommentarlNPP1;
	}

	public Date getBauterminStart() {
		return bauterminStart;
	}

	public void setBauterminStart(Date bauterminStart) {
		this.bauterminStart = bauterminStart;
	}

	public Date getBauterminEnde() {
		return bauterminEnde;
	}

	public void setBauterminEnde(Date bauterminEnde) {
		this.bauterminEnde = bauterminEnde;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public Set<TopProjekt> getTopProjekte() {
		return topProjekte;
	}

	public void setTopProjekte(Set<TopProjekt> topProjekte) {
		this.topProjekte = topProjekte;
	}

	public Set<Gleissperrung> getGleissperrungen() {
		return gleissperrungen;
	}

	public void setGleissperrungen(Set<Gleissperrung> gleissperrungen) {
		this.gleissperrungen = gleissperrungen;
	}

	public SAPMassnahme getAnkermassnahme() {
		return ankermassnahme;
	}

	public void setAnkermassnahme(SAPMassnahme ankermassnahme) {
		this.ankermassnahme = ankermassnahme;
	}

	public SAPMassnahme getVorbedingung() {
		return vorbedingung;
	}

	public void setVorbedingung(SAPMassnahme vorbedingung) {
		this.vorbedingung = vorbedingung;
	}

	public SAPMassnahme getVorphase() {
		return vorphase;
	}

	public void setVorphase(SAPMassnahme vorphase) {
		this.vorphase = vorphase;
	}

	public SAPMassnahme getUnterDeckung() {
		return unterDeckung;
	}

	public void setUnterDeckung(SAPMassnahme unterDeckung) {
		this.unterDeckung = unterDeckung;
	}

	public boolean isUngueltig() {
		return ungueltig;
	}

	public void setUngueltig(boolean ungueltig) {
		this.ungueltig = ungueltig;
	}

	public Boolean getDurchgehend() {
		return durchgehend;
	}

	public void setDurchgehend(Boolean durchgehend) {
		this.durchgehend = durchgehend;
	}

	public Boolean getSchichtweise() {
		return schichtweise;
	}

	public void setSchichtweise(Boolean schichtweise) {
		this.schichtweise = schichtweise;
	}

	public Verkehrstageregelung getVtr() {
		return vtr;
	}

	public void setVtr(Verkehrstageregelung vtr) {
		this.vtr = vtr;
	}

	public Arbeitstyp getArbeiten() {
		return arbeiten;
	}

	public void setArbeiten(Arbeitstyp arbeiten) {
		this.arbeiten = arbeiten;
	}

	public Betriebsstelle getArbeitenBetriebsstelle() {
		return arbeitenBetriebsstelle;
	}

	public void setArbeitenBetriebsstelle(Betriebsstelle arbeitenBetriebsstelle) {
		this.arbeitenBetriebsstelle = arbeitenBetriebsstelle;
	}

	public String getArbeitenKommentar() {
		return arbeitenKommentar;
	}

	public void setArbeitenKommentar(String arbeitenKommentar) {
		this.arbeitenKommentar = arbeitenKommentar;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public Anmelder getAnmelder() {
		return anmelder;
	}

	public void setAnmelder(Anmelder anmelder) {
		this.anmelder = anmelder;
	}

	public String getAnmelderErgaenzung() {
		return anmelderErgaenzung;
	}

	public void setAnmelderErgaenzung(String anmelderErgaenzung) {
		this.anmelderErgaenzung = anmelderErgaenzung;
	}

	public Date getErsteAnmeldung() {
		return ersteAnmeldung;
	}

	public void setErsteAnmeldung(Date ersteAnmeldung) {
		this.ersteAnmeldung = ersteAnmeldung;
	}

	public Date getLetzteAenderungAnmeldung() {
		return letzteAenderungAnmeldung;
	}

	public void setLetzteAenderungAnmeldung(Date letzteAenderungAnmeldung) {
		this.letzteAenderungAnmeldung = letzteAenderungAnmeldung;
	}

	public Boolean getGenehmigungsanforderung() {
		return genehmigungsanforderung;
	}

	public void setGenehmigungsanforderung(Boolean genehmigungsanforderung) {
		this.genehmigungsanforderung = genehmigungsanforderung;
	}

	public Boolean getBbEi() {
		return bbEi;
	}

	public void setBbEi(Boolean bbEi) {
		this.bbEi = bbEi;
	}

	public User getGenehmiger() {
		return genehmiger;
	}

	public void setGenehmiger(User genehmiger) {
		this.genehmiger = genehmiger;
	}

	public Date getGenehmigungsDatum() {
		return genehmigungsDatum;
	}

	public void setGenehmigungsDatum(Date genehmigungsDatum) {
		this.genehmigungsDatum = genehmigungsDatum;
	}

	public String getGenehmigungsbedingungen() {
		return genehmigungsbedingungen;
	}

	public void setGenehmigungsbedingungen(String genehmigungsbedingungen) {
		this.genehmigungsbedingungen = genehmigungsbedingungen;
	}

	public Regionalbereich getGenehmigerRegionalbereich() {
		return genehmigerRegionalbereich;
	}

	public void setGenehmigerRegionalbereich(Regionalbereich genehmigerRegionalbereich) {
		this.genehmigerRegionalbereich = genehmigerRegionalbereich;
	}

	public String getBetraNr() {
		return betraNr;
	}

	public void setBetraNr(String betraNr) {
		this.betraNr = betraNr;
	}

	public String getBaumassnahmeInBob() {
		return baumassnahmeInBob;
	}

	public void setBaumassnahmeInBob(String baumassnahmeInBob) {
		this.baumassnahmeInBob = baumassnahmeInBob;
	}

	public StatusBbzr getStatusBbzr() {
		return statusBbzr;
	}

	public void setStatusBbzr(StatusBbzr statusBbzr) {
		this.statusBbzr = statusBbzr;
	}

	public Boolean getLaEintragR406() {
		return laEintragR406;
	}

	public void setLaEintragR406(Boolean laEintragR406) {
		this.laEintragR406 = laEintragR406;
	}

	public Boolean getLueHinweis() {
		return lueHinweis;
	}

	public void setLueHinweis(Boolean lueHinweis) {
		this.lueHinweis = lueHinweis;
	}

	public Boolean getStudie() {
		return studie;
	}

	public void setStudie(Boolean studie) {
		this.studie = studie;
	}

	public FolgeNichtausfuehrung getFolgeNichtausfuehrung() {
		return folgeNichtausfuehrung;
	}

	public void setFolgeNichtausfuehrung(FolgeNichtausfuehrung folgeNichtausfuehrung) {
		this.folgeNichtausfuehrung = folgeNichtausfuehrung;
	}

	public Date getFolgeNichtausfuehrungBeginn() {
		return folgeNichtausfuehrungBeginn;
	}

	public void setFolgeNichtausfuehrungBeginn(Date folgeNichtausfuehrungBeginn) {
		this.folgeNichtausfuehrungBeginn = folgeNichtausfuehrungBeginn;
	}

	public Float getFolgeNichtausfuehrungFzv() {
		return folgeNichtausfuehrungFzv;
	}

	public void setFolgeNichtausfuehrungFzv(Float folgeNichtausfuehrungFzv) {
		this.folgeNichtausfuehrungFzv = folgeNichtausfuehrungFzv;
	}

	public Integer getFolgeNichtausfuehrungLaGeschwindigkeit() {
		return folgeNichtausfuehrungLaGeschwindigkeit;
	}

	public void setFolgeNichtausfuehrungLaGeschwindigkeit(
	    Integer folgeNichtausfuehrungLaGeschwindigkeit) {
		this.folgeNichtausfuehrungLaGeschwindigkeit = folgeNichtausfuehrungLaGeschwindigkeit;
	}

	public Grossmaschine getGrossmaschine() {
		return grossmaschine;
	}

	public void setGrossmaschine(Grossmaschine grossmaschine) {
		this.grossmaschine = grossmaschine;
	}

	public Finanztyp getFinanztyp() {
		return finanztyp;
	}

	public void setFinanztyp(Finanztyp finanztyp) {
		this.finanztyp = finanztyp;
	}

	public Date getAnforderungBbzr() {
		return anforderungBbzr;
	}

	public void setAnforderungBbzr(Date anforderungBbzr) {
		this.anforderungBbzr = anforderungBbzr;
	}

	public Date getEntwurfBbzr() {
		return entwurfBbzr;
	}

	public void setEntwurfBbzr(Date entwurfBbzr) {
		this.entwurfBbzr = entwurfBbzr;
	}

	public Date getErledigungBbzr() {
		return erledigungBbzr;
	}

	public void setErledigungBbzr(Date erledigungBbzr) {
		this.erledigungBbzr = erledigungBbzr;
	}

	public String getInterneBemerkungenZKbb() {
		return interneBemerkungenZKbb;
	}

	public void setInterneBemerkungenZKbb(String interneBemerkungenZKbb) {
		this.interneBemerkungenZKbb = interneBemerkungenZKbb;
	}

	public Integer getHervorhebFarbe() {
		return hervorhebFarbe;
	}

	public void setHervorhebFarbe(Integer hervorhebFarbe) {
		this.hervorhebFarbe = hervorhebFarbe;
	}

	public Boolean getNichtbehandlungIb() {
		return nichtbehandlungIb;
	}

	public void setNichtbehandlungIb(Boolean nichtbehandlungIb) {
		this.nichtbehandlungIb = nichtbehandlungIb;
	}

	public String getBbpMassnahmeId() {
		return bbpMassnahmeId;
	}

	public void setBbpMassnahmeId(String bbpMassnahmeId) {
		this.bbpMassnahmeId = bbpMassnahmeId;
	}

	public String getBbpMassnahmeIdVorphase() {
		return bbpMassnahmeIdVorphase;
	}

	public void setBbpMassnahmeIdVorphase(String bbpMassnahmeIdVorphase) {
		this.bbpMassnahmeIdVorphase = bbpMassnahmeIdVorphase;
	}

	public Boolean getHasFolgephaseMassnahme() {
		return hasFolgephaseMassnahme;
	}

	public void setHasFolgephaseMassnahme(Boolean hasFolgephaseMassnahme) {
		this.hasFolgephaseMassnahme = hasFolgephaseMassnahme;
	}

	public Boolean getEntwurf() {
		return entwurf;
	}

	public void setEntwurf(Boolean entwurf) {
		this.entwurf = entwurf;
	}

	public Integer getPrioritaetPhase() {
		return prioritaetPhase;
	}

	public void setPrioritaetPhase(Integer prioritaetPhase) {
		this.prioritaetPhase = prioritaetPhase;
	}

	public AnkermassnahmeArt getAnkermassnahmeArt() {
		return ankermassnahmeArt;
	}

	public void setAnkermassnahmeArt(AnkermassnahmeArt ankermassnahmeArt) {
		this.ankermassnahmeArt = ankermassnahmeArt;
	}

	public String getAnzeigeTemplateId() {
		return anzeigeTemplateId;
	}

	public void setAnzeigeTemplateId(String anzeigeTemplateId) {
		this.anzeigeTemplateId = anzeigeTemplateId;
	}

	public String getAnzeigeTemplateFile() {
		return anzeigeTemplateFile;
	}

	public void setAnzeigeTemplateFile(String anzeigeTemplateFile) {
		this.anzeigeTemplateFile = anzeigeTemplateFile;
	}

	public VzgStrecke getVzgStreckeBis() {
		return vzgStreckeBis;
	}

	public void setVzgStreckeBis(VzgStrecke vzgStreckeBis) {
		this.vzgStreckeBis = vzgStreckeBis;
	}

	public Anmelder getAuftraggeber() {
		return auftraggeber;
	}

	public void setAuftraggeber(Anmelder auftraggeber) {
		this.auftraggeber = auftraggeber;
	}

	public Date getBauterminStartKoordiniert() {
		return bauterminStartKoordiniert;
	}

	public void setBauterminStartKoordiniert(Date bauterminStartKoordiniert) {
		this.bauterminStartKoordiniert = bauterminStartKoordiniert;
	}

	public Date getBauterminEndeKoordiniert() {
		return bauterminEndeKoordiniert;
	}

	public void setBauterminEndeKoordiniert(Date bauterminEndeKoordiniert) {
		this.bauterminEndeKoordiniert = bauterminEndeKoordiniert;
	}

	public Betriebsstelle getBetriebsstelleVonKoordiniert() {
		return betriebsstelleVonKoordiniert;
	}

	public void setBetriebsstelleVonKoordiniert(Betriebsstelle betriebsstelleVonKoordiniert) {
		this.betriebsstelleVonKoordiniert = betriebsstelleVonKoordiniert;
	}

	public Betriebsstelle getBetriebsstelleBisKoordiniert() {
		return betriebsstelleBisKoordiniert;
	}

	public void setBetriebsstelleBisKoordiniert(Betriebsstelle betriebsstelleBisKoordiniert) {
		this.betriebsstelleBisKoordiniert = betriebsstelleBisKoordiniert;
	}

	@Transient
	public boolean isGrossmaschineneinsatz() {
		if (getGrossmaschine() == null
		    || (getGrossmaschine() != null && getGrossmaschine().getName().equals("keine"))) {
			return false;
		}
		return true;
	}

	@Transient
	public Set<Buendel> getGleissperrungsBuendel() {
		Set<Buendel> buendel = new HashSet<Buendel>();
		if (getGleissperrungen() != null) {
			for (Gleissperrung gl : getGleissperrungen()) {
				buendel.addAll(gl.getBuendel());
			}
		}
		return buendel;
	}

	/**
	 * Dieser Code ist dupliziert in der Klasse
	 * {@link db.training.osb.web.sperrpausenbedarf.SperrpausenbedarfListReport}
	 * 
	 * @return
	 */
	public String getMassnahmeId() {
		return String.format("%06d.%04d.%02d.%2d", lfdNr != null ? lfdNr : 0,
		    hauptStrecke != null ? hauptStrecke.getNummer() : 0,
		    regionalbereich != null ? regionalbereich.getId() : 0,
		    urspruenglichesPlanungsjahr != null ? (urspruenglichesPlanungsjahr - 2000) : 0);
	}

}