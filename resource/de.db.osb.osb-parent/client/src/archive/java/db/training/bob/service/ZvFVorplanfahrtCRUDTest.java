package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.ZvFVorplanfahrt;

public class ZvFVorplanfahrtCRUDTest {

	private ZvFVorplanfahrtService vorplanfahrtService;

	private ZvFVorplanfahrt zvfVorplanfahrt;

	private static Integer id;

	private static final Integer ZUG_NR1 = 111;
	
	private static final Integer ZEIT_VOR_PLAN = 12;

	@Before
	public void setUp() throws Exception {
		vorplanfahrtService = new ZvFVorplanfahrtServiceImpl();
		createZvF();
		zvfVorplanfahrt = vorplanfahrtService.findById(id);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertEquals(ZUG_NR1, zvfVorplanfahrt.getZugNr());
		assertEquals(ZEIT_VOR_PLAN, zvfVorplanfahrt.getZeitVorPlan());
	}

	private static void createZvF() {
		ZvFVorplanfahrt zvfVorplanfahrt = new ZvFVorplanfahrt();
		zvfVorplanfahrt.setZugNr(ZUG_NR1);
		zvfVorplanfahrt.setZeitVorPlan(ZEIT_VOR_PLAN);

		ZvFVorplanfahrtService vorplanfahrtService = new ZvFVorplanfahrtServiceImpl();
		vorplanfahrtService.create(zvfVorplanfahrt);

		id = zvfVorplanfahrt.getId();
	}
}
