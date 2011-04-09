package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsweise;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.security.hibernate.TqmUser;

public class FahrplanregelungEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(FahrplanregelungEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,

	HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering FahrplanregelungEditAction.");

		TqmUser secUser = getSecUser();
		User loginUser = getLoginUser(request);

		FahrplanregelungForm frForm = (FahrplanregelungForm) form;
		Integer frId = frForm.getFahrplanregelungId();
		Fahrplanregelung fr = null;

		// Aus CopyAction uebergebene ID
		if (request.getAttribute("fahrplanregelungId") != null) {
			frId = (Integer) request.getAttribute("fahrplanregelungId");
		}

		// fahrplanregelungId pruefen *************************************************
		if (frId == null) {
			if (log.isDebugEnabled())
				log.debug("No fahrplanregelungId found");
			addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing fahrplanregelungId: " + frId);

		// Aus CopyAction uebergebene Info, dass es sich um einen Kopiervorgang handelt.
		// Dieses Flag gibt die Felder Name und Regionalbereich frei
		boolean copy = false;
		if (request.getAttribute("copy") != null) {
			copy = (Boolean) request.getAttribute("copy");
		}

		// Fahrplanregelung bearbeiten
		if (frId != 0) {
			fr = serviceFactory.createFahrplanregelungService().findById(
			    frId,
			    new Preload[] { new Preload(Fahrplanregelung.class, "buendel"),
			            new Preload(Fahrplanregelung.class, "betriebsstelleVon"),
			            new Preload(Fahrplanregelung.class, "betriebsstelleBis"),
			            new Preload(Fahrplanregelung.class, "gleissperrungen"),
			            new Preload(Fahrplanregelung.class, "umleitungFahrplanregelungLinks"),
			            new Preload(UmleitungFahrplanregelungLink.class, "umleitung"),
			            new Preload(UmleitungFahrplanregelungLink.class, "fahrplanregelung"),
			            new Preload(Umleitung.class, "umleitungswege"),
			            new Preload(Umleitung.class, "umleitungFahrplanregelungLinks"),
			            new Preload(Gleissperrung.class, "buendel"),
			            new Preload(Regelung.class, "massnahme"),
			            new Preload(SAPMassnahme.class, "genehmiger"),
			            new Preload(Regelung.class, "vtr"),
			            new Preload(Regelung.class, "vzgStrecke") });

			// fahrplanregelung pruefen *************************************************
			if (frId == null || frId == 0) {
				if (log.isDebugEnabled())
					log.debug("No fahrplanregelung found");
				addError("error.notfound", msgRes.getMessage("fahrplanregelung"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing fahrplanregelung: " + fr.getId());

			// Rechtepruefung ***************************************************************
			if (EasyServiceFactory.getInstance().createFahrplanregelungAnyVoter().vote(secUser, fr,
			    "ROLE_FAHRPLANREGELUNG_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				if (log.isDebugEnabled())
					log.debug("User '" + secUser.getUsername()
					    + "' hat keine Berechtigung fuer: ROLE_FAHRPLANREGELUNG_BEARBEITEN");
				if (EasyServiceFactory.getInstance().createFahrplanregelungAnyVoter().vote(secUser,
				    fr, "ROLE_FAHRPLANREGELUNG_LESEN") == AccessDecisionVoter.ACCESS_GRANTED) {
					if (log.isDebugEnabled())
						log.debug("User '" + secUser.getUsername()
						    + "' hat Berechtigung fuer: ROLE_FAHRPLANREGELUNG_LESEN");
					return mapping.findForward("VIEW");
				}
				addError("common.noAuth");
				return mapping.findForward("ACCESS_DENIED");
			}

			// Fahrplanjahr pruefen ********************************
			if (!sessionFahrplanjahr.equals(fr.getFahrplanjahr())) {
				addError("error.fahrplanjahr.wrong");
				return mapping.findForward("FAILURE");
			}

			if (!hasErrors(request)) {
				frForm.reset();
				frForm.setImportiert(fr.isImportiert());
				frForm.setCopy(copy);
				frForm.setName(fr.getName());
				if (fr.getRegionalbereich() != null) {
					frForm.setRegionalbereichId(fr.getRegionalbereich().getId());
				}
				frForm.setFahrplanregelungId(fr.getId());
				frForm.setFahrplanjahr(fr.getFahrplanjahr());

				frForm.setFixiert(fr.isFixiert());
				if (fr.getAufnahmeNfpVorschlag() != null) {
					frForm.setAufnahmeNfpVorschlag(fr.getAufnahmeNfpVorschlag().toString());
				}
				if (fr.getAufnahmeNfp() != null) {
					frForm.setAufnahmeNfp(fr.getAufnahmeNfp().toString());
				}
				frForm.setBehandlungKS(fr.isBehandlungKS());
				frForm.setRelevanzBzu(fr.isRelevanzBzu());

				Betriebsweise betriebsweise = fr.getBetriebsweise();
				frForm.setBetriebsweiseId(betriebsweise != null ? betriebsweise.getId() : null);
				frForm.setNachtsperrpause(fr.isNachtsperrpause());
				frForm.setVerkehrstag_mo(fr.isVerkehrstag_mo());
				frForm.setVerkehrstag_di(fr.isVerkehrstag_di());
				frForm.setVerkehrstag_mi(fr.isVerkehrstag_mi());
				frForm.setVerkehrstag_do(fr.isVerkehrstag_do());
				frForm.setVerkehrstag_fr(fr.isVerkehrstag_fr());
				frForm.setVerkehrstag_sa(fr.isVerkehrstag_sa());
				frForm.setVerkehrstag_so(fr.isVerkehrstag_so());

				frForm.setPlanStart(FrontendHelper.castDateToString(fr.getPlanStart()));
				frForm.setPlanEnde(FrontendHelper.castDateToString(fr.getPlanEnde()));
				frForm.setStart(FrontendHelper.castDateToString(fr.getStart()));
				frForm.setEnde(FrontendHelper.castDateToString(fr.getEnde()));
				frForm.setBetriebsstelleVon(fr.getBetriebsstelleVon() != null ? fr
				    .getBetriebsstelleVon().getCaption() : null);
				frForm.setBetriebsstelleBis(fr.getBetriebsstelleBis() != null ? fr
				    .getBetriebsstelleBis().getCaption() : null);
			}
		}
		// Fahrplanregelung neu anlegen
		else {
			if (!secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE")
			    && !secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_ANLEGEN_REGIONALBEREICH")) {
				if (log.isDebugEnabled())
					log.debug("ACCESS DENIED -> ROLE_FAHRPLANREGELUNG_ANLEGEN");
				addError("common.noAuth");
				return mapping.findForward("ACCESS_DENIED");
			}

			if (!hasErrors(request)) {
				frForm.reset();
				frForm.setCopy(true); // Wird hier zweckentfremdet um Felder Name und
				// Regionalbereich einzublenden
				frForm.setFahrplanregelungId(frId);
				if (!secUser.hasAuthorization("ROLE_FAHRPLANREGELUNG_ANLEGEN_ALLE"))
					frForm.setRegionalbereichId(loginUser.getRegionalbereich().getId());
				frForm.setFahrplanjahr(sessionFahrplanjahr);
			}

			fr = new Fahrplanregelung();
			fr.setRegionalbereich(loginUser.getRegionalbereich());
			fr.setFahrplanjahr(sessionFahrplanjahr);
		}

		request.setAttribute("fahrplanregelung", fr);
		request.setAttribute("regionalbereiche", EasyServiceFactory.getInstance()
		    .createRegionalbereichService().findAll());
		request.setAttribute("betriebsweisen", EasyServiceFactory.getInstance()
		    .createBetriebsweiseService().findAll());
		request.setAttribute("fahrplanregelungId", request.getParameter("fahrplanregelungId"));

		return mapping.findForward("SUCCESS");
	}
}