/**
 * 
 */
package db.training.security.domain;

import java.util.Arrays;
import java.util.Iterator;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.SecurityConfig;

import db.training.logwrapper.Logger;
import db.training.osb.model.SqlQuery;
import db.training.security.hibernate.TqmUser;

/**
 * @author michels
 * 
 */
public class SqlQueryAnyVoter implements EasyAccessDecisionVoter {

	private static final String[] supportedAuthorizations = { "ROLE_SQLQUERY_ANLEGEN",
	        "ROLE_SQLQUERY_ANLEGEN_ALLE", "ROLE_SQLQUERY_BEARBEITEN",
	        "ROLE_SQLQUERY_BEARBEITEN_ALLE", "ROLE_SQLQUERY_LOESCHEN",
	        "ROLE_SQLQUERY_LOESCHEN_ALLE", "ROLE_SQLQUERY_AUSFUEHREN",
	        "ROLE_SQLQUERY_AUSFUEHREN_ALLE" };

	private Logger log = Logger.getLogger(SqlQueryAnyVoter.class);

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

			private static final long serialVersionUID = 742607169912421224L;

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
		return Arrays.asList(supportedAuthorizations).contains(arg0.getAttribute());
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class arg0) {
		return arg0 == SqlQuery.class;
	}

	@SuppressWarnings("unchecked")
	public int vote(Authentication arg0, Object arg1, ConfigAttributeDefinition arg2) {
		int result = ACCESS_DENIED;

		if (!(arg1 instanceof SqlQuery)) {
			throw new IllegalArgumentException();
		}

		Iterator iterConfig = arg2.getConfigAttributes();
		while (iterConfig.hasNext()) {
			ConfigAttribute configAttribute = (ConfigAttribute) iterConfig.next();
			if (supports(configAttribute)) {
				GrantedAuthority[] authorities = arg0.getAuthorities();
				for (GrantedAuthority authority : authorities) {
					if (authority.getAuthority().startsWith(configAttribute.getAttribute())) {
						if (log.isDebugEnabled())
							log.debug(String.format("Found authority %s", authority));

						// Berechtigung gilt für alle Objektinstanzen
						if (authority.getAuthority().endsWith("_ALLE")) {
							result = ACCESS_GRANTED;
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