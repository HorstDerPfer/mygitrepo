package db.training.security.hibernate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

import db.training.logwrapper.Logger;
import db.training.security.Authorization;
import db.training.security.Role;
import db.training.security.User;

/**
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class TqmUser extends User implements UserDetails, Serializable {

	private static final long serialVersionUID = 3081249287086975915L;

	private static Logger log = Logger.getLogger(TqmUser.class);

	private Integer id;

	/*
	 * enthaelt eine flache Hierarchie von Rechten, volatile damit der synchronized Block
	 * funktioniert
	 */
	private volatile Set<Authorization> flatAuthorities = null;

	public TqmUser() {
		super();
	}

	public TqmUser(String name) {
		this.username = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * gibt eine flache Hierarchie der Rechte eines Nutzers zurueck
	 */
	public GrantedAuthority[] getAuthorities() {
		if (flatAuthorities == null)
			initFlatAuthorities();
		return flatAuthorities.toArray(new GrantedAuthority[0]);
	}

	/**
	 * initialisiert das Attribut flatAuthorities Thread-sicher
	 * 
	 */
	private void initFlatAuthorities() {
		if (log.isDebugEnabled())
			log.debug("Initialize authorities");
		synchronized (this) {
			if (flatAuthorities == null) {
				flatAuthorities = new HashSet<Authorization>();
				flatAuthorities.add(new TqmAuthorization("ROLE_NON_ANONYMOUS"));
				for (Role r : roles) {
					flatAuthorities.addAll(r.getAllAuthorizations());
				}
			}
		}
	}

	public boolean hasAuthorization(String roleName) {
		return (Arrays.asList(getAuthorities()).contains(new TqmAuthorization(roleName)));
	}

	public boolean hasRole(String role) {
		boolean hasrole = false;
		for (Role tempRole : getRoles()) {
			if (((TqmRole) tempRole).getName().equals(role)) {
				hasrole = true;
				break;
			}
		}
		return hasrole;
	}

}
