package db.training.easy.web.admin.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.UserService;
import db.training.easy.util.SMTP;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.util.ConfigResources;
import db.training.security.SecurityAdministrationService;
import db.training.security.SecurityService;
import db.training.security.hibernate.TqmRole;
import db.training.security.hibernate.TqmUser;
import db.training.security.password.PasswordException;
import db.training.security.password.PasswordGenerator;

public class UserSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(UserSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering /admin/UserSaveAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		User user = null;
		UserForm userForm = (UserForm) form;
		Integer userId = userForm.getUserId();

		UserService userService = serviceFactory.createUserService();

		if (userId == 0) {
			user = new User();
			if (log.isDebugEnabled())
				log.debug("Create new User: " + userId);
		} else {
			user = userService.findUserById(userId);
			if (log.isDebugEnabled())
				log.debug("Find User by Id: " + userId);
		}

		if (user == null) {
			if (log.isDebugEnabled())
				log.debug("User not found: " + userId);
			addError("error.user.notfound");
			return mapping.findForward("FAILURE");
		}

		RegionalbereichService regionalbereichService = serviceFactory
		    .createRegionalbereichService();
		user.setRegionalbereich(regionalbereichService.findById(userForm.getRegionalbereichId()));

		BearbeitungsbereichService bearbeitungsbereichService = serviceFactory
		    .createBearbeitungsbereichService();
		user.setBearbeitungsbereich(bearbeitungsbereichService.findById(userForm
		    .getBearbeitungsbereichId()));

		user.setName(userForm.getName().equals("") ? null : userForm.getName());
		user.setFirstName(userForm.getFirstName().equals("") ? null : userForm.getFirstName());
		user.setEmail(userForm.getEmail().equals("") ? null : userForm.getEmail());

		SecurityService secService = serviceFactory.createSecurityService();
		SecurityAdministrationService secAdminService = serviceFactory
		    .createSecurityAdministrationService();

		SMTP mail = new SMTP();

		MessageResources msgRes = getResources(request);

		// applicationUser ************************************************************************
		if (userId == 0) {
			// Rechteprüfung: Benutzer anlegen
			if (serviceFactory.createUserAnyVoter().vote(secUser, user, "ROLE_BENUTZER_ANLEGEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");

			userService.createUser(user);
			request.setAttribute("userId", user.getId());
		} else {
			// Rechteprüfung: Benutzer bearbeiten
			if (serviceFactory.createUserAnyVoter().vote(secUser, user, "ROLE_BENUTZER_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");

			userService.updateUser(user);
		}

		// #########################################################################################
		// # securityUser ##########################################################################
		// #########################################################################################

		TqmUser securityUser;
		try {
			securityUser = (TqmUser) secService.loadUserByUsername(user.getLoginName());
		} catch (UsernameNotFoundException e) {
			securityUser = null;
		}

		// angemeldeter Benutzer darf seinen eigenen Benutzernamen nicht ändern
		if (userForm.isChangeLogin() && !loginUser.getLoginName().equals(user.getLoginName())) {
			if (securityUser == null) { // !form.loginName.("") und Duplikate werden in Form
				// geprüft
				// securityUser anlegen
				String password = PasswordGenerator.createNewPassword();
				TqmUser userDetails = new TqmUser(userForm.getLoginName());
				// Benutzer muss sich sofort neu einloggen
				userDetails.setCredentialsNonExpired(false);
				try {
					secAdminService.createUser(userDetails, password);
					addMessage("success.user.login.create");

					try {
						String nameFrom = loginUser.getFirstName() + " " + loginUser.getName();
						String nameTo = userForm.getFirstName() + " " + userForm.getName();
						String applicationName = ConfigResources.getInstance()
						    .getApplicationTitle();
						String applicationUri = ConfigResources.getInstance().getApplicationUri();

						StringBuilder messageBuilder = new StringBuilder();

						messageBuilder.append(msgRes.getMessage("mail.login.create.text", nameTo,
						    applicationName, userForm.getLoginName(), password));
						messageBuilder.append(msgRes.getMessage("mail.link", applicationUri));
						messageBuilder.append(msgRes.getMessage("mail.footer", nameFrom));

						mail.sendEmail(user.getEmail(), ConfigResources.getInstance()
						    .getAutoMailSender(), msgRes.getMessage("mail.login.create.subject"),
						    messageBuilder.toString());

					} catch (Exception ex) {
						if (log.isInfoEnabled())
							log.info("error.sendMail: " + user.getEmail(), ex);
						addError("error.sendMail");
					}
				} catch (PasswordException e) {
					addError("errors.exception");
					return mapping.findForward("FAILURE");
				}

				// appUser anpassen / updaten
				user.setLoginName(userForm.getLoginName());
				userService.updateUser(user);
			} else {
				// Benutzername ändern und Passwort neu generieren und initialisieren
				// !form.loginName.("") und Duplikate werden in Form geprüft
				if (!securityUser.getUsername().equals(userForm.getLoginName())) {
					securityUser.setUsername(userForm.getLoginName());
					String password = PasswordGenerator.createNewPassword();
					securityUser.initPassword(secAdminService.createPasswordHash(password,
					    securityUser));

					secAdminService.updateUser(securityUser);
					addMessage("success.user.login.change");

					// updaten
					user.setLoginName(userForm.getLoginName());
					userService.updateUser(user);

					try {
						String nameFrom = loginUser.getFirstName() + " " + loginUser.getName();
						String nameTo = userForm.getFirstName() + " " + userForm.getName();
						String applicationName = ConfigResources.getInstance()
						    .getApplicationTitle();
						String applicationUri = ConfigResources.getInstance().getApplicationUri();

						StringBuilder messageBuilder = new StringBuilder();

						messageBuilder.append(msgRes.getMessage("mail.login.change.text", nameTo,
						    applicationName, userForm.getLoginName(), password));
						messageBuilder.append(msgRes.getMessage("mail.link", applicationUri));
						messageBuilder.append(msgRes.getMessage("mail.footer", nameFrom));

						mail.sendEmail(user.getEmail(), ConfigResources.getInstance()
						    .getAutoMailSender(), msgRes.getMessage("mail.login.change.subject"),
						    messageBuilder.toString());

					} catch (Exception ex) {
						if (log.isInfoEnabled())
							log.info("error.sendMail: " + user.getEmail(), ex);
						addError("error.sendMail");
					}
				}
			}
		}

		// securityUser updaten **************************************************************
		if (securityUser != null) {

			if (userForm.isDisabled())
				securityUser.disable();
			else
				securityUser.enable();

			if (userForm.isLocked())
				securityUser.lock();
			else
				securityUser.unlock();

			// Passwort auf Initialpasswort zurücksetzen
			if (userForm.isResetPassword()
			    && serviceFactory.createUserAnyVoter().vote(secUser, user,
			        "ROLE_BENUTZER_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				securityUser.resetPassword();
				addMessage("success.user.password.reset");

				try {
					String nameFrom = loginUser.getFirstName() + " " + loginUser.getName();
					String nameTo = userForm.getFirstName() + " " + userForm.getName();
					String applicationName = ConfigResources.getInstance().getApplicationTitle();
					String applicationUri = ConfigResources.getInstance().getApplicationUri();

					StringBuilder messageBuilder = new StringBuilder();

					messageBuilder.append(msgRes.getMessage("mail.password.reset.text", nameTo,
					    userForm.getLoginName(), applicationName));
					messageBuilder.append(msgRes.getMessage("mail.link", applicationUri));
					messageBuilder.append(msgRes.getMessage("mail.footer", nameFrom));

					mail.sendEmail(user.getEmail(), ConfigResources.getInstance()
					    .getAutoMailSender(), msgRes.getMessage("mail.password.reset.subject"),
					    messageBuilder.toString());

				} catch (Exception e) {
					if (log.isInfoEnabled())
						log.info("error.sendMail: " + user.getEmail(), e);
					addError("error.sendMail");
				}
			}

			// Passwort neu generieren und initialisieren
			if (userForm.isGeneratePassword()
			    && serviceFactory.createUserAnyVoter().vote(secUser, user,
			        "ROLE_BENUTZER_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {

				String password = PasswordGenerator.createNewPassword();
				securityUser.initPassword(secAdminService
				    .createPasswordHash(password, securityUser));
				addMessage("success.user.password.generate");

				try {
					String nameFrom = loginUser.getFirstName() + " " + loginUser.getName();
					String nameTo = userForm.getFirstName() + " " + userForm.getName();
					String applicationName = ConfigResources.getInstance().getApplicationTitle();
					String applicationUri = ConfigResources.getInstance().getApplicationUri();

					StringBuilder messageBuilder = new StringBuilder();

					messageBuilder.append(msgRes.getMessage("mail.password.init.text", nameTo,
					    applicationName, userForm.getLoginName(), password));
					messageBuilder.append(msgRes.getMessage("mail.link", applicationUri));
					messageBuilder.append(msgRes.getMessage("mail.footer", nameFrom));

					mail.sendEmail(user.getEmail(), ConfigResources.getInstance()
					    .getAutoMailSender(), msgRes.getMessage("mail.password.init.subject"),
					    messageBuilder.toString());

				} catch (Exception e) {
					if (log.isInfoEnabled())
						log.info("error.sendMail: " + user.getEmail(), e);
					addError("error.sendMail");
					e.printStackTrace();
				}
			}

			// Rollen setzten *********************************************
			if (serviceFactory.createUserAnyVoter().vote(secUser, user,
			    "ROLE_BENUTZER_ROLLEN_VERGEBEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				List<TqmRole> roles = secAdminService.findAllRoles();
				for (TqmRole role : roles) {
					if (request.getParameter("role_" + role.getId()) != null) {
						String hasRole = request.getParameter("role_" + role.getId());
						if (hasRole.equals("true") && !securityUser.getRoles().contains(role)) {
							securityUser.getRoles().add(role);
						}
						if (hasRole.equals("false") && securityUser.getRoles().contains(role)) {
							securityUser.getRoles().remove(role);
						}
					}
				}
			}

			secAdminService.updateUser(securityUser);
		}

		addMessage("success.user.save");

		return mapping.findForward("SUCCESS");
	}
}
