package db.training.bob.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.Bearbeitungsbereich;
import db.training.bob.model.Regionalbereich;


public class RegionalbereichCRUDTest {

	private RegionalbereichService service;
	private Regionalbereich regionalbereich;
	
	private static Integer id;
	private static final String NAME = "Südost";
	private static final String NEUER_NAME = "Mitte";
	private static final Bearbeitungsbereich BEARBEITUNGSBEREICH1 = new Bearbeitungsbereich("Team Ost");
	private static final Bearbeitungsbereich BEARBEITUNGSBEREICH2 = new Bearbeitungsbereich("Team West");
	private static final Set<Bearbeitungsbereich> bearbeitungsbereiche =  new HashSet<Bearbeitungsbereich>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createRegionalbereich();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new RegionalbereichServiceImpl();
		regionalbereich = service.findById(id);
	}
	
	@Test
	public void testLoad() {
		assertEquals(NAME, regionalbereich.getName());
		Set<Bearbeitungsbereich> setBearbeitungsbereiche = regionalbereich.getBearbeitungsbereiche();
		assertEquals(2, setBearbeitungsbereiche.size());
		assertTrue(setBearbeitungsbereiche.contains(BEARBEITUNGSBEREICH1));
		assertTrue(setBearbeitungsbereiche.contains(BEARBEITUNGSBEREICH2));
	}
	
	@Test
	public void testUpdate() {
		regionalbereich.setName(NEUER_NAME);
		service.update(regionalbereich);
		
		Regionalbereich updatedRegionalbereich = service.findById(id);
		
		assertEquals(NEUER_NAME, updatedRegionalbereich.getName());
	}
	
//	@Ignore
	@Test
	public void testDelete() {
		service.delete(regionalbereich);
		Regionalbereich emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createRegionalbereich() {
		Regionalbereich regionalbereich = new Regionalbereich(null);
		regionalbereich.setName(NAME);
		
		bearbeitungsbereiche.add(BEARBEITUNGSBEREICH1);
		bearbeitungsbereiche.add(BEARBEITUNGSBEREICH2);
		regionalbereich.setBearbeitungsbereiche(bearbeitungsbereiche);
		
		BearbeitungsbereichService bearbeitungsbereichService = new BearbeitungsbereichServiceImpl();			
		bearbeitungsbereichService.create(BEARBEITUNGSBEREICH1);
		bearbeitungsbereichService.create(BEARBEITUNGSBEREICH2);
		RegionalbereichService service = new RegionalbereichServiceImpl();
		service.create(regionalbereich);
		
		return regionalbereich.getId();
	}
}
