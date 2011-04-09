package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicService;

public interface RegionalbereichService extends BasicService<Regionalbereich, Serializable> {

	List<Regionalbereich> findAllAndCache();
	
	Regionalbereich findByKuerzel(String kuerzel);
	
	List<String> findRegionalbereichBmByKeyword(String keyword);
	
}
