package db.training.osb.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.osb.dao.TopProjektDao;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;

public class TopProjektServiceImpl extends EasyServiceImpl<TopProjekt, Serializable> implements
    TopProjektService {

	public TopProjektServiceImpl() {
		super(TopProjekt.class);
	}

	public TopProjektDao getDao() {
		return (TopProjektDao) getBasicDao();
	}

	@Transactional
	public List<TopProjekt> findByVzgStrecke(VzgStrecke vzgStrecke, Preload[] preloads) {
		List<TopProjekt> result = new ArrayList<TopProjekt>();

		PreloadEventListener.setPreloads(preloads);
		result = getDao().findByVzgStrecke(vzgStrecke);
		PreloadEventListener.clearPreloads();

		return result;
	}

	@Transactional
	public PaginatedList<TopProjekt> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCritieria, Integer start, Integer count, Preload[] preloads,
	    int fahrplanjahr) {

		// Criteria füllen
		Criteria criteria = getCurrentSession().createCriteria(TopProjekt.class);

		if (searchCritieria.containsKey(SAP_NUMMER)) {
			criteria.add(Restrictions.ilike("sapProjektNummer",
			    (String) searchCritieria.get(SAP_NUMMER), MatchMode.ANYWHERE));
		}

		criteria.add(Restrictions.eq("fahrplanjahr", fahrplanjahr));
		criteria = applyOrders(criteria, sortOrders);

		PaginatedList<TopProjekt> result = findPageByCriteria(criteria, start, count, preloads);

		return result;
	}

	private Criteria applyOrders(Criteria criteria, Collection<Order> sortOrders) {
		if (criteria != null && sortOrders != null) {
			for (Order order : sortOrders) {
				criteria.addOrder(order);
			}
		}

		return criteria;
	}
}