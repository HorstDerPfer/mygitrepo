package db.training.osb.web.sqlQuery;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.SqlQuery;
import db.training.osb.model.SqlQuery.Cluster;
import db.training.osb.model.SqlQuery.Modul;
import db.training.osb.service.SqlQueryService;
import db.training.security.hibernate.TqmUser;

public class SqlQueryEditAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SqlQueryEditAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		log.debug("Entering SqlQueryEditAction.");

		TqmUser secUser = getSecUser();

		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL") && !secUser.hasRole("ADMINISTRATOR_REGIONAL")) {
			addError("common.noAuth");
			return mapping.findForward("NO_AUTH");
		}

		SqlQueryForm sqlQueryForm = (SqlQueryForm) form;

		Integer sqlQueryId = null;
		if (request.getAttribute("sqlQueryId") != null)
			sqlQueryId = (Integer) request.getAttribute("sqlQueryId");
		else
			sqlQueryId = sqlQueryForm.getSqlQueryId();

		if (sqlQueryId == null) {
			addError("error.sqlQuery.notFound");
			return mapping.findForward("FAILURE");
		}

		SqlQueryService sqlQueryService = serviceFactory.createSqlQueryService();
		SqlQuery sqlQuery = null;

		if (!hasErrors(request)) {
			if (sqlQueryId == 0) {
				String cluster = sqlQueryForm.getCluster();
				sqlQueryForm.reset();
				sqlQueryForm.setCluster(cluster);
				sqlQuery = new SqlQuery();
			} else {
				sqlQuery = sqlQueryService.findById(sqlQueryId);
				sqlQueryForm.setName(sqlQuery.getName());
				sqlQueryForm.setBeschreibung(sqlQuery.getBeschreibung());
				sqlQueryForm.setQuery(sqlQuery.getQuery());
				sqlQueryForm.setGueltigVon(FrontendHelper
				    .castDateToString(sqlQuery.getGueltigVon()));
				sqlQueryForm.setGueltigBis(FrontendHelper
				    .castDateToString(sqlQuery.getGueltigBis()));
				sqlQueryForm.setModul(String.valueOf(sqlQuery.getModul()));
				sqlQueryForm.setCluster(String.valueOf(sqlQuery.getCluster()));
				request.setAttribute("sqlQuery", sqlQuery);
			}
		}

		request.setAttribute("modulListe", Arrays.asList(Modul.values()));
		request.setAttribute("clusterListe", Arrays.asList(Cluster.values()));

		return mapping.findForward("SUCCESS");
	}
}
