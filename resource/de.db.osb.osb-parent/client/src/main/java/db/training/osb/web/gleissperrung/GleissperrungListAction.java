package db.training.osb.web.gleissperrung;

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

import db.training.bob.service.RegionalbereichService;
import db.training.bob.util.CollectionHelper;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.VzgStreckeService;
import db.training.security.hibernate.TqmUser;

public class GleissperrungListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungListAction.class);
	
	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungListAction.");

		TqmUser secUser = getSecUser();

		// Rechtepruefung
		if (!secUser.hasAuthorization("ROLE_GLEISSPERRUNG_LESEN_ALLE")
		    && !secUser.hasAuthorization("ROLE_GLEISSPERRUNG_LESEN_REGIONALBEREICH"))
			return mapping.findForward("ACCESS_DENIED");

		GleissperrungFilterForm filterForm = (GleissperrungFilterForm) form;

		if (filterForm == null)
			filterForm = new GleissperrungFilterForm();
		
		// initialise list page size
		request.setAttribute("pageSize", filterForm.getPageSize());
		
		if (request.getParameter("reset") != null) {
			filterForm.reset();
		}
		
		if (hasUserRestrictedAccess(secUser)) {
			if (filterForm.getRegionalbereichId() == null)
				filterForm.setRegionalbereichId(getLoginUser(request).getRegionalbereich().getId());
		}

		// fill regionalbereich liste
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		request.setAttribute("regionalbereichListe", regionalbereichService.findAllAndCache());

		Map<String, Object> searchCriteria = new HashMap<String, Object>();

		// Regionalbereich
		if (filterForm.getRegionalbereichId() != null
		    && !filterForm.getRegionalbereichId().equals(0)) {
			searchCriteria.put("REGIONALBEREICH", regionalbereichService.findById(filterForm
			    .getRegionalbereichId()));
		}
		// VZG-Strecke
		VzgStrecke vzgStrecke = null;
		if (FrontendHelper.stringNotNullOrEmpty(filterForm.getStreckeVZG())) {
			VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
			Integer vzgStreckeNummer = vzgService.castCaptionToNummer(filterForm.getStreckeVZG());
			if (vzgStreckeNummer != null) {
				vzgStrecke = CollectionHelper.getFirst(serviceFactory.createVzgStreckeService()
				    .findByNummer(vzgStreckeNummer, sessionFahrplanjahr, null, true, null));
				if (vzgStrecke != null)
					searchCriteria.put("VZG_STRECKE", vzgStrecke);
			}
		}

		// Betriebsstellen
		BetriebsstelleService betriebsstelleService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();
		// BetriebsstelleVon
		if (FrontendHelper.stringNotNullOrEmpty(filterForm.getBetriebsstelleVon())) {
			searchCriteria.put("BST_VON", betriebsstelleService.findByKuerzelAndFahrplanjahr(
			    betriebsstelleService.castCaptionToKuerzel(filterForm.getBetriebsstelleVon()),
			    sessionFahrplanjahr));
		}
		// BetriebsstelleBis
		if (FrontendHelper.stringNotNullOrEmpty(filterForm.getBetriebsstelleBis())) {
			searchCriteria.put("BST_BIS", betriebsstelleService.findByKuerzelAndFahrplanjahr(
			    betriebsstelleService.castCaptionToKuerzel(filterForm.getBetriebsstelleBis()),
			    sessionFahrplanjahr));
		}

		// PageNumber
		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null)
			listPageNumber = 1;
		int count = PaginatedList.LIST_OBJECTS_PER_PAGE;
		if (filterForm.getPageSize() != null && !filterForm.getPageSize().equals(0)) {
			count = filterForm.getPageSize();
		}
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
			listSortColumn = "bezeichnung";
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

		GleissperrungService gsService = serviceFactory.createGleissperrungService();
		PaginatedList<Gleissperrung> gleissperrungen = gsService.findPaginatedBySort(sortOrders,
		    searchCriteria, start, count, new Preload[] {
		            new Preload(Gleissperrung.class, "massnahme"),
		            new Preload(Gleissperrung.class, "vtr"),
		            new Preload(Gleissperrung.class, "buendel"),
		            new Preload(Gleissperrung.class, "vzgStrecke") }, sessionFahrplanjahr);

		gleissperrungen.setObjectsPerPage(count);
		gleissperrungen.setPageNumber(listPageNumber);
		gleissperrungen.setSortDirection(listSortOrder);
		gleissperrungen.setSortCriterion(listSortColumn);
		request.setAttribute("sort", listSortColumn);

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// baumassnahmeList.getSortDirection nicht korrekt auswertet
		if (listSortOrder == SortOrderEnum.ASCENDING) {
			request.setAttribute("dir", "asc");
		} else if (listSortOrder == SortOrderEnum.DESCENDING) {
			request.setAttribute("dir", "desc");
		}

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

		request.setAttribute("gleissperrungen", gleissperrungen);

		return mapping.findForward("SUCCESS");
	}
}
