package db.training.bob.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Benchmark;
import db.training.bob.model.EVU;
import db.training.bob.model.EVUGruppe;
import db.training.bob.model.Regelung;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.TerminUebersichtBaubetriebsplanung;
import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;
import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;
import db.training.bob.model.zvf.Bahnhof;
import db.training.bob.model.zvf.FileType;
import db.training.bob.model.zvf.Halt;
import db.training.bob.model.zvf.Header;
import db.training.bob.model.zvf.KID;
import db.training.bob.model.zvf.Knotenzeit;
import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.RegelungAbw;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.Haltart;
import db.training.bob.service.zvf.BahnhofService;
import db.training.easy.core.service.EasyServiceFactory;

public class ZvfImporter {

	private Baumassnahme baumassnahme = null;

	private Uebergabeblatt importFile = null;

	private Uebergabeblatt oldVersion = null;

	private Uebergabeblatt newVersion = null;

	private BahnhofService bhfService = null;

	private Map<String, Bahnhof> bahnhofCache = null;

	long addevutobm = 0;

	public ZvfImporter() {
		bhfService = EasyServiceFactory.getInstance().createBahnhofService();
	}

	public void setBaumassnahme(Baumassnahme baumassnahme) {
		this.baumassnahme = baumassnahme;
	}

	public Baumassnahme getBaumassnahme() {
		return baumassnahme;
	}

	public void setImportFile(Uebergabeblatt importFile) {
		this.importFile = importFile;
	}

	public Uebergabeblatt getImportFile() {
		return importFile;
	}

	public Uebergabeblatt getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(Uebergabeblatt oldVersion) {
		this.oldVersion = oldVersion;
	}

	public void setNewVersion(Uebergabeblatt newVersion) {
		this.newVersion = newVersion;
	}

	public Uebergabeblatt getNewVersion() {
		return newVersion;
	}

	public void setBhfService(BahnhofService bhfService) {
		this.bhfService = bhfService;
	}

	public FileType getImportFileType() {
		if (importFile == null) {
			return null;
		}
		if (importFile.getMassnahmen().iterator().next().getVersion()
				.getFormular().toString().startsWith("ZVF")) {
			return FileType.ZVF;
		}
		return FileType.UEB;
	}

	/**
	 * Übernimmt einige Daten der Baumassnahme in die importierte ZvF
	 */
	public void copyDataFromBaumassnahmeToZvfFile() {
		Massnahme m = newVersion.getMassnahmen().iterator().next();
		for (BBPMassnahme bbp : baumassnahme.getBbpMassnahmen()) {
			for (Regelung r : bbp.getRegelungen()) {
				if (r.getBeginnFuerTerminberechnung()) {
					m.setKigbau(r.getLisbaNr());
					m.setKennung(bbp.getMasId());
				}
			}
			if (m.getKigbau() != null && m.getKigbau().length() > 255)
				m.setKigbau("");
		}

		m.setVorgangsNr(baumassnahme.getVorgangsNr());
		if (m.getQsKsVesNr() == null) {
			List<String> qsNr = new ArrayList<String>(baumassnahme.getQsNr());
			if (!qsNr.isEmpty()) {
				m.setQsKsVesNr(qsNr.get(0));
			}
		}
	}

	/**
	 * Kopiert alle ausgewählten Züge aus der importierten Datei
	 * 
	 * @param importZugIdList
	 *            Liste der ausgewählten Züge
	 * @return Liste der zu den Zügen gehörenden EVU-Kunden
	 */
	public Set<EVU> copyZuegeFromImportFileToZvfFile(
			List<Integer> importZugIdList) {
		Date beginn = baumassnahme.getBeginnDatum();
		Date ende = baumassnahme.getEndDatum();
		Massnahme m = importFile.getMassnahmen().iterator().next();
		List<Zug> zuegeImportFile = m.getZug();

		// Cache für bereits aus der Datenbank gelesene Bahnhöfe (vermeidet
		// unnötige Selects, da
		// Bahnhöfe in ZvF/ÜB häufig mehrfach vorkommen)
		bahnhofCache = new HashMap<String, Bahnhof>();

		if (importZugIdList == null)
			return null;

		List<Zug> zuege = null;
		if (oldVersion == null)
			zuege = new ArrayList<Zug>();
		else
			zuege = oldVersion.getMassnahmen().iterator().next().getZug();

		// Bahnhöfe speichern (nur importierte Züge)
		boolean error = false;
		int i = 0;
		EVUService evuService = EasyServiceFactory.getInstance()
				.createEVUService();
		Set<EVU> evuNrsOfSelectedZuege = new HashSet<EVU>();
		for (Zug z : zuegeImportFile) {
			// nur ausgewählte Züge
			if (importZugIdList.contains(Integer.valueOf(i++))) {
				EVU evu = evuService.findByKundenNr(z.getBetreiber());
				if (evu != null) {
					/*
					 * evu ist null, wenn Kundennr nicht in Stammdaten
					 * hinterlegt oder falsche kundennr im xml
					 */
					evuNrsOfSelectedZuege.add(evu);
				}
				zuege.add(z);
				Bahnhof abgangsbhf = z.getRegelweg().getAbgangsbahnhof();
				z.getRegelweg().setAbgangsbahnhof(
						findBahnhof(abgangsbhf.getLangName(),
								abgangsbhf.getDs100()));

				Bahnhof zielbahnhof = z.getRegelweg().getZielbahnhof();
				z.getRegelweg().setZielbahnhof(
						findBahnhof(zielbahnhof.getLangName(),
								zielbahnhof.getDs100()));

				Bahnhof ausfallVon = z.getAbweichung().getAusfallvon();
				if (ausfallVon != null) {
					z.getAbweichung().setAusfallvon(
							findBahnhof(ausfallVon.getLangName(),
									ausfallVon.getDs100()));
				}

				Bahnhof ausfallBis = z.getAbweichung().getAusfallbis();
				if (ausfallBis != null) {
					z.getAbweichung().setAusfallbis(
							findBahnhof(ausfallBis.getLangName(),
									ausfallBis.getDs100()));
				}

				Bahnhof vorplanAb = z.getAbweichung().getVorplanab();
				if (vorplanAb != null) {
					z.getAbweichung().setVorplanab(
							findBahnhof(vorplanAb.getLangName(),
									vorplanAb.getDs100()));
				}

				List<Halt> haltliste = z.getAbweichung().getHalt();
				for (Halt h : haltliste) {
					Bahnhof ausfall = h.getAusfall();
					if (ausfall != null) {
						h.setAusfall(findBahnhof(ausfall.getLangName(),
								ausfall.getDs100()));
					}
					Bahnhof ersatz = h.getErsatz();
					if (ersatz != null) {
						h.setErsatz(findBahnhof(ersatz.getLangName(),
								ersatz.getDs100()));
					}
				}
				List<RegelungAbw> regelungenliste = z.getAbweichung()
						.getRegelungen();
				for (RegelungAbw reg : regelungenliste) {
					Bahnhof giltIn = reg.getGiltIn();
					if (giltIn != null) {
						reg.setGiltIn(findBahnhof(giltIn.getLangName(),
								giltIn.getDs100()));
					}
				}

				if (z.getVerkehrstag().before(beginn)
						|| (z.getVerkehrstag().after(ende))) {
					if (error == false) {
						// addMessage("error.uebergabeblatt.zug.invalid");
						error = true;
					}
				}
				try {
					String tfz = z.getZugdetails().getTfz().getTfz();
					if (tfz.length() > 5)
						z.getZugdetails().getTfz().setTfz(tfz.substring(0, 4));
				} catch (Exception e) {
				}
				if (z.getKnotenzeiten().size() == 1) {// wenn EINE leere
														// Knotenzeit im XML,
														// dann
					// wird von JAXB trotzdem ein Objekt erzeugt.
					// Dieses wird hier wieder gelöscht. Ideen zur
					// Vermeidung der Objekterzeugung willkommen
					Knotenzeit kn = z.getKnotenzeiten().get(0);
					if (kn.getAbfahrt() == null & kn.getAnkunft() == null
							& kn.getBahnhof().equals("")
							& kn.getRelativlage().equals(Integer.valueOf(0))
							& kn.getHaltart().equals(Haltart.LEER))
						z.setKnotenzeiten(null);
				}
			}
		}

		// if (error == false)
		// if (getImportFileType() == FileType.ZVF)
		// // addMessage("success.zvf.import");
		// ;
		// else
		// addMessage("success.uebergabeblatt.import");
		newVersion.getMassnahmen().iterator().next().setZug(zuege);
		return evuNrsOfSelectedZuege;
	}

	private Bahnhof findBahnhof(String langName, String ds100) {
		Bahnhof bhf = null;
		if (ds100 != null) {// über ds100 suchen
			bhf = bahnhofCache.get(ds100);
			if (bhf != null) {// ds100, cache gefunden
				bahnhofCache.put(ds100, bhf);
				return bhf;
			}
			bhf = bhfService.findByDs100(ds100);
			if (bhf != null) {// ds100, db, gefunden
				bahnhofCache.put(ds100, bhf);
				return bhf;
			}
		}
		if (langName != null) { // kein Bahnhof mit ds100, Suche über Langname
			bhf = bahnhofCache.get(langName);
			if (bhf != null) {// langname, cache gefunden
				bahnhofCache.put(langName, bhf);
				return bhf;
			}
			List<Bahnhof> list = null;
			list = bhfService.findByLangname(langName);
			if (list != null) {// langname, db gefunden
				if (list.size() > 0) {
					bhf = list.get(0);
					bahnhofCache.put(langName, bhf);
					return bhf;
				}
			}
		}
		// nichts gefunden, ggf neu anlegen
		if (ds100 != null || langName != null) {
			bhf = new Bahnhof(langName, ds100);
			bhfService.create(bhf);
			bahnhofCache.put(langName, bhf);
			return bhf;
		}
		// langname und ds100 = null
		return null;
	}

	/**
	 * Liefert die EVU-Objekte für eine Liste von EVU-Kundennummern(Betreiber)
	 * 
	 * @param kunden
	 *            Liste der EVU-Kundennummern
	 * @return Liste der EVU-Objekte
	 */
	public Set<EVUGruppe> getEVUGruppeObjects(Set<EVU> kunden) {
		Set<EVUGruppe> evuInZvfFile = new HashSet<EVUGruppe>();
		for (EVU evu : kunden)
			evuInZvfFile.add(evu.getEvugruppe());

		return evuInZvfFile;
	}

	private EVUGruppe getEVUGruppeObject(String kundennr) {
		EVUGruppeService service = EasyServiceFactory.getInstance()
				.createEVUGruppeService();
		EVUGruppe grp = service.findByKundenNr(kundennr);
		return grp;
	}

	/**
	 * Fügt EVUs zur jeweils ausgewählten Schnittstelle der Baumassnahme hinzu,
	 * sofern Züge des EVU für den Import ausgewählt wurden
	 * 
	 * @param gevus
	 *            alle G-EVU der importierten Datei
	 * @param pevus
	 *            alle G-EVU der importierten Datei
	 * @param evuOfSelectedZuege
	 *            alle EVU aus der importierten Datei, von denen Züge ausgewählt
	 *            wurden
	 */
	public void addEVUtoBaumassnahme(List<EVUGruppe> gevus,
			List<EVUGruppe> pevus, Set<EVUGruppe> evuNrsOfSelectedZuege) {
		if (gevus != null) {
			List<EVUGruppe> tempList = new ArrayList<EVUGruppe>(gevus);
			for (EVUGruppe evu : gevus) {
				if (!evuNrsOfSelectedZuege.contains(evu)) {
					tempList.remove(evu);
				}
			}
			// G-EVU speichern
			TerminUebersichtGueterverkehrsEVUService termineGEVUService = EasyServiceFactory
					.getInstance()
					.createTerminUebersichtGueterverkehrsEVUService();
			for (EVUGruppe evu : tempList) {
				TerminUebersichtGueterverkehrsEVU t = new TerminUebersichtGueterverkehrsEVU(
						baumassnahme.getArt());
				t.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
						baumassnahme.getArt());
				t.setEvuGruppe(evu);
				t.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
						baumassnahme.getArt());
				termineGEVUService.create(t);
				baumassnahme.getGevus().add(t);
			}
		}
		if (pevus != null) {
			List<EVUGruppe> tempList = new ArrayList<EVUGruppe>(pevus);
			for (EVUGruppe evu : pevus) {
				if (!evuNrsOfSelectedZuege.contains(evu)) {
					tempList.remove(evu);
				}
			}
			// P-EVU speichern
			TerminUebersichtPersonenverkehrsEVUService terminePEVUService = EasyServiceFactory
					.getInstance()
					.createTerminUebersichtPersonenverkehrsEVUService();
			for (EVUGruppe evu : tempList) {
				TerminUebersichtPersonenverkehrsEVU t = new TerminUebersichtPersonenverkehrsEVU(
						baumassnahme.getArt());
				t.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
						baumassnahme.getArt());
				t.setEvuGruppe(evu);
				t.setSollTermine(baumassnahme.getBeginnFuerTerminberechnung(),
						baumassnahme.getArt());
				terminePEVUService.create(t);
				baumassnahme.getPevus().add(t);
			}
		}
	}

	/**
	 * Füllt den Meilenstein ZvF-Vorentwurf, ZvF-Entwurf oder ZvF mit dem Wert
	 * aus <timestamp> für alle importierten EVU
	 * 
	 * @param evuInZvfFile
	 *            Liste der in der importierten ZvF enthaltenen EVU
	 * @param zvfMeilenstein
	 */
	public void setMeilensteinZvF(Set<EVUGruppe> evuInZvfFile,
			Integer zvfMeilenstein) {
		Date timeStamp = importFile.getHeader().getTimestamp();
		if (zvfMeilenstein != null)
			switch (zvfMeilenstein) {
			case 1: {// ZvF-Vorentwurf
				if (baumassnahme.getBaubetriebsplanung().getBiUeEntwurf() == null) {
					TerminUebersichtBaubetriebsplanung bbp = baumassnahme
							.getBaubetriebsplanung();
					bbp.setBiUeEntwurf(timeStamp);
					bbp.setBiUeEntwurfErforderlich(true);
				}
				break;
			}
			case 2: {// ZvF-Entwurf
				if (baumassnahme.getBaubetriebsplanung().getZvfEntwurf() == null) {
					TerminUebersichtBaubetriebsplanung bbp = baumassnahme
							.getBaubetriebsplanung();
					bbp.setZvfEntwurf(timeStamp);
					bbp.setZvfEntwurfErforderlich(true);
				}
				Set<TerminUebersichtGueterverkehrsEVU> termineGEVUs = baumassnahme
						.getGevus();
				for (TerminUebersichtGueterverkehrsEVU termineGEVU : termineGEVUs) {
					if (termineGEVU.getZvfEntwurf() == null) {
						if (evuInZvfFile.contains(termineGEVU.getEvuGruppe())) {
							termineGEVU.setZvfEntwurf(timeStamp);
							termineGEVU.setZvfEntwurfErforderlich(true);
						}
					}
				}
				Set<TerminUebersichtPersonenverkehrsEVU> terminePEVUs = baumassnahme
						.getPevus();
				for (TerminUebersichtPersonenverkehrsEVU terminePEVU : terminePEVUs) {
					if (terminePEVU.getZvfEntwurf() == null) {
						if (evuInZvfFile.contains(terminePEVU.getEvuGruppe())) {
							terminePEVU.setZvfEntwurf(timeStamp);
							terminePEVU.setZvfEntwurfErforderlich(true);
						}
					}
				}
				break;
			}
			case 6: { // ZvF
				if (baumassnahme.getBaubetriebsplanung().getZvf() == null) {
					TerminUebersichtBaubetriebsplanung bbp = baumassnahme
							.getBaubetriebsplanung();
					bbp.setZvf(timeStamp);
					bbp.setZvFErforderlich(true);
				}
				Set<TerminUebersichtGueterverkehrsEVU> termineGEVUs = baumassnahme
						.getGevus();
				for (TerminUebersichtGueterverkehrsEVU termineGEVU : termineGEVUs) {
					if (termineGEVU.getZvF() == null) {
						if (evuInZvfFile.contains(termineGEVU.getEvuGruppe())) {
							termineGEVU.setZvF(timeStamp);
							termineGEVU.setZvfErforderlich(true);
						}
					}
				}
				Set<TerminUebersichtPersonenverkehrsEVU> terminePEVUs = baumassnahme
						.getPevus();
				for (TerminUebersichtPersonenverkehrsEVU terminePEVU : terminePEVUs) {
					if (terminePEVU.getZvF() == null) {
						if (evuInZvfFile.contains(terminePEVU.getEvuGruppe())) {
							terminePEVU.setZvF(timeStamp);
							terminePEVU.setZvfErforderlich(true);
						}
					}
				}
				break;
			}

			}
	}

	/**
	 * Übernimmt Benchmarkwerte für "geregelte Trassen" (<kid1>),
	 * "überarbeitete Trassen" (<kid4>), "veröff. Trassen ZvF" (<kid2>) und
	 * "erstellte BiÜ" (<kid3>) aus der importierten Datei
	 */
	public void setBenchmarks() {
		Map<Regionalbereich, Benchmark> arbeitsleistungen = baumassnahme
				.getBenchmark();
		Set<Entry<Regionalbereich, Benchmark>> entries = arbeitsleistungen
				.entrySet();
		Iterator<Entry<Regionalbereich, Benchmark>> it = entries.iterator();

		Massnahme m = importFile.getMassnahmen().iterator().next();
		Integer anzahlGeregelterTrassen = null;
		if (m.getKID1() != null) {
			anzahlGeregelterTrassen = m.getKID1().getAnzahl();
		}
		Integer anzahlUeberarbeiteterTrassen = 0;
		List<KID> kid4 = m.getkID4();
		for (KID k : kid4) {
			anzahlUeberarbeiteterTrassen += k.getAnzahl();
		}

		Integer anzahlVeroeffentlichterTrassenZvF = m.getKID2();
		Integer anzahlErstellterBiUe = m.getKID3();
		while (it.hasNext()) {
			Entry<Regionalbereich, Benchmark> e = it.next();
			String rb = e.getKey().getName();
			Benchmark value = e.getValue();

			if (rb.equals(baumassnahme.getRegionalBereichFpl().getName())) {
				value.setGeregelteTrassenBiUeE(anzahlGeregelterTrassen);
				value.setUeberarbeiteteTrassenBiUe(anzahlUeberarbeiteterTrassen);
				value.setErstellteBiUe(anzahlErstellterBiUe);
				value.setVeroeffentlichteTrassenZvF(anzahlVeroeffentlichterTrassenZvF);
			}
		}
	}

	/**
	 * Setzt den Meilenstein Master-ÜB=<timestamp> für alle importierten EVU,
	 * wenn endstueck true
	 * 
	 * @param evuInZvfFile
	 *            Liste der in der importierten ZvF enthaltenen EVU
	 */
	public void setMeilensteinMasterUeb(Set<EVUGruppe> evuInZvfFile) {
		Date timeStamp = importFile.getHeader().getTimestamp();

		Set<TerminUebersichtGueterverkehrsEVU> termineGEVUs = baumassnahme
				.getGevus();
		for (TerminUebersichtGueterverkehrsEVU termineGEVU : termineGEVUs) {
			if (evuInZvfFile.contains(termineGEVU.getEvuGruppe())) {
				if (termineGEVU.getMasterUebergabeblattGV() == null) {
					termineGEVU.setMasterUebergabeblattGV(timeStamp);
					termineGEVU.setMasterUebergabeblattGVErforderlich(true);
				}
			}
		}

		Set<TerminUebersichtPersonenverkehrsEVU> terminePEVUs = baumassnahme
				.getPevus();
		for (TerminUebersichtPersonenverkehrsEVU terminePEVU : terminePEVUs) {
			if (evuInZvfFile.contains(terminePEVU.getEvuGruppe())) {
				if (terminePEVU.getMasterUebergabeblattPV() == null) {
					terminePEVU.setMasterUebergabeblattPV(timeStamp);
					terminePEVU.setMasterUebergabeblattPVErforderlich(true);
				}
			}
		}
	}

	/**
	 * Prüft, ob die importierte ZvF eine neuere Versionsnummer hat als die
	 * bereits in der Baumassnahme enthaltene ZvF
	 * 
	 * @return true, wenn eine neuere Version importiert werden soll, sonst
	 *         false
	 */
	public boolean isNewerVersion() {
		Uebergabeblatt aktuell = baumassnahme.getAktuelleZvf();
		if (aktuell.getVersion().compareTo(importFile.getVersion()) >= 0) {
			return false;
		}
		return true;
	}

	/**
	 * Kopiert Daten aus Header und Massnahme der importierten Datei in
	 * bestehende ZvF
	 */
	public void copyHeaderAndMassnahmeFromImportFileToZvfFile() {
		Header oldHeader = newVersion.getHeader();
		Massnahme oldMassnahme = newVersion.getMassnahmen().iterator().next();
		Massnahme newMassnahme = importFile.getMassnahmen().iterator().next();

		oldMassnahme.importMassnahme(newMassnahme);
		oldHeader.importHeader(importFile.getHeader());
	}
}
