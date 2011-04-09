package db.training.bob.web.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserService;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshBearbeiterAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshBearbeiterAction.class);

	private BaumassnahmeService baumassnahmeService;

	private UserService userService;

	public RefreshBearbeiterAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		userService = serviceFactory.createUserService();
	}

	@Override
	public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshBearbeiterAction.");

		User loginUser = getLoginUser(request);

		Integer bmId = null;
		String name = null;
		Integer bearbeiterId = null;
		Baumassnahme bm = null;

		if (request.getParameter("id") != null)
			bmId = FrontendHelper.castStringToInteger(request.getParameter("id"));
		if (request.getParameter("name") != null)
			name = request.getParameter("name");
		if (request.getParameter("bearbeiterId") != null)
			bearbeiterId = FrontendHelper.castStringToInteger(request.getParameter("bearbeiterId"));

		if (bmId != null) {
			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BEARBEITER };
			bm = baumassnahmeService.findById(bmId, fetchPlans);
		}

		String errorKey = null;
		boolean update = false;

		if (bm != null) {

			User user = null;

			if (name != null && !name.equals("")) {
				user = userService.findUserByLoginName(name);
				if (user != null) {
					if (!bm.getBearbeiter().contains(user)) {
						// Set<User> b = (Set<User>) bm.getBearbeiter();
						// b.add(user);
						// bm.setUser(b);
						update = true;
					} else
						errorKey = "error.bearbeiter.vorhanden";
				} else
					errorKey = "error.bearbeiter.notfound";
			}

			if (bearbeiterId == null && (name == null || name.equals(""))) // versuch hinzufügen,
				// aber kein name
				errorKey = "error.required.common"; // TODO

			if (bearbeiterId != null) { // löschen
				user = userService.findUserById(bearbeiterId);
				if (user != null) {
					bm.getBearbeiter().remove(user);
					update = true;
				} else
					errorKey = "error.bearbeiter.notfound"; // TODO
			}

			if (update) {
				bm.setLastChange(null);

				baumassnahmeService.update(bm);
			}
		}

		request.setAttribute("errorKey", errorKey);
		request.setAttribute("baumassnahme", bm);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		return mapping.findForward("SUCCESS");
	}
}