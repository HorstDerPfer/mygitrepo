package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Fahrplanregelung;

public class FahrplanregelungDaoImpl extends BasicDaoImp<Fahrplanregelung, Serializable> implements
    FahrplanregelungDao {

	public FahrplanregelungDaoImpl(SessionFactory instance) {
		super(Fahrplanregelung.class, instance);
	}
}