package db.training.bob.web.baumassnahme;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.LazyInitializationException;
import org.hibernate.criterion.Order;

import db.training.bob.model.Aenderung;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.SearchBean;
import db.training.bob.model.SearchConfig;
import db.training.bob.model.StatusType;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.ExcelExporter;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.service.SearchConfigService;
import db.training.bob.web.baumassnahme.Column.ValueType;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseDispatchAction;
import db.training.logwrapper.Logger;
import db.training.osb.util.ConfigResources;
import db.training.security.Role;
import db.training.security.hibernate.TqmRole;
import db.training.security.hibernate.TqmUser;

public class BaumassnahmeListAction extends BaseDispatchAction {

	private Logger log;

	private static enum SchnittstelleTyp {
		BBP, PEVU, GEVU
	}

	private BaumassnahmeService baumassnahmeService;

	private RegionalbereichService regionalbereichService;

	private BearbeitungsbereichService bearbeitungsbereichService;

	private SearchConfigService searchConfigService;

	// unterscheidung für web: listview, meilensteinview
	private static FetchPlan[] fetchplansListview = new FetchPlan[] {
	        FetchPlan.BOB_REGIONALBEREICH_FPL, FetchPlan.BOB_BEARBEITUNGSBEREICH,
	        FetchPlan.BOB_BBP_MASSNAHME };

	private static FetchPlan[] fetchplansMilestoneView = new FetchPlan[] {
	        FetchPlan.BOB_BBP_MASSNAHME, FetchPlan.BOB_REGIONALBEREICH_FPL,
	        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_TERMINE_BBP,
	        FetchPlan.BOB_TERMINE_PEVU, FetchPlan.BOB_TERMINE_GEVU };

	// private static Preload[] preloads = new Preload[] {
	// new Preload(Baumassnahme.class, "regionalBereichBM"),
	// new Preload(Baumassnahme.class, "regionalBereichFpl"),
	// new Preload(Massnahme.class, "bbp"),
	// new Preload(Massnahme.class, "bearbeitungsbereich"),
	// new Preload(Massnahme.class, "baubetriebsplanung"),
	// new Preload(Massnahme.class, "gevus"), new Preload(Massnahme.class, "pevus"),
	// new Preload(Massnahme.class, "qsNr"), new Preload(Massnahme.class, "kigBauNr"),
	// new Preload(Massnahme.class, "korridorZeitfenster"),
	// new Preload(Massnahme.class, "ausfallGrund"),
	// new Preload(Massnahme.class, "aenderungen") };

	public BaumassnahmeListAction() {
		log = Logger.getLogger(this.getClass());
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		regionalbereichService = serviceFactory.createRegionalbereichService();
		bearbeitungsbereichService = serviceFactory.createBearbeitungsbereichService();
		searchConfigService = serviceFactory.createSearchConfigService();
	}

	public BaumassnahmeListAction(EasyServiceFactory serviceFactory) {
		log = Logger.getLogger(this.getClass());
		try {
			this.serviceFactory = serviceFactory;
			baumassnahmeService = serviceFactory.createBaumassnahmeService();
			regionalbereichService = serviceFactory.createRegionalbereichService();
			bearbeitungsbereichService = serviceFactory.createBearbeitungsbereichService();
		} catch (Exception e) { // do nothing
		}
	}

	public ActionForward web(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeListAction.");

		TqmUser secUser = getSecUser();

		// korrekte Startseite anzeigen
		if (!secUser.hasRole("BENUTZER_BOB_TOOL")) {
			if (log.isDebugEnabled())
				log.debug("kein BOB-Benutzer, pruefe auf OSB-Benutzer.");
			if (secUser.hasRole("BENUTZER_OSB_TOOL")) {
				if (log.isDebugEnabled())
					log.debug("OSB-Benutzer, leite auf OSB-Startseite um.");
				return mapping.findForward("OSB_START");
			}
			if (log.isDebugEnabled())
				log.debug("Weder BOB, noch OSB-Benutzer, leite auf Kontaktseite um.");
			addError("common.noAuth");
			return mapping.findForward("NO_AUTH");
		}

		BaumassnahmeSearchForm searchForm = (BaumassnahmeSearchForm) form;
		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true")) {
			searchForm.reset();
		}

		// ///////////////
		// Paging/Sorting
		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null)
			// Das page Property der Basisklasse ValidatorForm des Struts Frameworks wird hier
			// zweckentfremdet verwendet! Stuts verwendet page, um mehrseitige Formulare zu
			// validieren. Da dieses Suchformular nicht validiert wird (struts-config.xml), wird
			// page nicht vom Framework verwendet.
			listPageNumber = searchForm.getPage();

		String listSortColumn = null;
		SortOrderEnum listSortOrder = null;
		if (searchForm != null) {
			// Sortierkriterium aus Form verwenden
			if (searchForm.getSortColumn() == null || searchForm.getSortColumn().equals("")) {
				if (searchForm.getViewMode() != null
				    && searchForm.getViewMode().equals("milestoneView")) {
					searchForm.setSortColumn("baubetriebsplanung.anforderungBBZRSoll");
				} else {
					searchForm.setSortColumn("beginnDatum");
				}
			}
			listSortColumn = searchForm.getSortColumn();
			if (searchForm.getSortDirection() != null) {
				listSortOrder = SortOrderEnum.fromName(searchForm.getSortDirection());
			}
		}
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("sort"))) {
			// Sortierkriterium aus Request verwenden, wenn vorhanden
			listSortColumn = request.getParameter("sort");
		}

		if ("asc".equalsIgnoreCase(request.getParameter("dir"))) {
			listSortOrder = SortOrderEnum.ASCENDING;
		} else if ("desc".equalsIgnoreCase(request.getParameter("dir"))) {
			listSortOrder = SortOrderEnum.DESCENDING;
		}
		if (listPageNumber == null || listPageNumber == 0) {
			listPageNumber = 1;
		}
		// Sortierparameter anpassen
		if (listSortColumn == null) {
			listSortColumn = "beginnDatum";
			listSortOrder = SortOrderEnum.ASCENDING;
		}

		int lowerLimit = (listPageNumber - 1) * PaginatedList.LIST_OBJECTS_PER_PAGE;
		List<Order> sortOrders = new LinkedList<Order>();

		if (listSortOrder != null) {
			if (listSortOrder == SortOrderEnum.ASCENDING) {
				sortOrders.add(Order.asc(listSortColumn));
			} else if (listSortOrder == SortOrderEnum.DESCENDING) {
				sortOrders.add(Order.desc(listSortColumn));
			}
		}

		PaginatedList<Baumassnahme> list = null;
		List<Regionalbereich> listRegFpl = new ArrayList<Regionalbereich>();
		List<Bearbeitungsbereich> listBearbeitungsbereich = new ArrayList<Bearbeitungsbereich>();
		List<User> listBearbeiter = new ArrayList<User>();

		try {
			SearchBean searchBean = fillSearchBean(searchForm);
			if (searchBean != null) {
				if (searchBean.isViewModeMilestones() == true) {
					list = baumassnahmeService.findBySearchBean(sortOrders, searchBean, lowerLimit,
					    PaginatedList.LIST_OBJECTS_PER_PAGE, fetchplansMilestoneView);
				} else {
					list = baumassnahmeService.findBySearchBean(sortOrders, searchBean, lowerLimit,
					    PaginatedList.LIST_OBJECTS_PER_PAGE, fetchplansListview);
				}
			} else {
				list = baumassnahmeService.findBySearchBean(sortOrders, null, lowerLimit,
				    PaginatedList.LIST_OBJECTS_PER_PAGE, fetchplansListview);
			}

			listRegFpl = regionalbereichService.findAll();

			// Bearbeitungsbereiche entsprechend des ausgewählten Regionalbereichs finden
			if (searchForm != null
			    & (FrontendHelper.castStringToInteger(searchForm.getRegionalbereichFpl()) != null)) {
				Regionalbereich selectedRb = regionalbereichService.findById(Integer
				    .parseInt(searchForm.getRegionalbereichFpl()));
				listBearbeitungsbereich = bearbeitungsbereichService
				    .findByRegionalbereich(selectedRb);
				searchForm.setBearbeitungsbereichList(listBearbeitungsbereich);
			}

			// Bearbeiter für Dropdown entsprechend der Rechte finden
			User loginUser = getLoginUser(request);
			MessageResources msgRessources = getResources();

			// erweiterte Rechte für Auswerter vorerst deaktiviert (#76)
			// if (secUser.hasRole("AUSWERTER_ZENTRAL") || secUser.hasRole("AUSWERTER_REGIONAL")) {
			// // alle Benutzer des eigenen Regionalbereichs
			// UserService userService = serviceFactory.createUserService();
			// listBearbeiter.addAll(userService.findUsersByRegionalbereich(getLoginUser(request)
			// .getRegionalbereich()));
			// for (User u : listBearbeiter) {
			// if (u.getId().equals(loginUser.getId())) {
			// User user = new User();
			// user.setId(loginUser.getId());
			// user.setName(msgRessources.getMessage("baumassnahme.select.option.eigene"));
			// user.setFirstName(null);
			// u = user;
			// }
			// }
			// } else { // nur Benutzer selbst
			User user = new User();
			user.setId(loginUser.getId());
			user.setName(msgRessources.getMessage("baumassnahme.select.option.eigene"));
			user.setFirstName(null);
			listBearbeiter.add(user);
			// }

		} catch (LazyInitializationException e) {
			return mapping.findForward("FAILURE");
		}

		// Ausgabeart festlegen - wenn kein Meilenstein markiert wurde, wird normale Suche
		// durchgeführt => Listenansicht
		/*
		 * if((searchForm != null) && (searchForm.getMilestones() != null) &&
		 * (searchForm.getMilestones().length > 0)) { searchForm.setViewMode("milestoneView"); }
		 * else { if(searchForm != null) { searchForm.setViewMode("listView"); } }
		 */

		list.setObjectsPerPage(PaginatedList.LIST_OBJECTS_PER_PAGE);
		list.setPageNumber(listPageNumber);
		list.setSortDirection(listSortOrder);

		if (FrontendHelper.stringNotNullOrEmpty(listSortColumn)) {
			list.setSortCriterion(listSortColumn);
			searchForm.setSortColumn(listSortColumn);
			request.setAttribute("sort", listSortColumn);
		} else {
			// list.setSortCriterion("kennungNetz");
			// list.setSortDirection(listSortOrder);
		}

		// Sortierung als Parameter zurückgeben, weil Displaytag die Methode
		// baumassnahmeList.getSortDirection nicht korrekt auswertet
		// Die verwendeten Parameter werden im Form gespeichert.
		if (list.getSortDirection() == SortOrderEnum.ASCENDING) {
			request.setAttribute("dir", "asc");
			searchForm.setSortDirection(SortOrderEnum.ASCENDING.getName());
		} else if (list.getSortDirection() == SortOrderEnum.DESCENDING) {
			request.setAttribute("dir", "desc");
			searchForm.setSortDirection(SortOrderEnum.DESCENDING.getName());
		}

		request.setAttribute("regionalbereiche", listRegFpl);
		request.setAttribute("bearbeitungsbereiche", listBearbeitungsbereich);
		request.setAttribute("bearbeiter", listBearbeiter);
		// request.setAttribute("paginatedBaumassnahmen", list);
		request.setAttribute("baumassnahmen", list);
		request.setAttribute("availableFahrplanjahre",
		    baumassnahmeService.findAvailableFahrplanjahre());

		return mapping.findForward("SUCCESS");
	}

	public ActionForward saveConfig(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("SAVE USER CONFIG.");

		BaumassnahmeSearchForm searchForm = (BaumassnahmeSearchForm) form;
		saveUserConfig(searchForm, request);
		return web(mapping, searchForm, request, response);
	}

	public ActionForward loadConfig(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("LOAD USER CONFIG.");

		BaumassnahmeSearchForm searchForm = (BaumassnahmeSearchForm) form;
		searchForm = loadUserConfig(searchForm, request);
		return web(mapping, searchForm, request, response);
	}

	private void saveUserConfig(BaumassnahmeSearchForm searchForm, HttpServletRequest request) {
		User user = getLoginUser(request);
		SearchConfig searchConfig = searchConfigService.findByUser(user);
		if (searchConfig != null) {
			// update
			searchConfig = searchForm.fillSearchConfig(searchConfig);
			searchConfigService.update(searchConfig);
		} else {
			// create
			searchConfig = new SearchConfig(user);
			searchConfig = searchForm.fillSearchConfig(searchConfig);
			searchConfigService.create(searchConfig);
		}
	}

	private BaumassnahmeSearchForm loadUserConfig(BaumassnahmeSearchForm searchForm,
	    HttpServletRequest request) {
		User user = getLoginUser(request);
		SearchConfig searchConfig = searchConfigService.findByUser(user);
		searchForm.fillFromSearchConfig(searchConfig);
		return searchForm;
	}

	@SuppressWarnings("unchecked")
	public ActionForward xlsSummary(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeListAction:kurzer Excelexport");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		BaumassnahmeSearchForm searchForm = (BaumassnahmeSearchForm) form;
		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true")) {
			searchForm.reset();
		}

		// Sortierung übernehmen
		if (searchForm.getSortColumn() == null || searchForm.getSortColumn().equals("")) {
			if (searchForm.getViewMode() != null
			    && searchForm.getViewMode().equals("milestoneView")) {
				searchForm.setSortColumn("baubetriebsplanung.anforderungBBZRSoll");
			} else {
				searchForm.setSortColumn("beginnDatum");
			}
		}
		String listSortColumn = searchForm.getSortColumn();

		SortOrderEnum listSortOrder = null;
		if (searchForm.getSortDirection() != null) {
			listSortOrder = SortOrderEnum.fromName(searchForm.getSortDirection());
		} else {
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

		// Suchparameter füllen
		SearchBean searchBean = null;
		if (hasUserRestrictedAccess(secUser)) {
			String tmpRegFpl = searchForm.getRegionalbereichFpl();

			searchForm.setRegionalbereichFpl(loginUser.getRegionalbereich().getId().toString());
			searchBean = fillSearchBean(searchForm);

			searchForm.setRegionalbereichFpl(tmpRegFpl);
		} else {
			searchBean = fillSearchBean(searchForm);
		}

		FetchPlan[] fetchPlan = new FetchPlan[] { FetchPlan.BOB_REGIONALBEREICH_FPL,
		        FetchPlan.BOB_KIGBAU, FetchPlan.BOB_QS, FetchPlan.BOB_KORRIDOR_ZEITFENSTER,
		        FetchPlan.BOB_AUSFALLGRUND, FetchPlan.BOB_AENDERUNGSDOKUMENTATION };

		try {
			List<Baumassnahme> list = null;

			if (searchBean != null) {
				// list = baumassnahmeService.findBySearchBean(searchBean, fetchplans);
				list = baumassnahmeService.findBySearchBean(sortOrders, searchBean, null, null,
				    fetchPlan).getList();
			} else {
				// list = baumassnahmeService.findAll(fetchplans).getList();
				list = baumassnahmeService
				    .findBySearchBean(sortOrders, null, null, null, fetchPlan).getList();
			}

			// Excel-Template füllen
			Map beans = new HashMap();
			beans.put("baumassnahmen", list);

			try {
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = configureWorkbook(wb);
				buildSummaryReportSheet(wb, beans);

				// Ausgabe in Reponse Stream schreiben
				response.setHeader("Content-Disposition", "attachment;filename=Excel-Export.xls");
				response.setContentType("application/vnd.ms-excel");
				// response.setContentLength(wb.getBytes().length);
				OutputStream out = response.getOutputStream();
				wb.write(out);
			} catch (Exception ex) {
				if (log.isDebugEnabled()) {
					log.debug(ex);
				}
			}

		} catch (LazyInitializationException e) {
			if (log.isDebugEnabled())
				log.debug(e);

			return mapping.findForward("FAILURE");
		}

		return null;
	}

	private boolean hasUserRestrictedAccess(TqmUser secUser) {
		boolean result = false;

		if (secUser != null && secUser.getRoles() != null) {
			for (Role role : secUser.getRoles()) {

				if (role instanceof TqmRole) {
					TqmRole r = (TqmRole) role;

					// ist Rolle auf Region beschränkt?
					if (r.getName().indexOf("REGIONAL") > -1)
						result = true;
				} else {
					result = false;
				}
			}
		} else {
			// keine Rollen, keine Rechte
			result = false;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public ActionForward xls(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeListAction:vollständiger Excelexport");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		BaumassnahmeSearchForm searchForm = (BaumassnahmeSearchForm) form;
		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true")) {
			searchForm.reset();
		}

		// Sortierung übernehmen
		if (searchForm.getSortColumn() == null || searchForm.getSortColumn().equals("")) {
			if (searchForm.getViewMode() != null
			    && searchForm.getViewMode().equals("milestoneView")) {
				searchForm.setSortColumn("baubetriebsplanung.anforderungBBZRSoll");
			} else {
				searchForm.setSortColumn("beginnDatum");
			}
		}
		String listSortColumn = searchForm.getSortColumn();

		SortOrderEnum listSortOrder = null;
		if (searchForm.getSortDirection() != null) {
			listSortOrder = SortOrderEnum.fromName(searchForm.getSortDirection());
		} else {
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

		// Suchparameter füllen
		SearchBean searchBean = null;
		if (hasUserRestrictedAccess(secUser)) {
			String tmpRegFpl = searchForm.getRegionalbereichFpl();

			searchForm.setRegionalbereichFpl(loginUser.getRegionalbereich().getId().toString());
			searchBean = fillSearchBean(searchForm);

			searchForm.setRegionalbereichFpl(tmpRegFpl);
		} else {
			searchBean = fillSearchBean(searchForm);
		}
		try {
			FetchPlan[] fetchplan = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME,
			        FetchPlan.BOB_REGIONALBEREICH_FPL, FetchPlan.BOB_BEARBEITUNGSBEREICH,
			        FetchPlan.BOB_TERMINE_BBP, FetchPlan.BOB_TERMINE_PEVU,
			        FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_QS, FetchPlan.BOB_KIGBAU,
			        FetchPlan.BOB_KORRIDOR_ZEITFENSTER, FetchPlan.BOB_AUSFALLGRUND,
			        FetchPlan.BOB_AENDERUNGSDOKUMENTATION };
			List<Baumassnahme> list = null;
			if (searchBean != null) {
				// list = baumassnahmeService.findBySearchBean(searchBean, fetchplans);
				list = baumassnahmeService.findBySearchBean(sortOrders, searchBean, null, null,
				    fetchplan).getList();
			} else {
				// list = baumassnahmeService.findAll(fetchplans).getList();
				list = baumassnahmeService
				    .findBySearchBean(sortOrders, null, null, null, fetchplan).getList();
			}
			// Excel-Template füllen
			Map beans = new HashMap();
			beans.put("baumassnahmen", list);
			try {
				// Ausgabe in Reponse Stream schreiben
				response.setHeader("Content-Disposition", "attachment;filename=Excel-Export.xls");
				response.setContentType("application/vnd.ms-excel");
				OutputStream out = response.getOutputStream();
				// HSSFWorkbook wb = new HSSFWorkbook();
				ExcelExporter ex = new ExcelExporter();
				HSSFWorkbook wb = ex.getXls(list);
				// configureWorkbook(wb);
				// buildReportSheet(wb, beans);
				wb.write(out);
			} catch (Exception ex) {
				if (log.isDebugEnabled()) {
					log.debug(ex);
				}
			}
		} catch (LazyInitializationException e) {
			if (log.isDebugEnabled())
				log.debug(e);

			return mapping.findForward("FAILURE");
		}
		return null;
	}

	private MessageResources getResources() {
		MessageResources msgRes = MessageResources.getMessageResources("MessageResources");
		// MessageResources msgRes = getResources(request);
		return msgRes;
	}

	private void applyDocumentFooter(HSSFSheet sheet) {
		if (sheet == null)
			return;
		MessageResources msgRes = getResources();
		HSSFFooter footer = sheet.getFooter();
		footer.setCenter(msgRes.getMessage("print.createdwith") + ": "
		    + ConfigResources.getInstance().getApplicationTitle() + "\n"
		    + msgRes.getMessage("print.createdon") + ": "
		    + FrontendHelper.castDateToString(new Date(), "dd.MM.yyyy HH:mm"));
		footer.setRight(msgRes.getMessage("print.page") + " " + HeaderFooter.page() + "/"
		    + HeaderFooter.numPages());
	}

	/**
	 * stellt die Druckausgabe Querformat, DIN A4 ein.
	 * 
	 * @param sheet
	 * @return
	 */
	private HSSFPrintSetup applyPrintSetup(HSSFSheet sheet) {
		if (sheet == null)
			return null;
		HSSFPrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape(true);
		ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		return ps;
	}

	public ActionForward testWebHelp(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		return web(mapping, form, request, response);
	}

	private SearchBean fillSearchBean(BaumassnahmeSearchForm searchForm) {
		SearchBean searchBean = null;
		if (searchForm != null) {
			// Suchparameter
			searchBean = new SearchBean();

			searchBean.setSearchMasId(searchForm.getMasId());
			searchBean.setSearchArtA(searchForm.getArtA());
			searchBean.setSearchArtB(searchForm.getArtB());
			searchBean.setSearchArtQs(searchForm.getArtQs());
			searchBean.setSearchArtKs(searchForm.getArtKs());
			if (FrontendHelper.stringNotNullOrEmpty(searchForm.getAusfaelle())) {
				searchBean.setSearchAusfaelle(Boolean.valueOf(searchForm.getAusfaelle()));
			} else {
				searchBean.setSearchAusfaelle(null);
			}
			if (FrontendHelper.stringNotNullOrEmpty(searchForm.getAenderungen())) {
				searchBean.setSearchAenderungen(Boolean.valueOf(searchForm.getAenderungen()));
			} else {
				searchBean.setSearchAenderungen(null);
			}
			searchBean.setSearchKorridorZeitfenster(searchForm.getKorridorZeitfenster());

			searchBean.setSearchStreckeBBP(searchForm.getStreckeBBP());
			searchBean.setSearchStreckeVZG(searchForm.getStreckeVZG());
			searchBean.setSearchStreckenAbschnitt(searchForm.getStreckenAbschnitt());
			searchBean.setVorgangsNummer(searchForm.getVorgangsNummer());
			searchBean.setSearchKigBauNr(searchForm.getKigBauNr());
			searchBean.setSearchKorridorNr(searchForm.getKorridorNr());
			searchBean.setSearchQsNr(searchForm.getQsNr());
			searchBean.setSearchBearbeiter(searchForm.getBearbeiter());
			searchBean.setSearchFahrplanjahr(searchForm.getFahrplanjahr());
			searchBean.setSearchBeginnDatum(searchForm.getBeginnDatum());
			searchBean.setSearchEndDatum(searchForm.getEndDatum());
			searchBean.setNurAktiv(searchForm.getNurAktiv());
			searchBean.setBauZeitraumVon(searchForm.getBauZeitraumVon());
			searchBean.setBauZeitraumBis(searchForm.getBauZeitraumBis());
			searchBean.setSearchFploNr(searchForm.getFploNr());
			searchBean.setSearchRegionalbereichFpl(searchForm.getRegionalbereichFpl());
			searchBean.setSearchBearbeitungsbereich(searchForm.getBearbeitungsbereich());
			searchBean.setSearchMilestones(searchForm.getMilestones());
			searchBean.setSearchControllingBeginnDatum(searchForm.getControllingBeginnDatum());
			searchBean.setSearchControllingEndDatum(searchForm.getControllingEndDatum());
			searchBean.setOnlyOpenMilestones(searchForm.getOnlyOpenMilestones());
			if (searchForm.getViewMode() != null
			    && searchForm.getViewMode().equals("milestoneView")) {
				searchBean.setViewModeMilestones(true);
			}
			searchBean.setLetzteXWochen(searchForm.getLetzteXWochen());
			searchBean.setNaechsteXWochen(searchForm.getNaechsteXWochen());
			if (searchForm.getOptionDatumZeitraum() != null
			    && searchForm.getOptionDatumZeitraum().equals("zeitraum")) {
				searchBean.setOptionZeitraum(true);
			}
		}
		return searchBean;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private HSSFWorkbook buildXlsWorkbook(Map beanParams) {
		HSSFWorkbook wb = new HSSFWorkbook();
		wb = configureWorkbook(wb);
		HSSFSheet summarySheet = buildSummaryReportSheet(wb, beanParams);
		HSSFSheet reportSheet = buildReportSheet(wb, beanParams);
		return wb;
	}

	@SuppressWarnings("unchecked")
	private HSSFSheet buildReportSheet(HSSFWorkbook wb, Map beanParams) {
		HSSFSheet sheet = wb.createSheet("Bericht");

		applyDocumentFooter(sheet);
		applyPrintSetup(sheet);

		HSSFRow row = null;
		HSSFCell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30.0f);

		SortedMap<String, Column> baumassnahmeColumnMap = getBaumassnahmeColumns();
		SortedMap<String, Column> schnittstellenColumnMap = getSchnittstellenColumns(baumassnahmeColumnMap
		    .size());

		MessageResources messageRessources = getResources();

		int column = 0;

		// 1. Kopfzeilen für Schnittstellen definieren (colspan/rowspan!)
		cell = Column.createCell(row, column, HSSFCell.CELL_TYPE_BLANK, "", true); // Lfd.Nr.
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // vorgangsNr
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // fploNr
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // streckeBBP
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // streckeVZG
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // streckenAbschnitt
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // bauzeitraum
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // beginnDatum
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // endDatum
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // artDerMassnahme
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // betriebsweise
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // regionalBereichBM
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // regionalBereichFpl
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // prioritaet
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // art
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // kigBau
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // kigBauNr
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // korridorNr
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // korridorZeitfenster
		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_STRING, "", true); // qsNummer

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "Änderung/Ausfall",
		    true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("title.meilensteine.schnittstelle"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.anforderungbbzr.long"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.studie.long"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.bbp.biueentwurf.long"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.bbp.zvfentwurf.long"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		// cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		// "Koordinationsergebnis", true);// entfällt
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.stellungnahmeevu.nobr"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.gesamtkonzeptbbzr.nobr"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // ausfaellesev

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "", true); // bkonzeptevu

		// cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK, "BiÜ", true);//
		// entfällt
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.bbp.zvf"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.masteruebergabeblatt"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.uebergabeblatt"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.fplo"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		cell = Column.createCell(row, column += 1, HSSFCell.CELL_TYPE_BLANK,
		    messageRessources.getMessage("baumassnahme.termine.eingabegfd_z"), true);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, column, column += 2));

		// Tabellenspalten initialisieren
		row = sheet.createRow(1);

		for (Column col : baumassnahmeColumnMap.values()) {
			sheet.setColumnWidth(col.getIndex(), col.getWidth());
			col.createCell(row, true);
		}
		for (Column col : schnittstellenColumnMap.values()) {
			sheet.setColumnWidth(col.getIndex(), col.getWidth());
			col.createCell(row, true);
		}

		// ///////////////////////
		// Baumaßnahmen schreiben
		int tableRowCount = 2; // die ersten 2 Zeilen werden für Überschriften verwendet
		int lfdNr = 1;

		// über alle Baumaßnahmen iterieren und Zeilen in Worksheet erstellen
		for (Iterator iter = ((Collection) beanParams.get("baumassnahmen")).iterator(); iter
		    .hasNext();) {
			Baumassnahme bm = (Baumassnahme) iter.next();

			// alle Schnittstellen einer Baumaßnahme ausgeben, jeder Schnittstelle werden die
			// Stammdaten vorangestellt
			// 1. BBP
			if (bm.getBaubetriebsplanung() != null) {

				row = sheet.createRow(tableRowCount);

				// Daten der Baumassnahme schreiben
				writeBaumassnahme(lfdNr, row, cell, bm, baumassnahmeColumnMap);
				tableRowCount += writeAenderungAusfallGrund(tableRowCount, sheet, bm,
				    baumassnahmeColumnMap);

				tableRowCount++;

				// BBP
				writeSchnittstelle(SchnittstelleTyp.BBP, lfdNr, wb, row, cell,
				    bm.getBaubetriebsplanung(), schnittstellenColumnMap);
				// log.debug("BBP ID: " + bm.getBaubetriebsplanung().getId());

				for (Iterator<HSSFCell> iterCell = row.cellIterator(); iterCell.hasNext();) {
					HSSFCell c = iterCell.next();

					List<Integer> dateColumns = Arrays
					    .asList(new Integer[] { 7, 8, 26, 27, 29, 30, 32, 33, 35, 36, 38, 39, 41,
					            42, 46, 47, 49, 50, 52, 53, 55, 56, 58, 59 });
					if (dateColumns.contains(c.getColumnIndex())) {
						addBorder(wb, c, "borderTop-Date", HSSFCellStyle.BORDER_MEDIUM);
					} else {
						addBorder(wb, c, "borderTop", HSSFCellStyle.BORDER_MEDIUM);
					}
				}
			}

			// 2. EVU P
			for (Iterator<TerminUebersichtPersonenverkehrsEVU> iterPevu = bm.getPevus().iterator(); iterPevu
			    .hasNext();) {
				TerminUebersichtPersonenverkehrsEVU pevu = iterPevu.next();

				row = sheet.getRow(tableRowCount);
				if (row == null) {
					row = sheet.createRow(tableRowCount);
				}
				tableRowCount++;

				// Daten der Baumassnahme schreiben
				writeBaumassnahme(lfdNr, row, cell, bm, baumassnahmeColumnMap);

				// PEVU
				writeSchnittstelle(SchnittstelleTyp.PEVU, lfdNr, wb, row, cell, pevu,
				    schnittstellenColumnMap);

				// log.debug("EVU P - ID: " + pevu.getId());
			}

			// 3. EVU G
			for (Iterator<TerminUebersichtGueterverkehrsEVU> iterGevu = bm.getGevus().iterator(); iterGevu
			    .hasNext();) {
				TerminUebersichtGueterverkehrsEVU gevu = iterGevu.next();

				row = sheet.getRow(tableRowCount);
				if (row == null) {
					row = sheet.createRow(tableRowCount);
				}
				tableRowCount++;

				// Daten der Baumassnahme schreiben
				writeBaumassnahme(lfdNr, row, cell, bm, baumassnahmeColumnMap);

				// GEVU
				writeSchnittstelle(SchnittstelleTyp.GEVU, lfdNr, wb, row, cell, gevu,
				    schnittstellenColumnMap);

				// log.debug("EVU G - ID: " + gevu.getId());
			}

			// lfdNr. erhöhen
			lfdNr++;
		}

		return sheet;
	}

	/**
	 * initialisiert ein Workbook Objekt, d.h. konfiguriert Fonts, Farben und Datentypen und gibt
	 * dieses Objekt wieder zurück.
	 * 
	 * @param wb
	 *            zu konfigurierendes Workbook
	 * @return konfiguriertes Workbook
	 */
	private HSSFWorkbook configureWorkbook(HSSFWorkbook wb) {

		if (wb == null) {
			return null;
		}

		// //////////
		// Kopfzeile
		HSSFCellStyle headerStyle = wb.createCellStyle();
		HSSFFont headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontName("Calibri");
		headerFont.setColor((short) 1);

		headerStyle.setAlignment((HSSFCellStyle.ALIGN_CENTER));
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle.setFillForegroundColor(HSSFColor.TEAL.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);

		// Styles definieren
		HSSFCellStyle dateStyle = wb.createCellStyle();
		dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		Column.setHeaderStyle(headerStyle);
		Column.setDateStyle(dateStyle);

		return wb;
	}

	@SuppressWarnings("unchecked")
	private HSSFSheet buildSummaryReportSheet(HSSFWorkbook wb, Map beanParams) {
		HSSFSheet sheet = wb.createSheet("Zusammenfassung");

		applyDocumentFooter(sheet);
		applyPrintSetup(sheet);

		HSSFRow row = null;
		HSSFCell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30.0f);

		SortedMap<String, Column> baumassnahmeColumnMap = getBaumassnahmeColumns();

		// 1. Kopfzeilen für Schnittstellen definieren (colspan/rowspan!)
		cell = Column.createCell(row, 0, HSSFCell.CELL_TYPE_BLANK, "", true); // Lfd.Nr.
		cell = Column.createCell(row, 1, HSSFCell.CELL_TYPE_BLANK, "", true); // vorgangsNr
		cell = Column.createCell(row, 2, HSSFCell.CELL_TYPE_BLANK, "", true); // fploNr
		cell = Column.createCell(row, 3, HSSFCell.CELL_TYPE_NUMERIC, "", true); // streckeBBP
		cell = Column.createCell(row, 4, HSSFCell.CELL_TYPE_NUMERIC, "", true); // streckeVZG
		cell = Column.createCell(row, 5, HSSFCell.CELL_TYPE_BLANK, "", true); // streckenAbschnitt
		cell = Column.createCell(row, 6, HSSFCell.CELL_TYPE_BLANK, "", true); // beginnFuerTerminberechnung
		cell = Column.createCell(row, 7, HSSFCell.CELL_TYPE_BLANK, "", true); // beginnDatum
		cell = Column.createCell(row, 8, HSSFCell.CELL_TYPE_BLANK, "", true); // endDatum
		cell = Column.createCell(row, 9, HSSFCell.CELL_TYPE_BLANK, "", true); // artDerMassnahme
		cell = Column.createCell(row, 10, HSSFCell.CELL_TYPE_BLANK, "", true); // betriebsweise
		cell = Column.createCell(row, 11, HSSFCell.CELL_TYPE_BLANK, "", true); // regionalBereichBM
		cell = Column.createCell(row, 12, HSSFCell.CELL_TYPE_BLANK, "", true); // regionalBereichFpl
		cell = Column.createCell(row, 13, HSSFCell.CELL_TYPE_NUMERIC, "", true); // prioritaet
		cell = Column.createCell(row, 14, HSSFCell.CELL_TYPE_BLANK, "", true); // art
		cell = Column.createCell(row, 15, HSSFCell.CELL_TYPE_BLANK, "", true); // kigBau
		cell = Column.createCell(row, 16, HSSFCell.CELL_TYPE_BLANK, "", true); // kigBauNr
		cell = Column.createCell(row, 17, HSSFCell.CELL_TYPE_BLANK, "", true); // korridorNr
		cell = Column.createCell(row, 18, HSSFCell.CELL_TYPE_BLANK, "", true); // korridorZeitfenster
		cell = Column.createCell(row, 19, HSSFCell.CELL_TYPE_STRING, "", true); // qsNummer
		cell = Column.createCell(row, 20, HSSFCell.CELL_TYPE_BLANK, "Änderung/Ausfall", true);

		// javadoc: firstRow, lastRow, firstColumn, lastColumn

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 20, 22));

		// Tabellenspalten initialisieren
		row = sheet.createRow(1);

		for (Column col : baumassnahmeColumnMap.values()) {
			sheet.setColumnWidth(col.getIndex(), col.getWidth());
			col.createCell(row, true);
		}

		// ///////////////////////
		// Baumaßnahmen schreiben
		int tableRowCount = 2;
		int lfdNr = 1;

		// über alle Baumaßnahmen iterieren und Zeilen in Worksheet erstellen
		for (Iterator iter = ((Collection) beanParams.get("baumassnahmen")).iterator(); iter
		    .hasNext();) {
			Baumassnahme bm = (Baumassnahme) iter.next();

			row = sheet.createRow(tableRowCount);

			// Daten der Baumassnahme schreiben
			writeBaumassnahme(lfdNr, row, cell, bm, baumassnahmeColumnMap);
			tableRowCount += writeAenderungAusfallGrund(tableRowCount, sheet, bm,
			    baumassnahmeColumnMap);

			// lfdNr. erhöhen
			tableRowCount++;
			lfdNr++;
		}

		return sheet;
	}

	private SortedMap<String, Column> getBaumassnahmeColumns() {

		// Tabellenspalten definieren
		SortedMap<String, Column> columnMap = new TreeMap<String, Column>();
		int index = 0;

		columnMap.put("lfdNr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, "Lfd.Nr."));
		columnMap.put("vorgangsNr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, "Vorgangsnr."));
		columnMap.put("fploNr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, "Fplo-Nr."));
		columnMap.put("streckeBBP", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, "Strecke BBP"));
		columnMap.put("streckeVZG", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, "Strecke VZG"));
		columnMap.put("streckenAbschnitt", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Streckenabschnitt"));
		columnMap.put("beginnFuerTerminberechnung", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Beginn Terminberechnung"));
		columnMap.put("beginnDatum", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Baubeginn"));
		columnMap.put("endDatum", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		    "Bauende"));
		columnMap
		    .put("artDerMassnahme", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Arbeiten"));
		columnMap.put("betriebsweise", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Betriebsweise"));
		columnMap.put("regionalbereichBM", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Regionalbereich BM"));
		columnMap.put("regionalBereichFpl", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Regionalbereich Fpl"));
		columnMap.put("prioritaet", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Priorität"));
		columnMap.put("art", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Art"));
		columnMap.put("kigBau", new Column(index++, HSSFCell.CELL_TYPE_STRING, "KiGBau"));
		columnMap.put("kigBauNr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, "KiGBau-Nr."));
		columnMap
		    .put("korridorNr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, "Korridor-Nr."));
		columnMap.put("korridorZeitfenster", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Korridor-Zf."));
		columnMap.put("qsNummer", new Column(index++, HSSFCell.CELL_TYPE_STRING, "QS-Nummer"));
		columnMap.put("aenderungAusfallTyp", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Typ"));
		columnMap.put("aenderungAusfallGrund", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Grund"));
		columnMap.put("aenderungAusfallAufwand", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    "Aufwand"));

		columnMap.get("lfdNr").setWidthInPoints(11);
		columnMap.get("vorgangsNr").setWidthInPoints(11);
		columnMap.get("fploNr").setWidthInPoints(11);
		columnMap.get("streckeBBP").setWidthInPoints(11);
		columnMap.get("streckeVZG").setWidthInPoints(11);
		columnMap.get("streckenAbschnitt").setWidthInPoints(40);
		columnMap.get("beginnFuerTerminberechnung").setWidthInPoints(25);
		columnMap.get("beginnDatum").setWidthInPoints(11);
		columnMap.get("endDatum").setWidthInPoints(11);
		columnMap.get("artDerMassnahme").setWidthInPoints(30);
		columnMap.get("betriebsweise").setWidthInPoints(30);
		columnMap.get("regionalbereichBM").setWidthInPoints(20);
		columnMap.get("regionalBereichFpl").setWidthInPoints(20);
		columnMap.get("prioritaet").setWidthInPoints(11);
		columnMap.get("art").setWidthInPoints(11);
		columnMap.get("kigBau").setWidthInPoints(11);
		columnMap.get("kigBauNr").setWidthInPoints(20);
		columnMap.get("korridorNr").setWidthInPoints(11);
		columnMap.get("korridorZeitfenster").setWidthInPoints(11);
		columnMap.get("qsNummer").setWidthInPoints(20);
		columnMap.get("aenderungAusfallTyp").setWidthInPoints(20);
		columnMap.get("aenderungAusfallGrund").setWidthInPoints(30);
		columnMap.get("aenderungAusfallAufwand").setWidthInPoints(11);

		return columnMap;
	}

	private SortedMap<String, Column> getSchnittstellenColumns(int index) {

		// Tabellenspalten definieren
		SortedMap<String, Column> columnMap = new TreeMap<String, Column>();
		columnMap.put("typSchnittstelle", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Typ"));
		columnMap.put("nameSchnittstelle", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Name"));

		columnMap.put("sollAnforderungBbzr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istAnforderungBbzr", new Column(index++, HSSFCell.CELL_TYPE_BLANK,
		    ValueType.DATE, "Ist"));
		columnMap.put("statusAnforderungBbzr", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Status"));

		columnMap.put("sollStudieGrobkonzept", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istStudieGrobkonzept", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Ist"));
		columnMap.put("statusStudieGrobkonzept", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Status"));

		columnMap.put("sollBiUeEntwurf", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istBiUeEntwurf", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Ist"));
		columnMap
		    .put("statusBiUeEntwurf", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		columnMap.put("sollZvfEntwurf", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istZvfEntwurf", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Ist"));
		columnMap.put("statusZvfEntwurf", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		// columnMap.put("sollKoordinationsergebnis", new Column(index++,
		// HSSFCell.CELL_TYPE_NUMERIC,
		// ValueType.DATE, "Soll"));
		// columnMap.put("istKoordinationsergebnis", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		// ValueType.DATE, "Ist"));
		// columnMap.put("statusKoordinationsergebnis", new Column(index++,
		// HSSFCell.CELL_TYPE_STRING,
		// "Status"));

		columnMap.put("sollStellungnahmeEvu", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istStellungnahmeEvu", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Ist"));
		columnMap.put("statusStellungnahmeEvu", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Status"));

		columnMap.put("sollGesamtkonzeptBbzr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istGesamtkonzeptBbzr", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Ist"));
		columnMap.put("statusGesamtkonzeptBbzr", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Status"));

		columnMap.put("statusAusfallSev", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "Ausfälle/SEV"));// baumassnahme.termine.ausfaellesev.nobr
		columnMap.put("statusBKonzeptEvu", new Column(index++, HSSFCell.CELL_TYPE_STRING,
		    "B-Konzept EVU"));// baumassnahme.termine.bkonzeptevu.nobr

		// columnMap.put("sollBiUe", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		// "Soll"));
		// columnMap.put("istBiUe", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		// "Ist"));
		// columnMap.put("statusBiUe", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		columnMap.put("sollZvf", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		    "Soll"));
		columnMap.put("istZvf", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		    "Ist"));
		columnMap.put("statusZvf", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		columnMap.put("sollMasterUeb", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istMasterUeb", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Ist"));
		columnMap.put("statusMasterUeb", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		columnMap.put("sollUeb", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		    "Soll"));
		columnMap.put("istUeb", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		    "Ist"));
		columnMap.put("statusUeb", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		columnMap.put("sollFplo", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		    "Soll"));
		columnMap.put("istFplo", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC, ValueType.DATE,
		    "Ist"));
		columnMap.put("statusFplo", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		columnMap.put("sollEingabeGfdZ", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Soll"));
		columnMap.put("istEingabeGfdZ", new Column(index++, HSSFCell.CELL_TYPE_NUMERIC,
		    ValueType.DATE, "Ist"));
		columnMap
		    .put("statusEingabeGfdZ", new Column(index++, HSSFCell.CELL_TYPE_STRING, "Status"));

		columnMap.get("typSchnittstelle").setWidthInPoints(11);
		columnMap.get("nameSchnittstelle").setWidthInPoints(30);

		columnMap.get("sollAnforderungBbzr").setWidthInPoints(11);
		columnMap.get("istAnforderungBbzr").setWidthInPoints(11);
		columnMap.get("statusAnforderungBbzr").setWidthInPoints(20);

		columnMap.get("sollStudieGrobkonzept").setWidthInPoints(11);
		columnMap.get("istStudieGrobkonzept").setWidthInPoints(11);
		columnMap.get("statusStudieGrobkonzept").setWidthInPoints(20);

		columnMap.get("sollBiUeEntwurf").setWidthInPoints(11);
		columnMap.get("istBiUeEntwurf").setWidthInPoints(11);
		columnMap.get("statusBiUeEntwurf").setWidthInPoints(20);

		columnMap.get("sollZvfEntwurf").setWidthInPoints(11);
		columnMap.get("istZvfEntwurf").setWidthInPoints(11);
		columnMap.get("statusZvfEntwurf").setWidthInPoints(20);

		// columnMap.get("sollKoordinationsergebnis").setWidthInPoints(11);
		// columnMap.get("istKoordinationsergebnis").setWidthInPoints(11);
		// columnMap.get("statusKoordinationsergebnis").setWidthInPoints(20);

		columnMap.get("sollStellungnahmeEvu").setWidthInPoints(11);
		columnMap.get("istStellungnahmeEvu").setWidthInPoints(11);
		columnMap.get("statusStellungnahmeEvu").setWidthInPoints(20);

		columnMap.get("sollGesamtkonzeptBbzr").setWidthInPoints(11);
		columnMap.get("istGesamtkonzeptBbzr").setWidthInPoints(11);
		columnMap.get("statusGesamtkonzeptBbzr").setWidthInPoints(20);

		columnMap.get("statusAusfallSev").setWidthInPoints(20);
		columnMap.get("statusBKonzeptEvu").setWidthInPoints(20);

		// columnMap.get("sollBiUe").setWidthInPoints(11);
		// columnMap.get("istBiUe").setWidthInPoints(11);
		// columnMap.get("statusBiUe").setWidthInPoints(20);

		columnMap.get("sollZvf").setWidthInPoints(11);
		columnMap.get("istZvf").setWidthInPoints(11);
		columnMap.get("statusZvf").setWidthInPoints(20);

		columnMap.get("sollMasterUeb").setWidthInPoints(11);
		columnMap.get("istMasterUeb").setWidthInPoints(11);
		columnMap.get("statusMasterUeb").setWidthInPoints(20);

		columnMap.get("sollUeb").setWidthInPoints(11);
		columnMap.get("istUeb").setWidthInPoints(11);
		columnMap.get("statusUeb").setWidthInPoints(20);

		columnMap.get("sollFplo").setWidthInPoints(11);
		columnMap.get("istFplo").setWidthInPoints(11);
		columnMap.get("statusFplo").setWidthInPoints(20);

		columnMap.get("sollEingabeGfdZ").setWidthInPoints(11);
		columnMap.get("istEingabeGfdZ").setWidthInPoints(11);
		columnMap.get("statusEingabeGfdZ").setWidthInPoints(20);

		return columnMap;
	}

	private void writeBaumassnahme(int rowNumber, HSSFRow row, HSSFCell cell, Baumassnahme bm,
	    SortedMap<String, Column> baumassnahmeColumnMap) {
		cell = baumassnahmeColumnMap.get("lfdNr").createCell(row, false);
		cell.setCellValue(rowNumber);

		cell = baumassnahmeColumnMap.get("vorgangsNr").createCell(row, false);
		cell.setCellValue(bm.getVorgangsNr());

		cell = baumassnahmeColumnMap.get("fploNr").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getFploNr()));

		cell = baumassnahmeColumnMap.get("streckeBBP").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getStreckeBBP()));

		cell = baumassnahmeColumnMap.get("streckeVZG").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getStreckeVZG()));

		cell = baumassnahmeColumnMap.get("streckenAbschnitt").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getStreckenAbschnitt()));

		cell = baumassnahmeColumnMap.get("beginnFuerTerminberechnung").createCell(row, false);
		cell.setCellValue(bm.getBeginnFuerTerminberechnung());
		// cell.setCellValue(new HSSFRichTextString(bm.getBauzeitraum()));

		cell = baumassnahmeColumnMap.get("beginnDatum").createCell(row, false);
		cell.setCellValue(bm.getBeginnDatum());

		cell = baumassnahmeColumnMap.get("endDatum").createCell(row, false);
		cell.setCellValue(bm.getEndDatum());

		cell = baumassnahmeColumnMap.get("artDerMassnahme").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getArtDerMassnahme()));

		cell = baumassnahmeColumnMap.get("betriebsweise").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getBetriebsweise()));

		cell = baumassnahmeColumnMap.get("regionalbereichBM").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getRegionalbereichBM()));

		cell = baumassnahmeColumnMap.get("regionalBereichFpl").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getRegionalBereichFpl().getName()));

		cell = baumassnahmeColumnMap.get("prioritaet").createCell(row, false);
		if (bm.getPrioritaet() != null) {
			cell.setCellValue(Integer.parseInt(bm.getPrioritaet().getValue()));
		} else {
			cell.setCellValue(new HSSFRichTextString(""));
		}

		cell = baumassnahmeColumnMap.get("art").createCell(row, false);
		cell.setCellValue(new HSSFRichTextString(bm.getArt().name()));

		cell = baumassnahmeColumnMap.get("kigBau").createCell(row, false);
		String kigBauValue = "nein";
		if (bm.getKigBau()) {
			kigBauValue = "ja";
		}
		cell.setCellValue(new HSSFRichTextString(kigBauValue));

		cell = baumassnahmeColumnMap.get("kigBauNr").createCell(row, false);
		StringBuilder sbKigBauNr = new StringBuilder();
		for (String nr : bm.getKigBauNr()) {
			if (sbKigBauNr.length() > 0)
				sbKigBauNr.append(", ");
			sbKigBauNr.append(nr);
		}
		cell.setCellValue(new HSSFRichTextString(sbKigBauNr.toString()));

		cell = baumassnahmeColumnMap.get("korridorNr").createCell(row, false);
		if (bm.getKorridorNr() != null) {
			cell.setCellValue(bm.getKorridorNr());
		}

		cell = baumassnahmeColumnMap.get("korridorZeitfenster").createCell(row, false);
		StringBuilder sbKorridorZeitfenster = new StringBuilder();
		for (String zf : bm.getKorridorZeitfenster()) {
			if (sbKorridorZeitfenster.length() > 0)
				sbKorridorZeitfenster.append(", ");
			sbKorridorZeitfenster.append(zf);
		}
		cell.setCellValue(new HSSFRichTextString(sbKorridorZeitfenster.toString()));

		cell = baumassnahmeColumnMap.get("qsNummer").createCell(row, false);
		StringBuilder sbQsNummer = new StringBuilder();
		for (String nr : bm.getQsNr()) {
			if (sbQsNummer.length() > 0)
				sbQsNummer.append(", ");
			sbQsNummer.append(nr);
		}
		cell.setCellValue(new HSSFRichTextString(sbQsNummer.toString()));

		// die Änderungshistorie wird in Methode writeAenderungAusfallGrund geschrieben
		// leere Zellen erzeugen
		cell = baumassnahmeColumnMap.get("aenderungAusfallTyp").createCell(row, false);
		cell = baumassnahmeColumnMap.get("aenderungAusfallGrund").createCell(row, false);
		cell = baumassnahmeColumnMap.get("aenderungAusfallAufwand").createCell(row, false);
	}

	/**
	 * gibt die Anzahl der geschriebenen Zeilen zurück
	 * 
	 * @param rowNumber
	 *            0-basierter Index der Zeile, in welche die Baumaßnahme geschrieben wurde
	 * @return Anzahl der neu erzeugten Zeilen (bei mehr als einer Änderung/einem Ausfall)
	 */
	private int writeAenderungAusfallGrund(int rowNumber, HSSFSheet sheet, Baumassnahme bm,
	    SortedMap<String, Column> baumassnahmeColumnMap) {

		int createdRows = 0;
		HSSFRow row = sheet.getRow(rowNumber);
		HSSFCell cell = null;

		if (row == null) {
			row = sheet.createRow(rowNumber + createdRows);
			createdRows += 1;
		}

		// Eskalation/Ausfall ausgeben
		if (bm.getAusfallGrund() != null) {
			cell = baumassnahmeColumnMap.get("aenderungAusfallTyp").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString("Ausfall"));

			cell = baumassnahmeColumnMap.get("aenderungAusfallGrund").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString(bm.getAusfallGrund().getName()));

			cell = baumassnahmeColumnMap.get("aenderungAusfallAufwand").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString(bm.getBisherigerAufwandTimeString()));
		}

		// Wenn für Änderungsdoku UND Eskalation Werte auszugeben sind, wird eine neue Zeile
		// benötigt
		if (bm.getAusfallGrund() != null && !bm.getAenderungen().isEmpty()) {
			row = sheet.createRow(rowNumber + (++createdRows));
		}

		// Änderungsdokumentation ausgeben
		Iterator<Aenderung> aenderungIter = bm.getAenderungen().iterator();
		while (aenderungIter.hasNext()) {
			Aenderung a = aenderungIter.next();

			cell = baumassnahmeColumnMap.get("aenderungAusfallTyp").createCell(row, false);
			if (a != null) {
				cell.setCellValue(new HSSFRichTextString("Änderung"));
			}

			cell = baumassnahmeColumnMap.get("aenderungAusfallGrund").createCell(row, false);
			if (a != null) {
				cell.setCellValue(new HSSFRichTextString(a.getGrund().getName()));
			}

			cell = baumassnahmeColumnMap.get("aenderungAusfallAufwand").createCell(row, false);
			if (a != null) {
				cell.setCellValue(new HSSFRichTextString(a.getAufwandTimeString()));
			}

			// neue Zeile erzeugen
			if (aenderungIter.hasNext()) {
				row = sheet.createRow(rowNumber + (++createdRows));
			}
		}

		// Set<Aenderung> aenderungen = bm.getAenderungen();
		// Aenderung a = ((aenderungen.size() > 0) ? (Aenderung) aenderungen.toArray()[0] : null);
		// cell = baumassnahmeColumnMap.get("aenderungAusfallTyp").createCell(row, false);
		// if (a != null) {
		// cell.setCellValue(new HSSFRichTextString("Änderung"));
		// }
		// Eskalation überschreibt Änderung !
		// if (bm.getAusfallGrund() != null) {
		// cell.setCellValue(new HSSFRichTextString("Ausfall"));
		// }

		// cell = baumassnahmeColumnMap.get("aenderungAusfallGrund").createCell(row, false);
		// if (a != null) {
		// cell.setCellValue(new HSSFRichTextString(a.getGrund().getName()));
		// }
		// if (bm.getAusfallGrund() != null) {
		// cell.setCellValue(new HSSFRichTextString(bm.getAusfallGrund().getName()));
		// }

		// cell = baumassnahmeColumnMap.get("aenderungAusfallAufwand").createCell(row, false);
		// if (a != null) {
		// cell.setCellValue(new HSSFRichTextString(a.getAufwandTimeString()));
		// }
		// else {
		// cell.setCellValue(new HSSFRichTextString(bm.getBisherigerAufwandTimeString()));
		// }

		return createdRows;
	}

	private void writeSchnittstelle(SchnittstelleTyp typ, int rowNumber, HSSFWorkbook wb,
	    HSSFRow row, HSSFCell cell, Object object, SortedMap<String, Column> columnMap)
	    throws IllegalArgumentException {

		if (!((object instanceof TerminUebersichtBaubetriebsplanung)
		    || (object instanceof TerminUebersichtPersonenverkehrsEVU) || (object instanceof TerminUebersichtGueterverkehrsEVU))) {
			throw new IllegalArgumentException("object entspricht keinem erwarteten Typ.");
		}

		switch (typ) {
		case BBP:
			TerminUebersichtBaubetriebsplanung bbp = (TerminUebersichtBaubetriebsplanung) object;

			cell = columnMap.get("typSchnittstelle").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString("BBP"));

			cell = columnMap.get("nameSchnittstelle").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString(""));

			cell = columnMap.get("sollAnforderungBbzr").createCell(row, false);
			if (bbp.getAnforderungBBZRSoll() != null) {
				cell.setCellValue(bbp.getAnforderungBBZRSoll());
			}
			cell = columnMap.get("istAnforderungBbzr").createCell(row, false);
			if (bbp.getAnforderungBBZR() != null) {
				cell.setCellValue(bbp.getAnforderungBBZR());
			}
			cell = columnMap.get("statusAnforderungBbzr").createCell(row, false);
			if (bbp.getAnforderungBBZRStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp
				    .getAnforderungBBZRStatus())));
			}

			cell = columnMap.get("sollStudieGrobkonzept").createCell(row, false);
			if (bbp.getStudieGrobkonzept() != null) {
				cell.setCellValue(new HSSFRichTextString(""));
			}
			cell = columnMap.get("istStudieGrobkonzept").createCell(row, false);
			if (bbp.getStudieGrobkonzeptSoll() != null) {
				cell.setCellValue(new HSSFRichTextString(""));
			}
			cell = columnMap.get("statusStudieGrobkonzept").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString(""));

			cell = columnMap.get("sollBiUeEntwurf").createCell(row, false);
			if (bbp.getBiUeEntwurfSoll() != null) {
				cell.setCellValue(bbp.getBiUeEntwurfSoll());
			}
			cell = columnMap.get("istBiUeEntwurf").createCell(row, false);
			if (bbp.getBiUeEntwurf() != null) {
				cell.setCellValue(bbp.getBiUeEntwurf());
			}
			cell = columnMap.get("statusBiUeEntwurf").createCell(row, false);
			if (bbp.getBiUeEntwurfStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp
				    .getBiUeEntwurfStatus())));
			}

			cell = columnMap.get("sollZvfEntwurf").createCell(row, false);
			if (bbp.getZvfEntwurfSoll() != null) {
				cell.setCellValue(bbp.getZvfEntwurfSoll());
			}
			cell = columnMap.get("istZvfEntwurf").createCell(row, false);
			if (bbp.getZvfEntwurf() != null) {
				cell.setCellValue(bbp.getZvfEntwurf());
			}
			cell = columnMap.get("statusZvfEntwurf").createCell(row, false);
			if (bbp.getZvfEntwurfStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp
				    .getZvfEntwurfStatus())));
			}

			// cell = columnMap.get("sollKoordinationsergebnis").createCell(row, false);
			// if (bbp.getKoordinationsErgebnisSoll() != null) {
			// cell.setCellValue(bbp.getKoordinationsErgebnisSoll());
			// }
			//
			// cell = columnMap.get("istKoordinationsergebnis").createCell(row, false);
			// if (bbp.getKoordinationsErgebnis() != null) {
			// cell.setCellValue(bbp.getKoordinationsErgebnis());
			// }
			// cell = columnMap.get("statusKoordinationsergebnis").createCell(row, false);
			// if (bbp.getKoordinationsErgebnisStatus() != null) {
			// cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp
			// .getKoordinationsErgebnisStatus())));
			// }

			cell = columnMap.get("sollStellungnahmeEvu").createCell(row, false);
			cell = columnMap.get("istStellungnahmeEvu").createCell(row, false);
			cell = columnMap.get("statusStellungnahmeEvu").createCell(row, false);

			cell = columnMap.get("sollGesamtkonzeptBbzr").createCell(row, false);
			if (bbp.getGesamtKonzeptBBZRSoll() != null) {
				cell.setCellValue(bbp.getZvfEntwurfSoll());
			}
			cell = columnMap.get("istGesamtkonzeptBbzr").createCell(row, false);
			if (bbp.getGesamtKonzeptBBZR() != null) {
				cell.setCellValue(bbp.getGesamtKonzeptBBZR());
			}
			cell = columnMap.get("statusGesamtkonzeptBbzr").createCell(row, false);
			if (bbp.getGesamtKonzeptBBZRStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp
				    .getGesamtKonzeptBBZRStatus())));
			}

			cell = columnMap.get("statusAusfallSev").createCell(row, false);
			cell = columnMap.get("statusBKonzeptEvu").createCell(row, false);

			// cell = columnMap.get("sollBiUe").createCell(row, false);
			// if (bbp.getBiUeSoll() != null) {
			// cell.setCellValue(bbp.getBiUeSoll());
			// }
			// cell = columnMap.get("istBiUe").createCell(row, false);
			// if (bbp.getBiUe() != null) {
			// cell.setCellValue(bbp.getBiUe());
			// }
			// cell = columnMap.get("statusBiUe").createCell(row, false);
			// if (bbp.getBiUeStatus() != null) {
			// cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp.getBiUeStatus())));
			// }

			cell = columnMap.get("sollZvf").createCell(row, false);
			if (bbp.getZvfSoll() != null) {
				cell.setCellValue(bbp.getZvfSoll());
			}
			cell = columnMap.get("istZvf").createCell(row, false);
			if (bbp.getZvf() != null) {
				cell.setCellValue(bbp.getZvf());
			}
			cell = columnMap.get("statusZvf").createCell(row, false);
			if (bbp.getZvfStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(bbp.getZvfStatus())));
			}

			cell = columnMap.get("sollMasterUeb").createCell(row, false);
			cell = columnMap.get("istMasterUeb").createCell(row, false);
			cell = columnMap.get("statusMasterUeb").createCell(row, false);
			cell = columnMap.get("sollUeb").createCell(row, false);
			cell = columnMap.get("istUeb").createCell(row, false);
			cell = columnMap.get("statusUeb").createCell(row, false);
			cell = columnMap.get("sollFplo").createCell(row, false);
			cell = columnMap.get("istFplo").createCell(row, false);
			cell = columnMap.get("statusFplo").createCell(row, false);
			cell = columnMap.get("sollEingabeGfdZ").createCell(row, false);
			cell = columnMap.get("istEingabeGfdZ").createCell(row, false);
			cell = columnMap.get("statusEingabeGfdZ").createCell(row, false);

			break;
		case PEVU:
			TerminUebersichtPersonenverkehrsEVU pevu = (TerminUebersichtPersonenverkehrsEVU) object;

			cell = columnMap.get("typSchnittstelle").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString("EVU P"));
			try {
				if (pevu.getEvuGruppe() != null && pevu.getEvuGruppe().getName() != null) {
					cell = columnMap.get("nameSchnittstelle").createCell(row, false);
					cell.setCellValue(new HSSFRichTextString(pevu.getEvuGruppe().getName()));
				}

				// Studie Grobkonzept
				cell = columnMap.get("sollStudieGrobkonzept").createCell(row, false);
				if (pevu.getStudieGrobkonzept() != null) {
					cell.setCellValue(new HSSFRichTextString(""));
				}
				cell = columnMap.get("istStudieGrobkonzept").createCell(row, false);
				if (pevu.getStudieGrobkonzeptSoll() != null) {
					cell.setCellValue(new HSSFRichTextString(""));
				}
				cell = columnMap.get("statusStudieGrobkonzept").createCell(row, false);
				cell.setCellValue(new HSSFRichTextString(""));

				// ZvF-Entwurf
				cell = columnMap.get("sollZvfEntwurf").createCell(row, false);
				if (pevu.getZvfEntwurfSoll() != null) {
					cell.setCellValue(pevu.getZvfEntwurfSoll());
				}
				cell = columnMap.get("istZvfEntwurf").createCell(row, false);
				if (pevu.getZvfEntwurf() != null) {
					cell.setCellValue(pevu.getZvfEntwurf());
				}
				cell = columnMap.get("statusZvfEntwurf").createCell(row, false);
				if (pevu.getZvfEntwurfStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
					    .getZvfEntwurfStatus())));
				}

				// Stellungnahme EVU P
				cell = columnMap.get("sollStellungnahmeEvu").createCell(row, false);
				if (pevu.getStellungnahmeEVUSoll() != null) {
					cell.setCellValue(pevu.getStellungnahmeEVUSoll());
				}
				cell = columnMap.get("istStellungnahmeEvu").createCell(row, false);
				if (pevu.getStellungnahmeEVU() != null) {
					cell.setCellValue(pevu.getStellungnahmeEVU());
				}
				cell = columnMap.get("statusStellungnahmeEvu").createCell(row, false);
				if (pevu.getStellungnahmeEVUStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
					    .getStellungnahmeEVUStatus())));
				}

				// SEV
				cell = columnMap.get("statusAusfallSev").createCell(row, false);
				if (pevu.isAusfaelleSEV() == true) {
					cell.setCellValue(new HSSFRichTextString("ja"));
				} else {
					cell.setCellValue(new HSSFRichTextString("nein"));
				}

				// B-Konzept EVU
				cell = columnMap.get("statusBKonzeptEvu").createCell(row, false);
				if (pevu.getBKonzeptEVUStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
					    .getBKonzeptEVUStatus())));
				}

				// ZvF
				cell = columnMap.get("sollZvf").createCell(row, false);
				if (pevu.getZvFSoll() != null) {
					cell.setCellValue(pevu.getZvFSoll());
				}
				cell = columnMap.get("istZvf").createCell(row, false);
				if (pevu.getZvF() != null) {
					cell.setCellValue(pevu.getZvF());
				}
				cell = columnMap.get("statusZvf").createCell(row, false);
				if (pevu.getZvFStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(
					    castStatusToString(pevu.getZvFStatus())));
				}

				// Master ÜB
				cell = columnMap.get("sollMasterUeb").createCell(row, false);
				if (pevu.getMasterUebergabeblattPVSoll() != null) {
					cell.setCellValue(pevu.getMasterUebergabeblattPVSoll());
				}
				cell = columnMap.get("istMasterUeb").createCell(row, false);
				if (pevu.getMasterUebergabeblattPV() != null) {
					cell.setCellValue(pevu.getMasterUebergabeblattPV());
				}
				cell = columnMap.get("statusMasterUeb").createCell(row, false);
				if (pevu.getMasterUebergabeblattPVStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
					    .getMasterUebergabeblattPVStatus())));
				}

				// ÜB
				cell = columnMap.get("sollUeb").createCell(row, false);
				if (pevu.getUebergabeblattPVSoll() != null) {
					cell.setCellValue(pevu.getUebergabeblattPVSoll());
				}
				cell = columnMap.get("istUeb").createCell(row, false);
				if (pevu.getUebergabeblattPV() != null) {
					cell.setCellValue(pevu.getUebergabeblattPV());
				}
				cell = columnMap.get("statusUeb").createCell(row, false);
				if (pevu.getUebergabeblattPVStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
					    .getUebergabeblattPVStatus())));
				}

				// Fplo
				cell = columnMap.get("sollFplo").createCell(row, false);
				if (pevu.getFploSoll() != null) {
					cell.setCellValue(pevu.getFploSoll());
				}
				cell = columnMap.get("istFplo").createCell(row, false);
				if (pevu.getFplo() != null) {
					cell.setCellValue(pevu.getFplo());
				}
				cell = columnMap.get("statusFplo").createCell(row, false);
				if (pevu.getFploStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
					    .getFploStatus())));
				}

				// Eingabe GFDZ
				cell = columnMap.get("sollEingabeGfdZ").createCell(row, false);
				if (pevu.getEingabeGFD_ZSoll() != null) {
					cell.setCellValue(pevu.getEingabeGFD_ZSoll());
				}
				cell = columnMap.get("istEingabeGfdZ").createCell(row, false);
				if (pevu.getEingabeGFD_Z() != null) {
					cell.setCellValue(pevu.getEingabeGFD_Z());
				}
				cell = columnMap.get("statusEingabeGfdZ").createCell(row, false);
				if (pevu.getEingabeGFD_ZStatus() != null) {
					cell.setCellValue(new HSSFRichTextString(castStatusToString(pevu
					    .getEingabeGFD_ZStatus())));
				}
			} catch (Exception ex) {
				log.debug(ex);
				log.debug("PEVU ID: " + pevu.getId());
			}
			break;
		case GEVU:
			TerminUebersichtGueterverkehrsEVU gevu = (TerminUebersichtGueterverkehrsEVU) object;

			cell = columnMap.get("typSchnittstelle").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString("EVU G"));

			if (gevu.getEvuGruppe() != null && gevu.getEvuGruppe().getName() != null) {
				cell = columnMap.get("nameSchnittstelle").createCell(row, false);
				cell.setCellValue(new HSSFRichTextString(gevu.getEvuGruppe().getName()));
			}

			// Studie Grobkonzept
			cell = columnMap.get("sollStudieGrobkonzept").createCell(row, false);
			if (gevu.getStudieGrobkonzept() != null) {
				cell.setCellValue(new HSSFRichTextString(""));
			}
			cell = columnMap.get("istStudieGrobkonzept").createCell(row, false);
			if (gevu.getStudieGrobkonzeptSoll() != null) {
				cell.setCellValue(new HSSFRichTextString(""));
			}
			cell = columnMap.get("statusStudieGrobkonzept").createCell(row, false);
			cell.setCellValue(new HSSFRichTextString(""));

			// ZvF-Entwurf
			cell = columnMap.get("sollZvfEntwurf").createCell(row, false);
			if (gevu.getZvfEntwurfSoll() != null) {
				cell.setCellValue(gevu.getZvfEntwurfSoll());
			}
			cell = columnMap.get("istZvfEntwurf").createCell(row, false);
			if (gevu.getZvfEntwurf() != null) {
				cell.setCellValue(gevu.getZvfEntwurf());
			}
			cell = columnMap.get("statusZvfEntwurf").createCell(row, false);
			if (gevu.getZvfEntwurfStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getZvfEntwurfStatus())));
			}

			// Stellungnahme EVU G
			cell = columnMap.get("sollStellungnahmeEvu").createCell(row, false);
			if (gevu.getStellungnahmeEVUSoll() != null) {
				cell.setCellValue(gevu.getStellungnahmeEVUSoll());
			}
			cell = columnMap.get("istStellungnahmeEvu").createCell(row, false);
			if (gevu.getStellungnahmeEVU() != null) {
				cell.setCellValue(gevu.getStellungnahmeEVU());
			}
			cell = columnMap.get("statusStellungnahmeEvu").createCell(row, false);
			if (gevu.getStellungnahmeEVUStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getStellungnahmeEVUStatus())));
			}

			// ZvF
			cell = columnMap.get("sollZvf").createCell(row, false);
			if (gevu.getZvFSoll() != null) {
				cell.setCellValue(gevu.getZvFSoll());
			}
			cell = columnMap.get("istZvf").createCell(row, false);
			if (gevu.getZvF() != null) {
				cell.setCellValue(gevu.getZvF());
			}
			cell = columnMap.get("statusZvf").createCell(row, false);
			if (gevu.getZvFStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu.getZvFStatus())));
			}

			// Master ÜB
			cell = columnMap.get("sollMasterUeb").createCell(row, false);
			if (gevu.getMasterUebergabeblattGVSoll() != null) {
				cell.setCellValue(gevu.getMasterUebergabeblattGVSoll());
			}
			cell = columnMap.get("istMasterUeb").createCell(row, false);
			if (gevu.getMasterUebergabeblattGV() != null) {
				cell.setCellValue(gevu.getMasterUebergabeblattGV());
			}
			cell = columnMap.get("statusMasterUeb").createCell(row, false);
			if (gevu.getMasterUebergabeblattGVStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getMasterUebergabeblattGVStatus())));
			}

			// ÜB
			cell = columnMap.get("sollUeb").createCell(row, false);
			if (gevu.getUebergabeblattGVSoll() != null) {
				cell.setCellValue(gevu.getUebergabeblattGVSoll());
			}
			cell = columnMap.get("istUeb").createCell(row, false);
			if (gevu.getUebergabeblattGV() != null) {
				cell.setCellValue(gevu.getUebergabeblattGV());
			}
			cell = columnMap.get("statusUeb").createCell(row, false);
			if (gevu.getUebergabeblattGVStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getUebergabeblattGVStatus())));
			}

			// Fplo
			cell = columnMap.get("sollFplo").createCell(row, false);
			if (gevu.getFploSoll() != null) {
				cell.setCellValue(gevu.getFploSoll());
			}
			cell = columnMap.get("istFplo").createCell(row, false);
			if (gevu.getFplo() != null) {
				cell.setCellValue(gevu.getFplo());
			}
			cell = columnMap.get("statusFplo").createCell(row, false);
			if (gevu.getFploStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu.getFploStatus())));
			}

			// Eingabe GFDZ
			cell = columnMap.get("sollEingabeGfdZ").createCell(row, false);
			if (gevu.getEingabeGFD_ZSoll() != null) {
				cell.setCellValue(gevu.getEingabeGFD_ZSoll());
			}
			cell = columnMap.get("istEingabeGfdZ").createCell(row, false);
			if (gevu.getEingabeGFD_Z() != null) {
				cell.setCellValue(gevu.getEingabeGFD_Z());
			}
			cell = columnMap.get("statusEingabeGfdZ").createCell(row, false);
			if (gevu.getEingabeGFD_ZStatus() != null) {
				cell.setCellValue(new HSSFRichTextString(castStatusToString(gevu
				    .getEingabeGFD_ZStatus())));
			}

			break;

		default:
			break;
		}
	}

	private static String castStatusToString(StatusType status) {
		if (status == null)
			return null;

		String result = null;

		switch (status) {
		case GREEN:
			result = "pünktlich";
			break;

		case NEUTRAL:
			result = "nicht erforderlich";
			break;

		case RED:
			result = "überschritten";
			break;

		case COUNTDOWN14:
			result = "bald fällig";
			break;

		case OFFEN:
			result = "offen";
			break;

		case LILA:
			result = "überschritten/offen";
			break;

		default:
			result = "nicht erforderlich";
		}

		return result;
	}

	private static HSSFCell addBorder(HSSFWorkbook wb, HSSFCell cell, String borderAlignment,
	    short borderFormat) {
		if (cell == null) {
			return null;
		}

		HSSFCellStyle style = null;
		// if (borderStyles.containsKey(borderAlignment)) {
		// style = borderStyles.get(borderAlignment);
		// } else {
		HSSFCellStyle srcStyle = cell.getCellStyle();
		style = wb.createCellStyle();
		style.cloneStyleFrom(srcStyle);

		if (borderAlignment.indexOf("borderTop-Date") > -1) {
			style.setBorderTop(borderFormat);
		} else if (borderAlignment.indexOf("borderTop") > -1) {
			style.setBorderTop(borderFormat);
		} else if (borderAlignment.indexOf("borderLeft") > -1) {
			style.setBorderLeft(borderFormat);
		} else if (borderAlignment.indexOf("borderRight") > -1) {
			style.setBorderRight(borderFormat);
		} else if (borderAlignment.indexOf("borderBottom") > -1) {
			style.setBorderBottom(borderFormat);
		}

		// borderStyles.put(borderAlignment, style);
		// }

		cell.setCellStyle(style);

		return cell;
	}

	public void setBaumassnahmeService(BaumassnahmeService service) {
		this.baumassnahmeService = service;
	}
}
