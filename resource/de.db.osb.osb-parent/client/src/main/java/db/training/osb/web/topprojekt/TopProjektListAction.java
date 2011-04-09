package db.training.osb.web.topprojekt;

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

import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.TopProjekt;
import db.training.osb.service.TopProjektService;
import db.training.security.hibernate.TqmUser;

public class TopProjektListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(TopProjektListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering TopProjektListAction.");

		TqmUser secUser = getSecUser();

		// Rechtepruefung
		if (!secUser.hasAuthorization("ROLE_TOPPROJEKT_LESEN_ALLE")
		    && !secUser.hasAuthorization("ROLE_TOPPROJEKT_LESEN_REGIONALBEREICH"))
			return mapping.findForward("ACCESS_DENIED");

		TopProjektFilterForm filterForm = (TopProjektFilterForm) form;

		if (request.getParameter("reset") != null) {
			filterForm.reset();
		}

		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null)
			listPageNumber = 1;

		Integer count = PaginatedList.LIST_OBJECTS_PER_PAGE;
		Integer start = (listPageNumber - 1) * count;
		if (request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null) {
			count = null;
			start = null;
		}

		// ///////////////
		// Sorting
		String listSortColumn = null;
		SortOrderEnum listSortOrder = null;

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("sort"))) {
			// Sortierkriterium aus Request verwenden, wenn vorhanden
			listSortColumn = request.getParameter("sort");
		}

		if ("asc".equalsIgnoreCase(request.getParameter("dir"))) {
			listSortOrder = SortOrderEnum.ASCENDING;
		} else if ("desc".equalsIgnoreCase(request.getParameter("dir"))) {
			listSortOrder = SortOrderEnum.DESCENDING;
		}

		// Sortierparameter anpassen
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
		// /////////////////////////

		Map<String, Object> searchCriteria = new HashMap<String, Object>();
		if (filterForm.getSapProjektNummer() != null) {
			searchCriteria.put(TopProjektService.SAP_NUMMER, filterForm.getSapProjektNummer());
		}

		PaginatedList<TopProjekt> projekte = serviceFactory.createTopProjektService()
		    .findPaginatedBySort(sortOrders, searchCriteria, start, count,
		        new Preload[] { new Preload(TopProjekt.class, "regionalbereich") },
		        sessionFahrplanjahr);
		projekte.setObjectsPerPage(PaginatedList.LIST_OBJECTS_PER_PAGE);
		projekte.setPageNumber(listPageNumber);
		projekte.setSortDirection(listSortOrder);

		if (FrontendHelper.stringNotNullOrEmpty(listSortColumn)) {
			projekte.setSortCriterion(listSortColumn);
			request.setAttribute("sort", listSortColumn);
		}

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// baumassnahmeList.getSortDirection nicht korrekt auswertet
		if (listSortOrder == SortOrderEnum.ASCENDING) {
			request.setAttribute("dir", "asc");
		} else if (listSortOrder == SortOrderEnum.DESCENDING) {
			request.setAttribute("dir", "desc");
		}
		request.setAttribute("projekte", projekte);

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