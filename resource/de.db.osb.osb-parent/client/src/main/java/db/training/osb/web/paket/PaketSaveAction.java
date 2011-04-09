package db.training.osb.web.paket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Paket;
import db.training.osb.service.PaketService;

public class PaketSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(PaketSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering PaketSaveAction.");

		PaketForm paketForm = (PaketForm) form;
		Integer id;
		try {
			id = paketForm.getId();
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Paket not found");
			// -- TODO
			addError("error.baumassnahme.notfound");
			return mapping.findForward("FAILURE");
		}

		PaketService paketService = EasyServiceFactory.getInstance().createPaketService();

		if (id != 0) {
			Paket paket = paketService.findById(id);
			initMassnahme();

			paket.setLastChange(null);
			paketService.update(paket);
			if (log.isDebugEnabled())
				log.debug("Find Paket by Id: " + id);
		} else {
			if (log.isDebugEnabled())
				log.debug("Paket not found");
			// TODO
			addError("error.baumassnahme.notfound");
			return mapping.findForward("FAILURE");
		}

		// TODO if failure

		addMessage("success.saved");
		return mapping.findForward("SUCCESS");
	}

	private void initMassnahme() {
	}
}
