package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.BetriebsweiseDao;
import db.training.osb.model.Betriebsweise;

public class BetriebsweiseServiceImpl extends EasyServiceImpl<Betriebsweise, Serializable>
    implements BetriebsweiseService {

	public BetriebsweiseServiceImpl() {
		super(Betriebsweise.class);
	}

	public BetriebsweiseDao getDao() {
		return (BetriebsweiseDao) getBasicDao();
	}
}