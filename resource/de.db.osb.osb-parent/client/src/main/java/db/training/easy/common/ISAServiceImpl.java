package db.training.easy.common;

import java.io.Serializable;

import org.hibernate.classic.Session;

import db.training.easy.core.dao.ISASessionFactory;
import db.training.logwrapper.Logger;

public class ISAServiceImpl<T, ID extends Serializable> extends BasicServiceImpl<T, ID> {

	public ISAServiceImpl(Class<T> clazz) {
		super(clazz);
	}

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ISAServiceImpl.class);

	protected Session getCurrentSession() {
		// if (session != null)
		// return session;
		return ISASessionFactory.getInstance().getCurrentSession();
	}

}
