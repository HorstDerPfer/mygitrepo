package db.training.bob.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.Art;
import db.training.bob.model.zvf.Abweichung;
import db.training.bob.model.zvf.Bahnhof;
import db.training.bob.model.zvf.Fplonr;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Knotenzeit;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Niederlassung;
import db.training.bob.model.zvf.Regelweg;
import db.training.bob.model.zvf.Sender;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Tfz;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Version;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.Zugdetails;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.model.zvf.helper.FormularKennung;
import db.training.bob.model.zvf.helper.Haltart;
import db.training.bob.service.zvf.UebergabeblattService;
import db.training.bob.service.zvf.UebergabeblattServiceImpl;

public class UebergabeblattCRUDTest {

	private UebergabeblattService service;

	private Uebergabeblatt ueb;

	private static final Date TIMESTAMP = new GregorianCalendar(2008, 9, 1, 23, 30, 59).getTime();

	private static final String FILENAME = "file.xml";

	private static final String VERSION_PRG_ZVF = "v2.1.2";

	private static final String SENDER_NAME = "Nachname";

	private static final String EMPFAENGER1 = "AbteilungA";

	private static final String EMPFAENGER2 = "AbteilungB";

	private static final String MASTERNIEDERLASSUNG = "RB Nord";

	private static final Art ART = Art.A;

	private static final Date ANTWORT_DATUM = new GregorianCalendar(2008, 10, 1).getTime();

	private static final FormularKennung FORMULARKENNUNG = FormularKennung.UeB_061108;

	private static final Integer MAJOR = 1;

	private static final String SUB = "01";

	private static final String STARTBST1 = "AAA";

	private static final Date BAUBEGINN1 = new GregorianCalendar(2008, 9, 1, 22, 30, 50).getTime();

	private static final Boolean ZEITRAUM_UNTERBR1 = true;

	private static final String STARTBST2 = "BBB";

	private static final String ALLG_REGELUNG1 = "abcdef";

	private static final String ALLG_REGELUNG2 = "ghijkl";

	private static final Integer ZUGNR1 = 123;

	private static final Boolean BEDARF1 = true;

	private static final Date VERKEHRSTAG1 = new GregorianCalendar(2008, 9, 1).getTime();

	private static final Integer ZUGNR2 = 456;

	private static final Integer LAUFENDENR1 = new Integer(1);

	private static final Integer LAUFENDENR2 = new Integer(2);

	private static final String GATTUNGSNR = "47.2";

	private static final Integer LAENGE = new Integer(500);

	private static final Boolean ENDSTUECK_ZVF = true;

	private static final Boolean FESTGELEGT_SPFV = true;

	private static final String ZUGBEZ1 = "CS";

	private static final String RB1 = "Ost";

	private static final Boolean BETEILIGT1 = true;

	private static final String RB2 = "Südost";

	private static final Boolean BETEILIGT2 = false;

	private static final String BHF_LANG1 = "Wroblin Glog";

	private static final String BHF_DS1 = "XPWBG";

	private static final String BHF_LANG2 = "Tergnier";

	private static final String BHF_DS2 = "XFTG";

	private static final Abweichungsart UMLEITUNG = Abweichungsart.UMLEITUNG;

	private static final Boolean LZB = true;

	private static final Date ABFAHRT_DATUM = new GregorianCalendar(2008, 9, 1).getTime();

	private static final Haltart HALT_B = Haltart.B;

	private static Integer id;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createUebergabeblatt();
	}

	@Before
	public void setUp() throws Exception {
		service = new UebergabeblattServiceImpl();
		ueb = service.findById(id);
	}

	@Test
	public void testLoad() {
		// Header
		assertEquals(TIMESTAMP, ueb.getHeader().getTimestamp());
		assertEquals(FILENAME, ueb.getHeader().getFilename());
		assertEquals(VERSION_PRG_ZVF, ueb.getHeader().getVersionPrgZvf());
		assertEquals(SENDER_NAME, ueb.getHeader().getSender().getName());
		assertEquals(2, ueb.getHeader().getEmpfaenger().size());
		assertTrue(ueb.getHeader().getEmpfaenger().contains(EMPFAENGER1));
		assertTrue(ueb.getHeader().getEmpfaenger().contains(EMPFAENGER2));

		// Baumassnahmen
		assertEquals(1, ueb.getMassnahmen().size());
		assertEquals(MASTERNIEDERLASSUNG, ueb.getMassnahmen().iterator().next()
		    .getMasterniederlassung());
		assertEquals(ART, ueb.getMassnahmen().iterator().next().getBaumassnahmenart());
		assertEquals(ANTWORT_DATUM, ueb.getMassnahmen().iterator().next().getAntwort());
		assertEquals(FESTGELEGT_SPFV, ueb.getMassnahmen().iterator().next().getFestgelegtSPFV());
		assertEquals(FORMULARKENNUNG, ueb.getMassnahmen().iterator().next().getVersion()
		    .getFormular());
		assertEquals(MAJOR, ueb.getMassnahmen().iterator().next().getVersion().getMajor());
		assertEquals(ENDSTUECK_ZVF, ueb.getMassnahmen().iterator().next().getVersion()
		    .getEndStueckZvf());
		assertEquals(SUB, ueb.getMassnahmen().iterator().next().getVersion().getSub());
		assertEquals(2, ueb.getMassnahmen().iterator().next().getStrecke().size());
		Iterator<Strecke> its = ueb.getMassnahmen().iterator().next().getStrecke().iterator();
		Strecke s1 = its.next();
		assertEquals(STARTBST1, s1.getStartbst());
		assertEquals(BAUBEGINN1, s1.getBaubeginn());
		assertEquals(ZEITRAUM_UNTERBR1, s1.getZeitraumUnterbrochen());
		Strecke s2 = its.next();
		assertEquals(STARTBST2, s2.getStartbst());
		assertTrue(ueb.getMassnahmen().iterator().next().getAllgregelungen().contains(
		    ALLG_REGELUNG1));
		assertTrue(ueb.getMassnahmen().iterator().next().getAllgregelungen().contains(
		    ALLG_REGELUNG2));
		assertEquals(2, ueb.getMassnahmen().iterator().next().getZug().size());
		assertEquals(LAUFENDENR1, ueb.getMassnahmen().iterator().next().getZug().get(0)
		    .getLaufendeNr());
		assertEquals(ZUGNR1, ueb.getMassnahmen().iterator().next().getZug().get(0).getZugnr());
		assertEquals(BEDARF1, ueb.getMassnahmen().iterator().next().getZug().get(0).getBedarf());
		assertEquals(VERKEHRSTAG1, ueb.getMassnahmen().iterator().next().getZug().get(0)
		    .getVerkehrstag());
		assertEquals(ZUGBEZ1, ueb.getMassnahmen().iterator().next().getZug().get(0).getZugbez());
		assertEquals(LAUFENDENR2, ueb.getMassnahmen().iterator().next().getZug().get(1)
		    .getLaufendeNr());
		assertEquals(ZUGNR2, ueb.getMassnahmen().iterator().next().getZug().get(1).getZugnr());
		assertEquals(RB1, ueb.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(0).getRegionalbereich().getName());
		assertEquals(BETEILIGT1, ueb.getMassnahmen().iterator().next().getFplonr()
		    .getNiederlassungen().get(0).getBeteiligt());
		assertEquals(RB2, ueb.getMassnahmen().iterator().next().getFplonr().getNiederlassungen()
		    .get(1).getRegionalbereich().getName());
		assertEquals(BETEILIGT2, ueb.getMassnahmen().iterator().next().getFplonr()
		    .getNiederlassungen().get(1).getBeteiligt());
		assertEquals(BHF_LANG1, ueb.getMassnahmen().iterator().next().getZug().get(0).getRegelweg()
		    .getAbgangsbahnhof().getLangName());
		assertEquals(BHF_DS1, ueb.getMassnahmen().iterator().next().getZug().get(0).getRegelweg()
		    .getAbgangsbahnhof().getDs100());
		assertEquals(BHF_LANG2, ueb.getMassnahmen().iterator().next().getZug().get(0).getRegelweg()
		    .getZielbahnhof().getLangName());
		assertEquals(BHF_DS2, ueb.getMassnahmen().iterator().next().getZug().get(0).getRegelweg()
		    .getZielbahnhof().getDs100());
		assertEquals(UMLEITUNG, ueb.getMassnahmen().iterator().next().getZug().get(0)
		    .getAbweichung().getArt());
		assertEquals(BHF_LANG1, ueb.getMassnahmen().iterator().next().getZug().get(0)
		    .getAbweichung().getHalt().get(0).getAusfall().getLangName());
		// assertEquals(BHF_LANG2, ueb.getMassnahmen().iterator().next().getZug().get(0)
		// .getAbweichung().getHalt().get(0).getErsatz().getLangName());
		assertEquals(GATTUNGSNR, ueb.getMassnahmen().iterator().next().getZug().get(0)
		    .getZugdetails().getGattungsnummer());
		assertEquals(LAENGE, ueb.getMassnahmen().iterator().next().getZug().get(0).getZugdetails()
		    .getLaenge());
		assertEquals(LZB, ueb.getMassnahmen().iterator().next().getZug().get(0).getZugdetails()
		    .getTfz().getLzb());
		assertEquals(ABFAHRT_DATUM, ueb.getMassnahmen().iterator().next().getZug().get(0)
		    .getKnotenzeiten().get(0).getAbfahrt());
		assertEquals(HALT_B, ueb.getMassnahmen().iterator().next().getZug().get(0)
		    .getKnotenzeiten().get(0).getHaltart());
	}

	@Test
	public void testUpdate() {
		final Date TIMESTAMP_NEU = new GregorianCalendar(2008, 9, 1, 23, 30, 58).getTime();
		final String FILENAME_NEU = "newfile.xml";
		final String VERSION_PRG_ZVF_NEU = "v2.1.3";
		final String SENDER_NAME_NEU = "neuerNachname";
		final String EMPFAENGER1_NEU = "AbteilungC";
		final String EMPFAENGER3 = "AbteilungD";
		final String MASTERNIEDERLASSUNG_NEU = "RB Mitte";
		final Art ART_NEU = Art.B;
		final Date ANTWORT_DATUM_NEU = new GregorianCalendar(2008, 10, 2).getTime();
		final FormularKennung FORMULARKENNUNG_NEU = FormularKennung.ZVF_061108;
		final Integer MAJOR_NEU = 2;
		final String SUB_NEU = "02";
		final boolean ZEITRAUM_UNTERBR1_NEU = false;
		final String STARTBST3 = "CCC";
		final Integer ZUGNR1_NEU = 789;
		final Date VERKEHRSTAG1_NEU = new GregorianCalendar(2008, 10, 2).getTime();
		final Boolean BEDARF1_NEU = false;
		final Integer LAUFENDENR3 = new Integer(3);

		// Header
		ueb.getHeader().setTimestamp(TIMESTAMP_NEU);
		ueb.getHeader().setFilename(FILENAME_NEU);
		ueb.getHeader().setVersionPrgZvf(VERSION_PRG_ZVF_NEU);
		ueb.getHeader().getSender().setName(SENDER_NAME_NEU);
		Set<String> emp = ueb.getHeader().getEmpfaenger();
		emp.remove(EMPFAENGER1);
		emp.add(EMPFAENGER1_NEU);
		emp.add(EMPFAENGER3);
		ueb.getHeader().setEmpfaenger(emp);

		// Baumassnahmen
		Massnahme m = ueb.getMassnahmen().iterator().next();
		m.setMasterniederlassung(MASTERNIEDERLASSUNG_NEU);
		m.setBaumassnahmenart(ART_NEU);
		m.setAntwort(ANTWORT_DATUM_NEU);
		Version v = m.getVersion();
		v.setFormular(FORMULARKENNUNG_NEU);
		v.setMajor(MAJOR_NEU);
		v.setSub(SUB_NEU);
		Strecke s = m.getStrecke().iterator().next();
		s.setZeitraumUnterbrochen(ZEITRAUM_UNTERBR1_NEU);
		Strecke s3 = new Strecke();
		s3.setStartbst(STARTBST3);
		m.getStrecke().add(s3);
		m.getZug().get(0).setZugnr(ZUGNR1_NEU);
		m.getZug().get(0).setVerkehrstag(VERKEHRSTAG1_NEU);
		m.getZug().get(0).setBedarf(BEDARF1_NEU);
		m.addZug(new Zug());

		service.update(ueb);
		Uebergabeblatt updatedUeb = service.findById(id);

		// Header
		assertEquals(TIMESTAMP_NEU, updatedUeb.getHeader().getTimestamp());
		assertEquals(FILENAME_NEU, updatedUeb.getHeader().getFilename());
		assertEquals(VERSION_PRG_ZVF_NEU, updatedUeb.getHeader().getVersionPrgZvf());
		assertEquals(SENDER_NAME_NEU, updatedUeb.getHeader().getSender().getName());
		assertEquals(3, updatedUeb.getHeader().getEmpfaenger().size());
		assertTrue(ueb.getHeader().getEmpfaenger().contains(EMPFAENGER1_NEU));
		assertTrue(ueb.getHeader().getEmpfaenger().contains(EMPFAENGER2));
		assertTrue(ueb.getHeader().getEmpfaenger().contains(EMPFAENGER3));

		// Baumassnahmen
		assertEquals(1, updatedUeb.getMassnahmen().size());
		assertEquals(MASTERNIEDERLASSUNG_NEU, updatedUeb.getMassnahmen().iterator().next()
		    .getMasterniederlassung());
		assertEquals(ART_NEU, updatedUeb.getMassnahmen().iterator().next().getBaumassnahmenart());
		assertEquals(ANTWORT_DATUM_NEU, updatedUeb.getMassnahmen().iterator().next().getAntwort());
		assertEquals(FORMULARKENNUNG_NEU, updatedUeb.getMassnahmen().iterator().next().getVersion()
		    .getFormular());
		assertEquals(MAJOR_NEU, updatedUeb.getMassnahmen().iterator().next().getVersion()
		    .getMajor());
		assertEquals(SUB_NEU, updatedUeb.getMassnahmen().iterator().next().getVersion().getSub());
		assertEquals(3, updatedUeb.getMassnahmen().iterator().next().getStrecke().size());
		Iterator<Strecke> it = ueb.getMassnahmen().iterator().next().getStrecke().iterator();
		Strecke s_3 = it.next();
		assertEquals(STARTBST3, s_3.getStartbst());
		Strecke s_1 = it.next();
		assertEquals(ZEITRAUM_UNTERBR1_NEU, s_1.getZeitraumUnterbrochen());
		assertEquals(3, updatedUeb.getMassnahmen().iterator().next().getZug().size());
		assertEquals(LAUFENDENR1, updatedUeb.getMassnahmen().iterator().next().getZug().get(0)
		    .getLaufendeNr());
		assertEquals(LAUFENDENR2, updatedUeb.getMassnahmen().iterator().next().getZug().get(1)
		    .getLaufendeNr());
		assertEquals(LAUFENDENR3, updatedUeb.getMassnahmen().iterator().next().getZug().get(2)
		    .getLaufendeNr());
		assertEquals(ZUGNR1_NEU, updatedUeb.getMassnahmen().iterator().next().getZug().get(0)
		    .getZugnr());
		assertEquals(BEDARF1_NEU, updatedUeb.getMassnahmen().iterator().next().getZug().get(0)
		    .getBedarf());
		assertEquals(VERKEHRSTAG1_NEU, updatedUeb.getMassnahmen().iterator().next().getZug().get(0)
		    .getVerkehrstag());
	}

	@Test
	public void testDelete() {
		service.delete(ueb);
		Uebergabeblatt emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}

	private static Integer createUebergabeblatt() {
		Uebergabeblatt ueb = new Uebergabeblatt();

		Header h = new Header();
		h.setTimestamp(TIMESTAMP);
		h.setFilename(FILENAME);
		h.setVersionPrgZvf(VERSION_PRG_ZVF);
		Sender s = new Sender();
		s.setName(SENDER_NAME);
		h.setSender(s);
		Set<String> emp = new HashSet<String>();
		emp.add(EMPFAENGER1);
		emp.add(EMPFAENGER2);
		h.setEmpfaenger(emp);
		ueb.setHeader(h);

		Massnahme m = new Massnahme();
		m.setMasterniederlassung(MASTERNIEDERLASSUNG);
		m.setBaumassnahmenart(ART);
		m.setAntwort(ANTWORT_DATUM);
		m.setFestgelegtSPFV(FESTGELEGT_SPFV);
		Version v = new Version();
		v.setFormular(FORMULARKENNUNG);
		v.setMajor(MAJOR);
		v.setEndStueckZvf(ENDSTUECK_ZVF);
		v.setSub(SUB);
		m.setVersion(v);
		Strecke s1 = new Strecke();
		s1.setStartbst(STARTBST1);
		s1.setBaubeginn(BAUBEGINN1);
		s1.setZeitraumUnterbrochen(ZEITRAUM_UNTERBR1);
		Strecke s2 = new Strecke();
		s2.setStartbst(STARTBST2);
		List<Strecke> strecken = new ArrayList<Strecke>();
		strecken.add(s1);
		strecken.add(s2);
		m.setStrecke(strecken);
		List<String> allgRegelungen = new ArrayList<String>();
		allgRegelungen.add(ALLG_REGELUNG1);
		allgRegelungen.add(ALLG_REGELUNG2);
		m.setAllgregelungen(allgRegelungen);

		Niederlassung n1 = new Niederlassung();
		RegionalbereichService rbService = new RegionalbereichServiceImpl();
		n1.setRegionalbereich(rbService.findByName(RB1).get(0));
		n1.setBeteiligt(BETEILIGT1);
		Niederlassung n2 = new Niederlassung();
		n2.setRegionalbereich(rbService.findByName(RB2).get(0));
		n2.setBeteiligt(BETEILIGT2);
		List<Niederlassung> nl = new ArrayList<Niederlassung>();
		nl.add(n1);
		nl.add(n2);
		Fplonr fplo = new Fplonr();
		fplo.setNiederlassungen(nl);
		m.setFplonr(fplo);

		Zug z1 = new Zug();
		// ATTRIBUTE
		z1.setZugnr(ZUGNR1);
		z1.setBedarf(BEDARF1);
		z1.setVerkehrstag(VERKEHRSTAG1);
		z1.setZugbez(ZUGBEZ1);
		// REGELWEG
		Bahnhof b1 = new Bahnhof();
		b1.setLangName(BHF_LANG1);
		b1.setDs100(BHF_DS1);
		Bahnhof b2 = new Bahnhof();
		b2.setLangName(BHF_LANG2);
		b2.setDs100(BHF_DS2);
		Regelweg rw = new Regelweg();
		rw.setAbgangsbahnhof(b1);
		rw.setZielbahnhof(b2);
		z1.setRegelweg(rw);
		// Abweichung
		Abweichung a = new Abweichung();
		a.setArt(UMLEITUNG);
		List<Halt> hl = new ArrayList<Halt>();
		Halt h1 = new Halt();
		h1.setAusfall(b1);
		// Bahnhof b3 = new Bahnhof();
		// b3.setLangName(BHF_LANG2);
		// b3.setDs100(BHF_DS2);
		// h1.setErsatz(b3);
		hl.add(h1);
		a.setHalt(hl);
		z1.setAbweichung(a);
		// ZUGDETAILS
		Zugdetails z1Details = new Zugdetails();
		z1Details.setGattungsnummer(GATTUNGSNR);
		z1Details.setLaenge(LAENGE);
		Tfz tfz = new Tfz();
		tfz.setLzb(LZB);
		z1Details.setTfz(tfz);
		z1.setZugdetails(z1Details);
		Zug z2 = new Zug();
		z2.setZugnr(ZUGNR2);
		m.addZug(z1);
		m.addZug(z2);
		// Knotenzeiten
		List<Knotenzeit> kn = new ArrayList<Knotenzeit>();
		Knotenzeit k1 = new Knotenzeit();
		k1.setAbfahrt(ABFAHRT_DATUM);
		k1.setHaltart(HALT_B);
		kn.add(k1);
		z1.setKnotenzeiten(kn);

		Set<Massnahme> massnahmen = new HashSet<Massnahme>();
		massnahmen.add(m);
		ueb.setMassnahmen(massnahmen);

		UebergabeblattService service = new UebergabeblattServiceImpl();
		service.create(ueb);
		return ueb.getId();
	}
}
