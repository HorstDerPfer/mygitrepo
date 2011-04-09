package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.zvf.Bahnhof;
import db.training.easy.common.BasicService;

public interface BahnhofService extends BasicService<Bahnhof, Serializable> {

	public Bahnhof findByDs100(String ds100);

	public List<Bahnhof> findByLangname(String langname);
}