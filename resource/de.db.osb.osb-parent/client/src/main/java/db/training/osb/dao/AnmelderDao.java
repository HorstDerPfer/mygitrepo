package db.training.osb.dao;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDao;
import db.training.osb.model.Anmelder;

public interface AnmelderDao extends BasicDao<Anmelder, Serializable> {

	public List<Anmelder> findByRegionalbereich(Regionalbereich regionalbereich,
	    boolean withNullRegions);
}