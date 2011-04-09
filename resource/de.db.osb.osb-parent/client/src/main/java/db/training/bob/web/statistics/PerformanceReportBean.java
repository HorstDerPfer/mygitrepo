package db.training.bob.web.statistics;

public class PerformanceReportBean extends AbstractReportBean {

	private int geregelteTrassen;

	private int ueberarbeiteteTrassen;

	private int anzahlBiUe;

	private int veroeffentlichteTrassen;

	public PerformanceReportBean(String key) {
		this(key, 0, 0, 0, 0);
	}

	public PerformanceReportBean(String key, int geregelteTrassen, int ueberarbeiteteTrassen,
	    int anzahlBiUe, int veroeffentlichteTrassen) {
		super(key);
		setGeregelteTrassen(geregelteTrassen);
		setUeberarbeiteteTrassen(ueberarbeiteteTrassen);
		setAnzahlBiUe(anzahlBiUe);
		setVeroeffentlichteTrassen(veroeffentlichteTrassen);
	}

	public int getGeregelteTrassen() {
		return geregelteTrassen;
	}

	public void setGeregelteTrassen(int geregelteTrassen) {
		this.geregelteTrassen = geregelteTrassen;
	}

	public int getUeberarbeiteteTrassen() {
		return ueberarbeiteteTrassen;
	}

	public void setUeberarbeiteteTrassen(int ueberarbeiteteTrassen) {
		this.ueberarbeiteteTrassen = ueberarbeiteteTrassen;
	}

	public int getAnzahlBiUe() {
		return anzahlBiUe;
	}

	public void setAnzahlBiUe(int anzahlBiUe) {
		this.anzahlBiUe = anzahlBiUe;
	}

	public int getVeroeffentlichteTrassen() {
		return veroeffentlichteTrassen;
	}

	public void setVeroeffentlichteTrassen(int veroeffentlichteTrassen) {
		this.veroeffentlichteTrassen = veroeffentlichteTrassen;
	}

}
