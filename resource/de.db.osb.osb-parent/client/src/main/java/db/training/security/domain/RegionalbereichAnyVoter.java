package db.training.security.domain;

import java.util.Iterator;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.User;
import db.training.easy.core.service.UserServiceImpl;
import db.training.security.Role;
import db.training.security.hibernate.TqmUser;

/**
 * Voter, der abstimmt, ob ein Benutzer Zugriffsrechte auf einen Regionalbereich hat.
 * 
 * @author Florian Hueter
 * 
 */
public class RegionalbereichAnyVoter implements EasyAccessDecisionVoter {

	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == Role.class;
	}

	/**
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf den Regionalbereich erlaubt.
	 * 
	 * <pre>
	 * requested: ROLE_BAUMASSNAHME_BENCHMARK_LESEN
	 * granted: ROLE_BAUMASSNAHME_BENCHMARK_LESEN_ALLE oder ROLE_BAUMASSNAHME_BENCHMARK_LESEN_REGIONALBEREICH,
	 * wenn der Regionalbereich der Regionalbereich des Benutzers ist
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
		int result = ACCESS_DENIED;
		User user = UserServiceImpl.getCurrentApplicationUser();
		Regionalbereich region = (Regionalbereich) object;
		Iterator iter = config.getConfigAttributes();
		while (iter.hasNext()) {
			ConfigAttribute attribute = (ConfigAttribute) iter.next();
			if (supports(attribute)) {
				for (GrantedAuthority authority : authentication.getAuthorities()) {
					if (authority.getAuthority().startsWith(attribute.getAttribute())
					    && checkDefaultValidation(region, user, authority.getAuthority())) {
						result = ACCESS_GRANTED;
						break;
					}
				}
			}
		}
		return result;
	}

	private boolean checkDefaultValidation(Regionalbereich region, User user, String authority) {
		if (authority.endsWith("_ALLE")) {
			return true;
		} else if (authority.endsWith("_REGIONALBEREICH") && user.getRegionalbereich() != null
		    && region != null && user.getRegionalbereich().getId().equals(region.getId())) {
			return true;
		}
		return false;
	}

	/**
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
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