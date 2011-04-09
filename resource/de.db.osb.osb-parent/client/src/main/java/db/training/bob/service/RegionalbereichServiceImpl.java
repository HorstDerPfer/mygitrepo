package db.training.bob.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.RegionalbereichDao;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Regionalbereich;
import db.training.easy.common.EasyServiceImpl;
import db.training.logwrapper.Logger;

public class RegionalbereichServiceImpl extends EasyServiceImpl<Regionalbereich, Serializable>
    implements RegionalbereichService {

	private static Logger log = Logger.getLogger(RegionalbereichServiceImpl.class);

	public RegionalbereichServiceImpl() {
		super(Regionalbereich.class);
	}

	public RegionalbereichDao getDao() {
		return (RegionalbereichDao) getBasicDao();
	}

	@Override
	public void fill(Regionalbereich rb, Collection<FetchPlan> plans) {
		if (log.isDebugEnabled())
			log.debug("Entering RegionalbereichServiceImpl:fill()");

		Hibernate.initialize(rb);

		// FIXME ich habe einen Null-Check eingebaut, ohne weitere Prüfung, Felix sollte sich das
		// mal anschauen
		if (rb == null || rb.getBearbeitungsbereiche() == null)
			return;
		if (plans.contains(FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH)) {
			Hibernate.initialize(rb.getBearbeitungsbereiche());
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Regionalbereich> findAllAndCache() {

		List<Regionalbereich> result = new ArrayList<Regionalbereich>();

		result = getCurrentSession().createQuery("from Regionalbereich as rb order by rb.name")
		    .setCacheable(true).list();

		return result;
	}

	@Transactional
	public Regionalbereich findByKuerzel(String kuerzel) {
		Regionalbereich rb = null;
		rb = getDao().findByKuerzel(kuerzel);
		return rb;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> findRegionalbereichBmByKeyword(String keyword) {
		List<String> result = null;

		Criteria criteria = getCurrentSession().createCriteria(Baumassnahme.class);
		criteria.setProjection(Projections.distinct(Projections.property("regionalbereichBM")));
		criteria.add(Restrictions.ilike("regionalbereichBM", keyword.toLowerCase() + "",
		    MatchMode.START));
		result = criteria.list();

		return result;
	}
}