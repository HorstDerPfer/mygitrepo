package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.dao.ArbeitsleistungRegionalbereichDao;
import db.training.bob.model.Benchmark;
import db.training.easy.common.EasyServiceImpl;

public class ArbeitsleistungRegionalbereichServiceImpl extends
    EasyServiceImpl<Benchmark, Serializable> implements ArbeitsleistungRegionalbereichService {

	public ArbeitsleistungRegionalbereichServiceImpl() {
		super(Benchmark.class);
	}

	public ArbeitsleistungRegionalbereichDao getDao() {
		return (ArbeitsleistungRegionalbereichDao) getBasicDao();
	}
}