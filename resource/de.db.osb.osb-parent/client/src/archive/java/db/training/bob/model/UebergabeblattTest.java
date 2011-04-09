package db.training.bob.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Niederlassung;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.helper.RegionalbereichAdapter;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.service.RegionalbereichServiceImpl;

public class UebergabeblattTest {

	private Uebergabeblatt ueb;

	private String rb1 = "Nord";

	private String rb2 = "Süd";

	@Before
	public void setUp() throws Exception {
		ueb = new Uebergabeblatt();
		initializeNiederlassungen();
		initializeZuege();
		ueb.initializeBeteiligteRb();
		ueb.refreshZugStatusRbEntry();
	}

	@Test
	public void testGetStatusZug1() {
		Zug z1 = ueb.getMassnahmen().iterator().next().getZug().get(0);
		Map<Regionalbereich, Boolean> status = z1.getBearbeitungsStatusMap();
		assertEquals(2, status.size());
	}

	@Test
	public void testGetStatusZug2() {
		Zug z2 = ueb.getMassnahmen().iterator().next().getZug().get(0);
		Map<Regionalbereich, Boolean> status = z2.getBearbeitungsStatusMap();
		assertEquals(2, status.size());
	}

	@Test
	public void testInitializeBeteiligteRegionalbereiche() {
		ueb.initializeBeteiligteRb();
		Set<Regionalbereich> beteiligteRbs = ueb.getBeteiligteRegionalbereiche();

		assertEquals(2, beteiligteRbs.size());
		assertTrue(beteiligteRbs.contains(new Regionalbereich(rb1)));
		assertTrue(beteiligteRbs.contains(new Regionalbereich(rb2)));
	}

	@Test
	public void testZugstatusRefresh() {
		ueb.initializeBeteiligteRb();
		ueb.refreshZugStatusRbEntry();
		Massnahme m = ueb.getMassnahmen().iterator().next();
		Zug z1 = m.getZug().get(0);
		Zug z2 = m.getZug().get(1);
		Map<Regionalbereich, Boolean> status1 = z1.getBearbeitungsStatusMap();
		Map<Regionalbereich, Boolean> status2 = z2.getBearbeitungsStatusMap();

		assertEquals(2, status1.size());
		assertEquals(2, status2.size());

		Set<Regionalbereich> rbs1 = status1.keySet();
		assertTrue(rbs1.contains(new Regionalbereich(rb1)));
		assertTrue(rbs1.contains(new Regionalbereich(rb2)));

		Set<Regionalbereich> rbs2 = status1.keySet();
		assertTrue(rbs2.contains(new Regionalbereich(rb1)));
		assertTrue(rbs2.contains(new Regionalbereich(rb2)));
	}

	private void initializeNiederlassungen() {
		List<Niederlassung> niederlassungen = ueb.getMassnahmen().iterator().next().getFplonr()
		    .getNiederlassungen();
		RegionalbereichService rbService = new RegionalbereichServiceImpl();
		List<Regionalbereich> rbs = rbService.findAll();
		List<String> rbNames = RegionalbereichAdapter.getRegionalbereichNames();
		for (Regionalbereich r : rbs) {
			if (rbNames.contains(r.getName())) {
				niederlassungen.add(new Niederlassung(r));
			}
		}

		for (Niederlassung n : niederlassungen) {
			if (n.getRegionalbereich().getName().equals(rb1)
			    || n.getRegionalbereich().getName().equals(rb2))
				n.setBeteiligt(true);
		}
	}

	private void initializeZuege() {
		ueb.getMassnahmen().iterator().next().addZug(new Zug());
		ueb.getMassnahmen().iterator().next().addZug(new Zug());
	}
}
