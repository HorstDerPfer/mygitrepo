/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * Netzbezirkseigenschaften
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.3.6 Netzbezirk
 */
@Entity
@Table(name = "netzbezirk")
@SuppressWarnings("serial")
public class Netzbezirk extends EasyPersistentExpirationObject {

	/** bezeichnung */
	@Column(length = 32)
	private String name;

	@Column(nullable = false)
	private int nummer;

	@ManyToOne(optional = false)
	private Regionalbereich regionalbereich;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNummer() {
		return nummer;
	}

	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}
}
