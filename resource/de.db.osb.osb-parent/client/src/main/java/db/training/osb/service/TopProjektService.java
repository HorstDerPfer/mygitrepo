package db.training.osb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;

public interface TopProjektService extends BasicService<TopProjekt, Serializable> {

	/* Suchkriterien */
	public static final String ANMELDER = "anmelder";

	public static final String NAME = "name";

	public static final String REGION = "regionalbereich";

	public static final String SAP_NUMMER = "sap_nummer";

	/**
	 * @param sortOrders
	 *            Sortierung
	 * @param searchCriteria
	 *            Suchparameter
	 * @param start
	 *            Index des ersten Suchergebnisses
	 * @param count
	 *            max. Anzahl zurückgegebener Ergebnisse
	 * @param fetchPlans
	 */
	public PaginatedList<TopProjekt> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCritieria, Integer start, Integer count, Preload[] preloads,
	    int fahrplanjahr);

	/**
	 * Sucht TopProjekte anhand der VzgStrecke der untergeordneten SAPMassnahmen
	 * 
	 * @param vzgStrecke
	 * @param preloads
	 * @return topProjekte
	 */
	public List<TopProjekt> findByVzgStrecke(VzgStrecke vzgStrecke, Preload[] preloads);

}