package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.BBPMassnahme;
import db.training.easy.common.BasicDaoImp;

public class BBPMassnahmeDaoImpl extends BasicDaoImp<BBPMassnahme, Serializable> implements
    BBPMassnahmeDao {

	public BBPMassnahmeDaoImpl(SessionFactory instance) {
		super(BBPMassnahme.class, instance);
	}
}