package db.training.osb.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.dao.EasyDaoFactory;
import db.training.logwrapper.Logger;
import db.training.osb.dao.SqlQueryDao;
import db.training.osb.model.SqlQuery;
import db.training.osb.model.SqlQuery.Cluster;
import db.training.osb.model.SqlQuery.Modul;
import db.training.osb.web.sqlQuery.SqlQueryExecuteAction;

public class SqlQueryServiceImpl extends EasyServiceImpl<SqlQuery, Serializable> implements
    SqlQueryService {

	private static Logger log = Logger.getLogger(SqlQueryExecuteAction.class);

	private String connectionUrl;

	private String connectionUsername;

	private String connectionPassword;

	public SqlQueryServiceImpl() {
		super(SqlQuery.class);
	}

	public SqlQueryDao getDao() {
		return (SqlQueryDao) getBasicDao();
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getConnectionUsername() {
		return connectionUsername;
	}

	public void setConnectionUsername(String connectionUsername) {
		this.connectionUsername = connectionUsername;
	}

	public String getConnectionPassword() {
		return connectionPassword;
	}

	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	@Transactional
	public List<SqlQuery> findByCluster(Cluster cluster) {
		log.debug("Entering findByCluster.");
		List<SqlQuery> result = null;
		result = EasyDaoFactory.getInstance().getSqlQueryDao().findByCriteria(
		    getCurrentSession().createCriteria(SqlQuery.class).add(
		        Restrictions.eq("modul", Modul.OSB)).add(Restrictions.eq("cluster", cluster)));
		return result;
	}

	/**
	 * ResultSet muss in der aufrufenden Methode geschlossen werden!
	 */
	@Transactional
	public ResultSet findResultsByQuery(String sqlQuery) {
		log.debug("Entering findResultsByQuery.");

		Connection connection = null;
		ResultSet rs = null;

		try {
			connection = DriverManager.getConnection(getConnectionUrl(), getConnectionUsername(),
			    getConnectionPassword());
			rs = connection.createStatement().executeQuery(sqlQuery);

			return rs;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}
}