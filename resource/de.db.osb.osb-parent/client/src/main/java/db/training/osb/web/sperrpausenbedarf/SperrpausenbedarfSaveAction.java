package db.training.osb.web.sperrpausenbedarf;

import java.util.Date;

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
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.enums.Bauverfahren;
import db.training.osb.model.enums.TiefentwaesserungLage;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.osb.service.VerkehrstageregelungService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.hibernate.TqmUser;

public class SperrpausenbedarfSaveAction extends BaseAction {

	private static Logger log = Logger.getLogger(SperrpausenbedarfSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering SperrpausenbedarfSaveAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		SAPMassnahme massnahme = null;
		SperrpausenbedarfForm spForm = (SperrpausenbedarfForm) form;
		Integer spId = spForm.getSperrpausenbedarfId();

		SAPMassnahmeService massnahmeService = serviceFactory.createSAPMassnahmeService();

		if (spId != null) {
			if (spId == 0) {
				if (log.isDebugEnabled())
					log.debug("Create new Sperrpausenbedarf: " + spId);
				massnahme = new SAPMassnahme();
				// LfdNr wird automatisch bei create mit gesetzt
				massnahme.setUrspruenglichesPlanungsjahr(sessionFahrplanjahr);
				massnahme.setFahrplanjahr(sessionFahrplanjahr);
			} else {
				if (log.isDebugEnabled())
					log.debug("Find Sperrpausenbedarf by Id: " + spId);
				massnahme = massnahmeService.findById(spId, new Preload[] {
				        new Preload(SAPMassnahme.class, "regionalbereich"),
				        new Preload(SAPMassnahme.class, "paket"),
				        new Preload(SAPMassnahme.class, "betriebsstelleVon"),
				        new Preload(SAPMassnahme.class, "betriebsstelleBis"),
				        new Preload(SAPMassnahme.class, "weitereStrecken") });
			}
		}

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

		// Berechtigungen prüfen, muss vor dem Setzen der Attribute geschehen, da es sonst durch
		// Fixierung zu Fehlern kommt
		if ((spId == 0 && serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
		    "ROLE_MASSNAHME_ANLEGEN") == AccessDecisionVoter.ACCESS_DENIED)
		    || (spId != 0 && serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
		        "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)) {
			addError("common.noAuth");
			return mapping.findForward("ACCESS_DENIED");
		}

		// Ist der Benutzer kein Administrator, muss nach einer Fixierung muss auf Liste gesprungen
		// werden
		boolean showList = false;
		if (spForm.getFixiert() && massnahme.getGenehmiger() == null
		    && !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
			showList = true;
		}

		// Felder setzen
		initMassnahmeAllgemein(spForm, massnahme, loginUser, secUser);
		if (secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
			initMassnahmeAdministrator(spForm, massnahme, loginUser);
		}
		massnahme.setLastChange(loginUser);

		if (spId == 0) {
			// Sperrpausenbedarf erstellen
			massnahmeService.create(massnahme);
			// ID zum Laden in EditAction setzen
			if (log.isDebugEnabled())
				log.debug("Set sperrpausenbedarfId to request: " + massnahme.getId());
			request.setAttribute("sperrpausenbedarfId", massnahme.getId());
		} else {
			massnahmeService.update(massnahme);
		}
		addMessage("success.saved");

		if (showList)
			return mapping.findForward("SUCCESS_LIST");
		return mapping.findForward("SUCCESS");
	}

	private void initMassnahmeAllgemein(SperrpausenbedarfForm spForm, SAPMassnahme massnahme,
	    User loginUser, TqmUser secUser) {
		if ((massnahme.getGenehmiger() == null && serviceFactory.createSAPMassnahmeAnyVoter().vote(
		    secUser, massnahme, "ROLE_MASSNAHME_FIXIEREN") == AccessDecisionVoter.ACCESS_GRANTED)
		    || (massnahme.getGenehmiger() != null && secUser.hasRole("ADMINISTRATOR_ZENTRAL"))) {
			// Fixierung
			if (spForm.getFixiert() == true) {
				massnahme.setGenehmiger(loginUser);
				massnahme.setGenehmigerRegionalbereich(loginUser.getRegionalbereich());
				massnahme.setGenehmigungsDatum(new Date());
			} else {
				massnahme.setGenehmiger(null);
				massnahme.setGenehmigerRegionalbereich(null);
				massnahme.setGenehmigungsDatum(null);
			}
		}
		// Anker
		if (spForm.getAnkermassnahmeArtId() != null) {
			massnahme.setAnkermassnahmeArt(serviceFactory.createAnkermassnahmeArtService()
			    .findById(spForm.getAnkermassnahmeArtId()));
		}
		// Betriebsstellen
		BetriebsstelleService bsService = serviceFactory.createBetriebsstelleService();
		if (spForm.getBetriebsstelleVonKoordiniertId() != null) {
			massnahme.setBetriebsstelleVonKoordiniert(bsService.findById(spForm
			    .getBetriebsstelleVonKoordiniertId()));
		}
		if (spForm.getBetriebsstelleBisKoordiniertId() != null) {
			massnahme.setBetriebsstelleBisKoordiniert(bsService.findById(spForm
			    .getBetriebsstelleBisKoordiniertId()));
		}
		// Termine
		massnahme.setBauterminStartKoordiniert(FrontendHelper.castStringToDate(spForm
		    .getBauterminStartKoordiniert()));
		massnahme.setBauterminEndeKoordiniert(FrontendHelper.castStringToDate(spForm
		    .getBauterminEndeKoordiniert()));
		// Kommentare
		massnahme.setKommentarKoordination(FrontendHelper.getNullOrTrimmed(spForm
		    .getKommentarKoordination()));
	}

	private void initMassnahmeAdministrator(SperrpausenbedarfForm spForm, SAPMassnahme massnahme,
	    User loginUser) {
		// Auftraggeber
		if (spForm.getAuftraggeberId() != null) {
			massnahme.setAuftraggeber(serviceFactory.createAnmelderService().findById(
			    spForm.getAuftraggeberId()));
		}
		// Regionalbereich
		if (spForm.getRegionalbereichId() != null) {
			massnahme.setRegionalbereich(serviceFactory.createRegionalbereichService().findById(
			    spForm.getRegionalbereichId()));
		}
		// Finanztyp
		if (spForm.getFinanztypId() != null) {
			massnahme.setFinanztyp(serviceFactory.createFinanztypService().findById(
			    spForm.getFinanztypId()));
		}
		// Arbeiten
		if (spForm.getArbeitenId() != null) {
			massnahme.setArbeiten(serviceFactory.createArbeitstypService().findById(
			    spForm.getArbeitenId()));
		}
		massnahme.setArbeitenKommentar(FrontendHelper.getNullOrTrimmed(spForm
		    .getArbeitenKommentar()));
		// Strecke
		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(spForm.getHauptStrecke());
		if (vzgStreckeNummer != null) {
			massnahme.setHauptStrecke(CollectionHelper.getFirst(vzgService.findByNummer(
			    vzgStreckeNummer, spForm.getFahrplanjahr(), null, true, null)));
		}
		massnahme.setRichtungsKennzahl(FrontendHelper.castStringToInteger(spForm
		    .getRichtungsKennzahl()));
		massnahme.setKmVon(FrontendHelper.castStringToDouble(spForm.getKmVon()));
		massnahme.setKmBis(FrontendHelper.castStringToDouble(spForm.getKmBis()));
		// Betriebsstellen
		BetriebsstelleService bsService = serviceFactory.createBetriebsstelleService();
		if (spForm.getBetriebsstelleVonId() != null) {
			massnahme.setBetriebsstelleVon(bsService.findById(spForm.getBetriebsstelleVonId()));
		}
		if (spForm.getBetriebsstelleBisId() != null) {
			massnahme.setBetriebsstelleBis(bsService.findById(spForm.getBetriebsstelleBisId()));
		}
		// Termine
		massnahme.setBauterminStart(FrontendHelper.castStringToDate(spForm.getBauterminStart()));
		massnahme.setBauterminEnde(FrontendHelper.castStringToDate(spForm.getBauterminEnde()));
		// Schicht
		massnahme.setDurchgehend(spForm.getDurchgehend());
		massnahme.setSchichtweise(spForm.getSchichtweise());
		// Verkehrstageregelung
		Integer vts = VtrHelper.getVtsByDays(spForm.isWtsMo(), spForm.isWtsDi(), spForm.isWtsMi(),
		    spForm.isWtsDo(), spForm.isWtsFr(), spForm.isWtsSa(), spForm.isWtsSo());
		VerkehrstageregelungService vtrService = serviceFactory.createVerkehrstageregelungService();
		massnahme.setVtr(vtrService.findByVtsWithoutDuplicates(vts));
		// Paket
		if (spForm.getPaketId() != null) {
			massnahme.setPaket(serviceFactory.createPaketService().findById(spForm.getPaketId()));
		}
		// Bauverfahren
		if (FrontendHelper.stringNotNullOrEmpty(spForm.getBauverfahren())) {
			massnahme.setBauverfahren(Bauverfahren.valueOf(spForm.getBauverfahren()));
		} else
			massnahme.setBauverfahren(null);
		// Bauangelegenheiten
		massnahme
		    .setTechnischerPlatz(FrontendHelper.getNullOrTrimmed(spForm.getTechnischerPlatz()));
		massnahme.setPspElement(FrontendHelper.getNullOrTrimmed(spForm.getPspElement()));
		massnahme.setGewerk(FrontendHelper.getNullOrTrimmed(spForm.getGewerk()));
		massnahme.setUntergewerk(FrontendHelper.getNullOrTrimmed(spForm.getUntergewerk()));
		massnahme.setWeichenGleisnummerBfGleisen(FrontendHelper.getNullOrTrimmed(spForm
		    .getWeichenGleisnummerBfGleisen()));
		massnahme.setWeichenbauform(FrontendHelper.getNullOrTrimmed(spForm.getWeichenbauform()));
		// Kommentare
		massnahme.setKommentar(FrontendHelper.getNullOrTrimmed(spForm.getKommentar()));
		// Booleans
		massnahme.setBahnsteige(spForm.getBahnsteige());
		massnahme.setEinbauPss(spForm.getEinbauPss());
		massnahme.setKabelkanal(spForm.getKabelkanal());
		massnahme.setOberleitungsAnpassung(spForm.getOberleitungsAnpassung());
		massnahme.setLst(spForm.getLst());
		// Maßeinheiten
		massnahme.setGeplanteNennleistung(FrontendHelper.castStringToInteger(spForm
		    .getGeplanteNennleistung()));
		massnahme.setNotwendigeLaengePss(FrontendHelper.castStringToInteger(spForm
		    .getNotwendigeLaengePss()));
		massnahme.setUmbaulaenge(FrontendHelper.castStringToInteger(spForm.getUmbaulaenge()));
		// TiefentwaesserungLage
		if (FrontendHelper.stringNotNullOrEmpty(spForm.getTiefentwaesserungLage())) {
			massnahme.setTiefentwaesserungLage(TiefentwaesserungLage.valueOf(spForm
			    .getTiefentwaesserungLage()));
		} else
			massnahme.setTiefentwaesserungLage(null);
	}

}
