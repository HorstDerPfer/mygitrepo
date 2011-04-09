package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Regelung;
import db.training.bob.service.BBPMassnahmeService;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.RegelungService;
import db.training.bob.service.Terminberechnung;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class RegelungSaveAction extends BaseAction {

	private static Logger log = Logger.getLogger(RegelungSaveAction.class);

	private RegelungService regelungService;

	private BBPMassnahmeService bbpMassnahmeService;

	private BaumassnahmeService baumassnahmenService;

	public RegelungSaveAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmenService = serviceFactory.createBaumassnahmeService();
		regelungService = serviceFactory.createRegelungService();
		bbpMassnahmeService = serviceFactory.createBBPMassnahmeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		log.debug("instance is " + this);

		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeSaveAction.");

		RegelungForm regForm = (RegelungForm) form;
		Baumassnahme baumassnahme = null;
		BBPMassnahme bbpmassnahme = null;
		Regelung regelung = null;
		Integer id;

		try {
			id = regForm.getRegelungId();
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Regelung not found");
			addError("error.regelung.notfound");
			return mapping.findForward("FAILURE");
		}

		if (id != 0) {

			regelung = regelungService.findById(regForm.getRegelungId());

			bbpmassnahme = bbpMassnahmeService.findById(regForm.getBbpMassnahmeId());

			// Datumsvalidierung
			if (bbpmassnahme.getBeginn()
			    .after(FrontendHelper.castStringToDate(regForm.getBeginn()))) {

				addError("error.datumregelung.bauzeitraum");
				return mapping.findForward("FAILURE");
			} else if (bbpmassnahme.getEnde().before(
			    FrontendHelper.castStringToDate(regForm.getEnde()))) {

				addError("error.datumregelung.bauzeitraum");
				return mapping.findForward("FAILURE");
			}

			// Es kann nur eine Regelung geben, welche relavant fuer die Terminberechnung ist.
			// Daher alle anderen Regelungen der BBP-Massnahme auf false setzen
			if (regelung.getBeginnFuerTerminberechnung() != regForm.isBeginnFuerTerminberechnung()) {

				baumassnahme = baumassnahmenService.findById(regForm.getBaumassnahmeId(),
				    new Preload[] { new Preload(Baumassnahme.class, "bbpMassnahmen"),
				            new Preload(BBPMassnahme.class, "regelungen"),
				            new Preload(Baumassnahme.class, "baubetriebsplanung"),
				            new Preload(Baumassnahme.class, "gevus"),
				            new Preload(Baumassnahme.class, "pevus") });

				// neues Beginndatum setzen
				baumassnahme.setBeginnFuerTerminberechnung(regelung.getBeginn());
				Terminberechnung.refreshSolltermine(baumassnahme);
				baumassnahmenService.update(baumassnahme);

				// alle Regelungen aendern
				for (BBPMassnahme mass : baumassnahme.getBbpMassnahmen()) {
					for (Regelung reg : mass.getRegelungen()) {
						reg.setBeginnFuerTerminberechnung(false);
						regelungService.update(reg);
					}
				}
			}

			regelung.setBeginnFuerTerminberechnung(regForm.isBeginnFuerTerminberechnung());
			regelung.setBeginn(FrontendHelper.castStringToDate(regForm.getBeginn()));
			regelung.setEnde(FrontendHelper.castStringToDate(regForm.getEnde()));

			regelungService.update(regelung);

		} else {
			if (log.isDebugEnabled())
				log.debug("Regelung not found");
			addError("error.regelung.notfound");
			return mapping.findForward("FAILURE");
		}

		addMessage("success.regelung.save");
		return mapping.findForward("SUCCESS");
	}

}
