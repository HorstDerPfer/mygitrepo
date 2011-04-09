package db.training.bob.web.statistics.zvf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.SearchBean;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.web.statistics.BaseSearchAction;
import db.training.bob.web.statistics.StatisticsFilterForm;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;

public class UebAbweichungZuegeNummernReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(UebAbweichungZuegeNummernReportAction.class);

	private BaumassnahmeService baumassnahmeService;

	public UebAbweichungZuegeNummernReportAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
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
			log.debug("Entering UebAbweichungZuegeNummernReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;
		Date zeitraumVerkehrstagVon = null;
		Date zeitraumVerkehrstagBis = null;
		Integer qsksInt = null;
		List<BbzrAbweichungZuegeNummernReportBean> resultEVU = new ArrayList<BbzrAbweichungZuegeNummernReportBean>();
		List<BbzrAbweichungZuegeNummernReportBean> resultGesamt = new ArrayList<BbzrAbweichungZuegeNummernReportBean>();

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				String verkehrstagVon = searchBean.getSearchVerkehrstagBeginnDatum();
				String verkehrstagBis = searchBean.getSearchVerkehrstagEndDatum();
				String qsks = searchBean.getQsks();

				if ((verkehrstagVon != null) && !(verkehrstagVon.equals("")))
					zeitraumVerkehrstagVon = FrontendHelper.castStringToDate(verkehrstagVon);
				if ((verkehrstagBis != null) && !(verkehrstagBis.equals("")))
					zeitraumVerkehrstagBis = FrontendHelper.castStringToDate(verkehrstagBis);
				if ((qsks != null) && !(qsks.equals("")))
					qsksInt = FrontendHelper.castStringToInteger(qsks);

				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
				        FetchPlan.BOB_KIGBAU, FetchPlan.BOB_KORRIDOR_ZEITFENSTER,
				        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_TERMINE_BBP,
				        FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU,
				        FetchPlan.BOB_UEBERGABEBLATT, FetchPlan.UEB_BAUMASSNAHMEN,
				        FetchPlan.UEB_MN_ZUEGE, FetchPlan.UEB_KNOTENZEITEN };
				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);

				buildReport(bbps, resultGesamt, resultEVU, zeitraumVerkehrstagVon,
				    zeitraumVerkehrstagBis, qsksInt);

				request.setAttribute("reportBeanGesamt", resultGesamt);
				request.setAttribute("reportBeanEVU", resultEVU);
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

	private void buildReport(List<Baumassnahme> bbps,
	    List<BbzrAbweichungZuegeNummernReportBean> resultGesamt,
	    List<BbzrAbweichungZuegeNummernReportBean> resultEVU, Date zeitraumVerkehrstagVon,
	    Date zeitraumVerkehrstagBis, Integer qsks) {

		HashMap<String, BbzrAbweichungZuegeNummernReportBean> mapGesamt = new HashMap<String, BbzrAbweichungZuegeNummernReportBean>();
		mapGesamt.put("Gesamt", new BbzrAbweichungZuegeNummernReportBean("Gesamt"));
		mapGesamt.put("Fernverkehr", new BbzrAbweichungZuegeNummernReportBean("Fernverkehr"));
		mapGesamt.put("Nahverkehr", new BbzrAbweichungZuegeNummernReportBean("Nahverkehr"));
		mapGesamt.put("Güterverkehr", new BbzrAbweichungZuegeNummernReportBean("Güterverkehr"));
		mapGesamt.put("QS-Züge", new BbzrAbweichungZuegeNummernReportBean("QS-Züge"));
		mapGesamt.put("KS-Züge", new BbzrAbweichungZuegeNummernReportBean("KS-Züge"));

		HashMap<String, BbzrAbweichungZuegeNummernReportBean> mapEVU = new HashMap<String, BbzrAbweichungZuegeNummernReportBean>();

		for (Baumassnahme bm : bbps) {
			if (bm.getUebergabeblatt() != null) {
				BbzrAbweichungZuegeNummernReportBean rbFvNvGv = null;
				BbzrAbweichungZuegeNummernReportBean rbGesamt;
				BbzrAbweichungZuegeNummernReportBean rbQS = null;
				BbzrAbweichungZuegeNummernReportBean rbKS = null;

				// Gesamt
				rbGesamt = mapGesamt.get("Gesamt");

				// FV, NV, GV
				Massnahme m = null;
				List<Zug> zuege = null;
				try {
					m = bm.getUebergabeblatt().getMassnahmen().iterator().next();
					if (m.getFestgelegtSPFV() == true)
						rbFvNvGv = mapGesamt.get("Fernverkehr");
					else if (m.getFestgelegtSPNV() == true)
						rbFvNvGv = mapGesamt.get("Nahverkehr");
					else if (m.getFestgelegtSGV() == true)
						rbFvNvGv = mapGesamt.get("Güterverkehr");

					// QS/KS
					rbQS = mapGesamt.get("QS-Züge");
					rbKS = mapGesamt.get("KS-Züge");

					zuege = m.getZug();
				} catch (NullPointerException e) {
					log.debug("fehlerhafte BAUMASSNAHME: " + bm.getId());
					continue;
				}
				String evu;
				BbzrAbweichungZuegeNummernReportBean rbEVU;
				for (Zug z : zuege) {
					if (zeitraumVerkehrstagVon != null)
						if (z.getVerkehrstag().before(zeitraumVerkehrstagVon))
							continue;
					if (zeitraumVerkehrstagBis != null)
						if (z.getVerkehrstag().after(zeitraumVerkehrstagBis))
							continue;
					if (qsks != null) {
						if (qsks != -1) {// qs, ks oder nichtaktiv ausgewählt
							if (!qsks.equals(z.getQs_ks()))
								continue;
						}
					}

					Abweichungsart abweichungsart = z.getAbweichung().getArt();

					// Gesamt
					rbGesamt.addZug(abweichungsart, z);
					mapGesamt.put("Gesamt", rbGesamt);

					// QS/KS
					Integer qs_ks = z.getQs_ks();
					if (qs_ks != null) {
						if (qs_ks.equals(1)) {// qs
							rbQS.addZug(abweichungsart, z);
							mapGesamt.put("QS-Züge", rbQS);
						} else if (qs_ks.equals(2)) {
							rbKS.addZug(abweichungsart, z);
							mapGesamt.put("KS-Züge", rbKS);
						}
					}

					// FV, NV, GV
					if (rbFvNvGv != null) {
						rbFvNvGv.addZug(abweichungsart, z);
						mapGesamt.put(rbFvNvGv.getLabel(), rbFvNvGv);
					}

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
		for (BbzrAbweichungZuegeNummernReportBean rb : mapGesamt.values()) {
			resultGesamt.add(rb);
		}
		for (BbzrAbweichungZuegeNummernReportBean rb : mapEVU.values()) {
			resultEVU.add(rb);
		}

	}
}
