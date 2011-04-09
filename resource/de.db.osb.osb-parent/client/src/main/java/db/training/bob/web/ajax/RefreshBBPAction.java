package db.training.bob.web.ajax;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.service.BBPMassnahmeService;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshBBPAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshBBPAction.class);

	private BaumassnahmeService baumassnahmeService;

	private BBPMassnahmeService bbpService;

	public RefreshBBPAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		bbpService = serviceFactory.createBBPMassnahmeService();
	}

	@Override
	public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshBBPAction.");

		User loginUser = getLoginUser(request);

		Integer bmId = null;
		String masId = null;
		Integer bbpId = null;
		Baumassnahme bm = null;

		if (request.getParameter("id") != null)
			bmId = FrontendHelper.castStringToInteger(request.getParameter("id"));
		if (request.getParameter("masId") != null)
			masId = request.getParameter("masId");
		if (request.getParameter("bbpId") != null)
			bbpId = FrontendHelper.castStringToInteger(request.getParameter("bbpId"));

		if (bmId != null) {
			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME,
			        FetchPlan.BBP_REGELUNGEN };
			bm = baumassnahmeService.findById(bmId, fetchPlans);
		}

		String errorKey = null;
		boolean update = false;

		if (bm != null) {

			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BBP_REGELUNGEN };
			BBPMassnahme bbp = null;

			if (masId != null && !masId.equals("")) {
				List<BBPMassnahme> list = bbpService.findByMasId(masId, fetchPlans);
				if (list.size() == 1) { // TODO trefferliste
					bbp = list.get(0); // TODO nicht nur der erste Treffer
				}
				if (bbp != null) {
					if (!bm.getBbpMassnahmen().contains(bbp)) {
						Set<BBPMassnahme> b = bm.getBbpMassnahmen();
						b.add(bbp);
						bm.setBbpMassnahmen(b);
						update = true;
					} else
						errorKey = "error.bbpmassnahme.notunique";
				} else
					errorKey = "error.bbpmassnahme.notfound";
			}

			if (bbpId == null && (masId == null || masId.equals("")))
				errorKey = "error.required.common";

			if (bbpId != null) { // löschen
				bbp = bbpService.findById(bbpId, fetchPlans);
				if (bbp != null) {
					Set<BBPMassnahme> set = bm.getBbpMassnahmen();
					set.remove(bbp);
					bm.setBbpMassnahmen(set);
					update = true;
				} else
					errorKey = "error.kursbuchstrecke.notfound";
			}

			if (update) {
				bm.setLastChange(null);

				baumassnahmeService.update(bm);

				if (bbpId != null){
					bbpService.delete(bbp);
				}
				
			}
		}

		request.setAttribute("errorKey", errorKey);
		request.setAttribute("baumassnahme", bm);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		return mapping.findForward("SUCCESS");
	}
}
