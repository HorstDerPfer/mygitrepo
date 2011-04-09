package db.training.bob.web.baumassnahme;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;

public class UebergabeblattExportXmlAction extends UebergabeblattExportAction {

	private static Logger log = Logger.getLogger(UebergabeblattExportXmlAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattExportXmlAction.");

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("tab"))) {
			request.setAttribute("tab", request.getParameter("tab"));
		}

		Integer id = null;
		if (request.getParameter("zvfId") != null)
			id = FrontendHelper.castStringToInteger(request.getParameter("zvfId"));

		Uebergabeblatt ueb = getUebergabeblatt(id);

		String filename = ueb.getHeader().getFilename();
		if (filename == null || filename.equals(""))
			filename = "ÜB" + ueb.getMassnahmen().iterator().next().getVorgangsNr().toString()
			    + ".xml";
		else {
			filename = filename.substring(0, filename.length() - 3) + "xml";
		}

		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("db.training.bob.model.zvf");
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Marshaller marshaller = context.createMarshaller();

		ServletOutputStream outStream = response.getOutputStream();

		File f = new File(Integer.toHexString(ueb.hashCode()));
		marshaller.marshal(ueb, f);

		response.setContentLength((int) f.length());
		response.setContentType("xml");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
		FileInputStream in = new FileInputStream(f);

		byte[] buffer = new byte[32768];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			outStream.write(buffer, 0, n);
		}
		in.close();
		f.delete();

		outStream.flush();
		outStream.close();

		return (null);
	}

}
