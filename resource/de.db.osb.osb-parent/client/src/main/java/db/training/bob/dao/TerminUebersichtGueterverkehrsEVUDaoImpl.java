package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.easy.common.BasicDaoImp;

public class TerminUebersichtGueterverkehrsEVUDaoImpl extends
    BasicDaoImp<TerminUebersichtGueterverkehrsEVU, Serializable> implements
    TerminUebersichtGueterverkehrsEVUDao {

	public TerminUebersichtGueterverkehrsEVUDaoImpl(SessionFactory instance) {
		super(TerminUebersichtGueterverkehrsEVU.class, instance);
	}
}