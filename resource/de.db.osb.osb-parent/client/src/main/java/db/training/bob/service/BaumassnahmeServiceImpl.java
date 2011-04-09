package db.training.bob.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.BaumassnahmeDao;
import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.SearchBean;
import db.training.bob.service.report.SearchBeanHelper;
import db.training.bob.service.zvf.BbzrService;
import db.training.bob.service.zvf.BbzrServiceImpl;
import db.training.bob.service.zvf.UebergabeblattService;
import db.training.bob.util.CollectionHelper;
import db.training.bob.web.statistics.MethodsPerLocationReport;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.dao.EasyDaoFactory;
import db.training.easy.core.dao.EasySessionFactory;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class BaumassnahmeServiceImpl extends EasyServiceImpl<Baumassnahme, Serializable> implements
	BaumassnahmeService {
	
	private Logger log = Logger.getLogger(BaumassnahmeServiceImpl.class);

	public BaumassnahmeServiceImpl() {
		super(Baumassnahme.class);
	}

	public BaumassnahmeServiceImpl(BaumassnahmeDao dao) {
		super(Baumassnahme.class);
		setBasicDao(dao);
	}

	public BaumassnahmeDao getDao() {
		if (getBasicDao() != null)
			return (BaumassnahmeDao) getBasicDao();
		return EasyDaoFactory.getInstance().getBaumassnahmeDao();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void fill(Baumassnahme bm, Collection<FetchPlan> plans) {

		if (plans.contains(FetchPlan.BOB_BBP_MASSNAHME)) {
			Hibernate.initialize(bm.getBbpMassnahmen());
			if (plans.contains(FetchPlan.BBP_REGELUNGEN)
					|| plans.contains(FetchPlan.BBP_REGIONALBEREICH)) {
				if (bm.getBbpMassnahmen() != null) {
					BBPMassnahmeService bbpService = EasyServiceFactory.getInstance()
					.createBBPMassnahmeService();
					List<BBPMassnahme> bbps = new ArrayList<BBPMassnahme>(bm.getBbpMassnahmen());
					bbpService.fill(bbps, plans);
					bm.setBbpMassnahmen(new HashSet<BBPMassnahme>(bbps));
				}
			}
		}

		if (plans.contains(FetchPlan.BOB_REGIONALBEREICH_FPL))
			Hibernate.initialize(bm.getRegionalBereichFpl());

		if (plans.contains(FetchPlan.BOB_BEARBEITUNGSBEREICH))
			Hibernate.initialize(bm.getBearbeitungsbereich());

		if (plans.contains(FetchPlan.BOB_BEARBEITER))
			Hibernate.initialize(bm.getBearbeiter());

		if (plans.contains(FetchPlan.BOB_KIGBAU)) {
			Hibernate.initialize(bm.getKigBauNr());
			Hibernate.initialize(bm.getKigBau());
		}

		if (plans.contains(FetchPlan.BOB_QS)) {
			Hibernate.initialize(bm.getQsNr());
		}

		if (plans.contains(FetchPlan.BOB_KORRIDOR_ZEITFENSTER))
			Hibernate.initialize(bm.getKorridorZeitfenster());

		if (plans.contains(FetchPlan.BOB_AUSFALLGRUND))
			Hibernate.initialize(bm.getAusfallGrund());

		if (plans.contains(FetchPlan.BOB_AENDERUNGSDOKUMENTATION))
			Hibernate.initialize(bm.getAenderungen());

		if (plans.contains(FetchPlan.BOB_ARBEITSLEISTUNG_REGIONALBEREICHE))
			Hibernate.initialize(bm.getBenchmark());

		if (plans.contains(FetchPlan.BOB_TERMINE_BBP))
			Hibernate.initialize(bm.getBaubetriebsplanung());

		if (plans.contains(FetchPlan.BOB_TERMINE_GEVU))
			Hibernate.initialize(bm.getGevus());

		if (plans.contains(FetchPlan.BOB_TERMINE_PEVU))
			Hibernate.initialize(bm.getPevus());

		if (plans.contains(FetchPlan.BOB_UEBERGABEBLATT)) {
			Hibernate.initialize(bm.getUebergabeblatt());
			if (plans.contains(FetchPlan.UEB_HEADER) || plans.contains(FetchPlan.UEB_HEADER_SENDER)
					|| plans.contains(FetchPlan.UEB_HEADER_EMPFAENGER)
					|| plans.contains(FetchPlan.UEB_REGIONALBEREICHE)
					|| plans.contains(FetchPlan.UEB_BAUMASSNAHMEN)
					|| plans.contains(FetchPlan.UEB_BEARBEITUNGSSTATUS)
					|| plans.contains(FetchPlan.UEB_KNOTENZEITEN)
					|| plans.contains(FetchPlan.UEB_MN_FPLO) || plans.contains(FetchPlan.UEB_MN_ZUEGE)
					|| plans.contains(FetchPlan.UEB_ZUG_REGELWEG)
					|| plans.contains(FetchPlan.UEB_ZUG_ABWEICHUNG_BAHNHOF)
					|| plans.contains(FetchPlan.UEB_ZUG_ABWEICHUNG_HALT_BAHNHOF)) {
				if (bm.getUebergabeblatt() != null) {
					UebergabeblattService uebService = EasyServiceFactory.getInstance()
					.createUebergabeblattService();
					uebService.fill(bm.getUebergabeblatt(), plans);
				}
			}
		}

		if (plans.contains(FetchPlan.BOB_BBZR)) {
			Hibernate.initialize(bm.getZvf());
			if (plans.contains(FetchPlan.BBZR_HEADER)
					|| plans.contains(FetchPlan.UEB_HEADER_EMPFAENGER)
					|| plans.contains(FetchPlan.BBZR_BAUMASSNAHMEN)
					|| plans.contains(FetchPlan.BBZR_MN_ZUEGE)
					|| plans.contains(FetchPlan.ZVF_MN_VERSION)
					|| plans.contains(FetchPlan.UEB_ZUG_REGELWEG)) {
				if (bm.getZvf() == null || bm.getZvf().size() == 0) {
				} else {
					BbzrService bbzrService = null;
					try {
						bbzrService = EasyServiceFactory.getInstance().createBbzrService();
					} catch (NullPointerException e) {
						bbzrService = new BbzrServiceImpl();
					}
					bbzrService.fill(bm.getAktuelleZvf(), plans);
				}
			}
		}

		if (plans.contains(FetchPlan.BOB_ZUGCHARAKTERISTIK)) {
			Hibernate.initialize(bm.getZugcharakteristik());
		}
	}

	/*
	 * org.springframework.transaction.CannotCreateTransactionException: Could not open Hibernate
	 * Session for transaction; nested exception is
	 * org.springframework.transaction.InvalidIsolationLevelException: HibernateTransactionManager
	 * is not allowed to support custom isolation levels: make sure that its 'prepareConnection'
	 * flag is on (the default) and that the Hibernate connection release mode is set to 'on_close'
	 * (LocalSessionFactoryBean's default) Caused by:
	 * org.springframework.transaction.InvalidIsolationLevelException: HibernateTransactionManager
	 * is not allowed to support custom isolation levels: make sure that its 'prepareConnection'
	 * flag is on (the default) and that the Hibernate connection release mode is set to 'on_close'
	 * (LocalSessionFactoryBean's default) at
	 * org.springframework.orm.hibernate3.HibernateTransactionManager
	 * .doBegin(HibernateTransactionManager.java:464) at org.springframework.transaction
	 * .support.AbstractPlatformTransactionManager
	 * .getTransaction(AbstractPlatformTransactionManager.java:350) at
	 * org.springframework.transaction
	 * .interceptor.TransactionAspectSupport.createTransactionIfNecessary
	 * (TransactionAspectSupport.java:262) at
	 * org.springframework.transaction.interceptor.TransactionInterceptor
	 * .invoke(TransactionInterceptor.java:102) at
	 * org.springframework.aop.framework.ReflectiveMethodInvocation
	 * .proceed(ReflectiveMethodInvocation.java:161) at org.springframework.aop.framework
	 * .JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:203) at $Proxy53.create(Unknown Source) at
	 * db.training.bob.web.baumassnahme.XmlSelectRegelungAction .createBaumassnahme
	 * (XmlSelectRegelungAction.java:291) at
	 * db.training.bob.web.baumassnahme.XmlSelectRegelungAction
	 * .run(XmlSelectRegelungAction.java:130) at
	 * db.training.easy.web.BaseAction.execute(BaseAction.java:75) at
	 * org.mwolff.struts.back.BackRequestProcessor
	 * .processActionPerform(BackRequestProcessor.java:112) at org.apache.struts.
	 * action.RequestProcessor.process(RequestProcessor.java:228) at
	 * org.apache.struts.action.ActionServlet.process(ActionServlet.java:1913) at
	 * org.apache.struts.action.ActionServlet.doPost(ActionServlet.java:462) at
	 * javax.servlet.http.HttpServlet.service(HttpServlet.java:637) at
	 * javax.servlet.http.HttpServlet.service(HttpServlet.java:717) at
	 * org.apache.catalina.core.ApplicationFilterChain
	 * .internalDoFilter(ApplicationFilterChain.java:290) at org.apache.catalina.
	 * core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:206) at org
	 * .displaytag.filter.ResponseOverrideFilter.doFilter(ResponseOverrideFilter .java:125) at org.
	 * apache.catalina.core.ApplicationFilterChain.internalDoFilter
	 * (ApplicationFilterChain.java:235) at org.apache.catalina.core.ApplicationFilterChain
	 * .doFilter(ApplicationFilterChain.java:206) at
	 * db.training.easy.web.CharsetFilter.doFilter(CharsetFilter.java:33) at
	 * org.apache.catalina.core
	 * .ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:235) at
	 * org.apache.catalina.core.ApplicationFilterChain.doFilter( ApplicationFilterChain.java:206) at
	 * org .acegisecurity.util.FilterChainProxy$VirtualFilterChain
	 * .doFilter(FilterChainProxy.java:265) at
	 * org.acegisecurity.intercept.web.FilterSecurityInterceptor .invoke(FilterSecurityInterceptor.
	 * java:107) at org.acegisecurity.intercept
	 * .web.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor .java:72) at
	 * org.acegisecurity.util.FilterChainProxy$VirtualFilterChain.doFilter (FilterChainProxy
	 * .java:275) at org.acegisecurity.ui.ExceptionTranslationFilter
	 * .doFilter(ExceptionTranslationFilter.java:110) at
	 * org.acegisecurity.util.FilterChainProxy$VirtualFilterChain
	 * .doFilter(FilterChainProxy.java:275 ) at
	 * org.acegisecurity.providers.anonymous.AnonymousProcessingFilter.doFilter(
	 * AnonymousProcessingFilter.java:125) at
	 * org.acegisecurity.util.FilterChainProxy$VirtualFilterChain
	 * .doFilter(FilterChainProxy.java:275) at org.acegisecurity.ui.AbstractProcessingFilter
	 * .doFilter(AbstractProcessingFilter.java:229) at org
	 * .acegisecurity.util.FilterChainProxy$VirtualFilterChain .doFilter(FilterChainProxy.java:275)
	 * at org.acegisecurity.ui.logout.LogoutFilter.doFilter(LogoutFilter.java:106) at
	 * org.acegisecurity .util.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy
	 * .java:275) at org.acegisecurity .context.HttpSessionContextIntegrationFilter
	 * .doFilter(HttpSessionContextIntegrationFilter .java:286) at org.acegisecurity
	 * .util.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy .java:275) at
	 * org.acegisecurity.util.FilterChainProxy.doFilter(FilterChainProxy .java:149) at
	 * org.acegisecurity.util.FilterToBeanProxy.doFilter(FilterToBeanProxy .java:98) at
	 * org.apache.catalina .core.ApplicationFilterChain.internalDoFilter
	 * (ApplicationFilterChain.java:235) at org.apache.catalina.core.ApplicationFilterChain
	 * .doFilter(ApplicationFilterChain.java:206) at org.apache.catalina.core.StandardWrapperValve
	 * .invoke(StandardWrapperValve.java:233) at org.apache.catalina.core.StandardContextValve
	 * .invoke(StandardContextValve.java:191) at org.apache.catalina.core.StandardHostValve
	 * .invoke(StandardHostValve.java:127) at org.apache.catalina.valves.ErrorReportValve
	 * .invoke(ErrorReportValve.java:102) at org.apache.catalina.core.StandardEngineValve
	 * .invoke(StandardEngineValve.java:109) at org.apache.catalina.connector.CoyoteAdapter
	 * .service(CoyoteAdapter.java:298) at org.apache.coyote.http11.Http11Processor
	 * .process(Http11Processor.java:857) at org.apache.coyote
	 * .http11.Http11Protocol$Http11ConnectionHandler .process(Http11Protocol.java:588) at
	 * org.apache.tomcat.util.net.JIoEndpoint$Worker.run(JIoEndpoint.java:489) at
	 * java.lang.Thread.run(Thread.java:619)
	 */
	// @Transactional(readOnly = false)
	// public void create(Baumassnahme baumassnahme) throws RuntimeException {
	// createIntern(baumassnahme);
	// }

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public synchronized void create(Baumassnahme baumassnahme) throws RuntimeException {
		int min = getMinVorgangsNr(baumassnahme);
		int max = getMaxVorgangsNr(baumassnahme);

		Session session = getCurrentSession();
		synchronized (this) {
			int fahrplanjahr = baumassnahme.getFahrplanjahr();
			StringBuilder sb = new StringBuilder(
			"select b.vorgangsNr from Baumassnahme b where fahrplanjahr=");
			sb.append(fahrplanjahr);
			sb.append(" and b.vorgangsNr>=");
			sb.append(min);
			sb.append(" and b.vorgangsNr<=");
			sb.append(max);
			sb.append(" order by b.vorgangsNr asc");
			Query query = session.createQuery(sb.toString());
			List<?> usedNumbers = query.list();
			baumassnahme.setVorgangsNr(getVorgangsNr(min, max, usedNumbers));
			getDao().save(baumassnahme);
		}
	}

	@SuppressWarnings("unchecked")
	public int getVorgangsNr(int min, int max, List usedNumbers) {
		while (true) {
			for (int i = min; i <= max; i++) {
				if (!usedNumbers.contains(i)) {
					return i;
				}
				usedNumbers.remove((Object) i);
			}
		}
	}

	private int getMinVorgangsNr(Baumassnahme baumassnahme) {
		Bearbeitungsbereich bereich = baumassnahme.getBearbeitungsbereich();
		if (bereich != null && bereich.getVorgangsnrMin() != null)
			if (bereich.getVorgangsnrMin() != null)
				return bereich.getVorgangsnrMin();

		// User hat keinen Bearbeitungsbereich
		if (baumassnahme.getRegionalBereichFpl().getId() == 1) // Ost
			return 10000;
		if (baumassnahme.getRegionalBereichFpl().getId() == 2) // Nord
			return 20000;
		if (baumassnahme.getRegionalBereichFpl().getId() == 3) // West
			return 30000;
		if (baumassnahme.getRegionalBereichFpl().getId() == 4) // Südost
			return 60000;
		if (baumassnahme.getRegionalBereichFpl().getId() == 5) // Mitte
			return 40000;
		if (baumassnahme.getRegionalBereichFpl().getId() == 6) // Südwest
			return 50000;
		if (baumassnahme.getRegionalBereichFpl().getId() == 7) // Süd
			return 70000;
		if (baumassnahme.getRegionalBereichFpl().getId() == 10) // Zentrale
			return 80000;
		return 0;
	}

	private int getMaxVorgangsNr(Baumassnahme baumassnahme) {
		Bearbeitungsbereich bereich = baumassnahme.getBearbeitungsbereich();
		if (bereich != null && bereich.getVorgangsnrMax() != null)
			if (bereich.getVorgangsnrMax() != null)
				return bereich.getVorgangsnrMax();

		// User hat keinen Bearbeitungsbereich
		if (baumassnahme.getRegionalBereichFpl().getId() == 1) // Ost
			return 19999;
		if (baumassnahme.getRegionalBereichFpl().getId() == 2) // Nord
			return 29999;
		if (baumassnahme.getRegionalBereichFpl().getId() == 3) // West
			return 39999;
		if (baumassnahme.getRegionalBereichFpl().getId() == 4) // Südost
			return 69999;
		if (baumassnahme.getRegionalBereichFpl().getId() == 5) // Mitte
			return 49999;
		if (baumassnahme.getRegionalBereichFpl().getId() == 6) // Südwest
			return 59999;
		if (baumassnahme.getRegionalBereichFpl().getId() == 7) // Süd
			return 79999;
		if (baumassnahme.getRegionalBereichFpl().getId() == 10) // Zentrale
			return 89999;
		return 9999;
	}

	@Transactional
	public Integer countBySearchBean(SearchBean searchBean, FetchPlan[] fetchPlans){
		return countBySearchBean(null, searchBean, null, null, fetchPlans);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Integer countBySearchBean(List<Order> sortOrders,
			SearchBean searchBean, Integer start, Integer count, FetchPlan[] fetchPlans){

		Criteria criteria = getCurrentSession().createCriteria(Baumassnahme.class);
		criteria.setProjection(Projections.distinct(Projections.id())); 
		criteria = SearchBeanHelper.fillCriteriaFromBaumassnahmeSearchBean(criteria, searchBean);
		List<Integer> baumassnahmenIds = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("Abfrage erledigt. Anzahl gefundene Baumassnahmen: " + baumassnahmenIds.size());
		}

		if (baumassnahmenIds.size() > 1000)
			throw new QueryException(
					"Die Abfrage lieferte " + baumassnahmenIds.size() + " Ergebnisse (max. 1000 erlaubt!).");

		return baumassnahmenIds.size();
	}

	@Transactional
	public List<Baumassnahme> findBySearchBean(SearchBean searchBean, FetchPlan[] fetchPlans) {
		return findBySearchBean(null, searchBean, null, null, fetchPlans).getList();
	}
	
	@SuppressWarnings("deprecation")
	@Transactional
	public PaginatedList<Baumassnahme> findBySearchBean(List<Order> sortOrders,
			SearchBean searchBean, Integer start, Integer count, FetchPlan[] fetchPlans) {

		if (log.isDebugEnabled()) {
			log.debug("Entering findBySort()");
			log.debug("sortOrders: " + sortOrders);
			log.debug("searchCriteria: " + searchBean);
			log.debug("start :" + start);
			log.debug("count :" + count);
		}

		if (start != null && start < 0) {
			start = null;
		}

		Criteria criteria = getCurrentSession().createCriteria(Baumassnahme.class);
		criteria = SearchBeanHelper.fillCriteriaFromBaumassnahmeSearchBean(criteria, searchBean);

		SearchBeanHelper.applyBaumassnahmeOrders(criteria, sortOrders, searchBean);

		if (log.isDebugEnabled())
			log.debug(criteria.toString());

		return findPageByCriteria(criteria, start, count, fetchPlans);
	}

	@SuppressWarnings("deprecation")
	@Transactional(readOnly = false)
	public MethodsPerLocationReport findMethodsPerLocation(SearchBean searchBean,
			FetchPlan[] fetchPlan) {
		MethodsPerLocationReport resultBean = new MethodsPerLocationReport();
		List<Baumassnahme> bbps = null;

		Criteria criteria = getCurrentSession().createCriteria(Baumassnahme.class);

		criteria = SearchBeanHelper.fillCriteriaFromBaumassnahmeSearchBean(getCurrentSession()
				.createCriteria(Baumassnahme.class), searchBean);

		// System.out.println(criteria.toString());

		List<Order> sortOrders = new LinkedList<Order>();
		sortOrders.add(Order.asc("regionalBereichFpl.name"));
		SearchBeanHelper.applyBaumassnahmeOrders(criteria, sortOrders, searchBean);

		bbps = getDao().findByCriteria(criteria);

		fill(bbps, fetchPlan);

		for (Baumassnahme bm : bbps) {
			Integer idOfLocation = bm.getRegionalBereichFpl().getId();

			// die ID des Regionalbereichs ist Schlüssel in HashMap
			if (!resultBean.getReport().containsKey(idOfLocation)) {
				// Regionalbereich ist nicht enhalten
				resultBean.getReport().put(idOfLocation, 1);
			} else {
				resultBean.getReport().put(idOfLocation,
						resultBean.getReport().get(idOfLocation) + 1);
			}
		}

		return resultBean;
	}

	@Transactional
	@Deprecated
	public Baumassnahme findById(Integer id, FetchPlan[] fetchPlans) {
		Baumassnahme baumassnahme = null;
		baumassnahme = getDao().findById(id);
		fill(baumassnahme, fetchPlans);
		return baumassnahme;
	}

	@Override
	@Transactional(readOnly = false)
	public PaginatedList<Baumassnahme> findBySearchBean(List<Order> sortOrders,
			SearchBean searchBean, Integer start, Integer count, Preload[] preloads) {

		if (log.isDebugEnabled()) {
			log.debug("Entering findBySort()");
			log.debug("sortOrders: " + sortOrders);
			log.debug("searchCriteria: " + searchBean);
			log.debug("start :" + start);
			log.debug("count :" + count);
		}

		if (start != null && start < 0) {
			start = null;
		}

		Criteria criteria = getCurrentSession().createCriteria(Baumassnahme.class);

		if (searchBean != null) {
			criteria = SearchBeanHelper.fillCriteriaFromBaumassnahmeSearchBean(criteria, searchBean);
		}

		if (log.isDebugEnabled())
			log.debug(criteria.toString());

		SearchBeanHelper.applyBaumassnahmeOrders(criteria, sortOrders, null);

		return findPageByCriteria(criteria, start, count, preloads);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, Integer> findReasonsForChanges(List<Baumassnahme> baumassnahmen) {
		Session session = EasySessionFactory.getInstance().getCurrentSession();
		List<Integer> bmIds = new ArrayList<Integer>();
		for (Baumassnahme bm : baumassnahmen) {
			bmIds.add(bm.getId());
		}

		Map<String, Integer> results = new HashMap<String, Integer>();

		// nur 1000 IDs pro Abfrage, da bei Oracle limitiert
		final int listSize = 1000;
		Query query = null;
		List<Object[]> l = null;
		String key = null;
		Integer anzahl = null;

		for (int startIndex = 0; startIndex < bmIds.size(); startIndex += listSize) {
			String bbpIdList = CollectionHelper.toSeparatedStringListSize(bmIds, ",", startIndex,
					listSize);
			query = session
			.createSQLQuery("select g.name, (select count(*) from aenderung a join bm_aenderung ba on a.ID = ba.aenderungen_ID where ba.bm_ID in ("
					+ bbpIdList + ") and a.grund_ID = g.id) from grund g");
			l = query.list();
			for (Object[] o : l) {
				key = o[0].toString();
				anzahl = results.get(key);
				if (anzahl != null)
					anzahl = anzahl + Integer.parseInt(o[1].toString());
				else
					anzahl = Integer.parseInt(o[1].toString());

				results.put(key, anzahl);
			}
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, Integer> findAusfallgruende(List<Baumassnahme> baumassnahmen) {
		Session session = EasySessionFactory.getInstance().getCurrentSession();
		List<Integer> bmIds = new ArrayList<Integer>();
		for (Baumassnahme bm : baumassnahmen) {
			bmIds.add(bm.getId());
		}

		Map<String, Integer> results = new HashMap<String, Integer>();

		// nur 1000 IDs pro Abfrage, da bei Oracle limitiert
		final int listSize = 1000;
		Query query = null;
		List<Object[]> l = null;
		String key = null;
		Integer anzahl = null;

		for (int startIndex = 0; startIndex < bmIds.size(); startIndex += listSize) {
			String bbpIdList = CollectionHelper.toSeparatedStringListSize(bmIds, ",", startIndex,
					listSize);
			query = session.createSQLQuery("select g.name, (select count(*) from bm where id in ("
					+ bbpIdList + ") and bm.ausfallGrund_ID = g.id) from grund g");
			l = query.list();
			for (Object[] o : l) {
				key = o[0].toString();
				anzahl = results.get(key);
				if (anzahl != null)
					anzahl = anzahl + Integer.parseInt(o[1].toString());
				else
					anzahl = Integer.parseInt(o[1].toString());

				results.put(key, anzahl);
			}
		}
		return results;
	}

	/**
	 * listet alle Baumassnahmen bei denen der angebene User als Bearbeiter(Favorit) aktiv oder
	 * inaktiv eingetragen ist
	 */
	@Transactional
	public List<Baumassnahme> findByBearbeiter(Integer bearbeiterId) {

		Integer intValue = bearbeiterId;
		Criteria criteria = getCurrentSession().createCriteria(Baumassnahme.class);

		if (intValue != null) {
			if (intValue != 0) {
				criteria.createAlias("bearbeiter", "bearbeiterAlias",
						CriteriaSpecification.INNER_JOIN);
				criteria.add(Restrictions.eq("bearbeiterAlias.user.id", intValue));
			}
		}
		return findByCriteria(new Preload[] { new Preload(Baumassnahme.class, "bearbeiter") },
				criteria);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Integer> findAvailableFahrplanjahre() {
		return getCurrentSession().createCriteria(Baumassnahme.class)
		.setProjection(Projections.distinct(Projections.property("fahrplanjahr")))
		.addOrder(Order.asc("fahrplanjahr")).list();
	}
}