package db.training.web.administration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.web.constants.CommonConstants;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.DisplaytagHelper;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseDispatchAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

/*
 * erzeugt eine Collection von Listeneinträgen vom Typ EditableDisplaytagItem<Integer>, welches im
 * Request als Attribut "editableListItems" verfügbar gemacht wird.
 */
public class EditableFplBearbeitungsbereichListAction extends BaseDispatchAction {

	private static Logger log = Logger.getLogger(EditableFplBearbeitungsbereichListAction.class);

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableFplBearbeitungsbereichListAction; Method =list");

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems(form));
		request.setAttribute("regionalbereichList", getRegionalbereichList());

		return mapping.findForward(db.training.easy.web.constants.CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering listEditableEvuListAction Method= insert");

		EditableFplBearbeitungsbereichListItemForm listItemForm = (EditableFplBearbeitungsbereichListItemForm) form;

		BearbeitungsbereichService bbFplService = EasyServiceFactory.getInstance()
		    .createBearbeitungsbereichService();
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		Integer regionalbereichId = FrontendHelper.castStringToInteger(listItemForm
		    .getInsertRegionalbereichFplId());
		Regionalbereich regionalbereich = regionalbereichService.findById(regionalbereichId);

		if (regionalbereich != null) {
			Bearbeitungsbereich bbFplItem = new Bearbeitungsbereich(listItemForm.getInsertName());
			bbFplItem.setRegionalbereich(regionalbereich);
			bbFplService.create(bbFplItem);
			addMessage("success.saved");
		}
		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems(form));
		request.setAttribute("regionalbereichList", getRegionalbereichList());
		request.removeAttribute("insertName");
		listItemForm.reset();

		return mapping.findForward(db.training.easy.web.constants.CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableFplBearbeitungsbereichListAction; Method =update");

		EditableFplBearbeitungsbereichListItemForm listItemForm = (EditableFplBearbeitungsbereichListItemForm) form;

		if (listItemForm != null) {
			Integer bbFplId = listItemForm.getId();
			if (bbFplId != null) {
				// EVU Datensatz ändern
				BearbeitungsbereichService bbFplService = EasyServiceFactory.getInstance()
				    .createBearbeitungsbereichService();
				RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
				    .createRegionalbereichService();
				Regionalbereich regionalbereich = regionalbereichService.findById(listItemForm
				    .getUpdateRegionalbereichFplId());
				// Regionalbereich regionalbereich = regionalbereichService.findById(FrontendHelper
				// .castStringToInteger(listItemForm.getUpdateRegionalbereichFplId()));

				Bearbeitungsbereich bbFplItem = bbFplService.findById(bbFplId,
				    new Preload[] { new Preload(Bearbeitungsbereich.class, "regionalbereich") });

				if (log.isDebugEnabled())
					log
					    .debug("updating Bearbeitungsbereich Object(ID=" + bbFplItem.getId()
					        + "): ");
				if (bbFplItem != null && regionalbereich != null) {

					if (listItemForm.getUpdateRegionalbereichFplId() != null) {
						if (log.isDebugEnabled())
							log.debug("regionalbereich: "
							    + bbFplItem.getRegionalbereich().getName() + " => "
							    + listItemForm.getUpdateRegionalbereichFplId());
						bbFplItem.setRegionalbereich(regionalbereich);
					}

					if (listItemForm.isReadonly() == false) {
						if (FrontendHelper.stringNotNullOrEmpty(listItemForm.getUpdateName())) {
							if (log.isDebugEnabled())
								log.debug("name: " + bbFplItem.getName() + " => "
								    + listItemForm.getUpdateName());
							bbFplItem.setName(listItemForm.getUpdateName());
						}
					}

					Integer valueMin = null;
					if (FrontendHelper.stringNotNullOrEmpty(listItemForm.getUpdateVorgangsnrMin())) {
						if (log.isDebugEnabled())
							log.debug("VorgangsnrMin: " + bbFplItem.getVorgangsnrMin() + " => "
							    + listItemForm.getUpdateVorgangsnrMin());
						valueMin = FrontendHelper.castStringToInteger(listItemForm
						    .getUpdateVorgangsnrMin());
					}

					Integer valueMax = null;
					if (FrontendHelper.stringNotNullOrEmpty(listItemForm.getUpdateVorgangsnrMax())) {
						if (log.isDebugEnabled())
							log.debug("VorgangsnrMax: " + bbFplItem.getVorgangsnrMax() + " => "
							    + listItemForm.getUpdateVorgangsnrMax());
						valueMax = FrontendHelper.castStringToInteger(listItemForm
						    .getUpdateVorgangsnrMax());
					}

					if (valueMin != null & valueMax != null) {
						if (valueMin < valueMax) {
							if (testUeberschneidungen(valueMin, valueMax, listItemForm.getId()) == false) {
								bbFplItem.setVorgangsnrMin(valueMin);
								bbFplItem.setVorgangsnrMax(valueMax);
							} else {
								addError("error.vorgangsnr.ueberscheidung");
							}
						} else {
							addError("error.vorgangsnr.bereich");
						}
						if (valueMin < 0 || valueMax < 0) {
							addError("error.vorgangsnr.negativ");
						}
					} else {
						addError("error.vorgangsnr.format");
					}

					// Änderungen anwenden
					if (errors.get().isEmpty()) {
						bbFplService.update(bbFplItem);
						addMessage("success.saved");
					}

				} else {
					addError("error.bearbeitungsbereichfpl.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems(form));
		request.setAttribute("regionalbereichList", getRegionalbereichList());

		// Edit Modus verlassen
		request.removeAttribute("id");
		listItemForm.setId(null);

		if (hasErrors(request))
			return mapping.findForward(CommonConstants.STRUTS_SUCCESS_REDIRECT);

		return mapping.findForward(db.training.easy.web.constants.CommonConstants.STRUTS_SUCCESS);
	}

	private boolean testUeberschneidungen(Integer valueMin, Integer valueMax,
	    Integer currentBearbeitungsbereichId) {
		BearbeitungsbereichService bearbeitungsbereichService = EasyServiceFactory.getInstance()
		    .createBearbeitungsbereichService();
		List<Bearbeitungsbereich> bearbeitungsbereiche = bearbeitungsbereichService.findAll();
		Iterator<Bearbeitungsbereich> it = bearbeitungsbereiche.iterator();
		Bearbeitungsbereich b = null;
		while (it.hasNext()) {
			b = it.next();
			if (!b.getId().equals(currentBearbeitungsbereichId)) {
				if (b.getVorgangsnrMin() != null & b.getVorgangsnrMax() != null) {
					// Schnittmenge
					if (valueMin >= b.getVorgangsnrMin() & valueMin <= b.getVorgangsnrMax())
						return true;
					if (valueMax >= b.getVorgangsnrMin() & valueMax <= b.getVorgangsnrMax())
						return true;
					// Teilmenge
					if (valueMax >= b.getVorgangsnrMax() & valueMin <= b.getVorgangsnrMin())
						return true;
					if (valueMax <= b.getVorgangsnrMax() & valueMin >= b.getVorgangsnrMin())
						return true;
				}
			}
		}
		return false;
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableFplBearbeitungsbereichListAction; Method =delete");

		EditableFplBearbeitungsbereichListItemForm listItemForm = (EditableFplBearbeitungsbereichListItemForm) form;

		if (listItemForm != null) {
			Integer bbFplId = listItemForm.getId();
			if (bbFplId != null) {
				// Bearbeitungsbereich Datensatz löschen
				BearbeitungsbereichService bearbeitungsbereichService = EasyServiceFactory
				    .getInstance().createBearbeitungsbereichService();
				Bearbeitungsbereich bbFplItem = bearbeitungsbereichService.findById(bbFplId);

				if (bbFplItem != null) {
					if (log.isDebugEnabled())
						log.debug("deleting Bearbeitungsbereich Object(ID=" + bbFplItem.getId()
						    + "): " + bbFplItem.getName());
					bearbeitungsbereichService.delete(bbFplItem);
					addMessage("success.bearbeitungsbereichfpl.deletedfromlist");
				} else {
					addError("error.bearbeitungsbereichfpl.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems(form));
		request.setAttribute("regionalbereichList", getRegionalbereichList());

		return mapping.findForward(db.training.easy.web.constants.CommonConstants.STRUTS_SUCCESS);
	}

	/**
	 * erzeugt eine Collection von Listeneinträgen vom Typ
	 * EditableFplBearbeitungsbereichDisplaytagItem und gibt diese zurück
	 * 
	 * @return
	 */
	private List<EditableFplBearbeitungsbereichDisplaytagItem> getEditableListItems(ActionForm form) {
		BearbeitungsbereichService bbFplService = EasyServiceFactory.getInstance()
		    .createBearbeitungsbereichService();
		List<Bearbeitungsbereich> list = bbFplService.findAll(new Preload[] { new Preload(
		    Bearbeitungsbereich.class, "regionalbereich") });
		List<EditableFplBearbeitungsbereichDisplaytagItem> result = null;
		EditableFplBearbeitungsbereichListItemForm listItemForm = (EditableFplBearbeitungsbereichListItemForm) form;

		if (list != null) {
			List<Bearbeitungsbereich> usedBbFpl = bbFplService.findAllInUse();

			result = new ArrayList<EditableFplBearbeitungsbereichDisplaytagItem>(list.size());

			for (Bearbeitungsbereich bbFplItem : list) {
				EditableFplBearbeitungsbereichDisplaytagItem newItem = new EditableFplBearbeitungsbereichDisplaytagItem();
				newItem.setId(bbFplItem.getId());
				newItem.setText(bbFplItem.getName());
				newItem.setRegionalbereichFpl(bbFplItem.getRegionalbereich().getName());
				newItem.setRegionalbereichFplId(bbFplItem.getRegionalbereich().getId());

				Integer vorgangsnrMin = bbFplItem.getVorgangsnrMin();
				Integer vorgangsnrMax = bbFplItem.getVorgangsnrMax();
				if (vorgangsnrMin != null)
					newItem.setVorgangsnrMin(vorgangsnrMin.toString());
				if (vorgangsnrMax != null)
					newItem.setVorgangsnrMax(vorgangsnrMax.toString());

				if (listItemForm != null) {// Vorbelegung Dropdown Regionalbereich
					Integer bbFplId = listItemForm.getId();
					if (bbFplItem.getId().equals(bbFplId)) {
						listItemForm.setUpdateRegionalbereichFplId(bbFplItem.getRegionalbereich()
						    .getId());
					}
				}

				//
				// Bearbeitungsbereich, die bereits von anderen Datensätzen referenziert werden,
				// dürfen nicht
				// geändert werden
				//
				boolean readonly = usedBbFpl.contains(bbFplItem);
				newItem.setReadonly(readonly);
				listItemForm.setReadonly(readonly);

				result.add(newItem);
			}
		} else {
			result = new ArrayList<EditableFplBearbeitungsbereichDisplaytagItem>();
		}

		return result;
	}

	private List<Regionalbereich> getRegionalbereichList() {
		RegionalbereichService regionalbereichService = EasyServiceFactory.getInstance()
		    .createRegionalbereichService();
		return regionalbereichService.findAll();
	}
}
