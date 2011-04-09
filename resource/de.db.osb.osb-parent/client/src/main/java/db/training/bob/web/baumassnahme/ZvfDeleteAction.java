package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.UebergabeblattService;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class ZvfDeleteAction extends BaseAction {

	private static Logger log = null;

	public ZvfDeleteAction() {
		log = Logger.getLogger(this.getClass());
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering ZvfDeleteAction.");

		try {
			BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
			Integer id = baumassnahmeForm.getId();
			if (log.isDebugEnabled())
				log.debug("bmid: " + id);
			if (id == null)
				mapping.findForward("FAILURE");

			BaumassnahmeService bmService = serviceFactory.createBaumassnahmeService();
			Baumassnahme baumassnahme = bmService.findById(id, new FetchPlan[] {
			        FetchPlan.BOB_BBZR, FetchPlan.ZVF_MN_VERSION, FetchPlan.BBZR_BAUMASSNAHMEN,
			        FetchPlan.BBZR_HEADER, FetchPlan.UEB_HEADER_EMPFAENGER,
			        FetchPlan.UEB_HEADER_SENDER });
			Uebergabeblatt bbzr = baumassnahme.getAktuelleZvf();
			baumassnahme.getZvf().remove(bbzr);
			bmService.update(baumassnahme);

			UebergabeblattService uebService = serviceFactory.createUebergabeblattService();
			uebService.delete(bbzr);
		} catch (Exception e) {
			request.setAttribute("tab", "BBZR");
			addError("error.zvf.delete");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("tab", "BBZR");
		addMessage("success.zvf.delete");
		return mapping.findForward("SUCCESS");
	}
}
