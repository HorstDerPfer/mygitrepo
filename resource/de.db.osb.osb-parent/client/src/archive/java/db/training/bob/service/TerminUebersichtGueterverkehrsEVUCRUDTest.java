package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import db.training.bob.model.TerminUebersichtGueterverkehrsEVU;

public class TerminUebersichtGueterverkehrsEVUCRUDTest {
	private TerminUebersichtGueterverkehrsEVUService service;
	private TerminUebersichtGueterverkehrsEVU terminUebersicht;
	
	private static final Date STUDIE_DATUM = new GregorianCalendar(2008, 0, 1).getTime();
	private static final Date ZVF_ENTWURF_DATUM = new GregorianCalendar(2008, 0, 2).getTime();
	private static final Date ZVF_DATUM = new GregorianCalendar(2008, 0, 3).getTime();
	private static final Date STELLUNGNAHME_EVU_DATUM = new GregorianCalendar(2008, 0, 4).getTime();
	private static final Date FPLO_DATUM = new GregorianCalendar(2008, 0, 5).getTime();
	private static final Date EINGABE_GFDZ_DATUM = new GregorianCalendar(2008, 0, 6).getTime();
	private static final Date MASTERUEBERGABEBLATT_GV_DATUM = new GregorianCalendar(2008, 0, 7).getTime();
	private static final Date UEBERGABEBLATT_GV_DATUM = new GregorianCalendar(2008, 0, 8).getTime();
	private static final Date NEUES_MASTERUEBERGABEBLATT_GV_DATUM = new GregorianCalendar(2008, 0, 9).getTime();
	
	private static Integer id;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createTerminUebersicht();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new TerminUebersichtGueterverkehrsEVUServiceImpl();
		terminUebersicht = service.findById(id);
	}

	@Test
	public void testLoad() {
		assertEquals(STUDIE_DATUM, terminUebersicht.getStudieGrobkonzept());
		assertEquals(ZVF_ENTWURF_DATUM, terminUebersicht.getZvfEntwurf());
		assertEquals(STELLUNGNAHME_EVU_DATUM, terminUebersicht.getStellungnahmeEVU());
		assertEquals(ZVF_DATUM, terminUebersicht.getZvF());
		assertEquals(FPLO_DATUM, terminUebersicht.getFplo());
		assertEquals(EINGABE_GFDZ_DATUM, terminUebersicht.getEingabeGFD_Z());
		assertEquals(MASTERUEBERGABEBLATT_GV_DATUM, terminUebersicht.getMasterUebergabeblattGV());
		assertEquals(UEBERGABEBLATT_GV_DATUM, terminUebersicht.getUebergabeblattGV());
	}

	@Test
	public void testUpdate() {
		terminUebersicht.setMasterUebergabeblattGV(NEUES_MASTERUEBERGABEBLATT_GV_DATUM);
		
		service.update(terminUebersicht);
		
		TerminUebersichtGueterverkehrsEVU updatedTerminUebersicht = service.findById(id);
		
		assertEquals(NEUES_MASTERUEBERGABEBLATT_GV_DATUM, updatedTerminUebersicht.getMasterUebergabeblattGV());
	}
	
	@Ignore
	@Test
	public void testDelete() {
		service.delete(terminUebersicht);
		TerminUebersichtGueterverkehrsEVU emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createTerminUebersicht() {
		TerminUebersichtGueterverkehrsEVU terminUebersicht = new TerminUebersichtGueterverkehrsEVU();
		terminUebersicht.setStudieGrobkonzept(STUDIE_DATUM);
		terminUebersicht.setZvfEntwurf(ZVF_ENTWURF_DATUM);
		terminUebersicht.setZvF(ZVF_DATUM);
		terminUebersicht.setStellungnahmeEVU(STELLUNGNAHME_EVU_DATUM);
		terminUebersicht.setFplo(FPLO_DATUM);
		terminUebersicht.setEingabeGFD_Z(EINGABE_GFDZ_DATUM);
		terminUebersicht.setMasterUebergabeblattGV(MASTERUEBERGABEBLATT_GV_DATUM);
		terminUebersicht.setUebergabeblattGV(UEBERGABEBLATT_GV_DATUM);
    	
		TerminUebersichtGueterverkehrsEVUService service = new TerminUebersichtGueterverkehrsEVUServiceImpl();
		service.create(terminUebersicht);
		
		return terminUebersicht.getId();
    }
}
