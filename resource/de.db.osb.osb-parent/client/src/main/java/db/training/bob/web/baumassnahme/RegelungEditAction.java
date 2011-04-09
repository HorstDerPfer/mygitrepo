package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Regelung;
import db.training.bob.service.RegelungService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class RegelungEditAction extends BaseAction {

	private static Logger log = Logger.getLogger(RegelungEditAction.class);

	private RegelungService regelungService;

	private static final String dateFormatString = "dd.MM.yy";

	public RegelungEditAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		regelungService = serviceFactory.createRegelungService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RegelungEditAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		RegelungForm regelungForm = (RegelungForm) form;
		
		Regelung regelung = regelungService.findById(regelungForm.getRegelungId());

		regelungForm.setBeginnFuerTerminberechnung(regelung.getBeginnFuerTerminberechnung());
		regelungForm.setBeginn(FrontendHelper.castDateToString(regelung.getBeginn(), "dd.MM.yyyy HH:mm"));
		regelungForm.setEnde(FrontendHelper.castDateToString(regelung.getEnde(), "dd.MM.yyyy HH:mm"));

		request.setAttribute("regelung", regelung);
		return mapping.findForward("SUCCESS");
	}
}
