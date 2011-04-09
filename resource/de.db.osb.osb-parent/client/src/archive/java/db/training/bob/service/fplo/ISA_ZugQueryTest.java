package db.training.bob.service.fplo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.fplo.ISA_Zug;

public class ISA_ZugQueryTest {

	private final Integer BOB_VORGANGSNR = 12345;

	private final File HIBERNATE_CONFIG = new File("test/db/training/bob/service/fplo",
	    "hibernateISATest.cfg.xml");

	private Integer bobRegionalbereich;

	private ISA_ZugServiceImpl service;

	private Configuration configuration = null;

	private SessionFactory sessionFactory = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		configuration = new AnnotationConfiguration();
		configuration.configure(HIBERNATE_CONFIG);
		sessionFactory = configuration.buildSessionFactory();
		service = new ISA_ZugServiceImpl();
		Session session = sessionFactory.getCurrentSession();
		service.setSession(session);
		bobRegionalbereich = 3;
		createTestData();
	}

	private void createTestData() {

	}

	@Test
	public void testSelect() {
		List<ISA_Zug> objects = service.findByVorgangsnr(BOB_VORGANGSNR, bobRegionalbereich);
		assertEquals(1, objects.size());
		ISA_Zug resultZug = objects.get(0);
		assertEquals(2, resultZug.getFahrplan().size());
	}
}
