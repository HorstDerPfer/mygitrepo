package db.training.easy.web;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.ValidatorForm;

public abstract class BaseValidatorForm extends ValidatorForm {

	private static final long serialVersionUID = 6069257387850302043L;

	/**
	 * setzt das angegebene Property auf <code>null</code> wenn der Request zur selben Seite gehört
	 * (Referer = RequestURI)
	 * 
	 * @param arg1
	 *            Request
	 * @param form
	 *            das Formular (normalerweise kann <code>this</code> gewählt werden
	 * @param property
	 *            Name des Properties
	 * @param value
	 *            Wert, der in Property geschrieben werden soll, um dieses zurückzusetzen
	 * @author michels
	 * @see Internet, super.reset Workaround für das Zurücksetzen von Multiboxen
	 */
	protected void resetProperty(HttpServletRequest arg1, ActionForm form, String property,
	    Object value) {
		Logger log = Logger.getLogger(this.getClass());
		try {
			if (arg1.getParameter(property) == null && arg1.getHeader("referer") != null
			    && arg1.getHeader("referer").indexOf(arg1.getRequestURI()) >= 0) {
				PropertyUtils.setProperty(form, property, value);
				if (log.isDebugEnabled())
					log.debug(property + " reset.");
			}
		} catch (NoSuchMethodException ex) {
			if (log.isDebugEnabled())
				log.debug("Property not found: " + property);
			if (log.isDebugEnabled())
				log.debug(ex.getMessage());
		} catch (IllegalAccessException ex) {
			if (log.isDebugEnabled())
				log.debug(ex.getMessage(), ex);
		} catch (InvocationTargetException ex) {
			if (log.isDebugEnabled())
				log.debug(ex.getMessage(), ex);
		}
	}
}