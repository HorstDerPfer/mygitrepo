package db.training.bob.service.zvf;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.QueryException;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.zvf.ZugDao;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.SearchBean;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.RegelungAbw;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.report.SearchBeanHelper;
import db.training.bob.web.statistics.zvf.BbzrAbweichungZuegeNummernResultBean;
import db.training.easy.common.EasyServiceImpl;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class ZugServiceImpl extends EasyServiceImpl<Zug, Serializable> implements ZugService {

	public ZugServiceImpl() {
		super(Zug.class);
	}

	public ZugDao getDao() {
		return (ZugDao) getBasicDao();
	}

	@Override
	public void fill(Zug zug, Collection<FetchPlan> plans) {

		Hibernate.initialize(zug);

		if (plans.contains(FetchPlan.UEB_KNOTENZEITEN)) {
			Hibernate.initialize(zug.getKnotenzeiten());
		}

		if (plans.contains(FetchPlan.UEB_BEARBEITUNGSSTATUS)) {
			Hibernate.initialize(zug.getBearbeitungsStatusMap());
		}

		if (zug.getAbweichung().getArt().equals(Abweichungsart.UMLEITUNG)
		    || (plans.contains(FetchPlan.ZVF_ZUG_UMLEITWEG))) {
			Hibernate.initialize(zug.getAbweichung().getUmleitweg());
		}

		if (zug.getAbweichung().getArt().equals(Abweichungsart.ERSATZHALTE)
		    || (plans.contains(FetchPlan.ZVF_ZUG_ABWEICHUNG_HALT) || plans
		        .contains(FetchPlan.UEB_ZUG_ABWEICHUNG_HALT_BAHNHOF))) {

			Hibernate.initialize(zug.getAbweichung());
			Hibernate.initialize(zug.getAbweichung().getUmleitweg());
			Hibernate.initialize(zug.getAbweichung().getAusfallvon());
			Hibernate.initialize(zug.getAbweichung().getAusfallbis());
			Hibernate.initialize(zug.getAbweichung().getHalt());
			Hibernate.initialize(zug.getAbweichung().getVorplanab());

			if (plans.contains(FetchPlan.UEB_ZUG_ABWEICHUNG_HALT_BAHNHOF)) {
				for (Halt h : zug.getAbweichung().getHalt()) {
					Hibernate.initialize(h.getErsatz());
					Hibernate.initialize(h.getAusfall());
				}
			}
		}

		if (zug.getAbweichung().getArt().equals(Abweichungsart.REGELUNG)
		    || (plans.contains(FetchPlan.ZVF_ZUG_ABWEICHUNG_REGELUNG))) {

			Hibernate.initialize(zug.getAbweichung());
			Hibernate.initialize(zug.getAbweichung().getRegelungen());

			for (RegelungAbw reg : zug.getAbweichung().getRegelungen()) {
				Hibernate.initialize(reg.getGiltIn());
			}
		}

		if (plans.contains(FetchPlan.UEB_ZUG_REGELWEG)) {
			Hibernate.initialize(zug.getRegelweg());
			if (zug.getRegelweg().getAbgangsbahnhof() != null)
				Hibernate.initialize(zug.getRegelweg().getAbgangsbahnhof());
			if (zug.getRegelweg().getZielbahnhof() != null)
				Hibernate.initialize(zug.getRegelweg().getZielbahnhof());
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<BbzrAbweichungZuegeNummernResultBean> findBbzrMassnahmenByBaumassnahmen(
	    SearchBean searchBean, Preload[] preloads) {

		Logger log = Logger.getLogger(ZugServiceImpl.class);

		// Baumassnahmen auflisten
		log.debug("BaumassnahmenIds abfragen...");
		Criteria criteria = getCurrentSession().createCriteria(Baumassnahme.class);
		criteria = SearchBeanHelper.fillCriteriaFromBaumassnahmeSearchBean(criteria, searchBean);
		criteria.setProjection(Projections.distinct(Projections.id()));
		List<Integer> baumassnahmenIds = criteria.list();

		if (log.isDebugEnabled()) {
			log.debug("fertig. gefundene Baumaßnahmen: " + baumassnahmenIds.size());
			// for (Integer i : baumassnahmenIds)
			// log.debug(i + ", ");
		}

		if (baumassnahmenIds.size() > 1000)
			throw new QueryException("Die Abfrage lieferte " + baumassnahmenIds.size()
			    + " Ergebnisse (max. 1000 erlaubt!).");

		// ZugDao
		log.debug("Zugdaten abfragen...");
		Date verkehrstagBeginn = FrontendHelper.castStringToDate(searchBean
		    .getSearchVerkehrstagBeginnDatum());
		Date verkehrstagEnde = FrontendHelper.castStringToDate(searchBean
		    .getSearchVerkehrstagEndDatum());

		Calendar cal = GregorianCalendar.getInstance();

		if (verkehrstagBeginn != null) {
			cal.setTime(verkehrstagBeginn);
			EasyDateFormat.setToStartOfDay(cal);
			verkehrstagBeginn = cal.getTime();
		}

		if (verkehrstagEnde != null) {
			cal.setTime(verkehrstagEnde);
			EasyDateFormat.setToEndOfDay(cal);
			verkehrstagEnde = cal.getTime();
		}

		List<BbzrAbweichungZuegeNummernResultBean> result = getDao()
		    .findBbzrMassnahmenByBaumassnahmen(baumassnahmenIds, verkehrstagBeginn,
		        verkehrstagEnde, Integer.getInteger(searchBean.getQsks()));
		log.debug("fertig.");
		log.debug("Anzahl gefundene Züge: " + result.size());

		return result;
	}
}