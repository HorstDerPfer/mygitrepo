package db.training.osb.web.imports;

public class ImportMessage implements Comparable<ImportMessage> {
	public enum Level {
		WARNING, ERROR
	}

	/*
	 * Art des Fehlers, Warnung Wert musste erraten werden, Fehler Wert konnte nicht ermittelt
	 * werden
	 */
	private Level level;

	/* Ursache des Fehlers */
	private String reason;

	public ImportMessage(Level level, String reason) {
		super();
		this.level = level;
		this.reason = reason;
	}

	public int compareTo(ImportMessage that) {
		if (this == null) {
			if (that == null)
				return 0;
			return 1;
		}

		if (that == null)
			return -1;

		if (this.level.equals(that.level) && this.reason.equals(that.reason))
			return 0;

		return this.level.equals(Level.ERROR) ? -1 : 1;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return String.format("%s level=%s, reason=%s", getClass().getSimpleName(), level, reason);
	}

}
