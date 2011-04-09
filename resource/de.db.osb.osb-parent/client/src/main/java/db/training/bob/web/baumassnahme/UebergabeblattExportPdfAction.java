package db.training.bob.web.baumassnahme;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.easy.util.FileSystemHelper;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;

public class UebergabeblattExportPdfAction extends UebergabeblattExportAction {

	private static Logger log = Logger.getLogger(UebergabeblattExportPdfAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled())
			log.debug("Entering UebergabeblattExportPdfAction.");

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("tab"))) {
			request.setAttribute("tab", request.getParameter("tab"));
		}

		Integer id = null;
		if (request.getParameter("zvfId") != null)
			id = FrontendHelper.castStringToInteger(request.getParameter("zvfId"));

		Uebergabeblatt ueb = getUebergabeblatt(id);

		String filename = ueb.getHeader().getFilename();
		filename = filename.substring(0, filename.length() - 3) + ".xml";
		if (filename.equals("")) {
			filename = ueb.getMassnahmen().iterator().next().getVorgangsNr().toString() + ".xml";
		}

		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("db.training.bob.model.zvf");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		Marshaller marshaller = context.createMarshaller();

		ServletOutputStream outStream = response.getOutputStream();

		synchronized (outStream) {
			// File xmlFile = new File("xmlTemp");
			// marshaller.marshal(ueb, xmlFile);
			StringWriter sw = new StringWriter();
			marshaller.marshal(ueb, sw);
			if (log.isDebugEnabled())
				log.debug(sw.toString());

			File xsltfile = new File(FileSystemHelper.getFilePath(getServlet(),
			    "static/xsl/uebergabeblatt.xsl"));
			FopFactory fopFactory = FopFactory.newInstance();
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

			try {
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outStream);

				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

				// Set the value of a <param> in the stylesheet
				transformer.setParameter("versionParam", "2.0");

				// Setup input for XSLT transformation
				// Source src = new StreamSource(xmlFile);
				Source src = new StreamSource(new StringReader(sw.toString()));

				// Resulting SAX events (the generated FO) must be piped through to FOP
				Result res = new SAXResult(fop.getDefaultHandler());

				// Start XSLT transformation and FOP processing
				transformer.transform(src, res);
				outStream.close();
				outStream.flush();
			} catch (Exception e) {
				addError("errors.exception");
				// return mapping.findForward("FAILURE");
			} finally {
			}
		}

		return (null);
	}
}
