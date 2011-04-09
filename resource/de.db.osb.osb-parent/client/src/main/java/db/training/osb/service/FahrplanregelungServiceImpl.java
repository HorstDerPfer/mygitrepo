package db.training.osb.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.dao.EasySessionFactory;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.logwrapper.Logger;
import db.training.osb.dao.FahrplanregelungDao;
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;

public class FahrplanregelungServiceImpl extends EasyServiceImpl<Fahrplanregelung, Serializable>
    implements FahrplanregelungService {

	private static Logger log = Logger.getLogger(FahrplanregelungServiceImpl.class);

	public FahrplanregelungServiceImpl() {
		super(Fahrplanregelung.class);
	}

	public FahrplanregelungDao getDao() {
		return (FahrplanregelungDao) getBasicDao();
	}

	@Override
	public void fill(Fahrplanregelung fpl, Collection<FetchPlan> plans) {

		Hibernate.initialize(fpl);

		if (plans.contains(FetchPlan.OSB_FPL_BUENDEL)) {
			Hibernate.initialize(fpl.getBuendel());

			BuendelService service = EasyServiceFactory.getInstance().createBuendelService();
			service.fill(fpl.getBuendel(), plans);
		}

		if (plans.contains(FetchPlan.OSB_FPL_REGIONALBEREICH)) {
			Hibernate.initialize(fpl.getRegionalbereich());

			RegionalbereichService service = EasyServiceFactory.getInstance()
			    .createRegionalbereichService();
			service.fill(fpl.getRegionalbereich(), plans);
		}

		if (plans.contains(FetchPlan.OSB_FPL_UMLEITUNGEN)) {
			Hibernate.initialize(fpl.getUmleitungFahrplanregelungLinks());

			UmleitungFahrplanregelungLinkService service = EasyServiceFactory.getInstance()
			    .createUmleitungFahrplanregelungLinkService();
			service.fill(fpl.getUmleitungFahrplanregelungLinks(), plans);
		}

		if (plans.contains(FetchPlan.OSB_FPL_BST_VON)) {
			Hibernate.initialize(fpl.getBetriebsstelleVon());
			Hibernate.initialize(fpl.getBetriebsstelleVonSPFV());
			Hibernate.initialize(fpl.getBetriebsstelleVonSPNV());
			Hibernate.initialize(fpl.getBetriebsstelleVonSGV());
		}

		if (plans.contains(FetchPlan.OSB_FPL_BST_BIS)) {
			Hibernate.initialize(fpl.getBetriebsstelleBis());
			Hibernate.initialize(fpl.getBetriebsstelleBisSPFV());
			Hibernate.initialize(fpl.getBetriebsstelleBisSPNV());
			Hibernate.initialize(fpl.getBetriebsstelleBisSGV());
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Fahrplanregelung> findByBuendelId(Integer buendelId, Preload[] preloads) {
		List<Fahrplanregelung> fahrplanregelungen = null;
		Criteria criteria = getCurrentSession().createCriteria(Fahrplanregelung.class);
		criteria.createAlias("buendel", "b", CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.eq("b.id", buendelId));
		PreloadEventListener.setPreloads(preloads);
		fahrplanregelungen = criteria.setResultTransformer(
		    CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
		PreloadEventListener.clearPreloads();
		return fahrplanregelungen;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Fahrplanregelung> findUnlinkedByBuendelIdAndFahrplanjahr(Integer buendelId,
	    int fahrplanjahr, FetchPlan[] fetchPlans) {
		List<Fahrplanregelung> unlinkedFahrplanregelungenByBuendel = new ArrayList<Fahrplanregelung>();
		SQLQuery qry = getCurrentSession()
		    .createSQLQuery(
		        "select distinct ID from fahrplanregelung where fahrplanjahr = "
		            + fahrplanjahr
		            + " and ID not in (select fahrplanregelung_ID from buendel_fahrplanregelung where buendel_ID = "
		            + buendelId + ") order by ID;");
		List<Integer> results = qry.list();
		for (Integer fahrplanregelungId : results)
			unlinkedFahrplanregelungenByBuendel.add(findById(fahrplanregelungId, fetchPlans));
		return unlinkedFahrplanregelungenByBuendel;
	}

	@Transactional
	public Integer findNextLfdNr(Regionalbereich rb, int jahr) {
		Query qry = getCurrentSession()
		    .createQuery(
		        "select max(nummer) from Fahrplanregelung b where regionalbereich = :rb and fahrplanjahr = :jahr")
		    .setEntity("rb", rb).setInteger("jahr", jahr);
		Integer result = (Integer) qry.uniqueResult();
		if (result == null) {
			result = -1;
		}
		return result + 1;
	}

	@Transactional
	public void attachBuendel(Fahrplanregelung fpl, Buendel b) {

		Session session = EasySessionFactory.getInstance().getCurrentSession();

		Fahrplanregelung fplMerged = (Fahrplanregelung) session.merge(fpl);
		Buendel buendelMerged = (Buendel) session.merge(b);
		fplMerged.getBuendel().add(buendelMerged);
		getDao().update(fplMerged);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public PaginatedList<Fahrplanregelung> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCriteria, Integer start, Integer count, FetchPlan[] fetchPlans) {

		Criteria criteria = getCurrentSession().createCriteria(Fahrplanregelung.class);
		addCriteriasToFahrplanregelung(criteria, sortOrders, searchCriteria);
		return findPageByCriteria(criteria, start, count, fetchPlans);
	}

	/**
	 * Fügt die übergebenen Suchfilter und Sortierung an die angegebene Criteria an.
	 * 
	 * @param criteria
	 *            - Criteria, an welche die übergebenen Filter an Fahrplanregelung angehängt werden
	 *            sollen
	 * @param sortOrders
	 *            - Sortierung
	 * @param searchCriteria
	 *            - Suchfilter
	 */
	private void addCriteriasToFahrplanregelung(Criteria criteria, List<Order> sortOrders,
	    Map<String, Object> searchCriteria) {

		/*
		 * Suchkriterien hinzufügen
		 */
		// Name
		Object fahrplanregelungName = searchCriteria.get(FahrplanregelungService.NAME);
		if (fahrplanregelungName != null) {
			criteria.add(Restrictions.ilike(FahrplanregelungService.NAME,
			    (String) fahrplanregelungName, MatchMode.ANYWHERE));
		}
		// Nummer
		Object nummer = searchCriteria.get(FahrplanregelungService.NUMMER);
		if (nummer != null) {
			criteria.add(Restrictions.eq(FahrplanregelungService.NUMMER, nummer));
		}

		// Region
		Object region = searchCriteria.get(FahrplanregelungService.REGION);
		if (region != null) {
			Integer regionId = (Integer) region;
			Criteria regionCrit = criteria.createCriteria(FahrplanregelungService.REGION, "region");
			regionCrit.add(Restrictions.eq("region.id", regionId));
		}
		// Fahrplanjahr
		Object fahrplanjahr = searchCriteria.get(FahrplanregelungService.FAHRPLANJAHR);
		if (fahrplanjahr != null) {
			criteria.add(Restrictions.eq(FahrplanregelungService.FAHRPLANJAHR, fahrplanjahr));
		}
		// Buendel
		if (searchCriteria.containsKey(FahrplanregelungService.BUENDEL)) {
			searchCriteria.remove(FahrplanregelungService.BUENDEL);
			// Nur Fahrplanregelungen anzeigen, welche keinem Buendel zugeordnet sind
			criteria.add(Restrictions.isEmpty("buendel"));
		}

		/*
		 * Sortierung hinzufügen
		 */
		if (sortOrders != null) {
			for (Order order : sortOrders) {
				// da sich die fahrplanregelungId Id zusammensetzt, und kein eigenen Property
				// des Entities Fahrplanregelung ist, muss die Sortierung hier explizit
				// angegeben werden
				if (order.toString().startsWith("fahrplanregelungId")) {
					if (order.toString().endsWith("asc")) {
						criteria.addOrder(Order.asc("regionalbereich"));
						criteria.addOrder(Order.asc("fahrplanjahr"));
						criteria.addOrder(Order.asc("nummer"));
					} else {
						criteria.addOrder(Order.desc("regionalbereich"));
						criteria.addOrder(Order.desc("fahrplanjahr"));
						criteria.addOrder(Order.desc("nummer"));
					}
				} else {
					criteria.addOrder(order);
				}
			}
		}
	}
	
	/**
	 * findet die naechst hoehere lfd Nummer und speichert die übergebene Fahrplanregelung
	 * @param fplr
	 *            - die zu speichernde Fahrplanregelung
	 */
	@Transactional(readOnly = false)
	public void create(Fahrplanregelung fplr) throws RuntimeException {
		synchronized (this) {
			Integer nummer = findNextLfdNr(fplr.getRegionalbereich(), fplr.getFahrplanjahr());
			fplr.setNummer(nummer);
			getDao().save(fplr);
		}
	}

}