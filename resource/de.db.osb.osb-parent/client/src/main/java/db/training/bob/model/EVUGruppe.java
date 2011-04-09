package db.training.bob.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "evugruppe")
public class EVUGruppe extends EasyPersistentObject implements Historizable {

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "evugruppe", fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	private List<EVU> evu;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EVU> getEvu() {
		return evu;
	}

	public void setEvu(List<EVU> evu) {
		this.evu = evu;
	}
}