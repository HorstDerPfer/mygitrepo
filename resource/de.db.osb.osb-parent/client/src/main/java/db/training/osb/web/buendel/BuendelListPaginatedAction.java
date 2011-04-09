package db.training.osb.web.buendel;

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
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Buendel;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.BuendelService;
import db.training.security.hibernate.TqmUser;

public class BuendelListPaginatedAction extends BaseAction {

	private static final Logger log = Logger.getLogger(BuendelListPaginatedAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BuendelListPaginatedAction.");

		TqmUser secUser = getSecUser();

		// Rechtepruefung
		if (!secUser.hasAuthorization("ROLE_BUENDEL_LESEN_ALLE")
		    && !secUser.hasAuthorization("ROLE_BUENDEL_LESEN_REGIONALBEREICH"))
			return mapping.findForward("ACCESS_DENIED");

		BuendelSearchForm filterForm = (BuendelSearchForm) form;
		if (filterForm == null)
			filterForm = new BuendelSearchForm();

		PaginatedList<Buendel> buendelList = null;

		// get search parameters
		if (log.isDebugEnabled()) {
			log.debug("BuendelListPaginatedAction parameter: regionalbereichId="
			    + filterForm.getRegionalbereichId());
			log.debug("BuendelListPaginatedAction parameter: buendelName="
			    + filterForm.getSearchBuendelName());
			log.debug("BuendelListPaginatedAction parameter: hauptstrecke="
			    + filterForm.getHauptStreckeNummer());
		}

		// es gibt format fehler
		if (hasErrors()) {
			if (log.isDebugEnabled())
				log.debug("forward to FAILURE");

			return mapping.findForward("FAILURE");
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
				log.debug("BaumassnahmeSearchForm.reset()");
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
			listSortColumn = "id";
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

		// BuendelName
		if (filterForm.getSearchBuendelName() != null && !filterForm.getSearchBuendelName().equals("")) {
			searchCritieria.put(BuendelService.BUENDEL_NAME, filterForm.getSearchBuendelName());
		}

		// BuendelId
		if (filterForm.getBuendelKennung() != null && !filterForm.getBuendelKennung().equals("")) {
			if (!filterForm.getBuendelKennung().matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}")) {
				addError("error.buendel.buendelId");
			} else
				searchCritieria.put(BuendelService.BUENDEL_ID, filterForm.getBuendelKennung());
		}

		// Bst von
		if (filterForm.getBetriebsstelleVon() != null
		    && !filterForm.getBetriebsstelleVon().equals("")) {
			BetriebsstelleService bstService = EasyServiceFactory.getInstance()
			    .createBetriebsstelleService();
			Betriebsstelle bstVon = bstService.findByKuerzelAndFahrplanjahr(bstService
			    .castCaptionToKuerzel(filterForm.getBetriebsstelleVon()), sessionFahrplanjahr);
			searchCritieria.put(BuendelService.BUENDEL_BST_VON, bstVon);
		}

		// Bst bis
		if (filterForm.getBetriebsstelleBis() != null
		    && !filterForm.getBetriebsstelleBis().equals("")) {
			BetriebsstelleService bstService = EasyServiceFactory.getInstance()
			    .createBetriebsstelleService();
			Betriebsstelle bstBis = bstService.findByKuerzelAndFahrplanjahr(bstService
			    .castCaptionToKuerzel(filterForm.getBetriebsstelleBis()), sessionFahrplanjahr);
			searchCritieria.put(BuendelService.BUENDEL_BST_BIS, bstBis);
		}

		// Region
		if (filterForm.getRegionalbereichId() != null
		    && filterForm.getRegionalbereichId().intValue() != 0) {
			searchCritieria.put(BuendelService.REGION, filterForm.getRegionalbereichId());
		}

		// Streckennummer
		if (filterForm.getHauptStreckeNummer() != null
		    && !filterForm.getHauptStreckeNummer().equals("")) {
			Integer streckeNummer = VzgStrecke.getId(filterForm.getHauptStreckeNummer());
			if (streckeNummer != null)
				// BuendelService.VZG_STRECKE
				searchCritieria.put(BuendelService.VZG_STRECKE, streckeNummer);
		}

		// Fahrplanjahr
		searchCritieria.put(BuendelService.FAHRPLANJAHR, sessionFahrplanjahr);

		Integer pageSize = filterForm.getPageSize();
		int lowerLimit = (listPageNumber - 1) * pageSize;
		if (request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null) {
			pageSize = 100000;
			listPageNumber = 1;
			lowerLimit = 0;
		}

		// Suchen
		buendelList = EasyServiceFactory.getInstance().createBuendelService().findPaginatedBySort(
		    sortOrders, searchCritieria, lowerLimit, pageSize);

		buendelList.setObjectsPerPage(pageSize);
		buendelList.setPageNumber(listPageNumber);
		buendelList.setSortDirection(listSortOrder);

		if (request.getParameter("sort") != null) {
			buendelList.setSortCriterion(request.getParameter("sort"));
		} else {
			buendelList.setSortCriterion("buendelId");
			buendelList.setSortDirection(listSortOrder);
		}

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// buendelList.getSortDirection nicht korrekt auswertet
		if (buendelList.getSortDirection() == SortOrderEnum.ASCENDING)
			request.setAttribute("dir", "asc");
		if (buendelList.getSortDirection() == SortOrderEnum.DESCENDING)
			request.setAttribute("dir", "desc");

		request.setAttribute("buendelList", buendelList);
		
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
