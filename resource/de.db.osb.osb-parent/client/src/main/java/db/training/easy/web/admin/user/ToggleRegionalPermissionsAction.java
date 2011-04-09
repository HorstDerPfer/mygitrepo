package db.training.easy.web.admin.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class ToggleRegionalPermissionsAction extends BaseAction {

	private static Logger log = Logger.getLogger(ToggleRegionalPermissionsAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering ToggleRegionalPermissionsAction.");

		TqmUser secUser = getSecUser();

		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		String regionalPermissions = System.getProperty("regionalPermissions");

		if (regionalPermissions == null || regionalPermissions.equals("false")) {
			regionalPermissions = "true";
			addMessage("success.regionalPermissions.enabled");
		} else {
			regionalPermissions = "false";
			addMessage("success.regionalPermissions.disabled");
		}

		System.setProperty("regionalPermissions", regionalPermissions);
		request.setAttribute("regionalPermissions", regionalPermissions);

		return mapping.findForward("SUCCESS");
	}
}
