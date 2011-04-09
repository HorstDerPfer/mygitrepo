package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Baumassnahme;
import db.training.easy.common.BasicDaoImp;

public class BaumassnahmeDaoImpl extends BasicDaoImp<Baumassnahme, Serializable> implements
    BaumassnahmeDao {

	public BaumassnahmeDaoImpl(SessionFactory instance) {
		super(Baumassnahme.class, instance);
	}
}