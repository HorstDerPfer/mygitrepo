package db.training.osb.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "umleitungsweg", uniqueConstraints = @UniqueConstraint(columnNames = {
        "vzgStrecke_ID", "betriebsstelleVon_ID", "betriebsstelleBis_ID" }))
public class Umleitungsweg extends EasyPersistentObjectVc implements Historizable {

	@ManyToOne(fetch = FetchType.EAGER)
	private VzgStrecke vzgStrecke;

	@ManyToOne(fetch = FetchType.EAGER)
	private Betriebsstelle betriebsstelleVon;

	@ManyToOne(fetch = FetchType.EAGER)
	private Betriebsstelle betriebsstelleBis;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "umleitungswege")
	private Set<Umleitung> umleitungen;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Integer bbpRglNr;

	public VzgStrecke getVzgStrecke() {
		return vzgStrecke;
	}

	public void setVzgStrecke(VzgStrecke vzgStrecke) {
		this.vzgStrecke = vzgStrecke;
	}

	public Betriebsstelle getBetriebsstelleVon() {
		return betriebsstelleVon;
	}

	public void setBetriebsstelleVon(Betriebsstelle betriebsstelleVon) {
		this.betriebsstelleVon = betriebsstelleVon;
	}

	public Betriebsstelle getBetriebsstelleBis() {
		return betriebsstelleBis;
	}

	public void setBetriebsstelleBis(Betriebsstelle betriebsstelleBis) {
		this.betriebsstelleBis = betriebsstelleBis;
	}

	public Set<Umleitung> getUmleitungen() {
		return umleitungen;
	}

	public void setUmleitungen(Set<Umleitung> umleitungen) {
		this.umleitungen = umleitungen;
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
}