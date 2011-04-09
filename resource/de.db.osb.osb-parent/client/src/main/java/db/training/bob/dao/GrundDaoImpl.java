package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Grund;
import db.training.easy.common.BasicDaoImp;

public class GrundDaoImpl extends BasicDaoImp<Grund, Serializable> implements GrundDao {

	public GrundDaoImpl(SessionFactory instance) {
		super(Grund.class, instance);
	}
}