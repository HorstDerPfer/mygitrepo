package db.training.osb.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import db.training.bob.model.Regionalbereich;
import db.training.easy.common.BasicService;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.VzgStrecke;

public interface VzgStreckeService extends BasicService<VzgStrecke, Serializable> {

	/**
	 * Liefert Strecken anhand der Nummer zurueck.
	 * 
	 * @param nummer
	 * @param fahrplanjahr
	 * @param preloads
	 * @return vzgStrecken
	 */
	public VzgStrecke findByNummer(int nummer, Integer fahrplanjahr, Preload[] preloads);

	/**
	 * Liefert Strecken anhand der VzG-Streckennummer zurück. Bei der Suche kann angegeben werden,
	 * ob die Streckennummer exakt dem Suchwert entsprechen muss, oder nicht. Die Suche kann durch
	 * Angabe von Parametern eingeschränkt werden.
	 * 
	 * @param nummer
	 *            VzG-Streckennummer
	 * @param fahrplanjahr
	 * @param regionalbereich
	 * @param exactMatch
	 *            genaue Übereinstimmung der Streckennummer (<code>true</code>); Ähnlichkeitssuche (
	 *            <code>false</code>)
	 * @param preloads
	 * @return
	 */
	List<VzgStrecke> findByNummer(int nummer, int fahrplanjahr, Regionalbereich regionalbereich,
	    boolean exactMatch, Preload[] preloads);

	/**
	 * Liefert Strecken anhand der VzG-Streckennummer zurück. Bei der Suche kann angegeben werden,
	 * ob die Streckennummer exakt dem Suchwert entsprechen muss, oder nicht. Die Suche kann durch
	 * Angabe von Parametern eingeschränkt werden.
	 * 
	 * @param nummer
	 *            VzG-Streckennummer
	 * @param gueltigVon
	 * @param gueltigBis
	 * @param regionalbereich
	 * @param exactMatch
	 *            genaue Übereinstimmung der Streckennummer (<code>true</code>); Ähnlichkeitssuche (
	 *            <code>false</code>)
	 * @param preloads
	 * @return
	 */
	List<VzgStrecke> findByNummer(int nummer, Date gueltigVon, Date gueltigBis,
	    Regionalbereich regionalbereich, boolean exactMatch, Preload[] preloads);

	/**
	 * Nimmt Caption einer VZG-Strecke in der Form "[Nummer] Von - Bis" entgegen und gibt "Nummer"
	 * zurueck. Falls keine eckigen Klammern enthalten sind oder der Text darin laenger als 4
	 * Zeichen ist, wird null zurueckgegeben.
	 * 
	 * @param caption
	 * @return kuerzel
	 */
	public Integer castCaptionToNummer(String caption);

	/**
	 * Liefert Strecken alle Strecken zurueck, welche zusaetzlich zur uebergebenen Strecke dessen
	 * Betriebstellen kreuzen. Wird ein Regionalbereich mit uebergeben, werden die Strecken ueber
	 * den Regionalbereich der Betriebstellen eingeschraenkt.
	 * 
	 * @param vzgStrecke
	 * @param fahrplanjahr
	 * @param regionalbereich
	 * @param preloads
	 * @return
	 */
	public Set<VzgStrecke> findAbzweigendeStrecken(VzgStrecke vzgStrecke, int fahrplanjahr,
	    Regionalbereich regionalbereich, Preload[] preloads);

	/**
	 * Liefert Strecken alle Strecken zurueck, welche zusaetzlich zur uebergebenen Strecke dessen
	 * Betriebstellen kreuzen. Wird ein Regionalbereich mit uebergeben, werden die Strecken ueber
	 * den Regionalbereich der Betriebstellen eingeschraenkt.
	 * 
	 * @param vzgStrecke
	 * @param gueltigVon
	 * @param gueltigBis
	 * @param regionalbereich
	 * @param preloads
	 * @return
	 */
	public Set<VzgStrecke> findAbzweigendeStrecken(VzgStrecke vzgStrecke, Date gueltigVon,
	    Date gueltigBis, Regionalbereich regionalbereich, Preload[] preloads);

	/**
	 * Liefert alle Strecken alle Strecken eines Fahrplanjahres.
	 * 
	 * @param fahrplanjahr
	 * @param preloads
	 * @return
	 */
	public Set<VzgStrecke> findByFahrplanjahr(int fahrplanjahr, Preload[] preloads);

	/**
	 * Liefert alle Strecken alle Strecken anhand der Gueltigkeit.
	 * 
	 * @param gueltigVon
	 * @param gueltigBis
	 * @param preloads
	 * @return
	 */
	public Set<VzgStrecke> findByGueltigkeit(Date gueltigVon, Date gueltigBis, Preload[] preloads);

}