/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * Ablage der Änderungsgründe, die bei zeitlichen, örtlichen oder regelungsbezogenen Änderungen von
 * Baumaßnahmen bzw. Gleissperrungen anzugeben sind.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.5 Änderungsgrund
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "aenderungsgrund")
public class Aenderungsgrund extends EasyPersistentExpirationObject {

	/** aenderungsgrund */
	@Column(length = 32, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
