package db.training.osb.web.buendel;

import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.util.CollectionHelper;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.BuendelService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.osb.service.VzgStreckeService;
import db.training.security.hibernate.TqmUser;

public class BuendelSaveAction extends BaseAction {

	private static Logger log = Logger.getLogger(BuendelSaveAction.class);

	private BuendelService buendelService;

	private SAPMassnahmeService massnahmeService;

	public BuendelSaveAction() {
		EasyServiceFactory serviceFactory = EasyServiceFactory.getInstance();
		buendelService = serviceFactory.createBuendelService();
		massnahmeService = serviceFactory.createSAPMassnahmeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BuendelSaveAction.");

		if (!isTokenValid(request, true)) {
			addError("error.duplicateFormSubmission");
			return mapping.getInputForward();
		}
		
		TqmUser secUser = getSecUser();
		User loginUser = getLoginUser(request);

		BuendelForm buendelForm = (BuendelForm) form;
		Buendel buendel = null;
		Integer buendelId = buendelForm.getBuendelId();

		// BuendelId pruefen ****************************************
		if (buendelId == null) {
			if (log.isDebugEnabled())
				log.debug("No buendelId found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendelId: " + buendelId);

		// Buendel bearbeiten ********************************************
		if (buendelId != 0) {
			if (log.isDebugEnabled())
				log.debug("BuendelID=" + buendelId + ". Update Buendel.");

			buendel = buendelService.findById(buendelId, new Preload[] {
			        new Preload(Buendel.class, "weitereStrecken"),
			        new Preload(Buendel.class, "gleissperrungen"),
			        new Preload(Regelung.class, "vzgStrecke"),
			        new Preload(Regelung.class, "massnahme") });

			// BuendelId pruefen ****************************************
			if (buendel == null) {
				if (log.isDebugEnabled())
					log.debug("No buendel found");
				addError("error.notfound", msgRes.getMessage("buendel"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing buendel: " + buendelId);

			// Rechtepruefung ***************************************************************
			if (EasyServiceFactory.getInstance().createBuendelAnyVoter().vote(secUser, buendel,
			    "ROLE_BUENDEL_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			if (!buendel.isFixiert()) {
				setBuendel(buendelForm, buendel);
			} else {
				if (!buendelForm.getFixiert()) {
					addMessage("buendel.message.aenderungenErlaubt");
				} else {
					if (log.isDebugEnabled())
						log.debug("Buendel fixiert, keine Aenderungen erlaubt.");
					addError("buendel.message.keineAenderungenErlaubt");
					return mapping.findForward("FAILURE");
				}
			}

			// Fixierung
			if (buendelForm.getFixiert() == true) {
				buendel.setFixiert(true);
				buendel.setFixierungsDatum(new Date());
				// Massnahmen und Gleissperrungen muessen mit fixiert werden
				for (Gleissperrung gs : buendel.getGleissperrungen()) {
					if (gs.getMassnahme() != null) {
						gs.getMassnahme().setGenehmiger(loginUser);
						gs.getMassnahme().setGenehmigerRegionalbereich(
						    loginUser.getRegionalbereich());
						gs.getMassnahme().setGenehmigungsDatum(new Date());
						massnahmeService.update(gs.getMassnahme());
					}
				}
			} else {
				buendel.setFixiert(false);
				buendel.setFixierungsDatum(null);
				// Fixierung von Massnahmen und Gleissperrungen
				for (Gleissperrung gs : buendel.getGleissperrungen()) {
					if (gs.getMassnahme() != null) {
						gs.getMassnahme().setGenehmiger(null);
						gs.getMassnahme().setGenehmigerRegionalbereich(null);
						gs.getMassnahme().setGenehmigungsDatum(null);
						massnahmeService.update(gs.getMassnahme());
					}
				}
			}

			// Buendel speichern ******************************
			buendel.refreshWeitereStrecken();
			buendel.setLastChange(null);
			buendelService.update(buendel);
		}
		// Buendel anlegen ************************************************************
		else {
			if (log.isDebugEnabled())
				log.debug("BuendelID=0. Create new Buendel.");

			if (!FrontendHelper.stringNotNullOrEmpty(buendelForm.getGleissperrungIds())) {
				if (log.isWarnEnabled())
					log.warn("Keine Gleissperrungen im Formular gesetzt.");
				addError("error.notfound", msgRes.getMessage("gleissperrungen"));
				return mapping.findForward("FAILURE");
			}

			// Bündel neu anlegen
			buendel = new Buendel();

			setBuendel(buendelForm, buendel);

			// Rechtepruefung ****************************************************************
			if (EasyServiceFactory.getInstance().createBuendelAnyVoter().vote(secUser, buendel,
			    "ROLE_BUENDEL_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			// Fixierung setzen ***********************
			if (buendelForm.getFixiert() == true) {
				buendel.setFixiert(true);
				buendel.setFixierungsDatum(new Date());
			} else {
				buendel.setFixiert(false);
				buendel.setFixierungsDatum(null);
			}

			// Gleissperrungen verknüpfen
			GleissperrungService gleissperrungService = EasyServiceFactory.getInstance()
			    .createGleissperrungService();

			if (log.isDebugEnabled()) {
				log.debug("Gleissperrungen: " + buendelForm.getGleissperrungIds());
			}
			for (String strId : buendelForm.getGleissperrungIds().split((","))) {
				Integer mnId = FrontendHelper.castStringToInteger(strId);
				if (mnId != null) {
					Gleissperrung gl = gleissperrungService.findById(mnId,
					    new Preload[] { new Preload(Gleissperrung.class, "buendel") });
					gl.getBuendel().add(buendel);
					if (buendel.getGleissperrungen() == null) {
						buendel.setGleissperrungen(new HashSet<Gleissperrung>());
					}
					buendel.getGleissperrungen().add(gl);
				}
			}
			// Neue LfdNr generieren wird in create-Methode generiert
			// Buendel erzeugen ******************************
			buendelService.create(buendel);

			// Buendel neu laden um Sessionfehler zu umgehen zur Aktualisierung der weiteren
			// Strecken
			buendel = buendelService.findById(buendel.getId(), new Preload[] {
			        new Preload(Buendel.class, "weitereStrecken"),
			        new Preload(Buendel.class, "gleissperrungen"),
			        new Preload(Gleissperrung.class, "vzgStrecke") });
			buendel.refreshWeitereStrecken();
			buendel.setLastChange(null);
			buendelService.update(buendel);

			request.setAttribute("buendelId", buendel.getId());
			if (log.isDebugEnabled())
				log.debug("buendelId: " + buendel.getId());
		}

		addMessage("success.saved");
		// Wenn Buendel fixiert wurde, wird auf Liste zurueck gesprungen
		if (buendel.isFixiert())
			return mapping.findForward("FAILURE");
		return mapping.findForward("SUCCESS");
	}

	private void setBuendel(BuendelForm form, Buendel buendel) {
		buendel.setFahrplanjahr(form.getFahrplanjahr());
		setBuendelAllgemein(form, buendel);
		setBuendelStrecke(form, buendel);
	}

	private void setBuendelAllgemein(BuendelForm form, Buendel buendel) {
		buendel.setBuendelName(form.getBuendelName());
		if (form.getRegionalbereichId() != null) {
			buendel.setRegionalbereich(serviceFactory.createRegionalbereichService().findById(
			    form.getRegionalbereichId()));
		}
		if (form.getAnkermassnahmeArtId() != null) {
			buendel.setAnkermassnahmeArt(serviceFactory.createAnkermassnahmeArtService().findById(
			    form.getAnkermassnahmeArtId()));
		}
		// Fixierung wird ausserhalb festgelegt
		// Fahrplanjahr wird nur beim Anlegen geschrieben, danach nicht mehr veraendert
		buendel.setDurchfuehrungsZeitraumStartKoordiniert(FrontendHelper.castStringToDate(form
		    .getDurchfuehrungsZeitraumStartKoordiniert()));
		buendel.setDurchfuehrungsZeitraumEndeKoordiniert(FrontendHelper.castStringToDate(form
		    .getDurchfuehrungsZeitraumEndeKoordiniert()));
		buendel.setEiuVkS(FrontendHelper.castStringToInteger(form.getEiuVks()));
		buendel.setBaukostenVorBuendelung(FrontendHelper.castStringToDouble(form
		    .getBaukostenVorBuendelung()));
		buendel.setBaukostenNachBuendelung(FrontendHelper.castStringToDouble(form
		    .getBaukostenNachBuendelung()));
		buendel.setBaukostenErsparnis(FrontendHelper.castStringToDouble(form
		    .getBaukostenErsparnis()));
		buendel.setSperrzeitbedarfBuendel(FrontendHelper.castStringToInteger(form
		    .getSperrzeitbedarfBuendel()));
		buendel.setSperrzeitErsparnis(FrontendHelper.castStringToInteger(form
		    .getSperrzeitErsparnis()));
	}

	private void setBuendelStrecke(BuendelForm form, Buendel buendel) {
		// Hauptstrecke
		VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(form.getHauptStrecke());
		if (vzgStreckeNummer != null) {
			VzgStrecke vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(
			    vzgStreckeNummer, form.getFahrplanjahr(), null, true, null));
			buendel.setHauptStrecke(vzgStrecke);
		}
		// Betriebsstellen
		BetriebsstelleService bsService = serviceFactory.createBetriebsstelleService();
		if (form.getStartBahnhofId() != null) {
			buendel.setStartBahnhof(bsService.findById(form.getStartBahnhofId()));
		}
		if (form.getEndeBahnhofId() != null) {
			buendel.setEndeBahnhof(bsService.findById(form.getEndeBahnhofId()));
		}
	}

}
