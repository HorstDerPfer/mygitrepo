package db.training.easy.util;

/**
 * Klasse mit mathematischen Hilfsfunktionen
 * 
 * @author FlorianHueter
 * 
 */
public class MathEasy {

    /**
         * Addiert beliebig viele Double-Werte und gibt das Ergebnis als Double zurueck. Nur wenn
         * alle Werte null sind, wird null zurueckgegeben.
         * 
         * @param Doubles
         * @return
         */
    public static Double sum(final Double... Doubles) {
	double res = 0;
	int countNulls = 0;
	for (Double dblVal : Doubles) {
	    if (dblVal == null)
		countNulls++;
	    else
		res += dblVal.doubleValue();
	}
	if (countNulls == Doubles.length)
	    return null;
	return res;
    }

    /**
         * Addiert beliebig viele Double-Werte und gibt das Ergebnis als Double zurueck. Null-Werte
         * werden hierbei wie 0 bewertet.
         * 
         * @param Doubles
         * @return
         */
    public static Double sumNull(final Double... Doubles) {
	double res = 0;
	for (Double dblVal : Doubles) {
	    if (dblVal != null)
		res += dblVal.doubleValue();
	}
	return res;
    }

    /**
         * Addiert beliebig viele Integer-Werte und gibt das Ergebnis als Integer zurueck. Ist einer
         * der uebergebenen Werte null, so liefert die Funktion null zurueck
         * 
         * @param Integers
         * @return
         */
    public static Integer sum(final Integer... Integers) {
	int res = 0;
	int countNulls = 0;
	for (Integer intVal : Integers) {
	    if (intVal == null)
		countNulls++;
	    else
		res += intVal.intValue();
	}
	if (countNulls == Integers.length)
	    return null;
	return res;
    }

    /**
         * Subtrahiert den zweiten Double-Parameter vom ersten und liefert das Ergebnis als Double
         * zurueck
         * 
         * @param first
         * @param second
         * @return
         */
    public static Double diff(final Double first, final Double second) {
	if (first == null)
	    return null;
	if (second == null)
	    return first;
	return first.doubleValue() - second.doubleValue();
    }

    /**
         * Subtrahiert den zweiten Integer-Parameter vom ersten und liefert das Ergebnis als Integer
         * zurueck
         * 
         * @param first
         * @param second
         * @return
         */
    public static Integer diff(final Integer first, final Integer second) {
	if (first == null)
	    return null;
	if (second == null)
	    return first;
	return first - second;
    }
}