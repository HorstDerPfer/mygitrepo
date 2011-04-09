package db.training.osb.web.paket;

import org.apache.struts.validator.ValidatorForm;

@SuppressWarnings("serial")
public class PaketForm extends ValidatorForm {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
