package db.training.osb.web.topprojekt;

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
import db.training.osb.model.Anmelder;
import db.training.osb.model.TopProjekt;
import db.training.osb.service.TopProjektService;
import db.training.security.domain.TopProjektAnyVoter;
import db.training.security.hibernate.TqmUser;

public class TopProjektViewAction extends BaseAction {

	private static final Logger log = Logger.getLogger(TopProjektViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering TopProjektViewAction.");

		TqmUser secUser = getSecUser();

		TopProjekt topProjekt = null;
		TopProjektForm topProjektForm = (TopProjektForm) form;
		Integer topProjektId = topProjektForm.getTopProjektId();

		if (topProjektId == null || topProjektId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("topProjekt not found: " + topProjektId);
			addError("error.notfound", msgRes.getMessage("topProjekt"));
			return mapping.findForward("FAILURE");
		}

		TopProjektService service = EasyServiceFactory.getInstance().createTopProjektService();
		topProjekt = service.findById(topProjektId, new Preload[] {
		        new Preload(TopProjekt.class, "regionalbereich"),
		        new Preload(TopProjekt.class, "massnahmen"),
		        new Preload(TopProjekt.class, "anmelder"),
		        new Preload(Anmelder.class, "user") });

		if (topProjekt == null) {
			if (log.isDebugEnabled())
				log.debug("topProjekt not found: " + topProjektId);
			addError("error.notfound", msgRes.getMessage("topProjekt"));
			return mapping.findForward("FAILURE");
		}

		// Rechteprüfung: TopProjekt lesen
		TopProjektAnyVoter voter = EasyServiceFactory.getInstance().createTopProjektAnyVoter();
		if (voter.vote(secUser, topProjekt, "ROLE_TOPPROJEKT_LESEN") == AccessDecisionVoter.ACCESS_DENIED) {
			// keine Berechtigung
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("projekt", topProjekt);

		return mapping.findForward("SUCCESS");
	}
}
