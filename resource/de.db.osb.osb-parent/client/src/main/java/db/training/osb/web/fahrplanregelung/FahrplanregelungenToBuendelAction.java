package db.training.osb.web.fahrplanregelung;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.service.BuendelService;
import db.training.osb.service.FahrplanregelungService;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungenToBuendelAction extends BaseAction {

	private static final Logger log = Logger.getLogger(FahrplanregelungenToBuendelAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungenToBuendelAction.");

		TqmUser secUser = getSecUser();

		FahrplanregelungSearchForm frsForm = (FahrplanregelungSearchForm) form;
		BuendelService buendelService = serviceFactory.createBuendelService();
		Integer buendelId = frsForm.getBuendelId();

		// BuendelId pruefen ****************************************
		if (buendelId == null) {
			if (log.isDebugEnabled())
				log.debug("No buendelId found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendelId: " + buendelId);

		Buendel buendel = buendelService.findById(buendelId);

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
		    "ROLE_BUENDEL_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
			if (log.isDebugEnabled())
				log.debug("User '" + secUser.getUsername()
				    + "' hat keine Berechtigung fuer: ROLE_BUENDEL_BEARBEITEN");
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();
		List<Fahrplanregelung> fahrplanregelungen = frService
		    .findUnlinkedByBuendelIdAndFahrplanjahr(buendelId, sessionFahrplanjahr,
		        new FetchPlan[] { FetchPlan.OSB_FPL_BST_VON, FetchPlan.OSB_FPL_BST_BIS });

		request.setAttribute("buendel", buendel);
		request.setAttribute("fahrplanregelungen", fahrplanregelungen);

		return mapping.findForward("SUCCESS");
	}
}