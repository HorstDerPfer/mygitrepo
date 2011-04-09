package db.training.security;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasse ist ein Recht, das innerhalb der Anwendung gesetzt werden kann. Es wird einer Rolle
 * <code>Role</code> zugeordnet.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public abstract class Authorization implements Serializable {

	private static final long serialVersionUID = 1l;

	protected String name;

	protected Set<Role> roles = new HashSet<Role>();

	public Authorization() {
		super();
	}

	public Authorization(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0}: name={1}", getClass().getSimpleName(), name);
	}
}
