package db.training.osb.web.masterbuendel;

import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class MasterBuendelForm extends ValidatorForm {

	public Integer masterBuendelId;

	/**
	 * Angabe des zugeordneten Korridors. Obwohl im Datenmodell n:m-Verknüpfung, wird im Frontend
	 * nur 1:n zugelassen.
	 */
	public Integer korridorId;

	public void reset() {
		masterBuendelId = null;
	}

	public Integer getMasterBuendelId() {
		return masterBuendelId;
	}

	public void setMasterBuendelId(Integer masterBuendelId) {
		this.masterBuendelId = masterBuendelId;
	}

	public Integer getKorridorId() {
		return korridorId;
	}

	public void setKorridorId(Integer korridorId) {
		this.korridorId = korridorId;
	}

}
