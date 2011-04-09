package db.training.bob.web.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Aenderung;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.SearchBean;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class ChangesReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(ChangesReportAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		ActionForward baseSearchActionResult = super.run(mapping, form, request, response);
		if (baseSearchActionResult.getName().equals("FAILURE")) {
			return baseSearchActionResult;
		}

		if (log.isDebugEnabled())
			log.debug("Entering ChangesReportAction.");

		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true"))
			return baseSearchActionResult;

		BaumassnahmeService baumassnahmeService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try { // Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
				        FetchPlan.BOB_KIGBAU, FetchPlan.BOB_KORRIDOR_ZEITFENSTER,
				        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_TERMINE_BBP,
				        FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU,
				        FetchPlan.BOB_AENDERUNGSDOKUMENTATION };

				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);
				User loginUser = getLoginUser(request);
				TqmUser secUser = getSecUser();
				List<ChangesReportBean> result = buildReport(bbps,secUser,loginUser);

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

	private List<ChangesReportBean> buildReport(List<Baumassnahme> bbps, TqmUser secUser,
	    User loginUser) {
		List<ChangesReportBean> result = new ArrayList<ChangesReportBean>();

		HashMap<Integer, ChangesReportBean> map = new HashMap<Integer, ChangesReportBean>();

		for (Baumassnahme bm : bbps) {
			// prüfen, ob Regionalbereich schon im Bericht existiert, wenn ja verwenden, wenn nein
			// neu anlegen
			ChangesReportBean crb = map.get(bm.getRegionalBereichFpl().getId());
			if (crb == null) {
				crb = new ChangesReportBean(bm.getRegionalBereichFpl().getName());
				map.put(bm.getRegionalBereichFpl().getId(), crb);
			}

			// Anzahl der Baumaßnahmen zählen
			crb.setTotalCount(crb.getTotalCount() + 1);

			Set<Aenderung> aenderungen = bm.getAenderungen();
			if ((aenderungen != null) && (!aenderungen.isEmpty())) {
				// davon geändert zählen
				crb.setChangedCount(crb.getChangedCount() + 1);

				// Aufwand summieren
				int bmCosts = 0;
				for (Aenderung a : aenderungen) {
					bmCosts += (a.getAufwand() != null ? a.getAufwand() : 0);
				}
				crb.setCosts(crb.getCosts() + bmCosts);
			}
		}

		// nicht enhaltene Regionalbereiche einfügen
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		for (Regionalbereich regionalbereich : regionalbereichService.findAll()) {
			// die ID des Regionalbereichs ist Schlüssel in HashMap
			if (!map.containsKey(regionalbereich.getId())) {
				// Regionalbereich ist nicht enhalten
				map.put(regionalbereich.getId(), new ChangesReportBean(regionalbereich.getName()));
			}
		}

		// Ergebnis zusammenstellen
		for (ChangesReportBean crb : map.values()) {
			if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_ALLE"))
				if (secUser.hasRole("ADMINISTRATOR_REGIONAL")
				    & !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
					if (loginUser.getRegionalbereich().getName().equals(crb.getLabel()))
						result.add(crb);
				} else {
					result.add(crb);
				}
			else if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH")
			    && loginUser.getRegionalbereich().getName().equals(crb.getLabel()))
				result.add(crb);
		}

		return result;
	}
}
