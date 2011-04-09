package db.training.osb.dao;

import java.io.Serializable;
import java.util.List;

import db.training.easy.common.BasicDao;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.VzgStrecke;

public interface TopProjektDao extends BasicDao<TopProjekt, Serializable> {

	public List<TopProjekt> findByVzgStrecke(VzgStrecke vzgStrecke);
}