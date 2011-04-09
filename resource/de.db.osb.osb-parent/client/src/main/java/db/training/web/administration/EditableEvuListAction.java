package db.training.web.administration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.EVU;
import db.training.bob.model.EVUGruppe;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.EVUService;
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
public class EditableEvuListAction extends BaseDispatchAction {

	private static Logger log = Logger.getLogger(EditableEvuListAction.class);

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableEvuListAction; Method =list");

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering listEditableEvuListAction Method= insert");

		EditableEvuListItemForm listItemForm = (EditableEvuListItemForm) form;

		EVUService evuService = EasyServiceFactory.getInstance().createEVUService();

		// Kundennummer auf Eindeutigkeit prüfen
		EVU evuItem = evuService.findByKundenNr(listItemForm.getInsertKundenNr());
		try {

			if (evuItem != null) {
				if (log.isDebugEnabled())
					log.debug("dupicate EVU ID =" + evuItem.getId() + "; " + evuItem.getCaption());
				addError("error.duplicateEntry");
			} else {
				evuItem = new EVU();
				evuItem.setKundenNr(listItemForm.getInsertKundenNr());
				evuItem.setName(listItemForm.getInsertName());
				evuItem.setKurzbezeichnung(listItemForm.getInsertKurzbezeichnung());

				// Kundengruppe
				EVUGruppeService evugruppeService = EasyServiceFactory.getInstance()
				    .createEVUGruppeService();
				EVUGruppe evugruppeitem = null;

				evugruppeitem = evugruppeService.findById(FrontendHelper
				    .castStringToInteger(listItemForm.getInsertEvugruppeId()));

				if (evugruppeitem != null) {
					if (log.isDebugEnabled())
						log.debug("evugruppe: " + listItemForm.getInsertEvugruppe());

					evuItem.setEvugruppe(evugruppeitem);
				} else {
					log.debug("could not find EVU-Kundengruppe.");
					addError("error.evu.kundengruppe.notfound");
				}

				evuService.create(evuItem);
				addMessage("success.saved");

				request.removeAttribute("insertKundenNr");
				request.removeAttribute("insertName");
				listItemForm.reset();
			}

		} catch (Exception e) {
			log.debug("could not find EVU-Kundengruppe.");
			addError("error.evu.kundengruppe.notfound");
		}

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering listEditableEvuListAction; Method =update");

		EditableEvuListItemForm listItemForm = (EditableEvuListItemForm) form;
		try {
			if (listItemForm != null) {
				Integer evuId = listItemForm.getId();
				if (evuId != null) {
					// EVU Datensatz ändern
					EVUService evuService = EasyServiceFactory.getInstance().createEVUService();
					EVU evuItemId = evuService.findById(evuId);
					// EVU evuItemNr = evuService.findByKundenNr(listItemForm.getUpdateKundenNr());

					if (log.isDebugEnabled())
						log.debug("updating EVU Object(ID=" + evuItemId.getId() + "): ");
					if (evuItemId != null) {
						// EVU Kundennummer ist unique
						if (!evuItemId.equals(evuService.findByKundenNr(listItemForm
						    .getUpdateKundenNr()))) {
							log
							    .debug("could not execute update due to unique constraint violation: kundenNr");
							addError("error.evu.nummernotunique");
						} else {

							if (FrontendHelper.stringNotNullOrEmpty(listItemForm
							    .getUpdateKundenNr())) {
								if (log.isDebugEnabled())
									log.debug("kundenNr: " + evuItemId.getKundenNr() + " => "
									    + listItemForm.getUpdateKundenNr());
								evuItemId.setKundenNr(listItemForm.getUpdateKundenNr());
							}

							if (FrontendHelper.stringNotNullOrEmpty(listItemForm.getUpdateName())) {
								if (log.isDebugEnabled())
									log.debug("name: " + evuItemId.getName() + " => "
									    + listItemForm.getUpdateName());
								evuItemId.setName(listItemForm.getUpdateName());
							}

							if (FrontendHelper.stringNotNullOrEmpty(listItemForm
							    .getUpdateKurzbezeichnung())) {
								if (log.isDebugEnabled())
									log.debug("kurzbezeichnung: " + evuItemId.getKurzbezeichnung()
									    + " => " + listItemForm.getUpdateKurzbezeichnung());
								evuItemId.setKurzbezeichnung(listItemForm
								    .getUpdateKurzbezeichnung());
							}

							// Kundengruppe
							if (FrontendHelper.stringNotNullOrEmpty(listItemForm
							    .getUpdateEvugruppeId())) {

								EVUGruppeService evugruppeService = EasyServiceFactory
								    .getInstance().createEVUGruppeService();
								EVUGruppe evugruppeitem = null;

								evugruppeitem = evugruppeService.findById(FrontendHelper
								    .castStringToInteger(listItemForm.getUpdateEvugruppeId()));

								if (evugruppeitem != null) {
									if (log.isDebugEnabled())
										log.debug("kundengruppe: "
										    + evuItemId.getEvugruppe().getName() + " => "
										    + listItemForm.getUpdateEvugruppe());

									evuItemId.setEvugruppe(evugruppeitem);

									// Änderungen anwenden
									evuService.update(evuItemId);
									addMessage("success.saved");

									// Edit Modus verlassen
									request.removeAttribute("id");
									listItemForm.setId(null);

								} else {
									log.debug("could not find EVU-Kundengruppe.");
									addError("error.evu.kundengruppe.notfound");
								}

							}

						}
					} else {
						addError("error.evu.notfound");
					}
				}
			}

		} catch (Exception e) {
			log.debug("could not find EVU-Kundengruppe.");
			addError("error.evu.kundengruppe.notfound");
		}

		request.setAttribute("displaytagParamMap", DisplaytagHelper.getParameterMap(request
		    .getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems());

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering listEditableEvuListAction; Method =delete");

		EditableEvuListItemForm listItemForm = (EditableEvuListItemForm) form;

		if (listItemForm != null) {
			Integer evuId = listItemForm.getId();
			if (evuId != null) {
				// EVU Datensatz löschen
				EVUService evuService = EasyServiceFactory.getInstance().createEVUService();
				EVU evuItem = evuService.findById(evuId);

				if (evuItem != null) {
					if (log.isDebugEnabled())
						log.debug("deleting EVU Object(ID=" + evuItem.getId() + "): "
						    + evuItem.getCaption());
					evuService.delete(evuItem);
					addMessage("success.evu.deletedfromlist");
				} else {
					addError("error.evu.notfound");
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
		EVUService evuService = EasyServiceFactory.getInstance().createEVUService();
		List<EVU> list = evuService.findAll();
		List<EditableDisplaytagItem<Integer>> result = null;

		if (list != null) {
			List<EVU> usedEvu = evuService.findAllInUse();

			result = new ArrayList<EditableDisplaytagItem<Integer>>(list.size());

			for (EVU evuItem : list) {
				EditableEvuDisplaytagItem newItem = new EditableEvuDisplaytagItem();
				newItem.setId(evuItem.getId());
				newItem.setText(evuItem.getName());
				newItem.setKundenNr(evuItem.getKundenNr());
				newItem.setKurzbezeichnung(evuItem.getKurzbezeichnung());
				newItem.setEvugruppe(evuItem.getEvugruppe());

				//
				// EVUs, die bereits von anderen Datensätzen referenziert werden, dürfen nicht
				// geändert werden
				//
				newItem.setReadonly(usedEvu.contains(evuItem));

				result.add(newItem);
			}
		} else {
			result = new ArrayList<EditableDisplaytagItem<Integer>>();
		}

		return result;
	}
}
