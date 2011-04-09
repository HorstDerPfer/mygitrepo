package db.training.osb.web.baustelle;

import db.training.easy.web.BaseValidatorForm;

@SuppressWarnings("serial")
public class BaustelleForm extends BaseValidatorForm {

	private Integer lfdNr;

	private Integer id;

	private String name;

	public BaustelleForm() {
		reset();
	}

	public void reset() {
		setLfdNr(null);
		setId(null);
		setName(null);
	}

	public Integer getLfdNr() {
		return lfdNr;
	}

	public void setLfdNr(Integer lfdNr) {
		this.lfdNr = lfdNr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
