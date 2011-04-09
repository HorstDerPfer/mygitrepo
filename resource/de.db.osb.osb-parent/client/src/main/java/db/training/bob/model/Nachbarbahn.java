package db.training.bob.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "nachbarbahn")
public class Nachbarbahn extends EasyPersistentObject implements Historizable {

	private String name;

	protected Nachbarbahn() {
		this.name = "";
	}

	public Nachbarbahn(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
