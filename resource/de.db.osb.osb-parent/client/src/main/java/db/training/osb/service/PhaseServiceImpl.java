package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.PhaseDao;
import db.training.osb.model.babett.Phase;

public class PhaseServiceImpl extends EasyServiceImpl<Phase, Serializable> implements PhaseService {

	public PhaseServiceImpl() {
		super(Phase.class);
	}

	public PhaseDao getDao() {
		return (PhaseDao) getBasicDao();
	}
}