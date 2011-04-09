package db.training.osb.service;

import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.training.osb.model.Buendel;

/**
 * Testcase für die BuendelService Implementierung.
 * 
 * @author nmoehring
 */
public class BuendelServiceImplTest extends TestCase {

	BuendelServiceImpl buendelService = null;
	
	@Before
	public void setUp() throws Exception {
		buendelService = new BuendelServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
		buendelService = null;
	}

	@Test
	public void testFindAll() {
		List<Buendel> buendelList = buendelService.findAll();
		assertEquals(false, (buendelList.size() > 0));
	}

}
