package db.training.bob.model;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class XmlImporterTestEmptyFile {
	
	private static File BBP_XML_FILE = null;

	@Before
	public void setUp() throws Exception {
		BBP_XML_FILE = new File("test/db/training/bob/model","BBP_Export_Empty.XML");
	}
	
	@Test
	public void testReadFileWithoutBBPMassnahmen() {
		try {
			new XmlImporter(new FileInputStream(BBP_XML_FILE));
			fail("Exception not thrown");
		} catch (FileNotFoundException e) {
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {}
	}
	
}
