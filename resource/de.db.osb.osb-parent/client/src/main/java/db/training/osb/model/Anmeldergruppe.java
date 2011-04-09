/**
 * 
 */
package db.training.osb.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.2.2 Anmeldergruppe
 */
@Table(name = "anmeldergruppe")
@Entity
public class Anmeldergruppe extends EasyPersistentExpirationObject {

	private static final long serialVersionUID = 7517936359240289794L;

	@Column(length = 16, nullable = false)
	private String kuerzel;

	/** anmeldergruppe */
	@Column(length = 32, nullable = false)
	private String name;

	@ManyToMany(mappedBy = "anmeldergruppe")
	private Set<Anmelder> anmelder;

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

	public Set<Anmelder> getAnmelder() {
		return anmelder;
	}

	public void setAnmelder(Set<Anmelder> anmelder) {
		this.anmelder = anmelder;
	}
}
