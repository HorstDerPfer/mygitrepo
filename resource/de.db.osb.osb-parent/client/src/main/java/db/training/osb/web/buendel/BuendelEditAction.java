package db.training.osb.web.buendel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.BuendelService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.web.gleissperrung.AddGleissperrungenToBuendelAction;
import db.training.security.hibernate.TqmUser;

public class BuendelEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(BuendelEditAction.class);

	@SuppressWarnings("unchecked")
	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BuendelEditAction.");

		TqmUser secUser = getSecUser();
		User loginUser = getLoginUser(request);

		BuendelForm buendelForm = (BuendelForm) form;
		Buendel buendel = null;
		Integer buendelId = null;

		// BuendelId setzen *******************************************************************
		if (request.getSession().getAttribute(
		    AddGleissperrungenToBuendelAction.ADD_GLEISSPERRUNG_TO_BUENDEL_BUENDELID) != null) {
			buendelId = (Integer) request.getSession().getAttribute(
			    AddGleissperrungenToBuendelAction.ADD_GLEISSPERRUNG_TO_BUENDEL_BUENDELID);
			request.getSession().removeAttribute(
			    AddGleissperrungenToBuendelAction.ADD_GLEISSPERRUNG_TO_BUENDEL_BUENDELID);
		} else if (request.getAttribute("buendelId") != null) {
			buendelId = (Integer) request.getAttribute("buendelId");
		} else {
			buendelId = buendelForm.getBuendelId();
		}

		// BuendelId pruefen ****************************************
		if (buendelId == null) {
			if (log.isDebugEnabled())
				log.debug("No buendelId found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendelId: " + buendelId);

		// Uebergebene Gleissperrungen initialisieren *********************
		List<Gleissperrung> gleissperrungen = (List<Gleissperrung>) request
		    .getAttribute("gleissperrungen");
		if (gleissperrungen == null && buendelForm.getGleissperrungList() != null
		    && buendelForm.getGleissperrungList().size() > 0)
			gleissperrungen = buendelForm.getGleissperrungList();

		VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
		Integer vzgStreckeNummer = null;

		// Vorhandenes Buendel bearbeiten #######################################################
		if (buendelId != 0) {
			BuendelService buendelService = serviceFactory.createBuendelService();
			buendel = buendelService.findById(buendelId, new Preload[] {
			        new Preload(Buendel.class, "weitereStrecken"),
			        new Preload(Buendel.class, "gleissperrungen"),
			        new Preload(Buendel.class, "fahrplanregelungen"),
			        new Preload(Fahrplanregelung.class, "buendel"),
			        new Preload(Fahrplanregelung.class, "betriebsstelleVon"),
			        new Preload(Fahrplanregelung.class, "betriebsstelleBis"),
			        new Preload(Gleissperrung.class, "buendel"),
			        new Preload(Regelung.class, "massnahme"),
			        new Preload(SAPMassnahme.class, "gleissperrungen"),
			        new Preload(Regelung.class, "vzgStrecke"),
			        new Preload(Regelung.class, "bstVon"), new Preload(Regelung.class, "bstBis"),
			        new Preload(VzgStrecke.class, "betriebsstellen"),
			        new Preload(Gleissperrung.class, "vtr") });

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
			if (EasyServiceFactory.getInstance().createBuendelAnyVoter()
			    .vote(secUser, buendel, "ROLE_BUENDEL_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				if (log.isDebugEnabled()) {
					log.debug("User '" + secUser.getUsername()
					    + "' hat keine Berechtigung fuer: ROLE_BUENDEL_BEARBEITEN");
				}
				if (EasyServiceFactory.getInstance().createBuendelAnyVoter()
				    .vote(secUser, buendel, "ROLE_BUENDEL_LESEN") == AccessDecisionVoter.ACCESS_GRANTED) {
					if (log.isDebugEnabled()) {
						log.debug("User '" + secUser.getUsername()
						    + "' hat Berechtigung fuer: ROLE_BUENDEL_LESEN");
					}
					return mapping.findForward("VIEW");
				}
				if (log.isDebugEnabled()) {
					log.debug("User '" + secUser.getUsername()
					    + "' hat keine Berechtigung fuer: ROLE_BUENDEL_BEARBEITEN");
				}
				addError("common.noAuth");
				return mapping.findForward("ACCESS_DENIED");
			}

			if (!hasErrors(request)) {
				initForm(buendelForm, buendel);
				buendelForm.setFahrplanjahr(buendel.getFahrplanjahr());

				if (buendel.getHauptStrecke() != null) {
					buendelForm.setHauptStrecke(buendel.getHauptStrecke().getCaption());
					vzgStreckeNummer = buendel.getHauptStrecke().getNummer();
				}
			}

			/* variable zur Steuerung von Buttons in mehrfach verwendeten Listen */
			request.setAttribute("action", "edit");
		}
		// Neues Buendel anlegen ##################################################################
		else {
			buendel = new Buendel();

			if (!hasErrors(request)) {
				buendelForm.reset();
				buendelForm.setBuendelId(buendelId);

				// Setzen des Regionalbereichs
				// Regionale Bearbeiter koennen nur den eigenen Regionalbereich bearbeiten
				if (!secUser.hasAuthorization("ROLE_BUENDEL_ANLEGEN_ALLE"))
					buendel.setRegionalbereich(loginUser.getRegionalbereich());
				if (buendel.getRegionalbereich() != null)
					buendelForm.setRegionalbereichId(buendel.getRegionalbereich().getId());
			}

			// Fahrplanjahr setzen, ist nicht aenderbar
			buendel.setFahrplanjahr(sessionFahrplanjahr);
			buendelForm.setFahrplanjahr(buendel.getFahrplanjahr());

			if (gleissperrungen != null) {
				if (log.isDebugEnabled())
					log.debug("Anz. Gleissperrungen: " + gleissperrungen.size());
				buendelForm.setGleissperrungList(gleissperrungen);
				// Setzen der zugewiesenen Gleissperrungen
				buendel.setGleissperrungen(new HashSet<Gleissperrung>(gleissperrungen));

				if (!hasErrors(request)) {
					/*
					 * Stimmt die Streckennummer bei allen Maßnahmen überein, wird sie als Vorschlag
					 * für das Bündel genommen. Stimmt die Streckennummer nicht überein, bleibt das
					 * Feld leer.
					 */
					VzgStrecke vzgStrecke = null;
					for (Gleissperrung gl : gleissperrungen) {
						if (gl.getVzgStrecke() != null) {
							if (vzgStrecke == null) {
								vzgStrecke = gl.getVzgStrecke();
							}
							if (vzgStrecke.getNummer() != gl.getVzgStrecke().getNummer()) {
								vzgStrecke = null;
								addMessage("buendel.message.streckennummer");
								break;
							}
						} else {
							vzgStrecke = null;
							break;
						}
					}

					// VzgStrecke neu laden um Betriebstellen zu fetchen
					if (vzgStrecke != null) {
						vzgStrecke = vzgService.findById(vzgStrecke.getId(),
						    new Preload[] { new Preload(VzgStrecke.class, "betriebsstellen") });
						vzgStreckeNummer = vzgStrecke.getNummer();
					}

					// Setzen der Strecke
					buendel.setHauptStrecke(vzgStrecke);
					if (buendel.getHauptStrecke() != null)
						buendelForm.setHauptStrecke(buendel.getHauptStrecke().getCaption());
				}

				// Rechtepruefung
				if (EasyServiceFactory.getInstance().createBuendelAnyVoter()
				    .vote(secUser, buendel, "ROLE_BUENDEL_ANLEGEN") == AccessDecisionVoter.ACCESS_DENIED) {
					addError("common.noAuth");
					return mapping.findForward("ACCESS_DENIED");
				}
			} else {
				if (!secUser.hasAuthorization("ROLE_BUENDEL_ANLEGEN_ALLE")
				    && !secUser.hasAuthorization("ROLE_BUENDEL_ANLEGEN_REGIONALBEREICH")) {
					addError("common.noAuth");
					return mapping.findForward("ACCESS_DENIED");
				}
			}
			/* variable zur Steuerung von Buttons in mehrfach verwendeten Listen */
			request.setAttribute("action", "create");
		}

		// Fahrplanjahr pruefen *************************************
		if (!sessionFahrplanjahr.equals(buendelForm.getFahrplanjahr())) {
			addError("error.fahrplanjahr.wrong");
			return mapping.findForward("FAILURE");
		}

		// Gleissperrung-IDs in Form schreiben ************************
		String gleissperrungIds = null;
		if (buendel.getGleissperrungen() != null) {
			for (Gleissperrung gl : buendel.getGleissperrungen()) {
				if (gleissperrungIds != null) {
					gleissperrungIds = gleissperrungIds.concat(",");
					gleissperrungIds = gleissperrungIds.concat(gl.getId().toString());
				} else {
					gleissperrungIds = gl.getId().toString();
				}
			}
		}
		buendelForm.setGleissperrungIds(gleissperrungIds);

		// VzgStrecke zur Fuellung der Betriebsstellen setzen
		if (hasErrors(request)) {
			vzgStreckeNummer = vzgService.castCaptionToNummer(buendelForm.getHauptStrecke());
		}

		// Streckenabhaengige Betriebsstellen
		List<Betriebsstelle> bsts = new ArrayList<Betriebsstelle>();
		if (vzgStreckeNummer != null) {
			BetriebsstelleService bsService = EasyServiceFactory.getInstance()
			    .createBetriebsstelleService();
			bsts = bsService.findByVzgStreckeNummerAndFahrplanjahr(vzgStreckeNummer,
			    sessionFahrplanjahr, true, null);
		}
		request.setAttribute("betriebsstellen", bsts);

		request.setAttribute("buendel", buendel);
		request.setAttribute("regionalbereichListe", serviceFactory.createRegionalbereichService()
		    .findAll());
		request.setAttribute("ankermassnahmeArten", serviceFactory.createAnkermassnahmeArtService()
		    .findAll());

		// Abzweigende VzgStrecken fuer Auswahlliste *********************************************
		List<VzgStrecke> vzgStreckenAbzw = new ArrayList<VzgStrecke>();
		if (buendel.getHauptStrecke() != null) {
			// Regionalbereich definieren
			Regionalbereich rb = loginUser.getRegionalbereich();
			// Benutzer mit dem Recht ALLE bekommen keine Regionseinschraenkung
			if (secUser.hasAuthorization("ROLE_BUENDEL_ANLEGEN_ALLE")
			    || secUser.hasAuthorization("ROLE_BUENDEL_BEARBEITEN_ALLE")) {
				rb = null;
			}
			// Die Hauptstrecke muss auf jeden Fall in der Liste stehen unabhaengig von der Region
			// Hauptstrecke wird zuerst eingefuegt um an erster Stelle zustehen
			vzgStreckenAbzw.add(buendel.getHauptStrecke());
			Set<VzgStrecke> vzgStrecken = vzgService.findAbzweigendeStrecken(
			    buendel.getHauptStrecke(), buendel.getFahrplanjahr(), rb,
			    new Preload[] { new Preload(VzgStrecke.class, "betriebsstellen") });
			if (vzgStrecken.contains(buendel.getHauptStrecke())) {
				vzgStrecken.remove(buendel.getHauptStrecke());
			}
			vzgStreckenAbzw.addAll(vzgStrecken);
		}
		request.setAttribute("vzgStrecken", vzgStreckenAbzw);

		// Leere Liste VzgStrecken, wird spaeter mit Ajax befuellt*****
		List<VzgStrecke> vzgStreckenAlle = new ArrayList<VzgStrecke>();
		request.setAttribute("vzgStreckenAlle", vzgStreckenAlle);

		saveToken(request);
		return mapping.findForward("SUCCESS");
	}

	private void initForm(BuendelForm form, Buendel buendel) {
		form.reset();
		initFormAllgemein(form, buendel);
		initFormStrecke(form, buendel);
	}

	private void initFormAllgemein(BuendelForm form, Buendel buendel) {
		form.setBuendelId(buendel.getId());
		form.setBuendelName(buendel.getBuendelName());
		if (buendel.getRegionalbereich() != null) {
			form.setRegionalbereichId(buendel.getRegionalbereich().getId());
		}
		if (buendel.getAnkermassnahmeArt() != null) {
			form.setAnkermassnahmeArtId(buendel.getAnkermassnahmeArt().getId());
		}
		form.setFixiert(buendel.isFixiert());
		form.setDurchfuehrungsZeitraumStartKoordiniert(FrontendHelper.castDateToString(buendel
		    .getDurchfuehrungsZeitraumStartKoordiniert()));
		form.setDurchfuehrungsZeitraumEndeKoordiniert(FrontendHelper.castDateToString(buendel
		    .getDurchfuehrungsZeitraumEndeKoordiniert()));
		form.setEiuVks(FrontendHelper.castIntegerToString(buendel.getEiuVkS()));
		form.setBaukostenVorBuendelung(FrontendHelper.castDoubleToString(buendel
		    .getBaukostenVorBuendelung()));
		form.setBaukostenNachBuendelung(FrontendHelper.castDoubleToString(buendel
		    .getBaukostenNachBuendelung()));
		form.setBaukostenErsparnis(FrontendHelper.castDoubleToString(buendel
		    .getBaukostenErsparnis()));
		form.setSperrzeitbedarfBuendel(FrontendHelper.castIntegerToString(buendel
		    .getSperrzeitbedarfBuendel()));
		form.setSperrzeitErsparnis(FrontendHelper.castIntegerToString(buendel
		    .getSperrzeitErsparnis()));
	}

	private void initFormStrecke(BuendelForm form, Buendel buendel) {
		// Hauptstrecke
		if (buendel.getHauptStrecke() != null) {
			form.setHauptStrecke(buendel.getHauptStrecke().getCaption());
		}
		// Betriebsstellen
		Betriebsstelle startBahnhof = buendel.getStartBahnhof();
		Betriebsstelle endeBahnhof = buendel.getEndeBahnhof();
		BetriebsstelleService bsService = serviceFactory.createBetriebsstelleService();
		List<Betriebsstelle> betriebsstellen = bsService.findByVzgStreckeNummerAndFahrplanjahr(
		    buendel.getHauptStrecke().getNummer(), sessionFahrplanjahr, true, null);
		// Startbahnhof
		if (startBahnhof != null) {
			form.setStartBahnhofId(startBahnhof.getId());
		} else {
			// versuche StartBf zu ermitteln
			startBahnhof = bsService.findStartInList(buendel.getHauptStrecke(), betriebsstellen,
			    buendel.getGleissperrungen());
			if (startBahnhof != null) {
				form.setStartBahnhofId(startBahnhof.getId());
			}
		}
		// Endbahnhof
		if (endeBahnhof != null) {
			form.setEndeBahnhofId(endeBahnhof.getId());
		} else {
			// versuche EndeBf zu ermitteln
			endeBahnhof = bsService.findEndInList(buendel.getHauptStrecke(), betriebsstellen,
			    buendel.getGleissperrungen());
			if (endeBahnhof != null) {
				form.setEndeBahnhofId(endeBahnhof.getId());
			}
		}
	}

}
