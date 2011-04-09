package db.training.easy.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EasyDateFormat extends DateFormat {

	private static final long serialVersionUID = 7626856341792352214L;

	/* KSB Datumsformat HYYMMDD H=Jahrhundert 1=2000 0=1999 */
	private static DecimalFormat ksbFormat = new DecimalFormat("0000000");

	private DateFormat parent = null;

	public static final DateFormat instance = new EasyDateFormat(DateFormat.getDateInstance(
	    DateFormat.SHORT, Locale.GERMAN));

	private EasyDateFormat(DateFormat parent) {
		this.parent = parent;
	}

	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		if (date == null)
			return toAppendTo;
		return this.parent.format(date, toAppendTo, fieldPosition);
	}

	public static String format(Date date, DateFormat dateFormat) {
		if (date == null)
			return "";
		return dateFormat.format(date);
	}

	public Date parse(String source, ParsePosition pos) {
		if (source == null)
			return null;
		Date d = this.parent.parse(source, pos);
		if (d == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int year = c.get(Calendar.YEAR);
		if (year < 100) {
			// if (year > 50)
			// year += 1900;
			// else
			year += 2000;
			c.set(Calendar.YEAR, year);
			d = c.getTime();
		}
		return d;
	}

	public static void setToStartOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public static void setToEndOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}

	/**
	 * Konvertiert eine Uhrzeit in die KSB Stunden Dabei werden Minuten nicht beachtet
	 * 
	 * <pre>
	 *                         KSB-Zeit Uhrzeit
	 *                         -7	0
	 *                         -6	1
	 *                         0	7
	 *                         16	23
	 * </pre>
	 * 
	 * @param calendar
	 * @return
	 */
	public static int convertTimeToKsbHour(Calendar calendar) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour - 7;
	}

	/**
	 * Konvertiert ein KSB Datum in ein normales Datum Format: HJJMMTT H ist Jahrhundertkennzeichen
	 * 1 steht für Jahre im 21. Jahrhundert (20JJ)
	 * 
	 * @param hour
	 * @param ksbDate
	 * @return
	 */
	public static Calendar convertKsbTimeToDate(int hour, String ksbDate) {
		int year = Integer.parseInt(ksbDate.substring(1, 3));
		if (ksbDate.startsWith("1"))
			year += 2000;
		else
			year += 1900;

		int month = Integer.parseInt(ksbDate.substring(3, 5));
		// Greg calendar starts with month 0
		month--;
		int day = Integer.parseInt(ksbDate.substring(5, 7));
		hour = hour + 7;
		return new GregorianCalendar(year, month, day, hour, 0);

	}

	/**
	 * Konvertiert ein KSB Datum in ein normales Datum Format: HJJMMTT H ist Jahrhundertkennzeichen
	 * 1 steht für Jahre im 21. Jahrhundert (20JJ)
	 * 
	 * @param hour
	 * @param ksbDate
	 * @return
	 */
	public static Calendar convertKsbTimeToDate(int hour, long ksbDate) {

		String strKsbDate = ksbFormat.format(ksbDate);
		return convertKsbTimeToDate(hour, strKsbDate);

	}

	/**
	 * Bewegt StartDatum um [daysAdd] Tage in die Zukunft. Das EndDatum wird auf [endDateOffset]
	 * Tage Abstand zum StartDatum gesetzt. Uhrzeit StartDatum: 0:00:00 Uhr. Uhrzeit EndDatum:
	 * 23:59:59 Uhr.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param endDateOffset
	 * @param daysAdd
	 */
	public static void moveStartEndDate(Calendar startDate, Calendar endDate, int endDateOffset,
	    int daysAdd) {
		startDate.add(Calendar.DATE, daysAdd);
		EasyDateFormat.setToStartOfDay(startDate);

		endDate.setTime(startDate.getTime());
		endDate.add(Calendar.DATE, endDateOffset - 1);
		EasyDateFormat.setToEndOfDay(endDate);
	}

	/**
	 * Verringert den Zeitraum (in Tagen) um Wochenendtage
	 * 
	 * @param startDate
	 * @param duration
	 * @return
	 */
	public static int subtractWeekendDays(Date startDate, int duration) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		// Sa und So von Dauer abziehen
		for (int i = 0; i < duration; i++) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
			    || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				duration--;
			}
			calendar.add(Calendar.DATE, 1);
		}
		return duration;
	}

	/**
	 * Vergößert den Zeitraum (in Tagen) um Wochenendtage
	 * 
	 * @param startDate
	 * @param duration
	 * @return
	 */
	public static int extendByWeekendDays(Date startDate, int duration) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		// Sa und So zur Dauer hinzufügen
		for (int i = 0; i < duration; i++) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
			    || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				duration++;
			}
			calendar.add(Calendar.DATE, 1);
		}
		return duration;
	}

	/**
	 * Bewegt currentDate um einen/zwei Tage falls Sa/So
	 * 
	 * @param currentDate
	 */
	public static void skipWeekendDays(Calendar currentDate) {
		if (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			currentDate.add(Calendar.DATE, 2);
		}
		if (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			currentDate.add(Calendar.DATE, 1);
		}
	}

	/**
	 * bewegt den searchframe zum Beginn des nächsten Tages
	 * 
	 * @param product
	 * @param currentStartDate
	 * @param currentEndDate
	 * @param date
	 */
	public static void moveTimeframe(Calendar currentStartDate, Calendar currentEndDate, Date date,
	    int duration) {
		currentStartDate.setTime(date);
		currentStartDate.add(Calendar.DATE, 1);
		EasyDateFormat.skipWeekendDays(currentStartDate);
		EasyDateFormat.setToStartOfDay(currentStartDate);

		currentEndDate.setTime(currentStartDate.getTime());
		currentEndDate.add(Calendar.DATE,
		    extendByWeekendDays(currentStartDate.getTime(), duration) - 1);
		EasyDateFormat.setToEndOfDay(currentEndDate);
	}

	/**
	 * gibt den Tag eines KSB Datums zurueck
	 * 
	 * @param ksbDate
	 * @return
	 */
	public static int getDayFromKsbDate(long ksbDate) {
		/* KSB Datumsformat HYYMMDD H=Jahrhundert 1=2000 0=1999 */
		String ksbDateString = ksbFormat.format(ksbDate);
		return Integer.parseInt(ksbDateString.substring(5, 7));

	}

	/**
	 * gibt den Monat eines KSB Datums zurueck. Die Werte liegen zwischen 1 und 12.
	 * 
	 * @param ksbDate
	 * @return
	 */
	public static int getMonthFromKsbDate(long ksbDate) {
		/* KSB Datumsformat HYYMMDD H=Jahrhundert 1=2000 0=1999 */
		String ksbDateString = ksbFormat.format(ksbDate);
		return Integer.parseInt(ksbDateString.substring(3, 5));

	}

	public static Date getFirstDayOfYear(Integer year) {
		if (null == year)
			throw new IllegalArgumentException("year");

		Calendar firstDayOfYear = new GregorianCalendar(year, GregorianCalendar.JANUARY, 1, 0, 0, 0);
		return firstDayOfYear.getTime();
	}

	public static Date getLastDayOfYear(Integer year) {
		if (null == year)
			throw new IllegalArgumentException("year");

		Calendar lastDayOfYear = new GregorianCalendar(year, GregorianCalendar.DECEMBER, 31, 23,
		    59, 59);
		return lastDayOfYear.getTime();
	}

	/**
	 * Berechnet den ersten Tag eines Fahrplanjahres
	 * 
	 * @param fahrplanjahr
	 * @return
	 */
	public static Date getFirstDayOfFahrplanjahr(int fahrplanjahr) {
		return getFahrplanwechsel(fahrplanjahr - 1).getTime();
	}

	/**
	 * Berechnet den letzten Tag eines Fahrplanjahres
	 * 
	 * @param fahrplanjahr
	 * @return
	 */
	public static Date getLastDayOfFahrplanjahr(int fahrplanjahr) {
		Calendar cal = getFahrplanwechsel(fahrplanjahr);
		cal.add(Calendar.SECOND, -1);
		return cal.getTime();
	}

	/**
	 * berechnet den Termin des Fahrplanwechsel eines Fahrplanjahres und gibt das Datum zurück. Das
	 * Fahrplanjahr beginnt nach Definition des Pflichtenheftes OSB: Babett am Tag der auf den 2.
	 * Samstag im Dezember folgt, um 0:00 Uhr.
	 * 
	 * @param fahrplanjahr
	 * @return
	 */
	private static Calendar getFahrplanwechsel(int fahrplanjahr) {
		Calendar firstDayOfFahrplanjahr = new GregorianCalendar(fahrplanjahr, Calendar.DECEMBER, 1,
		    23, 59, 59);

		firstDayOfFahrplanjahr.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		firstDayOfFahrplanjahr.set(Calendar.WEEK_OF_MONTH, 2);
		firstDayOfFahrplanjahr.add(Calendar.SECOND, 1);

		return firstDayOfFahrplanjahr;
	}

	/**
	 * Ermittelt das Fahrplanjahr zum übergebenen Datum
	 * 
	 * @param date
	 * @return
	 */
	public static int getFahrplanJahr(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		if (date.compareTo(getLastDayOfFahrplanjahr(year)) <= 0)
			return year;
		return year + 1;
	}

	public static void main(String[] args) {

		for (int i = 2005; i <= 2020; i++) {
			Date beginn = getFirstDayOfFahrplanjahr(i);
			Date ende = getLastDayOfFahrplanjahr(i);

			System.out.println(String.format("%s\t\t%s\t%s", i,
			    FrontendHelper.castDateToString(beginn), FrontendHelper.castDateToString(ende)));
		}

		/*
		 * Calendar cal = new GregorianCalendar(2010, Calendar.DECEMBER, 1, 23, 59, 59);
		 * cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); cal.set(Calendar.WEEK_OF_MONTH, 2);
		 * cal.add(Calendar.SECOND, 1);
		 * 
		 * Date compareValue = EasyDateFormat.getFirstDayOfFahrplanjahr(2011);
		 * 
		 * System.out.println(cal.getTime().toString());
		 * System.out.println(compareValue.toString());
		 * 
		 * System.out.println("FirstDayOfWeek: " + cal.getFirstDayOfWeek());
		 * 
		 * System.out.println("-------------------------"); doSomething(new GregorianCalendar(2010,
		 * Calendar.APRIL, 1)); System.out.println("-------------------------"); doSomething(new
		 * GregorianCalendar(2010, Calendar.APRIL, 6));
		 * System.out.println("-------------------------"); doSomething(new GregorianCalendar(2010,
		 * Calendar.APRIL, 16)); System.out.println("-------------------------"); doSomething(new
		 * GregorianCalendar(2010, Calendar.APRIL, 21));
		 * System.out.println("-------------------------"); doSomething(new GregorianCalendar(2010,
		 * Calendar.APRIL, 28)); System.out.println("-------------------------"); doSomething(new
		 * GregorianCalendar(2010, Calendar.MAY, 1));
		 * System.out.println("-------------------------"); doSomething(new GregorianCalendar(2010,
		 * Calendar.MAY, 3)); System.out.println("-------------------------");
		 */
	}

	public static void doSomething(Calendar calendar) {
		System.out.println("ERA: " + calendar.get(Calendar.ERA));
		System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
		System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
		System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
		System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
		System.out.println("DATE: " + calendar.get(Calendar.DATE));
		System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
		System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
		System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
		System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
		System.out.println("ZONE_OFFSET: "
		    + (calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000)));
		System.out.println("DST_OFFSET: " + (calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000)));
	}
}
