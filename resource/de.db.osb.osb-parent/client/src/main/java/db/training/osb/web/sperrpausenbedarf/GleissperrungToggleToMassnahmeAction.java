package db.training.osb.web.sperrpausenbedarf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungToggleToMassnahmeAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungToggleToMassnahmeAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungToggleToMassnahmeAction.");

		TqmUser secUser = getSecUser();

		SAPMassnahmeService mService = serviceFactory.createSAPMassnahmeService();
		Integer mId = FrontendHelper.castStringToInteger(request
		    .getParameter("sperrpausenbedarfId"));
		String action = FrontendHelper.getNullOrTrimmed(request.getParameter("action"));
		SAPMassnahme massnahme = null;

		if (mId != null) {
			massnahme = mService.findById(mId, new Preload[] { new Preload(SAPMassnahme.class,
			    "regionalbereich") });
		}
		if (massnahme == null || action == null || (!action.equals("del") && !action.equals("add"))) {
			if (log.isDebugEnabled())
				log.debug("No massnahmeId found");
			addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled()) {
			log.debug("Processing massnahme: " + massnahme.getId());
			log.debug("Action: " + action);
		}

		// Gleissperrung ************************************************************************
		GleissperrungService gsService = serviceFactory.createGleissperrungService();
		Integer gsId = FrontendHelper.castStringToInteger(request.getParameter("gleissperrungId"));
		Gleissperrung gleissperrung = null;
		if (gsId != null) {
			gleissperrung = gsService.findById(gsId);
		}
		if (gleissperrung == null) {
			if (log.isDebugEnabled())
				log.debug("No gleissperrungId found");
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing gleissperrung: " + gleissperrung.getId());

		EasyAccessDecisionVoter voter = serviceFactory.createSAPMassnahmeAnyVoter();
		if (voter.vote(secUser, massnahme, "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			gleissperrung.setLastChange(null);

			if (action.equals("add")) {
				gleissperrung.setMassnahme(massnahme);
				gsService.update(gleissperrung);
				addMessage("success.attached.gleissperrungToMassnahme");
				return mapping.findForward("SUCCESS_ADD");
			} else if (action.equals("del")) {
				gleissperrung.setMassnahme(null);
				gsService.update(gleissperrung);
				addMessage("success.deleted.gleissperrungFromMassnahme");
				return mapping.findForward("SUCCESS_DEL");
			}
		} else {
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		return mapping.findForward("FAILURE");
	}
}