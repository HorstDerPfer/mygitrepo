package db.training.osb.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import db.training.easy.common.BasicService;
import db.training.easy.common.PersistenceException;
import db.training.hibernate.preload.Preload;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.BetriebsstelleRegionalbereichLink;
import db.training.osb.model.BetriebsstelleVzgStreckeLink;
import db.training.osb.model.Gleissperrung;
import db.training.osb.model.Streckenband;
import db.training.osb.model.VzgStrecke;

public interface BetriebsstelleService extends BasicService<Betriebsstelle, Serializable> {

	/**
	 * <p>
	 * listet alle Betriebsstellen auf, die innerhalb des angegebenen Fahrplanjahres gültig sind und
	 * deren Kürzel ganz oder teilweise dem Parameter <code>keyword</code> entspricht (kuerzel LIKE
	 * 'keyword%')
	 * </p>
	 * <p>
	 * Die Betriebsstellen werden bezgl. ihres Kürzels alphabetisch aufsteigend sortiert.
	 * </p>
	 * 
	 * @param keyword
	 * @param fahrplanjahr
	 * @return
	 * @see #437
	 */
	public List<Betriebsstelle> findByKeyword(String keyword, int fahrplanjahr);

	/**
	 * <p>
	 * listet alle Betriebsstellen einer VzG-Strecke auf, die innerhalb des angegebenen
	 * Fahrplanjahres gültig sind und deren Kürzel ganz oder teilweise dem Parameter
	 * <code>keyword</code> entspricht (kuerzel LIKE 'keyword%')
	 * </p>
	 * <p>
	 * Die Betriebsstellen werden bezgl. ihrer Lage auf der Strecke (km) aufsteigend sortiert.
	 * Sofern keine VzG-Streckennummer angegeben wird, erfolgt die Sortierung wie in
	 * {@link BetriebsstelleService#findByKeyword(String, int)}
	 * </p>
	 * 
	 * @param keyword
	 * @param streckennummer
	 * @param fahrplanjahr
	 * @return
	 * @see #437
	 */
	public List<Betriebsstelle> findByKeyword(String keyword, Integer streckennummer,
	    int fahrplanjahr);

	/**
	 * Nimmt Caption einer Betriebsstelle in der Form "[Kuerzel] Langname" entgegen und gibt
	 * "Kuerzel" zurück. Falls keine eckigen Klammern enthalten sind oder der Text darin länger als
	 * 5 Zeichen ist, wird null zurückgegeben.
	 * 
	 * @param caption
	 * @return kuerzel
	 */
	public String castCaptionToKuerzel(String caption);

	public Integer castCaptionWithIDToID(String caption);

	public String castCaptionWithIDToKuerzel(String caption);

	public Betriebsstelle findByKuerzelAndStreckenAndFahrplanjahr(String kuerzel,
	    Set<Integer> streckenNummern, int fahrplanjahr);

	public Betriebsstelle findByKuerzelAndFahrplanjahr(String kuerzel, int fahrplanjahr);

	public List<BetriebsstelleVzgStreckeLink> findByVzgStreckeReducedAndFahrplanjahr(
	    VzgStrecke streckeVzg, Streckenband sb, int fahrplanjahr);

	public Betriebsstelle findStartInList(VzgStrecke strecke, List<Betriebsstelle> list,
	    Collection<Gleissperrung> gleissperrungen);

	public Betriebsstelle findEndInList(VzgStrecke strecke, List<Betriebsstelle> list,
	    Collection<Gleissperrung> gleissperrungen);

	/**
	 * Liefert eine Liste von Betriebsstellen zurueck, die im angegeben Fahrplanjahr gueltig sind,
	 * und auf der VzG-Strecke liegen. OnlyZugmeldeStellen bewirkt, dass nur die erste oder letzte
	 * Betriebstelle auf der Strecke und/oder Zugmeldestellen zurueckgegeben werden. Liste wird nach
	 * Kilometerangaben aus Linktabelle betriebstelle_strecke sortiert.
	 * 
	 * @param vzgStreckeNummer
	 * @param fahrplanjahr
	 * @param onlyZugmeldestellen
	 * @param preloads
	 * @return betriebsstellen
	 */
	public List<Betriebsstelle> findByVzgStreckeNummerAndFahrplanjahr(int nummer, int fahrplanjahr,
	    boolean onlyZugmeldestellen, Preload[] preloads);

	/**
	 * Liefert eine Liste von Betriebsstellen zurueck, die im angegeben Zeitraum gueltig sind, und
	 * auf der VzG-Strecke liegen. OnlyZugmeldeStellen bewirkt, dass nur die erste oder letzte
	 * Betriebstelle auf der Strecke und/oder Zugmeldestellen zurueckgegeben werden. Liste wird nach
	 * Kilometerangaben aus Linktabelle betriebstelle_strecke sortiert.
	 * 
	 * @param vzgStreckeNummer
	 * @param fahrplanjahr
	 * @param onlyZugmeldestellen
	 * @param preloads
	 * @return betriebsstellen
	 */
	public List<Betriebsstelle> findByVzgStreckeNummerAndGueltigkeit(int nummer, Date gueltigVon,
	    Date gueltigBis, boolean onlyZugmeldestellen, Preload[] preloads);

	/**
	 * 
	 * @param id
	 *            Betriebsstellen-ID
	 * @param nummer
	 *            VzG-Streckennummer (!= Id)
	 * @param fahrplanjahr
	 * @return
	 */
	public BetriebsstelleVzgStreckeLink findByVzgStrecke(int id, int nummer, int fahrplanjahr);

	/**
	 * 
	 * @param id
	 *            Betriebsstellen-ID
	 * @param nummer
	 *            VzG-Streckennummer (!= Id)
	 * @param fahrplanjahr
	 * @return
	 */
	public BetriebsstelleVzgStreckeLink findByVzgStrecke(int id, int nummer, Date gueltigVon,
	    Date gueltigBis);

	BetriebsstelleRegionalbereichLink findByRegionalbereich(int betriebsstelleId,
	    int regionalbereichId, Date gueltigVon, Date gueltigBis);

	BetriebsstelleRegionalbereichLink findByRegionalbereich(int betriebsstelleId,
	    int regionalbereichId, int fahrplanjahr);

	/**
	 * testet, ob den Betriebsstellen auf einem Streckenabschnitt einer VzG-Strecke mehr als eine
	 * Mutter-Betriebsstelle zugeordnet ist.
	 * 
	 * @param strecke
	 *            VzG-Streckennummer
	 * @param kmVon
	 * @param kmBis
	 * @return <ul>
	 *         <li>false - es existiert genau eine Mutter-Betriebsstelle</li>
	 *         <li>sonst true</li>
	 *         </ul>
	 * @throws Exception
	 */
	boolean hasMoreThanOne_MutterBst(int strecke, float kmVon, float kmBis)
	    throws PersistenceException;
}