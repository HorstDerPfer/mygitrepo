package db.training.bob.util;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testSubtractWeeks() {
		GregorianCalendar cal1 = new GregorianCalendar(2010, GregorianCalendar.JANUARY, 10);
		GregorianCalendar expected = new GregorianCalendar(2010, GregorianCalendar.JANUARY, 3);
		cal1 = DateUtils.subtractWeeks(cal1, 1);
		assertEquals(expected, cal1);
	}

	@Test
	public void testSubtractWeeksDifferentYears() {
		GregorianCalendar cal1 = new GregorianCalendar(2010, GregorianCalendar.JANUARY, 10);
		GregorianCalendar expected = new GregorianCalendar(2009, GregorianCalendar.DECEMBER, 27);
		cal1 = DateUtils.subtractWeeks(cal1, 2);
		assertEquals(expected, cal1);
	}

	@Test
	public void testAddWeeks() {
		GregorianCalendar cal1 = new GregorianCalendar(2010, GregorianCalendar.JANUARY, 10);
		GregorianCalendar expected = new GregorianCalendar(2010, GregorianCalendar.JANUARY, 17);
		cal1 = DateUtils.addWeeks(cal1, 1);
		assertEquals(expected, cal1);
	}

	@Test
	public void testAddWeeksDifferentYears() {
		GregorianCalendar cal1 = new GregorianCalendar(2009, GregorianCalendar.DECEMBER, 27);
		GregorianCalendar expected = new GregorianCalendar(2010, GregorianCalendar.JANUARY, 10);
		cal1 = DateUtils.addWeeks(cal1, 2);
		assertEquals(expected, cal1);
	}
}
