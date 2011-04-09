package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Streckenband;

public class StreckenbandDaoImpl extends BasicDaoImp<Streckenband, Serializable> implements
    StreckenbandDao {

	public StreckenbandDaoImpl(SessionFactory instance) {
		super(Streckenband.class, instance);
	}
}