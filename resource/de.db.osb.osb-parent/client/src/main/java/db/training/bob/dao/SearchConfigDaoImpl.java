package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.SearchConfig;
import db.training.easy.common.BasicDaoImp;

public class SearchConfigDaoImpl extends BasicDaoImp<SearchConfig, Serializable> implements
    SearchConfigDao {

	public SearchConfigDaoImpl(SessionFactory sessionFactory) {
		super(SearchConfig.class, sessionFactory);
	}
}