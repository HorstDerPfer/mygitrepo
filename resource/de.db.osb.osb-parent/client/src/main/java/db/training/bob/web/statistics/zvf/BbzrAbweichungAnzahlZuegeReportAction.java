package db.training.bob.web.statistics.zvf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;
import org.hibernate.QueryException;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.SearchBean;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.ZugService;
import db.training.bob.web.statistics.BaseSearchAction;
import db.training.bob.web.statistics.StatisticsFilterForm;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.osb.util.ConfigResources;

public class BbzrAbweichungAnzahlZuegeReportAction extends BaseSearchAction {

	private static long maxVerkehrstagZeitraum;

	private static long maxControllingZeitraum;

	private static final Logger log = Logger.getLogger(BbzrAbweichungAnzahlZuegeReportAction.class);

	private class Report {
		private List<BbzrAbweichungAnzahlZuegeReportBean> resultEVU;

		private List<BbzrAbweichungAnzahlZuegeReportBean> resultGesamt;

		public Report() {
			resultGesamt = new ArrayList<BbzrAbweichungAnzahlZuegeReportBean>();
			resultEVU = new ArrayList<BbzrAbweichungAnzahlZuegeReportBean>();
		}

		public List<BbzrAbweichungAnzahlZuegeReportBean> getResultEVU() {
			return resultEVU;
		}

		public List<BbzrAbweichungAnzahlZuegeReportBean> getResultGesamt() {
			return resultGesamt;
		}
	}

	static {
		maxVerkehrstagZeitraum = ConfigResources.getInstance().getMaxZeitraumVerkehrstag();
		maxControllingZeitraum = ConfigResources.getInstance().getMaxZeitraumControlling();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		ActionForward baseSearchActionResult = super.run(mapping, form, request, response);
		if (baseSearchActionResult.getName().equals("FAILURE")) {
			return baseSearchActionResult;
		}

		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true"))
			return baseSearchActionResult;

		if (log.isDebugEnabled())
			log.debug("Entering BbzrAbweichungAnzahlZuegeReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {

				// Validierung
				Calendar calZeitraumVerkehrstagVon = null;
				Calendar calZeitraumVerkehrstagBis = null;
				Calendar calControllingBeginnDatum = null;
				Calendar calControllingEndDatum = null;

				// Suchzeiträume einschränken
				if (!FrontendHelper.stringNotNullOrEmpty(searchBean
				    .getSearchVerkehrstagBeginnDatum())) {
//					addError("error.required", msgRes.getMessage("zvf.verkehrstagvon"));
				} else {
					calZeitraumVerkehrstagVon = GregorianCalendar.getInstance();
					calZeitraumVerkehrstagVon.setTime(FrontendHelper.castStringToDate(searchBean
					    .getSearchVerkehrstagBeginnDatum()));
					EasyDateFormat.setToStartOfDay(calZeitraumVerkehrstagVon);
				}

				if (!FrontendHelper.stringNotNullOrEmpty(searchBean.getSearchVerkehrstagEndDatum())) {
//					addError("error.required", msgRes.getMessage("zvf.verkehrstagbis"));
				} else {
					calZeitraumVerkehrstagBis = GregorianCalendar.getInstance();
					calZeitraumVerkehrstagBis.setTime(FrontendHelper.castStringToDate(searchBean
					    .getSearchVerkehrstagEndDatum()));
					EasyDateFormat.setToEndOfDay(calZeitraumVerkehrstagBis);
				}

				if (!FrontendHelper.stringNotNullOrEmpty(searchBean
				    .getSearchControllingBeginnDatum())) {
//					addError("error.required", msgRes.getMessage("controlling.beginndatum"));
				} else {
					calControllingBeginnDatum = GregorianCalendar.getInstance();
					calControllingBeginnDatum.setTime(FrontendHelper.castStringToDate(searchBean
					    .getSearchControllingBeginnDatum()));
					EasyDateFormat.setToStartOfDay(calControllingBeginnDatum);
				}

				if (!FrontendHelper.stringNotNullOrEmpty(searchBean.getSearchControllingEndDatum())) {
//					addError("error.required", msgRes.getMessage("controlling.enddatum"));
				} else {
					calControllingEndDatum = GregorianCalendar.getInstance();
					calControllingEndDatum.setTime(FrontendHelper.castStringToDate(searchBean
					    .getSearchControllingEndDatum()));
					EasyDateFormat.setToEndOfDay(calControllingEndDatum);
				}

				if (calZeitraumVerkehrstagVon != null && calZeitraumVerkehrstagBis != null) {
					if (!calZeitraumVerkehrstagVon.before(calZeitraumVerkehrstagBis)) {
						addError("error.invalid", String.format("%s; %s", msgRes
						    .getMessage("zvf.verkehrstagvon"), msgRes
						    .getMessage("zvf.verkehrstagbis")));
					}

					if (calZeitraumVerkehrstagBis.getTimeInMillis()
					    - calZeitraumVerkehrstagVon.getTimeInMillis() > maxVerkehrstagZeitraum) {
						int maxTage = (int) Math
						    .floor(maxVerkehrstagZeitraum / 1000 / 60 / 60 / 24);
//						addError("error.auswertungen.maxtimespan.exceeded", "" + maxTage);
					}
				}

				if (calControllingBeginnDatum != null && calControllingEndDatum != null) {
					if (!calControllingBeginnDatum.before(calControllingEndDatum)) {
						addError("error.invalid", String.format("%s; %s", msgRes
						    .getMessage("controlling.beginndatum"), msgRes
						    .getMessage("controlling.enddatum")));
					}

					if (calControllingEndDatum.getTimeInMillis()
					    - calControllingBeginnDatum.getTimeInMillis() > maxControllingZeitraum) {
						int maxTage = (int) Math
						    .floor(maxControllingZeitraum / 1000 / 60 / 60 / 24);
//						addError("error.auswertungen.maxtimespan.exceeded", "" + maxTage);
					}
				}

				if (!hasErrors()) {
					Integer qsks = FrontendHelper.castStringToInteger(searchBean.getQsks());

					try {
						//ZugService zugService = EasyServiceFactory.getInstance().createZugService();
						// Report report = buildReport(
						// zugService.findBbzrMassnahmenByBaumassnahmen(searchBean, null), qsks);

						
						//TODO: Beginn Alte Methode
						BaumassnahmeService baumassnahmeService = EasyServiceFactory.getInstance()
						    .createBaumassnahmeService();
						FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBZR,
						        FetchPlan.BBZR_BAUMASSNAHMEN, FetchPlan.BBZR_MN_ZUEGE,
						        FetchPlan.ZVF_MN_VERSION };
						
						Integer anzahlBaumassnahmen = baumassnahmeService.countBySearchBean(
						    searchBean, fetchPlans);

						if (anzahlBaumassnahmen > 1000)
							throw new QueryException(
							    "zu viele Baunassnahmen. Anzahl Baumassnahmen: "
							        + anzahlBaumassnahmen);
						
						List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
						    fetchPlans);
						Report report = buildReport(bbps, calZeitraumVerkehrstagVon.getTime(),
						    calZeitraumVerkehrstagBis.getTime(), qsks);
						//TODO: Ende Alte Methode
						
						
						request.setAttribute("reportBeanGesamt", report.getResultGesamt());
						request.setAttribute("reportBeanEVU", report.getResultEVU());
					} catch (QueryException ex) {
						if (log.isDebugEnabled())
							log.debug(ex);

						addError("error.auswertungen.toomuchresults");
					}
				}
			}

		} catch (LazyInitializationException e) {
			if (log.isDebugEnabled())
				log.debug(e.getMessage());
			if (log.isDebugEnabled())
				log.debug(e);
			return mapping.findForward("FAILURE");
		}

		return mapping.findForward("SUCCESS");
	}

	public ActionForward testHelp(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		return run(mapping, form, request, response);
	}

	private Report buildReport(Set<Massnahme> massnahmen, Integer qsks) {
		// Auswertung über mehrere Baumaßnahmen
		Report report = new Report();

		HashMap<String, BbzrAbweichungAnzahlZuegeReportBean> mapEVU = new HashMap<String, BbzrAbweichungAnzahlZuegeReportBean>();

		BbzrAbweichungAnzahlZuegeReportBean rbFv = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Fernverkehr");
		BbzrAbweichungAnzahlZuegeReportBean rbNv = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Nahverkehr");
		BbzrAbweichungAnzahlZuegeReportBean rbGv = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Güterverkehr");
		BbzrAbweichungAnzahlZuegeReportBean rbGesamt = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Gesamt");

		String evu;
		BbzrAbweichungAnzahlZuegeReportBean rbEVU;
		if (log.isDebugEnabled())
			log.debug("Anzahl Massnahmen gefunden: " + massnahmen.size());
		for (Massnahme mn : massnahmen) {
			if (log.isDebugEnabled())
				log.debug("verarbeite MN: " + mn.getId() + "  Anzahl Züge: " + mn.getZug().size());

			for (Zug z : mn.getZug()) {

				if (z == null)
					continue;

				if (qsks != null && z.getQs_ks() != null) {
					if (qsks != -1) {// qs, ks oder nichtaktiv ausgewählt
						if (!qsks.equals(z.getQs_ks()))
							continue;
					}
				}

				Abweichungsart abweichungsart = z.getAbweichung().getArt();

				// Gesamt
				rbGesamt.increment(abweichungsart);

				// FV, NV, GV
				if (mn.getFestgelegtSPFV() == true) {
					rbFv.increment(abweichungsart);
				} else if (mn.getFestgelegtSPNV() == true) {
					rbNv.increment(abweichungsart);
				} else if (mn.getFestgelegtSGV() == true) {
					rbGv.increment(abweichungsart);
				}

				// EVU
				evu = z.getBetreiber();
				if (evu != null) {
					if (!evu.equals("")) {
						rbEVU = mapEVU.get(evu);
						if (rbEVU == null) { // EVU noch nicht in der Liste
							rbEVU = new BbzrAbweichungAnzahlZuegeReportBean(evu);
							rbEVU.increment(abweichungsart);
							mapEVU.put(evu, rbEVU);
						} else { // EVU in Liste vorhanden
							rbEVU.increment(abweichungsart);
							mapEVU.put(evu, rbEVU);
						}
					}
				}
			}
		}
		List<BbzrAbweichungAnzahlZuegeReportBean> resultGesamt = report.getResultGesamt();
		resultGesamt.add(rbGesamt);
		resultGesamt.add(rbFv);
		resultGesamt.add(rbNv);
		resultGesamt.add(rbGv);
		for (BbzrAbweichungAnzahlZuegeReportBean rb : mapEVU.values()) {
			report.getResultEVU().add(rb);
		}

		return report;
	}

	//TODO: Alte Methode
	private Report buildReport(List<Baumassnahme> bbps, Date zeitraumVerkehrstagVon,
	    Date zeitraumVerkehrstagBis, Integer qsks) {

		// Auswertung über mehrere Baumaßnahmen
		Report report = new Report();

		HashMap<String, BbzrAbweichungAnzahlZuegeReportBean> mapEVU = new HashMap<String, BbzrAbweichungAnzahlZuegeReportBean>();

		BbzrAbweichungAnzahlZuegeReportBean rbFv = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Fernverkehr");
		BbzrAbweichungAnzahlZuegeReportBean rbNv = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Nahverkehr");
		BbzrAbweichungAnzahlZuegeReportBean rbGv = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Güterverkehr");
		BbzrAbweichungAnzahlZuegeReportBean rbGesamt = new BbzrAbweichungAnzahlZuegeReportBean(
		    "Gesamt");

		for (Baumassnahme bm : bbps) {
			if (bm.getZvf() != null) {

				Massnahme m = null;
				try {
					m = bm.getAktuelleZvf().getMassnahmen().iterator().next();
				} catch (Exception e) {
					continue;
				}

				List<Zug> zuege = m.getZug();
				String evu;
				BbzrAbweichungAnzahlZuegeReportBean rbEVU;
				for (Zug z : zuege) {
					// Filter
					if (z.getVerkehrstag() == null)
						continue;
					if (zeitraumVerkehrstagVon != null)
						if (z.getVerkehrstag().before(zeitraumVerkehrstagVon))
							continue;
					if (zeitraumVerkehrstagBis != null)
						if (z.getVerkehrstag().after(zeitraumVerkehrstagBis))
							continue;
					if (qsks != null & z.getQs_ks() != null) {
						if (qsks != -1) {// qs, ks oder nichtaktiv ausgewählt
							if (!qsks.equals(z.getQs_ks()))
								continue;
						}
					}

					if (z.getAbweichung().getArt() == null)
						continue;
					Abweichungsart abweichungsart = z.getAbweichung().getArt();

					// Gesamt
					rbGesamt.increment(abweichungsart);

					// FV, NV, GV
					if (m.getFestgelegtSPFV() == true) {
						rbFv.increment(abweichungsart);
					} else if (m.getFestgelegtSPNV() == true) {
						rbNv.increment(abweichungsart);
					} else if (m.getFestgelegtSGV() == true) {
						rbGv.increment(abweichungsart);
					}

					// EVU
					evu = z.getBetreiber();
					if (evu != null) {
						if (!evu.equals("")) {
							rbEVU = mapEVU.get(evu);
							if (rbEVU == null) { // EVU noch nicht in der Liste
								rbEVU = new BbzrAbweichungAnzahlZuegeReportBean(evu);
								rbEVU.increment(abweichungsart);
								mapEVU.put(evu, rbEVU);
							} else { // EVU in Liste vorhanden
								rbEVU.increment(abweichungsart);
								mapEVU.put(evu, rbEVU);
							}
						}
					}
				}
			}
		}

		List<BbzrAbweichungAnzahlZuegeReportBean> resultGesamt = report.getResultGesamt();
		resultGesamt.add(rbGesamt);
		resultGesamt.add(rbFv);
		resultGesamt.add(rbNv);
		resultGesamt.add(rbGv);
		for (BbzrAbweichungAnzahlZuegeReportBean rb : mapEVU.values()) {
			report.getResultEVU().add(rb);
		}

		return report;
	}
}
