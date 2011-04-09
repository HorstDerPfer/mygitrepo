package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.dao.AenderungDao;
import db.training.bob.model.Aenderung;
import db.training.easy.common.EasyServiceImpl;

public class AenderungServiceImpl extends EasyServiceImpl<Aenderung, Serializable> implements
    AenderungService {

	public AenderungServiceImpl() {
		super(Aenderung.class);
	}

	public AenderungDao getDao() {
		return (AenderungDao) getBasicDao();
	}
}