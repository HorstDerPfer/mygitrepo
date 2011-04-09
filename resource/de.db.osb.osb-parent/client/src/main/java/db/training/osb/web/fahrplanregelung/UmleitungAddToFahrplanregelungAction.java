package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.UmleitungFahrplanregelungLinkService;
import db.training.osb.service.UmleitungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class UmleitungAddToFahrplanregelungAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungAddToFahrplanregelungAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungAddToFahrplanregelungAction.");

		TqmUser secUser = getSecUser();

		UmleitungenToFahrplanregelungForm inputForm = (UmleitungenToFahrplanregelungForm) form;

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();
		UmleitungService umlService = serviceFactory.createUmleitungService();

		UmleitungFahrplanregelungLinkService uflService = serviceFactory
		    .createUmleitungFahrplanregelungLinkService();

		Integer frId = inputForm.getFahrplanregelungId();

		Fahrplanregelung fahrplanregelung = frService.findById(frId);

		Integer umleitungId = inputForm.getUmleitungId();

		if (frId == null || umleitungId == null || frId == 0 || umleitungId == 0) {
			addError("error.umleitungToFahrplanregelung.keineumleitung");
			return mapping.findForward("FAILURE");
		}

		EasyAccessDecisionVoter voter = EasyServiceFactory.getInstance()
		    .createFahrplanregelungAnyVoter();
		if (voter.vote(secUser, fahrplanregelung, "ROLE_FAHRPLANREGELUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			UmleitungFahrplanregelungLink ufLink = new UmleitungFahrplanregelungLink();
			ufLink.setFahrplanregelung(fahrplanregelung);

			ufLink.setAnzahlSGV(inputForm.getAnzahlSGV());
			ufLink.setAnzahlSPFV(inputForm.getAnzahlSPFV());
			ufLink.setAnzahlSPNV(inputForm.getAnzahlSPNV());

			ufLink.setAnzahlSGVGegenRich(inputForm.getAnzahlSGVGegenRich());
			ufLink.setAnzahlSPFVGegenRich(inputForm.getAnzahlSPFVGegenRich());
			ufLink.setAnzahlSPNVGegenRich(inputForm.getAnzahlSPNVGegenRich());

			Umleitung umleitung = umlService.findById(umleitungId);
			ufLink.setUmleitung(umleitung);

			ufLink.setLastChange(null);

			uflService.create(ufLink);
		} else {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("fahrplanregelungId", fahrplanregelung.getId());

		return mapping.findForward("SUCCESS");
	}
}