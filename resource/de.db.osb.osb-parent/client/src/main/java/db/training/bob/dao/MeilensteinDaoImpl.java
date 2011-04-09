package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Meilenstein;
import db.training.easy.common.BasicDaoImp;

public class MeilensteinDaoImpl extends BasicDaoImp<Meilenstein, Serializable> implements
    MeilensteinDao {

	public MeilensteinDaoImpl(SessionFactory instance) {
		super(Meilenstein.class, instance);
	}
}