package db.training.security.domain;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.easy.core.model.User;
import db.training.easy.core.service.UserServiceImpl;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

/**
 * Voter, der abstimmt, ob ein Benutzer Zugriffsrechte auf eine Baumassnahme hat.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class BBZRAnyVoter implements EasyAccessDecisionVoter {

	Logger logger = Logger.getLogger(BBZRAnyVoter.class);

	public boolean supports(ConfigAttribute attribute) {
		// currently accept all kind of authorizations
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == Uebergabeblatt.class;
	}

	@SuppressWarnings("unchecked")
	/*
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf die Baumassnahme erlaubt.
	 * 
	 * <pre> requested: baumassnahme_bewerten, granted: baumassnahme_bewerten_alle oder
	 * baumassnahme_bewerten_region, wenn die Region dem Benutzer zugeordnet ist </pre>
	 */
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
		int result = ACCESS_DENIED;

		User user = UserServiceImpl.getCurrentApplicationUser();
		Uebergabeblatt zvf = (Uebergabeblatt) object;
		Iterator iter = config.getConfigAttributes();
		while (iter.hasNext()) {// Schleife über erforderliche Berechtigungen
			ConfigAttribute attribute = (ConfigAttribute) iter.next();
			if (supports(attribute)) {
				GrantedAuthority[] authorities = authentication.getAuthorities();

				// Schleife über vorhandene Berechtigungen
				for (int i = 0; i < authorities.length; i++) {
					if (authorities[i].getAuthority().startsWith(attribute.getAttribute())) {
						if (logger.isDebugEnabled())
							logger.debug(String.format("Found authority %s", authorities[i]));

						// Zentral
						if (authorities[i].getAuthority().endsWith("_ALLE"))
							result = ACCESS_GRANTED;
						// Regional
						else if (authorities[i].getAuthority().endsWith("_REGIONALBEREICH")) {
							if (attribute.getAttribute().startsWith("ROLE_BBZR_ANLEGEN")) {
								result = ACCESS_GRANTED;
							} else if (user.getRegionalbereich() != null) {
								// MasterRB
								try {
									String masterRB = zvf.getMassnahmen().iterator().next()
									    .getMasterniederlassung().substring(3);
									if (user.getRegionalbereich().getName().equalsIgnoreCase(
									    masterRB))
										result = ACCESS_GRANTED;
								} catch (NoSuchElementException e) {
								}
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
	 * requestedAuthorizations auf die Baumassnahme erlaubt.
	 * 
	 * <pre>
	 *  requested: baumassnahme_bewerten, granted: baumassnahme_bewerten_alle oder baumassnahme_bewerten_region, wenn die Region dem Benutzer zugeordnet ist
	 * </pre>
	 * 
	 * @param tqmUser
	 * @param domainObject
	 *            - sollte eine Baumassnahme sein
	 * @param requestedAuthorizations
	 * @return
	 */
	public int vote(final TqmUser tqmUser, Object domainObject, String requestedAuthorizations) {
		Authentication authentication = new Authentication() {

			private static final long serialVersionUID = 6602266556656215575L;

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
