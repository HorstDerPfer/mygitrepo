package db.training.osb.web.imports;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.easy.web.BaseAction;
import db.training.security.hibernate.TqmUser;

/**
 * @author mst
 * 
 */
public class DownloadTemplateAction extends BaseAction {

	private static Logger log = Logger.getLogger(DownloadTemplateAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering DownloadTemplateAction.");

		TqmUser secUser = getSecUser();

		// Zugriffsberechtigungen prüfen
		if (secUser == null || !secUser.hasRole("ADMINISTRATOR_ZENTRAL")) {
			addError("error.access.denied");
			return mapping.findForward("FAILURE");
		}

		ImportDataForm downloadTemplateForm = (ImportDataForm) form;
		ImportTable importTableType = ImportTable.valueOf(downloadTemplateForm.getImportTable());

		String outputFilename = "ImportTemplate " + importTableType + ".xls";

		HSSFWorkbook hssfworkbook = serviceFactory.createImportService().createWorkbookTemplate(
		    importTableType);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Cache-Control", "cache");
		response.setHeader("Pragma", "cache");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + outputFilename + "\"");
		OutputStream outputStream = response.getOutputStream();

		if (hssfworkbook != null) {
			hssfworkbook.write(outputStream);
			outputStream.flush();
		} else {
			log.error("Excel-Datei wurde nicht erzeugt.");
		}
		return null;
	}
}
