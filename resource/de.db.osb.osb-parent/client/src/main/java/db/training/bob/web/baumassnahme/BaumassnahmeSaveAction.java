package db.training.bob.web.baumassnahme;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import db.training.bob.model.Art;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.EVUGruppe;
import db.training.bob.model.Grund;
import db.training.bob.model.Nachbarbahn;
import db.training.bob.model.Prioritaet;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.model.zvf.Abweichung;
import db.training.bob.model.zvf.BBPStrecke;
import db.training.bob.model.zvf.Bahnhof;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Niederlassung;
import db.training.bob.model.zvf.RegelungAbw;
import db.training.bob.model.zvf.Strecke;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Version;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.Zugdetails;
import db.training.bob.model.zvf.helper.Abweichungsart;
import db.training.bob.model.zvf.helper.FormularKennung;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.FetchPlan;
import db.training.bob.service.GrundService;
import db.training.bob.service.NachbarbahnService;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.service.TerminUebersichtGueterverkehrsEVUService;
import db.training.bob.service.TerminUebersichtPersonenverkehrsEVUService;
import db.training.bob.service.Terminberechnung;
import db.training.bob.service.zvf.BBPStreckeService;
import db.training.bob.service.zvf.BahnhofService;
import db.training.bob.service.zvf.BbzrService;
import db.training.bob.service.zvf.UebergabeblattService;
import db.training.bob.util.CollectionHelper;
import db.training.easy.core.model.User;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.easy.web.BaseAction;
import db.training.logwrapper.Logger;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.VzgStreckeService;
import db.training.security.domain.BaumassnahmeAnyVoter;
import db.training.security.domain.UebergabeblattAnyVoter;
import db.training.security.domain.VoterFactory;
import db.training.security.domain.VoterType;
import db.training.security.hibernate.TqmUser;

public class BaumassnahmeSaveAction extends BaseAction {

	private static Logger log = Logger.getLogger(BaumassnahmeSaveAction.class);

	private BaumassnahmeService baumassnahmeService;

	private RegionalbereichService regionalbereichService;

	private GrundService grundService;

	private BahnhofService bahnhofService;

	private VzgStreckeService vzgService;

	private BBPStreckeService bbpStreckeService;

	final ReentrantLock lock = new ReentrantLock();

	public BaumassnahmeSaveAction() {
		serviceFactory = EasyServiceFactory.getInstance();
		baumassnahmeService = serviceFactory.createBaumassnahmeService();
		regionalbereichService = serviceFactory.createRegionalbereichService();
		grundService = serviceFactory.createGrundService();
		bahnhofService = serviceFactory.createBahnhofService();
		vzgService = serviceFactory.createVzgStreckeService();
		bbpStreckeService = serviceFactory.createBBPStreckeService();
	}

	@Override
	protected ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("instance is " + this);
			log.debug("Entering BaumassnahmeSaveAction.");
		}

		User loginUser = getLoginUser(request);
		TqmUser secUser = getSecUser();

		BaumassnahmeForm baumassnahmeForm = (BaumassnahmeForm) form;
		Baumassnahme baumassnahme = null;
		Integer id;
		Art oldArt = null;
		Date oldBaubeginn = null;

		try {
			id = baumassnahmeForm.getId();
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Baumassnahme not found");
			addError("error.baumassnahme.notfound");
			return mapping.findForward("FAILURE");
		}

		if (id != 0) {
			// FetchPlan.BOB_REGIONALBEREICH_FPL wird mind. für
			// BaumassnahmeAnyVoter benötigt!
			FetchPlan[] fetchPlans = new FetchPlan[] {
			        FetchPlan.BOB_ARBEITSLEISTUNG_REGIONALBEREICHE,
			        FetchPlan.BOB_AENDERUNGSDOKUMENTATION, FetchPlan.BOB_AUSFALLGRUND,
			        FetchPlan.BOB_BBP_MASSNAHME, FetchPlan.BOB_BEARBEITER,
			        FetchPlan.BOB_BEARBEITUNGSBEREICH, FetchPlan.BOB_KIGBAU, FetchPlan.BOB_QS,
			        FetchPlan.BOB_KORRIDOR_ZEITFENSTER, FetchPlan.BOB_REGIONALBEREICH_FPL,
			        FetchPlan.BOB_TERMINE_BBP, FetchPlan.BOB_TERMINE_GEVU,
			        FetchPlan.BOB_TERMINE_PEVU, FetchPlan.BOB_UEBERGABEBLATT,
			        FetchPlan.REGIONALBEREICH_BEARBEITUNGSBEREICH, FetchPlan.UEB_BAUMASSNAHMEN,
			        FetchPlan.BOB_BBZR, FetchPlan.BBZR_BAUMASSNAHMEN, FetchPlan.ZVF_MN_VERSION,
			        FetchPlan.UEB_REGIONALBEREICHE };

			baumassnahme = baumassnahmeService.findById(id, fetchPlans);

			oldArt = baumassnahme.getArt();
			oldBaubeginn = baumassnahme.getBeginnFuerTerminberechnung();

			if (log.isDebugEnabled())
				log.debug("Find Baumassnahme by Id: " + id);
		} else {// Baumassnahme erstellen
			baumassnahme = new Baumassnahme();
			setStammdaten(baumassnahme, baumassnahmeForm);
			Date beginndatum = FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBeginnFuerTerminberechnung());
			baumassnahme.setFahrplanjahr(EasyDateFormat.getFahrplanJahr(beginndatum));
			baumassnahme.addBearbeiter(loginUser);
			Terminberechnung.refreshSolltermine(baumassnahme);
			try {
				lock.lock();
				baumassnahmeService.create(baumassnahme);
			} finally {
				lock.unlock();
			}
			request.setAttribute("id", baumassnahme.getId());
			addMessage("success.baumassnahme.save");
			if (log.isDebugEnabled())
				log.debug("Baumassnahme angelegt mit Id: " + baumassnahme.getId());
			return mapping.findForward("SUCCESS");
		}
		// Zugriffsberechtigung prüfen
		boolean inBenchmarkOnlyMode = true;
		BaumassnahmeAnyVoter voter = EasyServiceFactory.createBaumassnahmeAnyVoter();
		inBenchmarkOnlyMode = (voter.vote(secUser, baumassnahme,
		    "ROLE_BAUMASSNAHME_GRUNDDATEN_BEARBEITEN") == AccessDecisionVoter.ACCESS_DENIED);
		UebergabeblattAnyVoter ueBlattVoter = EasyServiceFactory.getInstance()
		    .createUebergabeblattAnyVoter();
		try {
			if (!inBenchmarkOnlyMode) {
				setStammdaten(baumassnahme, baumassnahmeForm);
				setJbbKsQs(baumassnahmeForm, baumassnahme);
				setStatusAktivitaeten(baumassnahmeForm, baumassnahme);
				setEskalationAusfall(baumassnahmeForm, baumassnahme);
				setAufwand(baumassnahmeForm, baumassnahme);
				setKommentar(baumassnahmeForm, baumassnahme);
				setVerzichtQTrasse(baumassnahmeForm, baumassnahme);
				setAbstimmungNachbarbahn(baumassnahmeForm, baumassnahme);
				setTermineBBP(baumassnahmeForm, baumassnahme, oldArt, oldBaubeginn);
				baumassnahme = setTerminePersonenverkehrsEVU(baumassnahmeForm, baumassnahme,
				    oldArt, oldBaubeginn);
				baumassnahme = setTermineGueterverkehrsEVU(baumassnahmeForm, baumassnahme, oldArt,
				    oldBaubeginn);
				setUebergabeblatt(baumassnahmeForm, baumassnahme, secUser, loginUser);
				setBbzr(baumassnahmeForm, baumassnahme, secUser);
				setBearbeiter(baumassnahmeForm, baumassnahme);

				baumassnahme.setLastChange(null);
			} else {
				if ((baumassnahme.getUebergabeblatt() == null && voter.vote(secUser, baumassnahme,
				    "ROLE_BBZR_ANLEGEN") == AccessDecisionVoter.ACCESS_GRANTED)
				    || (baumassnahme.getUebergabeblatt() != null && ueBlattVoter.vote(secUser,
				        baumassnahme.getUebergabeblatt(), "ROLE_BBZR_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED)) {
					setUebergabeblatt(baumassnahmeForm, baumassnahme, secUser, loginUser);
				}
				if ((baumassnahme.getAktuelleZvf() == null && voter.vote(secUser, baumassnahme,
				    "ROLE_BBZR_ANLEGEN") == AccessDecisionVoter.ACCESS_GRANTED)
				    || (baumassnahme.getAktuelleZvf() != null && ueBlattVoter.vote(secUser,
				        baumassnahme.getAktuelleZvf(), "ROLE_BBZR_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED)) {
					setBbzr(baumassnahmeForm, baumassnahme, secUser);
				}
				//es darf nur gespeichert werden, wenn der User auch Favoriten speichern darf
				if (voter.vote(secUser, baumassnahme, "ROLE_FAVORIT_BEARBEITEN_ALLE") == AccessDecisionVoter.ACCESS_DENIED){
					addError("common.noAuth");
					return mapping.findForward("FAILURE");
				}else {
					setBearbeiter(baumassnahmeForm, baumassnahme);
					baumassnahmeService.update(baumassnahme);
				}
			}

			baumassnahmeService.update(baumassnahme);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			addError("error.qsnr.format");
			return mapping.findForward("FAILURE");
		} catch (Exception e) {
			e.printStackTrace();
			addError("errors.save.exception");
			return mapping.findForward("FAILURE");
		}
		request.setAttribute("id", id);

		addMessage("success.baumassnahme.save");
		return mapping.findForward("SUCCESS");
	}

	private void setUebergabeblatt(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme,
	    TqmUser secUser, User loginUser) {

		// Nur Daten des UB speichern
		UebergabeblattService uebergabeblattService = serviceFactory.createUebergabeblattService();
		Integer id = null;

		if (baumassnahme.getUebergabeblatt() != null) {
			id = baumassnahme.getUebergabeblatt().getId();
			Uebergabeblatt ueb = uebergabeblattService.findById(id, new FetchPlan[] {
			        FetchPlan.UEB_HEADER, FetchPlan.UEB_HEADER_SENDER, FetchPlan.UEB_BAUMASSNAHMEN,
			        FetchPlan.UEB_REGIONALBEREICHE, FetchPlan.ZVF_MN_VERSION,
			        FetchPlan.ZVF_MN_BBPSTRECKE, FetchPlan.UEB_MN_ZUEGE, FetchPlan.BBZR_MN_ZUEGE,
			        FetchPlan.ZVF_MN_STRECKEN, FetchPlan.ZVF_MN_STRECKE_STRECKEVZG,
			        FetchPlan.UEB_MN_FPLO, FetchPlan.MN_FPLO_NIEDERLASSUNGEN,
			        FetchPlan.UEB_BEARBEITUNGSSTATUS, FetchPlan.UEB_ZUG_REGELWEG });

			UebergabeblattAnyVoter voter = new UebergabeblattAnyVoter();
			boolean allowed = (voter.vote(secUser, ueb, "ROLE_BBZR_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED);

			if (allowed == true) {
				// Sender
				ueb.getHeader().getSender().setName(baumassnahmeForm.getSenderName());
				ueb.getHeader().getSender().setVorname(baumassnahmeForm.getSenderVorname());
				ueb.getHeader().getSender().setStrasse(baumassnahmeForm.getSenderStrasse());
				ueb.getHeader().getSender().setPlz(baumassnahmeForm.getSenderPLZ());
				ueb.getHeader().getSender().setOrt(baumassnahmeForm.getSenderOrt());
				ueb.getHeader().getSender().setTelefon(baumassnahmeForm.getSenderTelefon());
				ueb.getHeader().getSender()
				    .setTelefonIntern(baumassnahmeForm.getSenderTelefonIntern());
				ueb.getHeader().getSender().setEmail(baumassnahmeForm.getSenderEmail());

				// Massnahmendaten
				Massnahme m = ueb.getMassnahmen().iterator().next();

				Integer neueBBPNr;
				// falls noch nicht in zvf enthaltene BBP-Nr eingegeben wurde,
				// dann an
				// erster Stelle einfügen
				if (baumassnahmeForm.getUebBbpStrecke() != null) {
					neueBBPNr = FrontendHelper.castStringToInteger(baumassnahmeForm
					    .getUebBbpStrecke());
					if (neueBBPNr != null) {
						BBPStrecke bbpStrecke = bbpStreckeService.findByNummer(neueBBPNr);
						if (bbpStrecke != null & !m.getBbp().contains(bbpStrecke))
							m.getBbp().add(0, bbpStrecke);
					}
				}

				Version v = m.getVersion();
				if (baumassnahmeForm.getUebFormularkennung() != null)
					v.setFormular(FormularKennung.castFromString(baumassnahmeForm
					    .getUebFormularkennung()));
				else
					v.setFormular(FormularKennung.UeB_091008);
				v.setMajor(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebVersionMajor()));
				v.setMinor(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebVersionMinor()));
				v.setSub(baumassnahmeForm.getUebVersionSub());

				if (baumassnahmeForm.getUebBaumassnahmenart() != null) {
					try {
						m.setBaumassnahmenart(Art.valueOf(baumassnahmeForm.getUebBaumassnahmenart()));
					} catch (IllegalArgumentException e) {
						m.setBaumassnahmenart(null);
					}
				}

				m.setKennung(baumassnahmeForm.getUebKennung());
				m.setKigbau(baumassnahmeForm.getUebLisbaKigbau());
				m.setQsKsVesNr(baumassnahmeForm.getUebQsKsVes());
				m.setKorridor(baumassnahmeForm.getUebKorridor());
				m.setFestgelegtSPFV(baumassnahmeForm.getUebFestgelegtSPFV());
				m.setFestgelegtSPNV(baumassnahmeForm.getUebFestgelegtSPNV());
				m.setFestgelegtSGV(baumassnahmeForm.getUebFestgelegtSGV());

				// beteiligte Regionalbereiche
				List<Niederlassung> nl = m.getFplonr().getNiederlassungen();

				Niederlassung n = nl.get(0);
				n.setFplonr(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebFplo0()));
				if (baumassnahmeForm.getUebRb0() == null)
					n.setBeteiligt(false);
				else
					n.setBeteiligt(baumassnahmeForm.getUebRb0());

				n = nl.get(1);
				n.setFplonr(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebFplo1()));
				if (baumassnahmeForm.getUebRb1() == null)
					n.setBeteiligt(false);
				else
					n.setBeteiligt(baumassnahmeForm.getUebRb1());

				n = nl.get(2);
				n.setFplonr(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebFplo2()));
				if (baumassnahmeForm.getUebRb2() == null)
					n.setBeteiligt(false);
				else
					n.setBeteiligt(baumassnahmeForm.getUebRb2());

				n = nl.get(3);
				n.setFplonr(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebFplo3()));
				if (baumassnahmeForm.getUebRb3() == null)
					n.setBeteiligt(false);
				else
					n.setBeteiligt(baumassnahmeForm.getUebRb3());

				n = nl.get(4);
				n.setFplonr(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebFplo4()));
				if (baumassnahmeForm.getUebRb4() == null)
					n.setBeteiligt(false);
				else
					n.setBeteiligt(baumassnahmeForm.getUebRb4());

				n = nl.get(5);
				n.setFplonr(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebFplo5()));
				if (baumassnahmeForm.getUebRb5() == null)
					n.setBeteiligt(false);
				else
					n.setBeteiligt(baumassnahmeForm.getUebRb5());

				n = nl.get(6);
				n.setFplonr(FrontendHelper.castStringToInteger(baumassnahmeForm.getUebFplo6()));
				if (baumassnahmeForm.getUebRb6() == null)
					n.setBeteiligt(false);
				else
					n.setBeteiligt(baumassnahmeForm.getUebRb6());

				// Strecken
				List<Strecke> strecken = m.getStrecke();
				Iterator<Strecke> itStrecke = strecken.iterator();
				Strecke s = null;
				String index = null;
				int j = 0;
				while (itStrecke.hasNext()) {
					s = itStrecke.next();
					index = "" + j++;
					Integer neueVzgNr;

					// falls noch nicht in zvf enthaltene Vzg-Nr eingegeben
					// wurde, dann an
					// erster Stelle einfügen
					if (baumassnahmeForm.getUebStreckeVZG(index) != null) {
						neueVzgNr = FrontendHelper.castStringToInteger(baumassnahmeForm
						    .getUebStreckeVZG(index));
						if (neueVzgNr != null) {
							VzgStrecke vzgStrecke = vzgService.findByNummer(neueVzgNr, null, null);
							if (vzgStrecke != null & !s.getStreckeVZG().contains(vzgStrecke))
								s.getStreckeVZG().add(0, vzgStrecke);
						}
					}
					s.setMassnahme(baumassnahmeForm.getUebStreckeMassnahme(index));
					s.setBaubeginn(FrontendHelper.castStringToDate(baumassnahmeForm
					    .getUebStreckeBaubeginn(index)));
					s.setBauende(FrontendHelper.castStringToDate(baumassnahmeForm
					    .getUebStreckeBauende(index)));
					s.setZeitraumUnterbrochen(baumassnahmeForm.getUebStreckeUnterbrochen(index));
					s.setGrund(baumassnahmeForm.getUebStreckeGrund(index));
					s.setBetriebsweise(baumassnahmeForm.getUebStreckeBetriebsweise(index));
				}

				// Züge
				if (baumassnahmeForm.getShowZuegeUeb() == true) {
					List<Zug> zuege = m.getZug();
					Zug z = null;
					List<Bahnhof> bahnhoefe = null;
					Bahnhof bhf = null;

					for (int i = 0; i < zuege.size(); i++) {
						z = zuege.get(i);
						index = "" + i;
						z.setVerkehrstag(FrontendHelper.castStringToDate(baumassnahmeForm
						    .getZugVerkehrstag(index)));
						z.setZugbez(baumassnahmeForm.getZugZuggattung(index));
						z.setZugnr(FrontendHelper.castStringToInteger(baumassnahmeForm
						    .getZugZugnr(index)));
						// Abgangsbhf
						bahnhoefe = bahnhofService.findByLangname(baumassnahmeForm
						    .getZugAbgangsbhf(index));
						if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
							bhf = new Bahnhof(baumassnahmeForm.getZugAbgangsbhf(index), null);
							bahnhofService.create(bhf);
						} else {
							bhf = bahnhoefe.get(0);
						}
						z.getRegelweg().setAbgangsbahnhof(bhf);
						// Zielbhf
						bahnhoefe = bahnhofService.findByLangname(baumassnahmeForm
						    .getZugZielbhf(index));
						if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
							bhf = new Bahnhof(baumassnahmeForm.getZugZielbhf(index), null);
							bahnhofService.create(bhf);
						} else {
							bhf = bahnhoefe.get(0);
						}
						z.getRegelweg().setZielbahnhof(bhf);
						// Zugdetails
						Zugdetails zd = z.getZugdetails();
						zd.getTfz().setTfz(baumassnahmeForm.getZugTfz(index));
						zd.getLast().setLast(
						    FrontendHelper.castStringToInteger(baumassnahmeForm.getZugLast(index)));
						zd.setBrems(baumassnahmeForm.getZugMbr(index));
						zd.setLaenge(FrontendHelper.castStringToInteger(baumassnahmeForm
						    .getZugLaenge(index)));
						zd.setVmax(FrontendHelper.castStringToInteger(baumassnahmeForm
						    .getZugVmax(index)));
						z.setKvProfil(baumassnahmeForm.getZugKvProfil(index));
						z.setStreckenKlasse(baumassnahmeForm.getZugStreckenklasse(index));
						z.setBemerkung(baumassnahmeForm.getZugBemerkung(index));
						z.setQs_ks(baumassnahmeForm.getZugQsKs(index));
						z.setRichtung(baumassnahmeForm.getUebZugRichtung(index));

						if (baumassnahmeForm.getZugBearbeitet(index) == null)
							z.getBearbeitungsStatusMap().put(loginUser.getRegionalbereich(), false);
						else
							z.getBearbeitungsStatusMap().put(loginUser.getRegionalbereich(),
							    baumassnahmeForm.getZugBearbeitet(index));
					}
					ueb.refreshZugStatusRbEntry();
					ueb.refreshRegionalbereichBearbeitungsStatus();
				}
				baumassnahme.setUebergabeblatt(ueb);
				uebergabeblattService.update(ueb);
			}
		}
	}

	private void setBbzr(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme,
	    TqmUser secUser) {
		// Nur BBZR-Daten speichern
		BbzrService bbzrService = serviceFactory.createBbzrService();
		int indexOfAktuelleZvf = 0;

		if (baumassnahme.getZvf() != null & baumassnahme.getZvf().size() > 0) {
			Uebergabeblatt aktuelleZvf = baumassnahme.getAktuelleZvf();
			indexOfAktuelleZvf = baumassnahme.getZvf().indexOf(aktuelleZvf);
			Uebergabeblatt bbzr = bbzrService.findById(aktuelleZvf.getId(), new FetchPlan[] {
			        FetchPlan.BBZR_HEADER, FetchPlan.UEB_HEADER_SENDER,
			        FetchPlan.BBZR_BAUMASSNAHMEN, FetchPlan.ZVF_MN_VERSION,
			        FetchPlan.BBZR_MN_ZUEGE, FetchPlan.ZVF_MN_STRECKEN,
			        FetchPlan.ZVF_MN_STRECKE_STRECKEVZG, FetchPlan.ZVF_MN_BBPSTRECKE,
			        FetchPlan.UEB_ZUG_REGELWEG, FetchPlan.UEB_REGIONALBEREICHE });

			UebergabeblattAnyVoter voter = (UebergabeblattAnyVoter) VoterFactory.getDecisionVoter(
			    bbzr.getClass(), VoterType.ANY);
			if (voter.vote(secUser, bbzr, "ROLE_BBZR_BEARBEITEN") == AccessDecisionVoter.ACCESS_GRANTED) {
				// Sender
				bbzr.getHeader().getSender().setName(baumassnahmeForm.getZvfSenderName());
				bbzr.getHeader().getSender().setVorname(baumassnahmeForm.getZvfSenderVorname());
				bbzr.getHeader().getSender().setStrasse(baumassnahmeForm.getZvfSenderStrasse());
				bbzr.getHeader().getSender().setPlz(baumassnahmeForm.getZvfSenderPLZ());
				bbzr.getHeader().getSender().setOrt(baumassnahmeForm.getZvfSenderOrt());
				bbzr.getHeader().getSender().setTelefon(baumassnahmeForm.getZvfSenderTelefon());
				bbzr.getHeader().getSender()
				    .setTelefonIntern(baumassnahmeForm.getZvfSenderTelefonIntern());
				bbzr.getHeader().getSender().setEmail(baumassnahmeForm.getZvfSenderEmail());

				// Massnahmendaten
				try {
					Massnahme m = bbzr.getMassnahmen().iterator().next();
					Integer neueBBPNr;
					// falls noch nicht in zvf enthaltene BBP-Nr eingegeben
					// wurde, dann an
					// erster Stelle einfügen
					if (baumassnahmeForm.getZvfBbpStrecke() != null) {
						neueBBPNr = FrontendHelper.castStringToInteger(baumassnahmeForm
						    .getZvfBbpStrecke());
						if (neueBBPNr != null) {
							BBPStrecke bbpStrecke = bbpStreckeService.findByNummer(neueBBPNr);
							if (bbpStrecke != null & !m.getBbp().contains(bbpStrecke))
								m.getBbp().add(0, bbpStrecke);
						}
					}

					Version v = m.getVersion();
					if (baumassnahmeForm.getZvfFormularkennung() != null) {
						v.setFormular(FormularKennung.castFromString(baumassnahmeForm
						    .getZvfFormularkennung()));
					} else {
						v.setFormular(FormularKennung.ZVF_091008);
					}
					v.setMajor(FrontendHelper.castStringToInteger(baumassnahmeForm
					    .getZvfVersionMajor()));
					v.setMinor(FrontendHelper.castStringToInteger(baumassnahmeForm
					    .getZvfVersionMinor()));
					v.setSub(baumassnahmeForm.getZvfVersionSub());

					try {
						m.setBaumassnahmenart(Art.valueOf(baumassnahmeForm.getZvfBaumassnahmenart()));
					} catch (IllegalArgumentException e) {
						m.setBaumassnahmenart(null);
					}
					m.setKennung(baumassnahmeForm.getZvfKennung());
					m.setKigbau(baumassnahmeForm.getZvfLisbaKigbau());
					m.setQsKsVesNr(baumassnahmeForm.getZvfQsKsVes());
					m.setKorridor(baumassnahmeForm.getZvfKorridor());
					m.setFestgelegtSPFV(baumassnahmeForm.getZvfFestgelegtSPFV());
					m.setFestgelegtSPNV(baumassnahmeForm.getZvfFestgelegtSPNV());
					m.setFestgelegtSGV(baumassnahmeForm.getZvfFestgelegtSGV());

					// Strecken
					List<Strecke> strecken = m.getStrecke();
					Iterator<Strecke> itStrecke = strecken.iterator();
					Strecke s = null;
					String index = null;
					int j = 0;
					while (itStrecke.hasNext()) {
						s = itStrecke.next();
						index = "" + j++;
						Integer neueVzgNr;
						// falls noch nicht in zvf enthaltene Vzg-Nr eingegeben
						// wurde, dann an
						// erster Stelle einfügen
						if (baumassnahmeForm.getZvfStreckeVZG(index) != null) {
							neueVzgNr = FrontendHelper.castStringToInteger(baumassnahmeForm
							    .getZvfStreckeVZG(index));
							if (neueVzgNr != null) {
								VzgStrecke vzgStrecke = vzgService.findByNummer(neueVzgNr, null,
								    null);
								if (vzgStrecke != null & !s.getStreckeVZG().contains(vzgStrecke))
									s.getStreckeVZG().add(0, vzgStrecke);
							}
						}
						s.setMassnahme(baumassnahmeForm.getZvfStreckeMassnahme(index));
						s.setBaubeginn(FrontendHelper.castStringToDate(baumassnahmeForm
						    .getZvfStreckeBaubeginn(index)));
						s.setBauende(FrontendHelper.castStringToDate(baumassnahmeForm
						    .getZvfStreckeBauende(index)));
						s.setZeitraumUnterbrochen(baumassnahmeForm.getZvfStreckeUnterbrochen(index));
						s.setGrund(baumassnahmeForm.getZvfStreckeGrund(index));
						s.setBetriebsweise(baumassnahmeForm.getZvfStreckeBetriebsweise(index));
					}

					// Züge
					if (baumassnahmeForm.getShowZuegeZvf() == true) {
						List<Zug> zuege = m.getZug();
						Zug z = null;
						List<Bahnhof> bahnhoefe = null;
						Bahnhof bhf = null;

						for (int i = 0; i < zuege.size(); i++) {
							z = zuege.get(i);
							index = "" + i;

							z.setVerkehrstag(FrontendHelper.castStringToDate(baumassnahmeForm
							    .getZvfZugVerkehrstag(index)));
							z.setZugbez(baumassnahmeForm.getZvfZugZuggattung(index));
							z.setZugnr(FrontendHelper.castStringToInteger(baumassnahmeForm
							    .getZvfZugZugnr(index)));

							// Abgangsbhf
							bahnhoefe = bahnhofService.findByLangname(baumassnahmeForm
							    .getZvfZugAbgangsbhf(index));
							if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
								bhf = new Bahnhof(baumassnahmeForm.getZvfZugAbgangsbhf(index), null);
								bahnhofService.create(bhf);
							} else {
								bhf = bahnhoefe.get(0);
							}
							z.getRegelweg().setAbgangsbahnhof(bhf);
							// Zielbhf
							bahnhoefe = bahnhofService.findByLangname(baumassnahmeForm
							    .getZvfZugZielbhf(index));
							if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
								bhf = new Bahnhof(baumassnahmeForm.getZvfZugZielbhf(index), null);
								bahnhofService.create(bhf);
							} else {
								bhf = bahnhoefe.get(0);
							}
							z.getRegelweg().setZielbahnhof(bhf);
							z.setRichtung(baumassnahmeForm.getZvfZugRichtung(index));

							Abweichungsart abweichungsArt = z.getAbweichung().getArt();
							Abweichung abweichung = z.getAbweichung();

							if (abweichungsArt == Abweichungsart.UMLEITUNG) {
								z.setTageswechsel(baumassnahmeForm.getZvfZugTageswechsel(index));
								z.setBedarf(baumassnahmeForm.getZvfZugBedarf(index));
								abweichung.setUmleitung(baumassnahmeForm
								    .getZvfZugUmleitungsstrecke(index));
								z.setQs_ks(baumassnahmeForm.getZvfZugQsKs(index));
							}

							if (abweichungsArt == Abweichungsart.UMLEITUNG
							    || abweichungsArt == Abweichungsart.VERSPAETUNG) {
								abweichung.setVerspaetung(FrontendHelper
								    .castStringToInteger(baumassnahmeForm
								        .getZvfZugVerspaetung(index)));
							}

							if (abweichungsArt == Abweichungsart.AUSFALL) {
								bahnhoefe = bahnhofService.findByLangname(baumassnahmeForm
								    .getZvfZugAusfallAb(index));
								if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
									bhf = new Bahnhof(baumassnahmeForm.getZvfZugAusfallAb(index),
									    null);
									bahnhofService.create(bhf);
								} else {
									bhf = bahnhoefe.get(0);
								}
								abweichung.setAusfallvon(bhf);

								bahnhoefe = bahnhofService.findByLangname(baumassnahmeForm
								    .getZvfZugAusfallBis(index));
								if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
									bhf = new Bahnhof(baumassnahmeForm.getZvfZugAusfallBis(index),
									    null);
									bahnhofService.create(bhf);
								} else {
									bhf = bahnhoefe.get(0);
								}
								abweichung.setAusfallbis(bhf);

							}

							if (abweichungsArt == Abweichungsart.VORPLAN) {
								abweichung.setVerspaetung(FrontendHelper
								    .castStringToInteger(baumassnahmeForm
								        .getZvfZugVerspaetung(index)));

								bahnhoefe = bahnhofService.findByLangname(baumassnahmeForm
								    .getZvfZugVorplanAb(index));
								if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
									bhf = new Bahnhof(baumassnahmeForm.getZvfZugVorplanAb(index),
									    null);
									bahnhofService.create(bhf);
								} else {
									bhf = bahnhoefe.get(0);
								}
								abweichung.setVorplanab(bhf);
							}

							if (abweichungsArt == Abweichungsart.ERSATZHALTE) {
								List<String> ausfallHalteStrings = baumassnahmeForm
								    .getZvfZugAusfallVerkehrshalt(index);
								List<String> ersatzHalteStrings = baumassnahmeForm
								    .getZvfZugMoeglicherErsatzhalt(index);

								List<Halt> halte = abweichung.getHalt();
								for (int k = 0; k < halte.size(); k++) {
									bahnhoefe = bahnhofService.findByLangname(ausfallHalteStrings
									    .get(k));
									if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
										if (ausfallHalteStrings.get(k).trim().equals("")) {
											bhf = null;
										} else {
											bhf = new Bahnhof(ausfallHalteStrings.get(k), null);
											bahnhofService.create(bhf);
										}
									} else {
										bhf = bahnhoefe.get(0);
									}
									halte.get(k).setAusfall(bhf);

									bahnhoefe = bahnhofService.findByLangname(ersatzHalteStrings
									    .get(k));
									if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
										if (ersatzHalteStrings.get(k).trim().equals("")) {
											bhf = null;
										} else {
											bhf = new Bahnhof(ersatzHalteStrings.get(k), null);
											bahnhofService.create(bhf);
										}
									} else {
										bhf = bahnhoefe.get(0);
									}
									halte.get(k).setErsatz(bhf);
								}
							}
							if (abweichungsArt == Abweichungsart.REGELUNG) {
								List<String> regelungsartStrings = baumassnahmeForm
								    .getZvfZugRegelungsart(index);
								List<String> giltInStrings = baumassnahmeForm
								    .getZvfZugRegelungGiltin(index);
								List<String> textStrings = baumassnahmeForm
								    .getZvfZugRegelungtext(index);

								List<RegelungAbw> regs = abweichung.getRegelungen();
								for (int k = 0; k < regs.size(); k++) {
									String art = regelungsartStrings.get(k);
									regs.get(k).setArt(art);

									bahnhoefe = bahnhofService.findByLangname(giltInStrings.get(k));
									if (bahnhoefe == null || bahnhoefe.size() == 0) {// speichern
										if (giltInStrings.get(k).trim().equals("")) {
											bhf = null;
										} else {
											bhf = new Bahnhof(giltInStrings.get(k), null);
											bahnhofService.create(bhf);
										}
									} else {
										bhf = bahnhoefe.get(0);
									}
									regs.get(k).setGiltIn(bhf);
									String text = textStrings.get(k);
									regs.get(k).setText(text);
								}
							}
						}
					}
				} catch (NoSuchElementException e) {
				}
				baumassnahme.getZvf().set(indexOfAktuelleZvf, bbzr);
				bbzrService.update(bbzr);
			}
		}
	}

	private Baumassnahme setTermineGueterverkehrsEVU(BaumassnahmeForm baumassnahmeForm,
	    Baumassnahme baumassnahme, Art oldArt, Date oldBaubeginn) {
		// Verknüpfungen zu Schnittstellen aktualisieren
		String linkedIds = baumassnahmeForm.getGevuLinkedIds();
		if (!FrontendHelper.stringNotNullOrEmpty(linkedIds)) {
			// alle EVU wurden gelöscht
			baumassnahme.setGevus(new HashSet<TerminUebersichtGueterverkehrsEVU>());
		} else {
			ArrayList<String> linkedIdsList = CollectionHelper.separatedStringToCollection(
			    new ArrayList<String>(), linkedIds, ",");

			Set<TerminUebersichtGueterverkehrsEVU> removeList = new HashSet<TerminUebersichtGueterverkehrsEVU>();

			for (TerminUebersichtGueterverkehrsEVU gevu : baumassnahme.getGevus()) {
				EVUGruppe evugruppe = gevu.getEvuGruppe();

				// Id in Liste und Baumaßnahme enthalten: keine Aktion
				if (linkedIdsList.contains(evugruppe.getId().toString())) {
				}
				// ID nicht in Liste aber in Baumassnahme enthalten: EVU löschen
				else {
					removeList.add(gevu);
				}

				// Id wurde verarbeitet - aus Liste löschen
				linkedIdsList.remove(evugruppe.getId().toString());
			}

			baumassnahme.getGevus().removeAll(removeList);

			// Die Liste enthält nur noch EVUs die nicht mit der Baumaßnahme
			// verknüpft sind.
			// => neu anlegen
			if (!linkedIdsList.isEmpty()) {
				EVUGruppeService evuGruppeService = EasyServiceFactory.getInstance()
				    .createEVUGruppeService();
				TerminUebersichtGueterverkehrsEVUService gevuService = EasyServiceFactory
				    .getInstance().createTerminUebersichtGueterverkehrsEVUService();

				for (String evuId : linkedIdsList) {
					EVUGruppe grpObj = evuGruppeService.findById(FrontendHelper
					    .castStringToInteger(evuId));

					// nicht existierende EVU übergehen
					if (grpObj == null) {
						continue;
					}

					TerminUebersichtGueterverkehrsEVU t = new TerminUebersichtGueterverkehrsEVU(
					    baumassnahme.getArt());
					t.setEvuGruppe(grpObj);
					// Solltermine berechnen
					t.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
					    baumassnahme.getArt());
					gevuService.create(t);
					baumassnahme.getGevus().add(t);
				}
			}
		}

		// Werte speichern
		Set<TerminUebersichtGueterverkehrsEVU> termineGevu = baumassnahme.getGevus();
		Iterator<TerminUebersichtGueterverkehrsEVU> it = termineGevu.iterator();
		while (it.hasNext()) {
			TerminUebersichtGueterverkehrsEVU termine = it.next();
			String evuId = Integer.toString(termine.getEvuGruppe().getId());

			// Datumswerte übernehmen
			if (baumassnahmeForm.getGevuStudieGrobkonzept(evuId) != null)
				termine.setStudieGrobkonzept(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getGevuStudieGrobkonzept(evuId)));

			if (baumassnahmeForm.getGevuZvfEntwurf(evuId) != null)
				termine.setZvfEntwurf(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getGevuZvfEntwurf(evuId)));

			if (baumassnahmeForm.getGevuStellungnahmeEVU(evuId) != null)
				termine.setStellungnahmeEVU(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getGevuStellungnahmeEVU(evuId)));

			if (baumassnahmeForm.getGevuZvF(evuId) != null)
				termine.setZvF(FrontendHelper.castStringToDate(baumassnahmeForm.getGevuZvF(evuId)));

			if (baumassnahmeForm.getGevuMasterUebergabeblattGV(evuId) != null)
				termine.setMasterUebergabeblattGV(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getGevuMasterUebergabeblattGV(evuId)));

			if (baumassnahmeForm.getGevuUebergabeblattGV(evuId) != null)
				termine.setUebergabeblattGV(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getGevuUebergabeblattGV(evuId)));

			if (baumassnahmeForm.getGevuFplo(evuId) != null)
				termine
				    .setFplo(FrontendHelper.castStringToDate(baumassnahmeForm.getGevuFplo(evuId)));

			if (baumassnahmeForm.getGevuEingabeGFD_Z(evuId) != null)
				termine.setEingabeGFD_Z(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getGevuEingabeGFD_Z(evuId)));

			// Erforderlich-Häkchen übernehmen
			if (!oldArt.equals(baumassnahme.getArt())) {
				// Erforderlichhäkchen neu setzen, wenn Art geändert
				termine.setErforderlichHaekchen(baumassnahme.getArt());

			} else {
				if (baumassnahmeForm.getGevuIsZvFEntwurfErforderlich(evuId) != null)
					termine.setZvfEntwurfErforderlich(baumassnahmeForm
					    .getGevuIsZvFEntwurfErforderlich(evuId));

				if (baumassnahmeForm.getGevuIsStellungnahmeEVUErforderlich(evuId) != null)
					termine.setStellungnahmeEVUErforderlich(baumassnahmeForm
					    .getGevuIsStellungnahmeEVUErforderlich(evuId));

				if (baumassnahmeForm.getGevuIsZvFErforderlich(evuId) != null)
					termine.setZvfErforderlich(baumassnahmeForm.getGevuIsZvFErforderlich(evuId));
				if (baumassnahmeForm.getGevuIsMasterUebergabeblattGVErforderlich(evuId) != null) {
					termine.setMasterUebergabeblattGVErforderlich(baumassnahmeForm
					    .getGevuIsMasterUebergabeblattGVErforderlich(evuId));
				}

				if (baumassnahmeForm.getGevuIsUebergabeblattGVErforderlich(evuId) != null) {
					termine.setUebergabeblattGVErforderlich(baumassnahmeForm
					    .getGevuIsUebergabeblattGVErforderlich(evuId));
				}

				if (baumassnahmeForm.getGevuIsFploErforderlich(evuId) != null)
					termine.setFploErforderlich(baumassnahmeForm.getGevuIsFploErforderlich(evuId));

				if (baumassnahmeForm.getGevuIsEingabeGFD_ZErforderlich(evuId) != null)
					termine.setEingabeGFD_ZErforderlich(baumassnahmeForm
					    .getGevuIsEingabeGFD_ZErforderlich(evuId));
			}

			if (!oldArt.equals(baumassnahme.getArt())
			    || !oldBaubeginn.equals(baumassnahme.getBeginnFuerTerminberechnung())) {
				// Solltermine aktualisieren, wenn Art oder Baubeginn geändert
				termine.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
				    baumassnahme.getArt());
			}
		}
		return baumassnahme;
	}

	private Baumassnahme setTerminePersonenverkehrsEVU(BaumassnahmeForm baumassnahmeForm,
	    Baumassnahme baumassnahme, Art oldArt, Date oldBaubeginn) {
		// Verknüpfungen zu Schnittstellen aktualisieren
		String linkedIds = baumassnahmeForm.getPevuLinkedIds();
		if (!FrontendHelper.stringNotNullOrEmpty(linkedIds)) {
			// alle EVU wurden gelöscht
			baumassnahme.setPevus(new HashSet<TerminUebersichtPersonenverkehrsEVU>());
		} else {
			ArrayList<String> linkedIdsList = CollectionHelper.separatedStringToCollection(
			    new ArrayList<String>(), linkedIds, ",");

			Set<TerminUebersichtPersonenverkehrsEVU> removeList = new HashSet<TerminUebersichtPersonenverkehrsEVU>();

			for (TerminUebersichtPersonenverkehrsEVU pevu : baumassnahme.getPevus()) {
				EVUGruppe evugruppe = pevu.getEvuGruppe();

				// Id in Liste und Baumaßnahme enthalten: keine Aktion
				if (linkedIdsList.contains(evugruppe.getId().toString())) {
				}
				// ID nicht in Liste aber in Baumassnahme enthalten: EVU löschen
				else {
					removeList.add(pevu);
				}

				// Id wurde verarbeitet - aus Liste löschen
				linkedIdsList.remove(evugruppe.getId().toString());
			}

			baumassnahme.getPevus().removeAll(removeList);

			// Die Liste enthält nur noch EVUs die nicht mit der Baumaßnahme
			// verknüpft sind.
			// => neu anlegen
			if (!linkedIdsList.isEmpty()) {
				EVUGruppeService evuGruppeService = EasyServiceFactory.getInstance()
				    .createEVUGruppeService();
				TerminUebersichtPersonenverkehrsEVUService pevuService = EasyServiceFactory
				    .getInstance().createTerminUebersichtPersonenverkehrsEVUService();

				for (String evuId : linkedIdsList) {
					EVUGruppe grpObj = evuGruppeService.findById(FrontendHelper
					    .castStringToInteger(evuId));

					// nicht existierende EVU übergehen
					if (grpObj == null) {
						continue;
					}

					TerminUebersichtPersonenverkehrsEVU t = new TerminUebersichtPersonenverkehrsEVU(
					    baumassnahme.getArt());
					t.setEvuGruppe(grpObj);
					// Solltermine berechnen
					t.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
					    baumassnahme.getArt());
					pevuService.create(t);
					baumassnahme.getPevus().add(t);
				}
			}
		}

		// Eingaben speichern
		Set<TerminUebersichtPersonenverkehrsEVU> terminePevu = baumassnahme.getPevus();
		Iterator<TerminUebersichtPersonenverkehrsEVU> it = terminePevu.iterator();

		while (it.hasNext()) {
			TerminUebersichtPersonenverkehrsEVU termine = it.next();

			String evuId = Integer.toString(termine.getEvuGruppe().getId());

			// Datumswerte übernehmen
			if (baumassnahmeForm.getPevuStudieGrobkonzept(evuId) != null)
				termine.setStudieGrobkonzept(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getPevuStudieGrobkonzept(evuId)));

			if (baumassnahmeForm.getPevuZvfEntwurf(evuId) != null)
				termine.setZvfEntwurf(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getPevuZvfEntwurf(evuId)));

			if (baumassnahmeForm.getPevuStellungnahmeEVU(evuId) != null)
				termine.setStellungnahmeEVU(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getPevuStellungnahmeEVU(evuId)));

			if (baumassnahmeForm.getPevuZvF(evuId) != null)
				termine.setZvF(FrontendHelper.castStringToDate(baumassnahmeForm.getPevuZvF(evuId)));

			if (baumassnahmeForm.getPevuMasterUebergabeblattPV(evuId) != null)
				termine.setMasterUebergabeblattPV(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getPevuMasterUebergabeblattPV(evuId)));

			if (baumassnahmeForm.getPevuUebergabeblattPV(evuId) != null)
				termine.setUebergabeblattPV(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getPevuUebergabeblattPV(evuId)));

			if (baumassnahmeForm.getPevuFplo(evuId) != null)
				termine
				    .setFplo(FrontendHelper.castStringToDate(baumassnahmeForm.getPevuFplo(evuId)));

			if (baumassnahmeForm.getPevuEingabeGFD_Z(evuId) != null)
				termine.setEingabeGFD_Z(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getPevuEingabeGFD_Z(evuId)));

			if (baumassnahmeForm.getPevuAusfaelleSEV(evuId) != null)
				termine.setAusfaelleSEV(baumassnahmeForm.getPevuAusfaelleSEV(evuId));

			if (baumassnahmeForm.getPevuBKonzeptEVU(evuId) != null)
				termine.setBKonzeptEVU(FrontendHelper.castStringToDate(baumassnahmeForm
				    .getPevuBKonzeptEVU(evuId)));

			// Erforderlich-Häkchen übernehmen
			if (!oldArt.equals(baumassnahme.getArt())) {
				// Erforderlichhäkchen neu setzen
				termine.setErforderlichHaekchen(baumassnahme.getArt());

			} else {
				if (baumassnahmeForm.getPevuIsZvfEntwurfErforderlich(evuId) != null)
					termine.setZvfEntwurfErforderlich(baumassnahmeForm
					    .getPevuIsZvfEntwurfErforderlich(evuId));

				if (baumassnahmeForm.getPevuIsStellungnahmeEVUErforderlich(evuId) != null)
					termine.setStellungnahmeEVUErforderlich(baumassnahmeForm
					    .getPevuIsStellungnahmeEVUErforderlich(evuId));

				if (baumassnahmeForm.getPevuIsZvFErforderlich(evuId) != null)
					termine.setZvfErforderlich(baumassnahmeForm.getPevuIsZvFErforderlich(evuId));

				if (baumassnahmeForm.getPevuIsMasterUebergabeblattPVErforderlich(evuId) != null) {
					termine.setMasterUebergabeblattPVErforderlich(baumassnahmeForm
					    .getPevuIsMasterUebergabeblattPVErforderlich(evuId));
				}

				if (baumassnahmeForm.getPevuIsUebergabeblattPVErforderlich(evuId) != null) {
					termine.setUebergabeblattPVErforderlich(baumassnahmeForm
					    .getPevuIsUebergabeblattPVErforderlich(evuId));
				}

				if (baumassnahmeForm.getPevuIsFploErforderlich(evuId) != null)
					termine.setFploErforderlich(baumassnahmeForm.getPevuIsFploErforderlich(evuId));

				if (baumassnahmeForm.getPevuIsEingabeGFD_ZErforderlich(evuId) != null)
					termine.setEingabeGFD_ZErforderlich(baumassnahmeForm
					    .getPevuIsEingabeGFD_ZErforderlich(evuId));

				if (baumassnahmeForm.getPevuIsBKonzeptEVUErforderlich(evuId) != null)
					termine.setBKonzeptEVUErforderlich(baumassnahmeForm
					    .getPevuIsBKonzeptEVUErforderlich(evuId));
			}
			if (!oldArt.equals(baumassnahme.getArt())
			    || !oldBaubeginn.equals(baumassnahme.getBeginnFuerTerminberechnung())) {
				// Solltermine aktualisieren, wenn Art oder Baubeginn geändert
				termine.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
				    baumassnahme.getArt());
			}
		}
		return baumassnahme;
	}

	private void setTermineBBP(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme,
	    Art oldArt, Date oldBaubeginn) {
		TerminUebersichtBaubetriebsplanung terminebbp = baumassnahme.getBaubetriebsplanung();

		// Datumswerte übernehmen
		if (baumassnahmeForm.getBaubetriebsplanung(0) != null)
			terminebbp.setStudieGrobkonzept(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(0)));
		if (baumassnahmeForm.getBaubetriebsplanung(1) != null)
			terminebbp.setAnforderungBBZR(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(1)));
		if (baumassnahmeForm.getBaubetriebsplanung(2) != null)
			terminebbp.setBiUeEntwurf(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(2)));
		if (baumassnahmeForm.getBaubetriebsplanung(3) != null)
			terminebbp.setZvfEntwurf(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(3)));
		if (baumassnahmeForm.getBaubetriebsplanung(4) != null)
			terminebbp.setKoordinationsErgebnis(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(4)));
		if (baumassnahmeForm.getBaubetriebsplanung(5) != null)
			terminebbp.setGesamtKonzeptBBZR(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(5)));
		if (baumassnahmeForm.getBaubetriebsplanung(6) != null)
			terminebbp.setBiUe(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(6)));
		if (baumassnahmeForm.getBaubetriebsplanung(7) != null)
			terminebbp.setZvf(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBaubetriebsplanung(7)));

		// Erforderlich-Häkchen übernehmen
		if (!oldArt.equals(baumassnahme.getArt())) {
			// Erforderlichhäkchen neu setzen
			terminebbp.setErforderlichHaekchen(baumassnahme.getArt());

		} else {
			terminebbp.setBiUeEntwurfErforderlich(baumassnahmeForm
			    .getBaubetriebsplanungErforderlich(2));

			terminebbp.setZvfEntwurfErforderlich(baumassnahmeForm
			    .getBaubetriebsplanungErforderlich(3));

			terminebbp.setKoordinationsergebnisErforderlich(baumassnahmeForm
			    .getBaubetriebsplanungErforderlich(4));

			terminebbp.setGesamtkonzeptBBZRErforderlich(baumassnahmeForm
			    .getBaubetriebsplanungErforderlich(5));

			terminebbp.setBiUeErforderlich(baumassnahmeForm.getBaubetriebsplanungErforderlich(6));

			terminebbp.setZvFErforderlich(baumassnahmeForm.getBaubetriebsplanungErforderlich(7));
		}

		if (!oldArt.equals(baumassnahme.getArt())
		    || !oldBaubeginn.equals(baumassnahme.getBeginnFuerTerminberechnung())) {
			// Solltermine aktualisieren, wenn Art oder Baubeginn geändert
			terminebbp.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
			    baumassnahme.getArt());
		}
	}

	private void setAbstimmungNachbarbahn(BaumassnahmeForm baumassnahmeForm,
	    Baumassnahme baumassnahme) {
		baumassnahme.setAbstimmungNbErforderlich(baumassnahmeForm.isAbstimmungNbErforderlich());
		if (baumassnahmeForm.getNachbarbahn() != null) {
			NachbarbahnService nachbarbahnService = serviceFactory.createNachbarbahnService();
			Nachbarbahn nb = nachbarbahnService.findById(baumassnahmeForm.getNachbarbahn());
			baumassnahme.setNachbarbahn(nb);
		}
		if (baumassnahmeForm.getAbstimmungNbErfolgtAm() != null)
			baumassnahme.setAbstimmungNbErfolgtAm(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getAbstimmungNbErfolgtAm()));
	}

	private void setVerzichtQTrasse(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme) {
		if (baumassnahmeForm.getVerzichtQTrasseBeantragt() != null)
			baumassnahme.setVerzichtQTrasseBeantragt(FrontendHelper
			    .castStringToDate(baumassnahmeForm.getVerzichtQTrasseBeantragt()));
		if (baumassnahmeForm.getVerzichtQTrasseAbgestimmt() != null)
			baumassnahme.setVerzichtQTrasseAbgestimmt(FrontendHelper
			    .castStringToDate(baumassnahmeForm.getVerzichtQTrasseAbgestimmt()));
		if (baumassnahmeForm.getVerzichtQTrasseGenehmigt() != null)
			baumassnahme
			    .setVerzichtQTrasseGenehmigt(baumassnahmeForm.getVerzichtQTrasseGenehmigt());
	}

	private void setKommentar(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme) {
		if (baumassnahmeForm.getKommentar() != null)
			baumassnahme.setKommentar(baumassnahmeForm.getKommentar());
	}

	private void setAufwand(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme) {
		if (baumassnahmeForm.getAufwandZvF() != null) {
			Integer i = FrontendHelper.castStringToInteger(baumassnahmeForm.getAufwandZvF());
			if (i == null)
				baumassnahme.setAufwandZvF(0);
			else
				baumassnahme.setAufwandZvF(i);
		}
		if (baumassnahmeForm.getAufwandBiUeb() != null) {
			Integer i = FrontendHelper.castStringToInteger(baumassnahmeForm.getAufwandBiUeb());
			if (i == null)
				baumassnahme.setAufwandBiUeb(0);
			else
				baumassnahme.setAufwandBiUeb(i);
		}
		if (baumassnahmeForm.getAufwandUeb() != null) {
			Integer i = FrontendHelper.castStringToInteger(baumassnahmeForm.getAufwandUeb());
			if (i == null)
				baumassnahme.setAufwandUeb(0);
			else
				baumassnahme.setAufwandUeb(i);
		}
		if (baumassnahmeForm.getAufwandFplo() != null) {
			Integer i = FrontendHelper.castStringToInteger(baumassnahmeForm.getAufwandFplo());
			if (i == null)
				baumassnahme.setAufwandFplo(0);
			else
				baumassnahme.setAufwandFplo(i);
		}
	}

	private void setEskalationAusfall(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme) {
		if (baumassnahmeForm.getEskalationsBeginn() != null)
			baumassnahme.setEskalationsBeginn(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getEskalationsBeginn()));
		if (baumassnahmeForm.getEskalationsEntscheidung() != null)
			baumassnahme.setEskalationsEntscheidung(FrontendHelper
			    .castStringToDate(baumassnahmeForm.getEskalationsEntscheidung()));
		baumassnahme.setEskalationVeto(baumassnahmeForm.isEskalationVeto());
		if (baumassnahmeForm.getAusfallDatum() != null)
			baumassnahme.setAusfallDatum(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getAusfallDatum()));
		if (baumassnahmeForm.getAusfallGrund() != null) {
			Grund grund = grundService.findById(baumassnahmeForm.getAusfallGrund());
			baumassnahme.setAusfallGrund(grund);
		}
		if (baumassnahmeForm.getBisherigerAufwand() != null) {
			// Integer i =
			// FrontendHelper.castStringToInteger(baumassnahmeForm.getBisherigerAufwand());
			// if (i == null)
			// baumassnahme.setBisherigerAufwand(0);
			// else
			// baumassnahme.setBisherigerAufwand(i);
			baumassnahme.setBisherigerAufwandTimeString(baumassnahmeForm.getBisherigerAufwand());
		}
	}

	private void setStatusAktivitaeten(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme) {
		if (baumassnahmeForm.getKonstruktionsEinschraenkung() != null)
			baumassnahme.setKonstruktionsEinschraenkung(FrontendHelper
			    .castStringToDate(baumassnahmeForm.getKonstruktionsEinschraenkung()));
		if (baumassnahmeForm.getAbstimmungFfz() != null)
			baumassnahme.setAbstimmungFfz(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getAbstimmungFfz()));
		if (baumassnahmeForm.getAntragAufhebungDienstruhe() != null)
			baumassnahme.setAntragAufhebungDienstruhe(FrontendHelper
			    .castStringToDate(baumassnahmeForm.getAntragAufhebungDienstruhe()));
		if (baumassnahmeForm.getGenehmigungAufhebungDienstruhe() != null)
			baumassnahme.setGenehmigungAufhebungDienstruhe(FrontendHelper
			    .castStringToDate(baumassnahmeForm.getGenehmigungAufhebungDienstruhe()));
		if (baumassnahmeForm.getBiUeNr() != null)
			baumassnahme.setBiUeNr(baumassnahmeForm.getBiUeNr());
		if (baumassnahmeForm.getBetraNr() != null) {
			baumassnahme.setBetraNr(baumassnahmeForm.getBetraNr());
		}
	}

	private void setJbbKsQs(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme) {
		if (baumassnahmeForm.getKigBau() != null & baumassnahmeForm.getKigBau() == true) {
			String nummern = baumassnahmeForm.getKigBauNr();
			LinkedHashSet<String> numSet = new LinkedHashSet<String>();
			try {
				Scanner scanner = new Scanner(nummern);
				scanner.useDelimiter(",");
				while (scanner.hasNext())
					numSet.add(scanner.next().trim());
			} catch (NullPointerException e) {
			}
			baumassnahme.setKigBauNr(numSet);
		}

		if (baumassnahmeForm.getArt() != null && baumassnahmeForm.getArt().equals(Art.KS.name())) {
			if (baumassnahmeForm.getKorridorNr() != null
			    && baumassnahmeForm.getKorridorNr().length() > 0)
				baumassnahme.setKorridorNr(Integer.parseInt(baumassnahmeForm.getKorridorNr()));
			String nummern = baumassnahmeForm.getKorridorZeitfenster();
			HashSet<String> numSet = new HashSet<String>();
			try {
				CollectionHelper.separatedStringToCollection(numSet, nummern, ",");
			} catch (NullPointerException e) {
			}
			baumassnahme.setKorridorZeitfenster(numSet);
		}

		if (baumassnahmeForm.getArt().equals(Art.QS.name())) {
			if (baumassnahmeForm.getQsNr() != null && baumassnahmeForm.getQsNr().length() > 0) {
				String qsNr = baumassnahmeForm.getQsNr();
				ArrayList<String> qsList = CollectionHelper.separatedStringToCollection(
				    new ArrayList<String>(), qsNr, ",");

				for (String s : qsList) {
					if (s.toLowerCase().indexOf("q") != 0) {
						throw new IllegalArgumentException("QS-Nummer muss mit 'Q' beginnen: " + s);
					}
				}

				Iterator<String> it = qsList.iterator();
				LinkedHashSet<String> qsSet = new LinkedHashSet<String>();
				while (it.hasNext()) {
					qsSet.add(it.next());
				}
				List<String> result = new ArrayList<String>(qsSet);
				baumassnahme.setQsNr(result);
			} else
				baumassnahme.setQsNr(null);
			baumassnahme.setQsSGV(baumassnahmeForm.isQsSGV());
			baumassnahme.setQsSPFV(baumassnahmeForm.isQsSPFV());
			baumassnahme.setQsSPNV(baumassnahmeForm.isQsSPNV());
		} else {
			baumassnahme.setQsNr(null);
			baumassnahme.setQsSGV(false);
			baumassnahme.setQsSPFV(false);
			baumassnahme.setQsSPNV(false);
		}
	}

	private void setStammdaten(Baumassnahme baumassnahme, BaumassnahmeForm baumassnahmeForm) {
		try {
			// oldArt = baumassnahme.getArt();
			baumassnahme.setArt(Art.valueOf(baumassnahmeForm.getArt()));
		} catch (Exception e) {
			// baumassnahme.setArt(Art.KS);
		}
		if (baumassnahmeForm.getKigBau() != null)
			baumassnahme.setKigBau(baumassnahmeForm.getKigBau());
		if (baumassnahmeForm.getStreckeBBP() != null)
			baumassnahme.setStreckeBBP(baumassnahmeForm.getStreckeBBP());
		if (baumassnahmeForm.getStreckeVZG() != null)
			baumassnahme.setStreckeVZG(baumassnahmeForm.getStreckeVZG());
		if (baumassnahmeForm.getStreckenAbschnitt() != null)
			baumassnahme.setStreckenAbschnitt(baumassnahmeForm.getStreckenAbschnitt());
		if (baumassnahmeForm.getArtDerMassnahme() != null)
			baumassnahme.setArtDerMassnahme(baumassnahmeForm.getArtDerMassnahme());
		if (baumassnahmeForm.getBetriebsweise() != null)
			baumassnahme.setBetriebsweise(baumassnahmeForm.getBetriebsweise());

		if (baumassnahmeForm.getBeginnFuerTerminberechnung() != null)
			// Soll-Termine neu berechnen
			baumassnahme.setBeginnFuerTerminberechnung(FrontendHelper
			    .castStringToDate(baumassnahmeForm.getBeginnFuerTerminberechnung()));

		if (baumassnahmeForm.getBeginnDatum() != null)
			baumassnahme.setBeginnDatum(FrontendHelper.castStringToDate(baumassnahmeForm
			    .getBeginnDatum()));
		if (baumassnahmeForm.getEndDatum() != null)
			baumassnahme
			    .setEndDatum(FrontendHelper.castStringToDate(baumassnahmeForm.getEndDatum()));

		if (baumassnahmeForm.getRegionalbereichBM() != null) {
			baumassnahme.setRegionalbereichBM(baumassnahmeForm.getRegionalbereichBM());
		}

		if (baumassnahmeForm.getRegionalBereichFpl() != null) {
			Regionalbereich regionalbereichFpl = regionalbereichService.findById(baumassnahmeForm
			    .getRegionalBereichFpl());
			baumassnahme.setRegionalBereichFpl(regionalbereichFpl);
		}

		if (baumassnahmeForm.getBearbeitungsbereich() != null) {
			BearbeitungsbereichService bearbeitungsbereichService = serviceFactory
			    .createBearbeitungsbereichService();
			Bearbeitungsbereich bearbeitungsbereich = bearbeitungsbereichService
			    .findById(baumassnahmeForm.getBearbeitungsbereich());
			baumassnahme.setBearbeitungsbereich(bearbeitungsbereich);
		}

		if (baumassnahmeForm.getFploNr() != null)
			baumassnahme.setFploNr(baumassnahmeForm.getFploNr());
		try {
			baumassnahme.setPrioritaet(Prioritaet.valueOf(baumassnahmeForm.getPrioritaet()));
		} catch (IllegalArgumentException e) {
			baumassnahme.setPrioritaet(Prioritaet.EINS);
		}
		// return oldArt;
	}

	private void setBearbeiter(BaumassnahmeForm baumassnahmeForm, Baumassnahme baumassnahme) {
		List<Bearbeiter> bearbeiterList = baumassnahme.getBearbeiter();
		Map<String, Boolean> bearbeiterAktivList = baumassnahmeForm.getBearbeiter();
		Boolean value = false;
		for (Bearbeiter b : bearbeiterList) {
			Object v = bearbeiterAktivList.get(b.getId().toString());
			value = Boolean.valueOf((String) v);
			b.setAktiv(value);
		}
	}
}
