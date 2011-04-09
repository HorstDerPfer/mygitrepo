package db.training.bob.web.ajax;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.back.BackRequestProcessor;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.EVUGruppe;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.TerminUebersichtGueterverkehrsEVUService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshGEVUAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshGEVUAction.class);

	private BaumassnahmeService baumassnahmeService;

	private TerminUebersichtGueterverkehrsEVUService termineGEVUService;

	private EVUGruppeService evuGruppeService;

	// private BaumassnahmeForm baumassnahmeForm;
	//
	// Baumassnahme bm;

	public RefreshGEVUAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		termineGEVUService = serviceFactory.createTerminUebersichtGueterverkehrsEVUService();
		evuGruppeService = serviceFactory.createEVUGruppeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshGEVUAction.");

		User loginUser = getLoginUser(request);

		Integer bmId = null;
		Integer gevuGruppeId = null;
		String gevuKundengruppe = null;
		Integer termineGEVUId = null;
		Baumassnahme bm = null;
		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("id"))) {
			bmId = FrontendHelper.castStringToInteger(request.getParameter("id"));
		}
		// nicht null beim einfügen
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("gevu"))) {
			gevuGruppeId = FrontendHelper.castStringToInteger(request.getParameter("gevu"));
		}

		// nicht null beim einfügen
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("gevuKundengruppe"))) {
			gevuKundengruppe = request.getParameter("gevuKundennummer");
			EVUGruppe grpObj = evuGruppeService.findUniqueByName(gevuKundengruppe);
			gevuGruppeId = (grpObj != null) ? grpObj.getId() : null;
		}

		// nicht null beim löschen
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("termineGEVUId"))) {
			termineGEVUId = FrontendHelper.castStringToInteger(request
			    .getParameter("termineGEVUId"));
		}

		if (bmId != null) {
			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_TERMINE_GEVU };
			bm = baumassnahmeService.findById(bmId, fetchPlans);
		}

		String errorKey = null;
		boolean update = false;

		if (bm != null) {

			TerminUebersichtGueterverkehrsEVU termineGEVU = null;

			if (gevuGruppeId != null) {
				EVUGruppe grpObj = evuGruppeService.findById(gevuGruppeId);
				if (grpObj != null) {
					Set<TerminUebersichtGueterverkehrsEVU> termineGEVUs = bm.getGevus();
					Iterator<TerminUebersichtGueterverkehrsEVU> it = termineGEVUs.iterator();
					while (it.hasNext()) { // test, ob evu schon in der liste
						// vorhanden
						termineGEVU = it.next();
						if (grpObj.equals(termineGEVU.getEvuGruppe())) {
							errorKey = "error.evu.doubleinsert";
							request.setAttribute("errorKey", errorKey);
							request.setAttribute("baumassnahme", bm);
							return mapping.findForward("FAILURE");
						}
					}
					TerminUebersichtGueterverkehrsEVU t = new TerminUebersichtGueterverkehrsEVU(
					    bm.getArt());
					t.setEvuGruppe(grpObj);

					termineGEVUService.create(t);
					bm.getGevus().add(t);
					update = true;
				} else
					// versuch hinzufügen, aber nichts ausgewählt
					errorKey = "error.required.common";
			}

			if (termineGEVUId != null) { // löschen
				termineGEVU = termineGEVUService.findById(termineGEVUId);
				if (termineGEVU != null) {
					bm.getGevus().remove(termineGEVU);
					update = true;
				} else
					errorKey = "error.evu.notfound";
			}

			if (update) {
				bm.setLastChange(null);

				baumassnahmeService.update(bm);

				if (termineGEVUId != null)
					termineGEVUService.delete(termineGEVU);
			}
		}

		fill(baumassnahmeForm, bm);

		request.setAttribute("errorKey", errorKey);
		request.setAttribute("baumassnahme", bm);
		request.setAttribute("baumassnahmeForm", baumassnahmeForm);
		request.setAttribute(BackRequestProcessor.SUPRESS_ACTION_LOG_ATTRIBUTE, "true");

		return mapping.findForward("SUCCESS");
	}

	private void fill(BaumassnahmeForm baumassnahmeForm, Baumassnahme bm) {
		final String dateFormatString = "dd.MM.yy";
		Set<TerminUebersichtGueterverkehrsEVU> termineGevu = bm.getGevus();
		Iterator<TerminUebersichtGueterverkehrsEVU> it3 = termineGevu.iterator();
		while (it3.hasNext()) {
			TerminUebersichtGueterverkehrsEVU t = it3.next();
			String evuId = Integer.toString(t.getEvuGruppe().getId());

			baumassnahmeForm.setGevuStudieGrobkonzept(evuId,
			    FrontendHelper.castDateToString(t.getStudieGrobkonzept(), dateFormatString));

			baumassnahmeForm.setGevuZvfEntwurf(evuId,
			    FrontendHelper.castDateToString(t.getZvfEntwurf(), dateFormatString));
			baumassnahmeForm.setGevuIsZvFEntwurfErforderlich(evuId, t.isZvfEntwurfErforderlich());

			baumassnahmeForm.setPevuZvfEntwurf(evuId,
			    FrontendHelper.castDateToString(t.getZvfEntwurf(), dateFormatString));

			baumassnahmeForm.setGevuStellungnahmeEVU(evuId,
			    FrontendHelper.castDateToString(t.getStellungnahmeEVU(), dateFormatString));
			baumassnahmeForm.setGevuIsStellungnahmeEVUErforderlich(evuId,
			    t.isStellungnahmeEVUErforderlich());

			baumassnahmeForm.setGevuZvF(evuId,
			    FrontendHelper.castDateToString(t.getZvF(), dateFormatString));
			baumassnahmeForm.setGevuIsZvFErforderlich(evuId, t.isZvfErforderlich());

			baumassnahmeForm.setGevuMasterUebergabeblattGV(evuId,
			    FrontendHelper.castDateToString(t.getMasterUebergabeblattGV(), dateFormatString));

			baumassnahmeForm.setGevuIsMasterUebergabeblattGVErforderlich(evuId,
			    t.isMasterUebergabeblattGVErforderlich());

			baumassnahmeForm.setGevuUebergabeblattGV(evuId,
			    FrontendHelper.castDateToString(t.getUebergabeblattGV(), dateFormatString));

			baumassnahmeForm.setGevuIsUebergabeblattGVErforderlich(evuId,
			    t.isUebergabeblattGVErforderlich());

			baumassnahmeForm.setGevuFplo(evuId,
			    FrontendHelper.castDateToString(t.getFplo(), dateFormatString));
			baumassnahmeForm.setGevuIsFploErforderlich(evuId, t.isFploErforderlich());

			baumassnahmeForm.setGevuEingabeGFD_Z(evuId,
			    FrontendHelper.castDateToString(t.getEingabeGFD_Z(), dateFormatString));
			baumassnahmeForm.setGevuIsEingabeGFD_ZErforderlich(evuId,
			    t.isEingabeGFD_ZErforderlich());
		}
	}
}