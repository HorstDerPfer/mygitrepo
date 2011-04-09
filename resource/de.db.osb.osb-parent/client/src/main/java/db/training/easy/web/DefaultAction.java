package db.training.easy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

public class DefaultAction extends BaseAction {

	private static Logger log = Logger.getLogger(DefaultAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering DefaultAction.");

		// Evtl. vorhandenen Login-Fehlermeldungen aus Session entfernen
		if (request.getSession().getAttribute("loginErrors") != null)
			request.getSession().removeAttribute("loginErrors");

		return mapping.findForward("SUCCESS");
	}
}