package db.training.bob.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import db.training.bob.model.Aenderung;
import db.training.bob.model.Grund;

public class AenderungCRUDTest {

	private AenderungService service;

	private Aenderung aenderung;

	private static Integer id;

	private static final Integer AENDERUNGSNR = 20;

	private static final Integer NEUE_AENDERUNGSNR = 21;

	private static final String GRUND = "deshalb";

	private static final String NEUER_GRUND = "deswegen";

	private static final Integer AUFWAND = 50;

	private static final Integer NEUER_AUFWAND = 51;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = createAenderung();
	}

	@Before
	public void setUp() throws Exception {
		service = new AenderungServiceImpl();
		aenderung = service.findById(id);
	}

	@Test
	public void testLoad() {
		assertEquals(AENDERUNGSNR, aenderung.getAenderungsNr());
		assertEquals(GRUND, aenderung.getGrund());
		assertEquals(AUFWAND, aenderung.getAufwand());
	}

	@Test
	public void testUpdate() {
		aenderung.setAenderungsNr(NEUE_AENDERUNGSNR);
		Grund grund = new Grund(NEUER_GRUND);
		aenderung.setGrund(grund);
		aenderung.setAufwand(NEUER_AUFWAND);

		service.update(aenderung);

		Aenderung updatedAenderung = service.findById(id);

		assertEquals(NEUE_AENDERUNGSNR, updatedAenderung.getAenderungsNr());
		assertEquals(NEUER_GRUND, updatedAenderung.getGrund());
		assertEquals(NEUER_AUFWAND, updatedAenderung.getAufwand());
	}

	@Ignore
	@Test
	public void testDelete() {
		service.delete(aenderung);
		Aenderung emptyResult = service.findById(id);
		assertEquals(null, emptyResult);
	}

	private static Integer createAenderung() {
		Aenderung aenderung = new Aenderung();
		aenderung.setAenderungsNr(AENDERUNGSNR);
		Grund grund = new Grund(NEUER_GRUND);
		aenderung.setGrund(grund);
		aenderung.setAufwand(AUFWAND);

		AenderungService service = new AenderungServiceImpl();
		service.create(aenderung);

		return aenderung.getId();
	}
}
