package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.UmleitungFahrplanregelungLinkService;
import db.training.osb.service.UmleitungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class UmleitungDeleteFromFahrplanregelungAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungDeleteFromFahrplanregelungAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungDeleteFromFahrplanregelungAction.");

		TqmUser secUser = getSecUser();

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();
		UmleitungService umlService = serviceFactory.createUmleitungService();
		UmleitungFahrplanregelungLinkService uflService = serviceFactory
		    .createUmleitungFahrplanregelungLinkService();

		Integer frId = FrontendHelper.castStringToInteger(request
		    .getParameter("fahrplanregelungId"));
		Fahrplanregelung fahrplanregelung = frService.findById(frId);

		Integer umleitungId = FrontendHelper.castStringToInteger(request
		    .getParameter("umleitungId"));

		EasyAccessDecisionVoter voter = EasyServiceFactory.getInstance()
		    .createFahrplanregelungAnyVoter();
		if (voter.vote(secUser, fahrplanregelung, "ROLE_FAHRPLANREGELUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			Umleitung umleitung = umlService.findById(umleitungId);
			UmleitungFahrplanregelungLink ufLink = uflService.findByUmleitungAndFahrplanregelung(
			    umleitung, fahrplanregelung);
			uflService.delete(ufLink);
		} else {
			addError("common.noAuth");
		}
		return mapping.findForward("SUCCESS");
	}
}