package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.Hibernate;

import db.training.bob.dao.zvf.StreckeDao;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;

public class StreckeServiceImpl extends EasyServiceImpl<Strecke, Serializable> implements
    StreckeService {

	public StreckeServiceImpl() {
		super(Strecke.class);
	}

	public StreckeDao getDao() {
		return (StreckeDao) getBasicDao();
	}

	@Override
	public void fill(Strecke strecke, Collection<FetchPlan> plans) {
		if (plans.contains(FetchPlan.ZVF_MN_STRECKE_STRECKEVZG)) {
			Hibernate.initialize(strecke.getStreckeVZG());
		}
	}
}