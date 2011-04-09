package db.training.bob.web.baumassnahme;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class UebergabeblattFertigAction extends BaseAction {

	private static Logger log = Logger.getLogger(UebergabeblattFertigAction.class);

	BaumassnahmeService baumassnahmeService;

	public UebergabeblattFertigAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattFertigAction.");

		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		Baumassnahme baumassnahme = getBaumassnahme(request, baumassnahmeForm);

		if (baumassnahme != null) {
			setMeilensteinUeb(baumassnahme);
			setUebergabeblattFertig(baumassnahme);
			baumassnahmeService.update(baumassnahme);
		} else {
			addError("error.baumassnahme.notfound");
			return mapping.findForward("FAILURE");
		}

		return mapping.findForward("SUCCESS");

	}

	private Baumassnahme getBaumassnahme(HttpServletRequest request,
	    BaumassnahmeForm baumassnahmeForm) {
		Integer id = null;
		if (request.getAttribute("id") != null)
			id = (Integer) request.getAttribute("id");
		else
			id = baumassnahmeForm.getId();

		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_TERMINE_GEVU,
		        FetchPlan.BOB_TERMINE_PEVU, FetchPlan.BOB_UEBERGABEBLATT };
		Baumassnahme baumassnahme = baumassnahmeService.findById(id, fetchPlans);
		return baumassnahme;
	}

	private void setUebergabeblattFertig(Baumassnahme baumassnahme) {
		Date currentDate = new Date();
		Uebergabeblatt ueb = baumassnahme.getUebergabeblatt();
		if (ueb.getFertigStellungsdatum() == null)
			ueb.setFertigStellungsdatum(currentDate);
	}

	private void setMeilensteinUeb(Baumassnahme baumassnahme) {
		Date currentDate = new Date();

		Set<TerminUebersichtGueterverkehrsEVU> termineGEVUs = baumassnahme.getGevus();
		for (TerminUebersichtGueterverkehrsEVU termineGEVU : termineGEVUs) {
			termineGEVU.setUebergabeblattGV(currentDate);
		}

		Set<TerminUebersichtPersonenverkehrsEVU> terminePEVUs = baumassnahme.getPevus();
		for (TerminUebersichtPersonenverkehrsEVU terminePEVU : terminePEVUs) {
			terminePEVU.setUebergabeblattPV(currentDate);
		}
	}
}
