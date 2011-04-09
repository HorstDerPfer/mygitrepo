package db.training.osb.dao;

import java.io.Serializable;
import java.util.List;

import db.training.easy.common.BasicDao;
import db.training.osb.model.Verkehrstageregelung;

public interface VerkehrstageregelungDao extends BasicDao<Verkehrstageregelung, Serializable> {

	public List<Verkehrstageregelung> findByVts(Integer vts);
}