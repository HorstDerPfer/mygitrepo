package db.training.osb.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.hibernate.history.Historizable;

/**
 * gruppiert die Maßnahmen unter der Sicht 'Top-Maßnahme aus SAP'
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.4.1 Projekte
 */
@Entity
@Table(name = "topprojekt")
public class TopProjekt extends EasyPersistentObjectVc implements Historizable {

	private static final long serialVersionUID = -1119724089811635877L;

	@ManyToOne(optional = true)
	private Anmelder anmelder;

	@Column(name = "name")
	private String name;

	@Column(name = "baukosten", precision = 15, scale = 3)
	private BigDecimal baukosten;

	@Column(name = "sap_projektnummer")
	private String sapProjektNummer;

	@Column(name = "fahrplanjahr", nullable = false, length = 4)
	private Integer fahrplanjahr;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Regionalbereich regionalbereich;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<SAPMassnahme> massnahmen;

	private boolean deleted;

	public Anmelder getAnmelder() {
		return anmelder;
	}

	public void setAnmelder(Anmelder anmelder) {
		this.anmelder = anmelder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBaukosten() {
		return baukosten;
	}

	public void setBaukosten(BigDecimal baukosten) {
		this.baukosten = baukosten;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public String getSapProjektNummer() {
		return sapProjektNummer;
	}

	public void setSapProjektNummer(String sapProjektNummer) {
		this.sapProjektNummer = sapProjektNummer;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public Set<SAPMassnahme> getMassnahmen() {
		return massnahmen;
	}

	public void setMassnahmen(Set<SAPMassnahme> massnahmen) {
		this.massnahmen = massnahmen;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Transient
	public String getCaption() {
		StringBuilder caption = new StringBuilder();
		if (getSapProjektNummer() != null)
			caption.append(getSapProjektNummer());
		else if (getName() != null)
			caption.append(getName());
		else
			caption.append("ID" + getId());
		return caption.toString();
	}

}
