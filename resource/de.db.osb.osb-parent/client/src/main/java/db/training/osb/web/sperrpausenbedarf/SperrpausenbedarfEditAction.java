package db.training.osb.web.sperrpausenbedarf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.enums.Bauverfahren;
import db.training.osb.model.enums.TiefentwaesserungLage;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.hibernate.TqmUser;

public class SperrpausenbedarfEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SperrpausenbedarfEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering SperrpausenbedarfEditAction.");

		TqmUser secUser = getSecUser();

		SAPMassnahme massnahme = null;
		SperrpausenbedarfForm spForm = (SperrpausenbedarfForm) form;
		Integer spId = spForm.getSperrpausenbedarfId();

		// Wenn ID nach Neuanlegen von Save-Action uebergeben wird
		if (request.getAttribute("sperrpausenbedarfId") != null) {
			spId = (Integer) request.getAttribute("sperrpausenbedarfId");
			if (log.isDebugEnabled())
				log.debug("Request: sperrpausenbedarfId: " + spId);
		}
		if (spId == null) {
			if (log.isDebugEnabled())
				log.debug("No sperrpausenbedarfId found");
			addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing sperrpausenbedarfId: " + spId);

		Integer vzgStreckeNummer = null;

		// Sperrpausenbedarf bearbeiten
		if (spId != 0) {
			// Maßnahme laden
			SAPMassnahmeService massnahmeService = serviceFactory.createSAPMassnahmeService();

			massnahme = massnahmeService.findById(spId, new Preload[] {
			        new Preload(SAPMassnahme.class, "regionalbereich"),
			        new Preload(SAPMassnahme.class, "hauptStrecke"),
			        new Preload(VzgStrecke.class, "betriebsstellen"),
			        new Preload(SAPMassnahme.class, "paket"),
			        new Preload(SAPMassnahme.class, "betriebsstelleVon"),
			        new Preload(SAPMassnahme.class, "betriebsstelleBis"),
			        new Preload(SAPMassnahme.class, "betriebsstelleVonKoordiniert"),
			        new Preload(SAPMassnahme.class, "betriebsstelleBisKoordiniert"),
			        new Preload(SAPMassnahme.class, "topProjekte"),
			        new Preload(TopProjekt.class, "regionalbereich"),
			        new Preload(SAPMassnahme.class, "gleissperrungen"),
			        new Preload(Gleissperrung.class, "buendel"),
			        new Preload(Gleissperrung.class, "massnahme"),
			        new Preload(Gleissperrung.class, "vzgStrecke"),
			        new Preload(Gleissperrung.class, "vtr") });

			if (massnahme == null) {
				if (log.isDebugEnabled())
					log.debug("massnahme not found: " + spId);
				addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing Sperrpausenbedarf: " + massnahme.getId());

			// Fahrplanjahr prüfen
			if (!sessionFahrplanjahr.equals(massnahme.getFahrplanjahr())) {
				addError("error.fahrplanjahr.wrong");
				return mapping.findForward("FAILURE");
			}

			// Berechtigungen prüfen
			if (EasyServiceFactory.getInstance().createSAPMassnahmeAnyVoter()
			    .vote(secUser, massnahme, "ROLE_MASSNAHME_BEARBEITEN") != AccessDecisionVoter.ACCESS_GRANTED) {
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}

			// Formular füllen
			if (!hasErrors(request)) {
				initForm(spForm, massnahme);

				if (massnahme.getHauptStrecke() != null) {
					spForm.setHauptStrecke(massnahme.getHauptStrecke().getCaption());
					vzgStreckeNummer = massnahme.getHauptStrecke().getNummer();
				}
			}
			// Muss zur Validierung im Form gesetzt sein
			spForm.setFahrplanjahr(massnahme.getFahrplanjahr());
		}
		// neue Sperrpausenbedarf erstellen
		else {
			if (!secUser.hasAuthorization("ROLE_MASSNAHME_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_MASSNAHME_ANLEGEN_REGIONALBEREICH")
			    && !secUser.hasAuthorization("ROLE_MASSNAHME_ANLEGEN_TEMPORAER")
			    && !secUser.hasAuthorization("ROLE_MASSNAHME_BEARBEITEN_TEMPORAER")) {
				if (log.isDebugEnabled())
					log.debug("ACCESS DENIED -> ROLE_MASSNAHME_ANLEGEN");
				addError("common.noAuth");
				return mapping.findForward("FAILURE");
			}
			if (!hasErrors(request)) {
				spForm.reset();
				// VTRs default auf true
				spForm.setWtsMo(true);
				spForm.setWtsDi(true);
				spForm.setWtsMi(true);
				spForm.setWtsDo(true);
				spForm.setWtsFr(true);
				spForm.setWtsSa(true);
				spForm.setWtsSo(true);
			}
			spForm.setSperrpausenbedarfId(0);

			massnahme = new SAPMassnahme();
			massnahme.setFahrplanjahr(sessionFahrplanjahr);
			// Muss zur Validierung im Form gesetzt sein
			spForm.setFahrplanjahr(massnahme.getFahrplanjahr());
		}

		// VzgStrecke zur Fuellung der Betriebsstellen setzen
		if (hasErrors(request)) {
			VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
			vzgStreckeNummer = vzgService.castCaptionToNummer(spForm.getHauptStrecke());
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

		// Listen fuellen
		request.setAttribute("regionalbereiche", serviceFactory.createRegionalbereichService()
		    .findAll());
		request.setAttribute("pakete", serviceFactory.createPaketService().findAll());
		request.setAttribute("finanztypen", serviceFactory.createFinanztypService().findAll());
		request.setAttribute("arbeitstypen", serviceFactory.createArbeitstypService().findAll());
		request.setAttribute("bauverfahren", Arrays.asList(Bauverfahren.values()));
		request.setAttribute("tiefentwaesserungLagen",
		    Arrays.asList(TiefentwaesserungLage.values()));
		request.setAttribute("auftraggeber", serviceFactory.createAnmelderService()
		    .findAllAuftraggeber(null));
		request.setAttribute("ankermassnahmeArten", serviceFactory.createAnkermassnahmeArtService()
		    .findAll());
		request.setAttribute("baumassnahme", massnahme);

		// Variable zur Steuerung der Feldanzeige
		if (secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
			request.setAttribute("isAdmin", true);
			request.setAttribute("saveAction", "/osb/saveSperrpausenbedarf");
			return mapping.findForward("SUCCESS");
		}

		request.setAttribute("isAdmin", false);
		request.setAttribute("saveAction", "/osb/saveSperrpausenbedarfSmall");
		// Wenn eingeschraenkte Sicht gezeigt werden soll, muss anderes Form fuer die Validierung
		// genommen werden, deshalb diese Weiterleitung
		if (!request.getRequestURI().contains("Small"))
			return mapping.findForward("SUCCESS_SMALL");
		return mapping.findForward("SUCCESS");
	}

	private void initForm(SperrpausenbedarfForm spForm, SAPMassnahme massnahme) {
		spForm.setSperrpausenbedarfId(massnahme.getId());
		// Auftrag
		if (massnahme.getAuftraggeber() != null)
			spForm.setAuftraggeberId(massnahme.getAuftraggeber().getId());
		if (massnahme.getRegionalbereich() != null)
			spForm.setRegionalbereichId(massnahme.getRegionalbereich().getId());
		if (massnahme.getFinanztyp() != null)
			spForm.setFinanztypId(massnahme.getFinanztyp().getId());
		// Fixierung
		if (massnahme.getGenehmiger() != null)
			spForm.setFixiert(true);
		else
			spForm.setFixiert(false);
		// Anker
		if (massnahme.getAnkermassnahmeArt() != null)
			spForm.setAnkermassnahmeArtId(massnahme.getAnkermassnahmeArt().getId());
		// Arbeiten
		if (massnahme.getArbeiten() != null)
			spForm.setArbeitenId(massnahme.getArbeiten().getId());
		spForm.setArbeitenKommentar(massnahme.getArbeitenKommentar());
		// Strecke
		if (massnahme.getRichtungsKennzahl() != null)
			spForm.setRichtungsKennzahl(massnahme.getRichtungsKennzahl().toString());
		spForm.setKmVon(FrontendHelper.castDoubleToString(massnahme.getKmVon(), 3));
		spForm.setKmBis(FrontendHelper.castDoubleToString(massnahme.getKmBis(), 3));
		// Betriebsstellen
		if (massnahme.getBetriebsstelleVon() != null)
			spForm.setBetriebsstelleVonId(massnahme.getBetriebsstelleVon().getId());
		if (massnahme.getBetriebsstelleBis() != null)
			spForm.setBetriebsstelleBisId(massnahme.getBetriebsstelleBis().getId());
		if (massnahme.getBetriebsstelleVonKoordiniert() != null)
			spForm.setBetriebsstelleVonKoordiniertId(massnahme.getBetriebsstelleVonKoordiniert()
			    .getId());
		if (massnahme.getBetriebsstelleBisKoordiniert() != null)
			spForm.setBetriebsstelleBisKoordiniertId(massnahme.getBetriebsstelleBisKoordiniert()
			    .getId());
		// Termine
		spForm.setBauterminStart(FrontendHelper.castDateToString(massnahme.getBauterminStart(),
		    "dd.MM.yyyy HH:mm"));
		spForm.setBauterminEnde(FrontendHelper.castDateToString(massnahme.getBauterminEnde(),
		    "dd.MM.yyyy HH:mm"));
		spForm.setBauterminStartKoordiniert(FrontendHelper.castDateToString(
		    massnahme.getBauterminStartKoordiniert(), "dd.MM.yyyy HH:mm"));
		spForm.setBauterminEndeKoordiniert(FrontendHelper.castDateToString(
		    massnahme.getBauterminEndeKoordiniert(), "dd.MM.yyyy HH:mm"));
		// Schicht
		spForm.setDurchgehend(massnahme.getDurchgehend());
		spForm.setSchichtweise(massnahme.getSchichtweise());
		// Verkehrstageregelung
		if (massnahme.getVtr() != null) {
			Integer vts = massnahme.getVtr().getVts();

			spForm.setWtsMo(VtrHelper.isDayPartOfVts(vts, Calendar.MONDAY));
			spForm.setWtsDi(VtrHelper.isDayPartOfVts(vts, Calendar.TUESDAY));
			spForm.setWtsMi(VtrHelper.isDayPartOfVts(vts, Calendar.WEDNESDAY));
			spForm.setWtsDo(VtrHelper.isDayPartOfVts(vts, Calendar.THURSDAY));
			spForm.setWtsFr(VtrHelper.isDayPartOfVts(vts, Calendar.FRIDAY));
			spForm.setWtsSa(VtrHelper.isDayPartOfVts(vts, Calendar.SATURDAY));
			spForm.setWtsSo(VtrHelper.isDayPartOfVts(vts, Calendar.SUNDAY));
		}
		// Bauangelegenheiten
		if (massnahme.getPaket() != null)
			spForm.setPaketId(massnahme.getPaket().getId());
		spForm.setTechnischerPlatz(massnahme.getTechnischerPlatz());
		if (massnahme.getBauverfahren() != null)
			spForm.setBauverfahren(massnahme.getBauverfahren().toString());
		spForm.setPspElement(massnahme.getPspElement());
		spForm.setGewerk(massnahme.getGewerk());
		spForm.setUntergewerk(massnahme.getUntergewerk());
		spForm.setWeichenGleisnummerBfGleisen(massnahme.getWeichenGleisnummerBfGleisen());
		spForm.setWeichenbauform(massnahme.getWeichenbauform());
		// Kommentare
		spForm.setKommentar(massnahme.getKommentar());
		spForm.setKommentarKoordination(massnahme.getKommentarKoordination());
		// Booleans
		spForm.setBahnsteige(massnahme.getBahnsteige());
		spForm.setEinbauPss(massnahme.getEinbauPss());
		spForm.setKabelkanal(massnahme.getKabelkanal());
		spForm.setOberleitungsAnpassung(massnahme.getOberleitungsAnpassung());
		spForm.setLst(massnahme.getLst());
		// Maßeinheiten
		spForm.setGeplanteNennleistung(FrontendHelper.castIntegerToString(massnahme
		    .getGeplanteNennleistung()));
		spForm.setNotwendigeLaengePss(FrontendHelper.castIntegerToString(massnahme
		    .getNotwendigeLaengePss()));
		spForm.setUmbaulaenge(FrontendHelper.castIntegerToString(massnahme.getUmbaulaenge()));
		if (massnahme.getTiefentwaesserungLage() != null)
			spForm.setTiefentwaesserungLage(massnahme.getTiefentwaesserungLage().toString());
	}
}
