package db.training.bob.web.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserService;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshUebMailEmpfaengerAction extends BaseAction {

	private static Logger log = Logger.getLogger(RefreshUebMailEmpfaengerAction.class);

	private RegionalbereichService rbService;

	private UserService userService;

	public RefreshUebMailEmpfaengerAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		rbService = serviceFactory.createRegionalbereichService();
		userService = serviceFactory.createUserService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshUebMailEmpfaengerAction.");

		Integer regionalBereich = null;

		if (request.getParameter("regionalBereich") != null)
			regionalBereich = Integer.parseInt(request.getParameter("regionalBereich"));

		if (regionalBereich == 0) {
			addError("error.ueb.mail.regionalbereich");
			return mapping.findForward("FAILURE");
		}

		BaumassnahmeForm bmForm = (BaumassnahmeForm) form;

		Regionalbereich rb = rbService.findById(regionalBereich);

		List<User> users = userService.findUsersByRegionalbereich(rb);

		bmForm.setUebMailEmpfaengerList(users);

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		return mapping.findForward("SUCCESS");
	}
}