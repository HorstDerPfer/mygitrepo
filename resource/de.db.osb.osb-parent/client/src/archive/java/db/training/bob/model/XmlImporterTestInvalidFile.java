package db.training.bob.model;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


public class XmlImporterTestInvalidFile {

	private static File BBP_XML_FILE = null;
	private XmlImporter xmlImporter;

	@Test
	public void testInvalidMasIdTag() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		BBP_XML_FILE = new File("test/db/training/bob/model","BBP_Export_invalid_MasId_tag.XML");
		xmlImporter = new XmlImporter(new FileInputStream(BBP_XML_FILE));
		try {
	        xmlImporter.getBBPMassnahmen().size();
	        fail("Exception not thrown.");
        } catch (SAXNotRecognizedException e) {}
	}

	@Test
	public void testNoRegelungRecognized() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		BBP_XML_FILE = new File("test/db/training/bob/model","BBP_Export_invalid_RegId_tag.XML");
		xmlImporter = new XmlImporter(new FileInputStream(BBP_XML_FILE));
		try {
			xmlImporter.getBBPMassnahmen().size();
			fail("Exception not thrown.");
		} catch (SAXNotRecognizedException e) {}
	}

	@Test
	public void testRegelungNoAktivTag() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		BBP_XML_FILE = new File("test/db/training/bob/model","BBP_Export_invalid_Aktiv_tag.XML");
		xmlImporter = new XmlImporter(new FileInputStream(BBP_XML_FILE));
		try {
			xmlImporter.getBBPMassnahmen().size();
			fail("Exception not thrown.");
		} catch (SAXNotRecognizedException e) {}
	}
	
	@Test
	public void testRegelungNoBeginnRecognized() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		BBP_XML_FILE = new File("test/db/training/bob/model","BBP_Export_invalid_BeginnDate_tag.XML");
		xmlImporter = new XmlImporter(new FileInputStream(BBP_XML_FILE));
		try {
			xmlImporter.getBBPMassnahmen().size();
			fail("Exception not thrown.");
		} catch (SAXNotRecognizedException e) {}
	}
}
