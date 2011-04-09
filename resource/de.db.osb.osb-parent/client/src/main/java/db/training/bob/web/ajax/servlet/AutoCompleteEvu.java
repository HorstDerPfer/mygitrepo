package db.training.bob.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.bob.model.EVU;
import db.training.bob.service.EVUService;
import db.training.easy.core.service.EasyServiceFactory;

/**
 * @author michels
 */
@SuppressWarnings("serial")
public class AutoCompleteEvu extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		String label = "caption";
		if (request.getParameter("label") != null)
			label = request.getParameter("label");
		EVUService service = EasyServiceFactory.getInstance().createEVUService();
		List<EVU> list = service.findByKeyword(keyword);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter outtext = response.getWriter();
		if (list != null) {
			outtext.println("<ul>");
			for (EVU evu : list) {
				StringBuilder sb = new StringBuilder();
				sb.append("<li id='");
				sb.append(evu.getId());
				sb.append("'>");

				if (label.equals("kuerzel"))
					sb.append(evu.getKundenNr().replaceAll(" ", "&nbsp;"));
				else
					sb.append(evu.getCaption().replaceAll(" ", "&nbsp;"));
				sb.append("</li>");

				outtext.println(sb.toString());
			}

			outtext.println("</ul>");
		}
		outtext.close();
	}
}
