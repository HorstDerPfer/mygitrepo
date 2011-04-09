/**
 * 
 */
package db.training.osb.model.babett;

/**
 * Status der baubetrieblichen Zugregelung für Maßnahmen des Baubetriebsmanagements
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.11 Status BBZR
 */
public enum StatusBbzr {
	NICHT_ERFORDERLICH, ERFORDERLICH, BEAUFTRAGT, ENTWURF_GELIEFERT, ERLEDIGT,
	/** FE/FS */
	FE_FS
}
