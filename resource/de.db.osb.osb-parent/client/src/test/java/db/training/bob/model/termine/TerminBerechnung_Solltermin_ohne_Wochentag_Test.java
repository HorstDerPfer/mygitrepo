package db.training.bob.model.termine;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.Meilenstein;
import db.training.bob.service.Terminberechnung;

/**
 * Test für die Berechnung von Soll-Terminen bei BOB-Baumassnahmen ohne Angabe eines Wochentags beim
 * Soll-Termin
 * 
 */
public class TerminBerechnung_Solltermin_ohne_Wochentag_Test {

	private Meilenstein meilenstein = null;

	@Before
	public void setUp() {
		meilenstein = new Meilenstein();
	}

	@Test
	public void testCalculateNotWeekend() {
		final Date STARTTERMIN = new GregorianCalendar(2010, GregorianCalendar.OCTOBER, 21)
		    .getTime();
		final Date SOLLTERMIN = new GregorianCalendar(2010, GregorianCalendar.OCTOBER, 7).getTime();

		meilenstein.setWochentag(0);
		meilenstein.setAnzahlWochenVorBaubeginn(2);
		meilenstein.setMindestfrist(false);

		assertEquals(SOLLTERMIN, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateWeekend() {
		final Date STARTTERMIN = new GregorianCalendar(2010, GregorianCalendar.OCTOBER, 24)
		    .getTime();
		final Date SOLLTERMIN = new GregorianCalendar(2010, GregorianCalendar.OCTOBER, 8).getTime();

		meilenstein.setWochentag(0);
		meilenstein.setAnzahlWochenVorBaubeginn(2);
		meilenstein.setMindestfrist(false);

		assertEquals(SOLLTERMIN, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateWeekendMindestfrist() {
		final Date STARTTERMIN = new GregorianCalendar(2010, GregorianCalendar.OCTOBER, 24)
		    .getTime();
		final Date SOLLTERMIN = new GregorianCalendar(2010, GregorianCalendar.OCTOBER, 8).getTime();

		meilenstein.setWochentag(0);
		meilenstein.setAnzahlWochenVorBaubeginn(2);
		meilenstein.setMindestfrist(true);

		assertEquals(SOLLTERMIN, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateNotWeekend2() {
		final Date STARTTERMIN = new GregorianCalendar(2008, GregorianCalendar.MARCH, 27).getTime();
		final Date SOLLTERMIN = new GregorianCalendar(2007, GregorianCalendar.SEPTEMBER, 27)
		    .getTime();

		meilenstein.setWochentag(0);
		meilenstein.setAnzahlWochenVorBaubeginn(26);
		meilenstein.setMindestfrist(false);

		assertEquals(SOLLTERMIN, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}
}
