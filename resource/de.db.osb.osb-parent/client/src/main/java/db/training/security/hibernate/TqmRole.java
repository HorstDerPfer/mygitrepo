package db.training.security.hibernate;

import java.io.Serializable;

import db.training.security.Role;

// TODO she Spaltennamen in Tabelle sec_role_authorization vertauscht (Florian)

/**
 * Repraesentiert eine Rolle im Benutzer - Rolle - Rechte Konzept. Ergaenzt nur eine Id fuer die
 * Speicherung mit Hibernate
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class TqmRole extends Role implements Serializable {

	private static final long serialVersionUID = 6765997968113707086L;

	private Integer id;

	public TqmRole() {
		super();
	}

	public TqmRole(String name) {
		super();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
