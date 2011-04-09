package db.training.osb.web.umleitung;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

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
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.osb.model.Umleitungsweg;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.UmleitungService;
import db.training.osb.service.UmleitungswegService;
import db.training.osb.service.VzgStreckeService;
import db.training.security.hibernate.TqmUser;

public class UmleitungSaveAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungSaveAction.");

		TqmUser secUser = getSecUser();

		UmleitungForm umleitungForm = (UmleitungForm) form;

		if (umleitungForm.getSaveFlag() == null || umleitungForm.getSaveFlag().intValue() == 0) {
			UmleitungService umlService = serviceFactory.createUmleitungService();
			Integer umleitungId = umleitungForm.getUmleitungId();
			Umleitung umleitung = null;
			if (umleitungId.intValue() == 0) {
				if (!secUser.hasAuthorization("ROLE_UMLEITUNG_ANLEGEN_ALLE")
				    && !secUser.hasAuthorization("ROLE_UMLEITUNG_ANLEGEN_REGIONALBEREICH")) {
					addError("common.noAuth");
					return mapping.findForward("FAILURE");
				}
				umleitung = new Umleitung();
			} else {
				umleitung = umlService.findById(umleitungForm.getUmleitungId(), new Preload[] {
				        new Preload(Umleitung.class, "umleitungFahrplanregelungLinks"),
				        new Preload(UmleitungFahrplanregelungLink.class, "fahrplanregelung"),
				        new Preload(Umleitung.class, "umleitungswege") });

				if (EasyServiceFactory.getInstance().createUmleitungAnyVoter().vote(secUser,
				    umleitung, "ROLE_UMLEITUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
					addError("common.noAuth");
					return mapping.findForward("FAILURE");
				}
			}

			VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();

			Set<Integer> strecken = new TreeSet<Integer>();
			Integer streckeId = VzgStrecke.getId(umleitungForm.getVzgStrecke());

			VzgStrecke strecke = vzgService.findById(streckeId);
			if (strecke != null)
				strecken.add(strecke.getNummer());

			BetriebsstelleService bsService = serviceFactory.createBetriebsstelleService();
			Betriebsstelle betriebsstelleVon = bsService.findByKuerzelAndStreckenAndFahrplanjahr(
			    bsService.castCaptionToKuerzel(umleitungForm.getBetriebsstelleVon()), strecken,
			    sessionFahrplanjahr);
			Betriebsstelle betriebsstelleBis = bsService.findByKuerzelAndStreckenAndFahrplanjahr(
			    bsService.castCaptionToKuerzel(umleitungForm.getBetriebsstelleBis()), strecken,
			    sessionFahrplanjahr);

			boolean error = false;
			if (betriebsstelleVon == null
			    || !bsService.findByKeyword(betriebsstelleVon.getKuerzel(), strecke.getNummer(),
			        sessionFahrplanjahr).contains(betriebsstelleVon)) {
				addError("error.betriebsstelleVon.notOnTrack");
				error = true;
			}
			if (betriebsstelleBis == null
			    || !bsService.findByKeyword(betriebsstelleBis.getKuerzel(), strecke.getNummer(),
			        sessionFahrplanjahr).contains(betriebsstelleBis)) {
				addError("error.betriebsstelleBis.notOnTrack");
				error = true;
			}

			if (umleitungForm.getUmleitungName() == null
			    || umleitungForm.getUmleitungName().length() == 0) {
				addError("error.umleitung.noTitle");
				error = true;
			}
			if (error)
				return mapping.findForward("FAILURE");

			umleitung.setName(umleitungForm.getUmleitungName());
			umleitung.setFreieKapaRichtung(FrontendHelper.castStringToDouble(umleitungForm
			    .getFreieKapaRichtung()));
			umleitung.setFreieKapaGegenrichtung(FrontendHelper.castStringToDouble(umleitungForm
			    .getFreieKapaGegenrichtung()));

			UmleitungswegService umWegService = serviceFactory.createUmleitungswegService();
			Umleitungsweg umWeg = umWegService.findByBetriebsstellenAndStrecke(betriebsstelleVon,
			    betriebsstelleBis, strecke);

			if (!secUser.hasAuthorization("ROLE_UMLEITUNGSWEG_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_UMLEITUNG_ANLEGEN_REGIONALBEREICH")) {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			if (umWeg == null) {
				umWeg = new Umleitungsweg();
				umWeg.setLastChange(null);
				umWeg.setBetriebsstelleVon(betriebsstelleVon);
				umWeg.setBetriebsstelleBis(betriebsstelleBis);
				umWeg.setVzgStrecke(strecke);
			}

			umleitung.getUmleitungswege().add(umWeg);

			// Berechnung und Setzen der Gueltigkeit anhand der beteiligten BS
			Date gueltigVon = null;
			for (Umleitungsweg umleitungsweg : umleitung.getUmleitungswege()) {
				if (gueltigVon == null
				    || gueltigVon.before(umleitungsweg.getBetriebsstelleVon().getGueltigVon()))
					gueltigVon = umleitungsweg.getBetriebsstelleVon().getGueltigVon();
				if (gueltigVon.before(umleitungsweg.getBetriebsstelleBis().getGueltigVon()))
					gueltigVon = umleitungsweg.getBetriebsstelleBis().getGueltigVon();
			}
			umleitung.setGueltigVon(gueltigVon);

			Date gueltigBis = null;
			for (Umleitungsweg umleitungsweg : umleitung.getUmleitungswege()) {
				if (gueltigBis == null
				    || gueltigBis.after(umleitungsweg.getBetriebsstelleVon().getGueltigBis()))
					gueltigBis = umleitungsweg.getBetriebsstelleVon().getGueltigBis();
				if (gueltigBis.after(umleitungsweg.getBetriebsstelleBis().getGueltigBis()))
					gueltigBis = umleitungsweg.getBetriebsstelleBis().getGueltigBis();
			}
			umleitung.setGueltigBis(gueltigBis);

			// Reset der Relation, wenn Wegstueck hinzugefuegt wird (Neuberechnung wird erzwungen)
			umleitung.setRelation(null);

			if (umleitungId.intValue() == 0)
				umlService.create(umleitung);
			else
				umlService.update(umleitung);

			umleitungForm.setUmleitungId(umleitung.getId());

			if (umleitung.getRelation() == null)
				addMessage("info.umleitung.incomplete");
			else
				addMessage("info.umleitung.complete");

			request.setAttribute("umleitungId", umleitung.getId());
		} else if (umleitungForm.getSaveFlag() != null
		    && umleitungForm.getSaveFlag().intValue() == 1) {
			if (umleitungForm.getUmleitungId() != 0) {

				UmleitungService umlService = serviceFactory.createUmleitungService();

				Umleitung umleitung = umlService.findById(umleitungForm.getUmleitungId(),
				    new Preload[] { new Preload(Umleitung.class, "umleitungFahrplanregelungLinks"),
				            new Preload(UmleitungFahrplanregelungLink.class, "fahrplanregelung"),
				            new Preload(Umleitung.class, "umleitungswege") });

				umleitung.setFreieKapaRichtung(FrontendHelper.castStringToDouble(umleitungForm
				    .getFreieKapaRichtung()));
				umleitung.setFreieKapaGegenrichtung(FrontendHelper.castStringToDouble(umleitungForm
				    .getFreieKapaGegenrichtung()));

				if (EasyServiceFactory.getInstance().createUmleitungAnyVoter().vote(secUser,
				    umleitung, "ROLE_UMLEITUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
					addError("common.noAuth");
					return mapping.findForward("FAILURE");
				}

				umlService.update(umleitung);

				umleitungForm.setUmleitungId(umleitung.getId());

				if (umleitung.getRelationString() == null)
					addMessage("info.umleitung.incomplete");
				else
					addMessage("info.umleitung.complete");

				request.setAttribute("umleitungId", umleitung.getId());
			}

		}
		return mapping.findForward("SUCCESS");
	}
}