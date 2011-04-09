package db.training.security.domain;

import org.acegisecurity.vote.AccessDecisionVoter;

import db.training.security.hibernate.TqmUser;

/**
 * erweitert das Acegi Interface um eine Methode, die besser an unsere Service Klassen angepasst
 * ist.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public interface EasyAccessDecisionVoter extends AccessDecisionVoter {
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
	public int vote(final TqmUser tqmUser, Object domainObject, String requestedAuthorizations);
}
