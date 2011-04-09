package db.training.osb.web.gleissperrung;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.enums.SperrpausenbedarfArt;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class GleissperrungEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungEditAction.");

		TqmUser secUser = getSecUser();

		Gleissperrung gleissperrung = null;
		GleissperrungForm gsForm = (GleissperrungForm) form;
		Integer gleissperrungId = gsForm.getGleissperrungId();

		// Wenn ID nach Neuanlegen von Save-Action uebergeben wird
		if (request.getAttribute("gleissperrungId") != null) {
			gleissperrungId = (Integer) request.getAttribute("gleissperrungId");
			if (log.isDebugEnabled())
				log.debug("Request: gleissperrungId: " + gleissperrungId);
		}
		if (gleissperrungId == null) {
			if (log.isDebugEnabled())
				log.debug("No gleissperrungId found");
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing gleissperrungId: " + gleissperrungId);

		Integer vzgStreckeNummer = null;

		if (gleissperrungId != 0) {
			gleissperrung = serviceFactory.createGleissperrungService().findById(
			    gleissperrungId,
			    new Preload[] { new Preload(Gleissperrung.class, "massnahme"),
			            new Preload(Gleissperrung.class, "baustellen"),
			            new Preload(Gleissperrung.class, "betriebsweise"),
			            new Preload(Gleissperrung.class, "vzgStrecke"),
			            new Preload(VzgStrecke.class, "betriebsstellen"),
			            new Preload(Gleissperrung.class, "buendel"),
			            new Preload(Gleissperrung.class, "vtr") });

			if (gleissperrung == null) {
				if (log.isDebugEnabled())
					log.debug("Gleissperrung not found: " + gleissperrungId);
				addError("error.notfound", msgRes.getMessage("gleissperrung"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing gleissperrung: " + gleissperrung.getId());

			EasyAccessDecisionVoter voter = serviceFactory.createRegelungAnyVoter();
			if (voter.vote(secUser, gleissperrung, "ROLE_GLEISSPERRUNG_BEARBEITEN") != AccessDecisionVoter.ACCESS_GRANTED) {
				if (log.isDebugEnabled())
					log.debug("ACCESS DENIED -> ROLE_GLEISSPERRUNG_BEARBEITEN");
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			if (!hasErrors(request)) {
				// Formular füllen
				gsForm.reset();
				
				gsForm.setGleissperrungId(gleissperrung.getId());
				gsForm.setLfdNr(gleissperrung.getLfdNr());
				gsForm.setBezeichnung(gleissperrung.getBezeichnung());
				gsForm.setKmVon(FrontendHelper.castFloatToString(gleissperrung.getKmVon()));
				gsForm.setKmBis(FrontendHelper.castFloatToString(gleissperrung.getKmBis()));
				if (gleissperrung.getVzgStrecke() != null) {
					gsForm.setVzgStrecke(gleissperrung.getVzgStrecke().getCaption());
					vzgStreckeNummer = gleissperrung.getVzgStrecke().getNummer();
				}
				gsForm.setRichtungsKennzahl(gleissperrung.getRichtungsKennzahl());
				if (gleissperrung.getBstVon() != null) {
					gsForm.setBstVonId(gleissperrung.getBstVon().getId());
				}
				if (gleissperrung.getBstBis() != null) {
					gsForm.setBstBisId(gleissperrung.getBstBis().getId());
				}
				if (gleissperrung.getBstVonKoordiniert() != null) {
					gsForm.setBstVonKoordiniertId(gleissperrung.getBstVonKoordiniert().getId());
				}
				if (gleissperrung.getBstBisKoordiniert() != null) {
					gsForm.setBstBisKoordiniertId(gleissperrung.getBstBisKoordiniert().getId());
				}

				gsForm.setSigWeicheVon(gleissperrung.getSigWeicheVon());
				gsForm.setSigWeicheBis(gleissperrung.getSigWeicheBis());

				gsForm.setZeitVon(FrontendHelper.castDateToString(gleissperrung.getZeitVon(),
				    "dd.MM.yyyy HH:mm"));
				gsForm.setZeitBis(FrontendHelper.castDateToString(gleissperrung.getZeitBis(),
				    "dd.MM.yyyy HH:mm"));
				gsForm.setZeitVonKoordiniert(FrontendHelper.castDateToString(gleissperrung
				    .getZeitVonKoordiniert(), "dd.MM.yyyy HH:mm"));
				gsForm.setZeitBisKoordiniert(FrontendHelper.castDateToString(gleissperrung
				    .getZeitBisKoordiniert(), "dd.MM.yyyy HH:mm"));

				gsForm.setDurchgehend(gleissperrung.getDurchgehend());
				gsForm.setSchichtweise(gleissperrung.getSchichtweise());

				if (gleissperrung.getBetriebsweise() != null) {
					gsForm.setBetriebsweise(gleissperrung.getBetriebsweise().getName());
					gsForm.setBetriebsweiseId(gleissperrung.getBetriebsweise().getId());
				}

				/* Sperrpausenbedarfe */
				if (gleissperrung.getSperrpausenbedarfEsp() != null) {
					gsForm.setSperrpausenbedarf(FrontendHelper.castIntegerToString(gleissperrung
					    .getSperrpausenbedarfEsp()));
					gsForm.setSperrpausenbedarfArt(SperrpausenbedarfArt.ESP.toString());
				} else if (gleissperrung.getSperrpausenbedarfTsp() != null) {
					gsForm.setSperrpausenbedarf(FrontendHelper.castIntegerToString(gleissperrung
					    .getSperrpausenbedarfTsp()));
					gsForm.setSperrpausenbedarfArt(SperrpausenbedarfArt.TSP.toString());
				} else if (gleissperrung.getSperrpausenbedarfBfGl() != null) {
					gsForm.setSperrpausenbedarf(FrontendHelper.castIntegerToString(gleissperrung
					    .getSperrpausenbedarfBfGl()));
					gsForm.setSperrpausenbedarfArt(SperrpausenbedarfArt.SPERR_BF_GL.toString());
				}

				if (gleissperrung.getAuswirkung() != null) {
					gsForm.setAuswirkung(gleissperrung.getAuswirkung().name());
				}
				gsForm.setBauLue(gleissperrung.getBauLue());
				gsForm.setKommentar(gleissperrung.getKommentar());

				if (gleissperrung.getVtr() != null) {
					Integer vts = gleissperrung.getVtr().getVts();

					gsForm.setWtsMo(VtrHelper.isDayPartOfVts(vts, Calendar.MONDAY));
					gsForm.setWtsDi(VtrHelper.isDayPartOfVts(vts, Calendar.TUESDAY));
					gsForm.setWtsMi(VtrHelper.isDayPartOfVts(vts, Calendar.WEDNESDAY));
					gsForm.setWtsDo(VtrHelper.isDayPartOfVts(vts, Calendar.THURSDAY));
					gsForm.setWtsFr(VtrHelper.isDayPartOfVts(vts, Calendar.FRIDAY));
					gsForm.setWtsSa(VtrHelper.isDayPartOfVts(vts, Calendar.SATURDAY));
					gsForm.setWtsSo(VtrHelper.isDayPartOfVts(vts, Calendar.SUNDAY));
				}
			}
		}
		// neue Gleissperrung erstellen
		else {
			if (secUser.hasAuthorization("ROLE_GLEISSPERRUNG_ANLEGEN_ALLE")
			    && secUser.hasAuthorization("ROLE_GLEISSPERRUNG_ANLEGEN_REGIONALBEREICH")
			    && secUser.hasAuthorization("ROLE_GLEISSPERRUNG_ANLEGEN_TEMPORAER")
			    && secUser.hasAuthorization("ROLE_GLEISSPERRUNG_BEARBEITEN_TEMPORAER")) {
				if (log.isDebugEnabled())
					log.debug("ACCESS DENIED -> ROLE_GLEISSPERRUNG_ANLEGEN");
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
			if (!hasErrors(request))
				gsForm.reset();
			gsForm.setGleissperrungId(0);
		}

		// Fahrplanjahr setzen
		Integer fahrplanjahr = sessionFahrplanjahr;
		if (!hasErrors(request)) {
			if (gleissperrung != null && gleissperrung.getFahrplanjahr() != null)
				fahrplanjahr = gleissperrung.getFahrplanjahr();
			gsForm.setFahrplanjahr(fahrplanjahr.toString());
		} else {
			fahrplanjahr = FrontendHelper.castStringToInteger(gsForm.getFahrplanjahr());
		}

		// VzgStrecke zur Fuellung der Betriebsstellen setzen
		if (hasErrors(request)) {
			VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
			vzgStreckeNummer = vzgService.castCaptionToNummer(gsForm.getVzgStrecke());
		}

		List<Betriebsstelle> bsts = new ArrayList<Betriebsstelle>();
		if (vzgStreckeNummer != null) {
			BetriebsstelleService bsService = EasyServiceFactory.getInstance()
			    .createBetriebsstelleService();
			bsts = bsService.findByVzgStreckeNummerAndFahrplanjahr(vzgStreckeNummer, fahrplanjahr,
			    true, null);
		}
		request.setAttribute("betriebsstellen", bsts);

		request.setAttribute("gleissperrung", gleissperrung);
		request.setAttribute("betriebsweiseList", serviceFactory.createBetriebsweiseService()
		    .findAll());
		request.setAttribute("listRegionalbereiche", serviceFactory.createRegionalbereichService()
		    .findAll());
		request.setAttribute("listVtr", serviceFactory.createVerkehrstageregelungService()
		    .findAll());

		// sperrpausenbedarfArten fuer Dropdownlisten
		List<LabelValueBean> sperrpausenbedarfArten = new ArrayList<LabelValueBean>();
		for (SperrpausenbedarfArt art : SperrpausenbedarfArt.values()) {
			sperrpausenbedarfArten.add(new LabelValueBean(msgRes
			    .getMessage("sperrpausenbedarf.art." + art.name()), art.name()));
		}
		request.setAttribute("sperrpausenbedarfArten", sperrpausenbedarfArten);

		return mapping.findForward("SUCCESS");
	}
}