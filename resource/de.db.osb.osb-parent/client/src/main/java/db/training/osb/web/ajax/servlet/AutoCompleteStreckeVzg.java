package db.training.osb.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.VzgStreckeService;

/**
 * @author michels
 */
@SuppressWarnings("serial")
public class AutoCompleteStreckeVzg extends HttpServlet {

	private static Logger log = Logger.getLogger(AutoCompleteStreckeVzg.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		Integer nummer = FrontendHelper.castStringToInteger(request.getParameter("nummer"));
		Date beginn = FrontendHelper.castStringToDate(request.getParameter("beginn"));
		Date ende = FrontendHelper.castStringToDate(request.getParameter("ende"));
		Integer fahrplanjahr = (Integer) request.getSession().getAttribute("session_fahrplanjahr");
		if (request.getParameter("fahrplanjahr") != null)
			fahrplanjahr = FrontendHelper.castStringToInteger(request.getParameter("fahrplanjahr"));

		// Wird nur teilweise uebergeben, dient dann zur Einschraenkung der Strecken ueber Region
		// der zugewiesenen Betriebsstellen
		Integer regionalbereichId = FrontendHelper.castStringToInteger(request
		    .getParameter("regionalbereichId"));
		Regionalbereich regionalbereich = null;

		if (nummer == null) {
			if (log.isDebugEnabled())
				log.debug("Anfrage enhält keine Daten");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// Regionalbereich setzen falls vorhanden
		if (regionalbereichId != null) {
			RegionalbereichService rbService = EasyServiceFactory.getInstance()
			    .createRegionalbereichService();
			regionalbereich = rbService.findById(regionalbereichId);
		}

		VzgStreckeService service = EasyServiceFactory.getInstance().createVzgStreckeService();

		List<VzgStrecke> list = new ArrayList<VzgStrecke>();

		// Suche ueber Fahrplanjahr
		if (beginn == null && ende == null && fahrplanjahr != null) {
			list = service.findByNummer(nummer, fahrplanjahr, regionalbereich, false,
			    new Preload[] { new Preload(VzgStrecke.class, "betriebsstellen") });
		}
		// Suche mit Start- und Enddatum
		else {
			if (beginn == null)
				beginn = Calendar.getInstance().getTime();
			if (ende == null)
				ende = EasyPersistentExpirationObject.getMaxGueltigBis();

			list = service.findByNummer(nummer, beginn, ende, regionalbereich, false,
			    new Preload[] { new Preload(VzgStrecke.class, "betriebsstellen") });
		}

		if (list != null && list.size() > 0) {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter outtext = response.getWriter();
			outtext.println("<ul>");
			for (VzgStrecke item : list) {
				outtext.println(String.format("<li id=%s>%s</li>", item.getId(), item.getCaption()));
			}
			outtext.println("</ul>");
			outtext.close();
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
