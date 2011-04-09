package db.training.bob.model;

/**
 * <table>
 * <tr>
 * <th>Prio.</th>
 * <th>Name</th>
 * <th>Beschreibung</th>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>NEUTRAL</td>
 * <td>keine Bearbeitung erforderlich</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>GREEN</td>
 * <td>Bearbeitung ist erfolgt, Termin wurde nicht überschritten</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>RED</td>
 * <td>Bearbeitung ist erfolgt, Termin überschritten</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>COUNTDOWN14</td>
 * <td>Termin nicht erreicht und keine Bearbeitung (weiß mit kleinem ‚o‘), Soll-Termin kleiner oder
 * gleich 14 Tage.</td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>OFFEN</td>
 * <td>keine Bearbeitung erfolgt, Termin noch nicht überschritten (Soll-Termin größer 14 Tage)</td>
 * </tr>
 * <tr>
 * <tr>
 * <td>5</td>
 * <td>LILA</td>
 * <td>keine Bearbeitung, Termin überschritten</td>
 * </tr>
 * <tr>
 * <td>10</td>
 * <td>HONK</td>
 * <td>zu Testzwecken</td>
 * </tr>
 * </table>
 * 
 * @author gergs, michels
 * 
 */
public enum StatusType {
	// die Reihenfolge entspricht der Priorität des Status, Rot hat höchste Prio.
	NEUTRAL, GREEN, RED, COUNTDOWN14, OFFEN, LILA, HONK
}
