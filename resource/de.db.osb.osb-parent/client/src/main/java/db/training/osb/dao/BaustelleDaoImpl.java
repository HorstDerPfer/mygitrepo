package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Baustelle;

public class BaustelleDaoImpl extends BasicDaoImp<Baustelle, Serializable> implements BaustelleDao {

	public BaustelleDaoImpl(SessionFactory instance) {
		super(Baustelle.class, instance);
	}
}