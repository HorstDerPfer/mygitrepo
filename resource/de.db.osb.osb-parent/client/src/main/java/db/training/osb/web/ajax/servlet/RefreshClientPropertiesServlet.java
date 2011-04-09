package db.training.osb.web.ajax.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;

/**
 * @author AndreasLotz
 */
@SuppressWarnings("serial")
public class RefreshClientPropertiesServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(RefreshClientPropertiesServlet.class);

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		log.debug("Entering RefreshClientPropertiesServlet");

		Integer clientWidth = FrontendHelper.castStringToInteger(request
		    .getParameter("clientWidth"));
		Integer clientHeight = FrontendHelper.castStringToInteger(request
		    .getParameter("clientHeight"));

		// Sicherheitspruefung um Mindestgroessen nicht zu unterschreiten
		if (clientWidth < 780) {
			clientWidth = 780;
		}
		if (clientHeight < 540) {
			clientHeight = 540;
		}

		request.getSession().setAttribute("clientWidth", clientWidth);
		request.getSession().setAttribute("clientHeight", clientHeight);
	}
}
