package db.training.osb.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.dao.EasySessionFactory;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.logwrapper.Logger;
import db.training.osb.dao.GleissperrungDao;
import db.training.osb.model.Baustelle;
import db.training.osb.model.Buendel;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.SAPMassnahme;

public class GleissperrungServiceImpl extends EasyServiceImpl<Gleissperrung, Serializable>
    implements GleissperrungService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(GleissperrungServiceImpl.class);

	public GleissperrungServiceImpl() {
		super(Gleissperrung.class);
	}

	public GleissperrungDao getDao() {
		return (GleissperrungDao) getBasicDao();
	}

	@Override
	public void fill(Gleissperrung g, Collection<FetchPlan> plans) {
		plans.remove(FetchPlan.OSB_GLEISSPERRUNG);

		if (plans.contains(FetchPlan.OSB_GLEISSPERRUNG_MASSNAHME)) {
			SAPMassnahmeService service = EasyServiceFactory.getInstance()
			    .createSAPMassnahmeService();
			service.fill(g.getMassnahme(), plans);
		}

		if (plans.contains(FetchPlan.OSB_BUENDEL)) {
			BuendelService buendelService = EasyServiceFactory.getInstance().createBuendelService();
			buendelService.fill(g.getBuendel(), plans);
			plans.remove(FetchPlan.OSB_BUENDEL_GLEISSPERRUNGEN);
		}

		if (plans.contains(FetchPlan.OSB_GLEISSPERRUNG_BAUSTELLEN)) {
			EasyServiceFactory.getInstance().createBaustelleService()
			    .fill(g.getBaustellen(), plans);
		}

		if (plans.contains(FetchPlan.OSB_GLEISSPERRUNG_STRECKE)) {
			Hibernate.initialize(g.getVzgStrecke());
			// EasyServiceFactory.getInstance().createVzgStreckeService().fill(g.getVzgStrecke(),
			// plans);
		}

		if (plans.contains(FetchPlan.OSB_GLEISSPERRUNG_BETRIEBSWEISE)) {
			// EasyServiceFactory.getInstance().createBetriebsweiseService().fill(g.getBetriebsweise(),
			// plans);
			Hibernate.initialize(g.getBetriebsweise());
		}

		if (plans.contains(FetchPlan.OSB_BST_VON)) {
			Hibernate.initialize(g.getBstVon());
		}

		if (plans.contains(FetchPlan.OSB_BST_BIS)) {
			Hibernate.initialize(g.getBstBis());
		}

		if (plans.contains(FetchPlan.OSB_VTR)) {
			Hibernate.initialize(g.getVtr());
		}

		if (plans.contains(FetchPlan.OSB_REGIONALBEREICH)) {
			Hibernate.initialize(g.getMassnahme());

			SAPMassnahmeService mnService = EasyServiceFactory.getInstance()
			    .createSAPMassnahmeService();
			mnService.fill(g.getMassnahme(), plans);
		}
	}

	/**
	 * @deprecated stattdessen {@link GleissperrungService#findByIds(Integer[], Preload[])} benutzen
	 * @param ids
	 * @param fetchPlans
	 * @return
	 */
	@Deprecated
	@Transactional
	public List<Gleissperrung> findByIds(Integer[] ids, FetchPlan[] fetchPlans) {
		List<Gleissperrung> result = getDao().findByCriteria(Restrictions.in("id", ids));
		fill(result, fetchPlans);
		return result;
	}

	@Transactional
	public List<Gleissperrung> findByIds(Integer[] ids, Preload[] preloads) {
		return findByCriteria(preloads, Restrictions.in("id", ids));
	}

	@Transactional
	public PaginatedList<Gleissperrung> findPaginatedBySort(List<Order> sortOrders,
	    Map<String, Object> searchCriteria, Integer start, Integer count, Preload[] preloads,
	    int fahrplanjahr) {

		// Criteria füllen
		Criteria criteria = createCriteria(sortOrders, searchCriteria, fahrplanjahr);
		PaginatedList<Gleissperrung> result = findPageByCriteria(criteria, start, count, preloads);

		return result;
	}

	@Transactional
	public Integer findNextLfdNr(Integer fahrplanjahr) {
		Integer lastLfdNr = null;
		lastLfdNr = getDao().findLastLfdNr(fahrplanjahr);

		if (lastLfdNr == null)
			return 1;
		return lastLfdNr + 1;
	}

	private Criteria createCriteria(List<Order> sortOrders, Map<String, Object> searchCritieria,
	    int fahrplanjahr) {
		Criteria criteria = getCurrentSession().createCriteria(Gleissperrung.class);
		criteria.add(Restrictions.eq("fahrplanjahr", fahrplanjahr));

		if (searchCritieria.containsKey("REGIONALBEREICH")) {
			criteria.createAlias("massnahme", "mn");
			criteria.add(Restrictions.eq("mn.regionalbereich", searchCritieria
			    .get("REGIONALBEREICH")));
		}
		if (searchCritieria.containsKey("VZG_STRECKE")) {
			criteria.add(Restrictions.eq("vzgStrecke", searchCritieria.get("VZG_STRECKE")));
		}
		if (searchCritieria.containsKey("BST_VON")) {
			criteria.add(Restrictions.eq("bstVon", searchCritieria.get("BST_VON")));
		}
		if (searchCritieria.containsKey("BST_BIS")) {
			criteria.add(Restrictions.eq("bstBis", searchCritieria.get("BST_BIS")));
		}

		criteria = applyOrders(criteria, sortOrders);
		return criteria;
	}

	@Transactional(readOnly = false)
	public void attachBaustelle(Gleissperrung gs, Baustelle b) {

		Session session = EasySessionFactory.getInstance().getCurrentSession();

		Gleissperrung gsMerged = (Gleissperrung) session.merge(gs);
		Baustelle baustelleMerged = (Baustelle) session.merge(b);
		gsMerged.getBaustellen().add(baustelleMerged);
		getDao().update(gsMerged);
	}

	private Criteria applyOrders(Criteria criteria, Collection<Order> sortOrders) {
		if (criteria != null && sortOrders != null) {
			for (Order order : sortOrders) {
				criteria.addOrder(order);
			}
		}
		return criteria;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Gleissperrung> findByMassnahme(SAPMassnahme massnahme, Preload[] preloads) {
		List<Gleissperrung> gleissperrungen = new ArrayList<Gleissperrung>();
		Criteria criteria = getCurrentSession().createCriteria(Gleissperrung.class);
		if (massnahme != null)
			criteria.add(Restrictions.eq("massnahme", massnahme));
		else
			criteria.add(Restrictions.isNull("massnahme"));

		PreloadEventListener.setPreloads(preloads);
		gleissperrungen = criteria.list();
		PreloadEventListener.clearPreloads();

		return gleissperrungen;
	}

	/**
	 * findet die naechst hoehere lfd Nummer und speichert die übergebene Gleissperrung
	 * 
	 * @param gl
	 *            - die zu speichernde Gleissperrung
	 */
	@Transactional(readOnly = false)
	public void create(Gleissperrung gl) throws RuntimeException, IllegalArgumentException {
		synchronized (this) {
			if (gl.getFahrplanjahr() != 0 && gl.getFahrplanjahr() != null) {
				Integer lfdNr = findNextLfdNr(gl.getFahrplanjahr());
				gl.setLfdNr(lfdNr);
				getDao().save(gl);
			} else
				throw new IllegalArgumentException("Fahrplanjahr wurde nicht gesetzt!");
		}
	}
}