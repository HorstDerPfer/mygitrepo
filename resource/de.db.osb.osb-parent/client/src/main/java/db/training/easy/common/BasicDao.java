package db.training.easy.common;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public interface BasicDao<T, ID extends Serializable> {

	/**
	 * findet ein Objekt fuer den uebergebenen Primary key, gibt null zurueck, wenn kein Eintrag
	 * gefunden wurde
	 * 
	 * @param id
	 * @return
	 */
	public T findById(ID id);

	/**
	 * findet ein Objekt fuer den uebergebenen Name
	 * 
	 * @param id
	 * @return
	 */
	public List<T> findByName(String name);

	/**
	 * erzeugt ein neues Objekt
	 * 
	 * @param object
	 */
	public void save(T object);

	/**
	 * aktualisiert das uergebene Objekt
	 * 
	 * @param object
	 */
	public void update(T object);

	/**
	 * bindet das uebergebene Objekt an die aktuelle Session
	 * 
	 * @param object
	 */
	public void reattach(T object);

	/**
	 * loescht das uebergebene Objekt
	 * 
	 * @param object
	 */
	public void delete(T object);

	/**
	 * gibt alle Eintraege in der DB zurueck
	 * 
	 * @return
	 */
	public List<T> findAll();

	/**
	 * gibt eine java.util.List mit allen Eintraegen, die mit dem criteria gefunden werden
	 * 
	 * @param criteria
	 * @return
	 */
	public List<T> findByCriteria(Criteria criteria);

	/**
	 * gibt eine java.util.List mit allen Eintraegen, die mit den criterien gefunden werden
	 * 
	 * @param criterions
	 * @return
	 */
	public List<T> findByCriteria(Criterion... criterions);

	/**
	 * gibt eine nach order sortierte java.util.List mit allen Eintraegen, die mit den criterien
	 * gefunden werden
	 * 
	 * @param order
	 * @param criterions
	 * @return
	 */

	public List<T> findByCriteriaOrder(Order order, Criterion... criterions);

	/**
	 * Gibt ein Objekt fuer criteria oder null zurueck. Es wird eine RuntimeException geworfen, wenn
	 * mehrere Datensaetze gefunden werden
	 * 
	 * @param criteria
	 * @return
	 */
	public T findUniqueByCriteria(Criteria criteria);

	/**
	 * Gibt ein Objekt fuer die kriterien oder null zurueck. Es wird eine RuntimeException geworfen,
	 * wenn mehrere Datensaetze gefunden werden
	 * 
	 * @param criterions
	 * @return
	 */
	public T findUniqueByCriteria(Criterion... criterions);
}