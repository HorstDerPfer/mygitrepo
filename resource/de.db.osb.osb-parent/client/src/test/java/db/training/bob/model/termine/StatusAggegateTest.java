package db.training.bob.model.termine;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.Status;
import db.training.bob.model.StatusType;

public class StatusAggegateTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNullNull() {
		assertEquals(StatusType.NEUTRAL, Status.aggregate(null, null));
	}

	@Test
	public void testNullNeutral() {
		assertEquals(StatusType.NEUTRAL, Status.aggregate(null, StatusType.NEUTRAL));
	}

	@Test
	public void testNeutralNeutral() {
		assertEquals(StatusType.NEUTRAL, Status.aggregate(StatusType.NEUTRAL, StatusType.NEUTRAL));
	}

	@Test
	public void testNeutralOffen() {
		assertEquals(StatusType.OFFEN, Status.aggregate(StatusType.NEUTRAL, StatusType.OFFEN));
	}

	@Test
	public void testNeutralCountDown14() {
		assertEquals(StatusType.COUNTDOWN14, Status.aggregate(StatusType.NEUTRAL,
		    StatusType.COUNTDOWN14));
	}

	@Test
	public void testNeutralGreen() {
		assertEquals(StatusType.GREEN, Status.aggregate(StatusType.NEUTRAL, StatusType.GREEN));
	}

	@Test
	public void testNeutralRot() {
		assertEquals(StatusType.RED, Status.aggregate(StatusType.NEUTRAL, StatusType.RED));
	}

	@Test
	public void testNeutralLila() {
		assertEquals(StatusType.LILA, Status.aggregate(StatusType.NEUTRAL, StatusType.LILA));
	}

	@Test
	public void testOffenOffen() {
		assertEquals(StatusType.OFFEN, Status.aggregate(StatusType.OFFEN, StatusType.OFFEN));
	}

	@Test
	public void testOffenGreen() {
		assertEquals(StatusType.OFFEN, Status.aggregate(StatusType.OFFEN, StatusType.GREEN));
	}

	@Test
	public void testOffenRot() {
		assertEquals(StatusType.OFFEN, Status.aggregate(StatusType.OFFEN, StatusType.RED));
	}

	@Test
	public void testCountDown14CountDown14() {
		assertEquals(StatusType.COUNTDOWN14, Status.aggregate(StatusType.COUNTDOWN14,
		    StatusType.COUNTDOWN14));
	}

	@Test
	public void testCountDown14Green() {
		assertEquals(StatusType.COUNTDOWN14, Status.aggregate(StatusType.COUNTDOWN14,
		    StatusType.GREEN));
	}

	@Test
	public void testCountDown14Rot() {
		assertEquals(StatusType.COUNTDOWN14, Status.aggregate(StatusType.COUNTDOWN14,
		    StatusType.RED));
	}

	@Test
	public void testGreenGreen() {
		assertEquals(StatusType.GREEN, Status.aggregate(StatusType.GREEN, StatusType.GREEN));
	}

	@Test
	public void testGreenRot() {
		assertEquals(StatusType.RED, Status.aggregate(StatusType.GREEN, StatusType.RED));
	}

	@Test
	public void testGreenLila() {
		assertEquals(StatusType.LILA, Status.aggregate(StatusType.GREEN, StatusType.LILA));
	}

	@Test
	public void testLilaRot() {
		assertEquals(StatusType.LILA, Status.aggregate(StatusType.LILA, StatusType.RED));
	}

	@Test
	public void testLilaLila() {
		assertEquals(StatusType.LILA, Status.aggregate(StatusType.LILA, StatusType.LILA));
	}

	@Test
	public void testRotRot() {
		assertEquals(StatusType.RED, Status.aggregate(StatusType.RED, StatusType.RED));
	}

}
