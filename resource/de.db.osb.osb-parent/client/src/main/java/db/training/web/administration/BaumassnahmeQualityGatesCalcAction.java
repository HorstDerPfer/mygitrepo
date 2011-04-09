package db.training.web.administration;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Art;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.SearchBean;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.easy.web.constants.CommonConstants;
import db.training.security.hibernate.TqmUser;

public class BaumassnahmeQualityGatesCalcAction extends BaseAction {

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TqmUser secUser = getSecUser();
		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL"))
			return mapping.findForward(CommonConstants.STRUTS_FAILURE);

		// response.getOutputStream().println("Entering BaumassnahmeQualityGatesCalcAction.");
		// response.getOutputStream().println("<ul>");

		if (!request.getParameter("execute").equalsIgnoreCase("true")) {
			addError("common.impossible");
			return mapping.findForward("FAILURE");
		}

		// alle Baumaßnahmen auflisten, die es gibt
		BaumassnahmeService baumassnahmeService = EasyServiceFactory
				.getInstance().createBaumassnahmeService();

		int size = baumassnahmeService.findAll().size();
		int start = 0;
		int count = 100;
		for (start = 0; start <= size; start += count) {

			PaginatedList<Baumassnahme> list = baumassnahmeService
					.findBySearchBean(null, new SearchBean(), start, count,
							new FetchPlan[] { FetchPlan.BOB_TERMINE_BBP,
									FetchPlan.BOB_TERMINE_GEVU,
									FetchPlan.BOB_TERMINE_PEVU });

			List<Baumassnahme> baumassnahmen = list.getList();
			// response.getOutputStream().flush();
			// response.getOutputStream().println(
			// "<li>" + "verarbeite Baumassnahmen von " + start + " bis "
			// + (start + baumassnahmen.size()) + " von insg. " + size +
			// "<p>ID: ");

			for (Baumassnahme bm : baumassnahmen) {
				Date beginnFuerTerminberechnung = bm
						.getBeginnFuerTerminberechnung();
				Art art = bm.getArt();

				// response.getOutputStream().print(bm.getId() + ", ");

				// Terminberechnung BBP
				TerminUebersichtBaubetriebsplanung bbp = bm
						.getBaubetriebsplanung();
				bbp.setSollTermine(beginnFuerTerminberechnung, art);

				// Terminberechnung PEVU
				if (bm.getPevus() != null) {
					Iterator<TerminUebersichtPersonenverkehrsEVU> it = bm
							.getPevus().iterator();
					while (it.hasNext()) {
						TerminUebersichtPersonenverkehrsEVU termine = it.next();
						termine.setSollTermine(beginnFuerTerminberechnung, art);
					}
				}

				// Terminberechnung GEVU
				if (bm.getGevus() != null) {
					Iterator<TerminUebersichtGueterverkehrsEVU> it = bm
							.getGevus().iterator();
					while (it.hasNext()) {
						TerminUebersichtGueterverkehrsEVU termine = it.next();
						termine.setSollTermine(beginnFuerTerminberechnung, art);
					}
				}

				// Änderungen speichern
				baumassnahmeService.update(bm);
			}
			// response.getOutputStream().println("</p>");
		}

		// response.getOutputStream().println("</ul>");

		addMessage("success.saved");
		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}
}
