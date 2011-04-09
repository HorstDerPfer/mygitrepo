/**
 * Copyright 2006 laliluna.de
 * Sebastian Hennebrueder
 */
package db.training.easy.common;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author hennebrueder
 * 
 */
public class BasicDaoImp<T, ID extends Serializable> implements BasicDao<T, ID> {

	private SessionFactory sessionFactory;

	protected Class<T> clazz;

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public BasicDaoImp(Class<T> clazz, SessionFactory sessionFactory) {
		this.clazz = clazz;
		this.sessionFactory = sessionFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.dao.hibernate.BasicEasyDao#delete(java.lang.Object)
	 */
	public void delete(T object) {
		getCurrentSession().delete(object);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.dao.hibernate.BasicEasyDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getCurrentSession().createCriteria(clazz).setResultTransformer(
		    Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.dao.hibernate.BasicEasyDao#findByCriteria(org.hibernate.Criteria)
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Criteria criteria) {
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * db.training.easy.core.dao.hibernate.BasicEasyDao#findByCriteria(org.hibernate.criterion.Criterion
	 * [])
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Criterion... criterions) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		for (Criterion c : criterions)
			if (c != null)
				criteria.add(c);
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * db.training.easy.core.dao.hibernate.BasicEasyDao#findByCriteriaOrder(org.hibernate.criterion
	 * .Criterion[])
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteriaOrder(Order order, Criterion... criterions) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		for (Criterion c : criterions)
			if (c != null)
				criteria.add(c);
		criteria.addOrder(order);
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * db.training.easy.core.dao.hibernate.BasicEasyDao#findUniqueByCriteria(org.hibernate.Criteria)
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueByCriteria(Criteria criteria) {
		return (T) criteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * db.training.easy.core.dao.hibernate.BasicEasyDao#findUniqueByCriteria(org.hibernate.criterion
	 * .Criterion[])
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueByCriteria(Criterion... criterions) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		for (Criterion c : criterions)
			if (c != null)
				criteria.add(c);
		return (T) criteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.dao.hibernate.BasicEasyDao#findById(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	public List<T> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.dao.hibernate.BasicEasyDao#reattach(java.lang.Object)
	 */
	public void reattach(T object) {
		getCurrentSession().lock(object, LockMode.NONE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.dao.hibernate.BasicEasyDao#save(java.lang.Object)
	 */
	public void save(T object) {
		getCurrentSession().save(object);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see db.training.easy.core.dao.hibernate.BasicEasyDao#update(java.lang.Object)
	 */
	public void update(T object) {
		getCurrentSession().update(object);

	}

}