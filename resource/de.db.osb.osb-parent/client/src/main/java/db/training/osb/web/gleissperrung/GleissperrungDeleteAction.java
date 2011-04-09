package db.training.osb.web.gleissperrung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.Gleissperrung;
import db.training.osb.service.GleissperrungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungDeleteAction extends BaseAction {

	private static Logger log = Logger.getLogger(GleissperrungDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungDeleteAction.");

		TqmUser secUser = getSecUser();

		Gleissperrung gleissperrung = null;
		GleissperrungForm gleissperrungForm = (GleissperrungForm) form;
		Integer gleissperrungId = gleissperrungForm.getGleissperrungId();

		if (gleissperrungId == null || gleissperrungId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("Gleissperrung not found: " + gleissperrungId);
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}

		GleissperrungService service = serviceFactory.createGleissperrungService();
		gleissperrung = service.findById(gleissperrungId, new Preload[] { new Preload(
		    Gleissperrung.class, "massnahme") });

		if (gleissperrung == null) {
			if (log.isDebugEnabled())
				log.debug("Gleissperrung not found: " + gleissperrungId);
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing gleissperrung: " + gleissperrung.getId());

		EasyAccessDecisionVoter voter = serviceFactory.createRegelungAnyVoter();
		if (voter.vote(secUser, gleissperrung, "ROLE_GLEISSPERRUNG_LOESCHEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			if (log.isDebugEnabled())
				log.debug("ACCESS DENIED");
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		if (gleissperrungForm.getDelete() != null) {
			// Gleissperrung deaktivieren
			if (gleissperrungForm.getDelete() == true && !gleissperrung.isDeleted()) {
				gleissperrung.setDeleted(true);
				service.update(gleissperrung);

				addMessage("success.deleted");
				return mapping.findForward("SUCCESS");
			}
			// Gleissperrung aktivieren
			else if (gleissperrungForm.getDelete() == false && gleissperrung.isDeleted()) {
				gleissperrung.setDeleted(false);
				service.update(gleissperrung);

				addMessage("success.undelete");
				return mapping.findForward("SUCCESS");
			}
		}

		return mapping.findForward("SUCCESS");
	}

}
