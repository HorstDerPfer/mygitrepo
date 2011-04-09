package db.training.bob.service.report;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.SearchBean;
import db.training.bob.service.FetchPlan;

public interface BbzrVerspaetungsminutenReportService {

	/**
	 * Sucht eine Liste von ID's von Baumassnahmen zusammen, die den Suchkriterien
	 * der übergebenen SearchBean entsprechen.
	 */
	@Transactional
	public abstract List<Integer> findIDsBySearchBean(SearchBean searchBean, FetchPlan[] fetchPlans);

	/**
	 * Sucht eine Liste von ID's von Baumassnahmen zusammen, die den Suchkriterien
	 * der übergebenen SearchBean entsprechen.
	 */
	@Transactional
	public abstract List<Integer> findIDsBySearchBean(List<Order> sortOrders,
			SearchBean searchBean, Integer start, Integer count, FetchPlan[] fetchPlans);

	/**
	 * Sucht eine Liste von ID's von Baumassnahmen zusammen, die den Suchkriterien
	 * der übergebenen SearchBean entsprechen, allerdings nur seitenweise.
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public abstract List<Integer> findPageIDsByCriteria(Criteria criteria,
			Integer start, Integer count, FetchPlan[] fetchPlans);
	
	/**
	 * Baut einen Report zusammen, der BbzrVerspaetungsminuten auf bestimmten Kategorien von Zügen 
	 * anwendet.
	 * 
	 * @param bbps Die Liste der zu betrachtenden Baumassnahmen
	 * @param zeitraumVerkehrstagVon Betrachtungszeitraum von
	 * @param zeitraumVerkehrstagBis Betrachtungszeitraum bis
	 * @param qsks
	 * @return
	 */
	@Transactional
	public Report buildBbzrVerspaetungsminutenReport(List<Integer> bbps, Date zeitraumVerkehrstagVon,
			Date zeitraumVerkehrstagBis, Integer qsks);

}