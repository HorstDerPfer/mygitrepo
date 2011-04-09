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
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.SearchBean;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public class AusfallReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(AusfallReportAction.class);

	private BaumassnahmeService baumassnahmeService;

	private RegionalbereichService regionalbereichService;

	public AusfallReportAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		regionalbereichService = serviceFactory.createRegionalbereichService();
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
			log.debug("Entering AusfallReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
				        FetchPlan.BOB_KIGBAU, FetchPlan.BOB_KORRIDOR_ZEITFENSTER,
				        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_AUSFALLGRUND };

				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);
				
				User loginUser = getLoginUser(request);
				TqmUser secUser = getSecUser();
				List<AusfallReportBean> result = buildReport(bbps, secUser, loginUser);

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

	private List<AusfallReportBean> buildReport(List<Baumassnahme> bbps, TqmUser secUser,
	    User loginUser) {
		List<AusfallReportBean> result = new ArrayList<AusfallReportBean>();

		HashMap<Integer, AusfallReportBean> map = new HashMap<Integer, AusfallReportBean>();

		for (Baumassnahme bm : bbps) {
			// prüfen, ob Regionalbereich schon im Bericht existiert, wenn ja verwenden, wenn nein
			// neu anlegen
			AusfallReportBean arb = map.get(bm.getRegionalBereichFpl().getId());
			if (arb == null) {
				arb = new AusfallReportBean(bm.getRegionalBereichFpl().getName());
				map.put(bm.getRegionalBereichFpl().getId(), arb);
			}

			// Anzahl der Baumaßnahmen zählen
			arb.setTotalCount(arb.getTotalCount() + 1);

			if (bm.getAusfallDatum() != null) {
				// davon ausgefallen zählen
				arb.setAusfallCount(arb.getAusfallCount() + 1);

				// Aufwand summieren
				arb.setCosts(arb.getCosts() + bm.getBisherigerAufwand());
			}
		}

		// nicht enhaltene Regionalbereiche einf�gen
		for (Regionalbereich regionalbereich : regionalbereichService.findAll()) {
			// die ID des Regionalbereichs ist Schl�ssel in HashMap
			if (!map.containsKey(regionalbereich.getId())) {
				// Regionalbereich ist nicht enhalten
				map.put(regionalbereich.getId(), new AusfallReportBean(regionalbereich.getName()));
			}
		}

		// Ergebnis zusammenstellen
		for (AusfallReportBean arb : map.values()) {
			if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_ALLE"))
				if (secUser.hasRole("ADMINISTRATOR_REGIONAL")
				    & !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
					if (loginUser.getRegionalbereich().getName().equals(arb.getLabel()))
						result.add(arb);
				} else {
					result.add(arb);
				}
			else if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH")
			    && loginUser.getRegionalbereich().getName().equals(arb.getLabel()))
				result.add(arb);
		}

		return result;
	}
}
