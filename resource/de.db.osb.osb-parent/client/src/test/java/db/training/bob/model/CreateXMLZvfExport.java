package db.training.bob.model;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Test;

import db.training.bob.model.zvf.Uebergabeblatt;

public class CreateXMLZvfExport {

	private static String path = "src/test/resources/db/training/bob/model/testdata";
	
	private static File importFile = new File(path,
	    "UB-01.xml");

	private static File exportFile = new File(path,
	    "UB-01E.xml");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance("db.training.bob.model.zvf");
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Marshaller marshaller = jaxbContext.createMarshaller();

		Uebergabeblatt zvf;

		zvf = (Uebergabeblatt) unmarshaller.unmarshal(importFile);

		marshaller.marshal(zvf, new FileOutputStream(exportFile));
	}

	@Test
	public void testImportEqualsExport() {
	}
}
