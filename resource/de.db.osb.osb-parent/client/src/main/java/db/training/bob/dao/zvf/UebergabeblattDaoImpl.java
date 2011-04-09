package db.training.bob.dao.zvf;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.easy.common.BasicDaoImp;

public class UebergabeblattDaoImpl extends BasicDaoImp<Uebergabeblatt, Serializable> implements
    UebergabeblattDao {

	public UebergabeblattDaoImpl(SessionFactory instance) {
		super(Uebergabeblatt.class, instance);
	}
}