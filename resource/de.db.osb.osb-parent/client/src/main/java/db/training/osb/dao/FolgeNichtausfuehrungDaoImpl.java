package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.babett.FolgeNichtausfuehrung;

public class FolgeNichtausfuehrungDaoImpl extends BasicDaoImp<FolgeNichtausfuehrung, Serializable>
    implements FolgeNichtausfuehrungDao {

	public FolgeNichtausfuehrungDaoImpl(SessionFactory instance) {
		super(FolgeNichtausfuehrung.class, instance);
	}
}