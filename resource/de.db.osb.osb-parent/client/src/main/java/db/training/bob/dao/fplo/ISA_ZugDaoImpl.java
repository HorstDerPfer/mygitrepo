package db.training.bob.dao.fplo;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.fplo.ISA_Zug;
import db.training.easy.common.BasicDaoImp;

public class ISA_ZugDaoImpl extends BasicDaoImp<ISA_Zug, Serializable> implements ISA_ZugDao {

	public ISA_ZugDaoImpl(SessionFactory instance) {
		super(ISA_Zug.class, instance);
	}
}