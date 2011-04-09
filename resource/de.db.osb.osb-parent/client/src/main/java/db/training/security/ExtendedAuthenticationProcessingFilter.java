package db.training.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.security.hibernate.TqmUser;
import db.training.security.password.PasswordException;

/**
 * Erweitert den Acegi Filter um die Moeglichkeit eine updateCredentialSeite zu verwenden, auf der
 * ein neues Kennwort eingegeben werden kann.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class ExtendedAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

	@SuppressWarnings("unused")
	private String updateCredentialPage;

	public void setUpdateCredentialPage(String updateCredentialPage) {
		this.updateCredentialPage = updateCredentialPage;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request)
	    throws AuthenticationException {

		// Fahrplanjahr wird beim Einloggen festgelegt und gilt fuer die gesamte Session
		if (request.getParameter("fahrplanjahr") != null)
			request.getSession().setAttribute("session_fahrplanjahr",
			    FrontendHelper.castStringToInteger(request.getParameter("fahrplanjahr")));

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		String newPassword = request.getParameter("newpassword");
		String confirmPassword = request.getParameter("confirmpassword");

		List<String> loginErrors = new ArrayList<String>();

		if (username == null) {
			username = "";
		}
		if (password == null) {
			password = "";
		}
		if (newPassword != null) {
			if (confirmPassword == null || !newPassword.equals(confirmPassword))
				loginErrors.add("error.user.password.diff");
			else {
				SecurityAdministrationService administrationService = EasyServiceFactory
				    .getInstance().createSecurityAdministrationService();
				SecurityService securityService = EasyServiceFactory.getInstance()
				    .createSecurityService();
				TqmUser tqmUser = (TqmUser) securityService.loadUserByUsername(username);
				if (tqmUser != null) {
					// Wenn wir keinen Benutzer finden, fehlt das login ohnehin fehl
					try {
						administrationService.changePassword(tqmUser, password, newPassword);
						password = newPassword;
					} catch (SecurityException e) {
						loginErrors.add("error.security.password.BAD_PASSWORD");
						e.printStackTrace();
					} catch (PasswordException e) {
						switch (e.getDescription()) {
						case EMPTY_PASSWORD:
							loginErrors.add("error.security.password.EMPTY_PASSWORD");
							break;
						case PASSWORD_TO_SHORT:
							loginErrors.add("error.security.password.PASSWORD_TO_SHORT");
							break;
						case REUSED_PASSWORD:
							loginErrors.add("error.security.password.REUSED_PASSWORD");
							break;
						case TRIVIAL_PASSWORD:
							loginErrors.add("error.security.password.TRIVIAL_PASSWORD");
							break;
						case UNSAFE_PASSWORD:
							loginErrors.add("error.security.password.UNSAFE_PASSWORD");
							break;
						}
						e.printStackTrace();
					}
				}
			}
		}

		// Fehlermeldungen werden in Session geschrieben
		request.getSession().setAttribute("loginErrors", loginErrors);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
		    username, password);

		// Place the last username attempted into HttpSession for views
		request.getSession().setAttribute(ACEGI_SECURITY_LAST_USERNAME_KEY, username);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
