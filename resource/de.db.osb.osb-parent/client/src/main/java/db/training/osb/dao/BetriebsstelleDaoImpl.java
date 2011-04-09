package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Betriebsstelle;

public class BetriebsstelleDaoImpl extends BasicDaoImp<Betriebsstelle, Serializable> implements
    BetriebsstelleDao {

	public BetriebsstelleDaoImpl(SessionFactory instance) {
		super(Betriebsstelle.class, instance);
	}
}