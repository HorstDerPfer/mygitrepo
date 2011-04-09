package db.training.bob.model.termine;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.Meilenstein;
import db.training.bob.service.Terminberechnung;

/**
 * Test für die Berechnung von Soll-Terminen bei BOB-Baumassnahmen mit Art = KS für Schnittstelle
 * Baubetriebsplanung für Baumassnahmen mit Baubeginn ab 2010-01-01
 * 
 */
public class TerminBerechnung_KS_Baubetriebsplanung_Deadline2010_Test {

	private final Date STARTTERMIN = new GregorianCalendar(2010, GregorianCalendar.SEPTEMBER, 15)
	    .getTime();

	private Meilenstein meilenstein = null;

	@Before
	public void setUp() {
		meilenstein = new Meilenstein();
	}

	@Test
	public void testCalculateAnforderungBBZR() {
		final Date ANFORDERUNG_BBZR = new GregorianCalendar(2010, GregorianCalendar.FEBRUARY, 22)
		    .getTime();

		meilenstein.setWochentag(2);
		meilenstein.setAnzahlWochenVorBaubeginn(29);
		meilenstein.setMindestfrist(false);

		assertEquals(ANFORDERUNG_BBZR, Terminberechnung.calculateSolltermin(STARTTERMIN,
		    meilenstein));
	}

	@Test
	public void testCalculateBiUeEntwurf() {
		final Date BIUE_ENTWURF = new GregorianCalendar(2010, GregorianCalendar.MARCH, 17)
		    .getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(26);
		meilenstein.setMindestfrist(false);

		assertEquals(BIUE_ENTWURF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateZvFEntwurf() {
		final Date ZVF_ENTWURF = new GregorianCalendar(2010, GregorianCalendar.MARCH, 17).getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(26);
		meilenstein.setMindestfrist(false);

		assertEquals(ZVF_ENTWURF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateBiUe() {
		final Date BIUE = new GregorianCalendar(2010, GregorianCalendar.JUNE, 2).getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(15);
		meilenstein.setMindestfrist(false);

		assertEquals(BIUE, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateZvF() {
		final Date ZVF = new GregorianCalendar(2010, GregorianCalendar.JUNE, 2).getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(15);
		meilenstein.setMindestfrist(false);

		assertEquals(ZVF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

}
