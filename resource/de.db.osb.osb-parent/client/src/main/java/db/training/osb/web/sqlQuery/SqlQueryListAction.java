package db.training.osb.web.sqlQuery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.SqlQuery.Cluster;
import db.training.osb.service.SqlQueryService;
import db.training.security.hibernate.TqmUser;

public class SqlQueryListAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SqlQueryListAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering SqlQueryListAction.");

		TqmUser secUser = getSecUser();

		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL") && !secUser.hasRole("ADMINISTRATOR_REGIONAL")
		    && !secUser.hasRole("AUSWERTER_ZENTRAL") && !secUser.hasRole("AUSWERTER_REGIONAL")) {
			addError("common.noAuth");
			return mapping.findForward("NO_AUTH");
		}

		SqlQueryService sqlQueryService = serviceFactory.createSqlQueryService();

		request.setAttribute("sqlQueriesMengengeruest", sqlQueryService
		    .findByCluster(Cluster.MENGENGERUEST));
		request.setAttribute("sqlQueriesDatenauszug", sqlQueryService
		    .findByCluster(Cluster.DATENAUSZUG));
		request.setAttribute("sqlQueriesFortschrittskennzahl", sqlQueryService
		    .findByCluster(Cluster.FORTSCHRITTSKENNZAHL));

		return mapping.findForward("SUCCESS");
	}
}
