package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.Hibernate;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.dao.OberleitungDao;
import db.training.osb.model.Oberleitung;

public class OberleitungServiceImpl extends EasyServiceImpl<Oberleitung, Serializable> implements
    OberleitungService {

	public OberleitungServiceImpl() {
		super(Oberleitung.class);
	}

	public OberleitungDao getDao() {
		return (OberleitungDao) getBasicDao();
	}

	@Override
	public void fill(Oberleitung g, Collection<FetchPlan> plans) {
		plans.remove(FetchPlan.OSB_OBERLEITUNG);

		if (plans.contains(FetchPlan.OSB_OBERLEITUNG_MASSNAHME)) {
			EasyServiceFactory.getInstance().createSAPMassnahmeService().fill(g.getMassnahme(),
			    plans);
		}

		if (plans.contains(FetchPlan.OSB_OBERLEITUNG_VZGSTRECKE)) {
			Hibernate.initialize(g.getVzgStrecke());
		}
	}
}