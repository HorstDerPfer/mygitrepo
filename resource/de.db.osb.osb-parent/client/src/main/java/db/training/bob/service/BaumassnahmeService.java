package db.training.bob.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.SearchBean;
import db.training.bob.web.statistics.MethodsPerLocationReport;
import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;

public interface BaumassnahmeService extends BasicService<Baumassnahme, Serializable> {

	void create(Baumassnahme baumassnahme) throws RuntimeException;

	List<Baumassnahme> findBySearchBean(SearchBean searchBean, FetchPlan[] fetchPlans);
	
	PaginatedList<Baumassnahme> findBySearchBean(List<Order> sortOrders, SearchBean searchBean,
	    Integer start, Integer count, FetchPlan[] fetchPlans);

	MethodsPerLocationReport findMethodsPerLocation(SearchBean searchBean, FetchPlan[] fetchPlans);

	PaginatedList<Baumassnahme> findBySearchBean(List<Order> sortOrders, SearchBean searchBean,
	    Integer start, Integer count, Preload[] preloads);

	Map<String, Integer> findReasonsForChanges(List<Baumassnahme> baumassnahmen);

	Map<String, Integer> findAusfallgruende(List<Baumassnahme> baumassnahmen);

	List<Baumassnahme> findByBearbeiter(Integer bearbeiterId);

	/**
	 * listet alle Fahrplanjahre auf, zu denen Baumaßnahmen gespeichert sind, und gibt diese zurück.
	 * 
	 * @return Liste mit Jahreszahlen der verfügbaren Fahrplanjahre
	 */
	List<Integer> findAvailableFahrplanjahre();

	Integer countBySearchBean(SearchBean searchBean, FetchPlan[] fetchPlans);

	Integer countBySearchBean(List<Order> sortOrders, SearchBean searchBean, Integer start,
	    Integer count, FetchPlan[] fetchPlans);
}
