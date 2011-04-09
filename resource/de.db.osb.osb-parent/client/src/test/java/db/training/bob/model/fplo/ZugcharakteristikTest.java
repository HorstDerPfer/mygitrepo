package db.training.bob.model.fplo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

public class ZugcharakteristikTest {

	private final int ID = 1;

	private Zugcharakteristik zug1;

	private Zugcharakteristik zug2;

	@Before
	public void setUp() {
		zug1 = new Zugcharakteristik();
		zug2 = new Zugcharakteristik();
		zug1.setId(1);
		zug2.setId(2);
	}

	@Test
	public void testEqual() {
		zug1.setIdZug(ID);
		zug2.setIdZug(ID);
		assertEquals(zug1, zug2);
	}

	@Test
	public void testNotEqual() {
		zug1.setIdZug(ID);
		zug2.setIdZug(ID + 1);
		assertNotSame(zug1, zug2);
	}
}
