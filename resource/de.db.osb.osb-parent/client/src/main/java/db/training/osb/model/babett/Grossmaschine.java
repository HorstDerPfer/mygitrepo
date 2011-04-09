package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * gibt die bei der Baumaßnahme eingesetzten Großmaschinen an
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.6 Großmaschine
 * 
 */
@Entity
@Table(name = "grossmaschine")
@SuppressWarnings("serial")
public class Grossmaschine extends EasyPersistentExpirationObject {

	/** grossmaschine */
	@Column(length = 32, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
