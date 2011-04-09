package db.training.bob.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollectionHelperTest {

	String testString;

	List<String> vergleich;

	@Before
	public void setUp() throws Exception {
		testString = "eins, 12345, drei, eins, ende, -----";

		vergleich = new ArrayList<String>();
		vergleich.add("eins");
		vergleich.add("12345");
		vergleich.add("drei");
		vergleich.add("eins");
		vergleich.add("ende");
		vergleich.add("-----");
	}

	@After
	public void tearDown() throws Exception {
		vergleich.clear();
		vergleich = null;
	}

	@Test
	public void testSeparatedStringToSet() {
		Set<String> test = CollectionHelper.separatedStringToCollection(new HashSet<String>(),
		    testString, ", ");

		Set<String> vergleich = new HashSet<String>(this.vergleich);
		assertEquals(5, test.size());
		assertTrue(test.containsAll(vergleich));
	}

	@Test
	public void testSeparatedStringToList() {
		List<String> test = CollectionHelper.separatedStringToCollection(new ArrayList<String>(),
		    testString, ", ");

		assertEquals(6, vergleich.size());
		assertTrue(test.containsAll(vergleich));
	}

	@Test
	public void testToSeparatedString() {
		String test = CollectionHelper.toSeparatedString(vergleich, ", ");
		assertEquals(test, testString);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testIntegerCollectionToSeparatedString() {
		List rawIntegers = new ArrayList();
		rawIntegers.add(Integer.valueOf(100));
		rawIntegers.add(Integer.valueOf(222));
		rawIntegers.add(Integer.valueOf(45));
		rawIntegers.add(Integer.valueOf(3));
		rawIntegers.add(Integer.valueOf(9876));

		String expected = "100,222,45,3,9876";

		String test = CollectionHelper.toSeparatedString(rawIntegers, ",");
		assertEquals(expected, test);
	}
}
