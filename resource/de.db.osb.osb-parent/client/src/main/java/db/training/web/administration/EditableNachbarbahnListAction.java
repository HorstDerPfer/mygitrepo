package db.training.web.administration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Nachbarbahn;
import db.training.bob.service.NachbarbahnService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.DisplaytagHelper;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseDispatchAction;
import db.training.easy.web.constants.CommonConstants;
import db.training.logwrapper.Logger;

/*
 * erzeugt eine Collection von Listeneinträgen vom Typ EditableDisplaytagItem<Integer>, welches im
 * Request als Attribut "editableListItems" verfügbar gemacht wird.
 */
public class EditableNachbarbahnListAction extends BaseDispatchAction {

	private static final Logger log = Logger.getLogger(EditableNachbarbahnListAction.class);

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableNachbarbahnListAction; Method =list");

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableNachbarbahnListAction Method= insert");

		EditableNachbarbahnListItemForm listItemForm = (EditableNachbarbahnListItemForm) form;

		NachbarbahnService nbbService = EasyServiceFactory.getInstance().createNachbarbahnService();

		// neue Nachbarbahn anlegen
		Nachbarbahn nbbItem = new Nachbarbahn(listItemForm.getInsertName());
		nbbService.create(nbbItem);
		addMessage("success.saved");

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));

		request.setAttribute("editableListItems", getEditableListItems());

		listItemForm.reset();

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableNachbarbahnListAction; Method =update");

		EditableNachbarbahnListItemForm listItemForm = (EditableNachbarbahnListItemForm) form;

		if (listItemForm != null) {
			Integer nbbId = listItemForm.getId();
			if (nbbId != null) {
				// Nachbarbahn ändern
				NachbarbahnService nbbService = EasyServiceFactory.getInstance()
				    .createNachbarbahnService();
				Nachbarbahn nbbItemId = nbbService.findById(nbbId);

				if (log.isDebugEnabled())
					log.debug("updating Nachbarbahn Object(ID=" + nbbItemId.getId() + "): ");
				if (nbbItemId != null) {

					if (FrontendHelper.stringNotNullOrEmpty(listItemForm.getUpdateName())) {
						if (log.isDebugEnabled())
							log.debug("name: " + nbbItemId.getName() + " => "
							    + listItemForm.getUpdateName());
						nbbItemId.setName(listItemForm.getUpdateName());
					}

					// Änderungen anwenden
					nbbService.update(nbbItemId);
					addMessage("success.saved");

				} else {
					addError("error.nachbarbahn.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		// Edit Modus verlassen
		request.removeAttribute("id");
		listItemForm.setId(null);

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableNachbarbahnListAction; Method =delete");

		EditableNachbarbahnListItemForm listItemForm = (EditableNachbarbahnListItemForm) form;

		if (listItemForm != null) {
			Integer nbbId = listItemForm.getId();
			if (nbbId != null) {
				// Nachbarbahn Datensatz löschen
				NachbarbahnService nbbService = EasyServiceFactory.getInstance()
				    .createNachbarbahnService();
				Nachbarbahn nbbItem = nbbService.findById(nbbId);

				if (nbbItem != null) {
					if (log.isDebugEnabled())
						log.debug("deleting Nachbarbahn Object(ID=" + nbbItem.getId() + "): "
						    + nbbItem.getName());
					nbbService.delete(nbbItem);
					addMessage("success.nachbarbahn.deletedfromlist");
				} else {
					addError("error.nachbarbahn.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	/**
	 * erzeugt eine Collection von Listeneinträgen vom Typ EditableDisplaytagItem<Integer> und gibt
	 * diese zurück
	 * 
	 * @return
	 */
	private List<EditableDisplaytagItem<Integer>> getEditableListItems() {
		NachbarbahnService nbbService = EasyServiceFactory.getInstance().createNachbarbahnService();
		List<Nachbarbahn> list = nbbService.findAll();
		List<EditableDisplaytagItem<Integer>> result = null;

		if (list != null) {
			result = new ArrayList<EditableDisplaytagItem<Integer>>(list.size());

			for (Nachbarbahn nbbItem : list) {
				EditableEvuDisplaytagItem newItem = new EditableEvuDisplaytagItem();
				newItem.setId(nbbItem.getId());
				newItem.setText(nbbItem.getName());

				newItem.setReadonly(false);

				result.add(newItem);
			}
		} else {
			result = new ArrayList<EditableDisplaytagItem<Integer>>();
		}

		return result;
	}
}
