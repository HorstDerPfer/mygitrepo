package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import db.training.bob.model.Aenderung;
import db.training.bob.model.Art;
import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.easy.core.model.User;
import db.training.easy.util.FrontendHelper;
import db.training.osb.util.ConfigResources;

@SuppressWarnings("serial")
public class BaumassnahmeForm extends ValidatorForm {

	private Baumassnahme baumassnahme;

	private Boolean benchmarkOnly;

	// Stammdaten
	private Integer id;

	private String art;

	private Boolean kigBau;

	private String streckeBBP;

	private String streckeVZG;

	private String streckenAbschnitt;

	private String artDerMassnahme;

	private String betriebsweise;

	private String beginnFuerTerminberechnung;

	private String beginnDatum;

	private String endDatum;

	private Set<Integer> bbpMassnahmen;

	private String regionalbereichBM;

	private Integer regionalBereichFpl;

	private Integer bearbeitungsbereich;

	private List<Bearbeitungsbereich> bearbeitungsbereichList = new ArrayList<Bearbeitungsbereich>();

	private String fploNr;

	private Integer vorgangsNr;

	private String prioritaet;

	private Map<String, Boolean> bearbeiter;

	private Integer insertBearbeiter;

	// JBB/KS/QS
	private String kigBauNr;

	private String korridorNr;

	private String korridorZeitfenster;

	private String qsNr;

	private boolean qsSPFV;

	private boolean qsSGV;

	private boolean qsSPNV;

	// BBP
	private Set<BBPMassnahme> bbpMassnahmenSet;
	
	// Status Aktivitaeten
	private String konstruktionsEinschraenkung;

	private String abstimmungFfz;

	private String antragAufhebungDienstruhe;

	private String genehmigungAufhebungDienstruhe;

	private String biUeNr;

	private String betraNr;

	// Eskalation/Ausfall
	private String eskalationsBeginn;

	private String eskalationsEntscheidung;

	private boolean eskalationVeto;

	private String ausfallDatum;

	private Integer ausfallGrund;

	private String bisherigerAufwand;

	// Aufwand
	// werden seit Änderungsliste 07/2008 nicht mehr verwendet
	private String aufwandZvF;

	private String aufwandBiUeb;

	private String aufwandUeb;

	private String aufwandFplo;

	// Kommentar
	private String kommentar;

	// Verzicht Q-Trasse
	private String verzichtQTrasseBeantragt;

	private String verzichtQTrasseAbgestimmt;

	private Boolean verzichtQTrasseGenehmigt;

	// Aenderungsdokumentation
	private List<Aenderung> aenderungen;

	// Übergabeblatt
	private Integer uebergabeblatt;

	// ÜB, Header
	private String senderName;

	private String senderVorname;

	private String senderStrasse;

	private String senderPLZ;

	private String senderOrt;

	private String senderTelefon;

	private String senderTelefonIntern;

	private String senderEmail;

	private String uebFplo1;

	private String uebFplo2;

	private String uebFplo3;

	private String uebFplo4;

	private String uebFplo5;

	private String uebFplo6;

	private String uebFplo0;

	private Boolean uebRb1;

	private Boolean uebRb2;

	private Boolean uebRb3;

	private Boolean uebRb4;

	private Boolean uebRb5;

	private Boolean uebRb6;

	private Boolean uebRb0;

	// ÜB, Züge
	private final Map<String, String> zugVerkehrstag = new HashMap<String, String>();

	private final Map<String, String> zugZuggattung = new HashMap<String, String>();// Zugbez

	private final Map<String, String> zugZugnr = new HashMap<String, String>();

	private final Map<String, String> zugAbgangsbhf = new HashMap<String, String>();

	private final Map<String, String> zugZielbhf = new HashMap<String, String>();

	private final Map<String, String> zugTfz = new HashMap<String, String>();

	private final Map<String, String> zugLast = new HashMap<String, String>();

	private final Map<String, String> zugMbr = new HashMap<String, String>();

	private final Map<String, String> zugLaenge = new HashMap<String, String>();

	private final Map<String, String> zugVmax = new HashMap<String, String>();

	private final Map<String, String> zugKvProfil = new HashMap<String, String>();

	private final Map<String, String> zugStreckenklasse = new HashMap<String, String>();

	private final Map<String, String> zugBemerkung = new HashMap<String, String>();

	private final Map<String, Boolean> zugBearbeitet = new HashMap<String, Boolean>();

	private final Map<String, Integer> zugQsKs = new HashMap<String, Integer>();

	private final Map<String, Boolean> uebZugRichtung = new HashMap<String, Boolean>();

	// ÜB, Baumassnahmen
	private final Map<String, String> uebStreckeVZG = new HashMap<String, String>();

	private final Map<String, String> uebStreckeMassnahme = new HashMap<String, String>();

	private final Map<String, String> uebStreckeBaubeginn = new HashMap<String, String>();

	private final Map<String, String> uebStreckeBauende = new HashMap<String, String>();

	private final Map<String, Boolean> uebStreckeUnterbrochen = new HashMap<String, Boolean>();

	private final Map<String, String> uebStreckeGrund = new HashMap<String, String>();

	private final Map<String, String> uebStreckeBetriebsweise = new HashMap<String, String>();

	private String uebBbpStrecke;

	private String uebFormularkennung;

	private String uebVersionMajor;

	private String uebVersionMinor;

	private String uebVersionSub;

	private String uebBaumassnahmenart;

	private String uebKennung;

	private String uebLisbaKigbau;

	private String uebQsKsVes;

	private String uebKorridor;

	private Boolean uebFestgelegtSPFV;

	private Boolean uebFestgelegtSPNV;

	private Boolean uebFestgelegtSGV;

	// ÜB, Mailversand
	private Integer uebEmpfaengerRB;

	private String[] uebMailEmpfaenger;

	private List<User> uebMailEmpfaengerList = new ArrayList<User>();

	// BBZR
	private Integer zvf; // aktuelle Version

	private List<Uebergabeblatt> zvfAlteVersionen;

	// BBZR, Header
	private String zvfSenderName;

	private String zvfSenderVorname;

	private String zvfSenderStrasse;

	private String zvfSenderPLZ;

	private String zvfSenderOrt;

	private String zvfSenderTelefon;

	private String zvfSenderTelefonIntern;

	private String zvfSenderEmail;

	// BBZR, Baumassnahmen
	private final Map<String, String> zvfStreckeVZG = new HashMap<String, String>();

	private final Map<String, String> zvfStreckeMassnahme = new HashMap<String, String>();

	private final Map<String, String> zvfStreckeBaubeginn = new HashMap<String, String>();

	private final Map<String, String> zvfStreckeBauende = new HashMap<String, String>();

	private final Map<String, Boolean> zvfStreckeUnterbrochen = new HashMap<String, Boolean>();

	private final Map<String, String> zvfStreckeGrund = new HashMap<String, String>();

	private final Map<String, String> zvfStreckeBetriebsweise = new HashMap<String, String>();

	private String zvfBbpStrecke;

	private String zvfFormularkennung;

	private String zvfVersionMajor;

	private String zvfVersionMinor;

	private String zvfVersionSub;

	private String zvfBaumassnahmenart;

	private String zvfKennung;

	private String zvfLisbaKigbau;

	private String zvfQsKsVes;

	private String zvfKorridor;

	private Boolean zvfFestgelegtSPFV;

	private Boolean zvfFestgelegtSPNV;

	private Boolean zvfFestgelegtSGV;

	// zvf, Züge
	private final Map<String, String> zvfZugTageswechsel = new HashMap<String, String>();

	private final Map<String, String> zvfZugVerkehrstag = new HashMap<String, String>();

	private final Map<String, String> zvfZugZuggattung = new HashMap<String, String>();// Zugbez

	private final Map<String, String> zvfZugZugnr = new HashMap<String, String>();

	private final Map<String, Boolean> zvfZugBedarf = new HashMap<String, Boolean>();

	private final Map<String, String> zvfZugAbgangsbhf = new HashMap<String, String>();

	private final Map<String, String> zvfZugZielbhf = new HashMap<String, String>();

	private final Map<String, String> zvfZugUmleitungsstrecke = new HashMap<String, String>();

	private final Map<String, String> zvfZugVerspaetung = new HashMap<String, String>();

	private final Map<String, String> zvfZugAusfallAb = new HashMap<String, String>();

	private final Map<String, String> zvfZugAusfallBis = new HashMap<String, String>();

	private final Map<String, String> zvfZugVorplanAb = new HashMap<String, String>();

	private final Map<String, List<String>> zvfZugAusfallVerkehrshalt = new HashMap<String, List<String>>();

	private final Map<String, List<String>> zvfZugMoeglicherErsatzhalt = new HashMap<String, List<String>>();

	private final Map<String, List<String>> zvfZugRegelungsart = new HashMap<String, List<String>>();

	private final Map<String, List<String>> zvfZugRegelungGiltIn = new HashMap<String, List<String>>();

	private final Map<String, List<String>> zvfZugRegelungText = new HashMap<String, List<String>>();

	private final Map<String, String> zvfZugBemerkung = new HashMap<String, String>();

	private final Map<String, Integer> zvfZugQsKs = new HashMap<String, Integer>();

	private final Map<String, Boolean> zvfZugRichtung = new HashMap<String, Boolean>();

	// showZuege
	private Boolean showZuegeZvf = false;

	private Boolean showZuegeUeb = false;

	private String tab = "";

	private boolean ajax = false;

	public Baumassnahme getBaumassnahme() {
		return baumassnahme;
	}

	public void setBaumassnahme(Baumassnahme baumassnahme) {
		this.baumassnahme = baumassnahme;
	}

	// Abstimmung Nachbarbahn
	private boolean abstimmungNbErforderlich;

	private Integer nachbarbahn;

	private String abstimmungNbErfolgtAm;

	// Terminuebersicht BBP
	private List<String> baubetriebsplanung = new ArrayList<String>();

	private List<Boolean> baubetriebsplanungErforderlich = new ArrayList<Boolean>();

	public void addBaubetriebsplanung(String value) {
		baubetriebsplanung.add(value);
	}

	public void setBaubetriebsplanung(int key, String value) {
		try {
			baubetriebsplanung.set(key, value);
		} catch (IndexOutOfBoundsException e) {
			for (int i = 0; i < 8; i++) {
				baubetriebsplanung.add("");
			}
			baubetriebsplanung.set(key, value);
		}
	}

	public String getBaubetriebsplanung(int key) {
		return baubetriebsplanung.get(key);
	}

	public List<String> getBaubetriebsplanung() {
		return baubetriebsplanung;
	}

	public void setBaubetriebsplanungErforderlich(int index, boolean value) {
		try {
			baubetriebsplanungErforderlich.set(index, value);
		} catch (IndexOutOfBoundsException e) {
			for (int i = 0; i < 8; i++) {
				baubetriebsplanungErforderlich.add(Boolean.FALSE.booleanValue());
			}
			baubetriebsplanungErforderlich.set(index, value);
		}
	}

	public boolean getBaubetriebsplanungErforderlich(int index) {
		Boolean result = null;
		try {
			result = baubetriebsplanungErforderlich.get(index);
		} catch (IndexOutOfBoundsException e) {
		}
		if (result != null) {
			return result.booleanValue();
		}

		return Boolean.FALSE.booleanValue();
	}

	//
	// TODO: Kommentar schreiben
	//

	private List<Bearbeitungsbereich> bearbeitungsbereiche;

	public List<Bearbeitungsbereich> getBearbeitungsbereiche() {
		return bearbeitungsbereiche;
	}

	public void setBearbeitungsbereiche(List<Bearbeitungsbereich> bearbeitungsbereiche) {
		this.bearbeitungsbereiche = bearbeitungsbereiche;
	}

	// Getter und Setter
	public List<Aenderung> getAenderungen() {
		return aenderungen;
	}

	public void setAenderungen(List<Aenderung> aenderungen) {
		this.aenderungen = aenderungen;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getVerzichtQTrasseBeantragt() {
		return verzichtQTrasseBeantragt;
	}

	public void setVerzichtQTrasseBeantragt(String verzichtQTrasseBeantragt) {
		this.verzichtQTrasseBeantragt = verzichtQTrasseBeantragt;
	}

	public String getVerzichtQTrasseAbgestimmt() {
		return verzichtQTrasseAbgestimmt;
	}

	public void setVerzichtQTrasseAbgestimmt(String verzichtQTrasseAbgestimmt) {
		this.verzichtQTrasseAbgestimmt = verzichtQTrasseAbgestimmt;
	}

	public Boolean getVerzichtQTrasseGenehmigt() {
		return verzichtQTrasseGenehmigt;
	}

	public void setVerzichtQTrasseGenehmigt(Boolean verzichtQTrasseGenehmigt) {
		this.verzichtQTrasseGenehmigt = verzichtQTrasseGenehmigt;
	}

	public String getKigBauNr() {
		return kigBauNr;
	}

	public void setKigBauNr(String kigBauNr) {
		this.kigBauNr = kigBauNr;
	}

	public String getKorridorNr() {
		return korridorNr;
	}

	public void setKorridorNr(String korridorNr) {
		this.korridorNr = korridorNr;
	}

	public String getKorridorZeitfenster() {
		return korridorZeitfenster;
	}

	public void setKorridorZeitfenster(String korridorZeitfenster) {
		this.korridorZeitfenster = korridorZeitfenster;
	}

	public String getQsNr() {
		return qsNr;
	}

	public void setQsNr(String qsNr) {
		this.qsNr = qsNr;
	}

	public boolean isQsSPFV() {
		return qsSPFV;
	}

	public void setQsSPFV(boolean qsSPFV) {
		this.qsSPFV = qsSPFV;
	}

	public boolean isQsSGV() {
		return qsSGV;
	}

	public void setQsSGV(boolean qsSGV) {
		this.qsSGV = qsSGV;
	}

	public boolean isQsSPNV() {
		return qsSPNV;
	}

	public void setQsSPNV(boolean qsSPNV) {
		this.qsSPNV = qsSPNV;
	}

	public boolean getArtKs() {
		if (art.equals(Art.KS))
			return true;
		return false;
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

	public String getBeginnDatum() {
		return beginnDatum;
	}

	public void setBeginnDatum(String beginnDatum) {
		this.beginnDatum = beginnDatum;
	}

	public String getEndDatum() {
		return endDatum;
	}

	public void setEndDatum(String endDatum) {
		this.endDatum = endDatum;
	}

	public String getRegionalbereichBM() {
		return regionalbereichBM;
	}

	public void setRegionalbereichBM(String regionalbereichBM) {
		this.regionalbereichBM = regionalbereichBM;
	}

	public Integer getRegionalBereichFpl() {
		return regionalBereichFpl;
	}

	public void setRegionalBereichFpl(Integer regionalBereichFpl) {
		this.regionalBereichFpl = regionalBereichFpl;
	}

	public Integer getBearbeitungsbereich() {
		return bearbeitungsbereich;
	}

	public void setBearbeitungsbereich(Integer bearbeitungsbereich) {
		this.bearbeitungsbereich = bearbeitungsbereich;
	}

	public List<Bearbeitungsbereich> getBearbeitungsbereichList() {
		return bearbeitungsbereichList;
	}

	public void setBearbeitungsbereichList(List<Bearbeitungsbereich> bearbeitungsbereichList) {
		this.bearbeitungsbereichList = bearbeitungsbereichList;
	}

	public String getFploNr() {
		return fploNr;
	}

	public void setFploNr(String fploNr) {
		this.fploNr = fploNr;
	}

	public String getPrioritaet() {
		return prioritaet;
	}

	public void setPrioritaet(String prioritaet) {
		this.prioritaet = prioritaet;
	}

	public Map<String, Boolean> getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(Map<String, Boolean> bearbeiter) {
		this.bearbeiter = bearbeiter;
	}

	public void reset() {
		baubetriebsplanung = new ArrayList<String>();
		baubetriebsplanungErforderlich = new ArrayList<Boolean>();
		ausfallGrund = null;
		aenderungen = new ArrayList<Aenderung>();
		bearbeiter = new HashMap<String, Boolean>();

		// Terminübersichten PV
		resetPevuProperties();

		// Terminübersichten GV
		resetGevuProperties();
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);

		setId(null);
		setArt(null);
		setKigBau(null);
		setStreckeBBP(null);
		setStreckeVZG(null);
		setStreckenAbschnitt(null);
		setArtDerMassnahme(null);
		setBetriebsweise(null);
		setBeginnDatum(null);
		setEndDatum(null);
		setBbpMassnahmen(null);
		setFploNr(null);
		setPrioritaet(null);
		setBetraNr(null);
		setNachbarbahn(null);
		setVorgangsNr(null);
		setBeginnFuerTerminberechnung(null);
		setRegionalbereichBM(null);
		setKigBauNr(null);
		setAntragAufhebungDienstruhe(null);
		setAbstimmungFfz(null);
		setAbstimmungNbErfolgtAm(null);
		setAbstimmungNbErforderlich(false);
		setAenderungen(new ArrayList<Aenderung>());
		setAufwandBiUeb(null);
		setAufwandFplo(null);
		setAufwandUeb(null);
		setAufwandZvF(null);
		setAusfallGrund(null);
		setBaumassnahme(null);
		setBearbeitungsbereich(null);
		setBearbeitungsbereiche(null);
		setBearbeitungsbereichList(null);
		setBenchmarkOnly(false);
		setBisherigerAufwand(null);
		setBiUeNr(null);
		setEskalationsBeginn(null);
		setEskalationsEntscheidung(null);
		setEskalationVeto(false);
		setGenehmigungAufhebungDienstruhe(null);
		setInsertBearbeiter(null);
		setKommentar(null);
		setKonstruktionsEinschraenkung(null);
		setKorridorNr(null);
		setKorridorZeitfenster(null);
		setQsNr(null);
		setQsSGV(false);
		setQsSPFV(false);
		setQsSPNV(false);
		setRegionalBereichFpl(null);
		setVerzichtQTrasseAbgestimmt(null);
		setVerzichtQTrasseBeantragt(null);
		setVerzichtQTrasseGenehmigt(false);

		setUebergabeblatt(null);
		setSenderName(null);
		setSenderVorname(null);
		setSenderStrasse(null);
		setSenderPLZ(null);
		setSenderOrt(null);
		setSenderTelefon(null);
		setSenderTelefonIntern(null);
		setSenderEmail(null);

		setUebBbpStrecke(null);
		setUebFormularkennung(null);
		setUebVersionMajor(null);
		setUebVersionMinor(null);
		setUebVersionSub(null);
		setUebBaumassnahmenart(null);
		setUebKennung(null);
		setUebLisbaKigbau(null);
		setUebQsKsVes(null);
		setUebKorridor(null);
		setUebFestgelegtSPFV(null);
		setUebFestgelegtSPNV(null);
		setUebFestgelegtSGV(null);

		zugAbgangsbhf.clear();
		zugKvProfil.clear();
		zugLaenge.clear();
		zugLast.clear();
		zugMbr.clear();
		zugStreckenklasse.clear();
		zugBemerkung.clear();
		zugTfz.clear();
		zugVerkehrstag.clear();
		zugVmax.clear();
		zugZielbhf.clear();
		zugZuggattung.clear();
		zugZugnr.clear();
		uebStreckeVZG.clear();
		uebStreckeMassnahme.clear();
		uebStreckeBaubeginn.clear();
		uebStreckeBauende.clear();
		uebStreckeUnterbrochen.clear();
		uebStreckeGrund.clear();
		uebStreckeBetriebsweise.clear();
		zugBearbeitet.clear();
		uebMailEmpfaengerList.clear();

		zvfZugTageswechsel.clear();
		zvfZugVerkehrstag.clear();
		zvfZugZuggattung.clear();
		zvfZugZugnr.clear();
		zvfZugBedarf.clear();
		zvfZugAbgangsbhf.clear();
		zvfZugZielbhf.clear();
		zvfZugUmleitungsstrecke.clear();
		zvfZugVerspaetung.clear();
		zvfZugAusfallAb.clear();
		zvfZugAusfallBis.clear();
		zvfZugVorplanAb.clear();
		zvfZugBemerkung.clear();
		zvfZugQsKs.clear();

		uebRb0 = false;
		uebRb1 = false;
		uebRb2 = false;
		uebRb3 = false;
		uebRb4 = false;
		uebRb5 = false;
		uebRb6 = false;

		uebFplo0 = "";
		uebFplo1 = "";
		uebFplo2 = "";
		uebFplo3 = "";
		uebFplo4 = "";
		uebFplo5 = "";
		uebFplo6 = "";

		// Terminübersichten
		baubetriebsplanung = new ArrayList<String>();
		baubetriebsplanungErforderlich = new ArrayList<Boolean>();
		resetGevuProperties();
		resetPevuProperties();

		bearbeiter = new HashMap<String, Boolean>();
		tab = "";
		ajax = false;

		setShowZuegeZvf(false);
		setShowZuegeUeb(false);
	}

	private void resetGevuProperties() {
		setGevuLinkedIds("");
		gevuZvfEntwurf.clear();
		gevuIsZvFEntwurfErforderlich.clear();
		gevuStellungnahmeEVU.clear();
		gevuIsStellungnahmeEVUErforderlich.clear();
		gevuZvF.clear();
		gevuIsZvFErforderlich.clear();
		gevuStudieGrobkonzept.clear();
		gevuIsMasterUebergabeblattGVErforderlich.clear();
		gevuMasterUebergabeblattGV.clear();
		gevuIsUebergabeblattGVErforderlich.clear();
		gevuUebergabeblattGV.clear();
		gevuFplo.clear();
		gevuIsFploErforderlich.clear();
		gevuEingabeGFD_Z.clear();
		gevuIsEingabeGFD_ZErforderlich.clear();
	}

	private void resetPevuProperties() {
		setPevuLinkedIds("");
		pevuStudieGrobkonzept.clear();
		pevuZvfEntwurf.clear();
		pevuIsZvfEntwurfErforderlich.clear();
		pevuStellungnahmeEVU.clear();
		pevuIsStellungnahmeEVUErforderlich.clear();
		pevuZvF.clear();
		pevuIsZvFErforderlich.clear();
		pevuIsMasterUebergabeblattPVErforderlich.clear();
		pevuMasterUebergabeblattPV.clear();
		pevuIsUebergabeblattPVErforderlich.clear();
		pevuUebergabeblattPV.clear();
		pevuFplo.clear();
		pevuIsFploErforderlich.clear();
		pevuBKonzeptEVU.clear();
		pevuIsBKonzeptEVUErforderlich.clear();
		pevuEingabeGFD_Z.clear();
		pevuIsEingabeGFD_ZErforderlich.clear();
		pevuAusfaelleSEV.clear();
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);
		if (getBenchmarkOnly() == null || getBenchmarkOnly() != null && !getBenchmarkOnly()) {
			// if (getBenchmarkOnly() != null && !getBenchmarkOnly()) {
			// TODO benchmarkOnly == null im normalen Edit-Mode (nicht anonymous) verhindert Prüfung
			// Stammdaten
			if (beginnDatum != null && beginnDatum.length() > 0) {
				Date date = FrontendHelper.castStringToDate(beginnDatum);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Zeitraum von"));
			} else
				actionErrors.add("error.required", new ActionMessage("error.required",
				    "Zeitraum von"));

			if (endDatum != null && endDatum.length() > 0) {
				Date date = FrontendHelper.castStringToDate(endDatum);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Zeitraum bis"));
			} else
				actionErrors.add("error.required", new ActionMessage("error.required",
				    "Zeitraum bis"));

			if (beginnFuerTerminberechnung != null && beginnFuerTerminberechnung.length() > 0) {
				Date date = FrontendHelper.castStringToDate(beginnFuerTerminberechnung);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid", "Beginn"));
			} else
				actionErrors.add("error.required", new ActionMessage("error.required", "Beginn"));

			if (regionalbereichBM == null || regionalbereichBM.equals(""))
				actionErrors.add("error.required", new ActionMessage("error.required",
				    "RegionalbereichBM"));

			if (regionalBereichFpl == null || regionalBereichFpl == 0)
				actionErrors.add("error.required", new ActionMessage("error.required",
				    "RegionalbereichFpl"));

			// if (bearbeitungsbereich == null || bearbeitungsbereich == 0)
			// actionErrors.add("error.required",
			// new ActionMessage("error.required","Bearbeitungsbereich"));

			if (streckeBBP == null || streckeBBP.equals(""))
				actionErrors.add("error.required", new ActionMessage("error.required",
				    "Strecke BBP"));

			if (streckeVZG == null || streckeVZG.equals(""))
				actionErrors.add("error.required", new ActionMessage("error.required",
				    "Strecke VZG"));

			if (streckenAbschnitt == null || streckenAbschnitt.equals(""))
				actionErrors.add("error.required", new ActionMessage("error.required",
				    "Streckenabschnitt"));

			if (artDerMassnahme == null || artDerMassnahme.equals(""))
				actionErrors.add("error.required", new ActionMessage("error.required", "Arbeiten"));

			if (prioritaet == null)
				actionErrors.add("error.required",
				    new ActionMessage("error.required", "Prioritaet"));

			if (kigBau == null)
				actionErrors.add("error.required", new ActionMessage("error.required", "KigBau"));

			// Kommentar
			if (kommentar != null && kommentar.length() > 2000)
				kommentar = kommentar.substring(0, 1999);

			// JBB
			if (kigBauNr != null && !kigBauNr.equals("")) {
				Integer maxAnzahlKigbau = null;
				Integer maxLaengeKigbau = null;
				try {
					maxAnzahlKigbau = ConfigResources.getInstance().getMaxAnzahlKigbaunr();
					maxLaengeKigbau = ConfigResources.getInstance().getMaxLaengeKigbaunr();

					Scanner scanner = new Scanner(kigBauNr);
					scanner.useDelimiter(",");
					int counter = 0;
					while (scanner.hasNext()) {
						counter++;
						if (counter > (maxAnzahlKigbau != null ? maxAnzahlKigbau : 15))
							throw new Exception();
						if (scanner.next().trim().length() > (maxLaengeKigbau != null ? maxLaengeKigbau
						    : 10))
							throw new NumberFormatException();
					}
				} catch (NumberFormatException n) {
					actionErrors.add("error.kigbaunr.format", new ActionMessage(
					    "error.kigbaunr.format", "KigBauNr"));

				} catch (Exception e) {
					actionErrors.add("error.kigbaunr.arg", new ActionMessage("error.kigbaunr.arg",
					    maxAnzahlKigbau));
				}
			}

			if (korridorZeitfenster != null && !korridorZeitfenster.equals("")) {
				try {
					Scanner scanner = new Scanner(korridorZeitfenster);
					scanner.useDelimiter(",");
					int counter = 0;
					while (scanner.hasNext()) {
						counter++;
						if (counter > 5)
							throw new IllegalArgumentException();
						if (scanner.next().trim().length() > 10)
							throw new NumberFormatException();
					}
				} catch (NumberFormatException n) {
					actionErrors.add("error.korridorzf.format", new ActionMessage(
					    "error.korridorzf.format", "Korridor-Zf"));
				} catch (IllegalArgumentException i) {
					actionErrors.add("error.korridorzf.arg", new ActionMessage(
					    "error.korridorzf.arg", "Korridor-Zf"));
				}
			}

			// Status Aktivitaeten
			if (konstruktionsEinschraenkung != null && konstruktionsEinschraenkung.length() > 0) {
				Date date = FrontendHelper.castStringToDate(konstruktionsEinschraenkung);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Konstruktionseinschränkung"));
			}
			if (abstimmungFfz != null && abstimmungFfz.length() > 0) {
				Date date = FrontendHelper.castStringToDate(abstimmungFfz);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Abstimmung Ffz"));
			}
			if (antragAufhebungDienstruhe != null && antragAufhebungDienstruhe.length() > 0) {
				Date date = FrontendHelper.castStringToDate(antragAufhebungDienstruhe);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Antrag Aufhebung Dienstruhe"));
			}
			if (genehmigungAufhebungDienstruhe != null
			    && genehmigungAufhebungDienstruhe.length() > 0) {
				Date date = FrontendHelper.castStringToDate(genehmigungAufhebungDienstruhe);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Genehmigung Aufhebung Dienstruhe"));
			}

			// Eskalation/Ausfall
			if (eskalationsBeginn != null && eskalationsBeginn.length() > 0) {
				Date date = FrontendHelper.castStringToDate(eskalationsBeginn);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Eskalation Beginn"));
			}
			if (eskalationsEntscheidung != null && eskalationsEntscheidung.length() > 0) {
				Date date = FrontendHelper.castStringToDate(eskalationsEntscheidung);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Eskalation Entscheidung"));
			}
			if (ausfallDatum != null && ausfallDatum.length() > 0) {
				Date date = FrontendHelper.castStringToDate(ausfallDatum);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Ausfalldatum"));
			}
			if (bisherigerAufwand != null && bisherigerAufwand.length() > 0) {
				Integer i = FrontendHelper.castStringToInteger(bisherigerAufwand
				    .replaceAll(":", ""));
				if (i == null)
					actionErrors.add("error.decimalFormat", new ActionMessage(
					    "error.decimalFormat", "Aufwand"));
				else if (i < 0)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Ausfall Aufwand"));
			}

			// Aufwand
			if (aufwandZvF != null && aufwandZvF.length() > 0) {
				Integer i = FrontendHelper.castStringToInteger(aufwandZvF);
				if (i == null)
					actionErrors.add("error.decimalFormat", new ActionMessage(
					    "error.decimalFormat", "Aufwand ZvF"));
				else if (i < 0)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Aufwand ZvF"));
			}
			if (aufwandBiUeb != null && aufwandBiUeb.length() > 0) {
				Integer i = FrontendHelper.castStringToInteger(aufwandBiUeb);
				if (i == null)
					actionErrors.add("error.decimalFormat", new ActionMessage(
					    "error.decimalFormat", "Aufwand BiÜb"));
				else if (i < 0)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Aufwand BiÜb"));
			}
			if ((art != null && !Art.A.name().equals(art))
			    & (aufwandFplo != null && aufwandFplo.length() > 0)) {
				Integer i = FrontendHelper.castStringToInteger(aufwandFplo);
				if (i == null)
					actionErrors.add("error.decimalFormat", new ActionMessage(
					    "error.decimalFormat", "Aufwand Fplo"));
				else if (i < 0)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Aufwand Fplo"));
			}
			if ((art != null && Art.QS.name().equals(art))
			    & (aufwandUeb != null && aufwandUeb.length() > 0)) {
				Integer i = FrontendHelper.castStringToInteger(aufwandUeb);
				if (i == null)
					actionErrors.add("error.decimalFormat", new ActionMessage(
					    "error.decimalFormat", "Aufwand Üb"));
				else if (i < 0)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Aufwand Üb"));
			}

			// Verzicht Q-Trasse
			if (verzichtQTrasseBeantragt != null && verzichtQTrasseBeantragt.length() > 0) {
				Date date = FrontendHelper.castStringToDate(verzichtQTrasseBeantragt);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Verzicht Q-Trasse beantragt"));
			}
			if (verzichtQTrasseAbgestimmt != null && verzichtQTrasseAbgestimmt.length() > 0) {
				Date date = FrontendHelper.castStringToDate(verzichtQTrasseAbgestimmt);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Verzicht Q-Trasse abgestimmt"));
			}

			// Abstimmung Nachbarbahn
			if (abstimmungNbErfolgtAm != null && abstimmungNbErfolgtAm.length() > 0) {
				Date date = FrontendHelper.castStringToDate(abstimmungNbErfolgtAm);
				if (date == null)
					actionErrors.add("error.invalid", new ActionMessage("error.invalid",
					    "Abstimmung Nachbarbahn erfolgt am"));
			}

			// TODO Zahlen testen

			// Termine BBP
			ActionMessage message = testDateFormat(baubetriebsplanung,
			    "Schnittstelle Baubetriebsplanung");
			if (message != null)
				actionErrors.add("error.invalid", message);

			// Termine Personenverkehrs-EVU
			message = testDateFormat(pevuStudieGrobkonzept.values(),
			    "Schnittstelle Personenverkehrs-EVU, Studie/Grobkonzept");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuZvfEntwurf.values(),
			    "Schnittstelle Personenverkehrs-EVU, ZvF-Entwurf");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuStellungnahmeEVU.values(),
			    "Schnittstelle Personenverkehrs-EVU, Stellungnahme EVU");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuZvF.values(), "Schnittstelle Personenverkehrs-EVU, ZvF");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuMasterUebergabeblattPV.values(),
			    "Schnittstelle Personenverkehrs-EVU, Master-Übergabeblatt");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuUebergabeblattPV.values(),
			    "Schnittstelle Personenverkehrs-EVU, Übergabeblatt");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuFplo.values(), "Schnittstelle Personenverkehrs-EVU, Fplo");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuEingabeGFD_Z.values(),
			    "Schnittstelle Personenverkehrs-EVU, Eingabe GFD-Z");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(pevuBKonzeptEVU.values(),
			    "Schnittstelle Personenverkehrs-EVU, B-Konzept EVU");
			if (message != null)
				actionErrors.add("error.invalid", message);

			// Termine Güterverkehrs-EVU
			message = testDateFormat(gevuStudieGrobkonzept.values(),
			    "Schnittstelle Güterverkehrs-EVU, Studie/Grobkonzept");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(gevuZvfEntwurf.values(),
			    "Schnittstelle Güterverkehrs-EVU, ZvF-Entwurf");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(gevuStellungnahmeEVU.values(),
			    "Schnittstelle Güterverkehrs-EVU, Stellungnahme EVU");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(gevuZvF.values(), "Schnittstelle Güterverkehrs-EVU, ZvF");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(gevuMasterUebergabeblattGV.values(),
			    "Schnittstelle Güterverkehrs-EVU, Master-Übergabeblatt");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(gevuUebergabeblattGV.values(),
			    "Schnittstelle Güterverkehrs-EVU, Übergabeblatt");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(gevuFplo.values(), "Schnittstelle Güterverkehrs-EVU, Fplo");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(gevuEingabeGFD_Z.values(),
			    "Schnittstelle Güterverkehrs-EVU, Eingabe GFD-Z");
			if (message != null)
				actionErrors.add("error.invalid", message);

			// Übergabeblatt
			// uebFplo;
			List<String> uebfploNrn = new ArrayList<String>();
			uebfploNrn.add(uebFplo0);
			uebfploNrn.add(uebFplo1);
			uebfploNrn.add(uebFplo2);
			uebfploNrn.add(uebFplo3);
			uebfploNrn.add(uebFplo4);
			uebfploNrn.add(uebFplo5);
			uebfploNrn.add(uebFplo6);
			for (String fplo : uebfploNrn) {
				if (fplo != null && fplo.length() > 0) {
					Integer i = FrontendHelper.castStringToInteger(fplo);
					if (i == null)
						actionErrors.add("error.decimalFormat", new ActionMessage(
						    "error.decimalFormat", "Fplonr"));
					else if (i < 0)
						actionErrors.add("error.invalid", new ActionMessage("error.invalid",
						    "Fplonr"));
				}
			}

			message = testDateFormat(uebStreckeBaubeginn.values(), "Übergabeblatt Baubeginn");
			if (message != null)
				actionErrors.add("error.invalid", message);
			message = testDateFormat(uebStreckeBauende.values(), "Übergabeblatt Bauende");
			if (message != null)
				actionErrors.add("error.invalid", message);

		} else if (getBenchmarkOnly() != null && getBenchmarkOnly() == true) {
			// die Validierung in Validation.xml kann Fehler werfen, die aber nur für das
			// vollständige Baumassnahme Formular relevant sind.
			actionErrors.clear();
		}

		// BBZR
		// private final Map<String, String> zvfZugVerspaetung = new HashMap<String, String>();
		ActionMessage message = testDateFormat(zvfStreckeBaubeginn.values(),
		    "BBZR, Baubeginn Strecke");
		if (message != null)
			actionErrors.add("error.invalid", message);

		message = testDateFormat(zvfStreckeBauende.values(), "BBZR, Bauende Strecke");
		if (message != null)
			actionErrors.add("error.invalid", message);

		message = testDateFormat(zvfZugVerkehrstag.values(), "BBZR, Zug Verkehrstag");
		if (message != null)
			actionErrors.add("error.invalid", message);

		if (zvfVersionMajor != null)
			if (FrontendHelper.castStringToInteger(zvfVersionMajor) == null)
				actionErrors.add("error.decimalFormat", new ActionMessage("error.decimalFormat",
				    "BBZR, Version"));

		if (zvfVersionMinor != null)
			if (FrontendHelper.castStringToInteger(zvfVersionMinor) == null)
				actionErrors.add("error.decimalFormat", new ActionMessage("error.decimalFormat",
				    "BBZR, Version"));

		if (zvfVersionSub != null) {
			if (FrontendHelper.castStringToInteger(zvfVersionSub) == null)
				actionErrors.add("error.decimalFormat", new ActionMessage("error.decimalFormat",
				    "BBZR, Version"));

			if (zvfVersionSub != null && zvfVersionSub.length() < 2)
				zvfVersionSub = "0" + zvfVersionSub;
		}

		for (String tageswechsel : zvfZugTageswechsel.values()) {
			if (!tageswechsel.equals("-1") & !tageswechsel.equals("+1") & !tageswechsel.equals("")) {
				actionErrors.add("error.invalid", new ActionMessage("error.invalid",
				    "BBZR, Zug Tageswechsel"));
				break;
			}
		}

		for (String zugnr : zvfZugZugnr.values()) {
			Integer zugnrInt = FrontendHelper.castStringToInteger(zugnr);
			if (zugnrInt == null || zugnrInt < 1 || zugnrInt > 99999) {
				actionErrors.add("error.decimalFormat", new ActionMessage("error.decimalFormat",
				    "BBZR, Zug Zugnr"));
				break;
			}
		}

		for (String verspaetung : zvfZugVerspaetung.values()) {
			if (verspaetung.length() > 0) {
				Integer verspaetungInt = FrontendHelper.castStringToInteger(verspaetung);
				if (verspaetungInt == null || verspaetungInt < 0 || verspaetungInt > 9999) {
					actionErrors.add("error.decimalFormat", new ActionMessage(
					    "error.decimalFormat", "BBZR, Zug Verspätung / Zeit vor Plan"));
					break;
				}
			}
		}

		// Arbeitsleistung Regionalbereiche
		// Iterator<String> it = arbUebergabeUeb.values().iterator();
		// while (it.hasNext()) {
		// String date = it.next();
		// if (date.equals(""))
		// continue;
		// if (FrontendHelper.castStringToDate(date) == null) {
		// actionErrors.add("error.invalid", new ActionMessage("error.invalid",
		// "Arbeitsleistung Regionalbereiche, Übergabe ÜB"));
		// break;
		// }
		// }

		return actionErrors;
	}

	private ActionMessage testDateFormat(Collection<String> dates, String messagePart) {
		boolean hasErrors = false;

		Iterator<String> it = dates.iterator();
		while (it.hasNext() && !hasErrors) {
			String date = it.next();
			if (date != null & !date.equals("")) {
				if (FrontendHelper.castStringToDate(date) == null) {
					return new ActionMessage("error.invalid", messagePart);
				}
			}
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getKigBau() {
		return kigBau;
	}

	public void setKigBau(Boolean kigBau) {
		this.kigBau = kigBau;
	}

	public String getStreckenAbschnitt() {
		return streckenAbschnitt;
	}

	public void setStreckenAbschnitt(String streckenAbschnitt) {
		this.streckenAbschnitt = streckenAbschnitt;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public Set<Integer> getBbpMassnahmen() {
		return bbpMassnahmen;
	}

	public void setBbpMassnahmen(Set<Integer> bbpMassnahmen) {
		this.bbpMassnahmen = bbpMassnahmen;
	}

	public String getKonstruktionsEinschraenkung() {
		return konstruktionsEinschraenkung;
	}

	public void setKonstruktionsEinschraenkung(String konstruktionsEinschraenkung) {
		this.konstruktionsEinschraenkung = konstruktionsEinschraenkung;
	}

	public String getAbstimmungFfz() {
		return abstimmungFfz;
	}

	public void setAbstimmungFfz(String abstimmungFfz) {
		this.abstimmungFfz = abstimmungFfz;
	}

	public String getAntragAufhebungDienstruhe() {
		return antragAufhebungDienstruhe;
	}

	public void setAntragAufhebungDienstruhe(String antragAufhebungDienstruhe) {
		this.antragAufhebungDienstruhe = antragAufhebungDienstruhe;
	}

	public String getGenehmigungAufhebungDienstruhe() {
		return genehmigungAufhebungDienstruhe;
	}

	public void setGenehmigungAufhebungDienstruhe(String genehmigungAufhebungDienstruhe) {
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

	public String getEskalationsBeginn() {
		return eskalationsBeginn;
	}

	public void setEskalationsBeginn(String eskalationsBeginn) {
		this.eskalationsBeginn = eskalationsBeginn;
	}

	public String getEskalationsEntscheidung() {
		return eskalationsEntscheidung;
	}

	public void setEskalationsEntscheidung(String eskalationsEntscheidung) {
		this.eskalationsEntscheidung = eskalationsEntscheidung;
	}

	public boolean isEskalationVeto() {
		return eskalationVeto;
	}

	public void setEskalationVeto(boolean eskalationVeto) {
		this.eskalationVeto = eskalationVeto;
	}

	public String getAusfallDatum() {
		return ausfallDatum;
	}

	public void setAusfallDatum(String ausfallDatum) {
		this.ausfallDatum = ausfallDatum;
	}

	public Integer getAusfallGrund() {
		return ausfallGrund;
	}

	public void setAusfallGrund(Integer ausfallGrund) {
		this.ausfallGrund = ausfallGrund;
	}

	public String getBisherigerAufwand() {
		return bisherigerAufwand;
	}

	public void setBisherigerAufwand(String bisherigerAufwand) {
		this.bisherigerAufwand = bisherigerAufwand;
	}

	public String getAufwandZvF() {
		return aufwandZvF;
	}

	public void setAufwandZvF(String aufwandZvF) {
		this.aufwandZvF = aufwandZvF;
	}

	public String getAufwandBiUeb() {
		return aufwandBiUeb;
	}

	public void setAufwandBiUeb(String aufwandBiUeb) {
		this.aufwandBiUeb = aufwandBiUeb;
	}

	public String getAufwandUeb() {
		return aufwandUeb;
	}

	public void setAufwandUeb(String aufwandUeb) {
		this.aufwandUeb = aufwandUeb;
	}

	public String getAufwandFplo() {
		return aufwandFplo;
	}

	public void setAufwandFplo(String aufwandFplo) {
		this.aufwandFplo = aufwandFplo;
	}

	public boolean isAbstimmungNbErforderlich() {
		return abstimmungNbErforderlich;
	}

	public void setAbstimmungNbErforderlich(boolean abstimmungNbErforderlich) {
		this.abstimmungNbErforderlich = abstimmungNbErforderlich;
	}

	public Integer getNachbarbahn() {
		return nachbarbahn;
	}

	public void setNachbarbahn(Integer nachbarbahn) {
		this.nachbarbahn = nachbarbahn;
	}

	public String getAbstimmungNbErfolgtAm() {
		return abstimmungNbErfolgtAm;
	}

	public void setAbstimmungNbErfolgtAm(String abstimmungNbErfolgtAm) {
		this.abstimmungNbErfolgtAm = abstimmungNbErfolgtAm;
	}

	public String getStreckeBBP() {
		return streckeBBP;
	}

	public void setStreckeBBP(String streckeBBP) {
		this.streckeBBP = streckeBBP;
	}

	public String getStreckeVZG() {
		return streckeVZG;
	}

	public void setStreckeVZG(String streckeVZG) {
		this.streckeVZG = streckeVZG;
	}

	public Integer getVorgangsNr() {
		return vorgangsNr;
	}

	public void setVorgangsNr(Integer vorgangsNr) {
		this.vorgangsNr = vorgangsNr;
	}

	public String getBeginnFuerTerminberechnung() {
		return beginnFuerTerminberechnung;
	}

	public void setBeginnFuerTerminberechnung(String beginnFuerTerminberechnung) {
		this.beginnFuerTerminberechnung = beginnFuerTerminberechnung;
	}

	/**
	 * @return the uebergabeblatt
	 */
	public Integer getUebergabeblatt() {
		return uebergabeblatt;
	}

	/**
	 * @param uebergabeblatt
	 *            the uebergabeblatt to set
	 */
	public void setUebergabeblatt(Integer uebergabeblatt) {
		this.uebergabeblatt = uebergabeblatt;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderVorname() {
		return senderVorname;
	}

	public void setSenderVorname(String senderVorname) {
		this.senderVorname = senderVorname;
	}

	public String getSenderStrasse() {
		return senderStrasse;
	}

	public void setSenderStrasse(String senderStrasse) {
		this.senderStrasse = senderStrasse;
	}

	public String getSenderPLZ() {
		return senderPLZ;
	}

	public void setSenderPLZ(String senderPLZ) {
		this.senderPLZ = senderPLZ;
	}

	public String getSenderOrt() {
		return senderOrt;
	}

	public void setSenderOrt(String senderOrt) {
		this.senderOrt = senderOrt;
	}

	public String getSenderTelefon() {
		return senderTelefon;
	}

	public void setSenderTelefon(String senderTelefon) {
		this.senderTelefon = senderTelefon;
	}

	public String getSenderTelefonIntern() {
		return senderTelefonIntern;
	}

	public void setSenderTelefonIntern(String senderTelefonIntern) {
		this.senderTelefonIntern = senderTelefonIntern;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getZugVerkehrstag(String i) {
		return zugVerkehrstag.get(i);
	}

	public void setZugVerkehrstag(String i, String verkehrstag) {
		this.zugVerkehrstag.put(i, verkehrstag);
	}

	public String getZugZuggattung(String i) {
		return zugZuggattung.get(i);
	}

	public void setZugZuggattung(String i, String zuggattung) {
		this.zugZuggattung.put(i, zuggattung);
	}

	public String getZugZugnr(String i) {
		return zugZugnr.get(i);
	}

	public void setZugZugnr(String i, String zugnr) {
		this.zugZugnr.put(i, zugnr);
	}

	public String getZugAbgangsbhf(String i) {
		return zugAbgangsbhf.get(i);
	}

	public void setZugAbgangsbhf(String i, String abgangsbhf) {
		this.zugAbgangsbhf.put(i, abgangsbhf);
	}

	public String getZugZielbhf(String i) {
		return zugZielbhf.get(i);
	}

	public void setZugZielbhf(String i, String zielbhf) {
		this.zugZielbhf.put(i, zielbhf);
	}

	public String getZugTfz(String i) {
		return zugTfz.get(i);
	}

	public void setZugTfz(String i, String zugTfz) {
		this.zugTfz.put(i, zugTfz);
	}

	public String getZugLast(String i) {
		return zugLast.get(i);
	}

	public void setZugLast(String i, String zugLast) {
		this.zugLast.put(i, zugLast);
	}

	public String getZugMbr(String i) {
		return zugMbr.get(i);
	}

	public void setZugMbr(String i, String zugMbr) {
		this.zugMbr.put(i, zugMbr);
	}

	public String getZugLaenge(String i) {
		return zugLaenge.get(i);
	}

	public void setZugLaenge(String i, String zugLaenge) {
		this.zugLaenge.put(i, zugLaenge);
	}

	public String getZugVmax(String i) {
		return zugVmax.get(i);
	}

	public void setZugVmax(String i, String zugVmax) {
		this.zugVmax.put(i, zugVmax);
	}

	public String getZugKvProfil(String i) {
		return zugKvProfil.get(i);
	}

	public void setZugKvProfil(String i, String zugKvProfil) {
		this.zugKvProfil.put(i, zugKvProfil);
	}

	public String getZugStreckenklasse(String i) {
		return zugStreckenklasse.get(i);
	}

	public void setZugStreckenklasse(String i, String zugStreckenklasse) {
		this.zugStreckenklasse.put(i, zugStreckenklasse);
	}

	public String getZugBemerkung(String i) {
		return zugBemerkung.get(i);
	}

	public void setZugBemerkung(String i, String zugBemerkung) {
		this.zugBemerkung.put(i, zugBemerkung);
	}

	public String getUebBbpStrecke() {
		return uebBbpStrecke;
	}

	public void setUebBbpStrecke(String uebBbpStrecke) {
		this.uebBbpStrecke = uebBbpStrecke;
	}

	public String getUebFormularkennung() {
		return uebFormularkennung;
	}

	public void setUebFormularkennung(String uebFormularkennung) {
		this.uebFormularkennung = uebFormularkennung;
	}

	public String getUebVersionMajor() {
		return uebVersionMajor;
	}

	public void setUebVersionMajor(String uebVersionMajor) {
		this.uebVersionMajor = uebVersionMajor;
	}

	public String getUebVersionMinor() {
		return uebVersionMinor;
	}

	public void setUebVersionMinor(String uebVersionMinor) {
		this.uebVersionMinor = uebVersionMinor;
	}

	public String getUebVersionSub() {
		return uebVersionSub;
	}

	public void setUebVersionSub(String uebVersionSub) {
		this.uebVersionSub = uebVersionSub;
	}

	public String getUebBaumassnahmenart() {
		return uebBaumassnahmenart;
	}

	public void setUebBaumassnahmenart(String uebBaumassnahmenart) {
		this.uebBaumassnahmenart = uebBaumassnahmenart;
	}

	public String getUebKennung() {
		return uebKennung;
	}

	public void setUebKennung(String uebKennung) {
		this.uebKennung = uebKennung;
	}

	public String getUebLisbaKigbau() {
		return uebLisbaKigbau;
	}

	public void setUebLisbaKigbau(String uebLisbaKigbau) {
		this.uebLisbaKigbau = uebLisbaKigbau;
	}

	public String getUebQsKsVes() {
		return uebQsKsVes;
	}

	public void setUebQsKsVes(String uebQsKsVes) {
		this.uebQsKsVes = uebQsKsVes;
	}

	public String getUebKorridor() {
		return uebKorridor;
	}

	public void setUebKorridor(String uebKorridor) {
		this.uebKorridor = uebKorridor;
	}

	public Boolean getUebFestgelegtSPFV() {
		return uebFestgelegtSPFV;
	}

	public void setUebFestgelegtSPFV(Boolean uebFestgelegtSPFV) {
		this.uebFestgelegtSPFV = uebFestgelegtSPFV;
	}

	public Boolean getUebFestgelegtSPNV() {
		return uebFestgelegtSPNV;
	}

	public void setUebFestgelegtSPNV(Boolean uebFestgelegtSPNV) {
		this.uebFestgelegtSPNV = uebFestgelegtSPNV;
	}

	public Boolean getUebFestgelegtSGV() {
		return uebFestgelegtSGV;
	}

	public void setUebFestgelegtSGV(Boolean uebFestgelegtSGV) {
		this.uebFestgelegtSGV = uebFestgelegtSGV;
	}

	public String getUebStreckeVZG(String i) {
		return uebStreckeVZG.get(i);
	}

	public void setUebStreckeVZG(String i, String uebStreckeVZG) {
		this.uebStreckeVZG.put(i, uebStreckeVZG);
	}

	public String getUebStreckeMassnahme(String i) {
		return uebStreckeMassnahme.get(i);
	}

	public void setUebStreckeMassnahme(String i, String uebStreckeMassnahme) {
		this.uebStreckeMassnahme.put(i, uebStreckeMassnahme);
	}

	public String getUebStreckeBaubeginn(String i) {
		return uebStreckeBaubeginn.get(i);
	}

	public void setUebStreckeBaubeginn(String i, String uebStreckeBaubeginn) {
		this.uebStreckeBaubeginn.put(i, uebStreckeBaubeginn);
	}

	public String getUebStreckeBauende(String i) {
		return uebStreckeBauende.get(i);
	}

	public void setUebStreckeBauende(String i, String uebStreckeBauende) {
		this.uebStreckeBauende.put(i, uebStreckeBauende);
	}

	public Boolean getUebStreckeUnterbrochen(String i) {
		return uebStreckeUnterbrochen.get(i);
	}

	public void setUebStreckeUnterbrochen(String i, Boolean uebStreckeUnterbrochen) {
		this.uebStreckeUnterbrochen.put(i, uebStreckeUnterbrochen);
	}

	public Boolean getUebZugRichtung(String i) {
		return uebZugRichtung.get(i);
	}

	public void setUebZugRichtung(String i, Boolean uebZugRichtung) {
		this.uebZugRichtung.put(i, uebZugRichtung);
	}

	public Boolean getZugBearbeitet(String i) {
		return zugBearbeitet.get(i);
	}

	public void setZugBearbeitet(String i, Boolean zugBearbeitet) {
		this.zugBearbeitet.put(i, zugBearbeitet);
	}

	public Integer getZugQsKs(String i) {
		return zugQsKs.get(i);
	}

	public void setZugQsKs(String i, Integer qs_ks) {
		this.zugQsKs.put(i, qs_ks);
	}

	public String getUebStreckeGrund(String i) {
		return uebStreckeGrund.get(i);
	}

	public void setUebStreckeGrund(String i, String uebStreckeGrund) {
		this.uebStreckeGrund.put(i, uebStreckeGrund);
	}

	public String getUebStreckeBetriebsweise(String i) {
		return uebStreckeBetriebsweise.get(i);
	}

	public void setUebStreckeBetriebsweise(String i, String uebStreckeBetriebsweise) {
		this.uebStreckeBetriebsweise.put(i, uebStreckeBetriebsweise);
	}

	public List<User> getUebMailEmpfaengerList() {
		return uebMailEmpfaengerList;
	}

	public void setUebMailEmpfaengerList(List<User> user) {
		this.uebMailEmpfaengerList = user;
	}

	public void setUebMailEmpfaenger(String[] mailEmpfaenger) {
		this.uebMailEmpfaenger = mailEmpfaenger;
	}

	public String[] getUebMailEmpfaenger() {
		return uebMailEmpfaenger;
	}

	public Boolean getUebRb1() {
		return uebRb1;
	}

	public void setUebRb1(Boolean uebRb1) {
		this.uebRb1 = uebRb1;
	}

	public Boolean getUebRb2() {
		return uebRb2;
	}

	public void setUebRb2(Boolean uebRb2) {
		this.uebRb2 = uebRb2;
	}

	public Boolean getUebRb3() {
		return uebRb3;
	}

	public void setUebRb3(Boolean uebRb3) {
		this.uebRb3 = uebRb3;
	}

	public Boolean getUebRb4() {
		return uebRb4;
	}

	public void setUebRb4(Boolean uebRb4) {
		this.uebRb4 = uebRb4;
	}

	public Boolean getUebRb5() {
		return uebRb5;
	}

	public void setUebRb5(Boolean uebRb5) {
		this.uebRb5 = uebRb5;
	}

	public Boolean getUebRb6() {
		return uebRb6;
	}

	public void setUebRb6(Boolean uebRb6) {
		this.uebRb6 = uebRb6;
	}

	public Boolean getUebRb0() {
		return uebRb0;
	}

	public void setUebRb0(Boolean uebRb0) {
		this.uebRb0 = uebRb0;
	}

	public String getUebFplo1() {
		return uebFplo1;
	}

	public void setUebFplo1(String uebFplo1) {
		this.uebFplo1 = uebFplo1;
	}

	public String getUebFplo2() {
		return uebFplo2;
	}

	public void setUebFplo2(String uebFplo2) {
		this.uebFplo2 = uebFplo2;
	}

	public String getUebFplo3() {
		return uebFplo3;
	}

	public void setUebFplo3(String uebFplo3) {
		this.uebFplo3 = uebFplo3;
	}

	public String getUebFplo4() {
		return uebFplo4;
	}

	public void setUebFplo4(String uebFplo4) {
		this.uebFplo4 = uebFplo4;
	}

	public String getUebFplo5() {
		return uebFplo5;
	}

	public void setUebFplo5(String uebFplo5) {
		this.uebFplo5 = uebFplo5;
	}

	public String getUebFplo6() {
		return uebFplo6;
	}

	public void setUebFplo6(String uebFplo6) {
		this.uebFplo6 = uebFplo6;
	}

	public String getUebFplo0() {
		return uebFplo0;
	}

	public void setUebFplo0(String uebFplo0) {
		this.uebFplo0 = uebFplo0;
	}

	public Integer getUebEmpfaengerRB() {
		return uebEmpfaengerRB;
	}

	public void setUebEmpfaengerRB(Integer uebEmpfaengerRB) {
		this.uebEmpfaengerRB = uebEmpfaengerRB;
	}

	public Boolean getBenchmarkOnly() {
		return benchmarkOnly;
	}

	public void setBenchmarkOnly(Boolean benchmarkOnly) {
		this.benchmarkOnly = benchmarkOnly;
	}

	public Integer getZvf() {
		return zvf;
	}

	public void setZvf(Integer zvfId) {
		this.zvf = zvfId;
	}

	public String getZvfSenderName() {
		return zvfSenderName;
	}

	public void setZvfSenderName(String zvfSenderName) {
		this.zvfSenderName = zvfSenderName;
	}

	public String getZvfSenderVorname() {
		return zvfSenderVorname;
	}

	public void setZvfSenderVorname(String zvfSenderVorname) {
		this.zvfSenderVorname = zvfSenderVorname;
	}

	public String getZvfSenderStrasse() {
		return zvfSenderStrasse;
	}

	public void setZvfSenderStrasse(String zvfSenderStrasse) {
		this.zvfSenderStrasse = zvfSenderStrasse;
	}

	public String getZvfSenderPLZ() {
		return zvfSenderPLZ;
	}

	public void setZvfSenderPLZ(String zvfSenderPLZ) {
		this.zvfSenderPLZ = zvfSenderPLZ;
	}

	public String getZvfSenderOrt() {
		return zvfSenderOrt;
	}

	public void setZvfSenderOrt(String zvfSenderOrt) {
		this.zvfSenderOrt = zvfSenderOrt;
	}

	public String getZvfSenderTelefon() {
		return zvfSenderTelefon;
	}

	public void setZvfSenderTelefon(String zvfSenderTelefon) {
		this.zvfSenderTelefon = zvfSenderTelefon;
	}

	public String getZvfSenderTelefonIntern() {
		return zvfSenderTelefonIntern;
	}

	public void setZvfSenderTelefonIntern(String zvfSenderTelefonIntern) {
		this.zvfSenderTelefonIntern = zvfSenderTelefonIntern;
	}

	public String getZvfSenderEmail() {
		return zvfSenderEmail;
	}

	public void setZvfSenderEmail(String zvfSenderEmail) {
		this.zvfSenderEmail = zvfSenderEmail;
	}

	public String getZvfBbpStrecke() {
		return zvfBbpStrecke;
	}

	public void setZvfBbpStrecke(String zvfBbpStrecke) {
		this.zvfBbpStrecke = zvfBbpStrecke;
	}

	public String getZvfFormularkennung() {
		return zvfFormularkennung;
	}

	public void setZvfFormularkennung(String zvfFormularkennung) {
		this.zvfFormularkennung = zvfFormularkennung;
	}

	public String getZvfVersionMajor() {
		return zvfVersionMajor;
	}

	public void setZvfVersionMajor(String zvfVersionMajor) {
		this.zvfVersionMajor = zvfVersionMajor;
	}

	public String getZvfVersionMinor() {
		return zvfVersionMinor;
	}

	public void setZvfVersionMinor(String zvfVersionMinor) {
		this.zvfVersionMinor = zvfVersionMinor;
	}

	public String getZvfVersionSub() {
		return zvfVersionSub;
	}

	public void setZvfVersionSub(String zvfVersionSub) {
		this.zvfVersionSub = zvfVersionSub;
	}

	public String getZvfBaumassnahmenart() {
		return zvfBaumassnahmenart;
	}

	public void setZvfBaumassnahmenart(String zvfBaumassnahmenart) {
		this.zvfBaumassnahmenart = zvfBaumassnahmenart;
	}

	public String getZvfKennung() {
		return zvfKennung;
	}

	public void setZvfKennung(String zvfKennung) {
		this.zvfKennung = zvfKennung;
	}

	public String getZvfLisbaKigbau() {
		return zvfLisbaKigbau;
	}

	public void setZvfLisbaKigbau(String zvfLisbaKigbau) {
		this.zvfLisbaKigbau = zvfLisbaKigbau;
	}

	public String getZvfQsKsVes() {
		return zvfQsKsVes;
	}

	public void setZvfQsKsVes(String zvfQsKsVes) {
		this.zvfQsKsVes = zvfQsKsVes;
	}

	public String getZvfKorridor() {
		return zvfKorridor;
	}

	public void setZvfKorridor(String zvfKorridor) {
		this.zvfKorridor = zvfKorridor;
	}

	public Boolean getZvfFestgelegtSPFV() {
		return zvfFestgelegtSPFV;
	}

	public void setZvfFestgelegtSPFV(Boolean zvfFestgelegtSPFV) {
		this.zvfFestgelegtSPFV = zvfFestgelegtSPFV;
	}

	public Boolean getZvfFestgelegtSPNV() {
		return zvfFestgelegtSPNV;
	}

	public void setZvfFestgelegtSPNV(Boolean zvfFestgelegtSPNV) {
		this.zvfFestgelegtSPNV = zvfFestgelegtSPNV;
	}

	public Boolean getZvfFestgelegtSGV() {
		return zvfFestgelegtSGV;
	}

	public void setZvfFestgelegtSGV(Boolean zvfFestgelegtSGV) {
		this.zvfFestgelegtSGV = zvfFestgelegtSGV;
	}

	public String getZvfStreckeVZG(String i) {
		return zvfStreckeVZG.get(i);
	}

	public void setZvfStreckeVZG(String i, String zvfStreckeVZG) {
		this.zvfStreckeVZG.put(i, zvfStreckeVZG);
	}

	public String getZvfStreckeMassnahme(String i) {
		return zvfStreckeMassnahme.get(i);
	}

	public void setZvfStreckeMassnahme(String i, String zvfStreckeMassnahme) {
		this.zvfStreckeMassnahme.put(i, zvfStreckeMassnahme);
	}

	public String getZvfStreckeBaubeginn(String i) {
		return zvfStreckeBaubeginn.get(i);
	}

	public void setZvfStreckeBaubeginn(String i, String zvfStreckeBaubeginn) {
		this.zvfStreckeBaubeginn.put(i, zvfStreckeBaubeginn);
	}

	public String getZvfStreckeBauende(String i) {
		return zvfStreckeBauende.get(i);
	}

	public void setZvfStreckeBauende(String i, String zvfStreckeBauende) {
		this.zvfStreckeBauende.put(i, zvfStreckeBauende);
	}

	public Boolean getZvfStreckeUnterbrochen(String i) {
		return zvfStreckeUnterbrochen.get(i);
	}

	public void setZvfStreckeUnterbrochen(String i, Boolean zvfStreckeUnterbrochen) {
		this.zvfStreckeUnterbrochen.put(i, zvfStreckeUnterbrochen);
	}

	public Boolean getZvfZugRichtung(String i) {
		return zvfZugRichtung.get(i);
	}

	public void setZvfZugRichtung(String i, Boolean zvfZugRichtung) {
		this.zvfZugRichtung.put(i, zvfZugRichtung);
	}

	public String getZvfStreckeGrund(String i) {
		return zvfStreckeGrund.get(i);
	}

	public void setZvfStreckeGrund(String i, String zvfStreckeGrund) {
		this.zvfStreckeGrund.put(i, zvfStreckeGrund);
	}

	public String getZvfStreckeBetriebsweise(String i) {
		return zvfStreckeBetriebsweise.get(i);
	}

	public void setZvfStreckeBetriebsweise(String i, String zvfStreckeBetriebsweise) {
		this.zvfStreckeBetriebsweise.put(i, zvfStreckeBetriebsweise);
	}

	public String getZvfZugTageswechsel(String i) {
		return zvfZugTageswechsel.get(i);
	}

	public void setZvfZugTageswechsel(String i, String tageswechsel) {
		this.zvfZugTageswechsel.put(i, tageswechsel);
	}

	public String getZvfZugVerkehrstag(String i) {
		return zvfZugVerkehrstag.get(i);
	}

	public void setZvfZugVerkehrstag(String i, String verkehrstag) {
		this.zvfZugVerkehrstag.put(i, verkehrstag);
	}

	public String getZvfZugZuggattung(String i) {
		return zvfZugZuggattung.get(i);
	}

	public void setZvfZugZuggattung(String i, String zuggattung) {
		this.zvfZugZuggattung.put(i, zuggattung);
	}

	public String getZvfZugZugnr(String i) {
		return zvfZugZugnr.get(i);
	}

	public void setZvfZugZugnr(String i, String zugnr) {
		this.zvfZugZugnr.put(i, zugnr);
	}

	public Boolean getZvfZugBedarf(String i) {
		return zvfZugBedarf.get(i);
	}

	public void setZvfZugBedarf(String i, Boolean bedarf) {
		this.zvfZugBedarf.put(i, bedarf);
	}

	public String getZvfZugAbgangsbhf(String i) {
		return zvfZugAbgangsbhf.get(i);
	}

	public void setZvfZugAbgangsbhf(String i, String abgangsbhf) {
		this.zvfZugAbgangsbhf.put(i, abgangsbhf);
	}

	public String getZvfZugZielbhf(String i) {
		return zvfZugZielbhf.get(i);
	}

	public void setZvfZugZielbhf(String i, String zielbhf) {
		this.zvfZugZielbhf.put(i, zielbhf);
	}

	public String getZvfZugUmleitungsstrecke(String i) {
		return zvfZugUmleitungsstrecke.get(i);
	}

	public void setZvfZugUmleitungsstrecke(String i, String umleitungsstrecke) {
		this.zvfZugUmleitungsstrecke.put(i, umleitungsstrecke);
	}

	public String getZvfZugVerspaetung(String i) {
		return zvfZugVerspaetung.get(i);
	}

	public void setZvfZugVerspaetung(String i, String verspaetung) {
		this.zvfZugVerspaetung.put(i, verspaetung);
	}

	public String getZvfZugAusfallAb(String i) {
		return zvfZugAusfallAb.get(i);
	}

	public void setZvfZugAusfallAb(String i, String bahnhof) {
		this.zvfZugAusfallAb.put(i, bahnhof);
	}

	public String getZvfZugAusfallBis(String i) {
		return zvfZugAusfallBis.get(i);
	}

	public void setZvfZugAusfallBis(String i, String bahnhof) {
		this.zvfZugAusfallBis.put(i, bahnhof);
	}

	public String getZvfZugVorplanAb(String i) {
		return zvfZugVorplanAb.get(i);
	}

	public void setZvfZugVorplanAb(String i, String bahnhof) {
		this.zvfZugVorplanAb.put(i, bahnhof);
	}

	public List<String> getZvfZugAusfallVerkehrshalt(String i) {
		return zvfZugAusfallVerkehrshalt.get(i);
	}

	public void setZvfZugAusfallVerkehrshalt(String i, List<String> bahnhof) {
		this.zvfZugAusfallVerkehrshalt.put(i, bahnhof);
	}

	public String getZvfZugAusfallVerkehrshaltHalt(String indexString) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int haltIndex = Integer.valueOf(splits[1].trim());
		return zvfZugAusfallVerkehrshalt.get(zugIndex).get(haltIndex);
	}

	public void setZvfZugAusfallVerkehrshaltHalt(String indexString, String bahnhof) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int haltIndex = Integer.valueOf(splits[1].trim());
		List<String> halte = zvfZugAusfallVerkehrshalt.get(zugIndex);
		halte.set(haltIndex, bahnhof);
	}

	public List<String> getZvfZugMoeglicherErsatzhalt(String i) {
		return zvfZugMoeglicherErsatzhalt.get(i);
	}

	public void setZvfZugMoeglicherErsatzhalt(String i, List<String> bahnhof) {
		this.zvfZugMoeglicherErsatzhalt.put(i, bahnhof);
	}

	public String getZvfZugMoeglicherErsatzhaltHalt(String indexString) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int haltIndex = Integer.valueOf(splits[1].trim());
		return zvfZugMoeglicherErsatzhalt.get(zugIndex).get(haltIndex);
	}

	public void setZvfZugMoeglicherErsatzhaltHalt(String indexString, String bahnhof) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int haltIndex = Integer.valueOf(splits[1].trim());
		List<String> halte = zvfZugMoeglicherErsatzhalt.get(zugIndex);
		halte.set(haltIndex, bahnhof);
	}

	public String getZvfZugRegelungsArt(String indexString) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int artIndex = Integer.valueOf(splits[1].trim());
		return zvfZugRegelungsart.get(zugIndex).get(artIndex);
	}

	public void setZvfZugRegelungsArt(String indexString, String art) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int artIndex = Integer.valueOf(splits[1].trim());
		List<String> arten = zvfZugRegelungsart.get(zugIndex);
		arten.set(artIndex, art);
	}

	public List<String> getZvfZugRegelungsart(String i) {
		return zvfZugRegelungsart.get(i);
	}

	public void setZvfZugRegelungsart(String i, List<String> bahnhof) {
		this.zvfZugRegelungsart.put(i, bahnhof);
	}

	public String getZvfZugRegelungGiltIn(String indexString) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int bhfIndex = Integer.valueOf(splits[1].trim());
		return zvfZugRegelungGiltIn.get(zugIndex).get(bhfIndex);
	}

	public void setZvfZugRegelungGiltIn(String indexString, String bahnhof) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int bhfIndex = Integer.valueOf(splits[1].trim());
		List<String> bhfe = zvfZugRegelungGiltIn.get(zugIndex);
		bhfe.set(bhfIndex, bahnhof);
	}

	public List<String> getZvfZugRegelungGiltin(String i) {
		return zvfZugRegelungGiltIn.get(i);
	}

	public void setZvfZugRegelungGiltin(String i, List<String> bahnhof) {
		this.zvfZugRegelungGiltIn.put(i, bahnhof);
	}

	public String getZvfZugRegelungText(String indexString) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int textIndex = Integer.valueOf(splits[1].trim());
		return zvfZugRegelungText.get(zugIndex).get(textIndex);
	}

	public void setZvfZugRegelungText(String indexString, String text) {
		String[] splits = indexString.split(",");
		String zugIndex = splits[0].trim();
		int textIndex = Integer.valueOf(splits[1].trim());
		List<String> texte = zvfZugRegelungText.get(zugIndex);
		texte.set(textIndex, text);
	}

	public List<String> getZvfZugRegelungtext(String i) {
		return zvfZugRegelungText.get(i);
	}

	public void setZvfZugRegelungtext(String i, List<String> bahnhof) {
		this.zvfZugRegelungText.put(i, bahnhof);
	}

	public String getZvfZugBemerkung(String i) {
		return zvfZugBemerkung.get(i);
	}

	public void setZvfZugBemerkung(String i, String zugBemerkung) {
		this.zvfZugBemerkung.put(i, zugBemerkung);
	}

	public Integer getZvfZugQsKs(String i) {
		return zvfZugQsKs.get(i);
	}

	public void setZvfZugQsKs(String i, Integer qs_ks) {
		this.zvfZugQsKs.put(i, qs_ks);
	}

	public List<Uebergabeblatt> getZvfAlteVersionen() {
		return zvfAlteVersionen;
	}

	public void setZvfAlteVersionen(List<Uebergabeblatt> zvfAlteVersionen) {
		this.zvfAlteVersionen = zvfAlteVersionen;
	}

	//
	// Terminübersicht Güterverkehrs-EVU
	//

	private String gevuLinkedIds;

	private final Map<String, String> gevuStudieGrobkonzept = new HashMap<String, String>();

	private final Map<String, String> gevuZvfEntwurf = new HashMap<String, String>();

	private final Map<String, Boolean> gevuIsZvFEntwurfErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> gevuStellungnahmeEVU = new HashMap<String, String>();

	private final Map<String, Boolean> gevuIsStellungnahmeEVUErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> gevuZvF = new HashMap<String, String>();

	private final Map<String, Boolean> gevuIsZvFErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> gevuMasterUebergabeblattGV = new HashMap<String, String>();

	private final Map<String, Boolean> gevuIsMasterUebergabeblattGVErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> gevuUebergabeblattGV = new HashMap<String, String>();

	private final Map<String, Boolean> gevuIsUebergabeblattGVErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> gevuFplo = new HashMap<String, String>();

	private final Map<String, Boolean> gevuIsFploErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> gevuEingabeGFD_Z = new HashMap<String, String>();

	private final Map<String, Boolean> gevuIsEingabeGFD_ZErforderlich = new HashMap<String, Boolean>();

	public String getGevuLinkedIds() {
		return gevuLinkedIds;
	}

	public void setGevuLinkedIds(String gevuLinkedIds) {
		this.gevuLinkedIds = gevuLinkedIds;
	}

	public void setGevuStudieGrobkonzept(String key, String value) {
		gevuStudieGrobkonzept.put(key, value);
	}

	public String getGevuStudieGrobkonzept(String key) {
		return gevuStudieGrobkonzept.get(key);
	}

	public void setGevuZvfEntwurf(String key, String value) {
		gevuZvfEntwurf.put(key, value);
	}

	public String getGevuZvfEntwurf(String key) {
		return gevuZvfEntwurf.get(key);
	}

	public void setGevuIsZvFEntwurfErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		gevuIsZvFEntwurfErforderlich.put(key, value);
	}

	public Boolean getGevuIsZvFEntwurfErforderlich(String key) {
		Boolean result = gevuIsZvFEntwurfErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setGevuStellungnahmeEVU(String key, String value) {
		gevuStellungnahmeEVU.put(key, value);
	}

	public String getGevuStellungnahmeEVU(String key) {
		return gevuStellungnahmeEVU.get(key);
	}

	public void setGevuIsStellungnahmeEVUErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		gevuIsStellungnahmeEVUErforderlich.put(key, value);
	}

	public Boolean getGevuIsStellungnahmeEVUErforderlich(String key) {
		Boolean result = gevuIsStellungnahmeEVUErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setGevuZvF(String key, String value) {
		gevuZvF.put(key, value);
	}

	public String getGevuZvF(String key) {
		return gevuZvF.get(key);
	}

	public void setGevuIsZvFErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		gevuIsZvFErforderlich.put(key, value);
	}

	public Boolean getGevuIsZvFErforderlich(String key) {
		Boolean result = gevuIsZvFErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setGevuMasterUebergabeblattGV(String key, String value) {
		gevuMasterUebergabeblattGV.put(key, value);
	}

	public String getGevuMasterUebergabeblattGV(String key) {
		return gevuMasterUebergabeblattGV.get(key);
	}

	public void setGevuIsMasterUebergabeblattGVErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		gevuIsMasterUebergabeblattGVErforderlich.put(key, value);
	}

	public Boolean getGevuIsMasterUebergabeblattGVErforderlich(String key) {
		Boolean result = gevuIsMasterUebergabeblattGVErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setGevuUebergabeblattGV(String key, String value) {
		gevuUebergabeblattGV.put(key, value);
	}

	public String getGevuUebergabeblattGV(String key) {
		return gevuUebergabeblattGV.get(key);
	}

	public void setGevuIsUebergabeblattGVErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		gevuIsUebergabeblattGVErforderlich.put(key, value);
	}

	public Boolean getGevuIsUebergabeblattGVErforderlich(String key) {
		Boolean result = gevuIsUebergabeblattGVErforderlich.get(key);

		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setGevuFplo(String key, String value) {
		gevuFplo.put(key, value);
	}

	public String getGevuFplo(String key) {
		return gevuFplo.get(key);
	}

	public void setGevuIsFploErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		gevuIsFploErforderlich.put(key, value);
	}

	public Boolean getGevuIsFploErforderlich(String key) {
		Boolean result = gevuIsFploErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setGevuEingabeGFD_Z(String key, String value) {
		gevuEingabeGFD_Z.put(key, value);
	}

	public String getGevuEingabeGFD_Z(String key) {
		return gevuEingabeGFD_Z.get(key);
	}

	public void setGevuIsEingabeGFD_ZErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		gevuIsEingabeGFD_ZErforderlich.put(key, value);
	}

	public Boolean getGevuIsEingabeGFD_ZErforderlich(String key) {
		Boolean result = gevuIsEingabeGFD_ZErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	//
	// Terminübersicht Personenverkehrs-EVU
	//
	private String pevuLinkedIds;

	private final Map<String, String> pevuStudieGrobkonzept = new HashMap<String, String>();

	private final Map<String, String> pevuZvfEntwurf = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsZvfEntwurfErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> pevuStellungnahmeEVU = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsStellungnahmeEVUErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> pevuZvF = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsZvFErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> pevuMasterUebergabeblattPV = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsMasterUebergabeblattPVErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> pevuUebergabeblattPV = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsUebergabeblattPVErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> pevuFplo = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsFploErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> pevuEingabeGFD_Z = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsEingabeGFD_ZErforderlich = new HashMap<String, Boolean>();

	private final Map<String, String> pevuBKonzeptEVU = new HashMap<String, String>();

	private final Map<String, Boolean> pevuIsBKonzeptEVUErforderlich = new HashMap<String, Boolean>();

	private final Map<String, Boolean> pevuAusfaelleSEV = new HashMap<String, Boolean>();

	public String getPevuLinkedIds() {
		return pevuLinkedIds;
	}

	public void setPevuLinkedIds(String pevuLinkedIds) {
		this.pevuLinkedIds = pevuLinkedIds;
	}

	public void setPevuStudieGrobkonzept(String key, String value) {
		pevuStudieGrobkonzept.put(key, value);
	}

	public String getPevuStudieGrobkonzept(String key) {
		return pevuStudieGrobkonzept.get(key);
	}

	public void setPevuZvfEntwurf(String key, String value) {
		pevuZvfEntwurf.put(key, value);
	}

	public String getPevuZvfEntwurf(String key) {
		return pevuZvfEntwurf.get(key);
	}

	public void setPevuIsZvfEntwurfErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsZvfEntwurfErforderlich.put(key, value);
	}

	public Boolean getPevuIsZvfEntwurfErforderlich(String key) {
		Boolean result = pevuIsZvfEntwurfErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuStellungnahmeEVU(String key, String value) {
		pevuStellungnahmeEVU.put(key, value);
	}

	public String getPevuStellungnahmeEVU(String key) {
		return pevuStellungnahmeEVU.get(key);
	}

	public void setPevuIsStellungnahmeEVUErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsStellungnahmeEVUErforderlich.put(key, value);
	}

	public Boolean getPevuIsStellungnahmeEVUErforderlich(String key) {
		Boolean result = pevuIsStellungnahmeEVUErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuZvF(String key, String value) {
		pevuZvF.put(key, value);
	}

	public String getPevuZvF(String key) {
		return pevuZvF.get(key);
	}

	public void setPevuIsZvFErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsZvFErforderlich.put(key, value);
	}

	public Boolean getPevuIsZvFErforderlich(String key) {
		Boolean result = pevuIsZvFErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuMasterUebergabeblattPV(String key, String value) {
		pevuMasterUebergabeblattPV.put(key, value);
	}

	public String getPevuMasterUebergabeblattPV(String key) {
		return pevuMasterUebergabeblattPV.get(key);
	}

	public void setPevuIsMasterUebergabeblattPVErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsMasterUebergabeblattPVErforderlich.put(key, value);
	}

	public Boolean getPevuIsMasterUebergabeblattPVErforderlich(String key) {
		Boolean result = pevuIsMasterUebergabeblattPVErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuUebergabeblattPV(String key, String value) {
		pevuUebergabeblattPV.put(key, value);
	}

	public String getPevuUebergabeblattPV(String key) {
		return pevuUebergabeblattPV.get(key);
	}

	public void setPevuIsUebergabeblattPVErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsUebergabeblattPVErforderlich.put(key, value);
	}

	public Boolean getPevuIsUebergabeblattPVErforderlich(String key) {
		Boolean result = pevuIsUebergabeblattPVErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuFplo(String key, String value) {
		pevuFplo.put(key, value);
	}

	public String getPevuFplo(String key) {
		return pevuFplo.get(key);
	}

	public void setPevuIsFploErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsFploErforderlich.put(key, value);
	}

	public Boolean getPevuIsFploErforderlich(String key) {
		Boolean result = pevuIsFploErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuEingabeGFD_Z(String key, String value) {
		pevuEingabeGFD_Z.put(key, value);
	}

	public String getPevuEingabeGFD_Z(String key) {
		return pevuEingabeGFD_Z.get(key);
	}

	public void setPevuIsEingabeGFD_ZErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsEingabeGFD_ZErforderlich.put(key, value);
	}

	public Boolean getPevuIsEingabeGFD_ZErforderlich(String key) {
		Boolean result = pevuIsEingabeGFD_ZErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuBKonzeptEVU(String key, String value) {
		pevuBKonzeptEVU.put(key, value);
	}

	public String getPevuBKonzeptEVU(String key) {
		return pevuBKonzeptEVU.get(key);
	}

	public void setPevuIsBKonzeptEVUErforderlich(String key, Boolean value) {
		if (value == null)
			value = Boolean.FALSE;

		pevuIsBKonzeptEVUErforderlich.put(key, value);
	}

	public Boolean getPevuIsBKonzeptEVUErforderlich(String key) {
		Boolean result = pevuIsBKonzeptEVUErforderlich.get(key);
		if (result == null)
			result = Boolean.FALSE;

		return result;
	}

	public void setPevuAusfaelleSEV(String key, Boolean value) {
		pevuAusfaelleSEV.put(key, value);
	}

	public Boolean getPevuAusfaelleSEV(String key) {
		return pevuAusfaelleSEV.get(key);
	}

	public Boolean getShowZuegeUeb() {
		return showZuegeUeb;
	}

	public void setShowZuegeUeb(Boolean showZuegeUeb) {
		this.showZuegeUeb = showZuegeUeb;
	}

	public Boolean getShowZuegeZvf() {
		return showZuegeZvf;
	}

	public void setShowZuegeZvf(Boolean showZuegeZvf) {
		this.showZuegeZvf = showZuegeZvf;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public boolean isAjax() {
		return ajax;
	}

	public void setInsertBearbeiter(Integer insertBearbeiter) {
		this.insertBearbeiter = insertBearbeiter;
	}

	public Integer getInsertBearbeiter() {
		return insertBearbeiter;
	}

	public Set<BBPMassnahme> getBbpMassnahmenSet() {
    	return bbpMassnahmenSet;
    }

	public void setBbpMassnahmenSet(Set<BBPMassnahme> bbpMassnahmenSet) {
    	this.bbpMassnahmenSet = bbpMassnahmenSet;
    }

}
