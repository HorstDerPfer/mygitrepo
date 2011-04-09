package db.training.osb.web.korridor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Korridor;
import db.training.osb.service.KorridorService;

public class KorridorEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(KorridorEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering KorridorEditAction.");

		Korridor korridor = null;
		KorridorForm korridorForm = (KorridorForm) form;
		Integer korridorId = null;

		if (log.isDebugEnabled())
			log.debug("Form: " + korridorForm);

		// request-ID wird in SaveAction gesetzt und somit nur nach forward genutzt
		if (request.getAttribute("korridorId") != null)
			korridorId = (Integer) request.getAttribute("korridorId");
		else
			korridorId = korridorForm.getKorridorId();

		if (korridorId == null) {
			if (log.isDebugEnabled())
				log.debug("No korridorId given. Setting \"0\"");
			korridorId = 0;
		}
		if (log.isDebugEnabled())
			log.debug("Processing korridorId: " + korridorId);

		if (korridorId != 0) {
			KorridorService korridorService = serviceFactory.createKorridorService();
			korridor = korridorService.findById(korridorId);

			if (korridor == null) {
				if (log.isDebugEnabled())
					log.debug("Korridor not found: " + korridorId);
				addError("error.korridor.notfound");
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing korridor: " + korridor.getId());

			// TODO: Rechteprüfung: Korridor bearbeiten
			// if (serviceFactory.createUserAnyVoter().vote(secUser, user,
			// "ROLE_BENUTZER_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
			// return mapping.findForward("ACCESS_DENIED");

			if (!hasErrors(request)) {
				korridorForm.reset();
				korridorForm.setKorridorId(korridorId);
				korridorForm.setName(korridor.getName());
			}
		} else {
			// TODO: Rechteprüfung: Korridor bearbeiten
			// if (!secUser.hasAuthorization("ROLE_BENUTZER_ANLEGEN_ALLE")
			// && !secUser.hasAuthorization("ROLE_BENUTZER_ANLEGEN_REGIONALBEREICH"))
			// return mapping.findForward("ACCESS_DENIED");

			if (!hasErrors(request))
				korridorForm.reset();
			korridorForm.setKorridorId(0);
		}

		request.setAttribute("user", korridor);
		return mapping.findForward("SUCCESS");
	}
}
