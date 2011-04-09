package db.training.osb.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BetriebsstelleService;

@SuppressWarnings("serial")
public class AutocompleteOrtsbezeichnungen extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		Integer vzgStrecke = null;
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("vzgstrecke"))) {
			vzgStrecke = VzgStrecke.getId(request.getParameter("vzgstrecke"));
		}

		int fahrplanjahr = (Integer) request.getSession().getAttribute("session_fahrplanjahr");
		String keyword = request.getParameter("keyword");
		String label = "caption";
		if (request.getParameter("label") != null)
			label = request.getParameter("label");

		BetriebsstelleService service = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		List<Betriebsstelle> list = service.findByKeyword(keyword, vzgStrecke, fahrplanjahr);

		List<String> bsListe = new ArrayList<String>();
		for (Betriebsstelle ort : list) {
			if (label.equals("kuerzel"))
				bsListe.add("<li>" + ort.getKuerzel().replaceAll(" ", "&nbsp;") + "</li>");
			else if (label.equals("captionWithId"))
				bsListe.add("<li>" + ort.getCaptionWithID().replaceAll(" ", "&nbsp;") + "</li>");
			else
				bsListe.add("<li>" + ort.getCaption().replaceAll(" ", "&nbsp;") + "</li>");
		}

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter outtext = response.getWriter();
		outtext.println("<ul>");
		if (bsListe.size() > 0) {
			for (String zeile : bsListe) {
				outtext.append(zeile);
			}
		}
		outtext.println("</ul>");
		outtext.close();
	}
}