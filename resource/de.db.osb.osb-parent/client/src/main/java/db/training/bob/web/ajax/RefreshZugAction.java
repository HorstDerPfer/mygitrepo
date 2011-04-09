package db.training.bob.web.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.ZugService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshZugAction extends BaseAction {

	private static Logger log = Logger.getLogger(RefreshZugAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering RefreshZugAction.");

		Integer zugId = null;
		Integer bmId = null;

		if (request.getParameter("zugId") != null)
			zugId = Integer.parseInt(request.getParameter("zugId"));
		if (request.getParameter("id") != null)
			bmId = Integer.parseInt(request.getParameter("id"));

		BaumassnahmeService bmService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_UEBERGABEBLATT,
		        FetchPlan.UEB_BAUMASSNAHMEN, FetchPlan.UEB_REGIONALBEREICHE, FetchPlan.UEB_MN_ZUEGE };
		Baumassnahme bm = bmService.findById(bmId, fetchPlans);

		ZugService zugService = EasyServiceFactory.getInstance().createZugService();

		Uebergabeblatt ueb = bm.getUebergabeblatt();
		if (zugId != null) {// delete
			Zug z = zugService.findById(zugId);
			List<Zug> zuege = ueb.getMassnahmen().iterator().next().getZug();
			zuege.remove(z);
			ueb.getMassnahmen().iterator().next().setZug(zuege);
		} else { // add
			List<Zug> zuege = ueb.getMassnahmen().iterator().next().getZug();
			zuege.add(new Zug());

			ueb.refreshZugStatusRbEntry();
		}

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		response.setHeader("Content-Type", "text/html; charset=utf-8");
		bmService.update(bm);
		request.setAttribute("baumassnahme", bm);

		return mapping.findForward("SUCCESS");
	}
}