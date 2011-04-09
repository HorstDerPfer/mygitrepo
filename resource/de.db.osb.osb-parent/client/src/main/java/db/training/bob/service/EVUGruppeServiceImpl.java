package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.EVUGruppeDao;
import db.training.bob.model.EVUGruppe;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.FrontendHelper;

public class EVUGruppeServiceImpl extends EasyServiceImpl<EVUGruppe, Serializable> implements
    EVUGruppeService {

	public EVUGruppeServiceImpl() {
		super(EVUGruppe.class);
	}

	public EVUGruppeDao getDao() {
		return (EVUGruppeDao) getBasicDao();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<EVUGruppe> findByKeyword(String keyword) {
		List<EVUGruppe> list = null;

		Criteria criteria = getCurrentSession().createCriteria(EVUGruppe.class);
		criteria.createAlias("evu", "e");

		Disjunction disjunction = Restrictions.disjunction();
		disjunction
		    .add(Restrictions.ilike("e.kundenNr", keyword.toLowerCase(), MatchMode.ANYWHERE));
		disjunction.add(Restrictions.ilike("name", keyword.toLowerCase(), MatchMode.ANYWHERE));
		criteria.add(disjunction);

		criteria.addOrder(Order.asc("name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		list = criteria.setCacheable(true).list();

		return list;
	}

	@Transactional
	public EVUGruppe findUniqueByName(String name) {
		return (EVUGruppe) getCurrentSession().createCriteria(EVUGruppe.class)
		    .add(Restrictions.ilike("name", name, MatchMode.EXACT)).uniqueResult();
	}

	@Transactional
	public EVUGruppe findByKundenNr(String kundennummer) {
		if (!FrontendHelper.stringNotNullOrEmpty(kundennummer))
			throw new IllegalArgumentException("kundennummer");

		return (EVUGruppe) getCurrentSession().createCriteria(EVUGruppe.class)
		    .createAlias("evu", "member").add(Restrictions.eq("member.kundenNr", kundennummer))
		    .uniqueResult();
	}
}