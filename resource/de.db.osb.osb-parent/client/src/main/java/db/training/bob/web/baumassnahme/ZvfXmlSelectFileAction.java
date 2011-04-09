package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class ZvfXmlSelectFileAction extends BaseAction {

	private static Logger log = Logger.getLogger(ZvfXmlSelectFileAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
		if (log.isDebugEnabled())
			log.debug("Entering ZvfXmlSelectFileAction.");

		if (request.getParameter("baumassnahmeId") == null) {
			if (log.isDebugEnabled())
				log.debug("NO ID");
			return mapping.findForward("FAILURE");
		}

		if (request.getParameter("type") == null) {
			if (log.isDebugEnabled())
				log.debug("NO TYPE");
			return mapping.findForward("FAILURE");
		}

		if (request.getParameter("zvfId") == null) {
			if (log.isDebugEnabled())
				log.debug("NO ZVF");
			return mapping.findForward("FAILURE");
		}

		Integer id = null;
		String type;
		Integer zvfId = null;

		try {
			id = Integer.parseInt(request.getParameter("baumassnahmeId"));
			type = request.getParameter("type");
			zvfId = Integer.parseInt(request.getParameter("zvfId"));
			XmlImportForm xmlImportForm = (XmlImportForm) form;
			xmlImportForm.setBaumassnahmeId(id);
			xmlImportForm.setType(type);
			xmlImportForm.setZvfId(zvfId);
		} catch (Exception e) {
			return mapping.findForward("FAILURE");
		}

		request.setAttribute("baumassnahmeId", id);
		request.setAttribute("type", type);
		request.setAttribute("zvfId", zvfId);
		return mapping.findForward("SUCCESS");
	}
}
