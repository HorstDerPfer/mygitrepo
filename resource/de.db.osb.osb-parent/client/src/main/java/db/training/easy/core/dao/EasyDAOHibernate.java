package db.training.easy.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import db.training.easy.common.PersistenceException;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.logwrapper.Logger;

/**
 * Elternklasse fuer DAOs, die zur EASY-DB verbinden
 */
public class EasyDAOHibernate implements Serializable {

	/**
	 * serial iD
	 */
	private static final long serialVersionUID = -8546003165830601128L;

	private static Logger log = Logger.getLogger(EasyDAOHibernate.class);

	@SuppressWarnings("unchecked")
	public EasyPersistentObject createObject(Class clazz, EasyPersistentObject object) {
		if (object != null) {
			if (object instanceof EasyPersistentObjectVc) {
				EasyPersistentObjectVc obj = (EasyPersistentObjectVc) object;
				obj.setLastChangeDate(new Date());
				object = obj;
			}
			synchronized (clazz) {
				// object.setId(getNextId(clazz.getSimpleName().toUpperCase()));
				createObject(object);
			}
		}
		return object;
	}

	public void updateObject(EasyPersistentObject object) throws PersistenceException {
		if (object != null) {
			if (object instanceof EasyPersistentObjectVc) {
				EasyPersistentObjectVc obj = (EasyPersistentObjectVc) object;
				obj.setLastChangeDate(new Date());
				object = obj;
			}
			updateObject(object);
		}
	}

	protected Session getCurrentSession() {
		return EasySessionFactory.getInstance().getCurrentSession();
	}

	/**
	 * Holt persistierte Objekte ueber ihre Primaerschluessel
	 * 
	 * @param persistentClass
	 *            Typ der gesuchten Objekte
	 * @param ids
	 *            Coolection der Objekt-IDs
	 * @return Liste der persistierte Objekte
	 */
	@SuppressWarnings("unchecked")
	public List findObjectsByIds(Class persistentClass, Collection ids, final String idColumn) {
		List result = null;
		boolean handleTransaction = true;
		if (getCurrentSession().getTransaction().isActive())
			handleTransaction = false;
		if (handleTransaction)
			getCurrentSession().beginTransaction();
		Criteria crit = getCurrentSession().createCriteria(persistentClass);
		crit.add(Restrictions.in(idColumn, ids));
		result = crit.list();
		if (handleTransaction)
			getCurrentSession().getTransaction().commit();
		return result;
	}

	/**
	 * Loescht persistiertes Objekt
	 * 
	 * @param persistentObject
	 *            persistiertes Objekt das geloescht werden soll
	 */
	public void deleteObject(Object persistentObject) {
		if (persistentObject != null) {
			boolean handleTransaction = true;
			if (getCurrentSession().getTransaction().isActive())
				handleTransaction = false;
			if (handleTransaction)
				getCurrentSession().beginTransaction();
			getCurrentSession().delete(persistentObject);
			if (handleTransaction)
				getCurrentSession().getTransaction().commit();
		}
	}

	/**
	 * Aktualisiert persistiertes Objekt
	 * 
	 * @param persistentObject
	 *            persistiertes Objekt das geaendert werden soll
	 */
	public void updateObject(Object persistentObject) {
		if (persistentObject != null) {

			boolean handleTransaction = true;
			if (getCurrentSession().getTransaction().isActive())
				handleTransaction = false;
			if (handleTransaction)
				getCurrentSession().beginTransaction();
			getCurrentSession().update(persistentObject);
			if (handleTransaction)
				getCurrentSession().getTransaction().commit();

		}
	}

	/**
	 * Speichert persistiertes Objekt
	 * 
	 * @param persistentObject
	 *            persistiertes Objekt das gespeichert werden soll
	 * @return Primaerschluessel (wenn er existiert) des neuen Datensatzes
	 */
	public Serializable createObject(Object persistentObject) {
		boolean handleTransaction = true;
		if (getCurrentSession().getTransaction().isActive())
			handleTransaction = false;
		if (handleTransaction)
			getCurrentSession().beginTransaction();
		Serializable result = null;
		if (persistentObject != null) {
			result = getCurrentSession().save(persistentObject);

		}
		if (handleTransaction)
			getCurrentSession().getTransaction().commit();
		return result;
	}

	/**
	 * Holt alle persistierte Objekte eines Typs und gibt sie sortiert zurueck
	 * 
	 * @param persistentClass
	 *            Typ der Objekte
	 * @param order
	 *            Sortier-Reihenfolge (darf NULL sein)
	 * @return Liste aller Objekte
	 */
	@SuppressWarnings("unchecked")
	public List findAllObjects(Class persistentClass, Order order) {
		return findObjectsByCriteria(persistentClass, null, order, 0);
	}

	/**
	 * Holt alle persistierte Objekte eines Typs
	 * 
	 * @param persistentClass
	 *            Typ der Objekte
	 * @return Liste aller Objekte
	 */
	@SuppressWarnings("unchecked")
	public List findAllObjects(Class persistentClass) {
		return findAllObjects(persistentClass, null);
	}

	/**
	 * Sucht persistierte Objekte ueber spezifizierte Kriterien
	 * 
	 * @param persistentClass
	 *            Typ der gesuchten Objekte
	 * @param criteria
	 *            Suchkriterien (darf NULL oder leer sein)
	 * @return Liste der gesuchten Objekte
	 */
	@SuppressWarnings("unchecked")
	public List findObjectsByCriteria(Class persistentClass, List<Criterion> criteria) {
		return findObjectsByCriteria(persistentClass, criteria, null, 0);
	}

	/**
	 * Sucht persistierte Objekte ueber spezifizierte Kriterien und gibt bestimmte Anzahl der
	 * Objekte zurueck
	 * 
	 * @param persistentClass
	 *            Typ der gesuchten Objekte
	 * @param criteria
	 *            Suchkriterien (darf NULL oder leer sein)
	 * @param maxSize
	 *            Maximale Anzahl der zurueckgegebenen Objekte
	 * @return Liste der gesuchten Objekte
	 */
	@SuppressWarnings("unchecked")
	public List findObjectsByCriteria(Class persistentClass, List<Criterion> criteria, int maxSize) {
		return findObjectsByCriteria(persistentClass, criteria, null, maxSize);
	}

	/**
	 * Sucht persistierte Objekte ueber spezifizierte Kriterien und gibt bestimmte Anzahl der
	 * Objekte sortiert zurueck
	 * 
	 * @param persistentClass
	 *            Typ der gesuchten Objekte
	 * @param criteria
	 *            Suchkriterien (darf NULL oder leer sein)
	 * @param order
	 *            Sortier-Reihenfolge (darf NULL sein)
	 * @param maxSize
	 *            Maximale Anzahl der zurueckgegebenen Objekte
	 * @return Liste der gesuchten Objekte
	 */
	@SuppressWarnings("unchecked")
	public List findObjectsByCriteria(Class persistentClass, List<Criterion> criteria, Order order,
	    int maxSize) {
		List result = null;
		Session session = getCurrentSession();
		// TODO pruefen, wo Transaktion eigentlich gehandhabt werden soll
		// (e-she)
		boolean handleTransaction = true;
		if (session.getTransaction().isActive())
			handleTransaction = false;
		if (handleTransaction)
			session.beginTransaction();
		Criteria sessionCriteria = session.createCriteria(persistentClass);
		// Add criteration
		if (criteria != null) {
			for (int i = 0; i < criteria.size(); i++) {
				sessionCriteria.add(criteria.get(i));
			}
		}
		// Add order
		if (order != null) {
			sessionCriteria.addOrder(order);
		}
		if (maxSize > 0) {
			sessionCriteria.setMaxResults(maxSize);
		}
		// Execute query
		result = sessionCriteria.list();

		if (handleTransaction)
			session.getTransaction().commit();
		return result;
	}

	/**
	 * Holt persistiertes Objekt ueber Typ und Primaerschluessel
	 * 
	 * @return Gesuchtes Objekt
	 * @throws PersistenceException
	 */
	@SuppressWarnings("unchecked")
	public Object findObjectById(Class persistentClass, Serializable id) {

		if (persistentClass == null)
			throw new IllegalArgumentException("class is required.");

		if (id == null)
			throw new IllegalArgumentException("id is required to find object "
			    + persistentClass.getName());
		boolean handleTransaction = true;
		if (getCurrentSession().getTransaction().isActive())
			handleTransaction = false;
		if (handleTransaction)
			getCurrentSession().beginTransaction();
		Object result = null;
		if (log.isDebugEnabled())
			log.debug("find object by id: " + id);
		result = getCurrentSession().load(persistentClass, id);
		if (result == null) {
			// Throw not found exception
			final String errorMessage = "Persistent object of type" + persistentClass.getName()
			    + " by ID: " + id.toString() + " doesn't exist in the database";
			if (log.isWarnEnabled())
				log.warn(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		if (handleTransaction)
			getCurrentSession().getTransaction().commit();
		if (log.isDebugEnabled())
			log.debug("object found: " + result);
		return result;
	}
}