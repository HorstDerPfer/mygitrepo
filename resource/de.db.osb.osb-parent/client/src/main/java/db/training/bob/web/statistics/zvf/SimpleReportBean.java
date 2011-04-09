package db.training.bob.web.statistics.zvf;

import db.training.bob.web.statistics.AbstractReportBean;

public class SimpleReportBean extends AbstractReportBean {

	private int anzahl = 0;

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public SimpleReportBean(String key) {
		super(key);
	}
	
	/**
	 * Erhöht die Anzahl.
	 * 
	 * @param increment Zahl, um die die Anzahl erhöht wird.
	 */
	public int add(int increment) {
		this.anzahl = this.anzahl+ increment;
		return this.anzahl;
	}
	
	/**
	 * Veringert die Anzahl.
	 * 
	 * @param decrement Zahl, um die die Anzahl verringert wird.
	 */
	public int substract(int decrement) {
		this.anzahl = this.anzahl - decrement;
		return this.anzahl;
	}

}
