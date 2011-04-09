package db.training.osb.web.masterbuendel;

import java.util.HashSet;
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

public class MasterBuendelSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(MasterBuendelSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering /MasterBuendelSaveAction.");

		MasterBuendel masterBuendel = null;
		MasterBuendelForm masterBuendelForm = (MasterBuendelForm) form;
		Integer masterBuendelId = masterBuendelForm.getMasterBuendelId();

		MasterBuendelService masterBuendelService = serviceFactory.createMasterBuendelService();
		KorridorService korridorService = EasyServiceFactory.getInstance().createKorridorService();

		if (masterBuendelId == 0) {
			masterBuendel = new MasterBuendel();
			if (log.isDebugEnabled())
				log.debug("Create new MasterBuendel: " + masterBuendelId);
		} else {
			masterBuendel = masterBuendelService.findById(masterBuendelId);
			if (log.isDebugEnabled())
				log.debug("Find MasterBuendel by Id: " + masterBuendelId);
		}

		if (masterBuendel == null) {
			if (log.isDebugEnabled())
				log.debug("MasterBuendel not found: " + masterBuendelId);
			addError("error.masterBuendel.notfound");
			return mapping.findForward("FAILURE");
		}

		Set<Korridor> korridorSet = new HashSet<Korridor>();
		if (masterBuendelForm.getKorridorId() != null) {
			Korridor korridor = korridorService.findById(masterBuendelForm.getKorridorId());
			if (korridor != null)
				korridorSet.add(korridor);
		}

		masterBuendel.setKorridor(korridorSet);
		masterBuendel.setLastChange(null);

		// create or update
		if (masterBuendelId == 0) {
			masterBuendelService.create(masterBuendel);
		} else {
			masterBuendelService.update(masterBuendel);
		}
		request.setAttribute("masterBuendelId", masterBuendel.getId());
		addMessage("success.masterBuendel.save");
		List<Korridor> korridorList = korridorService.findAll();
		request.setAttribute("korridore", korridorList);

		return mapping.findForward("SUCCESS");
	}
}