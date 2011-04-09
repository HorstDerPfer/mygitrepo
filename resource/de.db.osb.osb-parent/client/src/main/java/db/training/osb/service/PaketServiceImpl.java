package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.logwrapper.Logger;
import db.training.osb.dao.PaketDao;
import db.training.osb.model.Paket;

public class PaketServiceImpl extends EasyServiceImpl<Paket, Serializable> implements PaketService {

	private static Logger log = Logger.getLogger(PaketServiceImpl.class);

	public PaketServiceImpl() {
		super(Paket.class);
	}

	public PaketDao getDao() {
		return (PaketDao) getBasicDao();
	}

	@Transactional
	public Paket findByRbFahrplanjahrAndLfdNr(Regionalbereich regionalbereich,
	    Integer fahrplanjahr, Integer lfdNr) {
		Criteria criteria = getCurrentSession().createCriteria(Paket.class);
		criteria.add(Restrictions.eq("regionalbereich", regionalbereich));
		criteria.add(Restrictions.eq("jahr", fahrplanjahr));
		criteria.add(Restrictions.eq("lfdNr", lfdNr));
		Paket paket = getDao().findUniqueByCriteria(criteria);
		return paket;
	}

	@Transactional
	public List<Paket> findByKeywords(Regionalbereich regionalbereich, Integer fahrplanjahrId,
	    Integer lfdNrId) {
		List<Paket> paketListe = null;
		if (regionalbereich == null && fahrplanjahrId == null && lfdNrId == null)
			return null;

		Criteria criteria = getCurrentSession().createCriteria(Paket.class);

		if (regionalbereich != null)
			criteria.add(Restrictions.eq("regionalbereich", regionalbereich));

		if (fahrplanjahrId != null && fahrplanjahrId.intValue() >= 0)
			criteria.add(Restrictions.eq("jahr", fahrplanjahrId));

		if (lfdNrId != null && lfdNrId.intValue() >= 0)
			criteria.add(Restrictions.eq("lfdNr", lfdNrId));

		paketListe = getDao().findByCriteria(criteria);

		return paketListe;
	}

	@Transactional
	public PaginatedList<Paket> findPaginatedAll(Integer start, Integer count, int fahrplanjahr) {

		List<Order> sortOrders = new LinkedList<Order>();
		sortOrders.add(Order.asc("kurzname"));

		return findPaginatedAllAndSort(sortOrders, start, count, fahrplanjahr, null);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public PaginatedList<Paket> findPaginatedAllAndSort(List<Order> sortOrders, Integer start,
	    Integer count, int fahrplanjahr, FetchPlan[] fetchPlans) {

		Criteria criteria = getCurrentSession().createCriteria(Paket.class);
		criteria.add(Restrictions.eq("fahrplanjahr", fahrplanjahr));
		criteria = applyOrders(criteria, sortOrders);
		return findPageByCriteria(criteria, start, count, fetchPlans);
	}

	private Criteria applyOrders(Criteria criteria, Collection<Order> sortOrders) {
		if (criteria != null && sortOrders != null) {
			for (Order order : sortOrders) {
				criteria.addOrder(order);
			}
		}

		return criteria;
	}

	@Override
	public void fill(Paket obj, Collection<FetchPlan> plans) {

		Hibernate.initialize(obj);

		if (plans.contains(FetchPlan.OSB_PAKET_MASSNAHMEN)) {
			Hibernate.initialize(obj.getMassnahmen());
		}
	}
}