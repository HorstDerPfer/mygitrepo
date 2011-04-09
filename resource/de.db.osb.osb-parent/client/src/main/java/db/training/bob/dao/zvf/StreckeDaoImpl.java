package db.training.bob.dao.zvf;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.zvf.Strecke;
import db.training.easy.common.BasicDaoImp;

public class StreckeDaoImpl extends BasicDaoImp<Strecke, Serializable> implements StreckeDao {

	public StreckeDaoImpl(SessionFactory instance) {
		super(Strecke.class, instance);
	}
}