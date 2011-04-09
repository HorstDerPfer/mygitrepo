package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.EVU;


public class EVUCRUDTest {
	private EVUService service;
	private EVU evu;
	
	private static Integer id;
	private static final String KUNDENNR = "A12345";
	private static final String NEUE_KUNDENNR = "A12346";
	private static final String NAME = "EVU1";
	private static final String NEUER_NAME = "EVU2";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createEVU();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new EVUServiceImpl();
		evu = service.findById(id);
	}
	
	@Test
	public void testLoad() {
		assertEquals(KUNDENNR, evu.getKundenNr());
		assertEquals(NAME, evu.getName());
	}
	
	@Test
	public void testUpdate() {
		evu.setKundenNr(NEUE_KUNDENNR);
		evu.setName(NEUER_NAME);
		
		service.update(evu);
		
		EVU updatedEVU = service.findById(id);
		
		assertEquals(NEUE_KUNDENNR, updatedEVU.getKundenNr());
		assertEquals(NEUER_NAME, updatedEVU.getName());
	}
	
//	@Ignore
	@Test
	public void testDelete() {
		service.delete(evu);
		EVU emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createEVU() {
		EVU evu = new EVU();
		evu.setKundenNr(KUNDENNR);
		evu.setName(NAME);
		
		EVUService service = new EVUServiceImpl();			
		service.create(evu);
		
		return evu.getId();
	}
}
