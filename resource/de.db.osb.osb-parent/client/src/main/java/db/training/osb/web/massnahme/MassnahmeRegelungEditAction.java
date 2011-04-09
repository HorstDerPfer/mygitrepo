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
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseDispatchAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.model.babett.LaTyp;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.util.VtrHelper;
import db.training.security.hibernate.TqmUser;

public class MassnahmeRegelungEditAction extends BaseDispatchAction {
	private static final Logger log = Logger.getLogger(MassnahmeRegelungEditAction.class);

	/* GLEISSPERRUNG */
	public ActionForward gleissperrung(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungEditAction->gleissperrung.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), new Preload[] {
		        new Preload(SAPMassnahme.class, "gleissperrungen"),
		        new Preload(Gleissperrung.class, "betriebsweise"),
		        new Preload(Regelung.class, "vzgStrecke"), new Preload(Regelung.class, "vtr"),
		        new Preload(VzgStrecke.class, "betriebsstellen"), }, request, secUser);

		if (massnahme == null)
			return mapping.findForward("FAILURE");

		// Bearbeitung eines Datensatzes. Formular fuellen
		if (mrForm.getRegelungId() != null) {
			Integer regelungId = mrForm.getRegelungId();

			// Anlegen
			if (regelungId.equals(0)) {
				if (!hasErrors(request))
					mrForm.reset();

				mrForm.setMassnahmeId(massnahme.getId());
				mrForm.setRegelungId(regelungId);
				mrForm.enableAllWtsDays();
			}
			// Bearbeiten
			else {
				for (Gleissperrung gleissperrung : massnahme.getGleissperrungen()) {
					if (gleissperrung.getId().equals(regelungId)) {
						if (!hasErrors(request)) {
							mrForm.reset();

							fillForm(mrForm, gleissperrung, sessionFahrplanjahr);

							if (gleissperrung.getBetriebsweise() != null)
								mrForm.setBetriebsweiseId(gleissperrung.getBetriebsweise().getId());
							mrForm.setErgaenzungBetriebsweise(gleissperrung
							    .getErgaenzungBetriebsweise());
							mrForm.setRichtungskennzahl(gleissperrung.getRichtungsKennzahl());
							mrForm.setKmVon(FrontendHelper.castFloatToString(gleissperrung
							    .getKmVon()));
							mrForm.setKmBis(FrontendHelper.castFloatToString(gleissperrung
							    .getKmBis()));
							mrForm.setFzvMusterzug(FrontendHelper.castFloatToString(gleissperrung
							    .getFzvMusterzug()));
							mrForm.setGeschwindigkeitVzg(FrontendHelper
							    .castIntegerToString(gleissperrung.getGeschwindigkeitVzg()));
							mrForm.setVorschlagLisba(gleissperrung.getVorschlagLisba());
						}
						mrForm.setMassnahmeId(massnahme.getId());
						mrForm.setRegelungId(regelungId);
					}
				}
			}
			if (log.isDebugEnabled())
				log.debug("Processing Gleissperrung: " + regelungId);
			request.setAttribute("regelungId", regelungId);
		}

		initializeVzgStrecke(mrForm, request);
		request.setAttribute("massnahme", massnahme);
		request.setAttribute("activeTab", "editMassnahmeGleissperrung");
		mrForm.setMethod(MassnahmeRegelungForm.GLEISSPERRUNG_METHOD);

		request.setAttribute("betriebsweiseList", serviceFactory.createBetriebsweiseService()
		    .findAllValid());

		return mapping.findForward("SUCCESS");
	}

	/* LANGSAMFAHRSTELLE */
	public ActionForward langsamfahrstelle(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungEditAction->langsamfahrstelle.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;

		// massnahme -> regelung -> vzgStrecke -> betriebstellen

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), new Preload[] {
		        new Preload(SAPMassnahme.class, "langsamfahrstellen"),
		        new Preload(Regelung.class, "vzgStrecke"), new Preload(Regelung.class, "vtr"),
		        new Preload(VzgStrecke.class, "betriebsstellen") }, request, secUser);

		if (massnahme == null)
			return mapping.findForward("FAILURE");

		// Bearbeitung eines Datensatzes. Formular fuellen
		if (mrForm.getRegelungId() != null) {
			Integer regelungId = mrForm.getRegelungId();

			// Anlegen
			if (regelungId.equals(0)) {
				if (!hasErrors(request))
					mrForm.reset();

				mrForm.setMassnahmeId(massnahme.getId());
				mrForm.setRegelungId(regelungId);
				mrForm.enableAllWtsDays();
			}
			// Bearbeiten
			else {
				for (Langsamfahrstelle lfs : massnahme.getLangsamfahrstellen()) {
					if (lfs.getId().equals(regelungId)) {
						if (!hasErrors(request)) {
							mrForm.reset();

							fillForm(mrForm, lfs, sessionFahrplanjahr);

							mrForm.setKmVon(FrontendHelper.castFloatToString(lfs.getKmVon()));
							mrForm.setKmBis(FrontendHelper.castFloatToString(lfs.getKmBis()));
							mrForm.setFzvMusterzug(FrontendHelper.castFloatToString(lfs
							    .getFzvMusterzug()));
							mrForm.setGeschwindigkeitVzg(FrontendHelper.castIntegerToString(lfs
							    .getGeschwindigkeitVzg()));
							mrForm.setGeschwindigkeitLa(FrontendHelper.castIntegerToString(lfs
							    .getGeschwindigkeitLa()));
							mrForm.setLaTyp(lfs.getLaTyp().toString());
						}
						mrForm.setMassnahmeId(massnahme.getId());
						mrForm.setRegelungId(regelungId);
					}
				}
			}
			if (log.isDebugEnabled())
				log.debug("Processing Langsamfahrstelle: " + regelungId);
			request.setAttribute("regelungId", regelungId);
		}

		initializeVzgStrecke(mrForm, request);
		request.setAttribute("massnahme", massnahme);
		request.setAttribute("activeTab", "editMassnahmeLangsamfahrstelle");
		mrForm.setMethod(MassnahmeRegelungForm.LANGSAMFAHRSTELLE_METHOD);

		// LaTypen werden in Liste geschrieben
		MessageResources msgRes = getResources(request);
		List<LabelValueBean> laTypList = new ArrayList<LabelValueBean>();
		for (LaTyp laTyp : LaTyp.values()) {
			laTypList.add(new LabelValueBean(msgRes.getMessage("langsamfahrstelle.laTyp."
			    + laTyp.name()), laTyp.name()));
		}
		request.setAttribute("laTypList", laTypList);

		return mapping.findForward("SUCCESS");
	}

	/* OBERLEITUNG */
	public ActionForward oberleitung(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungEditAction->oberleitung.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), new Preload[] {
		        new Preload(SAPMassnahme.class, "oberleitungen"),
		        new Preload(Regelung.class, "vzgStrecke"), new Preload(Regelung.class, "vtr"),
		        new Preload(VzgStrecke.class, "betriebsstellen") }, request, secUser);

		if (massnahme == null)
			return mapping.findForward("FAILURE");

		// Bearbeitung eines Datensatzes. Formular fuellen
		if (mrForm.getRegelungId() != null) {
			Integer regelungId = mrForm.getRegelungId();

			// Anlegen
			if (regelungId.equals(0)) {
				if (!hasErrors(request))
					mrForm.reset();

				mrForm.setMassnahmeId(massnahme.getId());
				mrForm.setRegelungId(regelungId);
				mrForm.enableAllWtsDays();
			}
			// Bearbeiten
			else {
				for (Oberleitung oberleitung : massnahme.getOberleitungen()) {
					if (oberleitung.getId().equals(regelungId)) {
						if (!hasErrors(request)) {
							mrForm.reset();

							fillForm(mrForm, oberleitung, sessionFahrplanjahr);

							mrForm.setSchaltgruppen(oberleitung.getSchaltgruppen());
							mrForm.setSigWeicheVon(oberleitung.getSigWeicheVon());
							mrForm.setSigWeicheBis(oberleitung.getSigWeicheBis());
						}
						mrForm.setMassnahmeId(massnahme.getId());
						mrForm.setRegelungId(regelungId);
					}
				}
			}
			if (log.isDebugEnabled())
				log.debug("Processing Oberleitung: " + regelungId);
			request.setAttribute("regelungId", regelungId);
		}

		initializeVzgStrecke(mrForm, request);
		request.setAttribute("massnahme", massnahme);
		request.setAttribute("activeTab", "editMassnahmeOberleitung");
		mrForm.setMethod(MassnahmeRegelungForm.OBERLEITUNG_METHOD);

		return mapping.findForward("SUCCESS");
	}

	private void fillForm(MassnahmeRegelungForm form, Regelung regelung, Integer fahrplanjahr) {
		form.setRegelungId(regelung.getId());
		form.setUngueltig(regelung.getUngueltig());
		if (regelung.getVzgStrecke() != null)
			form.setStrecke(regelung.getVzgStrecke().getCaption());
		if (regelung.getBstVon() != null)
			form.setBetriebsstelleVonId(regelung.getBstVon().getId());
		if (regelung.getBstBis() != null)
			form.setBetriebsstelleBisId(regelung.getBstBis().getId());
		form.setBeginn(FrontendHelper.castDateToString(regelung.getZeitVon(), "dd.MM.yyyy HH:mm"));
		form.setEnde(FrontendHelper.castDateToString(regelung.getZeitBis(), "dd.MM.yyyy HH:mm"));
		form.setDurchgehend(regelung.getDurchgehend());
		form.setSchichtweise(regelung.getSchichtweise());
		form.setBetroffenSpfv(regelung.getBetroffenSpfv());
		form.setBetroffenSpnv(regelung.getBetroffenSpnv());
		form.setBetroffenSgv(regelung.getBetroffenSgv());
		form.setKommentar(regelung.getKommentar());
		if (regelung.getVtr() != null) {
			Integer vts = regelung.getVtr().getVts();

			form.setWtsMo(VtrHelper.isDayPartOfVts(vts, Calendar.MONDAY));
			form.setWtsDi(VtrHelper.isDayPartOfVts(vts, Calendar.TUESDAY));
			form.setWtsMi(VtrHelper.isDayPartOfVts(vts, Calendar.WEDNESDAY));
			form.setWtsDo(VtrHelper.isDayPartOfVts(vts, Calendar.THURSDAY));
			form.setWtsFr(VtrHelper.isDayPartOfVts(vts, Calendar.FRIDAY));
			form.setWtsSa(VtrHelper.isDayPartOfVts(vts, Calendar.SATURDAY));
			form.setWtsSo(VtrHelper.isDayPartOfVts(vts, Calendar.SUNDAY));
		}
	}

	private SAPMassnahme initializeMassnahme(Integer massnahmeId, Preload[] preloads,
	    HttpServletRequest request, TqmUser secUser) {
		if (massnahmeId != null) {
			SAPMassnahme massnahme = serviceFactory.createSAPMassnahmeService().findById(
			    massnahmeId, preloads);

			if (massnahme != null) {
				if (log.isDebugEnabled())
					log.debug("Processing SAPMassnahme: " + massnahmeId);
				if (serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
				    "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
					addError("error.access.denied");
					return null;
				}
				return massnahme;
			}
		}
		MessageResources msgRes = getResources(request);
		addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
		return null;
	}

	private void initializeVzgStrecke(MassnahmeRegelungForm form, HttpServletRequest request) {
		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();
		Integer vzgStreckeNummer = vzgService.castCaptionToNummer(form.getStrecke());

		Date beginn = FrontendHelper.castStringToDate(form.getBeginn());
		Date ende = FrontendHelper.castStringToDate(form.getEnde());

		List<Betriebsstelle> betriebsstellen = new ArrayList<Betriebsstelle>();

		if (vzgStreckeNummer != null) {
			BetriebsstelleService bsService = EasyServiceFactory.getInstance()
			    .createBetriebsstelleService();
			if (beginn == null && ende == null) {
				betriebsstellen = bsService.findByVzgStreckeNummerAndFahrplanjahr(vzgStreckeNummer,
				    sessionFahrplanjahr, true, null);
			} else {
				betriebsstellen = bsService.findByVzgStreckeNummerAndGueltigkeit(vzgStreckeNummer,
				    beginn, ende, true, null);
			}
		}
		request.setAttribute("betriebsstellen", betriebsstellen);
	}
}