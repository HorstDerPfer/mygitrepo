package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Aenderung;
import db.training.easy.common.BasicDaoImp;

public class AenderungDaoImpl extends BasicDaoImp<Aenderung, Serializable> implements AenderungDao {

	public AenderungDaoImpl(SessionFactory instance) {
		super(Aenderung.class, instance);
	}
}