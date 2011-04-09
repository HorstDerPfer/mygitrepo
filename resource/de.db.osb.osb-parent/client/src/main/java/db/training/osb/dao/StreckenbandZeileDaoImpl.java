package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.Streckenband;
import db.training.osb.model.StreckenbandZeile;

public class StreckenbandZeileDaoImpl extends BasicDaoImp<StreckenbandZeile, Serializable>
    implements StreckenbandZeileDao {

	public StreckenbandZeileDaoImpl(SessionFactory instance) {
		super(StreckenbandZeile.class, instance);
	}

	@Transactional(readOnly = false)
	public void saveAll(Streckenband list) {
		// Daten schreiben
		for (StreckenbandZeile row : list) {
			getCurrentSession().saveOrUpdate(row);
		}
	}

	@Transactional(readOnly = false)
	public void deleteAll(Streckenband list) {
		for (StreckenbandZeile row : list) {
			delete(row);
		}
	}
}