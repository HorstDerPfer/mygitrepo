package db.training.osb.web.massnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseDispatchAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Langsamfahrstelle;
import db.training.osb.model.Oberleitung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.LangsamfahrstelleService;
import db.training.osb.service.OberleitungService;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.hibernate.TqmUser;

public class MassnahmeRegelungDeleteAction extends BaseDispatchAction {

	private static final Logger log = Logger.getLogger(MassnahmeRegelungSaveAction.class);

	private static EasyAccessDecisionVoter voterRegelung = EasyServiceFactory.getInstance()
	    .createRegelungAnyVoter();

	/* GLEISSPERRUNG */
	public ActionForward gleissperrung(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungDeleteAction->gleissperrung.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), secUser, request);
		if (massnahme == null)
			return mapping.findForward("FAILURE");

		GleissperrungService gsService = serviceFactory.createGleissperrungService();
		Gleissperrung gleissperrung = gsService.findById(mrForm.getRegelungId(),
		    new Preload[] { new Preload(Regelung.class, "massnahme") });

		if (gleissperrung == null) {
			if (log.isDebugEnabled())
				log.debug("Gleissperrung not found: " + mrForm.getRegelungId());
			addError("error.notfound", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing Gleissperrung: " + mrForm.getRegelungId());

		// Rechtepruefung
		if (voterRegelung.vote(secUser, gleissperrung, "ROLE_GLEISSPERRUNG_LOESCHEN") == AccessDecisionVoter.ACCESS_DENIED)
			return mapping.findForward("ACCESS_DENIED");
		try {
			gsService.delete(gleissperrung);
		} catch (Exception e) {
			addError("error.delete", msgRes.getMessage("gleissperrung"));
			return mapping.findForward("SUCCESS");
		}
		addMessage("success.deleted");
		return mapping.findForward("SUCCESS");
	}

	/* LANGSAMFAHRSTELLE */
	public ActionForward langsamfahrstelle(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungDeleteAction->langsamfahrstelle.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), secUser, request);
		if (massnahme == null)
			return mapping.findForward("FAILURE");

		LangsamfahrstelleService lfsService = serviceFactory.createLangsamfahrstelleService();
		Langsamfahrstelle lfs = lfsService.findById(mrForm.getRegelungId(),
		    new Preload[] { new Preload(Regelung.class, "massnahme") });

		if (lfs == null) {
			if (log.isDebugEnabled())
				log.debug("Langsamfahrstelle not found: " + mrForm.getRegelungId());
			addError("error.notfound", msgRes.getMessage("langsamfahrstelle"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing Langsamfahrstelle: " + mrForm.getRegelungId());

		// Rechtepruefung
		if (voterRegelung.vote(secUser, lfs, "ROLE_LANGSAMFAHRSTELLE_LOESCHEN") == AccessDecisionVoter.ACCESS_DENIED)
			return mapping.findForward("ACCESS_DENIED");
		try {
			lfsService.delete(lfs);
		} catch (Exception e) {
			addError("error.delete", msgRes.getMessage("langsamfahrstelle"));
			return mapping.findForward("SUCCESS");
		}
		addMessage("success.deleted");
		return mapping.findForward("SUCCESS");
	}

	/* OBERLEITUNG */
	public ActionForward oberleitung(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources msgRes = getResources(request);
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeRegelungDeleteAction->oberleitung.");

		TqmUser secUser = getSecUser();

		MassnahmeRegelungForm mrForm = (MassnahmeRegelungForm) form;

		SAPMassnahme massnahme = initializeMassnahme(mrForm.getMassnahmeId(), secUser, request);
		if (massnahme == null)
			return mapping.findForward("FAILURE");

		OberleitungService oberleitungService = serviceFactory.createOberleitungService();
		Oberleitung oberleitung = oberleitungService.findById(mrForm.getRegelungId(),
		    new Preload[] { new Preload(Regelung.class, "massnahme") });

		if (oberleitung == null) {
			if (log.isDebugEnabled())
				log.debug("Oberleitung not found: " + mrForm.getRegelungId());
			addError("error.notfound", msgRes.getMessage("oberleitung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing Oberleitung: " + mrForm.getRegelungId());

		// Rechtepruefung
		if (voterRegelung.vote(secUser, oberleitung, "ROLE_OBERLEITUNG_LOESCHEN") == AccessDecisionVoter.ACCESS_DENIED)
			return mapping.findForward("ACCESS_DENIED");
		try {
			oberleitungService.delete(oberleitung);
		} catch (Exception e) {
			addError("error.delete", msgRes.getMessage("oberleitung"));
			return mapping.findForward("SUCCESS");
		}
		addMessage("success.deleted");
		return mapping.findForward("SUCCESS");
	}

	private SAPMassnahme initializeMassnahme(Integer massnahmeId, TqmUser secUser,
	    HttpServletRequest request) {
		if (massnahmeId != null) {
			if (log.isDebugEnabled())
				log.debug("Find SAPMassnahme by Id: " + massnahmeId);
			SAPMassnahme massnahme = serviceFactory.createSAPMassnahmeService().findById(
			    massnahmeId);
			if (massnahme != null) {
				if (serviceFactory.createSAPMassnahmeAnyVoter().vote(secUser, massnahme,
				    "ROLE_MASSNAHME_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
					addError("error.access.denied");
					return null;
				}
				if (log.isDebugEnabled())
					log.debug("Processing SAPMassnahme: " + massnahme);
				return massnahme;
			}
		}
		MessageResources msgRes = getResources(request);
		addError("error.notfound", msgRes.getMessage("sperrpausenbedarf"));
		return null;
	}

}