package db.training.bob.web.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.service.BaumassnahmeService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class BearbeiterViewAction extends BaseAction {

	private BaumassnahmeService service;

	private Logger log;

	public BearbeiterViewAction() {
		log = Logger.getLogger(this.getClass());
		try {
			serviceFactory = EasyServiceFactory.getInstance();
			service = serviceFactory.createBaumassnahmeService();
		} catch (Exception e) {// do nothing
		}
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering BearbeiterViewAction.");

		int id = 0;

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("id"))) {
			id = FrontendHelper.castStringToInteger(request.getParameter("id"));
		}

		Preload[] preloads = new Preload[] { new Preload(Baumassnahme.class, "bearbeiter") };
		Baumassnahme baumassnahme = service.findById(id, preloads);

		request.setAttribute("baumassnahme", baumassnahme);

		return mapping.findForward("SUCCESS");
	}

	public void setBaumassnahmeService(BaumassnahmeService service) {
		this.service = service;
	}

}
