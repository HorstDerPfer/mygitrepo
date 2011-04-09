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
import db.training.osb.service.GleissperrungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungDeleteFromBaustelleAction extends BaseAction {

	private static final Logger log = Logger
	    .getLogger(GleissperrungDeleteFromBaustelleAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungDeleteFromBaustelleAction.");

		TqmUser secUser = getSecUser();

		BaustelleService bService = serviceFactory.createBaustelleService();
		GleissperrungService gsService = serviceFactory.createGleissperrungService();

		Integer gsId = FrontendHelper.castStringToInteger(request.getParameter("gleissperrungId"));
		Integer bId = FrontendHelper.castStringToInteger(request.getParameter("baustelleId"));

		Baustelle baustelle = bService.findById(bId);
		Gleissperrung gleissperrung = gsService.findById(gsId, new Preload[] { new Preload(
		    Gleissperrung.class, "baustellen") });

		EasyAccessDecisionVoter voter = EasyServiceFactory.getInstance().createBaustelleAnyVoter();
		if (voter.vote(secUser, baustelle, "ROLE_BAUSTELLE_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			gleissperrung.getBaustellen().remove(baustelle);
			gleissperrung.setLastChange(null);
			gsService.update(gleissperrung);
		} else {
			addError("common.noAuth");
		}

		request.setAttribute("id", bId);

		addMessage("success.deleted.gleissperrungFromBaustelle");
		return mapping.findForward("SUCCESS");
	}
}