package db.training.easy.web.admin.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.model.User;
import db.training.easy.core.service.UserService;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class UserListAction extends BaseAction {

	private static Logger log = Logger.getLogger(UserListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering /admin/UserListAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		UserService userService = serviceFactory.createUserService();

		if (secUser.hasAuthorization("ROLE_BENUTZER_LESEN_ALLE")) {
			request.setAttribute("users", userService.findAllUsers());
		} else if (secUser.hasAuthorization("ROLE_BENUTZER_LESEN_REGIONALBEREICH")) {
			request.setAttribute("users", userService.findUsersByRegionalbereich(loginUser
			    .getRegionalbereich()));
		} else
			return mapping.findForward("ACCESS_DENIED");

		return mapping.findForward("SUCCESS");
	}
}