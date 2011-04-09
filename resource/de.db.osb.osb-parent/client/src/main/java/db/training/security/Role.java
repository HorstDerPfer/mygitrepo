package db.training.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public abstract class Role implements Serializable{
	protected String name;

	protected Set<Role> subRoles = new HashSet<Role>();

	protected Role parentRole;

	protected Set<Authorization> authorizations = new HashSet<Authorization>();

	public Role() {
		super();
	}

	public Role(String name) {
		super();
		this.name = name;
	}

	public Set<Role> getSubRoles() {
		return subRoles;
	}

	public void setSubRoles(Set<Role> subRoles) {
		this.subRoles = subRoles;
	}

	/**
	 * fuegt eine Unterrolle hinzu. Garantiert, dass keine Schleife entsteht. Entfernt die SubRolle
	 * von ihrer jetztigen Parentrolle.
	 * 
	 * @param role
	 */
	public void addSubRole(Role role) {
		// pruefe ob eine Schleife entsteht
		Role current = role.parentRole;
		while (current != null) {
			if (current.name.equals(name))
				throw new RuntimeException(
				    "Zirkulaere Zuordnungen zwischen Rollen sind nicht erlaubt.");
			current = current.parentRole;
		}
		// Umhaengen
		if (role.parentRole != null)
			role.parentRole.subRoles.remove(role);
		subRoles.add(role);
		role.parentRole = this;
	}

	/**
	 * entfernt eine Unterrolle von einer Rolle
	 * 
	 * @param role
	 */
	public void removeRole(Role role) {
		if (role.parentRole != null) {
			role.parentRole.subRoles.remove(role);
			role.parentRole = null;
		}
	}

	public Set<Authorization> getAllAuthorizations() {
		Set<Authorization> result = new HashSet<Authorization>();
		result.addAll(authorizations);
		for (Role r : subRoles) {
			result.addAll(r.getAllAuthorizations());
		}
		return result;
	}

	public Set<Authorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(Set<Authorization> authorizations) {
		this.authorizations = authorizations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Role other = (Role) obj;
		return (name.equals(other.name));
	}

}
