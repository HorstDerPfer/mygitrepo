package db.training.bob.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;
import db.training.osb.model.babett.EvuKundentyp;

@SuppressWarnings("serial")
@Entity
@Table(name = "evu")
public class EVU extends EasyPersistentObject implements Comparable<EVU>, Historizable {

	@Column(name = "kundennr", unique = true)
	private String kundenNr;

	@Column(name = "name")
	private String name;

	@Column(name = "kurzbezeichnung")
	private String kurzbezeichnung;

	@CollectionOfElements()
	@Enumerated(EnumType.STRING)
	private Set<EvuKundentyp> typen;

	@ManyToOne()
	private EVUGruppe evugruppe;

	public String getKundenNr() {
		return kundenNr;
	}

	public void setKundenNr(String kundenNr) {
		this.kundenNr = kundenNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKurzbezeichnung() {
		return kurzbezeichnung;
	}

	public void setKurzbezeichnung(String kurzbezeichnung) {
		this.kurzbezeichnung = kurzbezeichnung;
	}

	public Set<EvuKundentyp> getTypen() {
		return typen;
	}

	public void setTypen(Set<EvuKundentyp> typen) {
		this.typen = typen;
	}

	public EVUGruppe getEvugruppe() {
		return evugruppe;
	}

	public void setEvugruppe(EVUGruppe evugruppe) {
		this.evugruppe = evugruppe;
	}

	public int compareTo(EVU o) {
		if (o == null)
			return -1;
		String thisName = this.getName();
		String oName = o.getName();
		if (thisName == null) {
			if (oName == null)
				return 0;
			return 1;
		}
		if (oName == null)
			return -1;
		return thisName.compareTo(oName);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		EVU evu = (EVU) obj;
		return (name != null && name.equals(evu.name))
		    && (kundenNr != null && kundenNr.equals(evu.kundenNr));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == name ? 0 : name.hashCode());
		return hash;
	}

	@Transient
	public String getCaption() {
		StringBuilder caption = new StringBuilder();
		if (kundenNr != null)
			caption.append("[").append(kundenNr).append("] ");

		if (evugruppe != null)
			caption.append(evugruppe.getName()).append(" - ");

		caption.append(name);
		return caption.toString();
	}
}