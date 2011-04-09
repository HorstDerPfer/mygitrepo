package db.training.bob.web.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Benchmark;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.SearchBean;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class PerformanceReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(PerformanceReportAction.class);

	private BaumassnahmeService baumassnahmeService;

	public PerformanceReportAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		ActionForward baseSearchActionResult = super.run(mapping, form, request, response);
		if (baseSearchActionResult.getName().equals("FAILURE")) {
			return baseSearchActionResult;
		}

		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true"))
			return baseSearchActionResult;

		if (log.isDebugEnabled())
			log.debug("Entering PerformanceReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
				        FetchPlan.BOB_ARBEITSLEISTUNG_REGIONALBEREICHE };

				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);

				List<PerformanceReportBean> result = buildReport(bbps, secUser, loginUser);

				request.setAttribute("reportBean", result);
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

	private List<PerformanceReportBean> buildReport(List<Baumassnahme> bbps, TqmUser secUser,
	    User loginUser) {
		List<PerformanceReportBean> result = new ArrayList<PerformanceReportBean>();

		HashMap<Regionalbereich, PerformanceReportBean> map = new HashMap<Regionalbereich, PerformanceReportBean>();

		for (Baumassnahme bm : bbps) {
			log.debug("Vorgang: " + bm.getVorgangsNr());
			Map<Regionalbereich, Benchmark> benchmarkMap = bm.getBenchmark();
			for (Map.Entry<Regionalbereich, Benchmark> entry : benchmarkMap.entrySet()) {
				Regionalbereich regionalBereich = entry.getKey();
				Benchmark benchmark = entry.getValue();

				// prüfen, ob Regionalbereich schon im Bericht existiert, wenn ja verwenden, wenn
				// nein neu anlegen
				PerformanceReportBean prb = map.get(regionalBereich);
				if (prb == null) {
					prb = new PerformanceReportBean(regionalBereich.getName());
					map.put(regionalBereich, prb);
				}

				// geregelte Trassen
				if (benchmark.getGeregelteTrassenBiUeE() != null) {
					log.debug("geregelte Trassen = " + prb.getGeregelteTrassen() + "+"
					    + +benchmark.getGeregelteTrassenBiUeE());
					prb.setGeregelteTrassen(prb.getGeregelteTrassen()
					    + benchmark.getGeregelteTrassenBiUeE());
				}

				// überarbeitete Trassen
				if (benchmark.getUeberarbeiteteTrassenBiUe() != null) {
					log.debug("überarb. Trassen = " + prb.getUeberarbeiteteTrassen() + "+"
					    + +benchmark.getUeberarbeiteteTrassenBiUe());
					prb.setUeberarbeiteteTrassen(prb.getUeberarbeiteteTrassen()
					    + benchmark.getUeberarbeiteteTrassenBiUe());
				}

				// Anzahl BiÜ
				if (benchmark.getErstellteBiUe() != null) {
					prb.setAnzahlBiUe(prb.getAnzahlBiUe() + benchmark.getErstellteBiUe());
				}

				// veröffentlichte Trassen
				if (benchmark.getVeroeffentlichteTrassenZvF() != null) {
					prb.setVeroeffentlichteTrassen(prb.getVeroeffentlichteTrassen()
					    + benchmark.getVeroeffentlichteTrassenZvF());
				}

			}
		}

		// Ergebnis zusammenstellen
		Map<Regionalbereich, PerformanceReportBean> sortedMap = new TreeMap<Regionalbereich, PerformanceReportBean>();
		sortedMap.putAll(map);
		for (PerformanceReportBean prb : sortedMap.values()) {
			if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_ALLE"))
				if (secUser.hasRole("ADMINISTRATOR_REGIONAL")
				    & !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
					if (loginUser.getRegionalbereich().getName().equals(prb.getLabel()))
						result.add(prb);
				} else {
					result.add(prb);
				}
			else if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH")
			    && loginUser.getRegionalbereich().getName().equals(prb.getLabel()))
				result.add(prb);
		}

		return result;
	}
}