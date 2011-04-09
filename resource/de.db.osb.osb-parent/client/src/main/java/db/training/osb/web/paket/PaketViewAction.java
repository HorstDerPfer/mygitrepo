package db.training.osb.web.paket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Paket;
import db.training.osb.service.PaketService;
import db.training.security.hibernate.TqmUser;

public class PaketViewAction extends BaseAction {

	private static final Logger log = Logger.getLogger(PaketViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering PaketEditAction.");

		TqmUser secUser = getSecUser();

		PaketForm paketForm = (PaketForm) form;

		if (paketForm.getId() == null) {
			if (log.isDebugEnabled())
				log.debug("No paket found");
			addError("error.notfound", msgRes.getMessage("paket"));
			return mapping.findForward("FAILURE");
		}

		PaketService paketService = EasyServiceFactory.getInstance().createPaketService();
		Paket paket = paketService.findById(paketForm.getId(), new Preload[] { new Preload(
		    Paket.class, "massnahmen") });

		if (EasyServiceFactory.getInstance().createPaketAnyVoter().vote(secUser, paket,
		    "ROLE_PAKET_LESEN") == AccessDecisionVoter.ACCESS_GRANTED) {
			initForm();
		} else {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		if (!sessionFahrplanjahr.equals(paket.getFahrplanjahr())) {
			addError("error.fahrplanjahr.wrong");
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("paket", paket);
		return mapping.findForward("SUCCESS");
	}

	private void initForm() {
	}
}
