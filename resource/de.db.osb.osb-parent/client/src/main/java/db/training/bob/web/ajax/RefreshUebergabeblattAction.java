package db.training.bob.web.ajax;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.properties.SortOrderEnum;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Zug;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.bob.web.baumassnahme.ZugComparator;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshUebergabeblattAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshUebergabeblattAction.class);

	private static final int OBJECTS_PER_PAGE = 10;

	private BaumassnahmeService baumassnahmeService;

	public RefreshUebergabeblattAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
	}

	@Override
	public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshUebergabeblattAction.");

		BaumassnahmeForm bmForm = (BaumassnahmeForm) form;
		Integer bmId = null;
		String sortCriterion = null;
		SortOrderEnum order = null;
		Baumassnahme bm = null;
		int page = 1;

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("id"))) {
			bmId = FrontendHelper.castStringToInteger(request.getParameter("id"));
		}
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("showZuege"))) {
			bmForm.setShowZuegeUeb(FrontendHelper.castStringToBoolean(request
			    .getParameter("showZuege")));
		}
		if ("asc".equalsIgnoreCase(request.getParameter("dir"))) {
			order = SortOrderEnum.ASCENDING;
		} else if ("desc".equalsIgnoreCase(request.getParameter("dir"))) {
			order = SortOrderEnum.DESCENDING;
		} else {
			order = SortOrderEnum.ASCENDING;
		}

		sortCriterion = request.getParameter("sort");

		if (bmId != null) {
			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_AENDERUNGSDOKUMENTATION,
			        FetchPlan.BOB_ARBEITSLEISTUNG_REGIONALBEREICHE, FetchPlan.BOB_AUSFALLGRUND,
			        FetchPlan.BOB_BBP_MASSNAHME, FetchPlan.BOB_BEARBEITER,
			        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_KIGBAU, FetchPlan.BOB_QS,
			        FetchPlan.BOB_KORRIDOR_ZEITFENSTER, FetchPlan.BOB_REGIONALBEREICH_FPL,
			        FetchPlan.BOB_TERMINE_BBP, FetchPlan.BOB_TERMINE_GEVU,
			        FetchPlan.BOB_TERMINE_PEVU, FetchPlan.BOB_UEBERGABEBLATT,
			        FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH, FetchPlan.UEB_BAUMASSNAHMEN,
			        FetchPlan.UEB_HEADER, FetchPlan.UEB_HEADER_SENDER,
			        FetchPlan.UEB_HEADER_EMPFAENGER, FetchPlan.UEB_BEARBEITUNGSSTATUS,
			        FetchPlan.BOB_BBZR, FetchPlan.BBZR_HEADER, FetchPlan.BBZR_BAUMASSNAHMEN,
			        FetchPlan.UEB_REGIONALBEREICHE, FetchPlan.UEB_MN_FPLO,
			        FetchPlan.UEB_REGIONALBEREICHE, FetchPlan.ZVF_MN_STRECKEN,
			        FetchPlan.ZVF_MN_BBPSTRECKE, FetchPlan.ZVF_MN_STRECKE_STRECKEVZG,
			        FetchPlan.ZVF_MN_VERSION, FetchPlan.BOB_ZUGCHARAKTERISTIK,
			        FetchPlan.BBZR_MN_ZUEGE, FetchPlan.UEB_ZUG_REGELWEG, FetchPlan.UEB_MN_ZUEGE,
			        FetchPlan.UEB_KNOTENZEITEN };
			bm = baumassnahmeService.findById(bmId, fetchPlans);
		}

		PaginatedList<Zug> uebZuege = new PaginatedList<Zug>();
		setAttributes(uebZuege, bm.getUebergabeblatt().getMassnahmen().iterator().next().getZug(),
		    sortCriterion, order, page);
		request.setAttribute("uebZugCollection", uebZuege);
		request.setAttribute("baumassnahme", bm);

		if (bm.getZvf() != null) {
			if (bm.getAktuelleZvf() != null) {
				request.setAttribute("selectedBbzrID", bm.getAktuelleZvf().getId());
			}
		}
		request.setAttribute("UMLEITUNG", new PaginatedList<Zug>());
		request.setAttribute("VERSPAETUNG", new PaginatedList<Zug>());
		request.setAttribute("AUSFALL", new PaginatedList<Zug>());
		request.setAttribute("VORPLAN", new PaginatedList<Zug>());
		request.setAttribute("ERSATZHALTE", new PaginatedList<Zug>());
		request.setAttribute("GESPERRT", new PaginatedList<Zug>());
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		if (request.getParameter("dir") == null)
			return mapping.findForward("SUCCESS");

		request.setAttribute("tab", "UEBERGABEBLATT");
		return mapping.findForward("SUCCESS_SORT");
	}

	private void setAttributes(PaginatedList<Zug> paginatedList, List<Zug> list,
	    String sortCriterion, SortOrderEnum order, int page) {
		Collections.sort(list, new ZugComparator(sortCriterion, order));
		paginatedList.setList(list);
		paginatedList.setSortCriterion(sortCriterion);
		paginatedList.setSortDirection(order);
		paginatedList.setObjectsPerPage(OBJECTS_PER_PAGE);
		paginatedList.setPageNumber(page);
	}
}
