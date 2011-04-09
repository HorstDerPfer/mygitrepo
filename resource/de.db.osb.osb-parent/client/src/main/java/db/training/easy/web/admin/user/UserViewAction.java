package db.training.easy.web.admin.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.model.User;
import db.training.easy.core.service.UserService;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.SecurityService;
import db.training.security.hibernate.TqmUser;

public class UserViewAction extends BaseAction {

	private static Logger log = Logger.getLogger(UserViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UserViewAction.");

		TqmUser secUser = getSecUser();

		User user = null;
		UserForm userForm = (UserForm) form;
		Integer userId = userForm.getUserId();

		if (userId == null || userId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("User not found: " + userId);
			addError("error.user.notfound");
			return mapping.findForward("FAILURE");
		}

		UserService userService = serviceFactory.createUserService();
		user = userService.findUserById(userId);

		if (user == null) {
			if (log.isDebugEnabled())
				log.debug("User not found: " + userId);
			addError("error.user.notfound");
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing user: " + user.getId());

		// Rechteprüfung: Benutzer lesen
		if (serviceFactory.createUserAnyVoter().vote(secUser, user, "ROLE_BENUTZER_LESEN") == AccessDecisionVoter.ACCESS_DENIED)
			return mapping.findForward("ACCESS_DENIED");

		if (user.getLoginName() != null) {
			SecurityService securityService = serviceFactory.createSecurityService();
			TqmUser securityUser;

			try {
				securityUser = (TqmUser) securityService.loadUserByUsername(user.getLoginName());
			} catch (UsernameNotFoundException e) {
				securityUser = null;
				e.printStackTrace();
			}

			request.setAttribute("secUser", securityUser);
		}

		request.setAttribute("user", user);
		return mapping.findForward("SUCCESS");
	}
}
