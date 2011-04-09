package db.training.bob.web.ajax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxanywhere.AAUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.Hibernate;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Abweichung;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Regelweg;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.bob.web.baumassnahme.ZugComparator;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class ZvfZugViewAction extends BaseAction {

	private BaumassnahmeService service;

	private Logger log;

	public ZvfZugViewAction() {
		log = Logger.getLogger(this.getClass());
		try {
			serviceFactory = EasyServiceFactory.getInstance();
			service = serviceFactory.createBaumassnahmeService();
		} catch (Exception e) {// do nothing
		}
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering ZvfZugViewAction.");

		int id = 0;
		int zvfId = 0;

		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		id = baumassnahmeForm.getId();
		zvfId = baumassnahmeForm.getZvf();

		if (AAUtils.isAjaxRequest(request)) {
			AAUtils.addZonesToRefreh(request, "divZvfZuege");
		}

		String sortCriterion = request.getParameter("sort");
		SortOrderEnum order = null;

		if ("asc".equalsIgnoreCase(request.getParameter("dir"))) {
			order = SortOrderEnum.ASCENDING;
		} else if ("desc".equalsIgnoreCase(request.getParameter("dir"))) {
			order = SortOrderEnum.DESCENDING;
		} else {
			order = SortOrderEnum.ASCENDING;
		}

		Preload[] preloads = new Preload[] { new Preload(Baumassnahme.class, "zvf"),
		        new Preload(Uebergabeblatt.class, "header"), new Preload(Header.class, "sender"),
		        new Preload(Uebergabeblatt.class, "massnahmen"),
		        new Preload(Massnahme.class, "version"), new Preload(Massnahme.class, "strecke"),
		        new Preload(Massnahme.class, "bbp"), new Preload(Strecke.class, "streckeVZG"),
		        new Preload(Massnahme.class, "zug"), new Preload(Zug.class, "regelweg"),
		        new Preload(Regelweg.class, "abgangsbahnhof"),
		        new Preload(Regelweg.class, "zielbahnhof"), new Preload(Zug.class, "abweichung"),
		        new Preload(Abweichung.class, "halt"), new Preload(Halt.class, "ausfall"),
		        new Preload(Halt.class, "ersatz"), new Preload(Abweichung.class, "ausfallvon"),
		        new Preload(Abweichung.class, "ausfallbis"),
		        new Preload(Abweichung.class, "vorplanab") };
		Baumassnahme baumassnahme = service.findById(id, preloads);

		request.setAttribute("baumassnahme", baumassnahme);

		List<Uebergabeblatt> zvfs = baumassnahme.getZvf();

		if (zvfs != null) {
			for (Uebergabeblatt zvf : zvfs) {
				if (zvf.getId() == zvfId)
					request.setAttribute("viewZvf", zvf);
				if (zvf.getMassnahmen() != null & baumassnahmeForm.getShowZuegeZvf() == true) {
					request.setAttribute("countZuege", zvf.getMassnahmen().iterator().next()
					    .getZug().size());
					setZugListen(zvf.getMassnahmen(), request, sortCriterion, order);
				}
			}
		}
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");
		return mapping.findForward("SUCCESS");
	}

	private void setZugListen(Set<Massnahme> massnahmen, HttpServletRequest request,
	    String sortCriterion, SortOrderEnum order) {
		final int page = 1;

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

	private void setAttributes(PaginatedList<Zug> paginatedList, List<Zug> list,
	    String sortCriterion, SortOrderEnum order, int page) {
		final int OBJECTS_PER_PAGE = 10;

		Collections.sort(list, new ZugComparator(sortCriterion, order));
		paginatedList.setList(list);
		paginatedList.setSortCriterion(sortCriterion);
		paginatedList.setSortDirection(order);
		paginatedList.setObjectsPerPage(OBJECTS_PER_PAGE);
		paginatedList.setPageNumber(page);
	}

	public void setBaumassnahmeService(BaumassnahmeService service) {
		this.service = service;
	}

}
