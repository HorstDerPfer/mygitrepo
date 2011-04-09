package db.training.osb.web.baustelle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Baustelle;
import db.training.osb.model.Gleissperrung;
import db.training.security.hibernate.TqmUser;

public class BaustelleEditAction extends BaseAction {

	private static Logger log = Logger.getLogger(BaustelleEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungEditAction.");

		TqmUser secUser = getSecUser();

		BaustelleForm baustelleForm = (BaustelleForm) form;
		Baustelle baustelle = null;
		Integer baustelleId = baustelleForm.getId();
		// ID aus BaustelleSaveAction->create
		if (request.getAttribute("baustelleId") != null)
			baustelleId = (Integer) request.getAttribute("baustelleId");

		// BuendelId pruefen ****************************************
		if (baustelleId == null) {
			if (log.isDebugEnabled())
				log.debug("No baustelleId found");
			addError("error.notfound", msgRes.getMessage("baustelle"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing baustelleId: " + baustelleId);

		if (request.getParameter("reset") != null) {
			baustelleForm.reset();
		}

		if (baustelleId != 0) {
			baustelle = serviceFactory.createBaustelleService().findById(
			    baustelleId,
			    new Preload[] { new Preload(Baustelle.class, "gleissperrungen"),
			            new Preload(Gleissperrung.class, "massnahme") });

			// BuendelId pruefen ****************************************
			if (baustelle == null) {
				if (log.isDebugEnabled())
					log.debug("No baustelle found");
				addError("error.notfound", msgRes.getMessage("baustelle"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing baustelle: " + baustelle.getId());

			// Rechtepruefung
			if (serviceFactory.createBaustelleAnyVoter().vote(secUser, baustelle,
			    "ROLE_BAUSTELLE_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				if (log.isDebugEnabled())
					log.debug("User '" + secUser.getUsername()
					    + "' hat keine Berechtigung fuer: ROLE_BAUSTELLE_BEARBEITEN");
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			// Formular füllen
			baustelleForm.setId(baustelle.getId());
			baustelleForm.setName(baustelle.getName());
		} else {
			// Rechtepruefung
			if (!secUser.hasAuthorization("ROLE_BAUSTELLE_ANLEGEN_ALLE")
			    && secUser.hasAuthorization("ROLE_BAUSTELLE_ANLEGEN_REGIONALBEREICH")) {
				if (log.isDebugEnabled())
					log.debug("User '" + secUser.getUsername()
					    + "' hat keine Berechtigung fuer: ROLE_BAUSTELLE_ANLEGEN");
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			// neue Baustelle erstellen
			baustelle = new Baustelle();
			baustelleForm.reset();
			baustelleForm.setId(0);
		}

		request.setAttribute("baustelle", baustelle);

		return mapping.findForward("SUCCESS");
	}
}
