package db.training.bob.service.fplo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import db.training.bob.model.fplo.Fahrplan;
import db.training.bob.model.fplo.ISA_Fahrplan;

public class CreateFahrplanFromISAObjectTest {

	private FahrplanServiceImpl service = null;

	private Set<ISA_Fahrplan> isaFahrplanSet = null;

	private ISA_Fahrplan isaFahrplan = null;

	@Before
	public void setUp() {
		service = new FahrplanServiceImpl();
		isaFahrplan = new ISA_Fahrplan();
	}

	@Test
	public void testCreateFahrplanSetNull() {
		Set<Fahrplan> fahrplanSet = service.createFromISAObjects(isaFahrplanSet);
		assertEquals(0, fahrplanSet.size());
	}

	@Test
	public void testCreateFahrplanSetEmpty() {
		isaFahrplanSet = new HashSet<ISA_Fahrplan>();
		Set<Fahrplan> fahrplanSet = service.createFromISAObjects(isaFahrplanSet);
		assertEquals(0, fahrplanSet.size());
	}

	@Test
	public void testCreateFahrplanSetOneEntry() {
		isaFahrplanSet = new HashSet<ISA_Fahrplan>();
		isaFahrplanSet.add(isaFahrplan);
		Set<Fahrplan> fahrplanSet = service.createFromISAObjects(isaFahrplanSet);
		assertEquals(1, fahrplanSet.size());
	}

	@Test
	public void testCreateFahrplanSetTwoEntries() {
		isaFahrplanSet = new HashSet<ISA_Fahrplan>();
		ISA_Fahrplan isaFahrplan2 = new ISA_Fahrplan();
		isaFahrplanSet.add(isaFahrplan);
		isaFahrplanSet.add(isaFahrplan2);
		Set<Fahrplan> fahrplanSet = service.createFromISAObjects(isaFahrplanSet);
		assertEquals(2, fahrplanSet.size());
	}

	@Test
	public void testCreateFahrplanNull() {
		isaFahrplan = null;
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(null, fahrplan);
	}

	@Test
	public void testCreateFahrplanNotNull() {
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertNotNull(fahrplan);
	}

	@Test
	public void testCreateFahrplanAbfahrt() {
		isaFahrplan.setAbfahrt("10:00");
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(new GregorianCalendar(1970, 0, 1, 10, 0).getTime(), fahrplan.getAbfahrt());
	}

	@Test
	public void testCreateFahrplanAbfahrtSeconds() {
		isaFahrplan.setAbfahrt("10:00:01");
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(new GregorianCalendar(1970, 0, 1, 10, 0, 1).getTime(), fahrplan.getAbfahrt());
	}

	@Test
	public void testCreateFahrplanAnkunft() {
		isaFahrplan.setAnkunft("10:00");
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(new GregorianCalendar(1970, 0, 1, 10, 0).getTime(), fahrplan.getAnkunft());
	}

	@Test
	public void testCreateFahrplanAnkunftSeconds() {
		isaFahrplan.setAnkunft("10:00:01");
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(new GregorianCalendar(1970, 0, 1, 10, 0, 1).getTime(), fahrplan.getAnkunft());
	}

	@Test
	public void testCreateFahrplanBst() {
		String bst = "ABCDE";
		isaFahrplan.setBst(bst);
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(bst, fahrplan.getBst());
	}

	@Test
	public void testCreateFahrplanHaltart() {
		String haltart = "+";
		isaFahrplan.setHaltart(haltart);
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(haltart, fahrplan.getHaltart());
	}

	@Test
	public void testCreateFahrplanLfd() {
		Integer lfd = 25;
		isaFahrplan.setLfd(lfd);
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(lfd, fahrplan.getLfd());
	}

	@Test
	public void testCreateFahrplanVzG() {
		String vzg = "1500";
		isaFahrplan.setVzg(vzg);
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(Integer.valueOf(vzg), fahrplan.getVzg());
	}

	@Test
	public void testCreateFahrplanZugfolge() {
		String zugfolge = "blablabla";
		isaFahrplan.setZugfolge(zugfolge);
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(zugfolge, fahrplan.getZugfolge());
	}

	@Test
	public void testCreateFahrplanIdFahrplan() {
		Integer idFahrplan = 1;
		isaFahrplan.setIdFahrplan(idFahrplan);
		Fahrplan fahrplan = service.createFromISAObject(isaFahrplan);
		assertEquals(idFahrplan, fahrplan.getIdFahrplan());
	}
}
