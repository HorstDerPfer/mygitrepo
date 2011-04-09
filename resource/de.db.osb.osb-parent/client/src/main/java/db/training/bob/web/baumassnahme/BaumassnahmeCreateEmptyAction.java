package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Art;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Prioritaet;
import db.training.bob.model.Regionalbereich;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.web.constants.CommonConstants;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class BaumassnahmeCreateEmptyAction extends BaseAction {

	private static Logger log;;

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		log = Logger.getLogger(this.getClass());
		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeCreateAction.");

		BaumassnahmeForm baumassnahmeform = (BaumassnahmeForm) form;

		if (request.getParameter("reset") != null && request.getParameter("reset").equals("true")) {
			baumassnahmeform.reset(mapping, request);
			baumassnahmeform.setRegionalBereichFpl(getLoginUser(request).getRegionalbereich()
			    .getId());
			baumassnahmeform.setStreckeBBP("99999");
			baumassnahmeform.setArt(Art.B.name());
			baumassnahmeform.setKigBau(Boolean.FALSE);
		}

		Baumassnahme baumassnahme = new Baumassnahme();
		baumassnahme.setFahrplanjahr(0);
		request.setAttribute("baumassnahme", baumassnahme);

		List<Art> arten = Arrays.asList(Art.values());
		request.setAttribute("arten", arten);

		List<Prioritaet> prioritaeten = Arrays.asList(Prioritaet.values());
		request.setAttribute("prioritaeten", prioritaeten);

		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		List<Regionalbereich> regionalbereiche = regionalbereichService.findAll();
		request.setAttribute("regionalbereiche", regionalbereiche);

		List<Bearbeitungsbereich> bearbeitungsbereiche = new ArrayList<Bearbeitungsbereich>();
		if (baumassnahme.getRegionalBereichFpl() != null) {
			BearbeitungsbereichService bearbeitungsbereichService = EasyServiceFactory
			    .getInstance().createBearbeitungsbereichService();
			bearbeitungsbereiche = bearbeitungsbereichService.findByRegionalbereich(baumassnahme
			    .getRegionalBereichFpl());
		}
		baumassnahmeform.setBearbeitungsbereichList(bearbeitungsbereiche);
		request.setAttribute("bearbeitungsbereiche", bearbeitungsbereiche);

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

}
