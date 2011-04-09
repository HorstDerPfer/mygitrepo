package db.training.osb.web.sqlQuery;

import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.util.Oracle10gRowSetDynaClass;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.SqlQuery;
import db.training.osb.service.SqlQueryService;
import db.training.security.hibernate.TqmUser;

public class SqlQueryExecuteAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SqlQueryExecuteAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		log.debug("Entering SqlQueryExecuteAction.");

		TqmUser secUser = getSecUser();

		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL") && !secUser.hasRole("ADMINISTRATOR_REGIONAL")
		    && !secUser.hasRole("AUSWERTER_ZENTRAL") && !secUser.hasRole("AUSWERTER_REGIONAL")) {
			addError("common.noAuth");
			return mapping.findForward("NO_AUTH");
		}

		SqlQueryService sqlQueryService = serviceFactory.createSqlQueryService();
		if (request.getParameter("sqlQueryId") != null) {
			Integer sqlQueryId = FrontendHelper.castStringToInteger(request
			    .getParameter("sqlQueryId"));
			if (sqlQueryId != null) {
				SqlQuery sqlQuery = sqlQueryService.findById(sqlQueryId);
				Date now = new Date();
				if (now.after(sqlQuery.getGueltigVon()) && sqlQuery.getGueltigBis().after(now)) {
					ResultSet results = sqlQueryService.findResultsByQuery(sqlQuery.getQuery());
					request.setAttribute("sqlQuery", sqlQuery);
					if (results != null) {
						request.setAttribute("results",
						    new Oracle10gRowSetDynaClass(results, false));
						results.close();
						return mapping.findForward("SUCCESS");
					}
				} else
					addError("error.sqlQuery.wrongDate");
			} else
				addError("error.sqlQuery.execute");
		} else
			addError("error.sqlQuery.execute");
		return mapping.findForward("FAILURE");
	}
}