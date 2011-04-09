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
import db.training.osb.service.UmleitungService;
import db.training.security.domain.UmleitungAnyVoter;
import db.training.security.hibernate.TqmUser;

public class UmleitungViewAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungViewAction.");

		TqmUser secUser = getSecUser();

		Umleitung umleitung = null;
		UmleitungForm umleitungForm = (UmleitungForm) form;
		Integer umleitungId = umleitungForm.getUmleitungId();

		if (umleitungId == null || umleitungId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("umleitung not found: " + umleitungId);
			addError("error.notfound", msgRes.getMessage("umleitung"));
			return mapping.findForward("FAILURE");
		}

		UmleitungService umlService = serviceFactory.createUmleitungService();
		umleitung = umlService.findById(umleitungId, new FetchPlan[] {
		        FetchPlan.OSB_UML_UMLEITUNGSWEGE, FetchPlan.OSB_UML_FAHRPLANREGELUNGEN });

		if (umleitung == null) {
			if (log.isDebugEnabled())
				log.debug("umleitung not found: " + umleitungId);
			addError("error.notfound", msgRes.getMessage("umleitung"));
			return mapping.findForward("FAILURE");
		}

		// Rechteprüfung: Umleitung lesen
		UmleitungAnyVoter voter = EasyServiceFactory.getInstance().createUmleitungAnyVoter();
		if (voter.vote(secUser, umleitung, "ROLE_UMLEITUNG_LESEN") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		umleitungForm.setUmleitungName(umleitung.getName());
		umleitungForm.setFreieKapaRichtung(FrontendHelper.castDoubleToString(umleitung
		    .getFreieKapaRichtung(), 2));
		umleitungForm.setFreieKapaGegenrichtung(FrontendHelper.castDoubleToString(umleitung
		    .getFreieKapaGegenrichtung(), 2));
		if (umleitung.getRelation() == null)
			addMessage("info.umleitung.incomplete");
		else
			addMessage("info.umleitung.complete");
		request.setAttribute("umleitung", umleitung);

		return mapping.findForward("SUCCESS");
	}
}
