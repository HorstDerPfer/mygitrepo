package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.SAPMassnahme;

public class SAPMassnahmeDaoImpl extends BasicDaoImp<SAPMassnahme, Serializable> implements
    SAPMassnahmeDao {

	public SAPMassnahmeDaoImpl(SessionFactory instance) {
		super(SAPMassnahme.class, instance);
	}
}