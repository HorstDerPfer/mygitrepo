package db.training.easy.web.admin.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.exception.ConstraintViolationException;

import db.training.easy.core.model.User;
import db.training.easy.core.service.UserService;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.SecurityAdministrationService;
import db.training.security.SecurityService;
import db.training.security.hibernate.TqmUser;

public class UserDeleteAction extends BaseAction {

	private static Logger log = Logger.getLogger(UserDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering /admin/UserDeleteAction.");

		TqmUser secUser = getSecUser();

		UserForm userForm = (UserForm) form;
		Integer userId = userForm.getUserId();

		if (log.isDebugEnabled())
			log.debug("Processing userId: " + userId);

		UserService userService = serviceFactory.createUserService();
		User user = userService.findUserById(userId);

		if (user == null) {
			if (log.isDebugEnabled())
				log.debug("User not found: " + userId);
			addError("error.user.notfound");
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Deleting user: " + user);

		// Rechteprüfung: Benutzer löschen
		if (serviceFactory.createUserAnyVoter().vote(secUser, user, "ROLE_BENUTZER_LOESCHEN") == AccessDecisionVoter.ACCESS_DENIED)
			return mapping.findForward("ACCESS_DENIED");

		try {
			userService.deleteUser(userId);
		} catch (ConstraintViolationException cve) {
			addError("error.user.delete.constraint");
			return mapping.findForward("FAILURE");
		} catch (Exception e) {
			addError("error.user.delete");
			return mapping.findForward("FAILURE");
		}

		// securityUser
		SecurityService secService = serviceFactory.createSecurityService();
		SecurityAdministrationService secAdminService = serviceFactory
		    .createSecurityAdministrationService();
		TqmUser secUserEdit;
		try {
			secUserEdit = (TqmUser) secService.loadUserByUsername(user.getLoginName());
		} catch (UsernameNotFoundException e) {
			secUserEdit = null;
		}

		if (secUserEdit != null) {
			if (log.isDebugEnabled())
				log.debug("Deleting secUser: " + secUserEdit.getUsername());
			secAdminService.deleteUser(secUserEdit);
		}

		addMessage("success.user.deleted");

		return mapping.findForward("SUCCESS");
	}

}
