package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Art;
import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Regelung;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.zvf.BBPStrecke;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Niederlassung;
import db.training.bob.model.zvf.Sender;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.helper.FormularKennung;
import db.training.bob.model.zvf.helper.RegionalbereichAdapter;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.service.zvf.BBPStreckeService;
import db.training.bob.service.zvf.UebergabeblattService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.core.service.UserServiceImpl;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class UebergabeblattManuellAction extends BaseAction {

	private static Logger log = Logger.getLogger(UebergabeblattManuellAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattManuellAction.");

		if (request.getParameter("id") == null) {
			if (log.isDebugEnabled())
				log.debug("NO ID");
			return mapping.findForward("FAILURE");
		}

		Integer id = null;
		id = Integer.parseInt(request.getParameter("id"));

		BaumassnahmeService bmService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME,
		        FetchPlan.BBP_REGELUNGEN, FetchPlan.BOB_KORRIDOR_ZEITFENSTER, FetchPlan.BOB_QS };
		Baumassnahme b = bmService.findById(id, fetchPlans);

		Uebergabeblatt ueb = new Uebergabeblatt();
		Header h = new Header();
		h.setSender(new Sender());
		Massnahme m = ueb.getMassnahmen().iterator().next();

		List<Niederlassung> niederlassungen = m.getFplonr().getNiederlassungen();
		RegionalbereichService rbService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		List<Regionalbereich> rbs = rbService.findAll();
		List<String> rbNames = RegionalbereichAdapter.getRegionalbereichNames();
		User user = UserServiceImpl.getCurrentApplicationUser();
		Regionalbereich userRb = user.getRegionalbereich();

		for (Regionalbereich r : rbs) {
			if (rbNames.contains(r.getName())) {
				if (r.getName().equals(userRb.getName()))
					niederlassungen.add(new Niederlassung(r, true));
				else
					niederlassungen.add(new Niederlassung(r));
			}
		}

		ueb.refreshZugStatusRbEntry();

		for (BBPMassnahme bbp : b.getBbpMassnahmen()) {
			for (Regelung r : bbp.getRegelungen()) {
				if (r.getBeginnFuerTerminberechnung()) {
					String baumassnahmeBBPStrecke = b.getStreckeBBP();
					BBPStrecke bbpStrecke = null;
					if (baumassnahmeBBPStrecke != null & baumassnahmeBBPStrecke.length() > 0) {
						Integer bbpInt = FrontendHelper.castStringToInteger(baumassnahmeBBPStrecke);
						if (bbpInt != null) {
							BBPStreckeService bbpStreckeService = EasyServiceFactory.getInstance()
							    .createBBPStreckeService();
							bbpStrecke = bbpStreckeService.findByNummer(bbpInt);
						}
					}
					if (bbpStrecke != null)
						m.getBbp().add(bbpStrecke);

					m.setKigbau(r.getLisbaNr());
					if (b.getArt().equals(Art.B))
						m.setBaumassnahmenart(Art.B);
					else
						m.setBaumassnahmenart(Art.A);
					m.setKennung(bbp.getMasId());
					m.setVorgangsNr(b.getVorgangsNr());
					if (b.getKorridorNr() != null) {
						m.setKorridor(b.getKorridorNr().toString());
					} else {
						m.setKorridor(null);
					}
				}
			}
		}
		m.setFestgelegtSPFV(false);
		m.setFestgelegtSPNV(false);
		m.setFestgelegtSGV(false);
		m.getVersion().setFormular(FormularKennung.UeB_091008);
		if (m.getQsKsVesNr() == null) {
			List<String> qsNr = new ArrayList<String>(b.getQsNr());
			if (!qsNr.isEmpty()) {
				m.setQsKsVesNr(qsNr.get(0));
			}
		}
		UebergabeblattService uebService = EasyServiceFactory.getInstance()
		    .createUebergabeblattService();
		uebService.create(ueb);

		ueb = uebService.findById(ueb.getId());

		b.setUebergabeblatt(ueb);
		bmService.update(b);

		addMessage("success.uebergabeblatt.create");
		request.setAttribute("id", id);
		return mapping.findForward("SUCCESS");
	}
}
