package db.training.easy.core.dao;

import db.training.bob.dao.fplo.ISA_FploDao;
import db.training.bob.dao.fplo.ISA_FploDaoImpl;
import db.training.bob.dao.fplo.ISA_ZugDao;
import db.training.bob.dao.fplo.ISA_ZugDaoImpl;

public class ISADaoFactory {

	public static ISA_ZugDao getISA_ZugDao() {
		return new ISA_ZugDaoImpl(ISASessionFactory.getInstance());
	}

	public static ISA_FploDao getISA_FploDao() {
		return new ISA_FploDaoImpl(ISASessionFactory.getInstance());
	}

}