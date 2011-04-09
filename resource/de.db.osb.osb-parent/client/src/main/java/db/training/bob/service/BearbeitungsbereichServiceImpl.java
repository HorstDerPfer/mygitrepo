package db.training.bob.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.BearbeitungsbereichDao;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;
import db.training.easy.common.EasyServiceImpl;
import db.training.logwrapper.Logger;

public class BearbeitungsbereichServiceImpl extends
    EasyServiceImpl<Bearbeitungsbereich, Serializable> implements BearbeitungsbereichService {

	private static Logger log = Logger.getLogger(BearbeitungsbereichServiceImpl.class);

	public BearbeitungsbereichServiceImpl() {
		super(Bearbeitungsbereich.class);
	}

	public BearbeitungsbereichDao getDao() {
		return (BearbeitungsbereichDao) getBasicDao();
	}

	@Override
	public void fill(Bearbeitungsbereich bb, Collection<FetchPlan> plans) {
		if (log.isDebugEnabled())
			log.debug("Entering BearbeitungsbereichServiceImpl:fill()");

		if (plans.contains(FetchPlan.BB_REGIONALBEREICH)) {
			Hibernate.initialize(bb.getRegionalbereich());
		}
	}

	@Transactional
	public List<Bearbeitungsbereich> findByRegionalbereich(Regionalbereich id) {
		List<Bearbeitungsbereich> objects = null;
		objects = getDao().findByCriteria(Restrictions.eq("regionalbereich", id));
		return objects;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Bearbeitungsbereich> findAllInUse() {
		List<Bearbeitungsbereich> result = null;
		Session session = getCurrentSession();
		String qryUsedList = "select bb_id from ((select distinct(bearbeitungsbereich_ID) bb_id from bm)union (select distinct(ID) bb_id from bearbeitungsbereich)union (select distinct(bearbeitungsbereich_ID) bb_id from app_user)) used_bb";
		List usedIdList = session.createSQLQuery(qryUsedList).list();
		String usedIds = usedIdList.toString();
		usedIds = usedIds.replace('[', ' ');
		usedIds = usedIds.replace(']', ' ');
		result = session.createQuery("from Bearbeitungsbereich b where b.id in (" + usedIds + ")")
		    .list();
		// result =session.createSQLQuery(qry).list();
		return result;
	}
}