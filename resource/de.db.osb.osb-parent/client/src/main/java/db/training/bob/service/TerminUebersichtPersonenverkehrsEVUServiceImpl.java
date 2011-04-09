package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.dao.TerminUebersichtPersonenverkehrsEVUDao;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.easy.common.EasyServiceImpl;

public class TerminUebersichtPersonenverkehrsEVUServiceImpl extends
    EasyServiceImpl<TerminUebersichtPersonenverkehrsEVU, Serializable> implements
    TerminUebersichtPersonenverkehrsEVUService {

	public TerminUebersichtPersonenverkehrsEVUServiceImpl() {
		super(TerminUebersichtPersonenverkehrsEVU.class);
	}

	public TerminUebersichtPersonenverkehrsEVUDao getDao() {
		return (TerminUebersichtPersonenverkehrsEVUDao) getBasicDao();
	}
}