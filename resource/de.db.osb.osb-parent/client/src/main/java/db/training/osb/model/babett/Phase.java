/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * ordnet Maßnahmen den Prozessschritten des Verbundprozesses "Fahren und Bauen" zu.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.8 Phasentypen
 */
@Entity
@Table(name = "phasentypen")
@SuppressWarnings("serial")
public class Phase extends EasyPersistentExpirationObject {

	@Column(length = 2, nullable = false)
	private String kuerzel;

	/** phase */
	@Column(length = 50, nullable = false)
	private String name;

	/** folgephase_id */
	@OneToOne(optional = true)
	private Phase folgePhase;

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

	/**
	 * @return gibt die auf diese Phase folgende Phase
	 */
	public Phase getFolgePhase() {
		return folgePhase;
	}

	/**
	 * @param folgePhase
	 *            die auf diese Phase folgende Phase
	 */
	public void setFolgePhase(Phase folgePhase) {
		this.folgePhase = folgePhase;
	}

	@Transient
	public String getCaption() {
		StringBuilder caption = new StringBuilder();
		if (kuerzel != null)
			caption.append("[").append(kuerzel).append("]");
		if (kuerzel != null && name != null)
			caption.append(" ");
		if (name != null)
			caption.append(name);
		return caption.toString();
	}
}
