package db.training.bob.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import db.training.bob.model.Aenderung;
import db.training.bob.model.ArbeitsleistungRegionalbereich;
import db.training.bob.model.Art;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.EVU;
import db.training.bob.model.Nachbarbahn;
import db.training.bob.model.Prioritaet;
import db.training.bob.model.Regelung;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;

public class BaumassnahmeCRUDTest {

	private BaumassnahmeService service;

	private Baumassnahme baumassnahme;

	private static Integer id;

	private static final boolean KIGBAU = false;

	private static final String STRECKENABSCHNITT = "streckenabschnitt";

	private static final Art ART = Art.KS;

	private static final String ART_DER_MASSNAHME = "Art der Maßnahme";

	private static final String BETRIEBSWEISE = "Betriebsweise";

	private static final Date BEGINN_DATUM = new GregorianCalendar(2008, 0, 1).getTime();

	private static final Date END_DATUM = new GregorianCalendar(2008, 0, 31).getTime();

	private static final Regelung REGELUNG1 = new Regelung();

	private static final Regelung REGELUNG2 = new Regelung();

	private static final Set<Regelung> BBP_REG_IDS = new HashSet<Regelung>();

	private static final String FPLO_NR = "fplonr1";

	private static final Prioritaet PRIORITAET = Prioritaet.ZWEI;

	private static final Date KONSTRUKTIONSEINSCHRAENKUNG = new GregorianCalendar(2008, 0, 15)
	    .getTime();

	private static final Date ABSTIMMUNG_FFZ_DATUM = new GregorianCalendar(2008, 0, 16).getTime();

	private static final Date ANTRAG_AUFHEBUNG_DIENSTRUHE_DATUM = new GregorianCalendar(2008, 0, 17)
	    .getTime();

	private static final Date GENEHMIGUNG_DIENSTRUHE_DATUM = new GregorianCalendar(2008, 0, 18)
	    .getTime();

	private static final String BIUE_NR = "biuenr1";

	private static final String KIGBAU_NR = "kigbau1";

	private static final Integer KORRIDOR_NR = 123;

	private static final String KORRIDOR_ZEITFENSTER = "Zeitfenster 1";

	private static final String QS_NR = "QS1";

	private static final boolean QS_SPFV = true;

	private static final boolean QS_SGV = true;

	private static final boolean QS_SPNV = true;

	private static final Date ESKALATION_BEGINN_DATUM = new GregorianCalendar(2008, 0, 19)
	    .getTime();

	private static final Date ESKALATION_ENTSCHEIDUNG_DATUM = new GregorianCalendar(2008, 0, 20)
	    .getTime();

	private static final boolean ESKALATION_VETO = false;

	private static final Date AUSFALL_DATUM = new GregorianCalendar(2008, 0, 21).getTime();

	private static final String AUSFALLGRUND = "ausfallgrund";

	private static final Integer BISHERIGER_AUFWAND = 5;

	private static final Bearbeitungsbereich BEARBEITUNGSBEREICH = new Bearbeitungsbereich(
	    "Team Umleitung");

	private static final Regionalbereich REGIONALBEREICH_BM = new Regionalbereich("Südost");

	private static final Regionalbereich REGIONALBEREICH_FPL = new Regionalbereich("Süd");

	private static final Set<Bearbeiter> BEARBEITER = new HashSet<Bearbeiter>();

	private static final Bearbeiter BEARBEITER1 = new Bearbeiter();

	private static final Bearbeiter BEARBEITER2 = new Bearbeiter();

	private static final String KOMMENTAR_2000Zeichen = "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddeeeeeeeeee\n"
	    + "aaaaaaaaaabbbbbbbbbb cccccccccdddddddddd eeeeeeeeeaaaaaaaaaa bbbbbbbbbcccccccccc dddddddddzzzzzzzzzz";

	private static final Integer ZVF = 10;

	private static final Integer BIUEB = 11;

	private static final Integer UEB = 12;

	private static final Integer FPLO = 13;

	private static final Set<Aenderung> AENDERUNGEN = new HashSet<Aenderung>();

	private static final Aenderung AENDERUNG1 = new Aenderung();

	private static final Aenderung AENDERUNG2 = new Aenderung();

	private static final Date VERZICHT_QTRASSE_ANTRAG_DATUM = new GregorianCalendar(2008, 0, 25)
	    .getTime();

	private static final Date VERZICHT_QTRASSE_ABSTIMMUNG_DATUM = new GregorianCalendar(2008, 0, 26)
	    .getTime();

	private static final Boolean VERZICHT_QTRASSE_GENEHMIGT = null;

	private static final Map<Regionalbereich, ArbeitsleistungRegionalbereich> ARBEITSLEISTUNGEN_REGIONALBEREICHE = new TreeMap<Regionalbereich, ArbeitsleistungRegionalbereich>();

	private static final ArbeitsleistungRegionalbereich ARBEITSLEISTUNG_RB1 = new ArbeitsleistungRegionalbereich();

	private static final ArbeitsleistungRegionalbereich ARBEITSLEISTUNG_RB2 = new ArbeitsleistungRegionalbereich();

	private static final boolean ABSTIMMUNG_NB_ERFORDERLICH = true;

	private static final Nachbarbahn NACHBARBAHN = new Nachbarbahn("SBB");

	private static final Date ABSTIMMUNG_NB_ERFOLGT = new GregorianCalendar(2008, 0, 27).getTime();

	private static final TerminUebersichtBaubetriebsplanung TERMINE_BBP = new TerminUebersichtBaubetriebsplanung();

	private static final Map<EVU, TerminUebersichtPersonenverkehrsEVU> TERMINE_P_EVU = new TreeMap<EVU, TerminUebersichtPersonenverkehrsEVU>();

	private static final EVU EVU1 = new EVU();

	private static final EVU EVU2 = new EVU();

	private static final TerminUebersichtPersonenverkehrsEVU TERMINE_P_EVU1 = new TerminUebersichtPersonenverkehrsEVU();

	private static final TerminUebersichtPersonenverkehrsEVU TERMINE_P_EVU2 = new TerminUebersichtPersonenverkehrsEVU();

	private static final TerminUebersichtGueterverkehrsEVU TERMINE_G_EVU1 = new TerminUebersichtGueterverkehrsEVU();

	private static final TerminUebersichtGueterverkehrsEVU TERMINE_G_EVU2 = new TerminUebersichtGueterverkehrsEVU();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		REGELUNG1.setRegelungId("123");
		REGELUNG2.setRegelungId("456");
		BEARBEITER1.setName("Nachname1");
		BEARBEITER1.setVorName("Vorname1");
		BEARBEITER2.setName("Nachname2");
		BEARBEITER2.setVorName("Nachname2");
		AENDERUNG1.setAenderungsNr(1);
		AENDERUNG2.setAenderungsNr(2);
		ARBEITSLEISTUNG_RB1.setAusFaelle(1);
		ARBEITSLEISTUNG_RB2.setAusFaelle(2);
		EVU1.setName("EVU1");
		EVU2.setName("EVU2");
		TERMINE_P_EVU1.setBKonzeptEVU(new GregorianCalendar(2008, 0, 26).getTime());
		TERMINE_P_EVU2.setBKonzeptEVU(new GregorianCalendar(2008, 0, 27).getTime());

		id = createBaumassnahme();
	}

	@Before
	public void setUp() throws Exception {
		service = new BaumassnahmeServiceImpl();
		baumassnahme = service.findById(id);
	}

	@Test
	public void testLoad() {
		assertEquals(ART, baumassnahme.getArt());
		assertEquals(KIGBAU, baumassnahme.isKigBau());
		// assertEquals(STRECKE, baumassnahme.getStrecke());
		assertEquals(STRECKENABSCHNITT, baumassnahme.getStreckenAbschnitt());
		assertEquals(ART_DER_MASSNAHME, baumassnahme.getArtDerMassnahme());
		assertEquals(BETRIEBSWEISE, baumassnahme.getBetriebsweise());
		assertEquals(BEGINN_DATUM, baumassnahme.getBeginnDatum());
		assertEquals(END_DATUM, baumassnahme.getEndDatum());
		// assertEquals(BBP_MAS_ID, baumassnahme.getBbpMasId());

		// Set<Regelung> regelungen = baumassnahme.getBbpRegIds();
		// assertEquals(2, regelungen.size());
		// assertTrue(regelungen.contains(REGELUNG1));
		// assertTrue(regelungen.contains(REGELUNG2));

		assertEquals(FPLO_NR, baumassnahme.getFploNr());
		assertEquals(PRIORITAET, baumassnahme.getPrioritaet());
		assertEquals(KONSTRUKTIONSEINSCHRAENKUNG, baumassnahme.getKonstruktionsEinschraenkung());
		assertEquals(ABSTIMMUNG_FFZ_DATUM, baumassnahme.getAbstimmungFfz());
		assertEquals(ANTRAG_AUFHEBUNG_DIENSTRUHE_DATUM, baumassnahme.getAntragAufhebungDienstruhe());
		assertEquals(GENEHMIGUNG_DIENSTRUHE_DATUM, baumassnahme.getGenehmigungAufhebungDienstruhe());
		assertEquals(BIUE_NR, baumassnahme.getBiUeNr());
		assertEquals(KIGBAU_NR, baumassnahme.getKigBauNr());
		assertEquals(KORRIDOR_NR, baumassnahme.getKorridorNr());
		assertEquals(KORRIDOR_ZEITFENSTER, baumassnahme.getKorridorZeitfenster());
		assertEquals(QS_NR, baumassnahme.getQsNr());
		assertEquals(QS_SPFV, baumassnahme.isQsSPFV());
		assertEquals(QS_SGV, baumassnahme.isQsSGV());
		assertEquals(QS_SPNV, baumassnahme.isQsSPNV());
		assertEquals(ESKALATION_BEGINN_DATUM, baumassnahme.getEskalationsBeginn());
		assertEquals(ESKALATION_ENTSCHEIDUNG_DATUM, baumassnahme.getEskalationsEntscheidung());
		assertEquals(ESKALATION_VETO, baumassnahme.isEskalationVeto());
		assertEquals(AUSFALL_DATUM, baumassnahme.getAusfallDatum());
		assertEquals(AUSFALLGRUND, baumassnahme.getAusfallGrund());
		assertEquals(BISHERIGER_AUFWAND, baumassnahme.getBisherigerAufwand());
		assertEquals(BEARBEITUNGSBEREICH, baumassnahme.getBearbeitungsbereich());
		assertEquals(REGIONALBEREICH_BM, baumassnahme.getRegionalBereichBM());
		assertEquals(REGIONALBEREICH_FPL, baumassnahme.getRegionalBereichFpl());
		// Set<Bearbeiter> bearbeiter = baumassnahme.getBearbeiter();
		// assertEquals(2, bearbeiter.size());
		// assertTrue(bearbeiter.contains(BEARBEITER1));
		// assertTrue(bearbeiter.contains(BEARBEITER2));
		assertEquals(ZVF, baumassnahme.getAufwandZvF());
		assertEquals(BIUEB, baumassnahme.getAufwandBiUeb());
		assertEquals(UEB, baumassnahme.getAufwandUeb());
		assertEquals(FPLO, baumassnahme.getAufwandFplo());
		Set<Aenderung> aenderungen = baumassnahme.getAenderungen();
		assertEquals(2, aenderungen.size());
		assertTrue(aenderungen.contains(AENDERUNG1));
		assertTrue(aenderungen.contains(AENDERUNG2));
		assertEquals(VERZICHT_QTRASSE_ANTRAG_DATUM, baumassnahme.getVerzichtQTrasseBeantragt());
		assertEquals(VERZICHT_QTRASSE_ABSTIMMUNG_DATUM, baumassnahme.getVerzichtQTrasseAbgestimmt());
		assertEquals(VERZICHT_QTRASSE_GENEHMIGT, baumassnahme.getVerzichtQTrasseGenehmigt());
		Map<Regionalbereich, ArbeitsleistungRegionalbereich> arbeitsleistungen = baumassnahme
		    .getArbeitsleistungDerRegionalbereiche();
		assertEquals(2, arbeitsleistungen.size());
		assertTrue(arbeitsleistungen.containsKey(REGIONALBEREICH_BM));
		assertTrue(arbeitsleistungen.containsKey(REGIONALBEREICH_FPL));
		assertEquals(ARBEITSLEISTUNG_RB1, arbeitsleistungen.get(REGIONALBEREICH_BM));
		assertEquals(ARBEITSLEISTUNG_RB2, arbeitsleistungen.get(REGIONALBEREICH_FPL));
		assertEquals(ABSTIMMUNG_NB_ERFORDERLICH, baumassnahme.isAbstimmungNbErforderlich());
		assertEquals(NACHBARBAHN, baumassnahme.getNachbarbahn());
		assertEquals(ABSTIMMUNG_NB_ERFOLGT, baumassnahme.getAbstimmungNbErfolgtAm());
		assertEquals(TERMINE_BBP, baumassnahme.getBaubetriebsplanung());
		// Map<EVU, TerminUebersichtPersonenverkehrsEVU> terminePevu =
		// baumassnahme.getPersonenverkehrsEVUs();
		// assertEquals(2, terminePevu.size());
		// assertTrue(terminePevu.containsKey(EVU1));
		// assertTrue(terminePevu.containsKey(EVU2));
		// assertEquals(TERMINE_P_EVU2, terminePevu.get(EVU2));
		// assertEquals(TERMINE_P_EVU1, terminePevu.get(EVU1));
	}

	@Test
	public void testUpdate() {
		// baumassnahme.setStrecke(NEUE_STRECKE);
		service.update(baumassnahme);

		// Baumassnahme updatedBaumassnahme = service.findById(id);

		// assertEquals(NEUE_STRECKE, updatedBaumassnahme.getStrecke());
	}

	@Ignore
	@Test
	public void testDelete() {
		service.delete(baumassnahme);
		Baumassnahme emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}

	private static Integer createBaumassnahme() {
		Baumassnahme baumassnahme = new Baumassnahme();
		baumassnahme.setArt(ART);
		baumassnahme.setKigBau(KIGBAU);
		// baumassnahme.setStrecke(STRECKE);
		baumassnahme.setStreckenAbschnitt(STRECKENABSCHNITT);
		baumassnahme.setArtDerMassnahme(ART_DER_MASSNAHME);
		baumassnahme.setBetriebsweise(BETRIEBSWEISE);
		baumassnahme.setBeginnDatum(BEGINN_DATUM);
		baumassnahme.setEndDatum(END_DATUM);
		// baumassnahme.setBbpMasId(BBP_MAS_ID);

		BBP_REG_IDS.add(REGELUNG1);
		BBP_REG_IDS.add(REGELUNG2);
		// baumassnahme.setBbpRegIds(BBP_REG_IDS);

		baumassnahme.setRegionalBereichBM(REGIONALBEREICH_BM);
		baumassnahme.setRegionalBereichFpl(REGIONALBEREICH_FPL);
		baumassnahme.setBearbeitungsbereich(BEARBEITUNGSBEREICH);
		baumassnahme.setFploNr(FPLO_NR);
		baumassnahme.setPrioritaet(PRIORITAET);

		BEARBEITER.add(BEARBEITER1);
		BEARBEITER.add(BEARBEITER2);
		// baumassnahme.setBearbeiter(BEARBEITER);

		baumassnahme.setKonstruktionsEinschraenkung(KONSTRUKTIONSEINSCHRAENKUNG);
		baumassnahme.setAbstimmungFfz(ABSTIMMUNG_FFZ_DATUM);
		baumassnahme.setAntragAufhebungDienstruhe(ANTRAG_AUFHEBUNG_DIENSTRUHE_DATUM);
		baumassnahme.setGenehmigungAufhebungDienstruhe(GENEHMIGUNG_DIENSTRUHE_DATUM);
		baumassnahme.setBiUeNr(BIUE_NR);
		// baumassnahme.setKigBauNr(KIGBAU_NR);
		baumassnahme.setKorridorNr(KORRIDOR_NR);
		// baumassnahme.setKorridorZeitfenster(KORRIDOR_ZEITFENSTER);
		// baumassnahme.setQsNr(QS_NR);
		baumassnahme.setQsSPFV(QS_SPFV);
		baumassnahme.setQsSGV(QS_SGV);
		baumassnahme.setQsSPNV(QS_SPNV);
		baumassnahme.setEskalationsBeginn(ESKALATION_BEGINN_DATUM);
		baumassnahme.setEskalationsEntscheidung(ESKALATION_ENTSCHEIDUNG_DATUM);
		baumassnahme.setEskalationVeto(ESKALATION_VETO);
		baumassnahme.setAusfallDatum(AUSFALL_DATUM);
		// baumassnahme.setAusfallGrund(AUSFALLGRUND);
		baumassnahme.setBisherigerAufwand(BISHERIGER_AUFWAND);
		baumassnahme.setKommentar(KOMMENTAR_2000Zeichen);
		baumassnahme.setAufwandZvF(ZVF);
		baumassnahme.setAufwandBiUeb(BIUEB);
		baumassnahme.setAufwandUeb(UEB);
		baumassnahme.setAufwandFplo(FPLO);

		AENDERUNGEN.add(AENDERUNG1);
		AENDERUNGEN.add(AENDERUNG2);
		baumassnahme.setAenderungen(AENDERUNGEN);

		baumassnahme.setVerzichtQTrasseBeantragt(VERZICHT_QTRASSE_ANTRAG_DATUM);
		baumassnahme.setVerzichtQTrasseAbgestimmt(VERZICHT_QTRASSE_ABSTIMMUNG_DATUM);
		baumassnahme.setVerzichtQTrasseGenehmigt(VERZICHT_QTRASSE_GENEHMIGT);

		ARBEITSLEISTUNGEN_REGIONALBEREICHE.put(REGIONALBEREICH_BM, ARBEITSLEISTUNG_RB1);
		ARBEITSLEISTUNGEN_REGIONALBEREICHE.put(REGIONALBEREICH_FPL, ARBEITSLEISTUNG_RB2);
		baumassnahme.setArbeitsleistungDerRegionalbereiche(ARBEITSLEISTUNGEN_REGIONALBEREICHE);

		baumassnahme.setAbstimmungNbErforderlich(ABSTIMMUNG_NB_ERFORDERLICH);
		baumassnahme.setNachbarbahn(NACHBARBAHN);
		baumassnahme.setAbstimmungNbErfolgtAm(ABSTIMMUNG_NB_ERFOLGT);
		baumassnahme.setBaubetriebsplanung(TERMINE_BBP);

		TERMINE_P_EVU.put(EVU1, TERMINE_P_EVU1);
		TERMINE_P_EVU.put(EVU2, TERMINE_P_EVU2);
		// baumassnahme.setPersonenverkehrsEVUs(TERMINE_P_EVU);

		// referenzierte Objekte Speichern
		RegelungService regelungService = new RegelungServiceImpl();
		regelungService.create(REGELUNG1);
		regelungService.create(REGELUNG2);

		TerminUebersichtPersonenverkehrsEVUService pEVUService = new TerminUebersichtPersonenverkehrsEVUServiceImpl();
		pEVUService.create(TERMINE_P_EVU1);
		pEVUService.create(TERMINE_P_EVU2);

		TerminUebersichtGueterverkehrsEVUService gEVUService = new TerminUebersichtGueterverkehrsEVUServiceImpl();
		gEVUService.create(TERMINE_G_EVU1);
		gEVUService.create(TERMINE_G_EVU2);

		EVUService evuService = new EVUServiceImpl();
		evuService.create(EVU1);
		evuService.create(EVU2);

		TerminUebersichtBaubetriebsplanungService bbpService = new TerminUebersichtBaubetriebsplanungServiceImpl();
		bbpService.create(TERMINE_BBP);

		NachbarbahnService nachbarbahnService = new NachbarbahnServiceImpl();
		nachbarbahnService.create(NACHBARBAHN);

		ArbeitsleistungRegionalbereichService arbeitsleistungRegionalbereichService = new ArbeitsleistungRegionalbereichServiceImpl();
		arbeitsleistungRegionalbereichService.create(ARBEITSLEISTUNG_RB1);
		arbeitsleistungRegionalbereichService.create(ARBEITSLEISTUNG_RB2);

		AenderungService aenderungService = new AenderungServiceImpl();
		aenderungService.create(AENDERUNG1);
		aenderungService.create(AENDERUNG2);

		BearbeitungsbereichService bearbeitungsbereichService = new BearbeitungsbereichServiceImpl();
		bearbeitungsbereichService.create(BEARBEITUNGSBEREICH);

		RegionalbereichService regionalbereichService = new RegionalbereichServiceImpl();
		regionalbereichService.create(REGIONALBEREICH_BM);
		regionalbereichService.create(REGIONALBEREICH_FPL);

		BearbeiterService bearbeiterService = new BearbeiterServiceImpl();
		bearbeiterService.create(BEARBEITER1);
		bearbeiterService.create(BEARBEITER2);

		// Baumassnahme speichern
		BaumassnahmeService service = new BaumassnahmeServiceImpl();
		service.create(baumassnahme);

		return baumassnahme.getId();
	}
}
