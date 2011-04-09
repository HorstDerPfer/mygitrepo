package db.training.bob.web.statistics;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.SearchBean;
import db.training.bob.model.StatusType;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;

public class AccuracyReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(AccuracyReportAction.class);

	private BaumassnahmeService baumassnahmeService;

	public AccuracyReportAction() {
		baumassnahmeService = EasyServiceFactory.getInstance().createBaumassnahmeService();
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
			log.debug("Entering AccuracyReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_TERMINE_BBP,
				        FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU };

				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);

				if (bbps.size() == 1) {
					List<SimpleAccuracyReportBean> result = buildSimpleReport(bbps);
					request.removeAttribute("complexReportBean");
					request.setAttribute("simpleReportBean", result);
				} else {
					// erweiterte Auswertung für mehrere Baumaßnahmen
					List<ComplexAccuracyReportBean> result = buildComplexReport(bbps);
					request.removeAttribute("simpleReportBean");
					request.setAttribute("complexReportBean", result);
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

	private List<SimpleAccuracyReportBean> buildSimpleReport(List<Baumassnahme> bbps) {
		// einfache Auswertung für eine Baumaßnahme
		List<SimpleAccuracyReportBean> result = new ArrayList<SimpleAccuracyReportBean>();
		// Baumassnahme bm = bbps.get(0);

		List<ComplexAccuracyReportBean> complexResult = buildComplexReport(bbps);
		for (ComplexAccuracyReportBean crb : complexResult) {
			if (crb.getDelayedCountValue() > 0)
				result.add(new SimpleAccuracyReportBean(crb.getKey(), StatusType.RED));
			else
				result.add(new SimpleAccuracyReportBean(crb.getKey(), StatusType.GREEN));
		}
		return result;
	}

	private List<ComplexAccuracyReportBean> buildComplexReport(List<Baumassnahme> bbps) {
		// Auswertung über mehrere Baumaßnahmen
		List<ComplexAccuracyReportBean> result = new ArrayList<ComplexAccuracyReportBean>();

		ComplexAccuracyReportBean anforderungBBRZ = new ComplexAccuracyReportBean(
		    "auswertungen.termin.anforderungbbzr", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean biUeEntwurf = new ComplexAccuracyReportBean(
		    "auswertungen.termin.biueentwurf", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean zvfEntwurf = new ComplexAccuracyReportBean(
		    "auswertungen.termin.zvfentwurf", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean koordinationsErgebnis = new ComplexAccuracyReportBean(
		    "auswertungen.termin.koordinationsergebnis", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean stellungnahmeEVU = new ComplexAccuracyReportBean(
		    "auswertungen.termin.stellungnahmeevu", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean gesamtKonzeptBBZR = new ComplexAccuracyReportBean(
		    "auswertungen.termin.gesamtkonzeptbbzr", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean biUe = new ComplexAccuracyReportBean("auswertungen.termin.biue",
		    0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean zvF = new ComplexAccuracyReportBean("auswertungen.termin.zvf", 0,
		    0, 0, 0, 0, 0);
		ComplexAccuracyReportBean masterUebergabeblattPV = new ComplexAccuracyReportBean(
		    "auswertungen.termin.masteruebergabeblattpv", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean masterUebergabeblattGV = new ComplexAccuracyReportBean(
		    "auswertungen.termin.masteruebergabeblattgv", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean uebergabeblattPV = new ComplexAccuracyReportBean(
		    "auswertungen.termin.uebergabeblattpv", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean uebergabeblattGV = new ComplexAccuracyReportBean(
		    "auswertungen.termin.uebergabeblattgv", 0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean fplo = new ComplexAccuracyReportBean("auswertungen.termin.fplo",
		    0, 0, 0, 0, 0, 0);
		ComplexAccuracyReportBean eingabeGFDZ = new ComplexAccuracyReportBean(
		    "auswertungen.termin.eingabegfd_z", 0, 0, 0, 0, 0, 0);

		for (Baumassnahme bm : bbps) {
			anforderungBBRZ.addDate(bm.getBaubetriebsplanung().getAnforderungBBZRSoll(), bm
			    .getBaubetriebsplanung().getAnforderungBBZR(), bm.getBaubetriebsplanung()
			    .getAnforderungBBZRStatus());

			if (bm.getBaubetriebsplanung().isBiUeEntwurfErforderlich()) {
				biUeEntwurf.addDate(bm.getBaubetriebsplanung().getBiUeEntwurfSoll(), bm
				    .getBaubetriebsplanung().getBiUeEntwurf(), bm.getBaubetriebsplanung()
				    .getBiUeEntwurfStatus());
			}

			if (bm.getBaubetriebsplanung().isZvfEntwurfErforderlich()) {
				zvfEntwurf.addDate(bm.getBaubetriebsplanung().getZvfEntwurfSoll(), bm
				    .getBaubetriebsplanung().getZvfEntwurf(), bm.getBaubetriebsplanung()
				    .getZvfEntwurfStatus());
			}

			if (bm.getBaubetriebsplanung().isKoordinationsergebnisErforderlich()) {
				koordinationsErgebnis.addDate(
				    bm.getBaubetriebsplanung().getGesamtKonzeptBBZRSoll(), bm
				        .getBaubetriebsplanung().getKoordinationsErgebnis(), bm
				        .getBaubetriebsplanung().getKoordinationsErgebnisStatus());
			}

			if (bm.getBaubetriebsplanung().isGesamtkonzeptBBZRErforderlich()) {
				gesamtKonzeptBBZR.addDate(bm.getBaubetriebsplanung().getGesamtKonzeptBBZRSoll(), bm
				    .getBaubetriebsplanung().getGesamtKonzeptBBZR(), bm.getBaubetriebsplanung()
				    .getGesamtKonzeptBBZRStatus());
			}

			if (bm.getBaubetriebsplanung().isBiUeErforderlich()) {
				biUe.addDate(bm.getBaubetriebsplanung().getBiUeSoll(), bm.getBaubetriebsplanung()
				    .getBiUe(), bm.getBaubetriebsplanung().getBiUeStatus());
			}

			if (bm.getBaubetriebsplanung().isZvFErforderlich()) {
				zvF.addDate(bm.getBaubetriebsplanung().getZvfSoll(), bm.getBaubetriebsplanung()
				    .getZvf(), bm.getBaubetriebsplanung().getZvfStatus());
			}

			for (TerminUebersichtGueterverkehrsEVU gevu : bm.getGevus()) {
				if (gevu.isZvfEntwurfErforderlich()) {
					zvfEntwurf.addDate(gevu.getZvfEntwurfSoll(), gevu.getZvfEntwurf(),
					    gevu.getZvfEntwurfStatus());
				}

				if (gevu.isStellungnahmeEVUErforderlich()) {
					stellungnahmeEVU.addDate(gevu.getStellungnahmeEVUSoll(),
					    gevu.getStellungnahmeEVU(), gevu.getStellungnahmeEVUStatus());
				}

				if (gevu.isZvfErforderlich()) {
					zvF.addDate(gevu.getZvFSoll(), gevu.getZvF(), gevu.getZvFStatus());
				}

				if (gevu.isMasterUebergabeblattGVErforderlich()) {
					masterUebergabeblattGV.addDate(gevu.getMasterUebergabeblattGVSoll(),
					    gevu.getMasterUebergabeblattGV(), gevu.getMasterUebergabeblattGVStatus());
				}

				if (gevu.isUebergabeblattGVErforderlich()) {
					uebergabeblattGV.addDate(gevu.getUebergabeblattGVSoll(),
					    gevu.getUebergabeblattGV(), gevu.getUebergabeblattGVStatus());
				}

				if (gevu.isFploErforderlich()) {
					fplo.addDate(gevu.getFploSoll(), gevu.getFplo(), gevu.getFploStatus());
				}

				if (gevu.isEingabeGFD_ZErforderlich()) {
					eingabeGFDZ.addDate(gevu.getEingabeGFD_ZSoll(), gevu.getEingabeGFD_Z(),
					    gevu.getEingabeGFD_ZStatus());
				}
			}

			for (TerminUebersichtPersonenverkehrsEVU pevu : bm.getPevus()) {
				if (pevu.isZvfEntwurfErforderlich()) {
					zvfEntwurf.addDate(pevu.getZvfEntwurfSoll(), pevu.getZvfEntwurf(),
					    pevu.getZvfEntwurfStatus());
				}

				if (pevu.isStellungnahmeEVUErforderlich()) {
					stellungnahmeEVU.addDate(pevu.getStellungnahmeEVUSoll(),
					    pevu.getStellungnahmeEVU(), pevu.getStellungnahmeEVUStatus());
				}

				if (pevu.isZvfErforderlich()) {
					zvF.addDate(pevu.getZvFSoll(), pevu.getZvF(), pevu.getZvFStatus());
				}

				if (pevu.isMasterUebergabeblattPVErforderlich()) {
					masterUebergabeblattPV.addDate(pevu.getMasterUebergabeblattPVSoll(),
					    pevu.getMasterUebergabeblattPV(), pevu.getMasterUebergabeblattPVStatus());
				}

				if (pevu.isUebergabeblattPVErforderlich()) {
					uebergabeblattPV.addDate(pevu.getUebergabeblattPVSoll(),
					    pevu.getUebergabeblattPV(), pevu.getUebergabeblattPVStatus());
				}

				if (pevu.isFploErforderlich()) {
					fplo.addDate(pevu.getFploSoll(), pevu.getFplo(), pevu.getFploStatus());
				}

				if (pevu.isEingabeGFD_ZErforderlich()) {
					eingabeGFDZ.addDate(pevu.getEingabeGFD_ZSoll(), pevu.getEingabeGFD_Z(),
					    pevu.getEingabeGFD_ZStatus());
				}
			}
		}

		result.add(anforderungBBRZ);
		result.add(biUeEntwurf);
		result.add(zvfEntwurf);
		result.add(koordinationsErgebnis);
		result.add(stellungnahmeEVU);
		result.add(gesamtKonzeptBBZR);
		result.add(biUe);
		result.add(zvF);
		result.add(masterUebergabeblattPV);
		result.add(masterUebergabeblattGV);
		result.add(uebergabeblattPV);
		result.add(uebergabeblattGV);
		result.add(fplo);
		result.add(eingabeGFDZ);

		return result;
	}
}
