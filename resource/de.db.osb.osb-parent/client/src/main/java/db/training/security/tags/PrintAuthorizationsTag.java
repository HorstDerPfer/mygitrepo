package db.training.security.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.acegisecurity.GrantedAuthority;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.security.hibernate.TqmUser;

public class PrintAuthorizationsTag extends TagSupport {

	private static final long serialVersionUID = -7049578845567748905L;

	@Override
	public int doStartTag() throws JspException {

		TqmUser currentUser = (TqmUser) EasyServiceFactory.getInstance().createSecurityService()
		    .getCurrentUser();
		try {
			if (currentUser == null)
				pageContext.getOut().write("No user logged in");
			else {
				GrantedAuthority[] authorities = currentUser.getAuthorities();
				for (GrantedAuthority grantedAuthority : authorities) {
					pageContext.getOut().write(grantedAuthority.getAuthority() + "<br>");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return SKIP_BODY;

	}

}
