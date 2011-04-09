package db.training.osb.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.osb.model.Paket;
import db.training.osb.service.PaketService;

@SuppressWarnings("serial")
public class AutocompletePaket extends HttpServlet {

	private static Logger log = Logger.getLogger(AutocompletePaket.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		String keyword = request.getParameter("keyword");

		if (keyword == null || "".equals(keyword.trim())) {
			if (log.isDebugEnabled())
				log.debug("Anfrage enhält keine Daten");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		keyword = keyword.trim();

		PaketService service = EasyServiceFactory.getInstance().createPaketService();

		// parse parameter von keyword in regionalbereichKeyword, fahrplanjahrKeyword, lfdNrKeyword

		Integer regionalbereichId = null;
		Integer fahrplanjahrId = null;
		Integer lfdNrId = null;
		Regionalbereich regionalbereich = null;

		// start parse fahrplanjahrKeyword "xx"
		if (keyword.length() >= 2) {

			regionalbereichId = FrontendHelper.castStringToInteger(keyword.substring(0, 2));
			if (regionalbereichId != null) {
				RegionalbereichService rbService = EasyServiceFactory.getInstance()
				    .createRegionalbereichService();
				regionalbereich = rbService.findById(regionalbereichId);

				if (regionalbereich != null) {

					keyword = keyword.substring(2, keyword.length());

					// skip char '.'
					if (keyword.length() > 0 && keyword.charAt(0) == '.') {
						keyword = keyword.substring(1, keyword.length());

						// start generate fahrplanjahrKeyword "xx"
						if (keyword.length() >= 2) {

							fahrplanjahrId = FrontendHelper.castStringToInteger("20"
							    + keyword.substring(0, 2));
							if (fahrplanjahrId != null) {

								keyword = keyword.substring(2, keyword.length());

								// skip char '.'
								if (keyword.length() > 0 && keyword.charAt(0) == '.') {
									keyword = keyword.substring(1, keyword.length());

									// start generate lfdNrKeyword "xxxx"
									if (keyword.length() >= 4) {

										lfdNrId = FrontendHelper.castStringToInteger(keyword
										    .substring(0, 4));
									}
								}
							}
						}
					}
				}
			}
		}

		if (log.isDebugEnabled())
			log.debug("keywords: '" + regionalbereichId + "'.'" + fahrplanjahrId + "'.'" + lfdNrId
			    + "'");

		// get paket liste
		List<Paket> list = service.findByKeywords(regionalbereich, fahrplanjahrId, lfdNrId);

		if (log.isDebugEnabled() && list != null)
			log.debug("paket list size: " + list.size());

		if (list != null && list.size() > 0) {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter outtext = response.getWriter();
			outtext.println("<ul>");
			for (Paket paket : list) {
				outtext.println("<li>" + paket.getPaketId().replaceAll(" ", "&nbsp;") + "</li>");
			}
			outtext.println("</ul>");
			outtext.close();
		}
	}

}