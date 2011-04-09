package db.training.osb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.Baustelle;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;

public interface GleissperrungService extends BasicService<Gleissperrung, Serializable> {

	/**
	 * @deprecated stattdessen {@link GleissperrungService#findByIds(Integer[], Preload[])} benutzen
	 * @param ids
	 * @param fetchPlans
	 * @return
	 */
	@Deprecated
	List<Gleissperrung> findByIds(Integer[] ids, FetchPlan[] fetchPlans);

	List<Gleissperrung> findByIds(Integer[] ids, Preload[] preloads);

	PaginatedList<Gleissperrung> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCriteria, Integer start, Integer count, Preload[] preloads,
	    int fahrplanjahr);

	void attachBaustelle(Gleissperrung gs, Baustelle b);

	Integer findNextLfdNr(Integer fahrplanjahr);

	List<Gleissperrung> findByMassnahme(SAPMassnahme massnahme, Preload[] preloads);
}