package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.logwrapper.Logger;
import db.training.osb.dao.BuendelDao;
import db.training.osb.dao.GleissperrungDao;
import db.training.osb.dao.PaketDao;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Paket;
import db.training.osb.model.VzgStrecke;

public class BuendelServiceImpl extends EasyServiceImpl<Buendel, Serializable> implements
    BuendelService {

	private static Logger log = Logger.getLogger(BuendelServiceImpl.class);

	private PaketDao paketDao;

	private GleissperrungDao gleissperrungDao;

	public BuendelServiceImpl() {
		super(Buendel.class);
	}

	public BuendelDao getDao() {
		return (BuendelDao) getBasicDao();
	}

	public void setPaketDao(PaketDao paketDao) {
		this.paketDao = paketDao;
	}

	public void setGleissperrungDao(GleissperrungDao gleissperrungDao) {
		this.gleissperrungDao = gleissperrungDao;
	}

	@Override
	public void fill(Buendel b, Collection<FetchPlan> plans) {

		Hibernate.initialize(b);

		plans.remove(FetchPlan.OSB_BUENDEL);

		if (plans.contains(FetchPlan.OSB_BUENDEL_GLEISSPERRUNGEN)) {
			GleissperrungService service = EasyServiceFactory.getInstance()
			    .createGleissperrungService();
			service.fill(b.getGleissperrungen(), plans);
		}
	}

	@Transactional
	public Integer findNextLfdNr(Regionalbereich rb, int jahr) {
		if (rb == null) {
			if (log.isWarnEnabled())
				log.warn("Abbruch weil RB=null. RB: null Jahr: " + jahr);
			return null;
		}

		Query qry = getCurrentSession()
		    .createQuery(
		        "select max(lfdNr) from Buendel b where regionalbereich = :rb and fahrplanjahr = :jahr")
		    .setEntity("rb", rb).setInteger("jahr", jahr);
		Integer result = (Integer) qry.uniqueResult();
		if (result == null) {
			result = -1;
		}

		return result + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * db.training.osb.service.BuendelService#createBuendelOnPaketeByVzgStrecke(db.training.osb.
	 * model.VzgStrecke)
	 */
	@Transactional(readOnly = false)
	public void createBuendelOnPaketeByVzgStrecke(VzgStrecke strecke) {

		List<Paket> pakete = findPaketeByVzgStrecke(strecke);
		for (Paket paket : pakete) {
			createBuendelByPaketAndVzgStrecke(paket, strecke);
		}
	}

	@Transactional
	private List<Paket> findPaketeByVzgStrecke(VzgStrecke strecke) {

		if (strecke == null)
			return null;
		if (log.isDebugEnabled())
			log.debug("VzgStreckeID: " + strecke.getId());

		Criteria criteria = getCurrentSession().createCriteria(Paket.class);
		criteria.createAlias("massnahmen", "mn", CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("mn.buendelStrecke", strecke));
		List<Paket> pakete = paketDao.findByCriteria(criteria);

		return pakete;
	}

	@Transactional(readOnly = false)
	private void createBuendelByPaketAndVzgStrecke(Paket paket, VzgStrecke strecke) {

		if (paket == null)
			return;
		if (strecke == null)
			return;
		if (log.isDebugEnabled())
			log.debug("paketID: " + paket.getId() + " streckeID: " + strecke.getId());

		Criteria criteria = getCurrentSession().createCriteria(Gleissperrung.class);
		criteria.createAlias("massnahme", "mn");
		criteria.add(Restrictions.eq("vzgStrecke", strecke));
		criteria.add(Restrictions.eq("mn.paket", paket));
		List<Gleissperrung> gleissperrungen = gleissperrungDao.findByCriteria(criteria);

		// Wenn Gleissperrungen mit Paket und Strecke gefunden wurden, werden diese zu einem neuen
		// Bündel zusammengefasst.
		if (gleissperrungen != null && gleissperrungen.size() > 0) {
			Buendel buendel = null;
			for (Gleissperrung gl : gleissperrungen) {
				if (log.isDebugEnabled())
					log.debug("Zu buendelnde Gleissperrung ID: " + gl.getId());

				// Ist die Gleissperrung bereits einem Buendel zugeordnet, passiert nichts.
				if (gl.getBuendel().size() > 0) {
					if (log.isInfoEnabled())
						log.info("Gleissperrung ID:" + gl.getId() + " bereits gebuendelt.");
				} else {
					if (buendel == null)
						buendel = new Buendel();
					gl.getBuendel().add(buendel);
					buendel.getGleissperrungen().add(gl);
					// TODO: #198 - Fahrplan gibt es nicht
					// if (mn.getUrspruenglichesPlanungsjahr() != null)
					// buendel.setFahrplanjahr(Integer.getInteger(FrontendHelper
					// .castIntegerToString(mn.getUrspruenglichesPlanungsjahr())));
				}
			}
			if (buendel != null) {
				// Falls keine Pflichtwerte gesetzt wurden, werden sie spaetestens hier auf
				// Standardwerte gesetzt.
				// TODO: Standardwerte korrekt?
				if (buendel.getFahrplanjahr() == null) {
					String s = FrontendHelper.castDateToString(new Date(), "yyyy");
					int jahr = FrontendHelper.castStringToInteger(s) + 3;
					buendel.setFahrplanjahr(jahr);
				}
				if (buendel.getLfdNr() == null) {

					if (buendel.getRegionalbereich() == null) {
						for (Gleissperrung gl : buendel.getGleissperrungen()) {
							if (buendel.getRegionalbereich() == null) {
								if (gl.getMassnahme() != null
								    && gl.getMassnahme().getRegionalbereich() != null) {
									buendel.setRegionalbereich(gl.getMassnahme()
									    .getRegionalbereich());
								}
							}
						}
					}

					if (buendel.getRegionalbereich() != null) {
						buendel.setLfdNr(findNextLfdNr(buendel.getRegionalbereich(), buendel
						    .getFahrplanjahr()));
					} else {
						buendel = null;
					}
				}
				RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
				    .createRegionalbereichService();
				if (buendel.getRegionalbereich() == null)
					buendel.setRegionalbereich(regionalbereichService.findAll().get(0));

				buendel.setHauptStrecke(strecke);

				create(buendel);
			}
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public PaginatedList<Buendel> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCriteria, Integer start, Integer count) {

		Criteria criteria = getCurrentSession().createCriteria(Buendel.class);
		addCriteriasToBuendel(criteria, sortOrders, searchCriteria);
		return findPageByCriteria(criteria, start, count, new FetchPlan[] {});
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
	private void addCriteriasToBuendel(Criteria criteria, List<Order> sortOrders,
	    Map<String, Object> searchCriteria) {

		Map<String, Criteria> aliasMap = new HashMap<String, Criteria>();

		/*
		 * Suchkriterien hinzufügen
		 */
		// BuendelName
		Object buendelName = searchCriteria.get(BuendelService.BUENDEL_NAME);
		if (buendelName != null) {
			criteria.add(Restrictions.ilike(BuendelService.BUENDEL_NAME, (String) buendelName,
			    MatchMode.ANYWHERE));
		}
		// Bst von
		Object bstVon = searchCriteria.get(BuendelService.BUENDEL_BST_VON);
		if (bstVon != null) {
			criteria.add(Restrictions.eq("startBahnhof", bstVon));
		}
		// Bst bis
		Object bstBis = searchCriteria.get(BuendelService.BUENDEL_BST_BIS);
		if (bstBis != null) {
			criteria.add(Restrictions.eq("endeBahnhof", bstBis));
		}
		// Buendel-Id
		Object buendelId = searchCriteria.get(BuendelService.BUENDEL_ID);
		if (buendelId != null) {
			Integer regionalbereichId = FrontendHelper.castStringToInteger(((String) buendelId)
			    .substring(0, 2));
			Integer fahrplanjahr = FrontendHelper.castStringToInteger(((String) buendelId)
			    .substring(3, 5));
			fahrplanjahr += 2000;
			Integer lfdNr = FrontendHelper.castStringToInteger(((String) buendelId).substring(6));
			if (regionalbereichId != null && lfdNr != null) {
				if (!aliasMap.containsKey(BuendelService.REGION)) {
					aliasMap.put(BuendelService.REGION, criteria.createAlias(BuendelService.REGION,
					    "region"));
				}
				Criteria regionCrit = aliasMap.get(BuendelService.REGION);
				regionCrit.add(Restrictions.eq("region.id", regionalbereichId));
				criteria.add(Restrictions.eq(BuendelService.FAHRPLANJAHR, fahrplanjahr));
				criteria.add(Restrictions.eq(BuendelService.LFD_NR, lfdNr));
			}
		}
		// Region
		Object region = searchCriteria.get(BuendelService.REGION);
		if (region != null) {
			Integer regionId = (Integer) region;
			if (!aliasMap.containsKey(BuendelService.REGION))
				aliasMap.put(BuendelService.REGION, criteria.createAlias(BuendelService.REGION,
				    "region"));

			Criteria regionCrit = aliasMap.get(BuendelService.REGION);
			regionCrit.add(Restrictions.eq("region.id", regionId));
		}
		// Streckennummer
		Object strecke = searchCriteria.get(BuendelService.VZG_STRECKE);
		if (strecke != null) {
			Integer streckenNr = (Integer) strecke;
			Criteria streckeCrit = criteria.createCriteria(BuendelService.VZG_STRECKE, "strecke");
			streckeCrit.add(Restrictions.eq("strecke.nummer", streckenNr));
		}
		// Fahrplanjahr
		Object fahrPlanJahr = searchCriteria.get(BuendelService.FAHRPLANJAHR);
		if (fahrPlanJahr != null) {
			Integer fahrplanjahr = (Integer) fahrPlanJahr;
			criteria.add(Restrictions.eq(BuendelService.FAHRPLANJAHR, fahrplanjahr));
		}

		/*
		 * Sortierung hinzufügen
		 */
		if (sortOrders != null) {
			for (Order order : sortOrders) {
				// da sich die buendelId zusammensetzt, und kein eigenen Property
				// des Entities Buendel ist, muss die Sortierung hier explizit
				// angegeben werden
				if (order.toString().startsWith("buendelId")) {
					if (order.toString().endsWith("asc")) {
						criteria.addOrder(Order.asc("regionalbereich"));
						criteria.addOrder(Order.asc("fahrplanjahr"));
						criteria.addOrder(Order.asc("lfdNr"));
					} else {
						criteria.addOrder(Order.desc("regionalbereich"));
						criteria.addOrder(Order.desc("fahrplanjahr"));
						criteria.addOrder(Order.desc("lfdNr"));
					}
				} else {
					criteria.addOrder(order);
				}
			}
		}
	}

	/**
	 * findet die naechst hoehere lfd Nummer und speichert das übergebene Buendel
	 * @param buendel
	 *            - das zu speichernde Buendel
	 */
	@Transactional(readOnly = false)
	public void create(Buendel buendel) throws RuntimeException {
		synchronized (this) {
			Integer buendelLfd = findNextLfdNr(buendel.getRegionalbereich(), buendel
			    .getFahrplanjahr());

			buendel.setLfdNr(buendelLfd);
			getDao().save(buendel);
		}
	}
}