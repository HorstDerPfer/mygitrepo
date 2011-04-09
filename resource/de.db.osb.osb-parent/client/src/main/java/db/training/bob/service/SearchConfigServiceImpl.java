package db.training.bob.service;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.SearchConfigDao;
import db.training.bob.model.SearchConfig;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.model.User;

public class SearchConfigServiceImpl extends EasyServiceImpl<SearchConfig, Serializable> implements
    SearchConfigService {

	public SearchConfigServiceImpl() {
		super(SearchConfig.class);
	}

	public SearchConfigDao getDao() {
		return (SearchConfigDao) getBasicDao();
	}

	@Override
	@Transactional
	public SearchConfig findByUser(User user) {
		SearchConfig result = null;
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(SearchConfig.class);
		criteria.add(Restrictions.eq("user", user));
		result = (SearchConfig) criteria.uniqueResult();

		return result;
	}
}