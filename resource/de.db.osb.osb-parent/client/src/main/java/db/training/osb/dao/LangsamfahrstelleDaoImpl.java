package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Langsamfahrstelle;

public class LangsamfahrstelleDaoImpl extends BasicDaoImp<Langsamfahrstelle, Serializable>
    implements LangsamfahrstelleDao {

	public LangsamfahrstelleDaoImpl(SessionFactory instance) {
		super(Langsamfahrstelle.class, instance);
	}
}