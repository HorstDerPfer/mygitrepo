package db.training.web.administration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import db.training.easy.util.FrontendHelper;

@SuppressWarnings("serial")
public class EditableNachbarbahnListItemForm extends ValidatorForm {

    private Integer id;

    private String updateName;

    private String insertName;

    public EditableNachbarbahnListItemForm() {
	reset();
    }

    public void reset() {
	setId(null);
	setUpdateName(null);
	setInsertName(null);
    }

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
	ActionErrors actionErrors = super.validate(arg0, request);

	String method = request.getParameter("method");

	if (FrontendHelper.stringNotNullOrEmpty(method)) {
	    if (method.equalsIgnoreCase("insert")) {
		if (!FrontendHelper.stringNotNullOrEmpty(getInsertName())) {
		    actionErrors.add("insertName", new ActionMessage("error.required.common"));
		}
	    }
	    if (method.equalsIgnoreCase("update")) {
		if (!FrontendHelper.stringNotNullOrEmpty(getUpdateName())) {
		    actionErrors.add("updateName", new ActionMessage("error.required.common"));
		}
	    }
	}
	return actionErrors;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setUpdateName(String name) {
	this.updateName = name;
    }

    public String getUpdateName() {
	return updateName;
    }

    public void setInsertName(String insertName) {
	this.insertName = insertName;
    }

    public String getInsertName() {
	return insertName;
    }
}
