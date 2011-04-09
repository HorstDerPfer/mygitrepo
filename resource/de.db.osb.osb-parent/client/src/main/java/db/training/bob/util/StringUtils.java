package db.training.bob.util;

/**
 * Einige Hilfsmethoden fuer den Umgang mit Strings
 * 
 * @author Sebastian Hennebrueder Date: 07.04.2010
 */
public final class StringUtils {
	private static final String COMMA = ",";

	/**
	 * Erstellt einen komma separierten String aus den uebergebenen Werten
	 * 
	 * @param params
	 *            - ein oder mehrere Werte
	 * @return - die Parameter kommasepariert
	 */
	public static String joinWithComma(Object... params) {
		return join(COMMA, params);
	}

	/**
	 * Erstellt einen String mit dem {@code separator} separierten uebergebenen Werten
	 * 
	 * @param params
	 *            - ein oder mehrere Werte
	 * @param separator
	 *            - das Trennzeichen
	 * @return - die Parameter kommasepariert
	 */
	public static String join(String separator, Object... params) {
		boolean first = true;
		StringBuilder builder = new StringBuilder();
		for (Object param : params) {
			if (param == null)
				continue;
			if (first)
				first = false;
			else
				builder.append(separator);
			builder.append(param.toString());
		}
		return builder.toString();
	}

	/**
	 * Guaranties a non null values
	 * 
	 * @param value
	 *            - the value which may be null
	 * @return - the value if not null else an empty String
	 */
	public static String coalesc(Object value) {
		return value == null ? "" : value.toString();
	}
}
