package db.training.bob.web.ajax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.Hibernate;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Abweichung;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.BbzrService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.bob.web.baumassnahme.ZugComparator;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshBBZRAction extends BaseAction {

	private static final int OBJECTS_PER_PAGE = 10;

	private static final Logger log = Logger.getLogger(RefreshBBZRAction.class);

	private BaumassnahmeService baumassnahmeService;

	private BbzrService bbzrService;

	public RefreshBBZRAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		bbzrService = serviceFactory.createBbzrService();
	}

	@Override
	public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshBBZRAction.");

		BaumassnahmeForm bmForm = (BaumassnahmeForm) form;
		Integer bmId = bmForm.getId();
		Integer id = null;
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("bbzr"))) {
			id = FrontendHelper.castStringToInteger(request.getParameter("bbzr"));
		}

		String sortCriterion = null;
		SortOrderEnum order = null;
		Baumassnahme bm = null;
		int page = 1;

		if ("asc".equalsIgnoreCase(request.getParameter("dir"))) {
			order = SortOrderEnum.ASCENDING;
		} else if ("desc".equalsIgnoreCase(request.getParameter("dir"))) {
			order = SortOrderEnum.DESCENDING;
		} else {
			order = SortOrderEnum.ASCENDING;
		}

		sortCriterion = request.getParameter("sort");

		Uebergabeblatt zvf = null;
		if (bmId != null) {
			FetchPlan[] fetchPlansBaumassnahme = new FetchPlan[] {
			        FetchPlan.BOB_AENDERUNGSDOKUMENTATION,
			        FetchPlan.BOB_ARBEITSLEISTUNG_REGIONALBEREICHE, FetchPlan.BOB_AUSFALLGRUND,
			        FetchPlan.BOB_BBP_MASSNAHME, FetchPlan.BOB_BEARBEITER,
			        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_KIGBAU, FetchPlan.BOB_QS,
			        FetchPlan.BOB_KORRIDOR_ZEITFENSTER, FetchPlan.BOB_REGIONALBEREICH_FPL,
			        FetchPlan.BOB_TERMINE_BBP, FetchPlan.BOB_TERMINE_GEVU,
			        FetchPlan.BOB_TERMINE_PEVU, FetchPlan.BOB_UEBERGABEBLATT,
			        FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH, FetchPlan.BOB_BBZR,
			        FetchPlan.BOB_ZUGCHARAKTERISTIK, FetchPlan.BBZR_BAUMASSNAHMEN,
			        FetchPlan.ZVF_MN_VERSION };
			FetchPlan[] fetchPlansBbzr = new FetchPlan[] { FetchPlan.BOB_UEBERGABEBLATT,
			        FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH, FetchPlan.UEB_BAUMASSNAHMEN,
			        FetchPlan.UEB_HEADER, FetchPlan.UEB_HEADER_SENDER,
			        FetchPlan.UEB_HEADER_EMPFAENGER, FetchPlan.UEB_BEARBEITUNGSSTATUS,
			        FetchPlan.BOB_BBZR, FetchPlan.BBZR_HEADER, FetchPlan.BBZR_BAUMASSNAHMEN,
			        FetchPlan.UEB_REGIONALBEREICHE, FetchPlan.UEB_MN_FPLO,
			        FetchPlan.ZVF_MN_STRECKEN, FetchPlan.ZVF_MN_STRECKE_STRECKEVZG,
			        FetchPlan.ZVF_MN_BBPSTRECKE, FetchPlan.ZVF_MN_VERSION, FetchPlan.BBZR_MN_ZUEGE,
			        FetchPlan.UEB_ZUG_REGELWEG, FetchPlan.UEB_MN_ZUEGE, FetchPlan.UEB_KNOTENZEITEN,
			        FetchPlan.UEB_ZUG_ABWEICHUNG_HALT_BAHNHOF };
			bm = baumassnahmeService.findById(bmId, fetchPlansBaumassnahme);
			zvf = bbzrService.findById(id, fetchPlansBbzr);

		}

		request.setAttribute("selectedBbzrID", id);
		request.setAttribute("baumassnahme", bm);
		bmForm.setZvf(id);

		// Zuglisten für Ü-Blatt erzeugen
		Uebergabeblatt selectedBbzr = zvf;
		request.setAttribute("viewBbzr", zvf);
		request.setAttribute("countZuege", zvf.getMassnahmen().iterator().next().getZug().size());

		Set<Massnahme> massnahmen = selectedBbzr.getMassnahmen();
		if (massnahmen != null) {
			PaginatedList<Zug> pagUmzuleitendeZuege = new PaginatedList<Zug>();
			PaginatedList<Zug> pagVerspaeteteZuege = new PaginatedList<Zug>();
			PaginatedList<Zug> pagAusfaelle = new PaginatedList<Zug>();
			PaginatedList<Zug> pagVorplanfahren = new PaginatedList<Zug>();
			PaginatedList<Zug> pagAusfallVerkehrshalte = new PaginatedList<Zug>();
			PaginatedList<Zug> pagSperrenBedarfsplaene = new PaginatedList<Zug>();
			List<Zug> umzuleitendeZuege = new ArrayList<Zug>();
			List<Zug> verspaeteteZuege = new ArrayList<Zug>();
			List<Zug> ausfaelle = new ArrayList<Zug>();
			List<Zug> vorplanfahren = new ArrayList<Zug>();
			List<Zug> ausfallVerkehrshalte = new ArrayList<Zug>();
			List<Zug> sperrenBedarfsplaene = new ArrayList<Zug>();

			// Über alle Maßnahmen iterieren
			for (Massnahme mn : massnahmen) {
				if (mn.getZug() == null || Hibernate.isInitialized(mn.getZug()) == false) {
					continue;
				}

				// über alle Züge iterieren und Züge nach Abweichungstyp sortieren
				List<Zug> zuege = mn.getZug();
				Collections.sort(zuege, new ZugComparator("id", SortOrderEnum.ASCENDING));
				Iterator<Zug> it = zuege.iterator();
				Zug zug = null;
				int lfdNrUmzuleitendeZuege = 1;
				int lfdNrVerspaeteteZuege = 1;
				int lfdNrSperrenBedarfsplaene = 1;
				int lfdNrAusfaelle = 1;
				int lfdNrVorplanfahren = 1;
				int lfdNrAusfallVerkehrshalte = 1;
				while (it.hasNext()) {
					zug = it.next();
					if (zug.getAbweichung() != null) {
						Abweichung abweichung = zug.getAbweichung();
						if (abweichung.getArt() == Abweichungsart.UMLEITUNG) {
							zug.setLaufendeNr(lfdNrUmzuleitendeZuege++);
							umzuleitendeZuege.add(zug);
						} else if (abweichung.getArt() == Abweichungsart.VERSPAETUNG) {
							zug.setLaufendeNr(lfdNrVerspaeteteZuege++);
							verspaeteteZuege.add(zug);
						} else if (abweichung.getArt() == Abweichungsart.GESPERRT) {
							zug.setLaufendeNr(lfdNrSperrenBedarfsplaene++);
							sperrenBedarfsplaene.add(zug);
						} else if (abweichung.getArt() == Abweichungsart.AUSFALL) {
							zug.setLaufendeNr(lfdNrAusfaelle++);
							ausfaelle.add(zug);
						} else if (abweichung.getArt() == Abweichungsart.VORPLAN) {
							zug.setLaufendeNr(lfdNrVorplanfahren++);
							vorplanfahren.add(zug);
						} else if (abweichung.getArt() == Abweichungsart.ERSATZHALTE) {
							zug.setLaufendeNr(lfdNrAusfallVerkehrshalte++);
							ausfallVerkehrshalte.add(zug);
						}
					}
				}
			}

			setAttributes(pagUmzuleitendeZuege, umzuleitendeZuege, sortCriterion, order, page);
			setAttributes(pagVerspaeteteZuege, verspaeteteZuege, sortCriterion, order, page);
			setAttributes(pagAusfaelle, ausfaelle, sortCriterion, order, page);
			setAttributes(pagVorplanfahren, vorplanfahren, sortCriterion, order, page);
			setAttributes(pagAusfallVerkehrshalte, ausfallVerkehrshalte, sortCriterion, order, page);
			setAttributes(pagSperrenBedarfsplaene, sperrenBedarfsplaene, sortCriterion, order, page);
			request.setAttribute("UMLEITUNG", pagUmzuleitendeZuege);
			request.setAttribute("VERSPAETUNG", pagVerspaeteteZuege);
			request.setAttribute("AUSFALL", pagAusfaelle);
			request.setAttribute("VORPLAN", pagVorplanfahren);
			request.setAttribute("ERSATZHALTE", pagAusfallVerkehrshalte);
			request.setAttribute("GESPERRT", pagSperrenBedarfsplaene);
		}

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		if (request.getParameter("dir") == null)
			return mapping.findForward("SUCCESS");// andere ZvF anzeigen

		request.setAttribute("tab", "BBZR");
		return mapping.findForward("SUCCESS_SORT");// Sortierung ändern
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