package db.training.osb.web.fahrplanregelung;

import java.util.Date;

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
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.UmleitungFahrplanregelungLinkService;
import db.training.security.domain.FahrplanregelungAnyVoter;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungCopyAction extends BaseAction {

	private static final Logger log = Logger.getLogger(FahrplanregelungCopyAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungCopyAction.");

		TqmUser secUser = getSecUser();

		FahrplanregelungForm frForm = (FahrplanregelungForm) form;
		Integer frId = frForm.getFahrplanregelungId();
		Fahrplanregelung fr = null;

		if (frId == null || frId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("fahrplanregelung not found: " + frId);
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}

		fr = serviceFactory.createFahrplanregelungService().findById(
		    frId,
		    new Preload[] { new Preload(Fahrplanregelung.class, "buendel"),
		            new Preload(Fahrplanregelung.class, "betriebsstelleVon"),
		            new Preload(Fahrplanregelung.class, "betriebsstelleBis"),
		            new Preload(Fahrplanregelung.class, "gleissperrungen"),
		            new Preload(Fahrplanregelung.class, "regelungsarten"),
		            new Preload(Fahrplanregelung.class, "umleitungFahrplanregelungLinks") });

		if (fr == null) {
			if (log.isDebugEnabled())
				log.debug("fahrplanregelung not found: " + fr.getId());
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}

		// Rechteprüfung: Fahrplanregelung lesen
		FahrplanregelungAnyVoter voter = EasyServiceFactory.getInstance()
		    .createFahrplanregelungAnyVoter();
		if (voter.vote(secUser, fr, "ROLE_FAHRPLANREGELUNG_ANLEGEN") == AccessDecisionVoter.ACCESS_DENIED) {
			if (log.isDebugEnabled()) {
				log.debug("User '" + secUser.getUsername()
				    + "' hat keine Berechtigung fuer: ROLE_FAHRPLANREGELUNG_ANLEGEN");
			}
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();

		// Neues Buendel erstellen
		Fahrplanregelung newFr = fr.clone();
		newFr.setDeleted(false);
		newFr.setFixiert(false);
		newFr.setLastChangeDate(null);
		// Neue LfdNr wird in create-Methode generiert
		frService.create(newFr);

		// Umleitungen werden uebernommen. Muessen separat gespeichert werden. Bei aktuellem Mapping
		// nicht ueber Fahrplanregelung mit speicherbar
		UmleitungFahrplanregelungLinkService uflService = serviceFactory
		    .createUmleitungFahrplanregelungLinkService();
		for (UmleitungFahrplanregelungLink ufl : fr.getUmleitungFahrplanregelungLinks()) {
			UmleitungFahrplanregelungLink u = ufl.clone();
			u.setFahrplanregelung(newFr);
			u.setLastChangeDate(new Date());
			uflService.create(u);
		}

		// Gleissperrungen koennen nicht uebernommen werden, da diese nur zu einer Fahrplanregelung
		// zugewiesen sein duerfen

		request.setAttribute("fahrplanregelung", newFr);

		// ID der neuen Fahrplanregelung in Request schreiben um in EDIT-Action wieder laden zu
		// lassen
		request.setAttribute("fahrplanregelungId", newFr.getId());
		request.setAttribute("copy", true);

		addMessage("success.fahrplanregelung.copy");

		// Action nicht für Back-Action speichern
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);
		return mapping.findForward("SUCCESS");
	}
}
