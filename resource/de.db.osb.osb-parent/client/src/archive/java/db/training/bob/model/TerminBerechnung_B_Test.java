package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;


public class TerminBerechnung_B_Test {

	private Date STARTTERMIN = new GregorianCalendar(2008, GregorianCalendar.OCTOBER, 19).getTime();
	private Date ANFORDERUNG_BBZR = new GregorianCalendar(2008, GregorianCalendar.JULY, 21).getTime();
	private Date BIUE_ENTWURF = new GregorianCalendar(2008, GregorianCalendar.AUGUST, 4).getTime();
	private Date ZVF_ENTWURF = new GregorianCalendar(2008, GregorianCalendar.AUGUST, 4).getTime();
//	private Date KOOREDINATIONSERGEBNIS = new GregorianCalendar(2008, GregorianCalendar.APRIL, 29).getTime();
	private Date STELLUNGNAHME_EVU = new GregorianCalendar(2008, GregorianCalendar.AUGUST, 25).getTime();
//	private Date GESAMTKONZEPT_BBZR = new GregorianCalendar(2008, GregorianCalendar.MAY, 6).getTime();
	private Date BIUE = new GregorianCalendar(2008, GregorianCalendar.AUGUST, 25).getTime();
	private Date ZVF = new GregorianCalendar(2008, GregorianCalendar.AUGUST, 25).getTime();
//	private Date MASTERUEB_PV = new GregorianCalendar(2008, GregorianCalendar.SEPTEMBER, 5).getTime();
//	private Date MASTERUEB_GV = new GregorianCalendar(2008, GregorianCalendar.SEPTEMBER, 5).getTime();
	private Date UEB_PV = new GregorianCalendar(2008, GregorianCalendar.SEPTEMBER, 5).getTime();
	private Date UEB_GV = new GregorianCalendar(2008, GregorianCalendar.SEPTEMBER, 5).getTime();
	private Date FPLO = new GregorianCalendar(2008, GregorianCalendar.OCTOBER, 3).getTime();
//	private Date EINGABE_GFDZ = new GregorianCalendar(2009, GregorianCalendar.MARCH, 7).getTime();
//	private Date UEB_PV_KS = new GregorianCalendar(2009, GregorianCalendar.NOVEMBER, 30).getTime();;
//	private Date UEB_GV_KS = new GregorianCalendar(2009, GregorianCalendar.NOVEMBER, 30).getTime();;
	
	
	@Test
	public void testGetStatusOpenBeforeDate() {
		GregorianCalendar today = new GregorianCalendar();
		today.add(GregorianCalendar.DATE, 1);
		Date ist = null;
		assertEquals(Status.NEUTRAL, TerminBerechnung.getStatus(today.getTime(), ist,true));
	}
	
	@Test
	public void testGetStatusOpenOnDate() {
		GregorianCalendar today= new GregorianCalendar();
		Date ist = null;
		assertEquals(Status.NEUTRAL, TerminBerechnung.getStatus(today.getTime(), ist,true));
	}
	
	@Test
	public void testGetStatusRedNoDate() {
		Date soll = new GregorianCalendar(2008, GregorianCalendar.JANUARY, 2).getTime();
		Date ist = null;
		assertEquals(Status.RED, TerminBerechnung.getStatus(soll, ist,true));
	}
	
	@Test
	public void testGetStatusRed() {
		Date soll = new GregorianCalendar(2008, GregorianCalendar.JANUARY, 2).getTime();
		Date ist = new GregorianCalendar(2008, GregorianCalendar.JANUARY, 3).getTime();
		assertEquals(Status.RED, TerminBerechnung.getStatus(soll, ist,true));
	}
	
	@Test
	public void testGetStatusGreenOnDate() {
		Date soll = new GregorianCalendar(2008, GregorianCalendar.JANUARY, 2).getTime();
		Date ist = new GregorianCalendar(2008, GregorianCalendar.JANUARY, 2).getTime();
		assertEquals(Status.GREEN, TerminBerechnung.getStatus(soll, ist,true));
	}
	
	@Test
	public void testGetStatusGreenBeforeDate() {
		Date soll = new GregorianCalendar(2008, GregorianCalendar.JANUARY, 2).getTime();
		Date ist = new GregorianCalendar(2008, GregorianCalendar.JANUARY, 1).getTime();
		assertEquals(Status.GREEN, TerminBerechnung.getStatus(soll, ist,true));
	}
	
	@Test
	public void testCalculateAnforderungBBZR() {
		assertEquals(ANFORDERUNG_BBZR, TerminBerechnung.getAnforderungBBZR_B_Soll(STARTTERMIN));		
	}
	
	@Test
	public void testCalculateBiUeEntwurf() {
		assertEquals(BIUE_ENTWURF, TerminBerechnung.getBiUeEntwurf_B_Soll(STARTTERMIN));
	}
	
	@Test
	public void testCalculateZvFEntwurf() {
		assertEquals(ZVF_ENTWURF, TerminBerechnung.getZvfEntwurf_B_Soll(STARTTERMIN));
		
	}
	
//	@Test
//	public void testCalculateKoordinationsergebnis() {
//		assertEquals(KOOREDINATIONSERGEBNIS, TerminBerechnung.getKoordinationsErgebnisSoll(STARTTERMIN));
//	}
	
	@Test
	public void testCalculateStellungnahmeEVU() {
		assertEquals(STELLUNGNAHME_EVU, TerminBerechnung.getStellungnahmeEVU_B_Soll(STARTTERMIN));
	}
	
//	@Test
//	public void testCalculateGesamtkonzeptBBZR() {
//		assertEquals(GESAMTKONZEPT_BBZR, TerminBerechnung.getGesamtkonzeptBBZRSoll(STARTTERMIN));
//	}
	
	@Test
	public void testCalculateBiUe() {
		assertEquals(BIUE, TerminBerechnung.getBiUe_B_Soll(STARTTERMIN));
	}
	
	@Test
	public void testCalculateZvF() {
		assertEquals(ZVF, TerminBerechnung.getZvf_B_Soll(STARTTERMIN));
	}
	
//	@Test
//	public void testCalculateMasterUeB_PV() {
//		assertEquals(MASTERUEB_PV, TerminBerechnung.getMasterUeB_PVSoll(STARTTERMIN));
//	}
//	
//	@Test
//	public void testCalculateMasterUeB_GV() {
//		assertEquals(MASTERUEB_GV, TerminBerechnung.getMasterUeB_GVSoll(STARTTERMIN));
//	}
	
	@Test
	public void testCalculateUeB_PV() {
		assertEquals(UEB_PV, TerminBerechnung.getUeB_PV_B_Soll(STARTTERMIN));
	}
	
	@Test
	public void testCalculateUeB_GV() {
		assertEquals(UEB_GV, TerminBerechnung.getUeB_GVSoll(STARTTERMIN));
	}
	
	@Test
	public void testCalculateFplo() {
		assertEquals(FPLO, TerminBerechnung.getFploSoll(STARTTERMIN));
	}
	
//	@Test
//	public void testCalculateEingabeGFDZ() {
//		assertEquals(EINGABE_GFDZ, TerminBerechnung.getEingabeGFDZSoll(STARTTERMIN));
//	}
//	
//	@Test
//	public void testCalculateUeB_PV_beiKS() {
//		assertEquals(UEB_PV_KS, TerminBerechnung.getUeB_PV_KS_Soll(STARTTERMIN));
//	}
//	
//	@Test
//	public void testCalculateUeB_GV_beiKS() {
//		assertEquals(UEB_GV_KS, TerminBerechnung.getUeB_GV_KS_Soll(STARTTERMIN));
//	}
}
