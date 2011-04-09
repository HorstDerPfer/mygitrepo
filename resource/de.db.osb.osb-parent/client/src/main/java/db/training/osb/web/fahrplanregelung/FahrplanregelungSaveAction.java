package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Fahrplanregelung.AufnahmeArt;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.FahrplanregelungService;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(FahrplanregelungSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungSaveAction.");

		TqmUser secUser = getSecUser();

		FahrplanregelungForm frForm = (FahrplanregelungForm) form;
		Integer frId = frForm.getFahrplanregelungId();
		Fahrplanregelung fr = null;

		// fahrplanregelungId pruefen ****************************************
		if (frId == null) {
			if (log.isDebugEnabled())
				log.debug("No fahrplanregelungId found");
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing fahrplanregelungId: " + frId);

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();

		// Fahrplanregelung bearbeiten
		if (frId != 0) {
			fr = frService.findById(frId, new Preload[] {});

			// fahrplanregelung pruefen ****************************************
			if (fr == null) {
				if (log.isDebugEnabled())
					log.debug("No fahrplanregelung found");
				addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing fahrplanregelung: " + fr.getId());

			// Rechtepruefung ***************************************************************
			if (EasyServiceFactory.getInstance().createFahrplanregelungAnyVoter().vote(secUser, fr,
			    "ROLE_FAHRPLANREGELUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				if (log.isDebugEnabled())
					log.debug("User '" + secUser.getUsername()
					    + "' hat keine Berechtigung fuer: ROLE_FAHRPLANREGELUNG_BEARBEITEN");
				addError("common.noAuth");
				return mapping.findForward("ACCESS_DENIED");
			}

		}
		// Fahrplanregelung anlegen
		else {
			// Rechtepruefung
			if (!secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_ANLEGEN_REGIONALBEREICH")) {
				if (log.isDebugEnabled())
					log.debug("ACCESS DENIED -> ROLE_FAHRPLANREGELUNG_ANLEGEN");
				addError("common.noAuth");
				return mapping.findForward("ACCESS_DENIED");
			}

			fr = new Fahrplanregelung();
			fr.setFahrplanjahr(frForm.getFahrplanjahr());
		}

		if (fr.isFixiert() && !frForm.isFixiert()) {
			fr.setFixiert(frForm.isFixiert());
			frService.update(fr);
		} else if (!fr.isFixiert()) {
			// Werte in Objekt schreiben
			fr.setFixiert(frForm.isFixiert());
			fr.setImportiert(frForm.isImportiert());
			fr.setName(frForm.getName());
			if (frForm.getRegionalbereichId() != null) {
				fr.setRegionalbereich(serviceFactory.createRegionalbereichService().findById(
				    frForm.getRegionalbereichId()));
			} else {
				fr.setRegionalbereich(null);
			}
			if (frForm.getAufnahmeNfp() != null) {
				fr.setAufnahmeNfp(AufnahmeArt.valueOf(frForm.getAufnahmeNfp()));
			}
			if (frForm.getAufnahmeNfpVorschlag() != null) {
				fr.setAufnahmeNfpVorschlag(AufnahmeArt.valueOf(frForm.getAufnahmeNfpVorschlag()));
			}
			fr.setBehandlungKS(frForm.getBehandlungKS());
			fr.setRelevanzBzu(frForm.isRelevanzBzu());
			fr.setNachtsperrpause(frForm.getNachtsperrpause());
			// Betriebsweise
			fr.setBetriebsweise(serviceFactory.createBetriebsweiseService().findById(
			    frForm.getBetriebsweiseId()));
			// Verkehrstage
			fr.setVerkehrstag_mo(frForm.isVerkehrstag_mo());
			fr.setVerkehrstag_di(frForm.isVerkehrstag_di());
			fr.setVerkehrstag_mi(frForm.isVerkehrstag_mi());
			fr.setVerkehrstag_do(frForm.isVerkehrstag_do());
			fr.setVerkehrstag_fr(frForm.isVerkehrstag_fr());
			fr.setVerkehrstag_sa(frForm.isVerkehrstag_sa());
			fr.setVerkehrstag_so(frForm.isVerkehrstag_so());
			// Datumswerte
			fr.setPlanStart(FrontendHelper.castStringToDate(frForm.getPlanStart()));
			fr.setPlanEnde(FrontendHelper.castStringToDate(frForm.getPlanEnde()));
			fr.setStart(FrontendHelper.castStringToDate(frForm.getStart()));
			fr.setEnde(FrontendHelper.castStringToDate(frForm.getEnde()));
			// Betriebsstellen
			BetriebsstelleService bsService = serviceFactory.createBetriebsstelleService();
			fr.setBetriebsstelleVon(bsService.findByKuerzelAndFahrplanjahr(bsService
			    .castCaptionToKuerzel(frForm.getBetriebsstelleVon()), sessionFahrplanjahr));
			fr.setBetriebsstelleBis(bsService.findByKuerzelAndFahrplanjahr(bsService
			    .castCaptionToKuerzel(frForm.getBetriebsstelleBis()), sessionFahrplanjahr));
			// LastChange
			fr.setLastChange(null);

			// Fahrplanjahr pruefen ********************************
			if (!sessionFahrplanjahr.equals(fr.getFahrplanjahr())) {
				addError("error.fahrplanjahr.wrong");
				return mapping.findForward("FAILURE");
			}

			// Fahrplanregelung speichern
			// bearbeiten
			if (frId != 0) {
				frService.update(fr);
			}
			// anlegen
			else {
				if (fr.getRegionalbereich() != null && fr.getFahrplanjahr() != null) {
					// Neue LfdNr wird in create-Methode generiert
					frService.create(fr);
					// ID zum Laden in EditAction setzen
					if (log.isDebugEnabled())
						log.debug("Set fahrplanregelungId to request: " + fr.getId());
					request.setAttribute("fahrplanregelungId", fr.getId());
				} else {
					addError("error.common");
					return mapping.findForward("FAILURE");
				}
			}

			addMessage("success.saved");
		}

		return mapping.findForward("SUCCESS");
	}
}