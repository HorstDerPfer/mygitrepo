package db.training.osb.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.model.Regionalbereich;
import db.training.bob.util.CollectionHelper;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.preload.Preload;
import db.training.hibernate.preload.PreloadEventListener;
import db.training.osb.dao.VzgStreckeDao;
import db.training.osb.model.VzgStrecke;

public class VzgStreckeServiceImpl extends EasyServiceImpl<VzgStrecke, Serializable> implements
    VzgStreckeService {

	public VzgStreckeServiceImpl() {
		super(VzgStrecke.class);
	}

	public VzgStreckeDao getDao() {
		return (VzgStreckeDao) getBasicDao();
	}

	@Transactional
	public List<VzgStrecke> findByNummer(int nummer, int fahrplanjahr,
	    Regionalbereich regionalbereich, boolean exactMatch, Preload[] preloads) {
		Calendar firstDayOfYear = new GregorianCalendar(fahrplanjahr, 0, 1, 0, 0, 0);
		Calendar lastDayOfYear = new GregorianCalendar(fahrplanjahr, 11, 31, 23, 59, 59);

		return findByNummer(nummer, firstDayOfYear.getTime(), lastDayOfYear.getTime(),
		    regionalbereich, exactMatch, preloads);
	}

	@Transactional
	public List<VzgStrecke> findByNummer(int nummer, Date gueltigVon, Date gueltigBis,
	    Regionalbereich regionalbereich, boolean exactMatch, Preload[] preloads) {
		List<VzgStrecke> result = null;

		Criteria criteria = getCurrentSession().createCriteria(VzgStrecke.class);

		if (exactMatch == true) {
			criteria.add(Restrictions.eq("nummer", nummer));
		} else {
			criteria.add(Restrictions.like("nummerAsString", nummer + "", MatchMode.START));
		}

		if (gueltigVon != null)
			criteria.add(Restrictions.le("gueltigVon", gueltigVon));

		if (gueltigBis != null)
			criteria.add(Restrictions.ge("gueltigBis", gueltigBis));

		if (regionalbereich != null) {
			criteria.createCriteria("betriebsstellen", CriteriaSpecification.INNER_JOIN)
			    .createCriteria("betriebsstelle", CriteriaSpecification.INNER_JOIN)
			    .createCriteria("regionalbereiche", CriteriaSpecification.INNER_JOIN)
			    .add(Restrictions.eq("regionalbereich", regionalbereich));
		}

		PreloadEventListener.setPreloads(preloads);
		result = getDao().findByCriteria(criteria);
		PreloadEventListener.clearPreloads();

		return result;
	}

	@Transactional
	public VzgStrecke findByNummer(int nummer, Integer fahrplanjahr, Preload[] preloads) {
		if (fahrplanjahr != null)
			return CollectionHelper.getFirst(findByNummer(nummer, fahrplanjahr, null, true,
			    preloads));

		return CollectionHelper.getFirst(findByNummer(nummer, null, null, null, true, preloads));
	}

	public Integer castCaptionToNummer(String caption) {
		if (caption == null || caption.equals(""))
			return null;
		if (!caption.contains("[") || !caption.contains("]"))
			return null;
		caption = caption.substring(caption.indexOf("[") + 1, caption.indexOf("]"));
		if (caption.length() > 4)
			return null;
		return FrontendHelper.castStringToInteger(caption);
	}

	@Transactional
	public Set<VzgStrecke> findAbzweigendeStrecken(VzgStrecke vzgStrecke, int fahrplanjahr,
	    Regionalbereich regionalbereich, Preload[] preloads) {
		Date firstDayOfYear = EasyDateFormat.getFirstDayOfYear(fahrplanjahr);
		Date lastDayOfYear = EasyDateFormat.getLastDayOfYear(fahrplanjahr);

		return findAbzweigendeStrecken(vzgStrecke, firstDayOfYear, lastDayOfYear, regionalbereich,
		    preloads);
	}

	@Transactional
	public Set<VzgStrecke> findAbzweigendeStrecken(VzgStrecke vzgStrecke, Date gueltigVon,
	    Date gueltigBis, Regionalbereich regionalbereich, Preload[] preloads) {
		Set<VzgStrecke> vzgStrecken = new HashSet<VzgStrecke>();

		vzgStrecken = getDao().findAbzweigendeStrecken(vzgStrecke, gueltigVon, gueltigBis,
		    regionalbereich);

		return vzgStrecken;
	}

	@Transactional
	public Set<VzgStrecke> findByFahrplanjahr(int fahrplanjahr, Preload[] preloads) {
		Date firstDayOfYear = EasyDateFormat.getFirstDayOfYear(fahrplanjahr);
		Date lastDayOfYear = EasyDateFormat.getLastDayOfYear(fahrplanjahr);

		return findByGueltigkeit(firstDayOfYear, lastDayOfYear, preloads);
	}

	@Transactional
	public Set<VzgStrecke> findByGueltigkeit(Date gueltigVon, Date gueltigBis, Preload[] preloads) {
		Set<VzgStrecke> vzgStrecken = new HashSet<VzgStrecke>();

		vzgStrecken = getDao().findByGueltigkeit(gueltigVon, gueltigBis);

		return vzgStrecken;
	}

}