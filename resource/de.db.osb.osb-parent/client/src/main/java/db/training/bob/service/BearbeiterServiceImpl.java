package db.training.bob.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.BearbeiterDao;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Regionalbereich;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.model.User;

public class BearbeiterServiceImpl extends EasyServiceImpl<Bearbeiter, Serializable> implements
    BearbeiterService {

	public BearbeiterServiceImpl() {
		super(Bearbeiter.class);
	}

	public BearbeiterDao getDao() {
		return getDao();
	}

	@Override
	@Transactional
	public List<User> listAvailableBearbeiter(Baumassnahme baumassnahme,
	    Regionalbereich regionalbereich) {

		if (regionalbereich == null) {
			return listAvailableBearbeiter(baumassnahme, (Integer) null);
		}
		return listAvailableBearbeiter(baumassnahme, regionalbereich.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> listAvailableBearbeiter(Baumassnahme baumassnahme, Integer regionalbereichId) {

		if (baumassnahme == null) {
			return new ArrayList<User>();
		}

		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.createAlias("regionalbereich", "rb").add(
		    Restrictions.eq("rb.id", regionalbereichId));

		Conjunction c = Restrictions.conjunction();
		for (Bearbeiter b : baumassnahme.getBearbeiter()) {
			criteria.add(Restrictions.not(Restrictions.eq("id", b.getUser().getId())));
		}
		criteria.add(c);

		criteria.addOrder(Order.asc("name"));
		criteria.addOrder(Order.asc("firstName"));

		return criteria.list();
	}
}