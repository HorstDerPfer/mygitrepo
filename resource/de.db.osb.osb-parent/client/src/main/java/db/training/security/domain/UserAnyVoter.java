package db.training.security.domain;

import java.util.Iterator;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.easy.core.model.User;
import db.training.easy.core.service.UserServiceImpl;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

/**
 * Voter, der abstimmt, ob ein Benutzer Zugriffsrechte auf einen Benutzer hat.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class UserAnyVoter implements EasyAccessDecisionVoter {

	private Logger log = Logger.getLogger(UserAnyVoter.class);

	public boolean supports(ConfigAttribute attribute) {
		// currently accept all kind of authorizations
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == User.class;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf den zu editierenden User erlaubt.
	 * 
	 * <pre>
	 *  requested: user_bewerten, granted: user_bewerten_alle oder user_bewerten_region, wenn die Region dem Benutzer zugeordnet ist
	 * </pre>
	 */
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
		int result = ACCESS_DENIED;

		User user = UserServiceImpl.getCurrentApplicationUser();
		User editUser = (User) object;

		Iterator iter = config.getConfigAttributes();
		while (iter.hasNext()) {
			ConfigAttribute attribute = (ConfigAttribute) iter.next();
			if (supports(attribute)) {
				GrantedAuthority[] authorities = authentication.getAuthorities();
				for (int i = 0; i < authorities.length; i++) {
					if (authorities[i].getAuthority().startsWith(attribute.getAttribute())) {
						if (log.isDebugEnabled())
							log.debug(String.format("Found authority %s", authorities[i]));
						if (authorities[i].getAuthority().endsWith("_ALLE"))
							result = ACCESS_GRANTED;
						else if (authorities[i].getAuthority().endsWith("_REGIONALBEREICH")) {
							if (user.getRegionalbereich() != null
							    && editUser.getRegionalbereich() != null) {
								if (user.getRegionalbereich().getId().equals(
								    editUser.getRegionalbereich().getId()))
									result = ACCESS_GRANTED;
							}
						}
						if (result == ACCESS_GRANTED)
							break;
					}
				}
			}
		}

		return result;
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
	 * @param domainObject -
	 *            sollte ein Anwendungsuser sein
	 * @param requestedAuthorizations
	 * @return
	 */
	public int vote(final TqmUser tqmUser, Object domainObject, String requestedAuthorizations) {
		Authentication authentication = new Authentication() {

			private static final long serialVersionUID = 742607169999421224L;

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
