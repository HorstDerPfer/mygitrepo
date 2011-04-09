package db.training.easy.common;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import db.training.easy.core.dao.EasySessionFactory;

public class EasyServiceImpl<T, ID extends Serializable> extends BasicServiceImpl<T, ID> {

	public EasyServiceImpl(Class<T> clazz) {
		super(clazz);
	}

	/**
	 * zur Entkopplung von der EasySessionFactory für Unittests
	 */
	protected SessionFactory sessionfactory;

	public void setSessionfactory(SessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	protected Session getCurrentSession() {
		// if (session != null)
		// return session;
		if (sessionfactory != null)
			return sessionfactory.getCurrentSession();
		return EasySessionFactory.getInstance().getCurrentSession();
	}

}
