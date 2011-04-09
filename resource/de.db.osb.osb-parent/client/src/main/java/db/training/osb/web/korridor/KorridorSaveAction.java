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

public class KorridorSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(KorridorSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering /KorridorSaveAction.");

		Korridor korridor = null;
		KorridorForm korridorForm = (KorridorForm) form;
		Integer korridorId = korridorForm.getKorridorId();

		KorridorService korridorService = serviceFactory.createKorridorService();

		if (korridorId == 0) {
			korridor = new Korridor();
			if (log.isDebugEnabled())
				log.debug("Create new Korridor: " + korridorId);
		} else {
			korridor = korridorService.findById(korridorId);
			if (log.isDebugEnabled())
				log.debug("Find Korridor by Id: " + korridorId);
		}

		if (korridor == null) {
			if (log.isDebugEnabled())
				log.debug("Korridor not found: " + korridorId);
			addError("error.korridor.notfound");
			return mapping.findForward("FAILURE");
		}

		korridor.setName(korridorForm.getName().equals("") ? null : korridorForm.getName());
		korridor.setLastChange(null);

		// create or update
		if (korridorId == 0) {
			korridorService.create(korridor);
		} else {
			korridorService.update(korridor);
		}
		request.setAttribute("korridorId", korridor.getId());
		addMessage("success.korridor.save");
		return mapping.findForward("SUCCESS");
	}
}