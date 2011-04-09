package db.training.security.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.hibernate.Hibernate;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.logwrapper.Logger;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.domain.VoterFactory;
import db.training.security.domain.VoterType;
import db.training.security.hibernate.TqmUser;

public class HasNotAuthorizationTag extends TagSupport {

	private static final long serialVersionUID = 7954349741604508718L;

	private String authorization;

	private Object model;

	private Logger log = Logger.getLogger(HasAuthorizationTag.class);

	@Override
	public int doStartTag() throws JspException {

		/*
		 * display tag library calls display:column with a null object, to work around this problem
		 * we will always not display the body, if no model is specified
		 */
		if (model == null)
			return SKIP_BODY;

		EasyAccessDecisionVoter voter = VoterFactory.getDecisionVoter(Hibernate.getClass(model),
		    VoterType.NONE);
		if (voter == null)
			throw new IllegalArgumentException(String.format(
			    "Tag hasAutorization cannot find a Voter for the tag %s", toString()));
		TqmUser currentUser = (TqmUser) EasyServiceFactory.getInstance().createSecurityService()
		    .getCurrentUser();

		if (voter.vote(currentUser, model, authorization) == AccessDecisionVoter.ACCESS_GRANTED) {
			if (log.isDebugEnabled())
				log.debug("Access denied for " + toString());
			return EVAL_BODY_INCLUDE;
		} else {
			if (log.isDebugEnabled())
				log.debug("Access granted for " + toString());
			return SKIP_BODY;
		}
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return String.format("%s <hasNotAuthorization authorization='%s' model='%s'", getClass()
		    .getSimpleName(), authorization, model);
	}
}
