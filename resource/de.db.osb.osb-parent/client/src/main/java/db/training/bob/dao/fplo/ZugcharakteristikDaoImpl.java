package db.training.bob.dao.fplo;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.fplo.Zugcharakteristik;
import db.training.easy.common.BasicDaoImp;

public class ZugcharakteristikDaoImpl extends BasicDaoImp<Zugcharakteristik, Serializable>
    implements ZugcharakteristikDao {

	public ZugcharakteristikDaoImpl(SessionFactory instance) {
		super(Zugcharakteristik.class, instance);
	}
}