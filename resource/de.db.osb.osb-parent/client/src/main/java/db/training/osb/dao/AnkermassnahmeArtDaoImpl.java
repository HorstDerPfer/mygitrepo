package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.AnkermassnahmeArt;

/**
 * @author AndreasLotz
 * 
 */
public class AnkermassnahmeArtDaoImpl extends BasicDaoImp<AnkermassnahmeArt, Serializable>
    implements AnkermassnahmeArtDao {

	public AnkermassnahmeArtDaoImpl(SessionFactory sessionFactory) {
		super(AnkermassnahmeArt.class, sessionFactory);
	}
}