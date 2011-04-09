package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import db.training.bob.model.TerminUebersichtPersonenverkehrsEVU;


public class TerminUebersichtPersonenverkehrsEVUCRUDTest {
	
	private TerminUebersichtPersonenverkehrsEVUService service;
	private TerminUebersichtPersonenverkehrsEVU terminUebersicht;
	
	private static final Date STUDIE_DATUM = new GregorianCalendar(2008, 0, 1).getTime();
	private static final Date ZVF_ENTWURF_DATUM = new GregorianCalendar(2008, 0, 2).getTime();
	private static final Date ZVF_DATUM = new GregorianCalendar(2008, 0, 3).getTime();
	private static final Date STELLUNGNAHME_EVU_DATUM = new GregorianCalendar(2008, 0, 4).getTime();
	private static final Date FPLO_DATUM = new GregorianCalendar(2008, 0, 5).getTime();
	private static final Date EINGABE_GFDZ_DATUM = new GregorianCalendar(2008, 0, 6).getTime();
	private static final Date MASTERUEBERGABEBLATT_PV_DATUM = new GregorianCalendar(2008, 0, 7).getTime();
	private static final Date UEBERGABEBLATT_PV_DATUM = new GregorianCalendar(2008, 0, 8).getTime();
	private static final boolean AUSFAELLE_SEV = true;
	private static final Date BKONZEPT_EVU_DATUM = new GregorianCalendar(2008, 0, 9).getTime();
	private static final Date NEUES_ZVF_DATUM = new GregorianCalendar(2008, 0, 10).getTime();
	private static final Date NEUES_BKONZEPT_EVU_DATUM = new GregorianCalendar(2008, 0, 11).getTime();;
	
	private static Integer id;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createTerminUebersicht();
	}
	
	@Before
	public void setUp() throws Exception {
		service = new TerminUebersichtPersonenverkehrsEVUServiceImpl();
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
		assertEquals(MASTERUEBERGABEBLATT_PV_DATUM, terminUebersicht.getMasterUebergabeblattPV());
		assertEquals(UEBERGABEBLATT_PV_DATUM, terminUebersicht.getUebergabeblattPV());
		assertEquals(AUSFAELLE_SEV, terminUebersicht.isAusfaelleSEV());
		assertEquals(BKONZEPT_EVU_DATUM, terminUebersicht.getBKonzeptEVU());
		
	}

	@Test
	public void testUpdate() {
		terminUebersicht.setZvF(NEUES_ZVF_DATUM);
		terminUebersicht.setBKonzeptEVU(NEUES_BKONZEPT_EVU_DATUM);
		
		service.update(terminUebersicht);
		
		TerminUebersichtPersonenverkehrsEVU updatedTerminUebersicht = service.findById(id);
		
		assertEquals(NEUES_ZVF_DATUM, updatedTerminUebersicht.getZvF());
		assertEquals(NEUES_BKONZEPT_EVU_DATUM, updatedTerminUebersicht.getBKonzeptEVU());
	}
	
	@Ignore
	@Test
	public void testDelete() {
		service.delete(terminUebersicht);
		TerminUebersichtPersonenverkehrsEVU emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}
	
	private static Integer createTerminUebersicht() {
		TerminUebersichtPersonenverkehrsEVU terminUebersicht = new TerminUebersichtPersonenverkehrsEVU();
		terminUebersicht.setStudieGrobkonzept(STUDIE_DATUM);
		terminUebersicht.setZvfEntwurf(ZVF_ENTWURF_DATUM);
		terminUebersicht.setZvF(ZVF_DATUM);
		terminUebersicht.setStellungnahmeEVU(STELLUNGNAHME_EVU_DATUM);
		terminUebersicht.setFplo(FPLO_DATUM);
		terminUebersicht.setEingabeGFD_Z(EINGABE_GFDZ_DATUM);
		terminUebersicht.setMasterUebergabeblattPV(MASTERUEBERGABEBLATT_PV_DATUM);
		terminUebersicht.setUebergabeblattPV(UEBERGABEBLATT_PV_DATUM);
		terminUebersicht.setAusfaelleSEV(AUSFAELLE_SEV);
		terminUebersicht.setBKonzeptEVU(BKONZEPT_EVU_DATUM);
    	
		TerminUebersichtPersonenverkehrsEVUService service = new TerminUebersichtPersonenverkehrsEVUServiceImpl();
		service.create(terminUebersicht);
		
		return terminUebersicht.getId();
    }
}
