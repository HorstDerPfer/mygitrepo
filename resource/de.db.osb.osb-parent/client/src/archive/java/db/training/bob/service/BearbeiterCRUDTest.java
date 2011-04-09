package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.Bearbeiter;
import db.training.bob.model.Regionalbereich;


public class BearbeiterCRUDTest {
	private BearbeiterService service;
	private Bearbeiter bearbeiter;
	
	private static Integer id;
	private static final String NAME = "Bearbeitername";
	private static final String NEUER_NAME = "neuerBearbeitername";
	private static final Regionalbereich REGIONALBEREICH = new Regionalbereich("Südost");
	private static final Regionalbereich NEUER_REGIONALBEREICH = new Regionalbereich("Süd");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createBearbeiter();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new BearbeiterServiceImpl();
		bearbeiter = (Bearbeiter) service.findById(id);
	}
	
	@Test
	public void testLoad() {
		assertEquals(NAME, bearbeiter.getName());
		assertEquals(REGIONALBEREICH, bearbeiter.getRegionalbereich());
	}
	
	@Test
	public void testUpdate() {
		bearbeiter.setName(NEUER_NAME);
		bearbeiter.setRegionalbereich(NEUER_REGIONALBEREICH);
		
		RegionalbereichService regionalbereichService = new RegionalbereichServiceImpl();
		regionalbereichService.create(NEUER_REGIONALBEREICH);
		
		service.update(bearbeiter);
		
		Bearbeiter updatedBearbeiter = (Bearbeiter) service.findById(id);
		
		assertEquals(NEUER_NAME, updatedBearbeiter.getName());
		assertEquals(NEUER_REGIONALBEREICH, updatedBearbeiter.getRegionalbereich());
	}
	
//	@Ignore
	@Test
	public void testDelete() {
		service.delete(bearbeiter);
		Bearbeiter emptyResult = (Bearbeiter) service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createBearbeiter() {
		Bearbeiter bearbeiter = new Bearbeiter();
		bearbeiter.setName(NAME);
		bearbeiter.setRegionalbereich(REGIONALBEREICH);
		
		RegionalbereichService regionalbereichService = new RegionalbereichServiceImpl();
		regionalbereichService.create(REGIONALBEREICH);
		BearbeiterService service = new BearbeiterServiceImpl();			
		service.create(bearbeiter);
		
		return bearbeiter.getId();
	}
}
