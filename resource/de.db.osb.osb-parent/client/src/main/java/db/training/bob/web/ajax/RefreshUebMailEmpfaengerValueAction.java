package db.training.bob.web.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserService;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.SMTP;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.util.ConfigResources;

public class RefreshUebMailEmpfaengerValueAction extends BaseAction {

	private static Logger log = Logger.getLogger(RefreshUebMailEmpfaengerValueAction.class);

	private BaumassnahmeService bmService;

	private UserService userService;

	public RefreshUebMailEmpfaengerValueAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		bmService = serviceFactory.createBaumassnahmeService();
		userService = serviceFactory.createUserService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshUebMailEmpfaengerValueAction.");

		User loginUser = getLoginUser(request);

		// String[] empfaenger = null;
		String empfaenger = null;
		Integer bmId = null;

		if (request.getParameter("empfaenger") != null)
			empfaenger = request.getParameter("empfaenger");

		if (request.getParameter("bmId") != null)
			bmId = FrontendHelper.castStringToInteger(request.getParameter("bmId"));

		String errorKey = null;
		if (empfaenger == null) {
			errorKey = "error.ueb.mail.empfaenger";
			request.setAttribute("errorKey", errorKey);
			request.setAttribute("id", bmId);
			return mapping.findForward("FAILURE");
		}

		FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_UEBERGABEBLATT,
		        FetchPlan.UEB_HEADER, FetchPlan.UEB_HEADER_SENDER };
		Baumassnahme b = bmService.findById(bmId, fetchPlans);

		// Empfängerliste erstellen
		User userRb = null;
		List<User> userList = new ArrayList<User>();
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(empfaenger, ",");
			while (stringTokenizer.hasMoreTokens()) {
				userRb = userService.findUserById(Integer.valueOf(stringTokenizer.nextToken()));
				userList.add(userRb);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorKey = "error.mail.empfaenger";
			request.setAttribute("errorKey", errorKey);
			request.setAttribute("id", bmId);
			return mapping.findForward("FAILURE");
		}
		if (userList.size() == 0) {
			errorKey = "error.mail.empfaenger";
			request.setAttribute("errorKey", errorKey);
			request.setAttribute("id", bmId);
			return mapping.findForward("FAILURE");
		}

		StringBuffer nextRBemail = new StringBuffer();
		for (User u : userList) {
			nextRBemail.append(u.getEmail());
			nextRBemail.append(",");
		}
		String masterRbemail = b.getUebergabeblatt().getHeader().getSender().getEmail();

		// Mail versenden
		SMTP smtp = new SMTP();
		MessageResources msgRes = getResources(request);
		String uebegabeblattBetreff = msgRes.getMessage("mail.ueb.betreff", "" + b.getVorgangsNr());
		StringBuilder sb = new StringBuilder();
		sb.append(msgRes.getMessage("mail.ueb.text.anfang", b.getVorgangsNr().toString(), loginUser
		    .getRegionalbereich().getName()));
		sb.append(ConfigResources.getInstance().getApplicationUri());
		sb.append(msgRes.getMessage("ueb.view.address"));
		sb.append("?id=");
		sb.append(b.getId());
		sb.append(msgRes.getMessage("mail.ueb.text.fortsetzung"));
		String message = sb.toString();
		if (log.isDebugEnabled())
			log.debug(uebegabeblattBetreff);
		if (log.isDebugEnabled())
			log.debug(message);

		try {
			smtp.sendEmail(ConfigResources.getInstance().getSmtpServer(), nextRBemail.toString(), loginUser
			    .getEmail(), masterRbemail, uebegabeblattBetreff, message);
		} catch (Exception e) {
			e.printStackTrace();
			errorKey = "error.mail.send";
			request.setAttribute("errorKey", errorKey);
			return mapping.findForward("FAILURE");
		}

		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		String messageKey = "success.mail.send";
		request.setAttribute("messageKey", messageKey);
		return mapping.findForward("SUCCESS");
	}
}