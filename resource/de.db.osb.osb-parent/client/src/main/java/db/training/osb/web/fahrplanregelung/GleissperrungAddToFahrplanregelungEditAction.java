package db.training.osb.web.fahrplanregelung;

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
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.VzgStreckeService;

public class GleissperrungAddToFahrplanregelungEditAction extends BaseAction {

	private static final Logger log = Logger
	    .getLogger(GleissperrungAddToFahrplanregelungEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungAddToFahrplanregelungEditAction.");

		FahrplanregelungGleissperrungFilterForm fplrForm = (FahrplanregelungGleissperrungFilterForm) form;

		if (request.getParameter("reset") != null
		    && request.getParameter("reset").equalsIgnoreCase("true"))
			fplrForm.reset();

		if (request.getParameter("fahrplanregelungId") != null)
			fplrForm.setFahrplanregelungId(Integer.parseInt(request
			    .getParameter("fahrplanregelungId")));

		if (fplrForm.getFahrplanregelungId() == null) {
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
		}

		if (hasErrors()) {
			return mapping.findForward("FAILURE");
		}

		// Services
		FahrplanregelungService fplrService = serviceFactory.createFahrplanregelungService();

		// Fahrplanregelung zur Ansicht laden
		Fahrplanregelung fplr = fplrService.findById(fplrForm.getFahrplanregelungId(),
		    new Preload[] {});

		// fill regionalbereich liste fuer Filter
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		request.setAttribute("regionalbereichListe", regionalbereichService.findAllAndCache());

		Map<String, Object> searchCriteria = new HashMap<String, Object>();

		// Regionalbereich
		if (fplrForm.getRegionalbereichId() != null && !fplrForm.getRegionalbereichId().equals(0)) {
			searchCriteria.put("REGIONALBEREICH", regionalbereichService.findById(fplrForm
			    .getRegionalbereichId()));
		}
		// VZG-Strecke
		VzgStrecke vzgStrecke = null;
		if (FrontendHelper.stringNotNullOrEmpty(fplrForm.getStreckeVZG())) {
			VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
			Integer vzgStreckeNummer = vzgService.castCaptionToNummer(fplrForm.getStreckeVZG());
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
		if (FrontendHelper.stringNotNullOrEmpty(fplrForm.getBetriebsstelleVon())) {
			searchCriteria.put("BST_VON", betriebsstelleService.findByKuerzelAndFahrplanjahr(
			    betriebsstelleService.castCaptionToKuerzel(fplrForm.getBetriebsstelleVon()),
			    sessionFahrplanjahr));
		}
		// BetriebsstelleBis
		if (FrontendHelper.stringNotNullOrEmpty(fplrForm.getBetriebsstelleBis())) {
			searchCriteria.put("BST_BIS", betriebsstelleService.findByKuerzelAndFahrplanjahr(
			    betriebsstelleService.castCaptionToKuerzel(fplrForm.getBetriebsstelleBis()),
			    sessionFahrplanjahr));
		}

		// PageNumber
		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null)
			listPageNumber = 1;
		int count = PaginatedList.LIST_OBJECTS_PER_PAGE;
		if (fplrForm.getPageSize() != null && !fplrForm.getPageSize().equals(0)) {
			count = fplrForm.getPageSize();
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

		// List<Gleissperrung> gleissperrungen = gsService.findAll(new Preload[] {
		// new Preload(Gleissperrung.class, "massnahme"),
		// new Preload(Gleissperrung.class, "vtr"),
		// new Preload(Gleissperrung.class, "buendel"),
		// new Preload(Gleissperrung.class, "vzgStrecke") });

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

		request.setAttribute("gleissperrungen", gleissperrungen);
		request.setAttribute("fahrplanregelung", fplr);
		return mapping.findForward("SUCCESS");
	}
}
