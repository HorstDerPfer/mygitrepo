package db.training.bob.web.statistics.zvf;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.SearchBean;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.report.BbzrVerspaetungsminutenReportService;
import db.training.bob.service.report.Report;
import db.training.bob.web.statistics.BaseSearchAction;
import db.training.bob.web.statistics.StatisticsFilterForm;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;

public class BbzrVerspaetungsminutenZuegeReportAction extends BaseSearchAction {
	private static final Logger log = Logger.getLogger(BbzrVerspaetungsminutenZuegeReportAction.class);

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
			log.debug("Entering BbzrVerspaetungsminutenZuegeReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);

			if (searchBean != null) {
				String verkehrstagVon = searchBean.getSearchVerkehrstagBeginnDatum();
				String verkehrstagBis = searchBean.getSearchVerkehrstagEndDatum();

				Date zeitraumVerkehrstagVon = null;
				Date zeitraumVerkehrstagBis = null;
				Integer qsks = null;

				if ((verkehrstagVon != null) && !(verkehrstagVon.equals("")))
					zeitraumVerkehrstagVon = FrontendHelper.castStringToDate(verkehrstagVon);
				if ((verkehrstagBis != null) && !(verkehrstagBis.equals("")))
					zeitraumVerkehrstagBis = FrontendHelper.castStringToDate(verkehrstagBis);
				if ((searchBean.getQsks() != null) && !(searchBean.getQsks().equals("")))
					qsks = FrontendHelper.castStringToInteger(searchBean.getQsks());

				FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBZR };
				BbzrVerspaetungsminutenReportService verspaetungsminutenService = EasyServiceFactory.getInstance().createBbzrVerspaetungsminutenReportService();
				List<Integer> bbps = verspaetungsminutenService.findIDsBySearchBean(searchBean, fetchPlans);  

				
				Report report = verspaetungsminutenService.buildBbzrVerspaetungsminutenReport( 	bbps, 
																								zeitraumVerkehrstagVon, zeitraumVerkehrstagBis,
																								qsks);

				request.setAttribute("reportBeanGesamt", report.getResultGesamt());
				request.setAttribute("reportBeanEVU", report.getResultEVU());
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
}
