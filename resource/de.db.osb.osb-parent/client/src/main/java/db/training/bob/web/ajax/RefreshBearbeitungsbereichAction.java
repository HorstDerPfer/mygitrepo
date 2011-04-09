package db.training.bob.web.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.bob.web.baumassnahme.BaumassnahmeSearchForm;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.easy.web.admin.user.UserForm;
import db.training.logwrapper.Logger;

public class RefreshBearbeitungsbereichAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshBearbeitungsbereichAction.class);

	private static enum SuccessForwardType {
		BAUMASSNAHME, STATISTICS, BAUMASSNAHMESEARCH, USER
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshBearbeitungsbereichAction.");

		Integer regionalBereichFpl = null;
		SuccessForwardType forwardType = SuccessForwardType.BAUMASSNAHME;

		if (request.getParameter("regionalBereichFpl") != null)
			regionalBereichFpl = FrontendHelper.castStringToInteger(request
			    .getParameter("regionalBereichFpl"));
		if (request.getParameter("forwardType") != null) {
			forwardType = SuccessForwardType.valueOf(request.getParameter("forwardType"));
		}

		RegionalbereichService regionalbereichService = serviceFactory
		    .createRegionalbereichService();
		List<Bearbeitungsbereich> bearbeitungsbereiche = null;
		if (regionalBereichFpl != null) {
			Regionalbereich rb = regionalbereichService.findById(regionalBereichFpl);

			BearbeitungsbereichService bearbeitungsbereichService = serviceFactory
			    .createBearbeitungsbereichService();
			bearbeitungsbereiche = bearbeitungsbereichService.findByRegionalbereich(rb);

			request.setAttribute("bearbeitungsbereiche", bearbeitungsbereiche);
		} else {
			request.setAttribute("bearbeitungsbereiche", new ArrayList<Bearbeitungsbereich>());
		}

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		if (forwardType == SuccessForwardType.STATISTICS) {
			if (bearbeitungsbereiche != null) {
				BaumassnahmeSearchForm searchForm = (BaumassnahmeSearchForm) form;
				searchForm.setBearbeitungsbereichList(bearbeitungsbereiche);
			}
			return mapping.findForward("SUCCESS_STATISTICS");
		} else if (forwardType == SuccessForwardType.BAUMASSNAHMESEARCH) {
			if (bearbeitungsbereiche != null) {
				BaumassnahmeSearchForm searchForm = (BaumassnahmeSearchForm) form;
				searchForm.setBearbeitungsbereichList(bearbeitungsbereiche);
			}
			return mapping.findForward("SUCCESS_BAUMASSNAHME_SEARCH");
		} else if (forwardType == SuccessForwardType.USER) {
			if (bearbeitungsbereiche != null) {
				UserForm userForm = (UserForm) form;
				userForm.setBearbeitungsbereichList(bearbeitungsbereiche);
			}
			return mapping.findForward("SUCCESS");
		}

		if (bearbeitungsbereiche != null) {
			BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
			baumassnahmeForm.setBearbeitungsbereichList(bearbeitungsbereiche);
		}
		return mapping.findForward("SUCCESS_BAUMASSNAHME");
	}
}