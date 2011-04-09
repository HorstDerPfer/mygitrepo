package db.training.osb.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicDao;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.VzgStrecke;

public interface VzgStreckeDao extends BasicDao<VzgStrecke, Serializable> {

	public Set<VzgStrecke> findAbzweigendeStrecken(VzgStrecke vzgStrecke, Date gueltigVon,
	    Date gueltigBis, Regionalbereich regionalbereich);

	public Set<VzgStrecke> findByGueltigkeit(Date gueltigVon, Date gueltigBis);

	/**
	 * <p>
	 * gibt eine Liste der VzG-Strecke, sowie aller "anstoßenden"/kreuzenden Strecken mit der
	 * jeweils ersten und letzten Betriebsstelle im Streckenverlauf zurück.
	 * </p>
	 * <ul>
	 * <lh>Sonderfälle:</lh>
	 * <li>Wird keine {@code vzgStrecke} angegeben, werden stattdessen alle VzG-Strecken im
	 * Gültigkeitsbereich ausgelistet.</li>
	 * <li>Wird ein {@code regionalbereich} angegeben, werden nur kreuzende Strecken ermittelt, die
	 * innerhalb dieses Regionalbereichs beginnen (erste Bst.) oder enden (letzte Bst.).</li>
	 * </ul>
	 * 
	 * <p>
	 * <b>Eventuell definierte {@link Preload} werden vor der Ausführung zurückgesetzt.</b>
	 * </p>
	 * 
	 * @param vzgStrecke
	 *            die {@link VzgStrecke}, deren "anstoßende"/kreuzende Strecken auflistet werden
	 * @param regionalbereich
	 *            der {@link Regionalbereich}, dessen Strecken aufgelistet werden
	 * @param gueltigVon
	 *            Beginn des Gültigkeitszeitraums, innerhalb dessen gesucht wird
	 * @param gueltigBis
	 *            Ende des Gültigkeitszeitraums, innerhalb dessen gesucht wird
	 * @return
	 */
	public Set<VzgStrecke> executeGetVzgStreckenverlaufCaptionQuery(VzgStrecke vzgStrecke,
	    Regionalbereich regionalbereich, Date gueltigVon, Date gueltigBis);
}