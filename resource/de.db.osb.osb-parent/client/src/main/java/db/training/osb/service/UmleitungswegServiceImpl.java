package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.UmleitungswegDao;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Umleitung;
import db.training.osb.model.Umleitungsweg;
import db.training.osb.model.VzgStrecke;

public class UmleitungswegServiceImpl extends EasyServiceImpl<Umleitungsweg, Serializable>
    implements UmleitungswegService {

	public UmleitungswegServiceImpl() {
		super(Umleitungsweg.class);
	}

	public UmleitungswegDao getDao() {
		return (UmleitungswegDao) getBasicDao();
	}

	@Override
	public void fill(Umleitungsweg umleitungsweg, Collection<FetchPlan> plans) {
		if (umleitungsweg != null && plans != null && !plans.isEmpty()) {

			if (plans.contains(FetchPlan.OSB_UMLEITUNGSWEG_UMLEITUNGEN))
				Hibernate.initialize(umleitungsweg.getUmleitungen());
			if (plans.contains(FetchPlan.OSB_UMLEITUNGSWEG_FPL)) {
				Hibernate.initialize(umleitungsweg.getUmleitungen());
				for (Umleitung umleitung : umleitungsweg.getUmleitungen())
					Hibernate.initialize(umleitung.getUmleitungFahrplanregelungLinks());
			}
		}
	}

	@Transactional
	public Umleitungsweg findByBetriebsstellenAndStrecke(Betriebsstelle von, Betriebsstelle bis,
	    VzgStrecke strecke) {
		Umleitungsweg result = null;
		result = (Umleitungsweg) getCurrentSession()
		    .createQuery(
		        "from Umleitungsweg where betriebsstelleVon = :von and betriebsstelleBis = :bis and vzgStrecke = :strecke")
		    .setEntity("von", von).setEntity("bis", bis).setEntity("strecke", strecke)
		    .uniqueResult();
		return result;
	}
}