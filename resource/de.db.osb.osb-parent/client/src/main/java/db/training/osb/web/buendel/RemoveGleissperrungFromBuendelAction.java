package db.training.osb.web.buendel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.service.BuendelService;
import db.training.osb.service.GleissperrungService;
import db.training.security.hibernate.TqmUser;

/**
 * entfernt eine Gleissperrung aus einem Bündel.
 * 
 * @author michels
 * 
 */
public class RemoveGleissperrungFromBuendelAction extends BaseAction {

	private static Logger log = Logger.getLogger(RemoveGleissperrungFromBuendelAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RemoveGleissperrungFromBuendelAction.");

		TqmUser secUser = getSecUser();

		BuendelService buendelService = serviceFactory.createBuendelService();
		GleissperrungService gleissperrungService = serviceFactory.createGleissperrungService();

		Integer buendelId = FrontendHelper.castStringToInteger(request.getParameter("buendelId"));
		Integer gleissperrungId = FrontendHelper.castStringToInteger(request
		    .getParameter("gleissperrungId"));

		Buendel buendel = buendelService.findById(buendelId, new Preload[] { new Preload(
		    Buendel.class, "gleissperrungen") });
		Gleissperrung gleissperrung = gleissperrungService.findById(gleissperrungId,
		    new Preload[] { new Preload(Gleissperrung.class, "buendel") });

		// BuendelId pruefen ****************************************
		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("No buendel found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendel: " + buendelId);

		if (gleissperrung == null) {
			if (log.isInfoEnabled())
				log.info("Gleissperrung nicht gefunden: " + gleissperrungId.toString());
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}

		if (EasyServiceFactory.getInstance().createBuendelAnyVoter()
		    .vote(secUser, buendel, "ROLE_BUENDEL_GLEISSPERRUNG_ZUORDNEN") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		gleissperrung.getBuendel().remove(buendel);
		buendel.getGleissperrungen().remove(gleissperrung);

		// Änderungen speichern
		gleissperrungService.update(gleissperrung);
		buendelService.update(buendel);

		request.setAttribute("id", buendelId);

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);

		addMessage("success.gleissperrung.remove", "" + gleissperrung.getLfdNr());

		return mapping.findForward("SUCCESS");
	}
}