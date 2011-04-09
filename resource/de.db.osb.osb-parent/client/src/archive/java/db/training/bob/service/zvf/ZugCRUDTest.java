package db.training.bob.service.zvf;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.zvf.Zug;
import db.training.bob.model.zvf.Zugdetails;

public class ZugCRUDTest {

	private static ZugService service;

	private Zug zug;

	private static Integer id;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createZug();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		service = new ZugServiceImpl();
		zug = service.findById(id);
	}

	@After
	public void tearDown() throws Exception {
		service.delete(zug);
	}

	@Test
	public void testRead() {
		Assert.assertEquals(new Integer(3), zug.getId());
	}

	private static Integer createZug() {
		Zug z = new Zug();
		Zugdetails zd = new Zugdetails();
		z.setZugdetails(zd);

		service = new ZugServiceImpl();
		service.create(z);

		return z.getId();
	}
}
