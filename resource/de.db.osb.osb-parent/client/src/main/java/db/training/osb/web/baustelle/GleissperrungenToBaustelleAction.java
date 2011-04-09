package db.training.osb.web.baustelle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Baustelle;
import db.training.osb.model.Gleissperrung;
import db.training.osb.service.BaustelleService;
import db.training.osb.service.GleissperrungService;

public class GleissperrungenToBaustelleAction extends BaseAction {

	private static final Logger log = Logger.getLogger(GleissperrungenToBaustelleAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering GleissperrungenToBaustelleAction.");

		BaustelleService bService = serviceFactory.createBaustelleService();
		Integer bId = FrontendHelper.castStringToInteger(request.getParameter("baustelleId"));

		if (bId != null) {
			Baustelle baustelle = bService.findById(bId, new Preload[] { new Preload(
			    Baustelle.class, "gleissperrungen") });

			if (baustelle == null) {
				if (log.isDebugEnabled())
					log.debug("No baustelleId found");
				addError("error.notfound", msgRes.getMessage("baustelle"));
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing baustelle: " + baustelle.getId());

			GleissperrungService gsService = serviceFactory.createGleissperrungService();
			List<Gleissperrung> gleissperrungen = gsService.findAll();
			// new Preload[] { new Preload(Gleissperrung.class, "massnahme") }

			// bereits hinzugefuegte Gleissperrungen werden entfernt
			gleissperrungen.removeAll(baustelle.getGleissperrungen());

			request.setAttribute("baustelle", baustelle);
			request.setAttribute("gleissperrungen", gleissperrungen);

			if (log.isDebugEnabled())
				log.debug("SUCCESS");

			return mapping.findForward("SUCCESS");
		}

		if (log.isDebugEnabled())
			log.debug("No baustelleId found");
		addError("error.notfound", msgRes.getMessage("baustelle"));
		return mapping.findForward("FAILURE");
	}
}