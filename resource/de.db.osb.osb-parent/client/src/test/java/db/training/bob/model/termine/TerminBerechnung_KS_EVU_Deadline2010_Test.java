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
 * EVU für Baumassnahmen mit Baubeginn ab 2010-01-01
 */
public class TerminBerechnung_KS_EVU_Deadline2010_Test {

	private Date STARTTERMIN = new GregorianCalendar(2010, GregorianCalendar.SEPTEMBER, 15)
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
	public void testCalculateZvF() {
		final Date ZVF = new GregorianCalendar(2010, GregorianCalendar.JUNE, 2).getTime();

		meilenstein.setWochentag(4);
		meilenstein.setAnzahlWochenVorBaubeginn(15);
		meilenstein.setMindestfrist(false);

		assertEquals(ZVF, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateUeB_PV() {
		final Date UEB_PV = new GregorianCalendar(2010, GregorianCalendar.JUNE, 4).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(15);
		meilenstein.setMindestfrist(false);

		assertEquals(UEB_PV, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}

	@Test
	public void testCalculateUeB_GV() {
		final Date UEB_GV = new GregorianCalendar(2010, GregorianCalendar.JUNE, 4).getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(15);
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
	public void testCalculateEingabeGFDZ() {
		final Date EINGABE_GFDZ = new GregorianCalendar(2010, GregorianCalendar.SEPTEMBER, 10)
		    .getTime();

		meilenstein.setWochentag(6);
		meilenstein.setAnzahlWochenVorBaubeginn(1);
		meilenstein.setMindestfrist(false);

		assertEquals(EINGABE_GFDZ, Terminberechnung.calculateSolltermin(STARTTERMIN, meilenstein));
	}
}
