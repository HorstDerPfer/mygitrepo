/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;

/**
 * dient der Gruppierung von Arbeiten mit bestimmten Inhalten zu einem Überbegriff, z. B.
 * "Gleisauswechslung", "Durcharbeitung von Gleisen", "Weichenerneuerungen" usw. zu einer Gruppe mit
 * dem Attribut "AGRP_Name": "Arbeiten am Oberbau". Diese Gruppierung ist erforderlich, um
 * einerseits Vorgaben der BNetzA bezüglich der KiGBau zu erfüllen und andererseits keine
 * frühzeitige detaillierte Angabe der konkreten Arbeiten zu publizieren, was Nachteile für die DB
 * Netz AG zur Folge haben könnte.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.2 Arbeiten-Gruppe
 * 
 */
@Entity()
@Table(name = "arbeitengruppen")
@SuppressWarnings("serial")
public class ArbeitstypGruppe extends EasyPersistentObject {

	@Column(name = "AGRP_Name", length = 32)
	private String name;

	/**
	 * @return Name der Gruppe
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param Name
	 *            der Gruppe
	 */
	public void setName(String name) {
		this.name = name;
	}
}
