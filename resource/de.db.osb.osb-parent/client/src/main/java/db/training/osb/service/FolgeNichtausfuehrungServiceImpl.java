package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.FolgeNichtausfuehrungDao;
import db.training.osb.model.babett.FolgeNichtausfuehrung;

public class FolgeNichtausfuehrungServiceImpl extends
    EasyServiceImpl<FolgeNichtausfuehrung, Serializable> implements FolgeNichtausfuehrungService {

	public FolgeNichtausfuehrungServiceImpl() {
		super(FolgeNichtausfuehrung.class);
	}

	public FolgeNichtausfuehrungDao getDao() {
		return (FolgeNichtausfuehrungDao) getBasicDao();
	}
}