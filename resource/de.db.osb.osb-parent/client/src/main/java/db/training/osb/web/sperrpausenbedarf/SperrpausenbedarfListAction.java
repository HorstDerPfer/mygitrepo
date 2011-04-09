package db.training.osb.web.sperrpausenbedarf;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Paket;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.PaketService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.security.hibernate.TqmUser;

public class SperrpausenbedarfListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SperrpausenbedarfListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering SperrpausenbedarfListAction.");

		TqmUser secUser = getSecUser();
		
		// korrekte Startseite anzeigen
		if (!secUser.hasRole("BENUTZER_OSB_TOOL")) {
			if (log.isDebugEnabled())
				log.debug("kein OSB-Benutzer, pruefe auf BOB-Benutzer.");
			if (secUser.hasRole("BENUTZER_BOB_TOOL")) {
				if (log.isDebugEnabled())
					log.debug("BOB-Benutzer, leite auf BOB-Startseite um.");
				return mapping.findForward("BOB_START");
			}
			if (log.isDebugEnabled())
				log.debug("Weder BOB, noch OSB-Benutzer, leite auf Kontaktseite um.");
			addError("common.noAuth");
			return mapping.findForward("NO_AUTH");
		} else if (secUser.hasRole("AUSWERTER_REGIONAL") || secUser.hasRole("AUSWERTER_ZENTRAL")) {
			return mapping.findForward("AUSWERTER_START");
		}

		SperrpausenbedarfFilterForm filterForm = (SperrpausenbedarfFilterForm) form;
		if (filterForm == null)
			filterForm = new SperrpausenbedarfFilterForm();

		if (request.getParameter("reset") != null) {
			filterForm.reset();
		}
		
		if (hasUserRestrictedAccess(secUser)) {
			if (filterForm.getRegionalbereichId() == null)
				filterForm.setRegionalbereichId(getLoginUser(request).getRegionalbereich().getId());
		}
				
		PaginatedList<? extends SAPMassnahme> massnahmen = new PaginatedList<SAPMassnahme>();

		// get search parameters
		if (log.isDebugEnabled()) {
			log.debug("SperrpausenbedarfeListAction parameter: regionalbereichId="
			    + filterForm.getRegionalbereichId());
			log.debug("SperrpausenbedarfeListAction parameter: anmelder="
			    + filterForm.getAnmelder());
			log.debug("SperrpausenbedarfeListAction parameter: gewerk=" + filterForm.getGewerk());
			log.debug("SperrpausenbedarfeListAction parameter: paket=" + filterForm.getPaket());
			log.debug("SperrpausenbedarfeListAction parameter: streckeVZG="
			    + filterForm.getStreckeVZG());
			log.debug("SperrpausenbedarfeListAction parameter: untergewerk="
			    + filterForm.getUntergewerk());
			log.debug("SperrpausenbedarfeListAction parameter: sapProjektnummer="
			    + filterForm.getSapProjektnummer());
		}

		// Validierung PaketId
		if (filterForm.getPaket() != null && filterForm.getPaket().trim().length() > 0) {
			if (!filterForm.getPaket().trim().matches(
			    "[0-9][0-9][.][0-9][0-9][.][0-9][0-9][0-9][0-9]")) {
				if (log.isDebugEnabled())
					log.debug("fehler in paketId format " + filterForm.getPaket());
				addError("error.paketId.format", "Paket");
			}
		}

		// Validierung Streckenband
		Integer hauptstreckenNummer = null;
		if (filterForm.getStreckeVZG() != null && filterForm.getStreckeVZG().trim().length() > 0) {
			String streckeVzgString = filterForm.getStreckeVZG();
			if (streckeVzgString.indexOf("[") >= 0 && streckeVzgString.indexOf("]") > 0) {
				streckeVzgString = streckeVzgString.substring(streckeVzgString.indexOf("[") + 1,
				    streckeVzgString.indexOf("]"));
			}
			hauptstreckenNummer = FrontendHelper.castStringToInteger(streckeVzgString);
			if (hauptstreckenNummer == null) {
				if (log.isDebugEnabled())
					log.debug("fehler in streckenband feld " + filterForm.getStreckeVZG());
				addError("error.hauptstreckeNr.format", "Hauptstreckennr.");
			}
		}

		// initialise list page size
		if (filterForm.getPageSize() == null || filterForm.getPageSize().intValue() <= 0) {
			filterForm.setPageSize(Integer.valueOf(SperrpausenbedarfFilterForm.DEFAULTPAGESIZE));
			massnahmen.setObjectsPerPage(filterForm.getPageSize());
		} else {
			massnahmen.setObjectsPerPage(filterForm.getPageSize());
		}
		request.setAttribute("pageSize", filterForm.getPageSize());

		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null) {
			listPageNumber = 1;
		}
		int lowerLimit = (listPageNumber - 1) * massnahmen.getObjectsPerPage();

		// fill regionalbereich liste
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		request.setAttribute("regionalbereichListe", regionalbereichService.findAllAndCache());

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
		
		
		// es gibt format fehler
		if (hasErrors()) {
			if (log.isDebugEnabled())
				log.debug("forward to FAILURE");

			request.setAttribute("baumassnahmen", massnahmen);
			return mapping.findForward("FAILURE");
		}

		// suche paket
		Integer paketId = null;

		if (filterForm.getPaket() != null && filterForm.getPaket().trim().length() >= 10) {

			Paket paket = null;

			String strPaketId = filterForm.getPaket().trim();

			PaketService paketService = EasyServiceFactory.getInstance().createPaketService();
			RegionalbereichService regionalService = EasyServiceFactory.getInstance()
			    .createRegionalbereichService();

			Regionalbereich regionalbereich = regionalService.findById(Paket
			    .getRegionalbereichFromPaketId(strPaketId));

			if (regionalbereich != null) {
				paket = paketService.findByRbFahrplanjahrAndLfdNr(regionalbereich, Paket
				    .getJahrFromPaketId(strPaketId), Paket.getLfdNrFromPaketId(strPaketId));

				if (log.isDebugEnabled() && paket != null)
					log.debug("paketId=" + paket.getId());
			}

			if (paket != null)
				paketId = paket.getId();
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
		if (listPageNumber == null) {
			listPageNumber = 1;
		}
		// Sortierparameter anpassen
		if (listSortColumn == null) {
			listSortColumn = "projektDefinitionDbBez";
			listSortOrder = SortOrderEnum.ASCENDING;
		}

		List<Order> sortOrders = new LinkedList<Order>();

		if (listSortColumn != null && listSortOrder != null) {
			if (listSortOrder == SortOrderEnum.ASCENDING) {
				sortOrders.add(Order.asc(listSortColumn));
			} else if (listSortOrder == SortOrderEnum.DESCENDING) {
				sortOrders.add(Order.desc(listSortColumn));
			}
		}
		// /////////////////////////

		SAPMassnahmeService sapMassnahmeService = serviceFactory.createSAPMassnahmeService();

		if (filterForm.getPaket() != null && filterForm.getPaket().trim().length() > 0
		    && paketId == null) {
			massnahmen = null;
		} else {

			FetchPlan[] fetchPlans = null;
			// private static final FetchPlan[] fetchPlans = new FetchPlan[] {
			// FetchPlan.OSB_REGIONALBEREICH,
			// FetchPlan.OSB_PAKET, FetchPlan.OSB_WEITERE_STRECKEN };
			boolean exporting = request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null;
			fetchPlans = exporting ? new FetchPlan[] { FetchPlan.OSB_WEITERE_STRECKEN,
			        FetchPlan.OSB_PAKET } : null;

			if (exporting) {
				massnahmen = sapMassnahmeService.findPaginatedByKeywordsAndSort(sortOrders,
				    filterForm.getRegionalbereichId(), filterForm.getAnmelder(), filterForm
				        .getGewerk(), filterForm.getUntergewerk(), hauptstreckenNummer, paketId,
				    filterForm.getSapProjektnummer(), fetchPlans, 0, 100000, sessionFahrplanjahr);
			} else {
				massnahmen = sapMassnahmeService.findReportPaginatedByKeywordsAndSort(sortOrders,
				    filterForm.getRegionalbereichId(), filterForm.getAnmelder(), filterForm
				        .getGewerk(), filterForm.getUntergewerk(), hauptstreckenNummer, paketId,
				    filterForm.getSapProjektnummer(), fetchPlans, lowerLimit, filterForm
				        .getPageSize(), sessionFahrplanjahr);
			}

			massnahmen.setObjectsPerPage(filterForm.getPageSize());
			massnahmen.setPageNumber(listPageNumber);
			massnahmen.setSortDirection(listSortOrder);
		}

		if (listSortColumn != null && FrontendHelper.stringNotNullOrEmpty(listSortColumn)) {
			massnahmen.setSortCriterion(listSortColumn);
			request.setAttribute("sort", listSortColumn);
		}

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// baumassnahmeList.getSortDirection nicht korrekt auswertet
		if (listSortOrder == SortOrderEnum.ASCENDING) {
			request.setAttribute("dir", "asc");
		} else if (listSortOrder == SortOrderEnum.DESCENDING) {
			request.setAttribute("dir", "desc");
		}

		request.setAttribute("baumassnahmen", massnahmen);

		return mapping.findForward("SUCCESS");
	}
}