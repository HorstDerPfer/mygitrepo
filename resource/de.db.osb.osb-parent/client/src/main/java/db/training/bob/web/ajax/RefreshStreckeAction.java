package db.training.bob.web.ajax;

import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.StreckeService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshStreckeAction extends BaseAction {

	private static final String UEB = "UEB";

	private static final Logger log = Logger.getLogger(RefreshStreckeAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering RefreshStreckeAction.");

		Integer streckeId = null;
		Integer bmId = null;
		String type = null; // Übergabeblatt (ueb) oder BBZR (bbzr)

		if (request.getParameter("streckeId") != null)
			streckeId = Integer.parseInt(request.getParameter("streckeId"));
		if (request.getParameter("id") != null)
			bmId = Integer.parseInt(request.getParameter("id"));
		if (request.getParameter("type") != null)
			type = request.getParameter("type");

		if (log.isDebugEnabled())
			log.debug(streckeId);
		if (log.isDebugEnabled())
			log.debug(bmId);
		if (log.isDebugEnabled())
			log.debug(type);

		BaumassnahmeService bmService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		StreckeService streckeService = EasyServiceFactory.getInstance().createStreckeService();

		FetchPlan[] fetchPlans = null;
		Baumassnahme bm = null;
		Uebergabeblatt zvf = null;

		if (type.equals(UEB)) {
			fetchPlans = new FetchPlan[] { FetchPlan.BOB_UEBERGABEBLATT,
			        FetchPlan.UEB_BAUMASSNAHMEN, FetchPlan.ZVF_MN_STRECKEN };
			bm = bmService.findById(bmId, fetchPlans);
			zvf = bm.getUebergabeblatt();
		} else {
			fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBZR, FetchPlan.BBZR_BAUMASSNAHMEN,
			        FetchPlan.ZVF_MN_STRECKEN, FetchPlan.ZVF_MN_VERSION };
			bm = bmService.findById(bmId, fetchPlans);
			zvf = bm.getAktuelleZvf();
		}

		if (streckeId != null) {// delete
			Strecke s = streckeService.findById(streckeId);
			try {
				List<Strecke> strecken = zvf.getMassnahmen().iterator().next().getStrecke();
				strecken.remove(s);
			} catch (NoSuchElementException e) {
			}
		} else { // add
			try {
				List<Strecke> strecken = zvf.getMassnahmen().iterator().next().getStrecke();
				strecken.add(new Strecke());
			} catch (NoSuchElementException e) {
			}
		}
		bmService.update(bm);

		response.setHeader("Content-Type", "text/html; charset=utf-8");
		request.setAttribute("baumassnahme", bm);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		if (type.equals(UEB)) {
			return mapping.findForward("SUCCESS_UEB");
		}
		return mapping.findForward("SUCCESS_ZVF");
	}
}