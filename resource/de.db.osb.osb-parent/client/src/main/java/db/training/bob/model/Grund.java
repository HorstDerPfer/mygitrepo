package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "grund")
public class Grund extends EasyPersistentObject implements Historizable {

	@Column(name = "name", unique = true)
	private String name;

	protected Grund() {
	}

	public Grund(String grund) {
		this.name = grund;
	}

	public String getName() {
		return name;
	}

	public void setName(String grund) {
		this.name = grund;
	}
}