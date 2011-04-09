package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.zvf.BahnhofDao;
import db.training.bob.model.zvf.Bahnhof;
import db.training.easy.common.EasyServiceImpl;

public class BahnhofServiceImpl extends EasyServiceImpl<Bahnhof, Serializable> implements
    BahnhofService {

	public BahnhofServiceImpl() {
		super(Bahnhof.class);
	}

	public BahnhofDao getDao() {
		return (BahnhofDao) getBasicDao();
	}

	@Transactional
	public Bahnhof findByDs100(String ds100) {
		Bahnhof bhf = null;
		bhf = getDao().findByDs100(ds100);
		return bhf;
	}

	@Transactional
	public List<Bahnhof> findByLangname(String langname) {
		List<Bahnhof> bhf = null;
		bhf = getDao().findByLangname(langname);
		return bhf;
	}
}