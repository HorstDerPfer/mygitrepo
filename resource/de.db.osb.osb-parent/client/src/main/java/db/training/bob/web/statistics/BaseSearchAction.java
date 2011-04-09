package db.training.bob.web.statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.LazyInitializationException;

import db.training.bob.model.Art;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.SearchBean;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class BaseSearchAction extends BaseAction {

	private static final Logger log = Logger.getLogger(BaseSearchAction.class);

	private RegionalbereichService regionalbereichService;

	private BearbeitungsbereichService bearbeitungsbereichService;

	public BaseSearchAction() {
		EasyServiceFactory serviceFactoy = EasyServiceFactory.getInstance();
		regionalbereichService = serviceFactoy.createRegionalbereichService();
		bearbeitungsbereichService = serviceFactoy.createBearbeitungsbereichService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaseSearchAction.");

		List<Regionalbereich> listRegFpl = new ArrayList<Regionalbereich>();

		StatisticsFilterForm filterForm = (StatisticsFilterForm) form;
		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true"))
			filterForm.reset();

		try {
			// DropDown Liste: Regionalbereich
			listRegFpl = regionalbereichService.findAll();
			request.setAttribute("regionalbereiche", listRegFpl);

			// DropDown Liste: Maßnahmenarten
			List<Art> arten = Arrays.asList(Art.values());
			Collections.sort(arten);
			request.setAttribute("arten", arten);

			List<Bearbeitungsbereich> bearbeitungsbereiche = new ArrayList<Bearbeitungsbereich>();
			if (listRegFpl != null && listRegFpl.size() > 0)
				bearbeitungsbereiche = bearbeitungsbereichService.findByRegionalbereich(listRegFpl
				    .get(0));
			request.setAttribute("bearbeitungsbereiche", bearbeitungsbereiche);
		} catch (LazyInitializationException e) {
			if (log.isDebugEnabled())
				log.debug(e.getMessage());
			if (log.isDebugEnabled())
				log.debug(e);
			return mapping.findForward("FAILURE");
		}

		return mapping.findForward("SUCCESS");
	}

	/**
	 * erstellt ein SearchBean Objekt und überträgt alle Suchparameter aus der Form-Bean.
	 * <em>Der Parameter <c>evu</c> wird nicht verarbeitet.</em>
	 * 
	 * @param filterForm
	 *            Form-Bean des Suchformulars für Auswertungen
	 * @return gibt <c>null</c> zurück, wenn filterForm oder alle Suchparameter <c>null</c> sind.
	 */
	protected SearchBean createSearchBean(StatisticsFilterForm filterForm) {
		SearchBean searchBean = null;

		if ((filterForm != null)
		    && ((filterForm.getArt() != null) || (filterForm.getKigBauNr() != null)
		        || (filterForm.getKorridorNr() != null) || (filterForm.getQsNr() != null)
		        || (filterForm.getBeginnDatum() != null) || (filterForm.getEndDatum() != null)
		        || (filterForm.getRegionalbereichFpl() != null)
		        || (filterForm.getBearbeitungsbereich() != null)
		        || (filterForm.getMilestones() != null)
		        || (filterForm.getControllingBeginnDatum() != null)
		        || (filterForm.getControllingEndDatum() != null)
		        || (filterForm.getNurAktiv() != null) || (filterForm.getBauZeitraumVon() != null)
		        || (filterForm.getBauZeitraumBis() != null)
		        || (filterForm.getRegionalbereichBM() != null)
		        || (filterForm.getZeitraumVerkehrstagVon() != null)
		        || (filterForm.getZeitraumVerkehrstagBis() != null)
		        || (filterForm.getQsks() != null) || (filterForm.getLetzteXWochen() != null) || (filterForm
		        .getNaechsteXWochen() != null))) {

			// Suchparameter
			searchBean = new SearchBean();
			searchBean.setSearchArt(filterForm.getArt());
			searchBean.setSearchKigBauNr(filterForm.getKigBauNr());
			searchBean.setSearchKorridorNr(filterForm.getKorridorNr());
			searchBean.setSearchQsNr(filterForm.getQsNr());
			searchBean.setSearchBeginnDatum(filterForm.getBeginnDatum());
			searchBean.setSearchEndDatum(filterForm.getEndDatum());
			searchBean.setSearchRegionalbereichFpl(filterForm.getRegionalbereichFpl());
			searchBean.setSearchBearbeitungsbereich(filterForm.getBearbeitungsbereich());
			searchBean.setSearchMilestones(filterForm.getMilestones());
			searchBean.setSearchControllingBeginnDatum(filterForm.getControllingBeginnDatum());
			searchBean.setSearchControllingEndDatum(filterForm.getControllingEndDatum());
			searchBean.setNurAktiv(filterForm.getNurAktiv());
			searchBean.setBauZeitraumVon(filterForm.getBauZeitraumVon());
			searchBean.setBauZeitraumBis(filterForm.getBauZeitraumBis());
			searchBean.setSearchRegionalbereichBM(filterForm.getRegionalbereichBM());
			searchBean.setSearchVerkehrstagBeginnDatum(filterForm.getZeitraumVerkehrstagVon());
			searchBean.setSearchVerkehrstagEndDatum(filterForm.getZeitraumVerkehrstagBis());
			searchBean.setQsks(filterForm.getQsks());
			searchBean.setOnlyOpenMilestones(false);
			searchBean.setLetzteXWochen(filterForm.getLetzteXWochen());
			searchBean.setNaechsteXWochen(filterForm.getNaechsteXWochen());
			if ("datum".equalsIgnoreCase(filterForm.getOptionDatumZeitraum()))
				searchBean.setOptionZeitraum(false);
			else
				searchBean.setOptionZeitraum(true);
		}

		return searchBean;
	}
}
