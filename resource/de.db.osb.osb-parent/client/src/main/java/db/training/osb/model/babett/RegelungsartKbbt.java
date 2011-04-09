/**
 * 
 */
package db.training.osb.model.babett;

/**
 * Regelungsarten der KBBT für die Verwendungszwecke Lisba/KiGbau und weitere
 * 
 * @author michels
 * 
 */
public enum RegelungsartKbbt {
	/** keine Auswirkungen */
	KEINE_AUSWIRKUNG,
	/** Umleitung */
	UML,
	/** Verspätung auf Regelweg */
	VERSP_A_RW,
	/** Vorplanfahrt auf Regelweg */
	VORPLAN_A_RW,
	/** Ausfall */
	AUSFALL,
	/** Uml. u. erl. Bedingungen */
	UUEB,
	/** entfällt */
	ENTFAELLT
}
