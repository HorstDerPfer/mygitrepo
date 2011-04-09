package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.dao.TerminUebersichtBaubetriebsplanungDao;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.easy.common.EasyServiceImpl;

public class TerminUebersichtBaubetriebsplanungServiceImpl extends
    EasyServiceImpl<TerminUebersichtBaubetriebsplanung, Serializable> implements
    TerminUebersichtBaubetriebsplanungService {

	public TerminUebersichtBaubetriebsplanungServiceImpl() {
		super(TerminUebersichtBaubetriebsplanung.class);
	}

	public TerminUebersichtBaubetriebsplanungDao getDao() {
		return (TerminUebersichtBaubetriebsplanungDao) getBasicDao();
	}
}