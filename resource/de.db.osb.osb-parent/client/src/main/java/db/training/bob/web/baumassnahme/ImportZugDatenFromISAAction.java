package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.service.fplo.ISABOBTransferService;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class ImportZugDatenFromISAAction extends BaseAction {

	private static final Logger log = Logger.getLogger(ImportZugDatenFromISAAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering ImportZugDatenFromISAAction.");

		Integer vorgangsnr = null;
		Integer rbId = null;
		Integer id = null;

		if (request.getParameter("vorgangsnr") != null)
			vorgangsnr = Integer.parseInt(request.getParameter("vorgangsnr"));
		if (request.getParameter("rbId") != null)
			rbId = Integer.parseInt(request.getParameter("rbId"));
		if (request.getParameter("id") != null)
			id = Integer.parseInt(request.getParameter("id"));

		ISABOBTransferService.getZugAndFahrplanFromISA(id, vorgangsnr, rbId);
		request.setAttribute("tab", "Fahrplan");
		return mapping.findForward("SUCCESS");
	}
}
