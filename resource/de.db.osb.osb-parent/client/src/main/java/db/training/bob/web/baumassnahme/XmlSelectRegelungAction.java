package db.training.bob.web.baumassnahme;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.xml.sax.SAXException;

import db.training.bob.model.Art;
import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Benchmark;
import db.training.bob.model.Regelung;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.XmlImporter;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.RegelungService;
import db.training.bob.service.RegionalbereichService;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.domain.BaumassnahmeAnyVoter;
import db.training.security.hibernate.TqmUser;

public class XmlSelectRegelungAction extends BaseAction {

	private static Logger log = Logger.getLogger(XmlSelectRegelungAction.class);

	final ReentrantLock lock = new ReentrantLock();

	private BaumassnahmeService baumassnahmeService;

	private RegelungService regelungService;

	public XmlSelectRegelungAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		regelungService = serviceFactory.createRegelungService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
		if (log.isDebugEnabled())
			log.debug("Entering XmlSelectRegelungAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		Integer baumassnahmeId;
		String hauptBbpId = null;
		Set<String> bbpIdsList;
		String regIdFuerTermine = null;
		Set<String> regIdsList;
		Set<BBPMassnahme> bbpMassnahmen;
		BBPMassnahme hauptBBP = null;
		Regelung regelungFuerTermine = null;
		Baumassnahme baumassnahme;
		Date bbpErstellDatum = null;

		XmlImportForm xmlImportForm = (XmlImportForm) form;

		String content = xmlImportForm.getFileContent();
		try {
			byte currentXMLBytes[] = content.getBytes("ISO-8859-1");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
			XmlImporter xmlImporter = new XmlImporter(byteArrayInputStream);
			bbpMassnahmen = xmlImporter.getBBPMassnahmen();
			bbpErstellDatum = xmlImporter.getErstelldatum();
		} catch (Exception e) {
			addError("error.baumassnahme.xml.importerror");
			return mapping.findForward("FAILURE");
		}

		baumassnahmeId = xmlImportForm.getBaumassnahmeId();
		if (baumassnahmeId != 0) { // zu bestehender Baumassnahme hinzufügen
			try {
				bbpIdsList = getParameterBBPMassnahmen(request);
				regIdsList = getParameterRegelungen(request);
				hauptBBP = getHauptBBP(bbpMassnahmen, hauptBbpId);
				regelungFuerTermine = saveSelectedBBPsAndRegelungen(bbpMassnahmen, hauptBbpId,
				    bbpIdsList, regIdsList, regIdFuerTermine);
				retainSelectedRegelungen(bbpMassnahmen, regIdsList);
			} catch (Exception e) {
				baumassnahme = baumassnahmeService.findById(baumassnahmeId);
				request.setAttribute("baumassnahme", baumassnahme);
				request.setAttribute("id", baumassnahme.getId());
				return mapping.findForward("FAILURE"); // TODO Errormessage
			}
			baumassnahme = updateBaumassnahme(baumassnahmeId, bbpMassnahmen, loginUser);
		} else { // neue Baumassnahme anlegen
			try {
				hauptBbpId = getParameterHauptBBPId(request);
				bbpIdsList = getParameterBBPMassnahmen(request);
				regIdsList = getParameterRegelungen(request);
				hauptBBP = getHauptBBP(bbpMassnahmen, hauptBbpId);
				regIdFuerTermine = getParameterRegelungIdFuerTermine(request, regIdsList);
				regelungFuerTermine = saveSelectedBBPsAndRegelungen(bbpMassnahmen, hauptBbpId,
				    bbpIdsList, regIdsList, regIdFuerTermine);
				retainSelectedRegelungen(bbpMassnahmen, regIdsList);
			} catch (Exception e) {
				log.debug(e);
				return mapping.findForward("FAILURE_LIST");// TODO Errormessage
			}
			try {
				baumassnahme = createBaumassnahme(bbpMassnahmen, hauptBBP, regelungFuerTermine,
				    secUser, loginUser, bbpErstellDatum);
			} catch (RuntimeException e) {
				log.debug(e);
				addError("error.import.xmlBaumassnahme");
				return mapping.findForward("FAILURE_LIST");// TODO Errormessage
			} catch (Exception e) {
				addError("error.import.xmlBaumassnahme");
				return mapping.findForward("FAILURE_LIST");// TODO Errormessage
			}
		}

		request.setAttribute("baumassnahme", baumassnahme);
		request.setAttribute("id", baumassnahme.getId());
		addMessage("success.baumassnahme.save");
		return mapping.findForward("SUCCESS");
	}

	private BBPMassnahme getHauptBBP(Set<BBPMassnahme> bbpMassnahmen, String hauptBbpId) {

		Iterator<BBPMassnahme> itBBP = bbpMassnahmen.iterator();

		while (itBBP.hasNext()) {
			BBPMassnahme bbp = itBBP.next();
			if (bbp.getMasId().equals(hauptBbpId))
				return bbp;
		}
		return null;
	}

	/**
	 * iteriert über alle BBP Maßnahmen und Regelungen und entfernt alle Regelungen, die bereits in
	 * der Datenbank vorhanden sind. BBP Maßnahmen, denen dadurch keine Regelungen mehr zugeordnet
	 * sind, werden entfernt. <code>bbpMassnahmen</code> enthält nach Ausführung ausschließlich neue
	 * Regelungen.
	 * 
	 * @param regIdsList
	 */
	private void retainSelectedRegelungen(Set<BBPMassnahme> bbpMassnahmen, Set<String> regIdsList) {
		Iterator<BBPMassnahme> itBBP = bbpMassnahmen.iterator();
		while (itBBP.hasNext()) {
			BBPMassnahme bbp = itBBP.next();
			Set<Regelung> regs = bbp.getRegelungen();
			Iterator<Regelung> itReg = regs.iterator();
			while (itReg.hasNext()) {
				Regelung reg = itReg.next();
				if (!regIdsList.contains(reg.getRegelungId())) {
					bbp.getRegelungen().remove(reg);
				}

			}
			if (bbp.getRegelungen().size() == 0)
				itBBP.remove();
		}
	}

	/**
	 * erstellt eine neue Baumassnahme und ordnet die zum Import ausgewählten BBP Massnahmen und
	 * Regelungen zu.
	 * 
	 * @param bbpMassnahmen
	 * @param hauptBBP
	 * @param bbpErstellDatum
	 * 
	 * @return
	 * @throws RuntimeException
	 * @throws org.acegisecurity.AccessDeniedException
	 *             wird geworfen, wenn der angemeldete Benutzer nicht über die Berechtigung verfügt,
	 *             Baumassnahmen zu importieren
	 */
	private Baumassnahme createBaumassnahme(Set<BBPMassnahme> bbpMassnahmen, BBPMassnahme hauptBBP,
	    Regelung regelungFuerTermine, TqmUser secUser, User loginUser, Date bbpErstellDatum)
	    throws Exception {
		Baumassnahme baumassnahme = new Baumassnahme();
		// Stammdaten
		baumassnahme.setBbpMassnahmen(bbpMassnahmen);
		baumassnahme.setStreckeBBP(hauptBBP.getStreckeBBP());
		baumassnahme.setStreckeVZG(hauptBBP.getStreckeVZG());
		baumassnahme.setStreckenAbschnitt(hauptBBP.getBstVonLang() + " - "
		    + hauptBBP.getBstBisLang());
		if (baumassnahme.getStreckenAbschnitt().length() > 255)
			baumassnahme
			    .setStreckenAbschnitt(baumassnahme.getStreckenAbschnitt().substring(0, 254));
		baumassnahme.setRegionalbereichBM(hauptBBP.getRegionalbereich());
		baumassnahme.setArtDerMassnahme(hauptBBP.getArbeiten());
		baumassnahme.setBeginnDatum(hauptBBP.getBeginn());
		baumassnahme.setEndDatum(hauptBBP.getEnde());
		baumassnahme.setBeginnFuerTerminberechnung(regelungFuerTermine.getBeginn());
		List<Bearbeiter> bearbeiter = new ArrayList<Bearbeiter>();
		bearbeiter.add(new Bearbeiter(loginUser, Boolean.TRUE));
		baumassnahme.setBearbeiter(bearbeiter);
		baumassnahme.setRegionalBereichFpl(loginUser.getRegionalbereich());
		baumassnahme.setBearbeitungsbereich(loginUser.getBearbeitungsbereich());

		// Fahrplanjahr einstellen
		int fahrplanjahr = 0;
		try {
			fahrplanjahr = EasyDateFormat.getFahrplanJahr(regelungFuerTermine.getBeginn());
		} catch (Exception e) {
			throw e;
		}
		baumassnahme.setFahrplanjahr(fahrplanjahr);

		// wenn alle Regelungen einer BBP-Maßnahme als "B" gekennzeichnet sind,
		// wird die Maßnahmenart "B" voreingestellt
		boolean isBMassnahme = true;
		for (Iterator<BBPMassnahme> bbpIterator = bbpMassnahmen.iterator(); bbpIterator.hasNext()
		    && isBMassnahme;) {
			BBPMassnahme bbp = bbpIterator.next();

			for (Iterator<Regelung> regIterator = bbp.getRegelungen().iterator(); regIterator
			    .hasNext() && isBMassnahme;) {
				Regelung r = regIterator.next();

				isBMassnahme = r.getBplArtText().equals("B");
			}
		}

		if (isBMassnahme) {
			baumassnahme.setArt(Art.B);
		}

		// KiGBau Nummern verknüpfen
		Set<String> kigBauNrs = getKiGBauNrs(bbpMassnahmen);
		baumassnahme.setKigBau(!kigBauNrs.isEmpty());
		baumassnahme.setKigBauNr(kigBauNrs);

		// TODO: Betriebsweise
		baumassnahme.setBetriebsweise(getBplRegelungLang(bbpMassnahmen));

		// Termine BBP anlegen
		TerminUebersichtBaubetriebsplanung bbp = new TerminUebersichtBaubetriebsplanung(
		    baumassnahme.getArt());
		bbp.setAnforderungBBZR(bbpErstellDatum);// Meilenstein AnforderungBBZR mit Erstelldatum der
		// BBP füllen
		bbp.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(), baumassnahme.getArt());
		baumassnahme.setBaubetriebsplanung(bbp);

		// Arbeitsleistung Regionalbereiche anlegen
		Map<Regionalbereich, Benchmark> arbeitsleistungen = new HashMap<Regionalbereich, Benchmark>();
		RegionalbereichService rbService = serviceFactory.createRegionalbereichService();
		List<Regionalbereich> rbs = rbService.findAll();
		for (Regionalbereich rb : rbs) {
			// Änderungsliste 07/2008
			// für Regionalbereich 'Zentrale' (ID=10) keine Arbeitsleistung erfassen
			if (rb.getId() != 10) {
				Benchmark al = new Benchmark();
				arbeitsleistungen.put(rb, al);
			}
		}
		if (log.isErrorEnabled()) {
			log.error("erstelle Baumaßnahme, Anzahl Datensätze in ArbeitsleistungDerRegionalbereiche: "
			    + arbeitsleistungen.size());
		}
		baumassnahme.setBenchmark(arbeitsleistungen);

		baumassnahme.setLastChange(null);

		// Rechteprüfung: Bausmassnahme erstellen
		BaumassnahmeAnyVoter voter = EasyServiceFactory.createBaumassnahmeAnyVoter();
		if (voter.vote(secUser, baumassnahme, "ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
			// keine Berechtigung
			addError("common.noAuth");
			throw new org.acegisecurity.AccessDeniedException("User has insufficient privileges: "
			    + secUser.toString());

		}

		try {
			lock.lock();
			baumassnahmeService.create(baumassnahme);
		} finally {
			lock.unlock();
		}

		return baumassnahme;
	}

	// TODO: KiGBau
	// TODO: Betriebsweise
	private Baumassnahme updateBaumassnahme(Integer baumassnahmeId,
	    Set<BBPMassnahme> bbpMassnahmen, User loginUser) {
		FetchPlan[] fetchplans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME,
		        FetchPlan.BBP_REGELUNGEN };
		Baumassnahme bm = baumassnahmeService.findById(baumassnahmeId, fetchplans);
		Set<BBPMassnahme> bbps = null;
		Set<BBPMassnahme> bbpsToAdd = new HashSet<BBPMassnahme>();
		Iterator<BBPMassnahme> newBBPs = bbpMassnahmen.iterator();
		while (newBBPs.hasNext()) {
			boolean neu = true;
			BBPMassnahme newBBP = newBBPs.next();
			bbps = bm.getBbpMassnahmen();
			Iterator<BBPMassnahme> it = bbps.iterator();
			while (it.hasNext()) {
				BBPMassnahme bbp = it.next();
				if (bbp.getMasId().equals(newBBP.getMasId())) {
					bbp.getRegelungen().addAll(newBBP.getRegelungen());
					neu = false;
				}
			}
			if (neu == true) {
				bbpsToAdd.add(newBBP);
			}
		}
		bbps.addAll(bbpsToAdd);

		bm.setBbpMassnahmen(bbps);
		baumassnahmeService.update(bm);
		return bm;
	}

	private Regelung saveSelectedBBPsAndRegelungen(Set<BBPMassnahme> bbpMassnahmen,
	    String hauptBbpId, Set<String> bbpIdsList, Set<String> regIdsList, String regIdFuerTermine)
	    throws Exception {

		Iterator<BBPMassnahme> itBBP = bbpMassnahmen.iterator();
		Set<Regelung> saveRegelungen = new HashSet<Regelung>();
		Regelung regelungFuerTermine = null;

		while (itBBP.hasNext()) {
			BBPMassnahme bbp = itBBP.next();
			if (!bbpIdsList.contains(bbp.getMasId())) { // prüfen, ob BBP-Massnahme in Formular
				// ausgewählt
				itBBP.remove();
			} else {
				Regelung reg = checkRegelungen(bbp, regIdsList, saveRegelungen, regIdFuerTermine);
				if (reg != null)
					regelungFuerTermine = reg;
				if (bbp.getRegelungen().size() == 0) {
					addError("error.bbpmassnahme.noregelungen");
					throw new Exception(
					    "keine neue Regelung für ausgewählte BBP-Massnahme importiert");
				}
			}
		}

		return regelungFuerTermine;
	}

	private Regelung checkRegelungen(BBPMassnahme bbp, Set<String> regIdsList,
	    Set<Regelung> saveRegelungen, String regIdFuerTermine) throws Exception {

		Iterator<Regelung> itRegelung = bbp.getRegelungenUnsorted().iterator();

		Regelung regelungFuerTermine = null;

		while (itRegelung.hasNext()) {
			Regelung reg = itRegelung.next();
			if (!regIdsList.contains(reg.getRegelungId())) {

				itRegelung.remove();
				// } else {
				// if (regelungService.findByRegelungId(reg.getRegelungId()) != null) {
				// // saveRegelungen.add(reg);
				// // } else {
				// addError("error.bbpmassnahme.regelungexist");
				// throw new Exception("zu importierende Regelung existiert schon.");
				// }
			}
			// die Regelung, die für Terminberechnung ausgewählt wurde markieren
			if (reg.getRegelungId().equals(regIdFuerTermine)) {
				regelungFuerTermine = reg;
				reg.setBeginnFuerTerminberechnung(true);
			} else {
				reg.setBeginnFuerTerminberechnung(false);
			}
		}
		return regelungFuerTermine;
	}

	/**
	 * iteriert durch alle BBP Maßnahmen und Regelungen und erstellt die Menge aller KiGBau Nummern,
	 * die gefunden werden und gibt diese zurück.
	 * 
	 * @return sortierte Menge aller verknüpften KiGBau Nummern (kann leer sein, nicht
	 *         <code>NULL</code>
	 */
	private Set<String> getKiGBauNrs(Set<BBPMassnahme> bbpMassnahmen) {
		SortedSet<String> result = new TreeSet<String>();

		for (BBPMassnahme bbp : bbpMassnahmen) {
			for (Regelung reg : bbp.getRegelungen()) {
				String s = reg.getLisbaNr();
				if (FrontendHelper.stringNotNullOrEmpty(s)) {
					result.add(s);
				}
			}
		}

		return result;
	}

	/**
	 * iteriert durch alle BBP Maßnahmen und Regelungen und gibt die Betriebsweise der für die
	 * Terminbestimmung relevanten Regelung zurück.
	 * 
	 * @param bbpMassnahmen
	 * 
	 * @return
	 */
	private String getBplRegelungLang(Set<BBPMassnahme> bbpMassnahmen) {
		for (BBPMassnahme bbp : bbpMassnahmen) {
			for (Regelung reg : bbp.getRegelungen()) {
				if (reg.getBeginnFuerTerminberechnung())
					return reg.getBetriebsweise();
			}
		}

		return "";
	}

	private Set<BBPMassnahme> importBBPMassnahmen(String content)
	    throws ParserConfigurationException, SAXException, IOException {
		byte currentXMLBytes[] = content.getBytes("ISO-8859-1");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
		XmlImporter xmlImporter = new XmlImporter(byteArrayInputStream);
		return xmlImporter.getBBPMassnahmen();
	}

	private Set<String> getParameterRegelungen(HttpServletRequest request) throws Exception {
		String[] regIds = null;
		if (request.getParameterValues("checkRegelung") == null) {
			addError("error.bbpmassnahme.noregelungen");
			throw new Exception("keine Regelungen ausgewählt");
		}
		regIds = request.getParameterValues("checkRegelung");
		HashSet<String> regIdsSet = new HashSet<String>();
		regIdsSet.addAll(Arrays.asList(regIds));
		return regIdsSet;
	}

	private String getParameterRegelungIdFuerTermine(HttpServletRequest request,
	    Set<String> regIdsList) throws Exception {
		String regIdFuerTermine = null;
		if (request.getParameter("radioRegelung") == null) {
			addError("error.bbpmassnahme.noterminregelung");
			throw new Exception("keine Regelung für Terminberechnung ausgewählt");
		}
		regIdFuerTermine = request.getParameter("radioRegelung");
		if (!regIdsList.contains(regIdFuerTermine)) {
			addError("error.bbpmassnahme.noterminregelung.uebernahme");
			throw new Exception("Regelung für Termine nicht zum Import ausgewählt");
		}
		return regIdFuerTermine;
	}

	private Set<String> getParameterBBPMassnahmen(HttpServletRequest request) throws Exception {
		String[] bbpIds = null;
		if (request.getParameterValues("checkBBPMassnahme") == null) {
			addError("error.bbpmassnahme.nobbpmassnahmen");
			throw new Exception("keine BBP-Massnahme ausgewählt");
		}
		bbpIds = request.getParameterValues("checkBBPMassnahme");
		HashSet<String> bbpIdsSet = new HashSet<String>();
		bbpIdsSet.addAll(Arrays.asList(bbpIds));
		return bbpIdsSet;
	}

	private String getParameterHauptBBPId(HttpServletRequest request) throws Exception {
		if (request.getParameter("radioBBPMassnahme") == null) {
			addError("error.bbpmassnahme.nohauptbbp");
			throw new Exception("keine HauptBBP-Massnahme ausgewählt");
		}
		return request.getParameter("radioBBPMassnahme");
	}
}