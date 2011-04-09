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

public class QualitaetstrasseReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(QualitaetstrasseReportAction.class);

	private BaumassnahmeService baumassnahmeService;

	public QualitaetstrasseReportAction() {
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
			log.debug("Entering QualitaetstrasseReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
				        FetchPlan.BOB_KIGBAU, FetchPlan.BOB_KORRIDOR_ZEITFENSTER,
				        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_TERMINE_BBP,
				        FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU };
				List<Baumassnahme> bbps = baumassnahmeService.findBySearchBean(searchBean,
				    fetchPlans);

				User loginUser = getLoginUser(request);
				TqmUser secUser = getSecUser();
				List<QualitaetstrasseReportBean> result = buildReport(bbps, secUser, loginUser);

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

	private List<QualitaetstrasseReportBean> buildReport(List<Baumassnahme> bbps, TqmUser secUser,
	    User loginUser) {
		// Auswertung über mehrere Baumaßnahmen
		List<QualitaetstrasseReportBean> result = new ArrayList<QualitaetstrasseReportBean>();

		HashMap<Integer, QualitaetstrasseReportBean> map = new HashMap<Integer, QualitaetstrasseReportBean>();

		for (Baumassnahme bm : bbps) {
			// prüfen, ob Regionalbereich schon im Bericht existiert, wenn ja verwenden, wenn nein
			// neu anlegen
			QualitaetstrasseReportBean qrb = map.get(bm.getRegionalBereichFpl().getId());
			if (qrb == null) {
				qrb = new QualitaetstrasseReportBean(bm.getRegionalBereichFpl().getName());
				map.put(bm.getRegionalBereichFpl().getId(), qrb);
			}

			if (bm.getVerzichtQTrasseBeantragt() != null) {
				qrb.setSubmittedCount(qrb.getSubmittedCount() + 1);

				Boolean isApproved = bm.getVerzichtQTrasseGenehmigt();
				if ((isApproved != null) && (isApproved == true)) {
					// genehmigten Antrag zählen
					qrb.setApprovedCount(qrb.getApprovedCount() + 1);
				} else {
					// abgelehnten Antrag zählen
					qrb.setRejectedCount(qrb.getRejectedCount() + 1);
				}
			}
		}

		
		// nicht enhaltene Regionalbereiche einfügen
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		for (Regionalbereich regionalbereich : regionalbereichService.findAll()) {
			// die ID des Regionalbereichs ist Schlüssel in HashMap
			if (!map.containsKey(regionalbereich.getId())) {
				// Regionalbereich ist nicht enhalten
				map.put(regionalbereich.getId(), new QualitaetstrasseReportBean(regionalbereich.getName()));
			}
		}
		
		// Ergebnis zusammenstellen
		for (QualitaetstrasseReportBean qrb : map.values()) {
			if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_ALLE"))
				if (secUser.hasRole("ADMINISTRATOR_REGIONAL")
				    & !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
					if (loginUser.getRegionalbereich().getName().equals(qrb.getLabel()))
						result.add(qrb);
				} else {
					result.add(qrb);
				}
			else if (secUser.hasAuthorization("ROLE_AUSWERTUNG_ALLGEMEIN_REGIONALBEREICH")
			    && loginUser.getRegionalbereich().getName().equals(qrb.getLabel()))
				result.add(qrb);
		}

		return result;
	}
}
