package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import db.training.bob.model.Nachbarbahn;

public class NachbarbahnCRUDTest {

	private NachbarbahnService service;

	private Nachbarbahn nachbarbahn;

	private static final String NAME = "SBB";

	private static final String NEUER_NAME = "ÖBB";

	private static Integer id;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createNachbarbahn();
	}

	@Before
	public void setUp() throws Exception {
		service = new NachbarbahnServiceImpl();
		nachbarbahn = service.findById(id);
	}

	@Test
	public void testLoad() {
		assertEquals(NAME, nachbarbahn.getName());
	}

	@Test
	public void testUpdate() {
		nachbarbahn.setName(NEUER_NAME);

		service.update(nachbarbahn);

		Nachbarbahn updatedNachbarbahn = service.findById(id);

		assertEquals(NEUER_NAME, updatedNachbarbahn.getName());
	}

	@Ignore
	@Test
	public void testDelete() {
		service.delete(nachbarbahn);
		Nachbarbahn emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}

	private static Integer createNachbarbahn() {
		Nachbarbahn nachbarbahn = new Nachbarbahn(null);
		nachbarbahn.setName(NAME);

		NachbarbahnService service = new NachbarbahnServiceImpl();
		service.create(nachbarbahn);

		return nachbarbahn.getId();
	}
}
