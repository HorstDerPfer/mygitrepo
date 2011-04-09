package db.training.easy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ForwardConfig;

import db.training.logwrapper.Logger;

public class ForwardAction extends BaseAction {

	private static final Logger log = Logger.getLogger(ForwardAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering ForwardAction.");

		ForwardConfig[] forwards = mapping.findForwardConfigs();

		if (forwards.length > 0) {
			String actionForwardName = forwards[0].getName();
			if (log.isDebugEnabled())
				log.debug("forward found: " + actionForwardName);
			return mapping.findForward(actionForwardName);
		}

		throw new IllegalAccessException("No action forward found in ForwardAction.");
	}
}
