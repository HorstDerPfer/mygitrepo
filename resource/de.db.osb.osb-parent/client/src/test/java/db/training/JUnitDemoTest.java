package db.training;

import junit.framework.TestCase;

public class JUnitDemoTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Setup der Tests bzw. der Testumgebung
	}

	/**
	 * Ein erfolgreicher Test
	 */
	public void testOne() {
		assertTrue(true);
	}

	/**
	 * Ein weiterer erfolgreicher Test
	 */
	public void testTwo() {
		assertFalse(false);
	}

	/**
	 * Der n-te erfolgreiche Test
	 */
	public void testN() {
		// Erwarter Wert: 1
		// Aktueller Wert: 5
		// Toleranz: 4
		assertEquals("Fehlermeldung", 1, 5, 4);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		// Aufräumen nach dem Testing
	}
}
