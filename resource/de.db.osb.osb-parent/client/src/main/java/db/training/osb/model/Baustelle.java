package db.training.osb.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

/**
 * Containerklasse für Maßnahmenzustände
 * 
 * @author michels
 * 
 */
@Entity
@Table(name = "baustelle")
public class Baustelle extends EasyPersistentObject implements Historizable {

	private static final long serialVersionUID = -5005128407404580202L;

	@Column(name = "lfd_nr", updatable = false)
	private int lfdNr;

	@Column(name = "name")
	private String name;

	@Column(name = "fahrplanjahr", nullable = false, length = 4)
	private Integer fahrplanjahr;

	@ManyToMany(mappedBy = "baustellen")
	private Set<Gleissperrung> gleissperrungen;

	public void setLfdNr(Integer lfdnr) {
		this.lfdNr = lfdnr;
	}

	public int getLfdNr() {
		return lfdNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public Set<Gleissperrung> getGleissperrungen() {
		return gleissperrungen;
	}

	public void setGleissperrungen(Set<Gleissperrung> gleissperrungen) {
		this.gleissperrungen = gleissperrungen;
	}
}