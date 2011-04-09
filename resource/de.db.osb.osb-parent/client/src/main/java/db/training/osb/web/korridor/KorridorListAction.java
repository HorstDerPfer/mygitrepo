package db.training.osb.web.korridor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.service.KorridorService;

public class KorridorListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(KorridorListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering /KorridorListAction.");

		KorridorService korridorService = serviceFactory.createKorridorService();

		// TODO: security check fuer KorridorList
		// if (secUser.hasAuthorization("ROLE_BENUTZER_LESEN_ALLE")) {
		request.setAttribute("korridore", korridorService.findAll());
		// } else if (secUser.hasAuthorization("ROLE_BENUTZER_LESEN_REGIONALBEREICH")) {
		// request.setAttribute("users", userService.findUsersByRegionalbereich(loginUser
		// .getRegionalbereich()));
		// } else
		// return mapping.findForward("ACCESS_DENIED");

		return mapping.findForward("SUCCESS");
	}
}