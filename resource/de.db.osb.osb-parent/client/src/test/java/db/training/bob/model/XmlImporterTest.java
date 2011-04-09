package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXNotRecognizedException;

public class XmlImporterTest {

	private static final String TEST_DIRECTORY = "target/test-classes/db/training/bob/model/testdata";
	
	private static final String MAS_ID1 = "0801B4E7D3221";

	private static final Date MAS_BEGINN1 = new GregorianCalendar(2008, Calendar.MAY, 31, 22, 45)
	    .getTime();

	private static final Date MAS_ENDE1 = new GregorianCalendar(2008, Calendar.JUNE, 1, 14, 45)
	    .getTime();

	private static final String MAS_BST_VON_LANG1 = "Neuhof Nord Bft";

	private static final String MAS_BST_BIS_LANG1 = "Flieden";

	private static final String MAS_ARBEITEN1 = "Brückenarbeiten Einbau Hbr Fliedebrücke Gl. 152";

	private static final String MAS_STRECKE_BBP1 = "44018";

	private static final String MAS_STRECKE_VZG1 = "3600";

	private static final Regionalbereich MAS_REGIONALBEREICH1 = new Regionalbereich("Mitte");

	private static final String MAS_ID2 = "07F4BEE23D341";

	private static final Date MAS_BEGINN2 = new GregorianCalendar(2008, Calendar.MAY, 31, 14, 5)
	    .getTime();

	private static final Date MAS_ENDE2 = new GregorianCalendar(2008, Calendar.JUNE, 2, 5, 0)
	    .getTime();

	private static final String MAS_BST_VON_LANG2 = "Ffm Süd Abstbf. Bft";

	private static final String MAS_BST_BIS_LANG2 = "Ffm Süd";

	private static final String MAS_ARBEITEN2 = "Durcharbeitung von Weichen nach WE 210,212,216,217,230,242 in Ffm Süd Abstbf. Bft";

	private static final String MAS_STRECKE_BBP2 = "44023";

	private static final String MAS_STRECKE_VZG2 = "3606";

	private static final Regionalbereich MAS_REGIONALBEREICH2 = new Regionalbereich("Mitte");

	private static final String MAS_ID3 = "08219157E6D02";

	private static final Date MAS_BEGINN3 = new GregorianCalendar(2008, Calendar.JUNE, 7, 23, 30)
	    .getTime();

	private static final Date MAS_ENDE3 = new GregorianCalendar(2008, Calendar.JUNE, 8, 9, 30)
	    .getTime();

	private static final String MAS_BST_VON_LANG3 = "Ffm Stadion";

	private static final String MAS_BST_BIS_LANG3 = "Ffm Stadion";

	private static final String MAS_ARBEITEN3 = "Inbetriebnahme ESTW FSP BS II in Ffm Stadion";

	private static final String MAS_STRECKE_BBP3 = "44004";

	private static final String MAS_STRECKE_VZG3 = "3520";

	private static final Regionalbereich MAS_REGIONALBEREICH3 = new Regionalbereich("Mitte");

	private static final String REG_ID = "0801B4E7D3221-0001";

	private static final Date REG_BEGINN = new GregorianCalendar(2008, Calendar.MAY, 31, 22, 45)
	    .getTime();

	private static final Date REG_ENDE = new GregorianCalendar(2008, Calendar.JUNE, 1, 14, 45)
	    .getTime();

	private static final String REG_BST_VON = "Neuhof(Kr Fulda)";

	private static final String REG_BST_BIS = "Neuhof Süd Üst";

	private static final String REG_BETRIEBSWEISE = "Befahren Ggl auf Zs 6/7 ständig";

	private static final String REG_STRECKE_BBP = "44018";

	private static final String REG_STRECKE_VZG = "3600";

	private static final SperrKz REG_SPERRKZ = SperrKz.DURCHGEHEND;

	private static final Integer REG_REGEL_VTS = 12700;

	private static final String REG_BPL_ART_TEXT = "A(J)";

	private static final String REG_BEMERKUNG_BPL = "";

	private static File BBP_XML_FILE = null;

	private XmlImporter xmlImporter;

	@Before
	public void setUp() throws Exception {
		BBP_XML_FILE = new File(TEST_DIRECTORY, "BBP_Export_3.XML");
		xmlImporter = new XmlImporter(new FileInputStream(BBP_XML_FILE));
	}

	@Test
	public void testNumberOfBBPMassnahmen() throws SAXNotRecognizedException {
		assertEquals(3, xmlImporter.getBBPMassnahmen().size());
	}

	@Test
	public void testNumberOfRegelungen() throws SAXNotRecognizedException {
		BBPMassnahme[] bbpArray = new BBPMassnahme[3];
		bbpArray = xmlImporter.getBBPMassnahmen().toArray(bbpArray);
		assertEquals(2, bbpArray[0].getRegelungen().size());
		assertEquals(4, bbpArray[1].getRegelungen().size());
		assertEquals(5, bbpArray[2].getRegelungen().size());
	}

	@Test
	public void testImportBBP1Data() throws Exception {
		BBPMassnahme[] bbpArray = new BBPMassnahme[3];
		bbpArray = xmlImporter.getBBPMassnahmen().toArray(bbpArray);
		assertEquals(MAS_ID1, bbpArray[0].getMasId());
		assertEquals(MAS_BEGINN1, bbpArray[0].getBeginn());
		assertEquals(MAS_ENDE1, bbpArray[0].getEnde());
		assertEquals(MAS_BST_VON_LANG1, bbpArray[0].getBstVonLang());
		assertEquals(MAS_BST_BIS_LANG1, bbpArray[0].getBstBisLang());
		assertEquals(MAS_ARBEITEN1, bbpArray[0].getArbeiten());
		assertEquals(MAS_STRECKE_BBP1, bbpArray[0].getStreckeBBP());
		assertEquals(MAS_STRECKE_VZG1, bbpArray[0].getStreckeVZG());
		assertEquals(MAS_REGIONALBEREICH1.getName(), bbpArray[0].getRegionalbereich());
	}

	@Test
	public void testImportBBP2Data() throws Exception {
		BBPMassnahme[] bbpArray = new BBPMassnahme[3];
		bbpArray = xmlImporter.getBBPMassnahmen().toArray(bbpArray);
		assertEquals(MAS_ID2, bbpArray[1].getMasId());
		assertEquals(MAS_BEGINN2, bbpArray[1].getBeginn());
		assertEquals(MAS_ENDE2, bbpArray[1].getEnde());
		assertEquals(MAS_BST_VON_LANG2, bbpArray[1].getBstVonLang());
		assertEquals(MAS_BST_BIS_LANG2, bbpArray[1].getBstBisLang());
		assertEquals(MAS_ARBEITEN2, bbpArray[1].getArbeiten());
		assertEquals(MAS_STRECKE_BBP2, bbpArray[1].getStreckeBBP());
		assertEquals(MAS_STRECKE_VZG2, bbpArray[1].getStreckeVZG());
		assertEquals(MAS_REGIONALBEREICH2.getName(), bbpArray[1].getRegionalbereich());
	}

	@Test
	public void testImportBBP3Data() throws Exception {
		BBPMassnahme[] bbpArray = new BBPMassnahme[3];
		bbpArray = xmlImporter.getBBPMassnahmen().toArray(bbpArray);
		assertEquals(MAS_ID3, bbpArray[2].getMasId());
		assertEquals(MAS_BEGINN3, bbpArray[2].getBeginn());
		assertEquals(MAS_ENDE3, bbpArray[2].getEnde());
		assertEquals(MAS_BST_VON_LANG3, bbpArray[2].getBstVonLang());
		assertEquals(MAS_BST_BIS_LANG3, bbpArray[2].getBstBisLang());
		assertEquals(MAS_ARBEITEN3, bbpArray[2].getArbeiten());
		assertEquals(MAS_STRECKE_BBP3, bbpArray[2].getStreckeBBP());
		assertEquals(MAS_STRECKE_VZG3, bbpArray[2].getStreckeVZG());
		assertEquals(MAS_REGIONALBEREICH3.getName(), bbpArray[2].getRegionalbereich());
	}

	@Test
	public void testImportRegelungenData() throws Exception {
		BBPMassnahme[] bbpArray = new BBPMassnahme[3];

		bbpArray = xmlImporter.getBBPMassnahmen().toArray(bbpArray);

		Set<Regelung> regelungen = bbpArray[0].getRegelungen();
		assertEquals(2, regelungen.size());

		Regelung[] regelungenArray = new Regelung[2];
		regelungenArray = regelungen.toArray(regelungenArray);
		Regelung regelung1 = regelungenArray[0];
		assertEquals(REG_ID, regelung1.getRegelungId());
		assertEquals(REG_BEGINN, regelung1.getBeginn());
		assertEquals(REG_ENDE, regelung1.getEnde());
		assertEquals(REG_BST_VON, regelung1.getBetriebsStelleVon());
		assertEquals(REG_BST_BIS, regelung1.getBetriebsStelleBis());
		assertEquals(REG_BETRIEBSWEISE, regelung1.getBetriebsweise());
		assertEquals(REG_STRECKE_BBP, regelung1.getStreckeBBP());
		assertEquals(REG_STRECKE_VZG, regelung1.getStreckeVZG());
		assertEquals(REG_SPERRKZ, regelung1.getSperrKz());
		assertEquals(REG_REGEL_VTS, regelung1.getRegelVTS());
		assertEquals(REG_BPL_ART_TEXT, regelung1.getBplArtText());
		assertEquals(REG_BEMERKUNG_BPL, regelung1.getBemerkungenBpl());
	}
}
