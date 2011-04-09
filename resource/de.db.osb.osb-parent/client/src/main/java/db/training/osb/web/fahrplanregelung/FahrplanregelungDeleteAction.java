package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.service.FahrplanregelungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungDeleteAction extends BaseAction {

	private static final Logger log = Logger.getLogger(FahrplanregelungDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungDeleteAction.");

		TqmUser secUser = getSecUser();

		Fahrplanregelung fplr = null;
		FahrplanregelungForm fplrForm = (FahrplanregelungForm) form;
		Integer fplrId = fplrForm.getFahrplanregelungId();

		if (fplrId == null || fplrId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("Fahrplanregelung not found: " + fplrId);
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}

		FahrplanregelungService service = serviceFactory.createFahrplanregelungService();
		fplr = service.findById(fplrForm.getFahrplanregelungId(), new Preload[] { new Preload(
		    Fahrplanregelung.class, "buendel") });

		if (fplr == null) {
			if (log.isDebugEnabled())
				log.debug("Fahrplanregelung not found: " + fplrId);
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing fahrplanregelung: " + fplr.getId());

		EasyAccessDecisionVoter voter = serviceFactory.createFahrplanregelungAnyVoter();
		if (voter.vote(secUser, fplr, "ROLE_FAHRPLANREGELUNG_LOESCHEN") != AccessDecisionVoter.ACCESS_GRANTED) {
			if (log.isDebugEnabled())
				log.debug("ACCESS DENIED");
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		if (fplrForm.getDelete() != null) {
			// Fahrplanregelung deaktivieren
			if (fplrForm.getDelete() == true && !fplr.isDeleted()) {
				fplr.setDeleted(true);
				service.update(fplr);
				/*
				 * List<UmleitungFahrplanregelungLink> ufLinks =
				 * uflService.findByFahrplanregelung(r, null); for (UmleitungFahrplanregelungLink
				 * link : ufLinks){ uflService.delete(link); } frService.delete(r);
				 */
				addMessage("success.deleted");
				return mapping.findForward("SUCCESS");
			}
			// Fahrplanregelung aktivieren
			else if (fplrForm.getDelete() == false && fplr.isDeleted()) {
				fplr.setDeleted(false);
				service.update(fplr);

				addMessage("success.undelete");
				return mapping.findForward("SUCCESS");
			}
		}

		return mapping.findForward("SUCCESS");
	}
}
