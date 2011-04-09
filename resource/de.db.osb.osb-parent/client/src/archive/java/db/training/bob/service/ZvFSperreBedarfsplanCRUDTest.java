package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.ZvFSperreBedarfsplan;

public class ZvFSperreBedarfsplanCRUDTest {

	private ZvFSperreBedarfsplanService sperreBedarfsplanService;

	private ZvFSperreBedarfsplan zvfSperreBedarfsplan;

	private static Integer id;

	private static final Integer ZUG_NR1 = 111;
	
	@Before
	public void setUp() throws Exception {
		sperreBedarfsplanService = new ZvFSperreBedarfsplanServiceImpl();
		createZvF();
		zvfSperreBedarfsplan = sperreBedarfsplanService.findById(id);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertEquals(ZUG_NR1, zvfSperreBedarfsplan.getZugNr());
	}

	private static void createZvF() {
		ZvFSperreBedarfsplan zvfSperreBedarfsplan = new ZvFSperreBedarfsplan();
		zvfSperreBedarfsplan.setZugNr(ZUG_NR1);

		ZvFSperreBedarfsplanService sperreBedarfsplanService = new ZvFSperreBedarfsplanServiceImpl();
		sperreBedarfsplanService.create(zvfSperreBedarfsplan);

		id = zvfSperreBedarfsplan.getId();
	}
}
