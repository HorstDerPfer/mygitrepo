package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.GleissperrungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungAddToFahrplanregelungRemoveAction extends BaseAction {

	private static final Logger log = Logger
	    .getLogger(GleissperrungAddToFahrplanregelungRemoveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungAddToFahrplanregelungRemoveAction.");

		TqmUser secUser = getSecUser();

		Integer gleissperrungId = Integer.parseInt(request.getParameter("gleissperrungId"));
		Integer frId = Integer.parseInt(request.getParameter("fahrplanregelungId"));

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();
		GleissperrungService gsService = serviceFactory.createGleissperrungService();

		Fahrplanregelung fahrplanregelung = frService.findById(frId, new Preload[] { new Preload(
		    Fahrplanregelung.class, "gleissperrungen") });

		EasyAccessDecisionVoter voter = EasyServiceFactory.getInstance()
		    .createFahrplanregelungAnyVoter();
		if (voter.vote(secUser, fahrplanregelung, "ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			Gleissperrung gs = gsService.findById(gleissperrungId, new Preload[] {
			        new Preload(Gleissperrung.class, "fahrplanregelung"),
			        new Preload(Gleissperrung.class, "massnahme"),
			        new Preload(SAPMassnahme.class, "genehmiger") });
			if (gs != null) {
				EasyAccessDecisionVoter gsVoter = EasyServiceFactory.getInstance()
				    .createRegelungAnyVoter();
				if (gsVoter.vote(secUser, gs, "ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN") == AccessDecisionVoter.ACCESS_GRANTED) {
					if (gs.getFahrplanregelung() != null
					    && gs.getFahrplanregelung().equals(fahrplanregelung)) {

						// Gleissperrung von Fahrplanregelung entfernen
						gs.setFahrplanregelung(null);
						gsService.update(gs);

					}
				} else {
					addError("common.noAuth");
				}
			}

			if (!hasErrors()) {
				addMessage("success.saved");
			}

		} else {
			addError("common.noAuth");
		}

		request.setAttribute("fahrplanregelungId", fahrplanregelung.getId());
		return mapping.findForward("SUCCESS");
	}
}
