package db.training.bob.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import db.training.bob.model.StatusType;

@SuppressWarnings("serial")
public class StatusIconParamTag extends TagSupport {

	private StatusIconTag parent;

	private StatusType status;

	public Object getValue() {
		return this.status;
	}

	public void setValue(Object status) {
		if (status instanceof db.training.bob.model.StatusType) {
			this.status = (StatusType) status;
		} else if (status instanceof java.lang.String) {
			String s = (String) status;
			this.status = StatusType.valueOf(s);
		} else {
			this.status = null;
		}
	}

	@Override
	public StatusIconTag getParent() {
		return parent;
	}

	@Override
	public void setParent(Tag t) {
		if (t instanceof StatusIconTag) {
			parent = (StatusIconTag) t;
		} else {
			throw new IllegalArgumentException(
			    "StatusIconParamTag ist nur innerhalb von StatusIconTag erlaubt.");
		}
	}
	
	@Override
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		// Parameter an StatusIconTag übergeben zur Auswertung
		getParent().setStatus(getValue());
		
		return EVAL_PAGE;
	}
}
