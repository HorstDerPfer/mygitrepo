package db.training.osb.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.easy.core.service.EasyServiceFactory;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "umleitung")
public class Umleitung extends EasyPersistentExpirationObject implements Historizable {

	private String name;

	@Lob
	private String relation;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(joinColumns = { @JoinColumn(name = "umleitung_id") }, inverseJoinColumns = { @JoinColumn(name = "umleitungsweg_id") })
	@Cascade(CascadeType.SAVE_UPDATE)
	private List<Umleitungsweg> umleitungswege = new ArrayList<Umleitungsweg>();

	@Column(name = "freieKapaRichtung")
	private Double freieKapaRichtung;

	@Column(name = "freieKapaGegenrichtung")
	private Double freieKapaGegenrichtung;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "umleitung")
	@Cascade(CascadeType.DELETE_ORPHAN)
	private Set<UmleitungFahrplanregelungLink> umleitungFahrplanregelungLinks;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Integer bbpRglNr;

	private boolean deleted;

	public Set<UmleitungFahrplanregelungLink> getUmleitungFahrplanregelungLinks() {
		return umleitungFahrplanregelungLinks;
	}

	public void setUmleitungFahrplanregelungLinks(
	    Set<UmleitungFahrplanregelungLink> umleitungFahrplanregelungLinks) {
		this.umleitungFahrplanregelungLinks = umleitungFahrplanregelungLinks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Umleitungsweg> getUmleitungswege() {
		return umleitungswege;
	}

	@Transient
	public List<Umleitungsweg> getUmleitungswegeSorted() {
		if (umleitungswege.size() < 2)
			return umleitungswege;

		List<Umleitungsweg> umleitungswegeSorted = new ArrayList<Umleitungsweg>();
		String start = null;
		String ziel = null;
		List<String> betriebsstellenVon = new ArrayList<String>();
		List<String> betriebsstellenBis = new ArrayList<String>();
		for (Umleitungsweg umleitungsweg : umleitungswege) {
			betriebsstellenVon.add(umleitungsweg.getBetriebsstelleVon().getKuerzel());
			betriebsstellenBis.add(umleitungsweg.getBetriebsstelleBis().getKuerzel());
		}
		List<String> betriebsstellenVonKopie = new ArrayList<String>(betriebsstellenVon);
		betriebsstellenVon.removeAll(betriebsstellenBis);
		if (betriebsstellenVon.size() == 1) {
			start = betriebsstellenVon.get(0);
			betriebsstellenBis.removeAll(betriebsstellenVonKopie);
			if (betriebsstellenBis.size() == 1)
				ziel = betriebsstellenBis.get(0);
		}

		// unvollstaendige Umleitung -> Abbruch
		if (start == null || ziel == null)
			return umleitungswege;

		for (int i = 0; i < umleitungswege.size(); i++) {
			for (Umleitungsweg umleitungsweg : umleitungswege) {
				if (start.equals(umleitungsweg.getBetriebsstelleVon().getKuerzel())) {
					umleitungswegeSorted.add(umleitungsweg);
					start = umleitungsweg.getBetriebsstelleBis().getKuerzel();
				}
			}
		}
		return umleitungswegeSorted;
	}

	public void setUmleitungswege(List<Umleitungsweg> umleitungswege) {
		this.umleitungswege = umleitungswege;
	}

	/**
	 * @return the freieKapaRichtung
	 */
	public Double getFreieKapaRichtung() {
		return freieKapaRichtung;
	}

	/**
	 * @param freieKapaRichtung
	 *            the freieKapaRichtung to set
	 */
	public void setFreieKapaRichtung(Double freieKapaRichtung) {
		this.freieKapaRichtung = freieKapaRichtung;
	}

	/**
	 * @return the freieKapaGegenrichtung
	 */
	public Double getFreieKapaGegenrichtung() {
		return freieKapaGegenrichtung;
	}

	/**
	 * @param freieKapaGegenrichtung
	 *            the freieKapaGegenrichtung to set
	 */
	public void setFreieKapaGegenrichtung(Double freieKapaGegenrichtung) {
		this.freieKapaGegenrichtung = freieKapaGegenrichtung;
	}

	public String getBbpMassnahmeId() {
		return bbpMassnahmeId;
	}

	public void setBbpMassnahmeId(String bbpMassnahmeId) {
		this.bbpMassnahmeId = bbpMassnahmeId;
	}

	public Integer getBbpRglNr() {
		return bbpRglNr;
	}

	public void setBbpRglNr(Integer bbpRglNr) {
		this.bbpRglNr = bbpRglNr;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Transient
	public String getRelationString() {
		return relation;
	}

	public String getRelation() {
		if (relation != null && relation.length() > 0)
			return relation;
		// falls noch keine Relation gespeichert wurde -> Relation berechnen und speichern
		StringBuffer rel = new StringBuffer();
		if (umleitungswege.size() > 0) {
			Set<Umleitungsweg> umWege = new HashSet<Umleitungsweg>(umleitungswege);
			String start = null;
			String ziel = null;
			List<String> betriebsstellenVon = new ArrayList<String>();
			List<String> betriebsstellenBis = new ArrayList<String>();
			for (Umleitungsweg umleitungsweg : umleitungswege) {
				betriebsstellenVon.add(umleitungsweg.getBetriebsstelleVon().getKuerzel());
				betriebsstellenBis.add(umleitungsweg.getBetriebsstelleBis().getKuerzel());
			}
			List<String> betriebsstellenVonKopie = new ArrayList<String>(betriebsstellenVon);
			betriebsstellenVon.removeAll(betriebsstellenBis);
			if (betriebsstellenVon.size() == 1) {
				start = betriebsstellenVon.get(0);
				betriebsstellenBis.removeAll(betriebsstellenVonKopie);
				if (betriebsstellenBis.size() == 1)
					ziel = betriebsstellenBis.get(0);
			}

			// unvollstaendige Umleitung -> Abbruch
			if (start == null || ziel == null)
				return null;

			for (int i = 0; i < umWege.size(); i++) {
				for (Umleitungsweg umleitungsweg : umWege) {
					if (start.equals(umleitungsweg.getBetriebsstelleVon().getKuerzel())) {
						rel.append(start + "-");
						start = umleitungsweg.getBetriebsstelleBis().getKuerzel();
					}
				}
			}
			rel.append(ziel);
		}

		if (rel.length() > 0) {
			relation = rel.toString();
			EasyServiceFactory.getInstance().createUmleitungService().update(this);
			return relation;
		}
		return null;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
}