package db.training.osb.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.EasyDateFormat;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.logwrapper.Logger;
import db.training.osb.dao.AnmelderDao;
import db.training.osb.model.Anmelder;
import db.training.osb.model.comparators.AnmelderNameComparator;

/**
 * @author michels
 * 
 */
public class AnmelderServiceImpl extends EasyServiceImpl<Anmelder, Serializable> implements
    AnmelderService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(AnmelderServiceImpl.class);

	public AnmelderServiceImpl() {
		super(Anmelder.class);
	}

	public AnmelderDao getDao() {
		return (AnmelderDao) getBasicDao();
	}

	@Override
	public void fill(Anmelder obj, Collection<FetchPlan> plans) {
		if (obj == null)
			return;
		if (plans.contains(FetchPlan.OSB_ANMELDER_USER))
			Hibernate.initialize(obj.getUser());
		if (plans.contains(FetchPlan.OSB_ANMELDER_ANMELDERGRUPPE))
			Hibernate.initialize(obj.getAnmeldergruppe());
	}

	@Transactional
	public List<Anmelder> findByGueltigBis(Date gueltigBis, Preload[] preloads) {
		// Uhrzeit von gueltigBis auf 23:59:59 Uhr stellen
		Calendar cal = Calendar.getInstance();
		cal.setTime(gueltigBis);
		EasyDateFormat.setToEndOfDay(cal);
		Criterion c = Restrictions.gt("gueltigBis", cal.getTime());
		return findByCriteria(preloads, c);
	}

	@Transactional
	public List<Anmelder> findByRegionalbereich(Regionalbereich regionalbereich,
	    boolean withNullRegions, Preload[] preloads) {
		List<Anmelder> result = new ArrayList<Anmelder>();
		PreloadEventListener.setPreloads(preloads);
		result = getDao().findByRegionalbereich(regionalbereich, withNullRegions);
		PreloadEventListener.clearPreloads();
		return result;
	}

	@Transactional
	public List<Anmelder> findForSelectList(Integer fahrplanjahr, Regionalbereich regionalbereich,
	    Preload[] preloads) {
		List<Anmelder> anmelder = new ArrayList<Anmelder>();
		// Suchkriterien erstellen
		Criteria criteria = getCurrentSession().createCriteria(Anmelder.class);
		Date minGueltigBis = EasyDateFormat.getFirstDayOfYear(fahrplanjahr);
		criteria.add(Restrictions.gt("gueltigBis", minGueltigBis));
		if (regionalbereich != null) {
			criteria.add(Restrictions.or(Restrictions.or(Restrictions.eq("regionalbereich",
			    regionalbereich), Restrictions.isNull("regionalbereich")), Restrictions.eq(
			    "regionalbereich.id", 10)));
		}
		criteria.add(Restrictions.eq("technischerAnmelder", true));
		// Anmelderliste erzeugen und sortiert zurueckliefern
		anmelder = findByCriteria(preloads, criteria);
		Collections.sort(anmelder, new AnmelderNameComparator());
		return anmelder;
	}

	@Transactional
	public List<Anmelder> findAllAuftraggeber(Preload[] preloads) {
		List<Anmelder> anmelder = new ArrayList<Anmelder>();
		Criteria criteria = getCurrentSession().createCriteria(Anmelder.class);
		criteria.add(Restrictions.eq("auftraggeber", true));
		anmelder = findByCriteria(preloads, criteria);
		return anmelder;
	}

}