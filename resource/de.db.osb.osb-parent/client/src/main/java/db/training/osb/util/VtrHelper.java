package db.training.osb.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Sammlung verschiedener Hilfsfunktionen zum Umgang mit Verkehrstageregelungen und
 * Wochentagschluesseln
 * 
 * @author Andreas Lotz
 * 
 */
public class VtrHelper {

	private static Map<Integer, Integer> days = new HashMap<Integer, Integer>();

	static {
		days.put(Calendar.MONDAY, 64);
		days.put(Calendar.TUESDAY, 32);
		days.put(Calendar.WEDNESDAY, 16);
		days.put(Calendar.THURSDAY, 8);
		days.put(Calendar.FRIDAY, 4);
		days.put(Calendar.SATURDAY, 2);
		days.put(Calendar.SUNDAY, 1);
	};

	public static Integer getVtsByDays(boolean mon, boolean tue, boolean wed, boolean thu,
	    boolean fri, boolean sat, boolean sun) {
		Integer wts = 0;

		if (mon)
			wts += days.get(Calendar.MONDAY);
		if (tue)
			wts += days.get(Calendar.TUESDAY);
		if (wed)
			wts += days.get(Calendar.WEDNESDAY);
		if (thu)
			wts += days.get(Calendar.THURSDAY);
		if (fri)
			wts += days.get(Calendar.FRIDAY);
		if (sat)
			wts += days.get(Calendar.SATURDAY);
		if (sun)
			wts += days.get(Calendar.SUNDAY);

		return wts * 100;
	}

	public static boolean isDayPartOfVts(Integer wts, int calendarDay) {
		if (wts == null || wts == 0 || days.get(calendarDay) == null)
			return false;

		wts = wts / 100;
		String binWts = Integer.toBinaryString(wts);
		String binDay = Integer.toBinaryString(days.get(calendarDay));

		if (binDay.length() > binWts.length())
			return false;
		else
			binWts = binWts.substring(binWts.length() - binDay.length());

		if (binWts.startsWith("1"))
			return true;
		else
			return false;
	}

}