package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.TerminUebersicht;
import db.training.easy.common.BasicDaoImp;

public class TerminUebersichtDaoImpl extends BasicDaoImp<TerminUebersicht, Serializable> implements
    TerminUebersichtDao {

	public TerminUebersichtDaoImpl(SessionFactory instance) {
		super(TerminUebersicht.class, instance);
	}
}