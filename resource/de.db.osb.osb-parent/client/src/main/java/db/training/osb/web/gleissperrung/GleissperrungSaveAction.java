package db.training.osb.web.gleissperrung;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.util.CollectionHelper;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.enums.SperrpausenbedarfArt;
import db.training.osb.model.enums.SperrungAuswirkung;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.VerkehrstageregelungService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungSaveAction.");

		TqmUser secUser = getSecUser();

		Gleissperrung gleissperrung = null;
		GleissperrungForm gsForm = (GleissperrungForm) form;
		Integer gleissperrungId = gsForm.getGleissperrungId();

		GleissperrungService gleissperrungService = serviceFactory.createGleissperrungService();

		// Fahrplanjahr
		Integer fahrplanjahr = FrontendHelper.castStringToInteger(gsForm.getFahrplanjahr());
		if (fahrplanjahr == null) {
			addError("error.fahrplanjahr.invalid");
			return mapping.findForward("FAILURE");
		}

		if (gleissperrungId != null) {
			if (gleissperrungId == 0) {
				if (log.isDebugEnabled())
					log.debug("Create new Gleissperrung: " + gleissperrungId);
				gleissperrung = new Gleissperrung();
			} else {
				if (log.isDebugEnabled())
					log.debug("Find Gleissperrung by Id: " + gleissperrungId);
				gleissperrung = gleissperrungService.findById(gleissperrungId,
				    new Preload[] { new Preload(Gleissperrung.class, "massnahme") });
			}
		}

		if (gleissperrung == null) {
			if (log.isDebugEnabled())
				log.debug("Gleissperrung not found: " + gleissperrungId);
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}

		EasyAccessDecisionVoter voter = serviceFactory.createRegelungAnyVoter();
		VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
		BetriebsstelleService bstService = serviceFactory.createBetriebsstelleService();

		// Formulardaten verarbeiten
		// Fahrplanjahr
		gleissperrung.setFahrplanjahr(fahrplanjahr);
		gleissperrung.setRichtungsKennzahl(gsForm.getRichtungsKennzahl());

		// VZG-Strecke pruefen
		VzgStrecke vzgStrecke = null;
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(gsForm.getVzgStrecke());
		if (vzgStreckeNummer != null) {
			vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
			    fahrplanjahr, null, true, new Preload[] { new Preload(VzgStrecke.class,
			        "betriebsstellen") }));
			// VZG-Strecke nicht gefunden
			if (vzgStrecke == null) {
				addError("error.required", msgRes.getMessage("regelung.vzgStrecke"));
			}
		}
		gleissperrung.setVzgStrecke(vzgStrecke);

		if (vzgStrecke != null) {
			// für die Validierung der Betriebsstellen wird jede Bst. mit der
			// Betriebsstellen-Collection
			// der Hauptstrecke verglichen.
			Set<Betriebsstelle> betriebsstellen = new HashSet<Betriebsstelle>();
			for (BetriebsstelleVzgStreckeLink link : vzgStrecke.getBetriebsstellen()) {
				betriebsstellen.add(link.getBetriebsstelle());
			}

			// Betriebsstelle VON
			Betriebsstelle bstVon = bstService.findById(gsForm.getBstVonId());
			if (bstVon != null) {
				if (!betriebsstellen.contains(bstVon)) {
					addError("error.invalid", msgRes.getMessage("regelung.bstVon"));
				} else {
					gleissperrung.setBstVon(bstVon);
				}
			}

			// Betriebsstelle BIS
			Betriebsstelle bstBis = bstService.findById(gsForm.getBstBisId());
			if (bstBis != null) {
				if (!betriebsstellen.contains(bstBis)) {
					addError("error.invalid", msgRes.getMessage("regelung.bstBis"));
				} else {
					gleissperrung.setBstBis(bstBis);
				}
			}

			// Betriebsstelle VON_KO
			Betriebsstelle bstVonKo = bstService.findById(gsForm.getBstVonKoordiniertId());
			if (bstVonKo != null) {
				if (!betriebsstellen.contains(bstVonKo)) {
					addError("error.invalid", msgRes.getMessage("regelung.bstVonKoordiniert"));
				} else {
					gleissperrung.setBstVonKoordiniert(bstVonKo);
				}
			}

			// Betriebsstelle BIS_KO
			Betriebsstelle bstBisKo = bstService.findById(gsForm.getBstBisKoordiniertId());
			if (bstBisKo != null) {
				if (!betriebsstellen.contains(bstBisKo)) {
					addError("error.invalid", msgRes.getMessage("regelung.bstBisKoordiniert"));
				} else {
					gleissperrung.setBstBisKoordiniert(bstBisKo);
				}
			}
		}

		gleissperrung.setKmVon(FrontendHelper.castStringToFloat(gsForm.getKmVon()));
		gleissperrung.setKmBis(FrontendHelper.castStringToFloat(gsForm.getKmBis()));
		gleissperrung.setSigWeicheVon(FrontendHelper.getNullOrTrimmed(gsForm.getSigWeicheVon()));
		gleissperrung.setSigWeicheBis(FrontendHelper.getNullOrTrimmed(gsForm.getSigWeicheBis()));

		gleissperrung.setZeitVon(FrontendHelper.castStringToDate(gsForm.getZeitVon()));
		gleissperrung.setZeitBis(FrontendHelper.castStringToDate(gsForm.getZeitBis()));
		gleissperrung.setZeitVonKoordiniert(FrontendHelper.castStringToDate(gsForm
		    .getZeitVonKoordiniert()));
		gleissperrung.setZeitBisKoordiniert(FrontendHelper.castStringToDate(gsForm
		    .getZeitBisKoordiniert()));

		gleissperrung.setDurchgehend(gsForm.getDurchgehend());
		gleissperrung.setSchichtweise(gsForm.getSchichtweise());

		/* Sperrpausenbedarfe */
		gleissperrung.setSperrpausenbedarfEsp(null);
		gleissperrung.setSperrpausenbedarfTsp(null);
		gleissperrung.setSperrpausenbedarfBfGl(null);
		if (FrontendHelper.stringNotNullOrEmpty(gsForm.getSperrpausenbedarf())
		    && FrontendHelper.stringNotNullOrEmpty(gsForm.getSperrpausenbedarfArt())) {
			if (SperrpausenbedarfArt.valueOf(gsForm.getSperrpausenbedarfArt()).equals(
			    SperrpausenbedarfArt.ESP)) {
				gleissperrung.setSperrpausenbedarfEsp(FrontendHelper.castStringToInteger(gsForm
				    .getSperrpausenbedarf()));
			} else if (SperrpausenbedarfArt.valueOf(gsForm.getSperrpausenbedarfArt()).equals(
			    SperrpausenbedarfArt.TSP)) {
				gleissperrung.setSperrpausenbedarfTsp(FrontendHelper.castStringToInteger(gsForm
				    .getSperrpausenbedarf()));
			} else if (SperrpausenbedarfArt.valueOf(gsForm.getSperrpausenbedarfArt()).equals(
			    SperrpausenbedarfArt.SPERR_BF_GL)) {
				gleissperrung.setSperrpausenbedarfBfGl(FrontendHelper.castStringToInteger(gsForm
				    .getSperrpausenbedarf()));
			}
		}

		gleissperrung.setBetriebsweise(serviceFactory.createBetriebsweiseService().findById(
		    gsForm.getBetriebsweiseId()));

		if (FrontendHelper.stringNotNullOrEmpty(gsForm.getAuswirkung())) {
			gleissperrung.setAuswirkung(SperrungAuswirkung.valueOf(gsForm.getAuswirkung()));
		} else {
			gleissperrung.setAuswirkung(null);
		}

		if (gsForm.getBauLue() != null && gsForm.getBauLue() == Boolean.TRUE) {
			gleissperrung.setBauLue(true);
		} else {
			gleissperrung.setBauLue(false);
		}

		gleissperrung.setKommentar(FrontendHelper.getNullOrTrimmed(gsForm.getKommentar()));

		// Vehrkehrstageregelung
		Integer vts = VtrHelper.getVtsByDays(gsForm.isWtsMo(), gsForm.isWtsDi(), gsForm.isWtsMi(),
		    gsForm.isWtsDo(), gsForm.isWtsFr(), gsForm.isWtsSa(), gsForm.isWtsSo());
		VerkehrstageregelungService vtrService = serviceFactory.createVerkehrstageregelungService();
		gleissperrung.setVtr(vtrService.findByVtsWithoutDuplicates(vts));

		gleissperrung.setLastChange(null);

		if (hasErrors()) {
			return mapping.findForward("FAILURE");
		}

		// Berechtigungen prüfen
		if (gleissperrungId == 0) {
			if (voter.vote(secUser, gleissperrung, "ROLE_GLEISSPERRUNG_ANLEGEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				// Gleissperrung erstellen
				gleissperrungService.create(gleissperrung);
				// ID zum Laden in EditAction setzen
				if (log.isDebugEnabled())
					log.debug("Set gleissperrungId to request: " + gleissperrung.getId());
				request.setAttribute("gleissperrungId", gleissperrung.getId());
			} else {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
		} else {
			if (voter.vote(secUser, gleissperrung, "ROLE_GLEISSPERRUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				// Änderungen speichern
				gleissperrungService.update(gleissperrung);
			} else {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
		}
		addMessage("success.saved");

		return mapping.findForward("SUCCESS");
	}
}