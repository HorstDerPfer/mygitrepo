package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.easy.common.BasicDaoImp;

public class TerminUebersichtBaubetriebsplanungDaoImpl extends
    BasicDaoImp<TerminUebersichtBaubetriebsplanung, Serializable> implements
    TerminUebersichtBaubetriebsplanungDao {

	public TerminUebersichtBaubetriebsplanungDaoImpl(SessionFactory instance) {
		super(TerminUebersichtBaubetriebsplanung.class, instance);
	}
}