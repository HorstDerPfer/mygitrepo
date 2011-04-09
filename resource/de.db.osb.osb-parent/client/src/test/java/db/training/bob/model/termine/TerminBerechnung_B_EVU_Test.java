package db.training.bob.model.termine;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.Meilenstein;
import db.training.bob.service.Terminberechnung;

/**
 * Test für die Berechnung von Soll-Terminen bei BOB-Baumassnahmen mit Art = B für Schnittstelle EVU
 * für Baumassnahmen mit Baubeginn vor 2010-01-01
 */
public class TerminBerechnung_B_EVU_Test {

	private Date STARTTERMIN = new GregorianCalendar(2009, GregorianCalendar.SEPTEMBER, 15)
	    .getTime();

	private Meilenstein meilenstein = null;

	@Before
	public void setUp() {
		meilenstein = new Meilenstein();
	}

	@Test
	public void testCalculateZvFEntwurf() {
		final Date ZVF_ENTWURF = new GregorianCalendar(2009, GregorianCalendar.JULY, 6).getTime();

		meilenstein.setWochentag(2);
		meilenstein.setAnzahlWochenVorBaubeginn(10);
		meilenstein.setMindestfrist(false);

		assertEquals(ZVF_ENTWURF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateStellungnahmeEVU() {
		final Date STELLUNGNAHME_EVU = new GregorianCalendar(2009, GregorianCalendar.JULY, 27)
		    .getTime();

		meilenstein.setWochentag(2);
		meilenstein.setAnzahlWochenVorBaubeginn(7);
		meilenstein.setMindestfrist(false);

		assertEquals(STELLUNGNAHME_EVU, Terminberechnung.calculateSolltermin(STARTTERMIN,
		    meilenstein));
	}

	@Test
	public void testCalculateZvF() {
		final Date ZVF = new GregorianCalendar(2009, GregorianCalendar.JULY, 27).getTime();

		meilenstein.setWochentag(2);
		meilenstein.setAnzahlWochenVorBaubeginn(7);
		meilenstein.setMindestfrist(false);

		assertEquals(ZVF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateUeB_PV() {
		final Date UEB_PV = new GregorianCalendar(2009, GregorianCalendar.AUGUST, 7).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(6);
		meilenstein.setMindestfrist(false);

		assertEquals(UEB_PV, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateUeB_GV() {
		final Date UEB_GV = new GregorianCalendar(2009, GregorianCalendar.AUGUST, 7).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(6);
		meilenstein.setMindestfrist(false);

		assertEquals(UEB_GV, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateFplo() {
		final Date FPLO = new GregorianCalendar(2009, GregorianCalendar.SEPTEMBER, 4).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(2);
		meilenstein.setMindestfrist(false);

		assertEquals(FPLO, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateEingabeGFDZ() {
		final Date EINGABE_GFDZ = new GregorianCalendar(2009, GregorianCalendar.SEPTEMBER, 11)
		    .getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(1);
		meilenstein.setMindestfrist(false);

		assertEquals(EINGABE_GFDZ, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}
}
