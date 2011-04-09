package db.training.web.administration;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Art;
import db.training.bob.model.Meilenstein;
import db.training.bob.model.Meilensteinbezeichnungen;
import db.training.bob.model.Schnittstelle;
import db.training.bob.service.MeilensteinService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.DisplaytagHelper;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseDispatchAction;
import db.training.easy.web.constants.CommonConstants;
import db.training.logwrapper.Logger;
import db.training.security.hibernate.TqmUser;

/*
 * erzeugt eine Collection von Listeneinträgen vom Typ EditableDisplaytagItem<Integer>, welches im
 * Request als Attribut "editableListItems" verfügbar gemacht wird.
 */
public class EditableMeilensteinListAction extends BaseDispatchAction {

	private static final Logger log = Logger.getLogger(EditableMeilensteinListAction.class);

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableMeilensteinListAction; Method =list");

		TqmUser secUser = getSecUser();
		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL"))
			return mapping.findForward(CommonConstants.STRUTS_FAILURE);

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));
		Integer id = FrontendHelper.castStringToInteger(request.getParameter("id"));
		request.setAttribute("editableListItems", getEditableListItems(id, form, request));

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableMeilensteinListAction Method= insert");

		TqmUser secUser = getSecUser();
		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL"))
			return mapping.findForward(CommonConstants.STRUTS_FAILURE);

		EditableMeilensteinListItemForm listItemForm = (EditableMeilensteinListItemForm) form;

		MeilensteinService service = EasyServiceFactory.getInstance().createMeilensteinService();

		Meilenstein meilenstein = new Meilenstein();
		Art[] artenArray = Art.values();
		meilenstein.setArt(artenArray[listItemForm.getInsertArt()]);
		Schnittstelle[] schnittstellenArray = Schnittstelle.values();
		meilenstein.setSchnittstelle(schnittstellenArray[(listItemForm.getInsertSchnittstelle())]);
		Meilensteinbezeichnungen[] meilensteinbezeichnungenArray = Meilensteinbezeichnungen
		    .values();
		meilenstein.setBezeichnung(meilensteinbezeichnungenArray[listItemForm
		    .getInsertMeilensteinBezeichnung()]);
		meilenstein.setAnzahlWochenVorBaubeginn(FrontendHelper.castStringToInteger(listItemForm
		    .getInsertWochenVorBaubeginn()));
		meilenstein.setWochentag(listItemForm.getInsertWochentag());
		meilenstein.setMindestfrist(listItemForm.getInsertMindestfrist());
		meilenstein.setGueltigAbBaubeginn(FrontendHelper.castStringToDate(listItemForm
		    .getInsertGueltigAb()));

		service.create(meilenstein);
		addMessage("success.saved");

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));

		request.setAttribute("editableListItems", getEditableListItems(null, form, request));

		listItemForm.reset();

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableMeilensteinListAction; Method =update");

		TqmUser secUser = getSecUser();
		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL"))
			return mapping.findForward(CommonConstants.STRUTS_FAILURE);

		EditableMeilensteinListItemForm listItemForm = (EditableMeilensteinListItemForm) form;

		if (listItemForm != null) {
			Integer id = listItemForm.getId();
			if (id != null) {

				MeilensteinService service = EasyServiceFactory.getInstance()
				    .createMeilensteinService();
				Meilenstein meilenstein = service.findById(id);

				if (log.isDebugEnabled())
					log.debug("updating Meilenstein Object(ID=" + meilenstein.getId() + ")");

				if (meilenstein != null) {

					if (FrontendHelper.stringNotNullOrEmpty(listItemForm
					    .getUpdateWochenVorBaubeginn())) {
						int anzahlWochen = FrontendHelper.castStringToInteger(listItemForm
						    .getUpdateWochenVorBaubeginn());
						meilenstein.setAnzahlWochenVorBaubeginn(anzahlWochen);
					}

					meilenstein.setMindestfrist(listItemForm.getUpdateMindestfrist());

					if (FrontendHelper.stringNotNullOrEmpty(listItemForm.getUpdateGueltigAb())) {
						Date date = FrontendHelper.castStringToDate(listItemForm
						    .getUpdateGueltigAb());
						meilenstein.setGueltigAbBaubeginn(date);
					}

					meilenstein.setWochentag(listItemForm.getUpdateWochentag());

					// Änderungen anwenden
					service.update(meilenstein);
					addMessage("success.saved");

				} else {
					addError("error.meilenstein.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));
		request.setAttribute("editableListItems", getEditableListItems(null, form, request));

		// Edit Modus verlassen
		request.removeAttribute("id");
		listItemForm.setId(null);

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering EditableMeilensteinListAction; Method =delete");

		TqmUser secUser = getSecUser();
		if (!secUser.hasRole("ADMINISTRATOR_ZENTRAL"))
			return mapping.findForward(CommonConstants.STRUTS_FAILURE);

		EditableMeilensteinListItemForm listItemForm = (EditableMeilensteinListItemForm) form;

		if (listItemForm != null) {
			Integer id = listItemForm.getId();
			if (id != null) {
				// Nachbarbahn Datensatz löschen
				MeilensteinService service = EasyServiceFactory.getInstance()
				    .createMeilensteinService();
				Meilenstein meilenstein = service.findById(id);

				if (meilenstein != null) {
					if (log.isDebugEnabled())
						log.debug("deleting Meilenstein Object(ID=" + meilenstein.getId() + ")");
					service.delete(meilenstein);
					addMessage("success.meilenstein.deletedfromlist");
				} else {
					addError("error.meilenstein.notfound");
				}
			}
		}

		request.setAttribute("displaytagParamMap",
		    DisplaytagHelper.getParameterMap(request.getParameterMap()));

		request.setAttribute("editableListItems", getEditableListItems(null, form, request));

		return mapping.findForward(CommonConstants.STRUTS_SUCCESS);
	}

	/**
	 * @param request
	 */
	private void getMeilensteinbezeichnungenList(HttpServletRequest request) {
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

		// Integer art = listItemForm.getInsertArt();
		// Integer schnittstelle = listItemForm.getInsertSchnittstelle();
		Integer art = 0;
		if (request.getParameter("art") != null)
			art = FrontendHelper.castStringToInteger(request.getParameter("art"));
		Integer schnittstelle = 0;
		if (request.getParameter("schnittstelle") != null)
			schnittstelle = FrontendHelper.castStringToInteger((request
			    .getParameter("schnittstelle")));

		if (schnittstelle != null) {
			if (Schnittstelle.BBP.ordinal() == schnittstelle) {
				meilensteinBezeichnungen = meilensteinBezeichnungen.subList(0, 7);
				if (Art.KS.ordinal() == art || Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(3);
					meilensteinBezeichnungen.remove(4);
				}
			} else if (Schnittstelle.PEVU.ordinal() == schnittstelle) {
				meilensteinBezeichnungen.remove(14);
				meilensteinBezeichnungen.remove(13);
				if (Art.KS.ordinal() == art || Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(8);
					meilensteinBezeichnungen.remove(4);
				}
				meilensteinBezeichnungen.remove(5);
				meilensteinBezeichnungen.remove(3);
				meilensteinBezeichnungen.remove(1);
				meilensteinBezeichnungen.remove(0);
			} else if (Schnittstelle.GEVU.ordinal() == schnittstelle) {
				if (Art.KS.ordinal() == art || Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(13);
				}
				meilensteinBezeichnungen.remove(12);
				meilensteinBezeichnungen.remove(9);
				meilensteinBezeichnungen.remove(8);
				meilensteinBezeichnungen.remove(5);
				if (Art.KS.ordinal() == art || Art.B.ordinal() == art) {
					meilensteinBezeichnungen.remove(4);
				}
				meilensteinBezeichnungen.remove(3);
				meilensteinBezeichnungen.remove(1);
				meilensteinBezeichnungen.remove(0);
			}
		}
		request.setAttribute("meilensteinBezeichnungenList", meilensteinBezeichnungen);
	}

	/**
	 * @param request
	 */
	private void getSchnittstellenList(HttpServletRequest request) {
		// Schnittstellen
		Schnittstelle[] schnittstellenArray = Schnittstelle.values();
		List<EditableDisplaytagItem<Integer>> schnittstellen = new ArrayList<EditableDisplaytagItem<Integer>>();
		for (int i = 0; i < schnittstellenArray.length; i++) {
			EditableDisplaytagItem<Integer> item = new EditableDisplaytagItem<Integer>();
			item.setId(i);
			item.setText(schnittstellenArray[i].toString());
			schnittstellen.add(item);
		}
		request.setAttribute("schnittstellenList", schnittstellen);
	}

	/**
	 * @param request
	 */
	private void getArtenList(HttpServletRequest request) {
		// Baumassnahmenarten
		Art[] artenArray = Art.values();
		List<EditableDisplaytagItem<Integer>> arten = new ArrayList<EditableDisplaytagItem<Integer>>();
		for (int i = 0; i < artenArray.length; i++) {
			EditableDisplaytagItem<Integer> item = new EditableDisplaytagItem<Integer>();
			item.setId(i);
			item.setText(artenArray[i].toString());
			arten.add(item);
		}
		request.setAttribute("artenList", arten);
	}

	/**
	 * @param request
	 */
	private void getWochentagList(HttpServletRequest request) {
		DateFormatSymbols symbols = DateFormatSymbols.getInstance(Locale.GERMANY);
		String[] weekdays = symbols.getWeekdays();

		// Wochentage
		List<EditableDisplaytagItem<Integer>> wochentage = new ArrayList<EditableDisplaytagItem<Integer>>();
		for (int i = 1; i < weekdays.length; i++) {
			EditableDisplaytagItem<Integer> item = new EditableDisplaytagItem<Integer>();
			item.setId(i);
			item.setText(weekdays[i]);
			wochentage.add(item);
		}

		EditableDisplaytagItem<Integer> item = new EditableDisplaytagItem<Integer>();
		item.setId(0);
		item.setText(getResources(request).getMessage("meilenstein.wochentage.ohne"));
		wochentage.add(item);

		request.setAttribute("wochentagList", wochentage);
	}

	/**
	 * erzeugt eine Collection von Listeneinträgen vom Typ EditableDisplaytagItem<Integer> und gibt
	 * diese zurück
	 * 
	 * @param request
	 *            TODO
	 * @param parameterMap
	 * 
	 * @return
	 */
	private List<EditableDisplaytagItem<Integer>> getEditableListItems(Integer id, ActionForm form,
	    HttpServletRequest request) {

		MeilensteinService service = EasyServiceFactory.getInstance().createMeilensteinService();

		List<Meilenstein> list = service.findAll();
		List<EditableDisplaytagItem<Integer>> result = null;
		EditableMeilensteinListItemForm meilensteinForm = null;

		if (id != null)
			meilensteinForm = (EditableMeilensteinListItemForm) form;

		if (list != null) {
			result = new ArrayList<EditableDisplaytagItem<Integer>>(list.size());

			DateFormatSymbols symbols = DateFormatSymbols.getInstance(Locale.GERMANY);
			String[] weekdays = symbols.getWeekdays();
			weekdays[0] = getResources(request).getMessage("meilenstein.wochentage.ohne");
			String dateFormat = "dd.MM.yyyy";

			for (Meilenstein item : list) {
				if (item.getId().equals(id)) {
					meilensteinForm.setUpdateMindestfrist(item.isMindestfrist());
					meilensteinForm.setUpdateWochentag(item.getWochentag());
					meilensteinForm.setUpdateWochenVorBaubeginn(FrontendHelper
					    .castIntegerToStringStandard(item.getAnzahlWochenVorBaubeginn()));
					String date = FrontendHelper.castDateToString(item.getGueltigAbBaubeginn(),
					    dateFormat);
					if (date.equals(""))
						meilensteinForm.setUpdateGueltigAb(null);
					else
						meilensteinForm.setUpdateGueltigAb(date);
				}

				EditableMeilensteinDisplaytagItem newItem = new EditableMeilensteinDisplaytagItem();
				newItem.setId(item.getId());
				newItem.setArt(item.getArt().toString());
				newItem.setSchnittstelle(item.getSchnittstelle().toString());
				newItem.setMeilensteinBezeichnung(item.getBezeichnung().toString());
				newItem.setWochenVorBaubeginn(item.getAnzahlWochenVorBaubeginn());
				newItem.setWochentag(weekdays[item.getWochentag()]);
				newItem.setMindestfrist(item.isMindestfrist());

				newItem.setGueltigAb(FrontendHelper.castDateToString(item.getGueltigAbBaubeginn(),
				    dateFormat));

				newItem.setReadonly(false);

				result.add(newItem);
			}
		} else {
			result = new ArrayList<EditableDisplaytagItem<Integer>>();
		}

		getWochentagList(request);
		getArtenList(request);
		getSchnittstellenList(request);
		getMeilensteinbezeichnungenList(request);

		return result;
	}
}