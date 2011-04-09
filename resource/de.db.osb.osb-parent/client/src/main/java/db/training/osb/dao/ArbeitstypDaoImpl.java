package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.babett.Arbeitstyp;

/**
 * @author michels
 * 
 */
public class ArbeitstypDaoImpl extends BasicDaoImp<Arbeitstyp, Serializable> implements
    ArbeitstypDao {

	public ArbeitstypDaoImpl(SessionFactory sessionFactory) {
		super(Arbeitstyp.class, sessionFactory);
	}
}