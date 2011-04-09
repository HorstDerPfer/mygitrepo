package db.training.bob.dao.zvf;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.zvf.Bahnhof;
import db.training.easy.common.BasicDaoImp;

public class BahnhofDaoImpl extends BasicDaoImp<Bahnhof, Serializable> implements BahnhofDao {

	public BahnhofDaoImpl(SessionFactory sessionFactory) {
		super(Bahnhof.class, sessionFactory);
	}

	@Transactional
	public Bahnhof findByDs100(String ds100) {
		return findUniqueByCriteria(Restrictions.eq("ds100", ds100));
	}

	@Transactional
	public List<Bahnhof> findByLangname(String langname) {
		return findByCriteria(Restrictions.eq("langName", langname));
	}
}
