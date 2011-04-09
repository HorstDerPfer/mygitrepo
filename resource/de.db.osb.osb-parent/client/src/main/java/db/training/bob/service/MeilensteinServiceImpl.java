package db.training.bob.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import db.training.bob.dao.MeilensteinDao;
import db.training.bob.model.Art;
import db.training.bob.model.Meilenstein;
import db.training.bob.model.Meilensteinbezeichnungen;
import db.training.bob.model.Schnittstelle;
import db.training.easy.common.EasyServiceImpl;

public class MeilensteinServiceImpl extends EasyServiceImpl<Meilenstein, Serializable> implements
    MeilensteinService {

	public MeilensteinServiceImpl() {
		super(Meilenstein.class);
	}

	public MeilensteinDao getDao() {
		return (MeilensteinDao) getBasicDao();
	}

	/**
	 * Berechnet den Soll-Termin eines Meilensteins in Abhängigkeit der Baumassnahmenart, der
	 * Schnittstelle, des Baubeginns und der Bezeichnung des Meilensteins
	 * 
	 * @return Solltermin des Meilensteins, null wenn nicht alle Argumente angegeben sind
	 */
	@Transactional
	public Date getSollTermin(Date baubeginn, Art art, Schnittstelle schnittstelle,
	    Meilensteinbezeichnungen bezeichnungMeilenstein) {

		if (baubeginn == null || art == null || schnittstelle == null
		    || bezeichnungMeilenstein == null)
			return null;

		Meilenstein meilenstein = findByArtSchnittstelleProzesskette(art, schnittstelle, baubeginn,
		    bezeichnungMeilenstein);

		if (meilenstein == null)
			return null;

		return Terminberechnung.calculateSolltermin(baubeginn, meilenstein);
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public Meilenstein findByArtSchnittstelleProzesskette(Art art, Schnittstelle schnittstelle,
	    Date baubeginn, Meilensteinbezeichnungen bezeichnung) {

		Meilenstein meilenstein = null;

		Criteria criteria = getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("art", art));
		criteria.add(Restrictions.eq("schnittstelle", schnittstelle));
		criteria.add(Restrictions.eq("bezeichnung", bezeichnung));

		Disjunction d = Restrictions.disjunction();
		d.add(Restrictions.isNull("gueltigAbBaubeginn"));
		Conjunction c = Restrictions.conjunction();
		c.add(Restrictions.le("gueltigAbBaubeginn", baubeginn));
		c.add(Restrictions.isNotNull("gueltigAbBaubeginn"));
		d.add(c);
		criteria.add(d);
		criteria.addOrder(Order.desc("gueltigAbBaubeginn"));
		criteria.setCacheable(true);
		List result = criteria.list();
		if (result.size() > 0)
			if (result.size() > 1)
				// Es gibt immer genau eine Version des Meilensteins mit
				// "gueltigAbBaubeginn=null". Oracle sortiert nullwerte bei
				// absteigender sortierung zuerst. Da immer der jüngste
				// Meilenstein vor Baubeginn gesucht ist, den zweiten aus der
				// liste auswählen
				meilenstein = (Meilenstein) result.get(1);
			else
				meilenstein = (Meilenstein) result.get(0);

		return meilenstein;
	}
}