package db.training.bob.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.Benchmark;
import db.training.easy.common.BasicDaoImp;

public class ArbeitsleistungRegionalbereichDaoImpl extends BasicDaoImp<Benchmark, Serializable>
    implements ArbeitsleistungRegionalbereichDao {

	public ArbeitsleistungRegionalbereichDaoImpl(SessionFactory instance) {
		super(Benchmark.class, instance);
	}
}