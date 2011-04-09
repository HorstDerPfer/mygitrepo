package db.training.osb.web.umleitung;

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
import db.training.osb.model.Umleitung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.UmleitungService;
import db.training.security.hibernate.TqmUser;

public class UmleitungListPaginatedAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungListPaginatedAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungListPaginatedAction.");

		TqmUser secUser = getSecUser();

		// Rechtepruefung
		if (!secUser.hasAuthorization("ROLE_UMLEITUNG_LESEN_ALLE")
		    && !secUser.hasAuthorization("ROLE_UMLEITUNG_LESEN_REGIONALBEREICH"))
			return mapping.findForward("ACCESS_DENIED");

		UmleitungSearchForm filterForm = (UmleitungSearchForm) form;
		if (filterForm == null)
			filterForm = new UmleitungSearchForm();

		PaginatedList<Umleitung> umleitungList = null;

		// get search parameters
		if (log.isDebugEnabled()) {
			log.debug("UmleitungListPaginatedAction parameter: name=" + filterForm.getName());
			log.debug("UmleitungListPaginatedAction parameter: betriebstelle="
			    + filterForm.getBetriebsStelle());
			log.debug("UmleitungListPaginatedAction parameter: VzgStreckennummer="
			    + filterForm.getVzgStreckenNummer());
			log.debug("UmleitungListPaginatedAction parameter: RegionalbereichId="
			    + filterForm.getRegionalbereichId());
		}

		// fill regionalbereich liste
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		request.setAttribute("regionalbereichListe", regionalbereichService.findAll());

		// initialise list page size
		request.setAttribute("pageSize", filterForm.getPageSize());

		// Default-Werte setzen
		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true")) {
			if (log.isDebugEnabled())
				log.debug("UmleitungSearchForm.reset()");
			filterForm.reset();
		}
		
		if (hasUserRestrictedAccess(secUser)) {
			if (filterForm.getRegionalbereichId() == null)
				filterForm.setRegionalbereichId(getLoginUser(request).getRegionalbereich().getId());
		}

		// Suche wird ausgefuehrt
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

		// Fahrplanjahr
		searchCritieria.put(UmleitungService.FAHRPLANJAHR, sessionFahrplanjahr);

		// Name
		if (filterForm.getName() != null && !filterForm.getName().equals("")) {
			searchCritieria.put(UmleitungService.NAME, filterForm.getName());
		}

		BetriebsstelleService betriebsstelleService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		// BetriebstelleVon
		if (filterForm.getBetriebsStelle() != null && !filterForm.getBetriebsStelle().equals("")) {
			Integer bstId = betriebsstelleService.castCaptionWithIDToID(filterForm
			    .getBetriebsStelle());
			String bstName = betriebsstelleService.castCaptionWithIDToKuerzel(filterForm
			    .getBetriebsStelle());
			if (bstId != null) {
				searchCritieria.put(UmleitungService.BETRIEBSTELLE, bstName);
			}
		}
		// VzgStreckennummer
		Integer vzgStrecke = VzgStrecke.getId(filterForm.getVzgStreckenNummer());
		if (vzgStrecke != null) {
			searchCritieria.put(UmleitungService.VZG_STRECKE, vzgStrecke);
		}

		// Regionalbereich liegt hier:
		// Umleitung
		// -> UmleitungFahrplanregelungLink
		// -> Fahrplanregelung
		// -> Regionalbereich (ID)
		Integer regionalId = filterForm.getRegionalbereichId();
		if (regionalId != null && regionalId != 0) {
			searchCritieria.put(UmleitungService.RB_BEREICH, regionalId);
		}

		Integer pageSize = filterForm.getPageSize();
		int lowerLimit = (listPageNumber - 1) * pageSize;
		if (request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null) {
			pageSize = 100000;
			listPageNumber = 1;
			lowerLimit = 0;
		}

		// Suchen
		umleitungList = EasyServiceFactory.getInstance().createUmleitungService()
		    .findPaginatedBySort(
		        sortOrders,
		        searchCritieria,
		        lowerLimit,
		        pageSize,
		        new FetchPlan[] { FetchPlan.OSB_UML_UMLEITUNGSWEGE,
		                FetchPlan.OSB_UML_FAHRPLANREGELUNGEN });

		umleitungList.setObjectsPerPage(pageSize);
		umleitungList.setPageNumber(listPageNumber);
		umleitungList.setSortDirection(listSortOrder);

		if (request.getParameter("sort") != null) {
			umleitungList.setSortCriterion(request.getParameter("sort"));
		} else {
			umleitungList.setSortCriterion("name");
			umleitungList.setSortDirection(listSortOrder);
		}

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// fahrplanregelungList.getSortDirection nicht korrekt auswertet
		if (umleitungList.getSortDirection() == SortOrderEnum.ASCENDING)
			request.setAttribute("dir", "asc");
		if (umleitungList.getSortDirection() == SortOrderEnum.DESCENDING)
			request.setAttribute("dir", "desc");

		request.setAttribute("umleitungenList", umleitungList);

		return mapping.findForward("SUCCESS");
	}
}