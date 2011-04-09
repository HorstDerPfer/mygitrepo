package db.training.osb.web.buendel;

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
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BuendelService;
import db.training.security.domain.BuendelAnyVoter;
import db.training.security.hibernate.TqmUser;

public class BuendelViewAction extends BaseAction {
	private static Logger log = Logger.getLogger(BuendelViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BuendelViewAction.");

		TqmUser secUser = getSecUser();

		Buendel buendel = null;
		BuendelForm buendelForm = (BuendelForm) form;
		Integer buendelId = buendelForm.getBuendelId();

		if (buendelId == null || buendelId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("buendel not found: " + buendelId);
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}

		BuendelService buendelService = EasyServiceFactory.getInstance().createBuendelService();
		buendel = buendelService.findById(buendelId, new Preload[] {
		        new Preload(Buendel.class, "weitereStrecken"),
		        new Preload(Buendel.class, "gleissperrungen"),
		        new Preload(Buendel.class, "fahrplanregelungen"),
		        new Preload(Fahrplanregelung.class, "betriebsstelleVon"),
		        new Preload(Fahrplanregelung.class, "betriebsstelleBis"),
		        new Preload(Fahrplanregelung.class, "buendel"),
		        new Preload(Gleissperrung.class, "buendel"),
		        new Preload(Gleissperrung.class, "vzgStrecke"),
		        new Preload(Regelung.class, "massnahme"),
		        new Preload(SAPMassnahme.class, "gleissperrungen"),
		        new Preload(Regelung.class, "bstVon"), new Preload(Regelung.class, "bstBis"),
		        new Preload(Regelung.class, "vtr"),
		        new Preload(VzgStrecke.class, "betriebsstellen") });

		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("buendel not found: " + buendelId);
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendel: " + buendel.getId());

		// Rechteprüfung: Bündel lesen
		BuendelAnyVoter voter = EasyServiceFactory.getInstance().createBuendelAnyVoter();
		if (voter.vote(secUser, buendel, "ROLE_BUENDEL_LESEN") == AccessDecisionVoter.ACCESS_DENIED) {
			if (log.isDebugEnabled())
				log.debug("User '" + secUser.getUsername()
				    + "' hat keine Berechtigung fuer: ROLE_BUENDEL_LESEN");
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		if (!sessionFahrplanjahr.equals(buendel.getFahrplanjahr())) {
			addError("error.fahrplanjahr.wrong");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("buendel", buendel);

		/* variable zur Steuerung von Buttons in mehrfach verwendeten Listen */
		request.setAttribute("action", "view");

		return mapping.findForward("SUCCESS");
	}
}
