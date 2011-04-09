package db.training.osb.dao;

import java.io.Serializable;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.SessionFactory;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Buendel;

public class BuendelDaoImpl extends BasicDaoImp<Buendel, Serializable> implements BuendelDao {

	public BuendelDaoImpl(SessionFactory instance) {
		super(Buendel.class, instance);
	}

	public Integer findNextLfd(Regionalbereich rb, Integer fahrplanjahr) {
		throw new NotImplementedException();
	}
}