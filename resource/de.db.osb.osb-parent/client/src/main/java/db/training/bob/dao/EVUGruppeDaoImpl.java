package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.EVUGruppe;
import db.training.easy.common.BasicDaoImp;

public class EVUGruppeDaoImpl extends BasicDaoImp<EVUGruppe, Serializable> implements EVUGruppeDao {

	public EVUGruppeDaoImpl(SessionFactory instance) {
		super(EVUGruppe.class, instance);
	}
}