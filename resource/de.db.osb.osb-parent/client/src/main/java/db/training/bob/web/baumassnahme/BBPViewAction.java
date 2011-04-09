package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.service.BBPMassnahmeService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class BBPViewAction extends BaseAction {

	private static Logger log = Logger.getLogger(BBPViewAction.class);

	private BBPMassnahmeService bbpService;

	public BBPViewAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		bbpService = serviceFactory.createBBPMassnahmeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BBPViewAction.execute.");

		// aus BaseAction.execute, da execute zum Test umgangen werden muss (wegen Userverwaltung)
		this.errors.set(new ActionMessages());
		this.messages.set(new ActionMessages());

		BBPMassnahme bbp = null;
		BBPMassnahmeForm bbpForm = (BBPMassnahmeForm) form;

		Integer id = null;

		if (request.getAttribute("id") != null)
			id = (Integer) request.getAttribute("id");
		else
			id = bbpForm.getId();

		Integer baumassnahmeID = FrontendHelper.castStringToInteger(request
		    .getParameter("baumassnahmeID"));
		String mode = request.getParameter("mode");

		if (id == null || id.equals(0)) {
			logNotFoundError(id);
			return mapping.findForward("FAILURE");
		}

		bbp = bbpService.findById(id,
		    new Preload[] { new Preload(BBPMassnahme.class, "regelungen") });

		if (bbp == null) {
			logNotFoundError(id);
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing BBPMassnahme: " + bbp.getId());

		request.setAttribute("bbpmassnahme", bbp);
		request.setAttribute("baumassnahmeID", baumassnahmeID);
		request.setAttribute("mode", mode);
		return mapping.findForward("SUCCESS");
	}

	private void logNotFoundError(Integer id) {
		if (log.isDebugEnabled())
			log.debug("BBPmassnahme not found: " + id);
		addError("error.bbpmassnahme.notfound");
	}

	public ActionForward testHelp(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		return run(mapping, form, request, response);
	}
}
