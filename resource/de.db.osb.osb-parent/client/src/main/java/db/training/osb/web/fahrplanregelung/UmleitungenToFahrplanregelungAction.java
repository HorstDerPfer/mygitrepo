package db.training.osb.web.fahrplanregelung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Umleitung;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.UmleitungService;

public class UmleitungenToFahrplanregelungAction extends BaseAction {

	private static Logger log = Logger.getLogger(UmleitungenToFahrplanregelungAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering UmleitungenToFahrplanregelungAction.");

		UmleitungenToFahrplanregelungForm inputForm = (UmleitungenToFahrplanregelungForm) form;

		FahrplanregelungService frService = serviceFactory.createFahrplanregelungService();
		UmleitungService umlService = serviceFactory.createUmleitungService();

		Integer frId = FrontendHelper.castStringToInteger(request
		    .getParameter("fahrplanregelungId"));

		Fahrplanregelung fahrplanregelung = frService.findById(frId);

		inputForm.reset();
		inputForm.setFahrplanregelungId(fahrplanregelung.getId());

		request.setAttribute("umleitungenToFahrplanregelungForm", inputForm);

		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null)
			listPageNumber = 1;

		int count = PaginatedList.LIST_OBJECTS_PER_PAGE;
		int start = (listPageNumber - 1) * count;
		PaginatedList<Umleitung> umleitungen = umlService
		    .findPaginatedUnlinkedByFahrplanregelungAndFahrplanjahr(fahrplanregelung, start, count,
		        sessionFahrplanjahr, null);
		umleitungen.setObjectsPerPage(count);
		umleitungen.setPageNumber(listPageNumber);

		request.setAttribute("fahrplanregelung", fahrplanregelung);
		request.setAttribute("umleitungen", umleitungen);

		return mapping.findForward("SUCCESS");
	}
}