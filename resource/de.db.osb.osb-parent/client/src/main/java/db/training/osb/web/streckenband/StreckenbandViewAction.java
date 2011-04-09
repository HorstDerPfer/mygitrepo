package db.training.osb.web.streckenband;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.util.CollectionHelper;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.Streckenband;
import db.training.osb.model.StreckenbandZeile;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.StreckenbandService;
import db.training.osb.service.VzgStreckeService;
import db.training.security.hibernate.TqmUser;

public class StreckenbandViewAction extends BaseAction {

	private static final Logger log = Logger.getLogger(StreckenbandViewAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering StreckenbandViewAction.");

		TqmUser secUser = getSecUser();

		StreckenbandForm filterForm = (StreckenbandForm) form;

		if ((request.getParameter("resetFilter") != null || request.getAttribute("resetFilter") != null)
		    && filterForm != null) {
			filterForm.setGewerke(null);
		}

		filterForm.setBuendelId(null);

		// Sessionattribute von Popup löschen
		request.getSession().removeAttribute("popupBaumassnahme");
		request.getSession().removeAttribute("popupGleissperrung");

		// ///////
		// VzG Strecken lesen und Betriebsstellen auflisten
		BetriebsstelleService betriebsstelleService = getBetriebsstelleService();

		VzgStrecke streckeVzg = null;
		List<BetriebsstelleVzgStreckeLink> bstList = null;
		Integer streckeVzgNummer = null;

		if (filterForm != null && filterForm.getVzgStreckeId() != null) {
			streckeVzgNummer = filterForm.getVzgStreckeId();
		} else if (request.getAttribute("VzgStreckeNummer") != null) {

			streckeVzgNummer = FrontendHelper.castStringToInteger(request.getAttribute(
			    "VzgStreckeNummer").toString());
		}

		if (streckeVzgNummer == null) {
			if (log.isDebugEnabled())
				log.debug("Keine VzG-Strecken-Nr. übergeben.");
			addError("error.notfound.vzg", msgRes.getMessage("common.notdefined"));
			return mapping.findForward("FAILURE");
		}

		streckeVzg = CollectionHelper.getFirst(getVzgStreckeService().findByNummer(
		    streckeVzgNummer, sessionFahrplanjahr, null, true, null));
		if (streckeVzg == null) {
			if (log.isDebugEnabled())
				log.debug("VZG Strecke nicht gefunden. Nummer=" + streckeVzgNummer);
			addError("error.notfound.vzg", Integer.toString(streckeVzgNummer));
			return mapping.findForward("FAILURE");
		}

		// ////////
		// Maßnahmen auf Strecke auflisten
		StreckenbandService streckenbandService = getStreckenbandService();

		Streckenband sb = streckenbandService.findByVzgStrecke(streckeVzg, sessionFahrplanjahr,
		    new Preload[] { new Preload(StreckenbandZeile.class, "gleissperrung"),
		            new Preload(Gleissperrung.class, "massnahme"),
		            new Preload(Gleissperrung.class, "bstVon"),
		            new Preload(Gleissperrung.class, "bstBis"),
		            new Preload(Gleissperrung.class, "buendel"),
		            new Preload(SAPMassnahme.class, "paket"),
		            // new Preload(Gleissperrung.class, "regionalbereich"),
		            new Preload(Betriebsstelle.class, "strecken") });

		if (sb == null || sb.size() == 0) {
			if (log.isDebugEnabled())
				log.debug("Keine Maßnahmen auf Strecke gefunden.");
			addError("error.notfound.massnahmen");
			return mapping.findForward("FAILURE");
		}

		bstList = betriebsstelleService.findByVzgStreckeReducedAndFahrplanjahr(streckeVzg, sb,
		    sessionFahrplanjahr);

		if (EasyServiceFactory.getInstance().createStreckenbandAnyVoter()
		    .vote(secUser, sb, "ROLE_STRECKENBAND_LESEN") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		// Gewerke-Filter füllen
		// die Liste wird um "" ergänzt, damit im Filter Formular auch der Haken
		// bei (leer) gesetzt
		// ist
		String[] gewerkeArray = new String[10];
		{
			Collection<String> gewerkeList = streckenbandService.findGewerkeOnStreckenband(
			    streckeVzg, sessionFahrplanjahr);
			gewerkeList.add("");
			gewerkeArray = gewerkeList.toArray(gewerkeArray);
		}

		// Gewerke-Filter anwenden
		Streckenband result = new Streckenband();

		if (filterForm.getGewerke() != null
		    && (gewerkeArray.length != filterForm.getGewerke().length)) {
			Collection<String> gewerke = Arrays.asList(filterForm.getGewerke());
			for (StreckenbandZeile zeile : sb) {
				if (zeile.getGleissperrung() != null
				    && zeile.getGleissperrung().getMassnahme() != null) {
					if (zeile.getGleissperrung().getMassnahme().getGewerk() != null) {
						if (gewerke.contains(zeile.getGleissperrung().getMassnahme().getGewerk()))
							result.add(zeile);
					} else if (gewerke.contains("")) {
						result.add(zeile);
					}
				}
			}
			request.setAttribute("isFilterApplied", Boolean.TRUE);
			request.setAttribute("streckenband", result);
		} else {
			request.setAttribute("isFilterApplied", Boolean.FALSE);
			filterForm.setGewerke(gewerkeArray);
			request.setAttribute("streckenband", sb);
		}

		request.setAttribute("streckeVzg", streckeVzg);
		request.setAttribute("gewerkeList", gewerkeArray);

		request.setAttribute("bstList", bstList);

		request.setAttribute("streckenbandViewType",
		    StreckenbandViewConstants.STRECKENBANDVIEW_VIEW);

		return mapping.findForward("SUCCESS");
	}

	private VzgStreckeService getVzgStreckeService() {
		return EasyServiceFactory.getInstance().createVzgStreckeService();
	}

	private BetriebsstelleService getBetriebsstelleService() {
		return EasyServiceFactory.getInstance().createBetriebsstelleService();
	}

	private StreckenbandService getStreckenbandService() {
		return EasyServiceFactory.getInstance().createStreckenbandService();
	}
}
