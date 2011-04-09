package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Regelung;
import db.training.easy.common.BasicDaoImp;

public class RegelungDaoImpl extends BasicDaoImp<Regelung, Serializable> implements RegelungDao {

	public RegelungDaoImpl(SessionFactory instance) {
		super(Regelung.class, instance);
	}
}