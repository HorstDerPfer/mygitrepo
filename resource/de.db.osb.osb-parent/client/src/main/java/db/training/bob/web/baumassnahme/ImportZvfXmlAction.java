package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.EVU;
import db.training.bob.model.EVUGruppe;
import db.training.bob.model.zvf.FileType;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.ZvfImporter;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;

public class ImportZvfXmlAction extends BaseAction {

	private Logger log;

	private BaumassnahmeService baumassnahmeService;

	public ImportZvfXmlAction() {
		log = Logger.getLogger(this.getClass());
		try {
			serviceFactory = EasyServiceFactory.getInstance();
			baumassnahmeService = serviceFactory.createBaumassnahmeService();
		} catch (Exception e) {// do nothing
		}
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled())
			log.debug("Entering ImportZvfXmlAction.");

		ZvfImporter importer = new ZvfImporter();

		XmlImportForm xmlImportForm = (XmlImportForm) form;

		// Parameter aus Request lesen
		Integer baumassnahmeId = xmlImportForm.getBaumassnahmeId();

		try {
			Uebergabeblatt zvfFile = getZvfObjectFromFile(mapping,
					xmlImportForm);
			importer.setImportFile(zvfFile);
		} catch (Exception e) {
			addError("error.ueb.import");
			return mapping.findForward("FAILURE");
		}

		boolean importKopfdaten;
		List<Integer> importZugIdList = null;
		List<EVUGruppe> importGevusNrList = null;
		List<EVUGruppe> importPevusNrList = null;
		Integer zvfMeilenstein = null;
		try {
			importKopfdaten = getParameterKopfdaten(request);
			importZugIdList = getParameterZuege(request);
			importGevusNrList = getParameterGevu(request);
			importPevusNrList = getParameterPevu(request);
			zvfMeilenstein = getParameterZvfMeilenstein(request);
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("FAILURE");
		}

		// Baumassnahme laden
		FileType type = importer.getImportFileType();
		Baumassnahme baumassnahme = ladeBaumassnahme(type, baumassnahmeId);
		importer.setBaumassnahme(baumassnahme);

		// Import
		if (xmlImportForm.getZvfId() == -1) {
			// Erstimport
			if (log.isDebugEnabled())
				log.debug("Erstimport");
			importer.setNewVersion(importer.getImportFile());
			importer.copyDataFromBaumassnahmeToZvfFile();
			Set<EVU> evuNrsOfSelectedZuege = importer
					.copyZuegeFromImportFileToZvfFile(importZugIdList);
			Set<EVUGruppe> evuOfSelectedZuege = importer
					.getEVUGruppeObjects(evuNrsOfSelectedZuege);
			importer.addEVUtoBaumassnahme(importGevusNrList, importPevusNrList,
					evuOfSelectedZuege);

			if (type == FileType.ZVF) {
				importer.setMeilensteinZvF(evuOfSelectedZuege, zvfMeilenstein);
				importer.setBenchmarks();
				importer.getBaumassnahme().getZvf()
						.add(importer.getNewVersion());
			} else {
				importer.setMeilensteinMasterUeb(evuOfSelectedZuege);
				// importer.getNewVersion().initializeBeteiligteRb();
				importer.getNewVersion().refreshZugStatusRbEntry();
				importer.getBaumassnahme().setUebergabeblatt(
						importer.getNewVersion());
			}

			// Speichern
			try {
				baumassnahmeService.update(importer.getBaumassnahme());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// Import weiterer Züge zu bestehender ZvF/Übergabeblatt oder neue
			// Version ZVF
			if (type == FileType.ZVF & importKopfdaten == true) { // neue
																	// ZvF-Version
				if (!importer.isNewerVersion()) {
					addError("error.import.old.version");
					return mapping.findForward("FAILURE");
				}
				importer.setNewVersion(importer.getImportFile());
				importer.copyDataFromBaumassnahmeToZvfFile();
				Set<EVU> evuNrsOfSelectedZuege = importer
						.copyZuegeFromImportFileToZvfFile(importZugIdList);
				Set<EVUGruppe> evuOfSelectedZuege = importer
						.getEVUGruppeObjects(evuNrsOfSelectedZuege);
				importer.addEVUtoBaumassnahme(importGevusNrList,
						importPevusNrList, evuOfSelectedZuege);
				importer.setMeilensteinZvF(evuOfSelectedZuege, zvfMeilenstein);
				importer.setBenchmarks();
				importer.getBaumassnahme().getZvf()
						.add(importer.getNewVersion());
				try {
					baumassnahmeService.update(importer.getBaumassnahme());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {// nur Züge importieren
				Uebergabeblatt zvf = null;
				if (type == FileType.ZVF)
					zvf = baumassnahme.getAktuelleZvf();
				else
					zvf = baumassnahme.getUebergabeblatt();
				importer.setOldVersion(zvf);
				importer.setNewVersion(zvf);

				Set<EVU> evuNrsOfSelectedZuege = importer
						.copyZuegeFromImportFileToZvfFile(importZugIdList);
				Set<EVUGruppe> evuOfSelectedZuege = importer
						.getEVUGruppeObjects(evuNrsOfSelectedZuege);
				importer.addEVUtoBaumassnahme(importGevusNrList,
						importPevusNrList, evuOfSelectedZuege);
				if (type == FileType.ZVF)
					importer.setMeilensteinZvF(evuOfSelectedZuege,
							zvfMeilenstein);
				else {
					importer.setMeilensteinMasterUeb(evuOfSelectedZuege);
					// importer.getNewVersion().initializeBeteiligteRb();
					if (importKopfdaten == true) {
						importer.copyHeaderAndMassnahmeFromImportFileToZvfFile();
					}
				}

				if (type == FileType.ZVF) {
					Uebergabeblatt aktuelleZvf = importer.getBaumassnahme()
							.getAktuelleZvf();
					importer.getBaumassnahme().getZvf().remove(aktuelleZvf);
					importer.getBaumassnahme().getZvf()
							.add(importer.getNewVersion());
				} else
					importer.getBaumassnahme().setUebergabeblatt(
							importer.getNewVersion());

				try {
					baumassnahmeService.update(importer.getBaumassnahme());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		request.setAttribute("id", baumassnahmeId);
		request.setAttribute("bearbeitet", Boolean.valueOf(false));

		if (hasErrors()) {
			return mapping.findForward("FAILURE");
		}

		if (type == FileType.ZVF) {
			return mapping.findForward("SUCCESS_ZVF");
		}
		return mapping.findForward("SUCCESS_UEB");
	}

	private Integer getParameterZvfMeilenstein(HttpServletRequest request) {
		return FrontendHelper.castStringToInteger(request
				.getParameter("zvfmeilenstein"));
	}

	private boolean getParameterKopfdaten(HttpServletRequest request)
			throws Exception {
		String importKopfdatenStr = request.getParameter("radioHeader");
		if (importKopfdatenStr.equals("true"))
			return true;
		return false;
	}

	private List<Integer> getParameterZuege(HttpServletRequest request)
			throws Exception {
		List<Integer> result = new ArrayList<Integer>();
		String[] zuege = null;
		if (request.getParameterValues("checkZug") != null) {
			zuege = request.getParameterValues("checkZug");
			int zIndex;
			for (String z : zuege) {
				zIndex = Integer.valueOf(z);
				result.add(zIndex);
			}
		}
		return result;
	}

	private List<EVUGruppe> getParameterGevu(HttpServletRequest request)
			throws Exception {
		return getParameterEvu(request, "checkGEVU");
	}

	private List<EVUGruppe> getParameterPevu(HttpServletRequest request)
			throws Exception {
		return getParameterEvu(request, "checkPEVU");
	}

	private List<EVUGruppe> getParameterEvu(HttpServletRequest request,
			String parameterName) throws Exception {
		List<EVUGruppe> result = new ArrayList<EVUGruppe>();
		EVUGruppeService grpService = EasyServiceFactory.getInstance()
				.createEVUGruppeService();

		String[] evu = null;
		if (request.getParameterValues(parameterName) != null) {
			evu = request.getParameterValues(parameterName);
			for (String evuNr : evu) {
				result.add(grpService.findById(FrontendHelper
						.castStringToInteger(evuNr)));
			}
		}
		return result;
	}

	private Uebergabeblatt getZvfObjectFromFile(ActionMapping mapping,
			XmlImportForm xmlImportForm) throws Exception {

		if (xmlImportForm.getZvfFile() == null) {
			addError("error.ueb.import");
			if (log.isDebugEnabled())
				log.debug("NO_ZVFFILE");
			throw new Exception();
		}

		return xmlImportForm.getZvfFile();
	}

	private Baumassnahme ladeBaumassnahme(FileType type, Integer baumassnahmeId) {
		FetchPlan[] fetchPlans = null;
		// Preload[] preloads = null;
		if (type == FileType.ZVF) {
			// preloads = new Preload[] { new Preload(Baumassnahme.class,
			// "bbpMassnahmen"),
			// new Preload(BBPMassnahme.class, "regelungen"),
			// new Preload(Baumassnahme.class, "baubetriebsplanung"),
			// new Preload(Baumassnahme.class, "gevus"),
			// new Preload(Baumassnahme.class, "pevus"),
			// new Preload(Baumassnahme.class,
			// "arbeitsleistungDerRegionalbereiche"),
			// new Preload(Baumassnahme.class, "bbzr"),
			// new Preload(Baumassnahme.class, "regionalBereichFpl"),
			// new Preload(Uebergabeblatt.class, "header"),
			// new Preload(Uebergabeblatt.class, "massnahmen"),
			// new Preload(Massnahme.class, "zug"),
			// new Preload(Massnahme.class, "allgregelungen"),
			// new Preload(Massnahme.class, "bbp"), new Preload(Massnahme.class,
			// "strecke"),
			// new Preload(Strecke.class, "streckeVZG"), new Preload(Zug.class,
			// "abweichung"),
			// new Preload(Abweichung.class, "halt"),
			// new Preload(Abweichung.class, "umleitweg"),
			// new Preload(Abweichung.class, "ausfallvon"),
			// new Preload(Abweichung.class, "ausfallbis"),
			// new Preload(Abweichung.class, "vorplanab"),
			// new Preload(Header.class, "empfaenger"), new
			// Preload(Header.class, "sender"),
			// new Preload(Halt.class, "ausfall"), new Preload(Halt.class,
			// "ersatz"),
			// new Preload(Zug.class, "regelweg"),
			// new Preload(Regelweg.class, "abgangsbahnhof"),
			// new Preload(Regelweg.class, "zielbahnhof"),
			// new Preload(Massnahme.class, "version") };
			fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME,
					FetchPlan.BBP_REGELUNGEN, FetchPlan.BOB_TERMINE_BBP,
					FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU,
					FetchPlan.BOB_ARBEITSLEISTUNG_REGIONALBEREICHE,
					FetchPlan.BOB_BBZR, FetchPlan.BOB_REGIONALBEREICH_FPL,
					FetchPlan.BBZR_HEADER, FetchPlan.BBZR_MN_ZUEGE,
					FetchPlan.ZVF_MN_ALLG_REGELUNGEN,
					FetchPlan.ZVF_MN_BBPSTRECKE,
					FetchPlan.ZVF_MN_STRECKE_STRECKEVZG,
					FetchPlan.ZVF_MN_STRECKEN,
					FetchPlan.ZVF_ZUG_ABWEICHUNG_HALT,
					FetchPlan.ZVF_ZUG_UMLEITWEG, FetchPlan.UEB_HEADER,
					FetchPlan.UEB_HEADER_EMPFAENGER,
					FetchPlan.UEB_HEADER_SENDER,
					FetchPlan.UEB_ZUG_ABWEICHUNG_BAHNHOF,
					FetchPlan.UEB_ZUG_ABWEICHUNG_HALT_BAHNHOF,
					FetchPlan.UEB_ZUG_REGELWEG, FetchPlan.BBZR_BAUMASSNAHMEN,
					FetchPlan.ZVF_MN_VERSION };
		} else {
			// preloads = new Preload[] { new Preload(Baumassnahme.class,
			// "bbpMassnahmen"),
			// new Preload(BBPMassnahme.class, "regelungen"),
			// new Preload(Baumassnahme.class, "baubetriebsplanung"),
			// new Preload(Baumassnahme.class, "gevus"),
			// new Preload(Baumassnahme.class, "pevus"),
			// new Preload(Baumassnahme.class, "uebergabeblatt"),
			// new Preload(Baumassnahme.class, "bbzr") };
			fetchPlans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME,
					FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU,
					FetchPlan.BOB_UEBERGABEBLATT, FetchPlan.BBP_REGELUNGEN,
					FetchPlan.BOB_TERMINE_GEVU, FetchPlan.BOB_TERMINE_PEVU };
		}
		// Baumassnahme baumassnahme =
		// baumassnahmeService.findById(baumassnahmeId, preloads);
		Baumassnahme baumassnahme = baumassnahmeService.findById(
				baumassnahmeId, fetchPlans);
		return baumassnahme;
	}

	public void setBaumassnahmeService(BaumassnahmeService baumassnahmeService) {
		this.baumassnahmeService = baumassnahmeService;
	}
}
