package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.Art;
import db.training.bob.model.ZvFUmleitung;

public class ZvfUmleitungCRUDTest {

	private ZvFUmleitungService umleitungService;

	private ZvFUmleitung zvfUmleitung;

	private static Integer id;

	private static final Integer ZUG_NR1 = 111;
	
	private static final Art QS_KS = Art.QS;

	@Before
	public void setUp() throws Exception {
		umleitungService = new ZvFUmleitungServiceImpl();
		createZvF();
		zvfUmleitung = umleitungService.findById(id);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertEquals(ZUG_NR1, zvfUmleitung.getZugNr());
		assertEquals(QS_KS, zvfUmleitung.getQs_ks());
	}

	private static void createZvF() {
		ZvFUmleitung zvfUmleitung = new ZvFUmleitung();
		zvfUmleitung.setZugNr(ZUG_NR1);
		zvfUmleitung.setQs_ks(QS_KS);

		ZvFUmleitungService umleitungService = new ZvFUmleitungServiceImpl();
		umleitungService.create(zvfUmleitung);

		id = zvfUmleitung.getId();
	}
}
