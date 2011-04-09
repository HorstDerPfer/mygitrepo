package db.training.osb.web.umleitung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Umleitung;
import db.training.osb.model.Umleitungsweg;
import db.training.osb.service.UmleitungService;
import db.training.osb.service.UmleitungswegService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class UmleitungswegDeleteAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungswegDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungswegDeleteAction.");

		TqmUser secUser = getSecUser();

		if (request.getParameter("umleitungId") != null
		    && request.getParameter("umleitungswegId") != null) {
			Integer umleitungId = FrontendHelper.castStringToInteger(request
			    .getParameter("umleitungId"));
			Integer umleitungswegId = FrontendHelper.castStringToInteger(request
			    .getParameter("umleitungswegId"));
			UmleitungService umlService = serviceFactory.createUmleitungService();
			UmleitungswegService uwegService = serviceFactory.createUmleitungswegService();
			Umleitung umleitung = umlService.findById(umleitungId, new FetchPlan[] {
			        FetchPlan.OSB_UML_UMLEITUNGSWEGE, FetchPlan.OSB_UML_FAHRPLANREGELUNGEN });
			Umleitungsweg uweg = uwegService.findById(umleitungswegId);

			EasyAccessDecisionVoter voterUWeg = EasyServiceFactory.getInstance()
			    .createUmleitungswegAnyVoter();
			EasyAccessDecisionVoter voterUmleitung = EasyServiceFactory.getInstance()
			    .createUmleitungAnyVoter();
			if (voterUWeg.vote(secUser, uweg, "ROLE_UMLEITUNGSWEG_LOESCHEN") == AccessDecisionVoter.ACCESS_GRANTED
			    && voterUmleitung.vote(secUser, umleitung, "ROLE_UMLEITUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				umleitung.getUmleitungswege().remove(uweg);
				umlService.update(umleitung);
			} else {
				addError("common.noAuth");
			}
			if (umleitung.getRelation() == null)
				addMessage("info.umleitung.incomplete");
			else
				addMessage("info.umleitung.complete");
		}

		return mapping.findForward("SUCCESS");
	}
}
