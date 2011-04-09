package db.training.bob.web.baumassnahme;

import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class BBPMassnahmeForm extends ValidatorForm {
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
