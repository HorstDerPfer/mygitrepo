package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.logwrapper.Logger;
import db.training.osb.dao.ArbeitstypDao;
import db.training.osb.model.babett.Arbeitstyp;

/**
 * @author michels
 * 
 */
public class ArbeitstypServiceImpl extends EasyServiceImpl<Arbeitstyp, Serializable> implements
    ArbeitstypService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ArbeitstypServiceImpl.class);

	public ArbeitstypServiceImpl() {
		super(Arbeitstyp.class);
	}

	public ArbeitstypDao getDao() {
		return (ArbeitstypDao) getBasicDao();
	}
}