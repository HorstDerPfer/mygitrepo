package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.babett.Grossmaschine;

public class GrossmaschineDaoImpl extends BasicDaoImp<Grossmaschine, Serializable> implements
    GrossmaschineDao {

	public GrossmaschineDaoImpl(SessionFactory instance) {
		super(Grossmaschine.class, instance);
	}
}