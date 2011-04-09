package db.training.osb.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;
import db.training.easy.common.PersistenceException;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.logwrapper.Logger;
import db.training.osb.dao.BetriebsstelleDao;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.BetriebsstelleRegionalbereichLink;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Streckenband;
import db.training.osb.model.StreckenbandZeile;
import db.training.osb.model.VzgStrecke;

public class BetriebsstelleServiceImpl extends EasyServiceImpl<Betriebsstelle, Serializable>
    implements BetriebsstelleService {

	private static Logger log = Logger.getLogger(BetriebsstelleServiceImpl.class);

	public BetriebsstelleServiceImpl() {
		super(Betriebsstelle.class);
	}

	public BetriebsstelleDao getDao() {
		return (BetriebsstelleDao) getBasicDao();
	}

	@Transactional
	public List<Betriebsstelle> findByKeyword(String keyword, int fahrplanjahr) {
		return findByKeyword(keyword, null, fahrplanjahr);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Betriebsstelle> findByKeyword(String keyword, Integer streckennummer,
	    int fahrplanjahr) {

		Query qry = null;

		if (streckennummer == null) {
			qry = getCurrentSession().getNamedQuery("betriebsstellenByFahrplanjahr");
		} else {
			qry = getCurrentSession().getNamedQuery("betriebsstellenByStreckeAndFahrplanjahr");
			qry.setInteger("strecke", streckennummer);
		}
		qry
		    .setString("kuerzel", FrontendHelper.stringNotNullOrEmpty(keyword) ? keyword + "%"
		        : "%");
		qry.setString("typ", "GP");
		qry.setBoolean("zmst", true);
		qry.setDate("gueltigVon", EasyDateFormat.getLastDayOfYear(fahrplanjahr));
		qry.setDate("gueltigBis", EasyDateFormat.getFirstDayOfYear(fahrplanjahr));

		qry.setCacheable(true);

		List<Betriebsstelle> result = qry.list();

		return result;
	}

	public String castCaptionToKuerzel(String caption) {
		if (caption == null || caption.equals(""))
			return null;
		if (!caption.contains("[") || !caption.contains("]"))
			return null;
		caption = caption.substring(caption.indexOf("[") + 1, caption.indexOf("]"));
		if (caption.length() > 5)
			return null;
		return FrontendHelper.replaceNBSPs(caption);
	}

	public Integer castCaptionWithIDToID(String caption) {
		if (caption == null || caption.equals(""))
			return null;
		if (!caption.contains("[") || !caption.contains("]") || !caption.contains("/"))
			return null;
		caption = caption.substring(caption.indexOf("/") + 1, caption.indexOf("]"));

		return FrontendHelper.castStringToInteger(caption);
	}

	public String castCaptionWithIDToKuerzel(String caption) {
		if (caption == null || caption.equals(""))
			return null;
		if (!caption.contains("[") || !caption.contains("]") || !caption.contains("/"))
			return null;
		caption = caption.substring(1, caption.indexOf("/"));

		return caption;
	}

	@Transactional
	public Betriebsstelle findByKuerzelAndStreckenAndFahrplanjahr(String kuerzel,
	    Set<Integer> streckenNummern, int fahrplanjahr) {
		List<Betriebsstelle> betriebsstellen = new ArrayList<Betriebsstelle>();
		if (streckenNummern.size() > 0) {
			Criteria criteria = getCurrentSession().createCriteria(Betriebsstelle.class);
			criteria.createCriteria("strecken").createCriteria("strecke", "str").add(
			    Restrictions.in("nummer", streckenNummern));
			criteria.add(Restrictions.eq("kuerzel", kuerzel));

			Calendar firstDayOfYear = new GregorianCalendar();
			Calendar lastDayOfYear = new GregorianCalendar();

			firstDayOfYear.setTime(EasyDateFormat.getFirstDayOfYear(fahrplanjahr));
			lastDayOfYear.setTime(EasyDateFormat.getLastDayOfYear(fahrplanjahr));

			criteria.add(Restrictions.le("gueltigVon", lastDayOfYear.getTime()));
			criteria.add(Restrictions.ge("gueltigBis", firstDayOfYear.getTime()));

			criteria.addOrder(Order.asc("kuerzel"));

			betriebsstellen = getDao().findByCriteria(criteria);
		}
		return betriebsstellen.size() > 0 ? betriebsstellen.get(0) : null;
	}

	@Transactional
	public Betriebsstelle findByKuerzelAndFahrplanjahr(String kuerzel, int fahrplanjahr) {
		if (log.isDebugEnabled())
			log.debug("findByKuerzelAndFahrplanjahr(" + kuerzel + ", " + fahrplanjahr + ")");
		Betriebsstelle betriebsstelle = null;
		Criteria criteria = getCurrentSession().createCriteria(Betriebsstelle.class);
		criteria.add(Restrictions.eq("kuerzel", kuerzel));

		Calendar firstDayOfYear = new GregorianCalendar();
		Calendar lastDayOfYear = new GregorianCalendar();

		firstDayOfYear.setTime(EasyDateFormat.getFirstDayOfYear(fahrplanjahr));
		lastDayOfYear.setTime(EasyDateFormat.getLastDayOfYear(fahrplanjahr));

		criteria.add(Restrictions.le("gueltigVon", lastDayOfYear.getTime()));
		criteria.add(Restrictions.ge("gueltigBis", firstDayOfYear.getTime()));

		criteria.addOrder(Order.asc("kuerzel"));

		betriebsstelle = (Betriebsstelle) criteria.setMaxResults(1).uniqueResult();
		return betriebsstelle;
	}

	private List<Betriebsstelle> reduceByStartAndEnde(List<Betriebsstelle> list,
	    Betriebsstelle start, Betriebsstelle ende) {

		Set<Betriebsstelle> removeable = new HashSet<Betriebsstelle>();

		// Liste vom Anfang durchlaufen und Betriebsstellen aus Liste entfernen
		for (Betriebsstelle bst : list) {
			// Abbruch wenn Start-Bst. erreicht
			if (bst.equals(start))
				break;

			if (log.isDebugEnabled())
				log.debug("remove " + bst.getCaption());
			removeable.add(bst);

		}

		// Liste vom Ende durchlaufen und Betriebsstellen aus Liste entfernen
		for (int i = list.size() - 1; i >= 0; i--) {
			Betriebsstelle bst = list.get(i);
			// Abbruch wenn End-Bst. erreicht
			if (bst.equals(ende))
				break;

			if (log.isDebugEnabled())
				log.debug("remove " + bst.getCaption());
			removeable.add(bst);
		}

		// überflüssige Betriebsstellen löschen
		if (!removeable.isEmpty()) {
			list.removeAll(removeable);
		}

		if (log.isDebugEnabled())
			log.debug("Betriebsstellen reduziert " + list.size());
		return list;
	}

	public Betriebsstelle findStartInList(VzgStrecke strecke, List<Betriebsstelle> list,
	    Collection<Gleissperrung> gleissperrungen) {

		BetriebsstelleService bstService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		// ggfs. müssen die Von- und Bis-Betriebsstellen einer Gleissperrung
		// "getauscht" werden, damit sie bzgl. ihrer Lage auf der Strecke
		// (Kilometerangabe) korrekt sortiert sind.
		Set<Betriebsstelle> bstOfGleissperrungen = new HashSet<Betriebsstelle>();
		for (Gleissperrung g : gleissperrungen) {
			// Betriebsstellen muessen neu geladen werden, um sicherzustellen, dass die Strecken mit
			// geladen sind. Muss an dieser Stelle getan werden, da Preload sonst zu Enlosschleife
			// fuehren kann, z.B. in BuendelEditAction
			Betriebsstelle bstVon = g.getBstVon();
			Betriebsstelle bstBis = g.getBstBis();
			if (bstVon != null && !Hibernate.isInitialized(bstVon.getStrecken())) {
				bstVon = bstService.findById(g.getBstVon().getId(), new Preload[] { new Preload(
				    Betriebsstelle.class, "strecken") });
			}
			if (bstBis != null && !Hibernate.isInitialized(bstBis.getStrecken())) {
				bstBis = bstService.findById(g.getBstBis().getId(), new Preload[] { new Preload(
				    Betriebsstelle.class, "strecken") });
			}
			BetriebsstelleVzgStreckeLink linkVon = BetriebsstelleVzgStreckeLink.getLink(bstVon,
			    strecke);
			BetriebsstelleVzgStreckeLink linkBis = BetriebsstelleVzgStreckeLink.getLink(bstBis,
			    strecke);

			if (linkVon != null && linkBis != null)
			{
				if (linkVon.getKm() <= linkBis.getKm() && g.getBstVon() != null) {
					bstOfGleissperrungen.add(g.getBstVon());
				} else if (linkVon.getKm() > linkBis.getKm() && g.getBstBis() != null) {
					bstOfGleissperrungen.add(g.getBstBis());
				}
			}
			
			
		}

		for (Betriebsstelle bst : list) {
			if (bstOfGleissperrungen.contains(bst))
				return bst;
		}

		if (log.isInfoEnabled())
			log.info("Keine Start-Betriebsstelle gefunden.");
		return null;
	}

	public Betriebsstelle findEndInList(VzgStrecke strecke, List<Betriebsstelle> list,
	    Collection<Gleissperrung> gleissperrungen) {

		BetriebsstelleService bstService = EasyServiceFactory.getInstance()
		    .createBetriebsstelleService();

		Set<Betriebsstelle> bstOfGleissperrungen = new HashSet<Betriebsstelle>();
		for (Gleissperrung g : gleissperrungen) {
			// Betriebsstellen muessen neu geladen werden, um sicherzustellen, dass die Strecken mit
			// geladen sind. Muss an dieser Stelle getan werden, da Preload sonst zu Enlosschleife
			// fuehren kann, z.B. in BuendelEditAction
			Betriebsstelle bstVon = g.getBstVon();
			Betriebsstelle bstBis = g.getBstBis();
			if (bstVon != null && !Hibernate.isInitialized(bstVon.getStrecken())) {
				bstVon = bstService.findById(g.getBstVon().getId(), new Preload[] { new Preload(
				    Betriebsstelle.class, "strecken") });
			}
			if (bstBis != null && !Hibernate.isInitialized(bstBis.getStrecken())) {
				bstBis = bstService.findById(g.getBstBis().getId(), new Preload[] { new Preload(
				    Betriebsstelle.class, "strecken") });
			}

			BetriebsstelleVzgStreckeLink linkVon = BetriebsstelleVzgStreckeLink.getLink(bstVon,
			    strecke);
			BetriebsstelleVzgStreckeLink linkBis = BetriebsstelleVzgStreckeLink.getLink(bstBis,
			    strecke);
			if (linkVon != null && linkBis != null)
			{
				if (g.getBstBis() != null && linkVon.getKm() <= linkBis.getKm()) {

					bstOfGleissperrungen.add(g.getBstBis());
				} else if (linkVon.getKm() > linkBis.getKm() && g.getBstVon() != null) {
					bstOfGleissperrungen.add(g.getBstVon());
				}
			}
			
		}

		for (int i = list.size() - 1; i >= 0; i--) {
			Betriebsstelle bst = list.get(i);
			if (bstOfGleissperrungen.contains(bst))
				return bst;
		}

		if (log.isInfoEnabled())
			log.info("Keine End-Betriebsstelle gefunden.");
		return null;
	}

	@Transactional
	public List<BetriebsstelleVzgStreckeLink> findByVzgStreckeReducedAndFahrplanjahr(
	    VzgStrecke streckeVzg, Streckenband sb, int fahrplanjahr) {

		List<Gleissperrung> gleissperrungen = new ArrayList<Gleissperrung>();
		for (StreckenbandZeile zeile : sb) {
			gleissperrungen.add(zeile.getGleissperrung());
		}
		if (log.isDebugEnabled())
			log.debug("Gleissperrungen: " + gleissperrungen.size());

		List<Betriebsstelle> betriebsstellen = findByVzgStreckeNummerAndFahrplanjahr(streckeVzg
		    .getNummer(), fahrplanjahr, false, null);
		if (log.isDebugEnabled())
			log.debug("Betriebsstellen: " + betriebsstellen.size());

		Betriebsstelle start = findStartInList(streckeVzg, betriebsstellen, gleissperrungen);
		if (log.isDebugEnabled())
			log.debug("Betriebsstelle Start: " + (start == null ? null : start.getCaption()));
		Betriebsstelle ende = findEndInList(streckeVzg, betriebsstellen, gleissperrungen);
		if (log.isDebugEnabled())
			log.debug("Betriebsstelle Ende: " + (ende == null ? null : ende.getCaption()));

		List<Betriebsstelle> bstList = reduceByStartAndEnde(betriebsstellen, start, ende);

		List<BetriebsstelleVzgStreckeLink> result = new ArrayList<BetriebsstelleVzgStreckeLink>();
		for (Betriebsstelle bst : bstList) {
			result.add(BetriebsstelleVzgStreckeLink.getLink(bst, streckeVzg));
		}

		return result;
	}

	@Transactional
	public List<Betriebsstelle> findByVzgStreckeNummerAndFahrplanjahr(int nummer, int fahrplanjahr,
	    boolean onlyZugmeldestellen, Preload[] preloads) {
		Calendar firstDayOfYear = new GregorianCalendar();
		Calendar lastDayOfYear = new GregorianCalendar();

		firstDayOfYear.setTime(EasyDateFormat.getFirstDayOfYear(fahrplanjahr));
		lastDayOfYear.setTime(EasyDateFormat.getLastDayOfYear(fahrplanjahr));

		return findByVzgStreckeNummerAndGueltigkeit(nummer, firstDayOfYear.getTime(), lastDayOfYear
		    .getTime(), onlyZugmeldestellen, preloads);
	}

	@Transactional
	public List<Betriebsstelle> findByVzgStreckeNummerAndGueltigkeit(int nummer, Date gueltigVon,
	    Date gueltigBis, boolean onlyZugmeldestellen, Preload[] preloads) {

		Criteria criteria = getCurrentSession().createCriteria(Betriebsstelle.class);

		criteria.createCriteria("strecken", CriteriaSpecification.INNER_JOIN).addOrder(
		    Order.asc("km")).createCriteria("strecke", "v")
		    .add(Restrictions.eq("v.nummer", nummer));

		// Like ist meines erachtes falsch, da sonst Betriebstellen von
		// vzgStrecke 100 und 1000
		// zusammen in einer Liste angezeigt werden koennten. LOTZ
		// .add(Restrictions.sqlRestriction("cast({alias}.nummer as char) like ?",
		// Integer.toString(nummer).concat("%"), Hibernate.STRING));

		// Gueltigkeit der Betriebsstelle
		criteria.add(Restrictions.le("gueltigVon", gueltigBis));
		criteria.add(Restrictions.ge("gueltigBis", gueltigVon));
		// Gueltigkeit der Betriebsstelle auf Strecke
		criteria.add(Restrictions.le("v.gueltigVon", gueltigBis));
		criteria.add(Restrictions.ge("v.gueltigBis", gueltigVon));

		PreloadEventListener.setPreloads(preloads);
		List<Betriebsstelle> result = findByCriteria(new Preload[] {}, criteria);
		PreloadEventListener.clearPreloads();

		// Es werden nur Zugmeldestellen oder die erste oder letzte
		// Betriebsstelle zurueckgegeben
		if (onlyZugmeldestellen) {
			List<Betriebsstelle> removeBs = new ArrayList<Betriebsstelle>();
			for (Betriebsstelle bs : result) {
				if (!bs.isZugmeldestelle() && !bs.equals(result.get(0))
				    && !bs.equals(result.get(result.size() - 1))) {
					removeBs.add(bs);
				}
			}
			result.removeAll(removeBs);
		}

		return result;
	}

	@Override
	@Transactional
	public BetriebsstelleVzgStreckeLink findByVzgStrecke(int betriebsstelleId,
	    int vzgStreckeNummer, Date gueltigVon, Date gueltigBis) {

		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(BetriebsstelleVzgStreckeLink.class);
		criteria.createCriteria("betriebsstelle", CriteriaSpecification.INNER_JOIN).add(
		    Restrictions.idEq(betriebsstelleId));
		criteria.createCriteria("strecke", CriteriaSpecification.INNER_JOIN).add(
		    Restrictions.eq("nummer", vzgStreckeNummer));

		criteria.add(Restrictions.le("gueltigVon", gueltigBis));
		criteria.add(Restrictions.ge("gueltigBis", gueltigVon));

		criteria.setMaxResults(1);

		BetriebsstelleVzgStreckeLink result = (BetriebsstelleVzgStreckeLink) criteria
		    .uniqueResult();

		return result;
	}

	@Override
	@Transactional
	public BetriebsstelleVzgStreckeLink findByVzgStrecke(int id, int nummer, int fahrplanjahr) {

		return findByVzgStrecke(id, nummer, EasyDateFormat.getFirstDayOfYear(fahrplanjahr),
		    EasyDateFormat.getLastDayOfYear(fahrplanjahr));
	}

	@Override
	@Transactional
	public BetriebsstelleRegionalbereichLink findByRegionalbereich(int betriebsstelleId,
	    int regionalbereichId, Date gueltigVon, Date gueltigBis) {

		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(BetriebsstelleRegionalbereichLink.class);
		criteria.createCriteria("bst").add(Restrictions.idEq(betriebsstelleId));
		criteria.createCriteria("regionalbereich").add(Restrictions.idEq(regionalbereichId));

		criteria.add(Restrictions.le("gueltigVon", gueltigBis));
		criteria.add(Restrictions.ge("gueltigBis", gueltigVon));

		criteria.setMaxResults(1);

		BetriebsstelleRegionalbereichLink result = (BetriebsstelleRegionalbereichLink) criteria
		    .uniqueResult();

		return result;
	}

	@Override
	@Transactional
	public BetriebsstelleRegionalbereichLink findByRegionalbereich(int betriebsstelleId,
	    int regionalbereichId, int fahrplanjahr) {
		return findByRegionalbereich(betriebsstelleId, regionalbereichId, EasyDateFormat
		    .getFirstDayOfYear(fahrplanjahr), EasyDateFormat.getLastDayOfYear(fahrplanjahr));
	}

	@Transactional
	public boolean hasMoreThanOne_MutterBst(int strecke, float kmVon, float kmBis)
	    throws PersistenceException {

		Session session = getCurrentSession();

		if (kmVon > kmBis) {
			float temp = kmBis;
			kmBis = kmVon;
			kmVon = temp;
		}

		Query qry = session.getNamedQuery("hasMoreThanOne_MutterBst");
		qry.setInteger("vzgStreckeNummer", strecke);
		qry.setFloat("kmVon", kmVon);
		qry.setFloat("kmBis", kmBis);
		qry.setMaxResults(1);

		Integer result = (Integer) qry.uniqueResult();

		return result != 1;
	}
}