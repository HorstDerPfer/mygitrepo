package db.training.easy.web.admin.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.UserService;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.SecurityAdministrationService;
import db.training.security.SecurityService;
import db.training.security.hibernate.TqmRole;
import db.training.security.hibernate.TqmUser;

public class UserEditAction extends BaseAction {

	private static Logger log = Logger.getLogger(UserEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UserEditAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		User user = null;
		UserForm userForm = (UserForm) form;
		Integer userId = null;

		// request-ID wird in SaveAction gesetzt und somit nur nach forward genutzt
		if (request.getAttribute("userId") != null)
			userId = (Integer) request.getAttribute("userId");
		else
			userId = userForm.getUserId();

		if (userId == null) {
			if (log.isDebugEnabled())
				log.debug("No userId given. Setting \"0\"");
			userId = 0;
		}
		if (log.isDebugEnabled())
			log.debug("Processing userId: " + userId);

		if (userId != 0) {
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

			// Rechteprüfung: Benutzer bearbeiten
			if (serviceFactory.createUserAnyVoter().vote(secUser, user, "ROLE_BENUTZER_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");

			if (!hasErrors(request)) {
				userForm.reset();
				userForm.setUserId(userId);
				if (user.getRegionalbereich() != null)
					userForm.setRegionalbereichId(user.getRegionalbereich().getId());
				userForm.setLoginName(user.getLoginName());
				userForm.setChangeLogin(false);
				userForm.setName(user.getName());
				userForm.setFirstName(user.getFirstName());
				userForm.setEmail(user.getEmail());
			}

			if (user.getLoginName() != null) {
				SecurityService securityService = serviceFactory.createSecurityService();
				TqmUser secUserEdit;

				try {
					secUserEdit = (TqmUser) securityService.loadUserByUsername(user.getLoginName());
					if (secUserEdit.isEnabled())
						userForm.setDisabled(false);
					else
						userForm.setDisabled(true);
					if (secUserEdit.isAccountNonLocked())
						userForm.setLocked(false);
					else
						userForm.setLocked(true);

					SecurityAdministrationService secAdminService = serviceFactory
					    .createSecurityAdministrationService();
					List<TqmRole> roles = secAdminService.findAllRoles();
					for (TqmRole role : roles) {
						if (secUserEdit.getRoles().contains(role))
							request.setAttribute("role_" + role.getId(), true);
						else
							request.setAttribute("role_" + role.getId(), false);
					}
				} catch (UsernameNotFoundException e) {
					secUserEdit = null;
					e.printStackTrace();
				}

				request.setAttribute("secUser", secUserEdit);
			}
		} else {
			// Rechteprüfung: Benutzer anlegen
			if (!secUser.hasAuthorization("ROLE_BENUTZER_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_BENUTZER_ANLEGEN_REGIONALBEREICH"))
				return mapping.findForward("ACCESS_DENIED");

			if (!hasErrors(request))
				userForm.reset();
			userForm.setUserId(0);
			if (loginUser.getRegionalbereich() != null)
				userForm.setRegionalbereichId(loginUser.getRegionalbereich().getId());
			if (loginUser.getBearbeitungsbereich() != null)
				userForm.setBearbeitungsbereichId(loginUser.getBearbeitungsbereich().getId());
			userForm.setChangeLogin(true);
		}

		RegionalbereichService regionalbereichService = serviceFactory
		    .createRegionalbereichService();
		List<Regionalbereich> regionalbereiche = new ArrayList<Regionalbereich>();

		if (secUser.hasAuthorization("ROLE_BENUTZER_BEARBEITEN_ALLE")) {
			regionalbereiche = regionalbereichService.findAll();
		} else if (secUser.hasAuthorization("ROLE_BENUTZER_BEARBEITEN_REGIONALBEREICH")) {
			regionalbereiche.add(loginUser.getRegionalbereich());
		}
		request.setAttribute("regionalbereiche", regionalbereiche);

		SecurityAdministrationService secAdminService = serviceFactory
		    .createSecurityAdministrationService();

		request.setAttribute("roles", secAdminService.findAllRoles());
		request.setAttribute("user", user);
		return mapping.findForward("SUCCESS");
	}
}
