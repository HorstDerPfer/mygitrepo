package db.training.easy.util.displaytag.export;

/**
 * CSV Export für Display Tag Library angepasst nach den Anforderungen aus Ticket #657
 * 
 * @author michels
 * 
 */
public class CsvView extends org.displaytag.export.CsvView {

	@Override
	protected String getCellEnd() {
		return ";";
	}

	@Override
	protected String escapeColumnValue(Object value) {
		String escapedResult = super.escapeColumnValue(value);

		if (escapedResult == null || "".equals(escapedResult)) {
			return escapedResult;
		}

		StringBuilder result = new StringBuilder();

		if (!escapedResult.startsWith("\""))
			result.append("\"");

		result.append(escapedResult);

		if (!escapedResult.endsWith("\""))
			result.append("\"");

		return result.toString();
	}
}
