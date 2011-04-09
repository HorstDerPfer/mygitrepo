package db.training.osb.service;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.List;

import db.training.easy.common.BasicService;
import db.training.osb.model.SqlQuery;
import db.training.osb.model.SqlQuery.Cluster;

public interface SqlQueryService extends BasicService<SqlQuery, Serializable> {

	public List<SqlQuery> findByCluster(Cluster cluster);

	public ResultSet findResultsByQuery(String sqlQuery);

}