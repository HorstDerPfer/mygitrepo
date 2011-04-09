package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.EVUGruppe;
import db.training.easy.common.BasicService;

public interface EVUGruppeService extends BasicService<EVUGruppe, Serializable> {

	/**
	 * erstellt eine Liste aller EVU-Gruppen, deren Name mit <code>keyword</code> enthält, oder zu
	 * denen ein EVU gehört, dessen Kundennummer mit <code>keyword</code> enthält, und gibt diese
	 * zurück.
	 * 
	 * @param keyword
	 *            Suchbegriff, wird auf EVUGruppe.name und EVUGruppe.evu.kundenNr angewendet
	 * @return
	 */
	public List<EVUGruppe> findByKeyword(String keyword);

	public EVUGruppe findUniqueByName(String name);

	/**
	 * gibt die Kundengruppe des EVU mit der angegebenen Kundennummer zurück.
	 * 
	 * @param kundennummer
	 * @return
	 */
	public EVUGruppe findByKundenNr(String kundennummer);
}
