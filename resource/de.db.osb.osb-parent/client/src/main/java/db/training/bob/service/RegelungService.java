package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.Regelung;
import db.training.easy.common.BasicService;

public interface RegelungService extends BasicService<Regelung, Serializable> {

	public List<Regelung> findByRegelungId(String regelungId);

	public List<Integer> findVorgangsNr(String regelungId);
	
}
