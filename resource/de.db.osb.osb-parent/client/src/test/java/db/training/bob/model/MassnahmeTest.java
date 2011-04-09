package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.zvf.Massnahme;
import db.training.bob.model.zvf.Zug;

public class MassnahmeTest {

	private Massnahme m;

	@Before
	public void setUp() throws Exception {
		m = new Massnahme();
	}

	@Test
	public void testEmptyList() {
		assertEquals(0, m.getZug().size());
	}

	@Test
	public void testAddZug() {
		Zug z1 = new Zug();
		Zug z2 = new Zug();
		Zug z3 = new Zug();

		m.addZug(z1);
		assertEquals(1, m.getZug().size());
		assertEquals(new Integer(1), m.getZug().get(0).getLaufendeNr());

		m.addZug(z2);
		assertEquals(2, m.getZug().size());
		assertEquals(new Integer(2), m.getZug().get(1).getLaufendeNr());

		m.addZug(z3);
		assertEquals(3, m.getZug().size());
		assertEquals(new Integer(3), m.getZug().get(2).getLaufendeNr());
	}
}
