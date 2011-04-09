package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;


public class BearbeitungsbereichCRUDTest {

	private BearbeitungsbereichService service;
	private Bearbeitungsbereich bearbeitungsbereich;
	
	private static Integer id;
	private static final String NAME = "Team Umleitung";
	private static final Regionalbereich REGIONALBEREICH = new Regionalbereich("Südost");
	private static final String NEUER_NAME = "Team West";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createBearbeitungsbereich();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new BearbeitungsbereichServiceImpl();
		bearbeitungsbereich = service.findById(id);
	}
	
	@Test
	public void testLoad() {
		assertEquals(NAME, bearbeitungsbereich.getName());
		assertEquals(REGIONALBEREICH, bearbeitungsbereich.getRegionalbereich());
	}
	
	@Test
	public void testUpdate() {
		bearbeitungsbereich.setName(NEUER_NAME);
		service.update(bearbeitungsbereich);
		
		Bearbeitungsbereich updatedBearbeitungsbereich = service.findById(id);
		
		assertEquals(NEUER_NAME, updatedBearbeitungsbereich.getName());
	}
	
//	@Ignore
	@Test
	public void testDelete() {
		service.delete(bearbeitungsbereich);
		Bearbeitungsbereich emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createBearbeitungsbereich() {
		Bearbeitungsbereich bearbeitungsbereich = new Bearbeitungsbereich(null);
		bearbeitungsbereich.setName(NAME);
		bearbeitungsbereich.setRegionalbereich(REGIONALBEREICH);
		
		RegionalbereichService regionalbereichService = new RegionalbereichServiceImpl();
		regionalbereichService.create(REGIONALBEREICH);
		BearbeitungsbereichService service = new BearbeitungsbereichServiceImpl();			
		service.create(bearbeitungsbereich);
		
		return bearbeitungsbereich.getId();
	}
}
