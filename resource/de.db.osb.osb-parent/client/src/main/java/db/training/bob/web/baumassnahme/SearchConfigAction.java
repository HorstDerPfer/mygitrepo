package db.training.bob.web.baumassnahme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseDispatchAction;
import db.training.easy.web.constants.CommonConstants;
import db.training.logwrapper.Logger;

public class SearchConfigAction extends BaseDispatchAction {

	private Logger log;

	public SearchConfigAction() {
		log = Logger.getLogger(this.getClass());
		serviceFactory = EasyServiceFactory.getInstance();
	}

	public SearchConfigAction(EasyServiceFactory serviceFactory) {
		log = Logger.getLogger(this.getClass());
		try {
			this.serviceFactory = serviceFactory;
		} catch (Exception e) { // do nothing
		}
	}

	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering SearchConfigAction.load");
		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);

	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering SearchConfigAction.save");
		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);

	}
}