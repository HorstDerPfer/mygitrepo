package db.training.security.domain;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.osb.model.Streckenband;
import db.training.security.hibernate.TqmUser;

/**
 * Voter, der abstimmt, ob ein Benutzer keine Zugriffsrechte auf ein Streckenband hat.
 * 
 */
public class StreckenbandNoneVoter extends StreckenbandAnyVoter {

	public boolean supports(ConfigAttribute attribute) {
		// currently accept all kind of authorizations
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == Streckenband.class;
	}

	/**
	 * * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf das Streckenband erlaubt.
	 * 
	 * <pre>
	 *  requested: baumassnahme_bewerten, granted: baumassnahme_bewerten_alle oder baumassnahme_bewerten_region, wenn die Region dem Benutzer zugeordnet ist
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
	 * requestedAuthorizations auf das Streckenband erlaubt.
	 * 
	 * <pre>
	 *  requested: baumassnahme_bewerten, granted: baumassnahme_bewerten_alle oder baumassnahme_bewerten_region, wenn die Region dem Benutzer zugeordnet ist
	 * </pre>
	 * 
	 * @param tqmUser
	 * @param domainObject
	 *            - ein Streckenband
	 * @param requestedAuthorizations
	 * @return
	 */
	public int vote(final TqmUser tqmUser, Object domainObject, String requestedAuthorizations) {
		Authentication authentication = new Authentication() {

			private static final long serialVersionUID = -5070612940380434691L;

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
