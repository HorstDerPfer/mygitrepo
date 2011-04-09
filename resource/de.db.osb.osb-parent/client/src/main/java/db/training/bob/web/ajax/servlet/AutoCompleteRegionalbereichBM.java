package db.training.bob.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;

public class AutoCompleteRegionalbereichBM extends HttpServlet {

    private static final long serialVersionUID = -2218306951395321710L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		
		String keyword = request.getParameter("keyword");
		
		if (!FrontendHelper.stringNotNullOrEmpty(keyword))
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		//Service methode für regioBM was mit kexyword anfängt
		RegionalbereichService rbService = EasyServiceFactory.getInstance().createRegionalbereichService();
		
		List<String> list = new ArrayList<String>();
		
		list = rbService.findRegionalbereichBmByKeyword(keyword);
		
		if (list != null && list.size() > 0) {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter outtext = response.getWriter();
			outtext.println("<ul>");
			for (String item : list) {
				outtext.println(String.format("<li>%s</li>", item));
			}
			outtext.println("</ul>");
			outtext.close();
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		
	}
}
