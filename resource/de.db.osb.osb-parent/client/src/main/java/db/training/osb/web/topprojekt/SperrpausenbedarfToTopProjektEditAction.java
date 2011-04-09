/**
 * 
 */
package db.training.osb.web.topprojekt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Paket;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.PaketService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.osb.service.TopProjektService;
import db.training.osb.web.sperrpausenbedarf.SperrpausenbedarfFilterForm;

/**
 * dient der Bearbeitung der Beziehung zwischen TOP-Projekt und Sperrpausenbedarf: - zu einem
 * ausgewählten TOP-Projekt können Sperrpausenbedarfe zugeordnet oder entfernt werden.
 * 
 * @author michels
 */
public class SperrpausenbedarfToTopProjektEditAction extends BaseAction {

	private static Logger log = Logger.getLogger(SperrpausenbedarfToTopProjektEditAction.class);

	private static final FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.OSB_REGIONALBEREICH,
	        FetchPlan.OSB_PAKET, FetchPlan.OSB_WEITERE_STRECKEN };

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering " + getClass().getSimpleName());

		TopProjektService topProjektService = serviceFactory.createTopProjektService();
		SAPMassnahmeService mnService = serviceFactory.createSAPMassnahmeService();

		Integer topProjektId = null;
		if (request.getAttribute("topProjektId") != null)
			topProjektId = (Integer) request.getAttribute("topProjektId");
		else
			topProjektId = FrontendHelper.castStringToInteger(request.getParameter("topProjektId"));

		if (topProjektId == null) {
			addError("topprojekt.error.notfound");
			return mapping.findForward("FAILURE");
		}

		TopProjekt topProjekt = topProjektService.findById(topProjektId, new Preload[] {
		        new Preload(TopProjekt.class, "regionalbereich"),
		        new Preload(TopProjekt.class, "massnahmen"),
		        new Preload(SAPMassnahme.class, "topProjekte") });

		request.setAttribute("topProjekt", topProjekt);

		// Filter und Suche

		SperrpausenbedarfFilterForm filterForm = (SperrpausenbedarfFilterForm) form;
		if (filterForm == null)
			filterForm = new SperrpausenbedarfFilterForm();

		List<SAPMassnahme> massnahmen = mnService.findByFahrplanjahr(sessionFahrplanjahr,
		    new FetchPlan[] { FetchPlan.OSB_REGIONALBEREICH });

		// get search parameters
		if (log.isDebugEnabled()) {
			log.debug("SperrpausenbedarfToTopProjektEdit para: regionalberId="
			    + filterForm.getRegionalbereichId());
			log.debug("SperrpausenbedarfToTopProjektEdit para: anmelder="
			    + filterForm.getAnmelder());
			log.debug("SperrpausenbedarfToTopProjektEdit para: gewerk=" + filterForm.getGewerk());
			log.debug("SperrpausenbedarfToTopProjektEdit para: paket=" + filterForm.getPaket());
			log.debug("SperrpausenbedarfToTopProjektEdit para: streckeVZG="
			    + filterForm.getStreckeVZG());
			log.debug("SperrpausenbedarfToTopProjektEdit para: untergewerk="
			    + filterForm.getUntergewerk());
			log.debug("SperrpausenbedarfToTopProjektEdit para: sapProjektnr="
			    + filterForm.getSapProjektnummer());
		}

		// Validierung PaketId
		if (filterForm.getPaket() != null && filterForm.getPaket().trim().length() > 0) {
			if (!filterForm.getPaket().trim()
			    .matches("[0-9][0-9][.][0-9][0-9][.][0-9][0-9][0-9][0-9]")) {
				if (log.isDebugEnabled())
					log.debug("fehler in paketId format " + filterForm.getPaket());
				addError("error.paketId.format", "Paket");
			}
		}

		// Validierung Streckenband
		if (filterForm.getStreckeVZG() != null && filterForm.getStreckeVZG().trim().length() > 0) {
			if (VzgStrecke.getId(filterForm.getStreckeVZG()) == null) {
				if (log.isDebugEnabled())
					log.debug("fehler in streckenband feld " + filterForm.getStreckeVZG());
				addError("error.hauptstreckeNr.format", "Hauptstreckennr.");
			}
		}

		// fill regionalbereich liste
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		request.setAttribute("regionalbereichListe", regionalbereichService.findAll());

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
				paket = paketService.findByRbFahrplanjahrAndLfdNr(regionalbereich,
				    Paket.getJahrFromPaketId(strPaketId), Paket.getLfdNrFromPaketId(strPaketId));

				if (log.isDebugEnabled() && paket != null)
					log.debug("paketId=" + paket.getId());
			}

			if (paket != null)
				paketId = paket.getId();
		}

		SAPMassnahmeService sapMassnahmeService = serviceFactory.createSAPMassnahmeService();

		Integer hauptstreckenNummer = VzgStrecke.getId(filterForm.getStreckeVZG());

		if (filterForm.getPaket() != null && filterForm.getPaket().trim().length() > 0
		    && paketId == null) {
			massnahmen = null;
		} else {
			// call search function
			massnahmen = sapMassnahmeService.findByKeywords(filterForm.getRegionalbereichId(),
			    filterForm.getAnmelder(), filterForm.getGewerk(), filterForm.getUntergewerk(),
			    hauptstreckenNummer, paketId, filterForm.getSapProjektnummer(), fetchPlans,
			    sessionFahrplanjahr);
		}

		request.setAttribute("massnahmen", massnahmen);

		if (log.isDebugEnabled())
			log.debug("SUCCESS");

		return mapping.findForward("SUCCESS");
	}
}
