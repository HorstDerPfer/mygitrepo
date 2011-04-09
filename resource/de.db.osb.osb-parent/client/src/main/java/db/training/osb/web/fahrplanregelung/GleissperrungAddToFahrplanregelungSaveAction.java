package db.training.osb.web.fahrplanregelung;

import java.util.List;

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
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.GleissperrungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungAddToFahrplanregelungSaveAction extends BaseAction {

	private static final Logger log = Logger
	    .getLogger(GleissperrungAddToFahrplanregelungSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungAddToFahrplanregelungSaveAction.");

		TqmUser secUser = getSecUser();

		FahrplanregelungGleissperrungFilterForm inputForm = (FahrplanregelungGleissperrungFilterForm) form;

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();
		GleissperrungService gsService = serviceFactory.createGleissperrungService();

		Integer frId = inputForm.getFahrplanregelungId();

		Fahrplanregelung fahrplanregelung = frService.findById(frId, new Preload[] { new Preload(
		    Fahrplanregelung.class, "gleissperrungen") });
		Integer[] gleissperrungenIds = inputForm.getGleissperrungenIds();

		if (frId == null || gleissperrungenIds == null || frId == 0
		    || gleissperrungenIds.length == 0) {
			addError("error.gleissperrungToFahrplanregelung.keineGleissperrung");
			return mapping.findForward("FAILURE");
		}

		EasyAccessDecisionVoter voter = EasyServiceFactory.getInstance()
		    .createFahrplanregelungAnyVoter();
		if (voter.vote(secUser, fahrplanregelung, "ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			List<Gleissperrung> gleissperrungen = gsService.findByIds(gleissperrungenIds,
			    new Preload[] { new Preload(Gleissperrung.class, "fahrplanregelung"),
			            new Preload(Gleissperrung.class, "massnahme"),
			            new Preload(SAPMassnahme.class, "genehmiger") });

			if (gleissperrungen != null) {
				for (Gleissperrung gs : gleissperrungen) {
					EasyAccessDecisionVoter gsVoter = EasyServiceFactory.getInstance()
					    .createRegelungAnyVoter();
					if (gsVoter.vote(secUser, gs, "ROLE_FAHRPLANREGELUNG_GLEISSPERRUNG_ZUORDNEN") == AccessDecisionVoter.ACCESS_GRANTED) {

						if (gs.getFahrplanregelung() == null){
							//fahrplanregelung.getGleissperrungen().add(gs);
							gs.setFahrplanregelung(fahrplanregelung);
							gsService.update(gs);
						}

					} else {
						addError("common.noAuth");
					}
				}

				if (!hasErrors()) {
					//frService.update(fahrplanregelung);
					
					addMessage("success.saved");
				}
			}
		} else {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("fahrplanregelungId", fahrplanregelung.getId());

		inputForm.setGleissperrungenIds(null);
		return mapping.findForward("SUCCESS");
	}
}
