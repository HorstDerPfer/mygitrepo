package db.training.osb.web.masterbuendel;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Korridor;
import db.training.osb.model.MasterBuendel;
import db.training.osb.service.KorridorService;
import db.training.osb.service.MasterBuendelService;

public class MasterBuendelEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(MasterBuendelEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering MasterBuendelEditAction.");

		MasterBuendel masterBuendel = null;
		MasterBuendelForm masterBuendelForm = (MasterBuendelForm) form;
		Integer masterBuendelId = null;

		if (log.isDebugEnabled())
			log.debug("Form: " + masterBuendelForm);

		// request-ID wird in SaveAction gesetzt und somit nur nach forward genutzt
		if (request.getAttribute("masterBuendelId") != null)
			masterBuendelId = (Integer) request.getAttribute("masterBuendelId");
		else
			masterBuendelId = masterBuendelForm.getMasterBuendelId();

		if (masterBuendelId == null) {
			if (log.isDebugEnabled())
				log.debug("No masterBuendelId given. Setting \"0\"");
			masterBuendelId = 0;
		}
		if (log.isDebugEnabled())
			log.debug("Processing masterBuendelId: " + masterBuendelId);

		// update
		if (masterBuendelId != 0) {
			MasterBuendelService masterBuendelService = serviceFactory.createMasterBuendelService();
			masterBuendel = masterBuendelService.findById(masterBuendelId);

			if (masterBuendel == null) {
				if (log.isDebugEnabled())
					log.debug("MasterBuendel not found: " + masterBuendelId);
				addError("error.masterBuendel.notfound");
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing masterBuendel: " + masterBuendel.getId());

			// TODO: Rechteprüfung: MasterBuendel bearbeiten
			// if (serviceFactory.createUserAnyVoter().vote(secUser, user,
			// "ROLE_BENUTZER_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
			// return mapping.findForward("ACCESS_DENIED");

			if (!hasErrors(request)) {
				masterBuendelForm.reset();
				masterBuendelForm.setMasterBuendelId(masterBuendelId);

				Integer korridorId = null;
				Set<Korridor> korridorSet = masterBuendel.getKorridor();
				if (korridorSet.size() > 1) {
					addError("error.masterbuendel.multiple.korridor");
				}

				for (Korridor korridor : korridorSet)
					korridorId = korridor.getId();
				masterBuendelForm.setKorridorId(korridorId);
			}

		}
		// create
		else {
			// TODO: Rechteprüfung: MasterBuendel bearbeiten
			// if (!secUser.hasAuthorization("ROLE_BENUTZER_ANLEGEN_ALLE")
			// && !secUser.hasAuthorization("ROLE_BENUTZER_ANLEGEN_REGIONALBEREICH"))
			// return mapping.findForward("ACCESS_DENIED");

			if (!hasErrors(request))
				masterBuendelForm.reset();
			masterBuendelForm.setMasterBuendelId(0);
		}

		KorridorService korridorService = EasyServiceFactory.getInstance().createKorridorService();
		List<Korridor> korridorList = korridorService.findAll();

		request.setAttribute("user", masterBuendel);
		request.setAttribute("korridore", korridorList);
		return mapping.findForward("SUCCESS");
	}
}
