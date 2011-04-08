package fm.tutorial.web;

import org.apache.struts.action.ActionForm;

public class HelloStrutsForm extends ActionForm {

	private static final long serialVersionUID = 3173956521544643337L;

	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
