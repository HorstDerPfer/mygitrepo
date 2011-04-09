package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Gleissperrung;

public class GleissperrungDaoImpl extends BasicDaoImp<Gleissperrung, Serializable> implements
    GleissperrungDao {

	public GleissperrungDaoImpl(SessionFactory instance) {
		super(Gleissperrung.class, instance);
	}

	public Integer findLastLfdNr(Integer fahrplanjahr) {
		return (Integer) getCurrentSession().createQuery(
		    "select max(lfdNr) from Gleissperrung where fahrplanjahr = :fahrplanjahr").setInteger(
		    "fahrplanjahr", fahrplanjahr).uniqueResult();
	}
}