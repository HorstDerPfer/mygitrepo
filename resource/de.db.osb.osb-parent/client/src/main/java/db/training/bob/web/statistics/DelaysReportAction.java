package db.training.bob.web.statistics;

import java.util.ArrayList;
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
import db.training.bob.model.StatusType;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;

public class DelaysReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(DelaysReportAction.class);

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
			log.debug("Entering DelaysReportAction.");

		BaumassnahmeService baumassnahmeService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		request.setAttribute("evuList", true);

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_TERMINE_BBP,
				        FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU };
				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);

				String evuType = filterForm.getEvu();
				if (evuType != null) {

					List<DelaysReportBean> result = buildReport(bbps, filterForm.getEvu());
					request.setAttribute("delaysReport", result);

				} else {
					addError("error.statDelays.noevuselected");
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

	private List<DelaysReportBean> buildReport(List<Baumassnahme> bbps, String type) {
		// Auswertung über mehrere Baumaßnahme
		List<DelaysReportBean> result = new ArrayList<DelaysReportBean>();

		HashMap<Integer, DelaysReportBean> evus = new HashMap<Integer, DelaysReportBean>();

		for (Baumassnahme bm : bbps) {
			// Güterverkehrs EVU auswerten
			if (type.equalsIgnoreCase("gevu")) {
				for (TerminUebersichtGueterverkehrsEVU gevu : bm.getGevus()) {
					// prüfen, ob EVU schon im Bericht existiert, wenn ja verwenden, wenn nein neu
					// anlegen
					DelaysReportBean drb = evus.get(gevu.getEvuGruppe().getId());
					if (drb == null) {
						drb = new DelaysReportBean(gevu.getEvuGruppe().getName());
						evus.put(gevu.getEvuGruppe().getId(), drb);
					}

					if (gevu.getZvfEntwurfStatus() == StatusType.RED)
						drb.setZvFEntwurf(drb.getZvFEntwurf() + 1);

					if (gevu.getStellungnahmeEVUStatus() == StatusType.RED)
						drb.setStellungNameEVU(drb.getStellungNameEVU() + 1);

					if (gevu.getZvFStatus() == StatusType.RED)
						drb.setZvF(drb.getZvF() + 1);

					if (gevu.getMasterUebergabeblattGVStatus() == StatusType.RED)
						drb.setMasterUebergabeblattGV(drb.getMasterUebergabeblattGV() + 1);

					if (gevu.getUebergabeblattGVStatus() == StatusType.RED)
						drb.setUebergabeblattGV(drb.getUebergabeblattGV() + 1);

					if (gevu.getFploStatus() == StatusType.RED)
						drb.setFplo(drb.getFplo() + 1);

					if (gevu.getEingabeGFD_ZStatus() == StatusType.RED)
						drb.setEingabeGFDZ(drb.getEingabeGFDZ() + 1);
				}
			}

			// Personenverkehrs EVU auswerten
			if (type.equalsIgnoreCase("pevu")) {
				for (TerminUebersichtPersonenverkehrsEVU pevu : bm.getPevus()) {
					// prüfen, ob EVU schon im Bericht existiert, wenn ja verwenden, wenn nein neu
					// anlegen
					DelaysReportBean drb = evus.get(pevu.getEvuGruppe().getId());
					if (drb == null) {
						drb = new DelaysReportBean(pevu.getEvuGruppe().getName());
						evus.put(pevu.getEvuGruppe().getId(), drb);
					}

					if (pevu.getZvfEntwurfStatus() == StatusType.RED)
						drb.setZvFEntwurf(drb.getZvFEntwurf() + 1);

					if (pevu.getStellungnahmeEVUStatus() == StatusType.RED)
						drb.setStellungNameEVU(drb.getStellungNameEVU() + 1);

					if (pevu.getZvFStatus() == StatusType.RED)
						drb.setZvF(drb.getZvF() + 1);

					if (pevu.getMasterUebergabeblattPVStatus() == StatusType.RED)
						drb.setMasterUebergabeblattPV(drb.getMasterUebergabeblattPV() + 1);

					if (pevu.getUebergabeblattPVStatus() == StatusType.RED)
						drb.setUebergabeblattPV(drb.getUebergabeblattPV() + 1);

					if (pevu.getFploStatus() == StatusType.RED)
						drb.setFplo(drb.getFplo() + 1);

					if (pevu.getEingabeGFD_ZStatus() == StatusType.RED)
						drb.setEingabeGFDZ(drb.getEingabeGFDZ() + 1);
				}
			}
		}

		// Ergebnis zusammenstellen
		for (DelaysReportBean drb : evus.values()) {
			result.add(drb);
		}

		return result;
	}
}
