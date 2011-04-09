package db.training.osb.web.fahrplanregelung;

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
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.service.BuendelService;
import db.training.osb.service.FahrplanregelungService;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungDeleteFromBuendelAction extends BaseAction {

	private static final Logger log = Logger
	    .getLogger(FahrplanregelungDeleteFromBuendelAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungDeleteFromBuendelAction.");

		TqmUser secUser = getSecUser();

		FahrplanregelungSearchForm frsForm = (FahrplanregelungSearchForm) form;

		// fahrplanregelung ###################################################
		Integer frId = frsForm.getFahrplanregelungId();

		if (frId == null) {
			if (log.isDebugEnabled())
				log.debug("No fahrplanregelungId found");
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing fahrplanregelungId: " + frId);

		// Fahrplanregelung laden
		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();
		Fahrplanregelung fahrplanregelung = frService.findById(frId, new Preload[] { new Preload(
		    Fahrplanregelung.class, "buendel") });

		// Fahrplanregelung pruefen ****************************************
		if (fahrplanregelung == null) {
			if (log.isDebugEnabled())
				log.debug("No fahrplanregelung found");
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing fahrplanregelung: " + fahrplanregelung.getId());

		// Buendel ###############################################################
		Integer buendelId = frsForm.getBuendelId();

		// buendelId pruefen **************************************
		if (buendelId == null) {
			if (log.isDebugEnabled())
				log.debug("No buendelId found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendelId: " + buendelId);

		BuendelService bService = serviceFactory.createBuendelService();
		Buendel buendel = bService.findById(buendelId, new Preload[] { new Preload(Buendel.class,
		    "fahrplanregelungen") });

		// Buendel pruefen ****************************************
		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("No buendel found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendel: " + buendel.getId());

		// Rechtepruefung ***************************************************************
		if (EasyServiceFactory.getInstance().createBuendelAnyVoter().vote(secUser, buendel,
		    "ROLE_BUENDEL_FAHRPLANREGELUNG_LOESCHEN") == AccessDecisionVoter.ACCESS_DENIED) {
			if (log.isDebugEnabled())
				log.debug("User '" + secUser.getUsername()
				    + "' hat keine Berechtigung fuer: ROLE_BUENDEL_FAHRPLANREGELUNG_LOESCHEN");
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		fahrplanregelung.getBuendel().remove(buendel);
		frService.update(fahrplanregelung);

		return mapping.findForward("SUCCESS");
	}
}