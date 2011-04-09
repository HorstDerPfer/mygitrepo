package db.training.osb.dao;

import java.io.Serializable;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDao;
import db.training.osb.model.Buendel;

public interface BuendelDao extends BasicDao<Buendel, Serializable> {

	/**
	 * findet die nächste freie, d.h. nicht verwendete laufende Nummer für ein Bündel. Die laufenden
	 * Nummern werden in Anhängigkeit von Regionalbereich und Fahrplanjahr vergeben.
	 * 
	 * @param rb
	 *            der Regionalbereich, in dem die zu bündelnden Maßnahmen liegen
	 * @param fahrplanjahr
	 *            das Fahrplanjahr des Maßnahmenbündels
	 * @return
	 */
	public Integer findNextLfd(Regionalbereich rb, Integer fahrplanjahr);
}