package db.training.security.domain;

import java.util.Iterator;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.security.Role;
import db.training.security.hibernate.TqmRole;
import db.training.security.hibernate.TqmUser;

/**
 * Voter, der abstimmt, ob ein Benutzer Zugriffsrechte auf eine Rolle hat.
 * 
 * @author Florian Hueter
 * 
 */
public class TqmRoleAnyVoter implements EasyAccessDecisionVoter {

	public boolean supports(ConfigAttribute attribute) {
		if (attribute.getAttribute().equals("ROLE_BENUTZER_ROLLEN_VERGEBEN")) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == Role.class;
	}

	/**
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf die Rolle erlaubt.
	 * 
	 * <pre>
	 * requested: ROLE_BENUTZER_ROLLEN_VERGEBEN
	 * granted: ROLE_BENUTZER_ROLLEN_VERGEBEN_ALLE oder ROLE_BENUTZER_ROLLEN_VERGEBEN_REGIONALBEREICH, wenn
	 * die Rolle eine regionale Rolle ist
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
		int result = ACCESS_DENIED;
		TqmRole role = (TqmRole) object;
		Iterator iter = config.getConfigAttributes();
		while (iter.hasNext()) {
			ConfigAttribute attribute = (ConfigAttribute) iter.next();
			if (supports(attribute)) {
				for (GrantedAuthority authority : authentication.getAuthorities()) {
					if (authority.getAuthority().startsWith(attribute.getAttribute())
					    && checkDefaultValidation(role, authority.getAuthority())) {
						result = ACCESS_GRANTED;
						break;
					}
				}
			}
		}
		return result;
	}

	private boolean checkDefaultValidation(TqmRole role, String authority) {
		if (authority.endsWith("_ALLE"))
			return true;
		else if (authority.endsWith("_REGIONALBEREICH") & role.getName().endsWith("_ZENTRAL"))
			return false;
		else if (authority.endsWith("_REGIONALBEREICH") & role.getName().endsWith("_TOOL"))
			return false;
		else if (authority.endsWith("_REGIONALBEREICH")
		    & role.getName().equals("ADMINISTRATOR_REGIONAL"))
			return false;
		else
			return true;
	}

	/**
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf die Rolle erlaubt.
	 * 
	 * @param tqmUser
	 * @param domainObject
	 *            Objekt der Klasse TqmRole
	 * @param requestedAuthorizations
	 * @return
	 */
	public int vote(final TqmUser tqmUser, Object domainObject, String requestedAuthorizations) {
		Authentication authentication = new Authentication() {

			private static final long serialVersionUID = 3734932700281423324L;

			public GrantedAuthority[] getAuthorities() {
				return tqmUser.getAuthorities();
			}

			public Object getCredentials() {
				return null;
			}

			public Object getDetails() {
				return tqmUser;
			}

			public Object getPrincipal() {
				return tqmUser;
			}

			public boolean isAuthenticated() {
				return true;
			}

			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				;
			}

			public String getName() {
				return tqmUser.getUsername();
			}
		};
		String[] splits = requestedAuthorizations.split(",");
		ConfigAttributeDefinition definition = new ConfigAttributeDefinition();
		for (String singleAuthorization : splits) {
			definition.addConfigAttribute(new SecurityConfig(singleAuthorization.trim()));
		}
		return vote(authentication, domainObject, definition);
	}
}