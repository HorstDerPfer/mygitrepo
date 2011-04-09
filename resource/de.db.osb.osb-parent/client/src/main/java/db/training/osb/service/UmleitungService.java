package db.training.osb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Umleitung;

public interface UmleitungService extends BasicService<Umleitung, Serializable> {

	/* Suchkriterien */
	public static final String NAME = "name";

	public static final String BETRIEBSTELLE = "betriebsstelle";

	public static final String VZG_STRECKE = "vzgStrecke";

	public static final String RB_BEREICH = "regionalBereich";

	public static final String FAHRPLANJAHR = "fahrplanjahr";

	public PaginatedList<Umleitung> findPaginatedUnlinkedByFahrplanregelungAndFahrplanjahr(
	    Fahrplanregelung fahrplanregelung, Integer start, Integer count, int fahrplanjahr,
	    FetchPlan[] fetchPlans);

	public PaginatedList<Umleitung> findPaginatedAll(Integer start, Integer count,
	    FetchPlan[] fetchPlans);

	public PaginatedList<Umleitung> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCriteria, Integer start, Integer count, FetchPlan[] fetchPlans);

}