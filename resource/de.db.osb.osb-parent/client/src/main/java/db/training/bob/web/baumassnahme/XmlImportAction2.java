package db.training.bob.web.baumassnahme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Regelung;
import db.training.bob.model.XmlImporter;
import db.training.bob.service.RegelungService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class XmlImportAction2 extends BaseAction {

	private static Logger log = Logger.getLogger(XmlImportAction2.class);

	private RegelungService regelungService;

	public XmlImportAction2() {
		serviceFactory = EasyServiceFactory.getInstance();
		regelungService = serviceFactory.createRegelungService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
		if (log.isDebugEnabled())
			log.debug("Entering XmlImportAction2.");

		XmlImportForm xmlImportForm = (XmlImportForm) form;

		if (xmlImportForm.getBaumassnahmeId() == null) {
			if (log.isDebugEnabled())
				log.debug("baumassnahmeId == null");
			return mapping.findForward("FAILURE_LIST");
		}
		Integer baumassnahmeId = xmlImportForm.getBaumassnahmeId();

		FormFile xmlFile = xmlImportForm.getXmlFile();
		if (xmlFile == null || xmlFile.getFileSize() == 0) {
			if (log.isDebugEnabled())
				log.debug("NOFILE baumassnahmeId:" + baumassnahmeId);
			return mapping.findForward("FAILURE_IMPORT");
		}

		XmlImporter xmlImporter;
		String content = null;
		try {
			content = new String(xmlFile.getFileData(), "ISO-8859-1");
			xmlImportForm.setFileContent(content);
			xmlImporter = new XmlImporter(xmlFile.getInputStream());
		} catch (FileNotFoundException e) {
			addError("error.baumassnahme.xml.importerror");
			if (log.isDebugEnabled())
				log.debug("error.baumassnahme.xml.importerror", e);
			return mapping.findForward("FAILURE_IMPORT");
		} catch (IOException e) {
			addError("error.baumassnahme.xml.importerror");
			if (log.isDebugEnabled())
				log.debug("error.baumassnahme.xml.importerror", e);
			return mapping.findForward("FAILURE_IMPORT");
		} catch (ParserConfigurationException e) {
			addError("error.baumassnahme.xml.importerror");
			if (log.isDebugEnabled())
				log.debug("error.baumassnahme.xml.importerror", e);
			return mapping.findForward("FAILURE_IMPORT");
		} catch (SAXException e) {
			addError("error.baumassnahme.xml.importerror");
			if (log.isDebugEnabled())
				log.debug("error.baumassnahme.xml.importerror", e);
			return mapping.findForward("FAILURE_IMPORT");
		}

		Set<BBPMassnahme> bbpMassnahmen = null;
		try {
			bbpMassnahmen = xmlImporter.getBBPMassnahmen();
			bbpMassnahmen = retainNewRegelungen(bbpMassnahmen);
		} catch (SAXNotRecognizedException e) {
			addError("error.baumassnahme.xml.invaliddata.error");
			return mapping.findForward("FAILURE_IMPORT");
		}

		if (bbpMassnahmen.size() == 0) {
			addError("error.baumassnahme.nothingtoimport");
			return mapping.findForward("FAILURE_IMPORT");
		}

		// xmlImportForm.setBbpMassnahmen(bbpMassnahmen);
		request.setAttribute("bbpmassnahmen", bbpMassnahmen);
		if (log.isDebugEnabled())
			log.debug("baumassnahmeId:" + baumassnahmeId);

		return mapping.findForward("SUCCESS");
	}

	private Set<BBPMassnahme> retainNewRegelungen(Set<BBPMassnahme> bbpMassnahmen) {
		Iterator<BBPMassnahme> itBBP = bbpMassnahmen.iterator();
		BBPMassnahme bbp = null;
		while (itBBP.hasNext()) {
			bbp = itBBP.next();
			Set<Regelung> regs = bbp.getRegelungen();
			int regCounter = regs.size();
			Iterator<Regelung> itReg = regs.iterator();
			Regelung reg = null;
			while (itReg.hasNext()) {
				reg = itReg.next();
				// Vorgangsnummern suchen und eintragen
				List<Integer> vorgangsnrList = new ArrayList<Integer>();
				vorgangsnrList = regelungService.findVorgangsNr(reg.getRegelungId());
				if (vorgangsnrList.size() != 0) {
					regCounter--;
					reg.setVorgangsnr(vorgangsnrList);
				}
			}
			if (regCounter == 0) {
				bbp.setAllRegs(Boolean.TRUE);
			}
		}
		return bbpMassnahmen;
	}
}