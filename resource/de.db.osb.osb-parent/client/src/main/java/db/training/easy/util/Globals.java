package db.training.easy.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class Globals {

    private final static char hexArray[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
	    'b', 'c', 'd', 'e', 'f' };

    public final static int SYSTEM_START_YEAR = 2006;

    /**
         * Konvertiert ein Byte-Array in den korrespondierenden Hex-String.
         * 
         * @param in
         *                Byte-Array
         * @param offset
         *                Offset des Byte-Arrays
         * @param length
         *                Länge des Byte-Arrays
         * @return Repräsentation des Byte-Arrays als Hex-String
         */
    public static String byteArrayToHexString(byte in[], int offset, int length) {
	if (in == null)
	    return null;
	StringBuffer buffer = new StringBuffer();
	for (int i = offset; i < length; i++) {
	    buffer.append(hexArray[in[i] >> 4 & 0xf]);
	    buffer.append(hexArray[in[i] & 0xf]);
	}
	return buffer.toString();
    }

    public static List<Integer> generateYearList() {
	return generateYearList(0, 0);
    }

    /**
         * Generiert eine Liste mit Integer-Objekten, die Jahreszahlen repräsentieren
         * 
         * @param historyYears
         *                Anzahl Jahre VOR SYSTEM_START_YEAR
         * @param futureYears
         *                Anzhal Jahre NACH dem aktuellen Jahr
         * @return
         */
    public static List<Integer> generateYearList(int historyYears, int futureYears) {
	Calendar c = Calendar.getInstance();
	int listEndYear = c.get(Calendar.YEAR) + futureYears;
	List<Integer> years = new ArrayList<Integer>();
	for (int year = SYSTEM_START_YEAR - historyYears; year <= listEndYear; year++) {
	    years.add(year);
	}
	return years;
    }

    public static ArrayList<LinkedHashMap<Integer, Integer>> getLast12MonthsMap() {
	Calendar c = Calendar.getInstance();
	int actualYear = c.get(Calendar.YEAR);
	int actualMonth = c.get(Calendar.MONTH);
	if (actualMonth == 0) {
	    actualMonth = 12;
	    actualYear -= 1;
	}
	int counter = 0;
	ArrayList<LinkedHashMap<Integer, Integer>> months = new ArrayList<LinkedHashMap<Integer, Integer>>();
	for (int year = actualYear; year >= SYSTEM_START_YEAR && counter < 12; year--) {
	    if (year == actualYear) {
		for (int month = actualMonth; month >= 1 && counter < 12; month--) {
		    LinkedHashMap<Integer, Integer> entry = new LinkedHashMap<Integer, Integer>();
		    entry.put(Integer.valueOf(year), Integer.valueOf(month));
		    months.add(entry);
		    counter++;
		}
	    } else {
		for (int month = 12; month >= 1 && counter < 12; month--) {
		    LinkedHashMap<Integer, Integer> entry = new LinkedHashMap<Integer, Integer>();
		    entry.put(Integer.valueOf(year), Integer.valueOf(month));
		    months.add(entry);
		    counter++;
		}
	    }
	}
	return months;
    }

    public static List<String> getAllPerProperties() {
	List<String> properties = new ArrayList<String>();
	properties.add("operatingResult2");
	properties.add("operatingResult1");
	properties.add("primaryResult");
	properties.add("sumSales");
	properties.add("externalSales");
	properties.add("internalPrimarySales");
	properties.add("sumPrimaryCosts");
	properties.add("purchases");
	properties.add("personnelCosts");
	properties.add("depreciation");
	properties.add("restCosts");
	properties.add("secondaryResultDB");
	properties.add("secondaryClearingDB");
	properties.add("secondaryDebitDB");
	properties.add("secondaryResultAFL");
	properties.add("secondaryClearingAFL");
	properties.add("secondaryClearingAFLOwn");
	properties.add("secondaryClearingAFLExt");
	properties.add("secondaryDebitAFL");
	properties.add("secondaryDebitAFLOwn");
	properties.add("secondaryDebitAFLExt");
	properties.add("operatingMargin");
	properties.add("vzp");
	properties.add("nap");
	properties.add("overallSales");
	properties.add("totalClearing");
	properties.add("totalDebit");
	return properties;
    }
}
