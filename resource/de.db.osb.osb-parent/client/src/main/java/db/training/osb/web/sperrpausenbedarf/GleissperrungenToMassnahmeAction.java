package db.training.osb.web.sperrpausenbedarf;

import java.util.List;

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
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.security.domain.SAPMassnahmeAnyVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungenToMassnahmeAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungenToMassnahmeAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungenToMassnahmeAction.");

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

		massnahme = massnahmeService.findById(spId, new Preload[] { new Preload(SAPMassnahme.class,
		    "gleissperrungen") });

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
		// Fahrplanjahr pruefen
		if (!sessionFahrplanjahr.equals(massnahme.getFahrplanjahr())) {
			addError("error.fahrplanjahr.wrong");
			return mapping.findForward("FAILURE");
		}

		GleissperrungService gsService = serviceFactory.createGleissperrungService();
		List<Gleissperrung> gleissperrungen = gsService.findByMassnahme(null,
		    new Preload[] { new Preload(Regelung.class, "massnahme") });

		// bereits hinzugefuegte Gleissperrungen werden entfernt
		gleissperrungen.removeAll(massnahme.getGleissperrungen());

		request.setAttribute("massnahme", massnahme);
		request.setAttribute("gleissperrungen", gleissperrungen);

		return mapping.findForward("SUCCESS");
	}
}