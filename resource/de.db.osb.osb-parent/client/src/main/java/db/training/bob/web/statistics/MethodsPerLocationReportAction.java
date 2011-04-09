package db.training.bob.web.statistics;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Regionalbereich;
import db.training.bob.model.SearchBean;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class MethodsPerLocationReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(MethodsPerLocationReportAction.class);

	private BaumassnahmeService baumassnahmeService;

	public MethodsPerLocationReportAction() {
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
			log.debug("Entering MethodsPerLocationAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {

				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
				        FetchPlan.BOB_KIGBAU, FetchPlan.BOB_KORRIDOR_ZEITFENSTER,
				        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_TERMINE_BBP,
				        FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU };

				MethodsPerLocationReport result = baumassnahmeService.findMethodsPerLocation(
				    searchBean, fetchPlans);

				HashMap<Integer, MethodsPerLocationReportBean> map = new HashMap<Integer, MethodsPerLocationReportBean>();

				RegionalbereichService rbService = EasyServiceFactory.getInstance()
				    .createRegionalbereichService();

				User loginUser = getLoginUser(request);
				TqmUser secUser = getSecUser();

				// Ergebnis zusammenstellen
				// #423 - Einschraenkung bei AUSWERTER_REGIONAL - soll nur seinen eigenen
				// Regionalbereich einsehen
				for (Regionalbereich regionalbereich : rbService.findAllAndCache()) {
					if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_ALLE"))
						if (secUser.hasRole("ADMINISTRATOR_REGIONAL")
						    & !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
							if (loginUser.getRegionalbereich().getId() == regionalbereich.getId())
								// die ID des Regionalbereichs ist Schlüssel in HashMap
								if (!map.containsKey(regionalbereich.getId())) {
									// Regionalbereich ist nicht enhalten
									map.put(regionalbereich.getId(),
									    new MethodsPerLocationReportBean(regionalbereich.getName(),
									        result.getReport().get(regionalbereich.getId())));
								}
						} else {
							// die ID des Regionalbereichs ist Schlüssel in HashMap
							if (!map.containsKey(regionalbereich.getId())) {
								// Regionalbereich ist nicht enhalten
								map.put(regionalbereich.getId(), new MethodsPerLocationReportBean(
								    regionalbereich.getName(), result.getReport().get(
								        regionalbereich.getId())));
							}
						}
					else if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH")
					    && loginUser.getRegionalbereich().getId().intValue() == regionalbereich.getId().intValue())
						// die ID des Regionalbereichs ist Schlüssel in HashMap
						if (!map.containsKey(regionalbereich.getId())) {
							// Regionalbereich ist nicht enhalten
							map.put(regionalbereich.getId(), new MethodsPerLocationReportBean(
							    regionalbereich.getName(), result.getReport().get(
							        regionalbereich.getId())));
						}
				}

				request.setAttribute("resultBean", map);
			}
			/*
			 * else { list = baumassnahmeService.findAll(); }
			 */

		} catch (LazyInitializationException e) {
			if (log.isDebugEnabled())
				log.debug(e.getMessage());
			if (log.isDebugEnabled())
				log.debug(e);
			return mapping.findForward("FAILURE");
		}
		// request.setAttribute("baumassnahmen", list);

		return mapping.findForward("SUCCESS");
	}

	public ActionForward testHelp(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		return run(mapping, form, request, response);
	}

}
