package db.training.easy.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.dbsystems.kolt.talo.CoreCatalog;
import de.dbsystems.kolt.talo.TaloLog;
import de.dbsystems.kolt.talo.TaloLogFactory;

public class LogoutAction extends Action {

	private static final TaloLog talo = TaloLogFactory.getLog(LogoutAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession httpSession = request.getSession();
		if (httpSession != null)
			httpSession.invalidate();
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.logout"));
		saveMessages(request, messages);
		if (talo.isDebugEnabled())
			talo.debug("Logout successful.");

		talo.log(CoreCatalog.infoUserLogout(null));

		return mapping.findForward("SUCCESS");
	}
}
