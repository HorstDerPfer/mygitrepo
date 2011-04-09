package db.training.osb.web.baustelle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Baustelle;
import db.training.osb.model.Gleissperrung;
import db.training.osb.service.BaustelleService;
import db.training.security.domain.BaustelleAnyVoter;
import db.training.security.hibernate.TqmUser;

public class BaustelleViewAction extends BaseAction {

	private static final Logger log = Logger.getLogger(BaustelleViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaustelleViewAction.");

		TqmUser secUser = getSecUser();

		Integer id = null;
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("id"))) {
			id = FrontendHelper.castStringToInteger(request.getParameter("id"));
		}

		if (id == null || id == 0) {
			addError("error.baustelle.notfound");
			return mapping.findForward("FAILURE");
		}

		BaustelleService service = EasyServiceFactory.getInstance().createBaustelleService();
		Baustelle baustelle = service.findById(id, new Preload[] {
		        new Preload(Baustelle.class, "gleissperrungen"),
		        new Preload(Gleissperrung.class, "massnahme") });

		// Berechtigungen prüfen
		BaustelleAnyVoter voter = serviceFactory.createBaustelleAnyVoter();
		if (voter.vote(secUser, baustelle, "ROLE_BAUSTELLE_LESEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		if (baustelle == null) {
			addError("error.baustelle.notfound");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("baustelle", baustelle);

		return mapping.findForward("SUCCESS");
	}
}
