package db.training.bob.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import db.training.bob.model.fplo.Zugcharakteristik;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.MeilensteinService;
import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "bm")
public class Baumassnahme extends EasyPersistentObjectVc implements Historizable {

	// Stammdaten -----------------------------------------
	@Column(name = "art")
	private Art art;

	@Column(name = "kigbau")
	private Boolean kigBau = false;

	@Column(name = "streckebbp", length = 5)
	private String streckeBBP;

	@Column(name = "streckevzg", length = 5)
	private String streckeVZG;

	@Column(name = "streckenabschnitt")
	private String streckenAbschnitt;

	@Column(name = "artdermassnahme")
	private String artDerMassnahme;

	@Column(name = "betriebsweise")
	private String betriebsweise;

	@Column(name = "beginnfuerterminberechnung")
	@Temporal(TemporalType.DATE)
	private Date beginnFuerTerminberechnung;

	@Column(name = "beginndatum")
	@Temporal(TemporalType.DATE)
	private Date beginnDatum;

	@Column(name = "enddatum")
	@Temporal(TemporalType.DATE)
	private Date endDatum;

	@OneToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	private BBPMassnahme hauptBbpMassnahme;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.DELETE, CascadeType.SAVE_UPDATE })
	// @Sort(type = SortType.NATURAL)
	private Set<BBPMassnahme> bbpMassnahmen;

	@Column(name = "regionalbereichbm")
	private String regionalbereichBM;

	@ManyToOne(fetch = FetchType.LAZY)
	private Regionalbereich regionalBereichFpl;

	@ManyToOne(fetch = FetchType.LAZY)
	private Bearbeitungsbereich bearbeitungsbereich;

	@Column(name = "fplonr", length = 10)
	private String fploNr;

	@Column(name = "vorgangsnr")
	private Integer vorgangsNr;

	@Column(name = "prioritaet")
	private Prioritaet prioritaet;

	@Column(nullable = false, updatable = false, scale = 4, precision = 0)
	private int fahrplanjahr;

	// Tab Bearbeiter --------------------------------------------
	@OneToMany(cascade = javax.persistence.CascadeType.ALL, fetch = FetchType.LAZY)
	@IndexColumn(name = "lfd_nr", base = 0, nullable = false)
	private List<Bearbeiter> bearbeiter = new ArrayList<Bearbeiter>();

	// Tab Status Aktivitaeten -----------------------------------
	@Column(name = "konstruktionseinschraenkung")
	@Temporal(TemporalType.DATE)
	private Date konstruktionsEinschraenkung;

	@Column(name = "abstimmungffz")
	@Temporal(TemporalType.DATE)
	private Date abstimmungFfz;

	@Column(name = "antragaufhebungdienstruhe")
	@Temporal(TemporalType.DATE)
	private Date antragAufhebungDienstruhe;

	@Column(name = "genehmigungaufhebungdienstruhe")
	@Temporal(TemporalType.DATE)
	private Date genehmigungAufhebungDienstruhe;

	@Column(name = "biuenr", length = 20)
	private String biUeNr;

	@Column(name = "betranr")
	private String betraNr;

	// JBB/KS/QS ---------------------------------------
	@Column(name = "kigbaunr", length = 10)
	@CollectionOfElements(fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private Set<String> kigBauNr;

	@Column(name = "korridornr", length = 5)
	private Integer korridorNr;

	@Column(name = "korridorzeitfenster", length = 10)
	@CollectionOfElements(fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private Set<String> korridorZeitfenster;

	@Column(name = "qsnr", length = 9)
	@IndexColumn(name = "nr", base = 1)
	@CollectionOfElements(fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private List<String> qsNr = new ArrayList<String>();

	@Column(name = "qsspfv")
	private Boolean qsSPFV = false;

	@Column(name = "qssgv")
	private Boolean qsSGV = false;

	@Column(name = "qsspnv")
	private Boolean qsSPNV = false;

	// Tab Eskalation/Ausfall -----------------------------
	@Column(name = "eskalationsbeginn")
	@Temporal(TemporalType.DATE)
	private Date eskalationsBeginn;

	@Column(name = "eskalationsentscheidung")
	@Temporal(TemporalType.DATE)
	private Date eskalationsEntscheidung;

	@Column(name = "eskalationveto")
	private Boolean eskalationVeto = false;

	@Column(name = "ausfalldatum")
	@Temporal(TemporalType.DATE)
	private Date ausfallDatum;

	@ManyToOne(fetch = FetchType.LAZY)
	private Grund ausfallGrund;

	@Column(name = "bisherigeraufwand")
	private Integer bisherigerAufwand;

	// Tab Kommentar ---------------------------------------------
	@Lob
	@Column(name = "kommentar", length = 2000)
	private String kommentar;

	// Tab Aufwand -----------------------------------------------
	@Column(name = "aufwandzvf")
	private Integer aufwandZvF;

	@Column(name = "aufwandbiueb")
	private Integer aufwandBiUeb;

	@Column(name = "aufwandueb")
	private Integer aufwandUeb;

	@Column(name = "aufwandfplo")
	private Integer aufwandFplo;

	// Tab Aenderungsdokumentation -------------------------------
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<Aenderung> aenderungen;

	// Tab Verzicht Q-Trasse -------------------------------------
	@Column(name = "verzichtqtrassebeantragt")
	@Temporal(TemporalType.DATE)
	private Date verzichtQTrasseBeantragt;

	@Column(name = "verzichtqtrasseabgestimmt")
	@Temporal(TemporalType.DATE)
	private Date verzichtQTrasseAbgestimmt;

	@Column(name = "verzichtqtrassegenehmigt")
	private Boolean verzichtQTrasseGenehmigt = false;

	// Tab Benchmark ------------------
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Map<Regionalbereich, Benchmark> benchmark;

	// Tab Abstimmung Nachbarbahn
	@Column(name = "abstimmungnberforderlich")
	private Boolean abstimmungNbErforderlich = false;

	@OneToOne
	private Nachbarbahn nachbarbahn;

	@Column(name = "abstimmungnberfolgtam")
	@Temporal(TemporalType.DATE)
	private Date abstimmungNbErfolgtAm;

	// Tab Quality-Gates
	@OneToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private TerminUebersichtBaubetriebsplanung baubetriebsplanung;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<TerminUebersichtGueterverkehrsEVU> gevus;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<TerminUebersichtPersonenverkehrsEVU> pevus;

	// Tab Übergabeblatt
	@OneToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Uebergabeblatt uebergabeblatt;

	// Tab Zvf
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private List<Uebergabeblatt> zvf;

	// Tab Fahrplan
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.DELETE_ORPHAN)
	private Set<Zugcharakteristik> zugcharakteristik;

	// -------------------------------------------------------

	public Baumassnahme() {
		this.art = Art.A;
		this.kommentar = "";
		this.aenderungen = new HashSet<Aenderung>();
		this.bisherigerAufwand = 0;
		this.aufwandBiUeb = 0;
		this.aufwandFplo = 0;
		this.aufwandUeb = 0;
		this.aufwandZvF = 0;
		this.baubetriebsplanung = new TerminUebersichtBaubetriebsplanung();
		this.pevus = new HashSet<TerminUebersichtPersonenverkehrsEVU>();
		this.gevus = new HashSet<TerminUebersichtGueterverkehrsEVU>();
	}

	public Prioritaet getPrioritaet() {
		return prioritaet;
	}

	public void setPrioritaet(Prioritaet prioritaet) {
		this.prioritaet = prioritaet;
	}

	/**
	 * Das Fahrplanjahr kann nach der Erstellung der Baumaßnahme (INSERT) nicht mehr verändert
	 * werden.
	 * 
	 * @param fahrplanjahr
	 *            das Fahrplanjahr, dem die Baumaßnahme zugeordnet ist.
	 */
	public void setFahrplanjahr(int fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	/**
	 * @return gibt das Fahrplanjahr zurück, dem die Baumaßnahme zugeordnet ist
	 */
	public int getFahrplanjahr() {
		return fahrplanjahr;
	}

	public Art getArt() {
		return art;
	}

	public void setArt(Art art) {
		this.art = art;
	}

	public Boolean getKigBau() {
		return kigBau;
	}

	public void setKigBau(Boolean kigBau) {
		this.kigBau = kigBau;
	}

	public String getStreckeBBP() {
		return streckeBBP;
	}

	public void setStreckeBBP(String streckeBBP) {
		this.streckeBBP = streckeBBP;
	}

	public String getStreckenAbschnitt() {
		return streckenAbschnitt;
	}

	public void setStreckenAbschnitt(String streckenAbschnitt) {
		this.streckenAbschnitt = streckenAbschnitt;
	}

	public String getArtDerMassnahme() {
		return artDerMassnahme;
	}

	public void setArtDerMassnahme(String artDerMassnahme) {
		this.artDerMassnahme = artDerMassnahme;
	}

	public String getBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(String betriebsweise) {
		this.betriebsweise = betriebsweise;
	}

	public Date getBeginnDatum() {
		return beginnDatum;
	}

	public void setBeginnDatum(Date beginnDatum) {
		this.beginnDatum = beginnDatum;
	}

	public Date getEndDatum() {
		return endDatum;
	}

	public void setEndDatum(Date endDatum) {
		this.endDatum = endDatum;
	}

	public String getBauzeitraum() {
		StringBuilder sb = new StringBuilder();
		sb.append(FrontendHelper.castDateToString(getBeginnDatum()));
		sb.append(" - ");
		sb.append(FrontendHelper.castDateToString(getEndDatum()));
		return sb.toString();
	}

	public String getFploNr() {
		return fploNr;
	}

	public void setFploNr(String fploNr) {
		this.fploNr = fploNr;
	}

	public Date getKonstruktionsEinschraenkung() {
		return konstruktionsEinschraenkung;
	}

	public void setKonstruktionsEinschraenkung(Date konstruktionsEinschraenkung) {
		this.konstruktionsEinschraenkung = konstruktionsEinschraenkung;
	}

	public Date getAbstimmungFfz() {
		return abstimmungFfz;
	}

	public void setAbstimmungFfz(Date abstimmungFfz) {
		this.abstimmungFfz = abstimmungFfz;
	}

	public Date getAntragAufhebungDienstruhe() {
		return antragAufhebungDienstruhe;
	}

	public void setAntragAufhebungDienstruhe(Date antragAufhebungDienstruhe) {
		this.antragAufhebungDienstruhe = antragAufhebungDienstruhe;
	}

	public Date getGenehmigungAufhebungDienstruhe() {
		return genehmigungAufhebungDienstruhe;
	}

	public void setGenehmigungAufhebungDienstruhe(Date genehmigungAufhebungDienstruhe) {
		this.genehmigungAufhebungDienstruhe = genehmigungAufhebungDienstruhe;
	}

	public String getBiUeNr() {
		return biUeNr;
	}

	public void setBiUeNr(String biUeNr) {
		this.biUeNr = biUeNr;
	}

	public String getBetraNr() {
		return betraNr;
	}

	public void setBetraNr(String betraNr) {
		this.betraNr = betraNr;
	}

	public Set<String> getKigBauNr() {
		return kigBauNr;
	}

	public void setKigBauNr(Set<String> kigBauNr) {
		this.kigBauNr = kigBauNr;
	}

	public Integer getKorridorNr() {
		return korridorNr;
	}

	public void setKorridorNr(Integer korridorNr) {
		this.korridorNr = korridorNr;
	}

	public Set<String> getKorridorZeitfenster() {
		return korridorZeitfenster;
	}

	public void setKorridorZeitfenster(Set<String> korridorZeitfenster) {
		this.korridorZeitfenster = korridorZeitfenster;
	}

	public List<String> getQsNr() {
		return qsNr;
	}

	public void setQsNr(List<String> qsNr) {
		this.qsNr = qsNr;
	}

	public Boolean getQsSPFV() {
		return qsSPFV;
	}

	public void setQsSPFV(Boolean qsSPFV) {
		this.qsSPFV = qsSPFV;
	}

	public Boolean getQsSGV() {
		return qsSGV;
	}

	public void setQsSGV(Boolean qsSGV) {
		this.qsSGV = qsSGV;
	}

	public Boolean getQsSPNV() {
		return qsSPNV;
	}

	public void setQsSPNV(Boolean qsSPNV) {
		this.qsSPNV = qsSPNV;
	}

	public Date getEskalationsBeginn() {
		return eskalationsBeginn;
	}

	public void setEskalationsBeginn(Date eskalationsBeginn) {
		this.eskalationsBeginn = eskalationsBeginn;
	}

	public Date getEskalationsEntscheidung() {
		return eskalationsEntscheidung;
	}

	public void setEskalationsEntscheidung(Date eskalationsEntscheidung) {
		this.eskalationsEntscheidung = eskalationsEntscheidung;
	}

	public Boolean getEskalationVeto() {
		return eskalationVeto;
	}

	public void setEskalationVeto(Boolean eskalationVeto) {
		this.eskalationVeto = eskalationVeto;
	}

	public Date getAusfallDatum() {
		return ausfallDatum;
	}

	public void setAusfallDatum(Date ausfallDatum) {
		this.ausfallDatum = ausfallDatum;
	}

	public Grund getAusfallGrund() {
		return ausfallGrund;
	}

	public void setAusfallGrund(Grund ausfallGrund) {
		this.ausfallGrund = ausfallGrund;
	}

	public Integer getBisherigerAufwand() {
		return bisherigerAufwand;
	}

	public void setBisherigerAufwand(Integer bisherigerAufwand) {
		this.bisherigerAufwand = bisherigerAufwand;
	}

	/**
	 * Minuten in Format hh:mm konvertieren und ggfs. mit 0 auffüllen
	 * 
	 * @return
	 */
	public String getBisherigerAufwandTimeString() {
		return FrontendHelper.castMinutesToTimeString(getBisherigerAufwand());
	}

	public void setBisherigerAufwandTimeString(String s) {
		if (s == null) {
			return;
		}

		setBisherigerAufwand(FrontendHelper.castTimeStringToMinutes(s));
	}

	public String getRegionalbereichBM() {
		return regionalbereichBM;
	}

	public void setRegionalbereichBM(String regionalbereichBM) {
		this.regionalbereichBM = regionalbereichBM;
	}

	public Bearbeitungsbereich getBearbeitungsbereich() {
		return bearbeitungsbereich;
	}

	public void setBearbeitungsbereich(Bearbeitungsbereich bearbeitungsbereich) {
		this.bearbeitungsbereich = bearbeitungsbereich;
	}

	public Regionalbereich getRegionalBereichFpl() {
		return regionalBereichFpl;
	}

	public void setRegionalBereichFpl(Regionalbereich regionalBereichFpl) {
		this.regionalBereichFpl = regionalBereichFpl;
	}

	public List<Bearbeiter> getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(List<Bearbeiter> bearbeiter) {
		this.bearbeiter = bearbeiter;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public Integer getAufwandZvF() {
		return aufwandZvF;
	}

	public void setAufwandZvF(Integer zvF) {
		this.aufwandZvF = zvF;
	}

	public Integer getAufwandBiUeb() {
		return aufwandBiUeb;
	}

	public void setAufwandBiUeb(Integer biUeb) {
		this.aufwandBiUeb = biUeb;
	}

	public Integer getAufwandUeb() {
		return aufwandUeb;
	}

	public void setAufwandUeb(Integer ueb) {
		this.aufwandUeb = ueb;
	}

	public Date getVerzichtQTrasseBeantragt() {
		return verzichtQTrasseBeantragt;
	}

	public void setVerzichtQTrasseBeantragt(Date verzichtQTrasseBeantragt) {
		this.verzichtQTrasseBeantragt = verzichtQTrasseBeantragt;
	}

	public Date getVerzichtQTrasseAbgestimmt() {
		return verzichtQTrasseAbgestimmt;
	}

	public void setVerzichtQTrasseAbgestimmt(Date verzichtQTrasseAbgestimmt) {
		this.verzichtQTrasseAbgestimmt = verzichtQTrasseAbgestimmt;
	}

	public Boolean getVerzichtQTrasseGenehmigt() {
		return verzichtQTrasseGenehmigt;
	}

	public void setVerzichtQTrasseGenehmigt(Boolean verzichtQTrasseGenehmigt) {
		this.verzichtQTrasseGenehmigt = verzichtQTrasseGenehmigt;
	}

	public Integer getAufwandFplo() {
		return aufwandFplo;
	}

	public void setAufwandFplo(Integer fplo) {
		this.aufwandFplo = fplo;
	}

	public Set<Aenderung> getAenderungen() {
		// System.out.println("SIZE:" + aenderungen.size());
		return aenderungen;
	}

	public void setAenderungen(Set<Aenderung> aenderungen) {
		// System.out.println("ARG_SIZE:" + aenderungen.size());
		this.aenderungen = aenderungen;
	}

	public Map<Regionalbereich, Benchmark> getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(Map<Regionalbereich, Benchmark> benchmark) {
		this.benchmark = benchmark;
	}

	public Boolean getAbstimmungNbErforderlich() {
		return abstimmungNbErforderlich;
	}

	public void setAbstimmungNbErforderlich(Boolean abstimmungNbErforderlich) {
		this.abstimmungNbErforderlich = abstimmungNbErforderlich;
	}

	public Nachbarbahn getNachbarbahn() {
		return nachbarbahn;
	}

	public void setNachbarbahn(Nachbarbahn nachbarbahn) {
		this.nachbarbahn = nachbarbahn;
	}

	public Date getAbstimmungNbErfolgtAm() {
		return abstimmungNbErfolgtAm;
	}

	public void setAbstimmungNbErfolgtAm(Date abstimmungNbErfolgtAm) {
		this.abstimmungNbErfolgtAm = abstimmungNbErfolgtAm;
	}

	public TerminUebersichtBaubetriebsplanung getBaubetriebsplanung() {
		// baubetriebsplanung.setSollTermine(beginnFuerTerminberechnung, art);
		if (baubetriebsplanung != null) {
			baubetriebsplanung.setTerminStatus();
		}

		return baubetriebsplanung;
	}

	public void setBaubetriebsplanung(TerminUebersichtBaubetriebsplanung baubetriebsplanung) {
		this.baubetriebsplanung = baubetriebsplanung;

		if (baubetriebsplanung != null)
			baubetriebsplanung.setSollTermine(beginnFuerTerminberechnung, art);
	}

	public void setHauptBbpMassnahme(BBPMassnahme bbpMassnahme) {
		this.hauptBbpMassnahme = bbpMassnahme;
	}

	public BBPMassnahme getHauptBbpMassnahme() {
		return this.hauptBbpMassnahme;
	}

	public void setBbpMassnahmen(Set<BBPMassnahme> bbpMassnahmen) {
		this.bbpMassnahmen = bbpMassnahmen;
	}

	public Set<BBPMassnahme> getBbpMassnahmen() {
		SortedSet<BBPMassnahme> sortedbbps = new TreeSet<BBPMassnahme>();
		sortedbbps.addAll(bbpMassnahmen);
		return sortedbbps;
	}

	public Integer getVorgangsNr() {
		return vorgangsNr;
	}

	public void setVorgangsNr(Integer vorgangsNr) {
		this.vorgangsNr = vorgangsNr;
	}

	public List<String> getSollTermineBBP() {
		List<String> sollTermine = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		
		MeilensteinService service = EasyServiceFactory.getInstance().createMeilensteinService();
		
		if (baubetriebsplanung != null) {
			sollTermine.add(""); // Studie/Grobkonzept
			
			sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung, art, 
				Schnittstelle.BBP, Meilensteinbezeichnungen.ANF_BBZR), df));
			if (art.equals(Art.A) || art.equals(Art.QS) || art.equals(Art.KS))
				sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung, art, 
					Schnittstelle.BBP, Meilensteinbezeichnungen.BIUE_ENTW), df));
			
			sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung, art, 
				Schnittstelle.BBP, Meilensteinbezeichnungen.ZVF_ENTW), df));

			if (art.equals(Art.A) || art.equals(Art.QS) || art.equals(Art.KS))
				sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung, art, 
					Schnittstelle.BBP, Meilensteinbezeichnungen.GESAMTKONZ_BBZR), df));
				
			
			sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung, art, 
				Schnittstelle.BBP, Meilensteinbezeichnungen.ZVF), df));
			
		}
		return sollTermine;
	}

	public List<String> getSollTermineGEVU() {
		List<String> sollTermine = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		MeilensteinService service = EasyServiceFactory.getInstance().createMeilensteinService();

		sollTermine.add(""); // Studie/Grobkonzept
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.GEVU, Meilensteinbezeichnungen.ZVF_ENTW), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.GEVU, Meilensteinbezeichnungen.STELLUNGN_EVU), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.GEVU, Meilensteinbezeichnungen.ZVF), df));
		if (art.equals(Art.B))
			sollTermine.add(""); // Master-Übergabeblatt GV
		else
			sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
			    art, Schnittstelle.GEVU, Meilensteinbezeichnungen.M_UEB_GV), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.GEVU, Meilensteinbezeichnungen.UEB_GV), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.GEVU, Meilensteinbezeichnungen.FPLO), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.GEVU, Meilensteinbezeichnungen.EING_GFDZ), df));

		return sollTermine;
	}

	public List<String> getSollTerminePEVU() {
		List<String> sollTermine = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		MeilensteinService service = EasyServiceFactory.getInstance().createMeilensteinService();

		sollTermine.add(""); // Studie/Grobkonzept
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.ZVF_ENTW), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.STELLUNGN_EVU), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.ZVF), df));
		if (art.equals(Art.B))
			sollTermine.add(""); // Master-Übergabeblatt GV
		else
			sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
			    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.M_UEB_PV), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.UEB_PV), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.FPLO), df));
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.EING_GFDZ), df));
		sollTermine.add(""); // Ausfaelle/SEV
		sollTermine.add(EasyDateFormat.format(service.getSollTermin(beginnFuerTerminberechnung,
		    art, Schnittstelle.PEVU, Meilensteinbezeichnungen.BKONZ_EVU), df));

		return sollTermine;
	}

	public String getStreckeVZG() {
		return streckeVZG;
	}

	public void setStreckeVZG(String streckeVZG) {
		this.streckeVZG = streckeVZG;
	}

	public Set<TerminUebersichtGueterverkehrsEVU> getGevus() {

		if (gevus != null && gevus.size() > 0) {
			Iterator<TerminUebersichtGueterverkehrsEVU> it = gevus.iterator();
			TerminUebersichtGueterverkehrsEVU gevu = null;
			while (it.hasNext()) {
				gevu = it.next();
				gevu.setTerminStatus();
			}
		}
		return gevus;
	}

	public void setGevus(Set<TerminUebersichtGueterverkehrsEVU> gevus) {
		this.gevus = gevus;

		if (gevus != null && gevus.size() > 0) {
			Iterator<TerminUebersichtGueterverkehrsEVU> it = gevus.iterator();
			TerminUebersichtGueterverkehrsEVU gevu = null;
			while (it.hasNext()) {
				gevu = it.next();
				gevu.setSollTermine(beginnFuerTerminberechnung, art);
				gevu.setTerminStatus();
			}
		}
	}

	public Set<TerminUebersichtPersonenverkehrsEVU> getPevus() {

		if (pevus != null && pevus.size() > 0) {
			Iterator<TerminUebersichtPersonenverkehrsEVU> it = pevus.iterator();
			TerminUebersichtPersonenverkehrsEVU pevu = null;
			while (it.hasNext()) {
				pevu = it.next();
				pevu.setTerminStatus();
			}
		}

		return pevus;
	}

	public void setPevus(Set<TerminUebersichtPersonenverkehrsEVU> pevus) {
		this.pevus = pevus;

		if (pevus != null && pevus.size() > 0) {
			Iterator<TerminUebersichtPersonenverkehrsEVU> it = pevus.iterator();
			TerminUebersichtPersonenverkehrsEVU pevu = null;
			while (it.hasNext()) {
				pevu = it.next();
				pevu.setSollTermine(beginnFuerTerminberechnung, art);
				pevu.setTerminStatus();
			}
		}
	}

	public Date getBeginnFuerTerminberechnung() {
		return beginnFuerTerminberechnung;
	}

	public void setBeginnFuerTerminberechnung(Date beginnFuerTerminberechnung) {
		this.beginnFuerTerminberechnung = beginnFuerTerminberechnung;
	}

	/**
	 * aggregiert die Status aller PEVUs der Baumaßnahme zu einer Gesamtübersicht der Meilensteine.
	 * Ist mindestens bei einem EVU ein Termin überschritten, ist der Gesamtstatus für diesen Termin
	 * rot.
	 * 
	 * <pre>
	 * Beispiel:
	 * PEVU1 - ZvF (grün) - BiÜ (grün)
	 * PEVU2 - ZvF (rot)  - BiÜ (grün)
	 * PEVU3 - ZvF (grün) - BiÜ (neutral)
	 * ----------------------------------
	 * Gesamt- ZvF (rot)  - BiÜ (grün)
	 * </pre>
	 * 
	 * @return
	 */
	public TerminUebersichtPersonenverkehrsEVU getPevusStatus() {
		TerminUebersichtPersonenverkehrsEVU result = new TerminUebersichtPersonenverkehrsEVU();

		// result.setSollTermine(this.getBeginnDatum(), this.getArt());
		// result.setTerminStatus();

		for (TerminUebersichtPersonenverkehrsEVU pevu : getPevus()) {
			if (pevu.isMasterUebergabeblattPVErforderlich()) {
				result.setMasterUebergabeblattPVErforderlich(true);
			}
			if (pevu.isUebergabeblattPVErforderlich()) {
				result.setUebergabeblattPVErforderlich(true);
			}

			// Wert von GEVU nur in Ergebnis übernehmen, wenn der neue Status eine höhere
			// Priorität hat
			if (result.getZvfEntwurfStatus().compareTo(pevu.getZvfEntwurfStatus()) < 0)
				result.setZvfEntwurfStatus(pevu.getZvfEntwurfStatus());

			if (result.getStellungnahmeEVUStatus().compareTo(pevu.getStellungnahmeEVUStatus()) < 0)
				result.setStellungnahmeEVUStatus(pevu.getStellungnahmeEVUStatus());

			if (result.getZvFStatus().compareTo(pevu.getZvFStatus()) < 0)
				result.setZvFStatus(pevu.getZvFStatus());

			if (result.getMasterUebergabeblattPVStatus().compareTo(
			    pevu.getMasterUebergabeblattPVStatus()) < 0)
				result.setMasterUebergabeblattPVStatus(pevu.getMasterUebergabeblattPVStatus());

			if (result.getUebergabeblattPVStatus().compareTo(pevu.getUebergabeblattPVStatus()) < 0)
				result.setUebergabeblattPVStatus(pevu.getUebergabeblattPVStatus());

			if (result.getFploStatus().compareTo(pevu.getFploStatus()) < 0)
				result.setFploStatus(pevu.getFploStatus());

			if (result.getEingabeGFD_ZStatus().compareTo(pevu.getEingabeGFD_ZStatus()) < 0)
				result.setEingabeGFD_ZStatus(pevu.getEingabeGFD_ZStatus());

			if (result.getBKonzeptEVUStatus().compareTo(pevu.getBKonzeptEVUStatus()) < 0)
				result.setBKonzeptEVUStatus(pevu.getBKonzeptEVUStatus());

			if (pevu.isMasterUebergabeblattPVErforderlich())
				result.setMasterUebergabeblattPVErforderlich(true);

			if (pevu.isStellungnahmeEVUErforderlich())
				result.setStellungnahmeEVUErforderlich(true);

			if (pevu.isUebergabeblattPVErforderlich())
				result.setUebergabeblattPVErforderlich(true);

			if (pevu.isZvfEntwurfErforderlich())
				result.setZvfEntwurfErforderlich(true);

			result.setMasterUebergabeblattPVSoll(pevu.getMasterUebergabeblattPVSoll());
			result.setUebergabeblattPVSoll(pevu.getUebergabeblattPVSoll());
			result.setBKonzeptEVUSoll(pevu.getBKonzeptEVUSoll());
			result.setStudieGrobkonzeptSoll(pevu.getStudieGrobkonzeptSoll());
			result.setZvfEntwurfSoll(pevu.getZvfEntwurfSoll());
			result.setStellungnahmeEVUSoll(pevu.getStellungnahmeEVUSoll());
			result.setZvFSoll(pevu.getZvFSoll());
			result.setFploSoll(pevu.getFploSoll());
			result.setEingabeGFD_ZSoll(pevu.getEingabeGFD_ZSoll());
		}

		return result;
	}

	/**
	 * aggregiert die Status aller PEVUs der Baumaßnahme zu einer Gesamtübersicht der Meilensteine.
	 * Ist mindestens bei einem EVU ein Termin überschritten, ist der Gesamtstatus für diesen Termin
	 * rot.
	 * 
	 * <pre>
	 * Beispiel:
	 * PEVU1 - ZvF (grün) - BiÜ (grün)
	 * PEVU2 - ZvF (rot)  - BiÜ (grün)
	 * PEVU3 - ZvF (grün) - BiÜ (neutral)
	 * ----------------------------------
	 * Gesamt- ZvF (rot)  - BiÜ (grün)
	 * </pre>
	 * 
	 * @return
	 */
	public TerminUebersichtGueterverkehrsEVU getGevusStatus() {
		TerminUebersichtGueterverkehrsEVU result = new TerminUebersichtGueterverkehrsEVU();

		Set<TerminUebersichtGueterverkehrsEVU> gevus = getGevus();

		if (gevus != null && gevus.size() > 0) {

			// result.setSollTermine(this.getBeginnDatum(), this.getArt());
			// result.setTerminStatus();

			for (TerminUebersichtGueterverkehrsEVU gevu : getGevus()) {
				if (gevu.isMasterUebergabeblattGVErforderlich()) {
					result.setMasterUebergabeblattGVErforderlich(true);
				}
				if (gevu.isUebergabeblattGVErforderlich()) {
					result.setUebergabeblattGVErforderlich(true);
				}

				// Wert von GEVU nur in Ergebnis übernehmen, wenn der neue Status eine höhere
				// Priorität hat
				if (result.getZvfEntwurfStatus().compareTo(gevu.getZvfEntwurfStatus()) < 0)
					result.setZvfEntwurfStatus(gevu.getZvfEntwurfStatus());

				if (result.getStellungnahmeEVUStatus().compareTo(gevu.getStellungnahmeEVUStatus()) < 0)
					result.setStellungnahmeEVUStatus(gevu.getStellungnahmeEVUStatus());

				if (result.getZvFStatus().compareTo(gevu.getZvFStatus()) < 0)
					result.setZvFStatus(gevu.getZvFStatus());

				if (result.getMasterUebergabeblattGVStatus().compareTo(
				    gevu.getMasterUebergabeblattGVStatus()) < 0)
					result.setMasterUebergabeblattGVStatus(gevu.getMasterUebergabeblattGVStatus());

				if (result.getUebergabeblattGVStatus().compareTo(gevu.getUebergabeblattGVStatus()) < 0)
					result.setUebergabeblattGVStatus(gevu.getUebergabeblattGVStatus());

				if (result.getFploStatus().compareTo(gevu.getFploStatus()) < 0)
					result.setFploStatus(gevu.getFploStatus());

				if (result.getEingabeGFD_ZStatus().compareTo(gevu.getEingabeGFD_ZStatus()) < 0)
					result.setEingabeGFD_ZStatus(gevu.getEingabeGFD_ZStatus());

				if (gevu.isMasterUebergabeblattGVErforderlich())
					result.setMasterUebergabeblattGVErforderlich(true);

				if (gevu.isStellungnahmeEVUErforderlich())
					result.setStellungnahmeEVUErforderlich(true);

				if (gevu.isUebergabeblattGVErforderlich())
					result.setUebergabeblattGVErforderlich(true);

				if (gevu.isZvfEntwurfErforderlich())
					result.setZvfEntwurfErforderlich(true);

				result.setMasterUebergabeblattGVSoll(gevu.getMasterUebergabeblattGVSoll());
				result.setUebergabeblattGVSoll(gevu.getUebergabeblattGVSoll());
				result.setStudieGrobkonzeptSoll(gevu.getStudieGrobkonzeptSoll());
				result.setZvfEntwurfSoll(gevu.getZvfEntwurfSoll());
				result.setStellungnahmeEVUSoll(gevu.getStellungnahmeEVUSoll());
				result.setZvFSoll(gevu.getZvFSoll());
				result.setFploSoll(gevu.getFploSoll());
				result.setEingabeGFD_ZSoll(gevu.getEingabeGFD_ZSoll());
			}
		}

		return result;
	}

	/**
	 * @return the uebergabeblatt
	 */
	public Uebergabeblatt getUebergabeblatt() {
		return uebergabeblatt;
	}

	/**
	 * @param uebergabeblatt
	 *            the uebergabeblatt to set
	 */
	public void setUebergabeblatt(Uebergabeblatt uebergabeblatt) {
		this.uebergabeblatt = uebergabeblatt;
	}

	public List<Uebergabeblatt> getZvf() {
		return zvf;
	}

	public void setZvf(List<Uebergabeblatt> zvf) {
		this.zvf = zvf;
	}

	public Uebergabeblatt getAktuelleZvf() {
		if (zvf == null || zvf.size() == 0)
			return null;
		Uebergabeblatt aktuell = zvf.get(0);
		String versionAktuell = aktuell.getVersion();
		String versionNext = null;
		int comparison = 0;
		for (Uebergabeblatt bbzr : this.zvf) {
			versionNext = bbzr.getVersion();
			try {
				comparison = versionAktuell.compareTo(versionNext);
			} catch (Exception e) {
				return aktuell;
			}
			if (comparison < 0)
				aktuell = bbzr;
		}
		return aktuell;
	}

	public void setZugcharakteristik(Set<Zugcharakteristik> zugcharakteristik) {
		this.zugcharakteristik = zugcharakteristik;
	}

	public Set<Zugcharakteristik> getZugcharakteristik() {
		return zugcharakteristik;
	}

	public void addBearbeiter(User user) {
		setBearbeiterStatus(user, true);
	}

	public void removeBearbeiter(User user) {
		Iterator<Bearbeiter> it = this.bearbeiter.iterator();
		Bearbeiter entry = null;
		while (it.hasNext()) {
			entry = it.next();
			if (entry.getUser().equals(user)) {
				this.bearbeiter.remove(entry);
				break;
			}
		}
	}

	public void setBearbeiterStatus(User user, Boolean status) {
		Bearbeiter bearbeiter = new Bearbeiter(user, status);
		if (this.bearbeiter.contains(bearbeiter)) {
			int index = this.bearbeiter.indexOf(bearbeiter);
			bearbeiter = this.bearbeiter.get(index);
			bearbeiter.setAktiv(status);
		} else {
			this.bearbeiter.add(bearbeiter);
		}
	}

	public StatusUebersichtGesamt getGesamtStatus() {
		StatusUebersichtGesamt result = new StatusUebersichtGesamt();
		StatusType anforderungBBZRStatus = StatusType.NEUTRAL;
		StatusType biUeEntwurfStatus = StatusType.NEUTRAL;
		StatusType zvfEntwurfStatus = StatusType.NEUTRAL;
		StatusType stellungnahmeEVUStatus = StatusType.NEUTRAL;
		StatusType zvfStatus = StatusType.NEUTRAL;
		StatusType masterUebergabeblattPVStatus = StatusType.NEUTRAL;
		StatusType masterUebergabeblattGVStatus = StatusType.NEUTRAL;
		StatusType uebergabeblattPVStatus = StatusType.NEUTRAL;
		StatusType uebergabeblattGVStatus = StatusType.NEUTRAL;
		StatusType fploStatus = StatusType.NEUTRAL;
		StatusType eingabeGFD_ZStatus = StatusType.NEUTRAL;

		if (pevus != null) {
			for (TerminUebersichtPersonenverkehrsEVU pevu : pevus) {
				zvfEntwurfStatus = Status.aggregate(zvfEntwurfStatus, pevu.getZvfEntwurfStatus());
				stellungnahmeEVUStatus = Status.aggregate(stellungnahmeEVUStatus,
				    pevu.getStellungnahmeEVUStatus());
				zvfStatus = Status.aggregate(zvfStatus, pevu.getZvFStatus());
				masterUebergabeblattPVStatus = Status.aggregate(masterUebergabeblattPVStatus,
				    pevu.getMasterUebergabeblattPVStatus());
				uebergabeblattPVStatus = Status.aggregate(uebergabeblattPVStatus,
				    pevu.getUebergabeblattPVStatus());
				fploStatus = Status.aggregate(fploStatus, pevu.getFploStatus());
				eingabeGFD_ZStatus = Status.aggregate(eingabeGFD_ZStatus,
				    pevu.getEingabeGFD_ZStatus());
			}
		}

		if (gevus != null) {
			for (TerminUebersichtGueterverkehrsEVU gevu : gevus) {
				zvfEntwurfStatus = Status.aggregate(zvfEntwurfStatus, gevu.getZvfEntwurfStatus());
				stellungnahmeEVUStatus = Status.aggregate(stellungnahmeEVUStatus,
				    gevu.getStellungnahmeEVUStatus());
				zvfStatus = Status.aggregate(zvfStatus, gevu.getZvFStatus());
				masterUebergabeblattGVStatus = Status.aggregate(masterUebergabeblattGVStatus,
				    gevu.getMasterUebergabeblattGVStatus());
				uebergabeblattGVStatus = Status.aggregate(uebergabeblattGVStatus,
				    gevu.getUebergabeblattGVStatus());
				fploStatus = Status.aggregate(fploStatus, gevu.getFploStatus());
				eingabeGFD_ZStatus = Status.aggregate(eingabeGFD_ZStatus,
				    gevu.getEingabeGFD_ZStatus());
			}
		}

		anforderungBBZRStatus = baubetriebsplanung.getAnforderungBBZRStatus();
		biUeEntwurfStatus = baubetriebsplanung.getBiUeEntwurfStatus();
		zvfEntwurfStatus = Status.aggregate(zvfEntwurfStatus,
		    baubetriebsplanung.getZvfEntwurfStatus());
		zvfStatus = Status.aggregate(zvfStatus, baubetriebsplanung.getZvfStatus());

		result.setAnforderungBBZRStatus(anforderungBBZRStatus);
		result.setBiUeEntwurfStatus(biUeEntwurfStatus);
		result.setZvfEntwurfStatus(zvfEntwurfStatus);
		result.setStellungnahmeEVUStatus(stellungnahmeEVUStatus);
		result.setZvfStatus(zvfStatus);
		result.setMasterUebergabeblattPVStatus(masterUebergabeblattPVStatus);
		result.setMasterUebergabeblattGVStatus(masterUebergabeblattGVStatus);
		result.setUebergabeblattPVStatus(uebergabeblattPVStatus);
		result.setUebergabeblattGVStatus(uebergabeblattGVStatus);
		result.setFploStatus(fploStatus);
		result.setEingabeGFD_ZStatus(eingabeGFD_ZStatus);

		return result;
	}
}
