package db.training.easy.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.service.FetchPlan;
import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.logwrapper.Logger;

/**
 * @author mst
 */
public class BasicServiceImpl<T, ID extends Serializable> implements BasicService<T, ID> {

	private BasicDao<T, ID> dao;

	private static Logger log = Logger.getLogger(BasicServiceImpl.class);

	protected Class<T> clazz;

	public BasicServiceImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	public BasicDao<T, ID> getBasicDao() {
		return dao;
	}

	public void setBasicDao(BasicDao<T, ID> dao) {
		this.dao = dao;
	}

	protected Session getCurrentSession() {
		// muss in den geerbten Klassen implementiert werden
		throw new UnsupportedOperationException("getCurrentSession muss überschrieben werden.");
	}

	/**
	 * @param obj
	 *            Nachzuladendes Entity
	 * @param fetchPlans
	 *            FetchPlan
	 * @deprecated Das Nachladen der Lazy-Properties erfolgt durch PreloadEventListener.
	 * @see PreloadEventListener
	 */
	@Deprecated
	public void fill(T obj, FetchPlan[] fetchPlans) {
		if (fetchPlans == null || obj == null) {
			log.debug("Nothing to fetch.");
			return;
		}
		List<FetchPlan> plans = new ArrayList<FetchPlan>(Arrays.asList(fetchPlans));
		fill(obj, plans);
	}

	/**
	 * @param obj
	 *            Nachzuladendes Entity
	 * @param plans
	 *            FetchPlan
	 * @deprecated Das Nachladen der Lazy-Properties erfolgt durch PreloadEventListener.
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Deprecated
	public void fill(T obj, Collection<FetchPlan> plans) {
		// muss in den geerbten Klassen implementiert werden
		throw new UnsupportedOperationException("fill muss überschrieben werden.");
	}

	/**
	 * initialisiert für jedes Objekt einer Liste alle Abhängigkeiten, für die Fetch-Pläne übergeben
	 * wurden.
	 * 
	 * @param collection
	 *            Liste von Objekten
	 * @param fetchPlans
	 *            FetchPlan
	 * @deprecated Das Nachladen der Lazy-Properties erfolgt durch PreloadEventListener.
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Deprecated
	public void fill(Collection<T> collection, FetchPlan[] fetchPlans) {
		log.debug("Start filling the object graph");
		if (fetchPlans == null || collection == null || collection.isEmpty()) {
			log.debug("Nothing to fetch.");
			return;
		}
		List<FetchPlan> plans = new ArrayList<FetchPlan>(Arrays.asList(fetchPlans));
		fill(collection, plans);
		log.debug("Finished filling the object graph");
	}

	/**
	 * initialisiert für jedes Objekt einer Liste alle Abhängigkeiten, für die Fetch-Pläne übergeben
	 * wurden.
	 * 
	 * @param collection
	 *            Liste von Objekten
	 * @param plans
	 *            FetchPlan
	 * @deprecated Das Nachladen der Lazy-Properties erfolgt durch PreloadEventListener.
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Deprecated
	public void fill(Collection<T> collection, Collection<FetchPlan> plans) {
		if (collection != null && !collection.isEmpty() && plans != null && !plans.isEmpty()) {
			for (T obj : collection) {
				fill(obj, plans);
			}
		}
	}

	@Transactional
	public List<T> findAll() {
		List<T> objects = new ArrayList<T>();
		objects = dao.findAll();
		defaultSort(objects);
		return objects;
	}

	/**
	 * @deprecated {@link BasicServiceImpl#findAll(Preload[])} benutzen
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Deprecated
	@Transactional
	public List<T> findAll(FetchPlan[] fetchPlans) {
		List<T> objects = new ArrayList<T>();

		objects = dao.findAll();
		fill(objects, fetchPlans);
		defaultSort(objects);
		return objects;
	}

	/**
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Override
	@Transactional
	public List<T> findAll(Preload[] preloads) {
		List<T> objects = new ArrayList<T>();

		PreloadEventListener.setPreloads(preloads);
		objects = dao.findAll();
		PreloadEventListener.clearPreloads();

		defaultSort(objects);
		return objects;
	}

	/**
	 * Kann von Servicen ueberschrieben werden, um eine StandardSortierung zu implementieren.
	 * 
	 * @param objects
	 *            - Liste der Objekte
	 */
	protected void defaultSort(List<T> objects) {
		// do nothing here, Wir kennen die Sortierung nicht
	}

	@Transactional
	public T findById(ID id) {
		T object = null;
		object = dao.findById(id);
		return object;
	}

	/**
	 * @deprecated {@link BasicServiceImpl#findById(Serializable, Preload[])} benutzen
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Deprecated
	@Transactional
	public T findById(ID id, FetchPlan[] fetchPlans) {
		T obj = null;

		obj = dao.findById(id);
		fill(obj, fetchPlans);

		return obj;
	}

	/**
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Override
	@Transactional
	public T findById(ID id, Preload[] preloads) {
		T obj = null;

		PreloadEventListener.setPreloads(preloads);
		obj = dao.findById(id);
		PreloadEventListener.clearPreloads();

		return obj;
	}

	@Transactional
	public List<T> findByName(String name) {
		List<T> objects = null;
		objects = dao.findByName(name);
		return objects;
	}

	@Transactional(readOnly = false)
	public void create(T object) {
		dao.save(object);
	}

	@Transactional(readOnly = false)
	public void update(T object) {
		dao.update(object);
	}

	@Transactional(readOnly = false)
	public void delete(T object) {
		dao.delete(object);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public T merge(T object) {
		return (T) getCurrentSession().merge(object);
	}

	/**
	 * @deprecated {@link #findPageByCriteria(Criteria, Integer, Integer, Preload[])} verwenden
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	@Transactional
	public PaginatedList<T> findPageByCriteria(Criteria criteria, Integer start, Integer count,
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
		PaginatedList<T> resultPage = new PaginatedList<T>();
		resultPage.setFullListSize(resultCount);
		if (resultCount > 0) {
			criteria.setProjection(null);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List<T> list = criteria.list();
			fill(list, fetchPlans);
			resultPage.setList(list);
		}
		log.debug("finished loading result list...");

		return resultPage;
	}

	/**
	 * @param criteria
	 *            Suchkriterien
	 * @param start
	 *            Index des ersten Datensatzes in der Ergebnismenge
	 * @param count
	 *            Anzahl der zu ladenden Datensätze
	 * @param preloads
	 *            FetchPläne
	 * @return
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public PaginatedList<T> findPageByCriteria(Criteria criteria, Integer start, Integer count,
	    Preload[] preloads) {
		log.debug("[findPageByCriteria]");
		
		if (criteria == null) {
			criteria = getCurrentSession().createCriteria(clazz);
		}

		PaginatedList<T> resultPage = null;

		resultPage = new PaginatedList<T>();

		// Anzahl der max. möglichen Suchergebnisse
		criteria.setProjection(Projections.countDistinct("id"));
		Integer resultCount = (Integer) criteria.uniqueResult();
		resultPage.setFullListSize(resultCount);

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
		if (resultCount > 0) {
			criteria.setProjection(null);
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			PreloadEventListener.setPreloads(preloads);
			List<T> list = criteria.list();
			PreloadEventListener.clearPreloads();

			resultPage.setList(list);
		}
		log.debug("finished loading result list...");

		return resultPage;
	}

	/**
	 * @param fetchPlans
	 *            Fetch-Pläne
	 * @param criterions
	 *            Suchkriterien
	 * @return
	 * @see {@link PreloadEventListener}, {@link Preload}
	 * @deprecated {@link #findByCriteria(Preload[], Criterion...)} verwenden
	 */
	@Deprecated
	@Transactional
	protected List<T> findByCriteria(FetchPlan[] fetchPlans, Criterion... criterions) {

		if (criterions == null || criterions.length == 0) {
			throw new IllegalArgumentException("Es wurden keine Critierions angegeben.");
		}

		List<T> result = null;

		result = dao.findByCriteria(criterions);

		if (fetchPlans != null && fetchPlans.length > 0) {
			fill(result, fetchPlans);
		}

		return result;
	}

	/**
	 * @param preloads
	 *            Fetch-Pläne
	 * @param criterions
	 *            Suchkriterien
	 * @return
	 * @see {@link PreloadEventListener}, {@link Preload}
	 */
	@Transactional
	protected List<T> findByCriteria(Preload[] preloads, Criterion... criterions) {

		if (criterions == null || criterions.length == 0) {
			throw new IllegalArgumentException("Es wurden keine Critierions angegeben.");
		}

		List<T> result = null;

		PreloadEventListener.setPreloads(preloads);
		result = dao.findByCriteria(criterions);
		PreloadEventListener.clearPreloads();

		return result;
	}

	@Transactional
	protected List<T> findByCriteria(Preload[] preloads, Criteria criteria) {
		if (criteria == null) {
			throw new IllegalArgumentException("criteria");
		}

		List<T> result = null;

		PreloadEventListener.setPreloads(preloads);
		result = dao.findByCriteria(criteria);
		PreloadEventListener.clearPreloads();

		return result;
	}

	@Transactional
	public List<T> findAllValid() {
		if (!(clazz.getSuperclass() == EasyPersistentExpirationObject.class)) {
			return findAll();
		}

		List<T> objects = new ArrayList<T>();

		Calendar today = Calendar.getInstance();
		EasyDateFormat.setToStartOfDay(today);

		Criteria criteria = getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.le("gueltigVon",
		    EasyPersistentExpirationObject.getMaxGueltigBis()));
		criteria.add(Restrictions.ge("gueltigBis", today.getTime()));

		objects = dao.findByCriteria(criteria);

		defaultSort(objects);
		return objects;
	}
}