package db.training.bob.web.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserService;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.security.domain.BaumassnahmeAnyVoter;
import db.training.security.hibernate.TqmUser;

public class BearbeiterAddAction extends BaseAction {

	private BaumassnahmeService service;

	private Logger log;

	public BearbeiterAddAction() {
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
			log.debug("Entering BearbeiterAddAction.");

		int id = 0;
		int userId = 0;

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("id"))) {
			id = FrontendHelper.castStringToInteger(request.getParameter("id"));
		}
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("userId"))) {
			userId = FrontendHelper.castStringToInteger(request.getParameter("userId"));
		}

		Baumassnahme baumassnahme = service.findById(id, new Preload[] { new Preload(
		    Baumassnahme.class, "bearbeiter") });

		UserService userService = serviceFactory.createUserService();
		User user = userService.findUserById(userId);

		BaumassnahmeAnyVoter voter = EasyServiceFactory.createBaumassnahmeAnyVoter();
		TqmUser secUser = getSecUser();

		// es duerfen nur User, welche die Rolle ROLE_FAVORIT_BEARBEITEN_ALLE besitzen die
		// Baumassnahme speichern
		if (voter.vote(secUser, baumassnahme, "ROLE_FAVORIT_BEARBEITEN_ALLE") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		} else {
			if (user != null) {
				baumassnahme.addBearbeiter(user);
				service.update(baumassnahme);
			}
		}

		List<User> bearbeiter = EasyServiceFactory.getInstance().createBearbeiterService()
		    .listAvailableBearbeiter(baumassnahme, user.getRegionalbereich().getId());

		request.setAttribute("possibleBearbeiterList", bearbeiter);

		Map<String, Boolean> bearbeiterMap = new HashMap<String, Boolean>();
		if (baumassnahme.getBearbeiter() != null)
			if (baumassnahme.getBearbeiter().size() > 0)
				for (Bearbeiter b : baumassnahme.getBearbeiter()) {
					bearbeiterMap.put(b.getId().toString(), b.getAktiv());
				}
		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		baumassnahmeForm.setBearbeiter(bearbeiterMap);

		request.setAttribute("baumassnahme", baumassnahme);
		User currentUser = getLoginUser(request);
		request.setAttribute("currentUser", currentUser);

		return mapping.findForward("SUCCESS");
	}

	public void setBaumassnahmeService(BaumassnahmeService service) {
		this.service = service;
	}

}
