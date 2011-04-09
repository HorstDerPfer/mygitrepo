package db.training.bob.web.baumassnahme;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.SAPMassnahmeService;

public class SAPBaumassnahmeListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SAPBaumassnahmeListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering SAPBaumassnahmeListAction.");

		SAPMassnahmeService sapMassnahmeService = EasyServiceFactory.getInstance()
		    .createSAPMassnahmeService();

		List<SAPMassnahme> massnahmen = sapMassnahmeService.findAll();

		request.setAttribute("baumassnahmen", massnahmen);

		// TODO if failure
		return mapping.findForward("SUCCESS");
	}
}
