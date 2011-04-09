package db.training.easy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserServiceImpl;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

public abstract class BaseDispatchAction extends DispatchAction {

	private static Logger log = Logger.getLogger(BaseDispatchAction.class);

	private static final String SESSION_KEY_USER = "db.training.easy.ApplicationUser";

	protected ThreadLocal<ActionMessages> errors = new ThreadLocal<ActionMessages>();

	protected ThreadLocal<ActionMessages> messages = new ThreadLocal<ActionMessages>();

	protected EasyServiceFactory serviceFactory;

	protected MessageResources msgRes;

	protected BaseDispatchAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		msgRes = MessageResources.getMessageResources("MessageResources");
	}

	protected Integer sessionFahrplanjahr = null;

	// protected abstract ActionForward run(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) throws Exception;

	@Override
	public void setServlet(ActionServlet actionServlet) {
		super.setServlet(actionServlet);
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaseAction.");
		this.errors.set(new ActionMessages());
		this.messages.set(new ActionMessages());
		User loginUser = getLoginUser(request);

		sessionFahrplanjahr = (Integer) request.getSession().getAttribute("session_fahrplanjahr");

		UserServiceImpl.setCurrentApplicationUser(loginUser);

		// Easy_User fuer Rechteabfragen in Request schreiben
		request.setAttribute("loginUser", loginUser);

		// ActionForward actionForward = run(mapping, form, request, response);

		ActionForward actionForward = null;
		String parameter = getParameter(mapping, form, request, response);
		if (parameter != null) {
			String method = getMethodName(mapping, form, request, response, parameter);
			if (method == null) {
				method = "unspecified"; // Standard-Methode, wenn keine Methode angegeben wird
			}
			try {
				actionForward = dispatchMethod(mapping, form, request, response, method);
			} catch (NoSuchMethodException ex) {
				log.error("dispatch method not found: " + ex.getMessage());
			}
		} else {
			log.debug("no dispatch parameter found.");
		}
		log.debug("Returning from action. Forwarding to: "
		    + (actionForward == null ? null : actionForward.getName()));
		if (!this.errors.get().isEmpty())
			saveErrors(request, this.errors.get());
		if (!this.messages.get().isEmpty())
			saveMessages(request, this.messages.get());
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");

		String currentAction = request.getRequestURL().toString();
		while (currentAction.contains("/"))
			currentAction = currentAction.substring(currentAction.indexOf("/") + 1);
		request.getSession().setAttribute("currentAction", currentAction);

		request.setAttribute("currentActionPath", mapping.getPath());
		request.setAttribute("regionalrechtEnabled", System.getProperty("regionalPermissions"));

		// Parameter für ausgewähltes Tab weitergeben
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("tab"))) {
			request.setAttribute("tab", request.getParameter("tab"));
		}

		// Prueft Actions fuer RingBuffer, saveActions werden ausgeschlossen
		BaseAction.controlRingBufferActions(request);

		return actionForward;
	}

	protected void addError(String key) {
		this.errors.get().add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
	}

	protected void addError(String key, String param1) {
		this.errors.get().add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, param1));
	}

	protected void addMessage(String key) {
		this.messages.get().add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
	}

	@Override
	public MessageResources getResources(HttpServletRequest arg0) {
		return super.getResources(arg0);
	}

	public boolean hasErrors(HttpServletRequest request) {
		return !getErrors(request).isEmpty();
	}

	/**
	 * gibt den eingelogten Anwendungsuser zurueck
	 * 
	 * @return
	 */
	protected User getLoginUser(HttpServletRequest request) {
		TqmUser user = getSecUser();

		User applicationUser = null;
		if (user != null) {
			applicationUser = (User) request.getSession().getAttribute(SESSION_KEY_USER);
			if (applicationUser == null) {
				synchronized (user) {
					applicationUser = serviceFactory.createUserService().findUserByLoginName(
					    user.getUsername());
					request.getSession().setAttribute(SESSION_KEY_USER, applicationUser);
				}
			}

		}
		return applicationUser;
	}

	protected TqmUser getSecUser() {
		return (TqmUser) serviceFactory.createSecurityService().getCurrentUser();
	}
}