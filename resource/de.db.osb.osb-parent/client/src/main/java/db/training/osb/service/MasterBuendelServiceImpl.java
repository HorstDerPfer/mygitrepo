package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.MasterBuendelDao;
import db.training.osb.model.MasterBuendel;

public class MasterBuendelServiceImpl extends EasyServiceImpl<MasterBuendel, Serializable>
    implements MasterBuendelService {

	public MasterBuendelServiceImpl() {
		super(MasterBuendel.class);
	}

	public MasterBuendelDao getDao() {
		return (MasterBuendelDao) getBasicDao();
	}
}