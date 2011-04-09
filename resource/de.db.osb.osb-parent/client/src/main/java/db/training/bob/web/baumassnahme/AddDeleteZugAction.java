package db.training.bob.web.baumassnahme;

import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.zvf.ZugService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class AddDeleteZugAction extends BaseAction {

	private static Logger log = Logger.getLogger(AddDeleteZugAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering AddDeleteZugAction.");

		BaumassnahmeForm bmForm = (BaumassnahmeForm) form;
		Integer zugId = null;
		Integer bmId = null;
		String abweichung = null;
		String type = null;

		if (request.getParameter("zugId") != null)
			zugId = Integer.parseInt(request.getParameter("zugId"));
		if (request.getParameter("id") != null)
			bmId = Integer.parseInt(request.getParameter("id"));
		if (request.getParameter("abweichung") != null)
			abweichung = request.getParameter("abweichung");
		if (request.getParameter("type") != null)
			type = request.getParameter("type");
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("showZuege"))) {
			bmForm.setShowZuegeUeb(FrontendHelper.castStringToBoolean(request
			    .getParameter("showZuege")));
		}

		BaumassnahmeService bmService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBZR, FetchPlan.UEB_BAUMASSNAHMEN,
		        FetchPlan.BOB_UEBERGABEBLATT, FetchPlan.BBZR_BAUMASSNAHMEN, FetchPlan.UEB_MN_ZUEGE,
		        FetchPlan.BBZR_MN_ZUEGE, FetchPlan.ZVF_MN_STRECKEN, FetchPlan.ZVF_MN_VERSION };
		Baumassnahme bm = bmService.findById(bmId, fetchPlans);

		ZugService zugService = EasyServiceFactory.getInstance().createZugService();

		Uebergabeblatt ueb = null;
		if (type.equals("ZVF"))
			ueb = bm.getAktuelleZvf();
		if (type.equals("UEB"))
			ueb = bm.getUebergabeblatt();

		if (zugId != null) {// delete
			Zug z = zugService.findById(zugId);
			try {
				List<Zug> zuege = ueb.getMassnahmen().iterator().next().getZug();
				zuege.remove(z);
				ueb.getMassnahmen().iterator().next().setZug(zuege);
				bmService.update(bm);
				zugService.delete(z);
			} catch (NoSuchElementException e) {
			}
		} else { // add
			try {
				List<Zug> zuege = ueb.getMassnahmen().iterator().next().getZug();
				Zug z = new Zug();
				if (type.equals("ZVF")) {
					Abweichungsart abw = getAbweichung(abweichung);
					z.getAbweichung().setArt(abw);
				}
				zuege.add(z);
				zugService.create(z);
				bmService.update(bm);
			} catch (NoSuchElementException e) {
			}
		}

		response.setHeader("Content-Type", "text/html; charset=utf-8");
		request.setAttribute("baumassnahme", bm);

		if (type.equals("ZVF")) {
			return mapping.findForward("SUCCESS_ZVF");
		}
		return mapping.findForward("SUCCESS_UEB");
	}

	private Abweichungsart getAbweichung(String abweichungsart) throws Exception {
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.ERSATZHALTE.toString()))
			return Abweichungsart.ERSATZHALTE;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.UMLEITUNG.toString()))
			return Abweichungsart.UMLEITUNG;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.AUSFALL.toString()))
			return Abweichungsart.AUSFALL;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.VORPLAN.toString()))
			return Abweichungsart.VORPLAN;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.GESPERRT.toString()))
			return Abweichungsart.GESPERRT;
		if (abweichungsart.equalsIgnoreCase(Abweichungsart.REGELUNG.toString()))
			return Abweichungsart.REGELUNG;
		return Abweichungsart.VERSPAETUNG;
	}
}
