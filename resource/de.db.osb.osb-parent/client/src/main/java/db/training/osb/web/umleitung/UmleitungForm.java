package db.training.osb.web.umleitung;

import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class UmleitungForm extends ValidatorForm {

	private Integer umleitungId;

	private String umleitungName;

	private String vzgStrecke;

	private String betriebsstelleVon;

	private String betriebsstelleBis;

	private String freieKapaRichtung;

	private String freieKapaGegenrichtung;

	private Integer saveFlag;

	private Boolean delete;

	public void reset() {
		umleitungId = null;
		umleitungName = null;
		vzgStrecke = null;
		betriebsstelleVon = null;
		betriebsstelleBis = null;
		freieKapaRichtung = null;
		freieKapaGegenrichtung = null;
		saveFlag = null;
		delete = null;
	}

	public Integer getUmleitungId() {
		return umleitungId;
	}

	public void setUmleitungId(Integer umleitungId) {
		this.umleitungId = umleitungId;
	}

	public String getUmleitungName() {
		return umleitungName;
	}

	public void setUmleitungName(String umleitungName) {
		this.umleitungName = umleitungName;
	}

	public String getVzgStrecke() {
		return vzgStrecke;
	}

	public void setVzgStrecke(String vzgStrecke) {
		this.vzgStrecke = vzgStrecke;
	}

	public String getBetriebsstelleVon() {
		return betriebsstelleVon;
	}

	public void setBetriebsstelleVon(String betriebsstelleVon) {
		this.betriebsstelleVon = betriebsstelleVon;
	}

	public String getBetriebsstelleBis() {
		return betriebsstelleBis;
	}

	public void setBetriebsstelleBis(String betriebsstelleBis) {
		this.betriebsstelleBis = betriebsstelleBis;
	}

	/**
	 * @return the freieKapaRichtung
	 */
	public String getFreieKapaRichtung() {
		return freieKapaRichtung;
	}

	/**
	 * @param freieKapaRichtung
	 *            the freieKapaRichtung to set
	 */
	public void setFreieKapaRichtung(String freieKapaRichtung) {
		this.freieKapaRichtung = freieKapaRichtung;
	}

	/**
	 * @return the freieKapaGegenrichtung
	 */
	public String getFreieKapaGegenrichtung() {
		return freieKapaGegenrichtung;
	}

	/**
	 * @param freieKapaGegenrichtung
	 *            the freieKapaGegenrichtung to set
	 */
	public void setFreieKapaGegenrichtung(String freieKapaGegenrichtung) {
		this.freieKapaGegenrichtung = freieKapaGegenrichtung;
	}

	/**
	 * @return the saveFlag
	 */
	public Integer getSaveFlag() {
		return saveFlag;
	}

	/**
	 * @param saveFlag
	 *            the saveFlag to set
	 */
	public void setSaveFlag(Integer saveFlag) {
		this.saveFlag = saveFlag;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

}
