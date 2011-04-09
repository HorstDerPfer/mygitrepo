package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Bearbeiter;
import db.training.easy.common.BasicDaoImp;

public class BearbeiterDaoImpl extends BasicDaoImp<Bearbeiter, Serializable> implements
    BearbeiterDao {

	public BearbeiterDaoImpl(SessionFactory instance) {
		super(Bearbeiter.class, instance);
	}
}