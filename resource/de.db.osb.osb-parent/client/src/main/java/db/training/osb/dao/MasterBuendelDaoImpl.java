package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.MasterBuendel;

public class MasterBuendelDaoImpl extends BasicDaoImp<MasterBuendel, Serializable> implements
    MasterBuendelDao {

	public MasterBuendelDaoImpl(SessionFactory instance) {
		super(MasterBuendel.class, instance);
	}
}