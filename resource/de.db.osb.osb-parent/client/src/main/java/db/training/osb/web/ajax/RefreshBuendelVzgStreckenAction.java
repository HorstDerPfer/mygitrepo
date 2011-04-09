package db.training.osb.web.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.BuendelService;
import db.training.osb.service.VzgStreckeService;

/**
 * @author AndreasLotz
 */
public class RefreshBuendelVzgStreckenAction extends BaseAction {

	private static Logger log = Logger.getLogger(RefreshBuendelVzgStreckenAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		log.debug("Entering RefreshBuendelVzgStreckenAction");

		Integer buendelId = FrontendHelper.castStringToInteger(request.getParameter("buendelId"));

		Buendel buendel = null;
		if (buendelId != null) {
			BuendelService buendelService = serviceFactory.createBuendelService();
			buendel = buendelService.findById(buendelId, new Preload[] { new Preload(
			    VzgStrecke.class, "betriebsstellen") });
		}

		List<VzgStrecke> vzgStreckenAlle = new ArrayList<VzgStrecke>();
		if (buendel != null) {
			log.debug("Processiong buendel: " + buendel.getId());

			VzgStreckeService vzgService = serviceFactory.createVzgStreckeService();
			// Die Hauptstrecke muss auf jeden Fall in der Liste stehen unabhaengig von der Region
			// Hauptstrecke wird zuerst eingefuegt um an erster Stelle zustehen
			vzgStreckenAlle.add(buendel.getHauptStrecke());
			Set<VzgStrecke> vzgStrecken = vzgService.findByFahrplanjahr(buendel.getFahrplanjahr(),
			    new Preload[] { new Preload(VzgStrecke.class, "betriebsstellen") });
			if (vzgStrecken.contains(buendel.getHauptStrecke())) {
				vzgStrecken.remove(buendel.getHauptStrecke());
			}
			vzgStreckenAlle.addAll(vzgStrecken);
		}
		request.setAttribute("vzgStreckenAlle", vzgStreckenAlle);
		return mapping.findForward("SUCCESS");
	}
}
