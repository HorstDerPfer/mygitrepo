package db.training.bob.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.BBPMassnahmeDao;
import db.training.bob.model.BBPMassnahme;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;

public class BBPMassnahmeServiceImpl extends EasyServiceImpl<BBPMassnahme, Serializable> implements
    BBPMassnahmeService {

	private static Logger log = Logger.getLogger(BBPMassnahmeServiceImpl.class);

	public BBPMassnahmeServiceImpl() {
		super(BBPMassnahme.class);
	}

	public BBPMassnahmeDao getDao() {
		return (BBPMassnahmeDao) getBasicDao();
	}

	@Override
	public void fill(BBPMassnahme mn, Collection<FetchPlan> plans) {

		if (log.isDebugEnabled())
			log.debug("Entering BBPMassnahmeServiceImpl:fill()");

		Hibernate.initialize(mn);

//		if (plans.contains(FetchPlan.BBP_REGIONALBEREICH)) {
//			Hibernate.initialize(mn.getRegionalbereich());
//
//			if (plans.contains(FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH)) {
//				RegionalbereichService rbService = EasyServiceFactory.getInstance()
//				    .createRegionalbereichService();
//				rbService.fill(mn.getRegionalbereich(), plans);
//			}
//		}

		if (plans.contains(FetchPlan.BBP_REGELUNGEN)) {
			Hibernate.initialize(mn.getRegelungen());
		}
	}

	@Transactional
	public List<BBPMassnahme> findByMasId(String masId, FetchPlan[] fetchPlans) {
		List<BBPMassnahme> bbps = null;
		bbps = getDao().findByCriteria(Restrictions.eq("masId", masId));
		fill(bbps, fetchPlans);
		return bbps;
	}
}