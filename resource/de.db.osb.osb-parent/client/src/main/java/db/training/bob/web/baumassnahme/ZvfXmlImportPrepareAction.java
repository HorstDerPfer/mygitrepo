package db.training.bob.web.baumassnahme;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import db.training.bob.model.Art;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.EVUGruppe;
import db.training.bob.model.Meilensteinbezeichnungen;
import db.training.bob.model.TerminUebersichtEVU;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.EVUGruppeService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;

public class ZvfXmlImportPrepareAction extends BaseAction {

	private static Logger log = Logger.getLogger(ZvfXmlImportPrepareAction.class);

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
		if (log.isDebugEnabled())
			log.debug("Entering ZvfXmlImportPrepareAction.");

		BaumassnahmeService baumassnahmeService = serviceFactory.createBaumassnahmeService();

		Uebergabeblatt zvfFile = new Uebergabeblatt();
		XmlImportForm xmlImportForm = (XmlImportForm) form;

		request.setAttribute("baumassnahmeId", xmlImportForm.getBaumassnahmeId());
		request.setAttribute("type", xmlImportForm.getType());
		request.setAttribute("zvfId", xmlImportForm.getZvfId());
		try {
			FormFile file = xmlImportForm.getXmlFile();
			InputStream input = file.getInputStream();
			InputStreamReader reader = new InputStreamReader(input, "utf-8");
			StringBuffer buffer = new StringBuffer();

			// Wenn die XML-Datei vorher unter Windows gespeichert wurde, werden dabei am Anfang
			// einige Bytes eingefügt, die beim Import Fehler verursachen. Diese Bytes werden hier
			// entfernt
			char[] buf = new char[1];
			int i = reader.read(buf);
			while (buf[0] != '<')
				i = reader.read(buf);
			buffer.append(buf, 0, i);

			// XML lesen
			buf = new char[500];
			i = reader.read(buf);
			while (i != -1) {
				buffer.append(buf, 0, i);
				buf = new char[500];
				i = reader.read(buf);
			}
			String fileContent = buffer.toString();
			fileContent.trim();
			try {
				zvfFile = getZvfObjectFromFile(xmlImportForm, mapping, fileContent, zvfFile);

				// Vorgangsnummern aus Baumassnahme und Zvf müssen übereinstimmen
				Integer vorgangsNrBm = baumassnahmeService.findById(
				    xmlImportForm.getBaumassnahmeId()).getVorgangsNr();
				Integer vorgangsNrZvf = null;
				if (zvfFile.getMassnahmen() != null) {
					vorgangsNrZvf = zvfFile.getMassnahmen().iterator().next().getVorgangsNr();
				}
				if (!vorgangsNrBm.equals(vorgangsNrZvf)) {
					addError("error.ueb.import.falschevorgangsnr");
					return mapping.findForward("FAILURE");
				}

			} catch (Exception e) {
				addError("error.ueb.import");
				return mapping.findForward("FAILURE");
			}

			xmlImportForm.setZvfFile(zvfFile);

		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug(e);
			}
		}

		// Prüfen auf neue Version
		Preload[] preloads = new Preload[] { new Preload(Baumassnahme.class, "zvf"),
		        new Preload(Uebergabeblatt.class, "massnahmen"),
		        new Preload(Massnahme.class, "version") };
		Baumassnahme bm = baumassnahmeService.findById(xmlImportForm.getBaumassnahmeId(), preloads);
		Uebergabeblatt aktuell = bm.getAktuelleZvf();
		boolean neueVersion = false;
		if (aktuell == null || aktuell.getVersion().compareTo(zvfFile.getVersion()) < 0) {
			neueVersion = true;
		}
		request.setAttribute("neueVersion", neueVersion);

		request.setAttribute("zvfFile", zvfFile);
		request.setAttribute("countZuege", zvfFile.getMassnahmen().iterator().next().getZug()
		    .size());

		if (xmlImportForm.getType().equals("UEB"))
			return mapping.findForward("SUCCESS_UEB");
		else if (xmlImportForm.getType().equals("ZVF")) {
			SortedSet<String> newEvus = getEVUsNotInBaumassnahme(xmlImportForm.getBaumassnahmeId(),
			    zvfFile);
			List<EVUGruppe> newEVUobjects = getEVUObjects(newEvus);
			request.setAttribute("newEVUList", newEVUobjects);

			Baumassnahme baumassnahme = baumassnahmeService.findById(xmlImportForm
			    .getBaumassnahmeId());
			List<MeilensteinBean> zvfMeilensteinList = new ArrayList<MeilensteinBean>();
			Integer selectedMeilenstein = null;
			if (baumassnahme != null) {
				if (!baumassnahme.getArt().equals(Art.B)) {
					MeilensteinBean m0 = new MeilensteinBean();
					m0.setBezeichnung(Meilensteinbezeichnungen.BIUE_ENTW.toString());
					m0.setId(Meilensteinbezeichnungen.BIUE_ENTW.ordinal());
					zvfMeilensteinList.add(m0);
				}
				MeilensteinBean m1 = new MeilensteinBean();
				m1.setBezeichnung(Meilensteinbezeichnungen.ZVF_ENTW.toString());
				m1.setId(Meilensteinbezeichnungen.ZVF_ENTW.ordinal());
				zvfMeilensteinList.add(m1);

				MeilensteinBean m2 = new MeilensteinBean();
				m2.setBezeichnung(Meilensteinbezeichnungen.ZVF.toString());
				m2.setId(Meilensteinbezeichnungen.ZVF.ordinal());
				zvfMeilensteinList.add(m2);
				request.setAttribute("zvfMeilensteinList", zvfMeilensteinList);

				if (zvfFile.getMassnahmen().iterator().next().getEndStueckZvf() == true) {
					selectedMeilenstein = m2.getId();
				} else {
					selectedMeilenstein = m1.getId();
				}

				request.setAttribute("selectedMeilenstein", selectedMeilenstein);

				return mapping.findForward("SUCCESS_ZVF");
			}
		}
		return mapping.findForward("FAILURE");
	}

	private List<EVUGruppe> getEVUObjects(Set<String> gruppennamen) {
		EVUGruppeService service = serviceFactory.createEVUGruppeService();
		List<EVUGruppe> evuList = new ArrayList<EVUGruppe>();
		EVUGruppe grp = null;
		if (gruppennamen.size() > 0) {
			for (String name : gruppennamen) {
				grp = service.findUniqueByName(name);
				if (grp != null)
					evuList.add(grp);
			}

		}
		return evuList;
	}

	private SortedSet<String> getEVUsNotInBaumassnahme(Integer baumassnahmeId,
	    Uebergabeblatt zvfFile) {
		EVUGruppeService grpService = EasyServiceFactory.getInstance().createEVUGruppeService();

		SortedSet<String> newEVUs = new TreeSet<String>();
		List<Zug> zuege = zvfFile.getMassnahmen().iterator().next().getZug();
		Set<EVUGruppe> evuGruppenBaumassnahme = getKundenGruppenFromBaumassnahme(baumassnahmeId);
		for (Zug z : zuege) {
			if (z.getBetreiber() != null) {
				EVUGruppe zugEvuGruppe = grpService.findByKundenNr(z.getBetreiber());
				if (zugEvuGruppe != null && !evuGruppenBaumassnahme.contains(zugEvuGruppe))
					newEVUs.add(zugEvuGruppe.getName().toUpperCase(Locale.GERMANY));
			}
		}
		List<String> newEVUList = new ArrayList<String>(newEVUs);
		Collections.sort(newEVUList);
		return newEVUs;
	}

	private Set<EVUGruppe> getKundenGruppenFromBaumassnahme(Integer baumassnahmeId) {
		Set<EVUGruppe> evus = new HashSet<EVUGruppe>();
		BaumassnahmeService baumassnahmeService = serviceFactory.createBaumassnahmeService();
		Preload[] preloads = new Preload[] { new Preload(Baumassnahme.class, "gevus"),
		        new Preload(Baumassnahme.class, "pevus") };
		Baumassnahme baumassnahme = baumassnahmeService.findById(baumassnahmeId, preloads);
		Set<TerminUebersichtEVU> tEVU = new HashSet<TerminUebersichtEVU>();
		tEVU.addAll(baumassnahme.getGevus());
		tEVU.addAll(baumassnahme.getPevus());

		evus.addAll(extractKundengruppen(tEVU));
		return evus;
	}

	private Set<EVUGruppe> extractKundengruppen(Set<TerminUebersichtEVU> tEVUs) {
		Set<EVUGruppe> gruppen = new HashSet<EVUGruppe>();
		for (TerminUebersichtEVU tEVU : tEVUs) {
			gruppen.add(tEVU.getEvuGruppe());
		}
		return gruppen;
	}

	private Uebergabeblatt getZvfObjectFromFile(XmlImportForm xmlImportForm, ActionMapping mapping,
	    String fileContent, Uebergabeblatt zvfFile) throws Exception {

		if (xmlImportForm.getBaumassnahmeId() == null) {
			if (log.isDebugEnabled())
				log.debug("id == null");
			throw new Exception();
		}
		Integer id = xmlImportForm.getBaumassnahmeId();

		FormFile xmlFile = xmlImportForm.getXmlFile();
		if (xmlFile == null || xmlFile.getFileSize() == 0) {
			addError("error.ueb.import");
			if (log.isDebugEnabled())
				log.debug("NOFILE id:" + id);
			throw new Exception();
		}

		try {
			StringReader input = new StringReader(fileContent);
			JAXBContext context = JAXBContext.newInstance("db.training.bob.model.zvf");
			Unmarshaller u = context.createUnmarshaller();
			zvfFile = (Uebergabeblatt) u.unmarshal(input);
		} catch (JAXBException e) {
			if (log.isDebugEnabled())
				log.debug(e);
			addError("error.import.invalid.file");
			throw e;
		}

		return zvfFile;
	}
}
