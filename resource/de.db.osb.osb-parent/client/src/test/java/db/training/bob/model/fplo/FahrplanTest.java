package db.training.bob.model.fplo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

public class FahrplanTest {

	private final int ID = 1;

	private Fahrplan fahrplan1;

	private Fahrplan fahrplan2;

	@Before
	public void setUp() {
		fahrplan1 = new Fahrplan();
		fahrplan2 = new Fahrplan();
		fahrplan1.setId(1);
		fahrplan2.setId(2);
	}

	@Test
	public void testEqual() {
		fahrplan1.setIdFahrplan(ID);
		fahrplan2.setIdFahrplan(ID);
		assertEquals(fahrplan1, fahrplan2);
	}

	@Test
	public void testNotEqual() {
		fahrplan1.setIdFahrplan(ID);
		fahrplan2.setIdFahrplan(ID + 1);
		assertNotSame(fahrplan1, fahrplan2);
	}
}
