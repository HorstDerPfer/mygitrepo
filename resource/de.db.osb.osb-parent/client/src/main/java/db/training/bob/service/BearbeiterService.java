package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicService;
import db.training.easy.core.model.User;

public interface BearbeiterService extends BasicService<Bearbeiter, Serializable> {

	/**
	 * erstellt eine Auslistung aller Benutzer, eines Regionalbereiches, die zu einer Baumaßnahme
	 * als Bearbeiter (=Favorit) zugeordnet werden können und gibt diese zurück. Die Liste umfasst
	 * alle Benutzer des Regionalbereichs abzüglich derer, die bereits als Bearbeiter eingetragen
	 * sind.
	 * 
	 * @param baumassnahme
	 *            Baumaßnahme, zu der die verfügbaren (=noch nicht zugewiesenen) Bearbeiter
	 *            aufgelistet werden; Wenn <code>null</code>, dann wird eine leere Liste
	 *            zurückgegeben.
	 * @param regionalbereich
	 *            der Regionalbereich, dessen Benutzer aufgelistet werden; Wenn <code>null</code>,
	 *            dann wird eine RB-übergreifende Liste zurückgegeben
	 * @return
	 */
	List<User> listAvailableBearbeiter(Baumassnahme baumassnahme, Regionalbereich regionalbereich);

	/**
	 * erstellt eine Auslistung aller Benutzer, eines Regionalbereiches, die zu einer Baumaßnahme
	 * als Bearbeiter (=Favorit) zugeordnet werden können und gibt diese zurück. Die Liste umfasst
	 * alle Benutzer des Regionalbereichs abzüglich derer, die bereits als Bearbeiter eingetragen
	 * sind.
	 * 
	 * @param baumassnahme
	 *            Baumaßnahme, zu der die verfügbaren (=noch nicht zugewiesenen) Bearbeiter
	 *            aufgelistet werden; Wenn <code>null</code>, dann wird eine leere Liste
	 *            zurückgegeben.
	 * @param regionalbereichId
	 *            die Id des Regionalbereichs, dessen Benutzer aufgelistet werden; Wenn
	 *            <code>null</code>, dann wird eine RB-übergreifende Liste zurückgegeben
	 * @return
	 */
	List<User> listAvailableBearbeiter(Baumassnahme baumassnahme, Integer regionalbereichId);
}
