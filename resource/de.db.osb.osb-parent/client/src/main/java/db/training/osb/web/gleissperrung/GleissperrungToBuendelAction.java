package db.training.osb.web.gleissperrung;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.util.CollectionHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.Streckenband;
import db.training.osb.model.StreckenbandZeile;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.BuendelService;
import db.training.osb.service.StreckenbandService;
import db.training.osb.service.VzgStreckeService;
import db.training.osb.web.streckenband.StreckenbandForm;
import db.training.osb.web.streckenband.StreckenbandViewConstants;

public class GleissperrungToBuendelAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungToBuendelAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungToBuendelAction.");

		StreckenbandForm sbForm = (StreckenbandForm) form;

		BuendelService bService = serviceFactory.createBuendelService();
		Integer buendelId = sbForm.getBuendelId();
		Integer vzgStreckeId = sbForm.getVzgStreckeId();

		if (buendelId == null) {
			if (log.isDebugEnabled())
				log.debug("Kein buendelId vorhanden.");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}

		Buendel buendel = bService.findById(buendelId);

		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("Buendel nicht gefunden.");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}

		VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
		VzgStrecke vzgStrecke = null;

		// Wenn Strecke uebergeben wird, dann diese nehmen, ansonsten die des Buendels
		if (vzgStreckeId != null) {
			vzgStrecke = vzgService.findById(vzgStreckeId);
		} else {
			vzgStrecke = buendel.getHauptStrecke();
		}

		if (vzgStrecke == null) {
			if (log.isDebugEnabled())
				log.debug("Keine VzG-Strecken-Nr. übergeben.");
			addError("error.notfound.vzg", "(keine übergeben)");
			return mapping.findForward("FAILURE");
		}

		Integer streckeVzgNummer = vzgStrecke.getNummer();

		if (streckeVzgNummer == null) {
			if (log.isDebugEnabled())
				log.debug("Keine VzG-Strecken-Nr. übergeben.");
			addError("error.notfound.vzg", "(keine übergeben)");
			return mapping.findForward("FAILURE");
		}

		vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(streckeVzgNummer,
		    sessionFahrplanjahr, null, true, null));

		if (vzgStrecke == null) {
			if (log.isDebugEnabled())
				log.debug("VZG Strecke nicht gefunden. Nummer=");
			addError("error.notfound.vzg");
			return mapping.findForward("FAILURE");
		}

		// Maßnahmen auf Strecke auflisten
		StreckenbandService streckenbandService = serviceFactory.createStreckenbandService();
		Streckenband sb = streckenbandService.findByVzgStrecke(vzgStrecke, sessionFahrplanjahr,
		    new Preload[] { new Preload(StreckenbandZeile.class, "gleissperrung"),
		            new Preload(Gleissperrung.class, "massnahme"),
		            new Preload(Regelung.class, "bstVon"), new Preload(Regelung.class, "bstBis"),
		            new Preload(Gleissperrung.class, "buendel"),
		            new Preload(SAPMassnahme.class, "paket"),
		            // new Preload(Regelung.class, "regionalbereich"),
		            new Preload(Betriebsstelle.class, "strecken") });

		if (sb == null || sb.size() == 0) {
			if (log.isDebugEnabled())
				log.debug("Keine Maßnahmen auf Strecke gefunden.");
			addError("error.notfound.massnahmen");
			return mapping.findForward("FAILURE");
		}

		// Formular zurücksetzen
		sbForm.setGleissperrungenIds(null);

		List<BetriebsstelleVzgStreckeLink> bstList = null;

		BetriebsstelleService betriebsstelleService = serviceFactory.createBetriebsstelleService();
		bstList = betriebsstelleService.findByVzgStreckeReducedAndFahrplanjahr(vzgStrecke, sb,
		    sessionFahrplanjahr);

		request.setAttribute("streckeVzg", vzgStrecke);
		request.setAttribute("streckenband", sb);
		request.setAttribute("bstList", bstList);

		request.setAttribute("buendel", buendel);

		request.setAttribute("streckenbandViewType",
		    StreckenbandViewConstants.STRECKENBANDVIEW_PROCESSBUENDEL);

		return mapping.findForward("SUCCESS");
	}
}