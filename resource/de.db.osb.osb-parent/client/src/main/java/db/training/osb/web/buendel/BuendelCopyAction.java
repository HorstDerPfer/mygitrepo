package db.training.osb.web.buendel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.service.BuendelService;
import db.training.security.domain.BuendelAnyVoter;
import db.training.security.hibernate.TqmUser;

public class BuendelCopyAction extends BaseAction {
	private static Logger log = Logger.getLogger(BuendelCopyAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BuendelCopyAction.");

		TqmUser secUser = getSecUser();

		Buendel buendel = null;
		BuendelForm buendelForm = (BuendelForm) form;
		Integer buendelId = buendelForm.getBuendelId();

		if (buendelId == null || buendelId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("buendel not found: " + buendelId);
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}

		BuendelService buendelService = EasyServiceFactory.getInstance().createBuendelService();
		buendel = buendelService.findById(buendelId, new Preload[] {
		        new Preload(Buendel.class, "weitereStrecken"),
		        new Preload(Buendel.class, "gleissperrungen"),
		        new Preload(Gleissperrung.class, "buendel") });

		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("buendel not found: " + buendelId);
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendel: " + buendel.getId());

		// Rechteprüfung: Bündel lesen
		BuendelAnyVoter voter = EasyServiceFactory.getInstance().createBuendelAnyVoter();
		if (voter.vote(secUser, buendel, "ROLE_BUENDEL_ANLEGEN") == AccessDecisionVoter.ACCESS_DENIED) {
			if (log.isDebugEnabled()) {
				log.debug("User '" + secUser.getUsername()
				    + "' hat keine Berechtigung fuer: ROLE_BUENDEL_ANLEGEN");
			}
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		if (!sessionFahrplanjahr.equals(buendel.getFahrplanjahr())) {
			addError("error.fahrplanjahr.wrong");
			return mapping.findForward("FAILURE");
		}

		// Neues Buendel erstellen
		Buendel newBuendel = buendel.clone();
		newBuendel.setDeleted(false);
		newBuendel.setFixiert(false);
		newBuendel.setFixierungsDatum(null);
		newBuendel.setLastChangeDate(null);
		newBuendel.setDurchfuehrungsZeitraumStartKoordiniert(null);
		newBuendel.setDurchfuehrungsZeitraumEndeKoordiniert(null);

		// Neue LfdNr generieren wird in create-Methode generiert
		buendelService.create(newBuendel);

		// Gleissperrungen sollen nicht mit kopiert werden #510

		// ID des neuen Buendels in Request schreiben um in EDIT-Action wieder laden zu lassen
		request.setAttribute("buendelId", newBuendel.getId());

		addMessage("success.buendel.copy");

		// Action nicht für Back-Action speichern
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);
		return mapping.findForward("SUCCESS");
	}
}
