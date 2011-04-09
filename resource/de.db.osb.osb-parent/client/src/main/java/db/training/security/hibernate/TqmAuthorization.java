package db.training.security.hibernate;

import java.io.Serializable;
import java.text.MessageFormat;

import org.acegisecurity.GrantedAuthority;

import db.training.security.Authorization;

/**
 * Reprasentiert einer Rolle im Security Modul. Ein Benutzer kann mehrere Rollen haben. Eine Rolle
 * kan Teil einer Rollenhierarchie sein.
 * 
 * @author hennebrueder
 * 
 */
public class TqmAuthorization extends Authorization implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = 1l;

	private Integer id;

	public TqmAuthorization() {

	}

	public TqmAuthorization(String name) {
		super();
		this.name = name;
	}

	public String getAuthority() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MessageFormat.format("{0} id={1}, name={2}", getClass().getSimpleName(), id, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TqmAuthorization other = (TqmAuthorization) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
