package db.training.bob.service.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.MessageResources;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.MassnahmeDao;
import db.training.bob.dao.MassnahmeDaoImpl;
import db.training.bob.dao.zvf.ZugDao;
import db.training.bob.dao.zvf.ZugDaoImpl;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.EVUGruppe;
import db.training.bob.model.SearchBean;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.web.statistics.zvf.SimpleReportBean;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.dao.EasySessionFactory;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;

/**
 * Stellt die Daten für den Report über die Verspätungsminuten zusammen.
 * 
 * @author EckhardJost
 *
 */
public class BbzrVerspaetungsminutenReportServiceImpl extends EasyServiceImpl<BbzrVerspaetungsminutenReportService, Serializable> 
	implements BbzrVerspaetungsminutenReportService {

	private Logger log = Logger.getLogger(BbzrVerspaetungsminutenReportServiceImpl.class);
	
	private static final String GUETERVERKEHR = "Gueterverkehr";
	private static final String NAHVERKHER = "Nahverkehr";
	private static final String FERNVERKEHR = "Fernverkehr";
	private static final String GESAMT = "Gesamt";
	
	public BbzrVerspaetungsminutenReportServiceImpl() {
		super(BbzrVerspaetungsminutenReportService.class);
	}
	
	/**
	 * Sucht eine Liste von ID's von Baumassnahmen zusammen, die den Suchkriterien
	 * der übergebenen SearchBean entsprechen.
	 * Es handelt sich um eine Komfortmethode
	 */
	@Transactional
	public List<Integer> findIDsBySearchBean(SearchBean searchBean, FetchPlan[] fetchPlans){
		return findIDsBySearchBean(null, searchBean, null, null, fetchPlans);
	}
	
	@Transactional
	public List<Integer> findIDsBySearchBean(List<Order> sortOrders,
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

		return findPageIDsByCriteria(criteria, start, count, fetchPlans);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Integer> findPageIDsByCriteria(Criteria criteria, Integer start, Integer count,
	    FetchPlan[] fetchPlans) {
		log.debug("[findPageByCriteria] @deprecated");
		
		if (criteria == null) {
			criteria = getCurrentSession().createCriteria(clazz);
		}

		// Anzahl der max. möglichen Suchergebnisse
		criteria.setProjection(Projections.countDistinct("id"));
		Integer resultCount = (Integer) criteria.uniqueResult();

		if (log.isDebugEnabled())
			log.debug(resultCount + " Records");

		// Paging
		if (start != null) {
			criteria.setFirstResult(start);
		}

		if (count != null) {
			criteria.setMaxResults(count);
		}
		
		// Suche durchführen
		log.debug("start loading result list...");
		List<Integer> list = new ArrayList<Integer>();
		if (resultCount > 0) {
			criteria.setProjection(Projections.id());
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			list.addAll(criteria.list());
		}
		log.debug("finished loading result list...");

		return list;
	}
	
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
	@SuppressWarnings("unchecked")
	public Report buildBbzrVerspaetungsminutenReport(List<Integer> bbps, Date zeitraumVerkehrstagVon,
			Date zeitraumVerkehrstagBis, Integer qsks) {
		log.debug("starting report");

		// Auswertung über mehrere Baumaßnahmen
		SimpleReportBean rbGesamt = new SimpleReportBean(GESAMT);
		SimpleReportBean rbFv = new SimpleReportBean(FERNVERKEHR);
		SimpleReportBean rbNv = new SimpleReportBean(NAHVERKHER);
		SimpleReportBean rbGv = new SimpleReportBean(GUETERVERKEHR);

		HashMap<String, SimpleReportBean> mapEVU = new HashMap<String, SimpleReportBean>();

		SessionFactory sessionFactory = EasySessionFactory.getInstance();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		for (Integer bm_id : bbps) {

			log.debug("trying to load Baumassnahme with id: " + bm_id);

			//finde die relevanten Massnahmen
			MassnahmeDao massnahmeDao = new MassnahmeDaoImpl(sessionFactory);
			Iterator<Object[]> mInterator = massnahmeDao.findMassnahmenByBaumassnahmen(bm_id).iterator();

			Integer lastUebergabeblattId = 0;
			List<Object[]> zugliste = new ArrayList<Object[]>();
			while (mInterator.hasNext()) {
				Object[] objects = mInterator.next();
				Integer uebergabeblattId = new Integer(((BigDecimal)objects[0]).intValue());
				Integer massnahmeId = new Integer(((BigDecimal)objects[3]).intValue());

				if(!lastUebergabeblattId.equals(uebergabeblattId)){ //weil sortiert, brauchen wir nur die erste Zeile von jeder Gruppe

					ZugDao zugDao = new ZugDaoImpl(sessionFactory);
					zugliste.addAll(zugDao.findZugInfosByMassnahme(massnahmeId));
				}

				lastUebergabeblattId = uebergabeblattId;
			}

			Iterator<Object[]> objectIterator = zugliste.iterator();
			while(objectIterator.hasNext()){
				Object[] objects = objectIterator.next();

				//Objektinitialisierung
				//Verspaetung
				Integer zVerspaetung = null;
				if(objects[1]!=null){
					zVerspaetung = new Integer(((BigDecimal) objects[1]).intValue());
				}

				//Verkehrstag
				Date zVerkehrstag = (Date) objects[2];

				//QS/KS
				Integer zQs_ks = null;
				if(objects[3]!=null){
					zQs_ks = new Integer(((BigDecimal) objects[3]).intValue());
				}

				//Betreiber
				String zBetreiber = (String) objects[4];

				//Güterverkehrsflag
				Boolean mFestgelegtSGV = false;
				if (((BigDecimal) objects[5]).intValue() > 0) {
					mFestgelegtSGV = true;
				}; 

				//Personenfernverkehrsflag
				Boolean mFestgelegtSPFV = false;
				if (((BigDecimal) objects[6]).intValue() > 0) {
					mFestgelegtSPFV = true;
				};

				//Personennahverkehrsflag
				Boolean mFestgelegtSPNV = false;
				if (((BigDecimal) objects[7]).intValue() > 0) {
					mFestgelegtSPNV = true;
				}

				//Sind die Zeiträume eingehalten?
				if (zVerkehrstag != null
						&& (zeitraumVerkehrstagVon == null || zVerkehrstag.after(zeitraumVerkehrstagVon))
						&& (zeitraumVerkehrstagBis == null || zVerkehrstag.before(zeitraumVerkehrstagBis))) {

					// qs, ks oder nichtaktiv ausgewählt
					if (qsks==null || zQs_ks== null || qsks==-1 || qsks.equals(zQs_ks)){

						// Gesamt
						rbGesamt.add(zVerspaetung);

						// FV, NV, GV
						if (mFestgelegtSPFV == true) {
							rbFv.add(zVerspaetung);
						}
						if (mFestgelegtSPNV == true) {
							rbNv.add(zVerspaetung);
						} 
						if (mFestgelegtSGV == true) {
							rbGv.add(zVerspaetung);
						}

						// EVU
						String evu = zBetreiber;
						if (evu != null && !"".equals(evu)) {
							SimpleReportBean rbEVU = mapEVU.get(evu);

							if (rbEVU == null && zVerspaetung > 0){ // EVU noch nicht in der Liste
								rbEVU = new SimpleReportBean(evu);
								mapEVU.put(evu, rbEVU);
							}

							if (zVerspaetung > 0) {
								rbEVU.add(zVerspaetung);
							}
						}
					}
				}
			}
		}
		tx.commit();
		session.close();
		System.gc();

		// Ergebnis zusammenstellen
		log.debug("Ergebnisse werden zusammengestellt.");
		Report report = new Report();
		report.getResultGesamt().add(rbGesamt);
		report.getResultGesamt().add(rbFv);
		report.getResultGesamt().add(rbNv);
		report.getResultGesamt().add(rbGv);

		// Gruppieren nach EVU-Kundengruppe
		EVUGruppeService evugruppeService = EasyServiceFactory.getInstance().createEVUGruppeService();
		EVUGruppe evuGruppe;

		for (SimpleReportBean rb : mapEVU.values()) {
			evuGruppe = evugruppeService.findByKundenNr(rb.getLabel());
			SimpleReportBean rb2 = null;
			if (evuGruppe != null){
				rb2 = new SimpleReportBean(evuGruppe.getName());
			}				

			if (evuGruppe == null){
				rb2 = new SimpleReportBean(MessageResources.getMessageResources("MessageResources").getMessage(
				"auswerungen.zuege.ohne.evugruppe"));
			}

			if (evuGruppe != null)
				rb2.setLabel(evuGruppe.getName());

			if (evuGruppe == null)
				rb2.setLabel(MessageResources.getMessageResources("MessageResources").getMessage(
				"auswerungen.zuege.ohne.evugruppe"));

			rb2.setAnzahl(rb.getAnzahl());

			Iterator<SimpleReportBean> it = report.getResultEVU().iterator();
			SimpleReportBean entry = null;
			Boolean found = false;

			while (it.hasNext()) {
				entry = it.next();
				// wenn Eintrag bereits in Ergebnisliste, dann Werte addieren
				if (entry.getLabel().equals(rb2.getLabel())) {
					found = true;
					entry.setAnzahl(entry.getAnzahl() + rb2.getAnzahl());
				}
			}

			// wenn noch nicht in Ergebnisliste, dann reinpacken
			if (!found)
				report.getResultEVU().add(rb2);

		}

		log.debug("ending report");
		return report;
	}

}
