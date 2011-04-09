package db.training.osb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.osb.model.SAPMassnahme;

public class SAPMassnahmeServiceTest {

	SAPMassnahmeService bmService;

	List<SAPMassnahme> bmList;

	private final static Integer TEST_1_regionalbereichId = Integer.valueOf(0);

	private final static String TEST_1_anmelder = "";

	private final static String TEST_1_gewerk = "";

	private final static String TEST_1_untergewerk = "";

	private final static Integer TEST_1_hauptStreckeNummer = Integer.valueOf(1700);

	private final static Integer TEST_1_paketId = null;

	private final static Integer TEST_2_regionalbereichId = Integer.valueOf(0);

	private final static String TEST_2_anmelder = "Netz";

	private final static String TEST_2_gewerk = "";

	private final static String TEST_2_untergewerk = "";

	private final static Integer TEST_2_hauptStreckeNummer = null;

	private final static Integer TEST_2_paketId = null;

	private final static Integer TEST_3_regionalbereichId = Integer.valueOf(2);

	private final static String TEST_3_anmelder = "Netz";

	private final static String TEST_3_gewerk = "";

	private final static String TEST_3_untergewerk = "";

	private final static Integer TEST_3_hauptStreckeNummer = null;

	private final static Integer TEST_3_paketId = null;

	private final static Integer TEST_4_regionalbereichId = Integer.valueOf(2);

	private final static String TEST_4_anmelder = "Netz";

	private final static String TEST_4_gewerk = "Oberbau";

	private final static String TEST_4_untergewerk = "";

	private final static Integer TEST_4_hauptStreckeNummer = null;

	private final static Integer TEST_4_paketId = null;

	private final static Integer TEST_5_regionalbereichId = Integer.valueOf(2);

	private final static String TEST_5_anmelder = "Netz";

	private final static String TEST_5_gewerk = "";

	private final static String TEST_5_untergewerk = "GE";

	private final static Integer TEST_5_hauptStreckeNummer = null;

	private final static Integer TEST_5_paketId = null;

	private final static Integer TEST_6_regionalbereichId = Integer.valueOf(1);

	private final static String TEST_6_anmelder = "Netz";

	private final static String TEST_6_gewerk = "";

	private final static String TEST_6_untergewerk = "";

	private final static Integer TEST_6_hauptStreckeNummer = Integer.valueOf(6124);

	private final static Integer TEST_6_paketId = Integer.valueOf(122);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		bmService = new SAPMassnahmeServiceImpl();
		bmList = null;
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test search: streckenband 1700
	 * 
	 */
	@Test
	public void testFillByKeyword_TEST_1() {

		bmList = bmService.findByKeywords(TEST_1_regionalbereichId, TEST_1_anmelder, TEST_1_gewerk,
		    TEST_1_untergewerk, TEST_1_hauptStreckeNummer, TEST_1_paketId, null, null, 2011);

		assertNotNull(bmList);

		SAPMassnahme bbp = bmList.iterator().next();
		assertNotNull(bbp);
		assertNotNull(bbp.getHauptStrecke());
		assertNotNull(bbp.getHauptStrecke().getNummer());
		assertEquals(bbp.getHauptStrecke().getNummer().intValue(), 1700);

	}

	/**
	 * Test search: anmelder = Netz
	 * 
	 */
	@Test
	public void testFillByKeyword_TEST_2() {

		bmList = bmService.findByKeywords(TEST_2_regionalbereichId, TEST_2_anmelder, TEST_2_gewerk,
		    TEST_2_untergewerk, TEST_2_hauptStreckeNummer, TEST_2_paketId, null, null, 2011);

		assertNotNull(bmList);

		SAPMassnahme bbp = bmList.iterator().next();
		assertNotNull(bbp);

	}

	/**
	 * Test search: anmelder = Netz, regionalbereich = 2
	 * 
	 */
	@Test
	public void testFillByKeyword_TEST_3() {

		bmList = bmService.findByKeywords(TEST_3_regionalbereichId, TEST_3_anmelder, TEST_3_gewerk,
		    TEST_3_untergewerk, TEST_3_hauptStreckeNummer, TEST_3_paketId, null, null, 2011);

		assertNotNull(bmList);

		SAPMassnahme bbp = bmList.iterator().next();
		assertNotNull(bbp);

	}

	/**
	 * Test search: anmelder = Netz, regionalbereich = 2, gewerk = Oberbau
	 * 
	 */
	@Test
	public void testFillByKeyword_TEST_4() {

		bmList = bmService.findByKeywords(TEST_4_regionalbereichId, TEST_4_anmelder, TEST_4_gewerk,
		    TEST_4_untergewerk, TEST_4_hauptStreckeNummer, TEST_4_paketId, null, null, 2011);

		assertNotNull(bmList);

		SAPMassnahme bbp = bmList.iterator().next();
		assertNotNull(bbp);

	}

	/**
	 * Test search: anmelder = Netz, regionalbereich = 2, untergewerk = GE
	 * 
	 */
	@Test
	public void testFillByKeyword_TEST_5() {

		bmList = bmService.findByKeywords(TEST_5_regionalbereichId, TEST_5_anmelder, TEST_5_gewerk,
		    TEST_5_untergewerk, TEST_5_hauptStreckeNummer, TEST_5_paketId, null, null, 2011);

		assertNotNull(bmList);

		SAPMassnahme bbp = bmList.iterator().next();
		assertNotNull(bbp);

	}

	/**
	 * Test search: anmelder = Netz, regionalbereich = 1, hauptStreckeNummer = 6124 paketID = 122
	 * (01.11.0002)
	 * 
	 */
	@Test
	public void testFillByKeyword_TEST_6() {

		bmList = bmService.findByKeywords(TEST_6_regionalbereichId, TEST_6_anmelder, TEST_6_gewerk,
		    TEST_6_untergewerk, TEST_6_hauptStreckeNummer, TEST_6_paketId, null, null, 2011);

		assertNotNull(bmList);

		SAPMassnahme bbp = bmList.iterator().next();
		assertNotNull(bbp);
		assertEquals(bbp.getHauptStrecke().getNummer(), TEST_6_hauptStreckeNummer);
		assertEquals(bbp.getRegionalbereich().getId(), TEST_6_regionalbereichId);

	}
}
