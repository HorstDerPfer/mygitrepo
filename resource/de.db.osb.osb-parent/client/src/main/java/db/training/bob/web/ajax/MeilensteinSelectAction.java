package db.training.bob.web.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Art;
import db.training.bob.model.Meilensteinbezeichnungen;
import db.training.bob.model.Schnittstelle;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.easy.web.constants.CommonConstants;
import db.training.logwrapper.Logger;
import db.training.web.administration.EditableDisplaytagItem;

public class MeilensteinSelectAction extends BaseAction {

	private static final Logger log = Logger.getLogger(MeilensteinSelectAction.class);

	public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering MeilensteinSelectAction");

		// Meilensteinbezeichnungen
		Meilensteinbezeichnungen[] meilensteinBezeichnungArray = Meilensteinbezeichnungen.values();
		List<EditableDisplaytagItem<Integer>> meilensteinBezeichnungen = new ArrayList<EditableDisplaytagItem<Integer>>();
		for (int i = 0; i < meilensteinBezeichnungArray.length; i++) {
			EditableDisplaytagItem<Integer> item = new EditableDisplaytagItem<Integer>();
			item.setId(i);
			item.setText(meilensteinBezeichnungArray[i].toString());
			meilensteinBezeichnungen.add(item);
		}

		// Je nach Auswahl von Art und Schnittstelle, alle nicht relevanten Meilensteine aus Liste
		// entfernen
		Integer art = 0;
		if (request.getParameter("art") != null)
			art = FrontendHelper.castStringToInteger(request.getParameter("art"));
		Integer schnittstelle = 0;
		if (request.getParameter("schnittstelle") != null)
			schnittstelle = FrontendHelper.castStringToInteger((request
			    .getParameter("schnittstelle")));

		if (log.isDebugEnabled()) {
			log.debug("ART =" + art);
			log.debug("SCHNITTSTELLE =" + schnittstelle);
		}

		if (schnittstelle != null) {
			if (Schnittstelle.BBP.ordinal() == schnittstelle) {
				meilensteinBezeichnungen = meilensteinBezeichnungen.subList(0, 7);
				if (Art.KS.ordinal() != art) {
					meilensteinBezeichnungen.remove(5);
				}
				if (Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(4);
				}
				meilensteinBezeichnungen.remove(3);
				if (Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(1);
				}
			} else if (Schnittstelle.PEVU.ordinal() == schnittstelle) {
				meilensteinBezeichnungen.remove(14);
				meilensteinBezeichnungen.remove(13);
				if (Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(8);
				}
				meilensteinBezeichnungen.remove(5);
				meilensteinBezeichnungen.remove(4);
				meilensteinBezeichnungen.remove(3);
				meilensteinBezeichnungen.remove(1);
				meilensteinBezeichnungen.remove(0);
			} else if (Schnittstelle.GEVU.ordinal() == schnittstelle) {
				if (Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(13);
				}
				meilensteinBezeichnungen.remove(12);
				meilensteinBezeichnungen.remove(9);
				meilensteinBezeichnungen.remove(8);
				meilensteinBezeichnungen.remove(5);
				meilensteinBezeichnungen.remove(4);
				meilensteinBezeichnungen.remove(3);
				meilensteinBezeichnungen.remove(1);
				meilensteinBezeichnungen.remove(0);
			}
		}
		request.setAttribute("meilensteinBezeichnungenList", meilensteinBezeichnungen);

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}
}
