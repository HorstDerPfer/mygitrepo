package db.training.osb.web.massnahme;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import db.training.bob.service.RegionalbereichService;
import db.training.bob.util.CollectionHelper;
import db.training.easy.core.model.User;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.babett.StatusBbzr;
import db.training.osb.service.AnmelderService;
import db.training.osb.service.ArbeitstypService;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.FinanztypService;
import db.training.osb.service.FolgeNichtausfuehrungService;
import db.training.osb.service.GrossmaschineService;
import db.training.osb.service.PhaseService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.osb.service.TopProjektService;
import db.training.osb.service.VerkehrstageregelungService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.hibernate.TqmUser;

public class MassnahmeSaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(MassnahmeSaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeSaveAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		SAPMassnahme massnahme = null;
		MassnahmeForm massnahmeForm = (MassnahmeForm) form;
		Integer massnahmeId = massnahmeForm.getMassnahmeId();

		SAPMassnahmeService service = serviceFactory.createSAPMassnahmeService();

		if (massnahmeId == 0) {
			massnahme = new SAPMassnahme();
			if (log.isDebugEnabled())
				log.debug("Create new SAPMassnahme: " + massnahmeId);
		} else {
			massnahme = service.findById(massnahmeId, new Preload[] { new Preload(
			    SAPMassnahme.class, "topProjekte") });
			if (log.isDebugEnabled())
				log.debug("Find SAPMassnahme by Id: " + massnahmeId);
		}

		if (massnahme == null) {
			if (log.isDebugEnabled())
				log.debug("SAPMassnahme not found: " + massnahmeId);
			addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
			return mapping.findForward("FAILURE");
		}

		// Object fuellen
		if (massnahmeForm.getRegionalbereichId() != null)
			massnahme.setRegionalbereich(serviceFactory.createRegionalbereichService().findById(
			    massnahmeForm.getRegionalbereichId()));
		fill(massnahme, massnahmeForm, loginUser);
		massnahme.setLastChange(null);

		// Noch nicht sicher auf welche Felder sich dies beziehen soll
		// massnahme.setLetzteAenderungAnmeldung(new Date());

		if (massnahmeId == 0) {
			// Rechtepruefung: Marktbereich anlegen
			if (serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
			    "ROLE_MASSNAHME_ANLEGEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");
			// Erstellungsdatum setzen
			massnahme.setErsteAnmeldung(new Date());
			massnahme.setLetzteAenderungAnmeldung(massnahme.getErsteAnmeldung());
			service.create(massnahme);
		} else {
			// Rechtepruefung: Massnahme bearbeiten
			if (serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
			    "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");
			service.update(massnahme);
		}

		addMessage("success.save");

		return mapping.findForward("SUCCESS");
	}

	private void fill(SAPMassnahme mn, MassnahmeForm form, User loginUser) {
		BetriebsstelleService bsService = serviceFactory.createBetriebsstelleService();
		SAPMassnahmeService mService = serviceFactory.createSAPMassnahmeService();
		// Regionalbereich
		RegionalbereichService rbService = serviceFactory.createRegionalbereichService();
		mn.setRegionalbereich(rbService.findById(form.getRegionalbereichId()));
		// Studie
		mn.setStudie(form.getStudie());
		// Finanztyp
		FinanztypService fService = serviceFactory.createFinanztypService();
		mn.setFinanztyp(fService.findById(form.getFinanztypId()));
		// Phase
		PhaseService pService = serviceFactory.createPhaseService();
		mn.setPhase(pService.findById(form.getPhaseId()));
		// BBEI
		mn.setBbEi(form.getBbei());
		// vzgStrecke
		VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
		Date beginn = FrontendHelper.castStringToDate(form.getBeginn());
		Date ende = FrontendHelper.castStringToDate(form.getEnde());
		VzgStrecke vzgStrecke = null;
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(form.getStrecke());
		if (vzgStreckeNummer != null) {
			if (beginn != null && ende != null) {
				vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
				    beginn, ende, null, true, null));
			} else {
				vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
				    sessionFahrplanjahr, null, true, null));
			}
		}
		mn.setHauptStrecke(vzgStrecke);
		// Betriebsstellen
		mn.setBetriebsstelleVon(bsService.findById(form.getBetriebsstelleVonId()));
		mn.setBetriebsstelleBis(bsService.findById(form.getBetriebsstelleBisId()));
		// Richtungskennzahl
		mn.setRichtungsKennzahl(form.getRichtungskennzahl());
		// KM von, bis
		mn.setKmVon(FrontendHelper.castStringToDouble(form.getKmVon()));
		mn.setKmBis(FrontendHelper.castStringToDouble(form.getKmBis()));
		// Start, Ende
		mn.setBauterminStart(FrontendHelper.castStringToDate(form.getBeginn()));
		mn.setBauterminEnde(FrontendHelper.castStringToDate(form.getEnde()));
		// durchgehend, schichtweise
		mn.setDurchgehend(form.getDurchgehend());
		mn.setSchichtweise(form.getSchichtweise());
		// Fahrplanjahr
		mn.setFahrplanjahr(FrontendHelper.castStringToInteger(form.getFahrplanjahr()));
		// Vehrkehrstageregelung
		Integer vts = VtrHelper.getVtsByDays(form.isWtsMo(), form.isWtsDi(), form.isWtsMi(), form
		    .isWtsDo(), form.isWtsFr(), form.isWtsSa(), form.isWtsSo());
		VerkehrstageregelungService vtrService = serviceFactory.createVerkehrstageregelungService();
		mn.setVtr(vtrService.findByVtsWithoutDuplicates(vts));
		// TopProjekt
		TopProjektService topService = serviceFactory.createTopProjektService();
		TopProjekt topProjekt = topService.findById(form.getTopprojektId());
		if (topProjekt != null)
			mn.getTopProjekte().add(topProjekt);
		// technischer Platz
		mn.setTechnischerPlatz(form.getTechnischerPlatz());
		// Anmelder
		AnmelderService anmelderService = serviceFactory.createAnmelderService();
		mn.setAnmelder(anmelderService.findById(form.getAnmelderId()));
		// Anmeldedatum, Kommentar, Ergaenzung
		mn.setErsteAnmeldung(FrontendHelper.castStringToDate(form.getDatumAnmeldung()));
		mn.setKommentar(form.getKommentarAnmelder());
		mn.setAnmelderErgaenzung(form.getErgaenzungAnmelder());
		// Bei gravierender Aenderung und Verursacher == Anmelder, dann DatumLetzteAnmeldung = jetzt
		// Wert in Feld wird unabhaengig von Inhalt ueberschrieben
		if (form.getGravierendeAenderung()) {
			if (mn.getAnmelder() != null && mn.getAnmelder().getId().equals(loginUser.getId())) {
				mn.setLetzteAenderungAnmeldung(new Date());
			}
		}
		// Arbeiten, Ort, Kommentar
		ArbeitstypService abtService = serviceFactory.createArbeitstypService();
		mn.setArbeiten(abtService.findById(form.getArbeitenId()));
		mn.setArbeitenBetriebsstelle(bsService.findById(form.getArbeitenOrtId()));
		mn.setArbeitenKommentar(form.getArbeitenKommentar());
		// FolgeNichtausfuehrung
		FolgeNichtausfuehrungService fnService = serviceFactory
		    .createFolgeNichtausfuehrungService();
		mn.setFolgeNichtausfuehrung(fnService.findById(form.getFolgeNichtausfuehrungId()));
		mn.setFolgeNichtausfuehrungBeginn(FrontendHelper.castStringToDate(form
		    .getFolgeNichtausfuehrungBeginn()));
		mn.setFolgeNichtausfuehrungFzv(FrontendHelper.castStringToFloat(form
		    .getFolgeNichtausfuehrungFzv()));
		mn.setFolgeNichtausfuehrungLaGeschwindigkeit(FrontendHelper.castStringToInteger(form
		    .getFolgeNichtausfuehrungGeschwindigkeitLa()));
		// Grossmaschine
		GrossmaschineService gService = serviceFactory.createGrossmaschineService();
		mn.setGrossmaschine(gService.findById(form.getGrossmaschineId()));
		// StatusBBZR
		mn.setStatusBbzr(StatusBbzr.valueOf(form.getStatusBbzr()));
		// Hervorhebung
		// LAEintrag406
		mn.setLaEintragR406(form.getLaEintrag406());
		// LueHinweis
		mn.setLueHinweis(form.getLueHinweis());
		// Vorbedingung
		mn.setVorbedingung(mService.findById(form.getVorbedingungId()));
		// UnterDeckung
		mn.setUnterDeckung(mService.findById(form.getUnterdeckungId()));
		// AnkerMassnahme
		mn.setAnkermassnahme(mService.findById(form.getAnkermassnahmeId()));
	}
}
