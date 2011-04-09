package db.training.bob.web.ajax;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Aenderung;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Grund;
import db.training.bob.service.AenderungService;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.GrundService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshAenderungAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshAenderungAction.class);

	private BaumassnahmeService baumassnahmeService;

	private AenderungService aenderungService;

	private GrundService grundService;

	public RefreshAenderungAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		aenderungService = serviceFactory.createAenderungService();
		grundService = serviceFactory.createGrundService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshAenderungAction.");

		User loginUser = getLoginUser(request);

		Integer bmId = null;
		Integer aenderungId = null; // der selbe name wie in ajax.js
		Integer grund = null; // der selbe name wie in ajax.js
		String aufwand = null; // der selbe name wie in ajax.js
		Baumassnahme bm = null;

		if (request.getParameter("id") != null)
			bmId = FrontendHelper.castStringToInteger(request.getParameter("id"));
		if (request.getParameter("aenderungId") != null)
			aenderungId = FrontendHelper.castStringToInteger(request.getParameter("aenderungId"));
		if (request.getParameter("grund") != null)
			grund = FrontendHelper.castStringToInteger(request.getParameter("grund"));
		if (request.getParameter("aufwand") != null)
			aufwand = request.getParameter("aufwand");

		if (bmId != null) {
			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_AENDERUNGSDOKUMENTATION };
			bm = baumassnahmeService.findById(bmId, fetchPlans);
		}

		String errorKey = null;
		boolean update = false;

		if (bm != null) {
			Aenderung aenderung = null;

			if (grund == null || grund == 0) {
				if (aenderungId == null)
					errorKey = "error.aenderung.grund.notfound";
			} else {
				// erstellen
				Grund grundObj = grundService.findById(grund);
				aenderung = new Aenderung();
				aenderung.setGrund(grundObj);
				int size = bm.getAenderungen().size();
				aenderung.setAenderungsNr(size + 1);
				if (aufwand == null) {
					aenderung.setAufwand(0);
				} else {
					aenderung.setAufwandTimeString(aufwand);
				}
				bm.getAenderungen().add(aenderung);
				aenderungService.create(aenderung);
				update = true;
			}

			if (aenderungId != null) {// löschen
				aenderung = aenderungService.findById(aenderungId);
				if (aenderung != null) {
					Set<Aenderung> aenderungen = bm.getAenderungen();
					aenderungen.remove(aenderung);
					Iterator<Aenderung> it = aenderungen.iterator();
					while (it.hasNext()) {
						Aenderung a = it.next();
						if (aenderung.getAenderungsNr() < a.getAenderungsNr()) {
							a.setAenderungsNr(a.getAenderungsNr() - 1);
							aenderungService.update(a);
						}
					}
					bm.setAenderungen(aenderungen);
					update = true;
				}
			}

			if (update) {
				bm.setLastChange(null);

				baumassnahmeService.update(bm);

				if (aenderungId != null)
					aenderungService.delete(aenderung);
			}
		}

		request.setAttribute("errorKey", errorKey);
		request.setAttribute("baumassnahme", bm);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		return mapping.findForward("SUCCESS");
	}
}
