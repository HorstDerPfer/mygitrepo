package db.training.bob.dao.zvf;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.zvf.BBPStrecke;
import db.training.easy.common.BasicDaoImp;

public class BBPStreckeDaoImpl extends BasicDaoImp<BBPStrecke, Serializable> implements
    BBPStreckeDao {

	public BBPStreckeDaoImpl(SessionFactory instance) {
		super(BBPStrecke.class, instance);
	}
}