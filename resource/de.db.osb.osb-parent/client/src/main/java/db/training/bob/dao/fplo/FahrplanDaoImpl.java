package db.training.bob.dao.fplo;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.fplo.Fahrplan;
import db.training.easy.common.BasicDaoImp;

public class FahrplanDaoImpl extends BasicDaoImp<Fahrplan, Serializable> implements FahrplanDao {

	public FahrplanDaoImpl(SessionFactory instance) {
		super(Fahrplan.class, instance);
	}
}