package db.training.osb.web.massnahme;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import db.training.bob.util.CollectionHelper;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseDispatchAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsweise;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.babett.LaTyp;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.BetriebsweiseService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.LangsamfahrstelleService;
import db.training.osb.service.OberleitungService;
import db.training.osb.service.VerkehrstageregelungService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class MassnahmeRegelungSaveAction extends BaseDispatchAction {

	private static final Logger log = Logger.getLogger(MassnahmeRegelungSaveAction.class);

	private static EasyAccessDecisionVoter voterRegelung = EasyServiceFactory.getInstance()
	    .createRegelungAnyVoter();

	/* GLEISSPERRUNG */
	public ActionForward gleissperrung(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungSaveAction->gleissperrung.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;
		Integer regelungId = mrForm.getRegelungId();
		Gleissperrung gleissperrung = null;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), secUser);
		if (massnahme == null)
			return mapping.findForward("FAILURE");

		GleissperrungService gsService = serviceFactory.createGleissperrungService();

		if (regelungId == 0) {
			gleissperrung = new Gleissperrung();
			if (log.isDebugEnabled())
				log.debug("Create new Gleissperrung: " + regelungId);
		} else {
			gleissperrung = gsService.findById(mrForm.getRegelungId(), new Preload[] { new Preload(
			    Regelung.class, "massnahme") });
			if (log.isDebugEnabled())
				log.debug("Find Gleissperrung by Id: " + regelungId);
		}

		if (gleissperrung == null) {
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}

		// Beziehung zu Massnahme wird gesetzt
		gleissperrung.setMassnahme(massnahme);

		// Fuellen des Objektes anhand der Formulardaten
		fillRegelungByForm(mrForm, gleissperrung, massnahme);
		// Betriebsweise
		BetriebsweiseService bwService = serviceFactory.createBetriebsweiseService();
		Betriebsweise betriebsweise = null;
		if (mrForm.getBetriebsweiseId() != null)
			betriebsweise = bwService.findById(mrForm.getBetriebsweiseId());
		gleissperrung.setBetriebsweise(betriebsweise);
		// Sonderfelder, welche nicht in Regelung enthalten sind
		gleissperrung.setErgaenzungBetriebsweise(FrontendHelper.getNullOrTrimmed(mrForm
		    .getErgaenzungBetriebsweise()));
		gleissperrung.setRichtungsKennzahl(mrForm.getRichtungskennzahl());
		gleissperrung.setKmVon(FrontendHelper.castStringToFloat(mrForm.getKmVon()));
		gleissperrung.setKmBis(FrontendHelper.castStringToFloat(mrForm.getKmBis()));
		gleissperrung.setFzvMusterzug(FrontendHelper.castStringToFloat(mrForm.getFzvMusterzug()));
		gleissperrung.setGeschwindigkeitVzg(FrontendHelper.castStringToInteger(mrForm
		    .getGeschwindigkeitVzg()));
		gleissperrung.setVorschlagLisba(mrForm.getVorschlagLisba());

		// Objekt anlegen
		if (regelungId == 0) {
			if (!secUser.hasAuthorization("ROLE_GLEISSPERRUNG_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_GLEISSPERRUNG_ANLEGEN_REGIONALBEREICH"))
				return mapping.findForward("ACCESS_DENIED");
			gsService.create(gleissperrung);
		}
		// Objekt bearbeiten
		else {
			if (voterRegelung.vote(secUser, gleissperrung, "ROLE_GLEISSPERRUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");
			gsService.update(gleissperrung);
		}

		addMessage("success.saved");
		return mapping.findForward("SUCCESS");
	}

	/* LANGSAMFAHRSTELLE */
	public ActionForward langsamfahrstelle(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungSaveAction->langsamfahrstelle.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;
		Integer regelungId = mrForm.getRegelungId();
		Langsamfahrstelle lfs = null;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), secUser);
		if (massnahme == null)
			return mapping.findForward("FAILURE");

		LangsamfahrstelleService lfsService = serviceFactory.createLangsamfahrstelleService();

		if (regelungId == 0) {
			lfs = new Langsamfahrstelle();
			if (log.isDebugEnabled())
				log.debug("Create new Langsamfahrstelle: " + regelungId);
		} else {
			lfs = lfsService.findById(mrForm.getRegelungId(), new Preload[] { new Preload(
			    Regelung.class, "massnahme") });
			if (log.isDebugEnabled())
				log.debug("Find Langsamfahrstelle by Id: " + regelungId);
		}

		if (lfs == null) {
			addError("error.notfound", msgRes.getMessage("langsamfahrstelle"));
			return mapping.findForward("FAILURE");
		}

		// Beziehung zu Massnahme wird gesetzt
		lfs.setMassnahme(massnahme);

		// Fuellen des Objektes anhand der Formulardaten
		fillRegelungByForm(mrForm, lfs, massnahme);
		// Sonderfelder, welche nicht in Regelung enthalten sind
		lfs.setKmVon(FrontendHelper.castStringToFloat(mrForm.getKmVon()));
		lfs.setKmBis(FrontendHelper.castStringToFloat(mrForm.getKmBis()));
		lfs.setFzvMusterzug(FrontendHelper.castStringToFloat(mrForm.getFzvMusterzug()));
		lfs.setGeschwindigkeitVzg(FrontendHelper
		    .castStringToInteger(mrForm.getGeschwindigkeitVzg()));
		lfs.setGeschwindigkeitLa(FrontendHelper.castStringToInteger(mrForm.getGeschwindigkeitLa()));
		if (FrontendHelper.stringNotNullOrEmpty(mrForm.getLaTyp()))
			lfs.setLaTyp(LaTyp.valueOf(mrForm.getLaTyp()));

		// Objekt anlegen
		if (regelungId == 0) {
			if (!secUser.hasAuthorization("ROLE_LANGSAMFAHRSTELLE_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_LANGSAMFAHRSTELLE_ANLEGEN_REGIONALBEREICH"))
				return mapping.findForward("ACCESS_DENIED");
			lfsService.create(lfs);
		}
		// Objekt bearbeiten
		else {
			if (voterRegelung.vote(secUser, lfs, "ROLE_LANGSAMFAHRSTELLE_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");
			lfsService.update(lfs);
		}

		addMessage("success.saved");
		return mapping.findForward("SUCCESS");
	}

	/* OBERLEITUNG */
	public ActionForward oberleitung(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungSaveAction->oberleitung.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;
		Integer regelungId = mrForm.getRegelungId();
		Oberleitung oberleitung = null;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), secUser);

		if (massnahme == null)
			return mapping.findForward("FAILURE");
		OberleitungService oService = serviceFactory.createOberleitungService();

		if (regelungId == 0) {
			oberleitung = new Oberleitung();
			if (log.isDebugEnabled())
				log.debug("Create new Oberleitung: " + regelungId);
		} else {
			oberleitung = oService.findById(mrForm.getRegelungId(), new Preload[] { new Preload(
			    Regelung.class, "massnahme") });
			if (log.isDebugEnabled())
				log.debug("Find Oberleitung by Id: " + regelungId);
		}

		if (oberleitung == null) {
			addError("error.notfound", msgRes.getMessage("oberleitung"));
			return mapping.findForward("FAILURE");
		}

		// Beziehung zu Massnahme wird gesetzt
		oberleitung.setMassnahme(massnahme);

		// Fuellen des Objektes anhand der Formulardaten
		fillRegelungByForm(mrForm, oberleitung, massnahme);
		// Sonderfelder, welche nicht in Regelung enthalten sind
		oberleitung.setSchaltgruppen(mrForm.getSchaltgruppen());
		oberleitung.setSigWeicheVon(mrForm.getSigWeicheVon());
		oberleitung.setSigWeicheBis(mrForm.getSigWeicheBis());

		// Objekt anlegen
		if (regelungId == 0) {
			if (!secUser.hasAuthorization("ROLE_OBERLEITUNG_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_OBERLEITUNG_ANLEGEN_REGIONALBEREICH"))
				return mapping.findForward("ACCESS_DENIED");
			oService.create(oberleitung);
		}
		// Objekt bearbeiten
		else {
			if (voterRegelung.vote(secUser, oberleitung, "ROLE_OBERLEITUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");
			oService.update(oberleitung);
		}

		addMessage("success.saved");
		return mapping.findForward("SUCCESS");
	}

	private void fillRegelungByForm(MassnahmeRegelungForm mrForm, Regelung regelung,
	    SAPMassnahme massnahme) {
		regelung.setUngueltig(mrForm.getUngueltig());
		regelung.setFahrplanjahr(massnahme.getFahrplanjahr());

		// Verbindung zur Massnahme wir hergestellt
		regelung.setMassnahme(massnahme);

		// VZG-Strecke setzen
		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();
		Integer vzgNummer = vzgService.castCaptionToNummer(mrForm.getStrecke());
		regelung.setVzgStrecke(CollectionHelper.getFirst(vzgService.findByNummer(vzgNummer,
		    massnahme.getFahrplanjahr(), null, true, null)));

		// Liste mit VZG-Streckennummern zur Betriebsstellensuche erstellen
		Set<Integer> streckenNummern = new HashSet<Integer>();
		streckenNummern.add(vzgNummer);

		// Betriebstellen setzen, Validierung ueber Fahrplanjahr und Zeitraum ist bereits erfolgt
		BetriebsstelleService bsService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		regelung.setBstVon(bsService.findById(mrForm.getBetriebsstelleVonId()));
		regelung.setBstBis(bsService.findById(mrForm.getBetriebsstelleBisId()));

		// Zeitraum setzen
		regelung.setZeitVon(FrontendHelper.castStringToDate(mrForm.getBeginn() + ":00.0"));
		regelung.setZeitBis(FrontendHelper.castStringToDate(mrForm.getEnde() + ":00.0"));

		// Allgemein
		regelung.setDurchgehend(mrForm.getDurchgehend());
		regelung.setSchichtweise(mrForm.getSchichtweise());
		regelung.setBetroffenSpfv(mrForm.getBetroffenSpfv());
		regelung.setBetroffenSpnv(mrForm.getBetroffenSpnv());
		regelung.setBetroffenSgv(mrForm.getBetroffenSgv());

		regelung.setKommentar(FrontendHelper.getNullOrTrimmed(mrForm.getKommentar()));

		// Verkehrstageschluessel wird gesetzt
		Integer vts = VtrHelper.getVtsByDays(mrForm.isWtsMo(), mrForm.isWtsDi(), mrForm.isWtsMi(),
		    mrForm.isWtsDo(), mrForm.isWtsFr(), mrForm.isWtsSa(), mrForm.isWtsSo());

		VerkehrstageregelungService vtrService = EasyServiceFactory.getInstance()
		    .createVerkehrstageregelungService();
		regelung.setVtr(vtrService.findByVtsWithoutDuplicates(vts));

		// LastChange setzen
		regelung.setLastChange(null);
	}

	private SAPMassnahme initializeMassnahme(Integer massnahmeId, TqmUser secUser) {
		if (massnahmeId != null) {
			if (log.isDebugEnabled())
				log.debug("Find SAPMassnahme by Id: " + massnahmeId);
			SAPMassnahme massnahme = serviceFactory.createSAPMassnahmeService().findById(
			    massnahmeId);
			if (massnahme != null) {
				if (serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
				    "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
					addError("error.access.denied");
					return null;
				}
				if (log.isDebugEnabled())
					log.debug("Processing SAPMassnahme: " + massnahmeId);
				return massnahme;
			}
		}
		addError("error.sperrpausenbedarf.notfound");
		return null;
	}

}