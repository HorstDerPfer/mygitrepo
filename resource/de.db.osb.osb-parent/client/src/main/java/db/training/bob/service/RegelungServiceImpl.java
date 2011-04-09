package db.training.bob.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.RegelungDao;
import db.training.bob.model.Regelung;
import db.training.easy.common.EasyServiceImpl;

public class RegelungServiceImpl extends EasyServiceImpl<Regelung, Serializable> implements
    RegelungService {

	public RegelungServiceImpl() {
		super(Regelung.class);
	}

	public RegelungDao getDao() {
		return (RegelungDao) getBasicDao();
	}

	@Transactional
	public List<Regelung> findByRegelungId(String regelungId) {
		List<Regelung> reg = null;
		reg = getDao().findByCriteria(Restrictions.eq("regelungId", regelungId));
		return reg;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Integer> findVorgangsNr(String regelungId) {
		Query query = getCurrentSession()
		    .createSQLQuery(
		        "select vorgangsnr from bm inner join bm_bbpmassnahme on bm.id=bm_bbpmassnahme.bm_id inner join bbpmassnahme on bbpmassnahme.id=bm_bbpmassnahme.bbpmassnahmen_id inner join bbpmassnahme_regelung on bbpmassnahme_regelung.bbpmassnahme_id=bbpmassnahme.id inner join regelung on regelung.id=bbpmassnahme_regelung.regelungen_id where regelung.regelungid='"
		            + regelungId + "'");
		List<BigDecimal> queryResult = query.list();
		List<Integer> ints = new ArrayList<Integer>();
		if (queryResult.size() > 0) {
			for (BigDecimal bd : queryResult) {
				ints.add(bd.intValue());
			}
		}
		return ints;

	}
}