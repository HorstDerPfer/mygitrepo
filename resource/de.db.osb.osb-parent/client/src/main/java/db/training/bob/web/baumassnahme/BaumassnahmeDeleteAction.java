package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.domain.BaumassnahmeAnyVoter;
import db.training.security.hibernate.TqmUser;

public class BaumassnahmeDeleteAction extends BaseAction {

	private static Logger log = Logger.getLogger(BaumassnahmeEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeDeleteAction.");

		TqmUser secUser = getSecUser();

		Baumassnahme baumassnahme = new Baumassnahme();
		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		Integer id;

		if (request.getAttribute("id") != null)
			id = (Integer) request.getAttribute("id");
		else
			id = baumassnahmeForm.getId();

		BaumassnahmeService baumassnahmeService = serviceFactory.createBaumassnahmeService();
		FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL };
		baumassnahme = baumassnahmeService.findById(id, fetchPlans);

		if (baumassnahme != null) {
			// Rechteprüfung: Bausmassnahme löschen
			BaumassnahmeAnyVoter voter = EasyServiceFactory.createBaumassnahmeAnyVoter();
			if (voter.vote(secUser, baumassnahme, "ROLE_BAUMASSNAHME_GRUNDDATEN_LOESCHEN") == AccessDecisionVoter.ACCESS_DENIED) {
				// keine Berechtigung
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			baumassnahmeService.delete(baumassnahme);
		} else {
			addError("error.baumassnahme.notfound");
			return mapping.findForward("FAILURE");
		}

		return mapping.findForward("SUCCESS");
	}
}