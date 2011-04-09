package db.training.easy.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import db.training.bob.service.FetchPlan;
import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.hibernate.preload.Preload;

public interface BasicService<T, ID extends Serializable> {

	@Deprecated
	void fill(T obj, FetchPlan[] fetchPlans);

	@Deprecated
	void fill(T obj, Collection<FetchPlan> fetchPlans);

	@Deprecated
	void fill(Collection<T> list, FetchPlan[] fetchPlans);

	@Deprecated
	void fill(Collection<T> list, Collection<FetchPlan> fetchPlans);

	List<T> findAll();

	@Deprecated
	List<T> findAll(FetchPlan[] fetchPlans);

	List<T> findAll(Preload[] preloads);

	/**
	 * listet alle Datensätze der Klasse T auf. Wenn die Klasse T von
	 * <code>EasyPersistentExpirationObject</code> erbt, wird dabei die Gültigkeit der Datensätze
	 * ausgewertet. Es werden alle Datensätze zurückgegeben, die am Tag des Funktionsaufruf gültig
	 * sind oder in der Zukunft gültig sein werden. Sofern T nicht von
	 * <code>EasyPersistentExpirationObject</code> erbt, ist die Funktionsweise mit
	 * {@link #findAll()} identisch.
	 * 
	 * @return Auflistung aller gültigen Datensätze von <code>T</code>
	 * @see {@link EasyPersistentExpirationObject}
	 */
	List<T> findAllValid();

	T findById(ID id);

	@Deprecated
	T findById(ID id, FetchPlan[] fetchPlans);

	T findById(ID id, Preload[] preloads);

	List<T> findByName(String name);

	void create(T object);

	void delete(T object);

	void update(T object);

	T merge(T object);
}