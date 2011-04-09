package db.training.bob.web.statistics.zvf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

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
import db.training.bob.service.zvf.ZugService;
import db.training.bob.web.statistics.BaseSearchAction;
import db.training.bob.web.statistics.StatisticsFilterForm;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.osb.util.ConfigResources;

public class BbzrAbweichungZuegeNummernReportAction extends BaseSearchAction {

	private static long maxVerkehrstagZeitraum;

	private static long maxControllingZeitraum;

	private static final Logger log = Logger
	    .getLogger(BbzrAbweichungZuegeNummernReportAction.class);

	private class Report {
		private List<BbzrAbweichungZuegeNummernReportBean> resultEVU;

		private List<BbzrAbweichungZuegeNummernReportBean> resultGesamt;

		public Report() {
			resultGesamt = new ArrayList<BbzrAbweichungZuegeNummernReportBean>();
			resultEVU = new ArrayList<BbzrAbweichungZuegeNummernReportBean>();
		}

		public List<BbzrAbweichungZuegeNummernReportBean> getResultEVU() {
			return resultEVU;
		}

		public List<BbzrAbweichungZuegeNummernReportBean> getResultGesamt() {
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
			log.debug("Entering BbzrAbweichungZuegeNummernReportAction.");

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
						addError(
						    "error.invalid",
						    String.format("%s; %s", msgRes.getMessage("zvf.verkehrstagvon"),
						        msgRes.getMessage("zvf.verkehrstagbis")));
					}

					if (calZeitraumVerkehrstagBis.getTimeInMillis()
					    - calZeitraumVerkehrstagVon.getTimeInMillis() > maxVerkehrstagZeitraum) {
						int maxTage = (int) Math
						    .floor(maxVerkehrstagZeitraum / 1000 / 60 / 60 / 24);
						addError("error.auswertungen.maxtimespan.exceeded", "" + maxTage);
					}
				}

				if (calControllingBeginnDatum != null && calControllingEndDatum != null) {
					if (!calControllingBeginnDatum.before(calControllingEndDatum)) {
						addError("error.invalid", String.format("%s; %s",
						    msgRes.getMessage("controlling.beginndatum"),
						    msgRes.getMessage("controlling.enddatum")));
					}

					if (calControllingEndDatum.getTimeInMillis()
					    - calControllingBeginnDatum.getTimeInMillis() > maxControllingZeitraum) {
						int maxTage = (int) Math
						    .floor(maxControllingZeitraum / 1000 / 60 / 60 / 24);
						addError("error.auswertungen.maxtimespan.exceeded", "" + maxTage);
					}
				}

				if (!hasErrors()) {
					Integer qsks = FrontendHelper.castStringToInteger(searchBean.getQsks());

					try {
						// Bericht erzeugen
						ZugService zugService = EasyServiceFactory.getInstance().createZugService();
						Report report = buildReport(
						    zugService.findBbzrMassnahmenByBaumassnahmen(searchBean, null), qsks);

						// FIXME: Beginn Alte Methode
						// BaumassnahmeService baumassnahmeService =
						// EasyServiceFactory.getInstance()
						// .createBaumassnahmeService();
						// FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBZR,
						// FetchPlan.BBZR_BAUMASSNAHMEN, FetchPlan.BBZR_MN_ZUEGE,
						// FetchPlan.ZVF_MN_VERSION };
						//
						// Integer anzahlBaumassnahmen = baumassnahmeService.countBySearchBean(
						// searchBean, fetchPlans);
						//
						// if (anzahlBaumassnahmen > 1000)
						// throw new QueryException(
						// "zu viele Baunassnahmen. Anzahl Baumassnahmen: "
						// + anzahlBaumassnahmen);
						//
						// List<Baumassnahme> bbps =
						// baumassnahmeService.findBySearchBean(searchBean,
						// fetchPlans);
						//
						// Report report = buildReport(bbps, calZeitraumVerkehrstagVon.getTime(),
						// calZeitraumVerkehrstagBis.getTime(), qsks);
						// FIXME: Ende Alte Methode

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

	private Report buildReport(List<BbzrAbweichungZuegeNummernResultBean> resultBeans, Integer qsks) {
		// Auswertung über mehrere Baumaßnahmen
		Report report = new Report();

		HashMap<String, BbzrAbweichungZuegeNummernReportBean> mapEVU = new HashMap<String, BbzrAbweichungZuegeNummernReportBean>();

		BbzrAbweichungZuegeNummernReportBean rbFv = new BbzrAbweichungZuegeNummernReportBean(
		    "Fernverkehr");
		BbzrAbweichungZuegeNummernReportBean rbNv = new BbzrAbweichungZuegeNummernReportBean(
		    "Nahverkehr");
		BbzrAbweichungZuegeNummernReportBean rbGv = new BbzrAbweichungZuegeNummernReportBean(
		    "Güterverkehr");
		BbzrAbweichungZuegeNummernReportBean rbGesamt = new BbzrAbweichungZuegeNummernReportBean(
		    "Gesamt");

		String evu;
		BbzrAbweichungZuegeNummernReportBean rbEVU;
		if (log.isDebugEnabled())
			log.debug("Anzahl Ergebniszeilen gefunden: " + resultBeans.size());

		for (BbzrAbweichungZuegeNummernResultBean bean : resultBeans) {

			//
			// Gesamt
			if (bean.isAusfall()) {
				rbGesamt.addZug(Abweichungsart.AUSFALL, bean.getZugNr());
			} else if (bean.isErsatzhalte()) {
				rbGesamt.addZug(Abweichungsart.ERSATZHALTE, bean.getZugNr());
			} else if (bean.isGesperrt()) {
				rbGesamt.addZug(Abweichungsart.GESPERRT, bean.getZugNr());
			} else if (bean.isRegelung()) {
				rbGesamt.addZug(Abweichungsart.REGELUNG, bean.getZugNr());
			} else if (bean.isUmleitung()) {
				rbGesamt.addZug(Abweichungsart.UMLEITUNG, bean.getZugNr());
			} else if (bean.isVerspaetung()) {
				rbGesamt.addZug(Abweichungsart.VERSPAETUNG, bean.getZugNr());
			} else if (bean.isVorplan()) {
				rbGesamt.addZug(Abweichungsart.VORPLAN, bean.getZugNr());
			}

			//
			// SPFV
			if (bean.isFestgelegtSpfv()) {
				if (bean.isAusfall()) {
					rbFv.addZug(Abweichungsart.AUSFALL, bean.getZugNr());
				} else if (bean.isErsatzhalte()) {
					rbFv.addZug(Abweichungsart.ERSATZHALTE, bean.getZugNr());
				} else if (bean.isGesperrt()) {
					rbFv.addZug(Abweichungsart.GESPERRT, bean.getZugNr());
				} else if (bean.isRegelung()) {
					rbFv.addZug(Abweichungsart.REGELUNG, bean.getZugNr());
				} else if (bean.isUmleitung()) {
					rbFv.addZug(Abweichungsart.UMLEITUNG, bean.getZugNr());
				} else if (bean.isVerspaetung()) {
					rbFv.addZug(Abweichungsart.VERSPAETUNG, bean.getZugNr());
				} else if (bean.isVorplan()) {
					rbFv.addZug(Abweichungsart.VORPLAN, bean.getZugNr());
				}
			}

			//
			// SPNV
			if (bean.isFestgelegtSpnv()) {
				if (bean.isAusfall()) {
					rbNv.addZug(Abweichungsart.AUSFALL, bean.getZugNr());
				} else if (bean.isErsatzhalte()) {
					rbNv.addZug(Abweichungsart.ERSATZHALTE, bean.getZugNr());
				} else if (bean.isGesperrt()) {
					rbNv.addZug(Abweichungsart.GESPERRT, bean.getZugNr());
				} else if (bean.isRegelung()) {
					rbNv.addZug(Abweichungsart.REGELUNG, bean.getZugNr());
				} else if (bean.isUmleitung()) {
					rbNv.addZug(Abweichungsart.UMLEITUNG, bean.getZugNr());
				} else if (bean.isVerspaetung()) {
					rbNv.addZug(Abweichungsart.VERSPAETUNG, bean.getZugNr());
				} else if (bean.isVorplan()) {
					rbNv.addZug(Abweichungsart.VORPLAN, bean.getZugNr());
				}
			}

			//
			// SGV
			if (bean.isFestgelegtSgv()) {
				if (bean.isAusfall()) {
					rbGv.addZug(Abweichungsart.AUSFALL, bean.getZugNr());
				} else if (bean.isErsatzhalte()) {
					rbGv.addZug(Abweichungsart.ERSATZHALTE, bean.getZugNr());
				} else if (bean.isGesperrt()) {
					rbGv.addZug(Abweichungsart.GESPERRT, bean.getZugNr());
				} else if (bean.isRegelung()) {
					rbGv.addZug(Abweichungsart.REGELUNG, bean.getZugNr());
				} else if (bean.isUmleitung()) {
					rbGv.addZug(Abweichungsart.UMLEITUNG, bean.getZugNr());
				} else if (bean.isVerspaetung()) {
					rbGv.addZug(Abweichungsart.VERSPAETUNG, bean.getZugNr());
				} else if (bean.isVorplan()) {
					rbGv.addZug(Abweichungsart.VORPLAN, bean.getZugNr());
				}
			}

			// EVU
			evu = bean.getBetreiber();
			if (evu != null) {
				if (!evu.equals("")) {
					rbEVU = mapEVU.get(evu);
					if (rbEVU == null) { // EVU noch nicht in der Liste
						rbEVU = new BbzrAbweichungZuegeNummernReportBean(evu);
					}

					if (bean.isAusfall()) {
						rbEVU.addZug(Abweichungsart.AUSFALL, bean.getZugNr());
					} else if (bean.isErsatzhalte()) {
						rbEVU.addZug(Abweichungsart.ERSATZHALTE, bean.getZugNr());
					} else if (bean.isGesperrt()) {
						rbEVU.addZug(Abweichungsart.GESPERRT, bean.getZugNr());
					} else if (bean.isRegelung()) {
						rbEVU.addZug(Abweichungsart.REGELUNG, bean.getZugNr());
					} else if (bean.isUmleitung()) {
						rbEVU.addZug(Abweichungsart.UMLEITUNG, bean.getZugNr());
					} else if (bean.isVerspaetung()) {
						rbEVU.addZug(Abweichungsart.VERSPAETUNG, bean.getZugNr());
					} else if (bean.isVorplan()) {
						rbEVU.addZug(Abweichungsart.VORPLAN, bean.getZugNr());
					}

					mapEVU.put(evu, rbEVU);
				}
			}

		}

		// Ergebnis zusammenstellen
		List<BbzrAbweichungZuegeNummernReportBean> resultGesamt = report.getResultGesamt();
		resultGesamt.add(rbGesamt);
		resultGesamt.add(rbFv);
		resultGesamt.add(rbNv);
		resultGesamt.add(rbGv);

		for (BbzrAbweichungZuegeNummernReportBean rb : mapEVU.values()) {
			report.getResultEVU().add(rb);
		}

		return report;
	}

	/**
	 * @deprecated stattdessen
	 *             {@link BbzrAbweichungZuegeNummernReportAction#buildReport(List, Integer)}
	 *             verwenden
	 */
	@Deprecated
	// FIXME: Alte Methode = bisherige Implementierung zur Erzeugung des Berichts;
	// die Kennzahlen werden erst im Code errechnet
	private Report buildReport(List<Baumassnahme> bbps, Date zeitraumVerkehrstagVon,
	    Date zeitraumVerkehrstagBis, Integer qsks) {

		// Auswertung über mehrere Baumaßnahmen
		Report report = new Report();

		HashMap<String, BbzrAbweichungZuegeNummernReportBean> mapEVU = new HashMap<String, BbzrAbweichungZuegeNummernReportBean>();

		BbzrAbweichungZuegeNummernReportBean rbFv = new BbzrAbweichungZuegeNummernReportBean(
		    "Fernverkehr");
		BbzrAbweichungZuegeNummernReportBean rbNv = new BbzrAbweichungZuegeNummernReportBean(
		    "Nahverkehr");
		BbzrAbweichungZuegeNummernReportBean rbGv = new BbzrAbweichungZuegeNummernReportBean(
		    "Güterverkehr");
		BbzrAbweichungZuegeNummernReportBean rbGesamt = new BbzrAbweichungZuegeNummernReportBean(
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
				BbzrAbweichungZuegeNummernReportBean rbEVU;
				for (Zug z : zuege) {
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
					rbGesamt.addZug(abweichungsart, z);

					// FV, NV, GV
					if (m.getFestgelegtSPFV() == true)
						rbFv.addZug(abweichungsart, z);
					else if (m.getFestgelegtSPNV() == true)
						rbNv.addZug(abweichungsart, z);
					else if (m.getFestgelegtSGV() == true)
						rbGv.addZug(abweichungsart, z);

					// EVU
					evu = z.getBetreiber();
					if (evu != null) {
						if (!evu.equals("")) {
							rbEVU = mapEVU.get(evu);
							if (rbEVU == null) { // EVU noch nicht in der Liste
								rbEVU = new BbzrAbweichungZuegeNummernReportBean(evu);
								rbEVU.addZug(abweichungsart, z);
								mapEVU.put(evu, rbEVU);
							} else { // EVU in Liste vorhanden
								rbEVU.addZug(abweichungsart, z);
								mapEVU.put(evu, rbEVU);
							}
						}
					}
				}
			}
		}

		// Ergebnis zusammenstellen
		List<BbzrAbweichungZuegeNummernReportBean> resultGesamt = report.getResultGesamt();
		resultGesamt.add(rbGesamt);
		resultGesamt.add(rbFv);
		resultGesamt.add(rbNv);
		resultGesamt.add(rbGv);

		for (BbzrAbweichungZuegeNummernReportBean rb : mapEVU.values()) {
			report.getResultEVU().add(rb);
		}

		return report;

	}
}
