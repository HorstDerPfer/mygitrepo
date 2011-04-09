package db.training.bob.model.termine;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.StatusType;
import db.training.bob.service.Terminberechnung;

/**
 * Test für die Berechnungen des Terminstatus bei BOB-Baumassnahmen
 * 
 */
public class TerminBerechnungStatusTest {

	private GregorianCalendar TODAY = new GregorianCalendar();

	private GregorianCalendar ist;

	private GregorianCalendar soll;

	@Before
	public void setUp() {
		soll = new GregorianCalendar();
		soll.add(GregorianCalendar.HOUR, 1); //leave some time to accomplish tasks
	}

	@Test
	public void testGetStatusNeutral() {
		assertEquals(StatusType.NEUTRAL, Terminberechnung.getStatus(null, null, false));
	}

	@Test
	public void testGetStatusOffen() {
		soll.add(GregorianCalendar.DAY_OF_MONTH, 15);

		assertEquals(StatusType.OFFEN, Terminberechnung.getStatus(soll.getTime(), null, true));
	}

//	@Test
//	public void testGetStatusCountdown14_14DaysBefore() {
//		soll.add(GregorianCalendar.DAY_OF_MONTH, 14);
//
//		assertEquals(StatusType.COUNTDOWN14, Terminberechnung.getStatus(soll.getTime(), null, true));
//	}

	@Test
	public void testGetStatusCountdown14_1DayBefore() {
		soll.add(GregorianCalendar.DAY_OF_MONTH, 1);

		assertEquals(StatusType.COUNTDOWN14, Terminberechnung.getStatus(soll.getTime(), null, true));
	}

	@Test
	public void testGetStatusCountdown14_0DaysBefore() {
		assertEquals(StatusType.COUNTDOWN14, Terminberechnung.getStatus(soll.getTime(), null, true));
	}

	@Test
	public void testGetStatusGreenBefore() {
		soll.add(GregorianCalendar.DAY_OF_MONTH, 1);
		ist = TODAY;

		assertEquals(StatusType.GREEN, Terminberechnung.getStatus(soll.getTime(), ist.getTime(),
		    true));
	}

	@Test
	public void testGetStatusGreenToday() {
		ist = TODAY;

		assertEquals(StatusType.GREEN, Terminberechnung.getStatus(soll.getTime(), ist.getTime(),
		    true));
	}

	@Test
	public void testGetStatusLila() {
		soll.add(GregorianCalendar.DAY_OF_MONTH, -1);

		assertEquals(StatusType.LILA, Terminberechnung.getStatus(soll.getTime(), null, true));
	}

	@Test
	public void testGetStatusRed() {
		soll.add(GregorianCalendar.DAY_OF_MONTH, -1);
		ist = TODAY;

		assertEquals(StatusType.RED, Terminberechnung
		    .getStatus(soll.getTime(), ist.getTime(), true));
	}
}
