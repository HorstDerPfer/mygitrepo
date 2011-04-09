package db.training.osb.web.massnahme;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.service.SAPMassnahmeService;

public class MassnahmeListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(MassnahmeListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering MassnahmeListAction.");

		SAPMassnahmeService sapMassnahmeService = serviceFactory.createSAPMassnahmeService();
		List<SAPMassnahme> massnahmen = sapMassnahmeService.findAll();

		request.setAttribute("massnahmen", massnahmen);

		return mapping.findForward("SUCCESS");
	}
}