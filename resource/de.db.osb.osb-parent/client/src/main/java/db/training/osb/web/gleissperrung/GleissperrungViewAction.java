package db.training.osb.web.gleissperrung;

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
import db.training.osb.service.GleissperrungService;
import db.training.security.domain.RegelungAnyVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungViewAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungViewAction.");

		TqmUser secUser = getSecUser();

		Gleissperrung gleissperrung = null;
		GleissperrungForm gleissperrungForm = (GleissperrungForm) form;
		Integer gleissperrungId = gleissperrungForm.getGleissperrungId();

		if (gleissperrungId == null || gleissperrungId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("gleissperrung not found: " + gleissperrungId);
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}

		GleissperrungService service = EasyServiceFactory.getInstance()
		    .createGleissperrungService();
		gleissperrung = service.findById(gleissperrungId, new Preload[] {
		        new Preload(Gleissperrung.class, "bstVon"),
		        new Preload(Gleissperrung.class, "bstBis"),
		        new Preload(Gleissperrung.class, "buendel"),
		        new Preload(Gleissperrung.class, "baustellen"),
		        new Preload(Gleissperrung.class, "massnahme"),
		        new Preload(SAPMassnahme.class, "regionalbereich"),
		        new Preload(Gleissperrung.class, "betriebsweise"),
		        new Preload(Gleissperrung.class, "vzgStrecke"),
		        new Preload(Gleissperrung.class, "vtr") });

		if (gleissperrung == null) {
			if (log.isDebugEnabled())
				log.debug("gleissperrung not found: " + gleissperrungId);
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}

		// Rechteprüfung: Gleissperrung lesen
		RegelungAnyVoter voter = EasyServiceFactory.getInstance().createRegelungAnyVoter();
		if (voter.vote(secUser, gleissperrung, "ROLE_GLEISSPERRUNG_LESEN") == AccessDecisionVoter.ACCESS_DENIED) {
			// keine Berechtigung
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("gleissperrung", gleissperrung);

		return mapping.findForward("SUCCESS");
	}
}
