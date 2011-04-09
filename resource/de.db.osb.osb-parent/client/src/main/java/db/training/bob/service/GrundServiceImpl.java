package db.training.bob.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.classic.Session;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.GrundDao;
import db.training.bob.model.Grund;
import db.training.easy.common.EasyServiceImpl;

public class GrundServiceImpl extends EasyServiceImpl<Grund, Serializable> implements GrundService {

	public GrundServiceImpl() {
		super(Grund.class);
	}

	public GrundDao getDao() {
		return (GrundDao) getBasicDao();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	@Override
	public List<Grund> findAllInUse() {
		List<Grund> result = null;

		Session session = getCurrentSession();
		String qryUsedList = "select grund_id from ((select distinct(ausfallgrund_id)as grund_id from bm) union (select distinct(grund_id) from aenderung)) used_gruende";
		List usedIdList = session.createSQLQuery(qryUsedList).list();
		if (usedIdList != null && usedIdList.size() > 0) {
			String usedIds = usedIdList.toString();
			usedIds = usedIds.replace('[', ' ');
			usedIds = usedIds.replace(']', ' ');
			result = session.createQuery("from Grund g where g.id in (" + usedIds + ")").list();
		} else {
			result = new ArrayList<Grund>();
		}
		return result;
	}
}