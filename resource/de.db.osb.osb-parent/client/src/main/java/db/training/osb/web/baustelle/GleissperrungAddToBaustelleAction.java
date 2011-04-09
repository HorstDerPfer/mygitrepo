package db.training.osb.web.baustelle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Baustelle;
import db.training.osb.model.Gleissperrung;
import db.training.osb.service.BaustelleService;
import db.training.osb.service.GleissperrungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungAddToBaustelleAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungAddToBaustelleAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungAddToBaustelleAction.");

		TqmUser secUser = getSecUser();

		// Baustelle **************************************************************************
		BaustelleService bService = serviceFactory.createBaustelleService();
		Integer bId = FrontendHelper.castStringToInteger(request.getParameter("baustelleId"));
		Baustelle baustelle = null;
		if (bId != null) {
			baustelle = bService.findById(bId);
		}
		if (baustelle == null) {
			if (log.isDebugEnabled())
				log.debug("No baustelleId found");
			addError("error.notfound", msgRes.getMessage("baustelle"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing baustelle: " + baustelle.getId());

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

		EasyAccessDecisionVoter voter = serviceFactory.createBaustelleAnyVoter();
		if (voter.vote(secUser, baustelle, "ROLE_BAUSTELLE_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			gleissperrung.setLastChange(null);
			gsService.attachBaustelle(gleissperrung, baustelle);
			addMessage("success.attached.gleissperrungToBaustelle");
		} else {
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		return mapping.findForward("SUCCESS");
	}
}