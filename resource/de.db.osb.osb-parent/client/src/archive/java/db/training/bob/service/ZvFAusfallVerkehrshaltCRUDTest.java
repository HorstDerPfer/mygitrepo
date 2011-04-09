package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.ZvFAusfallVerkehrshalt;

public class ZvFAusfallVerkehrshaltCRUDTest {

	private ZvFAusfallVerkehrshaltService ausfallVerkehrshaltService;

	private ZvFAusfallVerkehrshalt zvfAusfallVerkehrshalt;

	private static Integer id;

	private static final Integer ZUG_NR1 = 111;
	
	private static final String AUSFALLENDER_BHF = "bahnhof";

	@Before
	public void setUp() throws Exception {
		ausfallVerkehrshaltService = new ZvFAusfallVerkehrshaltServiceImpl();
		createZvF();
		zvfAusfallVerkehrshalt = ausfallVerkehrshaltService.findById(id);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertEquals(ZUG_NR1, zvfAusfallVerkehrshalt.getZugNr());
		assertEquals(AUSFALLENDER_BHF, zvfAusfallVerkehrshalt.getAusfallenderVerkehrshalt());
	}

	private static void createZvF() {
		ZvFAusfallVerkehrshalt zvfAusfallVerkehrshalt = new ZvFAusfallVerkehrshalt();
		zvfAusfallVerkehrshalt.setZugNr(ZUG_NR1);
		zvfAusfallVerkehrshalt.setAusfallenderVerkehrshalt(AUSFALLENDER_BHF);

		ZvFAusfallVerkehrshaltService ausfallVerkehrshaltService = new ZvFAusfallVerkehrshaltServiceImpl();
		ausfallVerkehrshaltService.create(zvfAusfallVerkehrshalt);

		id = zvfAusfallVerkehrshalt.getId();
	}
}
