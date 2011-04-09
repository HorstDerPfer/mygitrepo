package db.training.bob.web.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Fplonr;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.web.baumassnahme.BaumassnahmeForm;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class UebergabeblattViewAction extends BaseAction {

	private BaumassnahmeService service;

	private Logger log;

	public UebergabeblattViewAction() {
		log = Logger.getLogger(this.getClass());
		try {
			serviceFactory = EasyServiceFactory.getInstance();
			service = serviceFactory.createBaumassnahmeService();
		} catch (Exception e) {// do nothing
		}
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattViewAction.");

		int id = 0;

		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		id = baumassnahmeForm.getId();

		// Preload[] preloads = new Preload[] { new Preload(Baumassnahme.class, "uebergabeblatt"),
		// new Preload(Uebergabeblatt.class, "header"), new Preload(Header.class, "sender"),
		// new Preload(Uebergabeblatt.class, "massnahmen"),
		// new Preload(Massnahme.class, "version"), new Preload(Massnahme.class, "strecke"),
		// new Preload(Massnahme.class, "bbp"), new Preload(Strecke.class, "streckeVZG"),
		// new Preload(Massnahme.class, "fplonr"),
		// new Preload(Fplonr.class, "niederlassungen"), new Preload(Massnahme.class, "zug"),
		// new Preload(Zug.class, "regelweg"), new Preload(Regelweg.class, "abgangsbahnhof"),
		// new Preload(Regelweg.class, "zielbahnhof"), new Preload(Zug.class, "abweichung"),
		// new Preload(Abweichung.class, "halt"), new Preload(Halt.class, "ausfall"),
		// new Preload(Halt.class, "ersatz"), new Preload(Abweichung.class, "ausfallvon"),
		// new Preload(Abweichung.class, "ausfallbis"),
		// new Preload(Abweichung.class, "vorplanab") };
		// Baumassnahme baumassnahme = service.findById(id, preloads);
		Preload[] preloads = new Preload[] { new Preload(Baumassnahme.class, "uebergabeblatt"),
		        new Preload(Uebergabeblatt.class, "header"), new Preload(Header.class, "sender"),
		        new Preload(Uebergabeblatt.class, "massnahmen"),
		        new Preload(Massnahme.class, "version"), new Preload(Massnahme.class, "strecke"),
		        new Preload(Massnahme.class, "bbp"), new Preload(Strecke.class, "streckeVZG"),
		        new Preload(Massnahme.class, "fplonr"),
		        new Preload(Fplonr.class, "niederlassungen") };
		Baumassnahme baumassnahme = service.findById(id, preloads);

		request.setAttribute("baumassnahme", baumassnahme);

		return mapping.findForward("SUCCESS");
	}

	public void setBaumassnahmeService(BaumassnahmeService service) {
		this.service = service;
	}

}
