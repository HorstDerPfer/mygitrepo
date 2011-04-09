package db.training.easy.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class UserProfileEditAction extends BaseAction {

	private static Logger log = Logger.getLogger(UserProfileEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UserProfileEditAction.");

		TqmUser secUser = getSecUser();

		UserProfileForm userForm = (UserProfileForm) form;

		if (secUser == null) {
			if (log.isDebugEnabled())
				log.debug("secUser not found!");
			addError("error.user.notfound");
			return mapping.findForward("FAILURE");
		}

		if (!hasErrors(request))
			userForm.reset();

		request.setAttribute("secUser", secUser);
		return mapping.findForward("SUCCESS");
	}
}
