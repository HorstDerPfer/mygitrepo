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
import db.training.security.hibernate.TqmUser;

public class UmleitungEditAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungEditAction.");

		TqmUser secUser = getSecUser();

		UmleitungForm umleitungForm = (UmleitungForm) form;
		Integer umleitungId = umleitungForm.getUmleitungId();

		UmleitungService umlService = serviceFactory.createUmleitungService();

		if (umleitungForm.getVzgStrecke() == null || umleitungForm.getVzgStrecke().equals(0))
			umleitungForm.setVzgStrecke(null);
		if (!hasErrors(request)) {
			umleitungForm.setVzgStrecke(null);
			umleitungForm.setBetriebsstelleVon(umleitungForm.getBetriebsstelleBis());
			umleitungForm.setBetriebsstelleBis(null);
		}

		if (umleitungId != 0) {
			umleitungForm.reset();

			Umleitung umleitung = umlService.findById(umleitungId, new FetchPlan[] {
			        FetchPlan.OSB_UML_UMLEITUNGSWEGE, FetchPlan.OSB_UML_FAHRPLANREGELUNGEN });
			if (EasyServiceFactory.getInstance().createUmleitungAnyVoter().vote(secUser, umleitung,
			    "ROLE_UMLEITUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
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
		} else if (!hasErrors(request)) {
			if (!secUser.hasAuthorization("ROLE_UMLEITUNG_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_UMLEITUNG_ANLEGEN_REGIONALBEREICH")) {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
			umleitungForm.setUmleitungName(null);
			umleitungForm.setFreieKapaRichtung(null);
			umleitungForm.setFreieKapaGegenrichtung(null);
		}

		return mapping.findForward("SUCCESS");
	}
}