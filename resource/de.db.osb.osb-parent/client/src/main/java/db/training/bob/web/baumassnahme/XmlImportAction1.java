package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class XmlImportAction1 extends BaseAction {

	private static Logger log = Logger.getLogger(XmlImportAction1.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
		if (log.isDebugEnabled())
			log.debug("Entering XmlImportAction1.");

		XmlImportForm xmlImportForm = (XmlImportForm) form;
		if (xmlImportForm.getBaumassnahmeId() == null) {
			if (log.isDebugEnabled())
				log.debug("No baumassnahmeId");
			return mapping.findForward("FAILURE_LIST");
		}
		return mapping.findForward("SUCCESS");
	}
}