package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.babett.Phase;

public class PhaseDaoImpl extends BasicDaoImp<Phase, Serializable> implements PhaseDao {

	public PhaseDaoImpl(SessionFactory instance) {
		super(Phase.class, instance);
	}
}