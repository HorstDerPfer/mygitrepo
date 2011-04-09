package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.ZvFAusfallZug;

public class ZvFAusfallZugCRUDTest {

	private ZvFAusfallZugService ausfallZugService;

	private ZvFAusfallZug zvfAusfallZug;

	private static Integer id;

	private static final Integer ZUG_NR1 = 111;
	
	private static final String AUSFALL_AB = "hier";

	@Before
	public void setUp() throws Exception {
		ausfallZugService = new ZvFAusfallZugServiceImpl();
		createZvF();
		zvfAusfallZug = ausfallZugService.findById(id);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertEquals(ZUG_NR1, zvfAusfallZug.getZugNr());
		assertEquals(AUSFALL_AB, zvfAusfallZug.getAusfallAb());
	}

	private static void createZvF() {
		ZvFAusfallZug zvfAusfallZug = new ZvFAusfallZug();
		zvfAusfallZug.setZugNr(ZUG_NR1);
		zvfAusfallZug.setAusfallAb(AUSFALL_AB);

		ZvFAusfallZugService ausfallZugService = new ZvFAusfallZugServiceImpl();
		ausfallZugService.create(zvfAusfallZug);

		id = zvfAusfallZug.getId();
	}
}
