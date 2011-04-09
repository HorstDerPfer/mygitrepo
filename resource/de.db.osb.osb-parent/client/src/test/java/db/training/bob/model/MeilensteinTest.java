package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests für "Flexible Stammdatenbearbeitung der Soll-Termine für Meilensteine" (Ticket 267)
 * 
 * @author gergs
 * 
 */
public class MeilensteinTest {

	private Meilenstein meilenstein;

	@Before
	public void setUp() throws Exception {
		meilenstein = new Meilenstein();
		meilenstein.setArt(Art.A);
		meilenstein.setSchnittstelle(Schnittstelle.BBP);
		meilenstein.setBezeichnung(Meilensteinbezeichnungen.ZVF);
		meilenstein.setAnzahlWochenVorBaubeginn(8);
		meilenstein.setWochentag(GregorianCalendar.WEDNESDAY);
		meilenstein.setMindestfrist(true);
		meilenstein.setGueltigAbBaubeginn(new GregorianCalendar(2010, GregorianCalendar.MAY, 20)
		    .getTime());
	}

	@Test
	public void testArt() {
		assertEquals(Art.A, meilenstein.getArt());
	}

	@Test
	public void testSchnittstelle() {
		assertEquals(Schnittstelle.BBP, meilenstein.getSchnittstelle());
	}

	@Test
	public void testBezeichnung() {
		assertEquals(Meilensteinbezeichnungen.ZVF, meilenstein.getBezeichnung());
	}

	@Test
	public void testAnzahlWochenVorBaubeginn() {
		assertEquals(8, meilenstein.getAnzahlWochenVorBaubeginn());
	}

	@Test
	public void testWochentag() {
		assertEquals(GregorianCalendar.WEDNESDAY, meilenstein.getWochentag());
	}

	@Test
	public void testMindestfrist() {
		assertEquals(true, meilenstein.isMindestfrist());
	}

	@Test
	public void testGueltigAbBaubeginn() {
		assertEquals(new GregorianCalendar(2010, GregorianCalendar.MAY, 20).getTime(), meilenstein
		    .getGueltigAbBaubeginn());
	}

}
