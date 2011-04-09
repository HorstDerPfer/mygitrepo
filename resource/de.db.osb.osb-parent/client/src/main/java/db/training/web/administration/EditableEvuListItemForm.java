package db.training.web.administration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import db.training.easy.util.FrontendHelper;

@SuppressWarnings("serial")
public class EditableEvuListItemForm extends ValidatorForm {

	private Integer id;

	private String updateKundenNr;

	private String updateName;

	private String updateKurzbezeichnung;

	private String updateEvugruppe;

	private String insertKundenNr;

	private String insertName;

	private String insertKurzbezeichnung;

	private String insertEvugruppe;
	
	private String insertEvugruppeId;
	
	private String updateEvugruppeId;
	

	public EditableEvuListItemForm() {
		reset();
	}

	public void reset() {
		setId(null);
		setUpdateKundenNr(null);
		setUpdateName(null);
		setUpdateKurzbezeichnung(null);
		setUpdateEvugruppe(null);
		setInsertKundenNr(null);
		setInsertName(null);
		setInsertKurzbezeichnung(null);
		setInsertEvugruppe(null);
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);

		String method = request.getParameter("method");

		if (FrontendHelper.stringNotNullOrEmpty(method)) {
			if (method.equalsIgnoreCase("insert")) {
				if (!FrontendHelper.stringNotNullOrEmpty(getInsertKundenNr())) {
					actionErrors.add("insertKundenNr", new ActionMessage("error.required.common"));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getInsertName())) {
					actionErrors.add("insertName", new ActionMessage("error.required.common"));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getInsertName())) {
					actionErrors.add("insertKurzbezeichnung", new ActionMessage(
					    "error.required.common"));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getInsertEvugruppe())) {
					actionErrors.add("insertEvugruppe", new ActionMessage("error.required.common"));
				}
			}
			if (method.equalsIgnoreCase("update")) {
				if (!FrontendHelper.stringNotNullOrEmpty(getUpdateKundenNr())) {
					actionErrors.add("updateKundenNr", new ActionMessage("error.required.common"));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getUpdateName())) {
					actionErrors.add("updateName", new ActionMessage("error.required.common"));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getUpdateName())) {
					actionErrors.add("updateKurzbezeichnung", new ActionMessage(
					    "error.required.common"));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getUpdateEvugruppe())) {
					actionErrors.add("updateEvugruppe", new ActionMessage("error.required.common"));
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

	public void setUpdateKundenNr(String kundenNr) {
		this.updateKundenNr = kundenNr;
	}

	public String getUpdateKundenNr() {
		return updateKundenNr;
	}

	public void setUpdateName(String name) {
		this.updateName = name;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateKurzbezeichnung(String kurzbezeichnung) {
		this.updateKurzbezeichnung = kurzbezeichnung;
	}

	public String getUpdateKurzbezeichnung() {
		return updateKurzbezeichnung;
	}

	public void setUpdateEvugruppe(String evugruppe) {
		this.updateEvugruppe = evugruppe;
	}

	public String getUpdateEvugruppe() {
		return updateEvugruppe;
	}

	public void setInsertKundenNr(String insertKundenNr) {
		this.insertKundenNr = insertKundenNr;
	}

	public String getInsertKundenNr() {
		return insertKundenNr;
	}

	public void setInsertName(String insertName) {
		this.insertName = insertName;
	}

	public String getInsertName() {
		return insertName;
	}

	public void setInsertKurzbezeichnung(String insertKurzbezeichnung) {
		this.insertKurzbezeichnung = insertKurzbezeichnung;
	}

	public String getInsertKurzbezeichnung() {
		return insertKurzbezeichnung;
	}

	public void setInsertEvugruppe(String insertEvugruppe) {
		this.insertEvugruppe = insertEvugruppe;
	}

	public String getInsertEvugruppe() {
		return insertEvugruppe;
	}

	public String getInsertEvugruppeId() {
    	return insertEvugruppeId;
    }

	public String getUpdateEvugruppeId() {
    	return updateEvugruppeId;
    }

	public void setInsertEvugruppeId(String insertEvugruppeId) {
    	this.insertEvugruppeId = insertEvugruppeId;
    }

	public void setUpdateEvugruppeId(String updateEvugruppeId) {
    	this.updateEvugruppeId = updateEvugruppeId;
    }
}
