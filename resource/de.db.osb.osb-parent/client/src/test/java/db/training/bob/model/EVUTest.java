package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class EVUTest {

	private EVU evu;
	private EVU same;
	private EVU less;
	private EVU greater;
	
	private final String NAME1 = "EVU1";
	private final String NAME2 = "EVU0";
	private final String NAME3 = "EVU2";
	
	@Before
	public void setUp() {
		evu = new EVU();
		evu.setName(NAME1);
	}
	
	@Test
	public void testCompareEqualEVUs() {
		same = new EVU();
		same.setName(NAME1);
		
		assertEquals(0, evu.compareTo(same));
	}
	
	@Test
	public void testCompareNotEqualLessEVUs() {
		less = new EVU();
		less.setName(NAME2);
		
		assertEquals(-1, less.compareTo(evu));
	}

	@Test
	public void testCompareNotEqualGreaterEVUs() {
		greater = new EVU();
		greater.setName(NAME3);
		
		assertEquals(1, greater.compareTo(evu));
	}
	
}
