package db.training.osb.web.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.VzgStreckeService;

@SuppressWarnings("serial")
public class AutocompleteBetriebstellenByStrecke extends HttpServlet {

	private static Logger log = Logger.getLogger(AutocompleteBetriebstellenByStrecke.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		if (log.isDebugEnabled())
			log.debug("Entering AutocompleteBetriebsstellenByStrecke");

		Date beginn = FrontendHelper.castStringToDate(request.getParameter("beginn"));
		Date ende = FrontendHelper.castStringToDate(request.getParameter("ende"));

		Integer fahrplanjahr = null;
		if (request.getParameter("fahrplanjahr") != null)
			fahrplanjahr = FrontendHelper.castStringToInteger(request.getParameter("fahrplanjahr"));

		String vzgStrecke = request.getParameter("vzgStrecke");
		Integer vzgStreckeNummer = null;
		boolean onlyZugmeldestellen = Boolean.parseBoolean(request
		    .getParameter("onlyZugmeldestellen"));

		if (FrontendHelper.castStringToInteger(vzgStrecke) != null) {
			vzgStreckeNummer = FrontendHelper.castStringToInteger(vzgStrecke);
		} else {
			VzgStreckeService vzgService = EasyServiceFactory.getInstance()
			    .createVzgStreckeService();
			vzgStreckeNummer = vzgService.castCaptionToNummer(vzgStrecke);
		}

		if (vzgStreckeNummer == null) {
			if (log.isDebugEnabled())
				log.debug("Anfrage enhaelt keine Daten");
			return;
		}

		BetriebsstelleService bsService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		List<Betriebsstelle> list = null;

		if (beginn == null || ende == null) {
			if (fahrplanjahr != null) {
				list = bsService.findByVzgStreckeNummerAndFahrplanjahr(vzgStreckeNummer,
				    fahrplanjahr, onlyZugmeldestellen, null);
			} else {
				beginn = Calendar.getInstance().getTime();
				ende = EasyPersistentExpirationObject.getMaxGueltigBis();
				list = bsService.findByVzgStreckeNummerAndGueltigkeit(vzgStreckeNummer, beginn,
				    ende, onlyZugmeldestellen, null);
			}
		}

		JSONObject json = new JSONObject();
		for (Betriebsstelle bs : list) {
			json.put(bs.getId(), bs.getCaption());
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.close();
	}
}