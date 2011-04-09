package db.training.bob.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "regionalbereich", uniqueConstraints = {})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Regionalbereich extends EasyPersistentObject implements Comparable<Regionalbereich>,
    Historizable {

	@Column(name = "name", unique = true, length = 20)
	private String name;

	@Transient
	private Set<Bearbeitungsbereich> bearbeitungsbereiche;

	@Column(name = "kuerzel", unique = true, length = 5)
	private String kuerzel;

	protected Regionalbereich() {
		super();
	}

	public Regionalbereich(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

	public Set<Bearbeitungsbereich> getBearbeitungsbereiche() {
		return bearbeitungsbereiche;
	}

	public void setBearbeitungsbereiche(Set<Bearbeitungsbereich> bearbeitungsbereiche) {
		this.bearbeitungsbereiche = bearbeitungsbereiche;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		Regionalbereich regionalbereich = (Regionalbereich) obj;
		return (name != null && name.equals(regionalbereich.name));
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public int compareTo(Regionalbereich o) {
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
		if (thisName.compareTo(oName) == 0)
			return -1;
		return thisName.compareTo(oName);
	}
}
