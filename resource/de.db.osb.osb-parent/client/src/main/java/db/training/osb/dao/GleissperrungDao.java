package db.training.osb.dao;

import java.io.Serializable;

import db.training.easy.common.BasicDao;
import db.training.osb.model.Gleissperrung;

public interface GleissperrungDao extends BasicDao<Gleissperrung, Serializable> {

	public Integer findLastLfdNr(Integer fahrplanjahr);
}