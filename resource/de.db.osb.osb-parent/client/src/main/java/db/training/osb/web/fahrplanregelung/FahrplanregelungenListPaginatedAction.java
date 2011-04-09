package db.training.osb.web.fahrplanregelung;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.tags.TableTagParameters;
import org.hibernate.criterion.Order;

import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.service.FahrplanregelungService;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungenListPaginatedAction extends BaseAction {

	private static final Logger log = Logger.getLogger(FahrplanregelungenListPaginatedAction.class);
	
	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungenListPaginatedAction.");

		//User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		// Rechtepruefung
		if (!secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_LESEN_ALLE")
		    && !secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_LESEN_REGIONALBEREICH"))
			return mapping.findForward("ACCESS_DENIED");

		FahrplanregelungSearchForm filterForm = (FahrplanregelungSearchForm) form;
		if (filterForm == null)
			filterForm = new FahrplanregelungSearchForm();

		PaginatedList<Fahrplanregelung> fahrplanregelungList = null;

		// initialise list page size
		request.setAttribute("pageSize", filterForm.getPageSize());

		// Default-Werte setzen
		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true")) {
			if (log.isDebugEnabled())
				log.debug("FahrplanregelungSearchForm.reset()");
			filterForm.reset();
		}
		
		if (hasUserRestrictedAccess(secUser)) {
			if (filterForm.getRegionalbereichId() == null)
				filterForm.setRegionalbereichId(getLoginUser(request).getRegionalbereich().getId());
		}

		// Paging/Sorting
		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		String listSortColumn = request.getParameter("sort");

		SortOrderEnum listSortOrder = null;
		if ("asc".equalsIgnoreCase(request.getParameter("dir"))) {
			listSortOrder = SortOrderEnum.ASCENDING;
		} else if ("desc".equalsIgnoreCase(request.getParameter("dir"))) {
			listSortOrder = SortOrderEnum.DESCENDING;
		}
		if (listPageNumber == null) {
			listPageNumber = 1;
		}
		if (listSortColumn == null) {
			listSortColumn = "name";
			listSortOrder = SortOrderEnum.ASCENDING;
		}

		List<Order> sortOrders = new LinkedList<Order>();

		if (listSortOrder != null) {
			if (listSortOrder == SortOrderEnum.ASCENDING) {
				sortOrders.add(Order.asc(listSortColumn));
			} else if (listSortOrder == SortOrderEnum.DESCENDING) {
				sortOrders.add(Order.desc(listSortColumn));
			}
		}

		// Suchkriterien zusammenstellen
		Map<String, Object> searchCritieria = new HashMap<String, Object>();

		// Name
		if (filterForm.getName() != null && !filterForm.getName().equals("")) {
			searchCritieria.put(FahrplanregelungService.NAME, filterForm.getName());
		}
		// Nummer
		if (filterForm.getNummer() != null && !filterForm.getNummer().equals("")) {
			searchCritieria.put(FahrplanregelungService.NUMMER, FrontendHelper
			    .castStringToInteger(filterForm.getNummer()));
		}
		// Region
		if (filterForm.getRegionalbereichId() != null
		    && filterForm.getRegionalbereichId().intValue() != 0) {
			searchCritieria.put(FahrplanregelungService.REGION, filterForm.getRegionalbereichId());
		}

		// Fahrplanjahr
		searchCritieria.put(FahrplanregelungService.FAHRPLANJAHR, sessionFahrplanjahr);

		// Buendel
		if (FrontendHelper.castStringToInteger(request.getParameter("buendelId")) != null) {
			Integer buendelId = FrontendHelper.castStringToInteger(request
			    .getParameter("buendelId"));
			Buendel buendel = EasyServiceFactory.getInstance().createBuendelService().findById(
			    buendelId);
			searchCritieria.put(FahrplanregelungService.BUENDEL, buendelId);
			request.setAttribute("buendel", buendel);
		}

		Integer pageSize = filterForm.getPageSize();
		int lowerLimit = (listPageNumber - 1) * pageSize;
		if (request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null) {
			pageSize = 100000;
			listPageNumber = 1;
			lowerLimit = 0;
		}

		// Suchen
		fahrplanregelungList = EasyServiceFactory.getInstance().createFahrplanregelungService()
		    .findPaginatedBySort(
		        sortOrders,
		        searchCritieria,
		        lowerLimit,
		        pageSize,
		        new FetchPlan[] { FetchPlan.OSB_FPL_BST_VON, FetchPlan.OSB_FPL_BST_BIS,
		                FetchPlan.OSB_FPL_REGIONALBEREICH, FetchPlan.OSB_FPL_BUENDEL });

		fahrplanregelungList.setObjectsPerPage(pageSize);
		fahrplanregelungList.setPageNumber(listPageNumber);
		fahrplanregelungList.setSortDirection(listSortOrder);

		if (request.getParameter("sort") != null) {
			fahrplanregelungList.setSortCriterion(request.getParameter("sort"));
		} else {
			fahrplanregelungList.setSortCriterion("fahrplanregelungId");
			fahrplanregelungList.setSortDirection(listSortOrder);
		}

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// fahrplanregelungList.getSortDirection nicht korrekt auswertet
		if (fahrplanregelungList.getSortDirection() == SortOrderEnum.ASCENDING)
			request.setAttribute("dir", "asc");
		if (fahrplanregelungList.getSortDirection() == SortOrderEnum.DESCENDING)
			request.setAttribute("dir", "desc");

		request.setAttribute("fahrplanregelungenList", fahrplanregelungList);

		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		request.setAttribute("regionalbereichListe", regionalbereichService.findAll());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat dateFormatToCompare = new SimpleDateFormat("yyyy.MM.dd");
		String datumVonToCompare = null;
		String datumBisToCompare = null;

		if (FrontendHelper.castStringToDate(request.getParameter("datumBis")) != null) {
			filterForm.setDatumBis(dateFormat.format(FrontendHelper.castStringToDate(request
			    .getParameter("datumBis"))));
			datumBisToCompare = dateFormatToCompare.format(FrontendHelper.castStringToDate(request
			    .getParameter("datumBis")));
		} else if (filterForm.getDatumBis() != null) {
			datumBisToCompare = dateFormatToCompare.format(FrontendHelper
			    .castStringToDate(filterForm.getDatumBis()));
			filterForm.setDatumBis(filterForm.getDatumBis());
		} else {
			filterForm.setDatumBis(dateFormat.format(cal.getTime()));
			datumBisToCompare = dateFormatToCompare.format(cal.getTime());
		}

		if (FrontendHelper.castStringToDate(request.getParameter("datumVon")) != null) {
			filterForm.setDatumVon(dateFormat.format(FrontendHelper.castStringToDate(request
			    .getParameter("datumVon"))));
			datumVonToCompare = dateFormatToCompare.format(FrontendHelper.castStringToDate(request
			    .getParameter("datumVon")));
		} else if (filterForm.getDatumVon() != null) {
			datumVonToCompare = dateFormatToCompare.format(FrontendHelper
			    .castStringToDate(filterForm.getDatumVon()));
			filterForm.setDatumVon(filterForm.getDatumVon());

		} else {
			cal.add(Calendar.DAY_OF_MONTH, -7);
			filterForm.setDatumVon(dateFormat.format(cal.getTime()));
			datumVonToCompare = dateFormatToCompare.format(cal.getTime());
		}

		request.setAttribute("datumVonToCompare", datumVonToCompare);
		request.setAttribute("datumBisToCompare", datumBisToCompare);

		return mapping.findForward("SUCCESS");
	}

}
