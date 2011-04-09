package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.ArbeitsleistungRegionalbereich;


public class ArbeitsleistungRegionalbereichCRUDTest {

	private ArbeitsleistungRegionalbereichService service;
	private ArbeitsleistungRegionalbereich arbeitsleistungRegionalbereich;
	private static final Date UEBERGABE_DATUM = new GregorianCalendar(2008, 0, 20).getTime();
	private static final Date NEUES_UEBERGABE_DATUM = new GregorianCalendar(2008, 0, 21).getTime();
	private static final Integer TRASSEN_MIT_KS = 4;
	private static final Integer GEREGELTE_TRASSEN = 5;
	private static final Integer UEBERARBEITETE_TRASSEN = 6;
	private static final Integer KONSTRUIERTE_TRASSEN = 7;
	private static final Integer TRASSEN_MIT_QS = 8;
	private static final Integer AUSFAELLE = 9;
	private static final Integer NEUE_ANZAHL_AUSFAELLE = 10;
	
	private static Integer id;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createArbeitsleistungRegionalbereich();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new ArbeitsleistungRegionalbereichServiceImpl();
		arbeitsleistungRegionalbereich = service.findById(id);
	}
	
	@Test
	public void testLoad() {
		assertEquals(UEBERGABE_DATUM, arbeitsleistungRegionalbereich.getUebergabeUeB());
		assertEquals(TRASSEN_MIT_KS, arbeitsleistungRegionalbereich.getTrassenMitKS());
		assertEquals(GEREGELTE_TRASSEN, arbeitsleistungRegionalbereich.getGeregelteTrassenBiUeE());
		assertEquals(UEBERARBEITETE_TRASSEN, arbeitsleistungRegionalbereich.getUeberarbeiteteTrassenBiUe());
		assertEquals(KONSTRUIERTE_TRASSEN, arbeitsleistungRegionalbereich.getKonstruierteTrassenUeB());
		assertEquals(TRASSEN_MIT_QS, arbeitsleistungRegionalbereich.getTrassenMitQS());
		assertEquals(AUSFAELLE, arbeitsleistungRegionalbereich.getAusFaelle());
	}
	
	@Test
	public void testUpdate() {
		arbeitsleistungRegionalbereich.setUebergabeUeB(NEUES_UEBERGABE_DATUM);
		arbeitsleistungRegionalbereich.setAusFaelle(NEUE_ANZAHL_AUSFAELLE);
		
		service.update(arbeitsleistungRegionalbereich);
		
		ArbeitsleistungRegionalbereich updatedArbeitsleistungRegionalbereich = service.findById(id);

		assertEquals(NEUES_UEBERGABE_DATUM, updatedArbeitsleistungRegionalbereich.getUebergabeUeB());
		assertEquals(NEUE_ANZAHL_AUSFAELLE, updatedArbeitsleistungRegionalbereich.getAusFaelle());
	}
	
//	@Ignore
	@Test
	public void testDelete() {
		service.delete(arbeitsleistungRegionalbereich);
		ArbeitsleistungRegionalbereich emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createArbeitsleistungRegionalbereich() {
		ArbeitsleistungRegionalbereich arbeitsleistungRegionalbereich = new ArbeitsleistungRegionalbereich();
		arbeitsleistungRegionalbereich.setUebergabeUeB(UEBERGABE_DATUM);
		arbeitsleistungRegionalbereich.setTrassenMitKS(TRASSEN_MIT_KS);
		arbeitsleistungRegionalbereich.setGeregelteTrassenBiUeE(GEREGELTE_TRASSEN);
		arbeitsleistungRegionalbereich.setUeberarbeiteteTrassenBiUe(UEBERARBEITETE_TRASSEN);
		arbeitsleistungRegionalbereich.setKonstruierteTrassenUeB(KONSTRUIERTE_TRASSEN);
		arbeitsleistungRegionalbereich.setTrassenMitQS(TRASSEN_MIT_QS);
		arbeitsleistungRegionalbereich.setAusFaelle(AUSFAELLE);
		
		ArbeitsleistungRegionalbereichService service = new ArbeitsleistungRegionalbereichServiceImpl();			
		service.create(arbeitsleistungRegionalbereich);
		
		return arbeitsleistungRegionalbereich.getId();
	}
}
