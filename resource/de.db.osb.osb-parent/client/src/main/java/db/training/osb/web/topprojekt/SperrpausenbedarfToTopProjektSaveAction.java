package db.training.osb.web.topprojekt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseDispatchAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.TopProjekt;
import db.training.osb.service.SAPMassnahmeService;
import db.training.osb.service.TopProjektService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class SperrpausenbedarfToTopProjektSaveAction extends BaseDispatchAction {

	private static final Logger log = Logger
	    .getLogger(SperrpausenbedarfToTopProjektSaveAction.class);

	public ActionForward attach(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering " + getClass().getSimpleName());

		TqmUser secUser = getSecUser();

		TopProjektService topProjektService = serviceFactory.createTopProjektService();
		SAPMassnahmeService mnService = serviceFactory.createSAPMassnahmeService();

		Integer topProjektId = FrontendHelper.castStringToInteger(request
		    .getParameter("topProjektId"));
		if (topProjektId == null) {
			addError("topprojekt.error.notfound");
			return mapping.findForward("FAILURE_TOPPROJEKT_NOT_FOUND");
		}

		Integer massnahmeId = FrontendHelper.castStringToInteger(request
		    .getParameter("massnahmeId"));
		if (massnahmeId == null) {
			addError("error.sperrpausenbedarf.notfound");
			request.setAttribute("topProjektId", topProjektId);
			return mapping.findForward("FAILURE_MASSNAHME_NOT_FOUND");
		}

		TopProjekt topProjekt = topProjektService.findById(topProjektId, new Preload[] {
		        new Preload(TopProjekt.class, "regionalbereich"),
		        new Preload(TopProjekt.class, "massnahmen"),
		        new Preload(SAPMassnahme.class, "topProjekte") });

		SAPMassnahme massnahme = mnService.findById(massnahmeId,

		new Preload[] { new Preload(SAPMassnahme.class, "regionalbereich"),
		        new Preload(SAPMassnahme.class, "topProjekte"),
		        new Preload(TopProjekt.class, "massnahmen") });

		EasyAccessDecisionVoter voter = serviceFactory.createTopProjektAnyVoter();
		if (voter.vote(secUser, topProjekt, "ROLE_TOPPROJEKT_BEARBEITEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			addError("common.noAuth");
			request.setAttribute("topProjektId", topProjektId);
			return mapping.findForward("FAILURE");
		}

		// Zuordnung speichern
		topProjekt.getMassnahmen().add(massnahme);
		topProjektService.update(topProjekt);

		addMessage("success.saved");
		request.setAttribute("topProjektId", topProjektId);
		return mapping.findForward("SUCCESS");
	}

	public ActionForward detach(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering " + getClass().getSimpleName());

		TqmUser secUser = getSecUser();

		TopProjektService topProjektService = serviceFactory.createTopProjektService();
		SAPMassnahmeService mnService = serviceFactory.createSAPMassnahmeService();

		Integer topProjektId = FrontendHelper.castStringToInteger(request
		    .getParameter("topProjektId"));
		if (topProjektId == null) {
			addError("topprojekt.error.notfound");
			return mapping.findForward("FAILURE_TOPPROJEKT_NOT_FOUND");
		}

		Integer massnahmeId = FrontendHelper.castStringToInteger(request
		    .getParameter("massnahmeId"));
		if (massnahmeId == null) {
			addError("error.sperrpausenbedarf.notfound");
			request.setAttribute("topProjektId", topProjektId);
			return mapping.findForward("FAILURE_MASSNAHME_NOT_FOUND");
		}

		TopProjekt topProjekt = topProjektService.findById(topProjektId, new Preload[] {
		        new Preload(TopProjekt.class, "regionalbereich"),
		        new Preload(TopProjekt.class, "massnahmen"),
		        new Preload(SAPMassnahme.class, "topProjekte") });

		SAPMassnahme massnahme = mnService.findById(massnahmeId, new Preload[] { new Preload(
		    SAPMassnahme.class, "regionalbereich") });

		EasyAccessDecisionVoter voter = serviceFactory.createTopProjektAnyVoter();
		if (voter.vote(secUser, topProjekt, "ROLE_TOPPROJEKT_BEARBEITEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			addError("common.noAuth");
			request.setAttribute("topProjektId", topProjektId);
			return mapping.findForward("FAILURE");
		}

		// Zuordnung aufheben
		topProjekt.getMassnahmen().remove(massnahme);
		topProjektService.update(topProjekt);

		addMessage("success.saved");
		request.setAttribute("topProjektId", topProjektId);
		return mapping.findForward("SUCCESS");
	}
}
