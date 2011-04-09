package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.dao.UmleitungFahrplanregelungLinkDao;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;

public class UmleitungFahrplanregelungLinkServiceImpl extends
    EasyServiceImpl<UmleitungFahrplanregelungLink, Serializable> implements
    UmleitungFahrplanregelungLinkService {

	public UmleitungFahrplanregelungLinkServiceImpl() {
		super(UmleitungFahrplanregelungLink.class);
	}

	public UmleitungFahrplanregelungLinkDao getDao() {
		return (UmleitungFahrplanregelungLinkDao) getBasicDao();
	}

	@Override
	public void fill(UmleitungFahrplanregelungLink u, Collection<FetchPlan> plans) {

		Hibernate.initialize(u);

		if (plans.contains(FetchPlan.OSB_UML_FPL_FAHRPLANREGELUNG)) {
			FahrplanregelungService service = EasyServiceFactory.getInstance()
			    .createFahrplanregelungService();
			service.fill(u.getFahrplanregelung(), plans);
		}

		if (plans.contains(FetchPlan.OSB_UML_FPL_UMLEITUNG)) {
			UmleitungService service = EasyServiceFactory.getInstance().createUmleitungService();
			service.fill(u.getUmleitung(), plans);
		}
	}

	@Transactional
	public UmleitungFahrplanregelungLink findByUmleitungAndFahrplanregelung(Umleitung umleitung,
	    Fahrplanregelung fahrplanregelung) {
		UmleitungFahrplanregelungLink link = null;
		Criteria criteria = getCurrentSession().createCriteria(UmleitungFahrplanregelungLink.class);
		criteria.add(Restrictions.eq("umleitung", umleitung));
		criteria.add(Restrictions.eq("fahrplanregelung", fahrplanregelung));
		link = (UmleitungFahrplanregelungLink) criteria.uniqueResult();
		return link;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<UmleitungFahrplanregelungLink> findByUmleitung(Umleitung umleitung) {
		List<UmleitungFahrplanregelungLink> links = null;
		Criteria criteria = getCurrentSession().createCriteria(UmleitungFahrplanregelungLink.class);
		criteria.add(Restrictions.eq("umleitung", umleitung));
		links = criteria.list();
		return links;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<UmleitungFahrplanregelungLink> findByFahrplanregelung(
	    Fahrplanregelung fahrplanregelung, FetchPlan[] fetchPlans) {
		List<UmleitungFahrplanregelungLink> links = null;
		Criteria criteria = getCurrentSession().createCriteria(UmleitungFahrplanregelungLink.class);
		criteria.add(Restrictions.eq("fahrplanregelung", fahrplanregelung));
		links = criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
		fill(links, fetchPlans);
		return links;
	}
}