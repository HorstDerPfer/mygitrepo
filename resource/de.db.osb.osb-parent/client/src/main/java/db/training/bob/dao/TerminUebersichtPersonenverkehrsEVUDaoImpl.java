package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.easy.common.BasicDaoImp;

public class TerminUebersichtPersonenverkehrsEVUDaoImpl extends
    BasicDaoImp<TerminUebersichtPersonenverkehrsEVU, Serializable> implements
    TerminUebersichtPersonenverkehrsEVUDao {

	public TerminUebersichtPersonenverkehrsEVUDaoImpl(SessionFactory instance) {
		super(TerminUebersichtPersonenverkehrsEVU.class, instance);
	}
}