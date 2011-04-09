package db.training.osb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.Baustelle;

public interface BaustelleService extends BasicService<Baustelle, Serializable> {

	/* Suchkriterien */
	public static final String NAME = "name";

	/**
	 * @param sortOrders
	 *            Sortierung
	 * @param searchCriteria
	 *            Suchparameter
	 * @param start
	 *            Index des ersten Suchergebnisses
	 * @param count
	 *            max. Anzahl zurückgegebener Ergebnisse
	 * @param preloads
	 */
	public PaginatedList<Baustelle> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCritieria, Integer start, Integer count, Preload[] preloads,
	    int fahrplanjahr);
}