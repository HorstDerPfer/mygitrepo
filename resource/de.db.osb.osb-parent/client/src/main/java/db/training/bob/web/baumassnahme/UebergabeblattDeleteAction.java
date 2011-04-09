package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.service.BaumassnahmeService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class UebergabeblattDeleteAction extends BaseAction {

	private static Logger log = Logger.getLogger(UebergabeblattDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattDeleteAction.");
		Integer id = null;
		BaumassnahmeForm baumassnahmeForm = null;
		Baumassnahme baumassnahme = null;

		try {
			baumassnahmeForm = (BaumassnahmeForm) form;
			id = baumassnahmeForm.getId();
			if (log.isDebugEnabled())
				log.debug("bmid: " + id);
			if (id == null)
				mapping.findForward("FAILURE");

			BaumassnahmeService bmService = EasyServiceFactory.getInstance()
			    .createBaumassnahmeService();
			baumassnahme = bmService.findById(id);
			// Uebergabeblatt uebergabeblatt = baumassnahme.getUebergabeblatt();
			baumassnahme.setUebergabeblatt(null);

			// UebergabeblattService uebService = EasyServiceFactory.getInstance()
			// .createUebergabeblattService();
			// uebService.delete(uebergabeblatt);
			bmService.update(baumassnahme);
		} catch (Exception e) {
			request.setAttribute("tab", "Uebergabeblatt");
			addError("error.uebergabeblatt.delete");
			return mapping.findForward("FAILURE");
		}
		request.setAttribute("tab", "Uebergabeblatt");
		addMessage("success.uebergabeblatt.delete");
		return mapping.findForward("SUCCESS");
	}
}
