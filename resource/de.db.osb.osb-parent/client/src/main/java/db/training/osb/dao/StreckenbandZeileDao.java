package db.training.osb.dao;

import java.io.Serializable;

import db.training.easy.common.BasicDao;
import db.training.osb.model.Streckenband;
import db.training.osb.model.StreckenbandZeile;

public interface StreckenbandZeileDao extends BasicDao<StreckenbandZeile, Serializable> {

	void saveAll(Streckenband list);

	void deleteAll(Streckenband list);
}