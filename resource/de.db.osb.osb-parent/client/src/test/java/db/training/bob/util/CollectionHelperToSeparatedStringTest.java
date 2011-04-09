package db.training.bob.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollectionHelperToSeparatedStringTest {

	String testString;

	List<String> vergleich;

	@SuppressWarnings("rawtypes")
	private List rawIntegers;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Before
	public void setUp() throws Exception {
		rawIntegers = new ArrayList();
		rawIntegers.add(Integer.valueOf(100));
		rawIntegers.add(Integer.valueOf(222));
		rawIntegers.add(Integer.valueOf(45));
		rawIntegers.add(Integer.valueOf(3));
		rawIntegers.add(Integer.valueOf(9876));
	}

	@After
	public void tearDown() throws Exception {
		rawIntegers.clear();
		rawIntegers = null;
	}

	@Test
	public void testToSeparatedString() {
		String test = CollectionHelper.toSeparatedString(vergleich, ", ");
		assertEquals(test, testString);
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void testIntegerCollectionToSeparatedString() {
		String expected = "100,222,45,3,9876";

		String test = CollectionHelper.toSeparatedString(rawIntegers, ",");
		assertEquals(expected, test);
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void testIntegerCollectionToSeparatedStringListSize() {
		String expected = "100,222,45";

		String test = CollectionHelper.toSeparatedStringListSize(rawIntegers, ",", 0, 3);
		assertEquals(expected, test);

		expected = "45,3,9876";
		test = CollectionHelper.toSeparatedStringListSize(rawIntegers, ",", 2, 3);
		assertEquals(expected, test);

		expected = "3,9876";
		test = CollectionHelper.toSeparatedStringListSize(rawIntegers, ",", 3, 10);
		assertEquals(expected, test);
	}

	@SuppressWarnings({ "unchecked" })
	@Test(expected = IndexOutOfBoundsException.class)
	public void testIntegerCollectionToSeparatedStringListSizeInvalidArgumentStartIndex() {
		CollectionHelper.toSeparatedStringListSize(rawIntegers, ",", -1, 3);
	}

	@SuppressWarnings({ "unchecked" })
	@Test(expected = IndexOutOfBoundsException.class)
	public void testIntegerCollectionToSeparatedStringListSizeInvalidArgumentListSize() {
		CollectionHelper.toSeparatedStringListSize(rawIntegers, ",", 0, -1);
	}

	@SuppressWarnings({ "unchecked" })
	@Test(expected = IllegalArgumentException.class)
	public void testIntegerCollectionToSeparatedStringListSizeInvalidArgumentDelimiter() {
		CollectionHelper.toSeparatedStringListSize(rawIntegers, null, 0, 2);
	}
}
