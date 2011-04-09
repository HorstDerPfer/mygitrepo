package db.training.bob.web.controlling;

import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class ControllingFilterForm extends ValidatorForm {
	private String beginnDatum = null;

	private String endDatum = null;

	private String regionalbereich = null;

	private String meilenstein = null;

	public void reset() {
		beginnDatum = null;
		endDatum = null;
		regionalbereich = null;
		meilenstein = null;
	}

	/**
	 * @param beginnDatum the beginnDatum to set
	 */
	public void setBeginnDatum(String beginnDatum) {
		this.beginnDatum = beginnDatum;
	}

	/**
	 * @return the beginnDatum
	 */
	public String getBeginnDatum() {
		return beginnDatum;
	}

	/**
	 * @param endDatum the endDatum to set
	 */
	public void setEndDatum(String endDatum) {
		this.endDatum = endDatum;
	}

	/**
	 * @return the endDatum
	 */
	public String getEndDatum() {
		return endDatum;
	}

	/**
	 * @param regionalbereich the regionalbereich to set
	 */
	public void setRegionalbereich(String regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	/**
	 * @return the regionalbereich
	 */
	public String getRegionalbereich() {
		return regionalbereich;
	}

	/**
	 * @param meilenstein the meilenstein to set
	 */
	public void setMeilenstein(String meilenstein) {
		this.meilenstein = meilenstein;
	}

	/**
	 * @return the meilenstein
	 */
	public String getMeilenstein() {
		return meilenstein;
	}
}
