package db.training.bob.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.EVUDao;
import db.training.bob.model.EVU;
import db.training.easy.common.EasyServiceImpl;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;

public class EVUServiceImpl extends EasyServiceImpl<EVU, Serializable> implements EVUService {

	public EVUServiceImpl() {
		super(EVU.class);
	}

	public EVUDao getDao() {
		return getDao();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<EVU> findByKeyword(String keyword) {
		List<EVU> list = null;

		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(EVU.class);

		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.ilike("kundenNr", keyword, MatchMode.ANYWHERE));
		or.add(Restrictions.ilike("kurzbezeichnung", keyword, MatchMode.ANYWHERE));
		or.add(Restrictions.ilike("name", keyword, MatchMode.ANYWHERE));
		criteria.add(or);

		list = criteria.setCacheable(true).list();
		return list;
	}

	@Transactional
	public EVU findByKundenNr(String nr) {
		EVU result = null;

		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(EVU.class);
		criteria.add(Restrictions.ilike("kundenNr", nr, MatchMode.EXACT));

		result = (EVU) criteria.setMaxResults(1).uniqueResult();
		return result;
	}

	@Transactional
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EVU> findAllInUse() {
		List<EVU> result = null;

		Session session = getCurrentSession();
		String qryUsedList = "select evu_id from ((select distinct(evu_id) from terminuebersicht_gevu) union (select distinct(evu_id) from terminuebersicht_pevu)) used_evu";

		PreloadEventListener.setPreloads(new Preload[] { new Preload(EVU.class, "evugruppe") });
		List usedIdList = session.createSQLQuery(qryUsedList).list();
		PreloadEventListener.clearPreloads();

		if (usedIdList != null && usedIdList.size() > 0) {
			String usedIds = usedIdList.toString();
			usedIds = usedIds.replace('[', ' ');
			usedIds = usedIds.replace(']', ' ');

			result = session.createQuery("from EVU e where e.id in (" + usedIds + ")").list();

		} else {
			result = new ArrayList<EVU>();
		}
		return result;
	}
}