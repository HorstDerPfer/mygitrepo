package db.training.osb.web.gleissperrung;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.BuendelService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.web.streckenband.StreckenbandForm;
import db.training.security.hibernate.TqmUser;

/**
 * fügt eine oder mehrere Gleissperrungen zu einem Bündel hinzu.
 * 
 * @author michels
 * 
 */
public class AddGleissperrungenToBuendelAction extends BaseAction {

	public static final String ADD_GLEISSPERRUNG_TO_BUENDEL_BUENDELID = "__buendel_id__";

	private static final Logger log = Logger.getLogger(AddGleissperrungenToBuendelAction.class);

	public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering AddGleissperrungenToBuendelAction");

		TqmUser secUser = getSecUser();

		StreckenbandForm sbForm = (StreckenbandForm) form;

		if (log.isDebugEnabled()) {
			if (sbForm.getBuendelId() != null)
				log.debug("Einfügen in Bündel: " + sbForm.getBuendelId().toString());
			if (sbForm.getGleissperrungenIds() == null
			    || sbForm.getGleissperrungenIds().length == 0)
				log.debug("Gleissperrungen-Liste ist Leer.");
			else {
				log.debug("Gleissperrungen-Liste:");
				for (int i = 0; i < sbForm.getGleissperrungenIds().length; i++) {
					log.debug("Gleissperrung: " + sbForm.getGleissperrungenIds()[i]);
				}
			}
		}

		// Pruefung auf buendelId
		if (sbForm == null || sbForm.getBuendelId() == null) {
			// fehler: keine form, buendelId, oder massnahmen liste ist leer
			if (log.isDebugEnabled())
				log.debug("No buendel found");
			addError("error.notfound", msgRes.getMessage("buendel"));
			return mapping.findForward("FAILURE");
		}

		// Wenn keine gleissperrungIds uebergeben werden, wieder zurueck in Formular
		if (sbForm.getGleissperrungenIds() == null
		    || (sbForm.getGleissperrungenIds() != null && sbForm.getGleissperrungenIds().length == 0)) {
			if (log.isDebugEnabled()) {
				log.debug("Es wurden keine Gleissperrungen ausgewaehlt");
			}
			addMessage("buendel.message.gleissperrungIds.empty");
			return mapping.findForward("SUCCESS");
		}

		BuendelService buendelService = serviceFactory.createBuendelService();

		Buendel buendel = buendelService.findById(sbForm.getBuendelId(), new Preload[] {
		        new Preload(Buendel.class, "weitereStrecken"),
		        new Preload(Buendel.class, "gleissperrungen"),
		        new Preload(Buendel.class, "gleissperrungen"),
		        new Preload(Gleissperrung.class, "buendel"),
		        new Preload(Regelung.class, "vzgStrecke") });

		// buendel nicht gefunden
		if (buendel == null) {
			if (log.isDebugEnabled())
				log.debug("Bündel nicht gefunden: " + sbForm.getBuendelId().toString());
			addError("error.buendelnichtgefunden");
			return mapping.findForward("FAILURE");
		}

		if (EasyServiceFactory.getInstance().createBuendelAnyVoter()
		    .vote(secUser, buendel, "ROLE_BUENDEL_GLEISSPERRUNG_ZUORDNEN") == AccessDecisionVoter.ACCESS_DENIED) {
			addError("common.noAuth");
			return mapping.findForward("FAILURE");
		}

		GleissperrungService gleissperrungService = serviceFactory.createGleissperrungService();
		List<Gleissperrung> gleissperrungen = gleissperrungService.findByIds(
		    sbForm.getGleissperrungenIds(), new Preload[] {
		            new Preload(Gleissperrung.class, "buendel"),
		            new Preload(Regelung.class, "vzgStrecke"),
		            new Preload(Buendel.class, "gleissperrungen"),
		            new Preload(Buendel.class, "weitereStrecken"),
		            new Preload(Gleissperrung.class, "massnahme"),
		            new Preload(SAPMassnahme.class, "regionalbereich") });

		for (Gleissperrung gl : gleissperrungen) {
			if (log.isDebugEnabled())
				log.debug("Zu bündelnde Gleissperrung ID: " + gl.getId());

			if (gl.getBuendel().size() > 0) {
				if (log.isInfoEnabled())
					log.info("Gleissperrung ID:" + gl.getId() + " bereits "
					    + gl.getBuendel().size() + "x gebuendelt.");
			}

			gl.getBuendel().add(buendel);
			buendel.getGleissperrungen().add(gl);

			if (serviceFactory.createRegelungAnyVoter().vote(secUser, gl,
			    "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				addError("error.buendel.sperrpausenbedarf.noAuth", gl.getId().toString());
			} else {
				// Gleissperrung speichern
				gleissperrungService.update(gl);
			}
		}

		// Bündel speichern
		buendel.refreshWeitereStrecken();
		buendel.setLastChange(null);
		buendel = buendelService.merge(buendel);
		sbForm.reset();

		if (mapping.getPath().contains("/save")) {
			request.getSession().setAttribute(ADD_GLEISSPERRUNG_TO_BUENDEL_BUENDELID,
			    buendel.getId());
		}

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);

		addMessage("success.gleissperrung.add");
		return mapping.findForward("SUCCESS");
	}
}
