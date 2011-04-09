package db.training.osb.web.umleitung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.osb.service.UmleitungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class UmleitungDeleteAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungDeleteAction.");

		TqmUser secUser = getSecUser();

		Umleitung umleitung = null;
		UmleitungForm umleitungForm = (UmleitungForm) form;
		Integer umleitungId = umleitungForm.getUmleitungId();

		if (umleitungId == null || umleitungId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("Umleitung not found: " + umleitungId);
			addError("error.notfound", msgRes.getMessage("umleitung"));
			return mapping.findForward("FAILURE");
		}

		UmleitungService service = serviceFactory.createUmleitungService();
		umleitung = service.findById(umleitungId, new Preload[] {
		        new Preload(Umleitung.class, "umleitungFahrplanregelungLinks"),
		        new Preload(UmleitungFahrplanregelungLink.class, "fahrplanregelung"),
		        new Preload(Umleitung.class, "umleitungswege") });

		if (umleitung == null) {
			if (log.isDebugEnabled())
				log.debug("Umleitung not found: " + umleitungId);
			addError("error.notfound", msgRes.getMessage("umleitung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing umleitung: " + umleitung.getId());

		EasyAccessDecisionVoter voter = serviceFactory.createUmleitungAnyVoter();
		if (voter.vote(secUser, umleitung, "ROLE_UMLEITUNG_LOESCHEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			if (log.isDebugEnabled())
				log.debug("ACCESS DENIED");
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		if (umleitungForm.getDelete() != null) {
			// Umleitung deaktivieren
			if (umleitungForm.getDelete() == true && !umleitung.isDeleted()) {
				umleitung.setDeleted(true);
				service.update(umleitung);

				addMessage("success.deleted");
				return mapping.findForward("SUCCESS");
			}
			// Umleitung aktivieren
			else if (umleitungForm.getDelete() == false && umleitung.isDeleted()) {
				umleitung.setDeleted(false);
				service.update(umleitung);

				addMessage("success.undelete");
				return mapping.findForward("SUCCESS");
			}
		}

		return mapping.findForward("SUCCESS");
	}
}
