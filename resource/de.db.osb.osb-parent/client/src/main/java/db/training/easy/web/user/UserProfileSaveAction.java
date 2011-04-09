package db.training.easy.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.SecurityAdministrationService;
import db.training.security.SecurityException;
import db.training.security.hibernate.TqmUser;
import db.training.security.password.PasswordException;

public class UserProfileSaveAction extends BaseAction {

	private static Logger log = Logger.getLogger(UserProfileSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UserProfileSaveAction.");

		TqmUser secUser = getSecUser();

		UserProfileForm userForm = (UserProfileForm) form;

		if (userForm.isChangePassword()) {
			if (secUser == null) {
				if (log.isDebugEnabled())
					log.debug("secUser not found!");
				addError("error.user.notfound");
				return mapping.findForward("FAILURE");
			}

			SecurityAdministrationService secService = serviceFactory
			    .createSecurityAdministrationService();

			try {
				secService.changePassword(secUser, userForm.getOldPassword(), userForm
				    .getPassword());
			} catch (SecurityException e) {
				e.printStackTrace();
				addError("error.security.password.BAD_PASSWORD");
				return mapping.findForward("FAILURE");
			} catch (PasswordException e) {
				switch (e.getDescription()) {
				case EMPTY_PASSWORD:
					addError("error.security.password.EMPTY_PASSWORD");
					break;
				case PASSWORD_TO_SHORT:
					addError("error.security.password.PASSWORD_TO_SHORT");
					break;
				case REUSED_PASSWORD:
					addError("error.security.password.REUSED_PASSWORD");
					break;
				case TRIVIAL_PASSWORD:
					addError("error.security.password.TRIVIAL_PASSWORD");
					break;
				case UNSAFE_PASSWORD:
					addError("error.security.password.UNSAFE_PASSWORD");
					break;
				}
				return mapping.findForward("FAILURE");
			}
		}

		addMessage("success.userprofile.save");
		return mapping.findForward("SUCCESS");
	}
}
