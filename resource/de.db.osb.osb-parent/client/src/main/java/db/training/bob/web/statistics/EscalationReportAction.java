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

public class EscalationReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(EscalationReportAction.class);

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
			log.debug("Entering EscalationReportAction.");

		BaumassnahmeService baumassnahmeService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
				        FetchPlan.BOB_KIGBAU, FetchPlan.BOB_KORRIDOR_ZEITFENSTER,
				        FetchPlan.BOB_BEARBEITUNGSBEREICH };

				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);

				User loginUser = getLoginUser(request);
				TqmUser secUser = getSecUser();
				List<EscalationReportBean> result = buildReport(bbps, secUser, loginUser);

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

	private List<EscalationReportBean> buildReport(List<Baumassnahme> bbps, TqmUser secUser,
	    User loginUser) {
		List<EscalationReportBean> result = new ArrayList<EscalationReportBean>();

		HashMap<Integer, EscalationReportBean> map = new HashMap<Integer, EscalationReportBean>();

		for (Baumassnahme bm : bbps) {
			// prüfen, ob Regionalbereich schon im Bericht existiert, wenn ja verwenden, wenn nein
			// neu anlegen
			EscalationReportBean erb = map.get(bm.getRegionalBereichFpl().getId());
			if (erb == null) {
				erb = new EscalationReportBean(bm.getRegionalBereichFpl().getName());
				map.put(bm.getRegionalBereichFpl().getId(), erb);
			}

			// Baumaßnahme zählen
			erb.setTotalCount(erb.getTotalCount() + 1);

			// Eskalation zählen
			if (bm.getEskalationsBeginn() != null) {
				erb.setEscalationCount(erb.getEscalationCount() + 1);
			}

			// Veto zählen
			if (bm.getEskalationVeto() == true) {
				erb.setVetoCount(erb.getVetoCount() + 1);
			}
		}

		// nicht enhaltene Regionalbereiche einfügen
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		for (Regionalbereich regionalbereich : regionalbereichService.findAll()) {
			// die ID des Regionalbereichs ist Schlüssel in HashMap
			if (!map.containsKey(regionalbereich.getId())) {
				// Regionalbereich ist nicht enhalten
				map.put(regionalbereich.getId(),
				    new EscalationReportBean(regionalbereich.getName()));
			}
		}
		
		// Ergebnis zusammenstellen
		for (EscalationReportBean erb : map.values()) {
			if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_ALLE"))
				if (secUser.hasRole("ADMINISTRATOR_REGIONAL")
				    & !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
					if (loginUser.getRegionalbereich().getName().equals(erb.getLabel()))
						result.add(erb);
				} else {
					result.add(erb);
				}
			else if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH")
			    && loginUser.getRegionalbereich().getName().equals(erb.getLabel()))
				result.add(erb);
		}

		return result;
	}
}
