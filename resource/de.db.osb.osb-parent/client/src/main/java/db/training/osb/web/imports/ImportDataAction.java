package db.training.osb.web.imports;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.osb.service.ImportService;
import db.training.security.hibernate.TqmUser;

/**
 * @author mst
 * 
 */
public class ImportDataAction extends BaseAction {

	private static final Logger log = Logger.getLogger(ImportDataAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering ImportDataAction.");

		TqmUser secUser = getSecUser();

		// Zugriffsberechtigungen prüfen
		if (secUser == null || !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
			addError("error.access.denied");
			return mapping.findForward("FAILURE");
		}

		HttpSession session = request.getSession();

		// Session aufraeumen, falls vorher Import erfolgte
		session.removeAttribute("importMessages");
		session.removeAttribute("uploadFile");

		ImportDataForm importDataForm = (ImportDataForm) form;

		if (request.getParameter("reset") != null
		    && "true".equalsIgnoreCase(request.getParameter("reset"))) {
			importDataForm.reset();
		}

		InputStream inputStream = null;
		ImportService importService = serviceFactory.createImportService();

		request.setAttribute("importTableTypes", Arrays.asList(ImportTable.values()));

		if (importDataForm.getFile() != null
		    && importDataForm.getFile().getFileName().endsWith(".xls")
		    && importDataForm.getFile().getFileSize() > 0) {
			inputStream = importDataForm.getFile().getInputStream();
			if (inputStream != null) {
				List<ImportMessage> importMessages = importService.importData(inputStream, secUser);
				if (importMessages != null && importMessages.size() > 0) {
					session.setAttribute("importMessages", importMessages);
					addMessage("success.import.file.messages");
				} else {
					session.setAttribute("importMessages", importMessages);
					addMessage("success.import.file");
				}
			} else {
				log.error("Inputstream ist null.");
				addError("error.import.file");
				return mapping.findForward("FAILURE");
			}
			importDataForm.reset();
		} else if (importDataForm.getFile() != null) {
			log.error("Importdatei fehlerhaft.");
			addError("error.import.file");
			return mapping.findForward("FAILURE");
		}

		return mapping.findForward("SUCCESS");
	}
}