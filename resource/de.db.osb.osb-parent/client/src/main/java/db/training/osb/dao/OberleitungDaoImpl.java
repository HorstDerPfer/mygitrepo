package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Oberleitung;

public class OberleitungDaoImpl extends BasicDaoImp<Oberleitung, Serializable> implements
    OberleitungDao {

	public OberleitungDaoImpl(SessionFactory instance) {
		super(Oberleitung.class, instance);
	}
}