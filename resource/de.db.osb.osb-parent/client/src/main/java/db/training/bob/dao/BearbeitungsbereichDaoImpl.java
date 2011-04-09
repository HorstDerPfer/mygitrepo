package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.easy.common.BasicDaoImp;

public class BearbeitungsbereichDaoImpl extends BasicDaoImp<Bearbeitungsbereich, Serializable>
    implements BearbeitungsbereichDao {

	public BearbeitungsbereichDaoImpl(SessionFactory instance) {
		super(Bearbeitungsbereich.class, instance);
	}
}