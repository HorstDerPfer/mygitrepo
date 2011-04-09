package db.training.bob.web.statistics.zvf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.EVUGruppe;
import db.training.bob.model.SearchBean;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Zug;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.web.statistics.BaseSearchAction;
import db.training.bob.web.statistics.StatisticsFilterForm;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;

public class VerspaetungsminutenZuegeReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(VerspaetungsminutenZuegeReportAction.class);

	private BaumassnahmeService baumassnahmeService;
	
	private List<SimpleReportBean> resultGesamt;
	private List<SimpleReportBean> resultEVU;

	public VerspaetungsminutenZuegeReportAction() {
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
			log.debug("Entering VerspaetungsminutenZuegeReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;
		resultGesamt = new ArrayList<SimpleReportBean>();
		resultEVU = new ArrayList<SimpleReportBean>();
		Date zeitraumVerkehrstagVon = null;
		Date zeitraumVerkehrstagBis = null;
		Integer qsksInt = null;

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
				        FetchPlan.UEB_MN_ZUEGE };
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

	private void buildReport(List<Baumassnahme> bbps, List<SimpleReportBean> resultGesamt,
	    List<SimpleReportBean> resultEVU, Date zeitraumVerkehrstagVon, Date zeitraumVerkehrstagBis,
	    Integer qsks) {

		HashMap<String, SimpleReportBean> mapGesamt = new HashMap<String, SimpleReportBean>();
		mapGesamt.put("Gesamt", new SimpleReportBean("Gesamt"));
		mapGesamt.put("Fernverkehr", new SimpleReportBean("Fernverkehr"));
		mapGesamt.put("Nahverkehr", new SimpleReportBean("Nahverkehr"));
		mapGesamt.put("Güterverkehr", new SimpleReportBean("Güterverkehr"));
		mapGesamt.put("QS-Züge", new SimpleReportBean("QS-Züge"));
		mapGesamt.put("KS-Züge", new SimpleReportBean("KS-Züge"));

		HashMap<String, SimpleReportBean> mapEVU = new HashMap<String, SimpleReportBean>();

		for (Baumassnahme bm : bbps) {
			if (bm.getUebergabeblatt() != null) {
				SimpleReportBean rbFvNvGv = null;
				SimpleReportBean rbGesamt = null;
				SimpleReportBean rbQS = null;
				SimpleReportBean rbKS = null;

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
				SimpleReportBean rbEVU;
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

					int sum = 0;
					if (z.getAbweichung().getVerspaetung() != null)
						sum = Math.abs(z.getAbweichung().getVerspaetung());

					// Gesamt
					int anzahlGesamt = rbGesamt.getAnzahl();
					rbGesamt.setAnzahl(anzahlGesamt + sum);
					mapGesamt.put("Gesamt", rbGesamt);

					// QS/KS
					Integer qs_ks = z.getQs_ks();
					if (qs_ks != null) {
						if (qs_ks.equals(1)) {// qs
							int anzahlQS = rbQS.getAnzahl();
							rbQS.setAnzahl(anzahlQS + sum);
							mapGesamt.put("QS-Züge", rbQS);
						} else if (qs_ks.equals(2)) {
							int anzahlKS = rbKS.getAnzahl();
							rbKS.setAnzahl(anzahlKS + sum);
							mapGesamt.put("KS-Züge", rbKS);
						}
					}

					// FV, NV, GV
					if (rbFvNvGv != null) {
						int anzahlFvNvGv = rbFvNvGv.getAnzahl();
						rbFvNvGv.setAnzahl(anzahlFvNvGv + sum);
						mapGesamt.put(rbFvNvGv.getLabel(), rbFvNvGv);
					}

					// EVU
					evu = z.getBetreiber();
					if (evu != null) {
						if (!evu.equals("")) {
							rbEVU = mapEVU.get(evu);
							if (rbEVU != null) { // EVU in Liste vorhanden
								int anzahlEVU = rbEVU.getAnzahl();
								rbEVU.setAnzahl(anzahlEVU + sum);
								mapEVU.put(evu, rbEVU);
							} else if (sum > 0) { // EVU noch nicht in der Liste
								rbEVU = new SimpleReportBean(evu);
								rbEVU.setAnzahl(sum);
								mapEVU.put(evu, rbEVU);
							}
						}
					}
				}

			}
		}

		// Ergebnis zusammenstellen
		for (SimpleReportBean rb : mapGesamt.values()) {
			this.resultGesamt.add(rb);
		}
		
		//Gruppieren nach EVU-Kundengruppe
		EVUGruppeService evugruppeService = serviceFactory.createEVUGruppeService();
		EVUGruppe evuGruppe;
		
		for (SimpleReportBean rb : mapEVU.values()) {
			evuGruppe = evugruppeService.findByKundenNr(rb.getLabel());
			SimpleReportBean rb2 = new SimpleReportBean(rb.getLabel());
			rb2.setLabel(evuGruppe.getName());
			rb2.setAnzahl(rb.getAnzahl());
			
			Iterator<SimpleReportBean> it = this.resultEVU.iterator();
			SimpleReportBean entry = null;
			Boolean found = false;
			
			while (it.hasNext()) {
				entry = it.next();
				//wenn Eintrag bereits in Ergebnisliste, dann Werte addieren
				if (entry.getLabel().equals(rb2.getLabel())) {
					found = true;
					entry.setAnzahl(entry.getAnzahl() + rb2.getAnzahl());
				}
			}
			
			//wenn noch nicht in Ergebnisliste, dann reinpacken
			if (!found)
				this.resultEVU.add(rb2);
			
		}

	}
}
