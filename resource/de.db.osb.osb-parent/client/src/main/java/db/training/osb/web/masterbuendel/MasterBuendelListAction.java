package db.training.osb.web.masterbuendel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.service.MasterBuendelService;

public class MasterBuendelListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(MasterBuendelListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering /MasterBuendelListAction.");

		MasterBuendelService masterBuendelService = serviceFactory.createMasterBuendelService();

		// TODO: security check fuer MasterBuendelList
		// if (secUser.hasAuthorization("ROLE_BENUTZER_LESEN_ALLE")) {
		request.setAttribute("masterBuendel", masterBuendelService.findAll());
		// } else if (secUser.hasAuthorization("ROLE_BENUTZER_LESEN_REGIONALBEREICH")) {
		// request.setAttribute("users", userService.findUsersByRegionalbereich(loginUser
		// .getRegionalbereich()));
		// } else
		// return mapping.findForward("ACCESS_DENIED");

		return mapping.findForward("SUCCESS");
	}
}