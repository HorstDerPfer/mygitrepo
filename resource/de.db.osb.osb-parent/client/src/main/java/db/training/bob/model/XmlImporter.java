package db.training.bob.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

import db.training.easy.util.FrontendHelper;

public class XmlImporter {

	private static final String REGELUNG = "Regelung";

	private static final String BBPMASSNAHME = "BBPMassnahme";

	private static final String INAKTIVE_REGELUNG = "0";

	private static final String AKTIVE_REGELUNG = "1";

	private static final String ERSTELLZEIT = "Erstellzeit";

	private Document doc;

	private LinkedHashSet<BBPMassnahme> bbpMassnahmenSet;

	private BBPMassnahme bbpMassnahme = null;

	private Date erstellDatum = null;

	public XmlImporter(InputStream inputStream) throws ParserConfigurationException, SAXException,
	    IOException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fact.newDocumentBuilder();
		doc = builder.parse(inputStream);
		bbpMassnahmenSet = new LinkedHashSet<BBPMassnahme>();
	}

	private void searchForData(Element element) throws SAXNotRecognizedException {
		if (element.getNodeName().equals(ERSTELLZEIT)) {
			erstellDatum = FrontendHelper.castStringToDate(element.getTextContent());
		}

		if (element.getNodeName().equals(BBPMASSNAHME)) {
			bbpMassnahme = fillBBPMassnahme(element);
			bbpMassnahmenSet.add(bbpMassnahme);
		}

		if (element.getNodeName().equals(REGELUNG)) {
			Regelung reg = fillRegelung(element);
			if (reg != null) {
				Set<Regelung> regs = bbpMassnahme.getRegelungen();
				regs.add(reg);
				bbpMassnahme.setRegelungen(regs);
			}
		}

		if (element.hasChildNodes()) {
			NodeList nl = element.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node child = nl.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					searchForData((Element) child);
				}
			}
		}
	}

	private Regelung fillRegelung(Element element) throws SAXNotRecognizedException {
		Regelung regelung = new Regelung();
		boolean aktivTag = false;
		if (element.hasChildNodes()) {
			NodeList children = element.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child.getNodeName().equals("aktiv")) {
					if (child.getTextContent().equals(INAKTIVE_REGELUNG))
						return null;
					if (child.getTextContent().equals(AKTIVE_REGELUNG))
						aktivTag = true;
				}
				if (child.getNodeName().equals("RegID"))
					regelung.setRegelungId(child.getTextContent());
				if (child.getNodeName().equals("Beginn"))
					regelung.setBeginn(FrontendHelper.castStringToDate(child.getTextContent()));
				if (child.getNodeName().equals("Ende"))
					regelung.setEnde(FrontendHelper.castStringToDate(child.getTextContent()));
				if (child.getNodeName().equals("BstVonLang"))
					regelung.setBetriebsStelleVon(child.getTextContent());
				if (child.getNodeName().equals("BstBisLang"))
					regelung.setBetriebsStelleBis(child.getTextContent());
				if (child.getNodeName().equals("BplRegelungLang"))
					regelung.setBetriebsweise(child.getTextContent());
				if (child.getNodeName().equals("BBPStrecke"))
					regelung.setStreckeBBP(child.getTextContent());
				if (child.getNodeName().equals("VzGStrecke"))
					regelung.setStreckeVZG(child.getTextContent());
				try {
					if (child.getNodeName().equals("SperrKz"))
						regelung
						    .setSperrKz(SperrKz.getConstant(new Integer(child.getTextContent())));
				} catch (NumberFormatException e) {
				}
				if (child.getNodeName().equals("RegelVTS"))
					regelung.setRegelVTS(new Integer(child.getTextContent()));
				if (child.getNodeName().equals("BplArtText"))
					regelung.setBplArtText(child.getTextContent());
				if (child.getNodeName().equals("LisbaNr"))
					regelung.setLisbaNr(child.getTextContent());
				if (child.getNodeName().equals("BemerkungenBpl")) {
					if (child.getTextContent().length() > 255)
						regelung.setBemerkungenBpl(child.getTextContent().substring(0, 254));
					else
						regelung.setBemerkungenBpl(child.getTextContent());
				}

			}
		}
		if (regelung.getRegelungId() == null)
			throw new SAXNotRecognizedException("No or invalid RegID Tag");
		if (aktivTag != true)
			throw new SAXNotRecognizedException("No or invalid aktiv Tag");
		if (regelung.getBeginn() == null)
			throw new SAXNotRecognizedException("No or invalid Beginn Tag");

		return regelung;
	}

	private BBPMassnahme fillBBPMassnahme(Element element) throws SAXNotRecognizedException {
		BBPMassnahme bbpMassnahme = new BBPMassnahme();
		if (element.hasChildNodes()) {
			NodeList children = element.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child.getNodeName().equals("MasID"))
					bbpMassnahme.setMasId(child.getTextContent());
				if (child.getNodeName().equals("MasBeginn"))
					bbpMassnahme.setBeginn(FrontendHelper.castStringToDate(child.getTextContent()));
				if (child.getNodeName().equals("MasEnde"))
					bbpMassnahme.setEnde(FrontendHelper.castStringToDate(child.getTextContent()));
				if (child.getNodeName().equals("MasBstVonLang"))
					bbpMassnahme.setBstVonLang(child.getTextContent());
				if (child.getNodeName().equals("MasBstBisLang"))
					bbpMassnahme.setBstBisLang(child.getTextContent());
				if (child.getNodeName().equals("Arbeiten"))
					bbpMassnahme.setArbeiten(child.getTextContent());
				if (child.getNodeName().equals("BBPStrecke"))
					bbpMassnahme.setStreckeBBP(child.getTextContent());
				if (child.getNodeName().equals("VzGStrecke"))
					bbpMassnahme.setStreckeVZG(child.getTextContent());
				if (child.getNodeName().equals("Region")) {

					// #427 Regionalbereich wird Freitextfeld
					// RegionalbereichService regionalbereichService = null;
					// Regionalbereich regionalbereich = null;
					// try {
					//
					// regionalbereichService = EasyServiceFactory.getInstance()
					// .createRegionalbereichService();
					// Integer id = Integer.parseInt(child.getTextContent());
					// regionalbereich = regionalbereichService.findById(id);
					// } catch (NumberFormatException e) {
					// } catch (NullPointerException e) {
					// // Service not found
					// }
					bbpMassnahme.setRegionalbereich(child.getTextContent());
				}
			}
		}
		if (bbpMassnahme.getMasId() != null)
			return bbpMassnahme;
		throw new SAXNotRecognizedException("Tag MasId not found");
	}

	public LinkedHashSet<BBPMassnahme> getBBPMassnahmen() throws SAXNotRecognizedException {
		Element rootElement = doc.getDocumentElement();
		searchForData(rootElement);
		return bbpMassnahmenSet;
	}

	public Date getErstelldatum() {
		return erstellDatum;
	}
}
