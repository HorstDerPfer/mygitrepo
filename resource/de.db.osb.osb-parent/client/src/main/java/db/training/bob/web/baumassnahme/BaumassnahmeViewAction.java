package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.Hibernate;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.zvf.Abweichung;
import db.training.bob.model.zvf.Fplonr;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.RegelungAbw;
import db.training.bob.model.zvf.Regelweg;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.service.BaumassnahmeService;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.DisplaytagHelper;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.hibernate.preload.Preload;
import db.training.logwrapper.Logger;
import db.training.security.domain.VoterFactory;
import db.training.security.domain.VoterType;
import db.training.security.hibernate.TqmUser;

public class BaumassnahmeViewAction extends BaseAction {
	private static Logger log;

	private BaumassnahmeService baumassnahmeService;

	public BaumassnahmeViewAction() {
		log = Logger.getLogger(this.getClass());
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeViewAction.execute.");

		this.errors.set(new ActionMessages());
		this.messages.set(new ActionMessages());

		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		Baumassnahme baumassnahme = null;
		Integer id = null;

		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("id")))
			id = FrontendHelper.castStringToInteger(request.getParameter("id"));

		if (id == null || id.equals(0)) {
			logNotFoundError(id);
			return mapping.findForward("FAILURE");
		}

		String tab = "";
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("tab")))
			tab = request.getParameter("tab");
		Boolean ajax = FrontendHelper.castStringToBoolean(request.getParameter("ajax"));

		if (tab != "")
			baumassnahmeForm.setTab(tab);
		else {
			Preload[] preloads = new Preload[] { new Preload(Baumassnahme.class,
			    "regionalBereichFpl") };
			baumassnahme = baumassnahmeService.findById(id, preloads);
			TqmUser secUser = getSecUser();

			if (VoterFactory.getDecisionVoter(Baumassnahme.class, VoterType.ANY).vote(secUser,
			    baumassnahme, "ROLE_BAUMASSNAHME_GRUNDDATEN_LESEN_ALLE") == AccessDecisionVoter.ACCESS_DENIED
			    & VoterFactory.getDecisionVoter(Baumassnahme.class, VoterType.ANY).vote(secUser,
			        baumassnahme, "ROLE_BAUMASSNAHME_GRUNDDATEN_LESEN_REGIONALBEREICH") == AccessDecisionVoter.ACCESS_DENIED) {
				// weder generelle noch Rb-spezifische Leserechte
				baumassnahmeForm.setTab("Benchmark");
				tab = "Benchmark";
			} else {
				baumassnahmeForm.setTab("JBB");
				tab = "JBB";
			}
		}

		String forward = "SUCCESS";

		Set<Preload> preloadSet = new HashSet<Preload>();
		preloadSet.add(new Preload(Baumassnahme.class, "bbpMassnahmen"));
		preloadSet.add(new Preload(Baumassnahme.class, "bearbeitungsbereich"));
		preloadSet.add(new Preload(Baumassnahme.class, "regionalBereichFpl"));
		preloadSet.add(new Preload(BBPMassnahme.class, "regelungen"));
		
		if (tab.equals("") || tab.equals("JBB")) {
			preloadSet.add(new Preload(Baumassnahme.class, "kigBauNr"));
			preloadSet.add(new Preload(Baumassnahme.class, "qsNr"));
			preloadSet.add(new Preload(Baumassnahme.class, "korridorZeitfenster"));
			preloadSet.add(new Preload(Baumassnahme.class, "baubetriebsplanung"));
			preloadSet.add(new Preload(Baumassnahme.class, "pevus"));
			preloadSet.add(new Preload(Baumassnahme.class, "gevus"));
			request.setAttribute("tab", "JBB");
			if (tab.equals(""))
				forward = "SUCCESS";
			else
				forward = "SUCCESS_JBB";

		} else if (tab.equals("StatusAktivitaeten")) {
			request.setAttribute("tab", tab);
			forward = "SUCCESS_STATUS_AKT";

		} else if (tab.equals("VerzichtQTrasse")) {
			request.setAttribute("tab", tab);
			forward = "SUCCESS_VERZICHT_QT";

		} else if (tab.equals("AbstimmungNachbarbahn")) {
			request.setAttribute("tab", tab);
			forward = "SUCCESS_ABSTIMMUNG_NB";

		} else if (tab.equals("Kommentar")) {
			request.setAttribute("tab", tab);
			forward = "SUCCESS_KOMMENTAR";

		} else if (tab.equals("Benchmark")) {
			request.setAttribute("tab", tab);
			preloadSet.add(new Preload(Baumassnahme.class, "benchmark"));
			forward = "SUCCESS_BENCHMARK";

		} else if (tab.equals("Aenderungsdokumentation")) {
			request.setAttribute("tab", tab);
			preloadSet.add(new Preload(Baumassnahme.class, "aenderungen"));
			forward = "SUCCESS_AENDERUNG";

		} else if (tab.equals("EskalationAusfall")) {
			request.setAttribute("tab", tab);
			preloadSet.add(new Preload(Baumassnahme.class, "ausfallGrund"));
			forward = "SUCCESS_ESKAL_AUSFALL";

		} else if (tab.equals("Uebergabeblatt")) {
			request.setAttribute("tab", tab);
			preloadSet.add(new Preload(Baumassnahme.class, "uebergabeblatt"));
			preloadSet.add(new Preload(Uebergabeblatt.class, "header"));
			preloadSet.add(new Preload(Header.class, "sender"));
			preloadSet.add(new Preload(Uebergabeblatt.class, "massnahmen"));
			preloadSet.add(new Preload(Massnahme.class, "version"));
			preloadSet.add(new Preload(Massnahme.class, "strecke"));
			preloadSet.add(new Preload(Massnahme.class, "bbp"));
			preloadSet.add(new Preload(Strecke.class, "streckeVZG"));
			preloadSet.add(new Preload(Massnahme.class, "fplonr"));
			preloadSet.add(new Preload(Fplonr.class, "niederlassungen"));
			forward = "SUCCESS_UEB";
			if (baumassnahmeForm.getShowZuegeUeb() == true) {
				preloadSet.add(new Preload(Massnahme.class, "zug"));
				preloadSet.add(new Preload(Zug.class, "regelweg"));
				preloadSet.add(new Preload(Regelweg.class, "abgangsbahnhof"));
				preloadSet.add(new Preload(Regelweg.class, "zielbahnhof"));
				preloadSet.add(new Preload(Zug.class, "abweichung"));
				preloadSet.add(new Preload(Abweichung.class, "halt"));
				preloadSet.add(new Preload(Halt.class, "ausfall"));
				preloadSet.add(new Preload(Halt.class, "ersatz"));
				preloadSet.add(new Preload(Abweichung.class, "regelungen"));
				preloadSet.add(new Preload(RegelungAbw.class, "giltIn"));
				preloadSet.add(new Preload(Abweichung.class, "ausfallvon"));
				preloadSet.add(new Preload(Abweichung.class, "ausfallbis"));
				preloadSet.add(new Preload(Abweichung.class, "vorplanab"));
				preloadSet.add(new Preload(Zug.class, "knotenzeiten"));
			}

		} else if (tab.equals("Zvf")) {
			request.setAttribute("tab", tab);
			preloadSet.add(new Preload(Baumassnahme.class, "zvf"));
			preloadSet.add(new Preload(Uebergabeblatt.class, "header"));
			preloadSet.add(new Preload(Header.class, "sender"));
			preloadSet.add(new Preload(Uebergabeblatt.class, "massnahmen"));
			preloadSet.add(new Preload(Massnahme.class, "version"));
			preloadSet.add(new Preload(Massnahme.class, "strecke"));
			preloadSet.add(new Preload(Massnahme.class, "bbp"));
			preloadSet.add(new Preload(Strecke.class, "streckeVZG"));
			forward = "SUCCESS_ZVF";
			if (baumassnahmeForm.getShowZuegeZvf() == true) {
				preloadSet.add(new Preload(Massnahme.class, "zug"));
				preloadSet.add(new Preload(Zug.class, "regelweg"));
				preloadSet.add(new Preload(Regelweg.class, "abgangsbahnhof"));
				preloadSet.add(new Preload(Regelweg.class, "zielbahnhof"));
				preloadSet.add(new Preload(Zug.class, "abweichung"));
				preloadSet.add(new Preload(Abweichung.class, "halt"));
				preloadSet.add(new Preload(Halt.class, "ausfall"));
				preloadSet.add(new Preload(Halt.class, "ersatz"));
				preloadSet.add(new Preload(Abweichung.class, "regelungen"));
				preloadSet.add(new Preload(RegelungAbw.class, "giltIn"));
				preloadSet.add(new Preload(Abweichung.class, "ausfallvon"));
				preloadSet.add(new Preload(Abweichung.class, "ausfallbis"));
				preloadSet.add(new Preload(Abweichung.class, "vorplanab"));
			}

		} else if (tab.equals("Fahrplan")) {
			request.setAttribute("tab", tab);
			preloadSet.add(new Preload(Baumassnahme.class, "zugcharakteristik"));
			forward = "SUCCESS_FAHRPLAN";

		} else if (tab.equals("Bearbeiter")) {
			request.setAttribute("tab", tab);
			preloadSet.add(new Preload(Baumassnahme.class, "bearbeiter"));
			forward = "SUCCESS_BEARBEITER";
		}

		Preload[] preloads = new Preload[] {};
		preloads = preloadSet.toArray(preloads);
		baumassnahme = baumassnahmeService.findById(id, preloads);

		if (baumassnahme == null) {
			logNotFoundError(id);
			return mapping.findForward("FAILURE");
		}

		if (log.isDebugEnabled())
			log.debug("Processing Baumassnahme: " + baumassnahme.getId());

		request.setAttribute("baumassnahme", baumassnahme);

		// ZvF für Anzeige
		Integer zvfId = null;
		if (tab.equals("Zvf")) {

			if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("showzvf")))
				zvfId = FrontendHelper.castStringToInteger(request.getParameter("showzvf"));

			List<Uebergabeblatt> zvfs = baumassnahme.getZvf();

			if (zvfs != null) {
				if (zvfs.size() > 0) {
					if (zvfId != null) {
						for (Uebergabeblatt zvf : zvfs) {
							if (zvf.getId().equals(zvfId)) {
								request.setAttribute("viewZvf", zvf);
								baumassnahmeForm.setZvf(zvf.getId());
							}
						}
					} else {
						request.setAttribute("viewZvf", baumassnahme.getAktuelleZvf());
						baumassnahmeForm.setZvf(baumassnahme.getAktuelleZvf().getId());
					}
				}
			}
		}

		// Zuglisten für ZvF erzeugen
		if (baumassnahmeForm.getShowZuegeZvf() == true) {

			Map<String, String> parameterMap = DisplaytagHelper.getParameterMap(request
			    .getParameterMap());
			if (parameterMap.size() > 0)
				forward = "SUCCESS";

			List<Uebergabeblatt> zvfs = baumassnahme.getZvf();

			if (zvfs != null) {
				for (Uebergabeblatt zvf : zvfs) {
					if (zvf.getId().equals(zvfId)) {
						request.setAttribute("viewZvf", zvf);
						if (zvf.getMassnahmen() != null
						    & baumassnahmeForm.getShowZuegeZvf() == true) {
							request.setAttribute("countZuege", zvf.getMassnahmen().iterator()
							    .next().getZug().size());
							setZugListen(zvf.getMassnahmen(), request);
						}
					}
				}
			}

		}

		// Zugliste für ueb
		if (baumassnahmeForm.getShowZuegeUeb() == true) {
			try {
				List<Zug> zuege = baumassnahme.getUebergabeblatt().getMassnahmen().iterator()
				    .next().getZug();
				request.setAttribute("uebZuege", zuege);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// komplette Seite laden
		if (ajax == null || ajax == false)
			return mapping.findForward("SUCCESS");

		return mapping.findForward(forward);
	}

	private void setZugListen(Set<Massnahme> massnahmen, HttpServletRequest request) {

		List<Zug> umzuleitendeZuege = new ArrayList<Zug>();
		List<Zug> verspaeteteZuege = new ArrayList<Zug>();
		List<Zug> ausfaelle = new ArrayList<Zug>();
		List<Zug> vorplanfahren = new ArrayList<Zug>();
		List<Zug> ausfallVerkehrshalte = new ArrayList<Zug>();
		List<Zug> sperrenBedarfsplaene = new ArrayList<Zug>();
		List<Zug> regelungen = new ArrayList<Zug>();

		// Über alle Maßnahmen iterieren
		for (Massnahme mn : massnahmen) {
			if (mn.getZug() == null || Hibernate.isInitialized(mn.getZug()) == false) {
				continue;
			}

			// über alle Züge iterieren und Züge nach Abweichungstyp sortieren
			List<Zug> zuege = mn.getZug();
			Collections.sort(zuege, new ZugComparator("id", SortOrderEnum.ASCENDING));
			Iterator<Zug> it = zuege.iterator();
			Zug zug = null;
			int lfdNrUmzuleitendeZuege = 1;
			int lfdNrVerspaeteteZuege = 1;
			int lfdNrSperrenBedarfsplaene = 1;
			int lfdNrAusfaelle = 1;
			int lfdNrVorplanfahren = 1;
			int lfdNrAusfallVerkehrshalte = 1;
			int lfdNrRegelungen = 1;
			while (it.hasNext()) {
				zug = it.next();
				if (zug.getAbweichung() != null) {
					Abweichung abweichung = zug.getAbweichung();
					if (abweichung.getArt() == Abweichungsart.UMLEITUNG) {
						zug.setLaufendeNr(lfdNrUmzuleitendeZuege++);
						umzuleitendeZuege.add(zug);
					} else if (abweichung.getArt() == Abweichungsart.VERSPAETUNG) {
						zug.setLaufendeNr(lfdNrVerspaeteteZuege++);
						verspaeteteZuege.add(zug);
					} else if (abweichung.getArt() == Abweichungsart.GESPERRT) {
						zug.setLaufendeNr(lfdNrSperrenBedarfsplaene++);
						sperrenBedarfsplaene.add(zug);
					} else if (abweichung.getArt() == Abweichungsart.AUSFALL) {
						zug.setLaufendeNr(lfdNrAusfaelle++);
						ausfaelle.add(zug);
					} else if (abweichung.getArt() == Abweichungsart.VORPLAN) {
						zug.setLaufendeNr(lfdNrVorplanfahren++);
						vorplanfahren.add(zug);
					} else if (abweichung.getArt() == Abweichungsart.ERSATZHALTE) {
						zug.setLaufendeNr(lfdNrAusfallVerkehrshalte++);
						ausfallVerkehrshalte.add(zug);
					} else if (abweichung.getArt() == Abweichungsart.REGELUNG) {
						zug.setLaufendeNr(lfdNrRegelungen++);
						regelungen.add(zug);
					}
				}
			}
		}

		request.setAttribute("UMLEITUNG", umzuleitendeZuege);
		request.setAttribute("VERSPAETUNG", verspaeteteZuege);
		request.setAttribute("AUSFALL", ausfaelle);
		request.setAttribute("VORPLAN", vorplanfahren);
		request.setAttribute("ERSATZHALTE", ausfallVerkehrshalte);
		request.setAttribute("GESPERRT", sperrenBedarfsplaene);
		request.setAttribute("REGELUNG", regelungen);
	}

	private void logNotFoundError(Integer id) {
		if (log.isDebugEnabled())
			log.debug("Baumassnahme not found: " + id);
		addError("error.baumassnahme.notfound");
	}

	public BaumassnahmeService getBaumassnahmeService() {
		return baumassnahmeService;
	}

	public void setBaumassnahmeService(BaumassnahmeService baumassnahmeService) {
		this.baumassnahmeService = baumassnahmeService;
	}

}