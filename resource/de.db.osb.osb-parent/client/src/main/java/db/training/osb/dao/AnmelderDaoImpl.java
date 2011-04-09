package db.training.osb.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Anmelder;

/**
 * @author michels
 * 
 */
public class AnmelderDaoImpl extends BasicDaoImp<Anmelder, Serializable> implements AnmelderDao {

	public AnmelderDaoImpl(SessionFactory sessionFactory) {
		super(Anmelder.class, sessionFactory);
	}

	@SuppressWarnings("unchecked")
	public List<Anmelder> findByRegionalbereich(Regionalbereich regionalbereich,
	    boolean withNullRegions) {
		String sql = "from Anmelder where regionalbereich = :regionalbereich";
		if (withNullRegions)
			sql += " or regionalbereich is null";
		return getCurrentSession().createQuery(sql)
		    .setParameter("regionalbereich", regionalbereich).list();
	}
}