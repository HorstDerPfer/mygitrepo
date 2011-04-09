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
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.TerminUebersichtPersonenverkehrsEVUService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class RefreshPEVUAction extends BaseAction {

	private static final Logger log = Logger.getLogger(RefreshPEVUAction.class);

	private BaumassnahmeService baumassnahmeService;

	private TerminUebersichtPersonenverkehrsEVUService terminePEVUService;

	private EVUGruppeService evuGruppeService;

	public RefreshPEVUAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		terminePEVUService = serviceFactory.createTerminUebersichtPersonenverkehrsEVUService();
		evuGruppeService = serviceFactory.createEVUGruppeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering RefreshPEVUAction.");

		User loginUser = getLoginUser(request);

		Integer bmId = null;
		Integer grpId = null;
		String grpName = null;
		Integer terminePEVUId = null;
		Baumassnahme bm = null;
		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("id"))) {
			bmId = FrontendHelper.castStringToInteger(request.getParameter("id"));
		}

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("evu"))) {
			grpId = FrontendHelper.castStringToInteger(request.getParameter("evu"));
		}

		// nicht null beim einfügen
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("grpName"))) {
			grpName = request.getParameter("grpName");
			EVUGruppe grpObj = evuGruppeService.findUniqueByName(grpName);
			grpId = (grpObj != null) ? grpObj.getId() : null;
		}

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("terminePEVUId"))) {
			terminePEVUId = FrontendHelper.castStringToInteger(request
			    .getParameter("terminePEVUId"));
		}

		if (bmId != null) {
			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_TERMINE_PEVU };
			bm = baumassnahmeService.findById(bmId, fetchPlans);
		}

		String errorKey = null;
		boolean update = false;

		if (bm != null) {

			TerminUebersichtPersonenverkehrsEVU terminePEVU = null;

			if (grpId != null) {
				EVUGruppe grpObj = evuGruppeService.findById(grpId);
				if (grpObj != null) {
					Set<TerminUebersichtPersonenverkehrsEVU> terminePEVUs = bm.getPevus();
					Iterator<TerminUebersichtPersonenverkehrsEVU> it = terminePEVUs.iterator();
					while (it.hasNext()) {
						terminePEVU = it.next();
						if (grpObj.equals(terminePEVU.getEvuGruppe())) {
							errorKey = "error.evu.doubleinsert";
							request.setAttribute("errorKey", errorKey);
							request.setAttribute("baumassnahme", bm);
							return mapping.findForward("FAILURE");
						}
					}
					TerminUebersichtPersonenverkehrsEVU t = new TerminUebersichtPersonenverkehrsEVU(
					    bm.getArt());
					t.setEvuGruppe(grpObj);

					terminePEVUService.create(t);
					bm.getPevus().add(t);
					update = true;
				} else
					// versuch hinzufügen, aber nichts ausgewählt
					errorKey = "error.required.common";
			}

			if (terminePEVUId != null) { // löschen
				terminePEVU = terminePEVUService.findById(terminePEVUId);
				if (terminePEVU != null) {
					bm.getPevus().remove(terminePEVU);
					update = true;
				} else
					errorKey = "error.evu.notfound";
			}

			if (update) {
				bm.setLastChange(null);

				baumassnahmeService.update(bm);

				if (terminePEVUId != null)
					terminePEVUService.delete(terminePEVU);
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
		Set<TerminUebersichtPersonenverkehrsEVU> terminePevu = bm.getPevus();
		Iterator<TerminUebersichtPersonenverkehrsEVU> it2 = terminePevu.iterator();
		while (it2.hasNext()) {
			TerminUebersichtPersonenverkehrsEVU t = it2.next();
			String evuId = Integer.toString(t.getEvuGruppe().getId());

			baumassnahmeForm.setPevuStudieGrobkonzept(evuId,
			    FrontendHelper.castDateToString(t.getStudieGrobkonzept(), dateFormatString));

			baumassnahmeForm.setPevuZvfEntwurf(evuId,
			    FrontendHelper.castDateToString(t.getZvfEntwurf(), dateFormatString));
			baumassnahmeForm.setPevuIsZvfEntwurfErforderlich(evuId, t.isZvfEntwurfErforderlich());

			baumassnahmeForm.setPevuStellungnahmeEVU(evuId,
			    FrontendHelper.castDateToString(t.getStellungnahmeEVU(), dateFormatString));
			baumassnahmeForm.setPevuIsStellungnahmeEVUErforderlich(evuId,
			    t.isStellungnahmeEVUErforderlich());

			baumassnahmeForm.setPevuZvF(evuId,
			    FrontendHelper.castDateToString(t.getZvF(), dateFormatString));
			baumassnahmeForm.setPevuIsZvFErforderlich(evuId, t.isZvfErforderlich());

			baumassnahmeForm.setPevuMasterUebergabeblattPV(evuId,
			    FrontendHelper.castDateToString(t.getMasterUebergabeblattPV(), dateFormatString));
			baumassnahmeForm.setPevuIsMasterUebergabeblattPVErforderlich(evuId,
			    t.isMasterUebergabeblattPVErforderlich());

			baumassnahmeForm.setPevuUebergabeblattPV(evuId,
			    FrontendHelper.castDateToString(t.getUebergabeblattPV(), dateFormatString));
			baumassnahmeForm.setPevuIsUebergabeblattPVErforderlich(evuId,
			    t.isUebergabeblattPVErforderlich());

			baumassnahmeForm.setPevuFplo(evuId,
			    FrontendHelper.castDateToString(t.getFplo(), dateFormatString));
			baumassnahmeForm.setPevuIsFploErforderlich(evuId, t.isFploErforderlich());

			baumassnahmeForm.setPevuEingabeGFD_Z(evuId,
			    FrontendHelper.castDateToString(t.getEingabeGFD_Z(), dateFormatString));
			baumassnahmeForm.setPevuIsEingabeGFD_ZErforderlich(evuId,
			    t.isEingabeGFD_ZErforderlich());

			baumassnahmeForm.setPevuAusfaelleSEV(evuId, t.isAusfaelleSEV());

			baumassnahmeForm.setPevuBKonzeptEVU(evuId,
			    FrontendHelper.castDateToString(t.getBKonzeptEVU(), dateFormatString));
			baumassnahmeForm.setPevuIsBKonzeptEVUErforderlich(evuId, t.isBKonzeptEVUErforderlich());
		}
	}

}
