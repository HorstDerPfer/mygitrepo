package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.TerminUebersichtBaubetriebsplanung;


public class TerminUebersichtBaubetriebsplanungCRUDTest {

	private TerminUebersichtBaubetriebsplanungService service;
	private TerminUebersichtBaubetriebsplanung terminUebersicht;
	private static final Date STUDIE_DATUM = new GregorianCalendar(2008, 0, 1).getTime();
	private static final Date ANFORDERUNGBBZR_DATUM = new GregorianCalendar(2008, 0, 2).getTime();
	private static final Date BIUE_ENTWURF_DATUM = new GregorianCalendar(2008, 0, 3).getTime();
	private static final Date ZVF_ENTWURF_DATUM = new GregorianCalendar(2008, 0, 4).getTime();
	private static final Date KOORDINATIONSERGEBNIS_DATUM = new GregorianCalendar(2008, 0, 5).getTime();
	private static final Date GESAMTKONZEPT_DATUM = new GregorianCalendar(2008, 0, 6).getTime();
	private static final Date BIUE_DATUM = new GregorianCalendar(2008, 0, 7).getTime();
	private static final Date ZVF_DATUM = new GregorianCalendar(2008, 0, 8).getTime();
	private static final Date NEUES_ZVF_DATUM = new GregorianCalendar(2008, 0, 9).getTime();;
	
	private static Integer id;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createTerminUebersicht();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new TerminUebersichtBaubetriebsplanungServiceImpl();
		terminUebersicht = service.findById(id);
	}

	@Test
	public void testLoad() {
		assertEquals(STUDIE_DATUM, terminUebersicht.getStudieGrobkonzept());
		assertEquals(ANFORDERUNGBBZR_DATUM, terminUebersicht.getAnforderungBBZR());
		assertEquals(BIUE_ENTWURF_DATUM, terminUebersicht.getBiUeEntwurf());
		assertEquals(ZVF_ENTWURF_DATUM, terminUebersicht.getZvfEntwurf());
		assertEquals(KOORDINATIONSERGEBNIS_DATUM, terminUebersicht.getKoordinationsErgebnis());
		assertEquals(GESAMTKONZEPT_DATUM, terminUebersicht.getGesamtKonzeptBBZR());
		assertEquals(BIUE_DATUM, terminUebersicht.getBiUe());
		assertEquals(ZVF_DATUM, terminUebersicht.getZvf());
	}

	@Test
	public void testUpdate() {
		terminUebersicht.setZvf(NEUES_ZVF_DATUM);
		
		service.update(terminUebersicht);
		
		TerminUebersichtBaubetriebsplanung updatedTerminUebersicht = service.findById(id);
		
		assertEquals(NEUES_ZVF_DATUM, updatedTerminUebersicht.getZvf());
	}
	
	@Test
	public void testDelete() {
		service.delete(terminUebersicht);
		TerminUebersichtBaubetriebsplanung emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createTerminUebersicht() {
		TerminUebersichtBaubetriebsplanung terminUebersicht = new TerminUebersichtBaubetriebsplanung();
		terminUebersicht.setStudieGrobkonzept(STUDIE_DATUM);
		terminUebersicht.setAnforderungBBZR(ANFORDERUNGBBZR_DATUM);
		terminUebersicht.setBiUeEntwurf(BIUE_ENTWURF_DATUM);
		terminUebersicht.setZvfEntwurf(ZVF_ENTWURF_DATUM);
		terminUebersicht.setKoordinationsErgebnis(KOORDINATIONSERGEBNIS_DATUM);
		terminUebersicht.setGesamtKonzeptBBZR(GESAMTKONZEPT_DATUM);
		terminUebersicht.setBiUe(BIUE_DATUM);
		terminUebersicht.setZvf(ZVF_DATUM);
    	
		TerminUebersichtBaubetriebsplanungService service = new TerminUebersichtBaubetriebsplanungServiceImpl();
		service.create(terminUebersicht);
		
		return terminUebersicht.getId();
    }
}
