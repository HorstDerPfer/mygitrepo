package db.training.osb.web.sqlQuery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.SqlQuery;
import db.training.osb.service.SqlQueryService;
import db.training.security.hibernate.TqmUser;

public class SqlQueryDeleteAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SqlQueryDeleteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering SqlQueryDeleteAction.");

		TqmUser secUser = getSecUser();

		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL") && !secUser.hasRole("ADMINISTRATOR_REGIONAL")) {
			addError("common.noAuth");
			return mapping.findForward("NO_AUTH");
		}

		SqlQueryForm sqlQueryForm = (SqlQueryForm) form;

		if (sqlQueryForm.getSqlQueryId() == null) {
			addError("error.sqlQuery.notFound");
			return mapping.findForward("FAILURE");
		}

		SqlQueryService sqlQueryService = serviceFactory.createSqlQueryService();
		SqlQuery sqlQuery = sqlQueryService.findById(sqlQueryForm.getSqlQueryId());

		if (sqlQuery == null) {
			addError("error.sqlQuery.notFound");
			return mapping.findForward("FAILURE");
		}

		sqlQueryService.delete(sqlQuery);

		return mapping.findForward("SUCCESS");
	}
}
