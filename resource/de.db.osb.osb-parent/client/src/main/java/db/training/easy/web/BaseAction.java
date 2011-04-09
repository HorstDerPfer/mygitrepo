package db.training.easy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserServiceImpl;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.security.Role;
import db.training.security.hibernate.TqmRole;
import db.training.security.hibernate.TqmUser;

public abstract class BaseAction extends Action {

	private static final Logger log = Logger.getLogger(BaseAction.class);

	private static final String SESSION_KEY_USER = "db.training.easy.ApplicationUser";

	protected ThreadLocal<ActionMessages> errors = new ThreadLocal<ActionMessages>();

	protected ThreadLocal<ActionMessages> messages = new ThreadLocal<ActionMessages>();

	protected EasyServiceFactory serviceFactory;

	protected Integer sessionFahrplanjahr = null;

	protected MessageResources msgRes;

	protected abstract ActionForward run(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception;

	protected BaseAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		msgRes = MessageResources.getMessageResources("MessageResources");
	}

	@Override
	/*
	 * overwrite Struts default setServlet method
	 */
	public void setServlet(ActionServlet actionServlet) {
		// call Struts setServlet
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

		ActionForward actionForward = run(mapping, form, request, response);
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
		controlRingBufferActions(request);

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

	protected void addMessage(String key, String param1) {
		this.messages.get().add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, param1));
	}

	@Override
	public MessageResources getResources(HttpServletRequest arg0) {
		return super.getResources(arg0);
	}

	/**
	 * Test exist error in request
	 * 
	 * @return exist errors in request
	 */
	public boolean hasErrors(HttpServletRequest request) {
		return !getErrors(request).isEmpty();
	}

	/**
	 * Test exist error in BaseAction
	 * 
	 * @return exist errors in BaseAction
	 */
	public boolean hasErrors() {
		return !this.errors.get().isEmpty();
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

	public ActionForward testRun(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		return run(mapping, form, request, response);
	}

	public static void controlRingBufferActions(HttpServletRequest request) {
		String uri = request.getRequestURI().toLowerCase();
		
		//die "zvfxmlselectfile" Action muss mitprotokolliert werden, damit der Back-Button korrekt arbeitet
		if (uri.contains("zvfxmlselectfile")){
			;
		} else if (uri.contains("save") || uri.contains("delete") || uri.contains("toggle")
		    || uri.contains("login") || uri.contains("pass") || uri.contains("ajax")
		    || uri.contains("refresh") || uri.contains("add") || uri.contains("xml")
		    || uri.contains("zvf") || uri.contains("Export") || uri.contains("import")
		    || uri.contains("execute") || uri.contains("download")) {
			request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, true);
		}
	}
	
	public static boolean hasUserRestrictedAccess(TqmUser secUser) {
		boolean result = false;

		if (secUser != null && secUser.getRoles() != null) {
			for (Role role : secUser.getRoles()) {

				if (role instanceof TqmRole) {
					TqmRole r = (TqmRole) role;

					// ist Rolle auf Region beschränkt?
					if (r.getName().indexOf("REGIONAL") > -1)
						result = true;
				} else {
					result = false;
				}
			}
		} else {
			// keine Rollen, keine Rechte
			result = false;
		}

		return result;
	}

}