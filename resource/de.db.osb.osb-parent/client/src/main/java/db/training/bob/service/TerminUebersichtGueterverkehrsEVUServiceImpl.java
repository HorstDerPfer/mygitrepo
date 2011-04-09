package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.dao.TerminUebersichtGueterverkehrsEVUDao;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.easy.common.EasyServiceImpl;

public class TerminUebersichtGueterverkehrsEVUServiceImpl extends
    EasyServiceImpl<TerminUebersichtGueterverkehrsEVU, Serializable> implements
    TerminUebersichtGueterverkehrsEVUService {

	public TerminUebersichtGueterverkehrsEVUServiceImpl() {
		super(TerminUebersichtGueterverkehrsEVU.class);
	}

	public TerminUebersichtGueterverkehrsEVUDao getDao() {
		return (TerminUebersichtGueterverkehrsEVUDao) getBasicDao();
	}
}