package db.training.bob.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import db.training.bob.model.StatusType;

@SuppressWarnings("serial")
public class StatusIconTag extends TagSupport {

	private static MessageResources messageResources = null;

	private StatusType status = null;

	// private SortedSet<StatusType> statusSet = null;

	private boolean showLabel = false;

	private boolean showIcon = true;

	public StatusIconTag() {
		super();
		// statusSet = new TreeSet<StatusType>();
	}

	public static MessageResources getMessageResources() {
		if (messageResources == null) {
			messageResources = MessageResources.getMessageResources("MessageResources");
		}
		return messageResources;
	}

	public int doStartTag() throws javax.servlet.jsp.JspTagException {
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws javax.servlet.jsp.JspTagException {
		try {
			StatusType status = (StatusType) getStatus();

			// URL für Bilder zusammenstellen
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

			String iconName = "null";
			switch (status) {
			case LILA:
				iconName = "lila";
				break;
			case RED:
				iconName = "red";
				break;
			case GREEN:
				iconName = "green";
				break;
			case COUNTDOWN14:
				iconName = "yellow";
				break;
			case OFFEN:
				iconName = "countdown14";
				break;
			case NEUTRAL:
			default:
				iconName = "null";
				break;
			}

			String iconPath = request.getContextPath() + "/static/img/ampeltext" + iconName
			    + ".gif";

			if (showIcon) {
				pageContext.getOut().write("<img src=\"" + iconPath + "\" />");
			}

			if (showLabel) {
				String key = "baumassnahme.status." + status.name().toLowerCase();
				String label = getMessageResources().getMessage(key);
				pageContext.getOut().write(label);
			}

		} catch (java.io.IOException e) {
			throw new JspTagException("IO Error " + e.getMessage());
		} finally {
			// Status zurücksetzen
			// statusSet = new TreeSet<StatusType>();
			this.status = null;
		}

		return EVAL_PAGE;
	}

	/**
	 * fügt einen weiteren Status zur Menge der auszuwertenden Status hinzu.
	 * 
	 * @param s
	 *            the status to set
	 */
	public void setStatus(Object s) {
		if (s instanceof db.training.bob.model.StatusType) {
			status = (StatusType) s;
		} else if (s instanceof java.lang.String) {
			String str = (String) s;
			status = StatusType.valueOf(str);
		} else {
			status = null;
		}
		// if (s instanceof db.training.bob.model.StatusType) {
		// statusSet.add((StatusType) s);
		// } else if (s instanceof java.lang.String) {
		// String str = (String) s;
		// statusSet.add(StatusType.valueOf(str));
		// } else {
		// statusSet.add(null);
		// }
	}

	/**
	 * wertet die Status aus und gibt den Status mit der höchsten Priorität zurück
	 */
	public Object getStatus() {
		return status;
		// StatusType result = null;
		//
		// result = statusSet.last();
		//
		// return result;
	}

	/**
	 * @param showLabel
	 *            the showLabel to set
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * @return the showLabel
	 */
	public boolean isShowLabel() {
		return showLabel;
	}

	public boolean isShowIcon() {
		return this.showIcon;
	}

	public void setShowIcon(boolean showIcon) {
		this.showIcon = showIcon;
	}
}
