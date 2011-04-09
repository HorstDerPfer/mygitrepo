/**
 * 
 */
package db.training.osb.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicService;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.Anmelder;

/**
 * @author michels
 * 
 */
public interface AnmelderService extends BasicService<Anmelder, Serializable> {

	List<Anmelder> findByGueltigBis(Date gueltigBis, Preload[] preloads);

	public List<Anmelder> findByRegionalbereich(Regionalbereich regionalbereich,
	    boolean withNullRegions, Preload[] preloads);

	/**
	 * Liefert alle Anmelder fuer eine Anmelderauswahlliste zurueck.
	 * 
	 * @param fahrplanjahr
	 *            Der Anmelder muss mindestens einen Tag im Fahrplanjahr gueltig sein
	 * @param regionalbereich
	 *            Optional: Wenn angegeben, werden nur Anmelder dieses Regionalbereichs
	 *            zurueckgegeben, sonst alle
	 * @param preloads
	 * @return
	 */
	public List<Anmelder> findForSelectList(Integer fahrplanjahr, Regionalbereich regionalbereich,
	    Preload[] preloads);

	public List<Anmelder> findAllAuftraggeber(Preload[] preloads);

}
