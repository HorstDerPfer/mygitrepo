package db.training.osb.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.logwrapper.Logger;
import db.training.osb.dao.UmleitungDao;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;

public class UmleitungServiceImpl extends EasyServiceImpl<Umleitung, Serializable> implements
    UmleitungService {

	private static Logger log = Logger.getLogger(UmleitungServiceImpl.class);

	public UmleitungServiceImpl() {
		super(Umleitung.class);
	}

	public UmleitungDao getDao() {
		return (UmleitungDao) getBasicDao();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public PaginatedList<Umleitung> findPaginatedUnlinkedByFahrplanregelungAndFahrplanjahr(
	    Fahrplanregelung fahrplanregelung, Integer start, Integer count, int fahrplanjahr,
	    FetchPlan[] fetchPlans) {
		PaginatedList<Umleitung> umleitungList = new PaginatedList<Umleitung>();
		List<Umleitung> umleitungen = new ArrayList<Umleitung>();

		Calendar firstDayOfYear = new GregorianCalendar(fahrplanjahr, 0, 1, 0, 0, 0);
		Calendar lastDayOfYear = new GregorianCalendar(fahrplanjahr, 11, 31, 23, 59, 59);

		String sql = "select distinct id from umleitung where gueltig_von <= to_date('"
		    + FrontendHelper.castDateToString(lastDayOfYear.getTime(), "yyyy-MM-dd")
		    + "', 'yyyy-MM-dd') and gueltig_bis >= to_date('"
		    + FrontendHelper.castDateToString(firstDayOfYear.getTime(), "yyyy-MM-dd")
		    + "', 'yyyy-MM-dd') and id not in (select umleitung_ID from umleitung_fahrplanregelung where fahrplanregelung_ID = "
		    + fahrplanregelung.getId() + ")";

		List<Integer> ids = getCurrentSession().createSQLQuery(sql).list();

		if (log.isDebugEnabled()) {
			log.debug("findPaginatedAll: start=" + start + ", count=" + count);
			log.debug(ids.size() + " Umleitungen gefunden. IDs:" + ids);
		}

		umleitungList.setFullListSize(ids.size());

		if (start != null && count != null) {
			int c = ((umleitungList.getFullListSize() > (start + count)) ? (start + count)
			    : umleitungList.getFullListSize());
			ids = new ArrayList<Integer>(ids).subList(start, c);
		}

		if (ids.size() > 0) {
			Criteria criteria = getCurrentSession().createCriteria(Umleitung.class);
			criteria.add(Restrictions.in("id", ids));
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			umleitungen = criteria.list();
		}

		// falls Relation noch nicht gespeichert -> Relation berechnen
		for (Umleitung umleitung : umleitungen)
			if (umleitung.getRelationString() == null)
				Hibernate.initialize(umleitung.getUmleitungswege());

		if (fetchPlans != null)
			fill(umleitungen, fetchPlans);

		umleitungList.setList(umleitungen);

		return umleitungList;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public PaginatedList<Umleitung> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCriteria, Integer start, Integer count, FetchPlan[] fetchPlans) {

		Criteria criteria = getCurrentSession().createCriteria(Umleitung.class);
		addCriteriasToUmleitung(criteria, sortOrders, searchCriteria);
		return findPageByCriteria(criteria, start, count, fetchPlans);
	}

	/**
	 * Fügt die übergebenen Suchfilter und Sortierung an die angegebene Criteria an.
	 * 
	 * @param criteria
	 *            - Criteria, an welche die übergebenen Filter an Umleitung angehängt werden sollen
	 * @param sortOrders
	 *            - Sortierung
	 * @param searchCriteria
	 *            - Suchfilter
	 */
	private void addCriteriasToUmleitung(Criteria criteria, List<Order> sortOrders,
	    Map<String, Object> searchCriteria) {

		/*
		 * Suchkriterien hinzufügen
		 */

		Criteria umleitungsWegCrit = null;

		// Name
		Object name = searchCriteria.get(UmleitungService.NAME);
		if (name != null) {
			criteria.add(Restrictions.ilike(UmleitungService.NAME, (String) name,
			    MatchMode.ANYWHERE));
		}
		// Streckennummer
		Object strecke = searchCriteria.get(UmleitungService.VZG_STRECKE);
		if (strecke != null) {
			Integer streckenNr = (Integer) strecke;
			umleitungsWegCrit = criteria.createCriteria("umleitungswege", "umleitung");
			Criteria streckeCrit = umleitungsWegCrit.createCriteria("umleitung.vzgStrecke",
			    "strecke");
			streckeCrit.add(Restrictions.eq("strecke.nummer", streckenNr));
		}
		// Fahrplanjahr
		Object fahrplanjahr = searchCriteria.get(UmleitungService.FAHRPLANJAHR);
		if (fahrplanjahr != null) {
			Calendar firstDayOfYear = new GregorianCalendar((Integer) fahrplanjahr, 0, 1, 0, 0, 0);
			Calendar lastDayOfYear = new GregorianCalendar((Integer) fahrplanjahr, 11, 31, 23, 59,
			    59);
			criteria.add(Restrictions.le("gueltigVon", lastDayOfYear.getTime()));
			criteria.add(Restrictions.ge("gueltigBis", firstDayOfYear.getTime()));
		}
		// Betriebstelle
		/*
		 * 'Betriebstelle von' und 'Betriebstelle bis' werden beide abgefragt. Der Benzter kann
		 * jedoch nur eine Betriebsstelle in der Suchmaske angeben.
		 */
		Object betriebsSt = searchCriteria.get(UmleitungService.BETRIEBSTELLE);

		if (betriebsSt != null) {
			String betriebstelle = (String) betriebsSt;
			if (umleitungsWegCrit == null)
				umleitungsWegCrit = criteria.createCriteria("umleitungswege", "umleitung");
			umleitungsWegCrit.createAlias("betriebsstelleVon", "von");
			umleitungsWegCrit.createAlias("betriebsstelleBis", "bis");

			Junction d = Restrictions.disjunction();
			{
				d.add(Restrictions.ilike("von.kuerzel", betriebstelle, MatchMode.ANYWHERE));
				d.add(Restrictions.ilike("von.name", betriebstelle, MatchMode.ANYWHERE));
				d.add(Restrictions.ilike("bis.kuerzel", betriebstelle, MatchMode.ANYWHERE));
				d.add(Restrictions.ilike("bis.name", betriebstelle, MatchMode.ANYWHERE));
			}
			umleitungsWegCrit.add(d);
		}

		// Regionalbereich liegt hier:
		// Umleitung
		// -> UmleitungFahrplanregelungLink
		// -> Fahrplanregelung
		// -> Regionalbereich (ID)
		Object rbBereich = searchCriteria.get(UmleitungService.RB_BEREICH);
		if (rbBereich != null && !rbBereich.equals(0)) {
			Integer regionalBereichId = (Integer) rbBereich;
			Criteria umleitungLinkCrit = criteria.createCriteria("umleitungFahrplanregelungLinks",
			    "links");
			Criteria fahrplanregelCrit = umleitungLinkCrit.createCriteria("links.fahrplanregelung",
			    "fahrplan");
			Criteria regionalbereichCrit = fahrplanregelCrit.createCriteria(
			    "fahrplan.regionalbereich", "region");
			regionalbereichCrit.add(Restrictions.eq("region.id", regionalBereichId));
		}

		/*
		 * Sortierung hinzufügen
		 */
		if (sortOrders != null) {
			for (Order order : sortOrders) {
				criteria.addOrder(order);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public PaginatedList<Umleitung> findPaginatedAll(Integer start, Integer count,
	    FetchPlan[] fetchPlans) {

		Criteria criteria = getCurrentSession().createCriteria(Umleitung.class);

		PaginatedList<Umleitung> umleitungList = findPageByCriteria(criteria, start, count,
		    fetchPlans);

		// falls Relation noch nicht gespeichert -> Relation berechnen
		if (umleitungList != null) {
			for (Umleitung umleitung : umleitungList.getList()) {
				if (umleitung.getRelationString() == null) {
					Hibernate.initialize(umleitung.getUmleitungswege());
				}
			}
		}

		return umleitungList;
	}

	@Override
	public void fill(Umleitung umleitung, Collection<FetchPlan> plans) {
		if (umleitung != null && plans != null && !plans.isEmpty()) {

			if (plans.contains(FetchPlan.OSB_UML_UMLEITUNGSWEGE))
				Hibernate.initialize(umleitung.getUmleitungswege());
			if (plans.contains(FetchPlan.OSB_UML_FAHRPLANREGELUNGEN)) {
				Hibernate.initialize(umleitung.getUmleitungFahrplanregelungLinks());
				for (UmleitungFahrplanregelungLink ufLink : umleitung
				    .getUmleitungFahrplanregelungLinks()) {
					Hibernate.initialize(ufLink.getFahrplanregelung());
					Hibernate.initialize(ufLink.getFahrplanregelung().getRegionalbereich());
				}
			}
		}
	}
}