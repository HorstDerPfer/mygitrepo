package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Korridor;

public class KorridorDaoImpl extends BasicDaoImp<Korridor, Serializable> implements KorridorDao {

	public KorridorDaoImpl(SessionFactory instance) {
		super(Korridor.class, instance);
	}
}