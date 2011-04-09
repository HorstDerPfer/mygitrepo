package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.babett.Finanztyp;

public class FinanztypDaoImpl extends BasicDaoImp<Finanztyp, Serializable> implements FinanztypDao {

	public FinanztypDaoImpl(SessionFactory sessionFactory) {
		super(Finanztyp.class, sessionFactory);
	}
}