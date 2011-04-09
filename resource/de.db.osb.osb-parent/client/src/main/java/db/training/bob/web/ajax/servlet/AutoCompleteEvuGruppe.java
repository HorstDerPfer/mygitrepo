package db.training.bob.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.bob.model.EVUGruppe;
import db.training.bob.service.EVUGruppeService;
import db.training.easy.core.service.EasyServiceFactory;

/**
 * listet EVU-Gruppen (Kundengruppen) auf und gibt diese zurück. Der Suchparameter
 * <code>keyword</code> durchsucht den Namen der EVU-Gruppe sowie die Kundennummern der einzelnen
 * EVU.
 * 
 * @author michels
 */
public class AutoCompleteEvuGruppe extends HttpServlet {

	private static final long serialVersionUID = -2542652585312031718L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		EVUGruppeService service = EasyServiceFactory.getInstance().createEVUGruppeService();
		List<EVUGruppe> list = service.findByKeyword(keyword);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter outtext = response.getWriter();
		if (list != null) {
			outtext.println("<ul>");
			for (EVUGruppe evuKundengruppe : list) {
				StringBuilder sb = new StringBuilder();
				sb.append("<li id='");
				sb.append(evuKundengruppe.getId());
				sb.append("'>");

				sb.append(evuKundengruppe.getName().replaceAll(" ", "&nbsp;"));

				// if (label.equals("kuerzel"))
				// sb.append(evuKundengruppe.getKundenNr().replaceAll(" ", "&nbsp;"));
				// else
				// sb.append(evuKundengruppe.getCaption().replaceAll(" ", "&nbsp;"));
				sb.append("</li>");

				outtext.println(sb.toString());
			}

			outtext.println("</ul>");
		}
		outtext.close();
	}
}
