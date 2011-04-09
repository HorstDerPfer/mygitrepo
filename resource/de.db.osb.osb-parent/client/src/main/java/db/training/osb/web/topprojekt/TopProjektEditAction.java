package db.training.osb.web.topprojekt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.model.User;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Anmelder;
import db.training.osb.model.TopProjekt;
import db.training.osb.service.AnmelderService;
import db.training.security.hibernate.TqmUser;

public class TopProjektEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(TopProjektEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering TopProjektEditAction.");

		TqmUser secUser = getSecUser();
		User currentUser = getLoginUser(request);

		TopProjektForm topProjektForm = (TopProjektForm) form;
		Integer topProjektId = topProjektForm.getTopProjektId();

		// BuendelId pruefen ****************************************
		if (topProjektId == null) {
			if (log.isDebugEnabled())
				log.debug("No topProjektId found");
			addError("error.notfound", msgRes.getMessage("topprojekt"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing topProjektId: " + topProjektId);

		AnmelderService anmelderService = serviceFactory.createAnmelderService();

		// Bearbeiten
		// ###########################################################
		if (topProjektId != 0) {
			TopProjekt topProjekt = serviceFactory.createTopProjektService().findById(
			    topProjektId,
			    new Preload[] { new Preload(TopProjekt.class, "regionalbereich"),
			            new Preload(TopProjekt.class, "massnahmen") });

			// topProjekt pruefen ****************************************
			if (topProjekt == null) {
				if (log.isDebugEnabled())
					log.debug("No topProjekt found");
				addError("error.notfound", msgRes.getMessage("topprojekt"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing topProjekt: " + topProjekt.getId());

			// Rechtepruefung *******************************************
			if (serviceFactory.createTopProjektAnyVoter().vote(secUser, topProjekt,
			    "ROLE_TOPPROJEKT_BEARBEITEN") != AccessDecisionVoter.ACCESS_GRANTED) {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			// Formular fuellen
			if (!hasErrors(request)) {
				topProjektForm.reset();

				topProjektForm.setTopProjektId(topProjekt.getId());
				if (topProjekt.getAnmelder() != null) {
					topProjektForm.setAnmelderId(topProjekt.getAnmelder().getId());
				}
				topProjektForm.setName(topProjekt.getName());
				topProjektForm.setBaukosten(FrontendHelper.castBigDecimalToString(
				    topProjekt.getBaukosten(), 3));
				topProjektForm.setSapProjektNummer(topProjekt.getSapProjektNummer());
				topProjektForm.setRegionalbereichId(topProjekt.getRegionalbereich().getId());
				topProjektForm.setDelete(topProjekt.isDeleted());
			}
			request.setAttribute("projekt", topProjekt);
		}
		// Anlegen ###########################################################
		else {
			// neues TOP-Projekt erstellen
			if (!hasErrors(request)) {
				topProjektForm.reset();
				topProjektForm.setTopProjektId(0);
				if (!secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE"))
					topProjektForm.setRegionalbereichId(getLoginUser(request).getRegionalbereich().getId());
			}
		}

		request.setAttribute("listRegionalbereiche", serviceFactory.createRegionalbereichService()
		    .findAll());

		// Anmelderauswahlliste abhaengig von den Berechtigungen fuellen
		List<Anmelder> anmelderList = new ArrayList<Anmelder>();
		if (secUser.hasAuthorization("ROLE_TOPPROJEKT_BEARBEITEN_ALLE")) {
			anmelderList = anmelderService.findForSelectList(sessionFahrplanjahr, null, null);
		} else {
			anmelderList = anmelderService.findForSelectList(sessionFahrplanjahr,
			    currentUser.getRegionalbereich(), null);
		}
		request.setAttribute("anmelderList", anmelderList);

		return mapping.findForward("SUCCESS");
	}
}