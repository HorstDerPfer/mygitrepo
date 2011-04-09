package db.training.osb.web.streckenband;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseDispatchAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.Streckenband;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BuendelService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.StreckenbandService;
import db.training.osb.service.StreckenbandService.SortDirection;
import db.training.osb.service.VzgStreckeService;
import db.training.security.hibernate.TqmUser;

public class StreckenbandEditAction extends BaseDispatchAction {
	/*
	 * Die Schaltflächen zum Verschieben von Maßnahmen innerhalb eines Streckenbandes übergeben beim
	 * Aufruf als Parameter die ID der zu verschiebenden Streckenbandzeile sowie die Richtung, in
	 * die "geschoben" wird.
	 */
	private static final Logger log = Logger.getLogger(StreckenbandEditAction.class);

	public ActionForward moveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering StreckenbandEditAction::moveUp");

		TqmUser secUser = getSecUser();

		StreckenbandForm sbForm = (StreckenbandForm) form;

		Integer rowId = sbForm.getMoveRowId();
		if (rowId == null) {
			return null;
		}

		StreckenbandService streckenbandService = EasyServiceFactory.getInstance()
		    .createStreckenbandService();
		Streckenband sb = streckenbandService.findByZeileId(rowId, sessionFahrplanjahr);

		if (EasyServiceFactory.getInstance().createStreckenbandAnyVoter()
		    .vote(secUser, sb, "ROLE_STRECKENBAND_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		sb = streckenbandService.moveRow(sb, rowId, SortDirection.MOVE_UP);

		request.setAttribute("gewerke", sbForm.getGewerke());
		request.setAttribute("VzgStreckeNummer", sbForm.getVzgStreckeId());
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);
		return mapping.findForward("SUCCESS");
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering StreckenbandEditAction::moveDown");

		TqmUser secUser = getSecUser();

		StreckenbandForm sbForm = (StreckenbandForm) form;

		Integer rowId = sbForm.getMoveRowId();
		if (rowId == null) {
			return null;
		}

		StreckenbandService streckenbandService = EasyServiceFactory.getInstance()
		    .createStreckenbandService();
		Streckenband sb = streckenbandService.findByZeileId(rowId, sessionFahrplanjahr);

		if (EasyServiceFactory.getInstance().createStreckenbandAnyVoter()
		    .vote(secUser, sb, "ROLE_STRECKENBAND_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		sb = streckenbandService.moveRow(sb, rowId, SortDirection.MOVE_DOWN);

		request.setAttribute("gewerke", sbForm.getGewerke());
		request.setAttribute("VzgStreckeNummer", sbForm.getVzgStreckeId());
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);
		return mapping.findForward("SUCCESS");
	}

	public ActionForward createBuendel(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering StreckenbandEditAction::createBuendel");

		StreckenbandForm sbForm = (StreckenbandForm) form;
		if (sbForm == null) {
			// nichts tun und auf Streckenbandansicht weiterleiten
			return mapping.findForward("SUCCESS");
		}

		if (log.isDebugEnabled())
			log.debug("Erstelle Bündel für Gleissperrungen: "
			    + Arrays.toString(sbForm.getGleissperrungenIds()));

		GleissperrungService gleissperrungService = EasyServiceFactory.getInstance()
		    .createGleissperrungService();

		List<Gleissperrung> gleissperrungen = new ArrayList<Gleissperrung>();
		if (sbForm.getGleissperrungenIds() != null && sbForm.getGleissperrungenIds().length > 0)
			gleissperrungen = gleissperrungService.findByIds(sbForm.getGleissperrungenIds(),
			    new Preload[] { new Preload(Gleissperrung.class, "buendel"),
			            new Preload(Buendel.class, "gleissperrungen"),
			            new Preload(Gleissperrung.class, "vzgStrecke"),
			            new Preload(Regelung.class, "massnahme"),
			            new Preload(Gleissperrung.class, "baustellen"),
			            new Preload(SAPMassnahme.class, "gleissperrungen"),
			            new Preload(Gleissperrung.class, "vtr") });
		// gleissperrungen = gleissperrungService.findByIds(sbForm.getGleissperrungenIds(),
		// new FetchPlan[] { FetchPlan.OSB_BUENDEL, FetchPlan.OSB_REGIONALBEREICH,
		// FetchPlan.OSB_GLEISSPERRUNG_STRECKE,
		// FetchPlan.OSB_GLEISSPERRUNG_BAUSTELLEN,
		// FetchPlan.OSB_GLEISSPERRUNG_MASSNAHME, FetchPlan.OSB_GLEISSPERRUNG });

		for (Gleissperrung gl : gleissperrungen) {
			if (log.isDebugEnabled())
				log.debug("Gleissperrung fuer Buendel: " + gl.getId());

			if (gl.getBuendel().size() > 0) {
				log.warn("Gleissperrung bereits " + gl.getBuendel().size() + "x gebuendelt. ");
				addError("error.buendel.massnahme.bereits.gebuendelt");
				return mapping.findForward("FAILURE");
			}
		}

		request.setAttribute("gleissperrungen", gleissperrungen);
		request.setAttribute("method", "createBuendel");
		request.setAttribute("buendelId", 0);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);

		return mapping.findForward("NEW_BUENDEL");
	}

	public ActionForward removeFromBuendel(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering StreckenbandEditAction::removeFromBuendel");

		StreckenbandForm sbForm = (StreckenbandForm) form;

		TqmUser secUser = getSecUser();

		BuendelService buendelService = serviceFactory.createBuendelService();
		GleissperrungService gleissperrungService = serviceFactory.createGleissperrungService();

		Integer buendelId = sbForm.getBuendelId();
		Integer gleissperrungId = sbForm.getGleissperrungId();

		Buendel buendel = buendelService.findById(buendelId, new Preload[] { new Preload(
		    Buendel.class, "gleissperrungen") });
		Gleissperrung gleissperrung = gleissperrungService.findById(gleissperrungId,
		    new Preload[] { new Preload(Gleissperrung.class, "buendel") });

		// BuendelId pruefen ****************************************
		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("No buendel found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing buendel: " + buendelId);

		if (gleissperrung == null) {
			if (log.isInfoEnabled())
				log.info("Gleissperrung nicht gefunden: " + gleissperrungId.toString());
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}

		if (EasyServiceFactory.getInstance().createBuendelAnyVoter()
		    .vote(secUser, buendel, "ROLE_BUENDEL_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		gleissperrung.getBuendel().remove(buendel);
		buendel.getGleissperrungen().remove(gleissperrung);

		// Änderungen speichern
		gleissperrungService.update(gleissperrung);
		buendelService.update(buendel);

		request.setAttribute("id", buendelId);

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);
		request.setAttribute("VzgStreckeNummer", gleissperrung.getVzgStrecke().getNummerAsString());

		return mapping.findForward("SUCCESS");
	}

	public ActionForward createBuendelOnPakete(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering StreckenbandEditAction::createBuendelOnPakete");

		Integer streckeId = FrontendHelper.castStringToInteger(request.getParameter("streckeId"));
		if (streckeId == null) {
			if (log.isWarnEnabled())
				log.warn("Keine VzgStreckeId!");
			addError("error.streckenband.no.streckeid");
			return mapping.findForward("FAILURE");
		}

		VzgStrecke strecke = null;
		VzgStreckeService streckenService = EasyServiceFactory.getInstance()
		    .createVzgStreckeService();
		strecke = streckenService.findById(streckeId);

		if (strecke == null) {
			addError("error.strecke.notfound");
			return mapping.findForward("FAILURE");
		}

		BuendelService buendelService = EasyServiceFactory.getInstance().createBuendelService();
		buendelService.createBuendelOnPaketeByVzgStrecke(strecke);

		return mapping.findForward("SUCCESS");
	}

	/*
	 * Aufruf löscht alle gespeicherten eines Streckenbandes. Die Daten werden beim nächsten Aufruf
	 * des Streckenbandes (StreckenbandViewAction) mit Standardwerten neu erzeugt.
	 */
	public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering StreckenbandEditAction::reset");

		StreckenbandForm sbForm = (StreckenbandForm) form;

		EasyServiceFactory.getInstance().createStreckenbandService()
		    .delete(sbForm.getVzgStreckeId(), sessionFahrplanjahr);

		VzgStrecke vzgStrecke = EasyServiceFactory.getInstance().createVzgStreckeService()
		    .findById(sbForm.getVzgStreckeId());
		if (vzgStrecke != null)
			request.setAttribute("vzgStrecke", vzgStrecke);

		request.setAttribute("VzgStreckeNummer", sbForm.getVzgStreckeId());
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);

		if (sbForm.getBuendelId() != null && sbForm.getBuendelId() != 0) {
			return mapping.findForward("SUCCESS_PROCESSBUENDEL");
		} else {
			request.setAttribute("resetFilter", Boolean.TRUE);
			return mapping.findForward("SUCCESS");
		}
	}
}
