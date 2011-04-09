package db.training.osb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.osb.model.Buendel;
import db.training.osb.model.VzgStrecke;

public interface BuendelService extends BasicService<Buendel, Serializable> {

	public Integer findNextLfdNr(Regionalbereich rb, int jahr);

	public void createBuendelOnPaketeByVzgStrecke(VzgStrecke strecke);

	/* Suchkriterien */
	public static final String BUENDEL_NAME = "buendelName"; // buendelname

	public static final String BUENDEL_ID = "buendelId"; // id

	public static final String BUENDEL_BST_VON = "buendelBstVon"; // betriebsstelleVon

	public static final String BUENDEL_BST_BIS = "buendelBstBis"; // betriebsstelleBis

	public static final String VZG_STRECKE = "hauptStrecke"; // strecke

	public static final String FAHRPLANJAHR = "fahrplanjahr"; // fahrplanjahr

	public static final String REGION = "regionalbereich"; // region

	public static final String LFD_NR = "lfdNr"; // laufende Nummer

	/**
	 * Sucht Buendel mit den angegebenen Suchkritrien und der angegebenen range.
	 * 
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
	public PaginatedList<Buendel> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCritieria, Integer start, Integer count);

}