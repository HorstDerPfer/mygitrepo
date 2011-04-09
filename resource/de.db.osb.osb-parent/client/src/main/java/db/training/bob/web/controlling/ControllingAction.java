package db.training.bob.web.controlling;

import java.util.ArrayList;
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
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class ControllingAction extends BaseAction {

	private static final Logger log = Logger.getLogger(ControllingAction.class);

	private BaumassnahmeService baumassnahmeService;

	private RegionalbereichService regionalbereichService;

	public ControllingAction() {
		EasyServiceFactory serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		regionalbereichService = serviceFactory.createRegionalbereichService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering ControllingAction.");

		ControllingFilterForm searchForm = (ControllingFilterForm) form;
		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true"))
			searchForm.reset();

		List<Baumassnahme> listBM = new ArrayList<Baumassnahme>();
		List<Regionalbereich> listRegionalbereich = new ArrayList<Regionalbereich>();

		FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BEARBEITUNGSBEREICH,
		        FetchPlan.BOB_TERMINE_BBP };

		try {
			if ((searchForm != null)
			    && ((searchForm.getBeginnDatum() != null) || (searchForm.getEndDatum() != null) || (searchForm
			        .getMeilenstein() != null))) {
				// Suchparameter
				SearchBean searchBean = new SearchBean();
				searchBean.setSearchBeginnDatum(searchForm.getBeginnDatum());
				searchBean.setSearchEndDatum(searchForm.getEndDatum());
				searchBean.setSearchRegionalbereichFpl(searchForm.getRegionalbereich());
				searchBean.setSearchMeilenstein(searchForm.getMeilenstein());

				listBM = baumassnahmeService.findBySearchBean(searchBean, fetchPlans);
			} else {
				listBM = baumassnahmeService.findBySearchBean(null, null, null, null, fetchPlans)
				    .getList();
			}

			listRegionalbereich = regionalbereichService.findAll();

		} catch (LazyInitializationException e) {
			if (log.isDebugEnabled())
				log.debug(e.getMessage());
			if (log.isDebugEnabled())
				log.debug(e);
			return mapping.findForward("FAILURE");
		}
		request.setAttribute("baumassnahmen", listBM);
		request.setAttribute("regionalbereiche", listRegionalbereich);

		return mapping.findForward("SUCCESS");
	}

	public ActionForward testHelp(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		return run(mapping, form, request, response);
	}
}
