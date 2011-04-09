package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BaumassnahmeServiceVorgangsnrTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetNextVorgangsnr() {
		BaumassnahmeServiceImpl service = new BaumassnahmeServiceImpl();
		List numbersList = new ArrayList<Object>();
		numbersList.add(Integer.valueOf(80000));
		numbersList.add(Integer.valueOf(80001));
		numbersList.add(Integer.valueOf(80005));
		assertEquals(80002, service.getVorgangsNr(80000, 80010, numbersList));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetVorgangsnrAllUsed() {
		BaumassnahmeServiceImpl service = new BaumassnahmeServiceImpl();
		List numbersList = new ArrayList<Object>();
		numbersList.add(Integer.valueOf(80000));
		numbersList.add(Integer.valueOf(80001));
		numbersList.add(Integer.valueOf(80002));
		numbersList.add(Integer.valueOf(80003));
		assertEquals(80000, service.getVorgangsNr(80000, 80003, numbersList));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetVorgangsnrAllUsedMoreTimes() {
		BaumassnahmeServiceImpl service = new BaumassnahmeServiceImpl();
		List numbersList = new ArrayList<Object>();
		numbersList.add(Integer.valueOf(80000));
		numbersList.add(Integer.valueOf(80001));
		numbersList.add(Integer.valueOf(80002));
		numbersList.add(Integer.valueOf(80003));
		numbersList.add(Integer.valueOf(80000));
		numbersList.add(Integer.valueOf(80001));
		assertEquals(80002, service.getVorgangsNr(80000, 80003, numbersList));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetVorgangsnrAllUsedMoreTimesWithDeletedNumbers() {
		BaumassnahmeServiceImpl service = new BaumassnahmeServiceImpl();
		List numbersList = new ArrayList<Object>();
		numbersList.add(Integer.valueOf(80000));
		numbersList.add(Integer.valueOf(80001));
		numbersList.add(Integer.valueOf(80002));
		numbersList.add(Integer.valueOf(80003));
		// 80004 deleted
		numbersList.add(Integer.valueOf(80005));
		numbersList.add(Integer.valueOf(80000));
		numbersList.add(Integer.valueOf(80001));
		assertEquals(80004, service.getVorgangsNr(80000, 80005, numbersList));
	}
}
