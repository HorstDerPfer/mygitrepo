package db.training.osb.web.baustelle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.model.Baustelle;

public class BaustelleListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(BaustelleListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaustelleListAction.");

		Integer listPageNumber = FrontendHelper.castStringToInteger(request.getParameter("page"));
		if (listPageNumber == null)
			listPageNumber = 1;

		int count = PaginatedList.LIST_OBJECTS_PER_PAGE;
		int start = (listPageNumber - 1) * count;
		PaginatedList<Baustelle> baustellen = serviceFactory.createBaustelleService()
		    .findPaginatedBySort(null, null, start, count,
		        new Preload[] { new Preload(Baustelle.class, "gleissperrungen") },
		        sessionFahrplanjahr);

		baustellen.setObjectsPerPage(count);
		baustellen.setPageNumber(listPageNumber);

		request.setAttribute("baustellen", baustellen);

		// TODO if failure
		return mapping.findForward("SUCCESS");
	}
}
