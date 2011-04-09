package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.FinanztypDao;
import db.training.osb.model.babett.Finanztyp;

public class FinanztypServiceImpl extends EasyServiceImpl<Finanztyp, Serializable> implements
    FinanztypService {

	public FinanztypServiceImpl() {
		super(Finanztyp.class);
	}

	public FinanztypDao getDao() {
		return (FinanztypDao) getBasicDao();
	}
}