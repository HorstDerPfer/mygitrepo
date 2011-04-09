package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.NoResultException;

import db.training.easy.common.BasicService;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.Streckenband;
import db.training.osb.model.StreckenbandZeile;
import db.training.osb.model.VzgStrecke;

public interface StreckenbandService extends BasicService<StreckenbandZeile, Serializable> {

	enum SortDirection {
		MOVE_UP, MOVE_DOWN
	}

	/**
	 * listet alle StreckenbandZeilen(Maßnahmen) eines Streckenbandes auf und sortiert aufsteigend
	 * anhand der Zeilenzahl. Sofern das Streckenband noch nicht erzeugt wurde, werden alle auf der
	 * Strecke liegenden Maßnahmen dem Streckenband hinzugefügt (zufällige Sortierung).
	 * 
	 * @param streckeVzg
	 * @param preloads
	 * @exception IllegalArgumentException
	 * @return
	 */
	Streckenband findByVzgStrecke(VzgStrecke streckeVzg, int fahrplanjahr, Preload[] preloads)
	    throws IllegalArgumentException;

	/**
	 * listet alle StreckenbandZeilen(Maßnahmen) eines Streckenbandes auf und sortiert aufsteigend
	 * anhand der Zeilenzahl. Sofern das Streckenband noch nicht erzeugt wurde, werden alle auf der
	 * Strecke liegenden Maßnahmen dem Streckenband hinzugefügt (zufällige Sortierung).
	 * 
	 * @param streckennummer
	 * @param preloads
	 * @exception IllegalArgumentException
	 * @exception NoResultException
	 * @return
	 */
	Streckenband findByVzgStrecke(Integer streckennummer, int fahrplanjahr, Preload[] preloads)
	    throws IllegalArgumentException, NoResultException;

	/**
	 * listet alle Gewerke auf, die es gibt.
	 * 
	 * @param streckeVzg
	 * @param fahrplanjahr
	 * @return
	 */
	Collection<String> findGewerkeOnStreckenband(VzgStrecke streckeVzg, int fahrplanjahr);

	/**
	 * lädt das Streckenband, auf dem die angegebene StreckenbandZeile liegt und gibt dieses zurück.
	 * 
	 * @param rowId
	 *            ID eines MassnahmeZeile Objekts
	 * @param fahrplanjahr
	 * @return
	 * @throws IllegalArgumentException
	 */
	Streckenband findByZeileId(Integer rowId, int fahrplanjahr) throws IllegalArgumentException;

	Streckenband moveRow(Integer rowId, SortDirection direction, int fahrplanjahr);

	Streckenband moveRow(Streckenband sb, Integer rowId, SortDirection direction);

	void update(Streckenband list);

	void delete(Integer streckennummer, int fahrplanjahr);
}