package db.training.bob.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.BBPMassnahme;
import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Regelung;

public class BaumassnahmeServiceTest {

	BaumassnahmeService bmService;

	Baumassnahme bm;

	FetchPlan[] fetchplans;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		bmService = new BaumassnahmeServiceImpl();
		bm = null;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFillBaumassnahme() {
		fetchplans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME };

		bm = bmService.findById(1, fetchplans);
		assertNotNull(bm.getBbpMassnahmen());
		assertEquals(1, bm.getBbpMassnahmen().size());

		BBPMassnahme bbp = bm.getBbpMassnahmen().iterator().next();
		assertEquals("07932A3523744", bbp.getMasId());
		try {
			bbp.getRegelungen().size();
			fail("LazyInitializationException expected");
		} catch (LazyInitializationException e) {
			// expected
		}
	}

	@Test
	public void testFetchBBP_Regelung() {
		fetchplans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME, FetchPlan.BBP_REGELUNGEN };

		bm = bmService.findById(1, fetchplans);
		assertNotNull(bm.getBbpMassnahmen());
		assertEquals(1, bm.getBbpMassnahmen().size());
		BBPMassnahme bbp = bm.getBbpMassnahmen().iterator().next();
		assertEquals("07932A3523744", bbp.getMasId());
		assertEquals(1, bbp.getRegelungen().size());
		Regelung r = bbp.getRegelungen().iterator().next();
		assertEquals("07932A3523744-0001", r.getRegelungId());
		try {
			bbp.getRegionalbereich().getName();
			fail("LazyInitializationException expected");
		} catch (LazyInitializationException e) {
			// expected
		}
	}

	@Test
	public void testFetchBBP_RB() {
		fetchplans = new FetchPlan[] { FetchPlan.BOB_BBP_MASSNAHME, FetchPlan.BBP_REGIONALBEREICH };

		bm = bmService.findById(1, fetchplans);
		assertNotNull(bm.getBbpMassnahmen());
		assertEquals(1, bm.getBbpMassnahmen().size());
		BBPMassnahme bbp = bm.getBbpMassnahmen().iterator().next();
		assertEquals("07932A3523744", bbp.getMasId());
		assertEquals("Süd", bbp.getRegionalbereich().getName());
		try {
			assertEquals(1, bbp.getRegelungen().size());
			fail("LazyInitializationException expected");
		} catch (LazyInitializationException e) {
			// expected
		}
	}
}
