/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * Der finanzielle Maßnahmetyp sagt aus, welche Art der Finanzierung einer Baumaßnahme zu Grunde
 * liegt.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.3 Finanzieller Maßnahmetyp
 * 
 */
@Entity
@Table(name = "finanztyp")
@SuppressWarnings("serial")
public class Finanztyp extends EasyPersistentExpirationObject {

	@Column(length = 8, nullable = false)
	private String kuerzel;

	/** finanztyp */
	@Column(length = 64, nullable = false)
	private String name;

	public Finanztyp() {
	}

	public Finanztyp(String kuerzel, String name) {
		this.kuerzel = kuerzel;
		this.name = name;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
