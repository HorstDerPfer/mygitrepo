package db.training.easy.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import db.training.logwrapper.Logger;

public class EasyDateFormatTest extends TestCase {

	private static Logger log = Logger.getLogger(EasyDateFormatTest.class);

	public void testGetDay() {
		assertEquals(1, EasyDateFormat.getDayFromKsbDate(1051201));
		assertEquals(11, EasyDateFormat.getDayFromKsbDate(1051211));
		assertEquals(3, EasyDateFormat.getDayFromKsbDate(991203));

	}

	public void testGetMonth() {
		assertEquals(12, EasyDateFormat.getMonthFromKsbDate(1051201));
		assertEquals(11, EasyDateFormat.getMonthFromKsbDate(1051113));
		assertEquals(3, EasyDateFormat.getMonthFromKsbDate(990304));

	}

	public void testKsbConvert() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);

		int ksbHour = EasyDateFormat.convertTimeToKsbHour(calendar);
		assertEquals(-7, ksbHour);

		calendar.set(Calendar.HOUR_OF_DAY, 23);

		ksbHour = EasyDateFormat.convertTimeToKsbHour(calendar);
		assertEquals(16, ksbHour);
	}

	public void testKsbToEasy() {
		Calendar calendar = EasyDateFormat.convertKsbTimeToDate(0, "0600103");
		assertEquals(1960, calendar.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
		assertEquals(3, calendar.get(Calendar.DATE));

		assertEquals(7, calendar.get(Calendar.HOUR_OF_DAY));

		calendar = EasyDateFormat.convertKsbTimeToDate(8, "1600103");
		assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));

	}

	public void testGetFahrplanJahr2008() {
		assertEquals(new GregorianCalendar(2007, GregorianCalendar.DECEMBER, 16).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2008));
		assertEquals(
		    new GregorianCalendar(2008, GregorianCalendar.DECEMBER, 13, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2008));
	}

	public void testGetFahrplanJahr2009() {
		assertEquals(new GregorianCalendar(2008, GregorianCalendar.DECEMBER, 14).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2009));
		assertEquals(
		    new GregorianCalendar(2009, GregorianCalendar.DECEMBER, 12, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2009));
	}

	public void testGetFahrplanJahr2010() {
		assertEquals(new GregorianCalendar(2009, GregorianCalendar.DECEMBER, 13).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2010));
		assertEquals(
		    new GregorianCalendar(2010, GregorianCalendar.DECEMBER, 11, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2010));
	}

	public void testGetFahrplanJahr2011() {
		assertEquals(new GregorianCalendar(2010, GregorianCalendar.DECEMBER, 12).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2011));
		assertEquals(
		    new GregorianCalendar(2011, GregorianCalendar.DECEMBER, 10, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2011));
	}

	public void testGetFahrplanJahr2012() {
		assertEquals(new GregorianCalendar(2011, GregorianCalendar.DECEMBER, 11).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2012));
		assertEquals(
		    new GregorianCalendar(2012, GregorianCalendar.DECEMBER, 15, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2012));
	}

	public void testGetFahrplanJahr2013() {
		assertEquals(new GregorianCalendar(2012, GregorianCalendar.DECEMBER, 16).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2013));
		assertEquals(
		    new GregorianCalendar(2013, GregorianCalendar.DECEMBER, 14, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2013));
	}

	public void testGetFahrplanJahr2014() {
		assertEquals(new GregorianCalendar(2013, GregorianCalendar.DECEMBER, 15).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2014));
		assertEquals(
		    new GregorianCalendar(2014, GregorianCalendar.DECEMBER, 13, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2014));
	}

	public void testGetFahrplanJahr2015() {
		assertEquals(new GregorianCalendar(2014, GregorianCalendar.DECEMBER, 14).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2015));
		assertEquals(
		    new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 12, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2015));
	}

	public void testGetFahrplanJahr2016() {
		assertEquals(new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 13).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2016));
		assertEquals(
		    new GregorianCalendar(2016, GregorianCalendar.DECEMBER, 10, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2016));
	}

	public void testGetFahrplanJahr2017() {
		assertEquals(new GregorianCalendar(2016, GregorianCalendar.DECEMBER, 11).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2017));
		assertEquals(
		    new GregorianCalendar(2017, GregorianCalendar.DECEMBER, 16, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2017));
	}

	public void testGetFahrplanJahr2018() {
		assertEquals(new GregorianCalendar(2017, GregorianCalendar.DECEMBER, 17).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2018));
		assertEquals(
		    new GregorianCalendar(2018, GregorianCalendar.DECEMBER, 15, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2018));
	}

	public void testGetFahrplanJahr2019() {
		assertEquals(new GregorianCalendar(2018, GregorianCalendar.DECEMBER, 16).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2019));
		assertEquals(
		    new GregorianCalendar(2019, GregorianCalendar.DECEMBER, 14, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2019));
	}

	public void testGetFahrplanJahr2020() {
		assertEquals(new GregorianCalendar(2019, GregorianCalendar.DECEMBER, 15).getTime(),
		    EasyDateFormat.getFirstDayOfFahrplanjahr(2020));
		assertEquals(
		    new GregorianCalendar(2020, GregorianCalendar.DECEMBER, 12, 23, 59, 59).getTime(),
		    EasyDateFormat.getLastDayOfFahrplanjahr(2020));
	}

	public void testGetFahrplanJahrFromDate() {
		assertEquals(2010, EasyDateFormat.getFahrplanJahr(new GregorianCalendar(2010,
		    GregorianCalendar.DECEMBER, 11, 23, 59, 59).getTime()));
		assertEquals(2011, EasyDateFormat.getFahrplanJahr(new GregorianCalendar(2010,
		    GregorianCalendar.DECEMBER, 12, 0, 0, 0).getTime()));
	}
}
