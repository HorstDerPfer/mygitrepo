package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.Hibernate;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.dao.LangsamfahrstelleDao;
import db.training.osb.model.Langsamfahrstelle;

public class LangsamfahrstelleServiceImpl extends EasyServiceImpl<Langsamfahrstelle, Serializable>
    implements LangsamfahrstelleService {

	public LangsamfahrstelleServiceImpl() {
		super(Langsamfahrstelle.class);
	}

	public LangsamfahrstelleDao getDao() {
		return (LangsamfahrstelleDao) getBasicDao();
	}

	@Override
	public void fill(Langsamfahrstelle g, Collection<FetchPlan> plans) {
		plans.remove(FetchPlan.OSB_LANGSAMFAHRSTELLE);

		if (plans.contains(FetchPlan.OSB_LANGSAMFAHRSTELLE_MASSNAHME)) {
			EasyServiceFactory.getInstance().createSAPMassnahmeService().fill(g.getMassnahme(),
			    plans);
		}

		if (plans.contains(FetchPlan.OSB_LANGSAMFAHRSTELLE_VZGSTRECKE)) {
			Hibernate.initialize(g.getVzgStrecke());
		}
	}
}