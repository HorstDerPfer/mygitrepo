package db.training.osb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.osb.dao.BaustelleDao;
import db.training.osb.model.Baustelle;

public class BaustelleServiceImpl extends EasyServiceImpl<Baustelle, Serializable> implements
    BaustelleService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(BaustelleServiceImpl.class);

	public BaustelleServiceImpl() {
		super(Baustelle.class);
	}

	public BaustelleDao getDao() {
		return (BaustelleDao) getBasicDao();
	}

	@Transactional
	public PaginatedList<Baustelle> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCritieria, Integer start, Integer count, Preload[] preloads,
	    int fahrplanjahr) {

		// Criteria füllen
		Criteria criteria = getCurrentSession().createCriteria(Baustelle.class);
		criteria.add(Restrictions.eq("fahrplanjahr", fahrplanjahr));

		PaginatedList<Baustelle> result = findPageByCriteria(criteria, start, count, preloads);

		return result;
	}
}