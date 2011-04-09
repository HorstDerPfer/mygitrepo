package db.training.osb.web.korridor;

import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class KorridorForm extends ValidatorForm {

	public Integer korridorId;

	private String name;

	public void reset() {
		korridorId = null;
		name = null;
	}

	public Integer getKorridorId() {
		return korridorId;
	}

	public void setKorridorId(Integer korridorId) {
		this.korridorId = korridorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
