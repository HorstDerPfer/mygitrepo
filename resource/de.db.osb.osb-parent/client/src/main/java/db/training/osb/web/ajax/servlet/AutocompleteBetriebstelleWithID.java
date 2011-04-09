package db.training.osb.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.service.BetriebsstelleService;

@SuppressWarnings("serial")
public class AutocompleteBetriebstelleWithID extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		Integer vzgStrecke = null;
		if (request.getParameter("vzgstrecke") != null
		    && !request.getParameter("vzgstrecke").equals("")) {
			vzgStrecke = FrontendHelper.castStringToInteger(request.getParameter("vzgstrecke"));
		}
		String keyword = request.getParameter("keyword");
		int fahrplanjahr = (Integer) request.getSession().getAttribute("session_fahrplanjahr");

		BetriebsstelleService service = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		List<Betriebsstelle> list = service.findByKeyword(keyword, vzgStrecke, fahrplanjahr);

		if (list != null && list.size() > 0) {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter outtext = response.getWriter();
			outtext.println("<ul>");
			for (Betriebsstelle ort : list) {
				outtext.println("<li>" + ort.getCaptionWithID() + "</li>");
			}
			outtext.println("</ul>");
			outtext.close();
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}