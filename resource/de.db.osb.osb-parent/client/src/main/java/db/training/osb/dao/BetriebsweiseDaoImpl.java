package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Betriebsweise;

public class BetriebsweiseDaoImpl extends BasicDaoImp<Betriebsweise, Serializable> implements
    BetriebsweiseDao {

	public BetriebsweiseDaoImpl(SessionFactory instance) {
		super(Betriebsweise.class, instance);
	}
}