package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaumassnahmeTest {

	@Test
	public void testGetBisherigerAufwandTimeString90Minutes() {
		Baumassnahme bm = new Baumassnahme();
		bm.setBisherigerAufwand(90);

		assertEquals("01:30", bm.getBisherigerAufwandTimeString());
	}

	@Test
	public void testGetBisherigerAufwandTimeString60Minutes() {
		Baumassnahme bm = new Baumassnahme();
		bm.setBisherigerAufwand(60);

		assertEquals("01:00", bm.getBisherigerAufwandTimeString());
	}

	@Test
	public void testGetBisherigerAufwandTimeString30Minutes() {
		Baumassnahme bm = new Baumassnahme();
		bm.setBisherigerAufwand(30);

		assertEquals("00:30", bm.getBisherigerAufwandTimeString());
	}
}
