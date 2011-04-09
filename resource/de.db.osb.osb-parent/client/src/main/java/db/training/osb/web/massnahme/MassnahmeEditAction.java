package db.training.osb.web.massnahme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import db.training.bob.util.CollectionHelper;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Anmelder;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.babett.StatusBbzr;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.TopProjektService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.hibernate.TqmUser;

public class MassnahmeEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(MassnahmeEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("entering MassnahmeEditAction");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		SAPMassnahme massnahme = null;
		MassnahmeForm massnahmeForm = (MassnahmeForm) form;
		Integer massnahmeId = massnahmeForm.getMassnahmeId();

		if (massnahmeId == null) {
			if (log.isDebugEnabled())
				log.debug("No massnahmeId given. Setting \"0\"");
			massnahmeId = 0;
		}
		if (log.isDebugEnabled())
			log.debug("Processing massnahmeId: " + massnahmeId);

		// Bearbeitung eines Datensatzes. Formular fuellen
		// Anlegen
		if (massnahmeId.equals(0)) {
			// Rechtepruefung: SAPMassnahme anlegen
			if (!secUser.hasAuthorization("ROLE_MASSNAHME_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_MASSNAHME_ANLEGEN_REGIONALBEREICH"))
				return mapping.findForward("ACCESS_DENIED");

			if (!hasErrors(request)) {
				massnahmeForm.reset();
				massnahmeForm.setEntwurf(true);
				massnahmeForm.setWtsMo(true);
				massnahmeForm.setWtsDi(true);
				massnahmeForm.setWtsMi(true);
				massnahmeForm.setWtsDo(true);
				massnahmeForm.setWtsFr(true);
				massnahmeForm.setWtsSa(true);
				massnahmeForm.setWtsSo(true);

				massnahmeForm.setMassnahmeId(massnahmeId);
				massnahmeForm.setRegionalbereichId(loginUser.getRegionalbereich().getId());
			}
		}
		// Bearbeiten
		else {
			// Laden der Massnahme aus Datenbank
			massnahme = EasyServiceFactory.getInstance().createSAPMassnahmeService().findById(
			    massnahmeForm.getMassnahmeId(),
			    new Preload[] { new Preload(VzgStrecke.class, "betriebsstellen"),
			            new Preload(SAPMassnahme.class, "topProjekte") });

			if (massnahme == null) {
				if (log.isDebugEnabled())
					log.debug("SAPMassnahme not found: " + massnahmeForm.getMassnahmeId());
				addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing SAPMassnahme: " + massnahme);

			// Rechtepruefung: SAPMassnahme bearbeiten
			if (serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
			    "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED)
				return mapping.findForward("ACCESS_DENIED");

			// Fuellen des Forms
			if (!hasErrors(request)) {
				massnahmeForm.reset();

				massnahmeForm.setMassnahmeId(massnahmeId);
				if (massnahme.getRegionalbereich() != null)
					massnahmeForm.setRegionalbereichId(massnahme.getRegionalbereich().getId());
				else
					massnahmeForm.setRegionalbereichId(loginUser.getRegionalbereich().getId());
				fill(massnahmeForm, massnahme, sessionFahrplanjahr);

				// Entwurf setzen und Checkbox ggf. deaktivieren
				if (massnahme.getEntwurf() != null && massnahme.getEntwurf() == true)
					massnahmeForm.setEntwurf(massnahme.getEntwurf());
				else {
					massnahmeForm.setEntwurf(false);
					request.setAttribute("entwurfDisabled", true);
				}
				// Bei genehmigter Massnahme muss Checkbox Studie deaktiviert werden
				if (massnahme.getGenehmiger() != null)
					request.setAttribute("studieDisabled", true);
			}

			request.setAttribute("massnahme", massnahme);
			request.setAttribute("activeTab", "editMassnahme");
		}

		request.setAttribute("finanztypList", serviceFactory.createFinanztypService()
		    .findAllValid());
		request.setAttribute("anmelderList", serviceFactory.createAnmelderService()
		    .findByRegionalbereich(loginUser.getRegionalbereich(), true,
		        new Preload[] { new Preload(Anmelder.class, "user") }));
		request.setAttribute("arbeitstypList", serviceFactory.createArbeitstypService()
		    .findAllValid());
		request.setAttribute("vtrList", serviceFactory.createVerkehrstageregelungService()
		    .findAllValid());
		request.setAttribute("phaseList", serviceFactory.createPhaseService().findAllValid());
		request.setAttribute("folgeNichtausfuehrungList", serviceFactory
		    .createFolgeNichtausfuehrungService().findAllValid());
		request.setAttribute("grossmaschineList", serviceFactory.createGrossmaschineService()
		    .findAllValid());
		request.setAttribute("statusBbzrList", StatusBbzr.values());

		// TODO: Hervorhebung

		initializeVzgStrecke(massnahmeForm, request);

		return mapping.findForward("SUCCESS");
	}

	private void fill(MassnahmeForm form, SAPMassnahme mn, Integer fahrplanjahr) {
		form.setStudie(mn.getStudie());

		if (mn.getFinanztyp() != null)
			form.setFinanztypId(mn.getFinanztyp().getId());

		if (mn.getPhase() != null)
			form.setPhaseId(mn.getPhase().getId());

		form.setBbei(mn.getBbEi());

		if (mn.getHauptStrecke() != null)
			form.setStrecke(mn.getHauptStrecke().getCaption());
		if (mn.getBetriebsstelleVon() != null)
			form.setBetriebsstelleVonId(mn.getBetriebsstelleVon().getId());
		if (mn.getBetriebsstelleBis() != null)
			form.setBetriebsstelleBisId(mn.getBetriebsstelleBis().getId());

		form.setRichtungskennzahl(mn.getRichtungsKennzahl());

		form.setKmVon(FrontendHelper.castDoubleToString(mn.getKmVon()));
		form.setKmBis(FrontendHelper.castDoubleToString(mn.getKmBis()));

		form.setBeginn(FrontendHelper.castDateToString(mn.getBauterminStart(), "dd.MM.yyyy hh:mm"));
		form.setEnde(FrontendHelper.castDateToString(mn.getBauterminEnde(), "dd.MM.yyyy hh:mm"));

		form.setDurchgehend(mn.getDurchgehend());
		form.setSchichtweise(mn.getSchichtweise());

		if (mn.getFahrplanjahr() != null)
			form.setFahrplanjahr(mn.getFahrplanjahr().toString());

		if (mn.getVtr() != null) {
			Integer vts = mn.getVtr().getVts();

			form.setWtsMo(VtrHelper.isDayPartOfVts(vts, Calendar.MONDAY));
			form.setWtsDi(VtrHelper.isDayPartOfVts(vts, Calendar.TUESDAY));
			form.setWtsMi(VtrHelper.isDayPartOfVts(vts, Calendar.WEDNESDAY));
			form.setWtsDo(VtrHelper.isDayPartOfVts(vts, Calendar.THURSDAY));
			form.setWtsFr(VtrHelper.isDayPartOfVts(vts, Calendar.FRIDAY));
			form.setWtsSa(VtrHelper.isDayPartOfVts(vts, Calendar.SATURDAY));
			form.setWtsSo(VtrHelper.isDayPartOfVts(vts, Calendar.SUNDAY));
		}

		if (mn.getTopProjekte() != null && mn.getTopProjekte().size() > 0)
			form.setTopprojektId(mn.getTopProjekte().iterator().next().getId());

		form.setTechnischerPlatz(mn.getTechnischerPlatz());

		if (mn.getAnmelder() != null)
			form.setAnmelderId(mn.getAnmelder().getId());

		form.setDatumAnmeldung(FrontendHelper.castDateToString(mn.getLetzteAenderungAnmeldung()));
		form.setKommentarAnmelder(mn.getKommentar());
		form.setErgaenzungAnmelder(mn.getAnmelderErgaenzung());

		if (mn.getArbeiten() != null)
			form.setArbeitenId(mn.getArbeiten().getId());

		if (mn.getArbeitenBetriebsstelle() != null)
			form.setArbeitenOrtId(mn.getArbeitenBetriebsstelle().getId());

		form.setArbeitenKommentar(mn.getArbeitenKommentar());

		if (mn.getFolgeNichtausfuehrung() != null)
			form.setFolgeNichtausfuehrungId(mn.getFolgeNichtausfuehrung().getId());

		form.setFolgeNichtausfuehrungBeginn(FrontendHelper.castDateToString(mn
		    .getFolgeNichtausfuehrungBeginn()));
		form.setFolgeNichtausfuehrungFzv(FrontendHelper.castFloatToString(mn
		    .getFolgeNichtausfuehrungFzv(), "##0.0"));
		form.setFolgeNichtausfuehrungGeschwindigkeitLa(FrontendHelper.castIntegerToString(mn
		    .getFolgeNichtausfuehrungLaGeschwindigkeit()));

		if (mn.getGrossmaschine() != null)
			form.setGrossmaschineId(mn.getGrossmaschine().getId());

		if (mn.getStatusBbzr() != null)
			form.setStatusBbzr(mn.getStatusBbzr().name());

		// TODO: Hervorhebung

		form.setLaEintrag406(mn.getLaEintragR406());
		form.setLueHinweis(mn.getLueHinweis());

		if (mn.getVorbedingung() != null)
			form.setVorbedingungId(mn.getVorbedingung().getId());

		if (mn.getUnterDeckung() != null)
			form.setUnterdeckungId(mn.getUnterDeckung().getId());

		if (mn.getAnkermassnahme() != null)
			form.setAnkermassnahmeId(mn.getAnkermassnahme().getId());
	}

	private void initializeVzgStrecke(MassnahmeForm form, HttpServletRequest request) {
		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(form.getStrecke());
		VzgStrecke vzgStrecke = null;

		Date beginn = FrontendHelper.castStringToDate(form.getBeginn());
		Date ende = FrontendHelper.castStringToDate(form.getEnde());

		List<Betriebsstelle> bsts = new ArrayList<Betriebsstelle>();
		List<Betriebsstelle> bstsArbeiten = new ArrayList<Betriebsstelle>();
		List<TopProjekt> topProjekte = new ArrayList<TopProjekt>();

		if (vzgStreckeNummer != null) {
			// VzgStrecke
			if (beginn != null && ende != null) {
				vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
				    beginn, ende, null, true, null));
			} else {
				vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
				    sessionFahrplanjahr, null, true, null));
			}

			if (vzgStrecke != null) {
				BetriebsstelleService bsService = EasyServiceFactory.getInstance()
				    .createBetriebsstelleService();
				if (beginn == null && ende == null) {
					bsts = bsService.findByVzgStreckeNummerAndFahrplanjahr(vzgStrecke.getNummer(),
					    sessionFahrplanjahr, true, null);
					bstsArbeiten = bsService.findByVzgStreckeNummerAndFahrplanjahr(vzgStrecke
					    .getNummer(), sessionFahrplanjahr, false, null);
				} else {
					bsts = bsService.findByVzgStreckeNummerAndGueltigkeit(vzgStrecke.getNummer(),
					    beginn, ende, true, null);
					bstsArbeiten = bsService.findByVzgStreckeNummerAndGueltigkeit(vzgStrecke
					    .getNummer(), beginn, ende, false, null);
				}

				// Topprojekte
				TopProjektService tpService = EasyServiceFactory.getInstance()
				    .createTopProjektService();
				topProjekte = tpService.findByVzgStrecke(vzgStrecke, null);
			}
		}
		request.setAttribute("betriebsstellen", bsts);
		request.setAttribute("betriebsstellenArbeiten", bstsArbeiten);
		request.setAttribute("topProjekte", topProjekte);
	}
}