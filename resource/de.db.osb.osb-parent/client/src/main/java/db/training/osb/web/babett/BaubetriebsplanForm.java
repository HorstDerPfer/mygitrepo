package db.training.osb.web.babett;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @author Sebastian Hennebrueder Date: 24.02.2010
 */
public class BaubetriebsplanForm extends ActionForm {

	private static final long serialVersionUID = -6101726657772868136L;

	private Integer massnahmeId;

	public Integer getMassnahmeId() {
		return massnahmeId;
	}

	public void setMassnahmeId(Integer massnahmeId) {
		this.massnahmeId = massnahmeId;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (massnahmeId == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			    "error.baumassnahme.notfound"));
		}
		return errors;
	}
}
