package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.EVU;
import db.training.easy.common.BasicDaoImp;

public class EVUDaoImpl extends BasicDaoImp<EVU, Serializable> implements EVUDao {

	public EVUDaoImpl(SessionFactory instance) {
		super(EVU.class, instance);
	}
}