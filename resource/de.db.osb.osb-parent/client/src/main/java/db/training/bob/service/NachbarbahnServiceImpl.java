package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.dao.NachbarbahnDao;
import db.training.bob.model.Nachbarbahn;
import db.training.easy.common.EasyServiceImpl;

public class NachbarbahnServiceImpl extends EasyServiceImpl<Nachbarbahn, Serializable> implements
    NachbarbahnService {

	public NachbarbahnServiceImpl() {
		super(Nachbarbahn.class);
	}

	public NachbarbahnDao getDao() {
		return (NachbarbahnDao) getBasicDao();
	}
}