package db.training.bob.web.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.fplo.Fahrplan;
import db.training.bob.service.fplo.ZugcharakteristikService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshFahrplanDetailsAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshFahrplanDetailsAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering RefreshFahrplanDetailsAction.");

		Integer id = null;

		if (request.getParameter("id") != null)
			id = Integer.parseInt(request.getParameter("id"));

		ZugcharakteristikService zugcharakteristikService = EasyServiceFactory.getInstance()
		    .createZugcharakteristikService();

		List<Fahrplan> fahrplan = zugcharakteristikService.getFahrplanSortByLfd(id);
		request.setAttribute("fahrplan", fahrplan);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		return mapping.findForward("SUCCESS");
	}
}