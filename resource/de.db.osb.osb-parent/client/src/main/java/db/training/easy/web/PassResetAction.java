package db.training.easy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import db.training.easy.core.model.User;
import db.training.easy.core.service.UserService;
import db.training.easy.util.SMTP;
import db.training.osb.util.ConfigResources;
import db.training.security.SecurityAdministrationService;
import db.training.security.SecurityService;
import db.training.security.hibernate.TqmUser;
import db.training.security.password.PasswordGenerator;

public class PassResetAction extends BaseAction {

	private static final Logger log = Logger.getLogger(PassResetAction.class);

	public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering PassResetForm");

		PassResetForm passResetForm = (PassResetForm) form;
		String email = passResetForm.getEmail();

		UserService userService = serviceFactory.createUserService();
		User user = userService.findUserByEmail(email);

		if (user == null) {
			if (log.isDebugEnabled())
				log.debug("User not found, E-Mail: " + email);
			addError("error.user.notfound");
			return mapping.findForward("FAILURE");
		}

		String password = PasswordGenerator.createNewPassword();

		MessageResources msgRes = getResources(request);

		String nameTo = user.getFirstName() + " " + user.getName();
		String nameFrom = "";// loginUser.getFirstName() + " " + loginUser.getName();
		StringBuilder sbMailTxt = new StringBuilder();
		sbMailTxt.append(msgRes.getMessage("mail.password.init.text", nameTo, ConfigResources
		    .getInstance().getApplicationTitle(), user.getLoginName(), password));
		sbMailTxt.append(msgRes.getMessage("mail.link", ConfigResources.getInstance()
		    .getApplicationUri()));
		sbMailTxt.append(msgRes.getMessage("mail.footer", nameFrom));

		try {
			new SMTP().sendEmail(user.getEmail(),
			    ConfigResources.getInstance().getAutoMailSender(), msgRes
			        .getMessage("mail.password.init.subject"), sbMailTxt.toString());
		} catch (Exception e) {
			if (log.isInfoEnabled())
				log.info("error.sendMail: " + user.getEmail(), e);
			addError("error.sendMail");
			return mapping.findForward("FAILURE");
		}
		SecurityService secService = serviceFactory.createSecurityService();
		TqmUser securityUser = (TqmUser) secService.loadUserByUsername(user.getLoginName());
		SecurityAdministrationService secAdminService = serviceFactory
		    .createSecurityAdministrationService();
		securityUser.changePassword(securityUser.getPassword(), secAdminService.createPasswordHash(
		    password, securityUser));
		secAdminService.updateUser(securityUser);

		addMessage("success.user.password.reset");
		return mapping.findForward("SUCCESS");

	}
}
