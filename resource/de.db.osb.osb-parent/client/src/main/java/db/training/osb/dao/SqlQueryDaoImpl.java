package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.SqlQuery;

public class SqlQueryDaoImpl extends BasicDaoImp<SqlQuery, Serializable> implements SqlQueryDao {

	public SqlQueryDaoImpl(SessionFactory instance) {
		super(SqlQuery.class, instance);
	}
}