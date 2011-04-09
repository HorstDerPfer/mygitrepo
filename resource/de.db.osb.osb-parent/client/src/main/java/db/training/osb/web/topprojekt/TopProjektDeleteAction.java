package db.training.osb.web.topprojekt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.TopProjekt;
import db.training.osb.service.TopProjektService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class TopProjektDeleteAction extends BaseAction {

	private static final Logger log = Logger.getLogger(TopProjektDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering TopProjektDeleteAction.");

		TqmUser secUser = getSecUser();

		TopProjekt topProjekt = null;
		TopProjektForm tpForm = (TopProjektForm) form;
		Integer topProjektId = tpForm.getTopProjektId();

		if (topProjektId == null || topProjektId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("TopProjekt not found: " + topProjektId);
			addError("error.notfound", msgRes.getMessage("topProjekt"));
			return mapping.findForward("FAILURE");
		}

		TopProjektService tpService = serviceFactory.createTopProjektService();
		topProjekt = tpService.findById(topProjektId, new Preload[] {
		        new Preload(TopProjekt.class, "massnahmen"),
		        new Preload(TopProjekt.class, "regionalbereich") });

		EasyAccessDecisionVoter voter = serviceFactory.createTopProjektAnyVoter();
		if (voter.vote(secUser, topProjekt, "ROLE_TOPPROJEKT_LOESCHEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			if (log.isDebugEnabled())
				log.debug("ACCESS DENIED");
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		if (tpForm.getDelete() != null) {
			// TopProjekt deaktivieren
			if (tpForm.getDelete() == true && !topProjekt.isDeleted()) {
				topProjekt.setDeleted(true);
				tpService.update(topProjekt);

				addMessage("success.deleted");
				return mapping.findForward("SUCCESS");
			}
			// TopProjekt aktivieren
			else if (tpForm.getDelete() == false && topProjekt.isDeleted()) {
				topProjekt.setDeleted(false);
				tpService.update(topProjekt);

				addMessage("success.undelete");
				return mapping.findForward("SUCCESS");
			}
		}

		return mapping.findForward("SUCCESS");
	}
}
