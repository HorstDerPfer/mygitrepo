/**
 * 
 */
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
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.security.hibernate.TqmUser;

/**
 * @author michels
 * 
 */
public class UmleitungAnyVoter implements EasyAccessDecisionVoter {

	private Logger log = Logger.getLogger(UmleitungAnyVoter.class);

	/**
	 * Gibt ACCESS_GRANTED zurueck, wenn der Benutzer eine Rolle hat, die ihm eine der
	 * requestedAuthorizations auf das Model/Domain Objekt erlaubt.
	 * 
	 * <pre>
	 *  requested: baumassnahme_bewerten, granted: baumassnahme_bewerten_alle oder baumassnahme_bewerten_region, wenn die Region dem Benutzer zugeordnet ist
	 * </pre>
	 * 
	 * @param tqmUser
	 * @param domainObject
	 * @param requestedAuthorizations
	 * @return
	 */
	public int vote(final TqmUser tqmUser, Object domainObject, String requestedAuthorizations) {
		Authentication authentication = new Authentication() {

			private static final long serialVersionUID = 742607169988421224L;

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

	public boolean supports(ConfigAttribute arg0) {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class arg0) {
		return arg0 == Umleitung.class;
	}

	@SuppressWarnings("unchecked")
	public int vote(Authentication arg0, Object arg1, ConfigAttributeDefinition arg2) {
		int result = ACCESS_DENIED;

		User user = UserServiceImpl.getCurrentApplicationUser();
		if (!(arg1 instanceof Umleitung)) {
			throw new IllegalArgumentException();
		}

		Umleitung umleitung = (Umleitung) arg1;

		Iterator iterConfig = arg2.getConfigAttributes();
		while (iterConfig.hasNext()) {
			ConfigAttribute configAttribute = (ConfigAttribute) iterConfig.next();
			if (supports(configAttribute)) {
				GrantedAuthority[] authorities = arg0.getAuthorities();
				for (GrantedAuthority authority : authorities) {
					if (authority.getAuthority().startsWith(configAttribute.getAttribute())) {
						if (log.isDebugEnabled())
							log.debug(String.format("Found authority %s", authority));
						// ALLE
						if (authority.getAuthority().endsWith("_ALLE")) {
							result = ACCESS_GRANTED;
						}
						// TEMPORAER
						else if (authority.getAuthority().endsWith("_TEMPORAER")
						    && System.getProperty("regionalPermissions") != null
						    && System.getProperty("regionalPermissions").equals("true")) {
							result = ACCESS_GRANTED;
						}
						// REGIONALBEREICH
						else if (authority.getAuthority().endsWith("_REGIONALBEREICH")) {
							// Wenn der Umleitung keine Fahrplanregelung angehaengt ist, kann nicht
							// auf Region geprueft werden
							if (umleitung.getUmleitungFahrplanregelungLinks() == null
							    || umleitung.getUmleitungFahrplanregelungLinks().size() == 0) {
								result = ACCESS_GRANTED;
								break;
							}
							for (UmleitungFahrplanregelungLink ufLink : umleitung
							    .getUmleitungFahrplanregelungLinks()) {
								if (ufLink.getFahrplanregelung().getRegionalbereich().equals(
								    user.getRegionalbereich())) {
									result = ACCESS_GRANTED;
									break;
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
}