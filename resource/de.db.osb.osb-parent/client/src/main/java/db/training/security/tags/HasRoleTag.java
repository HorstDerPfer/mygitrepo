package db.training.security.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.security.hibernate.TqmUser;

@SuppressWarnings("serial")
public class HasRoleTag extends TagSupport {

	private String role;

	private Logger logger = Logger.getLogger(HasRoleTag.class);

	@Override
	public int doStartTag() throws JspException {
		TqmUser currentUser = (TqmUser) EasyServiceFactory.getInstance().createSecurityService()
		    .getCurrentUser();

		boolean accessGranted = false;
		String[] requestedRoles = role.split(",");

		for (String requestedRole : requestedRoles) {
			if (currentUser.hasRole(requestedRole.trim())) {
				accessGranted = true;
				break;
			}
		}

		if (accessGranted) {
			if (logger.isDebugEnabled())
				logger.debug("Access granted for " + toString());
			return EVAL_BODY_INCLUDE;
		} else {
			if (logger.isDebugEnabled())
				logger.debug("Access denied for " + toString());
			return SKIP_BODY;
		}
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return String.format("%s <hasRole role='%s'", getClass().getSimpleName(), role);
	}

}
