package db.training.osb.web.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.GleissperrungService;

public class RefreshStreckenbandPopupDetailsAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshStreckenbandPopupDetailsAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshStreckenbandPopupDetailsAction.");

		// Refresh-Action nicht für "zurück"-Button protokollieren
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);

		Integer id = FrontendHelper.castStringToInteger(request.getParameter("id"));

		if (id == null) {
			if (log.isDebugEnabled())
				log.debug("Anfrage enhält keine Daten");
			return mapping.findForward("FALURE");
		}

		GleissperrungService gleissperrungService = serviceFactory.createGleissperrungService();

		Gleissperrung g = gleissperrungService.findById(id, new Preload[] {
		        new Preload(Gleissperrung.class, "buendel"),
		        new Preload(Gleissperrung.class, "bstVon"),
		        new Preload(Gleissperrung.class, "bstBis"),
		        new Preload(Gleissperrung.class, "massnahme"),
		        new Preload(SAPMassnahme.class, "betriebsstelleVon"),
		        new Preload(SAPMassnahme.class, "betriebsstelleBis"),
		        new Preload(SAPMassnahme.class, "gleissperrungen"),
		        new Preload(Gleissperrung.class, "betriebsweise") });

		if (g == null) {
			if (log.isDebugEnabled())
				log.debug("Parameter 'id' fuehrt zu keinem Ergebnis.");
			return mapping.findForward("FALURE");
		}

		if (g.getMassnahme() != null) {
			request.getSession().setAttribute("popupBaumassnahme", g.getMassnahme());
			request.getSession().setAttribute("popupGleissperrung", g);
		} else {
			if (log.isDebugEnabled())
				log.debug("Keine Maßnahmen gefunden für Gleissperrung ID = " + g.getId());
			request.getSession().removeAttribute("popupBaumassnahme");
		}

		return mapping.findForward("SUCCESS");
	}
}