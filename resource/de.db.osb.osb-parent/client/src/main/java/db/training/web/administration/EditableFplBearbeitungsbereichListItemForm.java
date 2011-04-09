package db.training.web.administration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import db.training.easy.util.FrontendHelper;

@SuppressWarnings("serial")
public class EditableFplBearbeitungsbereichListItemForm extends ValidatorForm {

	private Integer id;

	private String updateRegionalbereichFpl;

	private Integer updateRegionalbereichFplId;

	private String updateName;

	private String insertRegionalbereichFplId;

	private String insertName;

	private String updateVorgangsnrMin;

	private String updateVorgangsnrMax;

	private boolean readonly;

	public EditableFplBearbeitungsbereichListItemForm() {
		reset();
	}

	public void reset() {
		setId(null);
		setUpdateRegionalbereichFpl(null);
		setUpdateName(null);
		setInsertRegionalbereichFplId(null);
		setInsertName(null);
		setUpdateVorgangsnrMin(null);
		setUpdateVorgangsnrMax(null);
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest request) {
		ActionErrors actionErrors = super.validate(arg0, request);

		String method = request.getParameter("method");

		if (FrontendHelper.stringNotNullOrEmpty(method)) {
			if (method.equalsIgnoreCase("insert")) {
				if (!FrontendHelper.stringNotNullOrEmpty(getInsertRegionalbereichFplId())) {
					actionErrors.add("error.required", new ActionMessage("error.required",
					    "Regionalbereich"));
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getInsertName())) {
					actionErrors.add("error.required", new ActionMessage("error.required",
					    "Name des Bearbeitungsbereiches"));
				}
			}
			if (method.equalsIgnoreCase("update")) {
				if (readonly == false) {
					if (!FrontendHelper.stringNotNullOrEmpty(getUpdateName())) {
						actionErrors.add("error.required", new ActionMessage("error.required",
						    "Name des Bearbeitungsbereiches"));
					}
				}
				if (!FrontendHelper.stringNotNullOrEmpty(getUpdateVorgangsnrMin())
				    || !FrontendHelper.stringNotNullOrEmpty(getUpdateVorgangsnrMax())) {
					actionErrors.add("error.required", new ActionMessage("error.required",
					    "Vorgangsnummern"));
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

	public void setUpdateRegionalbereichFpl(String regionalbereichFpl) {
		this.updateRegionalbereichFpl = regionalbereichFpl;
	}

	public String getUpdateRegionalbereichFpl() {
		return updateRegionalbereichFpl;
	}

	public void setUpdateRegionalbereichFplId(Integer regionalbereichFplId) {
		this.updateRegionalbereichFplId = regionalbereichFplId;
	}

	public Integer getUpdateRegionalbereichFplId() {
		return updateRegionalbereichFplId;
	}

	public void setUpdateName(String name) {
		this.updateName = name;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setInsertRegionalbereichFplId(String insertRegionalbereichFplId) {
		this.insertRegionalbereichFplId = insertRegionalbereichFplId;
	}

	public String getInsertRegionalbereichFplId() {
		return insertRegionalbereichFplId;
	}

	public void setInsertName(String insertName) {
		this.insertName = insertName;
	}

	public String getInsertName() {
		return insertName;
	}

	public String getUpdateVorgangsnrMin() {
		return updateVorgangsnrMin;
	}

	public void setUpdateVorgangsnrMin(String updateVorgangsnrMin) {
		this.updateVorgangsnrMin = updateVorgangsnrMin;
	}

	public String getUpdateVorgangsnrMax() {
		return updateVorgangsnrMax;
	}

	public void setUpdateVorgangsnrMax(String updateVorgangsnrMax) {
		this.updateVorgangsnrMax = updateVorgangsnrMax;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
}
