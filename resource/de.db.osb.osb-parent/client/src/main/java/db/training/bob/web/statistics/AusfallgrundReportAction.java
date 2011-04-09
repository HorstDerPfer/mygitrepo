package db.training.bob.web.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.SearchBean;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;

public class AusfallgrundReportAction extends BaseSearchAction {

	private static final Logger log = Logger.getLogger(AusfallgrundReportAction.class);

	private BaumassnahmeService baumassnahmeService;

	public AusfallgrundReportAction() {
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
			log.debug("Entering AusfallgrundReportAction.");

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;

		List<Baumassnahme> bbps = new ArrayList<Baumassnahme>();

		try {
			// Suchparameter
			SearchBean searchBean = createSearchBean(filterForm);
			FetchPlan[] fetchPlans = new FetchPlan[] {};

			if (searchBean != null) {

				bbps = baumassnahmeService.findBySearchBean(searchBean, fetchPlans);
			} else {
				bbps = baumassnahmeService.findBySearchBean(null, null, null, null, fetchPlans)
				    .getList();
			}

			List<NumberOfReportBean> result = new ArrayList<NumberOfReportBean>();
			Map<String, Integer> l = baumassnahmeService.findAusfallgruende(bbps);
			for (String reason : l.keySet()) {
				result.add(new NumberOfReportBean(reason, l.get(reason)));
			}
			request.setAttribute("reportBean", result);

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
