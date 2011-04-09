package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicService;

public interface BearbeitungsbereichService extends BasicService<Bearbeitungsbereich, Serializable> {

	public List<Bearbeitungsbereich> findByRegionalbereich(Regionalbereich id);

	public List<Bearbeitungsbereich> findAllInUse();
}
