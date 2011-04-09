package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDaoImp;

public class RegionalbereichDaoImpl extends BasicDaoImp<Regionalbereich, Serializable> implements
    RegionalbereichDao {

	public RegionalbereichDaoImpl(SessionFactory instance) {
		super(Regionalbereich.class, instance);
	}

	public Regionalbereich findByKuerzel(String kuerzel) {
		return findUniqueByCriteria(Restrictions.eq("kuerzel", kuerzel));
	}
}