package db.training.bob.dao;

import java.io.Serializable;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDao;

public interface RegionalbereichDao extends BasicDao<Regionalbereich, Serializable> {

	public Regionalbereich findByKuerzel(String kuerzel);
}