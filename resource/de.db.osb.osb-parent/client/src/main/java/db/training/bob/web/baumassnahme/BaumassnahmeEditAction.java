package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.properties.SortOrderEnum;
import org.hibernate.Hibernate;

import db.training.bob.model.Art;
import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.EVU;
import db.training.bob.model.Grund;
import db.training.bob.model.Nachbarbahn;
import db.training.bob.model.Prioritaet;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Niederlassung;
import db.training.bob.model.zvf.RegelungAbw;
import db.training.bob.model.zvf.Regelweg;
import db.training.bob.model.zvf.Sender;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.Zugdetails;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.model.zvf.helper.FormularKennung;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.EVUService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.GrundService;
import db.training.bob.service.NachbarbahnService;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.util.ArrayHelper;
import db.training.bob.util.CollectionHelper;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.FrontendHelper;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.security.domain.EasyAccessDecisionVoter;
import db.training.security.domain.VoterFactory;
import db.training.security.domain.VoterType;
import db.training.security.hibernate.TqmUser;

public class BaumassnahmeEditAction extends BaseAction {

	private static Logger log = Logger.getLogger(BaumassnahmeEditAction.class);

	private BaumassnahmeService baumassnahmeService;

	private RegionalbereichService regionalbereichService;

	private NachbarbahnService nachbarbahnService;

	private GrundService grundService;

	private BearbeitungsbereichService bearbeitungsbereichService;

	private EVUService evuService;

	private static final String dateFormatString = "dd.MM.yy";

	public BaumassnahmeEditAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		regionalbereichService = serviceFactory.createRegionalbereichService();
		nachbarbahnService = serviceFactory.createNachbarbahnService();
		grundService = serviceFactory.createGrundService();
		bearbeitungsbereichService = serviceFactory.createBearbeitungsbereichService();
		evuService = serviceFactory.createEVUService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Entering BaumassnahmeEditAction.");

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		Baumassnahme baumassnahme = null;
		List<Regionalbereich> beteiligteRB = new ArrayList<Regionalbereich>();
		Boolean bearbeitet = null;
		Integer id = null;

		if (request.getAttribute("id") != null)
			id = (Integer) request.getAttribute("id");
		else
			id = baumassnahmeForm.getId();

		Boolean showZuegeZvf = false;
		Boolean showZuegeUeb = false;
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("showZuegeZvf"))) {
			showZuegeZvf = FrontendHelper.castStringToBoolean(request.getParameter("showZuegeZvf"));
			baumassnahmeForm.setShowZuegeZvf(showZuegeZvf);
		}
		if (FrontendHelper.stringNotNullOrEmpty(request.getParameter("showZuegeUeb"))) {
			showZuegeUeb = FrontendHelper.castStringToBoolean(request.getParameter("showZuegeUeb"));
			baumassnahmeForm.setShowZuegeUeb(showZuegeUeb);
		}

		if (id == null) {
			if (log.isDebugEnabled())
				log.debug("No id given.");
			return mapping.findForward("FAILURE");
		}
		if (log.isDebugEnabled())
			log.debug("Processing id: " + id);

		if (id != 0) { // edit
			FetchPlan[] fetchPlans = new FetchPlan[] { FetchPlan.BOB_AENDERUNGSDOKUMENTATION,
			        FetchPlan.BOB_ARBEITSLEISTUNG_REGIONALBEREICHE, FetchPlan.BOB_AUSFALLGRUND,
			        FetchPlan.BOB_BBP_MASSNAHME, FetchPlan.BOB_BEARBEITER,
			        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_KIGBAU, FetchPlan.BOB_QS,
			        FetchPlan.BOB_KORRIDOR_ZEITFENSTER, FetchPlan.BOB_REGIONALBEREICH_FPL,
			        FetchPlan.BOB_TERMINE_BBP, FetchPlan.BOB_TERMINE_GEVU,
			        FetchPlan.BOB_TERMINE_PEVU, FetchPlan.BOB_UEBERGABEBLATT,
			        FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH, FetchPlan.UEB_BAUMASSNAHMEN,
			        FetchPlan.UEB_HEADER, FetchPlan.UEB_HEADER_SENDER,
			        FetchPlan.UEB_HEADER_EMPFAENGER, FetchPlan.UEB_BEARBEITUNGSSTATUS,
			        FetchPlan.UEB_MN_ZUEGE, FetchPlan.BOB_BBZR, FetchPlan.BBZR_HEADER,
			        FetchPlan.BBZR_BAUMASSNAHMEN, FetchPlan.UEB_MN_FPLO,
			        FetchPlan.MN_FPLO_NIEDERLASSUNGEN, FetchPlan.UEB_REGIONALBEREICHE,
			        FetchPlan.ZVF_MN_STRECKEN, FetchPlan.ZVF_MN_BBPSTRECKE,
			        FetchPlan.ZVF_MN_STRECKE_STRECKEVZG, FetchPlan.ZVF_MN_VERSION,
			        FetchPlan.BOB_ZUGCHARAKTERISTIK, FetchPlan.BOB_BBP_MASSNAHME,
			        FetchPlan.BBP_REGELUNGEN };

			if (showZuegeZvf == true) {
				fetchPlans = ArrayHelper.buildArray(fetchPlans, new FetchPlan[] {
				        FetchPlan.BBZR_MN_ZUEGE, FetchPlan.UEB_ZUG_REGELWEG,
				        FetchPlan.UEB_ZUG_ABWEICHUNG_HALT_BAHNHOF,
				        FetchPlan.ZVF_ZUG_ABWEICHUNG_REGELUNG });
			}
			if (showZuegeUeb == true) {
				fetchPlans = ArrayHelper.buildArray(fetchPlans, new FetchPlan[] {
				        FetchPlan.UEB_MN_ZUEGE, FetchPlan.UEB_ZUG_REGELWEG,
				        FetchPlan.UEB_ZUG_ABWEICHUNG_HALT_BAHNHOF,
				        FetchPlan.ZVF_ZUG_ABWEICHUNG_REGELUNG, FetchPlan.UEB_KNOTENZEITEN });
			}

			baumassnahme = baumassnahmeService.findById(id, fetchPlans);
			if (log.isDebugEnabled())
				log.debug("Übergabeblatt isInitialized="
				    + Hibernate.isInitialized(baumassnahme.getUebergabeblatt()));
			if (baumassnahme == null) {
				if (log.isDebugEnabled())
					log.debug("Baumassnahme not found: " + id);
				addError("error.baumassnahme.notfound");
				return mapping.findForward("FAILURE");
			}
			if (log.isDebugEnabled())
				log.debug("Processing baumassnahme: " + id);

			// Rechtepruefung: Bausmassnahme bearbeiten
			EasyAccessDecisionVoter voter = VoterFactory.getDecisionVoter(Baumassnahme.class,
			    VoterType.ANY);
			baumassnahmeForm.setBenchmarkOnly(false);
			if (voter.vote(secUser, baumassnahme, "ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED) {
				boolean userCanEditFavorits = (voter.vote(secUser, baumassnahme,
				    "ROLE_FAVORIT_BEARBEITEN_ALLE") == AccessDecisionVoter.ACCESS_GRANTED);

				if (!userCanEditFavorits
				    && !secUser.hasAuthorization("ROLE_BAUMASSNAHME_BENCHMARK_BEARBEITEN_ALLE")
				    && !secUser
				        .hasAuthorization("ROLE_BAUMASSNAHME_BENCHMARK_BEARBEITEN_REGIONALBEREICH")) {
					// Wenn keine Berechtigung zum Bearbeiten der Baumassnahme
					// als Ganzes oder zum
					// Bearbeiten der Benchmark-Daten vorliegt: Abbruch
					addError("common.noAuth");
					return mapping.findForward("FAILURE");
				}
				baumassnahmeForm.setBenchmarkOnly(true);
			}

			if (!hasErrors(request)) {
				baumassnahmeForm.reset();

				if (baumassnahme.getAktuelleZvf() != null) {
					// Auf aktuelle BBZR einstellen, wenn in View-Mode alte
					// Version ausgewaehlt war
					baumassnahmeForm.setZvf(baumassnahme.getAktuelleZvf().getId());
					request.setAttribute("viewBbzr", baumassnahme.getAktuelleZvf());
				}

				baumassnahmeForm.setBaumassnahme(baumassnahme);
				fillStammdaten(baumassnahme, baumassnahmeForm);
				fillJBB(baumassnahme, baumassnahmeForm);
				fillBBP(baumassnahme, baumassnahmeForm);
				fillStatusAktivitaeten(baumassnahme, baumassnahmeForm);
				fillEskalationAusfall(baumassnahme, baumassnahmeForm);
				fillAufwand(baumassnahme, baumassnahmeForm);
				fillKommentar(baumassnahme, baumassnahmeForm);
				fillVerzichtQTrasse(baumassnahme, baumassnahmeForm);
				fillAbstimmungNachbarbahn(baumassnahme, baumassnahmeForm);
				fillSchnittstelleBBP(baumassnahme, baumassnahmeForm);
				fillSchnittstellePersonenverkehrsEVU(baumassnahme, baumassnahmeForm);
				fillSchnittstelleGueterverkehrsEVU(baumassnahme, baumassnahmeForm);
				fillBearbeiter(baumassnahme, baumassnahmeForm, loginUser);
				if (baumassnahme.getUebergabeblatt() != null) {
					beteiligteRB = fillUebergabeblattBeteiligteRB(baumassnahme, baumassnahmeForm);
					bearbeitet = fillUebergabeblatt(baumassnahme, baumassnahmeForm, showZuegeUeb,
					    bearbeitet, beteiligteRB, loginUser);
				}
				if (baumassnahme.getZvf() != null)
					fillBbzr(baumassnahme, baumassnahmeForm, showZuegeZvf);
			}
		}

		// Werte für Auswahlfelder
		List<Art> arten = Arrays.asList(Art.values());
		request.setAttribute("arten", arten);

		List<Art> uebBaumassnahmenarten = new ArrayList<Art>();
		uebBaumassnahmenarten.add(Art.A);
		uebBaumassnahmenarten.add(Art.B);
		request.setAttribute("uebBaumassnahmenarten", uebBaumassnahmenarten);

		List<FormularKennung> formularkennungenAll = Arrays.asList(FormularKennung.values());
		List<FormularKennung> uebFormularkennungen = new ArrayList<FormularKennung>();
		for (FormularKennung fk : formularkennungenAll) {
			if (fk.toString().startsWith("ÜB"))
				uebFormularkennungen.add(fk);
			if (fk.toString().startsWith("ZVF"))
				uebFormularkennungen.add(fk);
		}
		request.setAttribute("formularkennungen", uebFormularkennungen);

		request.setAttribute("bearbeitet", bearbeitet);

		request.setAttribute("beteiligteRB", beteiligteRB);

		List<User> mailEmpfaenger = new ArrayList<User>();
		request.setAttribute("mailEmpfaenger", mailEmpfaenger);

		List<Prioritaet> prioritaeten = Arrays.asList(Prioritaet.values());
		request.setAttribute("prioritaeten", prioritaeten);

		List<Regionalbereich> regionalbereiche = regionalbereichService.findAll();
		request.setAttribute("regionalbereiche", regionalbereiche);

		List<Nachbarbahn> nachbarbahnen = nachbarbahnService.findAll();
		request.setAttribute("nachbarbahnen", nachbarbahnen);

		List<Grund> gruende = grundService.findAll();
		request.setAttribute("gruende", gruende);

		List<Bearbeitungsbereich> bearbeitungsbereiche = new ArrayList<Bearbeitungsbereich>();
		if (baumassnahme.getRegionalBereichFpl() != null)
			bearbeitungsbereiche = bearbeitungsbereichService.findByRegionalbereich(baumassnahme
			    .getRegionalBereichFpl());
		baumassnahmeForm.setBearbeitungsbereichList(bearbeitungsbereiche);
		request.setAttribute("bearbeitungsbereiche", bearbeitungsbereiche);

		List<EVU> evus = evuService.findAll();
		request.setAttribute("evus", evus);

		baumassnahme.setId(id);
		request.setAttribute("baumassnahme", baumassnahme);

		List<User> possibleBearbeiterList = EasyServiceFactory.getInstance()
		    .createBearbeiterService()
		    .listAvailableBearbeiter(baumassnahme, loginUser.getRegionalbereich().getId());

		request.setAttribute("possibleBearbeiterList", possibleBearbeiterList);

		User currentUser = getLoginUser(request);
		request.setAttribute("currentUser", currentUser);

		// Zuglisten für Ü-Blatt erzeugen
		Set<Massnahme> massnahmen = null;
		if (baumassnahme.getZvf() != null) {
			if (baumassnahme.getAktuelleZvf() != null) {
				massnahmen = baumassnahme.getAktuelleZvf().getMassnahmen();
				request.setAttribute("selectedBbzrID", baumassnahme.getAktuelleZvf().getId());
			}
		}
		if (massnahmen != null) {
			PaginatedList<Zug> pagUmzuleitendeZuege = new PaginatedList<Zug>();
			PaginatedList<Zug> pagVerspaeteteZuege = new PaginatedList<Zug>();
			PaginatedList<Zug> pagAusfaelle = new PaginatedList<Zug>();
			PaginatedList<Zug> pagVorplanfahren = new PaginatedList<Zug>();
			PaginatedList<Zug> pagAusfallVerkehrshalte = new PaginatedList<Zug>();
			PaginatedList<Zug> pagSperrenBedarfsplaene = new PaginatedList<Zug>();
			List<Zug> umzuleitendeZuege = new ArrayList<Zug>();
			List<Zug> verspaeteteZuege = new ArrayList<Zug>();
			List<Zug> ausfaelle = new ArrayList<Zug>();
			List<Zug> vorplanfahren = new ArrayList<Zug>();
			List<Zug> ausfallVerkehrshalte = new ArrayList<Zug>();
			List<Zug> sperrenBedarfsplaene = new ArrayList<Zug>();

			setAttributes(pagUmzuleitendeZuege, umzuleitendeZuege, null, null, 1);
			setAttributes(pagVerspaeteteZuege, verspaeteteZuege, null, null, 1);
			setAttributes(pagAusfaelle, ausfaelle, null, null, 1);
			setAttributes(pagVorplanfahren, vorplanfahren, null, null, 1);
			setAttributes(pagAusfallVerkehrshalte, ausfallVerkehrshalte, null, null, 1);
			setAttributes(pagSperrenBedarfsplaene, sperrenBedarfsplaene, null, null, 1);
			request.setAttribute("UMLEITUNG", pagUmzuleitendeZuege);
			request.setAttribute("VERSPAETUNG", pagVerspaeteteZuege);
			request.setAttribute("AUSFALL", pagAusfaelle);
			request.setAttribute("VORPLAN", pagVorplanfahren);
			request.setAttribute("ERSATZHALTE", pagAusfallVerkehrshalte);
			request.setAttribute("GESPERRT", pagSperrenBedarfsplaene);
		}

		return mapping.findForward("SUCCESS");
	}

	private void setAttributes(PaginatedList<Zug> paginatedList, List<Zug> list,
	    String sortCriterion, SortOrderEnum order, int page) {
		paginatedList.setList(list);
		paginatedList.setSortCriterion(sortCriterion);
		paginatedList.setSortDirection(order);
		paginatedList.setObjectsPerPage(PaginatedList.LIST_OBJECTS_PER_PAGE);
		paginatedList.setPageNumber(page);
	}

	private List<Regionalbereich> fillUebergabeblattBeteiligteRB(Baumassnahme baumassnahme,
	    BaumassnahmeForm baumassnahmeForm) {
		Uebergabeblatt ueb = baumassnahme.getUebergabeblatt();
		List<Regionalbereich> beteiligteRB = new ArrayList<Regionalbereich>();
		if (ueb.getMassnahmen().size() != 0) {

			List<Niederlassung> nl = ueb.getMassnahmen().iterator().next().getFplonr()
			    .getNiederlassungen();

			Niederlassung n = nl.get(0);
			if (n.getFplonr() != null) {
				baumassnahmeForm.setUebFplo0(n.getFplonr().toString());
			}
			Boolean beteiligt = n.getBeteiligt();
			baumassnahmeForm.setUebRb0(beteiligt);
			if (beteiligt == true) {
				beteiligteRB.add(n.getRegionalbereich());
			}

			n = nl.get(1);
			if (n.getFplonr() != null) {
				baumassnahmeForm.setUebFplo1(n.getFplonr().toString());
			}
			beteiligt = n.getBeteiligt();
			baumassnahmeForm.setUebRb1(beteiligt);
			if (beteiligt == true) {
				beteiligteRB.add(n.getRegionalbereich());
			}

			n = nl.get(2);
			if (n.getFplonr() != null) {
				baumassnahmeForm.setUebFplo2(n.getFplonr().toString());
			}
			beteiligt = n.getBeteiligt();
			baumassnahmeForm.setUebRb2(beteiligt);
			if (beteiligt == true) {
				beteiligteRB.add(n.getRegionalbereich());
			}

			n = nl.get(3);
			if (n.getFplonr() != null) {
				baumassnahmeForm.setUebFplo3(n.getFplonr().toString());
			}
			beteiligt = n.getBeteiligt();
			baumassnahmeForm.setUebRb3(beteiligt);
			if (beteiligt == true) {
				beteiligteRB.add(n.getRegionalbereich());
			}

			n = nl.get(4);
			if (n.getFplonr() != null) {
				baumassnahmeForm.setUebFplo4(n.getFplonr().toString());
			}
			beteiligt = n.getBeteiligt();
			baumassnahmeForm.setUebRb4(beteiligt);
			if (beteiligt == true) {
				beteiligteRB.add(n.getRegionalbereich());
			}

			n = nl.get(5);
			if (n.getFplonr() != null) {
				baumassnahmeForm.setUebFplo5(n.getFplonr().toString());
			}
			beteiligt = n.getBeteiligt();
			baumassnahmeForm.setUebRb5(beteiligt);
			if (beteiligt == true) {
				beteiligteRB.add(n.getRegionalbereich());
			}

			n = nl.get(6);
			if (n.getFplonr() != null) {
				baumassnahmeForm.setUebFplo6(n.getFplonr().toString());
			}
			beteiligt = n.getBeteiligt();
			baumassnahmeForm.setUebRb6(beteiligt);
			if (beteiligt == true) {
				beteiligteRB.add(n.getRegionalbereich());
			}
		}
		return beteiligteRB;
	}

	private Boolean sindZuegeVonCurrentRbBearbeitet(Baumassnahme baumassnahme,
	    Regionalbereich userRB) {

		if (baumassnahme.getUebergabeblatt() != null) {
			List<Zug> zugListe = baumassnahme.getUebergabeblatt().getMassnahmen().iterator().next()
			    .getZug();
			for (Zug z : zugListe) {
				try {
					if (z.getBearbeitungsStatusMap().get(userRB) == true)
						return true;
				} catch (NullPointerException e) {
					// return false;
				}
			}
		}
		return false;
	}

	private void fillSchnittstelleGueterverkehrsEVU(Baumassnahme baumassnahme,
	    BaumassnahmeForm baumassnahmeForm) {
		// Schnittstelle Gueterverkehrs-EVU
		Set<TerminUebersichtGueterverkehrsEVU> termineGevu = baumassnahme.getGevus();
		Iterator<TerminUebersichtGueterverkehrsEVU> it3 = termineGevu.iterator();
		while (it3.hasNext()) {
			TerminUebersichtGueterverkehrsEVU t = it3.next();
			// String name = t.getEvu().getName();
			String evuId = Integer.toString(t.getEvuGruppe().getId());

			baumassnahmeForm.setGevuLinkedIds(baumassnahmeForm.getGevuLinkedIds() + evuId + ",");

			baumassnahmeForm.setGevuStudieGrobkonzept(evuId,
			    FrontendHelper.castDateToString(t.getStudieGrobkonzept(), dateFormatString));

			baumassnahmeForm.setGevuZvfEntwurf(evuId,
			    FrontendHelper.castDateToString(t.getZvfEntwurf(), dateFormatString));
			baumassnahmeForm.setGevuIsZvFEntwurfErforderlich(evuId, t.isZvfEntwurfErforderlich());

			baumassnahmeForm.setGevuZvfEntwurf(evuId,
			    FrontendHelper.castDateToString(t.getZvfEntwurf(), dateFormatString));

			baumassnahmeForm.setGevuStellungnahmeEVU(evuId,
			    FrontendHelper.castDateToString(t.getStellungnahmeEVU(), dateFormatString));
			baumassnahmeForm.setGevuIsStellungnahmeEVUErforderlich(evuId,
			    t.isStellungnahmeEVUErforderlich());

			baumassnahmeForm.setGevuZvF(evuId,
			    FrontendHelper.castDateToString(t.getZvF(), dateFormatString));
			baumassnahmeForm.setGevuIsZvFErforderlich(evuId, t.isZvfErforderlich());

			baumassnahmeForm.setGevuMasterUebergabeblattGV(evuId,
			    FrontendHelper.castDateToString(t.getMasterUebergabeblattGV(), dateFormatString));

			baumassnahmeForm.setGevuIsMasterUebergabeblattGVErforderlich(evuId,
			    t.isMasterUebergabeblattGVErforderlich());

			baumassnahmeForm.setGevuUebergabeblattGV(evuId,
			    FrontendHelper.castDateToString(t.getUebergabeblattGV(), dateFormatString));

			baumassnahmeForm.setGevuIsUebergabeblattGVErforderlich(evuId,
			    t.isUebergabeblattGVErforderlich());

			baumassnahmeForm.setGevuFplo(evuId,
			    FrontendHelper.castDateToString(t.getFplo(), dateFormatString));
			baumassnahmeForm.setGevuIsFploErforderlich(evuId, t.isFploErforderlich());

			baumassnahmeForm.setGevuEingabeGFD_Z(evuId,
			    FrontendHelper.castDateToString(t.getEingabeGFD_Z(), dateFormatString));
			baumassnahmeForm.setGevuIsEingabeGFD_ZErforderlich(evuId,
			    t.isEingabeGFD_ZErforderlich());
		}
	}

	private void fillSchnittstellePersonenverkehrsEVU(Baumassnahme baumassnahme,
	    BaumassnahmeForm baumassnahmeForm) {
		// Schnittstelle Personenverkehrs-EVU
		Set<TerminUebersichtPersonenverkehrsEVU> terminePevu = baumassnahme.getPevus();
		Iterator<TerminUebersichtPersonenverkehrsEVU> it2 = terminePevu.iterator();
		while (it2.hasNext()) {
			TerminUebersichtPersonenverkehrsEVU t = it2.next();
			// String name = t.getEvu().getName();
			String evuId = Integer.toString(t.getEvuGruppe().getId());

			baumassnahmeForm.setPevuLinkedIds(baumassnahmeForm.getPevuLinkedIds() + evuId + ",");

			baumassnahmeForm.setPevuStudieGrobkonzept(evuId,
			    FrontendHelper.castDateToString(t.getStudieGrobkonzept(), dateFormatString));

			baumassnahmeForm.setPevuZvfEntwurf(evuId,
			    FrontendHelper.castDateToString(t.getZvfEntwurf(), dateFormatString));
			baumassnahmeForm.setPevuIsZvfEntwurfErforderlich(evuId, t.isZvfEntwurfErforderlich());

			baumassnahmeForm.setPevuStellungnahmeEVU(evuId,
			    FrontendHelper.castDateToString(t.getStellungnahmeEVU(), dateFormatString));
			baumassnahmeForm.setPevuIsStellungnahmeEVUErforderlich(evuId,
			    t.isStellungnahmeEVUErforderlich());

			baumassnahmeForm.setPevuZvF(evuId,
			    FrontendHelper.castDateToString(t.getZvF(), dateFormatString));
			baumassnahmeForm.setPevuIsZvFErforderlich(evuId, t.isZvfErforderlich());

			baumassnahmeForm.setPevuMasterUebergabeblattPV(evuId,
			    FrontendHelper.castDateToString(t.getMasterUebergabeblattPV(), dateFormatString));
			baumassnahmeForm.setPevuIsMasterUebergabeblattPVErforderlich(evuId,
			    t.isMasterUebergabeblattPVErforderlich());

			baumassnahmeForm.setPevuUebergabeblattPV(evuId,
			    FrontendHelper.castDateToString(t.getUebergabeblattPV(), dateFormatString));
			baumassnahmeForm.setPevuIsUebergabeblattPVErforderlich(evuId,
			    t.isUebergabeblattPVErforderlich());

			baumassnahmeForm.setPevuFplo(evuId,
			    FrontendHelper.castDateToString(t.getFplo(), dateFormatString));
			baumassnahmeForm.setPevuIsFploErforderlich(evuId, t.isFploErforderlich());

			baumassnahmeForm.setPevuEingabeGFD_Z(evuId,
			    FrontendHelper.castDateToString(t.getEingabeGFD_Z(), dateFormatString));
			baumassnahmeForm.setPevuIsEingabeGFD_ZErforderlich(evuId,
			    t.isEingabeGFD_ZErforderlich());

			baumassnahmeForm.setPevuAusfaelleSEV(evuId, t.isAusfaelleSEV());

			baumassnahmeForm.setPevuBKonzeptEVU(evuId,
			    FrontendHelper.castDateToString(t.getBKonzeptEVU(), dateFormatString));
			baumassnahmeForm.setPevuIsBKonzeptEVUErforderlich(evuId, t.isBKonzeptEVUErforderlich());
		}
	}

	private void fillSchnittstelleBBP(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// Schnittstelle BBP

		// Index:0
		TerminUebersichtBaubetriebsplanung bbp = baumassnahme.getBaubetriebsplanung();
		if (bbp != null) {

			if (bbp.getStudieGrobkonzept() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getStudieGrobkonzept(), dateFormatString));

			// Index:1
			if (bbp.getAnforderungBBZR() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getAnforderungBBZR(), dateFormatString));

			// Index:2
			if (bbp.getBiUeEntwurf() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getBiUeEntwurf(), dateFormatString));

			// Index:3
			if (bbp.getZvfEntwurf() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getZvfEntwurf(), dateFormatString));

			// Index:4
			if (bbp.getKoordinationsErgebnis() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getKoordinationsErgebnis(), dateFormatString));

			// Index:5
			if (bbp.getGesamtKonzeptBBZR() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getGesamtKonzeptBBZR(), dateFormatString));

			// Index:6
			if (bbp.getBiUe() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getBiUe(), dateFormatString));

			// Index:7
			if (bbp.getZvf() == null)
				baumassnahmeForm.addBaubetriebsplanung("");
			else
				baumassnahmeForm.addBaubetriebsplanung(FrontendHelper.castDateToString(
				    bbp.getZvf(), dateFormatString));

			// Index:8
			baumassnahmeForm.setBaubetriebsplanungErforderlich(2, bbp.isBiUeEntwurfErforderlich());

			// Index:9
			baumassnahmeForm.setBaubetriebsplanungErforderlich(3, bbp.isZvfEntwurfErforderlich());

			// Index:10
			baumassnahmeForm.setBaubetriebsplanungErforderlich(4,
			    bbp.isKoordinationsergebnisErforderlich());

			// Index:11
			baumassnahmeForm.setBaubetriebsplanungErforderlich(5,
			    bbp.isGesamtkonzeptBBZRErforderlich());

			// Index:12
			baumassnahmeForm.setBaubetriebsplanungErforderlich(6, bbp.isBiUeErforderlich());

			// Index:13
			baumassnahmeForm.setBaubetriebsplanungErforderlich(7, bbp.isZvFErforderlich());
		}
	}

	private void fillAbstimmungNachbarbahn(Baumassnahme baumassnahme,
	    BaumassnahmeForm baumassnahmeForm) {
		// Abstimmung Nachbarbahn
		baumassnahmeForm.setAbstimmungNbErforderlich(baumassnahme.getAbstimmungNbErforderlich());
		if (baumassnahme.getNachbarbahn() != null)
			baumassnahmeForm.setNachbarbahn(baumassnahme.getNachbarbahn().getId());
		else
			baumassnahmeForm.setNachbarbahn(null);
		baumassnahmeForm.setAbstimmungNbErfolgtAm(FrontendHelper.castDateToString(baumassnahme
		    .getAbstimmungNbErfolgtAm()));
	}

	private void fillVerzichtQTrasse(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// Verzicht Q-Trasse
		baumassnahmeForm.setVerzichtQTrasseBeantragt(FrontendHelper.castDateToString(
		    baumassnahme.getVerzichtQTrasseBeantragt(), "dd.MM.yyyy"));
		baumassnahmeForm.setVerzichtQTrasseAbgestimmt(FrontendHelper.castDateToString(
		    baumassnahme.getVerzichtQTrasseAbgestimmt(), "dd.MM.yyyy"));
		baumassnahmeForm.setVerzichtQTrasseGenehmigt(baumassnahme.getVerzichtQTrasseGenehmigt());
	}

	private void fillKommentar(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// Kommentar
		baumassnahmeForm.setKommentar(baumassnahme.getKommentar());
	}

	private void fillAufwand(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// Aufwand
		baumassnahmeForm.setAufwandZvF("" + baumassnahme.getAufwandZvF());
		baumassnahmeForm.setAufwandBiUeb("" + baumassnahme.getAufwandBiUeb());
		baumassnahmeForm.setAufwandUeb("" + baumassnahme.getAufwandUeb());
		baumassnahmeForm.setAufwandFplo("" + baumassnahme.getAufwandFplo());
	}

	private void fillEskalationAusfall(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {

		// Eskalation/Ausfall
		baumassnahmeForm.setEskalationsBeginn(FrontendHelper.castDateToString(
		    baumassnahme.getEskalationsBeginn(), "dd.MM.yyyy"));
		baumassnahmeForm.setEskalationsEntscheidung(FrontendHelper.castDateToString(
		    baumassnahme.getEskalationsEntscheidung(), "dd.MM.yyyy"));
		baumassnahmeForm.setEskalationVeto(baumassnahme.getEskalationVeto());
		baumassnahmeForm.setAusfallDatum(FrontendHelper.castDateToString(
		    baumassnahme.getAusfallDatum(), "dd.MM.yyyy"));
		if (baumassnahme.getAusfallGrund() != null)
			baumassnahmeForm.setAusfallGrund(baumassnahme.getAusfallGrund().getId());

		baumassnahmeForm.setBisherigerAufwand(baumassnahme.getBisherigerAufwandTimeString());
	}

	private void fillStatusAktivitaeten(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// Status Aktivitaeten
		baumassnahmeForm.setKonstruktionsEinschraenkung(FrontendHelper.castDateToString(
		    baumassnahme.getKonstruktionsEinschraenkung(), "dd.MM.yyyy"));
		baumassnahmeForm.setAbstimmungFfz(FrontendHelper.castDateToString(
		    baumassnahme.getAbstimmungFfz(), "dd.MM.yyyy"));
		baumassnahmeForm.setAntragAufhebungDienstruhe(FrontendHelper.castDateToString(
		    baumassnahme.getAntragAufhebungDienstruhe(), "dd.MM.yyyy"));
		baumassnahmeForm.setGenehmigungAufhebungDienstruhe(FrontendHelper.castDateToString(
		    baumassnahme.getGenehmigungAufhebungDienstruhe(), "dd.MM.yyyy"));
		baumassnahmeForm.setBiUeNr(baumassnahme.getBiUeNr());
		baumassnahmeForm.setBetraNr(baumassnahme.getBetraNr());
	}

	private void fillJBB(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// JBB/KS/QS
		if (baumassnahme.getKigBau()) {
			Set<String> numSet = baumassnahme.getKigBauNr();
			StringBuffer nummern = new StringBuffer();
			Iterator<String> it = numSet.iterator();
			while (it.hasNext()) {
				nummern.append(it.next());
				if (it.hasNext())
					nummern.append(",");
			}
			baumassnahmeForm.setKigBauNr(nummern.toString());
		}
		try {
			if (baumassnahme.getArt().equals(Art.KS)) {
				if (baumassnahme.getKorridorNr() == null)
					baumassnahmeForm.setKorridorNr("");
				else
					baumassnahmeForm.setKorridorNr("" + baumassnahme.getKorridorNr());

				Set<String> numSet = baumassnahme.getKorridorZeitfenster();
				String nummern = CollectionHelper.toSeparatedString(numSet, ", ");
				baumassnahmeForm.setKorridorZeitfenster(nummern);
			}
			if (baumassnahme.getArt().equals(Art.QS)) {
				List<String> qsSet = baumassnahme.getQsNr();
				String qsNr = CollectionHelper.toSeparatedString(qsSet, ", ");
				baumassnahmeForm.setQsNr(qsNr);
				baumassnahmeForm.setQsSGV(baumassnahme.getQsSGV());
				baumassnahmeForm.setQsSPFV(baumassnahme.getQsSPFV());
				baumassnahmeForm.setQsSPNV(baumassnahme.getQsSPNV());
			} else {
				baumassnahmeForm.setQsNr(null);
				baumassnahmeForm.setQsSGV(false);
				baumassnahmeForm.setQsSPFV(false);
				baumassnahmeForm.setQsSPNV(false);
			}
		} catch (NullPointerException e) {
		}
	}

	private void fillBBP(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// BBP
		Set<BBPMassnahme> allBbp = baumassnahme.getBbpMassnahmen();
		baumassnahmeForm.setBbpMassnahmenSet(allBbp);

	}

	private Boolean fillUebergabeblatt(Baumassnahme baumassnahme,
	    BaumassnahmeForm baumassnahmeForm, boolean showZuegeUeb, Boolean bearbeitet,
	    List<Regionalbereich> beteiligteRB, User loginUser) {
		if (baumassnahme.getUebergabeblatt() != null) {
			// Sender
			baumassnahmeForm.setUebergabeblatt(baumassnahme.getUebergabeblatt().getId());
			Sender sender = baumassnahme.getUebergabeblatt().getHeader().getSender();
			baumassnahmeForm.setSenderName(sender.getName());
			baumassnahmeForm.setSenderVorname(sender.getVorname());
			baumassnahmeForm.setSenderStrasse(sender.getStrasse());
			baumassnahmeForm.setSenderPLZ(sender.getPlz());
			baumassnahmeForm.setSenderOrt(sender.getOrt());
			baumassnahmeForm.setSenderTelefon(sender.getTelefon());
			baumassnahmeForm.setSenderTelefonIntern(sender.getTelefonIntern());
			baumassnahmeForm.setSenderEmail(sender.getEmail());

			// Massnahmendaten
			if (baumassnahme.getUebergabeblatt().getMassnahmen().size() != 0) {

				Massnahme m = baumassnahme.getUebergabeblatt().getMassnahmen().iterator().next();
				if (m.getBbp().size() > 0)
					baumassnahmeForm.setUebBbpStrecke(FrontendHelper.castIntegerToStringStandard(m
					    .getBbp().get(0).getNummer()));

				baumassnahmeForm.setUebFormularkennung(m.getVersion().getFormular().toString());
				if (m.getVersion().getMajor() != null) {
					baumassnahmeForm.setUebVersionMajor(m.getVersion().getMajor().toString());
					baumassnahmeForm.setUebVersionMinor(m.getVersion().getMinor().toString());
					baumassnahmeForm.setUebVersionSub(m.getVersion().getSub());
				} else {
					baumassnahmeForm.setUebVersionMajor("");
					baumassnahmeForm.setUebVersionMinor("");
					baumassnahmeForm.setUebVersionSub("");
				}

				if (m.getBaumassnahmenart() != null)
					baumassnahmeForm.setUebBaumassnahmenart(m.getBaumassnahmenart().name());
				else
					baumassnahmeForm.setUebBaumassnahmenart("");

				baumassnahmeForm.setUebKennung(m.getKennung());
				baumassnahmeForm.setUebLisbaKigbau(m.getKigbau());
				baumassnahmeForm.setUebQsKsVes(m.getQsKsVesNr());
				baumassnahmeForm.setUebKorridor(m.getKorridor());
				baumassnahmeForm.setUebFestgelegtSPFV(m.getFestgelegtSPFV());
				baumassnahmeForm.setUebFestgelegtSPNV(m.getFestgelegtSPNV());
				baumassnahmeForm.setUebFestgelegtSGV(m.getFestgelegtSGV());

				// Strecken
				List<Strecke> strecken = baumassnahme.getUebergabeblatt().getMassnahmen()
				    .iterator().next().getStrecke();
				Iterator<Strecke> itStrecke = strecken.iterator();
				Strecke s = null;
				String index = null;
				int j = 0;
				while (itStrecke.hasNext()) {
					s = itStrecke.next();
					index = "" + j++;
					if (s.getStreckeVZG().size() > 0)
						baumassnahmeForm.setUebStreckeVZG(
						    index,
						    FrontendHelper.castIntegerToStringStandard(s.getStreckeVZG().get(0)
						        .getNummer()));
					else
						baumassnahmeForm.setUebStreckeVZG(index, "");

					baumassnahmeForm.setUebStreckeMassnahme(index, s.getMassnahme());
					baumassnahmeForm.setUebStreckeBaubeginn(index,
					    FrontendHelper.castDateToString(s.getBaubeginn(), "dd.MM.yyyy HH:mm"));
					baumassnahmeForm.setUebStreckeBauende(index,
					    FrontendHelper.castDateToString(s.getBauende(), "dd.MM.yyyy HH:mm"));
					baumassnahmeForm.setUebStreckeUnterbrochen(index, s.getZeitraumUnterbrochen());
					baumassnahmeForm.setUebStreckeGrund(index, s.getGrund());
					baumassnahmeForm.setUebStreckeBetriebsweise(index, s.getBetriebsweise());
				}

				// Züge
				if (showZuegeUeb == true) {
					List<Zug> zugListe = baumassnahme.getUebergabeblatt().getMassnahmen()
					    .iterator().next().getZug();
					Zug z = null;
					for (int i = 0; i < zugListe.size(); i++) {
						z = zugListe.get(i);
						index = "" + i;
						if (z.getVerkehrstag() != null)
							baumassnahmeForm.setZugVerkehrstag(index,
							    FrontendHelper.castDateToString(z.getVerkehrstag(), "dd.MM.yyyy"));
						if (z.getZugbez() != null)
							baumassnahmeForm.setZugZuggattung(index, z.getZugbez());
						if (z.getZugnr() != null)
							baumassnahmeForm.setZugZugnr(index, z.getZugnr().toString());
						Regelweg r = z.getRegelweg();
						if (r != null) {
							if (r.getAbgangsbahnhof() != null)
								baumassnahmeForm.setZugAbgangsbhf(index, r.getAbgangsbahnhof()
								    .getLangName());
							if (r.getZielbahnhof() != null)
								baumassnahmeForm.setZugZielbhf(index, z.getRegelweg()
								    .getZielbahnhof().getLangName());
						}
						if (z.getKvProfil() != null)
							baumassnahmeForm.setZugKvProfil(index, z.getKvProfil());
						if (z.getStreckenKlasse() != null)
							baumassnahmeForm.setZugStreckenklasse(index, z.getStreckenKlasse());
						if (z.getBemerkung() != null)
							baumassnahmeForm.setZugBemerkung(index, z.getBemerkung());
						if (z.getQs_ks() != null)
							baumassnahmeForm.setZugQsKs(index, z.getQs_ks());
						else
							baumassnahmeForm.setZugQsKs(index, 0);

						baumassnahmeForm.setUebZugRichtung(index, z.getRichtung());

						if (z.getZugdetails() != null) {
							Zugdetails zd = z.getZugdetails();
							if (zd.getTfz() != null)
								baumassnahmeForm.setZugTfz(index, zd.getTfz().getTfz());
							if (zd.getLast() != null)
								if (zd.getLast().getLast() != null)
									baumassnahmeForm.setZugLast(index, zd.getLast().getLast()
									    .toString());
							if (zd.getBrems() != null)
								baumassnahmeForm.setZugMbr(index, zd.getBrems());
							if (zd.getLaenge() != null)
								if (zd.getLaenge() == 0)
									baumassnahmeForm.setZugLaenge(index, "");
								else
									baumassnahmeForm.setZugLaenge(index, zd.getLaenge().toString());
							if (zd.getVmax() != null)
								if (zd.getLaenge() == 0)
									baumassnahmeForm.setZugVmax(index, "");
								else
									baumassnahmeForm.setZugVmax(index, zd.getVmax().toString());
						}
						for (Regionalbereich rb : z.getBearbeitungsStatusMap().keySet()) {
							Boolean bearb = false;
							if (rb.equals(loginUser.getRegionalbereich())) {
								bearb = z.getBearbeitungsStatusMap().get(rb);
								baumassnahmeForm.setZugBearbeitet(index, bearb);
							}
						}
					}
				}
				baumassnahmeForm.setUebEmpfaengerRB(0);
				String[] mailEmp = new String[1];
				mailEmp[0] = "0";
				baumassnahmeForm.setUebMailEmpfaenger(mailEmp);
				Regionalbereich userRB = loginUser.getRegionalbereich();

				if (!beteiligteRB.contains(userRB)) {
					bearbeitet = false;
				} else {
					bearbeitet = sindZuegeVonCurrentRbBearbeitet(baumassnahme, userRB);
				}
			}
		}
		return bearbeitet;
	}

	private void fillBbzr(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm,
	    boolean showZuegeBbzr) {
		// Sender
		if (baumassnahme.getZvf().size() > 0) {
			baumassnahmeForm.setZvf(baumassnahme.getAktuelleZvf().getId());
			Sender sender = baumassnahme.getAktuelleZvf().getHeader().getSender();
			baumassnahmeForm.setZvfSenderName(sender.getName());
			baumassnahmeForm.setZvfSenderVorname(sender.getVorname());
			baumassnahmeForm.setZvfSenderStrasse(sender.getStrasse());
			baumassnahmeForm.setZvfSenderPLZ(sender.getPlz());
			baumassnahmeForm.setZvfSenderOrt(sender.getOrt());
			baumassnahmeForm.setZvfSenderTelefon(sender.getTelefon());
			baumassnahmeForm.setZvfSenderTelefonIntern(sender.getTelefonIntern());
			baumassnahmeForm.setZvfSenderEmail(sender.getEmail());

			// Massnahmendaten
			Massnahme m = null;
			try {
				m = baumassnahme.getAktuelleZvf().getMassnahmen().iterator().next();

				if (m.getBbp().size() > 0)
					baumassnahmeForm.setZvfBbpStrecke(FrontendHelper.castIntegerToStringStandard(m
					    .getBbp().get(0).getNummer()));

				baumassnahmeForm.setZvfFormularkennung(m.getVersion().getFormular().toString());
				if (m.getVersion().getMajor() != null) {
					baumassnahmeForm.setZvfVersionMajor(m.getVersion().getMajor().toString());
					baumassnahmeForm.setZvfVersionMinor(m.getVersion().getMinor().toString());
					baumassnahmeForm.setZvfVersionSub(m.getVersion().getSub());
				} else {
					baumassnahmeForm.setZvfVersionMajor("");
					baumassnahmeForm.setZvfVersionMinor("");
					baumassnahmeForm.setZvfVersionSub("");
				}

				if (m.getBaumassnahmenart() != null)
					baumassnahmeForm.setZvfBaumassnahmenart(m.getBaumassnahmenart().name());
				else
					baumassnahmeForm.setZvfBaumassnahmenart(Art.A.name());

				baumassnahmeForm.setZvfKennung(m.getKennung());
				baumassnahmeForm.setZvfLisbaKigbau(m.getKigbau());
				baumassnahmeForm.setZvfQsKsVes(m.getQsKsVesNr());
				baumassnahmeForm.setZvfKorridor(m.getKorridor());
				baumassnahmeForm.setZvfFestgelegtSPFV(m.getFestgelegtSPFV());
				baumassnahmeForm.setZvfFestgelegtSPNV(m.getFestgelegtSPNV());
				baumassnahmeForm.setZvfFestgelegtSGV(m.getFestgelegtSGV());

				// Strecken
				List<Strecke> strecken = baumassnahme.getAktuelleZvf().getMassnahmen().iterator()
				    .next().getStrecke();
				Iterator<Strecke> itStrecke = strecken.iterator();
				Strecke s = null;
				String index = null;
				int j = 0;
				while (itStrecke.hasNext()) {
					s = itStrecke.next();
					index = "" + j++;
					if (s.getStreckeVZG().size() > 0)
						baumassnahmeForm.setZvfStreckeVZG(
						    index,
						    FrontendHelper.castIntegerToStringStandard(s.getStreckeVZG().get(0)
						        .getNummer()));
					else
						baumassnahmeForm.setZvfStreckeVZG(index, "");

					baumassnahmeForm.setZvfStreckeMassnahme(index, s.getMassnahme());
					baumassnahmeForm.setZvfStreckeBaubeginn(index,
					    FrontendHelper.castDateToString(s.getBaubeginn(), "dd.MM.yyyy HH:mm"));
					baumassnahmeForm.setZvfStreckeBauende(index,
					    FrontendHelper.castDateToString(s.getBauende(), "dd.MM.yyyy HH:mm"));
					baumassnahmeForm.setZvfStreckeUnterbrochen(index, s.getZeitraumUnterbrochen());
					baumassnahmeForm.setZvfStreckeGrund(index, s.getGrund());
					baumassnahmeForm.setZvfStreckeBetriebsweise(index, s.getBetriebsweise());
				}

				// Züge
				if (showZuegeBbzr == true) {
					List<Zug> zugListe = baumassnahme.getAktuelleZvf().getMassnahmen().iterator()
					    .next().getZug();
					Zug z = null;
					for (int i = 0; i < zugListe.size(); i++) {
						z = zugListe.get(i);
						index = "" + i;
						if (z.getTageswechsel() != null)
							baumassnahmeForm.setZvfZugTageswechsel(index, z.getTageswechsel());
						if (z.getVerkehrstag() != null)
							baumassnahmeForm.setZvfZugVerkehrstag(index,
							    FrontendHelper.castDateToString(z.getVerkehrstag(), "dd.MM.yyyy"));
						if (z.getZugbez() != null)
							baumassnahmeForm.setZvfZugZuggattung(index, z.getZugbez());
						if (z.getZugnr() != null)
							baumassnahmeForm.setZvfZugZugnr(index, z.getZugnr().toString());
						if (z.getBedarf() != null)
							baumassnahmeForm.setZvfZugBedarf(index, z.getBedarf());
						Regelweg r = z.getRegelweg();
						if (r != null) {
							if (r.getAbgangsbahnhof() != null)
								baumassnahmeForm.setZvfZugAbgangsbhf(index, r.getAbgangsbahnhof()
								    .getLangName());
							if (r.getZielbahnhof() != null)
								baumassnahmeForm.setZvfZugZielbhf(index, r.getZielbahnhof()
								    .getLangName());
						}
						if (z.getAbweichung().getUmleitung() != null)
							baumassnahmeForm.setZvfZugUmleitungsstrecke(index, z.getAbweichung()
							    .getUmleitung());
						if (z.getAbweichung().getVerspaetung() != null) // Verspaetung
							// und
							// ZeitVorPlan
							baumassnahmeForm.setZvfZugVerspaetung(index, z.getAbweichung()
							    .getVerspaetung().toString());
						if (z.getAbweichung().getAusfallvon() != null)
							baumassnahmeForm.setZvfZugAusfallAb(index, z.getAbweichung()
							    .getAusfallvon().getLangName());
						if (z.getAbweichung().getAusfallbis() != null)
							baumassnahmeForm.setZvfZugAusfallBis(index, z.getAbweichung()
							    .getAusfallbis().getLangName());
						if (z.getAbweichung().getVorplanab() != null)
							baumassnahmeForm.setZvfZugVorplanAb(index, z.getAbweichung()
							    .getVorplanab().getLangName());
						if (z.getAbweichung().getArt() == Abweichungsart.ERSATZHALTE) {
							if (z.getAbweichung().getHalt() != null) {
								List<Halt> halte = z.getAbweichung().getHalt();
								List<String> ausfallhalteStrings = new ArrayList<String>();
								List<String> ersatzhalteStrings = new ArrayList<String>();
								for (Halt h : halte) {
									ausfallhalteStrings.add(h.getAusfall().getLangName());
									if (h.getErsatz() == null)
										ersatzhalteStrings.add("");
									else {
										if (h.getErsatz().getLangName() == null)
											ersatzhalteStrings.add("");
										else
											ersatzhalteStrings.add(h.getErsatz().getLangName());
									}
								}
								baumassnahmeForm.setZvfZugAusfallVerkehrshalt(index,
								    ausfallhalteStrings);
								baumassnahmeForm.setZvfZugMoeglicherErsatzhalt(index,
								    ersatzhalteStrings);
							}
						}
						if (z.getAbweichung().getArt() == Abweichungsart.REGELUNG) {
							if (z.getAbweichung().getRegelungen() != null) {
								List<RegelungAbw> regelungen = z.getAbweichung().getRegelungen();
								List<String> regelungsartStrings = new ArrayList<String>();
								List<String> giltInStrings = new ArrayList<String>();
								List<String> textStrings = new ArrayList<String>();
								for (RegelungAbw reg : regelungen) {
									regelungsartStrings.add(reg.getArt());
									if (reg.getGiltIn() == null) {
										giltInStrings.add("");
									} else {
										giltInStrings.add(reg.getGiltIn().getLangName());
									}
									if (reg.getText() == null) {
										textStrings.add("");
									} else {
										textStrings.add(reg.getText());
									}
								}
								baumassnahmeForm.setZvfZugRegelungsart(index, regelungsartStrings);
								baumassnahmeForm.setZvfZugRegelungGiltin(index, giltInStrings);
								baumassnahmeForm.setZvfZugRegelungtext(index, textStrings);
							}
						}

						if (z.getBemerkung() != null)
							baumassnahmeForm.setZvfZugBemerkung(index, z.getBemerkung());
						if (z.getQs_ks() != null)
							baumassnahmeForm.setZvfZugQsKs(index, z.getQs_ks());
						else
							baumassnahmeForm.setZvfZugQsKs(index, 0);
						baumassnahmeForm.setZvfZugRichtung(index, z.getRichtung());
					}
				}
			} catch (NoSuchElementException e) {
				// do nothing
			}
		}
	}

	private void fillStammdaten(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		// Stammdaten
		baumassnahmeForm.setId(baumassnahme.getId());
		try {
			baumassnahmeForm.setArt(baumassnahme.getArt().name());
		} catch (NullPointerException e) {
		}
		baumassnahmeForm.setKigBau(baumassnahme.getKigBau());
		baumassnahmeForm.setStreckeBBP(baumassnahme.getStreckeBBP());
		baumassnahmeForm.setStreckeVZG(baumassnahme.getStreckeVZG());
		baumassnahmeForm.setStreckenAbschnitt(baumassnahme.getStreckenAbschnitt());
		baumassnahmeForm.setArtDerMassnahme(baumassnahme.getArtDerMassnahme());
		baumassnahmeForm.setBetriebsweise(baumassnahme.getBetriebsweise());
		baumassnahmeForm.setBeginnFuerTerminberechnung(FrontendHelper.castDateToString(
		    baumassnahme.getBeginnFuerTerminberechnung(), "dd.MM.yyyy"));
		baumassnahmeForm.setBeginnDatum(FrontendHelper.castDateToString(
		    baumassnahme.getBeginnDatum(), "dd.MM.yyyy"));
		baumassnahmeForm.setEndDatum(FrontendHelper.castDateToString(baumassnahme.getEndDatum(),
		    "dd.MM.yyyy"));

		if (baumassnahme.getRegionalbereichBM() != null)
			baumassnahmeForm.setRegionalbereichBM(baumassnahme.getRegionalbereichBM());
		if (baumassnahme.getRegionalBereichFpl() != null)
			baumassnahmeForm.setRegionalBereichFpl(baumassnahme.getRegionalBereichFpl().getId());
		if (baumassnahme.getBearbeitungsbereich() != null)
			baumassnahmeForm.setBearbeitungsbereich(baumassnahme.getBearbeitungsbereich().getId());

		baumassnahmeForm.setFploNr(baumassnahme.getFploNr());
		baumassnahmeForm.setVorgangsNr(baumassnahme.getVorgangsNr());
		if (baumassnahme.getPrioritaet() != null)
			baumassnahmeForm.setPrioritaet(baumassnahme.getPrioritaet().name());

		List<Bearbeitungsbereich> bearbeitungsbereiche = bearbeitungsbereichService.findAll();
		baumassnahmeForm.setBearbeitungsbereiche(bearbeitungsbereiche);
	}

	private void fillBearbeiter(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm,
	    User loginUser) {
		Map<String, Boolean> bearbeiter = new HashMap<String, Boolean>();
		if (baumassnahme.getBearbeiter() != null)
			if (baumassnahme.getBearbeiter().size() > 0)
				for (Bearbeiter b : baumassnahme.getBearbeiter()) {
					bearbeiter.put(b.getId().toString(), b.getAktiv());
				}
		baumassnahmeForm.setBearbeiter(bearbeiter);
		baumassnahmeForm.setInsertBearbeiter(loginUser.getId());
	}
}
