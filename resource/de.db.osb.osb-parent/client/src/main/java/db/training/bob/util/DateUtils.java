package db.training.bob.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import db.training.easy.util.EasyDateFormat;

public class DateUtils {

	/**
	 * subtrahiert <c>d1</c> von <c>d2</c> - also d2 (23:59:59) - d1 (0:00:00) - und gibt die
	 * Differenz in Tagen zurück. Beispiel: 14.03.2011 15:30 Uhr - 29.03.2011 12:00 Uhr = 15 Tage
	 * 
	 * @param d1
	 * @param d2
	 * @return die Differenz der Daten in Tagen
	 */
	public static int getDateDiffInDays(Date d1, Date d2) {
		final int MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;

		if (d1 == null || d2 == null) {
			StringBuilder sb = new StringBuilder();
			if (d1 == null)
				sb.append("d1 == NULL;");
			if (d2 == null)
				sb.append("d2 == NULL;");
			sb.append("ungueltige Daten fuer Berechnung der Differenz.");
			throw new IllegalArgumentException(sb.toString());
		}
		Calendar cal = GregorianCalendar.getInstance(Locale.GERMANY);
		cal.setTime(d1);
		EasyDateFormat.setToStartOfDay(cal);
		d1 = cal.getTime();

		cal.setTime(d2);
		EasyDateFormat.setToEndOfDay(cal);
		d2 = cal.getTime();
		return (int) Math.ceil(getDateDiff(d1, d2) / MILLISECONDS_PER_DAY);
	}

	/**
	 * Berechnet die Differenz von d1 von d2 in Millisekunden
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getDateDiff(Date d1, Date d2) {
		assert (d1 != null && d2 != null);
		return d2.getTime() - d1.getTime();
	}

	/**
	 * Berechnet das Datum das x Wochen vor dem übergebenen datum liegt
	 * 
	 * @param cal
	 *            Ausgangsdatum
	 * @param numberOfWeeks
	 *            Anzahl der Wochen vor Ausgangsdatum
	 * @return Datum, das numberOfWeeks vor dem Ausgangsdatum liegt
	 */
	public static GregorianCalendar subtractWeeks(GregorianCalendar cal, int numberOfWeeks) {
		cal.add(GregorianCalendar.DATE, numberOfWeeks * -7);
		return cal;
	}

	/**
	 * Berechnet das Datum das x Wochen vor dem übergebenen datum liegt
	 * 
	 * @param cal
	 *            Ausgangsdatum
	 * @param numberOfWeeks
	 *            Anzahl der Wochen vor Ausgangsdatum
	 * @return Datum, das numberOfWeeks vor dem Ausgangsdatum liegt
	 */
	public static GregorianCalendar addWeeks(GregorianCalendar cal, int numberOfWeeks) {
		cal.add(GregorianCalendar.DATE, numberOfWeeks * 7);
		return cal;
	}

	/**
	 * Gibt das Datum eines bestimmten Wochentags zurück, der in der gleichen Kalenderwoche liegt
	 * wie das übergebene Datum
	 * 
	 * @param cal
	 *            Ausgangsdatum
	 * @param day
	 *            Wochentag (@see java.util.Calendar.DAY_OF_WEEK)
	 * @return Datum des Wochentags (day) innerhalb der selben Kalenderwoche liegt wie das
	 *         Ausgangsdatum (cal)
	 */
	public static GregorianCalendar getDayOfWeek(GregorianCalendar cal, int day) {
		GregorianCalendar result = (GregorianCalendar) cal.clone();
		result.set(GregorianCalendar.DAY_OF_WEEK, day);
		return result;
	}
}
