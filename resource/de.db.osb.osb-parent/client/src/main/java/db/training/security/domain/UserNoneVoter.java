package db.training.security.domain;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.easy.core.model.User;
import db.training.security.hibernate.TqmUser;

/**
 * Voter, der abstimmt, ob ein Benutzer Zugriffsrechte auf einen Benutzer hat.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class UserNoneVoter extends UserAnyVoter {

	// private Logger logger = Logger.getLogger(UserNoneVoter.class);

	public boolean supports(ConfigAttribute attribute) {
		// currently accept all kind of authorizations
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == User.class;
	}

	/**
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer keine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf den zu editierenden User erlaubt.
	 * 
	 * <pre>
	 *  requested: user_bewerten, granted: user_bewerten_alle oder user_bewerten_region, wenn die Region dem Benutzer zugeordnet ist
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
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf den zu editierenden User erlaubt.
	 * 
	 * <pre>
	 *  requested: user_bewerten, granted: user_bewerten_alle oder user_bewerten_region, wenn die Region dem Benutzer zugeordnet ist
	 * </pre>
	 * 
	 * @param tqmUser
	 * @param domainObject
	 *            - sollte ein Anwendungsuser sein
	 * @param requestedAuthorizations
	 * @return
	 */
	public int vote(final TqmUser tqmUser, Object domainObject, String requestedAuthorizations) {
		Authentication authentication = new Authentication() {

			private static final long serialVersionUID = 7755698883124533840L;

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
