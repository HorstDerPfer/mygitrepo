package db.training.bob.web.ajax;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshEmpfaengerAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshEmpfaengerAction.class);

	private static final String UEB = "ueb";

	private static final String BBZR = "bbzr";

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering RefreshEmpfaengerAction.");

		String empfaenger = null; // null bei add
		String empfaengerName = null; // null bei delete
		String empfaengerNameBBZR = null; // null bei delete
		Integer bmId = null;
		String type = null; // Übergabeblatt (ueb) oder BBZR (bbzr)

		if (request.getParameter("empfaenger") != null)
			empfaenger = request.getParameter("empfaenger");
		if (request.getParameter("empfaengerName") != null)
			empfaengerName = request.getParameter("empfaengerName");
		if (request.getParameter("empfaengerNameBBZR") != null)
			empfaengerNameBBZR = request.getParameter("empfaengerNameBBZR");
		if (request.getParameter("id") != null)
			bmId = FrontendHelper.castStringToInteger(request.getParameter("id"));
		if (request.getParameter("type") != null)
			type = request.getParameter("type");

		if (log.isDebugEnabled())
			log.debug(empfaenger);
		if (log.isDebugEnabled())
			log.debug("'" + empfaengerName + "'");
		if (log.isDebugEnabled())
			log.debug("'" + empfaengerNameBBZR + "'");

		BaumassnahmeService bmService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		FetchPlan[] fetchPlans = null;
		Uebergabeblatt zvf = null;
		Baumassnahme bm = null;

		if (type.equals(UEB)) {
			fetchPlans = new FetchPlan[] { FetchPlan.BOB_UEBERGABEBLATT, FetchPlan.UEB_HEADER };
			bm = bmService.findById(bmId, fetchPlans);
			zvf = bm.getUebergabeblatt();
		} else if (type.equals(BBZR)) {
			fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBZR, FetchPlan.BBZR_HEADER,
			        FetchPlan.UEB_HEADER_EMPFAENGER, FetchPlan.BBZR_BAUMASSNAHMEN,
			        FetchPlan.ZVF_MN_VERSION };
			bm = bmService.findById(bmId, fetchPlans);
			zvf = bm.getAktuelleZvf();
		}

		if (empfaenger != null) {// delete
			zvf.getHeader().getEmpfaenger().remove(empfaenger);
		} else { // add
			if (empfaengerName.equals("")) {
				Set<String> empfaengerList = zvf.getHeader().getEmpfaenger();
				empfaengerList.add(empfaengerNameBBZR);
			} else {
				Header h = zvf.getHeader();
				Set<String> empfaengerList = h.getEmpfaenger();
				empfaengerList.add(empfaengerName);
			}
		}

		bmService.update(bm);
		request.setAttribute("baumassnahme", bm);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		return mapping.findForward("SUCCESS");
	}
}