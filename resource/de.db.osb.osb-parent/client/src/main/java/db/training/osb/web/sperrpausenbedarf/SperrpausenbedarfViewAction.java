package db.training.osb.web.sperrpausenbedarf;

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
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.SAPMassnahmeService;
import db.training.security.domain.SAPMassnahmeAnyVoter;
import db.training.security.hibernate.TqmUser;

public class SperrpausenbedarfViewAction extends BaseAction {
	private static Logger log = Logger.getLogger(SperrpausenbedarfViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering SperrpausenbedarfeViewAction");

		TqmUser secUser = getSecUser();

		SAPMassnahme massnahme = null;
		SperrpausenbedarfForm spForm = (SperrpausenbedarfForm) form;
		Integer spId = spForm.getSperrpausenbedarfId();

		if (request.getAttribute("sperrpausenbedarfId") != null)
			spId = (Integer) request.getAttribute("sperrpausenbedarfId");

		if (spId == null || spId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("massnahme not found: " + spId);
			addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
			return mapping.findForward("FAILURE");
		}

		SAPMassnahmeService massnahmeService = EasyServiceFactory.getInstance()
		    .createSAPMassnahmeService();

		massnahme = massnahmeService.findById(spId, new Preload[] {
		        new Preload(SAPMassnahme.class, "regionalbereich"),
		        new Preload(SAPMassnahme.class, "hauptStrecke"),
		        new Preload(VzgStrecke.class, "betriebsstellen"),
		        new Preload(SAPMassnahme.class, "paket"),
		        new Preload(SAPMassnahme.class, "betriebsstelleVon"),
		        new Preload(SAPMassnahme.class, "betriebsstelleBis"),
		        new Preload(SAPMassnahme.class, "betriebsstelleVonKoordiniert"),
		        new Preload(SAPMassnahme.class, "betriebsstelleBisKoordiniert"),
		        new Preload(SAPMassnahme.class, "topProjekte"),
		        new Preload(TopProjekt.class, "regionalbereich"),
		        new Preload(SAPMassnahme.class, "gleissperrungen"),
		        new Preload(Gleissperrung.class, "buendel"),
		        new Preload(Gleissperrung.class, "massnahme"),
		        new Preload(Gleissperrung.class, "vzgStrecke"),
		        new Preload(Gleissperrung.class, "vtr") });

		if (massnahme == null) {
			if (log.isDebugEnabled())
				log.debug("massnahme not found: " + spId);
			addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing Sperrpausenbedarf: " + massnahme.getId());

		// Rechteprüfung: Massnahme lesen
		SAPMassnahmeAnyVoter voter = EasyServiceFactory.getInstance().createSAPMassnahmeAnyVoter();
		if (voter.vote(secUser, massnahme, "ROLE_MASSNAHME_LESEN") == AccessDecisionVoter.ACCESS_DENIED) {
			// keine Berechtigung
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		if (!sessionFahrplanjahr.equals(massnahme.getFahrplanjahr())) {
			addError("error.fahrplanjahr.wrong");
			return mapping.findForward("FAILURE");
		}
		request.setAttribute("baumassnahme", massnahme);

		return mapping.findForward("SUCCESS");
	}

}
