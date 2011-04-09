package db.training.osb.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "masterbuendel")
public class MasterBuendel extends EasyPersistentObjectVc implements Historizable {

	@ManyToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<Korridor> korridor = new HashSet<Korridor>();

	public Set<Korridor> getKorridor() {
		return korridor;
	}

	public void setKorridor(Set<Korridor> korridor) {
		this.korridor = korridor;
	}
}