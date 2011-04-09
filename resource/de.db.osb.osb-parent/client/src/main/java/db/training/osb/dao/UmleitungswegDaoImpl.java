package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Umleitungsweg;

public class UmleitungswegDaoImpl extends BasicDaoImp<Umleitungsweg, Serializable> implements
    UmleitungswegDao {

	public UmleitungswegDaoImpl(SessionFactory instance) {
		super(Umleitungsweg.class, instance);
	}
}