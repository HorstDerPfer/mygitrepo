package db.training.bob.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.zvf.Version;

public class VersionTest {

	private Version v1;

	private Version v2;

	@Before
	public void setUp() throws Exception {
		v1 = new Version(1, 1, "01");
	}

	@Test
	public void testCompareEqualVersions() {
		v2 = new Version(1, 1, "01");
		assertEquals(0, v1.compareTo(v2));
	}

	@Test
	public void testCompareNotEqualVersionsLess() {
		v2 = new Version(1, 1, "02");
		assertEquals(-1, v1.compareTo(v2));
		v2 = new Version(1, 2, "01");
		assertEquals(-1, v1.compareTo(v2));
		v2 = new Version(2, 1, "01");
		assertEquals(-1, v1.compareTo(v2));
	}

	@Test
	public void testCompareNotEqualVersionsGreater() {
		v2 = new Version(1, 1, "00");
		assertEquals(1, v1.compareTo(v2));
		v2 = new Version(1, 0, "01");
		assertEquals(1, v1.compareTo(v2));
		v2 = new Version(0, 1, "01");
		assertEquals(1, v1.compareTo(v2));
	}

	@Test
	public void testCompareVersionsNull() {
		v2 = new Version(null, 1, "00");
		try {
			v1.compareTo(v2);
			fail();
		} catch (NullPointerException e) {
		}// expected
		try {
			v2.compareTo(v1);
			fail();
		} catch (NullPointerException e) {
		}// expected

		v2 = new Version(1, null, "00");
		try {
			v1.compareTo(v2);
			fail();
		} catch (NullPointerException e) {
		}// expected
		try {
			v2.compareTo(v1);
			fail();
		} catch (NullPointerException e) {
		}// expected

		v2 = new Version(1, 1, null);
		try {
			v1.compareTo(v2);
			fail();
		} catch (NumberFormatException e) {
		}// expected
		try {
			v2.compareTo(v1);
			fail();
		} catch (NumberFormatException e) {
		}// expected

		v2 = new Version(1, 1, "");
		try {
			v1.compareTo(v2);
			fail();
		} catch (NumberFormatException e) {
		}// expected
		try {
			v2.compareTo(v1);
			fail();
		} catch (NumberFormatException e) {
		}// expected

	}

}
