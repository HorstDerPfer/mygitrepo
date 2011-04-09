package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.Regelung;


public class RegelungCRUDTest {
	private RegelungService service;
	private Regelung regelung;
	
	private static Integer id;
	private static final Date BEGINN = new GregorianCalendar(2008,0,1).getTime();
	private static final Date NEUER_BEGINN = new GregorianCalendar(2008,0,2).getTime();
	private static final Date ENDE = new GregorianCalendar(2008,0,3).getTime();
	private static final String BETRIEBSSTELLE_VON = "Anfang";
	private static final String BETRIEBSSTELLE_BIS = "Ende";
	private static final String REGELUNG_ID = "1234567A";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createRegelung();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new RegelungServiceImpl();
		regelung = service.findById(id);
	}
	
	@Test
	public void testLoad() {
		assertEquals(REGELUNG_ID, regelung.getRegelungId());
		assertEquals(BEGINN, regelung.getBeginn());
		assertEquals(ENDE, regelung.getEnde());
		assertEquals(BETRIEBSSTELLE_VON, regelung.getBetriebsStelleVon());
		assertEquals(BETRIEBSSTELLE_BIS, regelung.getBetriebsStelleBis());
	}
	
	@Test
	public void testUpdate() {
		regelung.setBeginn(NEUER_BEGINN);
		service.update(regelung);
		
		Regelung updatedRegelung = service.findById(id);
		
		assertEquals(NEUER_BEGINN, updatedRegelung.getBeginn());
	}
	
//	@Ignore
	@Test
	public void testDelete() {
		service.delete(regelung);
		Regelung emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createRegelung() {
		Regelung regelung = new Regelung();
		regelung.setRegelungId(REGELUNG_ID);
		regelung.setBeginn(BEGINN);
		regelung.setEnde(ENDE);
		regelung.setBetriebsStelleVon(BETRIEBSSTELLE_VON);
		regelung.setBetriebsStelleBis(BETRIEBSSTELLE_BIS);
		
		
		RegelungService service = new RegelungServiceImpl();
		service.create(regelung);
		
		return regelung.getId();
	}
}
