/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * beschreibt betriebliche bzw. technische Folgen, wenn eine Baumaßnahme nicht bzw. nicht
 * zeitgerecht ausgeführt werden kann.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.7 Folge Nichtausführung
 * 
 */
@Entity
@Table(name = "folge_nichtausfuehrung")
@SuppressWarnings("serial")
public class FolgeNichtausfuehrung extends EasyPersistentExpirationObject {

	@Column(length = 5, nullable = false)
	private String kuerzel;

	@Column(length = 16, nullable = false)
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
