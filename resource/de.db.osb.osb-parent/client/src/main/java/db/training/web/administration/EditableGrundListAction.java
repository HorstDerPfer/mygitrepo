package db.training.web.administration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Grund;
import db.training.bob.service.GrundService;
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
public class EditableGrundListAction extends BaseDispatchAction {

	private static Logger log = Logger.getLogger(EditableGrundListAction.class);

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableGrundListAction; Method =list");

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableGrundListAction Method= insert");

		EditableGrundListItemForm listItemForm = (EditableGrundListItemForm) form;

		GrundService grundService = EasyServiceFactory.getInstance().createGrundService();

		// neuen Grund anlegen
		Grund grundItem = new Grund(listItemForm.getInsertName());
		grundService.create(grundItem);
		addMessage("success.saved");

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));

		request.setAttribute("editableListItems", getEditableListItems());

		listItemForm.reset();

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableGrundListAction; Method =update");

		EditableGrundListItemForm listItemForm = (EditableGrundListItemForm) form;

		if (listItemForm != null) {
			Integer grundId = listItemForm.getId();
			if (grundId != null) {
				// Grund ändern
				GrundService grundService = EasyServiceFactory.getInstance().createGrundService();
				Grund grundItemId = grundService.findById(grundId);

				if (log.isDebugEnabled())
					log.debug("updating Grund Object(ID=" + grundItemId.getId() + "): ");
				if (grundItemId != null) {

					if (FrontendHelper.stringNotNullOrEmpty(listItemForm.getUpdateName())) {
						if (log.isDebugEnabled())
							log.debug("name: " + grundItemId.getName() + " => "
							    + listItemForm.getUpdateName());
						grundItemId.setName(listItemForm.getUpdateName());
					}

					// Änderungen anwenden
					grundService.update(grundItemId);
					addMessage("success.saved");

				} else {
					addError("error.grund.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		// Edit Modus verlassen
		request.removeAttribute("id");
		listItemForm.setId(null);

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableGrundListAction; Method =delete");

		EditableGrundListItemForm listItemForm = (EditableGrundListItemForm) form;

		if (listItemForm != null) {
			Integer grundId = listItemForm.getId();
			if (grundId != null) {
				// Grund Datensatz löschen
				GrundService grundService = EasyServiceFactory.getInstance().createGrundService();
				Grund grundItem = grundService.findById(grundId);

				if (grundItem != null) {
					if (log.isDebugEnabled())
						log.debug("deleting Grund Object(ID=" + grundItem.getId() + "): "
						    + grundItem.getName());
					grundService.delete(grundItem);
					addMessage("success.grund.deletedfromlist");
				} else {
					addError("error.grund.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));
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
		GrundService grundService = EasyServiceFactory.getInstance().createGrundService();
		List<Grund> list = grundService.findAll();
		List<EditableDisplaytagItem<Integer>> result = null;

		if (list != null) {
			List<Grund> usedGruende = grundService.findAllInUse();

			result = new ArrayList<EditableDisplaytagItem<Integer>>(list.size());

			for (Grund grundItem : list) {
				EditableEvuDisplaytagItem newItem = new EditableEvuDisplaytagItem();
				newItem.setId(grundItem.getId());
				newItem.setText(grundItem.getName());

				newItem.setReadonly(usedGruende.contains(grundItem));

				result.add(newItem);
			}
		} else {
			result = new ArrayList<EditableDisplaytagItem<Integer>>();
		}

		return result;
	}
}
