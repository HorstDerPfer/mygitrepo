package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.EVU;
import db.training.easy.common.BasicService;

public interface EVUService extends BasicService<EVU, Serializable> {

	public List<EVU> findByKeyword(String keyword);

	public EVU findByKundenNr(String nr);

	public List<EVU> findAllInUse();
}
