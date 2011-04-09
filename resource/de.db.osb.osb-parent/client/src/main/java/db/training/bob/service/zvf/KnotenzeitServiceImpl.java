package db.training.bob.service.zvf;

import java.io.Serializable;

import db.training.bob.dao.zvf.KnotenzeitDao;
import db.training.bob.model.zvf.Knotenzeit;
import db.training.easy.common.EasyServiceImpl;

public class KnotenzeitServiceImpl extends EasyServiceImpl<Knotenzeit, Serializable> implements
    KnotenzeitService {

	public KnotenzeitServiceImpl() {
		super(Knotenzeit.class);
	}

	public KnotenzeitDao getDao() {
		return (KnotenzeitDao) getBasicDao();
	}
}