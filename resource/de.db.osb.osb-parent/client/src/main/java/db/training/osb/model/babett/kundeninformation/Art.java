/**
 * 
 */
package db.training.osb.model.babett.kundeninformation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * Beschreibung der Art und des Zeitpunkts, in der die Kommunikation zwischen KBBT, KBB und EVU
 * stattfindet
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.17 Kundeninfo-Art
 */
@Entity
@Table(name = "kdinfo_art")
@SuppressWarnings("serial")
public class Art extends EasyPersistentExpirationObject {

	@Column(length = 16, nullable = false)
	private String kuerzel;

	/** dokumenttyp */
	@Column(nullable = false)
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
