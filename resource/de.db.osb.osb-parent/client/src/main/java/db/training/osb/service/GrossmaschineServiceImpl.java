package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.GrossmaschineDao;
import db.training.osb.model.babett.Grossmaschine;

public class GrossmaschineServiceImpl extends EasyServiceImpl<Grossmaschine, Serializable>
    implements GrossmaschineService {

	public GrossmaschineServiceImpl() {
		super(Grossmaschine.class);
	}

	public GrossmaschineDao getDao() {
		return (GrossmaschineDao) getBasicDao();
	}
}