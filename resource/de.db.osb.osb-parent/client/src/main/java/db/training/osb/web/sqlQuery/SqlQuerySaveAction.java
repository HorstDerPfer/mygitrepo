package db.training.osb.web.sqlQuery;

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

public class SqlQuerySaveAction extends BaseAction {

	private static final Logger log = Logger.getLogger(SqlQuerySaveAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		log.debug("Entering SqlQuerySaveAction.");

		TqmUser secUser = getSecUser();

		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL") && !secUser.hasRole("ADMINISTRATOR_REGIONAL")
		    && !secUser.hasRole("AUSWERTER_ZENTRAL") && !secUser.hasRole("AUSWERTER_REGIONAL")) {
			addError("common.noAuth");
			return mapping.findForward("NO_AUTH");
		}

		SqlQueryForm sqlQueryForm = (SqlQueryForm) form;
		SqlQueryService sqlQueryService = serviceFactory.createSqlQueryService();
		SqlQuery sqlQuery = null;

		if (sqlQueryForm.getSqlQueryId() == null) {
			addError("error.sqlQuery.notFound");
			return mapping.findForward("FAILURE");
		}

		if (sqlQueryForm.getSqlQueryId() == 0)
			sqlQuery = new SqlQuery();
		else
			sqlQuery = sqlQueryService.findById(sqlQueryForm.getSqlQueryId());

		sqlQuery.setName(sqlQueryForm.getName());
		sqlQuery.setBeschreibung(sqlQueryForm.getBeschreibung());
		String queryString = sqlQueryForm.getQuery();
		sqlQuery.setQuery(queryString);
		sqlQuery.setGueltigVon(FrontendHelper.castStringToDate(sqlQueryForm.getGueltigVon()));
		sqlQuery.setGueltigBis(FrontendHelper.castStringToDate(sqlQueryForm.getGueltigBis()));
		sqlQuery.setModul(Modul.valueOf(sqlQueryForm.getModul()));
		sqlQuery.setCluster(Cluster.valueOf(sqlQueryForm.getCluster()));

		if (sqlQueryForm.getSqlQueryId() == 0) {
			sqlQueryService.create(sqlQuery);
			request.setAttribute("sqlQueryId", sqlQuery.getId());
		} else
			sqlQueryService.update(sqlQuery);

		addMessage("success.saved");
		return mapping.findForward("SUCCESS");
	}
}
