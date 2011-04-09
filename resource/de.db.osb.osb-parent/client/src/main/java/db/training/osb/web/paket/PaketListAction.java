package db.training.osb.web.paket;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.tags.TableTagParameters;
import org.hibernate.criterion.Order;

import db.training.bob.service.FetchPlan;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Paket;

public class PaketListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(PaketListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering PaketListAction.");

		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null)
			listPageNumber = 1;

		int count = PaginatedList.LIST_OBJECTS_PER_PAGE;
		int start = (listPageNumber - 1) * count;
		if (request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null) {
			count = 100000;
			start = 0;
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
			listSortColumn = "kurzname";
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

		PaginatedList<Paket> pakete = serviceFactory.createPaketService().findPaginatedAllAndSort(
		    sortOrders, start, count, sessionFahrplanjahr,
		    new FetchPlan[] { FetchPlan.OSB_PAKET_MASSNAHMEN });

		pakete.setObjectsPerPage(count);
		pakete.setPageNumber(listPageNumber);
		pakete.setSortDirection(listSortOrder);

		if (FrontendHelper.stringNotNullOrEmpty(listSortColumn)) {
			pakete.setSortCriterion(listSortColumn);
			request.setAttribute("sort", listSortColumn);
		}

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// baumassnahmeList.getSortDirection nicht korrekt auswertet
		if (listSortOrder == SortOrderEnum.ASCENDING) {
			request.setAttribute("dir", "asc");
		} else if (listSortOrder == SortOrderEnum.DESCENDING) {
			request.setAttribute("dir", "desc");
		}

		request.setAttribute("pakete", pakete);

		// TODO if failure
		return mapping.findForward("SUCCESS");
	}
}