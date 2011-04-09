package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Nachbarbahn;
import db.training.easy.common.BasicDaoImp;

public class NachbarbahnDaoImpl extends BasicDaoImp<Nachbarbahn, Serializable> implements
    NachbarbahnDao {

	public NachbarbahnDaoImpl(SessionFactory instance) {
		super(Nachbarbahn.class, instance);
	}
}