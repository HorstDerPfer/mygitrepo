package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AenderungTest {

	@Test
	public void testAenderungTimeString90Minutes() {
		Aenderung a = new Aenderung();
		a.setAufwand(90);

		assertEquals("01:30", a.getAufwandTimeString());
	}

	@Test
	public void testAenderungTimeString60Minutes() {
		Aenderung a = new Aenderung();
		a.setAufwand(60);

		assertEquals("01:00", a.getAufwandTimeString());
	}

	@Test
	public void testAenderungTimeString30Minutes() {
		Aenderung a = new Aenderung();
		a.setAufwand(30);

		assertEquals("00:30", a.getAufwandTimeString());
	}

}
