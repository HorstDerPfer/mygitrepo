package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Umleitung;

public class UmleitungDaoImpl extends BasicDaoImp<Umleitung, Serializable> implements UmleitungDao {

	public UmleitungDaoImpl(SessionFactory instance) {
		super(Umleitung.class, instance);
	}
}