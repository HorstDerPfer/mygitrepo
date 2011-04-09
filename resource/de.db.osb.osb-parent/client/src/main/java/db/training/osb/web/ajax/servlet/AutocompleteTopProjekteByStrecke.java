package db.training.osb.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import db.training.bob.util.CollectionHelper;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.TopProjektService;
import db.training.osb.service.VzgStreckeService;

@SuppressWarnings("serial")
public class AutocompleteTopProjekteByStrecke extends HttpServlet {

	private static Logger log = Logger.getLogger(AutocompleteTopProjekteByStrecke.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

		VzgStrecke vzgStrecke = null;
		Integer vzgStreckeNummer = null;

		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();
		Integer fahrplanjahr = FrontendHelper.castStringToInteger(request
		    .getParameter("fahrplanjahr"));

		if (FrontendHelper.castStringToInteger(request.getParameter("vzgStreckeNummer")) != null) {
			vzgStreckeNummer = FrontendHelper.castStringToInteger(request
			    .getParameter("vzgStreckeNummer"));
		} else {
			vzgStreckeNummer = vzgService.castCaptionToNummer(request
			    .getParameter("vzgStreckeNummer"));
		}

		if (vzgStreckeNummer != null) {
			if (fahrplanjahr != null) {
				vzgStrecke = CollectionHelper.getFirst(vzgService.findByNummer(vzgStreckeNummer,
				    fahrplanjahr, null, true, null));

			} else
				vzgStrecke = vzgService.findByNummer(vzgStreckeNummer, null, null);
		}

		if (vzgStrecke == null) {
			if (log.isDebugEnabled())
				log.debug("Anfrage enhält keine Daten");
			return;
		}

		TopProjektService tpService = EasyServiceFactory.getInstance().createTopProjektService();
		List<TopProjekt> topProjekte = tpService.findByVzgStrecke(vzgStrecke, null);

		JSONObject json = new JSONObject();
		for (TopProjekt tp : topProjekte) {
			json.put(tp.getId(), tp.getCaption());
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.close();
	}
}