package db.training.easy.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Sammlung verschiedener Hilfsfunktionen fuers Frontend
 * 
 * @author FlorianHueter
 * 
 */
public class FrontendHelper {

	/**
	 * Wandelt Strings (Zahlen mit Tausenderpunkt und Komma) in Double-Werte
	 * 
	 * @param string
	 * @return Double-Wert oder null
	 */
	public static Double castStringToDouble(String string) {
		if (string == null)
			return null;
		// string = string.replace(".", "").replace(",", ".");

		if (string.length() == 0)
			return null;

		try {
			DecimalFormat format = new DecimalFormat("#,###,##0.00", new DecimalFormatSymbols(
			    Locale.GERMAN));
			return format.parse(string).doubleValue();
		} catch (NumberFormatException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Wandelt Double-Werte in Strings mit Tausenderpunkt und digits Nachkommastellen
	 * 
	 * @param doubleVal
	 * @param digits
	 *            Nachkommastellen
	 * 
	 * @return Formatierter String
	 */
	public static String castDoubleToString(Double doubleVal) {
		return doubleVal != null ? new DecimalFormat("#,###,##0.00", new DecimalFormatSymbols(
		    Locale.GERMAN)).format(doubleVal.doubleValue()) : "";
	}

	public static String castDoubleToString(Double doubleVal, int digits) {
		String decimalFormat = "#,###,##0";
		if (digits > 0)
			decimalFormat = "#,###,##0.";
		for (int i = 0; i < digits; i++)
			decimalFormat += "0";
		return doubleVal != null ? new DecimalFormat(decimalFormat, new DecimalFormatSymbols(
		    Locale.GERMAN)).format(doubleVal.doubleValue()) : "";
	}

	public static BigDecimal castStringToBigDecimal(String string) {
		if (string == null)
			return null;
		string = string.replace(".", "").replace(",", ".");
		if (string.length() == 0)
			return null;
		return new BigDecimal(string);
	}

	public static String castBigDecimalToString(BigDecimal decimalVal, int digits) {
		String decimalFormat = "#,###,##0";
		if (digits > 0)
			decimalFormat = "#,###,##0.";
		for (int i = 0; i < digits; i++)
			decimalFormat += "0";
		return decimalVal != null ? new DecimalFormat(decimalFormat, new DecimalFormatSymbols(
		    Locale.GERMAN)).format(decimalVal) : "";
	}

	/**
	 * Wandelt Strings (Zahlen mit Tausenderpunkt und Komma) in Float-Werte
	 * 
	 * @param string
	 * @return Float-Wert oder null
	 */
	public static Float castStringToFloat(String string) {
		if (string == null)
			return null;
		// string = string.replace(".", "").replace(",", ".");
		if (string.length() == 0)
			return null;
		try {
			DecimalFormat format = new DecimalFormat("#,###,##0.00", new DecimalFormatSymbols(
			    Locale.GERMAN));
			return format.parse(string).floatValue();
		} catch (NumberFormatException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Wandelt Float-Werte in Strings um. Ausgabeformat = '#,###,##0.00'
	 * 
	 * @param floatVal
	 * @return Formatierter String
	 */
	public static String castFloatToString(Float floatVal) {
		return castFloatToString(floatVal, "#,###,##0.00");
	}

	/**
	 * Wandelt Float-Werte in Strings um. Ausgabeformat (z.B. '#,###,##0.00') muss uebergeben
	 * werden.
	 * 
	 * @param floatVal
	 * @param formatString
	 * @return Formatierter String
	 */
	public static String castFloatToString(Float floatVal, String formatString) {
		if (floatVal == null)
			return "";
		DecimalFormat format = new DecimalFormat(formatString, new DecimalFormatSymbols(
		    Locale.GERMAN));
		return format.format(floatVal.floatValue());
	}

	/**
	 * Wandelt Strings (Zahlen mit Tausenderpunkt) in Integer-Werte
	 * 
	 * @param string
	 * @return Integer-Wert oder null
	 */
	public static Integer castStringToInteger(String string) {
		if (string == null)
			return null;
		string = string.replace(".", "");
		if (string.length() == 0)
			return null;
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Wandelt Strings ('true' oder 'false') in Boolean um
	 * 
	 * @param string
	 * @return Boolean.TRUE oder Boolean.FALSE
	 */
	public static Boolean castStringToBoolean(String string) {
		if (string == null)
			return null;
		if (string.length() == 0)
			return null;
		return Boolean.valueOf(string);
	}

	/**
	 * Wandelt Integer-Werte in Strings mit Tausenderpunkt
	 * 
	 * @param integerVal
	 * @return Formatierter String
	 */
	public static String castIntegerToString(Integer integerVal) {
		if (integerVal == null)
			return "";
		DecimalFormat format = new DecimalFormat("#,##0");
		return format.format(integerVal.intValue());
	}

	/**
	 * Wandelt Integer-Werte in Strings OHNE Tausenderpunkt
	 * 
	 * @param integerVal
	 * @return Formatierter String
	 */
	public static String castIntegerToStringStandard(Integer integerVal) {
		if (integerVal == null)
			return "";
		DecimalFormat format = new DecimalFormat("###0");
		return format.format(integerVal.intValue());
	}

	/**
	 * Wandelt String in Date um. Aktzeptiert wird deutsches oder englisches Format mit und ohne
	 * Uhrzeit und zwei- oder vierstelliger Jahreszahl
	 * 
	 * @param dateString
	 * @return Date oder null
	 */
	public static Date castStringToDate(String string) {
		if (string == null || string.equals(""))
			return null;

		Date date = null;
		String format = null;

		String start = "^";
		String end = "?";

		String H = "([0-9]){1}";
		String HH = "(0[0-9]|1[0-9]|2[0-3]){1}";
		String Hmm = "([0-9]){1}([0-5][0-9]){1}";
		String HHmm = "(0[0-9]|[1][0-9]|[2][0-3]){1}([0-5][0-9]){1}";
		String Hmmss = "([0-9]){1}([0-5][0-9]){1}([0-5][0-9]){1}";
		String HHmmss = "(0[0-9]|[1][0-9]|[2][0-3]){1}([0-5][0-9]){1}([0-5][0-9]){1}";
		String HH_mm = "(0?[0-9]|[1][0-9]|[2][0-3]){1}[:]{1}(0?[0-9]|[0-5][0-9]){1}";
		String HH_mm_ss = "(0?[0-9]|[1][0-9]|[2][0-3]){1}[:]{1}(0?[0-9]|[0-5][0-9]){1}[:]{1}(0?[0-9]|[0-5][0-9]){1}([.]{1}[0-9])?";

		String dMMyy = "([1-9]){1}(0[0-9]|1[0-2]){1}([0-9]{2}){1}";
		String dMMyyyy = "([1-9]){1}(0[0-9]|1[0-2]){1}(19[0-9]{2}|20[0-9]{2}){1}";
		String ddMMyy = "(0[1-9]|[12][0-9]|3[01]){1}(0[0-9]|1[0-2]){1}([0-9]{2}){1}";
		String ddMMyyyy = "(0[1-9]|[12][0-9]|3[01]){1}(0[0-9]|1[0-2]){1}(19[0-9]{2}|20[0-9]{2}){1}";
		String dd_MM_yy = "(0?[1-9]|[12][0-9]|3[01]){1}[.]{1}(0?[0-9]|1[0-2]){1}[.]{1}([0-9]{1,2}){1}";
		String dd_MM_yyyy = "(0?[1-9]|[12][0-9]|3[01]){1}[.]{1}(0?[0-9]|1[0-2]){1}[.]{1}([0-9]{3,4}){1}";
		String yy_MM_dd = "([0-9]{1,2}){1}[-]{1}(0?[0-9]|1[0-2]){1}[-]{1}(0?[1-9]|[12][0-9]|3[01]){1}";
		String yyyy_MM_dd = "([0-9]{3,4}){1}[-]{1}(0?[0-9]|1[0-2]){1}[-]{1}(0?[1-9]|[12][0-9]|3[01]){1}";
		String yyyyMMdd = "([0-9]{3,4}){1}(0?[0-9]|1[0-2]){1}(0?[1-9]|[12][0-9]|3[01]){1}";

		// Datum ohne Zeit
		// ****************************************************************
		if (string.matches(start + dMMyy + end))
			format = "dMMyy";
		else if (string.matches(start + dMMyyyy + end))
			format = "dMMyyyy";
		else if (string.matches(start + ddMMyy + end))
			format = "ddMMyy";
		else if (string.matches(start + ddMMyyyy + end))
			format = "ddMMyyyy";
		else if (string.matches(start + dd_MM_yy + end))
			format = "dd.MM.yy";
		else if (string.matches(start + dd_MM_yyyy + end))
			format = "dd.MM.yyyy";
		else if (string.matches(start + yy_MM_dd + end))
			format = "yy-MM-dd";
		else if (string.matches(start + yyyy_MM_dd + end))
			format = "yyyy-MM-dd";

		// Datum mit Zeit H
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + H + end))
			format = "dMMyy H";
		else if (string.matches(start + dMMyyyy + " " + H + end))
			format = "dMMyyyy H";
		else if (string.matches(start + ddMMyy + " " + H + end))
			format = "ddMMyy H";
		else if (string.matches(start + ddMMyyyy + " " + H + end))
			format = "ddMMyyyy H";
		else if (string.matches(start + dd_MM_yy + " " + H + end))
			format = "dd.MM.yy H";
		else if (string.matches(start + dd_MM_yyyy + " " + H + end))
			format = "dd.MM.yyyy H";
		else if (string.matches(start + yy_MM_dd + " " + H + end))
			format = "yy-MM-dd H";
		else if (string.matches(start + yyyy_MM_dd + " " + H + end))
			format = "yyyy-MM-dd H";

		// Datum mit Zeit HH
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + HH + end))
			format = "dMMyy HH";
		else if (string.matches(start + dMMyyyy + " " + HH + end))
			format = "dMMyyyy HH";
		else if (string.matches(start + ddMMyy + " " + HH + end))
			format = "ddMMyy HH";
		else if (string.matches(start + ddMMyyyy + " " + HH + end))
			format = "ddMMyyyy HH";
		else if (string.matches(start + dd_MM_yy + " " + HH + end))
			format = "dd.MM.yy HH";
		else if (string.matches(start + dd_MM_yyyy + " " + HH + end))
			format = "dd.MM.yyyy HH";
		else if (string.matches(start + yy_MM_dd + " " + HH + end))
			format = "yy-MM-dd HH";
		else if (string.matches(start + yyyy_MM_dd + " " + HH + end))
			format = "yyyy-MM-dd HH";

		// Datum mit Zeit Hmm
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + Hmm + end))
			format = "dMMyy Hmm";
		else if (string.matches(start + dMMyyyy + " " + Hmm + end))
			format = "dMMyyyy Hmm";
		else if (string.matches(start + ddMMyy + " " + Hmm + end))
			format = "ddMMyy Hmm";
		else if (string.matches(start + ddMMyyyy + " " + Hmm + end))
			format = "ddMMyyyy Hmm";
		else if (string.matches(start + dd_MM_yy + " " + Hmm + end))
			format = "dd.MM.yy Hmm";
		else if (string.matches(start + dd_MM_yyyy + " " + Hmm + end))
			format = "dd.MM.yyyy Hmm";
		else if (string.matches(start + yy_MM_dd + " " + Hmm + end))
			format = "yy-MM-dd Hmm";
		else if (string.matches(start + yyyy_MM_dd + " " + Hmm + end))
			format = "yyyy-MM-dd Hmm";

		// Datum mit Zeit HHmm
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + HHmm + end))
			format = "dMMyy HHmm";
		else if (string.matches(start + dMMyyyy + " " + HHmm + end))
			format = "dMMyyyy HHmm";
		else if (string.matches(start + ddMMyy + " " + HHmm + end))
			format = "ddMMyy HHmm";
		else if (string.matches(start + ddMMyyyy + " " + HHmm + end))
			format = "ddMMyyyy HHmm";
		else if (string.matches(start + dd_MM_yy + " " + HHmm + end))
			format = "dd.MM.yy HHmm";
		else if (string.matches(start + dd_MM_yyyy + " " + HHmm + end))
			format = "dd.MM.yyyy HHmm";
		else if (string.matches(start + yy_MM_dd + " " + HHmm + end))
			format = "yy-MM-dd HHmm";
		else if (string.matches(start + yyyy_MM_dd + " " + HHmm + end))
			format = "yyyy-MM-dd HHmm";
		else if (string.matches(start + yyyy_MM_dd + "[,] " + HH_mm + end))
			format = "yyyy-MM-dd";

		// Datum mit Zeit Hmmss
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + Hmmss + end))
			format = "dMMyy Hmmss";
		else if (string.matches(start + dMMyyyy + " " + Hmmss + end))
			format = "dMMyyyy Hmmss";
		else if (string.matches(start + ddMMyy + " " + Hmmss + end))
			format = "ddMMyy Hmmss";
		else if (string.matches(start + ddMMyyyy + " " + Hmmss + end))
			format = "ddMMyyyy Hmmss";
		else if (string.matches(start + dd_MM_yy + " " + Hmmss + end))
			format = "dd.MM.yy Hmmss";
		else if (string.matches(start + dd_MM_yyyy + " " + Hmmss + end))
			format = "dd.MM.yyyy Hmmss";
		else if (string.matches(start + yy_MM_dd + " " + Hmmss + end))
			format = "yy-MM-dd Hmmss";
		else if (string.matches(start + yyyy_MM_dd + " " + Hmmss + end))
			format = "yyyy-MM-dd Hmmss";

		// Datum mit Zeit HHmmss
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + HHmmss + end))
			format = "dMMyy HHmmss";
		else if (string.matches(start + dMMyyyy + " " + HHmmss + end))
			format = "dMMyyyy HHmmss";
		else if (string.matches(start + ddMMyy + " " + HHmmss + end))
			format = "ddMMyy HHmmss";
		else if (string.matches(start + ddMMyyyy + " " + HHmmss + end))
			format = "ddMMyyyy HHmmss";
		else if (string.matches(start + dd_MM_yy + " " + HHmmss + end))
			format = "dd.MM.yy HHmmss";
		else if (string.matches(start + dd_MM_yyyy + " " + HHmmss + end))
			format = "dd.MM.yyyy HHmmss";
		else if (string.matches(start + yy_MM_dd + " " + HHmmss + end))
			format = "yy-MM-dd HHmmss";
		else if (string.matches(start + yyyy_MM_dd + " " + HHmmss + end))
			format = "yyyy-MM-dd HHmmss";
		else if (string.matches(start + yyyyMMdd + HHmm + end))
			format = "yyyyMMddHHmm";

		// Datum mit Zeit HH:mm
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + HH_mm + end))
			format = "dMMyy HH:mm";
		else if (string.matches(start + dMMyyyy + " " + HH_mm + end))
			format = "dMMyyyy HH:mm";
		else if (string.matches(start + ddMMyy + " " + HH_mm + end))
			format = "ddMMyy HH:mm";
		else if (string.matches(start + ddMMyyyy + " " + HH_mm + end))
			format = "ddMMyyyy HH:mm";
		else if (string.matches(start + dd_MM_yy + " " + HH_mm + end))
			format = "dd.MM.yy HH:mm";
		else if (string.matches(start + dd_MM_yyyy + " " + HH_mm + end))
			format = "dd.MM.yyyy HH:mm";
		else if (string.matches(start + yy_MM_dd + " " + HH_mm + end))
			format = "yy-MM-dd HH:mm";
		else if (string.matches(start + yyyy_MM_dd + " " + HH_mm + end))
			format = "yyyy-MM-dd HH:mm";

		// Datum mit Zeit HH:mm:ss
		// ****************************************************************
		else if (string.matches(start + dMMyy + " " + HH_mm_ss + end))
			format = "dMMyy HH:mm:ss";
		else if (string.matches(start + dMMyyyy + " " + HH_mm_ss + end))
			format = "dMMyyyy HH:mm:ss";
		else if (string.matches(start + ddMMyy + " " + HH_mm_ss + end))
			format = "ddMMyy HH:mm:ss";
		else if (string.matches(start + ddMMyyyy + " " + HH_mm_ss + end))
			format = "ddMMyyyy HH:mm:ss";
		else if (string.matches(start + dd_MM_yy + " " + HH_mm_ss + end))
			format = "dd.MM.yy HH:mm:ss";
		else if (string.matches(start + dd_MM_yyyy + " " + HH_mm_ss + end))
			format = "dd.MM.yyyy HH:mm:ss";
		else if (string.matches(start + yy_MM_dd + " " + HH_mm_ss + end))
			format = "yy-MM-dd HH:mm:ss";
		else if (string.matches(start + yyyy_MM_dd + " " + HH_mm_ss + end))
			format = "yyyy-MM-dd HH:mm:ss";

		else
			return null;

		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			date = dateFormat.parse(string);
		} catch (ParseException e) {
			date = null;
		}

		return date;
	}

	/**
	 * Wandelt Strings in Date um
	 * 
	 * @param dateString
	 * @return Date oder null
	 */
	public static Date castStringToEasyDate(String string) {
		if (string == null || string.equals(""))
			return null;

		DateFormat dateFormat = EasyDateFormat.instance;
		try {
			return dateFormat.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Wandelt Date-Werte in Strings um (dd.MM.yyyy)
	 * 
	 * @param date
	 * @return Formatierter String
	 */
	public static String castDateToString(Date date) {
		return castDateToString(date, "dd.MM.yyyy");
	}

	/**
	 * Wandelt Date-Werte in Strings um (dd.MM.yyyy)
	 * 
	 * @param date
	 *            ,
	 * @return Formatierter String
	 */
	public static String castDateToString(Date date, String format) {
		if (date == null)
			return "";
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * Ersetzt geschützte Leerzeichen (&nbsp; / char160) durch Leerzeichen (char32)
	 * 
	 * @param string
	 * @return Umgewandelten String
	 */
	public static String replaceNBSPs(String string) {
		char[] chars = string.toCharArray();
		StringBuilder stringTemp = new StringBuilder();

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == 160)
				chars[i] = 32;
			stringTemp.append(chars[i]);
		}
		return stringTemp.toString();
	}

	/**
	 * Extrahiert die Kundennummer einer EVU aus deren Caption (z.B. "[ABC] Abc-Hausen" = "ABC")
	 * 
	 * @param caption
	 */
	public static String castCaptionToKuerzel(String caption) {
		if (caption == null || caption.equals(""))
			return null;
		if (!caption.contains("[") || !caption.contains("]"))
			return null;
		caption = caption.substring(caption.indexOf("[") + 1, caption.indexOf("]"));
		if (caption.length() > 5)
			return null;
		return FrontendHelper.replaceNBSPs(caption);
	}

	public static boolean stringNotNullOrEmpty(String s) {
		return ((s != null) && (!s.equals("")));
	}

	public static String getNullOrTrimmed(String value) {
		String result = null;
		if (value != null) {
			String trimmed = value.trim();
			if (trimmed.length() > 0)
				result = trimmed;
		}
		return result;
	}

	public static Date castStringToTime(String string) {

		Date date = null;

		try {
			String[] timeParts = string.split("[:]");
			int h, m, s = m = h = 0;

			if (timeParts.length > 0)
				h = Integer.parseInt(timeParts[0]);
			if (timeParts.length > 1)
				m = Integer.parseInt(timeParts[1]);
			if (timeParts.length > 2)
				s = Integer.parseInt(timeParts[2]);

			// Stunden korrigieren
			if (h > 0)
				h--;

			long time = ((h * 60 + m) * 60 + s) * 1000;
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTimeInMillis(time);
			date = cal.getTime();
		} catch (Exception ex) {

		}

		return date;
	}

	/**
	 * Minuten in Format hh:mm konvertieren und ggfs. mit 0 auffüllen
	 * 
	 * @param minutes
	 * @return
	 */
	public static String castMinutesToTimeString(Integer minutes) {
		StringBuilder sb = new StringBuilder(5);

		int hours;
		if (minutes == null) {
			hours = 0;
			minutes = 0;
		} else {
			hours = (int) Math.floor(((double) minutes) / 60);
			minutes = minutes % 60;
		}

		sb.append(("00" + hours).substring((hours > 9) ? 2 : 1));
		sb.append(':');
		sb.append(("00" + minutes).substring((minutes > 9) ? 2 : 1));
		return sb.toString();
	}

	/**
	 * Wandelt einen String der Form hh:mm in Minuten um
	 * 
	 * @param time
	 * @return
	 */
	public static Integer castTimeStringToMinutes(String time) {
		if (time == null) {
			return null;
		}

		if (time.indexOf(':') != -1) {
			// Zeit ist in Format HH:mm eingegeben worden
			String[] values = time.split(":");
			Integer hh = null;
			Integer mm = null;
			try {
				hh = FrontendHelper.castStringToInteger(values[0]);
			} catch (IndexOutOfBoundsException e) {
				// hh=null
			}
			try {
				mm = FrontendHelper.castStringToInteger(values[1]);
			} catch (IndexOutOfBoundsException e) {
				// mm=null
			}
			if (hh == null) {
				hh = Integer.valueOf(0);
			}
			if (mm == null) {
				mm = Integer.valueOf(0);
			}
			return hh * 60 + mm;
		} else {
			// Eingabe als Minuten interpretieren
			return FrontendHelper.castStringToInteger(time);
		}
	}
}