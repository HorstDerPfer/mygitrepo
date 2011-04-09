/**
 * 
 */
package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.3.4 Betriebsstellentyp
 */
@Entity
@Table(name = "bsttyp")
@SuppressWarnings("serial")
public class Betriebsstellentyp extends EasyPersistentExpirationObject {

	@Column(length = 16, nullable = false)
	private String kuerzel;

	/** bezeichnung */
	@Column(length = 32, nullable = false)
	private String name;

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
