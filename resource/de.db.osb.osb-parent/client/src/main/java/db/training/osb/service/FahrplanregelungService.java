package db.training.osb.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;

public interface FahrplanregelungService extends BasicService<Fahrplanregelung, Serializable> {

	public List<Fahrplanregelung> findByBuendelId(Integer buendelId, Preload[] preloads);

	public List<Fahrplanregelung> findUnlinkedByBuendelIdAndFahrplanjahr(Integer buendelId,
	    int ahrplanjahr, FetchPlan[] fetchPlans);

	public Integer findNextLfdNr(Regionalbereich rb, int jahr);

	public void attachBuendel(Fahrplanregelung fpl, Buendel b);

	/* Suchkriterien */
	public static final String NAME = "name";

	// public static final String BETRIEBSTELLEVON = "betriebstelleVon";
	public static final String BETRIEBSTELLEVON = "betriebsstelleVon";

	// public static final String BETRIEBSTELLEBIS = "betriebstelleBis";
	public static final String BETRIEBSTELLEBIS = "betriebsstelleBis";

	public static final String FAHRPLANJAHR = "fahrplanjahr"; // fahrplanjahr

	public static final String REGION = "regionalbereich"; // region

	public static final String NUMMER = "nummer";

	public static final String BUENDEL = "buendel";

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
	public PaginatedList<Fahrplanregelung> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCritieria, Integer start, Integer count, FetchPlan[] fetchPlans);

}