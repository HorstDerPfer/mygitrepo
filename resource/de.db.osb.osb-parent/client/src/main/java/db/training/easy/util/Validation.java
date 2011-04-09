package db.training.easy.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Validation {

	/**
	 * Validiert Datum-String anhand des Easy Date Formates.
	 * 
	 * @param aDateString
	 * @return true, wenn Datum String dem Easy Date Format entspricht
	 */
	public static boolean checkEasyDateFormat(String aDateString) {
		DateFormat dateFormat = EasyDateFormat.instance;
		boolean result = true;
		try {
			dateFormat.parse(aDateString);
		} catch (ParseException e) {
			result = false;
		}

		return result;
	}

	// TODO lotz: wird diese Funktion überhaupt gebraucht (ausgelagert aus
	// EventServiceImpl) ? (jsc)
	public static boolean checkTime(String time) {
		if (time == null)
			return false;
		if (time.contains(":") == false)
			return false;
		String[] params = time.split(":");
		Integer hours = null;
		Integer minutes = null;
		if (params.length != 2)
			return false;
		try {
			hours = Integer.parseInt(params[0]);
			minutes = Integer.parseInt(params[1]);
		} catch (NumberFormatException e) {
			return false;
		}
		if (hours == null || minutes == null || hours.intValue() < 0 || 
			hours.intValue() > 23 || minutes.intValue() < 0 || minutes.intValue() > 59)
			return false;
		return true;
	}

	/**
	 * Prueft, ob uebergebene Stunden gueltig sind (Ganzzahl >=0 und <=23)
	 * 
	 * @param hours
	 *                Stunden
	 * @return true, wenn gueltig
	 */
	public static boolean checkTimeHours(String hours) {
		int localHours = -1;
		try {
			localHours = Integer.parseInt(hours);
		} catch (NumberFormatException e) {
			return false;
		}
		if (localHours < 0 || localHours > 23)
			return false;
		return true;
	}

	/**
	 * TODO lotz: Kommentar fehlt: was diese Funktion macht, erwartete Parameter
	 * 
	 * @param date
	 *                Datum im Format ...
	 * @param time
	 *                Uhrzeit im Format ...
	 * @return Date Objekt oder null, wenn Date aufgrund der uebergebenen Parameter nicht
	 *         erzeugt werden konnte
	 */
	public static Date parseDateTime(String date, String time) {
		DateFormat dateFormat = EasyDateFormat.instance;

		String[] t = time.split(":");

		Integer hour = null;
		Integer min = null;

		if (t.length > 0)
			hour = Integer.parseInt(t[0]);
		if (t.length == 2)
			min = Integer.parseInt(t[1]);

		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(dateFormat.parse(date));
		} catch (ParseException e) {
			return null;
		}

		if (hour != null)
			cal.set(Calendar.HOUR_OF_DAY, hour);
		if (min != null)
			cal.set(Calendar.MINUTE, min);

		return cal.getTime();
	}

}
