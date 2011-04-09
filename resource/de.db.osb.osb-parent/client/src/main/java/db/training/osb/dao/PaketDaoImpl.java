package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Paket;

public class PaketDaoImpl extends BasicDaoImp<Paket, Serializable> implements PaketDao {

	public PaketDaoImpl(SessionFactory instance) {
		super(Paket.class, instance);
	}
}