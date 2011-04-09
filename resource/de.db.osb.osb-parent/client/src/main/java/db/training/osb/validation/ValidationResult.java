package db.training.osb.validation;

import org.apache.struts.action.ActionMessage;

public class ValidationResult {

	private String property;

	private ActionMessage actionError;

	public ValidationResult(String property, ActionMessage actionError) {
		this.property = property;
		this.actionError = actionError;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public ActionMessage getActionError() {
		return actionError;
	}

	public void setActionError(ActionMessage actionError) {
		this.actionError = actionError;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", property, actionError.toString());
	}
}
