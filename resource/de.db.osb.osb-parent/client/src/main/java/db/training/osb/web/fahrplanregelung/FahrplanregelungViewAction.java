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
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.security.domain.FahrplanregelungAnyVoter;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungViewAction extends BaseAction {

	private static final Logger log = Logger.getLogger(FahrplanregelungViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungViewAction.");

		TqmUser secUser = getSecUser();

		Fahrplanregelung fahrplanregelung = null;
		FahrplanregelungForm fahrplanregelungForm = (FahrplanregelungForm) form;
		Integer fahrplanregelungId = fahrplanregelungForm.getFahrplanregelungId();

		if (fahrplanregelungId == null || fahrplanregelungId.equals(0)) {
			if (log.isDebugEnabled())
				log.debug("fahrplanregelung not found: " + fahrplanregelungId);
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}

		fahrplanregelung = serviceFactory.createFahrplanregelungService().findById(
		    fahrplanregelungId,
		    new Preload[] { new Preload(Fahrplanregelung.class, "buendel"),
		            new Preload(Fahrplanregelung.class, "betriebsstelleVon"),
		            new Preload(Fahrplanregelung.class, "betriebsstelleBis"),
		            new Preload(Fahrplanregelung.class, "gleissperrungen"),
		            new Preload(Fahrplanregelung.class, "umleitungFahrplanregelungLinks"),
		            new Preload(UmleitungFahrplanregelungLink.class, "umleitung"),
		            new Preload(UmleitungFahrplanregelungLink.class, "fahrplanregelung"),
		            new Preload(Umleitung.class, "umleitungswege"),
		            new Preload(Umleitung.class, "umleitungFahrplanregelungLinks"),
		            new Preload(Gleissperrung.class, "buendel"),
		            new Preload(Regelung.class, "massnahme"),
		            new Preload(SAPMassnahme.class, "genehmiger"),
		            new Preload(Regelung.class, "vtr"),
		            new Preload(Regelung.class, "vzgStrecke") });

		if (fahrplanregelung == null) {
			if (log.isDebugEnabled())
				log.debug("fahrplanregelung not found: " + fahrplanregelungId);
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}

		// Rechteprüfung: Fahrplanregelung lesen
		FahrplanregelungAnyVoter voter = EasyServiceFactory.getInstance()
		    .createFahrplanregelungAnyVoter();
		if (voter.vote(secUser, fahrplanregelung, "ROLE_FAHRPLANREGELUNG_LESEN") == AccessDecisionVoter.ACCESS_DENIED) {
			if (log.isDebugEnabled()) {
				log.debug("User '" + secUser.getUsername()
				    + "' hat keine Berechtigung fuer: ROLE_FAHRPLANREGELUNG_LESEN");
			}
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("fahrplanregelung", fahrplanregelung);

		return mapping.findForward("SUCCESS");
	}
}
