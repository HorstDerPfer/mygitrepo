package db.training.osb.web.buendel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.service.BuendelService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class BuendelDeleteAction extends BaseAction {

	private static Logger log = Logger.getLogger(BuendelDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungDeleteAction.");

		TqmUser secUser = getSecUser();

		Buendel buendel = null;
		BuendelForm buendelForm = (BuendelForm) form;
		Integer buendelId = buendelForm.getBuendelId();
		Boolean darfVeraendertWerden = true;

		if (buendelId == null || buendelId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("Buendel not found: " + buendelId);
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}

		BuendelService service = serviceFactory.createBuendelService();
		buendel = service.findById(buendelId, new Preload[] {
		        new Preload(Buendel.class, "gleissperrungen"),
		        new Preload(Buendel.class, "fahrplanregelungen"),
		        new Preload(Buendel.class, "weitereStrecken") });

		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("Buendel not found: " + buendelId);
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendel: " + buendel.getId());

		EasyAccessDecisionVoter voter = serviceFactory.createBuendelAnyVoter();
		if (voter.vote(secUser, buendel, "ROLE_BUENDEL_LOESCHEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			if (log.isDebugEnabled())
				log.debug("ACCESS DENIED");
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		// Darf geändert werden:
		if (buendel.isFixiert()) {
			darfVeraendertWerden = false;
			addError("buendel.message.keineAenderungenErlaubt");
		}

		// Keine Gleissperrungen zugeordnet:
		if (buendel.getGleissperrungen() != null && buendel.getGleissperrungen().size() > 0) {
			darfVeraendertWerden = false;
			addError("buendel.message.gleissperrungenZugeordnet");
		}

		// Keine Fahrplanregelungen zugeordnet:
		if (buendel.getFahrplanregelungen() != null && buendel.getFahrplanregelungen().size() > 0) {
			darfVeraendertWerden = false;
			addError("buendel.message.fahrplanregelungenZugeordnet");
		}

		if (darfVeraendertWerden) {
			if (buendelForm.getDelete() != null) {
				// Buendel deaktivieren
				if (buendelForm.getDelete() == true && !buendel.isDeleted()) {
					buendel.setDeleted(true);
					service.update(buendel);

					addMessage("success.deleted");
					return mapping.findForward("SUCCESS");
				}
				// Buendel aktivieren
				else if (buendelForm.getDelete() == false && buendel.isDeleted()) {
					buendel.setDeleted(false);
					service.update(buendel);

					addMessage("success.undelete");
					return mapping.findForward("SUCCESS");
				}
			}
		}

		return mapping.findForward("SUCCESS");
	}
}
