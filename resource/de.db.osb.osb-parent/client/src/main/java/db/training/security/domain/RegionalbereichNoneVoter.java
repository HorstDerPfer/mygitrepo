package db.training.security.domain;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.security.Role;
import db.training.security.hibernate.TqmUser;

/**
 * Voter, der abstimmt, ob ein Benutzer Zugriffsrechte auf einen Regionalbereich hat.
 * 
 * @author Florian Hueter
 * 
 */
public class RegionalbereichNoneVoter extends TqmRoleAnyVoter {

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
	 * Gibt ACCESS_DENIED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf den Regionalbereich erlaubt.
	 * 
	 * <pre>
	 * requested: ROLE_BAUMASSNAHME_BENCHMARK_LESEN
	 * granted: ROLE_BAUMASSNAHME_BENCHMARK_LESEN_ALLE oder ROLE_BAUMASSNAHME_BENCHMARK_LESEN_REGIONALBEREICH,
	 * wenn der Regionalbereich der Regionalbereich des Benutzers ist
	 * </pre>
	 */
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
		int result = super.vote(authentication, object, config);
		if (result == ACCESS_DENIED)
			return ACCESS_GRANTED;
		else
			return ACCESS_DENIED;
	}

	/**
	 * Gibt ACCESS_DENIED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf den Regionalbereich erlaubt.
	 * 
	 * @param tqmUser
	 * @param domainObject
	 *            Objekt der Klasse Regionalbereich
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