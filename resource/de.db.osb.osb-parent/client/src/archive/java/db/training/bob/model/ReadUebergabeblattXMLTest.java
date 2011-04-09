package db.training.bob.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.model.zvf.helper.Haltart;

public class ReadUebergabeblattXMLTest {

	private static Uebergabeblatt zvf;

	@BeforeClass
	public static void setUpBeforeClass() throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance("db.training.bob.model.zvf");
		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		zvf = (Uebergabeblatt) unmarshaller.unmarshal(new File(
		    "src/osb/client/src/test/resources/db/training/bob/model/testdata", "UB-01.xml"));
	}

	// Header
	@Test
	public void testHeaderTimestamp() {
		assertEquals(new GregorianCalendar(2008, 9, 10, 14, 17, 43).getTime(), zvf.getHeader()
		    .getTimestamp());
	}

	@Test
	public void testHeaderFilename() {
		assertEquals("081101-081101--B-10.100-________-4-UMS___-UGO___-RAILION_-UB___-01.rtf", zvf
		    .getHeader().getFilename());
	}

	@Test
	public void testHeaderVersion() {
		assertEquals("v2.2.2", zvf.getHeader().getVersionPrgZvf());
	}

	// Header/Sender
	@Test
	public void testHeaderSenderName() {
		assertEquals("Köhler", zvf.getHeader().getSender().getName());
	}

	@Test
	public void testHeaderSenderVorname() {
		assertEquals("Matthias", zvf.getHeader().getSender().getVorname());
	}

	@Test
	public void testHeaderSenderKuerzel() {
		assertEquals(" KM", zvf.getHeader().getSender().getKuerzel());
	}

	@Test
	public void testHeaderSenderAbteilung() {
		assertEquals("I.NV-SO-F (B)", zvf.getHeader().getSender().getAbteilung());
	}

	@Test
	public void testHeaderSenderStrasse() {
		assertEquals("Brandenburger Straße 1", zvf.getHeader().getSender().getStrasse());
	}

	@Test
	public void testHeaderSenderPLZ() {
		assertEquals("04103", zvf.getHeader().getSender().getPlz());
	}

	@Test
	public void testHeaderSenderOrt() {
		assertEquals("Leipzig", zvf.getHeader().getSender().getOrt());
	}

	@Test
	public void testHeaderSenderEmail() {
		assertEquals("Matthias.Koehler@bahn.de", zvf.getHeader().getSender().getEmail());
	}

	@Test
	public void testHeaderSenderTelefon() {
		assertEquals("0341-968-7693", zvf.getHeader().getSender().getTelefon());
	}

	@Test
	public void testHeaderSenderTelefonIntern() {
		assertEquals("927-7693", zvf.getHeader().getSender().getTelefonIntern());
	}

	// Header/Empfaenger
	@Test
	public void testHeaderEmpfaengerlistSize() {
		assertEquals(1, zvf.getHeader().getEmpfaenger().size());
	}

	@Test
	public void testHeaderEmpfaenger() {
		assertTrue(zvf.getHeader().getEmpfaenger().contains("Railion"));
	}

	// Baumassnahmen
	@Test
	public void testBaumassnahme() {
		assertEquals(1, zvf.getMassnahmen().size());
	}

	@Test
	public void testBaumassnahmeMasterniederlassung() {
		assertEquals("RB Südost", zvf.getMassnahmen().iterator().next().getMasterniederlassung());
	}

	@Test
	public void testBaumassnahmeZvfId() {
		assertEquals("240309121110_1234", zvf.getMassnahmen().iterator().next().getZvfid());
	}

	@Test
	public void testBaumassnahmeArt() {
		assertEquals(Art.B, zvf.getMassnahmen().iterator().next().getBaumassnahmenart());
	}

	@Test
	public void testBaumassnahmeQS() {
		assertEquals("", zvf.getMassnahmen().iterator().next().getQsKsVesNr());
	}

	@Test
	public void testBaumassnahmeKorridor() {
		assertEquals("", zvf.getMassnahmen().iterator().next().getKorridor());
	}

	@Test
	public void testBaumassnahmeKigbau() {
		assertEquals("00001", zvf.getMassnahmen().iterator().next().getKigbau());
	}

	@Test
	public void testBaumassnahmeKennung() {
		assertEquals("FFF1011121314", zvf.getMassnahmen().iterator().next().getKennung());
	}

	@Test
	public void testBaumassnahmeExtension() {
		assertEquals("ABC", zvf.getMassnahmen().iterator().next().getExtension());
	}

	@Test
	@Ignore
	// Datenbankzugriff
	public void testBaumassnahmeBBP() {
		assertEquals(2, zvf.getMassnahmen().iterator().next().getBbp().size());
		assertEquals(65016, zvf.getMassnahmen().iterator().next().getBbp().get(0).getNummer()
		    .intValue());
		assertEquals(65017, zvf.getMassnahmen().iterator().next().getBbp().get(1).getNummer()
		    .intValue());
	}

	@Test
	public void testBaumassnahmeMasterFplo() {
		assertEquals(Integer.valueOf(4711), zvf.getMassnahmen().iterator().next().getVorgangsNr());
	}

	@Test
	public void testBaumassnahmeFestgelegtSPFV() {
		assertEquals(false, zvf.getMassnahmen().iterator().next().getFestgelegtSPFV());
	}

	@Test
	public void testBaumassnahmeFestgelegtSPNV() {
		assertEquals(true, zvf.getMassnahmen().iterator().next().getFestgelegtSPNV());
	}

	@Test
	public void testBaumassnahmeFestgelegtSGV() {
		assertEquals(false, zvf.getMassnahmen().iterator().next().getFestgelegtSGV());
	}

	@Test
	public void testBaumassnahmeAntwort() {
		assertEquals(new GregorianCalendar(2008, 7, 27).getTime(), zvf.getMassnahmen().iterator()
		    .next().getAntwort());
	}

	@Test
	public void testBaumassnahmeBaudateVon() {
		assertEquals(new GregorianCalendar(2008, 7, 27).getTime(), zvf.getMassnahmen().iterator()
		    .next().getBaudatevon());
	}

	@Test
	public void testBaumassnahmeBaudateBis() {
		assertEquals(new GregorianCalendar(2008, 7, 27).getTime(), zvf.getMassnahmen().iterator()
		    .next().getBaudatebis());
	}

	@Test
	public void testBaumassnahmeEndstueckZvf() {
		assertEquals(false, zvf.getMassnahmen().iterator().next().getEndStueckZvf());
	}

	@Test
	public void testBaumassnahmeKID1() {
		assertEquals(Integer.valueOf(0), zvf.getMassnahmen().iterator().next().getKID1()
		    .getAnzahl());
	}

	@Test
	public void testBaumassnahmeKID2() {
		assertEquals(Integer.valueOf(9), zvf.getMassnahmen().iterator().next().getKID2());
	}

	@Test
	public void testBaumassnahmeKID3() {
		assertEquals(Integer.valueOf(1), zvf.getMassnahmen().iterator().next().getKID3());
	}

	@Test
	public void testBaumassnahmeKID4() {
		assertEquals(2, zvf.getMassnahmen().iterator().next().getkID4().size());
		assertEquals(Integer.valueOf(2), zvf.getMassnahmen().iterator().next().getkID4().get(0)
		    .getAnzahl());
		assertEquals(Integer.valueOf(3), zvf.getMassnahmen().iterator().next().getkID4().get(1)
		    .getAnzahl());
		assertEquals(new GregorianCalendar(2010, 10, 25).getTime(), zvf.getMassnahmen().iterator()
		    .next().getkID4().get(0).getTimestamp());
		assertEquals(new GregorianCalendar(2010, 10, 26).getTime(), zvf.getMassnahmen().iterator()
		    .next().getkID4().get(1).getTimestamp());
	}

	// Baumassnahme/Version
	@Test
	public void testBaumassnahmeVersionTitel() {
		assertEquals("Übergabeblatt (ÜB)", zvf.getMassnahmen().iterator().next().getVersion()
		    .getTitel());
	}

	@Test
	public void testBaumassnahmeVersionFormularkennung() {
		assertEquals("ÜB-091008", zvf.getMassnahmen().iterator().next().getVersion().getFormular()
		    .toString());
	}

	@Test
	public void testBaumassnahmeVersionMajor() {
		assertEquals(Integer.valueOf(1), zvf.getMassnahmen().iterator().next().getVersion()
		    .getMajor());
	}

	@Test
	public void testBaumassnahmeVersionMinor() {
		assertEquals(Integer.valueOf(4), zvf.getMassnahmen().iterator().next().getVersion()
		    .getMinor());
	}

	@Test
	public void testBaumassnahmeVersionSub() {
		assertEquals("01", zvf.getMassnahmen().iterator().next().getVersion().getSub());
	}

	// Baumassnahme/Streckenabschnitte/Strecke
	@Test
	public void testBaumassnahmeStrecken() {
		assertEquals(1, zvf.getMassnahmen().iterator().next().getStrecke().size());
	}

	@Test
	public void testBaumassnahmeStreckenStartBst() {
		assertEquals("UMS", zvf.getMassnahmen().iterator().next().getStrecke().iterator().next()
		    .getStartbst());
	}

	@Test
	public void testBaumassnahmeStreckenEndBst() {
		assertEquals("UGO", zvf.getMassnahmen().iterator().next().getStrecke().iterator().next()
		    .getEndbst());
	}

	@Test
	public void testBaumassnahmeStreckenExport() {
		assertEquals(true, zvf.getMassnahmen().iterator().next().getStrecke().iterator().next()
		    .getExport());
	}

	@Test
	public void testBaumassnahmeStreckenMassnahme() {
		assertEquals("Mechterst-Sätt - Gotha", zvf.getMassnahmen().iterator().next().getStrecke()
		    .iterator().next().getMassnahme());
	}

	@Test
	public void testBaumassnahmeStreckenBetriebsweise() {
		assertEquals("Ggl Zs 7s", zvf.getMassnahmen().iterator().next().getStrecke().iterator()
		    .next().getBetriebsweise());
	}

	@Test
	public void testBaumassnahmeStreckenGrund() {
		assertEquals("Test-Baustelle", zvf.getMassnahmen().iterator().next().getStrecke()
		    .iterator().next().getGrund());
	}

	@Test
	public void testBaumassnahmeStreckenBaubeginn() {
		assertEquals(new GregorianCalendar(2008, 10, 1, 10, 0, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getStrecke().iterator().next().getBaubeginn());
	}

	@Test
	public void testBaumassnahmeStreckenBauende() {
		assertEquals(new GregorianCalendar(2008, 10, 1, 12, 0, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getStrecke().iterator().next().getBauende());
	}

	@Test
	public void testBaumassnahmeStreckenZeitraumUnterbrochen() {
		assertEquals(false, zvf.getMassnahmen().iterator().next().getStrecke().iterator().next()
		    .getZeitraumUnterbrochen());
	}

	@Test
	@Ignore
	// Datenbankzugriff
	public void testBaumassnahmeStreckenVzg() {
		assertEquals(2, zvf.getMassnahmen().iterator().next().getStrecke().iterator().next()
		    .getStreckeVZG().size());
		assertEquals(1700, zvf.getMassnahmen().iterator().next().getStrecke().iterator().next()
		    .getStreckeVZG().get(0).getNummer().intValue());
		assertEquals(1800, zvf.getMassnahmen().iterator().next().getStrecke().iterator().next()
		    .getStreckeVZG().get(1).getNummer().intValue());
	}

	// Baumassnahme/Allgregelungen
	@Test
	public void testBaumassnahmeAllgRegelungenCount() {
		assertEquals(1, zvf.getMassnahmen().iterator().next().getAllgregelungen().size());
	}

	@Test
	public void testBaumassnahmeAllgRegelungen() {
		assertTrue(zvf.getMassnahmen().iterator().next().getAllgregelungen().contains(""));
	}

	// Baumassnahme/Fplonr
	@Test
	public void testBaumassnahmeFploNrList() {
		assertEquals(7, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .size());
	}

	@Test
	public void testBaumassnahmeNiederlassungBeteiligt() {
		assertEquals(false, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(0).getBeteiligt());
		assertEquals(true, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(1).getBeteiligt());
		assertEquals(true, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(2).getBeteiligt());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(3).getBeteiligt());
		assertEquals(true, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(4).getBeteiligt());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(5).getBeteiligt());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(6).getBeteiligt());
	}

	@Test
	public void testBaumassnahmeNiederlassungFplo() {
		assertEquals(Integer.valueOf(70000), zvf.getMassnahmen().iterator().next().getFplonr()
		    .getNiederlassungen().get(0).getFplonr());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(1).getFplonr());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(2).getFplonr());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(3).getFplonr());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(4).getFplonr());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(5).getFplonr());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(6).getFplonr());
	}

	@Test
	@Ignore
	// Datenbankzugriff
	public void testBaumassnahmeNiederlassungName() {
		assertEquals("Ost", zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(0).getRegionalbereich().getName());
		assertEquals("Nord", zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(1).getRegionalbereich().getName());
		assertEquals("West", zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(2).getRegionalbereich().getName());
		assertEquals("Südost", zvf.getMassnahmen().iterator().next().getFplonr()
		    .getNiederlassungen().get(3).getRegionalbereich().getName());
		assertEquals("Mitte", zvf.getMassnahmen().iterator().next().getFplonr()
		    .getNiederlassungen().get(4).getRegionalbereich().getName());
		assertEquals("Südwest", zvf.getMassnahmen().iterator().next().getFplonr()
		    .getNiederlassungen().get(5).getRegionalbereich().getName());
		assertEquals("Süd", zvf.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(6).getRegionalbereich().getName());
	}

	// Baumassnahme/Zuege
	@Test
	public void testBaumassnahmeZuege() {
		assertEquals(7, zvf.getMassnahmen().iterator().next().getZug().size());
	}

	@Test
	public void testBaumassnahmeZugBedarf() {
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(0).getBedarf());
		assertEquals(true, zvf.getMassnahmen().iterator().next().getZug().get(1).getBedarf());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(2).getBedarf());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(3).getBedarf());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(4).getBedarf());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(5).getBedarf());
	}

	@Test
	public void testBaumassnahmeZugVerkehrstag() {
		assertEquals(new GregorianCalendar(2008, 10, 4).getTime(), zvf.getMassnahmen().iterator()
		    .next().getZug().iterator().next().getVerkehrstag());
		assertEquals(new GregorianCalendar(2008, 10, 2).getTime(), zvf.getMassnahmen().iterator()
		    .next().getZug().get(1).getVerkehrstag());
		assertEquals(new GregorianCalendar(2008, 10, 1).getTime(), zvf.getMassnahmen().iterator()
		    .next().getZug().get(2).getVerkehrstag());
	}

	@Test
	public void testBaumassnahmeZugZugnr() {
		assertEquals(Integer.valueOf(47292), zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getZugnr());
		assertEquals(Integer.valueOf(47298), zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getZugnr());
		assertEquals(Integer.valueOf(47296), zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getZugnr());
	}

	@Test
	public void testBaumassnahmeZugBezeichnung() {
		assertEquals("CSQ", zvf.getMassnahmen().iterator().next().getZug().get(0).getZugbez());
		assertEquals("CS", zvf.getMassnahmen().iterator().next().getZug().get(1).getZugbez());
		assertEquals("CS", zvf.getMassnahmen().iterator().next().getZug().get(2).getZugbez());
	}

	@Test
	public void testBaumassnahmeZugBetreiber() {
		assertEquals("Z1455", zvf.getMassnahmen().iterator().next().getZug().get(0).getBetreiber());
		assertEquals("Z1455", zvf.getMassnahmen().iterator().next().getZug().get(1).getBetreiber());
		assertEquals("Z1455", zvf.getMassnahmen().iterator().next().getZug().get(2).getBetreiber());
	}

	@Test
	public void testBaumassnahmeZugKvprofil() {
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(0).getKvProfil());
		assertEquals("abc", zvf.getMassnahmen().iterator().next().getZug().get(1).getKvProfil());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(2).getKvProfil());
	}

	@Test
	public void testBaumassnahmeZugStreckenklasse() {
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getStreckenKlasse());
		assertEquals("def", zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getStreckenKlasse());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getStreckenKlasse());
	}

	@Test
	public void testBaumassnahmeZugBZA() {
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(0).getBza());
		assertEquals("abcdefghijklmn", zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getBza());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(2).getBza());
	}

	@Test
	public void testBaumassnahmeZugFirstBst() {
		assertEquals("UMS", zvf.getMassnahmen().iterator().next().getZug().iterator().next()
		    .getFirstbst());
		assertEquals("UMS", zvf.getMassnahmen().iterator().next().getZug().get(1).getFirstbst());
		assertEquals("UMS", zvf.getMassnahmen().iterator().next().getZug().get(2).getFirstbst());
	}

	@Test
	public void testBaumassnahmeZugTageswechsel() {
		assertEquals("", zvf.getMassnahmen().iterator().next().getZug().get(0).getTageswechsel());
		assertEquals("", zvf.getMassnahmen().iterator().next().getZug().get(1).getTageswechsel());
		assertEquals("", zvf.getMassnahmen().iterator().next().getZug().get(2).getTageswechsel());
	}

	@Test
	public void testBaumassnahmeZugRegelwegAbgangsbahnhofLangname() {
		assertEquals("Wroblin Glog", zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getRegelweg().getAbgangsbahnhof().getLangName());
		assertEquals("Zdieszowice", zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getRegelweg().getAbgangsbahnhof().getLangName());
		assertEquals("Zdieszowice", zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getRegelweg().getAbgangsbahnhof().getLangName());
	}

	@Test
	public void testBaumassnahmeZugRegelwegAbgangsbahnhofDS100() {
		assertEquals("XPWBG", zvf.getMassnahmen().iterator().next().getZug().get(0).getRegelweg()
		    .getAbgangsbahnhof().getDs100());
		assertEquals("XPZDZ", zvf.getMassnahmen().iterator().next().getZug().get(1).getRegelweg()
		    .getAbgangsbahnhof().getDs100());
		assertEquals("XPZDZ", zvf.getMassnahmen().iterator().next().getZug().get(2).getRegelweg()
		    .getAbgangsbahnhof().getDs100());
	}

	@Test
	public void testBaumassnahmeZugRegelwegZielbahnhofLangname() {
		assertEquals("Tergnier", zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getRegelweg().getZielbahnhof().getLangName());
		assertEquals("Pont-a-Mousson", zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getRegelweg().getZielbahnhof().getLangName());
		assertEquals("Varangeville", zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getRegelweg().getZielbahnhof().getLangName());
	}

	@Test
	public void testBaumassnahmeZugRegelwegZielbahnhofDS100() {
		assertEquals("XFTG", zvf.getMassnahmen().iterator().next().getZug().get(0).getRegelweg()
		    .getZielbahnhof().getDs100());
		assertEquals("XFPM", zvf.getMassnahmen().iterator().next().getZug().get(1).getRegelweg()
		    .getZielbahnhof().getDs100());
		assertEquals("XFVN", zvf.getMassnahmen().iterator().next().getZug().get(2).getRegelweg()
		    .getZielbahnhof().getDs100());
	}

	@Test
	public void testBaumassnahmeZugBemerkung() {
		assertEquals("abababababab", zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getBemerkung());
		assertEquals("", zvf.getMassnahmen().iterator().next().getZug().get(1).getBemerkung());
		assertEquals("", zvf.getMassnahmen().iterator().next().getZug().get(2).getBemerkung());
	}

	@Test
	public void testBaumassnahmeZugAbweichungArt() {
		assertEquals(Abweichungsart.VERSPAETUNG, zvf.getMassnahmen().iterator().next().getZug()
		    .get(0).getAbweichung().getArt());
		assertEquals(Abweichungsart.UMLEITUNG, zvf.getMassnahmen().iterator().next().getZug()
		    .get(1).getAbweichung().getArt());
		assertEquals(Abweichungsart.AUSFALL, zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getAbweichung().getArt());
		assertEquals(Abweichungsart.VORPLAN, zvf.getMassnahmen().iterator().next().getZug().get(3)
		    .getAbweichung().getArt());
		assertEquals(Abweichungsart.GESPERRT, zvf.getMassnahmen().iterator().next().getZug().get(4)
		    .getAbweichung().getArt());
		assertEquals(Abweichungsart.ERSATZHALTE, zvf.getMassnahmen().iterator().next().getZug()
		    .get(5).getAbweichung().getArt());
		assertEquals(Abweichungsart.REGELUNG, zvf.getMassnahmen().iterator().next().getZug().get(6)
		    .getAbweichung().getArt());
	}

	@Test
	public void testBaumassnahmeZugAbweichungUmleitung() {
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(0).getAbweichung()
		    .getUmleitung());
		assertEquals("HHM-HH-HBS", zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getAbweichung().getUmleitung());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(2).getAbweichung()
		    .getUmleitung());
	}

	@Test
	public void testBaumassnahmeZugAbweichungUmleitweg() {
		assertEquals(3, zvf.getMassnahmen().iterator().next().getZug().get(1).getAbweichung()
		    .getUmleitweg().size());
		assertEquals("HHM", zvf.getMassnahmen().iterator().next().getZug().get(1).getAbweichung()
		    .getUmleitweg().get(0));
		assertEquals("HH", zvf.getMassnahmen().iterator().next().getZug().get(1).getAbweichung()
		    .getUmleitweg().get(1));
		assertEquals("HBS", zvf.getMassnahmen().iterator().next().getZug().get(1).getAbweichung()
		    .getUmleitweg().get(2));
	}

	@Test
	public void testBaumassnahmeZugAbweichungVerspaetung() {
		assertEquals(Integer.valueOf(7), zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getAbweichung().getVerspaetung());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(1).getAbweichung()
		    .getVerspaetung());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(2).getAbweichung()
		    .getVerspaetung());
	}

	@Test
	public void testBaumassnahmeZugAbweichungAusfallVon() {
		assertEquals("HH", zvf.getMassnahmen().iterator().next().getZug().get(2).getAbweichung()
		    .getAusfallvon().getDs100());
		assertEquals("Hannover Hbf", zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getAbweichung().getAusfallvon().getLangName());
	}

	@Test
	public void testBaumassnahmeZugAbweichungAusfallBis() {
		assertEquals("ALSK", zvf.getMassnahmen().iterator().next().getZug().get(2).getAbweichung()
		    .getAusfallbis().getDs100());
		assertEquals("Lübeck Skand-kai", zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getAbweichung().getAusfallbis().getLangName());
	}

	@Test
	public void testBaumassnahmeZugAbweichungVorplanAb() {
		assertEquals("XFPM", zvf.getMassnahmen().iterator().next().getZug().get(3).getAbweichung()
		    .getVorplanab().getDs100());
		assertEquals("Pont-a-Mousson", zvf.getMassnahmen().iterator().next().getZug().get(3)
		    .getAbweichung().getVorplanab().getLangName());
	}

	@Test
	public void testBaumassnahmeZugAbweichungHaltliste() {
		assertEquals(2, zvf.getMassnahmen().iterator().next().getZug().get(5).getAbweichung()
		    .getHalt().size());
	}

	@Test
	public void testBaumassnahmeZugAbweichungHaltlisteFolge() {
		assertEquals(Integer.valueOf(1), zvf.getMassnahmen().iterator().next().getZug().get(5)
		    .getAbweichung().getHalt().get(0).getFolge());
		assertEquals(Integer.valueOf(2), zvf.getMassnahmen().iterator().next().getZug().get(5)
		    .getAbweichung().getHalt().get(1).getFolge());
	}

	@Test
	public void testBaumassnahmeZugAbweichungHaltlisteAusfall() {
		assertEquals("HWUN", zvf.getMassnahmen().iterator().next().getZug().get(5).getAbweichung()
		    .getHalt().get(0).getAusfall().getDs100());
		assertEquals("Wunstorf", zvf.getMassnahmen().iterator().next().getZug().get(5)
		    .getAbweichung().getHalt().get(0).getAusfall().getLangName());
		assertEquals("HH", zvf.getMassnahmen().iterator().next().getZug().get(5).getAbweichung()
		    .getHalt().get(1).getAusfall().getDs100());
		assertEquals("Hannover Hbf", zvf.getMassnahmen().iterator().next().getZug().get(5)
		    .getAbweichung().getHalt().get(1).getAusfall().getLangName());
	}

	@Test
	public void testBaumassnahmeZugAbweichungHaltlisteErsatz() {
		assertEquals("XPZDZ", zvf.getMassnahmen().iterator().next().getZug().get(5).getAbweichung()
		    .getHalt().get(0).getErsatz().getDs100());
		assertEquals("Zdieszowice", zvf.getMassnahmen().iterator().next().getZug().get(5)
		    .getAbweichung().getHalt().get(0).getErsatz().getLangName());
		assertEquals(null, zvf.getMassnahmen().iterator().next().getZug().get(5).getAbweichung()
		    .getHalt().get(1).getErsatz());
	}

	@Test
	public void testBaumassnahmeZugAbweichungRegelung() {
		assertEquals(2, zvf.getMassnahmen().iterator().next().getZug().get(6).getAbweichung()
		    .getRegelungen().size());
	}

	@Test
	public void testBaumassnahmeZugAbweichungRegelungArt() {
		assertEquals("Neuer Startbahnhof", zvf.getMassnahmen().iterator().next().getZug().get(6)
		    .getAbweichung().getRegelungen().get(0).getArt());
		assertEquals("Neuer Endbahnhof", zvf.getMassnahmen().iterator().next().getZug().get(6)
		    .getAbweichung().getRegelungen().get(1).getArt());
	}

	@Test
	public void testBaumassnahmeZugAbweichungRegelungGiltIn() {
		assertEquals("HWUN", zvf.getMassnahmen().iterator().next().getZug().get(6).getAbweichung()
		    .getRegelungen().get(0).getGiltIn().getDs100());
		assertEquals("HWUG", zvf.getMassnahmen().iterator().next().getZug().get(6).getAbweichung()
		    .getRegelungen().get(1).getGiltIn().getDs100());
		assertEquals("Wunstorf", zvf.getMassnahmen().iterator().next().getZug().get(6)
		    .getAbweichung().getRegelungen().get(0).getGiltIn().getLangName());
		assertEquals("Wunstorf DB-Gr", zvf.getMassnahmen().iterator().next().getZug().get(6)
		    .getAbweichung().getRegelungen().get(1).getGiltIn().getLangName());
	}

	@Test
	public void testBaumassnahmeZugAbweichungRegelungText() {
		assertEquals("Freitext1", zvf.getMassnahmen().iterator().next().getZug().get(6)
		    .getAbweichung().getRegelungen().get(0).getText());
		assertEquals("Freitext2", zvf.getMassnahmen().iterator().next().getZug().get(6)
		    .getAbweichung().getRegelungen().get(1).getText());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsGattungsnr() {
		assertEquals("68.1", zvf.getMassnahmen().iterator().next().getZug().get(0).getZugdetails()
		    .getGattungsnummer());
		assertEquals("69.1", zvf.getMassnahmen().iterator().next().getZug().get(1).getZugdetails()
		    .getGattungsnummer());
		assertEquals("69.1", zvf.getMassnahmen().iterator().next().getZug().get(2).getZugdetails()
		    .getGattungsnummer());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsVmax() {
		assertEquals(Integer.valueOf(90), zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getZugdetails().getVmax());
		assertEquals(Integer.valueOf(90), zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getZugdetails().getVmax());
		assertEquals(Integer.valueOf(90), zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getZugdetails().getVmax());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsTfz() {
		assertEquals("155", zvf.getMassnahmen().iterator().next().getZug().get(0).getZugdetails()
		    .getTfz().getTfz());
		assertEquals("155", zvf.getMassnahmen().iterator().next().getZug().get(1).getZugdetails()
		    .getTfz().getTfz());
		assertEquals("155", zvf.getMassnahmen().iterator().next().getZug().get(2).getZugdetails()
		    .getTfz().getTfz());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsTfzLzb() {
		assertEquals(true, zvf.getMassnahmen().iterator().next().getZug().get(0).getZugdetails()
		    .getTfz().getLzb());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(1).getZugdetails()
		    .getTfz().getLzb());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(2).getZugdetails()
		    .getTfz().getLzb());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsLast() {
		assertEquals(Integer.valueOf(1800), zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getZugdetails().getLast().getLast());
		assertEquals(Integer.valueOf(1800), zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getZugdetails().getLast().getLast());
		assertEquals(Integer.valueOf(1800), zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getZugdetails().getLast().getLast());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsLastGl() {
		assertEquals(true, zvf.getMassnahmen().iterator().next().getZug().get(0).getZugdetails()
		    .getLast().getGl());
		assertEquals(false, zvf.getMassnahmen().iterator().next().getZug().get(1).getZugdetails()
		    .getLast().getGl());
		assertEquals(true, zvf.getMassnahmen().iterator().next().getZug().get(2).getZugdetails()
		    .getLast().getGl());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsLaenge() {
		assertEquals(Integer.valueOf(480), zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getZugdetails().getLaenge());
		assertEquals(Integer.valueOf(480), zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getZugdetails().getLaenge());
		assertEquals(Integer.valueOf(480), zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getZugdetails().getLaenge());
	}

	@Test
	public void testBaumassnahmeZugZugdetailsBrems() {
		assertEquals("68 G", zvf.getMassnahmen().iterator().next().getZug().get(0).getZugdetails()
		    .getBrems());
		assertEquals("68 G", zvf.getMassnahmen().iterator().next().getZug().get(1).getZugdetails()
		    .getBrems());
		assertEquals("68 G", zvf.getMassnahmen().iterator().next().getZug().get(2).getZugdetails()
		    .getBrems());
	}

	@Test
	public void testBaumassnahmeZugKnotenzeitenBahnhof() {
		assertEquals("AAA", zvf.getMassnahmen().iterator().next().getZug().get(0).getKnotenzeiten()
		    .get(0).getBahnhof());
		assertEquals("CCC", zvf.getMassnahmen().iterator().next().getZug().get(1).getKnotenzeiten()
		    .get(0).getBahnhof());
		assertEquals("BBB", zvf.getMassnahmen().iterator().next().getZug().get(2).getKnotenzeiten()
		    .get(0).getBahnhof());
	}

	@Test
	public void testBaumassnahmeZugKnotenzeitenHaltart() {
		assertEquals(Haltart.B, zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getKnotenzeiten().get(0).getHaltart());
		assertEquals(Haltart.LEER, zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getKnotenzeiten().get(0).getHaltart());
		assertEquals(Haltart.PLUS, zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getKnotenzeiten().get(0).getHaltart());
	}

	@Test
	public void testBaumassnahmeZugKnotenzeitenAnkunft() {
		assertEquals(new GregorianCalendar(1970, 0, 1, 14, 17, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getZug().get(0).getKnotenzeiten().get(0).getAnkunft());
		assertEquals(new GregorianCalendar(1970, 0, 1, 14, 17, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getZug().get(1).getKnotenzeiten().get(0).getAnkunft());
		assertEquals(new GregorianCalendar(1970, 0, 1, 14, 17, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getZug().get(2).getKnotenzeiten().get(0).getAnkunft());
	}

	@Test
	public void testBaumassnahmeZugKnotenzeitenAbfahrt() {
		assertEquals(new GregorianCalendar(1970, 0, 1, 14, 18, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getZug().get(0).getKnotenzeiten().get(0).getAbfahrt());
		assertEquals(new GregorianCalendar(1970, 0, 1, 14, 18, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getZug().get(1).getKnotenzeiten().get(0).getAbfahrt());
		assertEquals(new GregorianCalendar(1970, 0, 1, 14, 18, 0).getTime(), zvf.getMassnahmen()
		    .iterator().next().getZug().get(2).getKnotenzeiten().get(0).getAbfahrt());
	}

	@Test
	public void testBaumassnahmeZugKnotenzeitenRelativlage() {
		assertEquals(Integer.valueOf(-5), zvf.getMassnahmen().iterator().next().getZug().get(0)
		    .getKnotenzeiten().get(0).getRelativlage());
		assertEquals(Integer.valueOf(5), zvf.getMassnahmen().iterator().next().getZug().get(1)
		    .getKnotenzeiten().get(0).getRelativlage());
		assertEquals(Integer.valueOf(0), zvf.getMassnahmen().iterator().next().getZug().get(2)
		    .getKnotenzeiten().get(0).getRelativlage());
	}

}
