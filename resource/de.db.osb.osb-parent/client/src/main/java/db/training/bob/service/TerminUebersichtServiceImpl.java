package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.dao.TerminUebersichtDao;
import db.training.bob.model.TerminUebersicht;
import db.training.easy.common.EasyServiceImpl;

public class TerminUebersichtServiceImpl extends EasyServiceImpl<TerminUebersicht, Serializable>
    implements TerminUebersichtService {

	public TerminUebersichtServiceImpl() {
		super(TerminUebersicht.class);
	}

	public TerminUebersichtDao getDao() {
		return (TerminUebersichtDao) getBasicDao();
	}
}