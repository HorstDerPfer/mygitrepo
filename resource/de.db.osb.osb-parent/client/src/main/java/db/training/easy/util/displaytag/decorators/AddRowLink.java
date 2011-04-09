package db.training.easy.util.displaytag.decorators;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.TableDecorator;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.fplo.Zugcharakteristik;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.model.User;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.SqlQuery;
import db.training.osb.model.UmleitungFahrplanregelungLink;
import db.training.security.hibernate.TqmAuthorization;

/**
 * @remarks Damit eine URL Collection verwendet wird, muss der Bezeichner mit <code>__urls_</code>
 *          beginnen oder exakt gleich <code>urls</code> sein. Wird zu beiden Kriterien ein Bean
 *          gefunden, wird <code>urls</code> verwendet.
 */
public class AddRowLink extends TableDecorator {
	@SuppressWarnings("unchecked")
	public String addRowId() {
		// URL Collection in Scope PAGE suchen
		HashMap<String, Object> urls = null;
		Enumeration<String> attributeEnum = getPageContext().getAttributeNamesInScope(
		    PageContext.PAGE_SCOPE);
		for (String attributeName = attributeEnum.nextElement(); attributeEnum.hasMoreElements(); attributeName = attributeEnum
		    .nextElement()) {
			if (attributeName.indexOf("__urls_") == 0) {
				urls = (HashMap<String, Object>) getPageContext().getAttribute(attributeName);
				break;
			}
		}
		if (getPageContext().getAttribute("urls") != null) {
			urls = (HashMap<String, Object>) getPageContext().getAttribute("urls");
		}

		StringBuffer buffer = new StringBuffer(1000);
		String index = null;

		if (getCurrentRowObject() instanceof User) {
			index = ((User) getCurrentRowObject()).getId().toString();
		}
		if (getCurrentRowObject() instanceof TqmAuthorization) {
			index = ((TqmAuthorization) getCurrentRowObject()).getId().toString();
		}
		if (getCurrentRowObject() instanceof Baumassnahme) {
			index = ((Baumassnahme) getCurrentRowObject()).getId().toString();
		}
		if (getCurrentRowObject() instanceof Zugcharakteristik) {
			index = ((Zugcharakteristik) getCurrentRowObject()).getId().toString();
			buffer.append("rowid_" + index);

			if (urls != null) {
				Object url = urls.get("" + index);
				if (url != null) {
					buffer.append("\" onmouseover=\"changeRowStyle(this)");
					buffer.append("\" onclick=\"" + urls.get("" + index));
				}
			}

			return buffer.toString();
		}
		if (getCurrentRowObject() instanceof BBPMassnahme) {
			index = ((BBPMassnahme) getCurrentRowObject()).getId().toString();
		}
		if (getCurrentRowObject() instanceof SAPMassnahme) {
			index = ((SAPMassnahme) getCurrentRowObject()).getId().toString();
		}
		if (getCurrentRowObject() instanceof SqlQuery) {
			index = ((SqlQuery) getCurrentRowObject()).getId().toString();
			buffer.append("rowid_" + index);
			if (urls != null) {
				Object url = urls.get("" + index);
				if (url != null)
					buffer.append("\" onmouseover=\"changeRowBlank(this,'" + urls.get("" + index)
					    + "');");
			}
			return buffer.toString();
		}
		if (getCurrentRowObject() instanceof EasyPersistentObject) {
			index = ((EasyPersistentObject) getCurrentRowObject()).getId().toString();
		}
		if (getCurrentRowObject() instanceof UmleitungFahrplanregelungLink) {
			index = ((UmleitungFahrplanregelungLink) getCurrentRowObject()).getUmleitung().getId()
			    .toString();
		}

		buffer.append("rowid_" + index);

		if (urls != null) {
			Object url = urls.get("" + index);
			if (url != null)
				buffer.append("\" onmouseover=\"changeRow(this,'" + urls.get("" + index) + "');");
		}

		return buffer.toString();
	}
}
