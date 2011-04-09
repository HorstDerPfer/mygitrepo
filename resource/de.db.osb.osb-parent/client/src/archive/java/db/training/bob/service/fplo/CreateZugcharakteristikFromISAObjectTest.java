package db.training.bob.service.fplo;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.fplo.ISA_Bestellung;
import db.training.bob.model.fplo.ISA_Fahrplan;
import db.training.bob.model.fplo.ISA_Zug;
import db.training.bob.model.fplo.Zugcharakteristik;

public class CreateZugcharakteristikFromISAObjectTest {

	private ISA_Zug isaZug = null;

	private Zugcharakteristik zug = null;

	private ZugcharakteristikService service = null;

	private final Integer ID_ZUG = 1;

	private final Date DATUM_KONSTRUKTION = new GregorianCalendar(2009, GregorianCalendar.AUGUST,
	    20).getTime();

	private final Date VERKEHRSTAG = new GregorianCalendar(2009, GregorianCalendar.AUGUST, 21)
	    .getTime();

	private final String GATTUNG = "123 - ABC";

	private final String GATTUNGSBEZEICHNUNG = "ABC";

	private final String GATTUNGSNR = "12.3";

	private final String STARTBF = "ABCDE";

	private final String ZIELBF = "FGHIJ";

	private final String ZUGNR = "12345";

	private final Integer ZUGNR_INT = 12345;

	private final String TAGWECHSEL = "0";

	private final String KUNDENNR = "A1234";

	@Before
	public void setUp() {
		isaZug = new ISA_Zug();
		service = new ZugcharakteristikServiceImpl();
		service.setFahrplanService(new FahrplanServiceImpl());
	}

	@Test
	public void testCreateZugcharakteristikIdZug() {
		isaZug.setIdZug(ID_ZUG);
		zug = service.createFromISAObject(isaZug);
		assertEquals(ID_ZUG, zug.getIdZug());
	}

	@Test
	public void testCreateZugcharakteristikDatumKonstruktion() {
		isaZug.setDatumKonstruktion(DATUM_KONSTRUKTION);
		zug = service.createFromISAObject(isaZug);
		assertEquals(DATUM_KONSTRUKTION, zug.getDatumKonstruktion());
	}

	@Test
	public void testCreateZugcharakteristikVerkehrstag() {
		isaZug.setErstervtag(VERKEHRSTAG);
		zug = service.createFromISAObject(isaZug);
		assertEquals(VERKEHRSTAG, zug.getVerkehrstag());
	}

	@Test
	public void testCreateZugcharakteristikGattungsbezeichnung() {
		isaZug.setGattung(GATTUNG);
		zug = service.createFromISAObject(isaZug);
		assertEquals(GATTUNGSBEZEICHNUNG, zug.getGattungsbezeichnung());
	}

	@Test
	public void testCreateZugcharakteristikGattungsnr() {
		isaZug.setGattung(GATTUNG);
		zug = service.createFromISAObject(isaZug);
		assertEquals(GATTUNGSNR, zug.getGattungsnr());
	}

	@Test
	public void testCreateZugcharakteristikGattungsbezeichnungEmpty() {
		isaZug.setGattung("");
		zug = service.createFromISAObject(isaZug);
		assertEquals("", zug.getGattungsbezeichnung());
	}

	@Test
	public void testCreateZugcharakteristikGattungsnrEmpty() {
		isaZug.setGattung("");
		zug = service.createFromISAObject(isaZug);
		assertEquals("", zug.getGattungsnr());
	}

	@Test
	public void testCreateZugcharakteristikGattungsbezeichnungMinus() {
		isaZug.setGattung("-");
		zug = service.createFromISAObject(isaZug);
		assertEquals("", zug.getGattungsbezeichnung());
	}

	@Test
	public void testCreateZugcharakteristikGattungsnrMinus() {
		isaZug.setGattung("-");
		zug = service.createFromISAObject(isaZug);
		assertEquals("", zug.getGattungsnr());
	}

	@Test
	public void testCreateZugcharakteristikStartBf() {
		isaZug.setStartbf(STARTBF);
		zug = service.createFromISAObject(isaZug);
		assertEquals(STARTBF, zug.getStartBf());
	}

	@Test
	public void testCreateZugcharakteristikZielBf() {
		isaZug.setZielbf(ZIELBF);
		zug = service.createFromISAObject(isaZug);
		assertEquals(ZIELBF, zug.getZielBf());
	}

	@Test
	public void testCreateZugcharakteristikZugnr() {
		isaZug.setZugnummer(ZUGNR);
		zug = service.createFromISAObject(isaZug);
		assertEquals(ZUGNR_INT, zug.getZugnr());
	}

	@Test
	public void testCreateZugcharakteristikZugnrInValid() {
		isaZug.setZugnummer("A");
		zug = service.createFromISAObject(isaZug);
		assertEquals(null, zug.getZugnr());
	}

	@Test
	public void testCreateZugcharakteristikTagwechsel() {
		isaZug.setTagwechsel(TAGWECHSEL);
		zug = service.createFromISAObject(isaZug);
		assertEquals(TAGWECHSEL, zug.getTagwechsel());
	}

	@Test
	public void testCreateZugcharakteristikKundennr() {
		ISA_Bestellung bestellung = new ISA_Bestellung();
		bestellung.setKundennr(KUNDENNR);
		isaZug.setBestellung(bestellung);
		zug = service.createFromISAObject(isaZug);
		assertEquals(KUNDENNR, zug.getKundennummer());
	}

	@Test
	public void testCreateZugcharakteristikFahrplanNull() {
		Set<ISA_Fahrplan> fahrplan = new HashSet<ISA_Fahrplan>();
		isaZug.setFahrplan(fahrplan);
		zug = service.createFromISAObject(isaZug);
		assertEquals(0, zug.getFahrplan().size());
	}

	@Test
	public void testCreateZugcharakteristikFahrplan() {
		ISA_Fahrplan isaFahrplan = new ISA_Fahrplan();
		Set<ISA_Fahrplan> fahrplan = new HashSet<ISA_Fahrplan>();
		fahrplan.add(isaFahrplan);
		isaZug.setFahrplan(fahrplan);
		zug = service.createFromISAObject(isaZug);
		assertEquals(1, zug.getFahrplan().size());
	}
}
