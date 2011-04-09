package db.training.bob.model.termine;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.Meilenstein;
import db.training.bob.service.Terminberechnung;

/**
 * Test für die Berechnung von Soll-Terminen bei BOB-Baumassnahmen mit Art = A für Schnittstelle EVU
 * für Baumassnahmen mit Baubeginn ab 2010-01-01
 * 
 */
public class TerminBerechnung_A_EVU_Deadline2010_Test {

	private final Date STARTTERMIN = new GregorianCalendar(2010, GregorianCalendar.SEPTEMBER, 15)
	    .getTime();

	private Meilenstein meilenstein = null;

	@Before
	public void setUp() {
		meilenstein = new Meilenstein();
	}

	@Test
	public void testCalculateZvFEntwurf() {
		final Date ZVF_ENTWURF = new GregorianCalendar(2010, GregorianCalendar.MARCH, 31).getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(24);
		meilenstein.setMindestfrist(false);

		assertEquals(ZVF_ENTWURF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateStellungnahmeEVU() {
		final Date STELLUNGNAHME_EVU = new GregorianCalendar(2010, GregorianCalendar.APRIL, 21)
		    .getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(21);
		meilenstein.setMindestfrist(false);

		assertEquals(STELLUNGNAHME_EVU, Terminberechnung.calculateSolltermin(STARTTERMIN,
		    meilenstein));
	}

	@Test
	public void testCalculateGesamtkonzeptBBZR() {
		final Date GESAMTKONZEPT_BBZR = new GregorianCalendar(2010, GregorianCalendar.MAY, 5)
		    .getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(19);
		meilenstein.setMindestfrist(false);

		assertEquals(GESAMTKONZEPT_BBZR, Terminberechnung.calculateSolltermin(STARTTERMIN,
		    meilenstein));
	}

	@Test
	public void testCalculateZvF() {
		final Date ZVF = new GregorianCalendar(2010, GregorianCalendar.JUNE, 2).getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(15);
		meilenstein.setMindestfrist(false);

		assertEquals(ZVF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateMasterUebergabeblattPV() {
		final Date MASTERUEB_PV = new GregorianCalendar(2010, GregorianCalendar.JUNE, 25).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(12);
		meilenstein.setMindestfrist(false);

		assertEquals(MASTERUEB_PV, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateMasterUebergabeblattGV() {
		final Date MASTERUEB_GV = new GregorianCalendar(2010, GregorianCalendar.JULY, 9).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(10);
		meilenstein.setMindestfrist(false);

		assertEquals(MASTERUEB_GV, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateUebergabeblattPV() {
		final Date UEB_PV = new GregorianCalendar(2010, GregorianCalendar.JULY, 23).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(8);
		meilenstein.setMindestfrist(false);

		assertEquals(UEB_PV, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateUebergabeblattGV() {
		final Date UEB_GV = new GregorianCalendar(2010, GregorianCalendar.AUGUST, 6).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(6);
		meilenstein.setMindestfrist(false);

		assertEquals(UEB_GV, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateFplo() {
		final Date FPLO = new GregorianCalendar(2010, GregorianCalendar.SEPTEMBER, 3).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(2);
		meilenstein.setMindestfrist(false);

		assertEquals(FPLO, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateEingabeGfdz() {
		final Date EINGABE_GFDZ = new GregorianCalendar(2010, GregorianCalendar.SEPTEMBER, 10)
		    .getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(1);
		meilenstein.setMindestfrist(false);

		assertEquals(EINGABE_GFDZ, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}
}
