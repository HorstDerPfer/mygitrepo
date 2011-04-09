package db.training.bob.dao.zvf;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.zvf.Knotenzeit;
import db.training.easy.common.BasicDaoImp;

public class KnotenzeitDaoImpl extends BasicDaoImp<Knotenzeit, Serializable> implements
    KnotenzeitDao {

	public KnotenzeitDaoImpl(SessionFactory instance) {
		super(Knotenzeit.class, instance);
	}
}