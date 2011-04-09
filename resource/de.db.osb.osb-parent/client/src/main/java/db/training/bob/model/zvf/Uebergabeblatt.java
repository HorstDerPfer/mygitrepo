package db.training.bob.model.zvf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObject;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "header", "massnahmen" })
@XmlRootElement(name = "zvfexport")
@Entity
@Table(name = "uebergabeblatt")
public class Uebergabeblatt extends EasyPersistentObject {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * FetchPlan.UEB_HEADER
	 */
	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	protected Header header;

	/**
	 * FetchPlan.UEB_BAUMASSNAHMEN, ZVF_MN_STRECKEN, FetchPlan.ZVF_MN_STRECKE_STRECKEVZG,
	 * FetchPlan.ZVF_MN_VERSION, FetchPlan.ZVF_MN_BBPSTRECKE, FetchPlan.UEB_MN_FPLO
	 */
	@XmlElementWrapper(name = "baumassnahmen")
	@XmlElement(required = true, name = "baumassnahme")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	protected Set<Massnahme> massnahmen;

	@XmlTransient
	@Column(name = "fertigstellungsdatum")
	@Temporal(TemporalType.DATE)
	private Date fertigStellungsdatum;

	public Date getFertigStellungsdatum() {
		return fertigStellungsdatum;
	}

	public void setFertigStellungsdatum(Date fertigStellungsdatum) {
		this.fertigStellungsdatum = fertigStellungsdatum;
	}

	public Uebergabeblatt() {
		this.massnahmen = new HashSet<Massnahme>();
		this.massnahmen.add(new Massnahme());
		this.header = new Header();
	}

	public Set<Regionalbereich> getBeteiligteRb() {
		HashSet<Regionalbereich> beteiligteRegionalbereiche = new LinkedHashSet<Regionalbereich>();
		if (getMassnahmen().size() > 0) {
			try {
				List<Niederlassung> niederlassungen = getMassnahmen().iterator().next().getFplonr()
				    .getNiederlassungen();
				for (Niederlassung n : niederlassungen) {
					if (n.getBeteiligt() == true)
						beteiligteRegionalbereiche.add(n.getRegionalbereich());
				}
			} catch (Exception e) {
				// ZVF
			}
		}
		return beteiligteRegionalbereiche;
	}

	/**
	 * fügt allen Zügen jeweils einen Bearbeitungsstatus(=unbearbeitet) pro beteiligten
	 * Regionalbereich hinzu, falls für diesen Regionalbereich noch kein Status vorhanden ist
	 */
	public void refreshZugStatusRbEntry() {
		Iterator<Massnahme> it = massnahmen.iterator();
		List<Zug> zuege = new ArrayList<Zug>();
		// Liste aller Züge des Übergabeblattes erstellen
		while (it.hasNext()) {
			zuege.addAll(it.next().getZug());
		}
		Iterator<Zug> itZug = zuege.iterator();
		Zug z = null;
		Map<Regionalbereich, Boolean> status = null;
		Iterator<Regionalbereich> itRb = null;
		Regionalbereich rb = null;

		// über Züge iterieren
		while (itZug.hasNext()) {
			z = itZug.next();
			if (z.getBearbeitungsStatusMap() == null) {
				z.setBearbeitungsStatusMap(new HashMap<Regionalbereich, Boolean>());
			}
			status = z.getBearbeitungsStatusMap();
			// für jeden beteiligten Rb prüfen, ob in der Statusliste der
			// Züge vorhanden, ggf setzen

			Set<Regionalbereich> beteiligteRegionalbereiche;
			try {
				beteiligteRegionalbereiche = getBeteiligteRb();
				itRb = beteiligteRegionalbereiche.iterator();
				while (itRb.hasNext()) {
					rb = itRb.next();
					if (!status.containsKey(rb)) {
						status.put(rb, false);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public void refreshRegionalbereichBearbeitungsStatus() {
		// über RB iterieren
		Massnahme m = this.getMassnahmen().iterator().next();
		List<Zug> zuege = new ArrayList<Zug>();
		List<Niederlassung> niederlassungen = m.getFplonr().getNiederlassungen();
		Map<Regionalbereich, Boolean> statusMap = null;
		for (Niederlassung n : niederlassungen) {
			int bearbeitet = 0;
			int nichtBearbeitet = 0;
			if (n.getBeteiligt() == true) {
				// über Züge iterieren
				zuege = m.getZug();
				for (Zug z : zuege) {
					statusMap = z.getBearbeitungsStatusMap();
					if (statusMap.get(n.getRegionalbereich()) == true)
						bearbeitet++;
					else
						nichtBearbeitet++;

				}
				n.setBearbeitungsStatus(bearbeitet * 100 / (bearbeitet + nichtBearbeitet));
			}
		}
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header value) {
		this.header = value;
	}

	public Set<Massnahme> getMassnahmen() {
		if (massnahmen == null)
			return new HashSet<Massnahme>();
		return massnahmen;
	}

	public void setMassnahmen(Set<Massnahme> value) {
		this.massnahmen = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\nHeader=");
		sb.append(getHeader().toString());
		return sb.toString();
	}

	public String getVersion() {
		StringBuffer version = new StringBuffer();
		String result = null;
		Iterator<Massnahme> iter = getMassnahmen().iterator();
		if (iter.hasNext()) {
			Version v = iter.next().getVersion();
			version.append(v.getMajor());
			version.append(".");
			version.append(v.getMinor());
			version.append(".");
			version.append(v.getSub());
			result = version.toString();
		} else {
			result = "";
		}
		return result;
	}
}