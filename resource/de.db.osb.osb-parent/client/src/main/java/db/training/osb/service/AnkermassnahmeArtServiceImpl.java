package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.EasyServiceImpl;
import db.training.logwrapper.Logger;
import db.training.osb.dao.AnkermassnahmeArtDao;
import db.training.osb.model.AnkermassnahmeArt;

/**
 * @author AndreasLotz
 * 
 */
public class AnkermassnahmeArtServiceImpl extends EasyServiceImpl<AnkermassnahmeArt, Serializable>
    implements AnkermassnahmeArtService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(AnkermassnahmeArtServiceImpl.class);

	public AnkermassnahmeArtServiceImpl() {
		super(AnkermassnahmeArt.class);
	}

	public AnkermassnahmeArtDao getDao() {
		return (AnkermassnahmeArtDao) getBasicDao();
	}
}