package db.training.security;

import org.acegisecurity.Authentication;
import org.acegisecurity.event.authentication.AuthenticationFailureBadCredentialsEvent;
import org.acegisecurity.event.authorization.AuthorizedEvent;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import de.dbsystems.kolt.talo.CoreCatalog;
import de.dbsystems.kolt.talo.TaloLog;
import de.dbsystems.kolt.talo.TaloLogFactory;

public class ApplicationSecurityListener implements ApplicationListener {

	private static TaloLog talo = TaloLogFactory.getLog(ApplicationSecurityListener.class);

	private SecurityService securityService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context
	 * .ApplicationEvent)
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof AuthenticationFailureBadCredentialsEvent) {
			AuthenticationFailureBadCredentialsEvent failureEvent = (AuthenticationFailureBadCredentialsEvent) event;
			Authentication authentication = failureEvent.getAuthentication();
			if (talo.isInfoEnabled())
				talo.info("Login failure for user: " + authentication.getName());

			talo.log(CoreCatalog.warnLoginDenied(null, authentication.getName()));

			// Wir koennten einen Anonymen User bekommen, daher testen wird.
			securityService.loginFailed(authentication.getName());

		} else if (event instanceof AuthorizedEvent) {
			AuthorizedEvent successEvent = (AuthorizedEvent) event;
			Authentication authentication = successEvent.getAuthentication();
			// Wir koennten einen Anonymen User bekommen, daher testen wird.
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				if (talo.isInfoEnabled())
					talo.info("Resetting login failures after successful login: "
					    + authentication.getName());

				talo.log(CoreCatalog.infoUserLogin(authentication.getName()));

				securityService.loginSuccess(authentication.getName());
			} else if (talo.isDebugEnabled())
				talo.debug("Anonymous user");

		}
	}

	public void setSecurityService(SecurityService service) {
		this.securityService = service;
	}
}
