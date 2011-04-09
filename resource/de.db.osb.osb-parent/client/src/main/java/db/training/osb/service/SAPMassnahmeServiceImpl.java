package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.RegionalbereichDaoImpl;
import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.dao.EasyDaoFactory;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.osb.dao.SAPMassnahmeDao;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.web.sperrpausenbedarf.SperrpausenbedarfListReport;

public class SAPMassnahmeServiceImpl extends EasyServiceImpl<SAPMassnahme, Serializable> implements
    SAPMassnahmeService {

	private static Logger log = Logger.getLogger(SAPMassnahmeServiceImpl.class);

	private class CriteriaHelper {
		private Criteria criteria;

		private Dictionary<String, String> aliases;

		public CriteriaHelper(Criteria criteria) {
			this.criteria = criteria;
			aliases = new Hashtable<String, String>();
		}

		public Criteria getCriteria() {
			return this.criteria;
		}

		public void setCriteria(Criteria criteria) {
			this.criteria = criteria;
		}

		public Dictionary<String, String> getAliases() {
			return this.aliases;
		}
	}

	public SAPMassnahmeServiceImpl() {
		super(SAPMassnahme.class);
	}

	public SAPMassnahmeDao getDao() {
		return (SAPMassnahmeDao) getBasicDao();
	}

	@Override
	@Transactional(readOnly = false)
	public void create(SAPMassnahme mn) throws RuntimeException {
		Session session = getCurrentSession();

		synchronized (this) {
			Query qry = session.createQuery("select max(mn.lfdNr)+1 from SAPMassnahme mn");
			Integer nextLfdNr = (Integer) qry.uniqueResult();

			if (nextLfdNr == null) {
				if (log.isDebugEnabled())
					log.debug("keine neue LfdNr gefunden.");
				throw new NoResultException("keine neue lfd.Nr. gefunden.");
			}

			mn.setLfdNr(nextLfdNr);
			getDao().save(mn);
		}
	}

	@Override
	public void fill(SAPMassnahme bm, Collection<FetchPlan> plans) {
		Hibernate.initialize(bm);

		if (plans.contains(FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH)
		    || plans.contains(FetchPlan.OSB_REGIONALBEREICH)) {
			Hibernate.initialize(bm.getRegionalbereich());
			if (plans
			    .contains(db.training.bob.service.FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH))
				Hibernate.initialize(bm.getRegionalbereich().getBearbeitungsbereiche());
		}

		if (plans.contains(FetchPlan.OSB_PAKET)) {
			Hibernate.initialize(bm.getPaket());
		}

		if (plans.contains(FetchPlan.OSB_WEITERE_STRECKEN)) {
			Hibernate.initialize(bm.getWeitereStrecken());
		}

		if (plans.contains(FetchPlan.OSB_BST_VON)) {
			Hibernate.initialize(bm.getBetriebsstelleVon());
		}
		if (plans.contains(FetchPlan.OSB_BST_BIS)) {
			Hibernate.initialize(bm.getBetriebsstelleBis());
		}

		if (plans.contains(FetchPlan.OSB_GLEISSPERRUNG)) {
			Hibernate.initialize(bm.getGleissperrungen());
			EasyServiceFactory.getInstance().createGleissperrungService().fill(
			    bm.getGleissperrungen(), plans);
		}

		if (plans.contains(FetchPlan.OSB_LANGSAMFAHRSTELLE)) {
			Hibernate.initialize(bm.getLangsamfahrstellen());
			EasyServiceFactory.getInstance().createLangsamfahrstelleService().fill(
			    bm.getLangsamfahrstellen(), plans);
		}

		if (plans.contains(FetchPlan.OSB_OBERLEITUNG)) {
			Hibernate.initialize(bm.getOberleitungen());
			EasyServiceFactory.getInstance().createOberleitungService().fill(bm.getOberleitungen(),
			    plans);
		}

		if (plans.contains(FetchPlan.OSB_TOPPROJEKT)) {
			EasyServiceFactory.getInstance().createTopProjektService().fill(bm.getTopProjekte(),
			    plans);
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<SAPMassnahme> findByVzgStrecke(VzgStrecke streckeVzg, FetchPlan[] fetchPlans) {
		assert (streckeVzg != null);

		Criteria criteria = getCurrentSession().createCriteria(SAPMassnahme.class);
		criteria.createAlias("gleissperrungen", "gl", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("gl.vzgStrecke", "gl_st", CriteriaSpecification.LEFT_JOIN);
		Junction dis = Restrictions.disjunction();
		dis.add(Restrictions.eq("gl_st.nummer", streckeVzg.getNummer()));
		// dis.add(Restrictions.eq("weitereStrecken", streckeVzg));
		criteria.add(dis);

		List<SAPMassnahme> result = criteria.list();
		fill(result, fetchPlans);

		return result;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<SAPMassnahme> findByRegionalbereich(Regionalbereich regionalbereich,
	    FetchPlan[] fetchPlans) {
		Criteria criteria = getCurrentSession().createCriteria(SAPMassnahme.class);
		criteria.add(Restrictions.eq("regionalbereich", regionalbereich));
		List<SAPMassnahme> result = criteria.list();
		fill(result, fetchPlans);
		return result;
	}

	@Transactional
	public List<SAPMassnahme> findByIds(Integer[] ids, FetchPlan[] fetchPlans) {
		List<SAPMassnahme> result = getDao().findByCriteria(Restrictions.in("id", ids));
		fill(result, fetchPlans);
		return result;
	}

	@Transactional
	public List<SAPMassnahme> findByKeywords(Integer regionalbereichId, String anmelder,
	    String gewerk, String untergewerk, Integer hauptStreckeNummer, Integer paketId,
	    String sapProjektnummer, FetchPlan[] fetchPlans, int fahrplanjahr) {

		return findPaginatedByKeywords(regionalbereichId, anmelder, gewerk, untergewerk,
		    hauptStreckeNummer, paketId, sapProjektnummer, fetchPlans, null, null, fahrplanjahr)
		    .getList();
	}

	@Transactional
	public List<SAPMassnahme> findByFahrplanjahr(int fahrplanjahr, FetchPlan[] fetchPlans) {
		List<SAPMassnahme> result = getDao().findByCriteria(
		    Restrictions.eq("fahrplanjahr", fahrplanjahr));
		fill(result, fetchPlans);
		return result;
	}

	@Transactional
	public PaginatedList<SAPMassnahme> findPaginatedByKeywords(Integer regionalbereichId,
	    String anmelder, String gewerk, String untergewerk, Integer hauptStreckeNummer,
	    Integer paketId, String sapProjektnummer, FetchPlan[] fetchPlans, Integer start,
	    Integer count, int fahrplanjahr) {

		List<Order> sortOrders = new LinkedList<Order>();
		sortOrders.add(Order.asc("projektDefinitionDbBez"));

		return findPaginatedByKeywordsAndSort(sortOrders, regionalbereichId, anmelder, gewerk,
		    untergewerk, hauptStreckeNummer, paketId, sapProjektnummer, fetchPlans, start, count,
		    fahrplanjahr);

	}

	@Transactional
	@SuppressWarnings("unchecked")
	public PaginatedList<SAPMassnahme> findPaginatedByKeywordsAndSort(List<Order> sortOrders,
	    Integer regionalbereichId, String anmelder, String gewerk, String untergewerk,
	    Integer hauptStreckeNummer, Integer paketId, String sapProjektnummer,
	    FetchPlan[] fetchPlans, Integer start, Integer count, int fahrplanjahr) {

		if (log.isDebugEnabled()) {
			log.debug("Entering findByKeywordsAndSort()");
			log.debug("regionalbereichId = " + regionalbereichId);
			log.debug("sortOrders: " + sortOrders);
			log.debug("anmelder = " + anmelder);
			log.debug("gewerk = " + gewerk);
			log.debug("untergewerk = " + untergewerk);
			log.debug("hauptStreckeNummer = " + hauptStreckeNummer);
			log.debug("paketId = " + paketId);
			log.debug("sapProjektnummer = " + sapProjektnummer);
			log.debug("fetchPlans[] = " + fetchPlans);
			log.debug("start = " + start);
			log.debug("count = " + count);
		}

		CriteriaHelper criteriaHelper = createSearchCriteria(regionalbereichId, anmelder, gewerk,
		    untergewerk, hauptStreckeNummer, paketId, sapProjektnummer, fahrplanjahr);
		criteriaHelper.setCriteria(applyOrders(criteriaHelper.getCriteria(), sortOrders));

		return findPageByCriteria(criteriaHelper.getCriteria(), start, count, fetchPlans);
	}

	private CriteriaHelper createSearchCriteria(Integer regionalbereichId, String anmelder,
	    String gewerk, String untergewerk, Integer hauptStreckeNummer, Integer paketId,
	    String sapProjektnummer, int fahrplanjahr) {

		CriteriaHelper criteriaHelper = new CriteriaHelper(getCurrentSession().createCriteria(
		    SAPMassnahme.class));

		if (hauptStreckeNummer != null && hauptStreckeNummer.intValue() != 0) {
			criteriaHelper.getAliases().put("hauptStrecke", "hs");
			criteriaHelper.getCriteria().createAlias("hauptStrecke", "hs");
			criteriaHelper.getCriteria().add(Restrictions.eq("hs.nummer", hauptStreckeNummer));
			if (log.isDebugEnabled())
				log.debug("findByKeywords() hauptStreckeNummer: " + hauptStreckeNummer);
		}

		if (regionalbereichId != null && regionalbereichId.intValue() != 0) {
			criteriaHelper.getCriteria().add(
			    Restrictions.sqlRestriction("{alias}.regionalbereich_ID = ?", regionalbereichId,
			        Hibernate.INTEGER));
			if (log.isDebugEnabled())
				log.debug("findByKeywords() regionalbereichId: " + regionalbereichId);
		}

		if (paketId != null && paketId.intValue() != 0) {
			criteriaHelper.getCriteria().add(
			    Restrictions.sqlRestriction("{alias}.paket_ID = ?", paketId, Hibernate.INTEGER));
			if (log.isDebugEnabled())
				log.debug("findByKeywords() paketId: " + paketId);
		}

		if (anmelder != null && (!"".equals(anmelder.trim()))) {
			criteriaHelper.getCriteria().add(
			    Restrictions.ilike("anmelder", anmelder, MatchMode.ANYWHERE));
		}

		if (gewerk != null && (!"".equals(gewerk.trim()))) {
			criteriaHelper.getCriteria().add(
			    Restrictions.ilike("gewerk", gewerk, MatchMode.ANYWHERE));
		}
		if (untergewerk != null && (!"".equals(untergewerk.trim()))) {
			// Hier muss der Alias Name this verwendet werden, weil Hibernate sonst eine Exception
			// auslöst, wenn die Restriction zusammen mit Projections verwendet wird (Properties of
			// the root entity (Item) may be referred to without the qualifying alias or by using
			// the alias "this". Quelle: https://forum.hibernate.org/viewtopic.php?t=988049)
			criteriaHelper.getCriteria().add(
			    Restrictions.ilike("this.untergewerk", untergewerk, MatchMode.ANYWHERE));
		}
		if (sapProjektnummer != null && !sapProjektnummer.equals("")) {
			criteriaHelper.getCriteria().createCriteria("topProjekte").add(
			    Restrictions.ilike("sapProjektNummer", sapProjektnummer, MatchMode.ANYWHERE));
		}
		criteriaHelper.getCriteria().add(Restrictions.eq("fahrplanjahr", fahrplanjahr));
		return criteriaHelper;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public PaginatedList<SperrpausenbedarfListReport> findReportPaginatedByKeywordsAndSort(
	    List<Order> sortOrders, Integer regionalbereichId, String anmelder, String gewerk,
	    String untergewerk, Integer hauptstreckenNummer, Integer paketId, String sapProjektnummer,
	    FetchPlan[] fetchPlans, Integer start, Integer count, Integer sessionFahrplanjahr) {
		PaginatedList<SperrpausenbedarfListReport> result = new PaginatedList<SperrpausenbedarfListReport>();

		CriteriaHelper criteriaHelper = createSearchCriteria(regionalbereichId, anmelder, gewerk,
		    untergewerk, hauptstreckenNummer, paketId, sapProjektnummer, sessionFahrplanjahr);

		// Anzahl der max. moeglichen Suchergebnisse
		criteriaHelper.getCriteria().setProjection(Projections.countDistinct("id"));
		Integer resultCount = (Integer) criteriaHelper.getCriteria().uniqueResult();
		result.setFullListSize(resultCount);

		// Paging
		if (start != null) {
			criteriaHelper.getCriteria().setFirstResult(start);
		}

		if (count != null) {
			criteriaHelper.getCriteria().setMaxResults(count);
		}

		// Suche durchfuehren
		if (criteriaHelper.getAliases().get("hauptStrecke") == null) {
			criteriaHelper.getAliases().put("hauptStrecke", "hs");
			criteriaHelper.getCriteria().createAlias("hauptStrecke", "hs");
		}

		criteriaHelper.getCriteria().createAlias("regionalbereich", "r");
		criteriaHelper.getCriteria().setProjection(
		    Projections.projectionList().add(Projections.property("id").as("id")).add(
		    	Projections.property("lastChangeDate").as("lastChangeDate")).add(
		        Projections.property("projektDefinitionDbBez").as("projektDefinitionDbBez")).add(
		        Projections.property("untergewerk").as("untergewerk")).add(
		        Projections.property("r.id").as("regionalBereichId")).add(
		        Projections.property("r.name").as("regionalBereichName")).add(
		        Projections.property("hs.nummer").as("hauptstreckeNummer")).add(
		        Projections.property("urspruenglichesPlanungsjahr").as(
		            "urspruenglichesPlanungsjahr")).add(Projections.property("lfdNr").as("lfdNr"))
		        .add(Projections.property("genehmiger").as("genehmiger")));

		criteriaHelper.getCriteria().setResultTransformer(
		    new AliasToBeanResultTransformer(SperrpausenbedarfListReport.class));

		criteriaHelper.setCriteria(applyOrders(criteriaHelper.getCriteria(), sortOrders));
		List<SperrpausenbedarfListReport> resultList = criteriaHelper.getCriteria().list();
		RegionalbereichDaoImpl regionalbereichDao = EasyDaoFactory.getInstance()
		    .getRegionalbereichDao();
		for (SperrpausenbedarfListReport report : resultList) {
			report.setRegionalbereich(regionalbereichDao.findById(report.getRegionalBereichId()));
		}
		result.setList(resultList);

		return result;
	}

	private Criteria applyOrders(Criteria criteria, Collection<Order> sortOrders) {
		if (criteria != null && sortOrders != null) {
			for (Order order : sortOrders) {
				criteria.addOrder(order);
			}
		}
		return criteria;
	}
}