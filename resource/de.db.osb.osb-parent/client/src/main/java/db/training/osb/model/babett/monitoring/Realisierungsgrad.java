/**
 * 
 */
package db.training.osb.model.babett.monitoring;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * zur Ablage des Realisierungsgrads von Maßnahmen zum Stichtag.
 * 
 * @author michels
 * 
 */
@Entity
@Table(name = "monitoring_realisierungsgrad")
@SuppressWarnings("serial")
public class Realisierungsgrad extends EasyPersistentExpirationObject {

	@Column(length = 32, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
