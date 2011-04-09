package db.training.osb.web.topprojekt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Anmelder;
import db.training.osb.model.TopProjekt;
import db.training.osb.service.AnmelderService;
import db.training.osb.service.TopProjektService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class TopProjektSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(TopProjektSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering TopProjektSaveAction.");

		TqmUser secUser = getSecUser();

		TopProjektForm topProjektForm = (TopProjektForm) form;
		Integer topProjektId = topProjektForm.getTopProjektId();

		if (request.getParameter("reset") != null) {
			topProjektForm.reset();
		}

		EasyAccessDecisionVoter voter = EasyServiceFactory.getInstance().createTopProjektAnyVoter();
		RegionalbereichService rbService = serviceFactory.createRegionalbereichService();
		TopProjektService topProjektService = serviceFactory.createTopProjektService();
		TopProjekt topProjekt = topProjektService.findById(topProjektId, new Preload[] {
		        new Preload(TopProjekt.class, "regionalbereich"),
		        new Preload(TopProjekt.class, "massnahmen") });

		if (topProjektId == 0) {
			topProjekt = new TopProjekt();
		}

		// Formulardaten verarbeiten
		if (topProjektForm.getAnmelderId() != null) {
			AnmelderService service = EasyServiceFactory.getInstance().createAnmelderService();
			Anmelder a = service.findById(topProjektForm.getAnmelderId());
			topProjekt.setAnmelder(a);
		}

		topProjekt.setBaukosten(FrontendHelper
		    .castStringToBigDecimal(topProjektForm.getBaukosten()));
		topProjekt.setName(topProjektForm.getName());
		topProjekt.setSapProjektNummer(topProjektForm.getSapProjektNummer());
		Regionalbereich rb = rbService.findById(topProjektForm.getRegionalbereichId());
		topProjekt.setRegionalbereich(rb);
		
		topProjekt.setLastChange(getLoginUser(request));

		if (topProjektId == 0) {
			if (voter.vote(secUser, topProjekt, "ROLE_TOPPROJEKT_ANLEGEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				// TOP-Projekt erstellen
				topProjekt.setFahrplanjahr(sessionFahrplanjahr);
				topProjektService.create(topProjekt);

			} else {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
		} else {
			if (voter.vote(secUser, topProjekt, "ROLE_TOPPROJEKT_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				// Änderungen speichern
				topProjektService.update(topProjekt);

			} else {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
		}

		// Projekt ID in Form speichern, damit EditAction den Datensatz laden kann
		topProjektForm.reset();
		topProjektForm.setTopProjektId(topProjekt.getId());

		addMessage("success.saved");

		request.setAttribute("projekt", topProjekt);
		request.setAttribute("listRegionalbereiche", EasyServiceFactory.getInstance()
		    .createRegionalbereichService().findAll());

		return mapping.findForward("SUCCESS");
	}
}
