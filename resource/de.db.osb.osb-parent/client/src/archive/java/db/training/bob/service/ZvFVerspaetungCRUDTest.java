package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.Art;
import db.training.bob.model.ZvFVerspaetung;

public class ZvFVerspaetungCRUDTest {
	private ZvFVerspaetungService verspaetungService;

	private ZvFVerspaetung zvfVerspaetung;

	private static Integer id;

	private static final Integer ZUG_NR1 = 111;
	
	private static final Art QS_KS = Art.QS;

	@Before
	public void setUp() throws Exception {
		verspaetungService = new ZvFVerspaetungServiceImpl();
		createZvF();
		zvfVerspaetung = verspaetungService.findById(id);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertEquals(ZUG_NR1, zvfVerspaetung.getZugNr());
		assertEquals(QS_KS, zvfVerspaetung.getQs_ks());
	}

	private static void createZvF() {
		ZvFVerspaetung zvfVerspaetung = new ZvFVerspaetung();
		zvfVerspaetung.setZugNr(ZUG_NR1);
		zvfVerspaetung.setQs_ks(QS_KS);

		ZvFVerspaetungService verspaetungService = new ZvFVerspaetungServiceImpl();
		verspaetungService.create(zvfVerspaetung);

		id = zvfVerspaetung.getId();
	}
}
