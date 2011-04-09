package db.training.bob.dao.zvf;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.zvf.Bahnhof;
import db.training.easy.common.BasicDao;

public interface BahnhofDao extends BasicDao<Bahnhof, Serializable> {

	public Bahnhof findByDs100(String ds100);

	public List<Bahnhof> findByLangname(String langname);
}
