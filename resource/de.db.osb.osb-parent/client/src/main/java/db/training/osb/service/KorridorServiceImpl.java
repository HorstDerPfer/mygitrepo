package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.KorridorDao;
import db.training.osb.model.Korridor;

public class KorridorServiceImpl extends EasyServiceImpl<Korridor, Serializable> implements
    KorridorService {

	public KorridorServiceImpl() {
		super(Korridor.class);
	}

	public KorridorDao getDao() {
		return (KorridorDao) getBasicDao();
	}
}