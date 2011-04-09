package db.training.easy.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import db.training.logwrapper.Logger;

public class EasyExceptionHandler extends ExceptionHandler {

	private static Logger log = Logger.getLogger(EasyExceptionHandler.class);

	@Override
	public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping,
	    ActionForm formInstance, HttpServletRequest request, HttpServletResponse response)
	    throws ServletException {
		request.setAttribute("exceptionMessage", ex.getMessage());
		request.setAttribute("exception", ex);
		if (log.isDebugEnabled())
			log.debug("Exception thrown: " + ex.getMessage());
		return super.execute(ex, ae, mapping, formInstance, request, response);
	}
}