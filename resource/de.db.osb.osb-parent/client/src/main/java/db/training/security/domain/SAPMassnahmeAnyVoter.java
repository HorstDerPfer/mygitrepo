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
import db.training.osb.model.SAPMassnahme;
import db.training.security.hibernate.TqmUser;

/**
 * @author michels
 * 
 */
public class SAPMassnahmeAnyVoter implements EasyAccessDecisionVoter {

	private Logger log = Logger.getLogger(SAPMassnahmeAnyVoter.class);

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
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class arg0) {
		return arg0 == SAPMassnahme.class;
	}

	@SuppressWarnings("unchecked")
	public int vote(Authentication arg0, Object arg1, ConfigAttributeDefinition arg2) {
		int result = ACCESS_DENIED;

		if (!(arg1 instanceof SAPMassnahme)) {
			throw new IllegalArgumentException();
		}

		TqmUser secUser = (TqmUser) arg0.getPrincipal();
		User loginUser = UserServiceImpl.getCurrentApplicationUser();

		SAPMassnahme massnahme = (SAPMassnahme) arg1;

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
						// Berechtigung gilt temporär für Objektinstanzen des eigenen
						// Regionalbereichs
						else if (authority.getAuthority().endsWith("_TEMPORAER")
						    && System.getProperty("regionalPermissions") != null
						    && System.getProperty("regionalPermissions").equals("true")) {
							result = voteRegionalbereich(loginUser, massnahme, authority);
						}
						// Berechtigung gilt für alle Objektinstanzen des eigenen Regionalbereichs
						else if (authority.getAuthority().endsWith("_REGIONALBEREICH")) {
							result = voteRegionalbereich(loginUser, massnahme, authority);
						}

						// Ausnahme fuer Objekte mit fixierten Massnahmen #518
						if (result == ACCESS_GRANTED && massnahme.getGenehmiger() != null) {
							// Betrifft bearbeiten und loeschen
							if (authority.getAuthority().contains("BEARBEITEN")
							    || authority.getAuthority().contains("LOESCHEN")) {
								// Nur Administratoren duerfen fixierte Objekte bearbeiten
								if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
									result = ACCESS_DENIED;
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

	private int voteRegionalbereich(User user, SAPMassnahme massnahme, GrantedAuthority authority) {
		if (massnahme.getRegionalbereich() != null && user.getRegionalbereich() != null
		    && massnahme.getRegionalbereich().equals(user.getRegionalbereich())) {
			return ACCESS_GRANTED;
		}
		return ACCESS_DENIED;
	}
}