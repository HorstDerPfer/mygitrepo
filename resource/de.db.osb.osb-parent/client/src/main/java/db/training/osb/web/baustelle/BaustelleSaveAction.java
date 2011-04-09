package db.training.osb.web.baustelle;

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
import db.training.osb.model.Baustelle;
import db.training.osb.service.BaustelleService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class BaustelleSaveAction extends BaseAction {

	private static Logger log = Logger.getLogger(BaustelleSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaustelleSaveAction.");

		TqmUser secUser = getSecUser();

		BaustelleForm baustelleForm = (BaustelleForm) form;
		Integer baustelleId = baustelleForm.getId();

		if (request.getParameter("reset") != null) {
			baustelleForm.reset();
		}

		EasyAccessDecisionVoter voter = EasyServiceFactory.getInstance().createBaustelleAnyVoter();
		BaustelleService baustelleService = serviceFactory.createBaustelleService();
		Baustelle baustelle = baustelleService.findById(baustelleId, new Preload[] {});

		if (baustelleId == 0) {
			baustelle = new Baustelle();
		}

		// Formulardaten verarbeiten
		baustelle.setName(baustelleForm.getName());
		baustelle.setFahrplanjahr(sessionFahrplanjahr);

		// Berechtigungen prüfen
		if (baustelleId == 0) {
			if (voter.vote(secUser, baustelle, "ROLE_BAUSTELLE_ANLEGEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				// Baustelle erstellen
				baustelleService.create(baustelle);
				request.setAttribute("baustelleId", baustelle.getId());
			} else {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
		} else {
			if (voter.vote(secUser, baustelle, "ROLE_BAUSTELLE_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				// Änderungen speichern
				baustelleService.update(baustelle);

			} else {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
		}

		request.setAttribute("baustelle", baustelle);

		addMessage("success.saved");
		return mapping.findForward("SUCCESS");
	}
}
